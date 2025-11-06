package org.yy.mapper.dsno1.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工艺工序实例Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface WorkingProcedureExampleMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	void editStatus(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	/**
	 * 根据bom id获取 工艺工序实例列表
	 * @param bomNum
	 * @return
	 * @throws Exception
	 */
	List<PageData> getWorkingProcedureExampleListByBOMId(String BOM_ID);

	/**获取工序流程图列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllFlow(PageData pd);

	/**删除BOM关联删除实例工序
	 * @param pd
	 */
	void deleteByBomId(PageData pd);
	
}

