package daw.spring.service;

import daw.spring.model.Home;
import daw.spring.model.User;
import daw.spring.repository.HomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final HomeRepository homeRepository;

    @Autowired
    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public Home findOneById(Long id) {
        return homeRepository.findOne(id);
    }

    public List<Home> findAllHomes() {
        return homeRepository.findAll();
    }

    public void saveHome(Home home) {
        homeRepository.save(home);
    }

    public void deleteHome(Home home) {
        homeRepository.delete(home);
    }

    public long countHomeActives() {
        return homeRepository.countHomeByActivatedIsTrue();
    }

    public Page<Home> findAllHomePage(PageRequest pageRequest) {
        return homeRepository.findAll(pageRequest);
    }

    public List<Home> homesOrders() {
        return homeRepository.findByActivatedIsFalse();
    }

    public void activeHome(Home home) {
        home.setActivated(true);
        saveHome(home);
    }

    public List<Home> getHomesFromUser(User user) {
        return user.getHomeList();
    }

}
