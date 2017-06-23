package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.Delete;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.acciones.CrearProximaReunion;
import ar.com.kfgodel.temas.acciones.UsarExistente;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import ar.com.kfgodel.temas.filters.reuniones.ProximaReunion;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.ReunionTo;

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
public class ReunionResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LISTA_DE_REUNIONES_TO = new ReferenceOf<List<ReunionTo>>() {
  }.getReferencedType();

    private Reunion filtrarVotosDeReunionPendiente(Reunion reunion, Long userId){
        if(reunion.getStatus()== StatusDeReunion.PENDIENTE) {
            Reunion nuevaReunion = reunion.copy();
            List<TemaDeReunion> listaDeTemasNuevos = reunion.getTemasPropuestos().stream().
                    map(temaDeReunion -> temaDeReunion.copy()).collect(Collectors.toList());
            listaDeTemasNuevos.forEach(temaDeReunion ->
                    temaDeReunion.setInteresados(temaDeReunion.getInteresados().stream().
                            filter(usuario -> usuario.getId().equals(userId)).collect(Collectors.toList())));
            nuevaReunion.setTemasPropuestos(listaDeTemasNuevos);
            return nuevaReunion;
        }
        else{
            return reunion;
        }
    }
  @GET
  @Path("proxima")
  public ReunionTo getProxima() {
    return createOperation()
      .insideATransaction()
      .applying(ProximaReunion.create())
      .applyingResultOf((existente) ->
        existente.mapOptional(UsarExistente::create)
          .orElseGet(CrearProximaReunion::create))
      .convertTo(ReunionTo.class);
  }


  @GET
  @Path("cerrar/{resourceId}")
  public ReunionTo cerrar(@PathParam("resourceId") Long id) {
    return createOperation()
      .insideATransaction()
      .applying((context) -> FindById.create(Reunion.class, id).applyWithSessionOn(context))
      .mapping((encontrado) -> {
        Reunion reunion = encontrado.orElseThrow(() -> new WebApplicationException("reunion not found", 404));
        reunion.cerrarVotacion();
        return reunion;
      }).applyingResultOf(Save::create)
      .convertTo(ReunionTo.class);
  }

  @GET
  @Path("reabrir/{resourceId}")
  public ReunionTo reabrir(@PathParam("resourceId") Long id) {
    return createOperation()
      .insideATransaction()
      .applying((context) -> FindById.create(Reunion.class, id).applyWithSessionOn(context))
      .mapping((encontrado) -> {
        Reunion reunion = encontrado.orElseThrow(() -> new WebApplicationException("reunion not found", 404));
        reunion.reabrirVotacion();
        return reunion;
      }).applyingResultOf(Save::create)
      .convertTo(ReunionTo.class);
  }

  @GET
  public List<ReunionTo> getAll(@Context SecurityContext securityContext) {
      Long userId = ((JettyIdentityAdapter) securityContext.getUserPrincipal()).getApplicationIdentification();
      return createOperation()
      .insideASession()
      .applying(AllReunionesUltimaPrimero.create())
       .mapping(reuniones ->
        reuniones.map(reunion -> filtrarVotosDeReunionPendiente(reunion,userId)).collect(Collectors.toList())
       )
      .convertTo(LISTA_DE_REUNIONES_TO);
  }

  @POST
  public ReunionTo create(ReunionTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Reunion.class)
      .applyingResultOf(Save::create)
      .convertTo(ReunionTo.class);
  }

  @GET
  @Path("/{resourceId}")
  public ReunionTo getSingle(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
            //esta identificacion podria caer en el userResource
      Long userId = ((JettyIdentityAdapter) securityContext.getUserPrincipal()).getApplicationIdentification();
    return createOperation()
      .insideASession()
      .applying(FindById.create(Reunion.class, id))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        Reunion reunion= encontrado.orElseThrowRuntime(() -> new WebApplicationException("reunion not found", 404));
        return filtrarVotosDeReunionPendiente(reunion,userId);
    })
      .convertTo(ReunionTo.class);
  }


  @PUT
  @Path("/{resourceId}")
  public ReunionTo update(ReunionTo newState, @PathParam("resourceId") Long id) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Reunion.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("reunion not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(ReunionTo.class);
  }

  @DELETE
  @Path("/{resourceId}")
  public void delete(@PathParam("resourceId") Long id) {
    createOperation()
      .insideATransaction()
      .taking(id)
      .convertingTo(Reunion.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("reunion not found", 404);
        }
        return encontrado;
      })
      .applyResultOf(Delete::create);
  }

  public static ReunionResource create(DependencyInjector appInjector) {
    ReunionResource resource = new ReunionResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
