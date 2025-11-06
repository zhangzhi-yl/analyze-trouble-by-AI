package org.yy.service.mdm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备点巡检接口
 * 作者：YuanYe
 * 时间：2020-02-19
 * 
 * @version
 */
public interface EQM_POINT_INSPECTService{

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
	public void editStatus(PageData pd)throws Exception;
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
	
	/**更新点检人
	 * @param pd
	 * @throws Exception
	 */
	public void editOperator(PageData pd)throws Exception;
	
	/**更新确认人
	 * @param pd
	 * @throws Exception
	 */
	public void editIdentfied(PageData pd)throws Exception;

	/**手机设备点巡检
	 * @param page
	 * @return
	 */
	public List<PageData> appEqmPointInspectList(AppPage page)throws Exception;

	/**手机端完成
	 * @param pd
	 */
	public void editStatusx(PageData pd)throws Exception;
}

