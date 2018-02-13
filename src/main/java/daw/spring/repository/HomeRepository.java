package daw.spring.repository;

import daw.spring.model.Home;
import daw.spring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Home, Long> {
}
