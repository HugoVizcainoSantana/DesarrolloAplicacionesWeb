package daw.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    private Logger log = LoggerFactory.getLogger("LoginController");

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /*@RequestMapping(value = "/login",method = RequestMethod.POST)
    public String tryLogin(/*@RequestBody User user) {
        /*log.error(user.getEmail());
        log.error(user.getPasswordHash());
        log.error(user.getFirstName());
        return "login";
    }*/

    @RequestMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }
}
