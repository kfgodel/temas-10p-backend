package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.services.TemaGeneralService;

/**
 * Created by sandro on 04/07/17.
 */
public class TemaGeneralResource extends Resource {

    private TemaGeneralService temaGeneralService;

    public static TemaGeneralResource create(DependencyInjector appInjector) {
        TemaGeneralResource resource = new TemaGeneralResource();
        resource.appInjector = appInjector;
        resource.temaGeneralService = appInjector.createInjected(TemaGeneralService.class);
        appInjector.bindTo(TemaGeneralService.class, resource.temaGeneralService);
        return resource;
    }

}
