package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseApplyService{

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

	/**单号验重
	 * @param pd
	 * @return
	 */
	public PageData getRepeatNum(PageData pd)throws Exception;

	/**审核或反审核采购申请
	 * @param pd
	 */
	public void editAudit(PageData pd)throws Exception;

	/**获取采购申请编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getCGNumList(PageData pd)throws Exception;
	
	/**根据销售订单ID获取单据下物料
	 * @param pd
	 * @return
	 */
	public List<PageData> getWLNumList(PageData pd)throws Exception;
	
}

