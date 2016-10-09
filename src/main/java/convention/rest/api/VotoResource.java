package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.acciones.Voto;
import convention.rest.api.tos.VotoTo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

/**
 * Esta clase representa el recurso rest que permite manejar los votos
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class VotoResource {

  @Inject
  private DependencyInjector appInjector;

  @POST
  public void create(VotoTo newState) {
    createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Voto.class)
      .applyResultOf((voto) -> {
        return (context) -> {
          voto.sumar();
          return voto.getTema();
        };
      });
  }


  public static VotoResource create(DependencyInjector appInjector) {
    VotoResource resource = new VotoResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
