package in.geomitra.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.geomitra.example.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByUsername(String username); 
}
	