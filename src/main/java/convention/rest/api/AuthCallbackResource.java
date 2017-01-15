package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.users.UserByBackofficeId;
import convention.persistent.Usuario;
import convention.rest.api.tos.BackofficeUserTo;
import org.h2.util.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import java.net.URI;

public class AuthCallbackResource {

  @Inject
  private DependencyInjector appInjector;

  @GET
  public Response getSingleUser(@BeanParam BackofficeUserTo backofficeUser) {
    // Si el usuario cancela, denied llega no vació con la razón. Habría que mostrarle un mensaje al usuario
    if (!StringUtils.isNullOrEmpty(backofficeUser.denied)) {
      return Response.ok("DENIED:" + backofficeUser.denied).build();
    }

    if (!backofficeUser.isValid()) {
      // Si no podemos validar la autenticidad de los parámetros
      return Response.ok("HMAC INVALIDO").build();
    }

    if (!backofficeUser.isRoot()) {
      // Si no es root, no lo dejamos. Habría que también mostrar un mensaje
      return Response.ok("NOT ROOT").build();
    }

    createOperation()
      .insideASession()
      .applying(UserByBackofficeId.create(backofficeUser.uid))
      .mapping(usuarioExistente -> usuarioExistente.orElseGet(() -> crearUsuarioNuevo(backofficeUser)))
      .useIn(this::autenticarEnSesion);

    return Response.temporaryRedirect(URI.create("/proxima-roots")).build();
  }

  private void autenticarEnSesion(Usuario usuario) {

  }

  private Usuario crearUsuarioNuevo(BackofficeUserTo backofficeUser) {
    return createOperation()
      .insideATransaction()
      .taking(Usuario.create(backofficeUser.fullName,
        backofficeUser.username,
        "password",
        backofficeUser.uid))
      .applyingResultOf(Save::create)
      .get();
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

  /*
   * Otra cosa que hay que hacer es ver como mandar al usuario a la ruta de autenticación del backoffice;
   *  https://backoffice.10pines.com/auth/sign_in?app_id=temas-roots&redirect_url=http://localhost:9090/auth/callback
   *
   *  Una opción sería hardcodearlo en el frontend.
   *  Otra opción es crear un endpoint generico aca en el backend, como "/auth/backoffice" y que luego el backend rediriga a donde quiera.
   *
   *  La info de http://localhost:9090 debería salir del ambiente. (De la url en el caso del frontend, o de la data del request en el caso del backend)
   */
}
