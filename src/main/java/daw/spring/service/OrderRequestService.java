package daw.spring.service;

import daw.spring.model.Home;
import daw.spring.model.OrderRequest;
import daw.spring.repository.OrderRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

	public List<OrderRequest> homesOrdersList() {
		return orderRequestRepository.findByCompletedIsFalseOrCompletedIsFalse();
	}

    public void confirmOrder(long id) {
	    OrderRequest orderConfirm= orderRequestRepository.findOne(id);
	    orderConfirm.setCompleted(true);
	    saveOrder(orderConfirm);
    }

    public void deleteOrder(long id) {
	    orderRequestRepository.delete(id);

    }
    public OrderRequest findOrder(Long id){
    	
    		return orderRequestRepository.findOrderRequest(id);
    	
    }

	public Page<OrderRequest> findAll(PageRequest pageRequest) {
		return orderRequestRepository.findAll(pageRequest);
	}


	public Page<OrderRequest> findNotCompletedOrdersAllPage( PageRequest pageRequest) {
		return orderRequestRepository.findAllByCompletedIsFalse(pageRequest);
	}

	public Page<OrderRequest> findCompletedOrdersAllPage( PageRequest pageRequest) {
		return orderRequestRepository.findAllByCompletedIsTrue(pageRequest);
	}


	public List<OrderRequest> findNotCompletedOrdersAll() {
		return orderRequestRepository.findByCompletedIsFalseOrCompletedIsFalse();
	}

	public List<OrderRequest> findCompletedOrdersAll() {
		return orderRequestRepository.findByCompletedIsTrueOrCompletedIsTrue();
	}

    public List<OrderRequest> findNotCompletedOrders (List<Home> homes){
    		return orderRequestRepository.findByCompletedIsFalseAndHomeIn(homes);
    }

	public List<OrderRequest> findAllHomes(List<Home> homes) {
		return orderRequestRepository.findAllByHomeIn(homes);
	}
}
