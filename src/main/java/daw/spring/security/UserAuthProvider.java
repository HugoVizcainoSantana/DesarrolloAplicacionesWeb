package daw.spring.security;

import daw.spring.component.UserComponent;
import daw.spring.model.User;
import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final UserComponent userComponent;
    private Logger log = LoggerFactory.getLogger("UserAuthProvider");

    @Autowired
    public UserAuthProvider(UserService userService, BCryptPasswordEncoder encoder, UserComponent userComponent) {
        this.userService = userService;
        this.encoder = encoder;
        this.userComponent = userComponent;
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        User user = userService.findOneUserByEmail(auth.getName());
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        String password = (String) auth.getCredentials();
        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong password");
        }
        // added user logged to user component to check api
        userComponent.setLoggedUser(user);

        log.info("Succesful login from " + user.getEmail());
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return new UsernamePasswordAuthenticationToken(user.getId(), password, roles);

    }

    @Override
    public boolean supports(Class<?> authenticationObject) {
        return true;
    }
}

