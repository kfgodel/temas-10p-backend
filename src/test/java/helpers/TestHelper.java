package helpers;

import convention.persistent.ObligatoriedadDeReunion;
import convention.persistent.TemaDeReunion;

/**
 * Created by sandro on 30/06/17.
 */
public class TestHelper {

    public TemaDeReunion nuevoTemaObligatorio(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion nuevoTemaNoObligatorio(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeReunion.NO_OBLIGATORIO);
        return tema;
    }
}
