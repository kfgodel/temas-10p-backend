package Domain;

import convention.persistent.ObligatoriedadDeReunion;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaGeneral;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sandro on 03/07/17.
 */
public class TemaGeneralTest {

    @Test
    public void test01UnTemaGeneralPuedeGenerarUnTemaDeReunionParaUnaReunion(){
        TemaGeneral temaGeneral = new TemaGeneral();
        Reunion reunion = new Reunion();

        TemaDeReunion unTema = temaGeneral.generarTemaPara(reunion);

        Assert.assertEquals(reunion, unTema.getReunion());
    }

    @Test
    public void test02ElTemaDeReunionGeneradoPorUnTemaGeneralEsObligatorio(){
        TemaGeneral temaGeneral = new TemaGeneral();
        Reunion reunion = new Reunion();
        TemaDeReunion unTema = temaGeneral.generarTemaPara(reunion);

        Assert.assertEquals(ObligatoriedadDeReunion.OBLIGATORIO, unTema.getObligatoriedad());
    }

}
