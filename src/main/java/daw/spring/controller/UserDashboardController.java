package daw.spring.controller;

import com.itextpdf.text.DocumentException;
import daw.spring.component.CurrentUserInfo;
import daw.spring.component.InvoiceGenerator;
import daw.spring.model.*;
import daw.spring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import daw.spring.component.CurrentUserInfo;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final HomeService homeService;
    private final InvoiceGenerator invoiceGenerator;
    private final ProductService productService;
    private final DeviceService deviceService;
    private final OrderRequestService orderRequestService;


    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService, InvoiceGenerator invoiceGenerator, ProductService productService, DeviceService deviceService, OrderRequestService orderRequestService) {
        this.userService = userService;
        this.homeService = homeService;
        this.invoiceGenerator = invoiceGenerator;
        this.productService=productService;
        this.deviceService= deviceService;
        this.orderRequestService = orderRequestService;
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

	@RequestMapping("/shop")
	public String shop(Model model, Principal principal) {
		model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
		model.addAttribute("title", "Tienda");
		return "dashboard/shop";
	}


	@RequestMapping (value="/shop", method = RequestMethod.POST)
	public String addOrder (Principal principal, @RequestParam(name="direccion")String address,
                            @RequestParam(name = "postCode") long postCode,
                            @RequestParam(name = "blind") Integer blindQuantity,
                            @RequestParam(name = "light") Integer lightQuantity,
                            @RequestParam(name = "total") long total) {
		List<Device>deviceList= new ArrayList<>();
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		for(int i=0; i<blindQuantity; i++) {
			Device device = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, false, null);
			deviceService.saveDevice(device);
			deviceList.add(device);
		}
		for(int i=0; i<lightQuantity; i++) {
			Device device = new Device("Actuador de bombilla", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, false, null);
			deviceService.saveDevice(device);
			deviceList.add(device);
		}
		Home home = new Home(postCode, address, false, deviceList );
		homeService.saveHome(home);
		userService.saveHomeUser(home, user);
        //Order order = new Order(total, false, home);
        OrderRequest order = new OrderRequest(total, false, home, deviceList);
		orderRequestService.saveOrder(order);

        return "redirect:shop";
	}


    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> cargarProductos(@PathVariable String term) {
		return productService.findByNombre(term);
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

    @GetMapping(value = "/homes/{id}/generateInvoice", produces = "application/pdf")
    public void generateInvoice(Principal principal, @PathVariable long id, HttpServletResponse response) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        //Generate and send pdf
        try {
            OutputStream out = response.getOutputStream();
            byte[] pdf = invoiceGenerator.generateInvoiceAsStream(homeService.findOneById(id), user);
            out.write(pdf);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=factura-" + Date.from(Instant.now()) + ".pdf");
            response.flushBuffer();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
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
    @RequestMapping("/see")
    public String see(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("orderList",homeService.findOneById(getIdFromPrincipalName(principal.getName())));
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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
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

}
