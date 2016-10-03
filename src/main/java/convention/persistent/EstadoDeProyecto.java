package convention.persistent;

import ar.com.kfgodel.nary.api.Nary;

/**
 * Created by kfgodel on 21/08/16.
 */
public enum EstadoDeProyecto {
  PRESUPUESTADO("Presupuestado"),
  EN_DESARROLLO("En desarrollo"),
  TERMINADO("Terminado"),
  CANCELADO("Cancelado");

  private final String nombre;
  public static final String nombre_FIELD = "nombre";

  EstadoDeProyecto(String nombre) {
    this.nombre = nombre;
  }

  public static Nary<EstadoDeProyecto> getAll() {
    return Nary.create(values());
  }

  public String getNombre() {
    return nombre;
  }
}
