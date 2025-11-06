package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 功能操作记录接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-04
 * 官网：356703572@qq.com
 * @version
 */
public interface OperationRecordService{
	
	/**添加操作日志
	 * @param FunctionType  功能类型
	 * @param FunctionItem  功能项
	 * @param OperationType 操作类型
	 * @param DeleteTagID	删除标记ID
	 * @param FOperatorID	操作人ID
	 * @param FDescribe		操作描述
	 * @throws Exception
	 */
	public void add(String FunctionType,String FunctionItem,String OperationType,
			String DeleteTagID,String FOperatorID,String FDescribe)throws Exception;
	
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

}

