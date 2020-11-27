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
import in.geomitra.example.domain.BatchOrderInfo;
import in.geomitra.example.domain.OrderStatus;
import in.geomitra.example.domain.PaymentStatus;
import in.geomitra.example.domain.UpdateDuplicateStatus;
import in.geomitra.example.mapper.BatchOrderMapper;
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
	
	
	private BatchOrder findOrder(Long id) throws Exception {
		Optional<BatchOrder> foundOrd = orderRepository.findById(id);
		
		if(foundOrd.isEmpty()) {
			LOGGER.error("Order not found for the given order : " + id);
			throw new Exception("Order not found for the given order : " + id);
		}
		
		return foundOrd.get();
	}
	
	public void updatePaymentStatus(UpdatePaymentStatus status) throws Exception {
		LOGGER.debug("Service to update payment status...");
		
		Map<String, Object> vars = new HashMap<>();
		vars.put("paymentSuccess", status.getStatus());
		
		taskService.complete(status.getId(), vars);
	}
	
	public List<BatchOrderInfo> getTasks(String assignee) {
		LOGGER.debug("Service to get tasks...");
		List<Task> tasks = taskService.createTaskQuery()
				.taskAssignee(assignee)
				.list();
		
		LOGGER.debug("Number of tasks found : " + tasks.size());
		
		return tasks.stream().map(task -> {
			
			Map<String, Object> vars =	taskService.getVariables(task.getId());
			
			BatchOrderInfo ordInfo = (BatchOrderInfo)vars.get("order");
			
			return new BatchOrderTaskInfo(ordInfo, task.getId());		
		})
		.collect(Collectors.toList());
	}
	
	public void conformOrder(DelegateExecution exec) throws Exception {
		LOGGER.debug("Service to conform order...");
		
		BatchOrderInfo ordInfo = (BatchOrderInfo)exec.getVariable("order");
		
		BatchOrder ord = findOrder(ordInfo.getOrderId());
		
		ord.setOrderStatus(OrderStatus.CONFIRMED);
		ord.setPaymentStatus(PaymentStatus.COMPLETED);
		
		orderRepository.save(ord);
	}
	
	public void paymentPending(DelegateExecution exec) throws Exception {
		LOGGER.debug("Service to payment pending...");
		
		BatchOrderInfo ordInfo = (BatchOrderInfo)exec.getVariable("order");
		
		BatchOrder ord = findOrder(ordInfo.getOrderId());
		
		ord.setPaymentStatus(PaymentStatus.FAILED);
		
		orderRepository.save(ord);
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
		order.setOrderStatus(OrderStatus.NOT_CONFIRMED);
		
		BatchOrder savedOrder = orderRepository.save(order);
		
		BatchOrderInfo ordInfo = BatchOrderMapper.toInfo(savedOrder);
		
		Map<String, Object> vars = new HashMap<>();
		vars.put("order", ordInfo);
		
		runtimeService.startProcessInstanceByKey("orderProcess", vars);
	}

	public void updateDuplicateStatus(UpdateDuplicateStatus status) {
		LOGGER.debug("Service to update duplicate status...");
		
		Map<String, Object> vars = new HashMap<>();
		vars.put("isDuplicate", status.getDuplicateStatus());
		
		taskService.complete(status.getTaskId(), vars);
	}
	
	public void suspendOrder(DelegateExecution exec) throws Exception {
		LOGGER.debug("Service to suspend order...");
		
		BatchOrderInfo ordInfo = (BatchOrderInfo)exec.getVariable("order");
		
		BatchOrder ord = findOrder(ordInfo.getOrderId());
		ord.setDuplicateStatus(DuplicateStatus.RESOLVE_DUPLICATE);
		ord.setOrderStatus(OrderStatus.SUSPENDED);
		
		orderRepository.save(ord);
	}
	
	public void createJob(DelegateExecution exec) throws Exception {
		LOGGER.debug("Service to create job...");
		
		BatchOrderInfo ordInfo = (BatchOrderInfo)exec.getVariable("order");
		
		BatchOrder ord = findOrder(ordInfo.getOrderId());
		
		ord.setOrderStatus(OrderStatus.IN_PROCESS);
		ord.setDuplicateStatus(DuplicateStatus.NO_DUPLICATE);
		
		orderRepository.save(ord);
	}
}
