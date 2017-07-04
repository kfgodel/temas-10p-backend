package Persistence;

import ar.com.kfgodel.temas.application.Application;
import convention.persistent.ObligatoriedadDeReunion;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;

import convention.persistent.TemaGeneral;
import convention.services.ReunionService;
import convention.services.TemaGeneralService;
import convention.services.TemaService;
import helpers.TestConfig;
import org.junit.After;
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
    private TemaGeneralService temaGeneralService;

    @Before
    public void setUp(){
        startApplication();
        reunionService = application.getInjector().createInjected(ReunionService.class);
        temaService = application.getInjector().createInjected(TemaService.class);
        temaGeneralService = application.getInjector().createInjected(TemaGeneralService.class);
    }
    @After
    public void drop(){
        application.stop();
    }
    @Test
    public void test01SePuedePersistirCorrectamenteUnaReunion(){
        Reunion nuevaReunion = new Reunion();

        int cantidadDeReunionesAnteriores = reunionService.getAll(AllReunionesUltimaPrimero.create()).size();

        reunionService.save(nuevaReunion);

        List<Reunion> reunionesPersistidas = reunionService.getAll(AllReunionesUltimaPrimero.create());

        Assert.assertEquals(cantidadDeReunionesAnteriores + 1, reunionesPersistidas.size());
    }

    @Test
    public void test02SePuedePersistirCorrectamenteUnTema(){
        TemaDeReunion nuevoTema = new TemaDeReunion();

        int cantidadDeTemasAnteriores = temaService.getAll().size();

        temaService.save(nuevoTema);

        List<TemaDeReunion> temasPersistidos = temaService.getAll();

        Assert.assertEquals(cantidadDeTemasAnteriores + 1, temasPersistidos.size());
    }

    @Test
    public void test03AlObtenerUnaReunionSeTraenSoloSusTemas(){

       Reunion reunion = new Reunion();
       TemaDeReunion temaDeLaReunion = new TemaDeReunion();
       TemaDeReunion temaDeOtraReunion = new TemaDeReunion();

       reunion = reunionService.save(reunion);

       temaDeLaReunion.setReunion(reunion);


       reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

       reunion = reunionService.save(reunion);


       Reunion reunionPersistida = reunionService.get(reunion.getId());

       Assert.assertEquals(1, reunionPersistida.getTemasPropuestos().size());

    }

    @Test
    public void test04LaObligatoriedadDeUnTemaSePersisteCorrectamente(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);

        tema = temaService.save(tema);

        TemaDeReunion temaPersistido = temaService.get(tema.getId());
        Assert.assertEquals(ObligatoriedadDeReunion.OBLIGATORIO, temaPersistido.getObligatoriedad());
    }

    @Test
    public void test05ElMomentoDeCreacionDeUnTemaSeCreaAlPersistirElTema(){
        TemaDeReunion tema = new TemaDeReunion();
        tema = temaService.save(tema);
        TemaDeReunion temaPersistido = temaService.get(tema.getId());
        Assert.assertFalse(temaPersistido.getMomentoDeCreacion() == null);
    }

    @Test
    public void test06SePuedePersistirCorrectamenteUnTemaGeneral(){
        TemaGeneral temaGeneral = new TemaGeneral();
        String titulo = "Tema General";
        temaGeneral.setTitulo(titulo);
        temaGeneral = temaGeneralService.save(temaGeneral);
        Assert.assertEquals(titulo, temaGeneralService.get(temaGeneral.getId()).getTitulo());
    }

    private void startApplication(){
        application = TestApplication.create(TestConfig.create());
        application.start();
    }
}
