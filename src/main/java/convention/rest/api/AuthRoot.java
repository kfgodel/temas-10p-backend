package convention.rest.api;

import ar.com.kfgodel.temas.application.Application;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("/auth")
public class AuthRoot {

  // Injected by jetty/jersey H2k internal binder (which only knwons the application)
  @Inject
  private Application application;

  private AuthCallbackResource authCallback;

  @Path("/callback")
  public AuthCallbackResource authCallback() {
    if (authCallback == null) {
      authCallback = application.getInjector().createInjected(AuthCallbackResource.class);
    }
    return authCallback;
  }

}
