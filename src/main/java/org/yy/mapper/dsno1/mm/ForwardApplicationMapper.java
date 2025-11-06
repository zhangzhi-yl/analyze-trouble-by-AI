package org.yy.mapper.dsno1.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 发运申请Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 * @version
 */
public interface ForwardApplicationMapper{

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
	
	/**修改FCustomer
	 * @param pd
	 * @throws Exception
	 */
	void editFCustomer(PageData pd);
	
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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**审核或反审核发运申请
	 * @param pd
	 */
	void editAudit(PageData pd);

	/**下发或取消
	 * @param pd
	 */
	void editFStatus(PageData pd);
	
	public List<PageData> GET_FYSQ_ZHUISU_listPage(Page page) throws Exception;
}

