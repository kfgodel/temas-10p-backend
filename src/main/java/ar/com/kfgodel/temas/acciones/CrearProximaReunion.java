package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Reunion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * Esta clase representa el creador de reunion de proxima roots, que sabe calcular la fecha de la proxima
 * Created by kfgodel on 08/10/16.
 */
public class CrearProximaReunion implements TransactionOperation<Reunion> {

  @Override
  public Reunion applyWithTransactionOn(TransactionContext transactionContext) {
    LocalDate fechaDeProximaRoots = calcularFechaDeRoots();
    Reunion proximaRoots = Reunion.create(fechaDeProximaRoots);
    // La guardamos antes de devolverla para que quede persistida
    Save.create(proximaRoots).applyWithTransactionOn(transactionContext);
    return proximaRoots;
  }

  private LocalDate calcularFechaDeRoots() {
    LocalDate hoy = LocalDate.now();
    LocalDate tercerViernesDeEsteMes = hoy.with(tercerViernesDelMes());
    if (tercerViernesDeEsteMes.isBefore(hoy)) {
      return calcularTercerViernesDelProximoMes(hoy);
    }
    return tercerViernesDeEsteMes;
  }

  private LocalDate calcularTercerViernesDelProximoMes(LocalDate hoy) {
    LocalDate primerDiaDelProximoMes = hoy.with(TemporalAdjusters.firstDayOfMonth());
    LocalDate tercerViernesDelProximoMes = primerDiaDelProximoMes.with(tercerViernesDelMes());
    return tercerViernesDelProximoMes;
  }

  private TemporalAdjuster tercerViernesDelMes() {
    return TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.FRIDAY);
  }

  public static CrearProximaReunion create() {
    CrearProximaReunion accion = new CrearProximaReunion();
    return accion;
  }

}
