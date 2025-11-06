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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.REPAIR_OUTSOURCINGService;

/** 
 * 说明：报修委外
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/repair_outsourcing")
public class REPAIR_OUTSOURCINGController extends BaseController {
	
	@Autowired
	private REPAIR_OUTSOURCINGService repair_outsourcingService;
	
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
		pd.put("REPAIR_OUTSOURCING_ID", this.get32UUID());	//委外ID主键
		pd.put("FEXTEND1", "");	//扩展字段1
		pd.put("FEXTEND2", "");	//扩展字段2
		pd.put("FEXTEND3", "");	//扩展字段3
		pd.put("FEXTEND4", "");	//扩展字段4
		pd.put("FEXTEND5", "");	//扩展字段5
		repair_outsourcingService.save(pd);
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
		repair_outsourcingService.delete(pd);
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
		repair_outsourcingService.edit(pd);
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
		List<PageData>	varList = repair_outsourcingService.list(page);	//列出REPAIR_WORKORDERMx列表
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
		pd = repair_outsourcingService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**
	 * 查询单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findMaxNo")
	@ResponseBody
	public Object findMaxNo() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "";
		String currentDateStr = DateUtil.date2Str(new Date(), "yyyyMMdd");
		pd = repair_outsourcingService.findMaxNo();	//最大编号
		if(null!=pd && null!=pd.get("OUTSOURCING_NO") && !"".equals(pd.get("OUTSOURCING_NO"))) {
			String OUTSOURCING_NO = pd.get("OUTSOURCING_NO").toString();	//示例：20021700001，yyMMdd+00001(五位流水号，顺序加一)
			String dateStr = OUTSOURCING_NO.substring(0,8);
			String numberStr = OUTSOURCING_NO.substring(8,12);
			if(dateStr.equals(currentDateStr)) {//流水号中的日期部分是今天日期，流水号+1
				result = dateStr+String.format("%0" + 4 + "d", Integer.parseInt(numberStr) + 1);
			}else {								//流水号中的日期部分不是今天日期，直接获取今天日期+流水号置为1
				result = currentDateStr+"0001";
			}
		} else {
			result = currentDateStr+"0001";
		}
		map.put("BILLNO", result);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 根据报修工单ID查询该工单是否需要配件，是返回true，否则返回false
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/isNeedAccessories")
	@ResponseBody
	public Object findNeedAccessoriesCount() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//根据报修工单ID查询需要配件的委外信息数量
		map.put("msg", Integer.parseInt(repair_outsourcingService.findNeedAccessoriesCount(pd).get("num").toString()) > 0?Boolean.TRUE:Boolean.FALSE);
		map.put("result", errInfo);
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
			repair_outsourcingService.deleteAll(ArrayDATA_IDS);
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
		titles.add("委外ID");	//1
		titles.add("委外编号");	//2
		titles.add("报修工单ID");	//3
		titles.add("关联设备ID");	//4
		titles.add("委外类型");	//5
		titles.add("委外描述");	//6
		titles.add("委外金额");	//7
		titles.add("是否需要配件");	//8
		titles.add("扩展字段1");	//9
		titles.add("扩展字段2");	//10
		titles.add("扩展字段3");	//11
		titles.add("扩展字段4");	//12
		titles.add("扩展字段5");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = repair_outsourcingService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("REPAIR_OUTSOURCING_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("REPAIR_OUTSOURCING_NO"));	    //2
			vpd.put("var3", varOList.get(i).getString("REPAIR_WORKORDER_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("EQM_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("OUTSOURCING_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("OUTSOURCING_DES"));	    //6
			vpd.put("var7", varOList.get(i).get("OUTSOURCING_MONEY").toString());	//7
			vpd.put("var8", varOList.get(i).getString("ACCESSORY_WHETHER"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND1"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND2"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND3"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND4"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND5"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
