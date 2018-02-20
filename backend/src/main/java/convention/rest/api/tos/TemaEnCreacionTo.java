package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import convention.persistent.TemaDeReunion;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;
import net.sf.kfgodel.bean2bean.annotations.CopyTo;

/**
 * Created by fede on 26/06/17.
 */

@JsonIgnoreProperties("usuarioActual")
public class TemaEnCreacionTo {
    @CopyTo(TemaDeReunion.duracion_FIELD)
    private String duracion;

    @CopyTo(TemaDeReunion.autor_FIELD)
    private Long idDeAutor;


    @CopyTo(TemaDeReunion.reunion_FIELD)
    private Long idDeReunion;

    @CopyTo(TemaDeReunion.titulo_FIELD)
    private String titulo;

    @CopyTo(TemaDeReunion.descripcion_FIELD)
    private String descripcion;

    @CopyTo(TemaDeReunion.obligatoriedad_FIELD)
    private String obligatoriedad;

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Long getIdDeAutor() {
        return idDeAutor;
    }

    public void setIdDeAutor(Long idDeAutor) {
        this.idDeAutor = idDeAutor;
    }

    public Long getIdDeReunion() {
        return idDeReunion;
    }

    public void setIdDeReunion(Long idDeReunion) {
        this.idDeReunion = idDeReunion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObligatoriedad() {
        return obligatoriedad;
    }

    public void setObligatoriedad(String unaObligatoriedad) {
        this.obligatoriedad = unaObligatoriedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
