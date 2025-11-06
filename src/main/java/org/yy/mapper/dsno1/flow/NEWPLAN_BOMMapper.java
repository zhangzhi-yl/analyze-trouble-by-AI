package org.yy.mapper.dsno1.flow;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 实例阶段-计划BOM工单Mapper 作者：YuanYes QQ356703572 时间：2020-12-01
 * 官网：356703572@qq.com
 * 
 * @version
 */
public interface NEWPLAN_BOMMapper {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 根据子计划工单删除所有节点
	 */
	void deleteBySubPlanID(String SUBPLAN_ID);

	/**
	 * 下一个节点
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> nextNodeList(PageData pd);

	/**
	 * 上一个节点
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> lastNodeList(PageData pd);

	void deleteBySubPlanIdAndNodeId(PageData pd);
}
