package daw.spring.controller;

import daw.spring.model.User;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;

    @Autowired
    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("titulo", "Dashboard");
        return "dashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        model.addAttribute("titulo", "Dashboard");
        index(model);
    }

    @RequestMapping("/tienda")
    public String shop(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("titulo", "Tienda");
        return "dashboard/tienda";
    }

    @RequestMapping(value = "/tienda", method = RequestMethod.POST)
    public String save(@Valid User user, BindingResult result, Model model, SessionStatus status) {
        User userResult = new User();
        if (result.hasErrors()) {
            model.addAttribute("errorName", "Nombre requerido");
            return "dashboard/created";

        }
        userService.saveUser(userResult);
        status.setComplete();
        return "redirect:dashboard/created";
    }

    @RequestMapping("/charts")
    public String charts(Model model) {
        model.addAttribute("titulo", "Consumos");
        return "dashboard/charts";
    }

    @RequestMapping("/home-detail")
    public String homeDetail(Model model) {
        model.addAttribute("titulo", "Casa");
        return "dashboard/home-detail";
    }

    @RequestMapping("/homes")
    public String homes(Model model) {
        model.addAttribute("titulo", "Casa");
        return "dashboard/homes";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {
        User user = new User();
        model.addAttribute("titulo", "Perfil");
        model.addAttribute("nameProfile", "Juan");

        return "dashboard/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(@Valid User user, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, SessionStatus status) {
        if (result.hasErrors()) {

            return "dashboard/profile";

        }
        if (!photo.isEmpty()) {
            Path directorioRecusrsos = Paths.get("src//main//resources//static//upload");
            String rootPath = directorioRecusrsos.toFile().getAbsolutePath();

            try {
                byte[] bytes = photo.getBytes();
                Path rutaCompleta = Paths.get(rootPath + "//" + photo.getOriginalFilename());
                Files.write(rutaCompleta, bytes);
                //flash.addAttribute("info", "Ha subido correctamente '"+ foto.getOriginalFilename()+"'");
                user.setFoto(photo.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.saveUser(user);
        //model.addAttribute("titulo", "Perfil");
        //status.setComplete();
        return "dashboard/profile";
    }

    @RequestMapping("/terms-Conditions")
    public String termsConditions(Model model) {
        model.addAttribute("titulo", "Condiciones");
        return "dashboard/terms-Conditions";
    }

    @RequestMapping("/created")
    public String created(Model model) {
        model.addAttribute("titulo", "Nueva compra");
        return "dashboard/created";
    }

}
