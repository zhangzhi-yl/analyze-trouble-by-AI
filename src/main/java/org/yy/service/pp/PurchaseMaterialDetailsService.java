package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购物料明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseMaterialDetailsService{

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

	/**生成行号
	 * @param pd
	 * @return
	 */
	public PageData getRowNum(PageData pd)throws Exception;

	/**行关闭/反行关闭
	 * @param pd
	 */
	public void rowClose(PageData pd)throws Exception;

	/**关联行关闭采购订单明细
	 * @param pd
	 */
	public void deleteMxRelated(PageData pd)throws Exception;

	/**批量选择采购请单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listAllSelect(String[] arrayDATA_IDS)throws Exception;
	
	/**计算下推数量
	 * @param pd
	 */
	public void calFPushCount(PageData pd)throws Exception;

	/**列表不分页
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllX(PageData pd)throws Exception;
	
	/**更新下推数量
	 * @param pd
	 * @throws Exception
	 */
	public void editFPushCount(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPushId(PageData pd)throws Exception;
}

