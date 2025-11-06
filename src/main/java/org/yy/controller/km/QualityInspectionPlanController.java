package org.yy.controller.km;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.KMCustomerService;
import org.yy.service.km.QualityInspectionItemsService;
import org.yy.service.km.QualityInspectionPlanApplicableMaterialsService;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.km.QualityInspectionPlanService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.system.DictionariesService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.mchange.lang.ObjectUtils;

/**
 * 说明：质检方案 作者：YuanYes QQ356703572 时间：2020-11-10 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/QualityInspectionPlan")
public class QualityInspectionPlanController extends BaseController {

	@Autowired
	private QualityInspectionPlanService QualityInspectionPlanService;
	@Autowired
	QualityInspectionPlanApplicableMaterialsService qipamService;
	@Autowired
	private QualityInspectionPlanInspectionDetailsService qipiDetailService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private QualityInspectionItemsService qualityinspectionitemsService;
	@Autowired
	private KMCustomerService CustomerService;
	@Autowired
	private DictionariesService dictionariesService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("QualityInspectionPlan:add")
	@ResponseBody
	public Object add(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("QualityInspectionPlan_ID", this.get32UUID()); // 主键
		if (null != file && !file.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			PageData pd1 = new PageData();
			String ffile = DateUtil.getDays();
			String FFILENAME = "";
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			pd1.put("FUrl", FPFFILEPATH); // 附件路径
			pd1.put("FName", FFILENAME); // 附件名称
			pd1.put("DataSources", "质检方案"); // 数据源
			pd1.put("AssociationIDTable", "KM_QualityInspectionPlan"); // 数据源表
			pd1.put("AssociationID", pd.getString("QualityInspectionPlan_ID")); // 数据源ID
			pd1.put("FExplanation", "附件"); // 备注
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
			attachmentsetService.check(pd1);
		}

		QualityInspectionPlanService.save(pd);
		map.put("result", errInfo);
		map.put("QualityInspectionPlan_ID", pd.getString("QualityInspectionPlan_ID"));

		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	// @RequiresPermissions("QualityInspectionPlan:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 删除附件
		PageData pData = new PageData();
		pData.put("AssociationID", pd.getString("QualityInspectionPlan_ID"));
		attachmentsetService.delete(pd);
		QualityInspectionPlanService.delete(pd);
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
	// @RequiresPermissions("QualityInspectionPlan:edit")
	@ResponseBody
	public Object edit(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			PageData pd1 = new PageData();
			String ffile = DateUtil.getDays();
			String FFILENAME = "";
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			pd1.put("FUrl", FPFFILEPATH); // 附件路径
			pd1.put("FName", FFILENAME); // 附件名称
			pd1.put("DataSources", "质检方案"); // 数据源
			pd1.put("AssociationIDTable", "KM_QualityInspectionPlan"); // 数据源表
			pd1.put("AssociationID", pd.getString("QualityInspectionPlan_ID")); // 数据源ID
			pd1.put("FExplanation", "附件"); // 备注
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
			attachmentsetService.check(pd1);
		}
		QualityInspectionPlanService.edit(pd);
		map.put("result", errInfo);
		map.put("QualityInspectionPlan_ID", pd.getString("QualityInspectionPlan_ID"));

		return map;
	}

	/**
	 * 修改状态
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStatus")
	@ResponseBody
	public Object changeStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanService.changeStatus(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	// //@RequiresPermissions("QualityInspectionPlan:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = QualityInspectionPlanService.list(page); // 列出QualityInspectionPlan列表
		for (PageData pageData : varList) {
			String QualityInspectionPlan_ID = pageData.getString("QualityInspectionPlan_ID");
			PageData pData = new PageData();
			pData.put("QIPlanID", QualityInspectionPlan_ID);
			// List<PageData> qipamList = qipamService.listAll(pData);
			// for (PageData qipam : qipamList) {
			// String MaterialID = qipam.getString("ApplicableMaterialID");
			// PageData mdData = new PageData();
			// mdData.put("MAT_BASIC_ID",MaterialID);
			// PageData findById = mat_basicService.findById(mdData);
			// String MAT_NAME = String.valueOf(findById.get("MAT_NAME"));
			// qipam.put("MAT_NAME", MAT_NAME);
			//
			// }
			List<PageData> qipiDetailList = qipiDetailService.listAll(pData);
			List<Object> itemNameList = Lists.newArrayList();
			for (PageData detail : qipiDetailList) {
				String QIItemID = detail.getString("QIItemID");
				pData.put("QualityInspectionItems_ID", QIItemID);

				PageData qiitem = qualityinspectionitemsService.findById(pData);
				if (null == (qiitem)) {
					continue;
				}
				String FName = qiitem.getString("FName");
				itemNameList.add(FName);
			}
			pageData.put("itemNameList", itemNameList);
			// pageData.put("QIPlanMaterialList", qipamList);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 质检方案列表(不带分页，用于下拉框)
	 * 
	 * @param 无
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Object listAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = QualityInspectionPlanService.listAll(pd); // 列出QualityInspectionPlan列表
		for (PageData pageData : varList) {
			String QualityInspectionPlan_ID = pageData.getString("QualityInspectionPlan_ID");
			PageData pData = new PageData();
			pData.put("QIPlanID", QualityInspectionPlan_ID);
			List<PageData> qipiDetailList = qipiDetailService.listAll(pData);
			List<Object> itemNameList = Lists.newArrayList();
			for (PageData detail : qipiDetailList) {
				String QIItemID = detail.getString("QIItemID");
				pData.put("QualityInspectionItems_ID", QIItemID);

				PageData qiitem = qualityinspectionitemsService.findById(pData);
				if (null == (qiitem)) {
					continue;
				}
				String FName = qiitem.getString("FName");
				itemNameList.add(FName);
			}
			pageData.put("itemNameList", itemNameList);
		}

		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	// //@RequiresPermissions("QualityInspectionPlan:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanService.findById(pd); // 根据ID读取
		if(null!=pd){
			PageData pData = new PageData();
			pData.put("QIPlanID", pd.getString("QualityInspectionPlan_ID"));
			List<PageData> qipamList = qipamService.listAll(pData);
			for (PageData qipam : qipamList) {
				String MaterialID = qipam.getString("ApplicableMaterialID");
				PageData mdData = new PageData();
				mdData.put("MAT_BASIC_ID", MaterialID);
				PageData findById = mat_basicService.findById(mdData);
				if (null == findById) {
					qipam.put("MAT_NAME", "");
					qipam.put("MAT_CODE", "");
					continue;
				}
				String MAT_NAME = String.valueOf(findById.get("MAT_NAME"));
				String MAT_CODE = String.valueOf(findById.get("MAT_CODE"));
				qipam.put("MAT_NAME", MAT_NAME);
				qipam.put("MAT_CODE", MAT_CODE);
			}
			List<PageData> qipiDetailList = qipiDetailService.listAll(pData);
			for (PageData detail : qipiDetailList) {
				PageData custParam = new PageData();

				String FCustomerID = detail.getString("FCustomer");
				custParam.put("Customer_ID", FCustomerID);
				PageData customer = CustomerService.findById(custParam);
				if (null != customer) {
					detail.put("FCustomerName", customer.getString("FNum") + "/" + customer.getString("FName"));
				}
				String QIItemID = detail.getString("QIItemID");
				pData.put("QualityInspectionItems_ID", QIItemID);

				PageData qiitem = qualityinspectionitemsService.findById(pData);
				if (null == (qiitem)) {
					continue;
				}
				String FName = qiitem.getString("FName");
				detail.put("QIItemName", FName);
				
				
				if(null!=detail && detail.containsKey("BreakdownOfBadReasons") && null!= detail.getString("BreakdownOfBadReasons")) {
					String[] sarr=detail.getString("BreakdownOfBadReasons").split(",yl,");
					List<String> varList1 =  new ArrayList<>();
					for(int j=0;j<sarr.length;j++) {
						PageData temp = new PageData();
						temp.put("DICTIONARIES_ID", sarr[j]);
						PageData spd = dictionariesService.findById(temp);	//根据ID读取
						if(null!=spd && spd.containsKey("NAME")) {
							varList1.add(spd.getString("NAME"));
						}
						
					}
					detail.put("BreakdownOfBadReasons", sarr);
					detail.put("BreakdownOfBadReasonsNAME", varList1);
				}
			}
			// 根据主键获取上传的文件
			PageData pageData = new PageData();
			pageData.put("AssociationID", pd.getString("QualityInspectionPlan_ID"));
			List<PageData> findByAssId2 = attachmentsetService.findByAssId(pageData);
			if (CollectionUtil.isNotEmpty(findByAssId2)) {
				PageData findByAssId = findByAssId2.get(0);
				if (null != findByAssId) {
					String FileName = (null != findByAssId && null != findByAssId.getString("FName"))
							? findByAssId.getString("FName") : "";
					pd.put("FileName", FileName);
					String FileUrl = (null != findByAssId && null != findByAssId.getString("FUrl"))
							? findByAssId.getString("FUrl") : "";
					pd.put("FileUrl", FileUrl);
				}
			}
			pd.put("QIPlanDetailList", qipiDetailList);
			pd.put("QIPlanMaterialList", qipamList);
		}
		

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
	// @RequiresPermissions("QualityInspectionPlan:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			// 删除附件
			for (String AssociationID : ArrayDATA_IDS) {
				PageData pData = new PageData();
				pData.put("AssociationID", AssociationID);
				attachmentsetService.delete(pd);
			}
			QualityInspectionPlanService.deleteAll(ArrayDATA_IDS);
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
	// @RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("名称"); // 1
		titles.add("状态"); // 2
		titles.add("自动生成任务"); // 3
		titles.add("质检类型"); // 4
		titles.add("质检频次"); // 5
		titles.add("质检方式"); // 6
		titles.add("记录方式"); // 7
		titles.add("报废性检查"); // 8
		titles.add("样本判定维度"); // 9
		titles.add("质检项填写规则"); // 10
		titles.add("特别提醒"); // 11
		titles.add("可添加多种物料"); // 12
		titles.add("备注"); // 13
		dataMap.put("titles", titles);
		List<PageData> varOList = QualityInspectionPlanService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FName")); // 1
			vpd.put("var2", varOList.get(i).getString("FStatus")); // 2
			vpd.put("var3", varOList.get(i).getString("AutomaticallyGenerateTasks")); // 3
			vpd.put("var4", varOList.get(i).getString("QIType")); // 4
			vpd.put("var5", varOList.get(i).getString("QIFrequency")); // 5
			vpd.put("var6", varOList.get(i).getString("QIMethod")); // 6
			vpd.put("var7", varOList.get(i).getString("RecordingMethod")); // 7
			vpd.put("var8", varOList.get(i).getString("ScrapInspection")); // 8
			vpd.put("var9", varOList.get(i).getString("SampleDecisionDimension")); // 9
			vpd.put("var10", varOList.get(i).getString("QIItemsRules")); // 10
			vpd.put("var11", varOList.get(i).getString("SpecialReminder")); // 11
			vpd.put("var12", varOList.get(i).getString("MultipleMaterialsCanBeAdded")); // 12
			vpd.put("var13", varOList.get(i).getString("FExplanation")); // 13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
