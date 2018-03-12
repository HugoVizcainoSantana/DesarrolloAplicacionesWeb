package daw.spring.restcontroller;

import daw.spring.model.*;
import daw.spring.service.NotificationService;
import daw.spring.service.OrderRequestService;
import daw.spring.service.ProductService;
import daw.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/adminDashboard")
public class AdminDashboardRestController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderRequestService orderRequestService;
    private final NotificationService notificationService;

    @Autowired
    public AdminDashboardRestController(UserService userService, ProductService productService, OrderRequestService orderRequestService, NotificationService notificationService) {
        this.userService = userService;
        //this.deviceService = deviceService;
        //this.homeService = homeService;
        this.productService = productService;
        this.orderRequestService = orderRequestService;
        this.notificationService = notificationService;
    }


    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<Object>> index() {
        Map<String, List<Object>> indexOut = new HashMap<>();
        long id = 1;
        List<OrderRequest> listOrders = orderRequestService.homesOrdersList();
        List<Notification> listNotifications = notificationService.findAllNotifications();
        User user = userService.findOneById(id);
        indexOut.put("userData", Collections.singletonList(user));
        indexOut.put("listOrders", Collections.unmodifiableList(listOrders));
        indexOut.put("listNotifications", Collections.unmodifiableList(listNotifications));
        return indexOut;
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> inventory() {
        return productService.findAllProducts();
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product productInfo(@PathVariable long id) {
        return productService.findOneById(id);
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity newProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/editProducts", method = RequestMethod.PUT )
    @ResponseStatus(HttpStatus.OK)
    public String modStock(@RequestParam("id") long id, @RequestParam("numberStock") long stock, @RequestParam("numberCost") double cost, @RequestParam("defDescription") String description) {
        productService.updateStockProduct(id, stock, cost, description);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.findAll();
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable long id) {
        return userService.findOneById(id);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<OrderRequest>> orders() {
        List<OrderRequest> listOrdersNotcomplete = orderRequestService.findNotCompletedOrdersAll();
        List<OrderRequest> listOrdersAreComplete = orderRequestService.findCompletedOrdersAll();
        Map<String, List<OrderRequest>> ordersOut = new HashMap<>();
        ordersOut.put("OrdersNotComplete", listOrdersNotcomplete);
        ordersOut.put("OrdersComplete", listOrdersAreComplete);
        return ordersOut;
    }


    @RequestMapping("/orders/{id}")
    public Map<String, List<Object>> getOrderDetail( @PathVariable long id) {
        Map<String, List<Object>> OrderDetailOut = new HashMap<>();
        OrderRequest orderDt = orderRequestService.finOneById(id);
        Home homeOrder = orderDt.getHome();
        User homeUser = userService.findUserByHomeId(homeOrder);
        OrderDetailOut.put("userHomeData", Collections.singletonList(homeUser));
        OrderDetailOut.put("orderData", Collections.singletonList(orderDt));
        return OrderDetailOut;
    }

    /*
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
    * */


    @RequestMapping(value = "/issues", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> issues(@PathVariable long id) {
        List<Notification> listOutNotification = new ArrayList<>();
        listOutNotification = notificationService.findAllNotifications();
        return listOutNotification;
    }

    @RequestMapping(value = "/issues/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void issueViewed(@PathVariable long id) {
        notificationService.deleteNotification(notificationService.findOneById(id));
    }


}

/*  Cosas del controller normal

Las operaciones se codifican como métodos http
 GET: Devuelve el recurso, generalmente codificado en
JSON. No envían información en el cuerpo de la petición.
 DELETE: Borra el recurso. No envían información en el
cuerpo de la petición.
 POST: Añade un nuevo recurso. Envía el recurso en el cuerpo
de la petición.
 PUT: Modifica el recurso. Habitualmente se envía el recurso
obtenido con GET pero modificando los campos que se
consideren (existen optimizaciones)

}
*/