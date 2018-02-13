package daw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {

    @RequestMapping("/")
    public String index(Model model) {
        return "dashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }
}
