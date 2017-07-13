package convention.persistent;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fede on 07/07/17.
 */
@Entity
public class TemaDeMinuta extends PersistableSupport {

    @ManyToOne
    private Minuta minuta;
    public static final String minuta_FIELD = "minuta";


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ActionItem> actionItems;

    public static final String actionItems_FIELD = "actionItems";
    @OneToOne
    private TemaDeReunion tema;
    public static final String tema_FIELD = "tema";

        private String conclusion;
    public static final String conclusion_FIELD = "conclusion";

    public static TemaDeMinuta create(TemaDeReunion temaDeReunion,Minuta minuta) {
        TemaDeMinuta nuevoTema=new TemaDeMinuta();
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

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }
}
