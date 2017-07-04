package convention.services;

import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.temas.filters.reuniones.ReunionesAbiertas;
import convention.persistent.Reunion;
import convention.persistent.TemaGeneral;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sandro on 04/07/17.
 */
public class TemaGeneralService extends Service<TemaGeneral> {

    public TemaGeneralService(){
        setClasePrincipal(TemaGeneral.class);
    }

    public List<TemaGeneral> getAll() {
        return getAll(FindAll.of(TemaGeneral.class));
    }

    @Override
    public TemaGeneral save(TemaGeneral temaGeneral){
        ReunionService reunionService = appInjector.getImplementationFor(ReunionService.class).get();
        List<Reunion> reunionesAbiertas = reunionService.getAll(ReunionesAbiertas.create());
        reunionesAbiertas.forEach(reunion -> reunion.agregarTemaGeneral(temaGeneral));
        reunionService.saveAll(reunionesAbiertas);
        return super.save(temaGeneral);
    }
}
