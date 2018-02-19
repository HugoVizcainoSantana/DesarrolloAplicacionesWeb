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

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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

    @PostConstruct
    public void init() {
        Product product1 = new Product("bombilla 45w", 15.50, LIGHT, "product-2.jpg");
        saveProduct(product1);
        Product product2 = new Product("Laminas de aluminio", 32.50, BLIND, "product-1.jpg");
        saveProduct(product2);
    }
}
