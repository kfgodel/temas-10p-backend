package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.sun.org.apache.regexp.internal.RE;
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
public class TemaDeMinutaResource{

        @Inject
    TemaDeMinutaService temaDeMinutaService;

        private ResourceHelper resourceHelper;
    @GET
    @Path("/{resourceId}")
    public TemaDeMinutaTo getSingle(@PathParam("resourceId") Long id) {
            TemaDeMinuta temaDeMinuta=temaDeMinutaService.get(id);
        return  getResourceHelper().convertir(temaDeMinuta, TemaDeMinutaTo.class);
    }
    @PUT
    @Path("/{resourceId}")
    public TemaDeMinutaTo update(TemaDeMinutaTo newState, @PathParam("resourceId") Long id) {
        TemaDeMinuta temaDeMinutaActualizada = temaDeMinutaService.update( getResourceHelper().convertir(newState, TemaDeMinuta.class));
        return  getResourceHelper().convertir(temaDeMinutaActualizada, TemaDeMinutaTo.class);
    }
    public static TemaDeMinutaResource create(DependencyInjector appInjector) {
        TemaDeMinutaResource TemaDeMinutaResource = new TemaDeMinutaResource();
                    TemaDeMinutaResource.resourceHelper= ResourceHelper.create(appInjector);
        TemaDeMinutaResource. getResourceHelper().bindAppInjectorTo(TemaDeMinutaResource.class, TemaDeMinutaResource);
        return TemaDeMinutaResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}
