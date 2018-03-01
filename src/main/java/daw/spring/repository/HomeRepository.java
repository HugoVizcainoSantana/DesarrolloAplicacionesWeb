package daw.spring.repository;

import daw.spring.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {
    long countHomeByActivatedIsTrue();
    List<Home> findByActivatedIsFalse();
    
    
    
    //@Query(value = "SELECT home_list_id FROM user_home_list WHERE user_id = ?1", nativeQuery = true)
    //List<Long> findByUserId(long id);
    //List<Long> findAllHomeIdByUserId(long id);
    
    
  
}

