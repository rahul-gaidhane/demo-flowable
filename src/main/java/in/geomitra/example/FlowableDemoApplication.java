package in.geomitra.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import in.geomitra.example.service.MyService;

@SpringBootApplication
public class FlowableDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableDemoApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner init(final MyService myService) {

	    return new CommandLineRunner() {
	        public void run(String... strings) throws Exception {
	            myService.createDemoUsers();
	        }
	    };
	}
}
