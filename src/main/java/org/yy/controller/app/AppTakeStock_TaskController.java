package org.yy.controller.app;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.pdf417.encoder.PDF417;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mm.StockService;
import org.yy.service.mm.TakeStock_Task_ExecuteService;
import org.yy.service.mm.TakeStock_Task_MaterialService;
import org.yy.service.mm.TakeStock_WinOrLoseService;
import org.yy.service.mm.Takestock_TaskService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;

/** 
 * 说明：盘点任务
 * 作者：YuanYe
 * 时间：2020-11-26
 * 
 */
@Controller
@RequestMapping("/appTakeStock_task")
public class AppTakeStock_TaskController extends BaseController {
	
	@Autowired
	private Takestock_TaskService takestock_taskService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private TakeStock_Task_MaterialService TakeStock_Task_MaterialService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private TakeStock_WinOrLoseService TakeStock_WinOrLoseService;
	@Autowired
	private TakeStock_Task_ExecuteService TakeStock_Task_ExecuteService;
	@Autowired
	private StockService StockService;
	@Autowired
	private StockService stockService;
	@Autowired
	private WH_LocationService wh_locationService;
	/**
	 * 生成盘盈盘亏单
	 */
	@RequestMapping(value="CreatResult")
	@ResponseBody
	public Object CreatResult(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData pdMA = new PageData();
			PageData pdEX = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			pd = takestock_taskService.findById(pd);	//根据ID读取
			//获取盘点任务物料，
			List<PageData> varList = TakeStock_Task_MaterialService.listAll(pd);
			//循环物料表
			for (int i = 0; i < varList.size(); i++) {
				//获取物料个数
				Integer material = Integer.parseInt(varList.get(i).get("Repertory_Count").toString());
				//获取物料表主键ID,根据ID从执行表中获取物料数量
				String TakeStock_Task_Material_ID = varList.get(i).get("TakeStock_Task_Material_ID").toString();
				pd.put("TakeStock_Task_Material_ID", TakeStock_Task_Material_ID);
				pdMA = takestock_taskService.getCount(pd);
				pdEX = takestock_taskService.findByMaterialId(pd);//获取单条物料的数据
				//获取盘点物料下执行表中差异单编号与盘点任务差异单编号相同的物料个数
				Integer excuteCount = Integer.parseInt(pdMA.get("num").toString());
				//计算单条盘点结果
				Integer result = excuteCount - material;//计算物料数量差异
				pd.put("TakeStock_WinOrLose_ID", this.get32UUID());//盘盈盘亏单ID
				pd.put("FEntryID", varList.get(i).get("FEntryID"));//行号
				pd.put("TakeStock_Task_ID", pd.get("TakeStock_Task_ID"));//盘点任务ID
				pd.put("TakeStock_Batch",pd.get("DifferenceOrder_NUM"));//盘点批次
				pd.put("Material_ID", varList.get(i).get("Material_ID"));//物料ID
				pd.put("TakeStock_Result", result);//盘点结果
				if(result >= 0){
					pd.put("WinOrLose", 1);//盘盈
				}else{
					pd.put("WinOrLose", 0);//盘亏
				}
				pd.put("Difference_Count", Math.abs(result));//差异数量
				pd.put("DifferenceOrder_NUM", pd.get("DifferenceOrder_NUM"));//差异单批号
				pd.put("FOperator", 0);//是否处理 默认否
				pd.put("Relevance_Order", "");//关联单号
				pd.put("IfFEntryClose", 0);//行关闭  默认否
				pd.put("FExplanation", "");//描述
				TakeStock_WinOrLoseService.save(pd);//保存
			}
			takestock_taskService.CreatOrder(pd);
			return AppResult.success("生成成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**
	 * 盘点任务提交审核
	 */
	@RequestMapping(value="goCheck")
	@ResponseBody
	public Object goCheck(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			takestock_taskService.goCheck(pd);
			return AppResult.success("操作成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**盘点任务列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(AppPage page,HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "FMakeBillsTime";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}
			page.setPd(pd);
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(takestock_taskService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			varList = pageInfo.getList();
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = takestock_taskService.findById(pd);	//根据ID读取
			map.put("pd", pd);
			return AppResult.success(pd, "获取成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	/**盘点任务物料列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listMX")
	@ResponseBody
	public Object listMX(AppPage page,HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "FEntryID";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}
			page.setPd(pd);
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(TakeStock_Task_MaterialService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			varList = pageInfo.getList();
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 更新行关闭状态
	 */
	@RequestMapping(value="FinishEntryClose")
	@ResponseBody
	public Object FinishEntryClose(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			TakeStock_Task_MaterialService.CloseHang(pd);
			return AppResult.success("success","操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	 /**获取一级物料明细详情
	  * 通过一级物料ID获取一级物料详情
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getTaskMaterial")
	@ResponseBody
	public Object getTaskMaterial(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = TakeStock_Task_MaterialService.getTaskMaterial(pd);	//根据盘点任务ID获取数量
			return AppResult.success(pd, "获取成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	 /**获取上一条或下一条详情
	  * 入参盘点任务ID、行号、上一条下一条标识
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getTaskMaterialDetails")
	@ResponseBody
	public Object getTaskMaterialDetails(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "errInfo";
			String msg = "无待执行任务";
			PageData pd = new PageData();
			PageData pdNum = new PageData();
			PageData TaskMaterialPd = new PageData();
			pd = this.getPageData();
			int identification = Integer.parseInt(pd.get("identification").toString());//标识 0上一条  1下一条
			String TakeStock_Task_ID = pd.getString("TakeStock_Task_ID");
			int FEntryID = Integer.parseInt(pd.get("FEntryID").toString());
			pdNum=TakeStock_Task_MaterialService.getTaskMaterialNum(pd);	//根据盘点任务ID获取数量
			int num=Integer.parseInt(pdNum.get("TaskMaterialNum").toString());//任务物料数量
			List<PageData> varList = TakeStock_Task_MaterialService.getTaskMaterialAll(pd);//通过任务ID获取物料明细列表
		    if(identification==0) {//点击上一条
		    	FEntryID=FEntryID-1;//当前行号-1获取上一条行号
		    		for (int i = FEntryID; i < varList.size(); i--) {
		    			if(i>0) {
		    				PageData TaskMaterialPdAll = new PageData();
			    			TaskMaterialPdAll=varList.get(i-1);
			    			if(null!=TaskMaterialPdAll.getString("IfFEntryClose")&&"未关闭".equals(TaskMaterialPdAll.getString("IfFEntryClose"))) {
			    				TaskMaterialPd=TaskMaterialPdAll;
			    				msg = "获取成功";
			    				errInfo = "success";
			    				break;
			    			}else {
			    				continue;
			    			}
		    			}else {
		    				break;
		    			}
		    			
					}
		    }
		    if(identification==1) {//点击下一条
		    	FEntryID=FEntryID+1;//当前行号+1获取下一条行号
		    	if(FEntryID<=num) {
		    		for (int i = FEntryID; i <=varList.size(); i++) {
		    			if(i<=num) {
		    				PageData TaskMaterialPdAll = new PageData();
			    			TaskMaterialPdAll=varList.get(i-1);
			    			if(null!=TaskMaterialPdAll.getString("IfFEntryClose")&&"未关闭".equals(TaskMaterialPdAll.getString("IfFEntryClose"))) {
			    				TaskMaterialPd=TaskMaterialPdAll;
			    				msg = "获取成功";
			    				errInfo = "success";
			    				break;
			    			}else {
			    				continue;
			    			}
		    			}else {
		    				break;
		    			}
		    			
					}
		    	
		    	
		    }
		    }
			return AppResult.success(TaskMaterialPd, msg, errInfo);
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	 /**获取一级物料明细数量
	  * 通过盘点任务ID获取一级物料明细数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getTaskMaterialNum")
	@ResponseBody
	public Object getTaskMaterialNum(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = TakeStock_Task_MaterialService.getTaskMaterialNum(pd);	//根据盘点任务ID获取数量
			return AppResult.success(pd, "获取成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	 /**获取基础物料信息
	  * 通过物料ID或物料代码获取物料信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getMaterial")
	@ResponseBody
	public Object getMaterial(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = TakeStock_Task_MaterialService.getTaskMaterialNum(pd);	//根据盘点任务ID获取数量
			return AppResult.success(pd, "获取成功", "success");
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	 /**扫码判断
	  * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/scanVerify")
	@ResponseBody
	public Object scanVerify(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "errInfo";
			String msg = "无物料信息请核对";
			PageData pd = new PageData();
			PageData DetailsItempd = new PageData();
			PageData getDetailsItemPd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			String TakeStock_Task_Type=pd.getString("TakeStock_Task_Type");
			String MAT_CODE=pd.getString("MAT_CODE");
			String oneThingsCode = "";
			String qrCode = "";
			String materialId = "";
			Double count = 0.0;
			if(TakeStock_Task_Type.equals("按仓库盘点")) {
				// 唯一码 带出 物料码 和 序列号
				if ("W".equals(code.substring(0, 1))) {
					// 根据唯一码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						oneThingsCode = codeSplitList.get(1);
						count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
						PageData pData = new PageData();
						// 一物码
						pData.put("OneThingCode", oneThingsCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							if(MAT_CODE.equals(DetailsItempd.getString("MAT_CODE"))) {
								getDetailsItemPd=DetailsItempd;
								getDetailsItemPd.put("count", count);
								msg = "获取成功";
			    				errInfo = "success";
							}
							
						}
						
					}
				}
				// 类型码 带出 物料码
				if ("L".equals(code.substring(0, 1))) {
					// 根据类型码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						qrCode = codeSplitList.get(1);
						count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
						PageData pData = new PageData();
						// 类型码
						pData.put("QRCode", qrCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							if(MAT_CODE.equals(DetailsItempd.getString("MAT_CODE"))) {
								getDetailsItemPd=DetailsItempd;
								getDetailsItemPd.put("count", count);
								msg = "获取成功";
			    				errInfo = "success";
							}
						}
					}
				}
			}
			if(TakeStock_Task_Type.equals("按工单盘点")) {
				// 唯一码 带出 物料码 和 序列号
				if ("W".equals(code.substring(0, 1))) {
					// 根据唯一码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						oneThingsCode = codeSplitList.get(1);
						count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
						String MAT_AUXILIARYMX_CODE = codeSplitList.get(3);
						PageData pData = new PageData();
						// 一物码
						pData.put("OneThingCode", oneThingsCode);
						pData.put("MAT_AUXILIARYMX_CODE", MAT_AUXILIARYMX_CODE);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							if(MAT_CODE.equals(DetailsItempd.getString("MAT_CODE"))) {
								getDetailsItemPd=DetailsItempd;
								getDetailsItemPd.put("count", count);
								msg = "获取成功";
			    				errInfo = "success";
							}
						}
						
					}
				}
				// 类型码 带出 物料码
				if ("L".equals(code.substring(0, 1))) {
					// 根据类型码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						qrCode = codeSplitList.get(1);
						count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
						String MAT_AUXILIARYMX_CODE = codeSplitList.get(3);
						PageData pData = new PageData();
						// 类型码
						pData.put("QRCode", qrCode);
						pData.put("MAT_AUXILIARYMX_CODE", MAT_AUXILIARYMX_CODE);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							if(MAT_CODE.equals(DetailsItempd.getString("MAT_CODE"))) {
								getDetailsItemPd=DetailsItempd;
								getDetailsItemPd.put("count", count);
								msg = "获取成功";
			    				errInfo = "success";
							}
						}
					}
				}
			}
			
			return AppResult.success(getDetailsItemPd, msg, errInfo);
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	 /**库位扫码验证
	  * 库位扫码验证，通过扫码值，拆分，查询仓库仓位相关信息返回前台
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/locationScanVerify")
	@ResponseBody
	public Object locationScanVerify(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "errInfo";
			String msg = "库位信息不准确";
			PageData pd = new PageData();
			PageData DetailsLocationpd = new PageData();
			PageData getDetailsLocationpd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			String warehouseCode = "";
			String locationCode = "";
			String[] split = code.split(",YL,");
			if (split.length > 1) {
				List<String> codeSplitList = Lists.newArrayList(split);
				warehouseCode = codeSplitList.get(1);
				locationCode = codeSplitList.get(2);
				PageData pData = new PageData();
				pData.put("warehouseCode", warehouseCode);
				pData.put("locationCode", locationCode);
				DetailsLocationpd = wh_locationService.locationScanVerify(pData);
				if(null!=DetailsLocationpd) {
					getDetailsLocationpd=DetailsLocationpd;
					msg = "获取成功";
	    			errInfo = "success";
				}
			}
			return AppResult.success(getDetailsLocationpd, msg, errInfo);
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	 /**扫码搜索
	  * 通过扫码的物料条码进行拆分，拆分后通过物料条码查询物料代码、名称相关信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/scanSeek")
	@ResponseBody
	public Object scanSeek(HttpServletResponse response) throws Exception{
		try{
			String errInfo = "errInfo";
			String msg = "无物料信息请核对";
			PageData pd = new PageData();
			PageData DetailsItempd = new PageData();
			PageData getDetailsItemPd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			String oneThingsCode = "";
			String qrCode = "";
			String materialId = "";
				// 唯一码 带出 物料码 和 序列号
				if ("W".equals(code.substring(0, 1))) {
					// 根据唯一码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						oneThingsCode = codeSplitList.get(1);
						PageData pData = new PageData();
						// 一物码
						pData.put("OneThingCode", oneThingsCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							getDetailsItemPd=DetailsItempd;
							msg = "获取成功";
		    				errInfo = "success";
							
						}
						
					}
				}
				// 类型码 带出 物料码
				if ("L".equals(code.substring(0, 1))) {
					// 根据类型码去库存中查找物料叫啥
					String[] split = code.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						qrCode = codeSplitList.get(1);
						PageData pData = new PageData();
						// 类型码
						pData.put("QRCode", qrCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if(null!=DetailsItempd) {
							getDetailsItemPd=DetailsItempd;
							msg = "获取成功";
		    				errInfo = "success";
						}
					}
				}
			
			return AppResult.success(getDetailsItemPd, msg, errInfo);
		}catch(Exception e){
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**盘点执行，扫码新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String msg = "";
			PageData pd = new PageData();
			PageData pdMa = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			System.out.println("pd:"+pd);
			//获取人员账号，并根据账号查询名称
			PageData pdl = new PageData();
			String USERNAME = pd.getString("UserName");
			pdl.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pdl);
			pdMa = TakeStock_Task_ExecuteService.getMaterial(pd);//根据物料ID从盘点任务明细表中获取数据，判断当前扫码的物料是否存在于表中
			if(pdMa != null){//判断 ，如果当前扫码的物料存在于盘点任务物料明细表中
				Integer EnteryID = TakeStock_Task_ExecuteService.getEntryID(pdMa);
				pd.put("TakeStock_Task_Execute_ID", this.get32UUID());	//主键
				pd.put("FEntryID", EnteryID+1);//行号
				pd.put("Warehouse_ID",pd.getString("Warehouse_ID"));//仓库ID 默认空
				pd.put("Seat_ID", pd.getString("Seat_ID"));//仓位ID  默认空
				pd.put("TakeStock_Task_Material_ID", pd.getString("TakeStock_Task_Material_ID"));//盘点任务物料ID
				pd.put("TakeStock_Task_ID", pd.getString("TakeStock_Task_ID"));//盘点任务ID
				pd.put("Material_ID", pd.getString("Material_ID"));//物料ID
				pd.put("TakeStock_Count",  Double.parseDouble(pd.get("TakeStock_Count").toString()));//数量 默认1 
				pd.put("TakeStock_Task_PersonID", staff.getString("STAFF_ID"));//盘点人
				pd.put("TakeStock_Task_Time", Tools.date2Str(new Date()));//盘点时间 默认当前时间
				pd.put("IfFEntryClose", 1);// 行关闭  默认未关闭
				pd.put("FExplanation", pd.getString("FExplanation"));//描述，默认为空 
				pd.put("DifferenceOrder_NUM", pd.getString("DifferenceOrder_NUM"));//差异单批号
				TakeStock_Task_ExecuteService.save(pd);
				msg = "扫描成功";
				map.put("msg", msg);
				return AppResult.success(map,"操作成功", "success");
			}else{
				 msg = "该物料不存在于此盘点任务中，请核实";//错误提示
				 map.put("msg", msg);
				return AppResult.failed(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**
	 * 盘点完成
	 */
	@RequestMapping(value="FinishTakeStock")
	@ResponseBody
	public Object FinishTakeStock(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			TakeStock_Task_ExecuteService.FinishTakeStock(pd);
			return AppResult.success("操作成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**盘点任务执行物料列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listExecute")
	@ResponseBody
	public Object listExecute(AppPage page,HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "RowNum";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}
			page.setPd(pd);
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(TakeStock_Task_ExecuteService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
}
