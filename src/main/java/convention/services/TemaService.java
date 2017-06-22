package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 21/06/17.
 */
public class TemaService {

    @Inject
    private DependencyInjector appInjector;

    private static final Type LISTA_DE_TEMAS_TO = new ReferenceOf<List<TemaTo>>() {}.getReferencedType();

    public List<TemaTo> getAll(){
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(FindAll.of(TemaDeReunion.class))
                .convertTo(LISTA_DE_TEMAS_TO);
    }

    public TemaTo save(TemaTo nuevoTema){
        return ApplicationOperation.createFor(appInjector)
                .insideATransaction()
                .taking(nuevoTema)
                .convertingTo(TemaDeReunion.class)
                .applyingResultOf(Save::create)
                .convertTo(TemaTo.class);
    }
}
