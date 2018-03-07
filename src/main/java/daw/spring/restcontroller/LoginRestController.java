package daw.spring.restcontroller;

import daw.spring.component.UserComponent;
import daw.spring.model.User;
import daw.spring.utilities.ApiRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@ApiRestController
public class LoginRestController {

    private final UserComponent userComponent;

    @Autowired
    public LoginRestController(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<Map<Boolean, User>> login() {

        Map<Boolean, User> map = new HashMap<>();
        map.put(true, userComponent.getLoggedUser());

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @RequestMapping("/logout")
    public ResponseEntity<Map<Boolean, User>> logout(HttpSession session) {

        Map<Boolean, User> map = new HashMap<>();
        map.put(true, userComponent.getLoggedUser());

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.invalidate();
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

}