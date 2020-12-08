package at.downdrown.housekeeper.web.config;

import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.service.GrantedAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Web-Security related configuration bean for development.
 * Disables CORS and CSRF for easier development.
 *
 * @author Manfred Huber
 */
@Profile("dev")
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurationDev extends WebSecurityConfigurerAdapter {

    private final GrantedAuthorityService grantedAuthorityService;
    private final HttpFirewall httpFirewall;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfigurationDev(
        final GrantedAuthorityService grantedAuthorityService,
        final HttpFirewall httpFirewall,
        final PasswordEncoder passwordEncoder) {
        this.grantedAuthorityService = grantedAuthorityService;
        this.httpFirewall = httpFirewall;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(httpFirewall);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
        .and()
            .httpBasic();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .passwordEncoder(passwordEncoder)
            .withUser(new User("admin", passwordEncoder.encode("password"), grantedAuthorityService.calculateAuthoritiesForRole(Role.ADMIN)))
            .withUser(new User("user", passwordEncoder.encode("password"), grantedAuthorityService.calculateAuthoritiesForRole(Role.USER)))
            .withUser(new User("guest", passwordEncoder.encode("password"), grantedAuthorityService.calculateAuthoritiesForRole(Role.GUEST)));
    }
}
