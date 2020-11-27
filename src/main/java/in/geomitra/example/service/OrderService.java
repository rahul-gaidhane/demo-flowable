package in.geomitra.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import in.geomitra.example.domain.DuplicateStatus;
import in.geomitra.example.domain.BatchOrder;
import in.geomitra.example.domain.OrderInfo;
import in.geomitra.example.domain.PaymentStatus;
import in.geomitra.example.repository.OrderRepository;
import in.geomitra.example.request.UpdatePaymentStatus;

@Service
public class OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	public void updatePaymentStatus(UpdatePaymentStatus status) throws Exception {
		LOGGER.debug("Service to update payment status...");
		
		Optional<BatchOrder> foundOrd = orderRepository.findById(status.getOrderId());
		
		if(foundOrd.isEmpty()) {
			LOGGER.error("Order not found for the given order : " + status.getId());
			throw new Exception("Order not found for the given order : " + status.getId());
		}
		
		BatchOrder ord = foundOrd.get();
		
		if(status.getStatus()) {
			ord.setPaymentStatus(PaymentStatus.COMPLETED);
		} else {
			ord.setPaymentStatus(PaymentStatus.FAILED);
		}
		
		orderRepository.save(ord);
		
		Map<String, Object> vars = new HashMap<>();
		vars.put("paymentSuccess", status.getStatus());
		
		taskService.complete(status.getId(), vars);
	}
	
	public List<OrderInfo> getTasks(String assignee) {
		LOGGER.debug("Service to get tasks...");
		List<Task> tasks = taskService.createTaskQuery()
				.taskAssignee(assignee)
				.list();
		
		LOGGER.debug("Number of tasks found : " + tasks.size());
		
		return tasks.stream().map(task -> {
			
			Map<String, Object> vars =	taskService.getVariables(task.getId());
			
			return new OrderInfo(task.getId(), 
									(String)vars.get("orderNumber"), 
									(String)vars.get("address"), 
									(PaymentStatus)vars.get("paymentStatus"), 
									(DuplicateStatus)vars.get("duplicateStatus"),
									(String)vars.get("createdBy"), 
									(Long)vars.get("orderId"));		
		})
		.collect(Collectors.toList());
	}
	
	public void conformOrder(DelegateExecution exec) {
		LOGGER.debug("Service to conform order...");
	}
	
	public void paymentPending(DelegateExecution exec) {
		LOGGER.debug("Service to payment pending...");
	}

	public void create(String name) {
		LOGGER.debug("Service to create order..." + name);
		
		Faker faker = new Faker();
		
		BatchOrder order = new BatchOrder();
		order.setAddress(faker.address().fullAddress());
		order.setDuplicateStatus(DuplicateStatus.NONE);
		order.setOrderNumber(faker.regexify("G[0-9]{10}"));
		order.setPaymentStatus(PaymentStatus.PENDING);
		order.setCreatedBy(name);
		
		BatchOrder savedOrder = orderRepository.save(order);
		
		Map<String, Object> vars = new HashMap<>();
		vars.put("address", order.getAddress());
		vars.put("duplicateStatus", order.getDuplicateStatus());
		vars.put("orderNumber", order.getOrderNumber());
		vars.put("paymentStatus", order.getPaymentStatus());
		vars.put("createdBy", order.getCreatedBy());
		vars.put("orderId", savedOrder.getId());
		
		runtimeService.startProcessInstanceByKey("orderProcess", vars);
	}
}
