package convention.rest.api.tos;

import ar.com.kfgodel.temas.acciones.Voto;
import net.sf.kfgodel.bean2bean.annotations.CopyTo;

/**
 * Esta clase es el to de un voto (puede sumar o restar)
 * Created by kfgodel on 09/10/16.
 */
public class VotoTo {

  @CopyTo(Voto.votante_FIELD)
  private Long idDeVotante;

  @CopyTo(Voto.tema_FIELD)
  private Long idDeTema;

  public Long getIdDeVotante() {
    return idDeVotante;
  }

  public void setIdDeVotante(Long idDeVotante) {
    this.idDeVotante = idDeVotante;
  }

  public Long getIdDeTema() {
    return idDeTema;
  }

  public void setIdDeTema(Long idDeTema) {
    this.idDeTema = idDeTema;
  }
}
