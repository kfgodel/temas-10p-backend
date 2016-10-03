package ar.com.kfgodel.ndp.config;

import ar.com.kfgodel.orm.api.config.DbCoordinates;

/**
 * This type represents the configuration data for the application
 * Created by kfgodel on 12/03/16.
 */
public interface NdpConfiguration {

  /**
   * @return The database information to connect to it
   */
  DbCoordinates getDatabaseCoordinates();

  /**
   * @return The port number to use for the web server
   */
  int getHttpPort();
}
