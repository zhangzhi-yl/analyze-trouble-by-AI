package org.yy.controller.km;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import org.yy.service.km.QualityInspectionItemsService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

/**
 * 说明：质检项 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/qualityinspectionitems")
public class QualityInspectionItemsController extends BaseController {

	@Autowired
	private QualityInspectionItemsService qualityinspectionitemsService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private AttachmentSetService attachmentsetService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		String QualityInspectionItems_ID = this.get32UUID();// 主键
		String FFILENAME = "";
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人

		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			pd1.put("FUrl", FPFFILEPATH); // 附件路径
			pd1.put("FName", FFILENAME); // 附件名称
			pd1.put("DataSources", "质检项"); // 数据源
			pd1.put("AssociationIDTable", "KM_QualityInspectionItems"); // 数据源表
			pd1.put("AssociationID", QualityInspectionItems_ID); // 数据源ID
			pd1.put("FExplanation", pd.getString("FExplanation")); // 备注
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
			attachmentsetService.check(pd1);
		}

		pd.put("QualityInspectionItems_ID", QualityInspectionItems_ID);
		qualityinspectionitemsService.save(pd);

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
	// @RequiresPermissions("qualityinspectionitems:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		qualityinspectionitemsService.delete(pd);
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
	public Object edit(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		String FFILENAME = "";
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人
		if (null != FFILEPATH && !FFILEPATH.isEmpty() && !"undefined".equals(FFILEPATH)) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			pd1.put("FUrl", FPFFILEPATH); // 附件路径
			pd1.put("FName", FFILENAME); // 附件名称
			pd1.put("DataSources", "质检项"); // 数据源
			pd1.put("AssociationIDTable", "KM_QualityInspectionItems"); // 数据源表
			pd1.put("AssociationID", pd.getString("QualityInspectionItems_ID")); // 数据源ID
			pd1.put("FExplanation", pd.getString("FExplanation")); // 备注
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
			attachmentsetService.check(pd1);
		}
		if (pd.getString("FFILEPATH") != null && pd.getString("FFILEPATH") != ""
				&& !"undefined".equals(pd.getString("FFILEPATH"))) {
			pd1.put("FUrl", pd.getString("FFILEPATH")); // 附件路径
			pd1.put("FName", pd.getString("FFILENAME")); // 附件名称
			pd1.put("DataSources", "质检项"); // 数据源
			pd1.put("AssociationIDTable", "KM_QualityInspectionItems"); // 数据源表
			pd1.put("AssociationID", pd.getString("QualityInspectionItems_ID")); // 数据源ID
			pd1.put("FExplanation", pd.getString("FExplanation")); // 备注
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
			attachmentsetService.check(pd1);
		}

		qualityinspectionitemsService.edit(pd);
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
	// @RequiresPermissions("qualityinspectionitems:list")
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
		List<PageData> varList = qualityinspectionitemsService.list(page); // 列出QualityInspectionItems列表
		map.put("varList", varList);
		map.put("page", page);
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
	// @RequiresPermissions("qualityinspectionitems:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = qualityinspectionitemsService.findById(pd); // 根据ID读取
		PageData pageData = new PageData();
		pageData.put("AssociationID", pd.getString("QualityInspectionItems_ID"));
		pageData.put("AssociationIDTable", "KM_QualityInspectionItems");
		List<PageData> findByAssId = attachmentsetService.findByAssId(pageData);
		if (CollectionUtil.isNotEmpty(findByAssId)) {
			PageData attact = findByAssId.get(0);
			if (null != attact) {
				pd.put("AFName", attact.getString("FName"));
				pd.put("FUrl", attact.getString("FUrl"));
			}

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
	// @RequiresPermissions("qualityinspectionitems:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			qualityinspectionitemsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("分类"); // 2
		titles.add("备注"); // 3
		titles.add("附件"); // 4
		dataMap.put("titles", titles);
		List<PageData> varOList = qualityinspectionitemsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNAME")); // 1
			vpd.put("var2", varOList.get(i).getString("FCATEGORY")); // 2
			vpd.put("var3", varOList.get(i).getString("FEXPLANATION")); // 3
			vpd.put("var4", varOList.get(i).getString("FATTACHMENT")); // 4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 下载
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/Dowland")
	public void downExcel(HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pageData = new PageData();
		pageData.put("AssociationID", pd.getString("QualityInspectionItems_ID"));
		pageData.put("AssociationIDTable", "KM_QualityInspectionItems");
		List<PageData> findByAssId = attachmentsetService.findByAssId(pageData);
		if (CollectionUtil.isNotEmpty(findByAssId)) {
			PageData attact = findByAssId.get(0);
			if(null!=attact){
				String FFILEPATH = attact.getString("FUrl"); // 文件路径
				String FFILENAME = attact.getString("FName"); // 文件名称
				FileDownload.fileDownload(response, PathUtil.getProjectpath() + FFILEPATH, FFILENAME);
			}

		}
	}

}
