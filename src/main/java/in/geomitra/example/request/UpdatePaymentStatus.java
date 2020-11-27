package in.geomitra.example.request;

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
public class UpdatePaymentStatus {
	
	private String id;
	
	private Long orderId;
	
	private Boolean status;
}
