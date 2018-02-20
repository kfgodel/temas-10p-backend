package ar.com.kfgodel.temas.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.QUsuario;
import convention.persistent.Usuario;

/**
 * This type represents a filter that looks for a single user with given login and password
 * Created by kfgodel on 04/04/15.
 */
public class UserByCredentials implements SessionOperation<Nary<Usuario>> {

  private WebCredential credentials;

  public static UserByCredentials create(WebCredential credentials) {
    UserByCredentials filter = new UserByCredentials();
    filter.credentials = credentials;
    return filter;
  }

  @Override
  public Nary<Usuario> applyWithSessionOn(SessionContext sessionContext) {
    QUsuario usuario = QUsuario.usuario;

    try {
      Usuario foundUsuario = new HibernateQueryFactory(sessionContext.getSession())
        .selectFrom(usuario)
        .where(usuario.login.eq(credentials.getUsername())
          .and(usuario.password.eq(credentials.getPassword())))
        .fetchOne();
      return Nary.ofNullable(foundUsuario);
    } catch (NonUniqueResultException e) {
      throw new IllegalStateException("There's more than one user with same credentials? " + credentials);
    }
  }
}
