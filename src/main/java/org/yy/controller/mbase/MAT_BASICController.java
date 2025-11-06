package org.yy.controller.mbase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
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

import com.alibaba.druid.util.Base64;
import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_ANNEXService;
import org.yy.service.mbase.MAT_AUXILIARYService;
import org.yy.service.mbase.MAT_BARCODEService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_CLASSService;
import org.yy.service.mbase.MAT_DESIGNService;
import org.yy.service.mbase.MAT_EXTENDService;
import org.yy.service.mbase.MAT_LOGISTICService;
import org.yy.service.mbase.MAT_QUALITYService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.mom.Unit_InfoService;
import org.yy.service.system.DictionariesService;

/** 
 * 说明：物料基础资料
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 */
@Controller
@RequestMapping("/mat_basic")
public class MAT_BASICController extends BaseController {
	
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_ANNEXService mat_annexService;//附件
	@Autowired
	private MAT_BARCODEService mat_barcodeService;//条码
	@Autowired
	private MAT_DESIGNService mat_designService;//设计资料
	@Autowired
	private MAT_EXTENDService mat_extendService;//
	@Autowired
	private MAT_LOGISTICService mat_logisticService;//物料
	@Autowired
	private MAT_QUALITYService mat_qualityService;//质量资料
	@Autowired
	private MAT_CLASSService mat_classService;//物料种类
	@Autowired
	private MAT_SPECService MAT_SPECService;//物料规格
	@Autowired
	private StaffService staffService;//员工
	@Autowired
	private Unit_InfoService unit_infoService;//基础单位
	@Autowired
	private MAT_AUXILIARYService mat_auxiliaryService;//辅助属性
	@Autowired
	private DictionariesService dictionariesService;//数据字典
	
	/**保存
	 * 默认同时插入一条物料规格表数据，
	 * 取物料定义中的辅单位为物料规格附表中的规格单位id，
	 * 取物料定义中的规格数量为物料规格附表中的规格数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("mat_basic:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		PageData pd = new PageData();
		PageData pdLogistic = new PageData();//物流资料
		PageData pdDesign = new PageData();//设计资料
		PageData pdQuality = new PageData();//质量资料
		PageData pdSpec = new PageData();//物料规格
		pd = this.getPageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData num = new PageData();
		num = mat_basicService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("MAT_BASIC_ID", this.get32UUID());	//主键
			pd.put("FCREATOR",Jurisdiction.getName());//创建人
			pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
			mat_basicService.save(pd);
			pdLogistic.put("MAT_LOGISTIC_ID", this.get32UUID());//物流资料ID
			pdLogistic.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));//基础资料id
			pdLogistic.put("FCREATOR", Jurisdiction.getName());//创建人
			pdLogistic.put("CREATE_TIME",dateFormat.format(date));//创建时间
			mat_logisticService.save(pdLogistic);//物流资料保存
			pdDesign.put("MAT_DESIGN_ID", this.get32UUID());//设计资料ID
			pdDesign.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));//基础资料id
			pdDesign.put("FVERSIONS", "1");//设计资料版本
			pdDesign.put("FCREATOR", Jurisdiction.getName());//创建人
			pdDesign.put("CREATE_TIME",dateFormat.format(date));//创建时间
			mat_designService.save(pdDesign);//设计资料保存
			pdQuality.put("MAT_QUALITY_ID", this.get32UUID());//质量资料ID
			pdQuality.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));//基础资料id
			pdQuality.put("FCREATOR", Jurisdiction.getName());//创建人
			pdQuality.put("CREATE_TIME",dateFormat.format(date));//创建时间
			mat_qualityService.save(pdQuality);//质量资料保存
			//pd = mat_basicService.findById(pd);	//根据ID读取
			pdSpec.put("MAT_SPEC_ID", this.get32UUID());//物料规格ID
			pdSpec.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));//基础资料id
			pdSpec.put("UNIT_INFO_ID", pd.getString("MAT_AUXILIARY_UNIT"));//物料定义的辅助单位id（规格单位id）
			pdSpec.put("MAT_SPEC_QTY", new BigDecimal(pd.get("MAT_SPECS_QTY").toString()));//规格数量
			pdSpec.put("FCreatePersonID", staffid);
			pdSpec.put("FCreateTime", dateFormat.format(date));
			pdSpec.put("LastModifiedBy", staffid);
			pdSpec.put("LastModificationTime", dateFormat.format(date));
			MAT_SPECService.save(pdSpec);
		}
		map.put("pd", pd);						//返回结果
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("mat_basic:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			mat_basicService.delete(pd);
			mat_annexService.deleteBasic(pd);
			mat_barcodeService.deleteBasic(pd);
			mat_designService.deleteBasic(pd);
			mat_extendService.deleteBasic(pd);
			mat_logisticService.deleteBasic(pd);
			mat_qualityService.deleteBasic(pd);
			MAT_SPECService.deleteBasic(pd);
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
	//@RequiresPermissions("mat_basic:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData gdPd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = mat_basicService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			gdPd = mat_basicService.findById(pd);	//根据ID读取
			gdPd.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));//归档主键
			gdPd.put("FCREATOR", Jurisdiction.getName());//创建人
			gdPd.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
			//mat_basicService.saveGD(gdPd);
			//pd.put("FEXTEND1", Double.parseDouble(pd.getString("FEXTEND1").toString())+0.1);//新版本号
			mat_basicService.edit(pd);
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("mat_basic:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = mat_basicService.list(page);	//列出MAT_BASIC列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**根据物料关键字查询数据
	* @param
	* @throws Exception
	*/
	@RequestMapping(value="/getBasic")
	//@RequiresPermissions("mat_basic:list")
	@ResponseBody
	public Object getBasic() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String result = "success";
		PageData pd = new PageData();
		HttpServletRequest rt=this.getRequest();//从父类获取到request
		String keywork=rt.getParameter("query");
		if(keywork.equals(null)||keywork.equals("")){
			result="error";
			map.put("result", result);
			return map;
		}
		pd.put("inputName", keywork);
		List<PageData> varList=mat_basicService.getBasic(pd);
		ObjectMapper mapper = new ObjectMapper();  
		String listName = mapper.writeValueAsString(varList); 
		map.put("listName", listName);
		map.put("result", result);
		return map;
		}
	/**根据物料关键字查询数据
	* @param
	* @throws Exception
	*/
	@RequestMapping(value="/getBasicListAll")
	//@RequiresPermissions("mat_basic:list")
	@ResponseBody
	public Object getBasicListAll() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "success";
		PageData pd = new PageData();		
		pd= this.getPageData();
		List<PageData> varList=mat_basicService.getBasic(pd);
		
		map.put("varList", varList);
		map.put("result", result);
		return map;
		}
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("mat_basic:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = mat_basicService.findById(pd);	//根据ID读取
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
		titles.add("物料代码");		//1
		titles.add("名称");			//2
		titles.add("物料分类");		//3
		titles.add("使用状态");		//4
		titles.add("主单位");			//5
		titles.add("辅单位");			//6
		titles.add("辅助属性");		//7
		titles.add("物料属性");		//8
		titles.add("是否启用批次管理");	//9
		titles.add("是否支持唯一码");	//10
		titles.add("物料规格");		//11
		titles.add("规格数量");		//12
		titles.add("品牌");		//12
		dataMap.put("titles", titles);
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData> varOList = mat_basicService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_CODE"));	    			//1
			vpd.put("var2", varOList.get(i).getString("MAT_NAME"));	    			//2
			vpd.put("var3", varOList.get(i).getString("FCLASS"));	    			//3	
			vpd.put("var4", varOList.get(i).getString("MAT_STATE"));				//4
			vpd.put("var5", varOList.get(i).getString("MAT_MAIN_UNIT_NAME"));		//5
			vpd.put("var6", varOList.get(i).getString("MAT_AUXILIARY_UNIT_NAME"));	//6
			vpd.put("var7", varOList.get(i).getString("MAT_AUXILIARY_NAME"));	    //7
			vpd.put("var8", varOList.get(i).getString("MAT_ATTRIBUTE_NAME"));		//8
			vpd.put("var9", varOList.get(i).getString("ENABLEFBATCH"));	    		//9
			vpd.put("var10", varOList.get(i).getString("UNIQUE_CODE_WHETHER"));		//10
			vpd.put("var11", varOList.get(i).getString("MAT_SPECS"));	    		//11
			vpd.put("var12", varOList.get(i).get("MAT_SPECS_QTY").toString());	    //12
			vpd.put("var13", varOList.get(i).getString("MAT_BRAND"));	    //12
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
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
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
	            PageData basicPd = new PageData();
	            basicPd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 0));//物料代码
	            basicPd.put("MAT_NAME", ObjectExcelRead.getCellValue(sheet, row, 1));//名称
	            basicPd.put("MAT_CLASS", ObjectExcelRead.getCellValue(sheet, row, 2));//物料分类
	            basicPd.put("MAT_STATE", "启用中");//使用状态
	            basicPd.put("MAT_MAIN_UNIT", ObjectExcelRead.getCellValue(sheet, row, 3));//主单位
	            basicPd.put("MAT_AUXILIARY_UNIT", ObjectExcelRead.getCellValue(sheet, row, 3));//辅单位
	            basicPd.put("ENABLEFBATCH", "不启用");//批次管理
	            basicPd.put("MAT_AUXILIARY", ObjectExcelRead.getCellValue(sheet, row, 4));//辅助属性
	            basicPd.put("MAT_ATTRIBUTE", ObjectExcelRead.getCellValue(sheet, row, 5));//物料属性
	            basicPd.put("UNIQUE_CODE_WHETHER", "是".equals(ObjectExcelRead.getCellValue(sheet, row, 6))?
	            		ObjectExcelRead.getCellValue(sheet, row, 6):"否");//是否支持唯一码
	            basicPd.put("MAT_SPECS", ObjectExcelRead.getCellValue(sheet, row, 7));//物料规格
	            basicPd.put("MAT_SPECS_QTY", isNumeric(ObjectExcelRead.getCellValue(sheet, row, 8))?
	            		new BigDecimal(ObjectExcelRead.getCellValue(sheet, row, 8)):1);//物料规格数量
	            basicPd.put("MAT_BRAND", ObjectExcelRead.getCellValue(sheet, row, 9));//品牌
	            basicPd.put("FCREATOR",Jurisdiction.getName());//创建人
	    		basicPd.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
	            //basicPd.put("MAT_ALIAS", ObjectExcelRead.getCellValue(sheet, row, 2));//别名
	            //basicPd.put("MAT_BRAND", ObjectExcelRead.getCellValue(sheet, row, 3));//品牌
	            //basicPd.put("MAT_FULL_NAME", ObjectExcelRead.getCellValue(sheet, row, 4));//物料全名
	            //basicPd.put("FVERIFIER", "");//审核人
	            //basicPd.put("MAT_SOURCE", ObjectExcelRead.getCellValue(sheet, row, 9));//来源
	            //basicPd.put("MAT_QTY_ACCURACY", ObjectExcelRead.getCellValue(sheet, row, 10));//数量精度
	            //basicPd.put("MAT_QTY_L", ObjectExcelRead.getCellValue(sheet, row, 11));//最低存量
	            //basicPd.put("MAT_QTY_H", ObjectExcelRead.getCellValue(sheet, row, 12));//最高存量
	            //basicPd.put("SAFETY_STOCK_QTY", ObjectExcelRead.getCellValue(sheet, row, 13));//安全库存数量
	            //basicPd.put("DANGER_LEVEL", ObjectExcelRead.getCellValue(sheet, row, 15));//危险程度
	            //basicPd.put("ARCHIVE_WHETHER", ObjectExcelRead.getCellValue(sheet, row, 16));//是否封存
	            //basicPd.put("FICON", "");//图标
	            //basicPd.put("FDISCOUNT1", "");//折扣1
	            //basicPd.put("FDISCOUNT2", "");//折扣2
	            //basicPd.put("FDISCOUNT3", "");//折扣3
	            //basicPd.put("FDISCOUNT4", "");//折扣4
	            //basicPd.put("FDISCOUNT5", "");//折扣5
	            //basicPd.put("FEXTEND1", "1");//版本
	            //basicPd.put("ERP_CODE", ObjectExcelRead.getCellValue(sheet, row, 17));//ERP编码
	    		
	    		if(null!=basicPd.get("MAT_CLASS") && !"".equals(basicPd.getString("MAT_CLASS"))) {
	    			PageData pd = new PageData();
	    			pd=mat_classService.findByClassId(basicPd);
	    			if(null!=pd && pd.containsKey("MAT_CLASS_ID") && !"".equals(pd.get("MAT_CLASS_ID").toString())) {
	    				basicPd.put("MAT_CLASS_ID", pd.getString("MAT_CLASS_ID"));//物料类ID
	    			} else {
	    				basicPd.put("MAT_CLASS_ID", "2f6f5eda56134e938c6971af25ce2382");//原材料物料类ID
	    			}
	    		}
	    		if(null!=basicPd.get("MAT_MAIN_UNIT") && !"".equals(basicPd.getString("MAT_MAIN_UNIT"))) {
	    			PageData pd = new PageData();
	    			pd.put("FCODE", basicPd.getString("MAT_MAIN_UNIT"));
	    			pd=unit_infoService.findByUnitCode(pd);
	    			if(null!=pd && pd.containsKey("UNIT_INFO_ID") && !"".equals(pd.get("UNIT_INFO_ID").toString())) {
	    				basicPd.put("MAT_MAIN_UNIT", pd.getString("UNIT_INFO_ID"));//物料主单位ID
	    				basicPd.put("MAT_AUXILIARY_UNIT", pd.getString("UNIT_INFO_ID"));//物料辅单位ID
	    			} else {
	    				basicPd.put("MAT_MAIN_UNIT", "f550017108b14729ac5440666e71a575");//物料主单位ID
	    				basicPd.put("MAT_AUXILIARY_UNIT", "f550017108b14729ac5440666e71a575");//物料辅单位ID
	    			}
	    		}
	    		if(null!=basicPd.get("MAT_AUXILIARY") && !"".equals(basicPd.getString("MAT_AUXILIARY"))) {
	    			PageData pd = new PageData();
	    			pd.put("MAT_AUXILIARY_NAME", basicPd.getString("MAT_AUXILIARY"));
	    			pd=mat_auxiliaryService.findByMAT_AUXILIARY_NAME(pd);
	    			if(null!=pd && pd.containsKey("MAT_AUXILIARY_ID") && !"".equals(pd.get("MAT_AUXILIARY_ID").toString())) {
	    				basicPd.put("MAT_AUXILIARY_ID", pd.getString("MAT_AUXILIARY_ID"));//辅助属性ID
	    			} else {
	    				basicPd.put("MAT_AUXILIARY_ID", "80108fa272884119b09eab03a78d3cc5");//公用辅助属性ID
	    			}
	    		}
	    		if(null!=basicPd.get("MAT_ATTRIBUTE") && !"".equals(basicPd.getString("MAT_ATTRIBUTE"))) {
	    			PageData pd = new PageData();
	    			pd.put("NAME", basicPd.getString("MAT_ATTRIBUTE"));
	    			pd=dictionariesService.findByName(basicPd);
	    			if(null!=pd && pd.containsKey("DICTIONARIES_ID") && !"".equals(pd.get("DICTIONARIES_ID").toString())) {
	    				basicPd.put("MAT_ATTRIBUTE", pd.getString("DICTIONARIES_ID"));//物料属性ID
	    			} else {
	    				basicPd.put("MAT_ATTRIBUTE", "58db10cd919e45ecb5ce416799c6deb4");//采购物料属性ID
	    			}
	    		}
	    		PageData num = new PageData();
	    		num = mat_basicService.findCountByCode(basicPd);
	    		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
	    			//errInfo = "error";//编号重复
	    			continue;
	    		} else {
	    			basicPd.put("MAT_BASIC_ID", this.get32UUID());//基础资料ID主键
	    			mat_basicService.save(basicPd);
	    			PageData pdLogistic = new PageData();//物流资料
		    		PageData pdDesign = new PageData();//设计资料
		    		PageData pdQuality = new PageData();//质量资料
		    		PageData pdSpec = new PageData();//物料规格附表
		            pdLogistic.put("MAT_LOGISTIC_ID", this.get32UUID());//物流资料ID
		    		pdLogistic.put("MAT_BASIC_ID", basicPd.getString("MAT_BASIC_ID"));//基础资料id
		    		pdLogistic.put("FCREATOR", Jurisdiction.getName());//创建人
		    		pdLogistic.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
		    		mat_logisticService.save(pdLogistic);//物流资料保存
		    		pdDesign.put("MAT_DESIGN_ID", this.get32UUID());//设计资料ID
		    		pdDesign.put("MAT_BASIC_ID", basicPd.getString("MAT_BASIC_ID"));//基础资料id
		    		pdDesign.put("FVERSIONS", "1");//设计资料版本
		    		pdDesign.put("FCREATOR", Jurisdiction.getName());//创建人
		    		pdDesign.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
		    		mat_designService.save(pdDesign);//设计资料保存
		    		pdQuality.put("MAT_QUALITY_ID", this.get32UUID());//质量资料ID
		    		pdQuality.put("MAT_BASIC_ID", basicPd.getString("MAT_BASIC_ID"));//基础资料id
		    		pdQuality.put("FCREATOR", Jurisdiction.getName());//创建人
		    		pdQuality.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
		    		mat_qualityService.save(pdQuality);//质量资料保存
		    		pdSpec.put("MAT_SPEC_ID", this.get32UUID());//物料规格ID
		    		pdSpec.put("MAT_BASIC_ID", basicPd.getString("MAT_BASIC_ID"));//基础资料id
		    		pdSpec.put("UNIT_INFO_ID", basicPd.getString("MAT_AUXILIARY_UNIT"));//规格单位id
		    		pdSpec.put("MAT_SPEC_QTY", new BigDecimal(basicPd.get("MAT_SPECS_QTY").toString()));//规格数量
		    		pdSpec.put("LastModifiedBy", Jurisdiction.getName());//修改人
		    		pdSpec.put("LastModificationTime", Tools.date2Str(new Date()));//修改时间
		    		pdSpec.put("FCreatePersonID", Jurisdiction.getName());//创建人
		    		pdSpec.put("FCreateTime",Tools.date2Str(new Date()));//创建时间
		    		MAT_SPECService.save(pdSpec);//物料规格保存
	            }
	        }
		}
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
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "MATBASIC.xlsx", "MATBASIC.xlsx");
	}
	/**获取物料列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getMaterialList")
	@ResponseBody
	public Object getMaterialList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-物料名/物料编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = mat_basicService.getMaterialList(pd);	//列出Customer列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	/**获取物料详情-销售订单
	 * @author 管悦
	 * @date 2020-11-06
	 * @param MAT_BASIC_ID:物料ID
	 * @throws Exception
	 */
	@RequestMapping(value="/getMaterialMessage")
	@ResponseBody
	public Object getMaterialMessage() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdMat= mat_basicService.getMaterialMessage(pd);
		map.put("pd", pdMat);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 生成二维码 输入text 返回base64
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/generateQRCodeImage")
	@ResponseBody
	public Object generateQRCodeImage() throws Exception{
		String text = this.getPageData().getString("text");
		Integer width = 300;
		Integer height = 300;
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		HashMap hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, "2");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        Map<String, Object> map = Maps.newHashMap();
        map.put("text", text);
        map.put("encode", Base64Utils.encodeToString(outputStream.toByteArray()) );
        outputStream.close();
        return map;
	}
	/**车间库存物料列表
	 * @author s
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getWorkShopStockList")
	@ResponseBody
	public Object getWorkShopStockList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = mat_basicService.getWorkShopStockList(pd);	//车间库存物料列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
