package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;
import net.sf.kfgodel.bean2bean.annotations.MissingPropertyAction;

/**
 * Esta clase representa el TO de una tema para ser editado
 * Created by kfgodel on 08/10/16.
 */
public class TemaTo extends PersistableToSupport {

  @CopyFromAndTo(TemaDeReunion.reunion_FIELD)
  private Long idDeReunion;

  @CopyFromAndTo(TemaDeReunion.autor_FIELD)
  private Long idDeAutor;

  @CopyFrom(value = TemaDeReunion.autor_FIELD + "." + Usuario.name_FIELD, whenMissing = MissingPropertyAction.TREAT_AS_NULL)
  private String autor;

  @CopyFromAndTo(TemaDeReunion.titulo_FIELD)
  private String titulo;

  @CopyFromAndTo(TemaDeReunion.descripcion_FIELD)
  private String descripcion;

  private Integer cantidadVotosTotales;
  private Integer cantidadVotosPropios;

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

  public Integer getCantidadVotosTotales() {
    return cantidadVotosTotales;
  }

  public void setCantidadVotosTotales(Integer cantidadVotosTotales) {
    this.cantidadVotosTotales = cantidadVotosTotales;
  }

  public Integer getCantidadVotosPropios() {
    return cantidadVotosPropios;
  }

  public void setCantidadVotosPropios(Integer cantidadVotosPropios) {
    this.cantidadVotosPropios = cantidadVotosPropios;
  }

  public Long getIdDeReunion() {
    return idDeReunion;
  }

  public void setIdDeReunion(Long idDeReunion) {
    this.idDeReunion = idDeReunion;
  }

  public Long getIdDeAutor() {
    return idDeAutor;
  }

  public void setIdDeAutor(Long idDeAutor) {
    this.idDeAutor = idDeAutor;
  }
}
