package convention.services;

import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import convention.persistent.TemaGeneral;

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
}
