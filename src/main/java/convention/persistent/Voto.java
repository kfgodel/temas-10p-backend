package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Esta clase representa el voto de un usuario a un tema de una reunion
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Voto extends PersistableSupport {

  @ManyToOne
  private Usuario votante;

  @ManyToOne
  private TemaDeReunion temaVotado;

  public Usuario getVotante() {
    return votante;
  }

  public void setVotante(Usuario votante) {
    this.votante = votante;
  }

  public TemaDeReunion getTemaVotado() {
    return temaVotado;
  }

  public void setTemaVotado(TemaDeReunion temaVotado) {
    this.temaVotado = temaVotado;
  }
}
