package Domain;

import convention.persistent.Minuta;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sandro on 06/07/17.
 */
public class MinutaTest {

    @Test
    public void test01UnaMinutaNuevaNoTieneAsistentes(){
        Minuta minuta = Minuta.create();
        Assert.assertEquals(0, minuta.getAsistentes().size());
    }

    @Test
    public void test02AUnaMinutaSeLePuedenAgregarAsistentes(){
        Minuta minuta = Minuta.create();
        Usuario unUsuario = new Usuario();
        minuta.agregarAsistente(unUsuario);
        Assert.assertEquals(1, minuta.getAsistentes().size());
    }

    @Test
    public void test03AUnaMinutaSeLePuedenQuitarAsistentes(){
        Minuta minuta = Minuta.create();
        Usuario unUsuario = new Usuario();
        minuta.agregarAsistente(unUsuario);
        minuta.quitarAsistente(unUsuario);
        Assert.assertEquals(0, minuta.getAsistentes().size());
    }

}
