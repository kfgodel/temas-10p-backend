package helpers;

import convention.persistent.ObligatoriedadDeTema;
import convention.persistent.TemaDeReunion;

/**
 * Created by sandro on 30/06/17.
 */
public class TestHelper {

    public TemaDeReunion nuevoTemaObligatorio(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion nuevoTemaNoObligatorio(){
        TemaDeReunion tema = new TemaDeReunion();
        tema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);
        return tema;
    }
}
