package daw.spring.controller;

import daw.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    private static final String FILES_FOLDER = ".\\src\\main\\resources\\static\\images";

    private List<String> imageTitles = new ArrayList<>();

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "index";
    }

    @RequestMapping("/index")
    public void index2(Model model) {
        index(model);
    }

}
