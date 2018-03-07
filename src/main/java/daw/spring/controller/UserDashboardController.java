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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

//import org.apache.catalina.startup.HomesUserDatabase;
//import static org.hamcrest.CoreMatchers.instanceOf;
//import static org.mockito.Mockito.after;

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
    private final NotificationService notificationService;

    private final BCryptPasswordEncoder encoder;

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService, AnalyticsService analyticsService, InvoiceGenerator invoiceGenerator, ProductService productService, DeviceService deviceService, OrderRequestService orderRequestService, BCryptPasswordEncoder encoder, NotificationService notificationService) {
        this.userService = userService;
        this.homeService = homeService;
        this.invoiceGenerator = invoiceGenerator;
        this.productService = productService;
        this.deviceService = deviceService;
        this.orderRequestService = orderRequestService;
        this.analyticsService = analyticsService;
        this.encoder = encoder;
        this.notificationService = notificationService;
    }

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        model.addAttribute("user", user);
        List<Device> favoriteDevices = userService.getUserFavoriteDevices(user);
        model.addAttribute("favoriteDevices", favoriteDevices);
        List<Home> allHomesWithDevices = userService.getUserHomesActivated(user);
        model.addAttribute("allHomesWithDevices", allHomesWithDevices);
        model.addAttribute("title", "Dashboard");
        return "dashboard/index";
    }

    @RequestMapping("/index/{id}")
    public String addInteraction(Principal principal, Model model, @PathVariable long id) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        Device d = deviceService.findOneById(id);
        // Security check
        if (userService.userIsOwnerOf(user, d)) {
            Analytics analytics;
            log.info("---add interaction---");
            // handle types
            if ((d.getType() == Device.DeviceType.LIGHT) || (d.getType() == Device.DeviceType.RASPBERRYPI)) {
                if (d.getStatus() == Device.StateType.OFF) {
                    // if OFF and button is clicked, turn ON
                    d.setStatus(Device.StateType.ON);
                    deviceService.saveDevice(d);

                    // create a new analytic when status = ON
                    analytics = new Analytics(d, new Date(), Device.StateType.OFF, Device.StateType.ON);
                    // and save it
                    analyticsService.saveAnalytics(analytics);
                } else {
                    // if ON, only turn OFF and save
                    d.setStatus(Device.StateType.OFF);
                    deviceService.saveDevice(d);
                }
            } else if (d.getType() == Device.DeviceType.BLIND) {
                if (d.getStatus() == Device.StateType.UP) {
                    d.setStatus(Device.StateType.DOWN);
                    deviceService.saveDevice(d);

                    // create a new analytic when status = UP
                    analytics = new Analytics(d, new Date(), Device.StateType.UP, Device.StateType.DOWN);
                    // and save it
                    analyticsService.saveAnalytics(analytics);
                } else {
                    // if DOWN, UP and save
                    d.setStatus(Device.StateType.UP);
                    deviceService.saveDevice(d);
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("title", "Dashboard");

            return "redirect:/dashboard/";
        } else {
            notificationService.alertAdmin(user);
            return "redirect:/dashboard/";
        }


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


    @RequestMapping(value = "/shop", method = RequestMethod.POST)
    public String addOrder(Principal principal,
                           @RequestParam(name = "direccion") String address,
                           @RequestParam(name = "postCode") long postCode,
                           @RequestParam(name = "blind", required = false) Integer blindQuantity,
                           @RequestParam(name = "light", required = false) Integer lightQuantity,
                           @RequestParam(name = "observation", required = false) String observation) {
        if (blindQuantity == null) {
            blindQuantity = 0;
        }
        if (lightQuantity == null) {
            lightQuantity = 0;
        }
        if (observation.isEmpty()) {
            observation = "Sin observaciones";
        }
        int costBlind = deviceService.findCost("BLIND");
        int costLight = deviceService.findCost("LIGHT");
        Double totalPrice = (double) (blindQuantity * costBlind + lightQuantity * costLight);
        List<Device> deviceList = new ArrayList<>();
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        for (int i = 0; i < blindQuantity; i++) {
            Device device = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, false, null, false);
            deviceService.saveDevice(device);
            deviceList.add(device);
        }
        for (int i = 0; i < lightQuantity; i++) {
            Device device = new Device("Actuador de bombilla", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, false, null, false);
            deviceService.saveDevice(device);
            deviceList.add(device);
        }
        Home home = new Home(postCode, address, false, deviceList);
        homeService.saveHome(home);
        userService.saveHomeUser(home, user);
        //Order order = new Order(total, false, home);
        OrderRequest order = new OrderRequest(totalPrice, false, new Date(), home, deviceList, observation); //added date
        orderRequestService.saveOrder(order);
        user.getOrderList().add(order);
        userService.saveUser(user);
        log.info("Order created");
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
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        Home home = homeService.findOneById(id);
        //Security Check
        if (userService.userIsOwnerOf(user, home)) {
            model.addAttribute("homeInfo", home);
            if (home.getDeviceList().isEmpty())
                model.addAttribute("hasDevices", false);
            else
                model.addAttribute("hasDevices", true);
            model.addAttribute("user", user);
            model.addAttribute("title", "Casa");
            return "dashboard/home-detail";
        } else {
            notificationService.alertAdmin(user);
            return "redirect:/dashboard/";
        }
    }

    @GetMapping(value = "/homes/{id}/generateInvoice", produces = "application/pdf")
    public void generateInvoice(Principal principal, @PathVariable long id, HttpServletResponse response) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        Home home = homeService.findOneById(id);
        //Security Check
        if (userService.userIsOwnerOf(user, home)) {//Generate and send pdf
            try {
                OutputStream out = response.getOutputStream();
                byte[] pdf = invoiceGenerator.generateInvoiceAsStream(home, user);
                log.info("UserDash" + homeService.findOneById(id).getId());
                out.write(pdf);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=factura-" + Date.from(Instant.now()) + ".pdf");
                response.flushBuffer();
            } catch (IOException | DocumentException e) {
                log.error(e.getLocalizedMessage());
            }
        } else {
            notificationService.alertAdmin(user);
            //After alerting to admin, redirect user to dashboard
            try {
                response.sendRedirect("/dashboard/");
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }

    @RequestMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(Model model, @RequestParam("file") MultipartFile photo,
                              @RequestParam("password") String password,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone, Principal principal) {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if (!photo.isEmpty()) {
            if (user.getPhoto() != null && user.getPhoto().length() > 0) {
                Path rootPath = Paths.get("upload").resolve(user.getPhoto()).toAbsolutePath();
                File file = rootPath.toFile();
                if (file.exists() && file.canRead()) {
                    if (!file.delete()) {
                        throw new RuntimeException("File not deleted Properly");
                    }
                }
            }
            String uniqueFilname = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path rootPath = Paths.get("upload").resolve(uniqueFilname);
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutePath: " + rootAbsolutePath);
            try {
                Files.copy(photo.getInputStream(), rootAbsolutePath);
                user.setPhoto(uniqueFilname);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        if (!password.isEmpty()) {
        		if(user.getPasswordHash()!=null && user.getPasswordHash().length() > 0) {
        			String newPass = encoder.encode(password);
        			user.setPasswordHash(newPass);
        		}
        }
        if (!email.isEmpty()) {
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                user.setEmail(email);
            }
        }
        if (!phone.isEmpty()) {
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                user.setPhone(phone);
            }
        }
        userService.saveUser(user);
        return "redirect:created";
    }

    @RequestMapping("/see")
    public String see(Model model, Principal principal) {
        //del usuario tengo el id
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        List<Home> homeList = homeService.getHomesFromUser(user);

        // orders not completed yet
        List<OrderRequest> orderRequestList = orderRequestService.findNotCompletedOrders(homeList);

        // orders completed or not
        List<OrderRequest> orderRequestListAll = orderRequestService.findAllHomes(homeList);
        if (!orderRequestListAll.isEmpty()) {
            model.addAttribute("existAll", "OK");
        }

        if (!orderRequestList.isEmpty()) {
            model.addAttribute("existPending", "OK");
        }

        model.addAttribute("orderList", orderRequestList);
        model.addAttribute("orderListAll", orderRequestListAll);
        model.addAttribute("userHome", homeList);
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
                throw new NoSuchFieldException("Error no se ha podido cargar la imgen: " + pathPhoto.toString());
            }
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } catch (MalformedURLException | NoSuchFieldException exception) {
            log.error(exception.getLocalizedMessage());
        }
        return ResponseEntity.notFound().build();
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
