package in.geomitra.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.geomitra.example.domain.BatchOrder;

@Repository
public interface OrderRepository extends JpaRepository<BatchOrder, Long> {

}
