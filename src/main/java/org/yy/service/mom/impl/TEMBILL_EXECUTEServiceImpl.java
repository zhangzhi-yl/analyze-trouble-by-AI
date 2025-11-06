package org.yy.service.mom.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.TEMBILL_EXECUTEMapper;
import org.yy.service.mom.DOCUMENTTEMPLATEMxService;
import org.yy.service.mom.DOCUMENTTEMPLATEService;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.mom.TEMBILL_EXECUTETICKService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;

/** 
 * 说明： 质量检测发布接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-24
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class TEMBILL_EXECUTEServiceImpl implements TEMBILL_EXECUTEService{

	@Autowired
	private TEMBILL_EXECUTEMapper tembill_executeMapper;
	@Autowired
	private DOCUMENTTEMPLATEMxService documenttemplatemxService;
	@Autowired
	private DOCUMENTTEMPLATEService documenttemplateService;
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		tembill_executeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		tembill_executeMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		tembill_executeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return tembill_executeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return tembill_executeMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return tembill_executeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		tembill_executeMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据工单关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getPlan(PageData pd)throws Exception{
		return tembill_executeMapper.getPlan(pd);
	}
	
	/**修改检验状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		tembill_executeMapper.editState(pd);
	}
	
	/**新增检测单
	 * @param pd
	 * @throws Exception
	 */
	public void saveTem(PageData pd)throws Exception{
		PageData docPd = new PageData();
		PageData temPd = new PageData();
		BaseController bc = new BaseController();
		docPd=documenttemplateService.findById(pd);
		temPd.put("TEMBILL_EXECUTE_ID", bc.get32UUID());	//主键
		temPd.put("FCREATOR",Jurisdiction.getName());//创建人
		temPd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		temPd.put("FDETECTOR",Jurisdiction.getName());//检测人
		temPd.put("FDETECT_TIME", Tools.date2Str(new Date()));//检测时间
		temPd.put("FOPERATOR",Jurisdiction.getName());//操作人
		temPd.put("FOPERAT_TIME", Tools.date2Str(new Date()));//操作时间
		temPd.put("FNAME", docPd.getString("FNAME"));//模板名称
		temPd.put("VERSION", docPd.getString("VERSION"));//版本
		temPd.put("DAYTIME", docPd.getString("DAYTIME"));//日期
		temPd.put("BELONG_TYPE", docPd.getString("BELONG_TYPE"));//所属类别
		temPd.put("FSTATE", "待检");//状态
		temPd.put("FVERIFY_ODD", "JC"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));//检验单单据
		List<PageData> varList = documenttemplatemxService.listMxAll(pd);	//列出质量检测下所有明细
		if(varList.size()>0) {
			for(int i = 0; i < varList.size(); i++) {
				PageData TembillMxPd = new PageData();
				TembillMxPd.put("TEMBILL_EXECUTEMX_ID", bc.get32UUID());//明细ID
            	TembillMxPd.put("TEMBILL_EXECUTE_ID", temPd.getString("TEMBILL_EXECUTE_ID"));//主键ID
            	TembillMxPd.put("CAPTION", varList.get(i).getString("FNAME"));//标题
            	TembillMxPd.put("FDESCRIBE", varList.get(i).getString("FDESCRIBE"));//描述
            	TembillMxPd.put("DESCRIPTION_DETAILS", varList.get(i).getString("FTYPE1"));//有无描述明细(1无,2有)
            	TembillMxPd.put("DESCRIBE_TYPE", varList.get(i).getString("FTYPE"));//描述类型(1单,2多,3填,4描)
            	TembillMxPd.put("DESCRIPTION_CONTENT", varList.get(i).getString("FDESCRIBETERM"));//描述明细内容
            	TembillMxPd.put("DESCRIBE_CONTENT_BF", "");//描述明细内容反馈
            	TembillMxPd.put("RIGHT_OPTION_CONTENT", varList.get(i).getString("FRIGTHTERM"));//右侧选项内容
            	TembillMxPd.put("RIGHT_CONTENT_BF", "");//右侧选项内容反馈
            	TembillMxPd.put("OTHER_DESCRIPTION_TYPE", varList.get(i).getString("OTHER_DESCRIPTION_TYPE"));//有无其他描述(1无,2有)
            	TembillMxPd.put("OTHER_DESCRIPTION", "");//其他描述内容
            	TembillMxPd.put("SORT", varList.get(i).get("FORDER").toString());//排序
            	TembillMxPd.put("FLASTUPDATEPEOPLE",Jurisdiction.getName());//创建人
            	TembillMxPd.put("FLASTUPDATETIME", Tools.date2Str(new Date()));//创建时间
            	TembillMxPd.put("FSERIAL_NUM", varList.get(i).getString("FSERIAL_NUM")!=null?varList.get(i).getString("FSERIAL_NUM"):"");//编号
            	TembillMxPd.put("FINSEPCTION", varList.get(i).getString("FINSEPCTION")!=null?varList.get(i).getString("FINSEPCTION"):"");//检查方法
            	tembill_executemxService.save(TembillMxPd);
			}
		}
		tembill_executeMapper.save(temPd);
		String updateSql="";
		updateSql="update "+pd.getString("FTABLE_NAME")+" set "+pd.getString("FIEID_NAME")+"= '"+temPd.getString("TEMBILL_EXECUTE_ID")+"' where "+pd.getString("IDNAME")+"='"+pd.getString("ID")+"'";
		PageData sqlPd = new PageData();
		sqlPd.put("updateSql", updateSql);
		documenttemplateService.editSql(sqlPd);
		//tembill_executeMapper.saveTem(pd);
	}
}

