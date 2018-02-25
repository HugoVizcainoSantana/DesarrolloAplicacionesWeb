package daw.spring.service;

import daw.spring.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daw.spring.repository.OrderRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void saveOrder(Order order) {
		orderRepository.save(order);
	}

	public List<Order> homesOrders() {
		return orderRepository.findByConfirmedIsFalse();
	}


}
