package org.yy.service.mbase;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料辅助属性(明细)接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface MAT_AUXILIARYMxService{

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
	
	/**通过MAT_AUXILIARYMX_CODE获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMAT_AUXILIARYMX_CODE(PageData pd)throws Exception;
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;

	/**获取辅助属性值列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getAuxiliaryList(PageData pd);
	
	/**根据计划工单编号类型id和物料辅助属性值编码删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd);
	
	/**根据计划工单编号类型id和物料辅助属性值编码获取列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd);
	
}

