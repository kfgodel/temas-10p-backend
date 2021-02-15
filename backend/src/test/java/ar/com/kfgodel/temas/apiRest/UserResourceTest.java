package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.UserResource;
import org.junit.Assert;
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
        userResource =  UserResource.create(app.injector());
    }

    @Test
    public void AlPedirLosNoVotantesDeUnaReunionMeDevuelveTodosLosUsuariosQueNoVotaron(){
        Reunion unaReunion = reunionService.save(new Reunion());
        TemaDeReunion unTema = TemaDeReunion.create();
        unTema.agregarInteresado(user);
        unTema.setReunion(unaReunion);
        unaReunion.setTemasPropuestos(Arrays.asList(unTema));
        unaReunion = reunionService.update(unaReunion);

        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().allMatch(userTo -> userTo.getId() != userId));
        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().anyMatch(userTo -> userTo.getId().equals(otherUserId)));

    }
}
