package daw.spring.controller;

import daw.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import daw.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private final ProductService productService;

    @Autowired
    public IndexController(ProductService productService) {
        this.productService = productService;
    }

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
