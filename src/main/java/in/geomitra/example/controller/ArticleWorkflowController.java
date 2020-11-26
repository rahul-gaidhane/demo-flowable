package in.geomitra.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.geomitra.example.domain.Approval;
import in.geomitra.example.domain.Article;
import in.geomitra.example.service.ArticleWorkflowService;

@RestController
public class ArticleWorkflowController {
	
	@Autowired
	private ArticleWorkflowService service;
	
	@PostMapping("/deploy")
	public void deploy() {
		service.deploy();
	}
	
	@PostMapping("/submit")
	public void submit(@RequestBody Article article) {
		service.startProcess(article);
	}
	
	@GetMapping("/tasks")
	public List<Article> getTasks(@RequestParam String assignee) {
		return service.getTasks(assignee);
	}
	
	@PostMapping("/review")
	public void review(@RequestBody Approval approval) {
		service.submitReview(approval);
	}
}
