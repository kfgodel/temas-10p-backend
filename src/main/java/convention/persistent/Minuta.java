package convention.persistent;

import convention.rest.api.tos.TemaTo;
import convention.rest.api.tos.UserTo;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fede on 04/07/17.
 */
@Entity
public class Minuta extends PersistableSupport {

    @ManyToMany
    private List<Usuario> asistentes;
    public static final String asistentes_FIELD = "asistentes";

    @OneToOne
    private Reunion reunion;
    public static final String reunion_FIELD = "reunion";

    public static final String fecha_FIELD = "fecha";
    public static final String temas_FIELD = "temas";

    public LocalDate getFecha() {
        return reunion.getFecha();
    }

    public List<TemaDeReunion> getTemas(){
        return reunion.getTemasPropuestos();
    }

    public Reunion getReunion(){
        return reunion;
    }

    public void setReunion(Reunion reunion){
        this.reunion = reunion;
    }

    public static Minuta create(Reunion reunion) {
        Minuta nuevaMinuta = new Minuta();
        nuevaMinuta.asistentes = new ArrayList<>();
        nuevaMinuta.reunion = reunion;
        return nuevaMinuta;
    }

    public List<Usuario> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<Usuario> asistentes) {
        this.asistentes = asistentes;
    }

    public void agregarAsistente(Usuario unUsuario) {
        asistentes.add(unUsuario);
    }

    public void quitarAsistente(Usuario unUsuario) {
        asistentes.remove(unUsuario);
    }
}
