package daw.spring.restcontroller;

import daw.spring.model.Product;
import daw.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class IndexRestController {


    private final ProductService productService;

    @Autowired
    public IndexRestController(ProductService productService) {
        this.productService = productService;
    }


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();

        if (products == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Product newProduct(@RequestBody Product newProduct) {
        productService.saveProduct(newProduct);
        return newProduct;
    }

}
