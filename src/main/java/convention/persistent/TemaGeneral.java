package convention.persistent;

/**
 * Created by sandro on 03/07/17.
 */
public class TemaGeneral {

    public TemaDeReunion generarTemaPara(Reunion reunion) {
        TemaDeReunion temaDeReunion = new TemaDeReunion();
        temaDeReunion.setReunion(reunion);
        temaDeReunion.setObligatoriedad(ObligatoriedadDeReunion.OBLIGATORIO);
        return temaDeReunion;
    }
}
