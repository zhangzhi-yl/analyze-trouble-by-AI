package org.yy.controller.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.NEW_BOMService;

/** 
 * 说明：实例阶段-生产BOM
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/NEW_BOM")
public class NEW_BOMController extends BaseController {
	
	@Autowired
	private NEW_BOMService NEW_BOMService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("NEW_BOM:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NEW_BOM_ID", this.get32UUID());	//主键
		NEW_BOMService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("NEW_BOM:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		NEW_BOMService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("NEW_BOM:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		NEW_BOMService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("NEW_BOM:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = NEW_BOMService.list(page);	//列出NEW_BOM列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("NEW_BOM:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = NEW_BOMService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("NEW_BOM:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			NEW_BOMService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("PH_节点ID");	//1
		titles.add("PH_节点名称");	//2
		titles.add("PH_节点种类");	//3
		titles.add("PH_节点类型");	//4
		titles.add("PH_步骤类型");	//5
		titles.add("PH_跳转条件");	//6
		titles.add("PH_开始节点");	//7
		titles.add("PH_结束节点");	//8
		titles.add("PH_参与者");	//9
		titles.add("创建人");	//10
		titles.add("创建时间");	//11
		titles.add("最后变更人");	//12
		titles.add("最后变更时间");	//13
		titles.add("PH_描述");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = NEW_BOMService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NODE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("NODE_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("NODE_KIND"));	    //3
			vpd.put("var4", varOList.get(i).getString("NODE_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("PHASE_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("JUMP_CONDITION"));	    //6
			vpd.put("var7", varOList.get(i).getString("BEGIN_NODE"));	    //7
			vpd.put("var8", varOList.get(i).getString("END_NODE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FPARTICIPATOR"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCREATOR"));	    //10
			vpd.put("var11", varOList.get(i).getString("CREATE_TIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("LAST_MODIFIER"));	    //12
			vpd.put("var13", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("FDES"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**获取流程图信息
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/getBYTEARRAYMsg")
		@ResponseBody
		public Object getBYTEARRAYMsg() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = BYTEARRAYService.findByPID(pd);	//根据ID读取
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
}
