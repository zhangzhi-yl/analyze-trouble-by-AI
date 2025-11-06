package org.yy.controller.project.manager;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.ObjectExcelReadYT;
import org.yy.entity.PageData;
import org.yy.service.project.manager.DTPROJECTFILEService;
import org.yy.service.project.manager.PRESALEPLANONEService;
import org.yy.service.project.manager.PRESALEPLANService;
import org.yy.service.project.manager.PRESALEPLANTWOService;

/** 
 * 说明：售前方案计划
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/presaleplan")
public class PRESALEPLANController extends BaseController {
	
	@Autowired
	private PRESALEPLANService presaleplanService;
	
	@Autowired
	private PRESALEPLANONEService presaleplanoneService;
	
	@Autowired
	private PRESALEPLANTWOService presaleplantwoService;
	@Autowired
	private DTPROJECTFILEService dtprojectfileService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("presaleplan:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PRESALEPLAN_ID", this.get32UUID());	//主键
		pd.put("FCREATETIME", Tools.date2Str(new Date()));
		pd.put("FFOUNDER", Jurisdiction.getName());
		pd.put("FFOUNDERACCOUNT", Jurisdiction.getName());
		pd.put("FAUDIT", "未提交");
		pd.put("FSTATE_LXSQ", "未下推");//是否下推立项申请
		pd.put("FSTATE_LX", "未立项");//是否立项
		presaleplanService.save(pd);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("presaleplan:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		presaleplanService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("presaleplan:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FMODIFIERSTIME", Tools.date2Str(new Date()));
		pd.put("FMODIFIERS", Jurisdiction.getName());
		pd.put("FMODIFIERSACCOUNT", Jurisdiction.getName());
		presaleplanService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("presaleplan:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FFOUNDER", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData>	varList = presaleplanService.list(page);	//列出presaleplan列表
		pd.put("NAME", Jurisdiction.getName());
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**获得售前计划方案
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPresalePlanListt")
	@ResponseBody
	public Object getPresalePlanListt(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = presaleplanService.listAll(pd);	//列出presaleplan列表
		pd.put("NAME", Jurisdiction.getName());
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("presaleplan:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = presaleplanService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**方案变化金额自动变化
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goChange")
	@ResponseBody
	public Object goChange() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdB = presaleplanService.findById(pd);
		List<PageData> varListB = presaleplantwoService.listProchance(pd);		//获取材料费、技术费、主要成本
		if(varListB.size()>0){
			PageData pdBB=varListB.get(0);
			pdB.put("FMAJORCOSTS",pdBB.get("xmzycb"));
			pdB.put("FMATERIALCOST",pdBB.get("ZJCL"));
			pdB.put("FTECHNICALEXPENSES",pdBB.get("JSGSF"));
		}
		List<PageData>  FYList = presaleplantwoService.findByJHNew(pd);	//获取各个工时费用
		if(FYList.size()>0){
			pdB.put("FDESIGNHOUR",FYList.get(0).get("FQUANTITY"));
			pdB.put("FDESIGNUNITPRICE",FYList.get(0).get("FUNITPRICE"));
			pdB.put("FDESIGNAMOUNT",FYList.get(0).get("FTOTALAMOUNT"));
			
			pdB.put("FDIVISIONHOUR",FYList.get(1).get("FQUANTITY"));
			pdB.put("FDIVISIONUNITPRICE",FYList.get(1).get("FUNITPRICE"));
			pdB.put("FDIVISIONAMOUNT",FYList.get(1).get("FTOTALAMOUNT"));
			
			pdB.put("FASSEMBLYHOUR",FYList.get(2).get("FQUANTITY"));
			pdB.put("FASSEMBLYUNITPRICE",FYList.get(2).get("FUNITPRICE"));
			pdB.put("FASSEMBLYAMOUNT",FYList.get(2).get("FTOTALAMOUNT"));
			
			pdB.put("FWIRINGHOUR",FYList.get(3).get("FQUANTITY"));
			pdB.put("FWIRINGUNITPRICE",FYList.get(3).get("FUNITPRICE"));
			pdB.put("FWIRINGAMOUNT",FYList.get(3).get("FTOTALAMOUNT"));
			
			pdB.put("FEXAMINATIONHOUR",FYList.get(4).get("FQUANTITY"));
			pdB.put("FEXAMINATIONUNITPRICE",FYList.get(4).get("FUNITPRICE"));
			pdB.put("FEXAMINATIONAMOUNT",FYList.get(4).get("FTOTALAMOUNT"));
			
			pdB.put("FFINALINSPECTIONHOUR",FYList.get(5).get("FQUANTITY"));
			pdB.put("FFINALINSPECTIONPRICE",FYList.get(5).get("FUNITPRICE"));
			pdB.put("FFINALINSPECTIONAMOUNT",FYList.get(5).get("FTOTALAMOUNT"));
			
			pdB.put("FPACKINGHOUR",FYList.get(6).get("FQUANTITY"));
			pdB.put("FPACKINGUNITPRICE",FYList.get(6).get("FUNITPRICE"));
			pdB.put("FPACKINGAMOUNT",FYList.get(6).get("FTOTALAMOUNT"));
		}
		try{
			if(pdB != null){
				errInfo = "success";
			}
		}catch (Exception e){
			errInfo = "exception";
		}finally{
			map.put("pdB",pdB);
			map.put("result", errInfo);
		}
		map.put("pd", pdB);
		map.put("result", errInfo);
		return map;
	}
	
	/**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("presaleplan:add")
	@ResponseBody
	public Object goAdd() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FBUDGETNUMBER", "SQJH"+Tools.date2Str(new Date(),"yyyyMMddhhmmss"));
		
		pd.put("FDESIGNHOUR", "0");
		pd.put("FDIVISIONHOUR", "0");
		pd.put("FASSEMBLYHOUR", "0");
		pd.put("FWIRINGHOUR", "0");
		pd.put("FEXAMINATIONHOUR", "0");
		pd.put("FPACKINGHOUR", "0");
		
		pd.put("FDESIGNUNITPRICE", "600");
		pd.put("FDIVISIONUNITPRICE", "600");
		pd.put("FASSEMBLYUNITPRICE", "600");
		pd.put("FWIRINGUNITPRICE", "600");
		pd.put("FEXAMINATIONUNITPRICE", "600");
		pd.put("FPACKINGUNITPRICE", "600");
		
		pd.put("FDESIGNAMOUNT", "0");
		pd.put("FDIVISIONAMOUNT", "0");
		pd.put("FASSEMBLYAMOUNT", "0");
		pd.put("FWIRINGAMOUNT", "0");
		pd.put("FEXAMINATIONAMOUNT", "0");
		pd.put("FPACKINGAMOUNT", "0");
		
		pd.put("FFINALINSPECTIONHOUR", "0");
		pd.put("FFINALINSPECTIONPRICE", "600");
		pd.put("FFINALINSPECTIONAMOUNT", "0");
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("presaleplan:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			presaleplanService.deleteAll(ArrayDATA_IDS);
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
		titles.add("预算名称");	//1
		titles.add("预算编号");	//2
		titles.add("合同总额");	//3
		titles.add("直接材料成本");	//4
		titles.add("技术费用");	//5
		titles.add("装配工时费");	//6
		titles.add("设计工时");	//7
		titles.add("分料工时");	//8
		titles.add("装配工时");	//9
		titles.add("接线工时");	//10
		titles.add("检验工时");	//11
		titles.add("包装工时");	//12
		titles.add("项目主要成本");	//13
		titles.add("项目毛利");	//14
		titles.add("项目毛利率");	//15
		titles.add("创建时间");	//16
		titles.add("创建人");	//17
		titles.add("创建人账号");	//18
		titles.add("修改人");	//19
		titles.add("修改人账号");	//20
		titles.add("修改时间");	//21
		titles.add("提交人");	//22
		titles.add("提交人账号");	//23
		titles.add("提交时间");	//24
		titles.add("审核人");	//25
		titles.add("审核人账号");	//26
		titles.add("审核时间");	//27
		titles.add("预留字段1");	//28
		titles.add("预留字段2");	//29
		titles.add("预留字段3");	//30
		titles.add("预留字段4");	//31
		titles.add("预留字段5");	//32
		dataMap.put("titles", titles);
		List<PageData> varOList = presaleplanService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FBUDGETNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FBUDGETNUMBER"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCONTRACTAMOUNT"));	    //3
			vpd.put("var4", varOList.get(i).getString("FMATERIALCOST"));	    //4
			vpd.put("var5", varOList.get(i).getString("FTECHNICALEXPENSES"));	    //5
			vpd.put("var6", varOList.get(i).getString("FASSEMBLYCHARGE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FDESIGNHOUR"));	    //7
			vpd.put("var8", varOList.get(i).getString("FDIVISIONHOUR"));	    //8
			vpd.put("var9", varOList.get(i).getString("FASSEMBLYHOUR"));	    //9
			vpd.put("var10", varOList.get(i).getString("FWIRINGHOUR"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXAMINATIONHOUR"));	    //11
			vpd.put("var12", varOList.get(i).getString("FPACKINGHOUR"));	    //12
			vpd.put("var13", varOList.get(i).getString("FMAJORCOSTS"));	    //13
			vpd.put("var14", varOList.get(i).getString("FPROJECTGROSSPROFIT"));	    //14
			vpd.put("var15", varOList.get(i).getString("FGROSSMARGIN"));	    //15
			vpd.put("var16", varOList.get(i).getString("FCREATETIME"));	    //16
			vpd.put("var17", varOList.get(i).getString("FFOUNDER"));	    //17
			vpd.put("var18", varOList.get(i).getString("FFOUNDERACCOUNT"));	    //18
			vpd.put("var19", varOList.get(i).getString("FMODIFIERS"));	    //19
			vpd.put("var20", varOList.get(i).getString("FMODIFIERSACCOUNT"));	    //20
			vpd.put("var21", varOList.get(i).getString("FMODIFIERSTIME"));	    //21
			vpd.put("var22", varOList.get(i).getString("FAUTHOR"));	    //22
			vpd.put("var23", varOList.get(i).getString("FAUTHORNUMBER"));	    //23
			vpd.put("var24", varOList.get(i).getString("FAUTHORTIME"));	    //24
			vpd.put("var25", varOList.get(i).getString("FEXAMINERS"));	    //25
			vpd.put("var26", varOList.get(i).getString("FEXAMINERSNUMBER"));	    //26
			vpd.put("var27", varOList.get(i).getString("FEXAMINERSTIME"));	    //27
			vpd.put("var28", varOList.get(i).getString("FRESERVE1"));	    //28
			vpd.put("var29", varOList.get(i).getString("FRESERVE2"));	    //29
			vpd.put("var30", varOList.get(i).getString("FRESERVE3"));	    //30
			vpd.put("var31", varOList.get(i).getString("FRESERVE4"));	    //31
			vpd.put("var32", varOList.get(i).getString("FRESERVE5"));	    //32
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**提交审核
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/goAduit")
	@ResponseBody
	public Object goAduit() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FAUDIT", "审核中");
		presaleplanService.goAduit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**确认审核
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/goConfirmed")
	@ResponseBody
	public Object goConfirmed() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		presaleplanService.goAduit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
   	
   	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/getFormPlate")
	public void getFormPlate(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "presaleplan.xlsx", "售前计划模板.xlsx");
	}
   	
   	/**读取文件
   	 * @param
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value = "file", required = false) MultipartFile file,String PRESALEPLAN_ID)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData jsonObject = new PageData();
		DecimalFormat df = new DecimalFormat("#0.00");
		int a=1;
		try {
			if (null != file && !file.isEmpty()) {

				String  ffile = DateUtil.getDays(), fileName = "";
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				List<PageData> list = (List) ObjectExcelReadYT.readExcelFA(filePath, fileName, 1, 0, 0, 9);
				int detail = 0;
				File target = new File(filePath, fileName);
				FileInputStream fi = new FileInputStream(target);
			    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
			    XSSFSheet sheet= wb.getSheetAt(0); //sheet 从0开始
			    boolean flg=false;
				int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
				detail = findRow(sheet, "detail:");
				if(detail==-1){//没找到detail:
					return a = 1;
				}else{
					a = detail+1;
				}
				for (int i = 0; i < list.size(); i++) {
					a = a + 2;
					PageData epd = list.get(i);
					PageData pdMxONE = new PageData();
					pdMxONE.put("PRESALEPLANONE_ID", this.get32UUID()); //明细主键
					pdMxONE.put("PRESALEPLAN_ID", PRESALEPLAN_ID);
					pdMxONE.put("FFOUNDER", Jurisdiction.getName());
					pdMxONE.put("FFOUNDERACCOUNT", Jurisdiction.getName());
					pdMxONE.put("FCREATETIME", Tools.date2Str(new Date()));
					pdMxONE.put("FSORT", epd.getString("var0"));		//排序
					pdMxONE.put("FDESCRIPTION", epd.getString("var3"));	//名称描述
					pdMxONE.put("FCABINETTYPE", epd.getString("var4"));	//名称描述
					PageData pdmax = presaleplanoneService.maxNum(pdMxONE);
					pdMxONE.put("FNUMBER", "" + pdmax.get("maxNum")); // 最大序号+1
					presaleplanoneService.save(pdMxONE);	//保存一级明细
					if (null != epd.get("FDs")) {
						List<PageData> mxTWOList = (List<PageData>) epd.get("FDs");
						for (int j = 0; j < mxTWOList.size(); j++) {
							a = a + 1;
							String FMRMATCODE = "";
							PageData epdMX = mxTWOList.get(j);
							PageData pdMxTWO = new PageData();
							pdMxTWO.put("PRESALEPLANTWO_ID",this.get32UUID()); // 明细主键
							pdMxTWO.put("PRESALEPLAN_ID", PRESALEPLAN_ID);// 主表主键
							pdMxTWO.put("PRESALEPLANONE_ID",pdMxONE.getString("PRESALEPLANONE_ID"));// 主表主键
							pdMxTWO.put("FFOUNDER", Jurisdiction.getName());
							pdMxTWO.put("FFOUNDERACCOUNT", Jurisdiction.getName());
							pdMxTWO.put("FCREATETIME", Tools.date2Str(new Date())); // 创建时间
							if (null != epdMX.getString("var0").trim() && !"".equals(epdMX.getString("var0"))) {// 判断序号是否为空
								Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]{0,}");
								Matcher isNum = pattern.matcher(epdMX.getString("var0").trim());
								if (!isNum.matches()) {// 判断var0数据是否是数字
									pdMxTWO.put("FNUMBER", "0");// 序号
								} else {
									pdMxTWO.put("FNUMBER", epdMX.getString("var0").trim());// 序号
								}
							} else {
								pdMxTWO.put("FNUMBER", "0");// 序号
							}
							pdMxTWO.put("FNAME", epdMX.getString("var1"));
							pdMxTWO.put("FORDERNUMBER",epdMX.get("var2") == null ? epdMX.get("var2") : (epdMX.get("var2") + "").trim());
							pdMxTWO.put("FTECHNICALDESCRIPTION", epdMX.getString("var3"));
							pdMxTWO.put("FUNIT", epdMX.getString("var4"));
							if (!"".equals(epdMX.getString("var5").trim())) {
								pdMxTWO.put("FQUANTITY", df.format(Double.parseDouble(epdMX.getString("var5").trim())));
							} else {
								pdMxTWO.put("FQUANTITY", "0.00");
							}
							if (!"".equals(epdMX.getString("var6").trim())) {
								pdMxTWO.put("FUNITPRICE", df.format(Double.parseDouble(epdMX.getString("var6").trim())));
							} else {
								pdMxTWO.put("FUNITPRICE", "0.00");
							}
							if (!"".equals(epdMX.getString("var7").trim())) {
								pdMxTWO.put("FTOTALAMOUNT", df.format(Double.parseDouble(epdMX.getString("var7").trim())));// 主表主键
							} else {
								pdMxTWO.put("FTOTALAMOUNT", "0.00");
							}
							pdMxTWO.put("FMANUFACTURER", epdMX.getString("var8").trim());
//							pdMxTWO.put("FCABINETNAME", epdMX.getString("var9").trim());// 柜体名称
							if (!"".equals(pdMxTWO.getString("FQUANTITY")) && !"".equals(pdMxTWO.getString("FUNITPRICE"))) {
								pdMxTWO.put("FTOTALAMOUNT", df.format(Double.parseDouble(pdMxTWO.getString("FQUANTITY")) * Double.parseDouble(pdMxTWO.getString("FUNITPRICE"))));
							}
							presaleplantwoService.save(pdMxTWO);
						}
					}
				}
			}
			result = "200";
			map.put("msg", "ok");
			map.put("msgText", "导入Execl成功！");
		} catch (Exception e) {
			result = "500";
			map.put("msg", "no");
			map.put("msgText",  + a +"行出错！报错信息：" + e.getMessage().split(":")[1]);
			PageData dp = new PageData();
			dp.put("PRESALEPLAN_ID", PRESALEPLAN_ID);
			presaleplanoneService.deleteFBOne(dp);
			presaleplantwoService.deleteFBTwo(dp);
		} finally {
			map.put("result", result);
		}
		return map;
	}
   	//查找具有字符串值的Excel单元格并获取其位置（行）
  	private static int findRow(XSSFSheet sheet, String cellContent) {
  		for (Row row : sheet) {
  			for (Cell cell : row) {
  				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
  					if (cell.getRichStringCellValue().getString().trim()
  							.equals(cellContent)) {
  						return row.getRowNum();
  					}
  				}
  			}
  		}
  		return -1;
  	}

	 /**v1 管悦 20210916 下推立项申请
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editLXSQ")
	@ResponseBody
	public Object editLXSQ() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FSTATE_LXSQ", "已下推");
		pd.put("FTIME1",Tools.date2Str(new Date()));
		pd.put("FMAN1", Jurisdiction.getName());
		PageData pdf=new PageData();
		pdf.put("PROJECT_ID", pd.getString("PRESALEPLAN_ID"));
		pdf.put("FFILETYPE", "双章合同");
		PageData pdFJ=dtprojectfileService.findFJ(pdf);//根据类型获取项目申请附件
		if(pdFJ != null && Integer.parseInt(pdFJ.get("FNUM").toString())==0){
			errInfo="fail2";
			map.put("result", errInfo);
			return map;
		}
		
		presaleplanService.editLXSQ(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	/**v1 管悦 20210916 立项申请
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listLXSQ")
	@ResponseBody
	public Object listLXSQ(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = presaleplanService.listLXSQ(page);	//列出presaleplan列表
		pd.put("NAME", Jurisdiction.getName());
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
