package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 部门计划接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
public interface DepPlanService{

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
	public void goXiafa(PageData pd)throws Exception;
	public void goXiafaAll(PageData pd)throws Exception;
	public void updateType(PageData pd)throws Exception;
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	public List<PageData> listDep(Page page)throws Exception;
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
	public PageData getPlanTime(PageData pd)throws Exception;
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	public PageData getStartTime(PageData pd)throws Exception;
	public PageData getEndTime(PageData pd)throws Exception;
	public void updateTime(PageData pd)throws Exception;
	
	/**项目计划部门反馈列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listFeedBack(Page page)throws Exception;

	/**项目计划部门反馈列表-excel导出
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> excelFeedBack(PageData pd)throws Exception;
	
	/**
	 * @param pd
	 * @return
	 */
	public PageData findDept(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listDepDeisgn(Page page)throws Exception;

	/**
	 * @param pd
	 */
	public void deletex(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listx(PageData pd)throws Exception;
}

