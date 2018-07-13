package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Reunion;

import java.time.LocalDate;

/**
 * Esta clase representa el creador de reunion de proxima roots, que sabe calcular la fecha de la proxima
 * Created by kfgodel on 08/10/16.
 */
public class CrearProximaReunion implements TransactionOperation<Reunion> {

  @Override
  public Reunion applyWithTransactionOn(TransactionContext transactionContext) {
    LocalDate fechaDeProximaRoots = new CalculadorDeProximaFecha().calcularFechaDeRoots(LocalDate.now());
    Reunion proximaRoots = Reunion.create(fechaDeProximaRoots);
    // La guardamos antes de devolverla para que quede persistida
    Save.create(proximaRoots).applyWithTransactionOn(transactionContext);
    return proximaRoots;
  }


  public static CrearProximaReunion create() {
    CrearProximaReunion accion = new CrearProximaReunion();
    return accion;
  }

}
