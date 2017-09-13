package Persistence;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.HibernateOrm;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.HibernateFacade;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.application.initializers.InicializadorDeDatos;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.impl.B2BTransformer;
import ar.com.kfgodel.transformbyconvention.impl.config.TransformerConfigurationByConvention;
import ar.com.kfgodel.webbyconvention.api.WebServer;
import ar.com.kfgodel.webbyconvention.api.config.WebServerConfiguration;
import ar.com.kfgodel.webbyconvention.impl.JettyWebServer;
import ar.com.kfgodel.webbyconvention.impl.config.ConfigurationByConvention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sandro on 19/06/17.
 */
public class TestApplication implements Application{
    public static Logger LOG = LoggerFactory.getLogger(TemasApplication.class);

    private TemasConfiguration config;
    private DependencyInjector injector;
    @Override
    public WebServer getWebServerModule() {
        return this.injector.getImplementationFor(WebServer.class).get();
    }

    @Override
    public HibernateOrm getOrmModule() {
        return this.injector.getImplementationFor(HibernateOrm.class).get();
    }

    @Override
    public TypeTransformer getTransformerModule() {
        return this.injector.getImplementationFor(TypeTransformer.class).get();
    }

    @Override
    public TemasConfiguration getConfiguration() {
        return config;
    }

    @Override
    public DependencyInjector getInjector() {
        return injector;
    }


    @Override
    public void start() {
        this.initialize();
    }

    @Override
    public void stop() {
        this.getOrmModule().close();
    }

    public static Application create(TemasConfiguration config) {
        TestApplication application = new TestApplication();
        application.config = config;
        return application;
    }

    protected TypeTransformer createTransformer() {
        TransformerConfigurationByConvention configuration = TransformerConfigurationByConvention.create(this.getInjector());
        return B2BTransformer.create(configuration);
    }

    private void registerCleanupHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Cleaning-up before shutdown");
            this.stop();
        }, "cleanup-thread"));
    }

    protected HibernateOrm createPersistenceLayer() {
        DbCoordinates dbCoordinates = config.getDatabaseCoordinates();
        HibernateOrm hibernateOrm = HibernateFacade.createWithConventionsFor(dbCoordinates);
        return hibernateOrm;
    }

    protected WebServer createWebServer() {
        WebServerConfiguration serverConfig = ConfigurationByConvention.create()
                .authenticatingWith(config.autenticador())
                .listeningHttpOn(config.getHttpPort())
                .withInjections((binder) -> {
                    //Make application the only jetty injectable dependency
                    binder.bind(this).to(Application.class);
                })
                .withSecuredRootPaths("/api/v1")
                .redirectingAfterSuccessfulAuthenticationTo("/")
                .redirectingAfterFailedAuthenticationTo("/login?failed=true");
        return JettyWebServer.createFor(serverConfig);
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
