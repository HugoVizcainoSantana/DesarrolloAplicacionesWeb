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

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public User findOneById(Long id){
        return userRepository.findOne(id);
    }


    public void saveUser(User user){
        userRepository.save(user);
    }



   /* public void deleteHome(Home home){
        homeRepository.delete(home);
    }

    public Page<Home> findAllHomePage(PageRequest pageRequest){
        return homeRepository.findAll(pageRequest);
    }*/


   @PostConstruct
    public void prueba (){
       saveUser(new User(1, "Pepe","Lopez", "a@a.com", "aaa", null));

   }
}
