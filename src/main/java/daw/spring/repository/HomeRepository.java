package daw.spring.repository;

import daw.spring.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {
    long countHomeByActivatedIsTrue();
    List<Home> findByActivatedIsFalse();
}

