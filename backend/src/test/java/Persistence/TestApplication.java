package Persistence;

import ar.com.kfgodel.orm.api.HibernateOrm;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.application.initializers.InicializadorDeDatos;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.webbyconvention.api.WebServer;

/**
 * Created by sandro on 19/06/17.
 */
public class TestApplication extends TemasApplication{

    public static Application create(TemasConfiguration config) {
        TestApplication application = new TestApplication();
        application.config = config;
        return application;
    }

    @Override
    public void start() {
        this.initialize();
    }

    @Override
    public void stop() {
        this.getOrmModule().close();
    }

    private void initialize() {
        this.injector = config.getInjector();
        this.injector.bindTo(Application.class, this);
        this.injector.bindTo(WebServer.class, createWebServer());
        this.injector.bindTo(HibernateOrm.class, createPersistenceLayer());
        this.injector.bindTo(TypeTransformer.class, createTransformer());

        registerCleanupHook();

        InicializadorDeDatos.create(this).inicializar();
    }
}
