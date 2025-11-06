package org.yy.mapper.dsno1.momp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 双周计划主表Mapper
 * 作者：YuanYe
 * 时间：2020-04-20
 * 
 * @version
 */
public interface DOUBLE_PLAN_WEEK_MAINMapper{

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

	/**获得项目列表
	 * @param pd
	 * @return
	 */
	List<PageData> getEPROJECTS(PageData pd);

	/**根据图标显示文字获得id
	 * @param pdIC
	 * @return
	 */
	PageData getICON(PageData pdIC);

	/**查询本周双周计划数
	 * @param pd
	 * @return
	 */
	PageData findNUM(PageData pd);

	/**模板导入后更新主表
	 * @param pdM
	 */
	void editE(PageData pdM);
	
}

