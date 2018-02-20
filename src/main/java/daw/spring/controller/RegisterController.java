package daw.spring.controller;

import daw.spring.model.Home;
import daw.spring.model.User;
import daw.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,@RequestParam String phone, @RequestParam String passwordHash) {
        List<Home> homeList = new ArrayList<>();
        User user = new User(firstName, lastName, email, passwordHash," ", 0, null, "",phone,null,Collections.singletonList("USER"));
        userRepository.save(user);

        return "login";

    }

}
