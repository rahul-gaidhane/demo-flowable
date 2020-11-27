package in.geomitra.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private ArticleRepository articleRepository;
	
	@Transactional
	public void startProcess(ArticleRequest article) {
		LOGGER.debug("Service to send article for review...");
		
		Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.getAuthor());
        variables.put("url", article.getUrl());
        
        Article art = new Article(null, article.getAuthor(), article.getUrl(), Status.APPROVAL_PENDING);
        
        Article savedArt = articleRepository.save(art);
        
        LOGGER.debug("art : " + savedArt);
        variables.put("articleId", savedArt.getId());
        runtimeService.startProcessInstanceByKey("articleReview", variables);
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
		LOGGER.debug("Service to submit approval for article...");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approved", approval.getStatus());
		taskService.complete(approval.getId(), variables);
	}	
}
