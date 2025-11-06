package org.yy.service.fhoa;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.fhoa.Mfolder;

/** 
 * 说明： 文件管理接口
 * 作者：YuanYes QQ356703572
 * 官网：356703572@qq.com
 * @version
 */
public interface MfolderService{

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
	
	/**批量操作
	 * @param pd
	 * @throws Exception
	 */
	public void makeAll(PageData pd)throws Exception;
	
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
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Mfolder> listByParentId(PageData pd) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Mfolder> listTree(PageData pd,String SHARE) throws Exception;
	
}

