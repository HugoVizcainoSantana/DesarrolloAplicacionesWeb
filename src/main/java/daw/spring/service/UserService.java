package daw.spring.service;

import daw.spring.model.*;
import daw.spring.repository.ProductRepository;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final OrderService orderService;
    private final HomeService homeService;
    private final DeviceService deviceService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder, OrderService orderService, HomeService homeService, DeviceService deviceService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.orderService = orderService;
        this.homeService = homeService;
        this.deviceService = deviceService;
    }
    

    public User findOneById(Long id) {
        return userRepository.findOne(id);
    }

    public List<User> findAll() { return userRepository.findAll();}

    public Page<User> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public long countAllUsers(){ return userRepository.count(); }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findOneUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findAllUsersByFirstName(String name) {
        return userRepository.findAllByFirstName(name);
    }
    
    public void saveHomeUser(Home home, User user) {
    		List<Home> listHome= user.getHomeList();
    		listHome.add(home);
    		user.setHomeList(listHome);
    		saveUser(user);
    }

    @PostConstruct
    public void init() {

        Device device1 = new Device("Actuador de bombilla", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, false);
        Device device2 = new Device("Actuador de persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, false);
        Device device3 = new Device("RaspberryPi", 30, Device.DeviceType.RASPBERRYPI, Device.StateType.OFF, null, false);
        ArrayList<Device> deviceList = new ArrayList<>();
        deviceList.add(device1);
        deviceList.add(device2);
        deviceList.add(device3);
     //   deviceService.saveDevice(device1);
     //   deviceService.saveDevice(device2);
     //   deviceService.saveDevice(device3);

        User user1 = new User("Amador", "Rivas", "amador@merengue.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        Home home2 = new Home(28045, "c/montepinar", true, null);
        Home home3 = new Home(21111, "c/montepinar1111", false, deviceList);
        ArrayList<Home> user1Homes = new ArrayList<>();
        user1Homes.add(home2);
        user1Homes.add(home3);
        user1.setHomeList(user1Homes);
        //homeService.saveHome(home2);
        //homeService.saveHome(home3);
        saveUser(user1);

        Order order1 = new Order(31, false, home3, deviceList);

        orderService.saveOrder(order1);


        User user2 = new User("Teodoro", "Rivas", "teodor69@merengue.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        saveUser(user2);

        User user3 = new User("ramon", "serrano", "ramon@ramon.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        saveUser(user3);

        User user4 = new User("dani", "maci", "dani@maci.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        saveUser(user4);

        User user5 = new User("Hugo", "Santana", "hugo@santana.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        Home home1 = new Home(28007, "c/hugo", true, null);
        ArrayList<Home> user5Homes = new ArrayList<>();
        user5Homes.add(home1);
        user5.setHomeList(user5Homes);
        //homeService.saveHome(home1);
        saveUser(user5);

        User user6 = new User("Jorge", "Bicho", "Jorge@gmail.com", encoder.encode("1234"), null, "9866363", null, null, Roles.USER.getRoleName());
        saveUser(user6);

        User userAdmin1 = new User("Admin", "Root", "admin@admin.com", encoder.encode("1234"), null, "9866363", null, null, Roles.ADMIN.getRoleName(), Roles.USER.getRoleName());
        saveUser(userAdmin1);

        /*
         Example Data:
         ('alicia', 'rodriguez', 'alicia@gmail.com', 'c/murcia', 12345, '', '')
         ('aberto', 'serrano', 'ramon@ramon.com', 'c/miami', 12345, '', '')
         ('Michael', 'Gallego', 'mic@gmail.com', 'c/frackfurt', 12345, '', '')
         ('Patxi', 'lopez', 'patxi@gmail.com', 'c/berlin', 12345, '', '')
         ('cristina', 'garcia', 'cristina@gmail.com', 'c/miraflor', 12345, '', '')
         ('ana', 'serrano', 'aba@gmail.com', 'c/conde', 12345, '', '')
         ('maria', 'serrano', 'maria@gmail.com', 'c/zorron', 12345, '', '')
         ('antonia', 'serrano', 'antonia@gmail.com', 'c/mira que si voy', 12345, '', '')
         ('fidel', 'castro', 'fidel@gmail.com', 'c/genocida', 12345, '', '')
         ('Santiago ', 'Barnabeu', 'satiago@gmail.com', 'c/estadio', 12345, '', '')
         ('Cristiano', 'Ronaldo', 'cristianao@gmail.com', 'c/abdominal', 12345, '', '')
         ('Raul', 'Blanco', 'raul@gmail.com', 'c/burbuja', 12345, '', '')
        ('Florentino', 'Perez', 'titofloren@gmail.com', 'c/ricachoes', 12345, '', '')
         ('Valentino', 'Rossi', 'vale46@gmail.com', 'c/italia', 12345, '', '')
         ('Marc', 'Marquez', 'marc@gmail.com', 'c/honda', 12345, '', '')}
         */

    }


}
