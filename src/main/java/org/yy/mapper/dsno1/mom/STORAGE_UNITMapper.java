package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 存储单元实体Mapper
 * 作者：YuanYe
 * 时间：2020-01-16
 * 
 * @version
 */
public interface STORAGE_UNITMapper{

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

	/**存储单元实体-选择存储单元类列表
	 * @param pd
	 * @return
	 */
	List<PageData> getSTORAGE_UNITCLASS_IDList(PageData pd);

	/**模板阶段库-工作中心列表和查询
	 * @param pd
	 * @return
	 */
	List<PageData> getWORKCENTER_CODEList(PageData pd);

	/**模板阶段库-物料列表和查询
	 * @param pd
	 * @return
	 */
	List<PageData> getMAT_CODEList(PageData pd);
	
	/**更新容器实际数量
	 * @param pd
	 * @throws Exception
	 */
	void editQty(PageData pd);
	
	/**获取容器列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getVesselList(PageData pd);
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData getVesselNum(PageData pd);
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData getVesselVerify(PageData pd);
}

