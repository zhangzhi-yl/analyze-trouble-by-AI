package org.yy.controller.km;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.km.StandardCapacityService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：标准产能 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/standardcapacity")
public class StandardCapacityController extends BaseController {

	@Autowired
	private StandardCapacityService standardcapacityService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("standardcapacity:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("STANDARDCAPACITY_ID", this.get32UUID()); // 主键
		standardcapacityService.save(pd);
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
	@RequiresPermissions("standardcapacity:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		standardcapacityService.delete(pd);
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
	@RequiresPermissions("standardcapacity:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		standardcapacityService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 修改状态
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStatus")
	@RequiresPermissions("standardcapacity:edit")
	@ResponseBody
	public Object changeStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		standardcapacityService.changeStatus(pd);
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
	@RequiresPermissions("standardcapacity:list")
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
		List<PageData> varList = standardcapacityService.list(page); // 列出StandardCapacity列表
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
	@RequiresPermissions("standardcapacity:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = standardcapacityService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("standardcapacity:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			standardcapacityService.deleteAll(ArrayDATA_IDS);
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
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编号"); // 1
		titles.add("工序类型"); // 2
		titles.add("工序"); // 3
		titles.add("产出物料"); // 4
		titles.add("工位"); // 5
		titles.add("递增方式"); // 6
		titles.add("标准产能类型"); // 7
		titles.add("标准产能类型值"); // 8
		titles.add("标准产能类型单位"); // 9
		titles.add("版本号"); // 10
		titles.add("创建时间"); // 10
		titles.add("状态"); // 11
		dataMap.put("titles", titles);// 12
		List<PageData> varOList = standardcapacityService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNUM")); // 1
			vpd.put("var2", varOList.get(i).getString("WPTYPE")); // 2
			vpd.put("var3", varOList.get(i).getString("WP")); // 3
			vpd.put("var4", varOList.get(i).getString("OUTPUTMATERIAL")); // 4
			vpd.put("var5", varOList.get(i).getString("FSTATION")); // 5
			vpd.put("var6", varOList.get(i).getString("INCREMENTALMODE")); // 6
			vpd.put("var7", varOList.get(i).getString("STANDARDCAPACITYTYPE")); // 7
			vpd.put("var8", varOList.get(i).getString("STANDARDCAPACITYVALUE")); // 8
			vpd.put("var9", varOList.get(i).getString("STANDARDCAPACITYUNIT")); // 9
			vpd.put("var10", varOList.get(i).getString("FVERSION")); // 10
			vpd.put("var11", varOList.get(i).getString("FCREATETIME")); // 10
			vpd.put("var12", varOList.get(i).getString("FSTATUS")); // 10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
