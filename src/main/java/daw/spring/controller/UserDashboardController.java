package daw.spring.controller;

import daw.spring.model.User;
import daw.spring.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {
	
	
    private final UserService userService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserDashboardController(UserService userService) {
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
        model.addAttribute("user", userService.findOneById(1l));
        return "dashboard/tienda";
    }

    @RequestMapping(value = "/tienda", method = RequestMethod.POST)
    public String saveShop(@Valid User user, BindingResult result, Model model, SessionStatus status) {
        //User userResult = new User();
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
    
    @GetMapping(value="/see/{id}")
    public String seeDetail(@PathVariable(value="id") Long id, Map<String, Object> model) {
    		User user = userService.findOneById(id);
    		
    		if (user==null) {
    			return "dashboard/profile";
    		}
    		model.put("user", user);
    		model.put("title", "Detalle cliente"+ user.getFirstName());
    	
    		return"dashboard/see";
    	
    }
    
    @GetMapping(value="/upload/{filename:.+}")
    public ResponseEntity<Resource> seePhoto (@PathVariable String fileName){
    		Path pathPhoto = Paths.get("upload").resolve(fileName).toAbsolutePath();
    		log.info("Pathphoto: "+pathPhoto);
    		Resource resource = null;
    		try {
    				resource = new UrlResource(pathPhoto.toUri());
    				if(!resource.exists() || !resource.isReadable()) {
    					throw new RuntimeException("Error no se ha podido cargar la imgen: "+ pathPhoto.toString());
    				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION	, "attachment; filename=\""+resource.getFilename()+"\"").body(resource);
    	
    }

    @RequestMapping("/profile")
    public String profile(Model model) {
        User user = new User();
        model.addAttribute("user", userService.findOneById(2l));

        return "dashboard/profile";
    }
    

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(@Valid User user, BindingResult result, Model model, @RequestParam("file") MultipartFile photo) {
    		/*if (result.hasErrors()) {
    			return "dashboard/profile";
        }*/
        if (!photo.isEmpty()) {

        		String uniqueFilname = UUID.randomUUID().toString()+"_"+photo.getOriginalFilename();
        	
        		Path rootPath= Paths.get("upload").resolve(uniqueFilname);
        		Path rootAbsolutePath = rootPath.toAbsolutePath();
        		log.info("rootPath: "+ rootPath);
        		log.info("rootAbsolutePath: "+ rootAbsolutePath);

            try {
                Files.copy(photo.getInputStream(), rootAbsolutePath);
               
                user.setPhoto(uniqueFilname);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.saveUser(user);
        return "dashboard/see";
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
