package org.yy.controller.mdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTService;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTMxService;

/** 
 * 说明：设备配件采购
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/accessories_procurement")
public class ACCESSORIES_PROCUREMENTController extends BaseController {
	
	@Autowired
	private ACCESSORIES_PROCUREMENTService accessories_procurementService;
	
	@Autowired
	private ACCESSORIES_PROCUREMENTMxService accessories_procurementmxService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ACCESSORIES_PROCUREMENT_ID", this.get32UUID());	//设备配件采购ID主键
		pd.put("FSTATE", "创建");	//状态
		pd.put("FCREATER", Jurisdiction.getName());	//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		accessories_procurementService.save(pd);
		//pd = accessories_procurementService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(accessories_procurementmxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				accessories_procurementService.delete(pd);
			}
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		accessories_procurementService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**状态修改为完成(根据维修报工ID)
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/updateState")
	@ResponseBody
	public Object updateStateByREPAIR_WORKORDER_ID() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		accessories_procurementService.updateStateByREPAIR_WORKORDER_ID(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = accessories_procurementService.list(page);	//列出ACCESSORIES_PROCUREMENT列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = accessories_procurementService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("设备配件采购ID");	//1
		titles.add("报修工单ID");	//2
		titles.add("关联设备ID");	//3
		titles.add("单据日期");	//4
		titles.add("申请单号");	//5
		titles.add("优先级");	//6
		titles.add("要求到货日期");	//7
		titles.add("状态（创建、下推）");	//8
		titles.add("备注");	//9
		titles.add("创建人");	//10
		titles.add("创建时间");	//11
		titles.add("类别(采购，备件)");	//12
		titles.add("维修类型（自修，委外）");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = accessories_procurementService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ACCESSORIES_PROCUREMENT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("REPAIR_WORKORDER_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("EQM_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROCUREMENT_BILL_DATE"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROCUREMENT_BILL_NO"));	    //5
			vpd.put("var6", varOList.get(i).getString("FPRIORITY"));	    //6
			vpd.put("var7", varOList.get(i).getString("DEMAND_ARRIVAL_DATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FSTATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FREMARK"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCREATER"));	    //10
			vpd.put("var11", varOList.get(i).getString("CREATE_TIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("FKIND"));	    //12
			vpd.put("var13", varOList.get(i).getString("REPAIR_TYPE"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
