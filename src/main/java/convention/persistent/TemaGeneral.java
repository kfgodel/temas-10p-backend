package convention.persistent;

import javax.persistence.Entity;
import java.util.Optional;

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
        temaDeReunion.setTemaGenerador(this);
        return temaDeReunion;
    }

    public void actualizarTema(TemaDeReunion tema) {
        tema.setTitulo(this.getTitulo());
        tema.setDescripcion(this.getDescripcion());
        tema.setDuracion(this.getDuracion());
    }
}
