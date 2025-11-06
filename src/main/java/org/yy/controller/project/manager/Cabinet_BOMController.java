package org.yy.controller.project.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.CollectionUtil;
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
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.StockService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

import oracle.net.aso.i;

/**
 * 说明：装配柜体BOM表 作者：YuanYes QQ356703572 时间：2021-05-07 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/Cabinet_BOM")
public class Cabinet_BOMController extends BaseController {

	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private StockService StockService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;

	@Autowired
	private Cabinet_AssemblyService Cabinet_AssemblyService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private DPROJECTService dprojectService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private MAT_BASICService mat_basicService;
	/**
	 * 保存
	 * v2 管悦 2021-07-08 柜体下相同物料验重
	 * v3 管悦 2021-08-03 生成序号
	 * v3 管悦 2021-08-20 生成版本号
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Cabinet_BOM_ID", this.get32UUID()); // 主键
		pd.put("If_Purchase", "否");
		pd.put("FAudit_State", "否");
		pd.put("BOM_COUNT", "0");
		pd.put("FVersion", Tools.date2Str(new Date(),"yyyy-MM-dd"));//版本号
		PageData pdOrder=Cabinet_BOMService.findOrder(pd);//获取排序
		if(pdOrder !=null) {
			pd.put("FORDER",pdOrder.get("FORDER").toString());
		}else {
			pd.put("FORDER","1");
		}
		PageData pdNUM=Cabinet_BOMService.findNUM(pd);//验重
		if(pdNUM !=null && Integer.parseInt(pdNUM.get("FNUM").toString())==0) {
			Cabinet_BOMService.save(pd);
		}else {
			errInfo = "fail";//重复
		}
		
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}

	/**
	 * v1 陈春光 2021-05-28 变更BOM 给项目经理发提醒
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/change")
	@ResponseBody
	public Object change() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Cabinet_BOM_ID", this.get32UUID()); // 主键
		//如果变更的 数量 大于 0 则需要去采购。
		if(new BigDecimal(pd.get("BOM_COUNT").toString()).doubleValue()>0){
			pd.put("If_Purchase", "否");
		}else{
			pd.put("If_Purchase", "是");
		}
		PageData pdOrder=Cabinet_BOMService.findOrder(pd);//获取排序
		if(pdOrder !=null) {
			pd.put("FORDER",pdOrder.get("FORDER").toString());
		}else {
			pd.put("FORDER","1");
		}
		pd.put("BOM_COUNT", "0");
		// 保存更改的BOM信息
		Cabinet_BOMService.save(pd);

		//根据BOM信息获取柜体数据id
		String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
		PageData caData = new PageData();
		caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
		PageData cad = Cabinet_Assembly_DetailService.findById(caData);

		// 根据柜体id的获取计划信息id
		String caID = cad.getString("Cabinet_Assembly_ID");
		PageData caParam = new PageData();
		caParam.put("Cabinet_Assembly_ID", caID);

		PageData ca = Cabinet_AssemblyService.findById(caParam);
		// 根据计划id的获取项目信息
		String PROJECT_ID = ca.getString("PROJECT_ID");
		PageData projectParam = new PageData();

		projectParam.put("PROJECT_ID", PROJECT_ID);
		PageData project = dprojectService.findById(projectParam);
		//获取项目经理
		String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");

		PageData staffParam = new PageData();
		staffParam.put("FNAME", PPROJECT_MANAGER);
		//根据项目经理的名称获取登录名/英文名
		PageData staffInfo = StaffService.getStaffId(staffParam);
		
		//构建消息实体，并发送
		PageData pdNotice = new PageData();
		pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
		// 跳转页面
	 /*   pdNotice.put("AccessURL", "../../../views/projectManager/pro_plan/pro_plan_jssj.html");// 
		pdNotice.put("ReadPeople", ",");// 已读人默认空
		pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
		pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		pdNotice.put("TType", "消息推送");// 消息类型
		pdNotice.put("FContent",
				"负责技术" + Jurisdiction.getName() + "变更了柜体" + cad.getString("Cabinet_No") + "的一条BOM信息，请注意查看");// 消息正文
		pdNotice.put("FTitle", "柜体BOM变更");// 消息标题
		pdNotice.put("LinkIf", "no");// 是否跳转页面
		pdNotice.put("DataSources", "柜体BOM");// 数据来源
		pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID") + ",");// 接收人
		pdNotice.put("Report_Key", "changeBOM");
		pdNotice.put("Report_Value", "");
		noticeService.save(pdNotice);*/
		
		//变更后 将原来的柜体数据 进行 采购恢复为否 方便下次采购 可以把变更的带过来
		if(new BigDecimal(pd.get("BOM_COUNT").toString()).doubleValue()>0){
			cad.put("If_Purchase_Done", "否");
			Cabinet_Assembly_DetailService.edit(cad);
		}
		
		//返回结果
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		Cabinet_BOMService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * v2 管悦 2021-07-08 同柜体物料验重
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdNUM=Cabinet_BOMService.findNUM(pd);//验重
		if(pdNUM !=null && Integer.parseInt(pdNUM.get("FNUM").toString())==0) {
			Cabinet_BOMService.edit(pd);
		}else {
			errInfo = "fail";//重复
		}
		
		//变更内容记录
		String FSTATE=pd.getString("FSTATE");
	    if(FSTATE.equals("变更")) {
	    	
	    }
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * v1 陈春光 2021-05-27 根据柜体详情id获取BOM列表，判断数据权限，是否可以操作并且做变更
	 * 
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = Cabinet_BOMService.list(page); // 列出Cabinet_BOM列表

		String staffName = Jurisdiction.getName();

		// 默认 变更是不允许的 只有在 BOM 完成并且 负责技术是自己的 才可以变更
		page.getPd().put("showChange", "N");
		String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
		// 传参为空
		if (StringUtil.isEmpty(Cabinet_Assembly_Detail_ID)) {
			page.getPd().put("showOpt", "N");
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}

		PageData pdData = new PageData();
		pdData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
		PageData findById = Cabinet_Assembly_DetailService.findById(pdData);
		// 没找到详情数据
		if (null == findById) {
			page.getPd().put("showOpt", "N");
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}
		String If_Bom_Done = findById.getString("If_Bom_Done");
		// BOM 没完成
		if ("否".equals(If_Bom_Done)) {
			// 默认可操作
			page.getPd().put("showOpt", "Y");
			String Responsible_Technology_Name = findById.getString("Responsible_Technology_Name");
			// 我不是负责技术的 不让操作 也不让变更
			if (!staffName.equals(Responsible_Technology_Name)) {
				page.getPd().put("showOpt", "N");
			}

		} else {
			// BOM 完成
			page.getPd().put("showOpt", "N");
			String Responsible_Technology_Name = findById.getString("Responsible_Technology_Name");
			// 我是当前负责技术的
			if (staffName.equals(Responsible_Technology_Name)) {
				// 不可进行增加修改操作 但是能进行变更
				page.getPd().put("showChange", "Y");
			}
		}
	
		for (PageData pageData : varList) {
			String MaterialID = pageData.getString("MAT_BASIC_ID");
			String FClass = pageData.getString("MAT_CLASS");
			String wareHouseType = "工厂库";
			PageData pg = new PageData();
			pg.put("ItemId", MaterialID);
			pg.put("FType", wareHouseType);
			pg.put("FClass", FClass);
			List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);

			if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
				pageData.put("ActualCount", 0.00);
			} else {
				PageData actualCountByFTYPEAndItemIDData = actualCountByFTYPEAndItemID.get(0);
				String ActualCountStr = String.valueOf(actualCountByFTYPEAndItemIDData.get("ActualCount"));
				Double ActualCount = Double.valueOf(ActualCountStr);
				pageData.put("ActualCount", ActualCount);
			}

		}

		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * v1 陈春光 2021-05-27 根据柜体编号查询详情id，根据柜体详情id获取BOM变更后的所有列表
	 * 
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getChangelistByCabinetNo")
	@ResponseBody
	public Object getChangelistByCabinetNo(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(pd);
		if (CollectionUtil.isEmpty(listAll)) {
			map.put("varList", Lists.newArrayList());
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}

		PageData cabinetAssemblyDetail = listAll.get(0);
		String Cabinet_Assembly_Detail_ID = cabinetAssemblyDetail.getString("Cabinet_Assembly_Detail_ID");
		pd.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);

		page.setPd(pd);
		List<PageData> varList = Cabinet_BOMService.list(page); // 列出Cabinet_BOM列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = Cabinet_BOMService.findById(pd); // 根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			Cabinet_BOMService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}
	/**
	 * 批量变更
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeAll")
	@ResponseBody
	public Object changeAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			for(String ID :ArrayDATA_IDS) {
				pd.put("Cabinet_BOM_ID", ID);
				PageData pdCa=Cabinet_BOMService.findById(pd);
				if(pdCa !=null) {
					pdCa.put("FSTATE", pd.getString("FSTATE"));
					pdCa.put("REMARK", pd.getString("REMARK"));
					pdCa.put("Change_Duty", pd.getString("Change_Duty"));
					Cabinet_BOMService.edit(pdCa);
				}
			}
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}
	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("物料id"); // 1
		titles.add("物料名称"); // 2
		titles.add("物料编码"); // 3
		titles.add("物料分类"); // 4
		titles.add("物料规格"); // 5
		titles.add("物料主单位"); // 6
		titles.add("物料品牌"); // 7
		titles.add("bom数量"); // 8
		titles.add("上限"); // 9
		titles.add("下限"); // 10
		titles.add("物料种类"); // 11
		titles.add("详情ID"); // 12
		dataMap.put("titles", titles);
		List<PageData> varOList = Cabinet_BOMService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_BASIC_ID")); // 1
			vpd.put("var2", varOList.get(i).getString("MAT_NAME")); // 2
			vpd.put("var3", varOList.get(i).getString("MAT_CODE")); // 3
			vpd.put("var4", varOList.get(i).getString("MAT_CLASS")); // 4
			vpd.put("var5", varOList.get(i).getString("MAT_SPECS")); // 5
			vpd.put("var6", varOList.get(i).getString("MAT_MAIN_UNIT")); // 6
			vpd.put("var7", varOList.get(i).getString("MAT_BRAND")); // 7
			vpd.put("var8", varOList.get(i).getString("BOM_COUNT")); // 8
			vpd.put("var9", varOList.get(i).getString("UP_LIMIT")); // 9
			vpd.put("var10", varOList.get(i).getString("DOWM_LIMIT")); // 10
			vpd.put("var11", varOList.get(i).getString("MAT_CATEGRAY")); // 11
			vpd.put("var12", varOList.get(i).getString("Cabinet_Assembly_Detail_ID")); // 12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}
	/**
	 * 保存
	 * v2 管悦 2021-07-13 柜体设计引用BOM
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAllBom")
	@ResponseBody
	public Object selectAllBom() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varOList = Cabinet_BOMService.listAllByIds(pd);//获取已选择bom列表
		String DATA_IDS = pd.getString("DATA_IDS");
		String FIsQuote = pd.getString("FIsQuote");
		
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			for(int i=0;i<ArrayDATA_IDS.length;i++) {
				for(PageData pdBom:varOList) {
					pdBom.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[i]);
					pdBom.put("Cabinet_BOM_ID", this.get32UUID()); // 主键
					pdBom.put("If_Purchase", "否");
					pdBom.put("FAudit_State", "否");
					pdBom.put("REMARK", "");
					pdBom.put("Change_Duty", "");
					pdBom.put("FSTATE", "正常");
					if(FIsQuote.equals("否")) {
						pdBom.put("BOM_COUNT", "0");
					}
					pdBom.put("FVersion", Tools.date2Str(new Date(),"yyyy-MM-dd"));//版本号
					pdBom.put("CG_COUNT", "0");
					PageData pdNUM=Cabinet_BOMService.findNUM(pdBom);//验重
					if(pdNUM !=null && Integer.parseInt(pdNUM.get("FNUM").toString())==0) {
						Cabinet_BOMService.save(pdBom);
					}else {
						continue;
					}
				}
			}
			errInfo = "success";
		} else {
			errInfo = "error";
		}

		map.put("result", errInfo);
		return map;
	}
	
	/**从EXCEL导入到数据库
	 * v2 20210820 管悦 生成版本号
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = this.getPageData();
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
	        //判断物料代码是否存在
	        String matStr="";
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
	            basicPd.put("FORDER", ObjectExcelRead.getCellValue(sheet, row, 0));//序号
	            basicPd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 1));//物料代码
	            PageData num = new PageData();
	    		num = mat_basicService.findCountByCode(basicPd);
	    		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
	    			//errInfo = "error";//编号重复
	    			
	    		} else {
	    			 matStr+=basicPd.getString("FORDER")+"、"+basicPd.getString("MAT_CODE")+";";
	    			 errInfo="fail1";
	    			 
	    		}
	        }
	        if(!matStr.equals("")) {
	        	matStr="以下物料代码不存在："+matStr;
	        	map.put("matStr", matStr);
	        }else {
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
	            //basicPd.put("FORDER", ObjectExcelRead.getCellValue(sheet, row, 0));//序号
	            basicPd.put("MAT_CODE", ObjectExcelRead.getCellValue(sheet, row, 1));//物料代码
	            PageData pdOrder=Cabinet_BOMService.findOrder(pd);//获取排序
	    		if(pdOrder !=null) {
	    			basicPd.put("FORDER",pdOrder.get("FORDER").toString());
	    		}else {
	    			basicPd.put("FORDER","1");
	    		}
	            List<PageData>  matList= new ArrayList<PageData>();
	            matList = mat_basicService.getListByMatCode(""+ObjectExcelRead.getCellValue(sheet, row, 1));
	    		if(matList.size() >0) {
	    			basicPd.put("MAT_BASIC_ID", matList.get(0).getString("MAT_BASIC_ID"));
	    			basicPd.put("MAT_NAME", matList.get(0).getString("MAT_NAME"));
	    			basicPd.put("MAT_CLASS", matList.get(0).getString("MAT_CLASS"));
	    			basicPd.put("MAT_SPECS", matList.get(0).getString("MAT_SPECS"));
	    			basicPd.put("MAT_MAIN_UNIT", matList.get(0).getString("FUNITNAME"));
	    			basicPd.put("MAT_BRAND", matList.get(0).getString("MAT_BRAND"));
	    		}
	    		basicPd.put("Cabinet_Assembly_Detail_ID", pd.getString("Cabinet_Assembly_Detail_ID"));	   
    			basicPd.put("FSTATE", pd.getString("FSTATE"));	
    			basicPd.put("BOM_COUNT", ObjectExcelRead.getCellValue(sheet, row, 3));
    			basicPd.put("MAT_CATEGRAY", ObjectExcelRead.getCellValue(sheet, row, 4));
    			if(pd.getString("FSTATE").equals("变更")) {
    				basicPd.put("Change_Duty", ObjectExcelRead.getCellValue(sheet, row, 5));
    				basicPd.put("REMARK", ObjectExcelRead.getCellValue(sheet, row, 6));
    			}
	    		basicPd.put("Cabinet_BOM_ID", this.get32UUID()); // 主键
	    		basicPd.put("If_Purchase", "否");
	    		basicPd.put("FAudit_State", "否");
	    		basicPd.put("FVersion", Tools.date2Str(new Date(),"yyyy-MM-dd"));//版本号
	    		PageData pdNUM=Cabinet_BOMService.findNUM(basicPd);//验重
	    		if(pdNUM ==null || Integer.parseInt(pdNUM.get("FNUM").toString())==0) {
	    			Cabinet_BOMService.save(basicPd);
	    		}else {
	    			errInfo = "fail2";//重复
	    			break;
	    		} 
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
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "BOM.xlsx", "BOM.xlsx");
	}
}
