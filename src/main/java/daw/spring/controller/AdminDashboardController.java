package daw.spring.controller;

import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import daw.spring.service.DeviceService;
import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

@RequestMapping("/adminDashboard")
public class AdminDashboardController {

    private final UserService userService;
    private final DeviceService deviceService;

    @Autowired
    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger("AdminDashbpard");

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

    @RequestMapping("/inventario")
    public String inventario(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        model.addAttribute("device", deviceService.findAllDevices());
        return "adminDashboard/inventario";
    }

    @RequestMapping("/usuarios")
    public String users(Model model, @RequestParam(required = false) String name) {
        model.addAttribute("user", userService.findOneById((long) 1));
        Page<User> users = userService.findAll(new PageRequest(0, 4));
        model.addAttribute("user", users);

        return "adminDashboard/usuarios";
    }

    @RequestMapping(value="/moreUsers", method = RequestMethod.GET)
    public String moreUsuarios(Model model, @RequestParam int page) {
        log.warn("Page:"+page);
        Page<User> userList = userRepository.findAll(new PageRequest(page, 4));
        model.addAttribute("items", userList);

        return "listItemsPage";
    }

    /*@RequestMapping(value = "/moreUsers/?page=1&size=4", method = RequestMethod.GET)
    public Page<User> moreUsersPage(Pageable page){
        return userRepository.findAll(page);
    }*/

    @RequestMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        return "adminDashboard/pedidos";
    }


    @RequestMapping("/detail")
    public String detail(Model model) {
        model.addAttribute("user", userService.findOneById((long) 1));
        return "adminDashboard/detail";
    }
}
