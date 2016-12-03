package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.users.UserByBackofficeId;

import org.h2.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import convention.persistent.Usuario;

public class AuthCallbackResource {

  @Inject
  private DependencyInjector appInjector;

  // Puse esto aca pq no sabía en donde ponerlo
  public static class BackofficeUser {

    @QueryParam("uid")
    public String uid;

    @QueryParam("email")
    public String email;

    @QueryParam("username")
    public String username;

    @QueryParam("full_name")
    public String fullName;

    @QueryParam("root")
    public String root;

    @QueryParam("denied")
    public String denied;

    @QueryParam("hmac")
    public String hmac;

    public Boolean isRoot() {
      return root.equals("true");
    }

    public Boolean isValid() {
      // Esto no debería estar hardcodeado. Debería venir del ambiente.
      String key = "CHANGE_ME";
      String algorithm = "HmacSHA256";
      String data =
          "uid=" + uid + "&email=" + email + "&username=" + username + "&full_name=" + fullName + "&root=" + root;
      Mac mac;
      try {
        mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key.getBytes(), algorithm));
        byte[] generatedHmacBytes = mac.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : generatedHmacBytes) {
          sb.append(String.format("%02x", b));
        }
        String generatedHmac = sb.toString();
        return hmac.equals(generatedHmac);
      } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
        return false;
      }
    }
  }

  // Esta ruta no tendria que estar authenticada
  @GET
  public Response getSingleUser(@BeanParam BackofficeUser backofficeUser) {
    // Si el usuario cancela, denied llega no vació con la razón. Habría que mostrarle un mensaje al usuario
    if (!StringUtils.isNullOrEmpty(backofficeUser.denied)) {
      return Response.ok("DENIED:" + backofficeUser.denied).build();
    }

    if (!backofficeUser.isValid()) {
      // Si no podemos validar la autenticidad de los parámetros
      return Response.ok("HMAC INVALIDO").build();
    }

    if (backofficeUser.isRoot()) {
      /* Aca viene la papa
        Primero buscamos un Usuario por backofficeId (el uid). Si existe entonces lo loggeamos a la sesión y ya
        Si no existe, hay que crear un nuevo Usuario. Y luego logearlo.

        Finalmente redirigimos al home de la app o, idealmente, si venía de otra ruta pero no estaba autenticado,
        lo redirigimos a la ruta original de la que venía.
      */

      return createOperation()
          .insideASession()
          .applying(UserByBackofficeId.create(backofficeUser.uid))
          .mapping(maybeUsuario ->
                       maybeUsuario.orElseGet(() ->
                                                  createOperation()
                                                      .insideASession()
                                                      .taking(Usuario
                                                                  .create(backofficeUser.fullName,
                                                                          backofficeUser.username,
                                                                          "password",
                                                                          backofficeUser.uid))
                                                      .applyingResultOf(Save::create)
                                                      .get())
          ).mapping(usuario -> {
            // Aca hay que hacer log in del usuario, ni puta idea
            return Response.temporaryRedirect(URI.create("/")).build();
          }).get();
    } else {
      // Si no es root, no lo dejamos. Habría que también mostrar un mensaje
      return Response.ok("NOT ROOT").build();
    }
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
