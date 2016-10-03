package ar.com.kfgodel.ndp.filters.elementos;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import convention.persistent.Elemento;
import convention.persistent.QElemento;
import convention.persistent.QProyecto;

import java.util.List;

/**
 * Filtra todos los elementos que no fueron usados en proyectos
 * Created by kfgodel on 21/08/16.
 */
public class ElementosDisponibles implements SessionOperation<Nary<Elemento>> {

  public static ElementosDisponibles create() {
    ElementosDisponibles filter = new ElementosDisponibles();
    return filter;
  }

  @Override
  public Nary<Elemento> applyWithSessionOn(SessionContext sessionContext) {
    HibernateQueryFactory queryFactory = new HibernateQueryFactory(sessionContext.getSession());

    QElemento elemento = QElemento.elemento;
    QProyecto proyecto = QProyecto.proyecto;

    List<Elemento> elementos = queryFactory.selectFrom(elemento)
      .where(elemento.notIn(
        new JPAQuery<>().select(proyecto.elemento)
          .from(proyecto)
      ))
      .orderBy(elemento.numeroAtomico.asc())
      .fetch();
    return Nary.create(elementos);
  }

}
