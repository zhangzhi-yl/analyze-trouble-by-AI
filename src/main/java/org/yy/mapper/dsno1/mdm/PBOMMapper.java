package org.yy.mapper.dsno1.mdm;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.mdm.PBOM;
import org.yy.entity.mdm.PBOMNew;

/** 
 * 说明： 配方管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 * @version
 */
public interface PBOMMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<PBOM> listByParentId(PageData pd);

	/**通过ID获取其子级列表
	 * @param pd
	 * @return
	 */
	List<PBOMNew> listByParentIdNew(PageData pd);
	
	/**更新配方版本号
	 * @param pd
	 * @throws Exception
	 */
	void editVersions(PageData pd);
	
	/**更新配方发布状态
	 * @param pd
	 * @throws Exception
	 */
	void editState(PageData pd);
	
	/**通过顶级ID查询列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllId(PageData pd);
	
	/**归档配方
	 * @param pd
	 * @throws Exception
	 */
	void saveGD(PageData pd);

	/**根据根节点id查询所有
	 * @param pd
	 * @return
	 */
	List<PageData> listAllCopy(PageData pd);

	/**临时表插入数据
	 * @param copypd
	 */
	void savecopy(PageData copypd);

	/**更新临时表主键
	 * @param copypd
	 */
	void updateBomIdAndPid(PageData copypd);

	/**获取临时表数据
	 * @param pd
	 * @return
	 */
	List<PageData> copylistAll(PageData pd);

	/**删除临时表数据
	 * @param pd
	 */
	void deletecopylistAll(PageData pd);

	/**更新子级根节点
	 * @param pd
	 * @return
	 */
	void updateParent(PageData pdUpNow);

	/**更新子级根节点和层次
	 * @param pd
	 */
	void updateRoot(PageData pd);
}

