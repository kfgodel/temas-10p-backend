package convention.rest.api;

import com.google.inject.Inject;
import convention.persistent.Minuta;
import convention.rest.api.tos.MinutaTo;
import convention.services.MinutaService;
import convention.services.ReunionService;

import javax.ws.rs.*;

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
    @Path("/{resourceId}")
    public MinutaTo getSingle(@PathParam("resourceId") Long id) {
        return convertir(minutaService.get(id), MinutaTo.class);
    }

    @GET
    @Path("reunion/{reunionId}")
    public MinutaTo getParaReunion(@PathParam("reunionId") Long id){
        Minuta minuta = minutaService.getFromReunion(id);
        return convertir(minuta, MinutaTo.class);
    }

    @PUT
    @Path("/{resourceId}")
    public MinutaTo update(MinutaTo newState, @PathParam("resourceId") Long id){
        Minuta minutaActualizada = minutaService.update(convertir(newState, Minuta.class));
        return convertir(minutaActualizada, MinutaTo.class);
    }

}
