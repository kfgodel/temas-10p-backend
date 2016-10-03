package convention.persistent;

import javax.persistence.Entity;

/**
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Elemento extends PersistableSupport {

  private Integer numeroAtomico;

  private String simbolo;

  private String nombre;
  public static final String nombre_FIELD = "nombre";

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Integer getNumeroAtomico() {
    return numeroAtomico;
  }

  public void setNumeroAtomico(Integer numeroAtomico) {
    this.numeroAtomico = numeroAtomico;
  }

  public String getSimbolo() {
    return simbolo;
  }

  public void setSimbolo(String simbolo) {
    this.simbolo = simbolo;
  }


  public static final String nombreDeCombo_GETTER = "nombreDeCombo";

  public String getNombreDeCombo() {
    return this.numeroAtomico + " - " + this.nombre;
  }

  public static Elemento create(int numero, String simbolo, String nombre) {
    Elemento elemento = new Elemento();
    elemento.nombre = nombre;
    elemento.numeroAtomico = numero;
    elemento.simbolo = simbolo;
    return elemento;
  }

}
