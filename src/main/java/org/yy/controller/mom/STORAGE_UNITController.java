package org.yy.controller.mom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.STORAGE_UNITService;

/** 
 * 说明：存储单元实体
 * 作者：YuanYe
 * 时间：2020-01-16
 * 
 */
@Controller
@RequestMapping("/storage_unit")
public class STORAGE_UNITController extends BaseController {
	
	@Autowired
	private STORAGE_UNITService storage_unitService;
	/**存储单元实体-新增
	 * @param
	 * @author 管悦
	 * @date 2020-01-17
	 * @throws Exception
	 * @version 1.0
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ge_o:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("STORAGE_UNIT_ID", this.get32UUID());	//主键
		try{			
			storage_unitService.save(pd);
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("result", result);
			map.put("pd", pd);
			//map.put("msg", msg);
		}
		
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("storage_unit:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		storage_unitService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**存储单元实体-修改
	 * @param
	 * @author 管悦
	 * @date 2020-01-17
	 * @throws Exception
	 * @version 1.0
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ge_up:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{			
			storage_unitService.edit(pd);
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("result", result);
			map.put("pd", pd);
			//map.put("msg", msg);
		}
		return map;
	}

	/**存储单元类-列表和查询
	 * @param page
	 * @author 管悦
	 * @date 2020-01-16
	 * @throws Exception
	 * @version 1.0
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("storage_unit:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		String result = "500";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键字检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
        //页码
		if(null != pd.getString("currentPage")){
			page.setCurrentPage(Integer.parseInt(pd.getString("currentPage")));
			map.put("currentPage", pd.getString("currentPage"));
		}else{
			page.setCurrentPage(1);
			map.put("currentPage", "1");
		}		
		//每页应显示条数
		if(null != pd.getString("showCount")){
			page.setShowCount(Integer.parseInt(pd.getString("showCount")));
			map.put("showCount", pd.getString("showCount"));
		}else{
			page.setShowCount(10);
			map.put("showCount", "10");
		}
		page.setPd(pd);
		List<PageData>	varList = storage_unitService.list(page);	//列出列表		
		try{			
			//总条数
			map.put("total",page.getTotalResult());
			if(varList!=null && varList.size()!=0){	
				result = "200";
			}else{
				result = "201";
			}
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", result);
		}
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("storage_unit:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = storage_unitService.findById(pd);	//根据ID读取
		System.out.println("pd:"+pd);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("storage_unit:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			storage_unitService.deleteAll(ArrayDATA_IDS);
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
		titles.add("存储实体代码");	//1
		titles.add("存储实体名称");	//2
		titles.add("使用状态");	//3
		titles.add("混合类型");	//4
		titles.add("存储类型");	//5
		titles.add("出库类型");	//6
		titles.add("物料内码");	//7
		titles.add("实际存量");	//8
		titles.add("存量单位");	//9
		titles.add("存储总量");	//10
		titles.add("存量上线");	//11
		titles.add("存量下线");	//12
		titles.add("关联工作中心内码");	//13
		titles.add("描述");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = storage_unitService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FCODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FSTATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("BLEND_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("STORAGE_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("OUTGOING_TYPE"));	    //6
			vpd.put("var7", varOList.get(i).getString("MAT_CODE"));	    //7
			vpd.put("var8", varOList.get(i).get("ACTUAL_AMOUNT").toString());	//8
			vpd.put("var9", varOList.get(i).getString("STOCK_UNIT"));	    //9
			vpd.put("var10", varOList.get(i).get("STOCK_TOTAL_AMOUNT").toString());	//10
			vpd.put("var11", varOList.get(i).get("STOCK_QTY_H").toString());	//11
			vpd.put("var12", varOList.get(i).get("STOCK_QTY_L").toString());	//12
			vpd.put("var13", varOList.get(i).getString("WORKCENTER_CODE"));	    //13
			vpd.put("var14", varOList.get(i).getString("FDES"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**存储单元实体-获取代码
		 * @param
		 * @author 管悦
		 * @date 2020-01-17
		 * @throws Exception
		 * @version 1.0
		 */
		@RequestMapping(value="/getFCODE")
		@ResponseBody
		public Object getPHASE_NO() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String result = "200";
			PageData pd = new PageData();
			pd = this.getPageData();
			String FCODE ="";
			try{			
				FCODE ="MSUS"+Tools.date2Str(new Date(),"yyyyMMddHHmmss");
				pd.put("FCODE", FCODE);
			}catch (Exception e){
				result = "500";
			}finally{
				map.put("pd", pd);
				map.put("result", result);
			}		
			return map;
		}
		/**模板阶段库-获得设备类列表和查询
		 * @param pd
		 * @author 管悦
		 * @date 2020-01-17
		 * @throws Exception
		 * @version 1.0
		 */
		@RequestMapping(value="/getSTORAGE_UNITCLASS_IDList")
		@ResponseBody
		public Object getSTORAGE_UNITCLASS_IDList() throws Exception{
			String result = "500";
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd = this.getPageData();		
			List<PageData>	varList = storage_unitService.getSTORAGE_UNITCLASS_IDList(pd);	//列出列表		
			try{			
				
				if(varList!=null && varList.size()!=0){	
					result = "200";
				}else{
					result = "201";
				}
			}catch (Exception e){
				result = "500";
			}finally{
				map.put("varList", varList);
				map.put("result", result);
			}
			return map;
		}		
		/**模板阶段库-工作中心列表和查询
		 * @param pd
		 * @author 管悦
		 * @date 2020-01-17
		 * @throws Exception
		 * @version 1.0
		 */
		@RequestMapping(value="/getWORKCENTER_CODEList")
		@ResponseBody
		public Object getWORKCENTER_CODEList() throws Exception{
			String result = "500";
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd = this.getPageData();		
			List<PageData>	varList = storage_unitService.getWORKCENTER_CODEList(pd);	//列出列表		
			try{			
				
				if(varList!=null && varList.size()!=0){	
					result = "200";
				}else{
					result = "201";
				}
			}catch (Exception e){
				result = "500";
			}finally{
				map.put("varList", varList);
				map.put("result", result);
			}
			return map;
		}		
		/**模板阶段库-物料列表和查询
		 * @param pd
		 * @author 管悦
		 * @date 2020-01-17
		 * @throws Exception
		 * @version 1.0
		 */
		@RequestMapping(value="/getMAT_CODEList")
		@ResponseBody
		public Object getMAT_CODEList() throws Exception{
			String result = "500";
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd = this.getPageData();		
			List<PageData>	varList = storage_unitService.getMAT_CODEList(pd);	//列出列表		
			try{			
				
				if(varList!=null && varList.size()!=0){	
					result = "200";
				}else{
					result = "201";
				}
			}catch (Exception e){
				result = "500";
			}finally{
				map.put("varList", varList);
				map.put("result", result);
			}
			return map;
		}
		
		/**获取容器列表-可搜索-前100条
		 * @throws Exception
		 */
		@RequestMapping(value="/getVesselList")
		@ResponseBody
		public Object getVesselList() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-员工名
			if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
			List<PageData>	varList = storage_unitService.getVesselList(pd);	//列出员工列表
			map.put("varList", varList);
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}
		
		/**
		 * 容器扫码校验
		 */
		@RequestMapping(value="goVesselVerify")
		@ResponseBody
		public Object goVesselVerify() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			try{
				PageData pd = new PageData();
				PageData numpd = new PageData();
				pd = this.getPageData();
				numpd = storage_unitService.getVesselNum(pd);//通过单号获取数量
				if(Integer.parseInt(numpd.get("NUM").toString())>0) {//判断数量是否大于0，大于0存在数据进行获取
					pd = storage_unitService.getVesselVerify(pd);//通过单号获取数据
				}else {
					map.put("pd", pd);
					map.put("msg", "操作失败");
					map.put("errInfo", "errInfo");
					return map;
				}
				map.put("pd", pd);
				map.put("msg", "操作成功");
				map.put("errInfo", "success");
				return map;
			}catch(Exception e){
				map.put("pd", "");
				map.put("msg", "操作失败");
				map.put("errInfo", "errInfo");
				return map;
			}
		}
}
