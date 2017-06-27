package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 21/06/17.
 */
public class TemaService {

    @Inject
    private DependencyInjector appInjector;

    private static final Type LISTA_DE_TEMAS = new ReferenceOf<List<TemaDeReunion>>() {}.getReferencedType();

    public List<TemaDeReunion> getAll(){
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(FindAll.of(TemaDeReunion.class))
                .convertTo(LISTA_DE_TEMAS);
    }

    public TemaDeReunion save(TemaDeReunion nuevoTema){
        return ApplicationOperation.createFor(appInjector)
                .insideATransaction()
                .taking(nuevoTema)
                .convertingTo(TemaDeReunion.class)
                .applyingResultOf(Save::create)
                .convertTo(TemaDeReunion.class);
    }

    public TemaDeReunion getSingle(Long id){
        return ApplicationOperation.createFor(appInjector)
            .insideASession()
            .applying(FindById.create(TemaDeReunion.class, id))
            .mapping((encontrado) -> {
                // Answer 404 if missing
                return encontrado.orElseThrowRuntime(() -> new WebApplicationException("tema not found", 404));
            })
            .convertTo(TemaDeReunion.class);
    }
}
