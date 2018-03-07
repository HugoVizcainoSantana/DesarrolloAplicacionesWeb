package daw.spring.restcontroller;

import daw.spring.component.UserComponent;
import daw.spring.model.User;
import daw.spring.utilities.ApiRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@ApiRestController
public class LoginRestController {

    // TODO add interfaces for represent the api here and @JsonView (using interfaces) on attributes

    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);
    private final UserComponent userComponent;

    @Autowired
    public LoginRestController(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<User> login() {

        if (!userComponent.isLoggedUser()) {
            log.info("NO LOGGED :(");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            User loggedUser = userComponent.getLoggedUser();
            log.info("LOGGED AS " + loggedUser.getEmail());
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);
        }
    }

    @RequestMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpSession session) {

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.invalidate();
            log.info("LOGOUT DONE");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

    }

}