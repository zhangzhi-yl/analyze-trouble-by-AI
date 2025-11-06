package org.yy.controller.trouble;

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
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.service.trouble.TroubleLocalDefineService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.trouble.TroubleLocalService;

/** 
 * 说明：隐患管理
 * 作者：YuanYes QQ356703572
 * 时间：2025-09-23
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/troublelocal")
public class TroubleLocalController extends BaseController {
	
	@Autowired
	private TroubleLocalService troublelocalService;
	@Autowired
	private TroubleLocalDefineService troublelocaldefineService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TROUBLELOCAL_ID", this.get32UUID());	//主键
		pd.put("CREATE_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("CREATE_BY", Jurisdiction.getName());
		troublelocalService.save(pd);
		String[] defines = pd.get("define").toString().split(",");
		for(String define : defines){
			PageData pdm = new PageData();
			pdm.put("TROUBLELOCALDEFINE_ID", this.get32UUID());
			pdm.put("LOCAL_ID", pd.get("TROUBLELOCAL_ID"));
			pdm.put("DEFINE_ID", define);
			troublelocaldefineService.save(pdm);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		troublelocalService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("UPDATE_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("UPDATE_BY", Jurisdiction.getName());
		troublelocalService.edit(pd);
		String[] defines = pd.get("define").toString().split(",");
		//删除现有定义
		pd.put("LOCAL_ID", pd.get("TROUBLELOCAL_ID"));
		troublelocaldefineService.deleteByLocalId(pd);
		for(String define : defines){
			PageData pdm = new PageData();
			pdm.put("TROUBLELOCALDEFINE_ID", this.get32UUID());
			pdm.put("LOCAL_ID", pd.get("TROUBLELOCAL_ID"));
			pdm.put("DEFINE_ID", define);
			troublelocaldefineService.save(pdm);
		}
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/handleSwitchChange")
	@ResponseBody
	public Object handleSwitchChange() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData findById = troublelocalService.findById(pd);
		findById.put("UPDATE_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		findById.put("UPDATE_BY", Jurisdiction.getName());
		findById.put("CATCH_SWITCH", pd.get("CATCH_SWITCH"));
		troublelocalService.edit(findById);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = troublelocalService.list(page);	//列出TroubleLocal列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/listAll")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = troublelocalService.listAll(pd);	//列出TroubleLocal列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = troublelocalService.findById(pd);	//根据ID读取
		pd.put("LOCAL_ID",pd.get("TROUBLELOCAL_ID"));

		List<PageData> defines = troublelocaldefineService.listAll(pd);
		String[] define = new String[defines.size()];
		for(int i = 0; i < define.length; i++){
			define[i] = defines.get(i).get("DEFINE_ID").toString();
		}
		pd.put("define", define);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			troublelocalService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("厂区");	//1
		titles.add("位置名称");	//2
		titles.add("位置编码");	//3
		titles.add("经度");	//4
		titles.add("纬度");	//5
		titles.add("责任部门");	//6
		titles.add("责任人");	//7
		titles.add("创建人");	//8
		titles.add("创建时间");	//9
		titles.add("更新人");	//10
		titles.add("更新时间");	//11
		titles.add("备注");	//12
		titles.add("URL");	//13
		titles.add("是否自动抓取");	//14
		titles.add("采集时间间隔");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = troublelocalService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FACTORY_AREA"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("LONGITUDE"));	    //4
			vpd.put("var5", varOList.get(i).getString("DIMENSION"));	    //5
			vpd.put("var6", varOList.get(i).getString("RESPONSIBLE_DEP"));	    //6
			vpd.put("var7", varOList.get(i).getString("RESPONSIBLE_PEOPLE"));	    //7
			vpd.put("var8", varOList.get(i).getString("CREATE_BY"));	    //8
			vpd.put("var9", varOList.get(i).getString("CREATE_TIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("UPDATE_BY"));	    //10
			vpd.put("var11", varOList.get(i).getString("UPDATE_TIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("REMARK"));	    //12
			vpd.put("var13", varOList.get(i).getString("URL"));	    //13
			vpd.put("var14", varOList.get(i).getString("CATCH_SWITCH"));	    //14
			vpd.put("var15", varOList.get(i).getString("CATCH_INTERVAL"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
