package ApiRest;

import helpers.TestConfig;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;

/**
 * Created by fede on 22/06/17.
 */
public class JettyIdentityAdapterTest extends JettyIdentityAdapter {

    @Override
    public <T> T getApplicationIdentification(){
        return (T) TestConfig.create().autenticador().apply(null).get();
    }
}
