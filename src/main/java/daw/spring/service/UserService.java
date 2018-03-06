package daw.spring.service;

import daw.spring.model.*;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
   /* private final BCryptPasswordEncoder encoder;
    private final OrderRequestService orderRequestService;
    private final HomeService homeService;
    private final DeviceService deviceService;*/

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
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
    		//user.setHomeList(listHome);
    		saveUser(user);
    }
    
    public List<Device> getUserFavoriteDevices(User user){
    		List<Device> favoriteDevices= new ArrayList<>();
    		for (Home home : user.getHomeList()) {
				for (Device device : home.getDeviceList()) {
					if(device.isFavorite())
						favoriteDevices.add(device);
				}
			}
    		return favoriteDevices;
    }
    

    public User findUserByHomeId(Home home) {
        return userRepository.findUserByHomeListEquals(home);
    }
    public Optional findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
    public Optional findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}

}
