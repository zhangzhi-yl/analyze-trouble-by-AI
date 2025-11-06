package org.yy.controller.km;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.SupplierService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：供应商
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/supplier")
public class SupplierController extends BaseController {
	@Autowired
	private StaffService staffService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private OperationRecordService operationrecordService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		Date date = new Date();
		PageData num = new PageData();
		num = supplierService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("Supplier_ID", this.get32UUID());				//主键
			pd.put("FNAME", Jurisdiction.getName());				//创建人
			String userid=staffService.getStaffId(pd).getString("STAFF_ID");
			pd.put("FCreatePersonID", userid);						//操作人
			pd.put("FCreateTime", Tools.date2Str(date));			//创建时间
			pd.put("LastModifiedBy", userid);						//最后修改人
			pd.put("LastModificationTime", Tools.date2Str(date));	//最后修改时间
			supplierService.save(pd);
			operationrecordService.add("","供应商","添加",pd.getString("Supplier_ID"),staffid,"供应商添加");
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
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
		supplierService.delete(pd);
		operationrecordService.add("","供应商","删除",pd.getString("Supplier_ID"),staffid,"供应商删除");
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
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = supplierService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("FNAME", Jurisdiction.getName());				//当前用户
			String STAFF_ID=staffService.getStaffId(pd).getString("STAFF_ID");
			pd.put("LastModifiedBy", STAFF_ID);						//最后修改人
			pd.put("LastModificationTime", Tools.date2Str(new Date()));	//最后修改时间
			supplierService.edit(pd);
			operationrecordService.add("","供应商","修改",pd.getString("Supplier_ID"),staffid,"供应商修改");
		}
		map.put("result", errInfo);
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
		List<PageData>	varList = supplierService.list(page);	//列出Supplier列表
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
		pd = supplierService.findById(pd);	//根据ID读取
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
			supplierService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("名称");		//1
		titles.add("编码");		//2
		titles.add("所在城市");	//3
		titles.add("隶属行业");	//4
		titles.add("联系人");		//5
		titles.add("联系电话");	//6
		titles.add("地址");		//7
		titles.add("类型");		//8
		titles.add("使用状态");	//9
		titles.add("描述");		//10
		titles.add("创建人");		//11
		titles.add("创建时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = supplierService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FName"));	    			//1
			vpd.put("var2", varOList.get(i).getString("FNum"));	    				//2
			vpd.put("var3", varOList.get(i).getString("FCity"));	    			//3
			vpd.put("var4", varOList.get(i).getString("SubordinateIndustry"));	    //4
			vpd.put("var5", varOList.get(i).getString("FContacts"));	    		//5
			vpd.put("var6", varOList.get(i).getString("ContactPhone"));	    		//6
			vpd.put("var7", varOList.get(i).getString("FAddress"));	    			//7
			vpd.put("var8", varOList.get(i).getString("TType"));	    			//8
			vpd.put("var9", varOList.get(i).getString("FStatus"));	    			//9
			vpd.put("var10", varOList.get(i).getString("FDescribe"));	    		//10
			vpd.put("var11", varOList.get(i).getString("FCreatePersonID"));	    	//11
			vpd.put("var12", varOList.get(i).getString("FCreateTime"));	    		//12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**获取供应商列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-09
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getSupplierList")
	@ResponseBody
	public Object getSupplierList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-供应商名/供应商编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = supplierService.getSupplierList(pd);	//列出供应商列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
