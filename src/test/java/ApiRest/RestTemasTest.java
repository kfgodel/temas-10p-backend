package ApiRest;

import Persistence.TestApplication;
import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import convention.persistent.DuracionDeTema;
import convention.persistent.TemaDeReunion;
import helpers.TestConfig;
import ar.com.kfgodel.temas.application.Application;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaResource;
import convention.rest.api.UserResource;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaTo;

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
        reunionResource=app.getInjector().createInjected(ReunionResource.class);
        temaResource=app.getInjector().createInjected(TemaResource.class);
        userResource=app.getInjector().createInjected(UserResource.class);
          testContextUserFeche=new SecurityContextTest();
        userId=((JettyIdentityAdapterTest) testContextUserFeche.getUserPrincipal()).getApplicationIdentification();
        otherUserId=userResource.getAllUsers().stream().filter(userTo -> !userTo.getId().equals(userId)).findFirst().get().getId();
    }

    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeLosVotosDeOtros(){
        ReunionTo reunion = new ReunionTo();
        TemaTo temaDeLaReunion = new TemaTo();
        reunion = reunionResource.create(reunion);
        temaDeLaReunion.setDuracion(DuracionDeTema.LARGO);
        temaDeLaReunion.setIdDeReunion(reunion.getId());
        temaDeLaReunion.setIdDeAutor(userId);
        temaDeLaReunion.setIdsDeInteresados(Arrays.asList(userId,otherUserId,otherUserId));
        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion= reunionResource.update(reunion,reunion.getId());

        ReunionTo reunionSolicitada=reunionResource.getSingle(reunion.getId(),testContextUserFeche);

        Assert.assertEquals(reunionSolicitada.getTemasPropuestos().get(0).getIdsDeInteresados().size(),1);
    }
    @Test
    public void test001(){
        TemaDeReunion temaDeLaReunion = new TemaDeReunion();
        temaDeLaReunion.setDuracion(DuracionDeTema.CORTO);
        TemaTo tema=ApplicationOperation.createFor(app.getInjector())
                .insideATransaction()
                .taking(temaDeLaReunion)
                .convertTo(TemaTo.class);
        tema=temaResource.create(tema);
         tema=temaResource.getSingle(tema.getId());
        Assert.assertEquals(tema.getDuracion(),DuracionDeTema.CORTO);
  }
}
