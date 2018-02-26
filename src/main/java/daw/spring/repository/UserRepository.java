package daw.spring.repository;

import daw.spring.model.Home;
import daw.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findAllByFirstName(String name);

    User findUserByFirstName(String firstName);

    User findUserByEmail(String email);


    User findUserByHomeListEquals(Home home);

}
