package Domain;

import convention.persistent.ObligatoriedadDeReunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;

/**
 * Created by sandro on 19/06/17.
 */
public class TemaDeReunionTest {

    @Test
    public void test01AlAgregarUnInteresadoAlTemaLaCantidadDeVotosAumentaEn1() throws Exception {
        TemaDeReunion unTema = new TemaDeReunion();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        Assert.assertEquals(1, unTema.getCantidadDeVotos());
    }

    @Test
    public void test02AlBorrarUnInteresadoDelTemaLaCantidadDeVotosDisminuyeEn1() throws Exception {
        TemaDeReunion unTema = new TemaDeReunion();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        unTema.quitarInteresado(unUsuario);
        Assert.assertEquals(0, unTema.getCantidadDeVotos());
    }

    @Test
    public void test03UnTemaObligatorioNoPuedeSerVotado(){
        TemaDeReunion unTemaObligatorio = new TemaDeReunion();
        unTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        Assert.assertFalse(unTemaObligatorio.puedeSerVotado());
    }

    @Test
    public void test04UnTemaNoObligatorioPuedeSerVotado(){
        TemaDeReunion unTemaObligatorio = new TemaDeReunion();
        unTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        Assert.assertTrue(unTemaObligatorio.puedeSerVotado());
    }

    @Test
    public void test05NoSePuedenAgregarInteresadosAUnTemaObligatorio(){
        TemaDeReunion unTemaObligatorio = new TemaDeReunion();
        unTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        Usuario unUsuario = new Usuario();
        try{
            unTemaObligatorio.agregarInteresado(unUsuario);
            fail("No debería permitir agregar un interesado a un tema obligatorio");
        }catch (Exception exception){
            Assert.assertThat(exception.getMessage(), is(TemaDeReunion.mensajeDeErrorAlAgregarInteresado()));
        }

    }

}
