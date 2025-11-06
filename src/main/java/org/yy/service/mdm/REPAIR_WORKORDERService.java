package org.yy.service.mdm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 报修工单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-12
 * 官网：356703572@qq.com
 * @version
 */
public interface REPAIR_WORKORDERService{

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
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateState(PageData pd)throws Exception;
	
	/**根据流程实例ID修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateStateByPROC_INST_ID(PageData pd)throws Exception;
	
	/**修改Fopinion
	 * @param pd
	 * @throws Exception
	 */
	public void updateFopinion(PageData pd)throws Exception;
	
	/**修改Fopinion1
	 * @param pd
	 * @throws Exception
	 */
	public void updateFopinion1(PageData pd)throws Exception;
	
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
	
	/**
	 * 查询最大单号
	 * @return pd
	 */
	public PageData findMaxBillNo() throws Exception;
	/**
	 * 根据设备标识查询top1 
	 * @param pd
	 * @return pd
	 */
	public PageData findByIdentify(PageData pd) throws Exception;
	/**
	 * 手机端列表
	 */
	public List<PageData> AppList(AppPage page)throws Exception;
		
	
}

