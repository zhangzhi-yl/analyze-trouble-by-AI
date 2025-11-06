package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

import com.github.pagehelper.PageInfo;

/** 
 * 说明： 物料转移申请单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferApplicationFormService{

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
	
	/**编辑运转状态：待转，运行，关闭，下发操作
	 * @param pd
	 * @throws Exception
	 */
	public void editRunningState(PageData pd)throws Exception;
	
	/**更新审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editAuditMark(PageData pd)throws Exception;
	
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
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByFNum(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**手机移库列表
	 * @param page
	 * @return
	 */
	public List<PageData> listTransfer(AppPage page)throws Exception;
	
}

