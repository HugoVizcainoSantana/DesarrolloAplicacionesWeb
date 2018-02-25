package daw.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import daw.spring.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCompletedIsFalse();

}
