package org.yy.service.flow;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 实例阶段-计划BOM工单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 * @version
 */
public interface NEWPLAN_BOMService{

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
	/**
	 * 根据子计划工单删除所有节点
	 */
	public void deleteBySubPlanID(String SUBPLAN_ID)throws Exception;

	/**
	 * 下一个节点
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> nextNodeList(PageData pd) throws Exception;

	/**
	 * 上一个节点
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> lastNodeList(PageData pd) throws Exception;

	public void deleteBySubPlanIdAndNodeId(PageData pd);
}

