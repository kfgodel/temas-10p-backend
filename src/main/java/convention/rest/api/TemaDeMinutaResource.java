package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.persistent.Reunion;
import convention.persistent.Tema;
import convention.persistent.TemaDeMinuta;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaDeMinutaTo;
import convention.services.TemaDeMinutaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fede on 07/07/17.
 */

@Produces("application/json")
@Consumes("application/json")
public class TemaDeMinutaResource extends Resource{

        @Inject
    TemaDeMinutaService temaDeMinutaService;

    @GET
    @Path("/{resourceId}")
    public TemaDeMinutaTo getSingle(@PathParam("resourceId") Long id) {
            TemaDeMinuta temaDeMinuta=temaDeMinutaService.get(id);
        return convertir(temaDeMinuta, TemaDeMinutaTo.class);
    }
    @PUT
    @Path("/{resourceId}")
    public TemaDeMinutaTo update(TemaDeMinutaTo newState, @PathParam("resourceId") Long id) {
        TemaDeMinuta temaDeMinutaActualizada = temaDeMinutaService.update(convertir(newState, TemaDeMinuta.class));
        return convertir(temaDeMinutaActualizada, TemaDeMinutaTo.class);
    }
    public static TemaDeMinutaResource create(DependencyInjector appInjector) {
        TemaDeMinutaResource resource = new TemaDeMinutaResource();
        resource.appInjector = appInjector;
        resource.appInjector.bindTo(TemaDeMinutaResource.class, resource);
        return resource;
    }
}
