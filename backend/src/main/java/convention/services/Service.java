package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import ar.com.kfgodel.orm.api.operations.basic.Delete;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.PersistableSupport;
import convention.persistent.Reunion;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/**
 * Created by fede on 28/06/17.
 */
public abstract class Service<T extends PersistableSupport> {

    @Inject
    protected DependencyInjector appInjector;

    protected Type LIST_TYPE = new ReferenceOf<List<T>>() {}.getReferencedType();
    protected Class<T> clasePrincipal;

    public List<T> getAll(SessionOperation<Nary<T>> sessionOperation) {
        return createOperation()
                .insideASession()
                .applying(sessionOperation)
                .convertTo(LIST_TYPE);
    }

    public Type getList_Type() {
        return LIST_TYPE;
    }

    protected ApplicationOperation createOperation() {
        return ApplicationOperation.createFor(appInjector);
    }


    public T save(T newObject) {
        return createOperation()
                .insideATransaction()
                .taking(newObject)
                .applyingResultOf(Save::create)
                .convertTo(clasePrincipal);
    }

    public T get(Long id) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(clasePrincipal, id))
                .mapping((encontrado) -> {
                    return controlDeTargetAndReturn(encontrado);

                })
                .convertTo(clasePrincipal);
    }

    public T getAndMapping(Long id, Function<T, T> mapping) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(clasePrincipal, id))
                .mapping((encontrado) -> {
                    T elem = controlDeTargetAndReturn(encontrado);
                    return mapping.apply(elem);
                })
                .convertTo(clasePrincipal);
    }

    public T updateAndMapping(Long id, Function<T, T> mapping) {
        return createOperation()
                .insideATransaction()
                .applying((context) -> FindById.create(clasePrincipal, id).applyWithSessionOn(context))
                .mapping((encontrado) -> {
                    T instancia = controlDeTargetAndReturn(encontrado);

                    return mapping.apply(instancia);
                }).applyingResultOf(Save::create)
                .get();
    }


    public T update(T newState) {
        return createOperation()
                .insideATransaction()
                .taking(newState)
                .convertingTo(clasePrincipal)
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    controlDeTarget(encontrado);
                    return encontrado;
                }).applyingResultOf(Save::create).get();
    }

    public void delete(Long id) {

        createOperation()
                .insideATransaction()
                .taking(id)
                .convertingTo(clasePrincipal)
                .mapping((encontrado) -> {
                    controlDeTarget(encontrado);
                    return encontrado;
                })
                .applyResultOf(Delete::create);
    }

    private void controlDeTarget(T encontrado) {
        if (encontrado == null) {
            throw new WebApplicationException("target not found", 404);
        }
    }

    private T controlDeTargetAndReturn(Nary<T> encontrado) {
        return encontrado.orElseThrow(() -> new WebApplicationException("target not found", 404));
    }

    protected void setClasePrincipal(Class<T> clasePrincipal) {
        this.clasePrincipal = clasePrincipal;
    }

    public void saveAll(List<T> newObjects) {
        newObjects.forEach(newObject -> this.save(newObject));
    }

    public void updateAll(List<T> newObjects) {
        newObjects.forEach(newObject -> this.update(newObject));
    }
}
