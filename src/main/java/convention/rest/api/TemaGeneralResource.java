package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaGeneral;
import convention.persistent.Usuario;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.TemaGeneralTo;
import convention.rest.api.tos.TemaTo;
import convention.services.TemaGeneralService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 04/07/17.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaGeneralResource extends Resource {

    @Inject
    private TemaGeneralService temaGeneralService;

    private static final Type LISTA_DE_TEMAS_GENERALES_TO = new ReferenceOf<List<TemaGeneralTo>>() {
    }.getReferencedType();

    @GET
    public List<TemaGeneralTo> getAll() {
        List<TemaGeneral> temasGenerales = temaGeneralService.getAll();
        return convertir(temasGenerales, LISTA_DE_TEMAS_GENERALES_TO);

    }

    @POST
    public TemaGeneralTo create(TemaGeneralTo newState,@Context SecurityContext securityContext) {
        TemaGeneral temaCreado =convertir(newState, TemaGeneral.class);
        Usuario modificador=this.usuarioActual(securityContext);
            temaCreado.setUltimoModificador(modificador);
                temaCreado=temaGeneralService.save(temaCreado);
        return convertir(temaCreado, TemaGeneralTo.class);
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        temaGeneralService.delete(id);
    }

    @PUT
    @Path("/{resourceId}")
    public TemaGeneralTo update(TemaGeneralTo newState, @PathParam("resourceId") Long id,@Context SecurityContext securityContext) {
        TemaGeneral temaActualizado =convertir(newState, TemaGeneral.class);
        Usuario modificador=this.usuarioActual(securityContext);
        temaActualizado.setUltimoModificador(modificador);
        temaActualizado=temaGeneralService.update(temaActualizado);
        return convertir(temaActualizado, TemaGeneralTo.class);
    }

    public static TemaGeneralResource create(DependencyInjector appInjector) {
        TemaGeneralResource resource = new TemaGeneralResource();
        resource.appInjector = appInjector;
        resource.temaGeneralService = appInjector.createInjected(TemaGeneralService.class);
        appInjector.bindTo(TemaGeneralService.class, resource.temaGeneralService);
        return resource;
    }

}
