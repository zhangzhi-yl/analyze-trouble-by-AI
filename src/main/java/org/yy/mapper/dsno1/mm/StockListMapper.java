package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 * @version
 */
public interface StockListMapper{

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

	/**出入库单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**审核或反审核单据
	 * @param pd
	 */
	void editAudit(PageData pd);

	/**审核或反审核单据
	 * @param pd
	 * @return
	 */
	List<PageData> listAllAudit(PageData pd);

	/**获取编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getDocumentNoList(PageData pd);

	/**入厂记录列表
	 * @param page
	 * @return
	 */
	List<PageData> listInRecordlistPage(Page page);

	/**导出入厂记录到excel
	 * @param pd
	 * @return
	 */
	List<PageData> exportInRecord(PageData pd);
	
	/**手机端列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> appListIn(AppPage page);

	/**出入厂记录列表
	 * @param page
	 * @return
	 */
	List<PageData> listInOutRecordlistPage(Page page);
}

