package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 检验单主模板Mapper
 * 作者：YuanYe
 * 时间：2020-02-21
 * 
 * @version
 */
public interface DOCUMENTTEMPLATEMapper{

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
	
	/**修改发布状态
	 * @param pd
	 * @throws Exception
	 */
	void editIssue(PageData pd);
	
	/**模板名称根据工单关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getTemplate(PageData pd);
	
	/**修改SQL
	 * @param pd
	 * @throws Exception
	 */
	void editSql(PageData pd);
	
	/**查询模板名称及ID
	 * @param pd
	 * @throws Exception
	 */
	PageData findByStencil(PageData pd);
}

