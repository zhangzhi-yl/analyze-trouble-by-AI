package org.yy.mapper.dsno1.mdm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 报修工单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-12
 * 官网：356703572@qq.com
 * @version
 */
public interface REPAIR_WORKORDERMapper{

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
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	void updateState(PageData pd);
	
	/**根据流程实例ID修改状态
	 * @param pd
	 * @throws Exception
	 */
	void updateStateByPROC_INST_ID(PageData pd);
	
	/**修改Fopinion
	 * @param pd
	 * @throws Exception
	 */
	void updateFopinion(PageData pd);
	
	/**修改Fopinion1
	 * @param pd
	 * @throws Exception
	 */
	void updateFopinion1(PageData pd);
	
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
	/**
	 * 查询最大单号
	 * @return pd
	 */
	PageData findMaxBillNo();
	/**
	 * 根据设备标识查询top1 
	 * @param pd
	 * @return pd
	 */
	PageData findByIdentify(PageData pd);
	/**
	 * 手机端列表
	 */
	List<PageData> AppList(AppPage page);
}

