package daw.spring.service;


import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
        Product product1 = new Product("Bombilla de 45w preparada para domótica. Las llamadas bombillas LED conectadas son modelos capaces de conectarse a la red domótica de casa o la conexión WiFi para comunicarse con mandos, sistemas de control domóticos con tu móvil/pc.", 15.50, Product.ProductType.LIGHT, "product-2.jpg");
        saveProduct(product1);

        Product product2 = new Product("Laminas de aluminio preparadas para domótica. Así, podrás subir o bajar las persianas desde un mando a distancia, desde nuestros dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.", 32.50, Product.ProductType.BLIND, "product-1.jpg");
        saveProduct(product2);
    }
}
