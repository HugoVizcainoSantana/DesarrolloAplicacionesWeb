package daw.spring.controller;

import daw.spring.service.DeviceService;
import daw.spring.service.ProductService;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

@RequestMapping("/adminDashboard")
public class AdminDashboardController {

    @Autowired private UserService userService;
    @Autowired private DeviceService deviceService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("user", userService.findOneById((long)1));
        return "adminDashboard/index";
    }


    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }


    @RequestMapping("/inventario")
    public String inventario(Model model) {
        model.addAttribute("user", userService.findOneById((long)1));
        model.addAttribute("device", deviceService.findAllDevices());
        return "adminDashboard/inventario";
    }


    @RequestMapping("/usuarios")
    public String usuarios(Model model, @RequestParam(required = false) String name ) {
        model.addAttribute("user", userService.findOneById((long)1));
        if (name!=null && !name.isEmpty()) {
            model.addAttribute("listUser", userService.findAllUsersByFirstName(name));
        }else{
            model.addAttribute("listUser", userService.findAll());
        }
        return "adminDashboard/usuarios";
    }


    @RequestMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("user", userService.findOneById((long)1));
        return "adminDashboard/pedidos";
    }


    @RequestMapping("/detail")
    public String detail(Model model) {
        model.addAttribute("user", userService.findOneById((long)1));
        return "adminDashboard/detail";
    }
}
