package convention.rest.api;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * This type represents the root resource for v1 api access.<br>
 * This allows multiple versions of the api in the same server (if needed)
 * *
 * Created by kfgodel on 03/03/15.
 */
@Path("/api/v1")
public class ApiV1Root {

  // Injected by jetty/jersey H2k internal binder (which only knwons the application)
  @Inject
  private Application application;

  private UserResource users;
  private SessionResource session;
  private ReunionResource reuniones;
  private TemaResource temas;

  @Path("/session")
  public SessionResource session() {
    if (session == null) {
      session = SessionResource.create();
    }
    return session;
  }

  @Path("/users")
  public UserResource users() {
    if (users == null) {


      users = application.getInjector().createInjected(UserResource.class);
    }
    return users;
  }

  @Path("/reuniones")
  public ReunionResource reuniones() {
    if (reuniones == null) {
      reuniones = application.getInjector().createInjected(ReunionResource.class);
    }
    return reuniones;
  }

  @Path("/temas")
  public TemaResource temas() {
    if (temas == null) {
      temas = application.getInjector().createInjected(TemaResource.class);
    }
    return temas;
  }

}
