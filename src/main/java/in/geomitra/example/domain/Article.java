package in.geomitra.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String author;
	
	private String url;
	
	private Status status;
}
