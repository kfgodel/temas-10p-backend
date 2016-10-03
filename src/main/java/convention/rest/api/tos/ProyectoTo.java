package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Proyecto;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

/**
 * This type represents a user for the frontend
 * Created by kfgodel on 03/03/15.
 */
public class ProyectoTo extends PersistableToSupport {

  @CopyFromAndTo(Proyecto.nombreDescriptivo_FIELD)
  private String nombreDescriptivo;

  @CopyFromAndTo(Proyecto.elemento_FIELD)
  private ElementoEnComboTo elemento;

  @CopyFromAndTo(Proyecto.estado_FIELD)
  private EstadoEnComboTo estado;

  @CopyFrom(Proyecto.momentoDeCreacion_FIELD)
  private String creation;

  @CopyFrom(Proyecto.momentoDeUltimaModificacion_FIELD)
  private String modification;

  public String getNombreDescriptivo() {
    return nombreDescriptivo;
  }

  public void setNombreDescriptivo(String nombreDescriptivo) {
    this.nombreDescriptivo = nombreDescriptivo;
  }

  public ElementoEnComboTo getElemento() {
    return elemento;
  }

  public void setElemento(ElementoEnComboTo elemento) {
    this.elemento = elemento;
  }

  public EstadoEnComboTo getEstado() {
    return estado;
  }

  public void setEstado(EstadoEnComboTo estado) {
    this.estado = estado;
  }

  public String getCreation() {
    return creation;
  }

  public void setCreation(String creation) {
    this.creation = creation;
  }

  public String getModification() {
    return modification;
  }

  public void setModification(String modification) {
    this.modification = modification;
  }


}
