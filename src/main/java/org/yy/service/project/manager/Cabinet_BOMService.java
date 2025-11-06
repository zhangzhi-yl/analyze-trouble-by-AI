package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 装配柜体BOM表接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-05-07
 * 官网：356703572@qq.com
 * @version
 */
public interface Cabinet_BOMService{

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
	
	/**列表(合并同类物料)
	 * 
	 * v1 陈春光 2021-05-28 由于给计划工单中 物料投入产出列表展示用
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllGroupByMat(PageData pd)throws Exception;
	
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

	public List<PageData> listByCabinetAssemblyDetailIDS(String[] ArrayDATA_IDS);

	/**v2 管悦 2021-07-08 柜体下相同物料验重
	 * @param pd
	 * @return
	 */
	public PageData findNUM(PageData pd)throws Exception;

	/**v1 管悦 2021-07-08 按项目查看物料汇总
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listByCabinetAssemblyDetailIDSXM(String[] arrayDATA_IDS)throws Exception;

	/**柜体设计-获取已选择bom列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllByIds(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public PageData findOrder(PageData pd)throws Exception;



	/**
	 * @param arrayDATA_IDS
	 * @throws Exception 
	 */
	public void editCGNUM(String[] arrayDATA_IDS) throws Exception;

	/**
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listByCabinetAssemblyDetailIDSCH(String[] arrayDATA_IDS)throws Exception;
	
}

