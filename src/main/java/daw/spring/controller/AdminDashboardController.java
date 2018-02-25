package daw.spring.controller;

import daw.spring.component.CurrentUserInfo;
import daw.spring.model.Product;
import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import daw.spring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
@RequestMapping("/adminDashboard")
public class AdminDashboardController implements CurrentUserInfo{

    private final UserService userService;
    private final DeviceService deviceService;
    private final HomeService homeService;
    private final ProductService productService;
    private final OrderService orderService;


    private final Logger log = LoggerFactory.getLogger("AdminDashbpard");

    @Autowired
    public AdminDashboardController(UserService userService, DeviceService deviceService, HomeService homeService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.homeService = homeService;
        this.productService = productService;
        this.orderService = orderService;
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

    //@GetMapping(value ="/inventory/{{id}}/document.getElementById('numberInput{{id}}').value", method = RequestMethod.POST)
    //@GetMapping(value ="/inventory, method = RequestMethod.POST)
    //public String modStock(Model model, Principal principal, @RequestParam("id") long id, @RequestParam("numberInput{{id}}") long stock,) {
    //public String modStock(Model model, Principal principal, @PathVariable long id, @PathVariable long stock,HttpServletResponse response) {
        //Update stock   @RequestParam("file") MultipartFile photo
     //   productService.updateStockProduct(id,stock);
    //    model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
     //   model.addAttribute("product", productService.findAllProducts());
    //    return "redirect:inventory";
    //}

    //@ResponseBody

    //@RequestMapping(value ="/inventory/{id}", method = RequestMethod.POST)
    //public String modStock(Model model  , Principal principal, @PathVariable long id, @RequestParam(value="numberInput{id}") long stock){
    //    stock=40;
    //    productService.updateStockProduct(id,stock);
    //    model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
    //    model.addAttribute("product", productService.findAllProducts());
     //   return "redirect:inventory";
    //}  HttpServletResponse response

    //@RequestMapping( value ="/inventory/{id}", method = RequestMethod.POST)
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

    @RequestMapping(value="/moreUsers", method = RequestMethod.GET)
    public String moreUsuarios(Model model, @RequestParam int page) {
        log.warn("Page:"+page);
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
        model.addAttribute("orders", orderService.homesOrders());

        return "adminDashboard/orders";
    }


    @RequestMapping("/detail")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("userSesion", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "adminDashboard/detail";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public String saveProfile(Model model, @RequestParam("file") MultipartFile photo, Principal principal) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if (!photo.isEmpty()) {
            //Path directorioRecusrsos=Paths.get("file");

            Path directorioRecusrsos = Paths.get("src//main//resources//static//upload");
            String rootPath = directorioRecusrsos.toFile().getAbsolutePath();

            try {
                byte[] bytes = photo.getBytes();
                Path rutaCompleta = Paths.get(rootPath + "//" + photo.getOriginalFilename());
                Files.write(rutaCompleta, bytes);
                //flash.addAttribute("info", "Ha subido correctamente '"+ foto.getOriginalFilename()+"'");
                user.setPhoto(photo.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.saveUser(user);
        //model.addAttribute("titulo", "Perfil");
        //status.setComplete();
        return "dashboard/created";
    }
}
