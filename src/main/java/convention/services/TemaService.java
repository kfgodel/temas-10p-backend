package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaGeneral;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public void deleteAllForTemaGeneral(Long temaGeneralId) {
        List<TemaDeReunion> todosLosTemas = this.getAll();
        Stream<TemaDeReunion> temasABorrar = todosLosTemas.stream().
                filter(tema -> this.esUnTemaGeneradoPorElTemaGeneral(tema, temaGeneralId));
        temasABorrar.forEach(tema -> this.deleteSiReunionPendiente(tema));
    }

    private Boolean esUnTemaGeneradoPorElTemaGeneral(TemaDeReunion tema, Long temaGeneralId){
        return tema.getTemaGenerador().isPresent() &&
                temaGeneralId.equals(tema.getTemaGenerador().get().getId());
    }

    private void deleteSiReunionPendiente(TemaDeReunion tema) {
        if(tema.getReunion().getStatus().equals(StatusDeReunion.PENDIENTE)){
            this.delete(tema.getId());
        }else{
            tema.setTemaGenerador(null);
            this.update(tema);
        }
    }
}
