package Domain;

import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by sandro on 06/07/17.
 */
public class MinutaTest {

    @Test
    public void test01UnaMinutaNuevaNoTieneAsistentes(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        Minuta minuta = Minuta.create(reunion);
        Assert.assertEquals(0, minuta.getAsistentes().size());
    }

    @Test
    public void test02AUnaMinutaSeLePuedenAgregarAsistentes(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        Minuta minuta = Minuta.create(reunion);
        Usuario unUsuario = new Usuario();
        minuta.agregarAsistente(unUsuario);
        Assert.assertEquals(1, minuta.getAsistentes().size());
    }

    @Test
    public void test03AUnaMinutaSeLePuedenQuitarAsistentes(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        Minuta minuta = Minuta.create(reunion);
        Usuario unUsuario = new Usuario();
        minuta.agregarAsistente(unUsuario);
        minuta.quitarAsistente(unUsuario);
        Assert.assertEquals(0, minuta.getAsistentes().size());
    }

}
