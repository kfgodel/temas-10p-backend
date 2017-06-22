package Persistence;

import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import ar.com.kfgodel.temas.config.DevelopmentConfig;

/**
 * Created by sandro on 22/06/17.
 */
public class TestConfig extends DevelopmentConfig {

    @Override
    public DbCoordinates getDatabaseCoordinates() {
        return ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:mem:file:./db/testDB", "sa", "");
    }

    public static TestConfig create() {
        TestConfig config = new TestConfig();
        return config;
    }

}
