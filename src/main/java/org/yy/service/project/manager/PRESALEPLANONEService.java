package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 售前方案计划一级明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PRESALEPLANONEService{

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
	
	/**最大序号+1
	 * @param pd
	 * @throws Exception
	 */
	public PageData maxNum(PageData pd)throws Exception;
	
	/**读取出错删除明细
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteFBOne(PageData pd)throws Exception;
	
	/**依据售前方案ID查询一级明细柜体类型汇总信息
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getCabinByType(PageData pd)throws Exception;
	
	/**依据售前方案ID查询一级明细柜体信息
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getCabin(PageData pd)throws Exception;
}

