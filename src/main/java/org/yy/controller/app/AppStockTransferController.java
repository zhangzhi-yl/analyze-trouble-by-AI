package org.yy.controller.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.app.AppService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mdm.EQM_POINT_INSPECTService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mm.MaterialTransferApplicationDetailsService;
import org.yy.service.mm.MaterialTransferApplicationFormService;
import org.yy.service.mm.StockListScanningInService;
import org.yy.service.mm.StockListScanningOutService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.mom.TEMBILL_EXECUTETICKService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 移库管理
 * @author YULONG
 *
 */
@RestController
@RequestMapping("/appStockTransfer")
public class AppStockTransferController extends BaseController{
	@Autowired
	private AppService appService;
	@Autowired
	private EQM_POINT_INSPECTService eqm_point_inspectService;
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	@Autowired
	private TEMBILL_EXECUTETICKService tembill_executetickService;
	@Autowired
	private MaterialTransferApplicationFormService MaterialTransferApplicationFormService;
	@Autowired
	private MaterialTransferApplicationDetailsService MaterialTransferApplicationDetailsService;
	@Autowired
	private StaffService staffService;//员工
	@Autowired
	private MAKECODEService MAKECODEService;// 制码接口
	@Autowired
	private StockService StockService;//库存
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;//采购订单明细
	@Autowired
	private SALESORDERService salesorderService;//销售订单
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;//销售订单明细
	@Autowired
	private PlanningWorkOrderMasterService PlanningWorkOrderMasterService;//主计划工单
	/**
	 * 手机移库列表
	 */
	@RequestMapping("/stockTransferList")
	public Object stockTransferList(AppPage page) {
		try {
			PageData pd = new PageData();
			List<PageData> varList = Lists.newArrayList();
			pd = this.getPageData();
			// 获取数据
			if (null != pd) {
				String orderby = "f.PreparationTime";
				if (Tools.notEmpty(pd.getString("orderby"))) {
					orderby = pd.getString("orderby");
				}
				String sort = "desc";
				if ("asc".equals(pd.getString("sort"))) {
					sort = "asc";
				}
				String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
				if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
				page.setPd(pd);
				
				
				PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
				PageInfo<PageData> pageInfo = new PageInfo<>(
						 MaterialTransferApplicationFormService.listTransfer(page));
				varList = pageInfo.getList();
				page.setTotalPage(pageInfo.getPages());
				page.setTotalResult(pageInfo.getTotal());
			}
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
		@RequestMapping(value="/findById")
		@ResponseBody
		public Object findById(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				pd = MaterialTransferApplicationFormService.findById(pd);	
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		/**
		 *转入转出明细
		 */
		@RequestMapping("/stockTransferListMx")
		public Object stockTransferListMx(AppPage page) {
			try {
				PageData pd = new PageData();
				List<PageData> varList = Lists.newArrayList();
				pd = this.getPageData();
				// 获取数据
				if (null != pd) {
					String orderby = "MaterialNum,cast(SourceRowNum as int)";
					if (Tools.notEmpty(pd.getString("orderby"))) {
						orderby = pd.getString("orderby");
					}
					String sort = "desc";
					if ("asc".equals(pd.getString("sort"))) {
						sort = "asc";
					}

					page.setPd(pd);
					PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
					PageInfo<PageData> pageInfo = new PageInfo<>(
							MaterialTransferApplicationDetailsService.stockTransferListMx(page));
					varList = pageInfo.getList();
					page.setTotalPage(pageInfo.getPages());
					page.setTotalResult(pageInfo.getTotal());
				}
				return AppResult.success(varList, page);
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		 /**完成
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/editStatus")
		@ResponseBody
		public Object editStatus(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
			
				pd = this.getPageData();
				pd.put("FOPERATOR",pd.getString("FOPERATOR"));					//获取当前登录人的中文名称
				pd.put("FIDENTIFIED",pd.getString("FIDENTIFIED"));					//获取当前登录人的中文名称
				
				eqm_point_inspectService.editStatusx(pd);
				return AppResult.success(pd, "完成成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		/**更新反馈内容
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/setFeedback")
		@ResponseBody
		public Object setFeedback() throws Exception{
			PageData pd = new PageData();
			PageData mxPd = new PageData();
			PageData tickPd = new PageData();
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			try {
	        if(null!=pd.getString("TEMBILL_EXECUTEMX_ID")&&!"".equals(pd.getString("TEMBILL_EXECUTEMX_ID"))) {
	        	mxPd.put("TEMBILL_EXECUTEMX_ID", pd.getString("TEMBILL_EXECUTEMX_ID"));
	        	mxPd=tembill_executemxService.findById(mxPd);//查询明细数据
	        	tickPd.put("TEMBILL_EXECUTE_ID", mxPd.getString("TEMBILL_EXECUTE_ID"));//主表id
	        	tickPd.put("FTICK_TIME", Tools.date2Str(new Date()));//反馈时间
	        	tickPd.put("FTICK_PERSON", pd.get("FTICK_PERSON"));//反馈人
	        	tickPd.put("FTICK_CAPTION", mxPd.getString("CAPTION"));//反馈标题
	        	tickPd.put("FTICK_MATTER", pd.getString("BEAR"));//反馈内容
	        	tickPd.put("TEMBILL_EXECUTETICK_ID", this.get32UUID());//主键id
	        	tembill_executetickService.save(tickPd);
			}
			pd.put("FLASTUPDATEPEOPLE",pd.get("FTICK_PERSON"));
			String FLASTUPDATETIME=Tools.date2Str(new Date());
			String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"',"+"FLASTUPDATEPEOPLE='"+pd.getString("FLASTUPDATEPEOPLE")+"',"+"FLASTUPDATETIME='"+FLASTUPDATETIME+"'"+" WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
			//String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"' WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
			pd.put("chatType", chatType);
			tembill_executemxService.setFeedback(pd);
			return AppResult.success(pd, "保存成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}	
		
		 /**明细去修改页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goEditMx")
		@ResponseBody
		public Object goEditMx(HttpServletResponse response) throws Exception{
			try {
				boolean preflag = false;
				boolean nextflag = false;
				PageData pd = new PageData();
				pd = this.getPageData();
				pd = MaterialTransferApplicationDetailsService.findById(pd);	//根据ID读取
				//拼凑二维码
				String matCode=pd.getString("MaterialNum");
				String type="L";
				String num=pd.get("IssuedQuantity").toString();//实发数量
				if("是".equals(pd.get("UNIQUE_CODE_WHETHER").toString())){
					BigDecimal FCode = new BigDecimal(0);
					PageData pdCode = new PageData();
					pdCode.put("Ftype", "唯一码");
					pdCode = MAKECODEService.getCode(pdCode);
					FCode = new BigDecimal(pdCode.get("FCode").toString());
					FCode = FCode.add(new BigDecimal(1));
					pdCode.put("FCode", FCode);
					MAKECODEService.editCode(pdCode);
					matCode=FCode.toString();
					type="W";
					num="1";
				}
				String propKey=pd.containsKey("MAT_AUXILIARYMX_CODE") && !"".equals(pd.getString("MAT_AUXILIARYMX_CODE"))?
						pd.getString("MAT_AUXILIARYMX_CODE"):"";
				String QRCodeInformation = type+",YL," + matCode + ",YL," + num + ",YL," + propKey;
				pd.put("QRCodeInformation", QRCodeInformation);
				//是否存在上一条、下一条
				if(null!=pd && pd.containsKey("RowNum")) {
					PageData prepd = new PageData();
					PageData nextpd = new PageData();
					int pre = Integer.parseInt(pd.get("RowNum").toString())-1;
					int next = Integer.parseInt(pd.get("RowNum").toString())+1;
					if(pre>0) {
						prepd.put("RowNum", pre);
						prepd.put("TransitionType", pd.getString("TransitionType"));
						prepd.put("MTA_ID", pd.getString("MTA_ID"));
						prepd = MaterialTransferApplicationDetailsService.findByRowNumAndStockList_ID(prepd);
						if(null!=prepd)preflag=true;
					}
					nextpd.put("RowNum", next);
					nextpd.put("TransitionType", pd.getString("TransitionType"));
					nextpd.put("MTA_ID", pd.getString("MTA_ID"));
					nextpd = MaterialTransferApplicationDetailsService.findByRowNumAndStockList_ID(nextpd);
					if(null!=nextpd)nextflag=true;
				}
				pd.put("hasPre", preflag);
				pd.put("hasNext", nextflag);
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
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
						pd.put("StockListScanningYK_ID", this.get32UUID());	//主键
						pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));	//
						pd.put("FCreateTime", time);	//
						pd.put("FTYPE", pd.getString("FTYPE"));	//
						MaterialTransferApplicationDetailsService.saveJL(pd);//保存记录
					}
				}
				PageData pdMx=MaterialTransferApplicationDetailsService.findById(pd);
		         pdMx.put("TargetWarehouse", pd.getString("InWarehouseID"));   
				 pdMx.put("TargetPosition", pd.getString("InPositionID"));    
				 pdMx.put("DeliveryWarehouse", pd.getString("OutWarehouseID"));    
				 //pdMx.put("", pd.getString("OutPositionID"));    
				 pdMx.put("IssuedQuantity", Double.parseDouble(pdMx.get("IssuedQuantity").toString())+Double.parseDouble(pd.get("qty").toString()));      
				MaterialTransferApplicationDetailsService.edit(pdMx);
				map.put("pd", pd);
				return AppResult.success(map, "操作成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		/**
		 * 出库单物料扫码接口
		 * 扫描物料二维码（包含类型码/唯一码标识、物料代码/物料唯一码、数量、辅助属性代码），
		 * 辅助属性是计划主工单，销售订单下面每个产品都对应的一个计划主工单，
		 * 出库的物料的辅助属性原有不变，只要能上查到对应的销售订单就让出库；
		 * 
		 * 处理逻辑：根据出库单明细中的销售订单编号查询对应销售订单，
		 * 遍历所有销售订单明细，然后根据销售订单明细id查询主工单编码，判断是否与辅助属性匹配，
		 * 如果存在能够匹配上的计划主工单编码，那么返回成功
		 */
		@RequestMapping(value="/scanOut")
		@ResponseBody
		public Object scanOut(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				String code = pd.getString("code");
				if (Tools.isEmpty(code)) {
					throw new RuntimeException("二维码无法识别");
				}
				int flag=0;
				String[] split = code.split(",YL,");
				PageData detailpd = MaterialTransferApplicationDetailsService.findById(pd);
				if(null != detailpd && detailpd.containsKey("PlannedWorkOrderNum") 
						&& !"".equals(detailpd.getString("PlannedWorkOrderNum"))){
					if(split.length>3 && split[3].equals(detailpd.getString("PlannedWorkOrderNum"))) {
						flag=1;
						
					}else {
						flag=1;
					}
					
					if(flag==0) {
						return AppResult.failed("辅助属性不匹配");
					} else {
						return AppResult.success(pd, "获取成功", "success");
					}
				} else {
					throw new RuntimeException("没有找到数据");
				}
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
				pd.put("StockListScanningYK_ID", this.get32UUID());	//主键
				pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));	//
				pd.put("FCreateTime", time);	//
				pd.put("FTYPE", pd.getString("FTYPE"));	//
				MaterialTransferApplicationDetailsService.saveJL(pd);//保存记录
				PageData pdMx=MaterialTransferApplicationDetailsService.findById(pd);
		         pdMx.put("TargetWarehouse", pd.getString("InWarehouseID"));   
				 pdMx.put("TargetPosition", pd.getString("InPositionID"));    
				 pdMx.put("DeliveryWarehouse", pd.getString("OutWarehouseID"));    
				 //pdMx.put("", pd.getString("OutPositionID"));    
				 pdMx.put("IssuedQuantity", Double.parseDouble(pdMx.get("IssuedQuantity").toString())+Double.parseDouble(pd.get("qty").toString()));      
				MaterialTransferApplicationDetailsService.edit(pdMx);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("pd", pd);
				return AppResult.success(map, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}

		 /**去修改页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/findByRowNum")
		@ResponseBody
		public Object findByRowNum(HttpServletResponse response) throws Exception{
			try {
				boolean preflag = false;
				boolean nextflag = false;
				PageData pd = new PageData();
				pd = this.getPageData();
				pd = MaterialTransferApplicationDetailsService.findByRowNumAndStockList_ID(pd);	//根据ID读取
				if(null!=pd && pd.containsKey("RowNum")) {
					PageData prepd = new PageData();
					PageData nextpd = new PageData();
					int pre = Integer.parseInt(pd.get("RowNum").toString())-1;
					int next = Integer.parseInt(pd.get("RowNum").toString())+1;
					if(pre>0) {
						prepd.put("RowNum", pre);
						prepd.put("MTA_ID", pd.getString("MTA_ID"));
						prepd.put("TransitionType", pd.getString("TransitionType"));
						prepd = MaterialTransferApplicationDetailsService.findByRowNumAndStockList_ID(prepd);
						if(null!=prepd)preflag=true;
					}
					nextpd.put("RowNum", next);
					nextpd.put("MTA_ID", pd.getString("MTA_ID"));
					nextpd.put("TransitionType", pd.getString("TransitionType"));
					nextpd = MaterialTransferApplicationDetailsService.findByRowNumAndStockList_ID(nextpd);
					if(null!=nextpd)nextflag=true;
				}
				//拼凑二维码
				String matCode=pd.getString("MaterialNum");
				String type="L";
				String num=pd.get("IssuedQuantity").toString();//实发数量
				if("是".equals(pd.get("UNIQUE_CODE_WHETHER").toString())){
					BigDecimal FCode = new BigDecimal(0);
					PageData pdCode = new PageData();
					pdCode.put("Ftype", "唯一码");
					pdCode = MAKECODEService.getCode(pdCode);
					FCode = new BigDecimal(pdCode.get("FCode").toString());
					FCode = FCode.add(new BigDecimal(1));
					pdCode.put("FCode", FCode);
					MAKECODEService.editCode(pdCode);
					matCode=FCode.toString();
					type="W";
					num="1";
				}
				String propKey=pd.containsKey("MAT_AUXILIARYMX_CODE") && !"".equals(pd.getString("MAT_AUXILIARYMX_CODE"))?
						pd.getString("MAT_AUXILIARYMX_CODE"):"";
				String QRCodeInformation = type+",YL," + matCode + ",YL," + num + ",YL," + propKey;
				pd.put("QRCodeInformation", QRCodeInformation);
				pd.put("hasPre", preflag);
				pd.put("hasNext", nextflag);
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
}

