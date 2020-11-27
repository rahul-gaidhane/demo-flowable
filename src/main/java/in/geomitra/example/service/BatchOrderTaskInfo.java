package in.geomitra.example.service;

import in.geomitra.example.domain.BatchOrderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BatchOrderTaskInfo extends BatchOrderInfo {
	
	private String taskId;
	
	public BatchOrderTaskInfo(BatchOrderInfo ordInfo, String taskId) {
		super(ordInfo.getOrderNumber(), ordInfo.getAddress(), ordInfo.getCreatedBy(), ordInfo.getOrderId());
		this.taskId = taskId;
	}
}
