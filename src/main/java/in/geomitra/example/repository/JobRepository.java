package in.geomitra.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.geomitra.example.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

}
