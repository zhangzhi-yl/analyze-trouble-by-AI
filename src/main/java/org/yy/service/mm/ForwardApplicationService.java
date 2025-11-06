package org.yy.service.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 发运申请接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 * @version
 */
public interface ForwardApplicationService{

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
	
	/**修改FCustomer
	 * @param pd
	 * @throws Exception
	 */
	public void editFCustomer(PageData pd)throws Exception;
	
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

	/**单号验重
	 * @param pd
	 * @return
	 */
	public PageData getRepeatNum(PageData pd)throws Exception;

	/**审核或反审核发运申请
	 * @param pd
	 */
	public void editAudit(PageData pd)throws Exception;

	/**下发或取消
	 * @param pd
	 */
	public void editFStatus(PageData pd)throws Exception;
	
	/**
	 * 获取物料追溯列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> GET_FYSQ_ZHUISU_listPage(Page page) throws Exception;
	
}

