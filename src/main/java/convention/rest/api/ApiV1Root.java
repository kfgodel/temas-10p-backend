package convention.rest.api;

import ar.com.kfgodel.temas.application.Application;

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
  private TemaDeReunionResource temas;
  private DuracionesResource duraciones;
  private TemaGeneralResource temasGenrales;
  private MinutaResource minutas;
  private TemaDeMinutaResource temasDeMinuta;

  @Path("/session")
  public SessionResource session() {
    if (session == null) {
      session = SessionResource.create();
    }
    return session;
  }
  @Path("/duraciones")
  public DuracionesResource duraciones() {
    if (duraciones == null) {
      duraciones = application.injector().createInjected(DuracionesResource.class);;
    }
    return duraciones;
  }
  @Path("/temaDeMinuta")
  public TemaDeMinutaResource temasDeMinuta() {
    if (temasDeMinuta == null) {
      temasDeMinuta = application.injector().createInjected(TemaDeMinutaResource.class);
    }
    return temasDeMinuta;
  }
  @Path("/users")
  public UserResource users() {
    if (users == null) {


      users = application.injector().createInjected(UserResource.class);
    }
    return users;
  }
  @Path("/minuta")
  public MinutaResource minutas() {
    if (minutas == null) {


      minutas = application.injector().createInjected(MinutaResource.class);
    }
    return minutas;
  }
  @Path("/reuniones")
  public ReunionResource reuniones() {
    if (reuniones == null) {
      reuniones = application.injector().createInjected(ReunionResource.class);
    }
    return reuniones;
  }

  @Path("/temas")
  public TemaDeReunionResource temas() {
    if (temas == null) {
      temas = application.injector().createInjected(TemaDeReunionResource.class);
    }
    return temas;
  }

  @Path("/temas-generales")
  public TemaGeneralResource temasGenerales() {
    if (temasGenrales == null) {
      temasGenrales = application.injector().createInjected(TemaGeneralResource.class);
    }
    return temasGenrales;
  }

}
