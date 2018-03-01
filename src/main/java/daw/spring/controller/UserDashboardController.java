package daw.spring.controller;

import com.itextpdf.text.DocumentException;
import daw.spring.component.CurrentUserInfo;
import daw.spring.component.InvoiceGenerator;
import daw.spring.model.*;
import daw.spring.service.*;

//import org.apache.catalina.startup.HomesUserDatabase;
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

//import static org.hamcrest.CoreMatchers.instanceOf;
//import static org.mockito.Mockito.after;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final HomeService homeService;
    private final InvoiceGenerator invoiceGenerator;
    private final ProductService productService;
    private final DeviceService deviceService;
    private final OrderRequestService orderRequestService;
    private final AnalyticsService analyticsService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService, AnalyticsService analyticsService, InvoiceGenerator invoiceGenerator, ProductService productService, DeviceService deviceService, OrderRequestService orderRequestService) {
        this.userService = userService;
        this.homeService = homeService;
        this.invoiceGenerator = invoiceGenerator;
        this.productService = productService;
        this.deviceService = deviceService;
        this.orderRequestService = orderRequestService;
        this.analyticsService = analyticsService;
    }

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        model.addAttribute("user", user);
        List<Device> favoriteDevices = userService.getUserFavoriteDevices(user);
        model.addAttribute("favoriteDevices", favoriteDevices);
        List<Home> allHomesWithDevices = user.getHomeList();
        model.addAttribute("allHomesWithDevices", allHomesWithDevices);

        model.addAttribute("title", "Dashboard");
        return "dashboard/index";
    }

    @RequestMapping(value = "/index", params = "inputInteraction")
    public void addInteraction(Principal principal, Model model) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        List<Home> homeList = user.getHomeList();
        List<Device> deviceList = homeList.get(0).getDeviceList();
        for (Device d : deviceList) {
            if (d.getStatus() == Device.StateType.ON) {
                Analytics analytics1 = new Analytics(d, new Date(), Device.StateType.OFF, Device.StateType.ON, null);
                analyticsService.saveAnalytics(analytics1);
            } else {
                Analytics analytics2 = new Analytics(d, new Date(), Device.StateType.ON, Device.StateType.OFF, null);
                analyticsService.saveAnalytics(analytics2);
            }
        }

        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("title", "Dashboard");
        index(model, principal);
    }

    @RequestMapping("/index")
    public void index2(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("title", "Dashboard");
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
                            @RequestParam(name = "light") Integer lightQuantity) {
		Double totalPrice = (double) (blindQuantity*20+lightQuantity*30);
		log.info("Ha entrado en el metodo");
		List<Device>deviceList= new ArrayList<>();
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		for(int i=0; i<blindQuantity; i++) {
			Device device = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, false, null,false);
			deviceService.saveDevice(device);
			deviceList.add(device);
		}
		for(int i=0; i<lightQuantity; i++) {
			Device device = new Device("Actuador de bombilla", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, false, null,false);
			deviceService.saveDevice(device);
			deviceList.add(device);
		}
		log.info("Empieza a guardar");
		Home home = new Home(postCode, address, false, deviceList );
		homeService.saveHome(home);
		userService.saveHomeUser(home, user);
        //Order order = new Order(total, false, home);
        OrderRequest order = new OrderRequest(totalPrice, false, home, deviceList);
		orderRequestService.saveOrder(order);
        user.getOrderList().add(order);
        userService.saveUser(user);
		log.info("Oreder created");
        return "redirect:see";
	}

    @RequestMapping("/charts")
    public String charts(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("title", "Consumos");
        return "dashboard/charts";
    }

    @RequestMapping("/homes")
    public String homes(Model model, Principal principal) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        List<Home> homeList = user.getHomeList();
        model.addAttribute("title", "Casa");
        if (homeList.isEmpty())
            model.addAttribute("hasHomes", false);
        else
            model.addAttribute("hasHomes", true);
        model.addAttribute("homeList", homeList);
        model.addAttribute("user", user);
        return "dashboard/homes";
    }

    @RequestMapping("/homes/{id}")
    public String homeDetail(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("title", "Casa");
        Home home = homeService.findOneById(id);
        model.addAttribute("homeInfo", home);
        if (home.getDeviceList().isEmpty())
            model.addAttribute("hasDevices", false);
        else
            model.addAttribute("hasDevices", true);
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
    		//del usuario tengo el id
    		User user =  userService.findOneById(getIdFromPrincipalName(principal.getName()));
    		log.info("usuario pillado"+ user);
    		//con el id del usuario obtengo el id de su casa en una lista por si tiene mas de 1
    		List<Home> homeList= homeService.getHomesFromUser(user);
    		//Home homeUser = homeService.findOneById(user.getId());
    		List<OrderRequest> orderRequestList = orderRequestService.findNotCompletedOrders(homeList);
    		model.addAttribute("orderList", orderRequestList);
    		model.addAttribute("userHome", homeList);
    		
    		/*Home home = homeService.findOneById(homeList.get(0));   		
    		List<OrderRequest> orderRequest = user.getOrderList();
    		List<User> userHomeList = new ArrayList<>();
    		for (User user2 : userHomeList) {
				orderRequest.add(orderRequestService.finOneById(homeUser.getId()));
			}*/
    		
        //List<OrderRequest> orderRequestList;
        //List<User> userHomeList;
        /*for () {
			orderRequest.add(orderRequestService.findOrderByHomeId(home.getId()));
        	
		}*/
       
        model.addAttribute("user", user);
        
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
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @RequestMapping("/terms-Conditions")
    public String termsConditions(Model model) {
        model.addAttribute("title", "Condiciones");
        return "dashboard/terms-Conditions";
    }

    @RequestMapping("/created")
    public String created(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/created";
    }

}
