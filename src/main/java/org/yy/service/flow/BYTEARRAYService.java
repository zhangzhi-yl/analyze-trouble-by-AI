package org.yy.service.flow;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 流程图文件接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 * @version
 */
public interface BYTEARRAYService{

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
	
	/**根据pid和 FType 删除 
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByPidAndFTYPE(PageData pd)throws Exception;
	
	/**修改JSON
	 * @param pd
	 * @throws Exception
	 */
	public void editJson(PageData pd)throws Exception;
	
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
	
	/**通过PID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPID(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**删除流程图文件
	 * @param pd
	 */
	public void deleteByBomId(PageData pd)throws Exception;
	
}

