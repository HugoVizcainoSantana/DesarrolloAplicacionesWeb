package daw.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserAuthProvider userRepoAuthProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**");

        // URLs that need authentication to access to it
        // TODO Add more api request here to handle security
        // http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/register").permitAll();
        // all delete methods only can be done by admin users
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/**/**").hasAnyRole("ADMIN");

        // Let http in basic mode for api purposes
        http.httpBasic();

        // Do not redirect when logout
        http.logout().logoutSuccessHandler((rq, rs, a) -> {
        });

        // CSRF need to be disabled
        http.csrf().disable();
    }
}
