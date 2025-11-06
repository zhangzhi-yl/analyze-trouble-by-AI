package org.yy.controller.app;

import java.math.BigDecimal;
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
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

import org.yy.entity.PageData;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockListScanningOutService;

/** 
 * 说明：出库扫码记录
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appStockListScanningOut")
public class AppStockListScanningOutController extends BaseController {
	
	@Autowired
	private StockListScanningOutService StockListScanningOutService;
	@Autowired
	private StockListDetailService StockListDetailService;
	/**批量保存，扫码提交，记录
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/addAll")
	@ResponseBody
	public Object addAll() throws Exception{
		try {
			Map<String, Object> map = new HashMap<>();
			PageData pd = new PageData();
			String time = Tools.date2Str(new Date());
			pd = this.getPageData();
			if(pd.containsKey("qty")) {
				int a=Integer.parseInt(pd.get("qty").toString());
				for(int i=0;i<a;i++) {
					pd.put("StockListScanningOut_ID", this.get32UUID());	//主键
					pd.put("FCreatePersonID", pd.getString("STAFF_ID"));	//
					pd.put("FCreateTime", time);	//
					StockListScanningOutService.save(pd);
					//开始回写 出库数量
					String StockListDetail_ID = pd.getString("StockListDetail_ID");
					PageData pdData = new PageData();
					pdData.put("StockListDetail_ID",StockListDetail_ID);
					PageData findById = StockListDetailService.findById(pdData);
					if(null!=findById){
						//获取实际数量
						Object MaterialQuantityObj = findById.get("MaterialQuantity");
						if(StringUtil.isEmpty(String.valueOf(MaterialQuantityObj))){
							MaterialQuantityObj = "0";
						}
						BigDecimal MaterialQuantityDec = new BigDecimal(String.valueOf(MaterialQuantityObj));
						if(StringUtil.isNotEmpty(String.valueOf(pd.get("ActualCount")))){
							MaterialQuantityDec = MaterialQuantityDec.add(new BigDecimal(String.valueOf(pd.get("ActualCount"))));
						}
						//实际数量累加到 明细表
						findById.put("MaterialQuantity", MaterialQuantityDec);
						//覆盖仓库仓位id
						findById.put("WarehouseID", pd.getString("WarehouseID"));				
						findById.put("PositionID", pd.getString("PositionID"));
						StockListDetailService.edit(findById);
						
					}		
				}
			}
			map.put("pd", pd);
			return AppResult.success(map, "操作成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("StockListScanningOut:add")
	@ResponseBody
	public Object add() throws Exception{
		try {
			PageData pd = new PageData();
			String time = Tools.date2Str(new Date());
			pd = this.getPageData();
			pd.put("StockListScanningOut_ID", this.get32UUID());	//主键
			pd.put("FCreatePersonID", pd.getString("STAFF_ID"));	//
			pd.put("FCreateTime", time);	//
			StockListScanningOutService.save(pd);
			//开始回写 出库数量
			String StockListDetail_ID = pd.getString("StockListDetail_ID");
			PageData pdData = new PageData();
			pdData.put("StockListDetail_ID",StockListDetail_ID);
			PageData findById = StockListDetailService.findById(pdData);
			if(null!=findById){
				//获取实际数量
				Object MaterialQuantityObj = findById.get("MaterialQuantity");
				if(StringUtil.isEmpty(String.valueOf(MaterialQuantityObj))){
					MaterialQuantityObj = "0";
				}
				BigDecimal MaterialQuantityDec = new BigDecimal(String.valueOf(MaterialQuantityObj));
				if(StringUtil.isNotEmpty(String.valueOf(pd.get("ActualCount")))){
					MaterialQuantityDec = MaterialQuantityDec.add(new BigDecimal(String.valueOf(pd.get("ActualCount"))));
				}
				//实际数量累加到 明细表
				findById.put("MaterialQuantity", MaterialQuantityDec);
				//覆盖仓库仓位id
				findById.put("WarehouseID", pd.getString("WarehouseID"));				
				findById.put("PositionID", pd.getString("PositionID"));
				StockListDetailService.edit(findById);
				
			}	
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pd", pd);
			return AppResult.success(map, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("StockListScanningOut:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockListScanningOutService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("StockListScanningOut:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockListScanningOutService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("StockListScanningOut:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = StockListScanningOutService.list(page);	//列出StockListScanningOut列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("StockListScanningOut:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = StockListScanningOutService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("StockListScanningOut:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			StockListScanningOutService.deleteAll(ArrayDATA_IDS);
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
		titles.add("物料id");	//1
		titles.add("唯一码");	//2
		titles.add("实际数量");	//3
		titles.add("辅助属性值");	//4
		titles.add("创建人");	//5
		titles.add("创建时间");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = StockListScanningOutService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_BASIC_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("OneThingCode"));	    //2
			vpd.put("var3", varOList.get(i).get("ActualCount").toString());	//3
			vpd.put("var4", varOList.get(i).getString("MaterialSPropKey"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCreatePersonID"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCreateTime"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
