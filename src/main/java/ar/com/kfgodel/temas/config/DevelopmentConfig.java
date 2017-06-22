package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import ar.com.kfgodel.temas.filters.users.UserCount;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import convention.persistent.Usuario;
import convention.rest.api.UserResource;
import convention.rest.api.tos.BackofficeUserTo;
import convention.rest.api.tos.UserTo;

import ar.com.kfgodel.temas.filters.users.FindAllUsersOrderedByName;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the configuration for a development environment
 * <p>
 * Created by kfgodel on 12/03/16.
 */
public class DevelopmentConfig implements TemasConfiguration {


  public static DevelopmentConfig create() {
    DevelopmentConfig config = new DevelopmentConfig();
    return config;
  }

  @Override
  public DbCoordinates getDatabaseCoordinates() {
    return ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:file:./db/h2", "sa", "");
  }

  @Override
  public int getHttpPort() {
    return 9090;
  }

  @Override
  public Function<WebCredential,Optional<Object>> autenticador(){
    return (webCredential -> Optional.of(usuarioDeMentira()));
  }
  @Override
  public Void inicializarDatos(){
    if(getUsers().isEmpty()) {
      Usuario unUser = Usuario.create("feche", "fecheromero", "123", "sarlnga");
      ApplicationOperation.createFor(getInjector())
              .insideATransaction()
              .taking(unUser)
              .convertingTo(Usuario.class)
              .applyingResultOf(Save::create)
              .convertTo(UserTo.class);

      Usuario unUser2 = Usuario.create("sandro", "unSandro", "123", "sarlonga");
      ApplicationOperation.createFor(getInjector())
              .insideATransaction()
              .taking(unUser2)
              .convertingTo(Usuario.class)
              .applyingResultOf(Save::create)
              .convertTo(UserTo.class);
    }
    return null;
  }
  private Long usuarioDeMentira() {
    List<UserTo> listOfUserTo= getUsers();
    return listOfUserTo.stream().filter(user -> user.getId() == 1).findFirst().get().getId();
  }

  private List<UserTo> getUsers() {
    return ApplicationOperation.createFor(getInjector()).
            insideASession().
            applying(FindAllUsersOrderedByName.create())
            .convertTo(new ReferenceOf<List<UserTo>>() {
            }.getReferencedType());
  }
}

