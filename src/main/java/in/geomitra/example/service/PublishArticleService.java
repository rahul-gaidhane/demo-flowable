package in.geomitra.example.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class PublishArticleService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("Publishing the approved article");
	}
}
