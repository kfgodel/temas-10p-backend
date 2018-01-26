package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;

import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the configuration data for the application
 * Created by kfgodel on 12/03/16.
 */
public interface TemasConfiguration {
    DependencyInjector injector=DependencyInjectorImpl.create();
  /**
   * @return The database information to connect to it
   */
  DbCoordinates getDatabaseCoordinates();

  /**
   * @return The port number to use for the web server
   */
  int getHttpPort();

  Function<WebCredential,Optional<Object>> autenticador();

  default DependencyInjector getInjector(){

    return injector;
  }

  default Void inicializarDatos(){
    return null;
  }
}
