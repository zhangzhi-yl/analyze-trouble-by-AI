package org.yy.mapper.dsno1.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 生产BOMMapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface ProductionBOMMapper{

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

	/**获取BOM列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getBOMList(PageData pd);

	/**单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**计划工单获取投入产出列表
	 * @param pd
	 * @return
	 */
	List<PageData> getInOut(PageData pd);

	/**发布/停用
	 * @param pd
	 */
	void release(PageData pd);
	
}

