package org.yy.controller.km;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.InputOutputService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：投入产出
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/InputOutput")
public class InputOutputController extends BaseController {
	
	@Autowired
	private InputOutputService InputOutputService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StaffService staffService;
	/**保存
	 * @author 管悦
	 * @date 2020-11-12
	 * @param 投入产出信息、WorkingProcedureExample_ID：工艺工序实例ID，TType：投入/产出
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("InputOutput:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String TType=pd.getString("TType");
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FOperatorID", staffService.getStaffId(pd).getString("STAFF_ID"));//操作人			
		if(!errInfo.equals("fail1")) {
			pd.put("InputOutput_ID", this.get32UUID());	//主键
			PageData pdSerialNum=InputOutputService.getSerialNum(pd);//生成序号
			if(pdSerialNum != null) {
				pd.put("SerialNum", pdSerialNum.get("SerialNum").toString());
			}
			pd.put("FStatus", "N");
			InputOutputService.save(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FOperatorID"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "生产BOM-投入产出");//功能项
			pdOp.put("OperationType", "新增");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("InputOutput_ID"));//删改数据ID	
			operationrecordService.save(pdOp);
		}
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("InputOutput:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		InputOutputService.delete(pd);
		operationrecordService.add("","生产BOM-投入产出","删除",pd.getString("InputOutput_ID"),staffid,"生产BOM-投入产出删除");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @author 管悦
	 * @date 2020-11-12
	 * @param 投入产出信息、InputOutput_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("InputOutput:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FOperatorID", staffService.getStaffId(pd).getString("STAFF_ID"));//操作人	
		String TType=pd.getString("TType");
		if(TType.equals("产出")) {
			if("主产出".equals(pd.getString("ProcessIType"))){
				PageData pdNum=InputOutputService.getOutNum(pd);//获取工序产出物料数量
				if(pdNum != null && Integer.parseInt(pdNum.get("OutNum").toString())>0) {
					errInfo = "fail1";//只能有一个产出物料
				}
			}
		}
		if(!errInfo.equals("fail1")) {
			InputOutputService.edit(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FOperatorID"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "生产BOM-投入产出");//功能项
			pdOp.put("OperationType", "修改");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("InputOutput_ID"));//删改数据ID	
			operationrecordService.save(pdOp);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**批量修改
	 * @author s
	 * @date 2021-2-4
	 * @param jsonArr
	 * @throws Exception
	 */
	@RequestMapping(value="/batchEdit")
	@ResponseBody
	public Object batchEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		JSONArray jarr = JSONArray.fromObject(pd.getString("unSaveArr"));
		for(int i=0;i<jarr.size();i++) {
			PageData temp = new PageData();
			JSONObject o = jarr.getJSONObject(i);
			Iterator it =o.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Object, Object> entry = (Entry<Object, Object>) it.next();
				temp.put(entry.getKey(), entry.getValue());
			}
			if(!temp.containsKey("FCount") || null==temp.get("FCount") || "".equals(temp.get("FCount").toString()))
				temp.put("FCount", 0);
			InputOutputService.edit(temp);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**投入产出列表
	 * @param WorkingProcedureExample_ID：工艺工序实例ID,FTYPE:投入、产出
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("InputOutput:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FOperatorID", staffService.getStaffId(pd).getString("STAFF_ID"));//操作人	
		page.setPd(pd);
		List<PageData>	varList = InputOutputService.list(page);	//列出InputOutput列表
		//插入操作日志
		//pd.put("FNAME", Jurisdiction.getName());
		//pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FOperatorID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "工序"+pd.get("FTYPE")+"列表");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);			
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/calculateInputOutputListByBomIdAndCount")
	@ResponseBody
	public Object calculateInputOutputListByBomIdAndCount(@RequestParam String BOM_ID,@RequestParam Double count) throws Exception{
	return InputOutputService.calculateInputOutputListByBomIdAndCount(BOM_ID, count);	
	}
	
	/**根据ID获取详情
	 * @author 管悦
	 * @date 2020-11-19
	 * @param 投入产出信息、InputOutput_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = InputOutputService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("InputOutput:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			InputOutputService.deleteAll(ArrayDATA_IDS);
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
		titles.add("工艺路线实例ID");	//1
		titles.add("序号");	//2
		titles.add("类型");	//3
		titles.add("产出类型");	//4
		titles.add("副产出");	//5
		titles.add("物料ID");	//6
		titles.add("数量");	//7
		titles.add("代替物料");	//8
		titles.add("状态");	//9
		titles.add("备注");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = InputOutputService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ProcessRouteExampleID"));	    //1
			vpd.put("var2", varOList.get(i).getString("SerialNum"));	    //2
			vpd.put("var3", varOList.get(i).getString("TType"));	    //3
			vpd.put("var4", varOList.get(i).getString("ProcessIType"));	    //4
			vpd.put("var5", varOList.get(i).getString("ByProduct"));	    //5
			vpd.put("var6", varOList.get(i).getString("MaterialID"));	    //6
			vpd.put("var7", varOList.get(i).get("FCount").toString());	//7
			vpd.put("var8", varOList.get(i).getString("SubstituteMaterial"));	    //8
			vpd.put("var9", varOList.get(i).getString("FStatus"));	    //9
			vpd.put("var10", varOList.get(i).getString("FExplanation"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-12
	 * @param FStatus：Y/N，InputOutput_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData iopd = InputOutputService.findById(pd);
		String FStatus= pd.getString("FStatus");
		if(FStatus.equals("N") && null!=iopd && "产出".equals(iopd.getString("TType"))){
			//String WorkingProcedureExample_ID = iopd.getString("WorkingProcedureExample_ID");
			PageData temp = new PageData();
			temp.put("InputOutput_ID", "");
			temp.put("WorkingProcedureExample_ID", iopd.getString("WorkingProcedureExample_ID"));
			PageData pdNum=InputOutputService.getOutNum(temp);//获取工序产出物料数量
			if(pdNum != null && Integer.parseInt(pdNum.get("OutNum").toString())>0) {
				errInfo = "fail1";//只能有一个产出物料
			}
		}
		if("success".equals(errInfo)) {
			InputOutputService.rowClose(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			if(FStatus.equals("Y")) {
				pdOp.put("OperationType", "行关闭");//操作类型
			} else {
				pdOp.put("OperationType", "反行关闭");//操作类型
			}
			pdOp.put("FNAME", Jurisdiction.getName());
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		    pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "投入产出明细");//功能项
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("InputOutput_ID"));//删改数据ID
			operationrecordService.save(pdOp);	
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
}
