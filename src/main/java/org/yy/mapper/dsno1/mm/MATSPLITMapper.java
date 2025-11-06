package org.yy.mapper.dsno1.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料拆分表Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-02
 * 官网：356703572@qq.com
 * @version
 */
public interface MATSPLITMapper{

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

	/**获取拆分物料数量
	 * @param pd
	 * @return
	 */
	PageData getNum(PageData pd);

	/**验证唯一码唯一性
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);
	
}

