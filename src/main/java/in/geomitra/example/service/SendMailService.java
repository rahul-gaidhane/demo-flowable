package in.geomitra.example.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.geomitra.example.domain.Article;
import in.geomitra.example.domain.Status;
import in.geomitra.example.repository.ArticleRepository;

@Service
public class SendMailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PublishArticleService.class);
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public void sendRejectionMail(DelegateExecution execution) {
		LOGGER.debug("Service to send rejection mail");
		
		Long id = (Long)execution.getVariable("articleId");
		
		Article article = articleRepository.findById(id).get();
		article.setStatus(Status.REJECTED);
		
		articleRepository.save(article);
	}
}
