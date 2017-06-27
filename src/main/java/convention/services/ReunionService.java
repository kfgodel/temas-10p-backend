package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.Reunion;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 21/06/17.
 */
public class ReunionService {

    @Inject
    private DependencyInjector appInjector;

    private static final Type LISTA_DE_REUNIONES = new ReferenceOf<List<Reunion>>() {}.getReferencedType();

    public List<Reunion> getAll(){
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(AllReunionesUltimaPrimero.create())
                .convertTo(LISTA_DE_REUNIONES);
    }


    public Reunion save(Reunion nuevaReunion){
        return ApplicationOperation.createFor(appInjector)
                .insideATransaction()
                .taking(nuevaReunion)
//                .convertingTo(Reunion.class)
                .applyingResultOf(Save::create)
                .convertTo(Reunion.class);
    }

    public Reunion get(Long id) {
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(FindById.create(Reunion.class, id))
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    return encontrado.orElseThrowRuntime(() -> new WebApplicationException("reunion not found", 404));
                })
                .convertTo(Reunion.class);
    }

    public Reunion update(Reunion reunion){ //, Long id) {
        return ApplicationOperation.createFor(appInjector)
                .insideATransaction()
                .taking(reunion)
                .convertingTo(Reunion.class)
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    if (encontrado == null) {
                        throw new WebApplicationException("reunion not found", 404);
                    }
                    return encontrado;
                }).applyingResultOf(Save::create)
                .convertTo(Reunion.class);
    }
}
