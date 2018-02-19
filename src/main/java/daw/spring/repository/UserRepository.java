package daw.spring.repository;

import daw.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //metodos de las querys
    // Waiting for security...
}
