package daw.spring.service;

import daw.spring.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daw.spring.repository.OrderRequestRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderRequestService {

	private final OrderRequestRepository orderRequestRepository;

	@Autowired
	public OrderRequestService(OrderRequestRepository orderRequestRepository) {
		this.orderRequestRepository = orderRequestRepository;
	}

	public void saveOrder(OrderRequest order) {
		orderRequestRepository.save(order);
	}

	public OrderRequest finOneById(long id) {
		return orderRequestRepository.findOne(id);
	}

	public List<OrderRequest> homesOrders() {
		return orderRequestRepository.findByisOkIsFalse();
	}


    public void confirmOrder(long id) {
	    OrderRequest orderConfirm= orderRequestRepository.findOne(id);
	    orderConfirm.setConfirmed(true);
	    saveOrder(orderConfirm);
    }

    public void deleteOrder(long id) {
	    orderRequestRepository.delete(id);

    }
}
