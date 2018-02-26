package daw.spring.service;


import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional(readOnly = true)
   public List<Product> findByNombre(String term){
	   return productRepository.findByDescription(term);
   }

    public Product findOneById(Long id) {
        return productRepository.findOne(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void updateStockProduct(Long id, long stock , double cost) {
        Product productE=productRepository.findOne(id);
        productE.setStock(stock);
        productE.setCost(cost);
        productRepository.save(productE);
    }

    private void saveProduct(Product product) {
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
        Product product1 = new Product("Actuador de bombilla para domótica.  Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.", 15.50, Product.ProductType.LIGHT, "product-2.jpg", 36);
        saveProduct(product1);

        Product product2 = new Product("Motor actuador de persiana para domótica. Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.", 32.50, Product.ProductType.BLIND, "product-1.jpg",34);
        saveProduct(product2);

        Product product3 = new Product("Raspberry pi programada para domótica. Así, podrás actuar desde la App, ya sea desde dispositivos móviles, ordenador sobre los diferentes elementos domóticos.", 32.50, Product.ProductType.RASPBERRYPI, "raspberry-pie.jpg",67);
        saveProduct(product3);
    }
}
