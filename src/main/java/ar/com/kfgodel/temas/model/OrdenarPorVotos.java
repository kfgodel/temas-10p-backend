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
  public int compare(TemaDeReunion tema1, TemaDeReunion tema2) {
    if(tema1.tieneMayorPrioridadQue(tema2))
      return 1;
    else
      return -1;
  }

  public static OrdenarPorVotos create() {
    OrdenarPorVotos ordenarPorVotos = new OrdenarPorVotos();
    return ordenarPorVotos;
  }

}
