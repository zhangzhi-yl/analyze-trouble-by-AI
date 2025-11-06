package org.yy.controller.mom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.yy.util.FileDownload;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_StorageAreaService;
import org.yy.service.mom.WH_WareHouseService;

/** 
 * 说明：仓库管理
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 */
@Controller
@RequestMapping("/warehouse")
public class WH_WareHouseController extends BaseController {
	
	@Autowired
	private WH_WareHouseService wh_warehouseService;
	
	@Autowired
	private WH_StorageAreaService wh_storageareaService;
	
	@Autowired
	private WH_LocationService wh_locationService;
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "warehouse.xlsx", "warehouse.xlsx");
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	//@RequiresPermissions("fromExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData warehousepd = new PageData();
		PageData storageareapd = new PageData();
		PageData locationpd = new PageData();
		if (null != file && !file.isEmpty()) {

	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        for(int sheetIndex=0;sheetIndex<2;sheetIndex++) {
		        //得到sheet
		        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, sheetIndex);
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
		            if(sheetIndex==0) {
			            warehousepd.put("WH_WAREHOUSE_ID", this.get32UUID());
			            warehousepd.put("FCODE", ObjectExcelRead.getCellValue(sheet, row, 0));
			            warehousepd.put("FNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
			            warehousepd.put("FAREA", "".equals(ObjectExcelRead.getCellValue(sheet, row, 2))?0:Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 2)));
			            warehousepd.put("FADDRESS", ObjectExcelRead.getCellValue(sheet, row, 3));
			            warehousepd.put("FADMIN", ObjectExcelRead.getCellValue(sheet, row, 4));
			            warehousepd.put("FPHONE", ObjectExcelRead.getCellValue(sheet, row, 5));
			            warehousepd.put("PROHIBIT_WHETHER", "NO");
			            warehousepd.put("FTYPE", "仓库");
			            warehousepd.put("IsQuality", "NO");
			            warehousepd.put("IsCapacity", "NO");
			            warehousepd.put("QRcode", ObjectExcelRead.getCellValue(sheet, row, 0));
			            warehousepd.put("Fremarks", "");
			            warehousepd.put("WorkShop", "");
			            wh_warehouseService.save(warehousepd);
		            }else if(sheetIndex==1){
		            	Page page=new Page();
		            	PageData pd = new PageData();
		            	pd.put("FCODE", ObjectExcelRead.getCellValue(sheet, row, 4));
		            	page.setPd(pd);
		            	List<PageData> warehouselist = wh_warehouseService.list(page);
		            	locationpd.put("WH_LOCATION_ID", this.get32UUID());
		            	locationpd.put("FCODE", ObjectExcelRead.getCellValue(sheet, row, 0));
		            	locationpd.put("FNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
		            	locationpd.put("OUTGOING_RULES", ObjectExcelRead.getCellValue(sheet, row, 2));
		            	locationpd.put("MAXIMUM_WEIGHT", "".equals(ObjectExcelRead.getCellValue(sheet, row, 3))?0:Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 3)));
		            	locationpd.put("FSTATE", "空闲");
		            	locationpd.put("FDES", "");
		            	locationpd.put("PROHIBIT_WHETHER", "NO");
		            	locationpd.put("WH_STORAGEAREA_ID",  warehouselist.get(0).get("WH_WAREHOUSE_ID"));
			            wh_locationService.save(locationpd);
		            }
		        }
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
	//@RequiresPermissions("wh_warehouse:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_warehouseService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("WH_WAREHOUSE_ID", this.get32UUID());	//主键,仓库ID
			wh_warehouseService.save(pd);
		}
		pd = wh_warehouseService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("wh_warehouse:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			pd.put("WH_STORAGEAREA_ID", pd.getString("WH_WAREHOUSE_ID"));
			if(Integer.parseInt(wh_locationService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				wh_warehouseService.delete(pd);
			}
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
	//@RequiresPermissions("wh_warehouse:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_warehouseService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			wh_warehouseService.edit(pd);
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("wh_warehouse:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键字检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FCODE = pd.getString("FCODE");						//代码检索条件
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		String FNAME = pd.getString("FNAME");						//名称检索条件
		if(Tools.notEmpty(FNAME))pd.put("FNAME", FNAME.trim());
		String PROHIBIT_WHETHER = pd.getString("PROHIBIT_WHETHER");						//仓库是否禁用条件
		if(Tools.notEmpty(PROHIBIT_WHETHER))pd.put("PROHIBIT_WHETHER", PROHIBIT_WHETHER.trim());
		page.setPd(pd);
		List<PageData> varList = wh_warehouseService.list(page);	//列出WH_WAREHOUSE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("wh_warehouse:listAll")
	@ResponseBody
	public Object listAll(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wh_warehouseService.listAll(pd);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**仓库列表（下拉选用）
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	@RequestMapping(value="/wareHouseList")
	//@RequiresPermissions("wh_warehouse:list")
	@ResponseBody
	public Object wareHouseList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wh_warehouseService.wareHouseList(pd);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 通过FCODE获取数据查询数量
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findByCode")
//	@RequiresPermissions("wh_warehouse:list")
	@ResponseBody
	public Object findByFCODE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		page.setPd(pd);
		List<PageData>	varList = wh_warehouseService.findByFCODE(page);	//列出列表
		map.put("num", varList.size());
		//map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("wh_warehouse:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = wh_warehouseService.findById(pd);	//根据ID读取
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
		titles.add("仓库ID");	//1
		titles.add("代码");	//2
		titles.add("名称");	//3
		titles.add("面积");	//4
		titles.add("仓库管理员");	//5
		titles.add("仓库地址");	//6
		titles.add("联系电话");	//7
		titles.add("是否禁用");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = wh_warehouseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WH_WAREHOUSE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).get("FAREA").toString());	//4
			vpd.put("var5", varOList.get(i).getString("FADMIN"));	    //5
			vpd.put("var6", varOList.get(i).getString("FADDRESS"));	    //6
			vpd.put("var7", varOList.get(i).getString("FPHONE"));	    //7
			vpd.put("var8", varOList.get(i).getString("PROHIBIT_WHETHER"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**获取仓库列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-16
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getWarehouseList")
	@ResponseBody
	public Object getWarehouseList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-仓库名/仓库编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = wh_warehouseService.getWarehouseList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
