package at.downdrown.housekeeper.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Web-Security related configuration bean for unit-tests.
 * This is required because some security related settings have to
 * be disabled in order to be able to execute the controller tests.
 *
 * Disables CORS and CSRF.
 *
 * @author Manfred Huber
 */
@TestConfiguration
@EnableWebSecurity
public class WebSecurityConfigurationTest extends WebSecurityConfigurerAdapter {

    private final HttpFirewall httpFirewall;

    @Autowired
    public WebSecurityConfigurationTest(HttpFirewall httpFirewall) {
        this.httpFirewall = httpFirewall;
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(httpFirewall);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable();
    }
}
