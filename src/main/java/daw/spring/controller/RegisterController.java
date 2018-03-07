package daw.spring.controller;

import daw.spring.model.Roles;
import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public RegisterController(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String tryRegister(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {
        User user = new User(firstName, lastName, email, encoder.encode(password), null, null, null, null, null, Roles.USER.getRoleName());
        userRepository.save(user);
        return "redirect:/dashboard/";
    }

}
