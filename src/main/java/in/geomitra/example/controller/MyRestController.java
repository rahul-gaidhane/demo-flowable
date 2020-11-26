package in.geomitra.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.geomitra.example.service.MyService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController
public class MyRestController {
	
	@Autowired
	private MyService myService;
	
	@PostMapping(value = "/process")
	public void startProcessInstace() {
		myService.startProcess();
	}
	
	@RequestMapping(value="/tasks", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
		List<Task> tasks = myService.getTasks(assignee);
		
		List<TaskRepresentation> dtos = new ArrayList<>();
		
		for(Task task : tasks) {
			dtos.add(new TaskRepresentation(task.getId(), task.getName()));
		}
		
		return dtos;
	}
	
	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	static class TaskRepresentation {
		
		private String id;
		
		private String name;
	}
}
