package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseApplyForService{

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
	
	/**更新审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception;
	
	/**更新审批状态
	 * @param pd
	 * @throws Exception
	 */
	public void editAudit(PageData pd)throws Exception;
	
	/**审批列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listAudit(Page page)throws Exception;
	
	/**带明细导出
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listExcelAll(PageData pd)throws Exception;
}

