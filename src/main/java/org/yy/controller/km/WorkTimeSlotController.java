package org.yy.controller.km;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.km.WorkTimeSlotService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：工作时间段 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/worktimeslot")
public class WorkTimeSlotController extends BaseController {

	@Autowired
	private WorkTimeSlotService worktimeslotService;

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
		pd.put("WORKTIMESLOT_ID", this.get32UUID()); // 主键
		worktimeslotService.save(pd);
		map.put("result", errInfo);
		map.put("id", pd.getString("WORKTIMESLOT_ID"));
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
		worktimeslotService.delete(pd);
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
		worktimeslotService.edit(pd);
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
		List<PageData> varList = worktimeslotService.list(page); // 列出WorkTimeSlot列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value = "/listByIds")
	@ResponseBody
	public Object listByIds() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo;
		String SLOT_IDS = pd.getString("SLOT_IDS");
		if (Tools.notEmpty(SLOT_IDS)) {
			String ArrayDATA_IDS[] = SLOT_IDS.split(",");
			List<PageData> varList = worktimeslotService.listByIds(ArrayDATA_IDS);
			errInfo = "success";
			map.put("varList", varList);
			map.put("result", errInfo);
		} else {
			errInfo = "error";
		}
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
		pd = worktimeslotService.findById(pd); // 根据ID读取
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
			worktimeslotService.deleteAll(ArrayDATA_IDS);
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
		titles.add("工作时间ID"); // 1
		titles.add("序号"); // 2
		titles.add("开始时间"); // 3
		titles.add("结束时间"); // 4
		titles.add("时长"); // 5
		dataMap.put("titles", titles);
		List<PageData> varOList = worktimeslotService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WORKINGTIMEID")); // 1
			vpd.put("var2", varOList.get(i).getString("SERIALNUM")); // 2
			vpd.put("var3", varOList.get(i).getString("STARTTIME")); // 3
			vpd.put("var4", varOList.get(i).getString("ENDTIME")); // 4
			vpd.put("var5", varOList.get(i).getString("FDURATION")); // 5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
