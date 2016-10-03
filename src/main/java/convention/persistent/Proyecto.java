package convention.persistent;

import javax.persistence.*;

/**
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Proyecto extends PersistableSupport {

  @OneToOne
  private Elemento elemento;
  public static final String elemento_FIELD = "elemento";

  @Column(length = 1024)
  private String nombreDescriptivo;
  public static final String nombreDescriptivo_FIELD = "nombreDescriptivo";

  @Enumerated(EnumType.STRING)
  private EstadoDeProyecto estado;
  public static final String estado_FIELD = "estado";

  public Elemento getElemento() {
    return elemento;
  }

  public void setElemento(Elemento elemento) {
    this.elemento = elemento;
  }

  public String getNombreDescriptivo() {
    return nombreDescriptivo;
  }

  public void setNombreDescriptivo(String nombreDescriptivo) {
    this.nombreDescriptivo = nombreDescriptivo;
  }

  public EstadoDeProyecto getEstado() {
    return estado;
  }

  public void setEstado(EstadoDeProyecto estado) {
    this.estado = estado;
  }

  public static Proyecto create() {
    Proyecto proyecto = new Proyecto();
    return proyecto;
  }

}
