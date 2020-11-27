package in.geomitra.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.geomitra.example.domain.OrderInfo;
import in.geomitra.example.request.UpdatePaymentStatus;
import in.geomitra.example.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderWorkflowController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderWorkflowController.class);
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public void createOrder(@RequestParam("name") String name) {
		LOGGER.debug("Request to create order...");
		
		orderService.create(name);
	}
	
	@PutMapping("/paymentSuccess")
	public void updatePaymentStatus(@RequestBody UpdatePaymentStatus status) throws Exception {
		LOGGER.debug("Request to update payment status...");
		
		orderService.updatePaymentStatus(status);
	}
	
	@GetMapping("/tasks")
	public List<OrderInfo> getTasks() {
		LOGGER.debug("Service to get tasks...");
		
		return orderService.getTasks("rahul");
	}
}
