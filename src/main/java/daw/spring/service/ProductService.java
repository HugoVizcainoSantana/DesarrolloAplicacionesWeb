package daw.spring.service;


import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static daw.spring.model.Product.ProductType.BLIND;
import static daw.spring.model.Product.ProductType.LIGHT;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product findOneById(Long id) {

        return productRepository.findOne(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }


    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public Page<Product> findAllProductPage(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    public void save(Product product1) {
        productRepository.save(product1);
    }

    @PostConstruct
    public void init() {
        Product product1 = new Product(1, "bombilla 45w", 15.50, LIGHT, "product-2.jpg");
        save(product1);
        Product product2 = new Product(2, "Laminas de aluminio", 32.50, BLIND, "product-1.jpg");
        save(product2);
    }
}
