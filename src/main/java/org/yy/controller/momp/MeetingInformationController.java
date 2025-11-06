package org.yy.controller.momp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.yy.service.momp.MeetingAgendaService;
import org.yy.service.momp.MeetingDiscussService;
import org.yy.service.momp.MeetingGattendeeService;
import org.yy.service.momp.MeetingInformationService;
import org.yy.service.momp.MeetingItemService;

/** 
 * 说明：项目计划表从表
 * 作者：YuanYe
 * 时间：2020-04-13
 * 
 */
@Controller
@RequestMapping("/meetinginformation")
public class MeetingInformationController extends BaseController {
	
	@Autowired
	private MeetingInformationService meetinginformationService;
	@Autowired
	private MeetingAgendaService meetingagendaService;
	@Autowired
	private MeetingDiscussService meetingdiscussService;
	@Autowired
	private MeetingGattendeeService meetinggattendeeService;
	@Autowired
	private MeetingItemService meetingitemService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("meetinginformation:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MEETINGINFORMATION_ID", this.get32UUID());	//主键
		meetinginformationService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("meetinginformation:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		meetinginformationService.delete(pd);
		meetingagendaService.deleteFather(pd);
		meetingdiscussService.deleteFather(pd);
		meetinggattendeeService.deleteFather(pd);
		meetingitemService.deleteFather(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("meetinginformation:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		meetinginformationService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("meetinginformation:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = meetinginformationService.list(page);	//列出MeetingInformation列表
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
	//@RequiresPermissions("meetinginformation:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = meetinginformationService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("meetinginformation:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			meetinginformationService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/templateExcel")
	public void templateExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "meetingmodel.xlsx", "meetingmodel.xlsx");
	}
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	//@RequiresPermissions("toExcel")
	@ResponseBody
	public Object exportExcel() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		HttpServletRequest rt=this.getRequest();
		PageData pd=this.getPageData();
		PageData cpd=new PageData();
		List<PageData>	varList=new ArrayList<PageData>();
		List<PageData>	nvarList=new ArrayList<PageData>();
		List<PageData>	tvarList=new ArrayList<PageData>();
		List<PageData>	ivarList=new ArrayList<PageData>();
		int recordNums=0;
		int recordNum=0;
		int recordNumt=0;
		int recordNumi=0;
		try {
			cpd=meetinginformationService.findById(pd);
			//获取明细数据
			  nvarList =meetingagendaService.findByFatherId(pd);//会议议程
			  recordNum=nvarList.size();
			///获取明细数据
			varList = meetingdiscussService.findByFatherId(pd);//会议讨论
			recordNums=varList.size();
			tvarList= meetinggattendeeService.findByFatherId(pd);//参会人员
			recordNumt=tvarList.size();
			ivarList= meetingitemService.findByFatherId(pd);//
			recordNumi=ivarList.size();
		} catch (Exception e) {
			// TODO:获取数据异常
			map.put("result", "erro");
		}
		 if(recordNums<1)
		    {
		    	//没有明细
		    	map.put("result", "withoutrecord");
		    }
		    if(recordNum<1)
		    {
		    	//没有明细
		    	map.put("result", "withoutrecord");
		    }
		    if(recordNumt<1)
		    {
		    	//没有明细
		    	map.put("result", "withoutrecord");
		    }
		    if(recordNumi<1)
		    {
		    	//没有明细
		    	map.put("result", "withoutrecord");
		    }
		    String realpath = PathUtil.getProjectpath() + Const.FILEPATHFILE;
		    String filepath=realpath+"\\"+"\\meetingmodeldc.xlsx";///打印模板的物理路径
			String newfilename="MEET"+Tools.date2Str(new Date(),"yyyyMMddHHmmss");
			String newfilepath=realpath+"\\"+"\\"+newfilename +".xlsx";
			try {
				//if(recordNums>0||recordNum>0||recordNumt>0){
					PurchaseOrderReadExcel.copyExcelCQUOTemplate2(filepath, newfilepath, recordNums,recordNum,recordNumt,recordNumi,varList,nvarList,tvarList,ivarList);	
				//}
			} catch (Exception e) {
				map.put("result", "copyfailed");
			}
			Map<String, String> maprep=new HashMap<String, String>();
			try {
				//填充数据
				maprep.put("THEME", cpd.getString("THEME"));//会议主题
				maprep.put("BEGINDAY", cpd.getString("BEGINDAY"));//会议开始日期
				maprep.put("ENDDAY", cpd.getString("ENDDAY"));//会议结束日期
				maprep.put("LOCATION", cpd.getString("LOCATION"));//会议地点
				maprep.put("FCTIME", cpd.getString("FCTIME"));//时间（开始-结束）
				maprep.put("TELNUMBER", cpd.getString("TELNUMBER"));//电话会议号
				maprep.put("ORIGINATOR", cpd.getString("ORIGINATOR"));//会议发起人
				maprep.put("INTERNETINVITATION", cpd.getString("INTERNETINVITATION"));//网络邀请
				maprep.put("COMPILINGPERSON", cpd.getString("COMPILINGPERSON"));//编制人
				maprep.put("CHECKER", cpd.getString("CHECKER"));//审核人
				maprep.put("TARGET", cpd.getString("TARGET"));//会议目标
			} catch (Exception e) {
				map.put("result", "insertfailed");
			}
			//插入数据
			try {
				PurchaseOrderReadExcel.insertcquotationData1(newfilepath, maprep, recordNums, 1);
			} catch (Exception e) {
				// TODO: handle exception
				map.put("result", "insertfailed");
			}
			map.put("result", errInfo);
			map.put("newfilename", newfilename);
			return map;
			
	}
	//下载导出的文件
	@RequestMapping(value="/uploadExcel")
	public void uploadExcel(HttpServletResponse response)throws Exception{
		PageData pd=this.getPageData();
		String newfilename=pd.getString("newfilename");
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + newfilename +".xlsx", newfilename +".xlsx");
	}
	//从excel导入
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData Pd = new PageData();
		Pd=this.getPageData();
		String PLANINSTANCE_ID=Pd.getString("PLANINSTANCE_ID");
		PageData excelPd = new PageData();//主表数据
		String MEETINGINFORMATION_ID=this.get32UUID();
		PageData ryPd = new PageData();//人员数据
		PageData ycPd = new PageData();//议程数据
		PageData tlPd = new PageData();//讨论数据
		PageData dbPd = new PageData();//待办数据
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
				 //sheet中所有合并的单元格信息
		        List<CellRangeAddress> cras = getCombineCell(sheet);
		        	realRowCount = sheet.getPhysicalNumberOfRows();
		        	Row row = sheet.getRow(1);
		        	Row row2 = sheet.getRow(2);
		        	Row row3 = sheet.getRow(3);
		        	Row row4 = sheet.getRow(4);
		        	Row row5 = sheet.getRow(5);
		        	Row row6 = sheet.getRow(6);
		        	Row row7 = sheet.getRow(7);
		        	excelPd.put("PHASETEMPLATE_ID", PLANINSTANCE_ID);
		            excelPd.put("MEETINGINFORMATION_ID", MEETINGINFORMATION_ID);	//主键
		            excelPd.put("THEME", ObjectExcelRead.getCellValue(sheet, row, 1));//会议主题
		            excelPd.put("BEGINDAY", ObjectExcelRead.getCellValue(sheet, row2, 1));//会议开始日期
		            excelPd.put("ENDDAY", ObjectExcelRead.getCellValue(sheet, row2, 3));//会议结束日期
		            excelPd.put("LOCATION", ObjectExcelRead.getCellValue(sheet, row3, 1));//会议地点
		            excelPd.put("FCTIME", ObjectExcelRead.getCellValue(sheet, row4, 1));//会议时间
		            excelPd.put("TELNUMBER", ObjectExcelRead.getCellValue(sheet, row4, 3));//电话会议号
		            excelPd.put("ORIGINATOR", ObjectExcelRead.getCellValue(sheet, row5, 1));//会议发起人
		            excelPd.put("INTERNETINVITATION", ObjectExcelRead.getCellValue(sheet, row5, 3));//网络邀请
		            excelPd.put("COMPILINGPERSON", ObjectExcelRead.getCellValue(sheet, row6, 1));//编制人
		            excelPd.put("CHECKER", ObjectExcelRead.getCellValue(sheet, row6, 3));//审核人
		            excelPd.put("TARGET", ObjectExcelRead.getCellValue(sheet, row7, 1));//会议目标
		            meetinginformationService.save(excelPd);
		            for(int i=10;i<count;i++){
		            	String title="";
		            	Row rowry = sheet.getRow(i);
		            		title=ObjectExcelRead.getCellValue(sheet, rowry, 0);
		            		if(title.equals("会议议程")){
		            			for(int j=i+1;j<count;j++){
		            				Row rowyc = sheet.getRow(j);
				            		title=ObjectExcelRead.getCellValue(sheet, rowyc, 0);
				            		if(title.equals("会议讨论项")){
				            			for(int t=j+1;t<count;t++){
				            				Row rowtl = sheet.getRow(t);
				            				title=ObjectExcelRead.getCellValue(sheet, rowtl, 0);
				            				if(title.equals("会议待办项")){
				            					for(int a=t+1;a<count;a++){
				            						int fno=1;
				            						Row rowt2 = sheet.getRow(a);
				            						title=ObjectExcelRead.getCellValue(sheet, rowt2, 0);
				            						if(!title.equals("行动计划")){
				            							dbPd.put("MEETINGINFORMATION_ID", MEETINGINFORMATION_ID);
				            							dbPd.put("MEETINGITEM_ID", this.get32UUID());	
				            							dbPd.put("FNO",fno+"");
				            							dbPd.put("ACTIONPLAN", ObjectExcelRead.getCellValue(sheet, rowt2, 0));
				            							dbPd.put("HEAD", ObjectExcelRead.getCellValue(sheet, rowt2, 1));
				            							dbPd.put("DEADLINE", ObjectExcelRead.getCellValue(sheet, rowt2, 2));
				            							dbPd.put("FSTATE", ObjectExcelRead.getCellValue(sheet, rowt2, 3));
				            							meetingitemService.save(dbPd);
				            						}
				            						fno++;
					            				}
				            					break;
				            				}else{
				            					tlPd.put("MEETINGINFORMATION_ID", MEETINGINFORMATION_ID);	//主键
					            				tlPd.put("MEETINGDISCUSS_ID", this.get32UUID());
					            				tlPd.put("FNO", ObjectExcelRead.getCellValue(sheet, rowtl, 0));
					            				tlPd.put("CONTENT", ObjectExcelRead.getCellValue(sheet, rowtl, 1));
					            				meetingdiscussService.save(tlPd);
				            				}
				            			}
				            			break;
				            		}else if(!title.equals("会议时间")){
				            			ycPd.put("MEETINGINFORMATION_ID", MEETINGINFORMATION_ID);	//主键
				            			ycPd.put("MEETINGAGENDA_ID",  this.get32UUID());	//主键
				            			ycPd.put("TIME", ObjectExcelRead.getCellValue(sheet, rowyc, 0));
				            			ycPd.put("THEME", ObjectExcelRead.getCellValue(sheet, rowyc, 1));
				            			ycPd.put("SPOKESMAN", ObjectExcelRead.getCellValue(sheet, rowyc, 3));
				            			meetingagendaService.save(ycPd);
				            		}
		            			}
		            			break;
		            		}else{
		            			ryPd.put("MEETINGINFORMATION_ID", MEETINGINFORMATION_ID);	//主键
			            		ryPd.put("MEETINGGATTENDEE_ID", this.get32UUID());	//主键
			            		ryPd.put("NAME", ObjectExcelRead.getCellValue(sheet, rowry, 0));//姓名
			            		ryPd.put("POSITION", ObjectExcelRead.getCellValue(sheet, rowry, 2));//职位
			            		meetinggattendeeService.save(ryPd);
		            		}
		            }
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	
	/**  
	 * 获取sheet中合并的单元格信息，并返回合并的单元格list
	 * @param sheet  需要导入的工作表
	 * @return List<CellRangeAddress> 合并的单元格list
	 */
	public List<CellRangeAddress> getCombineCell(Sheet sheet)    
	{
	List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
	//获得一个 sheet 中合并单元格的数量
	int sheetmergerCount = sheet.getNumMergedRegions();
	//遍历所有的合并单元格
	for(int i = 0; i<sheetmergerCount;i++)
	{
	//获得合并单元格保存进list中
	CellRangeAddress ca = sheet.getMergedRegion(i);
	list.add(ca);
	}
	return list;
	}
	
	/**   
	 * 判断指定的单元格是否是合并单元格   
	 * @param cras 合并的单元格list
	 * @param row 行下标   
	 * @param column 列下标   
	 * @return   
	 */                                                                                
	@SuppressWarnings("unused")
	private CellRangeAddress isMergedRegion(List<CellRangeAddress> cras,int row ,int column) {    
	for (CellRangeAddress range : cras) {
	int firstColumn = range.getFirstColumn();
	int lastColumn = range.getLastColumn();
	int firstRow = range.getFirstRow();
	int lastRow = range.getLastRow();
	if(row >= firstRow && row <= lastRow){
	if(column >= firstColumn && column <= lastColumn){
	return range;
	}
	}
	}
	return null;
	}
	
}
