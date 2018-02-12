package daw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/adminDashboard")

public class AdminDashboardController {
    @RequestMapping("/index")
    public String index() {
        return "adminDashboard/index";
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
