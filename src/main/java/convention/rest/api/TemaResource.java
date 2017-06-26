package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Esta clase representa el recurso rest para modificar temas
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LISTA_DE_TEMAS_TO = new ReferenceOf<List<TemaTo>>() {
  }.getReferencedType();

  // Esto no debería funcionar, así que no se debe estar usando en ningún lado
  @GET
  public List<ReunionTo> getAll() {
    return createOperation()
      .insideASession()
      .applying(FindAll.of(TemaDeReunion.class))
      .convertTo(LISTA_DE_TEMAS_TO);
  }

  @POST
  public TemaTo create(TemaEnCreacionTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(TemaDeReunion.class)
      .applyingResultOf(Save::create)
      .convertTo(TemaTo.class);
  }

  @GET
  @Path("/{resourceId}")
  public TemaTo getSingle(@PathParam("resourceId") Long id) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(TemaDeReunion.class, id))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("tema not found", 404));
      })
      .convertTo(TemaTo.class);
  }

  @PUT
  @Path("/{resourceId}")
  public TemaTo update(TemaTo newState, @PathParam("resourceId") Long id) {
    TemaTo temaAActualizar=getSingle(id);
      temaAActualizar.agregarIdDeInteresado(newState.getIdsDeInteresados().get(0));
    return createOperation()
      .insideATransaction()
      .taking(temaAActualizar)
      .convertingTo(TemaDeReunion.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("tema not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(TemaTo.class);
  }

  @DELETE
  @Path("/{resourceId}")
  public void delete(@PathParam("resourceId") Long id) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(TemaDeReunion.class, id));
  }

  public static TemaResource create(DependencyInjector appInjector) {
    TemaResource resource = new TemaResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
