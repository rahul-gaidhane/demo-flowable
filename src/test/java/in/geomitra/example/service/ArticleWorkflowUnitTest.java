package in.geomitra.example.service;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
public class ArticleWorkflowUnitTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleWorkflowUnitTest.class);
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Test
	@Deployment(resources = {"processes/article-workflow.bpmn20.xml"})
	public void articleApprovalTest() {
		Map<String, Object> variables = new HashMap<>();
        variables.put("author", "test@baeldung.com");
        variables.put("url", "http://baeldung.com/dummy");
        
        runtimeService.startProcessInstanceByKey("articleReview", variables);
        
        Task task = taskService.createTaskQuery().singleResult();
        
        LOGGER.info("Task Name : " + task.getName());
   	 
        LOGGER.info("count : " + runtimeService.createProcessInstanceQuery().count());
        runtimeService.createProcessInstanceQuery().list()
        	.forEach(iq -> LOGGER.info("Serviec : " + iq.getProcessVariables().get("author")));
        
        variables.put("approved", true);
        taskService.complete(task.getId(), variables);
 
        LOGGER.info("times : " + runtimeService.createProcessInstanceQuery().count());
        
	}
}
