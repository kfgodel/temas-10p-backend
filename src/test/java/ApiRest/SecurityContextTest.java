package ApiRest;

import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by fede on 22/06/17.
 */
public class SecurityContextTest implements SecurityContext {
    @Override
    public Principal getUserPrincipal() {
        return new JettyIdentityAdapterTest();
    }

    @Override
    public boolean isUserInRole(String s) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
