package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 检验单主模板接口
 * 作者：YuanYe
 * 时间：2020-02-21
 * 
 * @version
 */
public interface DOCUMENTTEMPLATEService{

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
	
	/**修改发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception;
	
	/**模板名称根据工单关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getTemplate(PageData pd)throws Exception;
	
	/**修改SQL
	 * @param pd
	 * @throws Exception
	 */
	public void editSql(PageData pd)throws Exception;
	
	/**查询模板名称及ID
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByStencil(PageData pd)throws Exception;
}

