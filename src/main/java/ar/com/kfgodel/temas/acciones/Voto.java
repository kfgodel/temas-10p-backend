package ar.com.kfgodel.temas.acciones;

import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;

import javax.persistence.Entity;

/**
 * Esta clase representa el voto de un usuario a un tema de una reunion
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Voto {

  private Usuario votante;
  public static final String votante_FIELD = "votante";


  private TemaDeReunion tema;
  public static final String tema_FIELD = "tema";


  public Usuario getVotante() {
    return votante;
  }

  public void setVotante(Usuario votante) {
    this.votante = votante;
  }

  public TemaDeReunion getTema() {
    return tema;
  }

  public void setTema(TemaDeReunion tema) {
    this.tema = tema;
  }

  public void sumar() {
    this.getTema().agregarInteresado(this.getVotante());
  }

  public void restar() {
    this.getTema().quitarInteresado(this.getVotante());
  }
}
