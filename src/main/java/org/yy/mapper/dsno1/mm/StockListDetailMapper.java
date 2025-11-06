package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
public interface StockListDetailMapper{

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
	
	/**根据主表id删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteByStockList_ID(PageData pd);
	
	/**更新实际数量、仓库id、库位id
	 * @param pd
	 * @throws Exception
	 */
	void editPositionAndQty(PageData pd);
	
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
	
	/**通过行号和StockList_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByRowNumAndStockList_ID(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**关联行关闭
	 * @param pd
	 */
	void rowCloseByMainId(PageData pd);

	/**行关闭/反行关闭
	 * @param pd
	 */
	void rowClose(PageData pd);

	/**生成行号
	 * @param pd
	 * @return
	 */
	PageData getRowNum(PageData pd);

	/**批量选择采购请单物料列表
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listAllSelect(String[] arrayDATA_IDS);

	/**入库红字单添加物料列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllRedMat(PageData pd);

	/**批量选择蓝字入库单物料 
	 * @param arrayDATA_IDS
	 * @param pd 
	 * @return
	 */
	List<PageData> selectAllBlueIn(String[] arrayDATA_IDS);

	/**审核或反审核单据
	 * @param pd
	 * @return
	 */
	List<PageData> listAllAudit(PageData pd);

	/**反写入库单明细下推数量
	 * @param pdVar
	 */
	void calFPushCount(PageData pdVar);

	/**批量选择蓝字出库单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> selectAllBlueOut(String[] arrayDATA_IDS);

	/**打印物料列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllPrintMat(PageData pd);
	
	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> appListIn(AppPage page);
	
}

