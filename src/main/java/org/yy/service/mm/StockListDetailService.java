package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
public interface StockListDetailService{

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
	
	/**根据主表id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByStockList_ID(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**更新实际数量、仓库id、库位id
	 * @param pd
	 * @throws Exception
	 */
	public void editPositionAndQty(PageData pd)throws Exception;
	
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
	
	/**通过行号和StockList_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByRowNumAndStockList_ID(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**关联行关闭
	 * @param pd
	 */
	public void rowCloseByMainId(PageData pd)throws Exception;

	/**行关闭/反行关闭
	 * @param pd
	 */
	public void rowClose(PageData pd)throws Exception;

	/**生成行号
	 * @param pd
	 * @return
	 */
	public PageData getRowNum(PageData pd)throws Exception;

	/**批量选择采购请单物料列表
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listAllSelect(String[] arrayDATA_IDS)throws Exception;

	/**入库红字单添加物料列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllRedMat(PageData pd)throws Exception;

	/**批量选择蓝字入库单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllBlueIn(String[] arrayDATA_IDS)throws Exception;

	/**审核或反审核单据
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllAudit(PageData pd)throws Exception;

	/**反写入库单明细下推数量
	 * @param pdVar
	 */
	public void calFPushCount(PageData pdVar)throws Exception;

	/**批量选择蓝字出库单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllBlueOut(String[] arrayDATA_IDS)throws Exception;

	/**打印物料列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllPrintMat(PageData pd)throws Exception;
	
	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> appListIn(AppPage page)throws Exception;
}

