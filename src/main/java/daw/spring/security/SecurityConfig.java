package daw.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthProvider authenticationProvider;

    @Autowired
    public SecurityConfig(UserAuthProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Api config
        http.authorizeRequests().antMatchers("/api/**").permitAll();
        // Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/login?error").permitAll();
        http.authorizeRequests().antMatchers("/logout").permitAll();
        // Only development, for use of h2 Console
        http.authorizeRequests().antMatchers("/h2").permitAll();
        http.headers().frameOptions().sameOrigin();
        //Resources (CSS & JS & Images)
        http.authorizeRequests().antMatchers("/publicBootstrap/**").permitAll();
        http.authorizeRequests().antMatchers("/images/**").permitAll();
        http.authorizeRequests().antMatchers("/uploaded/**").permitAll();
        http.authorizeRequests().antMatchers("/dashboardBootstrap/**").authenticated();
        http.authorizeRequests().antMatchers("/uploaded/users/**").authenticated();
        // Private pages
        http.authorizeRequests().antMatchers("/login/redirect").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/dashboard/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/adminDashboard/**").hasAnyRole("ADMIN");
        // Login
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("email");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/login/redirect");
        http.formLogin().failureUrl("/login?error");
        // Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");

        // Disable CSRF at the moment
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // Database authentication provider
        auth.authenticationProvider(authenticationProvider);
    }
}
