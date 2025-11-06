package org.yy.controller.wglwt;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.service.wglwt.TypelogService;
import org.yy.util.*;
import org.yy.entity.PageData;
import org.yy.service.wglwt.QuestionbankService;

import javax.servlet.http.HttpServletResponse;

/** 
 * 说明：问题库
 * 作者：YuanYes QQ356703572
 * 时间：2021-09-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/questionbank")
public class QuestionbankController extends BaseController {
	
	@Autowired
	private QuestionbankService questionbankService;
	@Autowired
	private TypelogService typelogService;
	
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
		PageData pd1 = new PageData();
		PageData pd2 = new PageData();
		pd = this.getPageData();
		pd.put("QUESTIONBANK_ID", this.get32UUID());	//主键
		pd1=questionbankService.getMax();
		if(pd1!=null){
		Integer QUESTION_NUMBER1=Integer.parseInt(pd1.getString("QUESTION_NUMBER"))+1;
		String QUESTION_NUMBER=String.valueOf(QUESTION_NUMBER1);
		String zero="00000000";
		String QUESTION_NUMBER2=zero+QUESTION_NUMBER;
		pd.put("QUESTION_NUMBER",QUESTION_NUMBER2.substring(QUESTION_NUMBER2.length()-9));}
		else{
			String QUESTION_NUMBER="000000001";
			pd.put("QUESTION_NUMBER",QUESTION_NUMBER);
		}
		pd.put("STATE","创建");
		pd.put("PROPOSER", Jurisdiction.getName());
		pd.put("RELEASE_TIME",Tools.date2Str(new Date()));
		questionbankService.save(pd);
		pd2.put("TYPELOG_ID", this.get32UUID());
		pd2.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd2.put("OPERATOR",Jurisdiction.getName());
		pd2.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd2.put("CURRENT_STATE","创建");
		typelogService.save(pd2);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE;                                //文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, "dailyexpenses_excel");                    //执行上传
			List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 3, 0, 0);            //执行读EXCEL操作,读出的数据导入List 1:从第2行开始；0:从第A列开始(1的时候第一列var0为null，2的时候第二列var1为null以此类推)；0:第1个sheet
			System.out.println(listPd);
			pd.put("YL5", this.get32UUID());//用以标记本次导入数据，如果导入过程中出现错误，将删除有此标记的数据
			for (int i = 0; i < listPd.size(); i++) {//循环表格中的数据
				pd.put("QUESTIONBANK_ID", this.get32UUID());//明细表主键ID
				pd.put("QUESTION_TITLE",listPd.get(i).getString("var0"));//物料名称
				pd.put("PROBLEM_SOURCE",listPd.get(i).getString("var1"));
			    pd.put("SCOPE_OF_INFLUENCE",listPd.get(i).getString("var2"));
				pd.put("RESPONSIBILITY_JUDGMENT",listPd.get(i).getString("var3"));
				pd.put("SEVERITY",listPd.get(i).getString("var4"));
				pd.put("PERSON_LIABLE",listPd.get(i).getString("var5"));
				pd.put("PROBLEM_DESCRIPTION",listPd.get(i).getString("var6"));
				PageData pd1 = new PageData();
				pd1=questionbankService.getMax();
				if(pd1!=null){
					Integer QUESTION_NUMBER1=Integer.parseInt(pd1.getString("QUESTION_NUMBER"))+1;
					String QUESTION_NUMBER=String.valueOf(QUESTION_NUMBER1);
					String zero="00000000";
					String QUESTION_NUMBER2=zero+QUESTION_NUMBER;
					pd.put("QUESTION_NUMBER",QUESTION_NUMBER2.substring(QUESTION_NUMBER2.length()-9));}
				else{
					String QUESTION_NUMBER="000000001";
					pd.put("QUESTION_NUMBER",QUESTION_NUMBER);
				}
				pd.put("PROPOSER", Jurisdiction.getName());
				pd.put("RELEASE_TIME",Tools.date2Str(new Date()));
				pd.put("STATE","创建");
				questionbankService.save(pd);
			}
		}
		map.put("result", errInfo);                //返回结果
		return map;
	}

	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "问题表导入模板.xlsx", "问题表导入模板.xlsx");
	}


	/**删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		questionbankService.delete(pd);
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
		questionbankService.edit(pd);

		map.put("result", errInfo);
		return map;
	}
	/***
	 * 状态：发布
	 */
	@RequestMapping(value="/release")
	@ResponseBody
	public Object release() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd2 = new PageData();
		pd = this.getPageData();
		pd.put("QUESTIONBANK_ID",pd.get("row[QUESTIONBANK_ID]"));
		pd2.put("TYPELOG_ID", this.get32UUID());
		pd2.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd2.put("OPERATOR",Jurisdiction.getName());
		pd2.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd2.put("CURRENT_STATE","发布");
		typelogService.save(pd2);
		questionbankService.release(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/goend")
	@ResponseBody
	public Object goend() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd2 = new PageData();
		pd = this.getPageData();
		pd.put("QUESTIONBANK_ID",pd.get("row[QUESTIONBANK_ID]"));
		pd2.put("TYPELOG_ID", this.get32UUID());
		pd2.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd2.put("OPERATOR",Jurisdiction.getName());
		pd2.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd2.put("CURRENT_STATE","结束");
		typelogService.save(pd2);
		questionbankService.goend(pd);
		map.put("result", errInfo);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/PicUpload")
	public Object PicUpload(@RequestParam("file") MultipartFile file) throws Exception {
		//LoggerFactory.getLogger(getClass()).debug("长度"+file.length);
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		String type = "";
		if(file != null && !file.isEmpty()){//判断是否重新上传附件，重新上传走系统文件上传，不是直接走修改
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf(".")); // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
			type = FPFFILEPATH;
		}
		map.put("type",type);
		map.put("result", errInfo);    //返回结果
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
		String Loginperson=Jurisdiction.getName();
		pd.put("Loginperson",Loginperson);
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = questionbankService.list(page);	//列出Questionbank列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = questionbankService.findById(pd);	//根据ID读取
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
			questionbankService.deleteAll(ArrayDATA_IDS);
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
		titles.add("问题编号");	//1
		titles.add("问题标题");	//2
		titles.add("影响范围");	//3
		titles.add("问题描述");	//4
		titles.add("问题来源");	//5
		titles.add("责任判定");	//6
		titles.add("严重度");	//7
		titles.add("责任人");	//8
		titles.add("提出人");	//9
		titles.add("发布时间");	//10
		titles.add("状态");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = questionbankService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("QUESTION_NUMBER").toString());	//1
			vpd.put("var2", varOList.get(i).getString("QUESTION_TITLE"));	    //2
			vpd.put("var3", varOList.get(i).getString("SCOPE_OF_INFLUENCE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROBLEM_DESCRIPTION"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROBLEM_SOURCE"));	    //5
			vpd.put("var6", varOList.get(i).getString("RESPONSIBILITY_JUDGMENT"));	    //6
			vpd.put("var7", varOList.get(i).getString("SEVERITY"));	    //7
			vpd.put("var8", varOList.get(i).getString("PERSON_LIABLE"));	    //8
			vpd.put("var9", varOList.get(i).getString("PROPOSER"));	    //9
			vpd.put("var10", varOList.get(i).getString("RELEASE_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("STATE"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
