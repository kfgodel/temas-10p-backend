package convention.services;

import convention.persistent.TemaGeneral;

/**
 * Created by sandro on 04/07/17.
 */
public class TemaGeneralService extends Service<TemaGeneral> {

    public TemaGeneralService(){
        setClasePrincipal(TemaGeneral.class);
    }
}
