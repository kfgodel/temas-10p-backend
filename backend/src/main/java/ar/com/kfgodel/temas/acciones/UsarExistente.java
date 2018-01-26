package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import convention.persistent.Reunion;

/**
 * Esta clase representa la operacion de transaccion que devuelve el objeto indicado
 * Created by kfgodel on 08/10/16.
 */
public class UsarExistente implements TransactionOperation<Reunion> {
  private Reunion reunion;

  public static TransactionOperation<Reunion> create(Reunion reunion) {
    UsarExistente accion = new UsarExistente();
    accion.reunion = reunion;
    return accion;
  }

  @Override
  public Reunion applyWithTransactionOn(TransactionContext transactionContext) {
    return reunion;
  }

}
