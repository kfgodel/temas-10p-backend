package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Reunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.ReunionTo;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;

/**
 * Created by fede on 27/06/17.
 */
public abstract class Resource {


    @Inject
    protected DependencyInjector appInjector;

    protected Long idDeUsuarioActual(SecurityContext securityContext){
        return ((JettyIdentityAdapter) securityContext.getUserPrincipal()).getApplicationIdentification();

    }

    protected ApplicationOperation createOperation() {

        return ApplicationOperation.createFor(appInjector);
    }

    protected Usuario usuarioActual(SecurityContext securityContext){
        Long userId=idDeUsuarioActual(securityContext);
        return createOperation()
                .insideASession()
                .applying(FindById.create(Usuario.class, userId))
                .mapping((encontrado) -> encontrado.orElse(null)).get();
    }

    protected <T> T conversor(Object objetoAConvertir,Class claseDesde, Class<T> claseHacia){
        return (T) createOperation()
                .insideATransaction()
                .taking(objetoAConvertir)
                .convertingTo(claseDesde)
                .convertTo(claseHacia);
    }
    protected <T> T conversor(Object objetoAConvertir, Type claseDesde, Type claseHacia){
        return (T) createOperation()
                .insideATransaction()
                .taking(objetoAConvertir)
                .convertingTo(claseDesde)
                .convertTo(claseHacia);
    }
}
