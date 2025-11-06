package org.yy.mapper.dsno1.fhim;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明：状态(在线or隐身以及签名)Mapper
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public interface ImstateMapper {
	
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
	
	/**修改在线状态
	 * @param pd
	 * @throws Exception
	 */
	public void editOnline(PageData pd)throws Exception;
	
	/**修改个性签名
	 * @param pd
	 * @throws Exception
	 */
	public void editAuto(PageData pd)throws Exception;
	
	/**修改皮肤
	 * @param pd
	 * @throws Exception
	 */
	public void editSign(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception;
	
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
	
	/**通过用户名获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

}
