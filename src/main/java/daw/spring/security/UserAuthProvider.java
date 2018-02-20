package daw.spring.security;

import daw.spring.model.Roles;
import daw.spring.model.User;
import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthProvider implements AuthenticationProvider {

    private Logger log = LoggerFactory.getLogger("UserAuthProvider");

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserAuthProvider(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        User user = userService.findOneUserByEmail(auth.getName());
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        String password = (String) auth.getCredentials();
        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong password");
        }
        log.info("Succesful login from " + user.getEmail());
        List<GrantedAuthority> roles = new ArrayList<>();
        for (Roles role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.name()));
        }
        return new UsernamePasswordAuthenticationToken(user.getFirstName(), password, roles);
        
    }

    @Override
    public boolean supports(Class<?> authenticationObject) {
        return true;
    }
}

