package daw.spring.repository;

import daw.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByFirstName(String name);
    User findUserByFirstName(String firstName);
    User findUserByEmail(String email);
}
