package org.yy.service.pp;

import java.util.List;
import java.util.Map;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface WorkorderProcessIOExampleService {

	List<PageData> listByProcessWorkOrderExampleID(String processWorkOrderExample_ID);

	/**
	 * 根据工艺工单工序实例id查询投入产出实例列表
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findIOExampleByProcessWorkOrderExampleID(PageData pd);
	
	void edit(PageData pqData);

	List<PageData> listAll(PageData pd);

	void save(PageData pd);

	void delete(PageData pd);
	public List<PageData> list(Page page)throws Exception;
	/**
	 *	 调整投入产出实例数量及描述
	 * @param pd
	 * @return 
	 */
	void editPlannedQuantityAndFRemarks(PageData pd)throws Exception;
	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	List<PageData> getOutput(PageData appProcessWorkOrderExampleDetailByPK);
}
