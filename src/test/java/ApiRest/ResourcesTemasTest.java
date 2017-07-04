package ApiRest;

import Persistence.TestApplication;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.acciones.CrearProximaReunion;
import ar.com.kfgodel.temas.application.Application;
import convention.persistent.*;

import convention.rest.api.DuracionesResource;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaGeneralResource;
import convention.rest.api.TemaResource;
import convention.rest.api.tos.DuracionDeTemaTo;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;
import convention.services.ReunionService;
import convention.services.TemaGeneralService;
import convention.services.TemaService;
import convention.services.UsuarioService;
import helpers.TestConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


/**
 * Created by fede on 22/06/17.
 */
public class ResourcesTemasTest {

    Application app;

    ReunionService reunionService;
    UsuarioService usuarioService;
    TemaService temaService;
    ReunionResource reunionResource;
    DuracionesResource duracionResource;
    SecurityContext testContextUserFeche;
    Long userId;
    Long otherUserId;
    Usuario otherUser;
    private TemaResource temaResource;
    private TemaGeneralResource temaGeneralResource;

    @Before
    public void setUp() {
        app = TestApplication.create(TestConfig.create());
        app.start();

        reunionService = app.getInjector().createInjected(ReunionService.class);
        usuarioService = app.getInjector().createInjected(UsuarioService.class);
        temaService = app.getInjector().createInjected(TemaService.class);
        reunionResource = app.getInjector().createInjected(ReunionResource.class);
        temaResource = app.getInjector().createInjected(TemaResource.class);
        duracionResource = app.getInjector().createInjected(DuracionesResource.class);
        temaGeneralResource = TemaGeneralResource.create(app.getInjector());

        testContextUserFeche = new SecurityContextTest(usuarioService.getAll().get(0).getId());

        userId = ((JettyIdentityAdapterTest) testContextUserFeche.getUserPrincipal()).getApplicationIdentification();
        otherUser = usuarioService.getAll().stream().filter(userTo -> !userTo.getId().equals(userId)).findFirst().get();
        otherUserId = otherUser.getId();

    }

    @After
    public void drop() {
        app.stop();
    }

    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeLosVotosDeOtrosSiLaReunionEstaPendiente() {

        Reunion reunion = new Reunion();
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();

        reunion = reunionService.save(reunion);

        Usuario unUsuario = usuarioService.get(userId);
        Usuario otroUsuario = usuarioService.get(otherUserId);

        temaDeLaReunion.setReunion(reunion);
        temaDeLaReunion.setAutor(unUsuario);
        temaDeLaReunion.setInteresados(Arrays.asList(unUsuario, otroUsuario, otroUsuario));

        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion = reunionService.update(reunion);

        ReunionTo reunionSolicitada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        TemaTo tema = reunionSolicitada.getTemasPropuestos().get(0);
        Assert.assertEquals(tema.getIdsDeInteresados().size(), 1);
    }

    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeTodosLosVotosSiLaReunionEstaCerrada() {
        Reunion reunion = new Reunion();
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();
        reunion.setStatus(StatusDeReunion.CERRADA);
        reunion = reunionService.save(reunion);

        Usuario unUsuario = usuarioService.get(userId);
        Usuario otroUsuario = usuarioService.get(otherUserId);

        temaDeLaReunion.setReunion(reunion);
        temaDeLaReunion.setAutor(unUsuario);
        temaDeLaReunion.setInteresados(Arrays.asList(unUsuario, otroUsuario, otroUsuario));

        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion = reunionService.update(reunion);

        ReunionTo reunionSolicitada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        TemaTo tema = reunionSolicitada.getTemasPropuestos().get(0);
        Assert.assertEquals(tema.getIdsDeInteresados().size(), 3);
    }

    @Test
    public void sePersistenCorrectamenteLasDuracionesDeLosTemas() {
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();
        temaDeLaReunion.setDuracion(DuracionDeTema.CORTO);
        temaDeLaReunion = ApplicationOperation.createFor(app.getInjector())
                .insideATransaction()
                .taking(temaDeLaReunion)
                .convertingTo(TemaDeReunion.class)
                .applyingResultOf(Save::create)
                .convertTo(TemaDeReunion.class);
        Assert.assertEquals(temaDeLaReunion.getDuracion(), DuracionDeTema.CORTO);
    }

    @Test
    public void crearProximaReunionDevuelveElTercerViernesDeEsteMesSiNoPasoTodavia() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 1);
        LocalDate proximaRoot = CrearProximaReunion.create().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, LocalDate.of(2017, 6, 16));
    }

    @Test
    public void crearProximaReunionDevuelveElTercerViernesDelProximoMesSiYaPasoElTercerViernesDeEsteMes() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 27);
        LocalDate proximaRoot = CrearProximaReunion.create().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, LocalDate.of(2017, 7, 21));
    }

    @Test
    public void crearProximaReunionDevuelveHoySiEsElTercerViernesDelMes() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 16);
        LocalDate proximaRoot = CrearProximaReunion.create().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, diaDeHoy);

    }

    @Test
    public void votarUnTemaNoLoVuelveACrearPeroSiGuardaLosVotos() {
        TemaDeReunion unTema = new TemaDeReunion();
        unTema.setDuracion(DuracionDeTema.MEDIO);
        unTema = temaService.save(unTema);
        Assert.assertEquals(temaService.getAll().size(), 1);
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> {
            try {
                temaDeReunion.agregarInteresado(otherUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return temaDeReunion;
        });
        Assert.assertEquals(temaService.getAll().size(), 1);
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> {
            try {
                temaDeReunion.agregarInteresado(otherUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return temaDeReunion;
        });
        Assert.assertEquals(temaService.getAll().size(), 1);
        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 2);
    }

    @Test
    public void sePuedeAgregarUnVotoEnUnTemaConMenosDe3VotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(new TemaDeReunion());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 1);
    }

    @Test(expected = WebApplicationException.class)
    public void NosePuedeAgregarUnVotoEnUnTemaConMasDe3VotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(new TemaDeReunion());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
    }

    @Test
    public void sePuedeQuitarUnVotoEnUnTemaConVotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(new TemaDeReunion());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().desvotarTema(otherUser, temaDeReunion));

        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 0);
    }

    @Test(expected = WebApplicationException.class)
    public void NoSePuedeQuitarUnVotoEnUnTemaSinVotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(new TemaDeReunion());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaResource().desvotarTema(otherUser, temaDeReunion));


    }

    @Test
    public void siUnaReunionEstaCerradaSeRecuperaConOrdenDePrioridad() {
        TemaDeReunion temaDeMayorPrioridad = new TemaDeReunion();
        temaDeMayorPrioridad.setPrioridad(1);
        TemaDeReunion temaDeMenorPrioridad = new TemaDeReunion();
        temaDeMenorPrioridad.setPrioridad(4);
        TemaDeReunion temaDePrioridadMedia = new TemaDeReunion();
        temaDePrioridadMedia.setPrioridad(2);
        Reunion unaReunion = Reunion.create(LocalDate.now());
        unaReunion.setTemasPropuestos(Arrays.asList(temaDeMenorPrioridad, temaDeMayorPrioridad, temaDePrioridadMedia));
        unaReunion.setStatus(StatusDeReunion.CERRADA);
        unaReunion = reunionService.save(unaReunion);
        temaDeMayorPrioridad.setReunion(unaReunion);
        temaDeMenorPrioridad.setReunion(unaReunion);
        temaDePrioridadMedia.setReunion(unaReunion);
        unaReunion = reunionService.update(unaReunion);
        unaReunion = reunionService.getAndMapping(unaReunion.getId(), reunion -> reunionResource.muestreoDeReunion(reunion, userId,testContextUserFeche));
        Assert.assertArrayEquals(unaReunion.getTemasPropuestos().toArray(), Arrays.asList(temaDeMayorPrioridad, temaDePrioridadMedia, temaDeMenorPrioridad).toArray());
    }

    @Test
    public void lasDuracionesDeTemasSeDevuelvenOrdenadasEnElResource() {
        List<DuracionDeTema> listaOrdenada = Arrays.asList(DuracionDeTema.values());
        listaOrdenada.sort((duracion1, duracion2) -> duracion1.getCantidadDeMinutos() - duracion2.getCantidadDeMinutos());
        Assert.assertArrayEquals(duracionResource.getAllDuraciones().toArray(), listaOrdenada.toArray());
    }

    @Test
    public void seObtieneCorrectamenteLaObligatoriedadDeUnTema(){
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();
        temaDeLaReunion.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);

        temaDeLaReunion = temaService.save(temaDeLaReunion);

        TemaTo temaPersistido = temaResource.getSingle(temaDeLaReunion.getId());
        Assert.assertEquals("OBLIGATORIO", temaPersistido.getObligatoriedad());
    }

    @Test
    public void alObtenerUnaReunionSusTemasTienenObligatoriedad(){
        Reunion reunion = new Reunion();
        reunion = reunionService.save(reunion);

        TemaDeReunion tema = new TemaDeReunion();
        tema.setReunion(reunion);
        tema.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        tema.setDescripcion("una descripción");
        temaService.save(tema);

        ReunionTo reunionRecuperada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);
        Assert.assertEquals("OBLIGATORIO", reunionRecuperada.getTemasPropuestos().get(0).getObligatoriedad());
    }



    @Test
    public void alRecibirUnTemaDelFrontendSeRecibeSuMomentoDeCreacion(){
        TemaDeReunion tema = new TemaDeReunion();
        tema = temaService.save(tema);

        TemaTo temaEnviado = temaResource.getSingle(tema.getId());

        TemaDeReunion temaRecibido = ApplicationOperation.createFor(app.getInjector())
                .insideATransaction()
                .taking(temaEnviado)
                .convertingTo(TemaDeReunion.class)
                .convertTo(TemaDeReunion.class);

        Assert.assertFalse(temaRecibido.getMomentoDeCreacion() == null);
    }

    @Test
    public void alRecibirUnaReunionDelFrontendSusTemasTienenMomentoDeCreacion(){
        Reunion reunion = new Reunion();
        TemaDeReunion tema = new TemaDeReunion();
        tema.setReunion(reunion);
        reunion = reunionService.save(reunion);
        temaService.save(tema);

        ReunionTo reunionEnviada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        Reunion reunionRecibida = ApplicationOperation.createFor(app.getInjector())
                .insideATransaction()
                .taking(reunionEnviada)
                .convertingTo(Reunion.class)
                .convertTo(Reunion.class);

        Assert.assertFalse(reunionRecibida.getTemasPropuestos().get(0).getMomentoDeCreacion() == null);
    }
}

