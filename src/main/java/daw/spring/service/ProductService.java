package daw.spring.service;


import daw.spring.model.Product;
import daw.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired  ProductRepository productRepository;

    public Product findOneById(Long id){

        return productRepository.findOne(id);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }


    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProduct(Product product){
        productRepository.delete(product);
    }

    public Page<Product> findAllProductPage(PageRequest pageRequest){
        return productRepository.findAll(pageRequest);
    }

    public void save(Product product1) {
        productRepository.save(product1);
    }
}
