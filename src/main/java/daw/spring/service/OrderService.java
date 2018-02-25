package daw.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daw.spring.model.Order;
import daw.spring.repository.OrderRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository= orderRepository;
	}
	
	public void saveOrder (Order order) {
		orderRepository.save(order);
	}

}
