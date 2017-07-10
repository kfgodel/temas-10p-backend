package convention.persistent;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fede on 04/07/17.
 */
@Entity
public class Minuta extends PersistableSupport {

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Usuario> asistentes;
    public static final String asistentes_FIELD = "asistentes";

    @OneToOne
    private Reunion reunion;
    public static final String reunion_FIELD = "reunion";

    public static final String fecha_FIELD = "fecha";

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TemaDeMinuta> temas;
    public static final String temas_FIELD = "temas";

    public LocalDate getFecha() {
        return reunion.getFecha();
    }

    public List<TemaDeMinuta> getTemas(){
        return temas;
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
        nuevaMinuta.reunion=reunion;
        List<TemaDeMinuta> temas=reunion.getTemasPropuestos().stream()
                .map(temaDeReunion -> TemaDeMinuta.create(temaDeReunion,nuevaMinuta)).collect(Collectors.toList());
        nuevaMinuta.setTemas(temas);
        return nuevaMinuta;
    }

    public List<Usuario> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<Usuario> asistentes) {
        this.asistentes = asistentes;
    }

    public void setTemas(List<TemaDeMinuta> temas) {
        this.temas = temas;
    }
}
