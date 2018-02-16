package daw.spring.service;




import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;


import daw.spring.model.User;
import daw.spring.repository.UserRepository;

public class UserService {

    //metodos gets usuarios --llama al repositorio y ejecuta la accion
	
	
	
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


   
}
