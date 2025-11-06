package org.yy.controller.mdm;

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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mdm.EQM_VERIFYService;

/** 
 * 说明：设备校验登记
 * 作者：YuanYe
 * 时间：2020-02-18
 * 
 */
@Controller
@RequestMapping("/eqm_verify")
public class EQM_VERIFYController extends BaseController {
	
	@Autowired
	private EQM_VERIFYService eqm_verifyService;
	@Autowired
	private EQM_BASEService eqm_baseService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_verify:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_VERIFY_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_verifyService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_verify:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_verifyService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_verify:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_verifyService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_verify:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_verifyService.list(page);	//列出EQM_VERIFY列表
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
	//@RequiresPermissions("eqm_verify:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_verifyService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**去新增页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goAdd")
		//@RequiresPermissions("eqm_verify:edit")
		@ResponseBody
		public Object goAdd() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData verifyPd = new PageData();
			pd = this.getPageData();
			pd = eqm_baseService.findById(pd);	//根据基础资料ID读取相关信息
			if(null!=pd) {
				verifyPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));//设备基础资料ID
				verifyPd.put("FNAME", pd.getString("FNAME")!=null?pd.getString("FNAME"):"");//设备名称
				verifyPd.put("FNUMBER", pd.getString("FIDENTIFY")!=null?pd.getString("FIDENTIFY"):"");//设备编号
				verifyPd.put("FSPECS", pd.getString("FSPECS")!=null?pd.getString("FSPECS"):"");//规格型号
				verifyPd.put("FEQM_CLASS", pd.getString("CLASS_NAME")!=null?pd.getString("CLASS_NAME"):"");//设备类名
			}
			map.put("pd", verifyPd);
			map.put("result", errInfo);
			return map;
		}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_verify:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_verifyService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("描述");	//2
		titles.add("设备基础资料ID");	//3
		titles.add("设备名称");	//4
		titles.add("设备编号");	//5
		titles.add("规格型号");	//6
		titles.add("校验周期");	//7
		titles.add("校验证编号");	//8
		titles.add("颁证日期");	//9
		titles.add("颁证单位");	//10
		titles.add("有效期");	//11
		titles.add("下一次校验日期");	//12
		titles.add("创建人");	//13
		titles.add("设备类别");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_verifyService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CREATE_TIME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FDES"));	    //2
			vpd.put("var3", varOList.get(i).getString("EQM_BASE_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FNAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("FNUMBER"));	    //5
			vpd.put("var6", varOList.get(i).getString("FSPECS"));	    //6
			vpd.put("var7", varOList.get(i).getString("FVERIFY_CYCLE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FVERIFY_NUMBER"));	    //8
			vpd.put("var9", varOList.get(i).getString("FISSUANCE_DATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("FISSUANCE_UNIT"));	    //10
			vpd.put("var11", varOList.get(i).getString("FVALIDITY"));	    //11
			vpd.put("var12", varOList.get(i).getString("FNEXTFVERIFY_DATE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCREATOR"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEQM_CLASS"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
