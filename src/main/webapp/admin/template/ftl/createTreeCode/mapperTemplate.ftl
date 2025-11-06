package org.yy.mapper.dsno1.${packageName};

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.${packageName}.${objectName};

/** 
 * 说明： ${TITLE}Mapper
 * 作者：YuanYes QQ356703572
 * 时间：${nowDate?string("yyyy-MM-dd")}
 * 官网：356703572@qq.com
 * @version
 */
public interface ${objectName}Mapper{

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
	List<${objectName}> listByParentId(String parentId);
	
}

