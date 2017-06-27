package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import convention.persistent.Usuario;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

/**
 * Created by sandro on 26/06/17.
 */
public class UsuarioService {

    @Inject
    private DependencyInjector appInjector;

    public Usuario getSingleUser(Long userId) {
        return ApplicationOperation.createFor(appInjector)
                .insideASession()
                .applying(FindById.create(Usuario.class, userId))
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    return encontrado.orElseThrowRuntime(() -> new WebApplicationException("user not found", 404));
                })
                .convertTo(Usuario.class);
    }

}
