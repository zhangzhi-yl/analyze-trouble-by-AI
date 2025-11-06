package org.yy.controller.mm;

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
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：物料拆分表
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-02
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MATSPLIT")
public class MATSPLITController extends BaseController {
	
	@Autowired
	private MATSPLITService MATSPLITService;//物料拆分
	
	@Autowired
	private OperationRecordService operationrecordService;//操作日志
	
	@Autowired
	private StockListDetailService StockListDetailService;//出入库单明细
	
	@Autowired
	private StockService StockService;//库存
	
	/**入库单二级明细新增一物一码物料
	 * 选择入库仓库、库位，进行扫描二维码（是否唯一码物料，物料代码，物料数量），添加入库二级明细
	 * @author 宋
	 * @date 2020-12-10
	 * @param pd.StockListDetail_ID
	 * @param pd.WH_WAREHOUSE_ID
	 * @param pd.WH_LOCATION_ID
	 * @param pd.FUniqueCode
	 * @param pd.ItemID
	 * @param pd.FMainUnit
	 * @throws Exception
	 */
	@RequestMapping(value="/addW")
	@ResponseBody
	public Object addW() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();		
		String WH_WAREHOUSE_ID = pd.getString("WH_WAREHOUSE_ID");//仓库id
		String WH_LOCATION_ID = pd.getString("WH_LOCATION_ID");//库位id
		String StockListDetail_ID = pd.getString("StockListDetail_ID");//出入库单明细id
		String FUniqueCode = pd.getString("FUniqueCode");
		PageData detail = StockListDetailService.findById(pd);
		String MAT_AUXILIARYMX_CODE = "";
		if(null!=detail && detail.containsKey("MAT_AUXILIARYMX_CODE")) {
			MAT_AUXILIARYMX_CODE=detail.getString("MAT_AUXILIARYMX_CODE");
		}
		String[] arr = FUniqueCode.split(",YL,");//拆分，W，唯一码，物料数量，辅助属性值code
		//是否唯一码，是否是该明细对应的物料辅助属性的二维码
		if(arr.length>1 && arr[0].equals("W") && MAT_AUXILIARYMX_CODE.equals(arr[3])) {
			//检验该单据的明细中是否已经存在该唯一码
			PageData splitpd = new PageData();
			splitpd.put("FUniqueCode", arr[1]);
			splitpd.put("FRelatedID", StockListDetail_ID);
			List<PageData> splitList = MATSPLITService.listAll(splitpd);
			//检验库存中是否已经存在该唯一码
			PageData Stock = new PageData();
			Stock.put("OneThingCode", arr[1]);
			List<PageData> stockList = StockService.listAll(Stock);
			if(!splitList.isEmpty() && splitList.size()>0) {
				errInfo="existedStock";
			}else if(!stockList.isEmpty() && stockList.size()>0) {
				errInfo="existedStock";
			} else {
				pd.put("MATSPLIT_ID", this.get32UUID());	//主键
				//pd.put("ItemID", "FCode");//物料id
				pd.put("FUniqueCode", arr[1]);//唯一码
				pd.put("FRelatedID", StockListDetail_ID);
				pd.put("FCreatetime", Tools.date2Str(new Date()));
				pd.put("FCreator", "");
				//pd.put("FMainUnit", "FMainUnit");
				pd.put("FQuantity", arr[2]);
				pd.put("FTargetQuantity", arr[2]);
				pd.put("FIsScan", "是");
				pd.put("WH_WAREHOUSE_ID", WH_WAREHOUSE_ID);
				pd.put("WH_LOCATION_ID", WH_LOCATION_ID);
				pd.put("MaterialSPropKeyCode", MAT_AUXILIARYMX_CODE);
				MATSPLITService.save(pd);
			}
		} else {
			errInfo="formatError";
			pd=null;
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
	//@RequiresPermissions("MATSPLIT:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MATSPLIT_ID", this.get32UUID());	//主键
		MATSPLITService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("MATSPLIT:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MATSPLITService.delete(pd);
		//插入操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("MATSPLIT:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdNum=MATSPLITService.getRepeatNum(pd);//验证唯一码唯一性
		if(pdNum == null || Integer.parseInt(pdNum.get("FNum").toString())==0) {
			MATSPLITService.edit(pd);
			//插入操作日志
		}else {
			errInfo = "fail1";//唯一码重复
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("MATSPLIT:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_FUniqueCode = pd.getString("KEYWORDS_FUniqueCode");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS_FUniqueCode))pd.put("KEYWORDS_FUniqueCode", KEYWORDS_FUniqueCode.trim());
		page.setPd(pd);
		List<PageData>	varList = MATSPLITService.list(page);	//列出MATSPLIT列表
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
	//@RequiresPermissions("MATSPLIT:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MATSPLITService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("MATSPLIT:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MATSPLITService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		//插入操作日志
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
		titles.add("数量");	//3
		titles.add("关联ID");	//4
		titles.add("创建时间");	//5
		titles.add("创建人");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = MATSPLITService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ItemID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FUniqueCode"));	    //2
			vpd.put("var3", varOList.get(i).get("FQuantity").toString());	//3
			vpd.put("var4", varOList.get(i).getString("FRelatedID"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCreatetime"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCreator"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
