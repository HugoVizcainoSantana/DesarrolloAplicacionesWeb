package daw.spring.controller;

import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static daw.spring.model.Product.ProductType.BLIND;
import static daw.spring.model.Product.ProductType.LIGHT;
import static daw.spring.model.Product.StateType.*;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    private static final String FILES_FOLDER = ".\\src\\main\\resources\\static\\images";

    private List<String> imageTitles = new ArrayList<>();



    @PostConstruct
    public void init(){

        Product product1 = new Product (1, "bombilla 45w", 15.50, LIGHT, ON ,"product-2.jpg");
        productRepository.save(product1);
        Product product2 = new Product (2, "Laminas de aluminio", 32.50, BLIND, DOWN, "product-1.jpg");
        productRepository.save(product2);
    }



}
