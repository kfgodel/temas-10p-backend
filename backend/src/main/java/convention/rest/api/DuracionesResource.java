package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.DuracionDeTema;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.DuracionDeTemaTo;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;
import convention.rest.api.tos.UserTo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fede on 22/06/17.
 */

@Produces("application/json")
@Consumes("application/json")

public class DuracionesResource extends Resource {

   public  static  DuracionesResource create(DependencyInjector appInjector){
        DuracionesResource duracionesResource=new DuracionesResource();
        duracionesResource.appInjector=appInjector;
        return duracionesResource;
    }

    public List<DuracionDeTema> getAllDuraciones(){
            List<DuracionDeTema> listaOrdenada=Arrays.asList(DuracionDeTema.values());
            listaOrdenada.sort((duracion1, duracion2) ->duracion1.getCantidadDeMinutos()-duracion2.getCantidadDeMinutos() );
            return   createOperation().
                    insideASession()
                    .taking(listaOrdenada)
                    .convertTo(new ReferenceOf<List<DuracionDeTema>>(){}.getReferencedType());

    }

    @GET
    public List<DuracionDeTemaTo> getAll() {
         return  convertir(getAllDuraciones(), new ReferenceOf<List<DuracionDeTemaTo>>(){}.getReferencedType());
    }


}
