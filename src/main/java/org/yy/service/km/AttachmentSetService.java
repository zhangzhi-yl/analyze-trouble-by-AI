package org.yy.service.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 附件集接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface AttachmentSetService{

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
	
	/**列表(附件-嵌入)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listQR(Page page)throws Exception;
	
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
	
	/**查找是否已经上传过文档
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public PageData findByFid(PageData pd) throws Exception;
	
	/**修改文件归档状态
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void updateStatus(PageData pd) throws Exception;
	
	/**文件上传接口
	 * @param 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean check(PageData pd) throws Exception;
	
	/**根据数据id将状态为使用的文件修改为归档状态
	 * @param AssociationID 数据ID,FStatus='使用': 归档(使用,归档)
	 * @throws Exception
	 */
	public void updateStatusByAssociationID(PageData pd)throws Exception;
	
	/**通过关联ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByAssId(PageData pd)throws Exception;

	public void changesave(PageData pd);

	public PageData findByAId(PageData pd);
	
	
}

