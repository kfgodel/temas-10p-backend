package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.MinutaTo;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.UserTo;
import convention.services.ReunionService;
import convention.services.UsuarioService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class ReunionResource extends Resource {

    @Inject
    private ReunionService reunionService;

    private static final Type LISTA_DE_REUNIONES_TO = new ReferenceOf<List<ReunionTo>>() {
    }.getReferencedType();


    public Reunion muestreoDeReunion(Reunion reunion, Long userId, SecurityContext securityContext) {
        Reunion nuevaReunion = reunion.copy();

        if (reunion.getStatus() == StatusDeReunion.PENDIENTE) {
            List<TemaDeReunion> listaDeTemasNuevos = reunion.getTemasPropuestos().stream().
                    map(temaDeReunion ->
                            temaDeReunion.copy()).collect(Collectors.toList());
            listaDeTemasNuevos.forEach(temaDeReunion -> temaDeReunion.ocultarVotosPara(userId));
            Collections.shuffle(listaDeTemasNuevos, new Random(securityContext.getUserPrincipal().hashCode())); //random turbio
            nuevaReunion.setTemasPropuestos(listaDeTemasNuevos);
        }
        return nuevaReunion;
    }

    @GET
    @Path("proxima")
    public ReunionTo getProxima(@Context SecurityContext securityContext) {
        Reunion proxima = reunionService.getProxima();
        return getSingle(proxima.getId(), securityContext);
    }

    @GET
    @Path("cerrar/{resourceId}")
    public ReunionTo cerrar(@PathParam("resourceId") Long id) {
        Reunion reunionCerrada = reunionService.updateAndMapping(id,
                reunion -> {
                    reunion.cerrarVotacion();
                    return reunion;
                });
        return convertir(reunionCerrada, ReunionTo.class);
    }


    @GET
    @Path("reabrir/{resourceId}")
    public ReunionTo reabrir(@PathParam("resourceId") Long id) {
        Reunion reunionAbierta = reunionService.updateAndMapping(id,
                reunion -> {
                    reunion.reabrirVotacion();
                    return reunion;
                });
        return convertir(reunionAbierta, ReunionTo.class);
    }

    @GET
    public List<ReunionTo> getAll(@Context SecurityContext securityContext) {
        Long userId = idDeUsuarioActual(securityContext);
        List<Reunion> reuniones = reunionService.getAll();
        List<Reunion> reunionesFiltradas = reuniones.stream()
                .map(reunion -> muestreoDeReunion(reunion, userId, securityContext)).collect(Collectors.toList());
        return convertir(reunionesFiltradas, LISTA_DE_REUNIONES_TO);
    }

    @POST
    public ReunionTo create(ReunionTo reunionNueva) {

        Reunion reunionCreada = reunionService.save(convertir(reunionNueva, Reunion.class));
        return convertir(reunionCreada, ReunionTo.class);
    }

    @GET
    @Path("/{resourceId}")
    public ReunionTo getSingle(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
        Long userId = idDeUsuarioActual(securityContext);
        Reunion reunionFiltrada = reunionService.getAndMapping(id, reunion -> muestreoDeReunion(reunion, userId, securityContext));
        return convertir(reunionFiltrada, ReunionTo.class);
    }


    @PUT
    @Path("/{resourceId}")
    public ReunionTo update(ReunionTo newState, @PathParam("resourceId") Long id) {
        Reunion reunionActualizada = reunionService.update(convertir(newState, Reunion.class));
        return convertir(reunionActualizada, ReunionTo.class);
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        reunionService.delete(id);
    }

    public static ReunionResource create(DependencyInjector appInjector) {
        ReunionResource resource = new ReunionResource();
        resource.appInjector = appInjector;
        resource.reunionService = appInjector.createInjected(ReunionService.class);
        resource.appInjector.bindTo(ReunionService.class, resource.reunionService);
        return resource;
    }


}
