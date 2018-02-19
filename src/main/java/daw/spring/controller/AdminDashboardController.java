package daw.spring.controller;

import daw.spring.service.DeviceService;
import daw.spring.service.ProductService;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

@RequestMapping("/adminDashboard")
public class AdminDashboardController {

    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Autowired private DeviceService deviceService;

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
    public String inventario(Model model) {
        model.addAttribute("user", userService.findOneById(1l));
        model.addAttribute("device", deviceService.findAllDevices());
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


    @RequestMapping("/detail")
    public String detail() {
        return "adminDashboard/detail";
    }
}
