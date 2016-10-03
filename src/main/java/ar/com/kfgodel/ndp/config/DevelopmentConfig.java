package ar.com.kfgodel.ndp.config;

import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;

/**
 * This type represents the configuration for a development environment
 * <p>
 * Created by kfgodel on 12/03/16.
 */
public class DevelopmentConfig implements NdpConfiguration {

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
}
