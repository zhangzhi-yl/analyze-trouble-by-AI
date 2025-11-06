package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.AttachmentSetMapper;
import org.yy.mapper.dsno1.km.WorkingProcedureDefectiveItemsMapper;
import org.yy.mapper.dsno1.km.WorkingProcedureMapper;
import org.yy.service.km.WorkingProcedureService;

/** 
 * 说明： 工艺路线工序接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkingProcedureServiceImpl implements WorkingProcedureService{

	@Autowired
	private WorkingProcedureMapper WorkingProcedureMapper;
	
	@Autowired
	private WorkingProcedureDefectiveItemsMapper WorkingProcedureDefectiveItemsMapper;
	
	@Autowired
	private AttachmentSetMapper attachmentsetMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkingProcedureMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		pd.put("AssociationID", pd.getString("WorkingProcedure_ID"));
		attachmentsetMapper.delete(pd);
		WorkingProcedureDefectiveItemsMapper.deleteByWorkingProcedure_ID(pd);//根据工艺工序id删除工艺工序次品项
		WorkingProcedureMapper.delete(pd);
	}
	
	/**根据工艺路线id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByProcessRouteID(PageData pd)throws Exception{
		//查询工艺路线下工序
		List<PageData> list = WorkingProcedureMapper.listAll(pd);
		for(int i=0;i<list.size();i++) {
			PageData p = new PageData();
			p.put("AssociationID", list.get(i).getString("WorkingProcedure_ID"));
			attachmentsetMapper.delete(p);//删除附件
			WorkingProcedureDefectiveItemsMapper.deleteByWorkingProcedure_ID(list.get(i));//根据工艺工序id删除工艺工序次品项
			/*p.put("AssociationID", list.get(i).getString("WorkingProcedure_ID"));//根据工艺工序id将状态为使用的文件修改为归档状态
			attachmentsetMapper.updateStatusByAssociationID(p);*/
		}
		pd.put("ProcessRouteID", pd.getString("ProcessRoute_ID"));
		WorkingProcedureMapper.deleteByProcessRouteID(pd);//根据工艺路线id删除工序
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkingProcedureMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkingProcedureMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkingProcedureMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkingProcedureMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkingProcedureMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过工艺路线ID获取最大序号
	 * @param pd
	 * @throws Exception
	 */
	public PageData findMaxSerialNumByProcessRouteID(PageData pd)throws Exception{
		return WorkingProcedureMapper.findMaxSerialNumByProcessRouteID(pd);
	}
	
	/**通过工艺路线ID和节点id获取  top 1
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByProcessRouteIDAndNodeId(PageData pd)throws Exception{
		return WorkingProcedureMapper.findByProcessRouteIDAndNodeId(pd);
	}
	
}

