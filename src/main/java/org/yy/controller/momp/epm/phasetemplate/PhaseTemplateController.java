package org.yy.controller.momp.epm.phasetemplate;

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
import org.apache.poi.ss.util.CellRangeAddress;
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
import org.yy.service.momp.epm.activitytemplate.ActivityTemplateService;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;


/** 
 * 说明：计划模板
 * 作者：YuanYe
 * 时间：2020-03-12
 * 
 */
@Controller
@RequestMapping("/phasetemplate")
public class PhaseTemplateController extends BaseController {
	
	@Autowired
	private PhaseTemplateService phasetemplateService;
	@Autowired
	private ActivityTemplateService activitytemplateService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("phasetemplatecontroller:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PHASETEMPLATE_ID", this.get32UUID());	//主键
		phasetemplateService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/templateExcel")
	public void templateExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "plantemplate.xlsx", "plantemplate.xlsx");
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("phasetemplatecontroller:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		phasetemplateService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("phasetemplatecontroller:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		phasetemplateService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	//从excel导入
		@RequestMapping(value="/readExcel")
		@ResponseBody
		public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
			Map<String,String> map = new HashMap<String,String>();
			String errInfo = "success";
			PageData Pd = new PageData();
			Pd=this.getPageData();
			String PLANTEMPLATE_ID=Pd.getString("PLANTEMPLATE_ID");
			PageData jdPd = new PageData();//阶段数据
			PageData hdPd = new PageData();//活动数据
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
			        //总行数  
					 int count = sheet.getLastRowNum()+1;
			            for(int i=0;i<count;i++){
			            		String title="";
			            		Row rowry = sheet.getRow(i);
			            		title=ObjectExcelRead.getCellValue(sheet, rowry, 0);
			            		if(title.equals("阶段")){
			            			Row rowjd = sheet.getRow(i+2);
			            			title=ObjectExcelRead.getCellValue(sheet, rowjd, 0);
			            			//System.out.println("阶段:"+title);
			            			jdPd.put("PLANTEMPLATE_ID", PLANTEMPLATE_ID);
				            		jdPd.put("PHASETEMPLATE_ID", this.get32UUID());
				            		jdPd.put("FHTNAME",ObjectExcelRead.getCellValue(sheet, rowjd, 0));
				            		jdPd.put("FHTDESCRIPTION", ObjectExcelRead.getCellValue(sheet, rowjd, 1));
				            		jdPd.put("FHTLEVEL", ObjectExcelRead.getCellValue(sheet, rowjd, 2).equals("")?"0":(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, rowjd, 2))));
				            		jdPd.put("FHTREMARK", ObjectExcelRead.getCellValue(sheet, rowjd, 3));
				            		phasetemplateService.save(jdPd);
			            		}else if(title.equals("活动")){
			            			for(int j=i;j<count;j++){
			            				Row rowhd = sheet.getRow(j);
				            			title=ObjectExcelRead.getCellValue(sheet, rowhd, 0);
				            			if(title.equals("阶段")){
				            				break;
				            			}else{
				            				if(title.equals("活动")||title.equals("活动模板名称")){
				            						
				            				}else{
				            					String PHASETEMPLATE_ID=jdPd.getString("PHASETEMPLATE_ID");
				            					hdPd.put("ACTIVITYTEMPLATE_ID", this.get32UUID());
				            					hdPd.put("PHASETEMPLATE_ID", PHASETEMPLATE_ID);
				            					hdPd.put("PLANTEMPLATE_ID", PLANTEMPLATE_ID);
				            					hdPd.put("FATNAME",ObjectExcelRead.getCellValue(sheet, rowhd, 0));
				            					hdPd.put("FATDESCRIPTION", ObjectExcelRead.getCellValue(sheet, rowhd, 1));
				            					hdPd.put("FATLEVEL", ObjectExcelRead.getCellValue(sheet, rowhd, 2).equals("")?"0":(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, rowhd, 2))));
				            					hdPd.put("FATREMARK", ObjectExcelRead.getCellValue(sheet, rowhd, 3));
				            					//System.out.println("活动:"+title);
				            					activitytemplateService.save(hdPd);
				            				}
				            			}
			            			}
			            		}
//			            			
			            }
			}
			map.put("result", errInfo);				//返回结果
			return map;
		}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("phasetemplatecontroller:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		page.setShowCount(1000);
		List<PageData>	varList = phasetemplateService.list(page);	//列出PhaseTemplateController列表
		if(varList.size()>0){
			for (int i=0;i<varList.size();i++) {
				PageData pd1=varList.get(i);
				List<PageData>	varList1=activitytemplateService.listByParentId(pd1);
				varList.get(i).put("varList1", varList1);
			}
		}
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
	//@RequiresPermissions("phasetemplatecontroller:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = phasetemplateService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	@RequestMapping(value="/findByIdTwo")
	@ResponseBody
	public Object findByIdTwo() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>  newPd = phasetemplateService.findByIdTwo(pd);
		 try{
			 errInfo = "200";
	            map.put("list", newPd);   
	            map.put("msg", "ok");
	            map.put("msgText","查询成功！");
	        }catch (Exception e){
	        	errInfo = "500";
	            map.put("msg","no");
	            map.put("msgText","未知错误，请联系管理员！");
	        }finally{
	            map.put("result", errInfo);
	        }
		return map;
	}
	
	@RequestMapping(value="/findById")
	@ResponseBody
	public Object findById() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData  newPd = phasetemplateService.findById(pd);
		 try{
			 errInfo = "200";
	            map.put("pd", newPd);   
	            map.put("msg", "ok");
	            map.put("msgText","查询成功！");
	        }catch (Exception e){
	        	errInfo = "500";
	            map.put("msg","no");
	            map.put("msgText","未知错误，请联系管理员！");
	        }finally{
	            map.put("result", errInfo);
	        }
		return map;
	}
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("phasetemplatecontroller:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			phasetemplateService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注1");	//1
		titles.add("备注2");	//2
		titles.add("备注3");	//3
		titles.add("备注4");	//4
		titles.add("备注5");	//5
		titles.add("备注6");	//6
		titles.add("备注7");	//7
		titles.add("备注8");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = phasetemplateService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PHASETEMPLATE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FHTNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FHTDESCRIPTION"));	    //3
			vpd.put("var4", varOList.get(i).getString("FHTLEVEL"));	    //4
			vpd.put("var5", varOList.get(i).getString("FHTREMARK"));	    //5
			vpd.put("var6", varOList.get(i).getString("PLANTEMPLATE_ID"));	    //6
			vpd.put("var7", varOList.get(i).getString("FRF1"));	    //7
			vpd.put("var8", varOList.get(i).getString("FRF2"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
