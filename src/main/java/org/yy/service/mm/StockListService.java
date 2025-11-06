package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
public interface StockListService{

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

	/**出入库单号验重
	 * @param pd
	 * @return
	 */
	public PageData getRepeatNum(PageData pd)throws Exception;

	/**审核或反审核单据
	 * @param pd
	 */
	public void editAudit(PageData pd)throws Exception;

	/**获取编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getDocumentNoList(PageData pd)throws Exception;

	/**入厂记录列表
	 * @param page
	 * @return
	 */
	public List<PageData> listInRecord(Page page)throws Exception;

	/**导出入厂记录到excel
	 * @param pd
	 * @return
	 */
	public List<PageData> exportInRecord(PageData pd)throws Exception;

	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> appListIn(AppPage page)throws Exception;

	/**出入厂记录列表
	 * @param page
	 * @return
	 */
	public List<PageData> listInOutRecord(Page page)throws Exception;
}

