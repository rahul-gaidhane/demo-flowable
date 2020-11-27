package in.geomitra.example.domain;

import javax.persistence.Entity;
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
public class Job {

	@Id
	@GeneratedValue
	private Long id;
	
	private String orderNumber;
	
	private String address;
	
	private String jobNumber;
}
