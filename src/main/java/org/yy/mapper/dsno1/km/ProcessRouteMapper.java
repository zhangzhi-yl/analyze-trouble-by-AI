package org.yy.mapper.dsno1.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工艺路线Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface ProcessRouteMapper{

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
	void editStatus(PageData pd);
	
	/**根据工艺路线id查询是否存在引用该工艺路线的生产bom
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountProductionBomByProcessRouteId(PageData pd);
	
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
	
	/**查询编号数据数量，编号不能重复
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**查询名称数据数量，名称不能重复
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByFName(PageData pd);

	PageData findCountByCabinetType(PageData pd);

	
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

	/**获取工艺路线列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getRouteList(PageData pd);
	
}

