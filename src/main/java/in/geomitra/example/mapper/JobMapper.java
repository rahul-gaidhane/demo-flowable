package in.geomitra.example.mapper;

import com.github.javafaker.Faker;

import in.geomitra.example.domain.BatchOrder;
import in.geomitra.example.domain.Job;

public class JobMapper {
	
	public static Job toEntity(BatchOrder ord) {
		Faker faker = new Faker();
		Job job = new Job();
		
		job.setAddress(ord.getAddress());
		job.setOrderNumber(ord.getOrderNumber());
		job.setJobNumber(faker.regexify("J[0-9]{10}"));
		
		return job;
	}

}
