package org.yy.controller.qm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.qm.Compose_SampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

/**
 * 说明：合样接口 作者：YuanYes QQ356703572 时间：2021-06-07 官网：356703572@qq.com
 * 
 * v1-ccg-20210607 合样接口-增删改查-添加合样号并关联样品
 */
@Controller
@RequestMapping("/Compose_Sample")
public class Compose_SampleController extends BaseController {

	@Autowired
	private Compose_SampleService Compose_SampleService;
	@Autowired
	private QualityInspectionPlanExecuteSampleService QualityInspectionPlanExecuteSampleService;
	@Autowired
	private StaffService StaffService;

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
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		pd.put("Create_Person_ID", STAFF_ID);
		pd.put("Create_Time", Tools.date2Str(new Date()));
		pd.put("Update_Person_ID", STAFF_ID);
		pd.put("Update_Time", Tools.date2Str(new Date()));
		pd.put("Compose_Sample_ID", this.get32UUID()); // 主键
		Compose_SampleService.save(pd);
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
		Compose_SampleService.delete(pd);
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
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		pd.put("Update_Person_ID", STAFF_ID);
		pd.put("Update_Time", Tools.date2Str(new Date()));
		Compose_SampleService.edit(pd);
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
		List<PageData> varList = Compose_SampleService.list(page); // 列出Compose_Sample列表
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
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = Compose_SampleService.findById(pd); // 根据ID读取
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
			Compose_SampleService.deleteAll(ArrayDATA_IDS);
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
		titles.add("合样码"); // 1
		titles.add("创建时间"); // 2
		titles.add("修改时间"); // 3
		titles.add("创建人"); // 4
		titles.add("修改人"); // 5
		dataMap.put("titles", titles);
		List<PageData> varOList = Compose_SampleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("Compose_Code")); // 1
			vpd.put("var2", varOList.get(i).getString("Create_Time")); // 2
			vpd.put("var3", varOList.get(i).getString("Update_Time")); // 3
			vpd.put("var4", varOList.get(i).getString("Create_Person_ID")); // 4
			vpd.put("var5", varOList.get(i).getString("Update_Person_ID")); // 5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * v1-ccg-20210607-获取所有未绑定合样样品列表-左侧
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllNoBindComposeCode4SampleList")
	@ResponseBody
	public Object getAllNoBindComposeCode4SampleList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			// 1.获取前台的compose_code
			pd = this.getPageData();
			// 2.根据compose_code获取绑定数据
			List<PageData> listAll = QualityInspectionPlanExecuteSampleService.getAllNoBindComposeCode4SampleList(pd);
			map.put("result", errInfo); // 返回结果
			map.put("varlist", listAll);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
			return map;
		}

	}
	
	/**
	 * v1-ccg-20210607-合样绑定-右侧提交
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/bindComposeCode")
	@ResponseBody
	public Object bindComposeCode() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();

			// 0.获取前台的compose_code 如果合样code 存在 则提示 已经收集过了，否则新增记录
			String Compose_Code = pd.getString("Compose_Code");
			if (StringUtil.isNotEmpty(Compose_Code)) {
				throw new RuntimeException("该合样号码为空，请检查后添加");
			}
			String QualityInspectionPlanExecute_ID = pd.getString("QualityInspectionPlanExecute_ID");
			if (StringUtil.isNotEmpty(QualityInspectionPlanExecute_ID)) {
				throw new RuntimeException("该任务id为空，请检查后添加");
			}
			List<PageData> listAll2 = Compose_SampleService.listAll(pd);
			if (CollectionUtil.isNotEmpty(listAll2)) {
				throw new RuntimeException("该合样号码已经存在，请勿重复添加");
			}

			// 新增
			pd = this.getPageData();
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
			pd.put("Create_Person_ID", STAFF_ID);
			pd.put("Create_Time", Tools.date2Str(new Date()));
			pd.put("Update_Person_ID", STAFF_ID);
			pd.put("Update_Time", Tools.date2Str(new Date()));
			pd.put("Compose_Sample_ID", this.get32UUID()); // 主键
			Compose_SampleService.save(pd);
			// 1.获取前台的TagCode 用 逗号分割
			String DATA_IDS = pd.getString("TagCode");
			if (Tools.notEmpty(DATA_IDS)) {
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				for (String TagCode : ArrayDATA_IDS) {
					PageData pData = new PageData();
					pData.put("TagCode", TagCode);
					// 2.根据样品编号循环获取样品数据
					List<PageData> listAll = QualityInspectionPlanExecuteSampleService.listAll(pData);
					for (PageData pageData : listAll) {
						// 3. 给样品数据赋值 compose_code
						pageData.put("Compose_Code", Compose_Code);
						QualityInspectionPlanExecuteSampleService.edit(pageData);
					}
				}
				errInfo = "success";
			} else {
				errInfo = "error";
			}

			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
			return map;
		}

	}
	
	
	/**
	 * v1-ccg-20210607-获取合样列表-下侧列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Object listAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 根据质检任务获取合样列表
		List<PageData> varList = Compose_SampleService.listAll(pd); // 列出Compose_Sample列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**
	 * v1-ccg-20210607-根据合样code 获取样品列表-点击下侧详情
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSampleListByComposeCode")
	@ResponseBody
	public Object getSampleListByComposeCode() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			// 1.获取前台的compose_code
			pd = this.getPageData();
			// 2.根据compose_code获取绑定数据
			List<PageData> listAll = QualityInspectionPlanExecuteSampleService.listAll(pd);
			map.put("result", errInfo); // 返回结果
			map.put("varlist", listAll);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
			return map;
		}

	}


	/**
	 * v1-ccg-20210607-详情里-点击解除绑定
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/unbindComposeCode")
	@ResponseBody
	public Object unbindComposeCode() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 1.获取前台的TagCode
			String TagCode = pd.getString("TagCode");
			if (StringUtil.isNotEmpty(TagCode)) {
				throw new RuntimeException("该样品号码为空，请检查后添加");
			}
			PageData pData = new PageData();
			pData.put("TagCode", TagCode);
			// 2.移除样品 compose_code 字段 更新
			List<PageData> listAll = QualityInspectionPlanExecuteSampleService.listAll(pData);
			for (PageData pageData : listAll) {
				// 3. 给样品数据赋值 compose_code
				pageData.put("Compose_Code", "");
				QualityInspectionPlanExecuteSampleService.edit(pageData);
			}
			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
			return map;
		}
	}
}
