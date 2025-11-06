package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 叫料申请Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 * @version
 */
public interface CallMaterialMapper{

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

	List<PageData> chejiandatalistPage(Page page);

	List<PageData> kufangdatalistPage(Page page);

	List<PageData> listchejianApp(AppPage page);

	List<PageData> listkufangApp(AppPage page);
	
	List<PageData> getCallMaterialDataByMasterPlanningWorkNum(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllMat(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listXMkufangdatalistPage(Page page);
	
}

