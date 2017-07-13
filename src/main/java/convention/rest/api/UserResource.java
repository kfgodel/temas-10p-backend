package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.users.FindAllUsersOrderedByName;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Reunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;
import convention.services.ReunionService;
import convention.services.TemaService;
import convention.services.UsuarioService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class UserResource extends Resource {


    @Inject
    UsuarioService userService;

    @Inject
    ReunionService reunionService;

    private static final Type LIST_OF_USER_TOS = new ReferenceOf<List<UserTo>>() {
    }.getReferencedType();

    @GET
    @Path("current")
    public UserTo getCurrent(@Context SecurityContext securityContext) {
        Long currentUserId = idDeUsuarioActual(securityContext);
        return convertir(userService.get(currentUserId), UserTo.class);
    }

    @GET
    @Path("noVotaron/{reunionId}")
    public List<UserTo> getUsersQueNoVotaron(@PathParam("reunionId") Long reunionId)
    {

                List<Usuario> usuarios=userService.getAll();
        List<Usuario> votantes=reunionService.get(reunionId).usuariosQueVotaron();
        usuarios=usuarios.stream().filter(usuario ->
                !votantes.stream().anyMatch(votante -> votante.getId().equals(usuario.getId()) )).collect(Collectors.toList());
        return convertir(usuarios, LIST_OF_USER_TOS);
    }


    @GET
    public List<UserTo> getAllUsers() {

        return convertir(userService.getAll(), LIST_OF_USER_TOS);
    }

    @GET
    @Path("/{userId}")
    public UserTo getSingleUser(@PathParam("userId") Long userId) {
        return convertir(userService.get(userId), UserTo.class);
    }


    @PUT
    @Path("/{userId}")
    public UserTo updateUser(UserTo newUserState, @PathParam("userId") Long userId) {
        Usuario usuarioUpdateado = userService.update(convertir(newUserState, Usuario.class));
        return convertir(usuarioUpdateado, UserTo.class);
    }

    @DELETE
    @Path("/{userId}")
    public void deleteUser(@PathParam("userId") Long userId) {
        userService.delete(userId);
    }

    public static UserResource create(DependencyInjector appInjector) {
        UserResource resource = new UserResource();
        resource.appInjector = appInjector;
        resource.userService = resource.appInjector.createInjected(UsuarioService.class);
        resource.appInjector.bindTo(UsuarioService.class,resource.userService);
        return resource;
    }

}
