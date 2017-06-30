package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.TemaTo;
import convention.services.TemaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Esta clase representa el recurso rest para modificar temas
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaResource extends Resource {

    @Inject
    TemaService temaService;



  @POST
  public TemaTo create(TemaEnCreacionTo newState) {
        TemaDeReunion temaCreado=temaService.save(convertir(newState,TemaEnCreacionTo.class,TemaDeReunion.class));
    return convertir(temaCreado,TemaDeReunion.class,TemaTo.class);
  }

  @GET
  @Path("/{resourceId}")
  public TemaTo getSingle(@PathParam("resourceId") Long id) {
    return convertir(temaService.get(id),TemaDeReunion.class,TemaTo.class);
  }

  @GET
  @Path("votar/{resourceId}")
  public TemaTo votar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

    Usuario usuarioActual=usuarioActual(securityContext);

    TemaDeReunion temaVotado= temaService.updateAndMapping(id,
            temaDeReunion -> votarTema(usuarioActual, temaDeReunion));
          return convertir(temaVotado,TemaDeReunion.class,TemaTo.class);
  }

    public TemaDeReunion votarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {
        long cantidadDeVotos=temaDeReunion.getInteresados().stream()
                .filter(usuario ->
                        usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos>=3) {
            throw new WebApplicationException("excede la cantidad de votos permitidos", 409);
        }
        temaDeReunion.agregarInteresado(usuarioActual);
        return temaDeReunion;
    }

    @GET
  @Path("desvotar/{resourceId}")
  public TemaTo desvotar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

      Usuario usuarioActual=usuarioActual(securityContext);

      TemaDeReunion temaVotado= temaService.updateAndMapping(id,
              temaDeReunion -> desvotarTema(usuarioActual, temaDeReunion)
              );
      return convertir(temaVotado,TemaDeReunion.class,TemaTo.class);

  }

    public TemaDeReunion desvotarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {
        long cantidadDeVotos=temaDeReunion.getInteresados().stream()
                .filter(usuario ->
                        usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos<=0) {
            throw new WebApplicationException("el usuario no tiene votos en el tema", 409);
        }
        temaDeReunion.quitarInteresado(usuarioActual);
        return temaDeReunion;
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        temaService.delete(id);
    }

  public static TemaResource create(DependencyInjector appInjector) {
    TemaResource resource = new TemaResource();
    resource.appInjector = appInjector;
      resource.temaService = resource.appInjector.createInjected(TemaService.class);

      return resource;
  }

}
