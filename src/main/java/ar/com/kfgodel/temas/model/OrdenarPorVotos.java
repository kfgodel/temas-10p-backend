package ar.com.kfgodel.temas.model;

import convention.persistent.TemaDeReunion;

import java.util.Comparator;

/**
 * Ordena los temas segun la cantidad de votos que tiene. Primero la que tiene mas votos.
 * Si tienen la misma cantidad de votos ordena por fecha de creacion (primero el mas viejo)
 * <p>
 * Created by kfgodel on 10/10/16.
 */
public class OrdenarPorVotos implements Comparator<TemaDeReunion> {

  @Override
  public int compare(TemaDeReunion o1, TemaDeReunion o2) {
    int diferenciaDeVotos = o2.getCantidadDeVotos() - o1.getCantidadDeVotos();
    if (diferenciaDeVotos == 0) {
      // Misma cantidad de votos, ordenamos por creacion
      return o1.getMomentoDeCreacion().compareTo(o2.getMomentoDeCreacion());
    }
    return diferenciaDeVotos;
  }

  public static OrdenarPorVotos create() {
    OrdenarPorVotos ordenarPorVotos = new OrdenarPorVotos();
    return ordenarPorVotos;
  }

}
