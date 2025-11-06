package org.yy.controller.mdm;

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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTMxService;

/** 
 * 说明：设备配件采购(明细)
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/accessories_procurementmx")
public class ACCESSORIES_PROCUREMENTMxController extends BaseController {
	
	@Autowired
	private ACCESSORIES_PROCUREMENTMxService accessories_procurementmxService;
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "accessories.xlsx", "accessories.xlsx");
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	//@RequiresPermissions("fromExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file,
			@RequestParam(value="ACCESSORIES_PROCUREMENT_ID",required=false) String ACCESSORIES_PROCUREMENT_ID) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
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
		            if(ObjectExcelRead.isBlankRow(row)) {//空行跳过
		            	break;
		            }
		            if(row.getRowNum() == -1) {
		                continue;
		            }else {
		                if(row.getRowNum() == 0) {//第一行表头跳过
		                    continue;
		                }
		            }
		            pd.put("ACCESSORIES_PROCUREMENTMX_ID", this.get32UUID());
		            pd.put("ACCESSORIES_PROCUREMENT_ID", ACCESSORIES_PROCUREMENT_ID);
		            pd.put("LINE_NO", rowNum);
		            pd.put("PURCHASE_REQUISITION_ID", "采购计划ID"+rowNum);
		            pd.put("RELATION_BILL_NO", "关联单号"+rowNum);
		            pd.put("RELATION_ID", "关联ID"+rowNum);
		            pd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 0));
		            pd.put("MAT_NAME", ObjectExcelRead.getCellValue(sheet, row, 1));
		            pd.put("FSPECS", ObjectExcelRead.getCellValue(sheet, row, 2));
		            pd.put("FUNIT", ObjectExcelRead.getCellValue(sheet, row, 3));
		            pd.put("TARGET_QTY", "".equals(ObjectExcelRead.getCellValue(sheet, row, 4))?0:Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 4)));
		            pd.put("ACTUAL_QTY", "".equals(ObjectExcelRead.getCellValue(sheet, row, 5))?0:Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 5)));
		            pd.put("COST_PRICE", "".equals(ObjectExcelRead.getCellValue(sheet, row, 6))?0:Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 6)));
		            pd.put("LINE_STATE", "创建");
		            pd.put("LINE_CLOSE_WHETHER", "NO");
		            pd.put("FCREATER", Jurisdiction.getName());
		            pd.put("CREATE_TIME", Tools.date2Str(new Date()));
		            pd.put("FREMARK", "");
		            accessories_procurementmxService.save(pd);
		        }
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
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
		pd.put("ACCESSORIES_PROCUREMENTMX_ID", this.get32UUID());	//设备配件采购明细ID主键
		pd.put("FCREATER", Jurisdiction.getName());	//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		accessories_procurementmxService.save(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		accessories_procurementmxService.delete(pd);
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
		accessories_procurementmxService.edit(pd);
		map.put("result", errInfo);				//返回结果
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
		List<PageData>	varList = accessories_procurementmxService.list(page);	//列出ACCESSORIES_PROCUREMENTMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
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
		pd = accessories_procurementmxService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
			accessories_procurementmxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备配件采购明细ID");	//1
		titles.add("设备配件采购ID");	//2
		titles.add("行号");	//3
		titles.add("采购计划ID");	//4
		titles.add("关联单号");	//5
		titles.add("关联ID");	//6
		titles.add("物料编码");	//7
		titles.add("物料名称");	//8
		titles.add("规格");	//9
		titles.add("单位");	//10
		titles.add("目标数量");	//11
		titles.add("实际采购数量");	//12
		titles.add("成本单价");	//13
		titles.add("行状态");	//14
		titles.add("是否行关闭");	//15
		titles.add("创建人");	//16
		titles.add("创建时间");	//17
		titles.add("备注");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = accessories_procurementmxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ACCESSORIES_PROCUREMENTMX_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("ACCESSORIES_PROCUREMENT_ID"));	    //2
			vpd.put("var3", varOList.get(i).get("LINE_NO").toString());	//3
			vpd.put("var4", varOList.get(i).getString("PURCHASE_REQUISITION_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("RELATION_BILL_NO"));	    //5
			vpd.put("var6", varOList.get(i).getString("RELATION_ID"));	    //6
			vpd.put("var7", varOList.get(i).getString("MAT_CODE"));	    //7
			vpd.put("var8", varOList.get(i).getString("MAT_NAME"));	    //8
			vpd.put("var9", varOList.get(i).getString("FSPECS"));	    //9
			vpd.put("var10", varOList.get(i).getString("FUNIT"));	    //10
			vpd.put("var11", varOList.get(i).get("TARGET_QTY").toString());	//11
			vpd.put("var12", varOList.get(i).get("ACTUAL_QTY").toString());	//12
			vpd.put("var13", varOList.get(i).get("COST_PRICE").toString());	//13
			vpd.put("var14", varOList.get(i).getString("LINE_STATE"));	    //14
			vpd.put("var15", varOList.get(i).getString("LINE_CLOSE_WHETHER"));	    //15
			vpd.put("var16", varOList.get(i).getString("FCREATER"));	    //16
			vpd.put("var17", varOList.get(i).getString("CREATE_TIME"));	    //17
			vpd.put("var18", varOList.get(i).getString("FREMARK"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
