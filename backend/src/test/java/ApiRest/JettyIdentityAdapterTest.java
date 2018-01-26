package ApiRest;

import helpers.TestConfig;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;

import java.security.Principal;

/**
 * Created by fede on 22/06/17.
 */
public class JettyIdentityAdapterTest extends JettyIdentityAdapter {
    protected Long principal;
    public JettyIdentityAdapterTest(Long principal){
        this.principal=principal;
    }

    @Override
    public <T> T getApplicationIdentification(){
        return (T) principal;
    }
}
