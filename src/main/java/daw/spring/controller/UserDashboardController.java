package daw.spring.controller;

import daw.spring.component.CurrentUserInfo;
//import daw.spring.component.CurrentUserInfo;
import daw.spring.model.User;
import daw.spring.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

	private final UserService userService;
	//private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "upload");

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	public UserDashboardController(UserService userService) {
		this.userService = userService;
	}

	/*@PostConstruct
	public void init() throws IOException {

		if (!Files.exists(FILES_FOLDER)) {
			Files.createDirectories(FILES_FOLDER);
		}
	}*/

	@RequestMapping("/")
	public String index(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("titulo", "Dashboard");
		return "dashboard/index";
	}

	@RequestMapping("/index")
	public void index2(Model model, Authentication user, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("titulo", "Dashboard");
		index(model, principal);
	}

	@RequestMapping("/shop")
	public String shop(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("title", "Tienda");
		return "dashboard/shop";
	}

	@RequestMapping(value = "/shop", method = RequestMethod.POST)
	public String saveShop(@Valid User user, BindingResult result, Model model, SessionStatus status) {
		User userResult = new User();
		if (result.hasErrors()) {
			// model.addAttribute("errorName", "Nombre requerido");
			return "dashboard/created";

		}
		userService.saveUser(user);
		// status.setComplete();
		return "dashboard/created";
	}

	@RequestMapping("/charts")
	public String charts(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("titulo", "Consumos");
		return "dashboard/charts";
	}

	@RequestMapping("/home-detail")
	public String homeDetail(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("titulo", "Casa");
		return "dashboard/home-detail";
	}

	@RequestMapping("/homes")
	public String homes(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("titulo", "Casas");
		return "dashboard/homes";
	}

	@GetMapping(value = "/see/{id}")
	public String seeDetail(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		User user = userService.findOneById(id);

		if (user == null) {
			return "dashboard/profile";
		}
		model.put("user", user);
		model.put("title", "Detalle cliente" + user.getFirstName());

		return "dashboard/see";

	}

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
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

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

	/*@RequestMapping("/upload/{uniqueFilname:.+}")
	public void handleFileDownload(@PathVariable String uniqueFilname, HttpServletResponse res)
			throws FileNotFoundException, IOException {

		// Path image = FILES_FOLDER.resolve(fileName);
		//Path image = FILES_FOLDER.resolve(uniqueFilname);

		if (Files.exists(image)) {
			res.setContentType("image/jpeg");
			res.setContentLength((int) image.toFile().length());
			FileCopyUtils.copy(Files.newInputStream(image), res.getOutputStream());

		} else {
			res.sendError(404, "File" + uniqueFilname + "(" + image.toAbsolutePath() + ") does not exist");
		}
	}*/

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
