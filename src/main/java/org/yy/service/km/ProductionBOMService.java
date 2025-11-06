package org.yy.service.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 生产BOM接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface ProductionBOMService{

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

	/**获取BOM列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getBOMList(PageData pd)throws Exception;

	/**单号验重
	 * @param pd
	 * @return
	 */
	public PageData getRepeatNum(PageData pd)throws Exception;
	
	/**计划工单获取投入产出列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getInOut(PageData pd)throws Exception;

	/**发布/停用
	 * @param pd
	 */
	public void release(PageData pd)throws Exception;
}

