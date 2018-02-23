package daw.spring.controller;

import daw.spring.component.CurrentUserInfo;
import daw.spring.model.User;
import daw.spring.service.HomeService;
import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

//import daw.spring.component.CurrentUserInfo;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final HomeService homeService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService) {
        this.userService = userService;
        this.homeService = homeService;
    }

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("titulo", "Dashboard");
        return "dashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("titulo", "Dashboard");
        index(model, principal);
    }

    @RequestMapping("/tienda")
    public String shop(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("title", "Tienda");
        return "dashboard/tienda";
    }

    @RequestMapping(value = "/tienda", method = RequestMethod.POST)
    public String saveShop(BindingResult result, Model model, SessionStatus status) {
		/*
		if (result.hasErrors()) {
			// model.addAttribute("errorName", "Nombre requerido");
			return "dashboard/created";

		}
		userService.saveUser(user);
		// status.setComplete();
		*/
        return "dashboard/created";
    }

    @RequestMapping("/charts")
    public String charts(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("titulo", "Consumos");
        return "dashboard/charts";
    }

    @RequestMapping("/homes")
    public String homes(Model model, Principal principal) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("homeList", userService.findOneById(getIdFromPrincipalName(principal.getName())).getHomeList());
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/homes";
    }

    @RequestMapping("/homes/{id}")
    public String homeDetail(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("home", homeService.findOneById(id));
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/home-detail";
    }

    @RequestMapping("/homes/{id}/generateInvoice")
    public String generateInvoice(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("titulo", "Casa");
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
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
            String uniqueFilname = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path rootPath = Paths.get("upload").resolve(uniqueFilname);
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutePath: " + rootAbsolutePath);

            try {
                Files.copy(photo.getInputStream(), rootAbsolutePath);

                user.setPhoto(uniqueFilname);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.saveUser(user);
        return "redirect:profile";
    }

    @RequestMapping("/terms-Conditions")
    public String termsConditions(Model model) {
        model.addAttribute("titulo", "Condiciones");
        return "dashboard/terms-Conditions";
    }

    @RequestMapping("/created")
    public String created(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/created";
    }

	/*
    @GetMapping(value = "/upload/{filename:.+}")
    public ResponseEntity<Resource> seePhoto(@PathVariable String fileName) {
        Path pathPhoto = Paths.get("upload").resolve(fileName).toAbsolutePath();
        log.info("Pathphoto: " + pathPhoto);
        Resource resource = null;
        try {
            resource = new UrlResource(pathPhoto.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Error no se ha podido cargar la imgen: " + pathPhoto.toString());
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assert resource != null;
        return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }
    */

}
