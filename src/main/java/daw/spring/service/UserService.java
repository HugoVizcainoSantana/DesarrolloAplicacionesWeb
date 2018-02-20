package daw.spring.service;

import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

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


public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }    public void saveUser(User user){
        userRepository.save(user);
    }


    @PostConstruct
    public void init() {
        User user1 = new User("Amador", "Rivas", "amador@merengue.com", encoder.encode("1234"), null, Collections.singletonList("USER"));
        saveUser(user1);
        User user2 = new User("Teodoro", "Rivas", "teodoro@merengue.com", encoder.encode("1234"), null, Collections.singletonList("USER"));
        saveUser(user2);
        User userAdmin1 = new User("admin@oncontrolhome.com", encoder.encode("1234"), Collections.singletonList("ADMIN"));
        saveUser(userAdmin1);
    }

   /* public void deleteHome(Home home){
        homeRepository.delete(home);
    }

    public Page<Home> findAllHomePage(PageRequest pageRequest){
        return homeRepository.findAll(pageRequest);
    }*/

}
