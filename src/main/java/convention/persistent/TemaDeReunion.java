package convention.persistent;

import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta clase representa uno de los temas a tratar en un reunion de roots
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class TemaDeReunion extends Tema {

  @ManyToOne
  private Reunion reunion;
  public static final String reunion_FIELD = "reunion";

  private Integer prioridad;
  public static final String prioridad_FIELD = "prioridad";

  @Fetch(org.hibernate.annotations.FetchMode.SELECT)
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Usuario> interesados;
  public static final String interesados_FIELD = "interesados";

  @Enumerated(EnumType.STRING)
  private ObligatoriedadDeReunion obligatoriedad;
  public static final String obligatoriedad_FIELD = "obligatoriedad";

  public ObligatoriedadDeReunion getObligatoriedad(){
    return obligatoriedad;
  }

  public void setObligatoriedad(ObligatoriedadDeReunion unaObligatoriedad){
    this.obligatoriedad = unaObligatoriedad;
  }

  public Reunion getReunion() {
    return reunion;
  }

  public void setReunion(Reunion reunion) {
    this.reunion = reunion;
  }

  public Integer getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(Integer prioridad) {
    this.prioridad = prioridad;
  }

  public List<Usuario> getInteresados() {
    if (interesados == null) {
      interesados = new ArrayList<>();
    }
    return interesados;
  }

  public void setInteresados(List<Usuario> interesados) {
    getInteresados().clear();
    if (interesados != null) {
      getInteresados().addAll(interesados);
    }
  }

  public void agregarInteresado(Usuario votante) throws Exception {
    if(obligatoriedad != ObligatoriedadDeReunion.OBLIGATORIO)
      this.getInteresados().add(votante);
    else
      throw new Exception(mensajeDeErrorAlAgregarInteresado());
  }

  public static String mensajeDeErrorAlAgregarInteresado() {
    return "No se puede agregar un interesado a un tema obligatorio";
  }

  public void quitarInteresado(Usuario votante) {
    this.getInteresados().remove(votante);
  }

  public int getCantidadDeVotos() {
    return getInteresados().size();
  }


  public TemaDeReunion copy(){
    TemaDeReunion copia = new TemaDeReunion();
    copia.setInteresados(this.getInteresados());
    copia.setPersistenceVersion(this.getPersistenceVersion());
    copia.setMomentoDeUltimaModificacion(this.getMomentoDeUltimaModificacion());
    copia.setMomentoDeCreacion(this.getMomentoDeCreacion());
    copia.setId(this.getId());
    copia.setTitulo(this.getTitulo());
    copia.setDescripcion(this.getDescripcion());
    copia.setReunion(this.getReunion());
    copia.setPrioridad(this.getPrioridad());
    copia.setAutor(this.getAutor());
    copia.setDuracion(this.getDuracion());
    copia.setObligatoriedad(this.getObligatoriedad());
    return copia;
  }


    public boolean tieneMayorPrioridadQue(TemaDeReunion otroTema) {
      Integer prioridad = getObligatoriedad().prioridad();
      Integer otraPrioridad = otroTema.getObligatoriedad().prioridad();

      Integer cantidadDeVotos = this.getCantidadDeVotos();
      Integer otraCantidadDeVotos = otroTema.getCantidadDeVotos();

      if(prioridad.equals(otraPrioridad)
              && getObligatoriedad().equals(ObligatoriedadDeReunion.NO_OBLIGATORIO)
              && cantidadDeVotos != otraCantidadDeVotos)
        return cantidadDeVotos > otraCantidadDeVotos;

      if(prioridad.equals(otraPrioridad))
        return otroTema.seCreoDespuesDe(this);

      return prioridad < otraPrioridad;
    }

  protected boolean seCreoDespuesDe(TemaDeReunion otroTema) {
      return this.getMomentoDeCreacion().isAfter(otroTema.getMomentoDeCreacion());
  }

  public void ocultarVotosPara(Long userId) {
      this.setInteresados(this.getInteresados()
              .stream()
              .filter(usuario -> usuario.getId().equals(userId))
              .collect(Collectors.toList()));
  }

  public boolean puedeSerVotado() {
    return obligatoriedad.permiteRecibirVotos();
  }
}
