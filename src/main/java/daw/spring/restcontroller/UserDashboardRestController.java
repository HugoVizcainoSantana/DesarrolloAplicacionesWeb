package daw.spring.restcontroller;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import daw.spring.component.CurrentUserInfo;
import daw.spring.component.InvoiceGenerator;
import daw.spring.model.*;
import daw.spring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

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
	private final BCryptPasswordEncoder encoder;

	@Autowired
	public UserDashboardRestController(HomeService homeService, OrderRequestService orderService,
			UserService userService, InvoiceGenerator invoiceGenerator, NotificationService notificationService,
			DeviceService deviceService, AnalyticsService analitycsService, OrderRequestService orderRequestService, BCryptPasswordEncoder encoder) {

		this.homeService = homeService;
		this.orderService = orderService;
		this.userService = userService;
		this.invoiceGenerator = invoiceGenerator;
		this.notificationService = notificationService;
		this.deviceService = deviceService;
		this.analitycsService = analitycsService;
		this.orderRequestService = orderRequestService;
		this.encoder = encoder;
	}

	// ++++++++++++++++++++++++++++++++++++++++ Order ok+++++++++++++++++++++++++++++++++++
	// Create Order OK
	@RequestMapping(value = "/shop", method = POST)
	@ResponseStatus(HttpStatus.CREATED)
	public OrderRequest addOrder(Principal principal, @RequestBody OrderRequest orderRequest, @RequestBody Home home) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		homeService.saveHome(home);
		userService.saveHomeUser(home, user);
		orderService.saveOrder(orderRequest);
		return orderRequest;
	}

	// order for id
	@RequestMapping(value = "/orders/{id}", method = GET)
	public ResponseEntity<OrderRequest> getOrder(Principal principal, @PathVariable long id) {
		if(principal == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}else {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		List<Home> homeList = homeService.getHomesFromUser(user);
		// orders not completed yet
		List<OrderRequest> orderRequestList = orderRequestService.findNotCompletedOrders(homeList);
		// orders completed or not
		List<OrderRequest> orderRequestListAll = orderRequestService.findAllHomes(homeList);
		OrderRequest orderRequest = orderService.finOneById(id);
		if (orderRequestList != null && orderRequestListAll != null) {
			return new ResponseEntity<>(orderRequest, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	}

	// list orders
	@RequestMapping(value = "/orders", method = GET)
	public ResponseEntity<List<OrderRequest>> getAllOrders() {
		List<OrderRequest> ordersRequest = orderService.findAllOrder();
		if (ordersRequest != null) {
			return new ResponseEntity<>(ordersRequest, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++ Order ok +++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++++++++++++ Home ok+++++++++++++++++++++++++++++++++++

	// List home
	@RequestMapping(value = "/homes", method = GET)
	public ResponseEntity<List<Home>> homes() {
		List<Home> homes = homeService.findAllHomes();

		if (homes != null) {
			return new ResponseEntity<>(homes, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// home for id
	@RequestMapping(value = "/homes/{id}", method = GET)
	public ResponseEntity<Home> homeDetail(Principal principal, @PathVariable long id) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		Home home = homeService.findOneById(id);
		// Security Check
		if (userService.userIsOwnerOf(user, home) && (home != null)) {
			return new ResponseEntity<>(home, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// delete home, only admin
	@RequestMapping(value = "/homes/{id}", method = DELETE)
	public ResponseEntity<Home> deleteHome(@PathVariable long id) {
		Home homeSelected = homeService.findOneById(id);

		if (homeSelected != null) {
			homeService.deleteHome(homeSelected);
			return new ResponseEntity<>(homeSelected, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
	}

	// edit a home
	@RequestMapping(value = "/homes/{id}", method = PUT)
	public ResponseEntity<Home> putHome(Principal principal, @PathVariable long id, @RequestBody Home homeUpdated) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		Home homeSelected = homeService.findOneById(id);
		ResponseEntity<Home> responseHome;
		// security check
		if (userService.userIsOwnerOf(user, homeSelected) && (homeSelected != null)) {
			if (homeUpdated.getId()==id) {
				//homeSelected = homeUpdated;
				homeService.saveHome(homeUpdated);
				responseHome = new ResponseEntity<>(homeUpdated, HttpStatus.OK);
			} else {
				responseHome = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			responseHome = new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		return responseHome;
	}

	// ++++++++++++++++++++++++++++++++++++++++ Home ok+++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++++++++++++ profile ok +++++++++++++++++++++++++++++++++++
	// profile
	@JsonView(User.Basic.class)
	@RequestMapping(value = "/me", method = GET)
	public ResponseEntity<User> getUser(Principal principal) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// update profile
	@RequestMapping(value = "/me", method = PUT)
	public ResponseEntity<User> updateProfile(@RequestBody User updateUser, Principal principal) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} else {
			User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
				if(updateUser.getId() == user.getId()) {
					updateUser.setPasswordHash(encoder.encode(updateUser.getPasswordHash()));
					userService.saveUser(updateUser);
					return new ResponseEntity<>(updateUser, HttpStatus.OK);
				}
			 else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++ profile ok+++++++++++++++++++++++++++++++++++

	// +++++++++++++++++++++++++++++++++++++ invoice ok +++++++++++++++++++++++++++++++++++++++

	@RequestMapping(value = "/homes/{id}/generateInvoice", produces = "application/pdf", method = GET)
	public ResponseEntity<?> generateInvoice(@PathVariable long id, Principal principal) {
		ResponseEntity<?> response = null;
		if (principal == null) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
			Home home = homeService.findOneById(id);
			// Security Check
			if (userService.userIsOwnerOf(user, home)) {// Generate and send pdf
				try {
					byte[] pdf = invoiceGenerator.generateInvoiceAsStream(home, user);
					return ResponseEntity.ok()
							.header("Content-Disposition",
									"attachment; filename=factura-" + Date.from(Instant.now()) + ".pdf")
							.contentLength(pdf.length).contentType(MediaType.APPLICATION_PDF).body(pdf);
				} catch (DocumentException | IOException e) {
					log.error(e.toString());
				}
			} else {
				response =  new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		return response;
	}

	// +++++++++++++++++++++++++++++++++++++ invoice ok+++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++ device ++++++++++++++++++++++++++++++++++++++++++
	// List of device
	@RequestMapping(value = "/device", method = GET)
	public ResponseEntity<List<Device>> device(Principal principal) {
		if(principal == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		List<Home> homeList = user.getHomeList();
		List<Device> devicesList = new ArrayList<>();
		for (int i=0; i<homeList.size(); i++) {
			devicesList.addAll(homeList.get(i).getDeviceList());  
		}

		//ListDevice always full
		return new ResponseEntity<>(devicesList, HttpStatus.OK);
	}

	// Device for id
	@RequestMapping(value = "/device/{id}", method = GET)
	public ResponseEntity<Device> deviceDetail(@PathVariable long id, Principal principal) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		List<Home> homeList = user.getHomeList();
		List<Device> deviceList = new ArrayList<>();
		for (int i=0; i<homeList.size(); i++) {
			if (userService.userIsOwnerOf(user, homeList.get(i))) {
				deviceList=homeList.get(i).getDeviceList();
			}
		}
		for(int i=0; i<deviceList.size(); i++) {
			if(deviceList.get(i).getId()== id) {
				return new ResponseEntity<>(deviceList.get(i), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Update device for id
	//TODO work in progress
	@RequestMapping(value = "/device/{id}", method = PUT)
	public ResponseEntity<Device> changeDeviceStatus(Principal principal, @PathVariable long id, @RequestBody Device updateDevice) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		Device device = deviceService.findOneById(id);
		// Security check
		if (userService.userIsOwnerOf(user, device) && (device != null) && updateDevice.getId()==id) {
			Analytics analytics;
			log.info("---add interaction---");
			// handle types
			if ((device.getType() == Device.DeviceType.LIGHT) || (device.getType() == Device.DeviceType.RASPBERRYPI)) {
				if (device.getStatus() == Device.StateType.OFF) {
					// if OFF and button is clicked, turn ON
					updateDevice.setStatus(Device.StateType.ON);
					deviceService.saveDevice(updateDevice);

					// create a new analytic when status = ON
					analytics = new Analytics(updateDevice, new Date(), Device.StateType.OFF, Device.StateType.ON);
					// and save it
					analitycsService.saveAnalytics(analytics);
				} else {
					// if ON, only turn OFF and save
					updateDevice.setStatus(Device.StateType.OFF);
					deviceService.saveDevice(updateDevice);
				}
			} else if (device.getType() == Device.DeviceType.BLIND) {
				if (device.getStatus() == Device.StateType.UP) {
					updateDevice.setStatus(Device.StateType.DOWN);
					deviceService.saveDevice(updateDevice);

					// create a new analytic when status = UP
					analytics = new Analytics(updateDevice, new Date(), Device.StateType.UP, Device.StateType.DOWN);
					// and save it
					analitycsService.saveAnalytics(analytics);
				} else {
					// if DOWN, UP and save
					updateDevice.setStatus(Device.StateType.UP);
					deviceService.saveDevice(updateDevice);
				}
			}
			return new ResponseEntity<>(updateDevice, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// +++++++++++++++++++++++++++++++++++++++ device++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++ analytics ++++++++++++++++++++++++++++++++++++++++++

	// List analytics for device and id
	@RequestMapping(value = "/analytic/device/{id}", method = GET)
	public ResponseEntity<List<Analytics>> getAllAnalytics(@PathVariable long id, Device device, Principal principal) {
		User user = userService.findOneById(getIdFromPrincipalName(principal.getName()));
		Device d = deviceService.findOneById(id);
		List<Analytics> analyticsList = analitycsService.findAllByDeviceAndId(device, id);

		if ((userService.userIsOwnerOf(user, d)) && (analyticsList != null)) {
			return new ResponseEntity<>(analyticsList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// +++++++++++++++++++++++++++++++++++++++ analitycs ++++++++++++++++++++++++++++++++++++++++++
}

