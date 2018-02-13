package daw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {

    @RequestMapping("/index")
    public String index() {
        return "dashboard/index";
    }
}
