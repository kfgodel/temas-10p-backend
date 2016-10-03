package ar.com.kfgodel.ndp.filters.proyectos;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.Proyecto;
import convention.persistent.QProyecto;

import java.util.List;

/**
 * Created by kfgodel on 21/08/16.
 */
public class FindAllProyectosNewestFirst implements SessionOperation<Nary<Proyecto>> {

  @Override
  public Nary<Proyecto> applyWithSessionOn(SessionContext sessionContext) {
    HibernateQueryFactory queryFactory = new HibernateQueryFactory(sessionContext.getSession());

    QProyecto proyecto = QProyecto.proyecto;

    List<Proyecto> elementos = queryFactory.selectFrom(proyecto)
      .orderBy(proyecto.momentoDeCreacion.desc())
      .fetch();
    return Nary.create(elementos);
  }

  public static FindAllProyectosNewestFirst create() {
    FindAllProyectosNewestFirst findAllProyectosNewestFirst = new FindAllProyectosNewestFirst();
    return findAllProyectosNewestFirst;
  }

}
