package org.yy.controller.project.manager;

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
import org.yy.service.project.manager.ASSEMBLYSTATISTICSService;
import org.yy.service.project.manager.REASON_TYPEService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

/**
 * 说明：toc折线图报表 作者：YuanYes QQ356703572 时间：2021-05-13 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ASSEMBLYSTATISTICS")
public class ASSEMBLYSTATISTICSController extends BaseController {

	@Autowired
	private ASSEMBLYSTATISTICSService ASSEMBLYSTATISTICSService;
	@Autowired
	private REASON_TYPEService reason_typeService;
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
		pd.put("ASSEMBLYSTATISTICS_ID", this.get32UUID()); // 主键
		ASSEMBLYSTATISTICSService.save(pd);
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
		ASSEMBLYSTATISTICSService.delete(pd);
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
		ASSEMBLYSTATISTICSService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * v2 管悦 2021-07-19 原因类型拆分( ,yl, )变为数组传给前端
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
		List<PageData> varList = ASSEMBLYSTATISTICSService.list(page); // 列出ASSEMBLYSTATISTICS列表
		for (PageData pds : varList) {
			String REASON_TYPE_ID = Tools.notEmpty(pds.getString("REASON_TYPE_ID")) ? pds.getString("REASON_TYPE_ID") : "";
			String[] REASON_TYPE_IDsplit = REASON_TYPE_ID.split(",yl,");
			String[] REASON_TYPE_IDsplit1=new String[REASON_TYPE_IDsplit.length] ;
			PageData pdx=new PageData();
			String REASONS="";
			if (!"".equals(REASON_TYPE_IDsplit[0])) {
				for(int i=0;i<REASON_TYPE_IDsplit.length;i++)
				{
					pdx.put("REASON_TYPE_ID", REASON_TYPE_IDsplit[i]);
					PageData pdxx=reason_typeService.findById(pdx);
					if(pdxx !=null) {
						REASON_TYPE_IDsplit1[i]=pdxx.getString("REASON_TYPE");
						pds.put("REASON_TYPE_ID", Lists.newArrayList(REASON_TYPE_IDsplit1));
						if(i==0) {
							REASONS+=pdxx.getString("REASON_TYPE");
						}else {
							REASONS+=","+pdxx.getString("REASON_TYPE");
						}
					}else {
						pds.put("REASON_TYPE_ID", Lists.newArrayList());
					}
				}
				
			} else {
				pds.put("REASON_TYPE_ID", Lists.newArrayList());
			}
			pds.put("REASONS", REASONS);
		}
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
		pd = ASSEMBLYSTATISTICSService.findById(pd); // 根据ID读取
		// 原因类型拆分( ,yl, )变为数组传给前端
		String REASON_TYPE_ID = Tools.notEmpty(pd.getString("REASON_TYPE_ID")) ? pd.getString("REASON_TYPE_ID") : "";
		String[] REASON_TYPE_IDsplit = REASON_TYPE_ID.split(",yl,");
		if ("".equals(REASON_TYPE_IDsplit[0])) {
			pd.put("REASON_TYPE_ID", Lists.newArrayList());
		} else {
			pd.put("REASON_TYPE_ID", Lists.newArrayList(REASON_TYPE_IDsplit));
		}
		pd.put("REASON_TYPE_ID", Lists.newArrayList(REASON_TYPE_ID.split(",yl,")));
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
			ASSEMBLYSTATISTICSService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注12"); // 11
		titles.add("备注13"); // 12
		titles.add("备注14"); // 13
		titles.add("备注15"); // 14
		titles.add("备注16"); // 15
		dataMap.put("titles", titles);
		List<PageData> varOList = ASSEMBLYSTATISTICSService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FFOUNDER")); // 1
			vpd.put("var2", varOList.get(i).getString("FFOUNDERNUM")); // 2
			vpd.put("var3", varOList.get(i).getString("FCREATIONTIME")); // 3
			vpd.put("var4", varOList.get(i).getString("FLASTUPDATETIME")); // 4
			vpd.put("var5", varOList.get(i).getString("FDEPARTMENT")); // 5
			vpd.put("var6", varOList.get(i).getString("FDEPARTMENTUUID")); // 6
			vpd.put("var7", varOList.get(i).getString("FLASTUPDATEPEOPLE")); // 7
			vpd.put("var8", varOList.get(i).getString("FAuditStatus")); // 8
			vpd.put("var9", varOList.get(i).getString("PROC_INST_ID_")); // 9
			vpd.put("var10", varOList.get(i).getString("FDATE")); // 10
			vpd.put("var11", varOList.get(i).getString("FRED")); // 11
			vpd.put("var12", varOList.get(i).getString("FYELLOW")); // 12
			vpd.put("var13", varOList.get(i).getString("FGREEN")); // 13
			vpd.put("var14", varOList.get(i).getString("FBLACK")); // 14
			vpd.put("var15", varOList.get(i).getString("FREMARK")); // 15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
