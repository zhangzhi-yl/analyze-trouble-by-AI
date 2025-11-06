package org.yy.controller.km;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.km.CodingRulesDetailService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.TOOL_BASICSService;
import org.yy.service.km.TOOL_BORROWService;
import org.yy.service.km.TOOL_INSPECTService;
import org.yy.service.system.DictionariesService;

/** 
 * 说明：工器具管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-09
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/TOOL_BASICS")
public class TOOL_BASICSController extends BaseController {
	
	@Autowired
	private TOOL_BASICSService TOOL_BASICSService;
	@Autowired
	private TOOL_BORROWService TOOL_BORROWService;
	@Autowired
	private TOOL_INSPECTService TOOL_INSPECTService;
	@Autowired
	private DictionariesService dictionariesService;
	@Autowired
	private CodingRulesService codingrulesService;// 编码规则接口
	
	@Autowired
	private CodingRulesDetailService codingRulesDetailService;//编码规则详细接口
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("TOOL_BASICS:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TOOL_BASICS_ID", this.get32UUID());	//主键
		pd.put("FCreatePerson", Jurisdiction.getName());//创建人
		pd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
		TOOL_BASICSService.save(pd);
		pd = TOOL_BASICSService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("TOOL_BASICS:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			TOOL_BASICSService.delete(pd);
			TOOL_BORROWService.deleteBASICS(pd);//通过主表ID删除数据
			TOOL_INSPECTService.deleteBASICS(pd);//通过主表ID删除数据
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("TOOL_BASICS:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TOOL_BASICSService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("TOOL_BASICS:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = TOOL_BASICSService.list(page);	//列出TOOL_BASICS列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("TOOL_BASICS:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = TOOL_BASICSService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		titles.add("分类");	//1
		titles.add("编码");	//2
		titles.add("名称");	//3
		titles.add("状态");	//4
		titles.add("规格");	//5
		titles.add("价格");	//6
		titles.add("启用日期");	//7
		titles.add("检验周期");	//8
		titles.add("下次检验日期");	//9
		titles.add("库管员");	//10
		titles.add("报废说明");	//11
		titles.add("备注");	//12
		titles.add("创建人");	//13
		titles.add("创建时间");	//14
		titles.add("预留1");	//15
		titles.add("预留2");	//16
		titles.add("预留3");	//17
		titles.add("预留4");	//18
		titles.add("预留5");	//19
		dataMap.put("titles", titles);
		List<PageData> varOList = TOOL_BASICSService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FType"));	    //1
			vpd.put("var2", varOList.get(i).getString("Tool_Number"));	    //2
			vpd.put("var3", varOList.get(i).getString("Tool_Name"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSTATE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FSpectification"));	    //5
			vpd.put("var6", varOList.get(i).get("FPrice").toString());	//6
			vpd.put("var7", varOList.get(i).getString("FActive_Date"));	    //7
			vpd.put("var8", varOList.get(i).getString("FInspect_Cycle"));	    //8
			vpd.put("var9", varOList.get(i).getString("FNext_Inspect_Date"));	    //9
			vpd.put("var10", varOList.get(i).getString("FStore_Keeper"));	    //10
			vpd.put("var11", varOList.get(i).getString("FScrap_Explain"));	    //11
			vpd.put("var12", varOList.get(i).getString("FDESCRIBE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCreatePerson"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCreateTime"));	    //14
			vpd.put("var15", varOList.get(i).getString("FReservedOne"));	    //15
			vpd.put("var16", varOList.get(i).getString("FReservedTwo"));	    //16
			vpd.put("var17", varOList.get(i).getString("FReservedThree"));	    //17
			vpd.put("var18", varOList.get(i).getString("FReservedFour"));	    //18
			vpd.put("var19", varOList.get(i).getString("FReservedFive"));	    //19
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		int succeedNum=0;
		int failNum=0;
		PageData ToolBasicsPd = new PageData();
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        String reg="-?[0-9]+\\.?[0-9]*";
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //得到sheet
	        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
	        realRowCount = sheet.getPhysicalNumberOfRows();
	        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
	        	Row row = sheet.getRow(rowNum);
	            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
	            	break;
	            }
	            if(row.getRowNum() == -1) {
	                continue;
	            }else {
	                if(row.getRowNum() == 0) {//第一行表头跳过
	                    continue;
	                }
	            }
	            
	            ToolBasicsPd.put("TOOL_BASICS_ID", this.get32UUID());//工器具管理ID
	            //任务来源
	            if(null!=ObjectExcelRead.getCellValue(sheet, row, 0)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 0))) {
	            	PageData TypePd = new PageData();
	            	TypePd.put("NAME", ObjectExcelRead.getCellValue(sheet, row, 0));
	            	TypePd=dictionariesService.findByDICTIONARIESId(TypePd);
	            	if(null!=TypePd) {
	            		ToolBasicsPd.put("FType", TypePd.getString("DICTIONARIES_ID"));
	            	}else {
	            		failNum=failNum+1;
	            		continue;
	            	}
	            }else {
            		failNum=failNum+1;
            		continue;
            	}
	            
	            ToolBasicsPd.put("Tool_Name", ObjectExcelRead.getCellValue(sheet, row, 1));//名称
	            ToolBasicsPd.put("FSpectification", ObjectExcelRead.getCellValue(sheet, row, 2));//规格
	            ToolBasicsPd.put("FPrice", isNumeric(ObjectExcelRead.getCellValue(sheet, row, 3))?
	            		Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 3).toString()):0);//价格
	            ToolBasicsPd.put("FActive_Date", ObjectExcelRead.getCellValue(sheet, row, 4));//启用日期
	            ToolBasicsPd.put("FNext_Inspect_Date", ObjectExcelRead.getCellValue(sheet, row, 5));//下次检验日期
	            ToolBasicsPd.put("FStore_Keeper", ObjectExcelRead.getCellValue(sheet, row, 6));//库管员
	            ToolBasicsPd.put("FReservedOne", ObjectExcelRead.getCellValue(sheet, row, 7));//所在部门
	            ToolBasicsPd.put("FDESCRIBE", ObjectExcelRead.getCellValue(sheet, row, 8));//备注
	            ToolBasicsPd.put("FSTATE", "闲置");//任务状态
	            ToolBasicsPd.put("FCreatePerson", Jurisdiction.getName());//创建人
	            ToolBasicsPd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
	            
	           
	            PageData pgData = new PageData();
	    		pgData.put("CODINGRULESTYPE", "TOOL");//编码类型
	    		List<PageData> dataByCodingRulesType = codingrulesService.getDataByCodingRulesType(pgData);
	    		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
	    			throw new RuntimeException("获取规则号失败，该类型数据不存在");
	    		}
	    		String returnCode = "";
	    		String CODINGRULEID = "";
	    		String ACQUISITIONTIME = "";
	    		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		for (PageData pageData : dataByCodingRulesType) {
	    			// 根据详情类型 生成对应的编号
	    			CODINGRULEID = pageData.getString("CODINGRULES_ID");
	    			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
	    			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
	    			String DETAILTYPE = pageData.getString("DETAILTYPE");
	    			String FLENGTH = pageData.getString("FLENGTH");
	    			String FFORMAT = pageData.getString("FFORMAT");
	    			String TSTEP = pageData.getString("TSTEP");
	    			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
	    			String RESETPERIOD = pageData.getString("RESETPERIOD");
	    			// 进制问题需要确定
	    			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
	    			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");
	    			// 判断是否过期
	    			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
	    			Date parse = null;
	    			parse = sdFormat.parse(termDateTime);
	    			long time = parse.getTime();
	    			long currentTimeMillis = new Date().getTime();
	    			if (time < currentTimeMillis) {
	    				throw new RuntimeException("该编码规则已过期");
	    			}
	    			// 常量类型直接返回当初的设置值
	    			if ("constant".equals(DETAILTYPE)) {
	    				returnCode += SETTINGVALUE;
	    			}
	    			// 日期类型直接返回当初的设置格式后 格式化时间返回
	    			if ("date".equals(DETAILTYPE)) {
	    				String code = "";
	    				String acqNow = sdFormat.format(new Date());
	    				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
	    				try {
	    					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				if ("everyday".equals(RESETPERIOD)) {
	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	    						code = sdf.format(new Date());
	    					}
	    				}
	    				returnCode += code;
	    			}
	    			// 根据规则类型生成流水号
	    			if ("serial".equals(DETAILTYPE)) {
	    				String acqNow = sdFormat.format(new Date());
	    				// 如果每天重置，日期变更 重新设置位1开始
	    				if ("everyday".equals(RESETPERIOD)) {
	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	    						SETTINGVALUE = "0";
	    					}
	    				}
	    				// 如果不重置，则接着前一天的时间继续编号
	    				else {
	    					acqNow = ACQUISITIONTIME;
	    				}
	    				// 自增 步长
	    				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
	    				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

	    				// 回写数据
	    				PageData pgRuleDetail = new PageData();
	    				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
	    				PageData findDetailById = codingRulesDetailService.findById(pgRuleDetail);
	    				findDetailById.put("SETTINGVALUE", formatNum);
	    				codingRulesDetailService.edit(findDetailById);
	    				// 返回結果
	    				returnCode += formatNum;
	    			}
	    		}
	    		// 更新数据
	    		PageData pgRule = new PageData();
	    		pgRule.put("CODINGRULES_ID", CODINGRULEID);
	    		PageData findById = codingrulesService.findById(pgRule);
	    		findById.put("GETVALUE", returnCode);
	    		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
	    		codingrulesService.edit(findById);
	    		ToolBasicsPd.put("Tool_Number", returnCode);	//需求编号
	    		TOOL_BASICSService.save(ToolBasicsPd);
	            succeedNum=succeedNum+1;
	        }
		}
		map.put("succeedNum", succeedNum+"");				//返回成功数量结果
		map.put("failNum", failNum+"");				//返回失败数量结果
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    if(str.indexOf(".")>0){//判断是否有小数点
	        if(str.indexOf(".")==str.lastIndexOf(".") && str.split("\\.").length==2){ //判断是否只有一个小数点
	            return pattern.matcher(str.replace(".","")).matches();
	        }else {
	            return false;
	        }
	    }else {
	        return pattern.matcher(str).matches();
	    }
	}                                                     
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "ToolBasics.xlsx", "ToolBasics.xlsx");
	}
	
	private String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);
	}
}
