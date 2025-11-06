package org.yy.mapper.dsno1.mbase;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 物料辅助属性(明细)Mapper 作者：YuanYes QQ356703572 时间：2020-11-06 官网：356703572@qq.com
 * 
 * @version
 */
public interface MAT_AUXILIARYMxMapper {

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

	/**通过MAT_AUXILIARYMX_CODE获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByMAT_AUXILIARYMX_CODE(PageData pd);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByCode(PageData pd);
	
	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 查询明细总数
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);

	/**
	 * 获取辅助属性值列表-可搜索-前100条
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> getAuxiliaryList(PageData pd);

	/**
	 * 根据计划工单编号类型id和物料辅助属性值编码删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd);

	/**
	 * 根据计划工单编号类型id和物料辅助属性值编码获取列表
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd);

}
