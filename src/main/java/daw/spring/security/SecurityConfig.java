package daw.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserAuthProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/login?error").permitAll();
        http.authorizeRequests().antMatchers("/logout").permitAll();
        // Only development, for use of h2 Console
        http.authorizeRequests().antMatchers("/h2").permitAll();
        http.headers().frameOptions().sameOrigin();
        //Resources (CSS & JS & Images)
        http.authorizeRequests().antMatchers("/dashboardBootstrap/**").permitAll();
        http.authorizeRequests().antMatchers("/publicBootstrap/**").permitAll();
        http.authorizeRequests().antMatchers("/images/**").permitAll();
        // Private pages
        http.authorizeRequests().antMatchers("/dashboard").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/adminDashboard").hasAnyRole("ADMIN");
        // Login
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("email");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/dashboard/");
        http.formLogin().failureUrl("/login?error");
        // Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");

        // Disable CSRF at the moment
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Database authentication provider
        auth.authenticationProvider(authenticationProvider);
        //auth.inMemoryAuthentication()
        //        .withUser("amador@merengue.com").password("1234").roles("USER");
         //   .withUser(."1234".roles("USER"))
    }
}
