package daw.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

//Entry point
@SpringBootApplication
@Controller
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
