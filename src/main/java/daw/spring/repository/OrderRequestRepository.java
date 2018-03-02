package daw.spring.repository;


import daw.spring.model.Home;
import daw.spring.model.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

    List<OrderRequest> findByCompletedIsFalse();
    List<OrderRequest> findByCompletedIsFalseAndHomeIn(Collection<Home> homes);

    List<OrderRequest> findAllByHomeIn(Collection<Home> homes);

    @Query(value="SELECT * FROM order_request u WHERE u.home_id = ?1", nativeQuery = true)
    OrderRequest findOrderRequest(long id );
    
    

}
