package daw.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import daw.spring.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
