package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 工单工序投入产出实例Mapper 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
public interface WorkorderProcessIOExampleMapper {

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
	 * 列表(根据工艺工单工序实例ID查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByProcessWorkOrderExampleID(String ProcessWorkOrderExampleID);
	
	/**
	 * 根据工艺工单工序实例id查询投入产出实例列表
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findIOExampleByProcessWorkOrderExampleID(PageData pd);

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
	 *	 调整投入产出实例数量及描述
	 * @param pd
	 */
	void editPlannedQuantityAndFRemarks(PageData pd);

	List<PageData> getOutput(PageData appProcessWorkOrderExampleDetailByPK);



}
