package daw.spring.controller;

import daw.spring.model.Home;
import daw.spring.model.Product;
import daw.spring.service.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;



@Controller
public class HomeController {

    /*@Autowired
    private HomeService homeService;

    private static final String FILES_FOLDER = ".\\src\\main\\resources\\static\\images";

    private List<String> imageTitles = new ArrayList<>();

    @RequestMapping("index/homes")
    public String indexConHomes(Model model){
        model.addAttribute("homes",homeService.findAllHomes());
        return "index";
    }

    @PostConstruct
    public void init(){


        //Home home1 = new Home (1, 28007, "c/ibiza", true, List< Product > productList);
        //homeService.save(home1);
        //Home home2 = new Home (2, 28045, "c/alfonso xII, Boolean activated, List<Product> productList);
        //homeService.save(home2);
    }*/

}
