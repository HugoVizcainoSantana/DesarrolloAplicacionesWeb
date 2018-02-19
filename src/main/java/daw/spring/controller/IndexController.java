package daw.spring.controller;

import daw.spring.model.User;

import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
public class IndexController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        log.info("Root path");
        model.addAttribute("test", "testAttribute");
        return "index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        log.info("Routing to root path");
        index(model);
    }

    @PostConstruct
    public void init(){

        User user1 = new User("Amador","Rivas","amador@merengue.com","1234",null, "USER");
        userService.save(user1);
        User user2 = new User ("Teodoro","Rivas","teodoro@merengue.com","1234",null, "USER");
        userService.save(user2);
    }

}
