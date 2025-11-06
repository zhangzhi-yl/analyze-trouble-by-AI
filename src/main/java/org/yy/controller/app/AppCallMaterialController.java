package org.yy.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.mm.CallMaterialDetailsService;
import org.yy.service.mm.CallMaterialOperateService;
import org.yy.service.mm.CallMaterialService;
import org.yy.service.mm.StockService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

/**
 * 手机端叫料接口
 * 
 * @author CCG
 *
 */
@RestController
@RequestMapping("/appCallMaterial")
public class AppCallMaterialController extends BaseController {
	@Autowired
	private CallMaterialService CallMaterialService;
	@Autowired
	private CallMaterialOperateService CallMaterialOperateService;
	@Autowired
	private CallMaterialDetailsService CallMaterialDetailsService;
	@Autowired
	private StockService StockService;
	/**
	 * 列表(车间)
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listchejian")
	@ResponseBody
	public Object listchejian(AppPage page) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = Lists.newArrayList();
		PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),"f.FState asc, f.MakeTime desc");
		PageInfo<PageData> pageInfo = new PageInfo<>(CallMaterialService.listchejianApp(page)); // 列出CallMaterial列表
		varList = pageInfo.getList();
		return AppResult.success(varList, page);
	}
	/**
	 * 列表(库房)
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listkufang")
	@ResponseBody
	public Object listkufang(AppPage page) throws Exception {	
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = Lists.newArrayList();
		PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),"f.FState asc, f.MakeTime desc");
		PageInfo<PageData> pageInfo = new PageInfo<>(CallMaterialService.listkufangApp(page)); // 列出CallMaterial列表		
		varList = pageInfo.getList();
		return AppResult.success(varList, page);
	}
	
	/**
	 * 详情列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/detailListByCallMaterialID")
	@ResponseBody
	public Object detailListByCallMaterialID(AppPage page) throws Exception {	
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = Lists.newArrayList();
		PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),"f.IsCheck asc,f.OperateTime desc");
		PageInfo<PageData> pageInfo = new PageInfo<>(CallMaterialDetailsService.detailListByCallMaterialID(page)); // 列出CallMaterial列表		
		varList = pageInfo.getList();
		return AppResult.success(varList, page);
	}
	
	/**
	 * 修改状态
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeState")
	@ResponseBody
	public Object changeState() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		map.put("result", errInfo);
		PageData pd = new PageData();
		pd = this.getPageData();
		String pk = pd.getString("CallMaterial_ID");
		String FState = pd.getString("FState");
		PageData pData = new PageData();
		pData.put("CallMaterial_ID", pk);
		PageData findById = CallMaterialService.findById(pData);
		List<PageData> listAll = CallMaterialDetailsService.listAll(pData);
		if (CollectionUtil.isEmpty(listAll)) {
			map.put("result", "exception");
			map.put("msg", "请添加详情");
			return map;
		}
		findById.put("FState", FState);
		
	

		//
		if ("仓库出库".equals(FState)) {
			// 查看详情，循环获取 库存的数量是否满足 不满足不让发 直接返回 满足 则出库
			// 从发出仓库扣减库存
			for (PageData detail : listAll) {
				if (StringUtil.isEmpty(detail.getString("DemandNum"))
						|| StringUtil.isEmpty(detail.getString("QuantityCount"))) {
					
					map.put("result", "exception");
					map.put("msg", "有单据数量内容为空");
					return map;
				
				}
				if (StringUtil.isEmpty(detail.getString("Material_ID"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择物料");
					return map;
				}
				if (StringUtil.isEmpty(detail.getString("DeliveryWarehouse"))
						|| StringUtil.isEmpty(detail.getString("DeliveryPosition"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择发出仓库或仓位");
					return map;
				}
				if (StringUtil.isEmpty(detail.getString("TargetWarehouse"))
						|| StringUtil.isEmpty(detail.getString("TargetPosition"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择接收仓库或仓位");
					return map;
				}			
			}
			for (PageData detail : listAll) {
				PageData outParam = new PageData();
				outParam.put("Stock_ID", this.get32UUID());
				
				outParam.put("WarehouseID", detail.getString("DeliveryWarehouseID"));// 仓库id
				outParam.put("PositionID", detail.getString("DeliveryPositionID"));// 库位id
				outParam.put("ItemID", detail.getString("Material_ID"));// 物料id
				
				
				PageData stockpd = StockService.getSum(outParam);
				if(null!=stockpd){
					Double stockSumDouble = Double.valueOf(String.valueOf(stockpd.get("stockSum")));
					if(null!=stockSumDouble && 0!=stockSumDouble){
						Double QuantityCount = 	Double.valueOf(String.valueOf(detail.get("QuantityCount")));
						if((stockSumDouble -QuantityCount) <0){
							detail.put("QuantityCount","0");
							CallMaterialDetailsService.edit(detail);
						}
					}else{
						detail.put("QuantityCount","0");
						CallMaterialDetailsService.edit(detail);
					}
				}else{
					detail.put("QuantityCount","0");
					CallMaterialDetailsService.edit(detail);
				}
				
				// 去掉了 辅助属性 限制 目前是 不过滤辅助属性的  是这个料就让出
				outParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
				outParam.put("SpecificationDesc", detail.getString("Specification"));// 规格
				outParam.put("num", detail.getString("QuantityCount"));// 实际数量
				outParam.put("FBatch", detail.getString("BatchNum"));// 批号
				outParam.put("FUnit", detail.getString("UNIT_INFO_ID"));// 单位
				outParam.put("ProductionBatch", detail.getString("BatchNum"));// 生产批号
	
				outParam.put("UseCount", 0);// 使用数量
				outParam.put("UseIf", "NO");// 是否占用
				outParam.put("FLevel", 1);// 等级
	
				outParam.put("RunningState", "在用");// 运行状态
				outParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
				outParam.put("FCreateTime", pd.getString("STAFF_ID"));// 创建人
				outParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
				outParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期
	
				outParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
				outParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
				outParam.put("FStatus", "入库");// 状态
	
				outParam.put("ProjectNum", findById.getString("PlanningWorkOrderNum"));// 计划工单编号

				StockService.outStock(outParam);
			}
		}

		if ("车间入库".equals(FState)) {
			// 直接入到车间库
			// 加库存

			for (PageData detail : listAll) {
				if (StringUtil.isEmpty(detail.getString("DemandNum"))
						|| StringUtil.isEmpty(detail.getString("QuantityCount"))) {
					map.put("result", "exception");
					map.put("msg", "有单据数量内容为空");
					return map;
				}
				if (StringUtil.isEmpty(detail.getString("Material_ID"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择物料");
					return map;
				}
				if (StringUtil.isEmpty(detail.getString("TargetWarehouse"))
						|| StringUtil.isEmpty(detail.getString("TargetPosition"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择接收仓库或仓位");
					return map;
				}
			}
			for (PageData detail : listAll) {
				PageData inParam = new PageData();
				inParam.put("Stock_ID", this.get32UUID());
				
				inParam.put("WarehouseID", detail.getString("TargetWarehouseID"));// 仓库id
				inParam.put("PositionID", detail.getString("TargetPositionID"));// 库位id
				inParam.put("ItemID", detail.getString("Material_ID"));// 物料id
				inParam.put("StorageStatus", "车间");// 存储状态(工厂,车间)
				inParam.put("QRCode", detail.getString("MaterialCode"));// 物料二维码
				inParam.put("MaterialSPropKey", detail.getString("MAT_AUXILIARYMX_ID"));// 物料辅助属性值
				inParam.put("SpecificationDesc", detail.getString("Specification"));// 规格
				inParam.put("ActualCount", detail.getString("QuantityCount"));// 实际数量
				inParam.put("FBatch", detail.getString("BatchNum"));// 批号
				inParam.put("FUnit", detail.getString("UNIT_INFO_ID"));// 单位
				inParam.put("ProductionBatch", detail.getString("BatchNum"));// 生产批号
	
				inParam.put("UseCount", 0);// 使用数量
				inParam.put("UseIf", "NO");// 是否占用
				inParam.put("FLevel", 1);// 等级
	
				inParam.put("RunningState", "在用");// 运行状态
				inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
				inParam.put("FCreateTime", pd.getString("STAFF_ID"));// 创建人
				inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
				inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期
	
				inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
				inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
				inParam.put("FStatus", "入库");// 状态
	
				inParam.put("ProjectNum", findById.getString("PlanningWorkOrderNum"));// 计划工单编号

				StockService.inStock(inParam);
			}
		}

		// 加入操作记录
		PageData operate = new PageData();
		operate.put("CallMaterialOperate_ID", this.get32UUID());
		operate.put("CallMaterial_ID", pd.getString("CallMaterial_ID"));
		operate.put("OperatePerson", pd.getString("STAFF_NAME"));
		operate.put("OperateTime", Tools.date2Str(new Date()));
		operate.put("FState", FState);
		CallMaterialOperateService.save(operate);
		CallMaterialService.edit(findById);
		return map;
	}
	
	/**修改物料详情
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/DetailEdit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		CallMaterialDetailsService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**车间确认详情物料
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/checkDetail")
	@ResponseBody
	public Object checkDetail() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData findById = CallMaterialDetailsService.findById(pd);
		findById.put("IsCheck", "已确认");		
		CallMaterialDetailsService.edit(findById);
		map.put("result", errInfo);
		return map;
	}
	
}
