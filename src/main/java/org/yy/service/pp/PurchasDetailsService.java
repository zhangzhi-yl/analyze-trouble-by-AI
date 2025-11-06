package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchasDetailsService{

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

	/**关联行关闭采购申请明细
	 * @param pd
	 */
	public void rowCloseByApplyId(PageData pd)throws Exception;

	/**行关闭/反行关闭
	 * @param pd
	 */
	public void rowClose(PageData pd)throws Exception;

	/**生成行号
	 * @param pd
	 * @return
	 */
	public PageData getRowNum(PageData pd)throws Exception;

	/**采购申请明细-物料列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllMat(PageData pd)throws Exception;

	/**批量选择采购申请物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllCGSQ(String[] arrayDATA_IDS)throws Exception;

	/**反写采购申请明细下推数量
	 * @param pdSave
	 */
	public void calFPushCount(PageData pdSave)throws Exception;
	
	/**修改物料清单的下推状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateFIsPush(PageData pd)throws Exception;
}

