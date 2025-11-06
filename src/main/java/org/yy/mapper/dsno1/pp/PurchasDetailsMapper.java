package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchasDetailsMapper{

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

	/**关联行关闭采购申请明细
	 * @param pd
	 */
	void rowCloseByApplyId(PageData pd);

	/**行关闭/反行关闭
	 * @param pd
	 */
	void rowClose(PageData pd);

	/**生成行号
	 * @param pd
	 * @return
	 */
	PageData getRowNum(PageData pd);

	/**采购申请明细-物料列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllMat(PageData pd);

	/**批量选择采购申请物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> selectAllCGSQ(String[] arrayDATA_IDS);

	/**反写采购申请明细下推数量
	 * @param pdSave
	 */
	void calFPushCount(PageData pdSave);
	
	/**修改物料清单的下推状态
	 * @param pd
	 * @throws Exception
	 */
	void updateFIsPush(PageData pd);
	
}

