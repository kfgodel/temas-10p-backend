package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;

/**
 * Esta clase representa el TO de una tema para ser editado
 * Created by kfgodel on 08/10/16.
 */
public class TemaTo extends PersistableToSupport {
  private String autor;
  private String titulo;
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
}
