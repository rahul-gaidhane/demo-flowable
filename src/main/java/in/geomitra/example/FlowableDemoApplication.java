package in.geomitra.example;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FlowableDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(final RepositoryService repoService, 
									final RuntimeService runtimeService,
									final TaskService taskService) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				System.out.println("Number of process definitions : "
	                    + repoService.createProcessDefinitionQuery().count());
	                System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
	                runtimeService.startProcessInstanceByKey("articleReview");
	                System.out.println("Number of tasks after process start: "
	                    + taskService.createTaskQuery().count());
				
			}
		};
	}
}
