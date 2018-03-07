package daw.spring.restcontroller;

import daw.spring.model.Product;
import daw.spring.service.ProductService;
import daw.spring.utilities.ApiRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@ApiRestController
public class AdminDashboarRestController {

    private final ProductService productService;

    @Autowired
    public AdminDashboarRestController(ProductService productService) {
        //this.userService = userService;
        //this.deviceService = deviceService;
        //this.homeService = homeService;
        this.productService = productService;
        //this.orderRequestService = orderRequestService;
        //this.notificationService = notificationService;
    }


    @RequestMapping(value="/inventory", method= RequestMethod.GET)
    public List<Product> getInventory() {
        return productService.findAllProducts();
    }

}

/*  Cosas del controller normal
@Controller
@RequestMapping("/adminDashboard")
public class AdminDashboardController implements CurrentUserInfo {

    private final UserService userService;
    private final DeviceService deviceService;
    private final HomeService homeService;
    private final ProductService productService;
    private final OrderRequestService orderRequestService;
    private final NotificationService notificationService;


    private final Logger log = LoggerFactory.getLogger("AdminDashbpard");

    @Autowired
    public AdminDashboardController(UserService userService, DeviceService deviceService, HomeService homeService, ProductService productService, OrderRequestService orderRequestService, NotificationService notificationService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.homeService = homeService;
        this.productService = productService;
        this.orderRequestService = orderRequestService;
        this.notificationService = notificationService;
    }

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("ordersIndex", orderRequestService.homesOrdersList());
        model.addAttribute("alerts", notificationService.loadFirstAdminNotifications());
        return "adminDashboard/index";
    }

    @RequestMapping("/index")
    public void index2(Model model, Principal principal) {
        index(model, principal);
    }

    @RequestMapping("/inventory")
    public String inventario(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        return "adminDashboard/inventory";
    }


    @RequestMapping(value = "/editProducts", params = "add" )
    public String modStock(Model model, @RequestParam("id") long id, @RequestParam("numberStock") long stock, @RequestParam("numberCost") double cost, @RequestParam("defDescription") String description, Principal principal) {
        productService.updateStockProduct(id, stock, cost, description);
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        return "adminDashboard/inventory";
    }

    @RequestMapping("/inventory/{id}")
    public String showEditProduct(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        model.addAttribute("productEdit", productService.findOneById(id));
        return "adminDashboard/editProducts";
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

    @RequestMapping("/users/{id}")
    public String usersDetail(Model model, @PathVariable long id) {
        model.addAttribute("userDetail", userService.findOneById(id));

        return "adminDashboard/userDetail";
    }

    @RequestMapping(value = "/moreUsers", method = RequestMethod.GET)
    public String moreUsuarios(Model model, @RequestParam int page) {
        Page<User> userList = userService.findAll(new PageRequest(page, 4));
        model.addAttribute("items", userList);
        return "listItemsPage";
    }

    @RequestMapping("/orders")
    public String orders(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("ordersCount", !orderRequestService.findNotCompletedOrdersAll().isEmpty());
        model.addAttribute("ordersPageable", orderRequestService.findNotCompletedOrdersAll().size() > 5);
        model.addAttribute("ordersCompletePageable", orderRequestService.findCompletedOrdersAll().size() > 5);
        model.addAttribute("ordersCompletedCount", !orderRequestService.findCompletedOrdersAll().isEmpty());
        Page<OrderRequest> orders = orderRequestService.findNotCompletedOrdersAllPage(new PageRequest(0, 5));
        model.addAttribute("orders", orders);
        Page<OrderRequest> ordersCompleted = orderRequestService.findCompletedOrdersAllPage(new PageRequest(0, 5));
        model.addAttribute("ordersCompleted", ordersCompleted);
        return "adminDashboard/orders";
    }

    @RequestMapping(value = "/moreOrders", method = RequestMethod.GET)
    public String moreOrdersPage(Model model, @RequestParam int page) {
        Page<OrderRequest> orderList = orderRequestService.findNotCompletedOrdersAllPage(new PageRequest(page, 5));
        model.addAttribute("itemsOrder", orderList);
        return "listOrdersPage";
    }

    @RequestMapping(value = "/moreOrdersCompleted", method = RequestMethod.GET)
    public String moreOrdersCompletedPage(Model model, @RequestParam int page) {
        Page<OrderRequest> orderListCompleted = orderRequestService.findCompletedOrdersAllPage(new PageRequest(page, 5));
        model.addAttribute("itemsOrderCompleted", orderListCompleted);
        return "listOrdersCompletedPage";
    }

    @RequestMapping(value = "/detail/{id}", params = "activate")
    public String confirmOrder(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        OrderRequest orderDt = orderRequestService.finOneById(id);
        orderRequestService.confirmOrder(id);
        model.addAttribute("orderDetail", orderDt);
        Home homeOrder = orderDt.getHome();
        homeService.activeHome(homeOrder);
        User homeUser = userService.findUserByHomeId(homeOrder);
        model.addAttribute("userHome", homeUser);
        String messageConfirm = "Pedido " + id + " confirmado correctamente.";
        model.addAttribute("messageConfirm", messageConfirm);
        return "redirect:/adminDashboard/orders";
    }

    @RequestMapping(value = "/detail/{id}", params = "cancel")
    public String deleteOrder(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        OrderRequest orderDt = orderRequestService.finOneById(id);
        orderRequestService.deleteOrder(id);
        model.addAttribute("orderDetail", orderDt);
        Home homeOrder = orderDt.getHome();
        homeService.activeHome(homeOrder);
        User homeUser = userService.findUserByHomeId(homeOrder);
        model.addAttribute("userHome", homeUser);
        String messageConfirm = "Pedido " + id + " eliminado correctamente.";
        model.addAttribute("messageConfirm", messageConfirm);
        return "redirect:/adminDashboard/orders";
    }

    @RequestMapping("/orders/{id}")
    public String orderDetail(Model model, Principal principal, @PathVariable long id) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        OrderRequest orderDt = orderRequestService.finOneById(id);
        model.addAttribute("orderDetail", orderDt);
        Home homeOrder = orderDt.getHome();
        User homeUser = userService.findUserByHomeId(homeOrder);
        model.addAttribute("userHome", homeUser);
        return "adminDashboard/detail";
    }


    @RequestMapping(value = "/orders/{orderId}/{deviceId}", params = "activate")
    public String confirmDevice(Model model, Principal principal, @PathVariable long orderId, @PathVariable long deviceId, @RequestParam(required = false) String serialNumberInput) {
        deviceService.activeOneDevice(deviceId, serialNumberInput);
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "redirect:/adminDashboard/orders/" + orderId;
    }

    @RequestMapping(value = "/orders/{orderId}/{deviceId}", params = "cancel")
    public String cancelDevice(@PathVariable long orderId, @PathVariable long deviceId) {
        Device deviceCancel = deviceService.findOneById(deviceId);  //Scan device
        OrderRequest orderDt = orderRequestService.finOneById(orderId);  //Delete device from order
        List<Device> deviceList = orderDt.getDeviceList();
        deviceList.remove(deviceCancel);
        Home homeDt = homeService.findOneById(orderDt.getHome().getId());  //Delete device from home
        List<Device> deviceList2 = homeDt.getDeviceList();
        deviceList2.remove(deviceCancel);
        deviceService.cancelOneDevice(deviceId);  //Delete device
        return "redirect:/adminDashboard/orders/" + orderId;
    }

    @RequestMapping("/addProduct")
    public String shop(Model model, Principal principal) {
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        return "adminDashboard/addProduct";
    }

    @RequestMapping(value = "/addProduct", params = "add" )
    public String addProduct(Model model,
                             @RequestParam("file") MultipartFile photo,
                             @RequestParam("numberStock") long stock,
                             @RequestParam("numberCost") double cost,
                             @RequestParam("defDescription") String description,
                             Principal principal) throws IOException {
        Product product = new Product(description, cost, null, null, stock);
        if (!photo.isEmpty()) {
            if (product.getImg() != null && product.getImg().length() > 0) {
                Path rootPath = Paths.get("upload").resolve(product.getImg()).toAbsolutePath();
                File file = rootPath.toFile();
                if (file.exists() && file.canRead()) {
                    Files.delete(file.toPath());
                }
            }
            String uniqueFilname = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path rootPath = Paths.get("upload").resolve(uniqueFilname);
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            try {
                Files.copy(photo.getInputStream(), rootAbsolutePath);
                product.setImg(uniqueFilname);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        productService.saveProduct(product);
        model.addAttribute("user", userService.findOneById(getIdFromPrincipalName(principal.getName())));
        model.addAttribute("product", productService.findAllProducts());
        return "redirect:/adminDashboard/inventory";
    }

    @RequestMapping(value = "/addProduct", params = "cancel")
    public String cancelAddProduct() {
        return "redirect:/adminDashboard/inventory";
    }

    @RequestMapping(value = "/editProducts", params = "cancel")
    public String cancelEditProduct() {
        return "redirect:/adminDashboard/inventory";
    }

    @RequestMapping(value = "/issues/{id}")
    public String issueViewed(@PathVariable long id) {
        notificationService.deleteNotification(notificationService.findOneById(id));
        return "redirect:/adminDashboard/";
    }
}  */
