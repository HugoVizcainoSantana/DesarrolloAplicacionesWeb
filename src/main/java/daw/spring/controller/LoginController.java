package daw.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private Logger log = LoggerFactory.getLogger("LoginController");

    @RequestMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @RequestMapping("/login/redirect")
    public String login(HttpServletRequest request) {
        log.error("Redirecting");
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/chooseDashboard";
        } else if (request.isUserInRole("USER")) {
            return "redirect:/dashboard/";
        } else
            return null;
    }

    @RequestMapping("/chooseDashboard")
    public String chooseDashboard() {
        return "chooseDashboard";
    }

   /* @RequestMapping("/forgotPasword")
    public String forgotPassword() {
        return "forgotPasword";
    }*/
}
