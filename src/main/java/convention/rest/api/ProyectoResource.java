package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.ndp.filters.elementos.ElementosDisponibles;
import ar.com.kfgodel.ndp.filters.proyectos.FindAllProyectosNewestFirst;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.EstadoDeProyecto;
import convention.persistent.Proyecto;
import convention.rest.api.tos.ElementoEnComboTo;
import convention.rest.api.tos.EstadoEnComboTo;
import convention.rest.api.tos.ProyectoTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class ProyectoResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LIST_OF_PROYECTOS_TOS = new ReferenceOf<List<ProyectoTo>>() {
  }.getReferencedType();
  private static final Type LIST_OF_ESTADOS_TOS = new ReferenceOf<List<EstadoEnComboTo>>() {
  }.getReferencedType();
  private static final Type LIST_OF_ELEMENTOS_TOS = new ReferenceOf<List<ElementoEnComboTo>>() {
  }.getReferencedType();

  @GET
  public List<ProyectoTo> getAll() {
    return createOperation()
      .insideASession()
      .applying(FindAllProyectosNewestFirst.create())
      .convertTo(LIST_OF_PROYECTOS_TOS);
  }

  @GET
  @Path("/estados")
  public List<EstadoEnComboTo> getAllEstados() {
    return createOperation()
      .insideASession()
      .applying((session) -> EstadoDeProyecto.getAll())
      .convertTo(LIST_OF_ESTADOS_TOS);
  }

  @GET
  @Path("/elementos")
  public List<ElementoEnComboTo> getAllElementos() {
    return createOperation()
      .insideASession()
      .applying(ElementosDisponibles.create())
      .convertTo(LIST_OF_ELEMENTOS_TOS);
  }


  @POST
  public ProyectoTo create(ProyectoTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Proyecto.class)
      .applyingResultOf(Save::create)
      .convertTo(ProyectoTo.class);
  }

  @GET
  @Path("/{resourceId}")
  public ProyectoTo getSingle(@PathParam("resourceId") Long id) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Proyecto.class, id))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("proyecto not found", 404));
      })
      .convertTo(ProyectoTo.class);
  }


  @PUT
  @Path("/{resourceId}")
  public ProyectoTo update(ProyectoTo newState, @PathParam("resourceId") Long id) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Proyecto.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("proyecto not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(ProyectoTo.class);
  }

  @DELETE
  @Path("/{resourceId}")
  public void delete(@PathParam("resourceId") Long id) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Proyecto.class, id));
  }

  public static ProyectoResource create(DependencyInjector appInjector) {
    ProyectoResource resource = new ProyectoResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
