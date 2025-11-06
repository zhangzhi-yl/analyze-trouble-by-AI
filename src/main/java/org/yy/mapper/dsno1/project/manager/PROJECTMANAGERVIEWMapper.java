package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 开工会Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-27
 * 官网：356703572@qq.com
 * @version
 */
public interface PROJECTMANAGERVIEWMapper{

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
	
	/**甘特列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listGanTe(PageData pd);
	
	/**依据甘特图返回数据修改
	 * @param pd
	 * @throws Exception
	 */
	void editGanTE(PageData pd);

	/**
	 * @param pdT
	 * @return
	 */
	PageData findByName(PageData pdT);
}

