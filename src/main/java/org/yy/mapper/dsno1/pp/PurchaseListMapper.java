package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购订单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseListMapper{

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

	/**审核或反审核采购订单
	 * @param pd
	 */
	void editAudit(PageData pd);

	/**单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**结束
	 * @param pd
	 */
	void over(PageData pd);

	/**获取采购订单编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getFNumList(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listXMCGlistPage(Page page);
	
}

