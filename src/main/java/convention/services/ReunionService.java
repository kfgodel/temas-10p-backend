package convention.services;

import ar.com.kfgodel.temas.acciones.CrearProximaReunion;
import ar.com.kfgodel.temas.acciones.UsarExistente;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import ar.com.kfgodel.temas.filters.reuniones.ProximaReunion;
import convention.persistent.Reunion;

import java.util.List;


/**
 * Created by sandro on 21/06/17.
 */
public class ReunionService extends Service<Reunion> {

    public Reunion getProxima() {
        return createOperation()
                .insideATransaction()
                .applying(ProximaReunion.create())
                .applyingResultOf((existente) ->
                        existente.mapOptional(UsarExistente::create)
                                .orElseGet(CrearProximaReunion::create)).get();
    }

    public ReunionService() {
        setClasePrincipal(Reunion.class);
    }

    public List<Reunion> getAll() {
        return getAll(AllReunionesUltimaPrimero.create());
    }
}
