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
import org.springframework.transaction.annotation.Transactional;

import in.geomitra.example.domain.Approval;
import in.geomitra.example.domain.Article;
import in.geomitra.example.domain.ArticleInfo;
import in.geomitra.example.domain.ArticleRequest;
import in.geomitra.example.domain.Status;
import in.geomitra.example.repository.ArticleRepository;

@Service
public class ArticleWorkflowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleWorkflowService.class);
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repoService;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public void deploy() {
		LOGGER.info("Service to deploy process defination...");
		
		repoService.createDeployment()
					.addClasspathResource("processes/article-workflow.bpmn20.xml")
					.deploy();
	}
	
	@Transactional
	public void startProcess(ArticleRequest article) {
		LOGGER.debug("Service to send article for review...");
		
		Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.getAuthor());
        variables.put("url", article.getUrl());
        
        Article art = new Article(null, article.getAuthor(), article.getUrl(), Status.APPROVAL_PENDING);
        
        Article savedArt = articleRepository.save(art);
        
        LOGGER.debug("art : " + savedArt);
        
        runtimeService.startProcessInstanceByKey("articleReview");
	}

	public List<ArticleInfo> getTasks(String assignee) {
		LOGGER.debug("Service to get tasks...");
		List<Task> tasks = taskService.createTaskQuery()
				.taskCandidateGroup(assignee)
				.list();
		
		LOGGER.debug("Number of tasks found : " + tasks.size());
		return tasks.stream().map(task -> {
			Map<String, Object> variables =	taskService.getVariables(task.getId());
			return new ArticleInfo(task.getId(), (String)variables.get("author"), (String) variables.get("url"));		
		})
		.collect(Collectors.toList());
	}

	public void submitReview(Approval approval) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approved", approval.getStatus());
		taskService.complete(approval.getId(), variables);
	}	
}
