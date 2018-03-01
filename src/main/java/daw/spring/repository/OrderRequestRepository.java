package daw.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import daw.spring.model.OrderRequest;

import java.util.Collection;
import java.util.List;
import daw.spring.model.Home;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

    List<OrderRequest> findByCompletedIsFalse();
    List<OrderRequest> findByCompletedIsFalseAndHomeIn(Collection<Home> homes);
    
    @Query(value="SELECT * FROM order_request u WHERE u.home_id = ?1", nativeQuery = true)
    OrderRequest findOrderRequest(long id );
    
    

}
