package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工时填报接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 * @version
 */
public interface WorkingHoursService{

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
	
	/**获取单件工时
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getHour(PageData pd)throws Exception;
	
	/**提交审核
	 * @param pd
	 * @throws Exception
	 */
	public void goAudit(PageData pd)throws Exception;
	
	/**审核
	 * @param pd
	 * @throws Exception
	 */
	public void audit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listAudit(Page page)throws Exception;
}

