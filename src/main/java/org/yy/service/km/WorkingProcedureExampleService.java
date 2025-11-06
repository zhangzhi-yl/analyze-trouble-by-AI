package org.yy.service.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工艺工序实例接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface WorkingProcedureExampleService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editStatus(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 根据bom id获取 工艺工序实例列表
	 * @param bomNum
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getWorkingProcedureExampleListByBOMId(String BOM_ID)  throws Exception;

	/**获取工序流程图列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllFlow(PageData pd) throws Exception;

	/**删除BOM关联删除实例工序
	 * @param pd
	 */
	public void deleteByBomId(PageData pd) throws Exception;
}

