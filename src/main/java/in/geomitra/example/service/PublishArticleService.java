package in.geomitra.example.service;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.geomitra.example.domain.Article;
import in.geomitra.example.domain.Status;
import in.geomitra.example.repository.ArticleRepository;

@Service
public class PublishArticleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PublishArticleService.class);
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public void updateStatus(DelegateExecution execution) {
		String auth = (String)execution.getVariable("author");
		LOGGER.debug("auth : " + auth);
		
		String url = (String)execution.getVariable("url");
		LOGGER.debug("url : " + url);
		
		Long id = (Long)execution.getVariable("articleId");
		LOGGER.debug("articleId : " + id);
		
		LOGGER.debug("repo bean : " + articleRepository);
		
		Optional<Article> foundArticle = articleRepository.findById(id);
		
		if(foundArticle.isEmpty()) {
			LOGGER.debug("not found");
		}
		
		Article art = foundArticle.get();
		art.setStatus(Status.PUBLISHED);
		
		articleRepository.save(art);
		LOGGER.info("Publishing the approved article");
	}
}
