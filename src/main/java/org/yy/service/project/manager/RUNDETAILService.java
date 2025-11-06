package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 执行明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
public interface RUNDETAILService{

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

	/**获得执行中明细数量
	 * @param pd
	 * @return
	 */
	public PageData getNum(PageData pd)throws Exception;

	/**反写执行明细结束时间
	 * @param pdZ
	 */
	public void editEndTime(PageData pdZ)throws Exception;

	/**查询明细进行中明细信息
	 * @param pd
	 * @return
	 */
	public PageData findByIdN(PageData pd)throws Exception;

	/**更新任务表实际时间
	 * @param pd
	 */
	public void editActual(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void upVisible(PageData pd)throws Exception;
	
}

