package daw.spring.controller;

import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/adminDashboard")

public class AdminDashboardController {

    @Autowired private UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("user", userService.findOneById(1l));
        return "adminDashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }

    @RequestMapping("/inventario")
    public String inventario() {
        return "adminDashboard/inventario";
    }


    @RequestMapping("/usuarios")
    public String usuarios() {
        return "adminDashboard/usuarios";
    }

    @RequestMapping("/pedidos")
    public String pedidos() {
        return "adminDashboard/pedidos";
    }


}
