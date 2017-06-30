package Domain;

import convention.persistent.ObligatoriedadDeReunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Test
    public void test06UnTemaObligatorioTieneMayorPrioridadQueUnoNoObligatorio(){
        TemaDeReunion unTemaObligatorio = new TemaDeReunion();
        unTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);

        TemaDeReunion unTemaNoObligatorio = new TemaDeReunion();
        unTemaNoObligatorio.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);

        Assert.assertTrue(unTemaObligatorio.tieneMayorPrioridadQue(unTemaNoObligatorio));
    }

    @Test
    public void test07UnTemaNoObligatorioNoTieneMayorPrioridadQueUnoObligatorio(){
        TemaDeReunion unTemaObligatorio = new TemaDeReunion();
        unTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);

        TemaDeReunion unTemaNoObligatorio = new TemaDeReunion();
        unTemaNoObligatorio.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);

        Assert.assertFalse(unTemaNoObligatorio.tieneMayorPrioridadQue(unTemaObligatorio));
    }

    @Test
    public void test08UnTemaObligatorioTieneMayorPrioridadQueOtroSiFueCreadoAntes(){
        TemaDeReunion primerTemaObligatorio = new TemaDeReunion();
        primerTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        primerTemaObligatorio.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));

        TemaDeReunion segundoTemaObligatorio = new TemaDeReunion();
        segundoTemaObligatorio.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        segundoTemaObligatorio.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));

        Assert.assertTrue(primerTemaObligatorio.tieneMayorPrioridadQue(segundoTemaObligatorio));
    }

    @Test
    public void test09UnTemaNoObligatorioTieneMayorPrioridadQueOtroSiTieneMasVotos() throws Exception {
        Usuario unUsuario = new Usuario();

        TemaDeReunion temaMasVotado = new TemaDeReunion();
        temaMasVotado.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        temaMasVotado.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));
        temaMasVotado.agregarInteresado(unUsuario);

        TemaDeReunion temaMenosVotado = new TemaDeReunion();
        temaMenosVotado.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        temaMenosVotado.setMomentoDeCreacion(LocalDateTime.of(2016, 06, 26, 0, 0));

        Assert.assertTrue(temaMasVotado.tieneMayorPrioridadQue(temaMenosVotado));
    }

    @Test
    public void test10UnTemaNoObligatorioTieneMayorPrioridadQueOtroSiTienenLaMismaCantidadDeVotosYFueCreadoAntes() throws Exception {
        Usuario unUsuario = new Usuario();

        TemaDeReunion primerTema = new TemaDeReunion();
        primerTema.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        primerTema.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));
        primerTema.agregarInteresado(unUsuario);

        TemaDeReunion segundoTema = new TemaDeReunion();
        segundoTema.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        segundoTema.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));
        segundoTema.agregarInteresado(unUsuario);

        Assert.assertTrue(primerTema.tieneMayorPrioridadQue(segundoTema));
    }
}
