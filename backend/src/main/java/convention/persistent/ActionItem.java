package convention.persistent;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fede on 13/07/17.
 */

@Entity
public class ActionItem  extends PersistableSupport {

    public static final String responsables_FIELD = "responsables";
    public static final String tema_FIELD = "tema";
    public static final String descripcion_FIELD = "descripcion";
    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Usuario> responsables;
    @OneToOne
    private TemaDeMinuta tema;
    private String descripcion;

    public List<Usuario> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<Usuario> responsables) {
        this.responsables = responsables;
    }

    public TemaDeMinuta getTema() {
        return tema;
    }

    public void setTema(TemaDeMinuta tema) {
        this.tema = tema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
