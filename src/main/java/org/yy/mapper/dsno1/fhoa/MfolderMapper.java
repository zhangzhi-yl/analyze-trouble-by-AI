package org.yy.mapper.dsno1.fhoa;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.fhoa.Mfolder;

/** 
 * 说明： 文件管理Mapper
 * 作者：YuanYes QQ356703572
 * 官网：356703572@qq.com
 * @version
 */
public interface MfolderMapper{

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
	
	/**批量操作
	 * @param pd
	 * @throws Exception
	 */
	void makeAll(PageData pd);
	
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
	List<Mfolder> listByParentId(PageData pd);
	
}

