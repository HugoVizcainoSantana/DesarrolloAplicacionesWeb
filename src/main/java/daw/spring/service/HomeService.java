package daw.spring.service;

import daw.spring.model.Home;
import daw.spring.model.Product;
import daw.spring.repository.HomeRepository;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    @Autowired
    HomeRepository homeRepository;

    public Home findOneById(Long id){

        return homeRepository.findOne(id);
    }

    public List<Home> findAllHomes(){
        return homeRepository.findAll();
    }


    public void saveHome(Home home){
        homeRepository.save(home);
    }

    public void deleteHome(Home home){
        homeRepository.delete(home);
    }

    public Page<Home> findAllHomePage(PageRequest pageRequest){
        return homeRepository.findAll(pageRequest);
    }

    public void save(Home home) {
        homeRepository.save(home);
    }

}
