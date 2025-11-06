package org.yy.service.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 异常定义接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-07
 * 官网：356703572@qq.com
 * @version
 */
public interface ExceptionDefinitionService{

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
	/**获取数据字典中的异常类型
	 * @param 无参数
	 * @return 
	 * @throws Exception
	 */
	public List<PageData> getExceptionType(PageData pd)throws Exception;
	/**启用异常状态
	 * @param ExceptionDefinition_ID
	 * @return 
	 * @throws Exception
	 */
	public void toStartUsing(PageData pd)throws Exception;
	/**停用异常状态
	 * @param ExceptionDefinition_ID
	 * @return 
	 * @throws Exception
	 */
	public void toEndUsing(PageData pd)throws Exception;
}

