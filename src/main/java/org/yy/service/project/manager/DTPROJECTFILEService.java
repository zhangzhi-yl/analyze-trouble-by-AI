package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目文件接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-05
 * 官网：356703572@qq.com
 * @version
 */
public interface DTPROJECTFILEService{

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
	
	/**逻辑删除
	 * @param pd
	 * @throws Exception
	 */
	public void updateDel(PageData pd)throws Exception;
	
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
	
	/**项目列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listProAll(PageData pd)throws Exception;
	
	/**通过id获取项目编号
	 * @param pd
	 * @throws Exception
	 */
//	public PageData findProById(PageData pd)throws Exception;
	
	/**设备列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listEquAll(PageData pd)throws Exception;
	
	/**阶段列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listStaAll(PageData pd)throws Exception;
	
	/**活动名称列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listActAll(PageData pd)throws Exception;

	/**附件列表
	 * @param page
	 * @return
	 */
	public List<PageData> listx(Page page)throws Exception;

	/**新增页获取数据
	 * @param pd
	 * @return
	 */
	public PageData getDatax(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void insert(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public PageData findFJ(PageData pd)throws Exception;
}

