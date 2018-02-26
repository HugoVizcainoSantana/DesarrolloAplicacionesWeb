package daw.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import daw.spring.model.OrderRequest;

import java.util.List;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

    List<OrderRequest> findByisOkIsFalse();
    

}
