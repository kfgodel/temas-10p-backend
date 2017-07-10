package convention.persistent;

import javax.persistence.Entity;

/**
 * Created by sandro on 03/07/17.
 */
@Entity
public class TemaGeneral extends Tema {

    public TemaDeReunion generarTemaPara(Reunion reunion) {
        TemaDeReunion temaDeReunion = new TemaDeReunion();
        temaDeReunion.setReunion(reunion);
        temaDeReunion.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO_GENERAL);
        temaDeReunion.setTitulo(this.getTitulo());
        temaDeReunion.setDescripcion(this.getDescripcion());
        temaDeReunion.setAutor(this.getAutor());
        temaDeReunion.setDuracion(this.getDuracion());
        temaDeReunion.setEsDeUnTemaGeneral(true);
        return temaDeReunion;
    }
}
