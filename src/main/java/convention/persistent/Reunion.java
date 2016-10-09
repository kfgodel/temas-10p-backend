package convention.persistent;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa una reunion de roots con el temario a realizar
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Reunion extends PersistableSupport {

  @NotNull
  private LocalDate fecha;
  public static final String fecha_FIELD = "fecha";

  @OneToMany(cascade = CascadeType.ALL)
  private List<TemaDeReunion> temasPropuestos;
  public static final String temasPropuestos_FIELD = "temasPropuestos";

  @OneToMany(cascade = CascadeType.ALL)
  private List<Voto> votos;

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public List<TemaDeReunion> getTemasPropuestos() {
    if (temasPropuestos == null) {
      temasPropuestos = new ArrayList<>();
    }
    return temasPropuestos;
  }

  public void setTemasPropuestos(List<TemaDeReunion> temasPropuestos) {
    getTemasPropuestos().clear();
    getTemasPropuestos().addAll(temasPropuestos);
  }

  public List<Voto> getVotos() {
    if (votos == null) {
      votos = new ArrayList<>();
    }
    return votos;
  }

  public void setVotos(List<Voto> votos) {
    this.votos = votos;
  }

  public static Reunion create(LocalDate fecha) {
    Reunion reunion = new Reunion();
    reunion.fecha = fecha;
    return reunion;
  }

}
