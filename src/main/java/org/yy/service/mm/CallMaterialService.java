package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 叫料申请接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 * @version
 */
public interface CallMaterialService{

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

	public List<PageData> listchejian(Page page);

	public List<PageData> listkufang(Page page);

	public List<PageData> listchejianApp(AppPage page);

	public List<PageData> listkufangApp(AppPage page);
	
	public List<PageData> getCallMaterialDataByMasterPlanningWorkNum(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllMat(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listXMkufang(Page page)throws Exception;
	
}

