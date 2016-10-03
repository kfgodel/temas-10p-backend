package convention.rest.api.tos;

import ar.com.kfgodel.transformbyconvention.api.tos.EnumToSupport;
import convention.persistent.EstadoDeProyecto;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;

/**
 * Created by kfgodel on 21/08/16.
 */
public class EstadoEnComboTo extends EnumToSupport {

  @CopyFrom(EstadoDeProyecto.nombre_FIELD)
  private String nombre;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}
