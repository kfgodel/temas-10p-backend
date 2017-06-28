package ApiRest;

import Persistence.TestApplication;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.application.Application;
import convention.persistent.*;

import convention.rest.api.ReunionResource;
import convention.rest.api.TemaResource;
import convention.rest.api.UserResource;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;
import convention.services.ReunionService;
import convention.services.TemaService;
import convention.services.UsuarioService;
import helpers.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;


/**
 * Created by fede on 22/06/17.
 */
public class RestTemasTest {

    Application app;

    ReunionService reunionService;
    TemaService temaService;
    UsuarioService usuarioService;
    ReunionResource reunionResource;
    TemaResource temaResource;
    UserResource userResource;
    SecurityContext testContextUserFeche;
    Long userId;
    Long otherUserId;

    @Before
    public void setUp(){
        app= TestApplication.create(TestConfig.create());
        app.start();

        reunionService = app.getInjector().createInjected(ReunionService.class);
        temaService = app.getInjector().createInjected(TemaService.class);
        usuarioService = app.getInjector().createInjected(UsuarioService.class);
        reunionResource = app.getInjector().createInjected(ReunionResource.class);
        temaResource = app.getInjector().createInjected(TemaResource.class);
        userResource = app.getInjector().createInjected(UserResource.class);
        testContextUserFeche = new SecurityContextTest();

        userId=((JettyIdentityAdapterTest) testContextUserFeche.getUserPrincipal()).getApplicationIdentification();
        otherUserId=userResource.getAllUsers().stream().filter(userTo -> !userTo.getId().equals(userId)).findFirst().get().getId();
    }

    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeLosVotosDeOtros(){

        Reunion reunion = new Reunion();
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();

        reunion = reunionService.save(reunion);

        Usuario unUsuario = usuarioService.getSingleUser(userId);
        Usuario otroUsuario = usuarioService.getSingleUser(otherUserId);

        temaDeLaReunion.setReunion(reunion);
        temaDeLaReunion.setAutor(unUsuario);
        temaDeLaReunion.setInteresados(Arrays.asList(unUsuario,otroUsuario,otroUsuario));

        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion = reunionService.update(reunion);

        ReunionTo reunionSolicitada = reunionResource.getSingle(reunion.getId(),testContextUserFeche);

        TemaTo tema = reunionSolicitada.getTemasPropuestos().get(0);
        Assert.assertEquals(tema.getIdsDeInteresados().size(),1);
    }

    @Test
    public void sePersistenCorrectamenteLasDuracionesDeLosTemas(){
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();
        temaDeLaReunion.setDuracion(DuracionDeTema.CORTO);
        temaDeLaReunion= ApplicationOperation.createFor(app.getInjector())
                .insideATransaction()
                .taking(temaDeLaReunion)
                .convertingTo(TemaDeReunion.class)
                .applyingResultOf(Save::create)
                .convertTo(TemaDeReunion.class);
        Assert.assertEquals(DuracionDeTema.CORTO, temaDeLaReunion.getDuracion());
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

}

