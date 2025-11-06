package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 售前方案计划接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PRESALEPLANService{

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
	
	/**提交审核
	 * @param pd
	 * @throws Exception
	 */
	public void goAduit(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public void editLXSQ(PageData pd)throws Exception;

	/**反写立项申请立项状态
	 * @param pd
	 */
	public void editLX(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listLXSQ(Page page)throws Exception;
}

