package daw.spring.controller;

import daw.spring.model.Product;
import daw.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

import static daw.spring.model.Product.ProductType.BLIND;
import static daw.spring.model.Product.ProductType.LIGHT;

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
    public String indexConProductos(Model model){
        model.addAttribute("products",productService.findAllProducts());
        return "index";
    }

    @PostConstruct
    public void init(){

        Product product1 = new Product("bombilla 45w", 15.50, LIGHT, "product-2.jpg");
        productService.saveProduct(product1);
        Product product2 = new Product("Laminas de aluminio", 32.50, BLIND, "product-1.jpg");
        productService.saveProduct(product2);
    }



}

