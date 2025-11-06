package org.yy.service.momp.epm.planinstance;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目工单接口
 * 作者：YuanYe
 * 时间：2020-03-13
 * 
 * @version
 */
public interface PlanInstanceService{

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
	public List<PageData> listUserAll(PageData pd)throws Exception;
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	public void deleteJH(PageData pd)throws Exception;
	public PageData findUsefulPlanCount(PageData pd)throws Exception;
	public PageData findBySno(PageData pd)throws Exception;
	public void editPAP(PageData pd)throws Exception;
	public void updatePlanState(PageData pd)throws Exception;
	public PageData findXM(PageData pd)throws Exception;
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

