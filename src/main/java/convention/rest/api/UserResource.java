package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.users.FindAllUsersOrderedByName;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class UserResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LIST_OF_USER_TOS = new ReferenceOf<List<UserTo>>() {
  }.getReferencedType();

  @GET
  @Path("current")
  public UserTo getCurrent(@Context SecurityContext securityContext) {
    JettyIdentityAdapter principal = (JettyIdentityAdapter) securityContext.getUserPrincipal();
    Long currentUserId = principal.getApplicationIdentification();

    return createOperation()
      .insideASession()
      .applying(FindById.create(Usuario.class, currentUserId))
      .mapping((encontrado) -> encontrado.orElse(null))
      .convertTo(UserTo.class);
  }

  @GET
  public List<UserTo> getAllUsers() {
    return createOperation()
      .insideASession()
      .applying(FindAllUsersOrderedByName.create())
      .convertTo(LIST_OF_USER_TOS);
  }

  @GET
  @Path("/{userId}")
  public UserTo getSingleUser(@PathParam("userId") Long userId) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Usuario.class, userId))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("user not found", 404));
      })
      .convertTo(UserTo.class);
  }


  @PUT
  @Path("/{userId}")
  public UserTo updateUser(UserTo newUserState, @PathParam("userId") Long userId) {
    return createOperation()
      .insideATransaction()
      .taking(newUserState)
      .convertingTo(Usuario.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("user not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(UserTo.class);
  }

  @DELETE
  @Path("/{userId}")
  public void deleteUser(@PathParam("userId") Long userId) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Usuario.class, userId));
  }

  public static UserResource create(DependencyInjector appInjector) {
    UserResource resource = new UserResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
