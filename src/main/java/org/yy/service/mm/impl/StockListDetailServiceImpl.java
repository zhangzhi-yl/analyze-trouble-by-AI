package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.StockListDetailMapper;
import org.yy.service.mm.StockListDetailService;

/** 
 * 说明： 出入库单明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StockListDetailServiceImpl implements StockListDetailService{

	@Autowired
	private StockListDetailMapper StockListDetailMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		StockListDetailMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		StockListDetailMapper.delete(pd);
	}
	
	/**根据主表id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByStockList_ID(PageData pd)throws Exception{
		StockListDetailMapper.deleteByStockList_ID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		StockListDetailMapper.edit(pd);
	}
	
	/**更新实际数量、仓库id、库位id
	 * @param pd
	 * @throws Exception
	 */
	public void editPositionAndQty(PageData pd)throws Exception{
		StockListDetailMapper.editPositionAndQty(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return StockListDetailMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return StockListDetailMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return StockListDetailMapper.findById(pd);
	}
	
	/**通过行号和StockList_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByRowNumAndStockList_ID(PageData pd)throws Exception{
		return StockListDetailMapper.findByRowNumAndStockList_ID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		StockListDetailMapper.deleteAll(ArrayDATA_IDS);
	}

	/**关联行关闭
	 * @param pd
	 */
	@Override
	public void rowCloseByMainId(PageData pd) throws Exception {
		StockListDetailMapper.rowCloseByMainId(pd);
	}

	/**行关闭/反行关闭
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		StockListDetailMapper.rowClose(pd);
	}

	/**生成行号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRowNum(PageData pd) throws Exception {
		return StockListDetailMapper.getRowNum(pd);
	}

	/**批量选择采购请单物料列表
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> listAllSelect(String[] arrayDATA_IDS) throws Exception {
		return StockListDetailMapper.listAllSelect(arrayDATA_IDS);
	}

	/**入库红字单添加物料列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllRedMat(PageData pd) throws Exception {
		return StockListDetailMapper.listAllRedMat(pd);
	}

	/**批量选择蓝字入库单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllBlueIn(String[] arrayDATA_IDS) throws Exception {
		return StockListDetailMapper.selectAllBlueIn(arrayDATA_IDS);
	}
	/**审核或反审核单据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllAudit(PageData pd) throws Exception {
		return StockListDetailMapper.listAllAudit(pd);
	}

	/**反写入库单明细下推数量
	 * @param pdVar
	 */
	@Override
	public void calFPushCount(PageData pdVar) throws Exception {
		StockListDetailMapper.calFPushCount(pdVar);
	}


	/**批量选择蓝字出库单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllBlueOut(String[] arrayDATA_IDS) throws Exception {
		return StockListDetailMapper.selectAllBlueOut(arrayDATA_IDS);
	}
	
	/**打印物料列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllPrintMat(PageData pd) throws Exception {
		return StockListDetailMapper.listAllPrintMat(pd);
	}
	
	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> appListIn(AppPage page)throws Exception{
		return StockListDetailMapper.appListIn(page);
	}
}

