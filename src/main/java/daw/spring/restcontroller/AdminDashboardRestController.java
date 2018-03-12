package daw.spring.restcontroller;

import daw.spring.model.*;
import daw.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/adminDashboard")
public class AdminDashboardRestController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderRequestService orderRequestService;
    private final NotificationService notificationService;
    private final DeviceService deviceService;
    private final HomeService homeService;

    @Autowired
    public AdminDashboardRestController(UserService userService, ProductService productService, OrderRequestService orderRequestService, NotificationService notificationService, DeviceService deviceService , HomeService homeService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.homeService = homeService;
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


    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editProduct(@PathVariable long id, @RequestBody Product product) {
        if (product.getId()==id){
            productService.updateProduct(product);
        }
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
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<Object>> getOrderDetail( @PathVariable long id) {
        Map<String, List<Object>> orderDetailOut = new HashMap<>();
        OrderRequest orderDt = orderRequestService.finOneById(id);
        Home homeOrder = orderDt.getHome();
        User homeUser = userService.findUserByHomeId(homeOrder);
        orderDetailOut.put("userHomeData", Collections.singletonList(homeUser));
        orderDetailOut.put("orderData", Collections.singletonList(orderDt));
        return orderDetailOut;
    }

    @RequestMapping(value = "/orders/{orderId}/{deviceId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void confirmDevice( @PathVariable long orderId, @PathVariable long deviceId,  @RequestBody Device device) {
        deviceService.activeOneDevice(device);
    }

    @RequestMapping(value = "/orders/{orderId}/{deviceId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void cancelDevice( @PathVariable long orderId, @PathVariable long deviceId,  @RequestBody Device device) {
        Device deviceCancel = deviceService.findOneById(device.getId());  //Scan device
        OrderRequest orderDt = orderRequestService.finOneById(orderId);  //Delete device from order
        List<Device> deviceList = orderDt.getDeviceList();
        deviceList.remove(deviceCancel);
        Home homeDt = homeService.findOneById(orderDt.getHome().getId());  //Delete device from home
        List<Device> deviceList2 = homeDt.getDeviceList();
        deviceList2.remove(deviceCancel);
        deviceService.cancelOneDevice(deviceId);  //Delete device
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder( @PathVariable long id, @RequestBody OrderRequest order) {
        if(id == order.getId()){
            orderRequestService.confirmOrder(order.getId());
            Home homeOrder = order.getHome();
            homeService.activeHome(homeOrder);
        }
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@PathVariable long id, @RequestBody OrderRequest order) {
        if(id == order.getId()){
            orderRequestService.deleteOrder(order.getId());
            Home homeOrder = order.getHome();
            homeService.deleteHome(homeOrder);
        }
    }

    @RequestMapping(value = "/issues", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> issues(@PathVariable long id) {
        return notificationService.findAllNotifications();
    }

    @RequestMapping(value = "/issues/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void issueViewed(@PathVariable long id) {
        notificationService.deleteNotification(notificationService.findOneById(id));
    }


}
