package ar.com.kfgodel.temas.filters;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.Minuta;
import convention.persistent.QMinuta;
import convention.persistent.QReunion;
import convention.persistent.Reunion;

import java.time.LocalDate;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaDeReunion implements TransactionOperation<Nary<Minuta>> {

    private static Long reunionId;

    @Override
    public Nary<Minuta> applyWithTransactionOn(TransactionContext transactionContext) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(transactionContext.getSession());

        QMinuta minuta = QMinuta.minuta;

        Minuta minutaDeReunion = queryFactory.selectFrom(minuta)
                .where(minuta.reunion.id.eq(reunionId))
                .fetchOne();
        return Nary.ofNullable(minutaDeReunion);
    }

    public static MinutaDeReunion create(Long reunionId){
            MinutaDeReunion minutaDeReunion=new MinutaDeReunion();
        minutaDeReunion.reunionId = reunionId;
        return minutaDeReunion;
    }
}
