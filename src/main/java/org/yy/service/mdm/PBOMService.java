package org.yy.service.mdm;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.mdm.PBOM;
import org.yy.entity.mdm.PBOMNew;

/** 
 * 说明： 配方管理接口
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 * @version
 */
public interface PBOMService{

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
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<PBOM> listByParentId(PageData pd) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PBOM> listTree(PageData pd) throws Exception;

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PBOMNew> listTreeNew(PageData pd)throws Exception;

	/**更新配方版本号
	 * @param pd
	 * @throws Exception
	 */
	public void editVersions(PageData pd)throws Exception;
	
	/**更新配方发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception;
	
	/**通过顶级ID查询列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllId(PageData pd)throws Exception;

	/**归档配方
	 * @param pd
	 * @throws Exception
	 */
	public void saveGD(PageData pd)throws Exception;

	/**根据根节点id查询所有
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllCopy(PageData pd)throws Exception;

	/**临时表插入数据
	 * @param copypd
	 */
	public void savecopy(PageData copypd)throws Exception;

	/**更新临时表主键
	 * @param copypd
	 */
	public void updateBomIdAndPid(PageData copypd)throws Exception;

	/**获取临时表数据
	 * @param pd
	 * @return
	 */
	public List<PageData> copylistAll(PageData pd)throws Exception;

	/**删除临时表数据
	 * @param pd
	 */
	public void deletecopylistAll(PageData pd)throws Exception;

	/**更换母件-更新本级父级节点和层次
	 * @param pdUpNow
	 */
	public void updateParent(PageData pdUpNow)throws Exception;

	/**更新子级根节点
	 * @param pd
	 * @return
	 */
	public void updateRoot(PageData pd)throws Exception;

}

