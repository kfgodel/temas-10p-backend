package Persistence;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import convention.persistent.*;
import convention.services.MinutaService;
import convention.services.ReunionService;
import convention.services.TemaGeneralService;
import convention.services.TemaService;
import helpers.TestConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
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
    private MinutaService minutaService;

    @Before
    public void setUp(){
        startApplication();
        reunionService = application.getInjector().createInjected(ReunionService.class);
        temaService = application.getInjector().createInjected(TemaService.class);
        temaGeneralService = application.getInjector().createInjected(TemaGeneralService.class);
        minutaService = application.getInjector().createInjected(MinutaService.class);

        application.getInjector().bindTo(ReunionService.class, reunionService);
        application.getInjector().bindTo(TemaService.class, temaService);
        application.getInjector().bindTo(TemaGeneralService.class, temaGeneralService);
        application.getInjector().bindTo(MinutaService.class, minutaService);
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

       reunion = reunionService.update(reunion);


       Reunion reunionPersistida = reunionService.get(reunion.getId());

       Assert.assertEquals(1, reunionPersistida.getTemasPropuestos().size());

    }

    @Test
    public void test04LaObligatoriedadDeUnTemaSePersisteCorrectamente(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);

        tema = temaService.save(tema);

        TemaDeReunion temaPersistido = temaService.get(tema.getId());
        Assert.assertEquals(ObligatoriedadDeTema.OBLIGATORIO, temaPersistido.getObligatoriedad());
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

    @Test
    public void test07AlGuardarUnaReunionSeAgreganLosTemasGeneralesCorrespondientes(){
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));

        reunion = reunionService.save(reunion);

        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test08AlGuardarUnTemaGeneralElMismoSeAgregaATodasLasReunionesAbiertas(){
        Reunion reunionAbierta = Reunion.create(LocalDate.of(2017, 06, 26));
        reunionAbierta = reunionService.save(reunionAbierta);

        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);

        Assert.assertEquals(1, reunionService.get(reunionAbierta.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test09AlGuardarUnaTemaGeneralElMismoNoSeAgregaALasReunionesCerradas(){
        Reunion reunionCerrada = Reunion.create(LocalDate.of(2017, 06, 26));
        reunionCerrada.setStatus(StatusDeReunion.CERRADA);
        reunionCerrada = reunionService.save(reunionCerrada);

        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);

        Assert.assertEquals(0, reunionService.get(reunionCerrada.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test10SePuedePersistirSiUnTemaDeReunionFueGeneradoPorUnTemaGeneral(){
        Reunion reunion = new Reunion();
        reunionService.save(reunion);
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);
        TemaDeReunion temaDeReunion = temaGeneral.generarTemaPara(reunion);
        temaDeReunion = temaService.save(temaDeReunion);

        Assert.assertTrue(temaService.get(temaDeReunion.getId()).fueGeneradoPorUnTemaGeneral());
    }

    @Test
    public void test11SePuedePersistirUnaMinuta(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTema = new TemaDeReunion();
        reunion = reunionService.save(reunion);
        unTema.setReunion(reunion);
        temaService.save(unTema);
        reunion.setTemasPropuestos(Arrays.asList(unTema));
        reunion = reunionService.update(reunion);

        Minuta minuta = Minuta.create(reunion);
        minuta = minutaService.save(minuta);

        Minuta minutaRecuperada = minutaService.get(minuta.getId());

        Assert.assertEquals(minuta.getId(), minutaRecuperada.getId());
        Assert.assertEquals(minuta.getReunion().getId(), minutaRecuperada.getReunion().getId());
        Assert.assertEquals(minuta.getFecha(), minutaRecuperada.getFecha());
        Assert.assertEquals(1, minutaRecuperada.getTemas().size());
    }

    @Test
    public void test12AlBorrarUnTemaGeneralSusTemasDeReunionAsociadosSeEliminanTambien(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());

        temaGeneralService.delete(temaGeneral.getId());

        Assert.assertEquals(0, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test13AlBorrarUnTemaGeneralSusTemasDeReunionAsociadosNoSeEliminanDeLasReunionesCerradas(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        reunion = reunionService.get(reunion.getId());
        reunion.setStatus(StatusDeReunion.CERRADA);
        reunionService.update(reunion);

        temaGeneralService.delete(temaGeneral.getId());
        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test14AlBorrarUnTemaGeneralNoSeBorranlosTemasDeReunionQueNoFueronGeneradosPorEl(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTemaNoGenerado = new TemaDeReunion();
        unTemaNoGenerado.setReunion(reunion);
        reunion.setTemasPropuestos(Arrays.asList(unTemaNoGenerado));

        reunion = reunionService.save(reunion);
        temaService.save(unTemaNoGenerado);

        TemaGeneral unTemaGeneral = new TemaGeneral();
        unTemaGeneral = temaGeneralService.save(unTemaGeneral);
        TemaGeneral otroTemaGeneral = new TemaGeneral();
        temaGeneralService.save(otroTemaGeneral);

        temaGeneralService.delete(unTemaGeneral.getId());
        Assert.assertEquals(2, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    private void startApplication(){
        application = TestApplication.create(TestConfig.create());
        application.start();
    }
}
