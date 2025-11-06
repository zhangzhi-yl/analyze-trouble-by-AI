package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 装配柜体BOM表Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-05-07
 * 官网：356703572@qq.com
 * @version
 */
public interface Cabinet_BOMMapper{

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
	
	/**列表(合并同类物料)
	 * 
	 * v1 陈春光 20210528 由于给计划工单中 物料投入产出列表展示用
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllGroupByMat(PageData pd);
	
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

	List<PageData>  listByCabinetAssemblyDetailIDS(String[] ArrayDATA_IDS);

	/**同柜体物料验重
	 * @param pd
	 * @return
	 */
	PageData findNUM(PageData pd);

	/**v1 管悦 2021-07-08 按项目查看物料汇总
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listByCabinetAssemblyDetailIDSXM(String[] arrayDATA_IDS);

	/**柜体设计-获取已选择bom列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllByIds(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	PageData findOrder(PageData pd);

	/**
	 * @param pageData
	 */
	void editCGNUM(PageData pageData);

	/**
	 * @param arrayDATA_IDS
	 */
	void editCGNUM(String[] arrayDATA_IDS);

	/**
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listByCabinetAssemblyDetailIDSCH(String[] arrayDATA_IDS);
	
}

