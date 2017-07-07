package convention.services;

import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.temas.acciones.CrearMinuta;
import ar.com.kfgodel.temas.acciones.UsarMinutaExistente;
import ar.com.kfgodel.temas.filters.MinutaDeReunion;
import convention.persistent.Minuta;
import convention.persistent.Reunion;

import java.util.List;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaService extends Service<Minuta> {

    public MinutaService(){
        setClasePrincipal(Minuta.class);
    }
    public Minuta getFromReunion(Long id){
        ReunionService reunionService = appInjector.getImplementationFor(ReunionService.class).get();
        Reunion reunion = reunionService.get(id);
        return createOperation()
                .insideATransaction()
                .applying(MinutaDeReunion.create(id))
                .applyingResultOf((existente) ->
                        existente.mapOptional(UsarMinutaExistente::create)
                                .orElseGet(() -> CrearMinuta.create(reunion))
                        ).get();

    }
    public List<Minuta> getAll() {
        return getAll(FindAll.of(Minuta.class));
    }

}
