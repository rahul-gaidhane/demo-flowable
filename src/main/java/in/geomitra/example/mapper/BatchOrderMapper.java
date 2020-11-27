package in.geomitra.example.mapper;

import in.geomitra.example.domain.BatchOrder;
import in.geomitra.example.domain.BatchOrderInfo;

public class BatchOrderMapper {

	public static BatchOrderInfo toInfo(BatchOrder ord) {
		
		return new BatchOrderInfo(ord.getOrderNumber(), ord.getAddress(), ord.getCreatedBy(), ord.getId());
	}

}
