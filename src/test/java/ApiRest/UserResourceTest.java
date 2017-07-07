package ApiRest;

import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.UserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by fede on 05/07/17.
 */
public class UserResourceTest extends ResourcesTemasTest {

    UserResource userResource;

    @Override
    public void setUp() {
        super.setUp();
        userResource = new UserResource();
        app.getInjector().bindTo(UserResource.class, userResource);
    }

    @Test
    public void AlPedirLosNoVotantesDeUnaReunionMeDevuelveTodosLosUsuariosQueNoVotaron() throws Exception {
        Reunion unaReunion = reunionService.save(new Reunion());
        TemaDeReunion unTema = new TemaDeReunion();
        unTema.agregarInteresado(user);
        unTema.setReunion(unaReunion);
        unaReunion.setTemasPropuestos(Arrays.asList(unTema));
        unaReunion = reunionService.update(unaReunion);

        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().allMatch(userTo -> userTo.getId() != userId));
        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().anyMatch(userTo -> userTo.getId().equals(otherUserId)));

    }
}
