package daw.spring.restcontroller;

import daw.spring.component.UserComponent;
import daw.spring.model.User;
import daw.spring.service.ProductService;
import daw.spring.utilities.ApiRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@ApiRestController
public class LoginRestController {

    // TODO add interfaces for represent the api here and @JsonView (using interfaces) on attributes

    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);
    private final UserComponent userComponent;
    private final ProductService productService;

    @Autowired
    public LoginRestController(UserComponent userComponent, ProductService productService) {
        this.userComponent = userComponent;
        this.productService = productService;
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
    public ResponseEntity<Map<Boolean, User>> logout(HttpSession session) {

        User loggedUser = userComponent.getLoggedUser();
        Map<Boolean, User> map = new HashMap<>();
        map.put(true, loggedUser);

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.invalidate();
            log.info("LOGOUT DONE");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

}