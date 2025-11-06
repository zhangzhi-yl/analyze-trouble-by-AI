package org.yy.controller.pm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.pm.PM_MAINTAIN_CONSUMEService;

/** 
 * 说明：设备备件物料消耗明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PM_MAINTAIN_CONSUME")
public class PM_MAINTAIN_CONSUMEController extends BaseController {
	
	@Autowired
	private PM_MAINTAIN_CONSUMEService PM_MAINTAIN_CONSUMEService;
	@Autowired
	private MAT_BASICService mat_basicService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAIN_CONSUME_ID", this.get32UUID());	//主键
		PM_MAINTAIN_CONSUMEService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PM_MAINTAIN_CONSUMEService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PM_MAINTAIN_CONSUMEService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = PM_MAINTAIN_CONSUMEService.list(page);	//列出PM_MAINTAIN_CONSUME列表
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
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PM_MAINTAIN_CONSUMEService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("PM_MAINTAIN_CONSUME:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PM_MAINTAIN_CONSUMEService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID
			) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData basicPd = new PageData();
		PageData WLPd = new PageData();
		int succeedNum=0;
		int failNum=0;
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
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
	            try {
	            	if(null!=ObjectExcelRead.getCellValue(sheet, row, 2)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 2))) {//判断物料代码字符是否为空
	            		WLPd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 2));
		 	            WLPd=mat_basicService.getBasicId(WLPd);
		 	            String reg="^\\d+$";
		 	            if(null!=WLPd) {
		 	            	basicPd.put("PM_MAINTAIN_CONSUME_ID", this.get32UUID());//备件物料消耗明细ID主键
		 	 	            basicPd.put("PM_MAINTAIN_ID", PM_MAINTAIN_ID);//保养主表ID主键
		 	 	          basicPd.put("IF_CREATE_PICKING", "否");//是否生成领料单
		 	 	            basicPd.put("SOURCE", ObjectExcelRead.getCellValue(sheet, row, 1));//来源
		 	 	            basicPd.put("MAT_BASIC_ID", WLPd.getString("MAT_BASIC_ID"));//物料ID
		 	 	            if(ObjectExcelRead.getCellValue(sheet, row, 3).matches(reg)==true) {//判断表格申请数量值是否为数字，不为数字填写0
		 	 	            	basicPd.put("APPLYFOR_NUM", ObjectExcelRead.getCellValue(sheet, row, 3));//申请数量
		 	 	            }else {
		 	 	            	basicPd.put("APPLYFOR_NUM", 0);//申请数量
		 	 	            }
		 	 	            if(ObjectExcelRead.getCellValue(sheet, row, 4).matches(reg)==true) {//判断表格实际消耗数量值是否为数字，不为数字填写0
		 	 	            	basicPd.put("CONSUME_NUM", ObjectExcelRead.getCellValue(sheet, row, 4));//实际消耗数量
		 	 	            }else {
		 	 	            	basicPd.put("CONSUME_NUM", 0);//实际消耗数量
		 	 	            }
		 	 	           PM_MAINTAIN_CONSUMEService.save(basicPd);
		 	 	          succeedNum=succeedNum+1;
		 	            }else {
		 	            	failNum=failNum+1;
		 	            }
	            	}else {
	            		failNum=failNum+1;
	            	}
		        } catch (IOException e) {
		            e.printStackTrace();
		            errInfo = "fail";
		        }
	           
	        }
		}
		map.put("succeedNum", succeedNum+"");				//返回成功数量结果
		map.put("failNum", failNum+"");				//返回失败数量结果
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "PM_MAINTAIN_CONSUME.xlsx", "PM_MAINTAIN_CONSUME.xlsx");
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
		titles.add("设备保养计划ID");	//1
		titles.add("是否生成领料单");	//2
		titles.add("来源");	//3
		titles.add("物料ID");	//4
		titles.add("申请数量");	//5
		titles.add("实际消耗数量");	//6
		titles.add("预留1");	//7
		titles.add("预留2");	//8
		titles.add("预留3");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = PM_MAINTAIN_CONSUMEService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PM_MAINTAIN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("IF_CREATE_PICKING"));	    //2
			vpd.put("var3", varOList.get(i).getString("SOURCE"));	    //3
			vpd.put("var4", varOList.get(i).getString("MAT_BASIC_ID"));	    //4
			vpd.put("var5", varOList.get(i).get("APPLYFOR_NUM").toString());	//5
			vpd.put("var6", varOList.get(i).get("CONSUME_NUM").toString());	//6
			vpd.put("var7", varOList.get(i).getString("RESERVE_ONE"));	    //7
			vpd.put("var8", varOList.get(i).getString("RESERVE_TWO"));	    //8
			vpd.put("var9", varOList.get(i).getString("RESERVE_THREE"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
