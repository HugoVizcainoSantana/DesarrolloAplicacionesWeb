package daw.spring.controller;

import daw.spring.service.DeviceService;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

@RequestMapping("/adminDashboard")
public class AdminDashboardController {

    private final UserService userService;
    private final DeviceService deviceService;

    @Autowired
    public AdminDashboardController(UserService userService, DeviceService deviceService) {
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        return "adminDashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }

    @RequestMapping("/inventory")
    public String inventory(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        model.addAttribute("device", deviceService.findAllDevices());
        return "adminDashboard/inventory";
    }


    @RequestMapping("/users")
    public String users(Model model, @RequestParam(required = false) String name) {
        model.addAttribute("user", userService.findOneById((long) 1));
        if (name != null && !name.isEmpty()) {
            model.addAttribute("listUser", userService.findAllUsersByFirstName(name));
        } else {
            model.addAttribute("listUser", userService.findAll());
        }
        return "adminDashboard/users";
    }

    @RequestMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        return "adminDashboard/orders";
    }


    @RequestMapping("/detail")
    public String detail(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        return "adminDashboard/detail";
    }
}
