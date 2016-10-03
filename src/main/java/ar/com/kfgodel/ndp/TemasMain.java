package ar.com.kfgodel.ndp;

import ar.com.kfgodel.ndp.application.NdpApplication;
import ar.com.kfgodel.ndp.config.ConfigurationSelector;
import ar.com.kfgodel.ndp.config.HerokuPriorityConfigSelector;
import ar.com.kfgodel.ndp.config.NdpConfiguration;

/**
 * Punto de entrada del proceso java
 * <p>
 * Created by kfgodel on 21/08/16.
 */
public class TemasMain {

  public static void main(String[] args) {
    // Configuration depends on environment variables to detect if we are at heroku hosting
    ConfigurationSelector selector = HerokuPriorityConfigSelector.create();
    NdpConfiguration applicationConfig = selector.selectConfig();

    // Then proceed normally (heroku will connect to postgres, development uses local db)
    NdpApplication.create(applicationConfig).start();
  }

}
