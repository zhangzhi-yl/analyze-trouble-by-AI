package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 存储单元实体接口
 * 作者：YuanYe
 * 时间：2020-01-16
 * 
 * @version
 */
public interface STORAGE_UNITService{

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

	/**存储单元实体-选择存储单元类列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getSTORAGE_UNITCLASS_IDList(PageData pd)throws Exception;

	/**模板阶段库-工作中心列表和查询
	 * @param pd
	 * @return
	 */
	public List<PageData> getWORKCENTER_CODEList(PageData pd);

	/**模板阶段库-物料列表和查询
	 * @param pd
	 * @return
	 */
	public List<PageData> getMAT_CODEList(PageData pd);
	
	/**更新容器实际数量
	 * @param pd
	 * @throws Exception
	 */
	public void editQty(PageData pd)throws Exception;
	
	/**获取容器列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getVesselList(PageData pd)throws Exception;
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getVesselNum(PageData pd)throws Exception;
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getVesselVerify(PageData pd)throws Exception;
}

