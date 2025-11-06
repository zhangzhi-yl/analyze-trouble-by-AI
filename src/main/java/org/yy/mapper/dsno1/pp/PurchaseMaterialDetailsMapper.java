package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购物料明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseMaterialDetailsMapper{

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

	/**生成行号
	 * @param pd
	 * @return
	 */
	PageData getRowNum(PageData pd);

	/**行关闭/反行关闭
	 * @param pd
	 */
	void rowClose(PageData pd);

	/**关联行关闭采购订单明细
	 * @param pd
	 */
	void deleteMxRelated(PageData pd);

	/**批量选择采购请单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listAllSelect(String[] arrayDATA_IDS);

	/**计算下推数量
	 * @param pd
	 */
	void calFPushCount(PageData pd);

	/**列表不分页
	 * @param pd
	 * @return
	 */
	List<PageData> listAllX(PageData pd);
	
	/**更新下推数量
	 * @param pd
	 * @throws Exception
	 */
	void editFPushCount(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPushId(PageData pd);
}

