package daw.spring.restcontroller;

import daw.spring.component.UserComponent;
import daw.spring.model.Roles;
import daw.spring.model.User;
import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class LoginRestController {

    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

    private final UserComponent userComponent;
    private final UserService userService;

    @Autowired
    public LoginRestController(UserComponent userComponent, UserService userService) {
        this.userComponent = userComponent;
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<User> login() {

        User user = userComponent.getLoggedUser();

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            log.info("Login done!");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity logout(HttpSession session) {

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.invalidate();
            log.info("Logout done!");
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@RequestBody User user) {

        if (userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPasswordHash(), user.getHomeList(), user.getPhone(),
                    user.getNotificationList(), user.getPhoto(), user.getOrderList(), Roles.USER.getRoleName());

            userService.saveUser(newUser);
            log.info("Register done for user: " + newUser.getFirstName());

            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}