package daw.spring.restcontroller;


import daw.spring.component.CurrentUserInfo;
import daw.spring.component.InvoiceGenerator;
import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.model.OrderRequest;
import daw.spring.model.User;
import daw.spring.service.AnalyticsService;
import daw.spring.service.DeviceService;
import daw.spring.service.HomeService;
import daw.spring.service.NotificationService;
import daw.spring.service.OrderRequestService;
import daw.spring.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/dashboard")
public class UserDashboardRestController implements CurrentUserInfo {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final HomeService homeService;
	private final OrderRequestService orderService;
	private final UserService userService;
	private final InvoiceGenerator invoiceGenerator;
	private final NotificationService notificationService;
	private final DeviceService deviceService;
	private final AnalyticsService analitycsService;
	private final OrderRequestService orderRequestService;
    
    @Autowired
    public  UserDashboardRestController( HomeService homeService, OrderRequestService orderService, UserService userService, InvoiceGenerator invoiceGenerator, NotificationService notificationService,DeviceService deviceService, AnalyticsService analitycsService, OrderRequestService orderRequestService) {
		
		this.homeService=homeService;
		this.orderService = orderService;
		this.userService = userService;
		this.invoiceGenerator = invoiceGenerator;
		this.notificationService = notificationService;
		this.deviceService = deviceService;
		this.analitycsService = analitycsService;
		this.orderRequestService = orderRequestService;
	}

    @RequestMapping("/test")
    public Map<String, Long> test() {
        log.error("Test");
        Map<String, Long> map = new HashMap<>();
        map.put("Test1", 1L);
        return map;
    }
    //++++++++++++++++++++++++++++++++++++++++ Order ok+++++++++++++++++++++++++++++++++++
    //Crear Order OK
    @RequestMapping(value = "/shop", method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderRequest addOrder(@RequestBody OrderRequest orderRequest) {
        orderService.saveOrder(orderRequest);
        return orderRequest;
    }

    //Obtener Orden por id
    @RequestMapping(value="/orders/{id}", method=GET)
    public ResponseEntity<OrderRequest> getOrder (Principal principal, @PathVariable long id) {
    		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
    		List<Home> homeList = homeService.getHomesFromUser(user);
    	     // orders not completed yet
    	    List<OrderRequest> orderRequestList = orderRequestService.findNotCompletedOrders(homeList);
    	     // orders completed or not
    	    List<OrderRequest> orderRequestListAll = orderRequestService.findAllHomes(homeList);
    		OrderRequest orderRequest = orderService.finOneById(id);
	    		if(orderRequestList!=null && orderRequestListAll!= null){
	    			return new ResponseEntity<>(orderRequest,HttpStatus.OK);
	    		}else{
	    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    		}
    		
    }
  //Obtenemos una lista de ordenes
  	@RequestMapping(value="/orders", method= GET)
  	public ResponseEntity<List<OrderRequest>>getAllOrders(){
  		List<OrderRequest>ordersRequest = orderService.findAllOrder();
  		if(ordersRequest!=null){
  			return new ResponseEntity<>(ordersRequest,HttpStatus.OK);
  		}else{
  			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  		}	
  	}
  	
  	
    //++++++++++++++++++++++++++++++++++++++++ Order ok +++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++ Home  ok+++++++++++++++++++++++++++++++++++
   
  	
  	//Lista de casas 
    @RequestMapping(value="/homes", method= GET)
    public List<Home> homes() {
        return homeService.findAllHomes();
    }
    
    //Casa por id
    @RequestMapping(value="/homes/{id}", method= GET)
    public ResponseEntity<Home> homeDetail(Principal principal, @PathVariable long id) {
    		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
    		Home home = homeService.findOneById(id);
        //Security Check
    		if (userService.userIsOwnerOf(user, home) && (home != null) ) {    
	            return new ResponseEntity<>(home, HttpStatus.OK);   
    		}else {
	            //notificationService.alertAdmin(user);
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
    }
    
    //Eliminar una casa en l aparte de seguridad ningun usuario puede borrar 
  	@RequestMapping(value="/homes/{id}", method = DELETE)
  	public ResponseEntity<Home> deleteHome(@PathVariable long id){
  		Home homeSelcted = homeService.findOneById(id);
  		if(homeSelcted != null){
  			homeService.deleteHome(homeSelcted);
  			return new ResponseEntity<>(homeSelcted,HttpStatus.OK);
  		}else{
  			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  			
  		}
  	}
  	
  	//Editamos una casa 
  	@RequestMapping(value="/homes/{id}", method= PUT)
  	public ResponseEntity<Home> putHome(Principal principal, @PathVariable long id,@RequestBody Home homeUpdated){
  		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
  		Home homeSelcted = homeService.findOneById(id);
  		ResponseEntity<Home> responseHome =null; 
  		//security check
  		if (userService.userIsOwnerOf(user, homeSelcted) && (homeSelcted != null) ) {    
	  		if((homeSelcted.getId())== homeUpdated.getId()){
	  			homeService.saveHome(homeSelcted);
	  			responseHome= new ResponseEntity<>(homeUpdated,HttpStatus.OK);
	  		}else{
	  			responseHome= new ResponseEntity<>(HttpStatus.NOT_FOUND);	
	  		}
  		}
  		return responseHome;
  	}
    //++++++++++++++++++++++++++++++++++++++++ Home  ok+++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++ profile ok a espera de seguridad+++++++++++++++++++++++++++++++++++
    //Obtener perfil
    //@JsonView() 
    @RequestMapping(value="/me", method = GET)
    public ResponseEntity<User> getUser (Principal principal){
    		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));	
    		if (user != null) {
    			return new ResponseEntity<>(user, HttpStatus.OK);
    		}else {
    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    }
  
    //Actualiza perfil por id
    @RequestMapping(value = "/me", method = PUT)
    public ResponseEntity<User> upadateProfile(@RequestBody User updateUser, Principal principal)  {
    		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));	
        if (user != null && user.getId() != updateUser.getId()) {
        		userService.saveUser(user);
        		return new ResponseEntity<> (user, HttpStatus.OK);
        }else {
        		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

 
    //++++++++++++++++++++++++++++++++++++++++ profile ok+++++++++++++++++++++++++++++++++++
    
    //+++++++++++++++++++++++++++++++++++++ invoice ok +++++++++++++++++++++++++++++++++++++++
    
    @RequestMapping(value = "/homes/{id}/generateInvoice", produces = "application/pdf", method=GET)
    public ResponseEntity<?> generateInvoice(@PathVariable long id) {
        //User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
    		User user1= userService.findOneById(5l);
        Home home = homeService.findOneById(id);
        //Security Check
        if (userService.userIsOwnerOf(user1, home)) {//Generate and send pdf
            try {
                byte[] pdf = invoiceGenerator.generateInvoiceAsStream(home, user1);
                return ResponseEntity.ok()
                		.header("Content-Disposition", "attachment; filename=factura-" + Date.from(Instant.now()) + ".pdf")
                		.contentLength(pdf.length).contentType(MediaType.APPLICATION_PDF).body(pdf);
            } catch (DocumentException | IOException e) {
                log.error(e.toString());
            }
        } else {
            notificationService.alertAdmin(user1);
            //After alerting to admin, redirect user to dashboard
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  //+++++++++++++++++++++++++++++++++++++ invoice ok +++++++++++++++++++++++++++++++++++++++
  //+++++++++++++++++++++++++++++++++++++++ device ++++++++++++++++++++++++++++++++++++++++++    
  //Lista de device 
    @RequestMapping(value="/device", method= GET)
    public List<Device> device() {
        return deviceService.findAllDevices();
    }
    
    //Device por id
    @RequestMapping(value="/device/{id}", method= GET)
    public ResponseEntity<Device> deviceDetail(@PathVariable long id) {
    		Device device = deviceService.findOneById(id);
        //Security Check
        if (device != null) {
            return new ResponseEntity<>(device, HttpStatus.OK);
        } else {
            //notificationService.alertAdmin(user);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    //Actualiza device por id
    @RequestMapping(value = "/device", method = PUT)
    public ResponseEntity<Device> upadateDevice(@RequestBody Device updateDevice, @PathVariable long id)  {
        Device device = deviceService.findOneById(id);
        if (device != null && device.getId() != updateDevice.getId()) {
        		deviceService.saveDevice(device);
        		return new ResponseEntity<> (device, HttpStatus.OK);
        }else {
        		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
  //+++++++++++++++++++++++++++++++++++++++ device ++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++ analitycs ++++++++++++++++++++++++++++++++++++++++++    
    //Lista de analitycs 
      @RequestMapping(value="/analitycs", method= GET)
      public List<Analytics> analitycs() {
          return analitycsService.findAllAnalytics();
      }
      
      //analitycs por id
      @RequestMapping(value="/analitycs/{id}", method= GET)
      public ResponseEntity<Analytics> analitycsDetail(@PathVariable long id) {
      		Analytics analitycs = analitycsService.findOneById(id);
          //Security Check
          if (analitycs != null) {
              return new ResponseEntity<>(analitycs, HttpStatus.OK);
          } else {
              //notificationService.alertAdmin(user);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
      }
      
      
      //Actualiza analitycs por id
      @RequestMapping(value = "/analitycs", method = PUT)
      public ResponseEntity<Analytics> upadateAnalitycs(@RequestBody User updateAnalitycs, @PathVariable long id)  {
    	  	Analytics analitycs = analitycsService.findOneById(id);
          if (analitycs != null && analitycs.getId() != updateAnalitycs.getId()) {
        	  analitycsService.saveAnalytics(analitycs);
          		return new ResponseEntity<> (analitycs, HttpStatus.OK);
          }else {
          		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          
      }
      
    //+++++++++++++++++++++++++++++++++++++++ analitycs ++++++++++++++++++++++++++++++++++++++++++
}


/*
@Controller
@RequestMapping("/dashboard")
public class UserDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final HomeService homeService;
    private final InvoiceGenerator invoiceGenerator;
    private final DeviceService deviceService;
    private final OrderRequestService orderRequestService;
    private final AnalyticsService analyticsService;
    private final NotificationService notificationService;

    private final BCryptPasswordEncoder encoder;

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    public UserDashboardController(UserService userService, HomeService homeService, AnalyticsService analyticsService, InvoiceGenerator invoiceGenerator, DeviceService deviceService, OrderRequestService orderRequestService, BCryptPasswordEncoder encoder, NotificationService notificationService) {
        this.userService = userService;
        this.homeService = homeService;
        this.invoiceGenerator = invoiceGenerator;
        this.deviceService = deviceService;
        this.orderRequestService = orderRequestService;
        this.analyticsService = analyticsService;
        this.encoder = encoder;
        this.notificationService = notificationService;
    }

    @RequestMapping(value = {"", "/", "/index"})
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
                log.error(e.toString());
            }
        } else {
            notificationService.alertAdmin(user);
            //After alerting to admin, redirect user to dashboard
            try {
                response.sendRedirect("/dashboard/");
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }

    @RequestMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "dashboard/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(@RequestParam("file") MultipartFile photo,
                              @RequestParam("password") String password,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone, Principal principal) throws IOException {
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if (!photo.isEmpty()) {
            if (user.getPhoto() != null && user.getPhoto().length() > 0) {
                Path oldPhoto = Application.USERS_IMAGES_PATH.resolve(user.getPhoto()).toAbsolutePath();
                if (oldPhoto.toFile().exists()) {
                    Files.delete(oldPhoto);
                }
            }
            String filename = "user-" + user.getId();
            //Check if folder exists, if not, create it
            if (Utilities.checkIfPathNotExists(Application.USERS_IMAGES_PATH)) {
                Utilities.createFolder(Application.USERS_IMAGES_PATH);
            }
            Path imagePath = Application.USERS_IMAGES_PATH.resolve(filename).toAbsolutePath();
            log.info("imagePath: " + imagePath);
            try {
                Files.copy(photo.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                user.setPhoto(filename);
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        if (!password.isEmpty()) {
            if (user.getPasswordHash() != null && user.getPasswordHash().length() > 0) {
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

     
    @GetMapping(value = "/uploaded/{filename:.+}")
    public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {
        log.error("inside #seePhoto()");
        Path pathPhoto = Application.UPLOADED_FILES_PATH.resolve(filename).toAbsolutePath();
        log.info("Pathphoto: " + pathPhoto);
        Resource resource;
        try {
            resource = new UrlResource(pathPhoto.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new NoSuchFieldException("Error no se ha podido cargar la imagen: " + pathPhoto.toString());
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


*/