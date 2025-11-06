package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.StockListMapper;
import org.yy.service.mm.StockListService;

/** 
 * 说明： 出入库单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StockListServiceImpl implements StockListService{

	@Autowired
	private StockListMapper StockListMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		StockListMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		StockListMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		StockListMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return StockListMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return StockListMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return StockListMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		StockListMapper.deleteAll(ArrayDATA_IDS);
	}

	/**出入库单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return StockListMapper.getRepeatNum(pd);
	}

	/**审核或反审核单据
	 * @param pd
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		StockListMapper.editAudit(pd);
	}

	/**获取编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getDocumentNoList(PageData pd) throws Exception {
		return StockListMapper.getDocumentNoList(pd);
	}

	/**入厂记录列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listInRecord(Page page) throws Exception {
		return StockListMapper.listInRecordlistPage(page);
	}


	/**导出入厂记录到excel
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> exportInRecord(PageData pd) throws Exception {
		return StockListMapper.exportInRecord(pd);
	}
	
	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> appListIn(AppPage page)throws Exception{
		return StockListMapper.appListIn(page);
	}

	/**出入厂记录列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listInOutRecord(Page page) throws Exception {
		return StockListMapper.listInOutRecordlistPage(page);
	}
}

