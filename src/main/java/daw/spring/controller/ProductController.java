package daw.spring.controller;

import daw.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*private static final String FILES_FOLDER = ".\\src\\main\\resources\\static\\images";

    private List<String> imageTitles = new ArrayList<>();*/

    @RequestMapping("index/products")
    public String indexConProductos(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "index";
    }


}

