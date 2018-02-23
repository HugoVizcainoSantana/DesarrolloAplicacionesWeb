package daw.spring.controller;

import daw.spring.component.CurrentUserInfo;
import daw.spring.model.User;
import daw.spring.service.HomeService;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final HomeService homeService;

    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService) {
        this.userService = userService;
        this.homeService = homeService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("titulo", "Dashboard");
        return "dashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model, Authentication user) {
        model.addAttribute("titulo", "Dashboard");
        index(model);
    }

    @RequestMapping("/tienda")
    public String shop(Model model) {
        model.addAttribute("user", userService.findOneById(1l));
        return "dashboard/tienda";
    }

    @RequestMapping(value = "/tienda", method = RequestMethod.POST)
    public String saveShop(@Valid User user, BindingResult result, Model model, SessionStatus status) {
        User userResult = new User();
        if (result.hasErrors()) {
            //model.addAttribute("errorName", "Nombre requerido");
            return "dashboard/created";
        }
        userService.saveUser(user);
        //status.setComplete();
        return "dashboard/created";
    }

    @RequestMapping("/charts")
    public String charts(Model model) {
        model.addAttribute("titulo", "Consumos");
        return "dashboard/charts";
    }

    @RequestMapping("/homes")
    public String homes(Model model, Principal principal) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("homeList", userService.findOneById(getIdFromPrincipalName(principal.getName())).getHomeList());
        model.addAttribute("homeList.deviceNum", 5);
        return "dashboard/homes";
    }

    @RequestMapping("/homes/{id}")
    public String homeDetail(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("home", homeService.findOneById(id));
        return "dashboard/home-detail";
    }

    @RequestMapping("/homes/{id}/generateInvoice")
    public String generateInvoice(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("homeList", userService.findOneById(getIdFromPrincipalName(principal.getName())).getHomeList());
        return "dashboard/home-detail";
    }

    @RequestMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
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

    @RequestMapping("/terms-Conditions")
    public String termsConditions(Model model) {
        model.addAttribute("titulo", "Condiciones");
        return "dashboard/terms-Conditions";
    }

    @RequestMapping("/created")
    public String created(Model model) {
        model.addAttribute("user", userService.findOneById(1l));
        return "dashboard/created";
    }

}
