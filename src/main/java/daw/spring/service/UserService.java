package daw.spring.service;

import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.model.Roles;
import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOneById(Long id) {
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public long countAllUsers() {
        return userRepository.count();
    }

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
        List<Home> listHome = user.getHomeList();
        listHome.add(home);
        //user.setHomeList(listHome);
        saveUser(user);
    }

    public List<Device> getUserFavoriteDevices(User user) {
        List<Device> favoriteDevices = new ArrayList<>();
        for (Home home : user.getHomeList()) {
            for (Device device : home.getDeviceList()) {
                if (device.isFavorite())
                    favoriteDevices.add(device);
            }
        }
        return favoriteDevices;
    }


    public User findUserByHomeId(Home home) {
        return userRepository.findUserByHomeListEquals(home);
    }

    public List<Home> getUserHomesActivated(User user) {
        List<Home> homesListIn = user.getHomeList();
        List<Home> homesListOut = new ArrayList<>();
        for (Home home : homesListIn) {
            if (home.getActivated()) {
                List<Device> devicesOfHome = home.getDeviceList();
                List<Device> devicesListOut = new ArrayList<>();
                for (Device device : devicesOfHome) {
                    if (device.isActivated()) {
                        if ((device.getStatus() == Device.StateType.UP) || (device.getStatus() == Device.StateType.ON)) {
                            device.setActivatedStatus(true);
                        } else {
                            device.setActivatedStatus(false);
                        }
                        devicesListOut.add(device);
                    }
                }
                home.setDeviceList(devicesListOut);
                homesListOut.add(home);
            }
        }
        return homesListOut;
    }

    public boolean userIsOwnerOf(User user, Home home) {
        for (Home h : user.getHomeList()) {
            if (home.getId() == h.getId())
                return true;
        }
        return false;
    }

    public boolean userIsOwnerOf(User user, Device device) {
        for (Home home : user.getHomeList()) {
            for (Device d : home.getDeviceList()) {
                if (device.getId() == d.getId())
                    return true;
            }
        }
        return false;
    }

    public List<User> getAllAdmins() {
        return userRepository.findAllByRolesEquals(Roles.ADMIN.getRoleName());
    }
}
