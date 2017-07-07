package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Minuta;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaTo extends PersistableToSupport {

    @CopyFromAndTo(Minuta.asistentes_FIELD)
    private List<UserTo> asistentes;

    @CopyFromAndTo(Minuta.reunion_FIELD)
    private Long reunionId;

    @CopyFrom(Minuta.fecha_FIELD)
    private String fecha;

    @CopyFrom(Minuta.temas_FIELD)
    private List<TemaTo> temas;

    public List<UserTo> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<UserTo> asistentes) {
        this.asistentes = asistentes;
    }

    public Long getReunionId() {
        return reunionId;
    }

    public void setReunionId(Long reunionId) {
        this.reunionId = reunionId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<TemaTo> getTemas() {
        return temas;
    }

    public void setTemas(List<TemaTo> temas) {
        this.temas = temas;
    }
}
