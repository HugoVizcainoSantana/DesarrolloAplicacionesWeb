package daw.spring.controller;

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

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "index";
    }

}
