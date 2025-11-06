package org.yy.controller.ny;

import net.sf.json.JSONArray;
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
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.ny.NYLoopService;
import org.yy.service.ny.NYPLCService;
import org.yy.service.ny.NY_EQM_FILESService;
import org.yy.service.ny.NY_EquipmentsService;
import org.yy.service.zm.EQM_FILESService;
import org.yy.service.zm.EquipmentsService;
import org.yy.util.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** 
 * 说明：照明
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-08
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ny_equipments")
public class NY_EquipmentsController extends BaseController {
	
	@Autowired
	private NY_EquipmentsService equipmentService;

	@Autowired
	private NY_EQM_FILESService eqm_filesService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private NYPLCService plcService;

	@Autowired
	private NYLoopService loopService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("equipment:add")
	@ResponseBody
	public Object add(@RequestParam(value = "PATH", required = false) MultipartFile file) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQUIPMENT_ID", this.get32UUID());	//主键
		pd.put("CreateTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));  //创建时间
		pd.put("Creator", Jurisdiction.getName());    //创建人
		//保存主表信息
		equipmentService.save(pd);

		//附带的文件上传至从表
		if(null != file && !file.isEmpty()){
			if(null != pd.getString("FileName") && !"".equals(pd.getString("FileName"))){
				PageData filePd = new PageData();
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String dateName = df.format(calendar.getTime());
				String ffile = DateUtil.getDays(), fileName = "";
				if(null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))){
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FileName").substring(0, pd.getString("FileName").indexOf(".")); // 文件上传路径
					fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
					String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
					filePd.put("FilePath",FPFFILEPATH);
					filePd.put("Creator", Jurisdiction.getName());	//创建人
					filePd.put("CreateTime", pd.getString("CreateTime"));	//创建时间
					filePd.put("EQUIPMENT_ID", pd.getString("EQUIPMENT_ID"));	//设备主键
					filePd.put("EQM_FILES_ID", this.get32UUID());	//主键
					filePd.put("FName", pd.getString("FileName"));	//文件名称
					filePd.put("FileType", pd.getString("FileType"));	//文件类型

					//保存附件信息
					eqm_filesService.save(filePd);

				}else {
					errInfo = "201";  //设备主键未获取到
				}
			}else {
				errInfo = "202";   //附件名称未获取到
			}
		}

		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("equipment:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		equipmentService.delete(pd);
		eqm_filesService.deleteByEqm(pd);  //级联删除附件
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
//	@RequiresPermissions("equipment:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		equipmentService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
//	@RequiresPermissions("equipment:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = equipmentService.list(page);	//列出Equipment列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
//	@RequiresPermissions("equipment:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = equipmentService.listAll(pd);	//列出Equipment列表

		List<PageData>	paramList = new ArrayList<>();

		for (PageData var : varList){
			String EQUIPMENT_ID = var.getString("EQUIPMENT_ID");
			PageData getPlc = new PageData();
			getPlc.put("EQUIPMENT_ID",EQUIPMENT_ID);
			List<PageData> plcList = plcService.getPLCByEquipment(getPlc);

			String ParamName = "";
			PageData param = new PageData();
			List<String> plcNameList = new ArrayList<>();
			for (PageData plc : plcList){
				plcNameList.add(plc.getString("ParamName"));
			}
			param.put("label",var.getString("FName"));
			String value = String.join(",", plcNameList.toArray(new String[plcNameList.size()]));
			if("".equals(value)){
				value = "无";
			}
			param.put("id",EQUIPMENT_ID);
			param.put("value",value);
			paramList.add(param);
		}

		PageData loop = loopService.findById(pd);
		List<PageData> equList = new ArrayList<>();
		if(null != loop){
			if(null != loop.getString("Extend1") && !"".equals(loop.getString("Extend1"))) {
				PageData getEqu = handleIDS(loop.getString("Extend1"));
				equList = equipmentService.listAll(getEqu);
			}
		}

		map.put("paramList", paramList);
		map.put("equList", equList);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	private static PageData handleIDS(String IDS){
		PageData ids = new PageData();
		//处理逗号分割id
		String ID = "(";
		if(!"".equals(IDS) && null != IDS){
			String[] array = IDS.split(",");
			if(array.length != 0){
				for (int i = 0;i < array.length ; i++){
					ID = ID + "'" + array[i] + "',";
					if(i == array.length-1){
						ID = ID.substring(0,ID.length()-1);
					}
				}
				ID += ")";
			}
		}else {
			ID = "";
		}
		ids.put("IDS",ID);
		return ids;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("equipment:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		String ZDEPARTMENT_ID = "";//Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", (null == arr ?"":"{\"treeNodes\":" + arr.toString() + "}"));
		pd = equipmentService.findById(pd);	//根据ID读取
		PageData dpd = new PageData();
		pd.put("DEPARTMENT_ID",pd.getString("FDept"));
		dpd = departmentService.findById(pd);
		String depname = null == dpd?"请选择":dpd.getString("NAME");
		map.put("depname", null == depname?"请选择":depname);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("equipment:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			equipmentService.deleteAll(ArrayDATA_IDS);
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
//	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("设备名称");	//1
		titles.add("设备编号");	//2
		titles.add("所属部门");	//3
		//titles.add("开关状态");	//4
		titles.add("列坐标");	//5
		titles.add("横坐标");	//6
		titles.add("规格型号");	//7
		titles.add("厂家");	//8
		titles.add("备注");	//9
		titles.add("站点");	//10
		//titles.add("是否报警");	//11
		//titles.add("图纸文件");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = equipmentService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FName"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNumber"));	    //2
			vpd.put("var3", varOList.get(i).getString("FDeptName"));	    //3
			//vpd.put("var4", varOList.get(i).getString("FStatus"));	    //4
			vpd.put("var4", varOList.get(i).getString("FCol"));	    //5
			vpd.put("var5", varOList.get(i).getString("FRow"));	    //6
			vpd.put("var6", varOList.get(i).getString("FModel"));	    //7
			vpd.put("var7", varOList.get(i).getString("Manufactor"));	    //8
			vpd.put("var8", varOList.get(i).getString("Remarks"));	    //9
			vpd.put("var9", varOList.get(i).getString("Location"));	    //10
			//vpd.put("var11", varOList.get(i).getString("IfWarning"));	    //11
			//vpd.put("var12", varOList.get(i).getString("Files"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
