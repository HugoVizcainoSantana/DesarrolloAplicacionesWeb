package daw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("test", "testAttribute");
        return "index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }

}
