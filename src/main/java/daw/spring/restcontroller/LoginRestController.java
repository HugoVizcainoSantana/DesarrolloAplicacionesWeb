package daw.spring.restcontroller;

import daw.spring.component.UserComponent;
import daw.spring.model.Roles;
import daw.spring.model.User;
import daw.spring.service.UserService;
import daw.spring.utilities.ApiRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@ApiRestController
public class LoginRestController {

    private final UserComponent userComponent;
    private final UserService userService;

    @Autowired
    public LoginRestController(UserComponent userComponent, UserService userService) {
        this.userComponent = userComponent;
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Map<Boolean, User>> login() {

        Map<Boolean, User> map = new HashMap<>();
        map.put(true, userComponent.getLoggedUser());

        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<Boolean, User>> register(@RequestBody User user) {

        Map<Boolean, User> map = new HashMap<>();

        if (userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPasswordHash(), user.getHomeList(), user.getPhone(),
                    user.getNotificationList(), user.getPhoto(), user.getOrderList(), Roles.USER.getRoleName());

            userService.saveUser(newUser);
            map.put(true, newUser);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/registerAdmin", method = RequestMethod.POST)
    public ResponseEntity<Map<Boolean, User>> registerAdmin(@RequestBody User user) {

        Map<Boolean, User> map = new HashMap<>();

        if (userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPasswordHash(), user.getHomeList(), user.getPhone(),
                    user.getNotificationList(), user.getPhoto(), user.getOrderList(), Roles.ADMIN.getRoleName());

            userService.saveUser(newUser);
            map.put(true, newUser);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}