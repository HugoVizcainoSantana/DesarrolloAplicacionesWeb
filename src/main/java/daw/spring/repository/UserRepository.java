package daw.spring.repository;

import daw.spring.model.Home;
import daw.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findAllByFirstName(String name);

    User findUserByFirstName(String firstName);

    User findUserByEmail(String email);


    User findUserByHomeListEquals(Home home);
    
    Optional<User> findByEmail(String email);
	
    Optional<User> findByResetToken(String resetToken);

}
