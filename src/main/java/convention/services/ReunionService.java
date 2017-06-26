package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.Reunion;
import convention.rest.api.tos.ReunionTo;

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

    private static final Type LISTA_DE_REUNIONES_TO = new ReferenceOf<List<ReunionTo>>() {}.getReferencedType();

    public List<ReunionTo> getAll(){
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(AllReunionesUltimaPrimero.create())
                .convertTo(LISTA_DE_REUNIONES_TO);
    }

    public ReunionTo save(ReunionTo nuevaReunion){

        return ApplicationOperation.createFor(appInjector)
                .insideATransaction()
                .taking(nuevaReunion)
                .convertingTo(Reunion.class)
                .applyingResultOf(Save::create)
                .convertTo(ReunionTo.class);
    }

    public ReunionTo get(Long id) {
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(FindById.create(Reunion.class, id))
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    return encontrado.orElseThrowRuntime(() -> new WebApplicationException("reunion not found", 404));
                })
                .convertTo(ReunionTo.class);
    }
}
