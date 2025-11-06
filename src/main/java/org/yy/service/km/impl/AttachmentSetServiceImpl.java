package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.AttachmentSetMapper;
import org.yy.service.km.AttachmentSetService;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

/** 
 * 说明： 附件集接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class AttachmentSetServiceImpl implements AttachmentSetService{

	@Autowired
	private AttachmentSetMapper attachmentsetMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		attachmentsetMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		attachmentsetMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		attachmentsetMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return attachmentsetMapper.datalistPage(page);
	}
	
	/**列表(附件-嵌入)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listQR(Page page)throws Exception{
		return attachmentsetMapper.datalistPageQR(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return attachmentsetMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return attachmentsetMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		attachmentsetMapper.deleteAll(ArrayDATA_IDS);
	}

	/**查找是否已经上传过文档
	 * @param AssociationID: 关联ID,FStatus='使用': 状态(使用,归档)
	 * @throws Exception
	 */
	@Override
	public PageData findByFid(PageData pd) throws Exception {
		return attachmentsetMapper.findByFid(pd);
	}
	
	/**修改文件归档状态
	 * @param  
	 * @throws Exception
	 */
	@Override
	public void updateStatus(PageData pd) throws Exception {
		attachmentsetMapper.updateStatus(pd);
	}
	
	/**根据数据id将状态为使用的文件修改为归档状态
	 * @param AssociationID 数据ID,FStatus='使用': 归档(使用,归档)
	 * @throws Exception
	 */
	public void updateStatusByAssociationID(PageData pd)throws Exception{
		attachmentsetMapper.updateStatusByAssociationID(pd);
	}

	/**文件上传接口
	 * @param 
	 * {
	 * DataSources 数据来源
	 * AssociationIDTable 数据来源表名
	 * AssociationID 数据来源表ID
	 * FName 附件名称
	 * FUrl 附件路径
	 * FExplanation 备注
	 * FCreatePersonID 创建人ID
	 * FCreateTime 创建时间
	 *==================================================
	 * FExtension 附件扩展名: 不需要传参，在本地自动截取
	 * TType 附件类型: 不需要传参
	 * FStatus 状态(使用,归档): 不需要传参，自动判断插入是使用还是归档
	 * FAttachment 附件: 不需要传参,暂时无效字段
	 * }
	 * @return true/false
	 * @throws Exception
	 */
	@Override
	public boolean check(PageData pd) throws Exception {
		String AssociationID = pd.getString("AssociationID");//接收到的数据源ID
		PageData pd1 = new PageData();
		PageData pd2 = new PageData();
		pd1.put("AssociationID", AssociationID);
		pd1 = this.findByFid(pd1);	//查询是否已经上传过文件(使用状态)
		if(pd1 != null){	//判断是否上传过文件，不为空则证明上传过文件
			String AttachmentSet_ID = pd1.getString("AttachmentSet_ID");
			pd1.put("AttachmentSet_ID", AttachmentSet_ID);
			this.updateStatus(pd1);		//修改文件为归档状态
			String DataSources = pd.getString("DataSources");//接收到的数据源
			String AssociationIDTable = pd.getString("AssociationIDTable");//接收到的数据来源表名
			String FName = pd.getString("FName");//接收到的附件名称
			String FUrl = pd.getString("FUrl");//接收到的附件路径
			String FExplanation = pd.getString("FExplanation");//备注
			String FCreatePersonID = pd.getString("FCreatePersonID");//创建人ID
			String FCreateTime = pd.getString("FCreateTime");//创建时间
			if(Tools.notEmpty(FName)){
				String[] FExtension = FName.split("\\.");
				if(FExtension.length>1){
					String fex = FExtension[1];
					pd2.put("AttachmentSet_ID", this.get32UUID());	//附件集ID
					pd2.put("DataSources", DataSources);
					pd2.put("AssociationIDTable", AssociationIDTable);
					pd2.put("AssociationID", AssociationID);
					pd2.put("FName", FName);
					pd2.put("FUrl", FUrl);
					pd2.put("FExplanation", FExplanation);
					pd2.put("FCreatePersonID", FCreatePersonID);
					pd2.put("FCreateTime", FCreateTime);
					pd2.put("FStatus", "使用");
					pd2.put("FExtension", fex);
					this.save(pd2);	//保存文件到文件集
				}
			}
			return true;
		}else{	//判断是否上传过文件，为空则证明未上传过文件
			String DataSources = pd.getString("DataSources");//接收到的数据源
			String AssociationIDTable = pd.getString("AssociationIDTable");//接收到的数据来源表名
			String FName = pd.getString("FName");//接收到的附件名称
			String FUrl = pd.getString("FUrl");//接收到的附件路径
			String FExplanation = pd.getString("FExplanation");//备注
			String FCreatePersonID = pd.getString("FCreatePersonID");//创建人ID
			String FCreateTime = pd.getString("FCreateTime");//创建时间
			String FExtension = FName.split("\\.")[1]; //获取文件扩展名
			pd2.put("AttachmentSet_ID", this.get32UUID());	//附件集ID
			pd2.put("DataSources", DataSources);
			pd2.put("AssociationIDTable", AssociationIDTable);
			pd2.put("AssociationID", AssociationID);
			pd2.put("FName", FName);
			pd2.put("FUrl", FUrl);
			pd2.put("FExplanation", FExplanation);
			pd2.put("FCreatePersonID", FCreatePersonID);
			pd2.put("FCreateTime", FCreateTime);
			pd2.put("FStatus", "使用");
			pd2.put("FExtension", FExtension);
			this.save(pd2);	//保存文件到文件集
			return true;
		}
	}
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**通过关联ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByAssId(PageData pd)throws Exception{
		return attachmentsetMapper.findByAssId(pd);
	}

	@Override
	public void changesave(PageData pd) {
		attachmentsetMapper.changesave(pd);
		
	}

	@Override
	public PageData findByAId(PageData pd) {
		return attachmentsetMapper.findByAId(pd);
	}
}

