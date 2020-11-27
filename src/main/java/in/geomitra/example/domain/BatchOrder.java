package in.geomitra.example.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
@Entity
public class BatchOrder {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String orderNumber;
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private DuplicateStatus duplicateStatus;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	private String createdBy;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
}
