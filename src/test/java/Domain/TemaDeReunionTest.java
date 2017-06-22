package Domain;

import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sandro on 19/06/17.
 */
public class TemaDeReunionTest {

    @Test
    public void test01AlAgregarUnInteresadoAlTemaLaCantidadDeVotosAumentaEn1(){
        TemaDeReunion unTema = new TemaDeReunion();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        Assert.assertEquals(1, unTema.getCantidadDeVotos());
    }

    @Test
    public void test02AlBorrarUnInteresadoDelTemaLaCantidadDeVotosDisminuyeEn1(){
        TemaDeReunion unTema = new TemaDeReunion();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        unTema.quitarInteresado(unUsuario);
        Assert.assertEquals(0, unTema.getCantidadDeVotos());
    }

}
