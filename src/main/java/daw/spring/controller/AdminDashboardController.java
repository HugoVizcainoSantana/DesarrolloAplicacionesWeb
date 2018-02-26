package daw.spring.controller;

import daw.spring.component.CurrentUserInfo;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.model.OrderRequest;
import daw.spring.model.User;
import daw.spring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/adminDashboard")
public class AdminDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final DeviceService deviceService;
    private final HomeService homeService;
    private final ProductService productService;
    private final OrderRequestService orderRequestService;


    private final Logger log = LoggerFactory.getLogger("AdminDashbpard");

    @Autowired
    public AdminDashboardController(UserService userService, DeviceService deviceService, HomeService homeService, ProductService productService, OrderRequestService orderRequestService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.homeService = homeService;
        this.productService = productService;
        this.orderRequestService = orderRequestService;
    }

    @RequestMapping("/")
    public String index(Model model , Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "adminDashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model, Principal principal) {
        index(model, principal);
    }

    @RequestMapping("/inventory")
    public String inventario(Model model  , Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        return "adminDashboard/inventory";
    }


    @RequestMapping( value ="/inventory", method = RequestMethod.POST)
    public String modStock(Model model, @RequestParam("id") long id ,  @RequestParam("numberStock") long stock ,  @RequestParam("numberCost") double cost ,Principal principal){
        productService.updateStockProduct(id,stock,cost);
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        return "adminDashboard/inventory";
    }

    @RequestMapping("/users")
    public String users(Model model, @RequestParam(required = false) String name, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("userCount", userService.countAllUsers());
        model.addAttribute("homeActives", homeService.countHomeActives());
        if (name != null && !name.isEmpty()) {
            model.addAttribute("listUser", userService.findAllUsersByFirstName(name));
        } else {
            model.addAttribute("listUser", userService.findAll());
        }

        Page<User> users = userService.findAll(new PageRequest(0, 4));
        model.addAttribute("users", users);

        return "adminDashboard/users";
    }

    @RequestMapping("/users/{id}")
    public String usersDetail(Model model, @PathVariable long id){
        model.addAttribute("userDetail", userService.findOneById(id));

        return "adminDashboard/userDetail";
    }

    @RequestMapping(value="/moreUsers", method = RequestMethod.GET)
    public String moreUsuarios(Model model, @RequestParam int page) {
        Page<User> userList = userService.findAll(new PageRequest(page, 4));
        model.addAttribute("items", userList);
        return "listItemsPage";
    }

    /*@RequestMapping(value = "/moreUsers/?page=1&size=4", method = RequestMethod.GET)
    public Page<User> moreUsersPage(Pageable page){
        return userRepository.findAll(page);
    }*/

    @RequestMapping("/orders")
    public String orders(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("orders",orderRequestService.homesOrders());
        return "adminDashboard/orders";
    }

    @RequestMapping("/detail/{id}")
    public String confirmOrder(Model model, Principal principal, @PathVariable long id){

        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        OrderRequest orderDt = orderRequestService.finOneById(id);
        orderRequestService.confirmOrder(id);
        model.addAttribute("orderDetail", orderDt);
        Home homeOrder=orderDt.getHome();
        User homeUser=userService.findUserByHomeId(homeOrder);
        model.addAttribute("userHome", homeUser);
        return "adminDashboard/detail";
    }

    @RequestMapping("/orders/{id}")
    public String orderDetail(Model model, Principal principal, @PathVariable long id){
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        OrderRequest orderDt = orderRequestService.finOneById(id);
        model.addAttribute("orderDetail", orderDt);
        Home homeOrder=orderDt.getHome();
        User homeUser=userService.findUserByHomeId(homeOrder);
        model.addAttribute("userHome", homeUser);
        return "adminDashboard/detail";
    }


    @RequestMapping(value="/orders/{orderId}/{deviceId}", params = "activate")
    public String confirmDevice(Model model, Principal principal, @PathVariable long orderId,  @PathVariable long deviceId,  @RequestParam(required = false) String serialNumberInput){
        deviceService.activeOneDevice(deviceId, serialNumberInput);
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("orders", orderRequestService.homesOrders());
        return "redirect:/adminDashboard/orders";
    }

    @RequestMapping(value="/orders/{orderId}/{deviceId}", params = "cancel")
    public String cancelDevice(Model model, Principal principal, @PathVariable long orderId,  @PathVariable long deviceId,  @RequestParam(required = false) String serialNumberInput){
        Device deviceCancel = deviceService.findOneById(deviceId);  //Scan device
        OrderRequest orderDt = orderRequestService.finOneById(orderId);  //Delete device from order
        List<Device> deviceList = orderDt.getDeviceList();
        deviceList.remove(deviceCancel);
        Home homeDt = homeService.findOneById(orderDt.getHome().getId());  //Delete device from home
        List<Device> deviceList2 = homeDt.getDeviceList();
        deviceList2.remove(deviceCancel);
        deviceService.cancelOneDevice(deviceId);  //Delete device
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("orders", orderRequestService.homesOrders());
        return "adminDashboard/orders";
    }

}
