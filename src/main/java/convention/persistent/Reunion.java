package convention.persistent;

import ar.com.kfgodel.temas.model.OrdenarPorVotos;

import javax.persistence.*;
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

  @Enumerated(EnumType.STRING)
  private StatusDeReunion status;
  public static final String status_FIELD = "status";


  @OneToMany(cascade = CascadeType.ALL, mappedBy = TemaDeReunion.reunion_FIELD)
  @OrderBy(TemaDeReunion.prioridad_FIELD)
  private List<TemaDeReunion> temasPropuestos;
  public static final String temasPropuestos_FIELD = "temasPropuestos";

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

  public StatusDeReunion getStatus() {
    return status;
  }

  public void setStatus(StatusDeReunion status) {
    this.status = status;
  }

  public void setTemasPropuestos(List<TemaDeReunion> temasPropuestos) {
    getTemasPropuestos().clear();
    getTemasPropuestos().addAll(temasPropuestos);
  }

  public static Reunion create(LocalDate fecha) {
    Reunion reunion = new Reunion();
    reunion.fecha = fecha;
    reunion.status = StatusDeReunion.PENDIENTE;
    return reunion;
  }

  public void cerrarVotacion() {
    this.getTemasPropuestos().sort(OrdenarPorVotos.create());
    for (int i = 0; i < getTemasPropuestos().size(); i++) {
      TemaDeReunion tema = getTemasPropuestos().get(i);
      tema.setPrioridad(i + 1); // Queremos que empiece de 1 la prioridad
    }
    this.status = StatusDeReunion.CERRADA;
  }

  public void reabrirVotacion() {
    this.status = StatusDeReunion.PENDIENTE;
  }
}
