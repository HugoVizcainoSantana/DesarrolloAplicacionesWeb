package daw.spring;

import com.samskivert.mustache.Mustache;
import daw.spring.model.*;
import daw.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Entry point
@SpringBootApplication
public class Application {

    @Autowired
    private ProductService productService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AnalyticsService analyticsService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private OrderRequestService orderRequestService;
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader templateLoader, Environment environment) {
        MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
        collector.setEnvironment(environment);
        return Mustache.compiler()
                .defaultValue("### ERROR THIS IS A DEFAULT TAG ###")
                .withLoader(templateLoader)
                .withCollector(collector);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initDatabaseData() {
        return args -> {
        /*
            Default Products
         */
            Product product1 = new Product("Actuador de bombilla para domótica.  Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.", 15.50, Product.ProductType.LIGHT, "product-2.jpg", 36);
            productService.saveProduct(product1);
            Product product2 = new Product("Motor actuador de persiana para domótica. Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.", 32.50, Product.ProductType.BLIND, "product-1.jpg", 34);
            productService.saveProduct(product2);
            Product product3 = new Product("Raspberry pi programada para domótica. Así, podrás actuar desde la App, ya sea desde dispositivos móviles, ordenador sobre los diferentes elementos domóticos.", 32.50, Product.ProductType.RASPBERRYPI, "raspberry-pie.jpg", 67);
            productService.saveProduct(product3);

        /*
            Default devices
         */
            Device device1 = new Device("Actuador de bombilla", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, false, null, false);
            deviceService.saveDevice(device1);
            Device device2 = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, false, null, false);
            deviceService.saveDevice(device2);
            Device device3 = new Device("RaspberryPi", 30, Device.DeviceType.RASPBERRYPI, Device.StateType.OFF, null, false, null, false);
            deviceService.saveDevice(device3);
            Device device4 = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, true, null, false);
            deviceService.saveDevice(device4);

        /*
            Default Analytics
            · Dates are manually modified to show graphs functionality
        */
            Analytics analytics1 = new Analytics(device4, new Date(), Device.StateType.OFF, Device.StateType.ON);
            analytics1.setDate(Date.from(Instant.now().minus(2, ChronoUnit.HOURS)));
            analyticsService.saveAnalytics(analytics1);
            Analytics analytics2 = new Analytics(device4, new Date(), Device.StateType.ON, Device.StateType.OFF);
            analytics2.setDate(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)));
            analyticsService.saveAnalytics(analytics2);

        /*
            Default Homes
        */
            Home home1 = new Home(28045, "c/montepinar", true, null);
            homeService.saveHome(home1);
            Home home2 = new Home(21111, "c/ole", false, null);
            homeService.saveHome(home2);
            List<Device> home3_deviceList1 = new ArrayList<>();
            home3_deviceList1.add(device1);
            home3_deviceList1.add(device3);
            home3_deviceList1.add(device4);
            Home home3 = new Home(28007, "c/hugo", true, home3_deviceList1);
            homeService.saveHome(home3);

        /*
            Default Order Requests
         */
            List<Device> orderRequest1_deviceList = new ArrayList<>();
            orderRequest1_deviceList.add(device1);
            orderRequest1_deviceList.add(device2);
            OrderRequest orderRequest1 = new OrderRequest(31, false, new Date(), home1, orderRequest1_deviceList, "Mi observacion");
            orderRequestService.saveOrder(orderRequest1);

        /*
            Default Users
         */
            User user1 = new User("Amador", "Rivas", "amador@merengue.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.ADMIN.getRoleName());
            userService.saveUser(user1);
            User user2 = new User("Teodoro", "Rivas", "teodor69@merengue.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.USER.getRoleName());
            userService.saveUser(user2);
            User user3 = new User("ramon", "serrano", "ramon@ramon.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.USER.getRoleName());
            userService.saveUser(user3);
            User user4 = new User("dani", "maci", "dani@maci.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.USER.getRoleName());
            userService.saveUser(user4);
            List<Home> user5_homes = new ArrayList<>();
            user5_homes.add(home2);
            user5_homes.add(home3);
            User user5 = new User("Hugo", "Santana", "hugo@santana.com", encoder.encode("1234"), user5_homes, "9866363", null, null, null, Roles.USER.getRoleName());
            userService.saveUser(user5);
            User user6 = new User("Jorge", "Bicho", "Jorge@gmail.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.USER.getRoleName());
            userService.saveUser(user6);
            User userAdmin1 = new User("Admin", "Root", "admin@admin.com", encoder.encode("1234"), null, "9866363", null, null, null, Roles.ADMIN.getRoleName(), Roles.USER.getRoleName());
            userService.saveUser(userAdmin1);
        };
    }
}
