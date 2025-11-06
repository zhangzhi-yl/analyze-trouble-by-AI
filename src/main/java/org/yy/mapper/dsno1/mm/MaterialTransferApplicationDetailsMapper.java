package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料转移申请明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferApplicationDetailsMapper{

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
	
	/**根据转移申请单id删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteByMTA_ID(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**修改行关闭状态
	 * @param pd
	 * @throws Exception
	 */
	void editLineClose(PageData pd);
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	void editFStatus(PageData pd);
	
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
	
	/**通过父级id获取明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**合并转入列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> mergeTransferInlist(PageData pd);
	
	/**转入审核列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> auditInList(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**手机转入转出明细
	 * @param page
	 * @return
	 */
	List<PageData> stockTransferListMx(AppPage page);

	/**
	 * @param prepd
	 * @return
	 */
	PageData findByRowNumAndStockList_ID(PageData prepd);

	/**手机扫码插入记录
	 * @param pd
	 */
	void saveJL(PageData pd);
	/**
	 * 获取物料追溯页面的物料转移数据
	 * @param pd
	 * @return
	 */
	List<PageData> GET_WLZY_ZHUISU_listPage(Page page);
	
}

