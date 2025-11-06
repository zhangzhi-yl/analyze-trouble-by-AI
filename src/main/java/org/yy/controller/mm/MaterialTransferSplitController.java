package org.yy.controller.mm;

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
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.MaterialTransferApplicationDetailsService;
import org.yy.service.mm.MaterialTransferSplitService;
import org.yy.service.mm.StockService;

/** 
 * 说明：物料转移物料拆分表
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MaterialTransferSplit")
public class MaterialTransferSplitController extends BaseController {
	
	@Autowired
	private MaterialTransferSplitService MaterialTransferSplitService;
	
	@Autowired
	private MaterialTransferApplicationDetailsService MaterialTransferApplicationDetailsService;//转移明细
	
	@Autowired
	private StockService StockService;//库存
	
	@Autowired
	private StaffService staffService;//员工
	
	/**转入单二级明细扫描一物一码物料更新目标仓位、是否扫码状态、数量
	 * 	选择目标仓库、库位，进行扫描二维码（是否唯一码物料），
	 * 	验证该单据下是否存在该物料二级明细，如果存在，更新转入单二级明细
	 * @author 宋
	 * @date 2020-12-10
	 * @param pd.MTADetails_ID
	 * @param pd.FUniqueCode
	 * @param pd.WH_WAREHOUSE_ID
	 * @param pd.WH_LOCATION_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/editW")
	@ResponseBody
	public Object editW() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String MTADetails_ID = pd.getString("MTADetails_ID");//转出明细id
		String FUniqueCode = pd.getString("FUniqueCode");//唯一码
		String WH_WAREHOUSE_ID = pd.getString("WH_WAREHOUSE_ID");//目标仓库id
		String WH_LOCATION_ID = pd.getString("WH_LOCATION_ID");//目标仓位id
		PageData detail = MaterialTransferApplicationDetailsService.findById(pd);
		String MAT_AUXILIARYMX_CODE = "";
		if(null!=detail && detail.containsKey("MAT_AUXILIARYMX_CODE")) {
			MAT_AUXILIARYMX_CODE=detail.getString("MAT_AUXILIARYMX_CODE");
		}
		String[] arr = FUniqueCode.split(",YL,");//按照，YL，拆分      格式： W，唯一码，物料数量，辅助属性值code
		//是否唯一码，是否是该明细对应的物料辅助属性的二维码
		if(arr.length>1 && "W".equals(arr[0])) {
			if(MAT_AUXILIARYMX_CODE.equals(arr[3])) {
				//通过FUniqueCode和MTADetails_ID查询转入单二级明细
				PageData stockpd = new PageData();
				stockpd.put("FUniqueCode", arr[1]);
				stockpd.put("FRelatedID", MTADetails_ID);
				PageData p = MaterialTransferSplitService.findByMTADetails_IDAndFUniqueCode(stockpd);
				if(null != p && p.containsKey("MaterialTransferSplit_ID")) {
					p.put("WH_WAREHOUSE_ID", WH_WAREHOUSE_ID);	//目标仓库id
					p.put("WH_LOCATION_ID", WH_LOCATION_ID);	//目标仓位id
					p.put("FRelatedID", MTADetails_ID);
					p.put("FQuantity", arr[2]);
					p.put("FIsScan", "是");
					p.put("MaterialSPropKeyCode", MAT_AUXILIARYMX_CODE);
					MaterialTransferSplitService.edit(p);
				} else {
					errInfo="invalidUniqueCode";
				}
			} else {
				errInfo="invalidCode";
			}
		} else {
			errInfo="formatError";
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**转出单二级明细新增一物一码物料
	 * 选择入库仓库、库位，进行扫描二维码（是否唯一码物料，物料代码，物料数量），添加转出二级明细
	 * @author 宋
	 * @date 2020-12-10
	 * @param pd.MTADetails_ID
	 * @param pd.FUniqueCode
	 * @throws Exception
	 */
	@RequestMapping(value="/addW")
	@ResponseBody
	public Object addW() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		String MTADetails_ID = pd.getString("MTADetails_ID");//转出明细id
		PageData detail = MaterialTransferApplicationDetailsService.findById(pd);
		String MAT_AUXILIARYMX_CODE = "";
		if(null!=detail && detail.containsKey("MAT_AUXILIARYMX_CODE")) {
			MAT_AUXILIARYMX_CODE=detail.getString("MAT_AUXILIARYMX_CODE");
		}
		String FUniqueCode = pd.getString("FUniqueCode");
		String[] arr = FUniqueCode.split(",YL,");//按照，YL，拆分      格式： W，唯一码，物料数量，辅助属性值code
		//是否唯一码，是否是该明细对应的物料辅助属性的二维码
		if(arr.length>1 && "W".equals(arr[0])) {
			if(MAT_AUXILIARYMX_CODE.equals(arr[3])) {
				//检验该单据的明细中是否已经存在该唯一码
				PageData splitpd = new PageData();
				splitpd.put("FUniqueCode", arr[1]);
				splitpd.put("FRelatedID", MTADetails_ID);
				List<PageData> splitList = MaterialTransferSplitService.listAll(splitpd);
				if(!splitList.isEmpty() && splitList.size()>0) {
					errInfo="existStock";
				} else {
					//根据唯一码查询库存
					PageData stockpd = new PageData();
					stockpd.put("OneThingCode", arr[1]);
					List<PageData> stockList = StockService.listAll(stockpd);
					if(!stockList.isEmpty() && stockList.size()>0 
							&& new BigDecimal(stockList.get(0).get("ActualCount").toString()).compareTo(new BigDecimal ("0"))==1) {
						pd.put("MaterialTransferSplit_ID", this.get32UUID());	//主键
						pd.put("ItemID", stockList.get(0).get("ItemID"));//物料id
						pd.put("FUniqueCode", arr[1]);//唯一码
						pd.put("FRelatedID", MTADetails_ID);
						pd.put("FCreatetime", Tools.date2Str(new Date()));
						pd.put("FCreator", staffid);// 
						pd.put("FMainUnit", stockList.get(0).get("FUnit"));
						pd.put("FQuantity", arr[2]);
						pd.put("FTargetQuantity", arr[2]);
						pd.put("FIsScan", "是");
						pd.put("MaterialSPropKeyCode", MAT_AUXILIARYMX_CODE);
						MaterialTransferSplitService.save(pd);
					} else {
						errInfo="invalidstock";
						pd=null;
					}
				} 
			} else {
				errInfo="invalidCode";
			}
		} else {
			errInfo="formatError";
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("MaterialTransferSplit:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MaterialTransferSplit_ID", this.get32UUID());	//主键
		MaterialTransferSplitService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("MaterialTransferSplit:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferSplitService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("MaterialTransferSplit:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferSplitService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("MaterialTransferSplit:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialTransferSplitService.list(page);	//列出MaterialTransferSplit列表
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
	//@RequiresPermissions("MaterialTransferSplit:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MaterialTransferSplitService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("MaterialTransferSplit:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MaterialTransferSplitService.deleteAll(ArrayDATA_IDS);
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
		titles.add("物料ID");	//1
		titles.add("唯一码");	//2
		titles.add("关联ID");	//3
		titles.add("创建时间");	//4
		titles.add("创建人");	//5
		titles.add("单位");	//6
		titles.add("数量");	//7
		titles.add("需求数量");	//8
		titles.add("是否扫码");	//9
		titles.add("备注11");	//10
		titles.add("备注12");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = MaterialTransferSplitService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ItemID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FUniqueCode"));	    //2
			vpd.put("var3", varOList.get(i).getString("FRelatedID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCreatetime"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCreator"));	    //5
			vpd.put("var6", varOList.get(i).getString("FMainUnit"));	    //6
			vpd.put("var7", varOList.get(i).get("FQuantity").toString());	//7
			vpd.put("var8", varOList.get(i).get("FTargetQuantity").toString());	//8
			vpd.put("var9", varOList.get(i).getString("FIsScan"));	    //9
			vpd.put("var10", varOList.get(i).getString("WH_WAREHOUSE_ID"));	    //10
			vpd.put("var11", varOList.get(i).getString("WH_LOCATION_ID"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
