package org.yy.controller.pp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchasDetailsService;
import org.yy.service.pp.PurchaseApplyForMxService;

/** 
 * 说明：采购申请明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchasDetails")
public class PurchasDetailsController extends BaseController {
	
	@Autowired
	private PurchasDetailsService PurchasDetailsService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PurchaseApplyForMxService PurchaseApplyForMxService;//采购申请明细
	@Autowired
	private MAT_BASICService mat_basicService;//物料信息
	/**保存
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PurchasDetails:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PurchasDetails_ID", this.get32UUID());	//主键
		pd.put("RowClose", "N");	//行关闭
		pd.put("PurchaseApplyKey", "0");	//数量
		pd.put("FPushCount", "0");	//下推数量（推采购订单）
		pd.put("FIsPush", "N");	//是否下推
		PageData pdRowNum=PurchasDetailsService.getRowNum(pd);//生成行号
		if(pdRowNum != null) {
			pd.put("RowNum", pdRowNum.get("RowNum").toString());
		}
		PurchasDetailsService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请明细");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchasDetails_ID"));
		operationrecordService.save(pdOp);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("PurchasDetails:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchasDetailsService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("PurchasDetails:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchasDetailsService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请明细");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchasDetails_ID"));
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		return map;
	}
	
	/**采购申请明细列表
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PurchasDetails:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = PurchasDetailsService.list(page);	//列出PurchasDetails列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请明细");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseApplyID"));
		operationrecordService.save(pdOp);
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
	@RequiresPermissions("PurchasDetails:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PurchasDetailsService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("PurchasDetails:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PurchasDetailsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("行关闭");	//1
		titles.add("采购申请ID");	//2
		titles.add("物料ID");	//3
		titles.add("辅助属性");	//4
		titles.add("辅助属性值");	//5
		titles.add("数量");	//6
		titles.add("规格描述");	//7
		titles.add("单位");	//8
		titles.add("采购申请数值");	//9
		titles.add("采购申请单位");	//10
		titles.add("建议采购时间");	//11
		titles.add("采购提前期");	//12
		titles.add("需求时间");	//13
		titles.add("采购员");	//14
		titles.add("占用库存");	//15
		titles.add("单价");	//16
		titles.add("备注");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchasDetailsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("RowClose"));	    //1
			vpd.put("var2", varOList.get(i).getString("PurchaseApplyID"));	    //2
			vpd.put("var3", varOList.get(i).getString("MaterialID"));	    //3
			vpd.put("var4", varOList.get(i).getString("SProp"));	    //4
			vpd.put("var5", varOList.get(i).getString("SPropKey"));	    //5
			vpd.put("var6", varOList.get(i).get("FCount").toString());	//6
			vpd.put("var7", varOList.get(i).getString("SpecificationsDesc"));	    //7
			vpd.put("var8", varOList.get(i).getString("FUnit"));	    //8
			vpd.put("var9", varOList.get(i).getString("PurchaseApplyKey"));	    //9
			vpd.put("var10", varOList.get(i).getString("PurchaseApplyUnit"));	    //10
			vpd.put("var11", varOList.get(i).getString("SuggestedPurchasingTime"));	    //11
			vpd.put("var12", varOList.get(i).getString("PurchaseLeadTime"));	    //12
			vpd.put("var13", varOList.get(i).getString("DemandTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FPurchaser"));	    //14
			vpd.put("var15", varOList.get(i).getString("OccupationOfInventory"));	    //15
			vpd.put("var16", varOList.get(i).get("UnitPrice").toString());	//16
			vpd.put("var17", varOList.get(i).getString("FExplanation"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-21
	 * @param PurchasDetails_ID:采购申请明细ID、RowClose:行关闭（Y/N）
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchasDetailsService.rowClose(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String RowClose= pd.getString("RowClose");
		if(RowClose.equals("Y")) {
			pdOp.put("OperationType", "行关闭");//操作类型
		}else {
			pdOp.put("OperationType", "反行关闭");//操作类型
		}
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
	    pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请明细");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchasDetails_ID"));//删改数据ID
		operationrecordService.save(pdOp);	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**采购申请明细-物料列表
	 * @author 管悦
	 * @date 2020-11-21
	 * @param PurchaseList_ID：采购订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllMat")
	@ResponseBody
	public Object listAllMat() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_FNum = pd.getString("KEYWORDS_FNum");						//采购申请编号
		if(Tools.notEmpty(KEYWORDS_FNum))pd.put("KEYWORDS_FNum", KEYWORDS_FNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart");						//创建开始时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd");						//创建结束时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DemandTimeStart = pd.getString("KEYWORDS_DemandTimeStart");						//需求开始时间
		if(Tools.notEmpty(KEYWORDS_DemandTimeStart))pd.put("KEYWORDS_DemandTimeStart", KEYWORDS_DemandTimeStart.trim());
		String KEYWORDS_DemandTimeEnd = pd.getString("KEYWORDS_DemandTimeEnd");						//需求结束时间
		if(Tools.notEmpty(KEYWORDS_DemandTimeEnd))pd.put("KEYWORDS_DemandTimeEnd", KEYWORDS_DemandTimeEnd.trim());		
		List<PageData>	varList = PurchasDetailsService.listAllMat(pd);	
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细-采购申请物料列表");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.getString("PurchaseList_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**下推采购申请
	  * 通过ID查询信息往PP_PurchaseApplyForMx采购申请明细插入数据
	  * 并反写下推状态为Y
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/pushDown")
		@ResponseBody
		public Object pushDown() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData pdOp = new PageData();
			pd = this.getPageData();
			pdOp.put("FNAME", Jurisdiction.getName());
			String DATA_IDS = pd.getString("DATA_IDS");
			if(Tools.notEmpty(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				for(int i=0;i<ArrayDATA_IDS.length;i++) {
					PageData mta = new PageData();
					pd.put("PurchasDetails_ID", ArrayDATA_IDS[i]);
					mta=PurchasDetailsService.findById(pd);
					if(null != mta) {
						String currentTime = Tools.date2Str(new Date());
						PageData detailpd=new PageData();
						detailpd.put("PurchaseApplyForMx_ID", this.get32UUID());//采购申请单明细ID
						detailpd.put("MaterialID", mta.getString("MaterialID"));//物料ID
						detailpd.put("SpecificationsDesc", mta.getString("SpecificationsDesc"));//规格描述
						detailpd.put("FigureNum", mta.getString("FigureNum"));//图号
						detailpd.put("StandardNum", mta.getString("StandardNum"));//标准号
						detailpd.put("FProductQuantity", mta.getString("PurchaseApplyKey"));//产品数量
						detailpd.put("SalesOrder_ID", mta.getString("SalesOrder_ID"));//销售订单ID
						detailpd.put("SalesOrderMaterial_ID", mta.getString("OrderMaterialID"));//销售订单下物料ID
						detailpd.put("PlanningWorkOrder_ID", mta.getString("PlanningWorkOrder_ID"));//计划工单ID
						detailpd.put("FSupplier", mta.getString("FSupplier"));//供应商
						detailpd.put("FIfCreate", "N");//是否生成主表(N/Y)
						detailpd.put("FPushPeopleID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//推送人ID
						detailpd.put("FPushTime", currentTime);//推送时间
						detailpd.put("FType", mta.getString("FType"));//类型（零件/标准/电气）
						detailpd.put("FWorkblank", mta.getString("FWorkblank"));//毛坯
						detailpd.put("FRemark", mta.getString("FExplanation"));//备注
						detailpd.put("PurchasDetails_ID", mta.getString("PurchasDetails_ID"));//物料申请明细ID
						PurchaseApplyForMxService.save(detailpd);
						// 修改物料清单的下推状态为‘Y’
						PageData materialRequirement=new PageData();
						materialRequirement.put("PurchasDetails_ID", pd.getString("PurchasDetails_ID"));
						materialRequirement.put("FIsPush", "Y");
						PurchasDetailsService.updateFIsPush(materialRequirement);
					}
				}
			}else{
				errInfo = "error";
			}
			map.put("result", errInfo);
			return map;
		}
		
		/**从EXCEL导入到数据库
		 * @param file
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/readExcel")
		@ResponseBody
		public Object readExcel(
				@RequestParam(value="excel",required=false) MultipartFile file,
				@RequestParam(value="PurchaseApply_ID",required=false) String PurchaseApply_ID
				) throws Exception{
			Map<String,String> map = new HashMap<String,String>();
			String errInfo = "success";
			PageData basicPd = new PageData();
			PageData WLPd = new PageData();
			int succeedNum=0;
			int failNum=0;
			if (null != file && !file.isEmpty()) {
		        int realRowCount = 0;//真正有数据的行数
		        //得到工作空间
		        Workbook workbook = null;
		        try {
		            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        //得到sheet
		        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
		        realRowCount = sheet.getPhysicalNumberOfRows();
		        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
		        	Row row = sheet.getRow(rowNum);
		            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
		            	break;
		            }
		            if(row.getRowNum() == -1) {
		                continue;
		            }else {
		                if(row.getRowNum() == 0) {//第一行表头跳过
		                    continue;
		                }
		            }
		            try {
		            	if(null!=ObjectExcelRead.getCellValue(sheet, row, 1)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 1))) {//判断物料代码字符是否为空
		            		WLPd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 1));
			 	            WLPd=mat_basicService.getBasicId(WLPd);
			 	            String reg="^\\d+$";
			 	            if(null!=WLPd) {
			 	            	basicPd.put("PurchasDetails_ID", this.get32UUID());//物料清单明细ID
			 	            	basicPd.put("RowClose", "N");//行关闭
			 	            	basicPd.put("FIsPush", "N");//是否下推(N/Y)
			 	            	basicPd.put("RowNum", succeedNum+1);//行号
			 	 	            basicPd.put("PurchaseApplyID", PurchaseApply_ID);//主键ID
			 	 	            basicPd.put("MaterialID", WLPd.getString("MAT_BASIC_ID"));//物料ID
			 	 	            if(ObjectExcelRead.getCellValue(sheet, row, 6).matches(reg)==true) {//判断表格数量值是否为数字，不为数字填写0
			 	 	            	basicPd.put("PurchaseApplyKey", ObjectExcelRead.getCellValue(sheet, row, 6));//数量
			 	 	            }else {
			 	 	            	basicPd.put("PurchaseApplyKey", 0);//数量
			 	 	            }
			 	 	            basicPd.put("FType", ObjectExcelRead.getCellValue(sheet, row, 3));//类型
			 	 	            basicPd.put("FigureNum", ObjectExcelRead.getCellValue(sheet, row, 4));//图号
			 	 	            basicPd.put("StandardNum", ObjectExcelRead.getCellValue(sheet, row, 5));//标准号
			 	 	            //basicPd.put("MAT_BASIC_ID", ObjectExcelRead.getCellValue(sheet, row, 7));//采购员
			 	 	            basicPd.put("SpecificationsDesc", ObjectExcelRead.getCellValue(sheet, row, 8));//规格描述
			 	 	            basicPd.put("FWorkblank", ObjectExcelRead.getCellValue(sheet, row, 9));//毛坯
			 	 	            /*basicPd.put("OccupationOfInventory", "是".equals(ObjectExcelRead.getCellValue(sheet, row, 10))?
			 	 	            		ObjectExcelRead.getCellValue(sheet, row, 10):"否");//是否占用*/			 	 	            
			 	 	            basicPd.put("FSupplier", ObjectExcelRead.getCellValue(sheet, row, 10));//供应商
			 	 	            basicPd.put("FExplanation", ObjectExcelRead.getCellValue(sheet, row, 11));//备注
			 	 	            basicPd.put("DemandTime", ObjectExcelRead.getCellValue(sheet, row, 12));//需求时间
			 	 	            
			 	 	          PurchasDetailsService.save(basicPd);
			 	 	          succeedNum=succeedNum+1;
			 	            }else {
			 	            	failNum=failNum+1;
			 	            }
		            	}else {
		            		failNum=failNum+1;
		            	}
			        } catch (IOException e) {
			            e.printStackTrace();
			            errInfo = "fail";
			        }
		           
		        }
			}
			map.put("succeedNum", succeedNum+"");				//返回成功数量结果
			map.put("failNum", failNum+"");				//返回失败数量结果
			map.put("result", errInfo);				//返回结果
			return map;
		}
		
		/**下载模版
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value="/downExcel")
		public void downExcel(HttpServletResponse response)throws Exception{
			FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "PurchasDetails.xlsx", "PurchasDetails.xlsx");
		}
}