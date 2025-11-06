package org.yy.controller.app;

import java.math.BigDecimal;
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
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;
import org.yy.service.system.UsersService;

/** 
 * 说明：出入库单明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appStockListDetail")
public class AppStockListDetailController extends BaseController {
	
	@Autowired
	private StockListDetailService StockListDetailService;//入库单明细
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
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
	@Autowired
	private MATSPLITService MATSPLITService;//物料拆分二级明细
	@Autowired
	private StaffService staffService;//员工
	@Autowired
	private MAKECODEService MAKECODEService;// 制码接口
	
	/**
	 * 验证是否是该出库单的明细
	 * @since 2021.2.6
	 * @param StockListDetail_ID、StockList_ID
	 * @return
	 * @throws Exception
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
			PageData detailpd = StockListDetailService.findById(pd);
			if(null != detailpd && detailpd.containsKey("StockList_ID") 
					&& !"".equals(detailpd.getString("StockList_ID"))){
					return AppResult.success(pd, "获取成功", "success");
			} else {
				throw new RuntimeException("没有找到数据");
			}
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
	/*public Object scanOut(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				throw new RuntimeException("二维码无法识别");
			}
			int flag=0;
			String[] split = code.split(",YL,");
			PageData detailpd = StockListDetailService.findById(pd);
			if(null != detailpd && detailpd.containsKey("SourceOrderNum") 
					&& !"".equals(detailpd.getString("SourceOrderNum"))){
				detailpd.put("OrderNum", detailpd.getString("SourceOrderNum"));
				PageData salesOrder = salesorderService.findByOrderNum(detailpd);
				if(null != salesOrder && salesOrder.containsKey("SalesOrder_ID")) {
					PageData p = new PageData();
					p.put("SalesOrderID", salesOrder.getString("SalesOrder_ID"));
					List<PageData> list = salesorderdetailService.listAll(p);
					if(!list.isEmpty()) {
						for(PageData temp : list) {
							temp.put("SalesOrderDetailID", temp.getString("SalesOrderDetail_ID"));
							PageData masterPlanOrder = PlanningWorkOrderMasterService.findBySalesOrderDetailID(temp);
							if(null != masterPlanOrder && masterPlanOrder.containsKey("WorkOrderNum")
									&& split[3].equals(masterPlanOrder.getString("WorkOrderNum"))) {
								flag=1;
								break;
							}
						}
					}
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
	}*/
	
	/**扫码验证是否是该单据的物料
	 * 扫描物料二维码（包含类型码/唯一码标识、物料代码/物料唯一码、辅助属性代码），根据单据明细id、单据主表id，
	 * 检验该明细的辅助属性是否匹配，库存中是否存在该唯一码物料
	 * @param pd.StockListDetail_ID
	 * @param pd.StockList_ID
	 * @param pd.code
	 * @throws Exception
	 */
	@RequestMapping(value="/scanMaterial")
	@ResponseBody
	public Object scanMaterial(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				throw new RuntimeException("二维码无法识别");
			}
			String[] split = code.split(",YL,");
			//查询明细是否在该单据中
			PageData detailpd = StockListDetailService.findById(pd);
			if(null != detailpd && detailpd.containsKey("StockList_ID") 
					&& pd.getString("StockList_ID").equals(detailpd.getString("StockList_ID"))){
				String time=Tools.date2Str(new Date());
				//在单据明细中是否匹配该辅助属性
				if(split[3].equals(detailpd.getString("MAT_AUXILIARYMX_CODE"))){
					//如果是类型吗直接插入库存，如果是唯一码校验库存中是否存在
					if("W".equals(split[0])) {
						//查询库存中是否已存在，如果存在返回错误
						pd.put("OneThingCode", split[1]);
						List<PageData> list = StockService.listAll(pd);
						if(!list.isEmpty() && list.size()>0)
						throw new RuntimeException("二维码已存在");
					}
				}else {
					throw new RuntimeException("辅助属性不匹配");
				}
			}else {
				throw new RuntimeException("没有找到数据");
			}
			return AppResult.success(pd, "获取成功", "success");
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
			pd = StockListDetailService.findByRowNumAndStockList_ID(pd);	//根据ID读取
			if(null!=pd && pd.containsKey("RowNum")) {
				PageData prepd = new PageData();
				PageData nextpd = new PageData();
				int pre = Integer.parseInt(pd.get("RowNum").toString())-1;
				int next = Integer.parseInt(pd.get("RowNum").toString())+1;
				if(pre>0) {
					prepd.put("RowNum", pre);
					prepd.put("StockList_ID", pd.getString("StockList_ID"));
					prepd = StockListDetailService.findByRowNumAndStockList_ID(prepd);
					if(null!=prepd)preflag=true;
				}
				nextpd.put("RowNum", next);
				nextpd.put("StockList_ID", pd.getString("StockList_ID"));
				nextpd = StockListDetailService.findByRowNumAndStockList_ID(nextpd);
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
	
	/**入库单明细列表
	 * @author 宋
	 * @date 2020-12-25
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/getList")
	@ResponseBody
	public Object listIn(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "RowNum";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(StockListDetailService.appListIn(page));
			varList = pageInfo.getList();
			for(int i=0;i<varList.size();i++) {
				PageData pdKC=new PageData();
				pdKC.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKC.put("ItemID", varList.get(i).getString("FCode"));
				pdKC.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKC);//即时库存
				if(pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				} else {
					varList.get(i).put("stockSum", "0");
				}
			}
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
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
		try {
			boolean preflag = false;
			boolean nextflag = false;
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = StockListDetailService.findById(pd);	//根据ID读取
			//拼凑二维码
			String matCode=pd.getString("MAT_CODE");
			String type="L";
			String num=pd.containsKey("MaterialQuantity") && null!=pd.get("MaterialQuantity")?pd.get("MaterialQuantity").toString():"0";
			if(pd.containsKey("UNIQUE_CODE_WHETHER") && null!=pd.get("UNIQUE_CODE_WHETHER") && "是".equals(pd.get("UNIQUE_CODE_WHETHER").toString())){
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
					prepd.put("StockList_ID", pd.getString("StockList_ID"));
					prepd = StockListDetailService.findByRowNumAndStockList_ID(prepd);
					if(null!=prepd)preflag=true;
				}
				nextpd.put("RowNum", next);
				nextpd.put("StockList_ID", pd.getString("StockList_ID"));
				nextpd = StockListDetailService.findByRowNumAndStockList_ID(nextpd);
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

}
