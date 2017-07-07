package convention.services;

import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import convention.persistent.Minuta;

import java.util.List;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaService extends Service<Minuta> {

    public MinutaService(){
        setClasePrincipal(Minuta.class);
    }

    public List<Minuta> getAll() {
        return getAll(FindAll.of(Minuta.class));
    }

}
