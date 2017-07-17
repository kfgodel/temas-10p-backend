package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.google.inject.Inject;
import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.MinutaTo;
import convention.services.MinutaService;
import convention.services.ReunionService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by sandro on 07/07/17.
 */
@Produces("application/json")
@Consumes("application/json")
public class MinutaResource extends Resource {

    @Inject
    private MinutaService minutaService;
    @Inject
    private ReunionService reunionService;
    @GET
    @Path("reunion/{reunionId}")
    public MinutaTo getParaReunion(@PathParam("reunionId") Long id ){
         Minuta minuta = minutaService.getFromReunion(id);
        minutaService.update(minuta);
        return convertir(minuta, MinutaTo.class);
    }

    @PUT
    @Path("/{resourceId}")
    public MinutaTo update(MinutaTo newState, @PathParam("resourceId") Long id, @Context SecurityContext securityContext){
        Usuario ultimoMinuteador = this.usuarioActual(securityContext);
        Minuta minuta=convertir(newState, Minuta.class);
        minuta.setMinuteador(ultimoMinuteador);
        Minuta minutaActualizada = minutaService.update(minuta);
        return convertir(minutaActualizada, MinutaTo.class);
    }

    public static MinutaResource create(DependencyInjector appInjector) {
        MinutaResource resource = new MinutaResource();
        resource.appInjector = appInjector;
        resource.appInjector.bindTo(MinutaResource.class, resource);
        resource.appInjector.bindTo(MinutaService.class,resource.minutaService);
        return resource;
    }
}
