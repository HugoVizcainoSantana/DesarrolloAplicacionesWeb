package daw.spring.repository;

import daw.spring.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p where p.description like %?1%")
	public List<Product> findByDescription(String term);




}
