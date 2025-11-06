package org.yy.controller.mm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.CallMaterialDetailsFLService;
import org.yy.service.mm.CallMaterialDetailsService;
import org.yy.service.mm.CallMaterialFLService;
import org.yy.service.mm.CallMaterialOperateService;
import org.yy.service.mm.CallMaterialService;
import org.yy.service.mm.StockService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：叫料申请 作者：YuanYes QQ356703572 时间：2021-03-25 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/CallMaterial")
public class CallMaterialController extends BaseController {

	@Autowired
	private CallMaterialService CallMaterialService;
	@Autowired
	private CallMaterialOperateService CallMaterialOperateService;
	@Autowired
	private CallMaterialDetailsService CallMaterialDetailsService;
	@Autowired
	private StockService StockService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private CallMaterialFLService callmaterialflService;
	@Autowired
	private CallMaterialDetailsFLService callmaterialdetailsflService;
	@Autowired
	private CodingRulesService CodingRulesService;
	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrderNumStrs="";
		pd.put("CallMaterial_ID", this.get32UUID()); // 主键
		pd.put("MakePersonID", Jurisdiction.getName());
		pd.put("MakeTime", Tools.date2Str(new Date()));
		pd.put("FState", "新增");
		CallMaterialService.save(pd);

		// 加入操作记录
		PageData operate = new PageData();
		operate.put("CallMaterialOperate_ID", this.get32UUID());
		operate.put("CallMaterial_ID", pd.getString("CallMaterial_ID"));
		operate.put("OperatePerson", Jurisdiction.getName());
		operate.put("OperateTime", Tools.date2Str(new Date()));
		operate.put("FState", "新增");
		CallMaterialOperateService.save(operate);

		String PlanningWorkOrderNumStr = pd.getString("PlanningWorkOrderNum");
		if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
			List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStr.split(","));
			List<PageData> allDetailList = Lists.newArrayList();
			for (String num : PlanningWorkOrderNumList) {
				String mat_id = "";
				PageData pdData = new PageData();
				pdData.put("WorkOrderNum", num);
				List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					mat_id = listAll.get(0).getString("OutputMaterialID");
					PageData matParam = new PageData();
					matParam.put("MAT_BASIC_ID", mat_id);
					PageData MAT_INFO = mat_basicService.findById(matParam);
					if (null != MAT_INFO) {
						PageData cadparam = new PageData();
						cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
						List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
						if (CollectionUtil.isNotEmpty(cadList)) {
							PageData bomParam = new PageData();
							bomParam.put("FFTYPE","BOM");
							
							bomParam.put("Cabinet_Assembly_Detail_ID",
									cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
							List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

							String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
							String MAT_AUXILIARYMX_ID = "";
							PageData pg1 = new PageData();
							pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
							pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
							List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
									.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
							if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
								MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
										.getString("MAT_AUXILIARYMX_ID");
							}

							for (PageData bom : cadBomList) {
								PageData cmData = new PageData();
								cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
								cmData.put("CallMaterial_ID", pd.getString("CallMaterial_ID"));
								cmData.put("TType", "计划生成");
								cmData.put("OperatePersion", Jurisdiction.getName());
								cmData.put("OperateTime", Tools.date2Str(new Date()));

								cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
								cmData.put("MaterialName", bom.getString("MAT_NAME"));

								cmData.put("Specification", bom.getString("MAT_SPECS"));
								cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
								cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());
								
								cmData.put("MaterialCode", bom.getString("MAT_CODE"));
								cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));

								cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

								allDetailList.add(cmData);

							}
						}
					}
				}
			}


			Map<String, PageData> result2 = new HashMap<String, PageData>();
			for(PageData cmData : allDetailList){
				String Material_ID = cmData.get("Material_ID").toString();
				BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
				if(result2.containsKey(Material_ID)){
					BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
					value = value .add( temp );
					result2.get(Material_ID).put("DemandNum", value);
					continue;
				}
				result2.put(Material_ID, cmData);
			}

			for (PageData cmDetailData : result2.values()) {
				CallMaterialDetailsService.save(cmDetailData);
			}
		}
		

		map.put("result", errInfo);
		return map;
	}

	/**
	 * 重新选择计划的时候重新生成详情
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/reloadDetail")
	@ResponseBody
	public Object reloadDetail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("TType", "计划生成");
		CallMaterialDetailsService.deleteByCondition(pd);

		String PlanningWorkOrderNumStr = pd.getString("PlanningWorkOrderNum");
		if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
			List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStr.split(","));
			List<PageData> allDetailList = Lists.newArrayList();
			for (String num : PlanningWorkOrderNumList) {
				String mat_id = "";
				PageData pdData = new PageData();
				pdData.put("WorkOrderNum", num);
				List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					mat_id = listAll.get(0).getString("OutputMaterialID");
					PageData matParam = new PageData();
					matParam.put("MAT_BASIC_ID", mat_id);
					PageData MAT_INFO = mat_basicService.findById(matParam);
					if (null != MAT_INFO) {
						PageData cadparam = new PageData();
						cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
						List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
						if (CollectionUtil.isNotEmpty(cadList)) {
							PageData bomParam = new PageData();
							bomParam.put("Cabinet_Assembly_Detail_ID",
									cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
							List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

							String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
							String MAT_AUXILIARYMX_ID = "";
							PageData pg1 = new PageData();
							pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
							pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
							List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
									.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
							if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
								MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
										.getString("MAT_AUXILIARYMX_ID");
							}

							for (PageData bom : cadBomList) {
								PageData cmData = new PageData();
								cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
								cmData.put("CallMaterial_ID", pd.getString("CallMaterial_ID"));
								cmData.put("TType", "计划生成");
								cmData.put("OperatePersion", Jurisdiction.getName());
								cmData.put("OperateTime", Tools.date2Str(new Date()));

								cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
								cmData.put("MaterialName", bom.getString("MAT_NAME"));

								cmData.put("Specification", bom.getString("MAT_SPECS"));
								cmData.put("DemandNum", bom.getString("BOM_COUNT"));
								cmData.put("QuantityCount", bom.getString("BOM_COUNT"));
								cmData.put("MaterialCode", bom.getString("MAT_CODE"));
								cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));
								
								cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

								allDetailList.add(cmData);

							}
						}
					}
				}
			}


			Map<String, PageData> result2 = new HashMap<String, PageData>();
			for(PageData cmData : allDetailList){
				String Material_ID = cmData.get("Material_ID").toString();
				BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
				if(result2.containsKey(Material_ID)){
					BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
					value = value .add( temp );
					result2.get(Material_ID).put("DemandNum", value);
					continue;
				}
				result2.put(Material_ID, cmData);
			}

			for (PageData cmDetailData : result2.values()) {
				CallMaterialDetailsService.save(cmDetailData);
			}
		}
			

		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		String pk = pd.getString("CallMaterial_ID");

		PageData pData = new PageData();
		pData.put("CallMaterial_ID", pk);

		List<PageData> listAll = CallMaterialDetailsService.listAll(pData);
		for (PageData pageData : listAll) {
			CallMaterialDetailsService.delete(pageData);
		}

		CallMaterialService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		CallMaterialService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表(车间)
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listchejian")
	@ResponseBody
	public Object listchejian(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = CallMaterialService.listchejian(page); // 列出CallMaterial列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表(库房)
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listkufang")
	@ResponseBody
	public Object listkufang(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = CallMaterialService.listkufang(page); // 列出CallMaterial列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 项目看板-库房待执行
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXMkufang")
	@ResponseBody
	public Object listXMkufang(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("CREATORMAN", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = CallMaterialService.listXMkufang(page); // 列出CallMaterial列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 多次叫料
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/addS")
	@ResponseBody
	public Object addS() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdSave = new PageData();
		pd = this.getPageData();
		pdSave=CallMaterialService.findById(pd);
		pdSave.put("CallMaterial_ID", this.get32UUID()); // 主键
		pdSave.put("MakePersonID", Jurisdiction.getName());
		pdSave.put("MakeTime", Tools.date2Str(new Date()));
		pdSave.put("FState", "新增");
		pdSave.put("Associated_ID", pd.getString("CallMaterial_ID"));
		pdSave.put("DocumentNum", pd.getString("DocumentNum"));
		CallMaterialService.save(pdSave);
		List<PageData> varList = CallMaterialService.listAllMat(pd); 
		for(PageData cmData :varList) {
			cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
			cmData.put("CallMaterial_ID", pdSave.getString("CallMaterial_ID"));
			cmData.put("DemandNum", cmData.get("DemandNumx").toString());
			cmData.put("QuantityCount", cmData.get("QuantityCountx").toString());
			cmData.put("TType", "计划生成");
			cmData.put("OperatePersion", Jurisdiction.getName());
			cmData.put("OperateTime", Tools.date2Str(new Date()));
			cmData.put("DeliveryWarehouse", "");
			cmData.put("DeliveryPosition", "");
			cmData.put("TargetWarehouse", "");
			cmData.put("TargetPosition", "");
			cmData.put("DeliveryWarehouseID", "");
			cmData.put("DeliveryPositionID", "");
			cmData.put("TargetWarehouseID", "");
			cmData.put("TargetPositionID", "");
			CallMaterialDetailsService.save(cmData);
		}
		// 加入操作记录
		PageData operate = new PageData();
		operate.put("CallMaterialOperate_ID", this.get32UUID());
		operate.put("CallMaterial_ID", pdSave.getString("CallMaterial_ID"));
		operate.put("OperatePerson", Jurisdiction.getName());
		operate.put("OperateTime", Tools.date2Str(new Date()));
		operate.put("FState", "新增");
		CallMaterialOperateService.save(operate);
		map.put("result", errInfo); // 返回结果
		return map;
	}
	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = CallMaterialService.findById(pd); // 根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			CallMaterialService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("备注2"); // 1
		titles.add("备注3"); // 2
		titles.add("备注4"); // 3
		titles.add("备注5"); // 4
		titles.add("备注6"); // 5
		titles.add("备注7"); // 6
		titles.add("备注8"); // 7
		titles.add("备注9"); // 8
		titles.add("备注10"); // 9
		titles.add("备注11"); // 10
		dataMap.put("titles", titles);
		List<PageData> varOList = CallMaterialService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SerialNum")); // 1
			vpd.put("var2", varOList.get(i).getString("DocumentNum")); // 2
			vpd.put("var3", varOList.get(i).getString("NeedUseTime")); // 3
			vpd.put("var4", varOList.get(i).getString("SaleOrderNum")); // 4
			vpd.put("var5", varOList.get(i).getString("PlanningWorkOrderNum")); // 5
			vpd.put("var6", varOList.get(i).getString("TaskNum")); // 6
			vpd.put("var7", varOList.get(i).getString("WokNum")); // 7
			vpd.put("var8", varOList.get(i).getString("MakePersonID")); // 8
			vpd.put("var9", varOList.get(i).getString("MakeTime")); // 9
			vpd.put("var10", varOList.get(i).getString("FState")); // 10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 修改状态
	 * v2 管悦 20210825 库房分料单通过物料ID更新分料组任务的接收仓库
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
			findById.put("FOutMan", Jurisdiction.getName());
			findById.put("FOutTime", Tools.date2Str(new Date()));
			// 查看详情，循环获取 库存的数量是否满足 不满足不让发 直接返回 满足 则出库
			// 从发出仓库扣减库存
			for (PageData detail : listAll) {
				if (StringUtil.isEmpty(detail.get("DemandNum").toString())
						|| StringUtil.isEmpty(detail.get("QuantityCount").toString())) {

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
				/*if (StringUtil.isEmpty(detail.getString("TargetWarehouse"))
						|| StringUtil.isEmpty(detail.getString("TargetPosition"))) {
					map.put("result", "exception");
					map.put("msg", "有单据没有选择接收仓库或仓位");
					return map;
				}*/
			}
			for (PageData detail : listAll) {
				PageData outParam = new PageData();
				outParam.put("Stock_ID", this.get32UUID());

				outParam.put("WarehouseID", detail.getString("DeliveryWarehouseID"));// 仓库id
				outParam.put("PositionID", detail.getString("DeliveryPositionID"));// 库位id
				outParam.put("ItemID", detail.getString("Material_ID"));// 物料id

				PageData stockpd = StockService.getSum(outParam);
				if (null != stockpd) {
					Double stockSumDouble = Double.valueOf(String.valueOf(stockpd.get("stockSum")));
					if (null != stockSumDouble && 0 != stockSumDouble) {
						Double QuantityCount = Double.valueOf(String.valueOf(detail.get("QuantityCount")));
						if ((stockSumDouble - QuantityCount) < 0) {
							detail.put("QuantityCount", "0");
							CallMaterialDetailsService.edit(detail);
						}
					} else {
						detail.put("QuantityCount", "0");
						CallMaterialDetailsService.edit(detail);
					}
				} else {
					detail.put("QuantityCount", "0");
					CallMaterialDetailsService.edit(detail);
				}
				if (StringUtil.isEmpty(detail.getString("TargetWarehouse"))
						|| StringUtil.isEmpty(detail.getString("TargetPosition"))) {
					callmaterialdetailsflService.editTargetWarehouse(detail);//通过物料ID更新接收仓库
				}
				// 去掉了 辅助属性 限制 目前是 不过滤辅助属性的 是这个料就让出
				outParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
				outParam.put("SpecificationDesc", detail.getString("Specification"));// 规格
				outParam.put("num", detail.get("QuantityCount").toString());// 实际数量
				outParam.put("FBatch", detail.getString("BatchNum"));// 批号
				outParam.put("FUnit", detail.getString("UNIT_INFO_ID"));// 单位
				outParam.put("ProductionBatch", detail.getString("BatchNum"));// 生产批号

				outParam.put("UseCount", 0);// 使用数量
				outParam.put("UseIf", "NO");// 是否占用
				outParam.put("FLevel", 1);// 等级

				outParam.put("RunningState", "在用");// 运行状态
				outParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
				outParam.put("FCreateTime", Jurisdiction.getUserId());// 创建人
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
			findById.put("FInMan", Jurisdiction.getName());
			findById.put("FInTime", Tools.date2Str(new Date()));
			for (PageData detail : listAll) {
				if (StringUtil.isEmpty(detail.get("QuantityCount").toString())) {
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
				String UnitID = mat_basicService.getUnitID(detail);
				PageData inParam = new PageData();
				inParam.put("Stock_ID", this.get32UUID());

				inParam.put("WarehouseID", detail.getString("TargetWarehouseID"));// 仓库id
				inParam.put("PositionID", detail.getString("TargetPositionID"));// 库位id
				inParam.put("ItemID", detail.getString("Material_ID"));// 物料id
				inParam.put("StorageStatus", "车间");// 存储状态(工厂,车间)
				inParam.put("QRCode", detail.getString("MaterialCode"));// 物料二维码
				inParam.put("MaterialSPropKey", detail.getString("MAT_AUXILIARYMX_ID"));// 物料辅助属性值
				inParam.put("SpecificationDesc", detail.getString("Specification"));// 规格
				inParam.put("ActualCount", detail.get("QuantityCount").toString());// 实际数量
				inParam.put("FBatch", detail.getString("BatchNum"));// 批号
				inParam.put("ProductionBatch", detail.getString("BatchNum"));// 生产批号
				inParam.put("FUnit", UnitID);// 单位
				inParam.put("UseCount", 0);// 使用数量
				inParam.put("UseIf", "NO");// 是否占用
				inParam.put("FLevel", 1);// 等级

				inParam.put("RunningState", "在用");// 运行状态
				inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
				inParam.put("FCreateTime", Jurisdiction.getUserId());// 创建人
				inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
				inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期

				inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
				inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
				inParam.put("FStatus", "入库");// 状态

				inParam.put("ProjectNum", findById.getString("PlanningWorkOrderNum"));// 计划工单编号

				StockService.inStock(inParam);
			}
		}
		if ("叫料".equals(FState)) {
			findById.put("FCallMan", Jurisdiction.getName());
			findById.put("FCallTime", Tools.date2Str(new Date()));
			
			
			//插入分料任务
			String PlanningWorkOrderNumStr = findById.getString("PlanningWorkOrderNum");
			if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
				List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStr.split(","));
				for (String num : PlanningWorkOrderNumList) {
					List<PageData> allDetailList = Lists.newArrayList();

					//创建叫料申请
					PageData pdC = new PageData();
					pdC.put("CallMaterialFL_ID", this.get32UUID()); // 主键
					pdC.put("CallMaterial_ID", pd.getString("CallMaterial_ID")); 
					pdC.put("MakeTime", Tools.date2Str(new Date()));
					pdC.put("DocumentNum",(String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
					pdC.put("FState", "待分料");
					pdC.put("FCallTime",Tools.date2Str(new Date()));
					pdC.put("FCallMan",Jurisdiction.getName());
					pdC.put("MakeTime",Tools.date2Str(new Date()));
					pdC.put("PlanningWorkOrderNum", num);
					pdC.put("NeedUseTime",Tools.date2Str(new Date()));
					pdC.put("FIsYL", "是");
					callmaterialflService.save(pdC);

					// 加入操作记录
					PageData operatex = new PageData();
					operatex.put("CallMaterialOperate_ID", this.get32UUID());
					operatex.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
					operatex.put("OperatePerson", Jurisdiction.getName());
					operatex.put("OperateTime", Tools.date2Str(new Date()));
					operatex.put("FState", "叫料任务");
					CallMaterialOperateService.save(operatex);
					String mat_id = "";
					PageData pdData = new PageData();
					pdData.put("WorkOrderNum", num);
					List<PageData> listAllX = PlanningWorkOrderService.listAll(pdData);
					if (CollectionUtil.isNotEmpty(listAllX)) {
						mat_id = listAllX.get(0).getString("OutputMaterialID");
						PageData matParam = new PageData();
						matParam.put("MAT_BASIC_ID", mat_id);
						PageData MAT_INFO = mat_basicService.findById(matParam);
						if (null != MAT_INFO) {
							PageData cadparam = new PageData();
							cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
							List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
							if (CollectionUtil.isNotEmpty(cadList)) {
								PageData bomParam = new PageData();
								bomParam.put("FFTYPE","BOM");
								
								bomParam.put("Cabinet_Assembly_Detail_ID",
										cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
								List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

								String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
								String MAT_AUXILIARYMX_ID = "";
								PageData pg1 = new PageData();
								pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
								List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
										.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
								if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
									MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
											.getString("MAT_AUXILIARYMX_ID");
								}

								for (PageData bom : cadBomList) {
									PageData cmData = new PageData();
									cmData.put("CallMaterialDetailsFL_ID", this.get32UUID()); // 主键
									cmData.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
									cmData.put("CallMaterialFL_ID", pdC.getString("CallMaterialFL_ID"));
									cmData.put("TType", "计划生成");
									cmData.put("OperatePersion", Jurisdiction.getName());
									cmData.put("OperateTime", Tools.date2Str(new Date()));

									cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
									cmData.put("MaterialName", bom.getString("MAT_NAME"));

									cmData.put("Specification", bom.getString("MAT_SPECS"));
									cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
									cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());
									
									cmData.put("MaterialCode", bom.getString("MAT_CODE"));
									cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));
									
									cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
									cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

									allDetailList.add(cmData);

								}
								Map<String, PageData> result2 = new HashMap<String, PageData>();
								for(PageData cmData : allDetailList){
									String Material_ID = cmData.get("Material_ID").toString();
									BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
									if(result2.containsKey(Material_ID)){
										BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
										value = value .add( temp );
										result2.get(Material_ID).put("DemandNum", value);
										continue;
									}
									result2.put(Material_ID, cmData);
								}

								for (PageData cmDetailData : result2.values()) {
									callmaterialdetailsflService.save(cmDetailData);
								}
							}
						}
					}
				}
			}
		}
		if ("仓库接收".equals(FState)) {
			findById.put("FRecieveMan", Jurisdiction.getName());
			findById.put("FRecieveTime", Tools.date2Str(new Date()));
		}
		// 加入操作记录
		PageData operate = new PageData();
		operate.put("CallMaterialOperate_ID", this.get32UUID());
		operate.put("CallMaterial_ID", pd.getString("CallMaterial_ID"));
		operate.put("OperatePerson", Jurisdiction.getName());
		operate.put("OperateTime", Tools.date2Str(new Date()));
		operate.put("FState", FState);
		CallMaterialOperateService.save(operate);
		CallMaterialService.edit(findById);
		return map;
	}

}
