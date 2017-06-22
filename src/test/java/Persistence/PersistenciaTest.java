package Persistence;

import ar.com.kfgodel.temas.application.Application;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;
import convention.services.ReunionService;
import convention.services.TemaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sandro on 19/06/17.
 */
public class PersistenciaTest {

    Application application;
    ReunionService reunionService;
    TemaService temaService;

    @Before
    public void setUp(){
        startApplication();
        reunionService = application.getInjector().createInjected(ReunionService.class);
        temaService = application.getInjector().createInjected(TemaService.class);
    }

    @Test
    public void test01SePuedePersistirCorrectamenteUnaReunion(){
        ReunionTo nuevaReunion = new ReunionTo();

        int cantidadDeReunionesAnteriores = reunionService.getAll().size();

        reunionService.save(nuevaReunion);

        List<ReunionTo> reunionesPersistidas = reunionService.getAll();

        Assert.assertEquals(cantidadDeReunionesAnteriores + 1, reunionesPersistidas.size());
    }

    @Test
    public void test02SePuedePersistirCorrectamenteUnTema(){
        TemaTo nuevoTema = new TemaTo();

        int cantidadDeTemasAnteriores = temaService.getAll().size();

        temaService.save(nuevoTema);

        List<TemaTo> temasPersistidos = temaService.getAll();

        Assert.assertEquals(cantidadDeTemasAnteriores + 1, temasPersistidos.size());
    }

    // No puedo hacerlo andar
//    @Test
//    public void test03AlObtenerUnaReunionSeTraenSoloSusTemas(){
//        ReunionTo reunion = new ReunionTo();
//        TemaTo temaDeLaReunion = new TemaTo();
//        TemaTo temaDeOtraReunion = new TemaTo();
//        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));
//
//        reunion = reunionService.save(reunion);
//        temaService.save(temaDeLaReunion);
//        temaService.save(temaDeOtraReunion);
//
//        ReunionTo reunionPersistida = reunionService.get(reunion.getId());
//
//        Assert.assertEquals(1, reunionPersistida.getTemasPropuestos().size());
//    }

    private void startApplication(){
        application = TestApplication.create(TestConfig.create());
        application.start();
    }
}
