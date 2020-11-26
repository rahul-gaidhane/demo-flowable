package in.geomitra.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.geomitra.example.domain.Approval;
import in.geomitra.example.domain.Article;

@Service
public class ArticleWorkflowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleWorkflowService.class);
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repoService;
	
	public void deploy() {
		LOGGER.info("Service to deploy process defination...");
		
		repoService.createDeployment()
					.addClasspathResource("processes/article-workflow.bpmn20.xml")
					.deploy();
	}
	
	public void startProcess(Article article) {
		Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.getAuthor());
        variables.put("url", article.getUrl());
        runtimeService.startProcessInstanceById("articleReview", variables);
	}

	public List<Article> getTasks(String assignee) {
		List<Task> tasks = taskService.createTaskQuery()
				.taskCandidateGroup(assignee)
				.list();
		
		return tasks.stream().map(task -> {
			Map<String, Object> variables =	taskService.getVariables(task.getId());
			return new Article(task.getId(), (String)variables.get("author"), (String) variables.get("url"));		
		})
		.collect(Collectors.toList());
	}

	public void submitReview(Approval approval) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approved", approval.getStatus());
		taskService.complete(approval.getId(), variables);
	}	
}
