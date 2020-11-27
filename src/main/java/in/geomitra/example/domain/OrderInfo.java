package in.geomitra.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInfo {
	
	private String id;
	
	private String orderNumber;
	
	private String address;
	
	private PaymentStatus payStatus;
	
	private DuplicateStatus dupStatus;
	
	private String createdBy;
	
	private Long orderId;
}
