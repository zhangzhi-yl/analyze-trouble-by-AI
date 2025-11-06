package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseApplyMapper{

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

	/**单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**审核或反审核采购申请
	 * @param pd
	 */
	void editAudit(PageData pd);

	/**获取采购申请编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getCGNumList(PageData pd);
	
	/**根据销售订单ID获取单据下物料
	 * @param pd
	 * @return
	 */
	List<PageData> getWLNumList(PageData pd);
}

