package daw.spring.restcontroller;

import daw.spring.component.CurrentUserInfo;
import daw.spring.model.*;
import daw.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/adminDashboard")
public class AdminDashboardRestController implements CurrentUserInfo {

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
    public ResponseEntity index(Principal principal) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            Map<String, List<Object>> indexOut = new HashMap<>();
            List<OrderRequest> listOrders = orderRequestService.homesOrdersList();
            List<Notification> listNotifications = notificationService.findAllNotifications();
            user = userService.findOneById(user.getId());
            indexOut.put("userData", Collections.singletonList(user));
            indexOut.put("listOrders", Collections.unmodifiableList(listOrders));
            indexOut.put("listNotifications", Collections.unmodifiableList(listNotifications));
            return ResponseEntity.ok(indexOut) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity inventory(Principal principal) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            return ResponseEntity.ok(productService.findAllProducts()) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity productInfo(Principal principal, @PathVariable long id) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            return ResponseEntity.ok(productService.findOneById(id)) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity newProduct(Principal principal, @RequestBody Product product) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        product.setId(null);
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            productService.saveProduct(product);
            return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity editProduct(Principal principal ,@PathVariable long id, @RequestBody Product product) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            if (product.getId()==id){
                productService.updateProduct(product);
            }
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getUsers(Principal principal) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            return ResponseEntity.ok(userService.findAll()) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getUser(Principal principal, @PathVariable long id) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            return ResponseEntity.ok(userService.findOneById(id)) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity orders(Principal principal) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            List<OrderRequest> listOrdersNotcomplete = orderRequestService.findNotCompletedOrdersAll();
            List<OrderRequest> listOrdersAreComplete = orderRequestService.findCompletedOrdersAll();
            Map<String, List<OrderRequest>> ordersOut = new HashMap<>();
            ordersOut.put("OrdersNotComplete", listOrdersNotcomplete);
            ordersOut.put("OrdersComplete", listOrdersAreComplete);
            return ResponseEntity.ok(ordersOut) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getOrderDetail(Principal principal, @PathVariable long id) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            Map<String, List<Object>> orderDetailOut = new HashMap<>();
            OrderRequest orderDt = orderRequestService.finOneById(id);
            Home homeOrder = orderDt.getHome();
            User homeUser = userService.findUserByHomeId(homeOrder);
            orderDetailOut.put("userHomeData", Collections.singletonList(homeUser));
            orderDetailOut.put("orderData", Collections.singletonList(orderDt));
            return ResponseEntity.ok(orderDetailOut) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/orders/{orderId}/{deviceId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity confirmDevice(Principal principal, @PathVariable long orderId, @PathVariable long deviceId,  @RequestBody Device device) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            deviceService.activeOneDevice(device);
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/orders/{orderId}/{deviceId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity cancelDevice(Principal principal,  @PathVariable long orderId, @PathVariable long deviceId,  @RequestBody Device device) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            Device deviceCancel = deviceService.findOneById(device.getId());  //Scan device
            OrderRequest orderDt = orderRequestService.finOneById(orderId);  //Delete device from order
            List<Device> deviceList = orderDt.getDeviceList();
            deviceList.remove(deviceCancel);
            Home homeDt = homeService.findOneById(orderDt.getHome().getId());  //Delete device from home
            List<Device> deviceList2 = homeDt.getDeviceList();
            deviceList2.remove(deviceCancel);
            deviceService.cancelOneDevice(deviceId);  //Delete device
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity confirmOrder(Principal principal, @PathVariable long id, @RequestBody OrderRequest order) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            if(id == order.getId()){
                orderRequestService.confirmOrder(order.getId());
                Home homeOrder = order.getHome();
                homeService.activeHome(homeOrder);
            }
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteOrder(Principal principal, @PathVariable long id, @RequestBody OrderRequest order) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            if(id == order.getId()){
                orderRequestService.deleteOrder(order.getId());
                Home homeOrder = order.getHome();
                homeService.deleteHome(homeOrder);
            }
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/issues", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity issues(Principal principal) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            return ResponseEntity.ok(notificationService.loadFirstAdminNotifications()) ;
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/issues/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity issueViewed(Principal principal, @PathVariable long id) {
        if (principal==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
        if ( user.getRoles().contains(Roles.ADMIN.getRoleName())){
            notificationService.deleteNotification(notificationService.findOneById(id));
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }


}
