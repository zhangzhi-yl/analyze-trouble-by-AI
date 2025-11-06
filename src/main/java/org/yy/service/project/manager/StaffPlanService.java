package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 部门人员计划接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
public interface StaffPlanService{

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
	public void goXiafa(PageData pd)throws Exception;
	public void updateType(PageData pd)throws Exception;
	public PageData getStartTime(PageData pd)throws Exception;
	public PageData getEndTime(PageData pd)throws Exception;

	/**获得人员超负荷详情
	 * @param pd
	 * @return
	 */
	public List<PageData> getOverList(PageData pd)throws Exception;

	/**下发前验证
	 * @param pd
	 * @return
	 */
	public List<PageData> listTest(PageData pd)throws Exception;

	/**获取超负荷数量
	 * @param pd
	 * @return
	 */
	public PageData getNUM(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listx(PageData pd)throws Exception;

	/**
	 * @param arrayDATA_IDS
	 */
	public void deleteAllx(String[] arrayDATA_IDS)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public PageData getOrder(PageData pd)throws Exception;
}

