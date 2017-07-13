package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by fede on 07/07/17.
 */
@Entity
public class TemaDeMinuta extends PersistableSupport {

    @ManyToOne
    private Minuta minuta;
    public static final String minuta_FIELD = "minuta";
    @OneToOne
    private TemaDeReunion tema;
    public static final String tema_FIELD = "tema";

    private String conclusion;
    public static final String conclusion_FIELD = "conclusion";

    private boolean fueTratado;
    public static final String fueTratado_FIELD = "fueTratado";

    public static TemaDeMinuta create(TemaDeReunion temaDeReunion, Minuta minuta) {
        TemaDeMinuta nuevoTema = new TemaDeMinuta();
        nuevoTema.setTema(temaDeReunion);
        nuevoTema.setMinuta(minuta);
        return nuevoTema;
    }

    public TemaDeReunion getTema() {
        return tema;
    }

    public void setTema(TemaDeReunion tema) {
        this.tema = tema;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public void setFueTratado(boolean fueTratado) {
        this.fueTratado = fueTratado;
    }

    public boolean getFueTratado() {
        return fueTratado;
    }
}
