package Persistance;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.ConfigurationSelector;
import ar.com.kfgodel.temas.config.HerokuPriorityConfigSelector;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.Reunion;
import convention.rest.api.ReunionResource;
import convention.rest.api.tos.ReunionTo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 19/06/17.
 */
public class PersistenciaTest {

    private static final Type LISTA_DE_REUNIONES_TO = new ReferenceOf<List<ReunionTo>>() {}.getReferencedType();
    Application application;

    private void startApplication(){
        ConfigurationSelector selector = HerokuPriorityConfigSelector.create();
        TemasConfiguration applicationConfig = selector.selectConfig();
        application = TestApplication.create(applicationConfig);
        application.start();
    }

    @Test
    public void test01SePuedePersistirCorrectamenteUnaReunion(){
        ReunionTo nuevaReunion = new ReunionTo();
        startApplication();

        List<ReunionTo> reunionesAnteriores = ApplicationOperation.createFor(application.getInjector())
                                                .insideASession()
                                                .applying(AllReunionesUltimaPrimero.create())
                                                .convertTo(LISTA_DE_REUNIONES_TO);
        int cantidadDeReunionesAnteriores = reunionesAnteriores.size();

        ApplicationOperation.createFor(application.getInjector())
                .insideATransaction()
                .taking(nuevaReunion)
                .convertingTo(Reunion.class)
                .applyingResultOf(Save::create)
                .convertTo(ReunionTo.class);

        List<ReunionTo> reunionesPersistidas= ApplicationOperation.createFor(application.getInjector())
                                                .insideASession()
                                                .applying(AllReunionesUltimaPrimero.create())
                                                .convertTo(LISTA_DE_REUNIONES_TO);

        Assert.assertEquals(cantidadDeReunionesAnteriores + 1, reunionesPersistidas.size());
    }
}
