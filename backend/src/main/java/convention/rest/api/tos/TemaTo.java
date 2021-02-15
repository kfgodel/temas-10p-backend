package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;
import net.sf.kfgodel.bean2bean.annotations.MissingPropertyAction;

import java.util.List;

/**
 * Esta clase representa el TO de una tema para ser editado
 * Created by kfgodel on 08/10/16.
 */
@JsonIgnoreProperties({"usuarioActual"})
public class TemaTo extends PersistableToSupport {
  @CopyFromAndTo(TemaDeReunion.duracion_FIELD)
  private String duracion;

  @CopyFromAndTo(TemaDeReunion.autor_FIELD)
  private Long idDeAutor;

  @CopyFrom(value= TemaDeReunion.ultimoModificador_FIELD + "." +Usuario.name_FIELD, whenMissing = MissingPropertyAction.TREAT_AS_NULL)
  private String ultimoModificador;

  @CopyFromAndTo(TemaDeReunion.reunion_FIELD)
  private Long idDeReunion;

  @CopyFrom(value = TemaDeReunion.autor_FIELD + "." + Usuario.name_FIELD, whenMissing = MissingPropertyAction.TREAT_AS_NULL)
  private String autor;

  @CopyFromAndTo(TemaDeReunion.prioridad_FIELD)
  private Integer prioridad;

  @CopyFromAndTo(TemaDeReunion.titulo_FIELD)
  private String titulo;

  @CopyFromAndTo(TemaDeReunion.descripcion_FIELD)
  private String descripcion;

  @CopyFromAndTo(TemaDeReunion.interesados_FIELD)
  private List<Long> idsDeInteresados;

  @CopyFromAndTo(TemaDeReunion.obligatoriedad_FIELD)
  private String obligatoriedad;

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }


  public Long getIdDeAutor() {
    return idDeAutor;
  }

  public void setIdDeAutor(Long idDeAutor) {
    this.idDeAutor = idDeAutor;
  }

  public Integer getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(Integer prioridad) {
    this.prioridad = prioridad;
  }



  public String getDuracion() {
    return duracion;
  }

  // TODO wtf?
  public void String (String duracion) {
    this.duracion = duracion;
  }

  public Long getIdDeReunion() {
    return idDeReunion;
  }

  public void setIdDeReunion(Long idDeReunion) {
    this.idDeReunion = idDeReunion;
  }

  public List<Long> getIdsDeInteresados() {
    return idsDeInteresados;
  }

  public void setIdsDeInteresados(List<Long> idsDeInteresados) {
    this.idsDeInteresados = idsDeInteresados;
  }

  public String getObligatoriedad() {
    return obligatoriedad;
  }

  public void setObligatoriedad(String unaObligatoriedad) {
    this.obligatoriedad = unaObligatoriedad;
  }

  public String getUltimoModificador() {
    return ultimoModificador;
  }

  public void setUltimoModificador(String idDeUltimoModificador) {
    this.ultimoModificador = idDeUltimoModificador;
  }

}
