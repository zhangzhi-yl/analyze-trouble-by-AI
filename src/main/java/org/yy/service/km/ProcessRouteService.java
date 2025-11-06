package org.yy.service.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工艺路线接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface ProcessRouteService{

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
	public void editStatus(PageData pd)throws Exception;
	
	/**根据工艺路线id查询是否存在引用该工艺路线的生产bom
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountProductionBomByProcessRouteId(PageData pd)throws Exception;
	
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
	
	/**查询编号数据数量，编号不能重复
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**查询名称数据数量，名称不能重复
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByFName(PageData pd)throws Exception;
	/**查询柜体类型数据数量，柜体类型不能重复
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCabinetType(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**获取工艺路线列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getRouteList(PageData pd)throws Exception;
	

}

