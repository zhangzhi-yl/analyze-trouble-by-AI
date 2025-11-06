package org.yy.controller.zm;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

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
import org.yy.service.zm.LogService;

/**
 * 说明：日志
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;

//	/**保存
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/add")
//	//@RequiresPermissions("log:add")
//	@ResponseBody
//	public Object add() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd.put("LOG_ID", this.get32UUID());	//主键
//		logService.save(pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	/**删除
//	 * @param out
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/delete")
//	//@RequiresPermissions("log:del")
//	@ResponseBody
//	public Object delete() throws Exception{
//		Map<String,String> map = new HashMap<String,String>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		logService.delete(pd);
//		map.put("result", errInfo);				//返回结果
//		return map;
//	}
//
//	/**修改
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/edit")
//	//@RequiresPermissions("log:edit")
//	@ResponseBody
//	public Object edit() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		logService.edit(pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("log:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = logService.list(page);	//列出Log列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

//	 /**去修改页面获取数据
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/goEdit")
//	//@RequiresPermissions("log:edit")
//	@ResponseBody
//	public Object goEdit() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd = logService.findById(pd);	//根据ID读取
//		map.put("pd", pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	 /**批量删除
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/deleteAll")
//	//@RequiresPermissions("log:del")
//	@ResponseBody
//	public Object deleteAll() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		String DATA_IDS = pd.getString("DATA_IDS");
//		if(Tools.notEmpty(DATA_IDS)){
//			String ArrayDATA_IDS[] = DATA_IDS.split(",");
//			logService.deleteAll(ArrayDATA_IDS);
//			errInfo = "success";
//		}else{
//			errInfo = "error";
//		}
//		map.put("result", errInfo);				//返回结果
//		return map;
//	}
//
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
		titles.add("操作人");	//1
		titles.add("操作时间");	//2
		titles.add("操作内容");	//3
		titles.add("操作对象");	//7
		titles.add("PLC地址");	//4
		titles.add("IP地址");	//5
		//titles.add("扩展1");	//6
		//titles.add("扩展3");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = logService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("Operator"));	    //1
			vpd.put("var2", varOList.get(i).getString("OperationTime"));	    //2
			vpd.put("var3", varOList.get(i).getString("Content"));	    //3
			vpd.put("var4", varOList.get(i).getString("Extend2"));	    //7
			vpd.put("var5", varOList.get(i).getString("PLC"));	    //4
			vpd.put("var6", varOList.get(i).getString("IP"));	    //5
			//vpd.put("var6", varOList.get(i).getString("Extend1"));	    //6
			//vpd.put("var8", varOList.get(i).getString("Extend3"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
