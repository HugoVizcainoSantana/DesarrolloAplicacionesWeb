package daw.spring.service;


import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findByNombre(String term) {
        return productRepository.findByDescription(term);
    }

    public Product findOneById(Long id) {
        return productRepository.findOne(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void updateStockProduct(Long id, long stock, double cost, String description) {
        Product productE = productRepository.findOne(id);
        productE.setStock(stock);
        productE.setCost(cost);
        productE.setDescription(description);
        productRepository.save(productE);
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

    public void updateProduct(Product product) {
        productRepository.save(product);
    }


}
