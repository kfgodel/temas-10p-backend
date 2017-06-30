package Domain;

import convention.persistent.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sandro on 19/06/17.
 */
public class ReunionTest {

    @Test
    public void test01AlCrearUnaReunionSuEstadoEsPendiente(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }

    @Test
    public void test02AlCrearUnaReunionNoTieneTemas(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Assert.assertEquals(0, unaReunion.getTemasPropuestos().size());
    }

    @Test
    public void test03AlCerrarUnaReunionSuEstadoEsCerrada(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        unaReunion.cerrarVotacion();
        Assert.assertEquals(StatusDeReunion.CERRADA, unaReunion.getStatus());
    }

    @Test
    public void test04AlCerrarUnaReunionLosTemasPropuestosSeOrdenanPorCantidadDeVotos() throws Exception {
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Usuario unUsuario = new Usuario();
        TemaDeReunion tema1 = new TemaDeReunion();
        tema1.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        TemaDeReunion tema2 = new TemaDeReunion();
        tema2.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        TemaDeReunion tema3 = new TemaDeReunion();
        tema3.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        List<TemaDeReunion> temasDeLaReunion = Arrays.asList(tema1, tema2, tema3);
        unaReunion.setTemasPropuestos(temasDeLaReunion);

        //Agrego 2 votos a la propuesta 1
        tema1.agregarInteresado(unUsuario);
        tema1.agregarInteresado(unUsuario);

        //Agrego 1 votos a la propuesta 2
        tema2.agregarInteresado(unUsuario);

        //Agrego 3 votos a la propuesta 3
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);

        unaReunion.cerrarVotacion();

        Assert.assertEquals(tema3, unaReunion.getTemasPropuestos().get(0));
        Assert.assertEquals(tema1, unaReunion.getTemasPropuestos().get(1));
        Assert.assertEquals(tema2, unaReunion.getTemasPropuestos().get(2));
    }

    @Test
    public void test05AlReabrirUnaReunionCerradaSuEstadoEsPendiente(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        unaReunion.cerrarVotacion();
        unaReunion.reabrirVotacion();
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }
}
