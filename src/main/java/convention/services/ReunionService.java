package convention.services;

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
import convention.persistent.Reunion;
import convention.rest.api.tos.ReunionTo;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/**
 * Created by sandro on 21/06/17.
 */
public class ReunionService {

    @Inject
    private DependencyInjector appInjector;

    private static final Type LISTA_DE_REUNIONES = new ReferenceOf<List<Reunion>>() {}.getReferencedType();

    public Reunion getProxima() {
        return createOperation()
                .insideATransaction()
                .applying(ProximaReunion.create())
                .applyingResultOf((existente) ->
                        existente.mapOptional(UsarExistente::create)
                                .orElseGet(CrearProximaReunion::create)).get();
    }

    public List<Reunion> getAll(){
        return createOperation()
                .insideASession()
                .applying(AllReunionesUltimaPrimero.create())
                .convertTo(LISTA_DE_REUNIONES);
    }

    private ApplicationOperation createOperation() {
        return ApplicationOperation.createFor(appInjector);
    }


    public Reunion save(Reunion nuevaReunion){
        return createOperation()
                .insideATransaction()
                .taking(nuevaReunion)
                .applyingResultOf(Save::create)
                .convertTo(Reunion.class);
    }
    public Reunion get(Long id) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(Reunion.class, id))
                .mapping((encontrado) -> {
                    return encontrado.orElseThrowRuntime(() -> new WebApplicationException("reunion not found", 404));

                })
                .convertTo(Reunion.class);
    }

    public Reunion get(Long id,Function<Reunion,Reunion> mapping) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(Reunion.class, id))
                .mapping((encontrado) -> {
                    Reunion reunion= encontrado.orElseThrowRuntime(() -> new WebApplicationException("reunion not found", 404));
                return mapping.apply(reunion);
                })
                .convertTo(Reunion.class);
    }

    public Reunion update(Long id, Function<Reunion,Reunion> mapping){
        return createOperation()
                .insideATransaction()
                .applying((context) -> FindById.create(Reunion.class, id).applyWithSessionOn(context))
                .mapping((encontrado) -> {
                    Reunion reunion = encontrado.orElseThrow(() -> new WebApplicationException("reunion not found", 404));

                    return mapping.apply(reunion);
                }).applyingResultOf(Save::create)
                    .get();
    }
    public Reunion update(Reunion newState){
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
                }).applyingResultOf(Save::create).get();
    }
    public void delete( Long id) {
        createOperation()
                .insideATransaction()
                .taking(id)
                .convertingTo(Reunion.class)
                .mapping((encontrado) -> {
                    if (encontrado == null) {
                        throw new WebApplicationException("reunion not found", 404);
                    }
                    return encontrado;
                })
                .applyResultOf(Delete::create);
    }

}
