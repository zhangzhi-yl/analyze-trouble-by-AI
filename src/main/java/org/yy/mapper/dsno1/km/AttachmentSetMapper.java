package org.yy.mapper.dsno1.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 附件集Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface AttachmentSetMapper{

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
	
	/**列表(附件-嵌入)
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageQR(Page page);
	
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
	
	/**查找是否已经上传过文档
	 * @param AssociationID: 关联ID,FStatus='使用': 状态(使用,归档)
	 * @return AttachmentSet_ID 附件集表ID
	 * @throws Exception
	 */
	PageData findByFid(PageData pd);
	
	/**修改文件归档状态
	 * @param AttachmentSet_ID 附件集表ID,FStatus='使用': 归档(使用,归档)
	 * @throws Exception
	 */
	void updateStatus(PageData pd);
	
	/**根据数据id将状态为使用的文件修改为归档状态
	 * @param AssociationID 数据ID,FStatus='使用': 归档(使用,归档)
	 * @throws Exception
	 */
	void updateStatusByAssociationID(PageData pd);
	
	/**通过关联ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findByAssId(PageData pd);

	void changesave(PageData pd);

	PageData findByAId(PageData pd);
	
	
}

