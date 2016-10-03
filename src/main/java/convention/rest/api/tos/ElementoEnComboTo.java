package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Elemento;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;

/**
 * Created by kfgodel on 21/08/16.
 */
public class ElementoEnComboTo extends PersistableToSupport {

  @CopyFrom(Elemento.nombreDeCombo_GETTER)
  private String nombre;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}
