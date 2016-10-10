package ar.com.kfgodel.temas.model;

import convention.persistent.TemaDeReunion;

import java.util.Comparator;

/**
 * Ordena los temas segun la cantidad de votos que tiene. Primero la que tiene mas votos
 * <p>
 * Created by kfgodel on 10/10/16.
 */
public class OrdenarPorVotos implements Comparator<TemaDeReunion> {

  @Override
  public int compare(TemaDeReunion o1, TemaDeReunion o2) {
    return o2.getCantidadDeVotos() - o1.getCantidadDeVotos();
  }

  public static OrdenarPorVotos create() {
    OrdenarPorVotos ordenarPorVotos = new OrdenarPorVotos();
    return ordenarPorVotos;
  }

}
