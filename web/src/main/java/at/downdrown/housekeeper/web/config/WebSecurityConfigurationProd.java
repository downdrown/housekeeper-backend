package at.downdrown.housekeeper.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Web-Security related configuration bean for production.
 *
 * @author Manfred Huber
 */
@Profile("prod")
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurationProd extends WebSecurityConfigurerAdapter {

    private final HttpFirewall httpFirewall;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfigurationProd(
        final HttpFirewall httpFirewall,
        final PasswordEncoder passwordEncoder,
        final UserDetailsService userDetailsService) {
        this.httpFirewall = httpFirewall;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(httpFirewall);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
        .and()
            .httpBasic();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }
}
