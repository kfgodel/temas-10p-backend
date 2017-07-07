package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
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
public class TemaService extends Service<TemaDeReunion> {

    public List<TemaDeReunion> getAll() {
        return getAll(FindAll.of(TemaDeReunion.class));
    }
    public TemaService(){
        setClasePrincipal(TemaDeReunion.class);
    }

    @Override
    public void delete(Long id) {
        createOperation()
                .insideATransaction()
                .apply(DeleteById.create(TemaDeReunion.class, id));
    }

}
