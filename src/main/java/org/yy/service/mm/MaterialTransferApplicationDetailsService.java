package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料转移申请明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferApplicationDetailsService{

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
	
	/**根据转移申请单id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByMTA_ID(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**修改行关闭状态
	 * @param pd
	 * @throws Exception
	 */
	public void editLineClose(PageData pd)throws Exception;
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editFStatus(PageData pd)throws Exception;
	
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
	
	/**通过父级id获取明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**合并转入列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> mergeTransferInlist(PageData pd)throws Exception;
	
	/**转入审核列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> auditInList(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**手机转入转出明细
	 * @param page
	 * @return
	 */
	public List<PageData> stockTransferListMx(AppPage page)throws Exception;

	/**
	 * @param prepd
	 * @return
	 */
	public PageData findByRowNumAndStockList_ID(PageData prepd)throws Exception;

	/**手机扫码插入记录
	 * @param pd
	 */
	public void saveJL(PageData pd)throws Exception;
	
	public List<PageData> GET_WLZY_ZHUISU_listPage(Page page);
	
}

