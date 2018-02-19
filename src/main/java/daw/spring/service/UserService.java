package daw.spring.service;

import daw.spring.model.Home;
import daw.spring.model.User;
import daw.spring.repository.HomeRepository;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import daw.spring.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByName(String name) {
        return userRepository.findUserByFirstName(name);
    }

    public User findOneById(Long id){
        return userRepository.findOne(id);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }

}
