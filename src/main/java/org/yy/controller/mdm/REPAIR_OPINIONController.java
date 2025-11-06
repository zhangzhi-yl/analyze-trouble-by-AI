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
import org.yy.service.mdm.REPAIR_OPINIONService;
import org.yy.service.mdm.REPAIR_WORKORDERService;

/** 
 * 说明：报修审批意见
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/repair_opinion")
public class REPAIR_OPINIONController extends BaseController {
	
	@Autowired
	private REPAIR_WORKORDERService repair_workorderService;
	
	@Autowired
	private REPAIR_OPINIONService repair_opinionService;
	
	/**添加子表意见,更新主表Fopinion1
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/addRepairOpinion1")
	@ResponseBody
	public Object addRepairOpinion1() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REPAIR_OPINION_ID", this.get32UUID());	//审批意见ID主键
		pd.put("FISSUER", Jurisdiction.getName());
		pd.put("FTIME", Tools.date2Str(new Date()));
		repair_opinionService.save(pd);//添加子表意见
		repair_workorderService.updateFopinion1(pd);//更新主表Fopinion
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**添加子表意见,更新主表Fopinion
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/addRepairOpinion")
	@ResponseBody
	public Object addRepairOpinion() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REPAIR_OPINION_ID", this.get32UUID());	//审批意见ID主键
		pd.put("FISSUER", Jurisdiction.getName());
		pd.put("FTIME", Tools.date2Str(new Date()));
		repair_opinionService.save(pd);//添加子表意见
		repair_workorderService.updateFopinion(pd);//更新主表Fopinion
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
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
		pd.put("REPAIR_OPINION_ID", this.get32UUID());	//审批意见ID主键
		pd.put("FISSUER", Jurisdiction.getName());
		pd.put("FTIME", Tools.date2Str(new Date()));
		repair_opinionService.save(pd);
		map.put("result", errInfo);				//返回结果
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
		repair_opinionService.delete(pd);
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
		repair_opinionService.edit(pd);
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
		List<PageData>	varList = repair_opinionService.list(page);	//列出REPAIR_WORKORDERMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = repair_opinionService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			repair_opinionService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
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
		titles.add("审批意见ID");	//1
		titles.add("发布人");	//2
		titles.add("发布时间");	//3
		titles.add("报修工单ID");	//4
		titles.add("状态");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = repair_opinionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("REPAIR_OPINION_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FISSUER"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("REPAIR_WORKORDER_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("FSTATE"));	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
