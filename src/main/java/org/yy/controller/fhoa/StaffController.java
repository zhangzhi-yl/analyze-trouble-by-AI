package org.yy.controller.fhoa;

import java.util.*;

import net.sf.json.JSONArray;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.DatajurService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.StaffService;
import org.yy.util.Jurisdiction;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
//import org.yy.util.TwoDimensionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：员工管理
 * 作者：YuanYes Q356703572
 * 官网：www.yysystem. org
 */
@Controller
@RequestMapping("/staff")
public class StaffController extends BaseController {
	
	@Autowired
	private StaffService staffService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private DatajurService datajurService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("staff:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("STAFF_ID", this.get32UUID());			//主键
		pd.put("USER_ID", "");							//绑定账号ID
		pd.put("CreateTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		staffService.save(pd);							//保存员工信息到员工表
		String DEPARTMENT_IDS = departmentService.getDEPARTMENT_IDS(pd.getString("DEPARTMENT_ID"));//获取某个部门所有下级部门ID
		pd.put("DATAJUR_ID", pd.getString("STAFF_ID")); //主键
		pd.put("DEPARTMENT_IDS", DEPARTMENT_IDS);		//部门ID集
		datajurService.save(pd);						//把此员工默认部门及以下部门ID保存到组织数据权限表
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("staff:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		staffService.delete(pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("staff:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		staffService.edit(pd);
		String DEPARTMENT_IDS = departmentService.getDEPARTMENT_IDS(pd.getString("DEPARTMENT_ID"));//获取某个部门所有下级部门ID
		pd.put("DATAJUR_ID", pd.getString("STAFF_ID")); //主键
		pd.put("DEPARTMENT_IDS", DEPARTMENT_IDS);		//部门ID集
		datajurService.edit(pd);						//把此员工默认部门及以下部门ID保存到组织数据权限表
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**列表(检索条件中的部门，只列出此操作用户最高部门权限以下所有部门的员工)
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("staff:list")  //这个地方就不用加了，避免无此菜单权限的地方调用，比如添加工资单的地方选择员工的地方
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");								//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("keywords", KEYWORDS.trim());
		String DEPARTMENT_ID = pd.getString("DEPARTMENT_ID");
		pd.put("DEPARTMENT_ID",  ("".equals(DEPARTMENT_ID) || null==DEPARTMENT_ID)?Jurisdiction.getDEPARTMENT_ID():DEPARTMENT_ID);	//只有检索条件传过值时，才不为"",否则读取缓存
		pd.put("item", (("".equals(DEPARTMENT_ID))?Jurisdiction.getDEPARTMENT_IDS():departmentService.getDEPARTMENT_IDS(pd.getString("DEPARTMENT_ID"))));	//部门检索条件,列出此部门下级所属部门的员工
		if("无权".equals(Jurisdiction.getDEPARTMENT_ID())) {
			pd.put("item","('fh')");
		}
		/* 比如员工 张三 拥有部门权限的部门为 A ， A 的下级有  C , D ,F 只列出A以下部门的员工，不列出部门为A的员工，当部门检索条件值为C时，可以列出C及C以下员工 */
		if(!"".equals(DEPARTMENT_ID))pd.put("item", pd.getString("item").replaceFirst("\\(", "\\('"+DEPARTMENT_ID+"',"));
		page.setPd(pd);
		List<PageData>	varList = staffService.list(page);	//列出Staff列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);							//返回结果
		return map;
	}
	
	/**获取部门下拉树
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDepartmenttree")
	@ResponseBody
	public Object getDepartmenttree()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		//列表页面树形下拉框用(保持下拉树里面的数据不变)
		String ZDEPARTMENT_ID = "";//Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", "{\"treeNodes\":" + arr.toString() + "}");
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@ResponseBody
	public Object goAdd()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		String ZDEPARTMENT_ID = "";//Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", (null == arr ?"":"{\"treeNodes\":" + arr.toString() + "}"));
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		String ZDEPARTMENT_ID = "";//Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", (null == arr ?"":"{\"treeNodes\":" + arr.toString() + "}"));
		pd = staffService.findById(pd);	//根据ID读取
		PageData dpd = new PageData();
		dpd = departmentService.findById(pd);
		String depname = null == dpd?"请选择":dpd.getString("NAME");
		map.put("depname", null == depname?"请选择":depname);
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("staff:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			staffService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**绑定用户
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/userBinding")
	@ResponseBody
	public Object userBinding() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		staffService.userBinding(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**通过用户名获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/findByUsername")
	@ResponseBody
	public Object findByUsername()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = staffService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);		//返回结果
		return map;
	}
	
	/**用户列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/staffList")
	@ResponseBody
	public Object staffList(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = staffService.list(page);	//列出Staff列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);		//返回结果
		return map;
	}
	
	/**获取人员表数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getUser")
	@ResponseBody
	public Object getUser() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> roleList = staffService.listAll(pd);		//列出所有系统用户
		map.put("roleList", roleList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**生成二维码
	 * @param args
	 * @throws Exception
	 */
	@RequestMapping(value="/createTwoDimensionCode")
	@ResponseBody
	public Object createTwoDimensionCode() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(null!=pd.getString("STAFF_ID")&&!"".equals(pd.getString("STAFF_ID"))) {
			pd=staffService.findById(pd);	//根据ID读取
		}
		String errInfo = "success", 
		encoderImgId = this.get32UUID()+".png"; //encoderImgId此处二维码的图片名
		String encoderContent = pd.getString("NAME")+","+pd.getString("USER_ID")+","+pd.getString("BIANMA");				//内容
		if(null == encoderContent){
			errInfo = "error";
		}else{
			try {
				String filePath = PathUtil.getProjectpath() + "uploadFiles/staffTwoDimensionCode/" + encoderImgId;  //存放路径
			//	TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");							//执行生成二维码
			} catch (Exception e) {
				errInfo = "error";
			}
		}
		map.put("result", errInfo);						//返回结果
		map.put("encoderImgId", encoderImgId);			//二维码图片名
		return map;
	}
	
	/**根据登录名查询职员ID
	 * @author 管悦
	 * @date 2020-11-06
	 * @param FNAME:登录人姓名
	 * @throws Exception
	 */
	@RequestMapping(value="/getStaffId")
	@ResponseBody
	public Object getStaffId()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = staffService.getStaffId(pd);
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**获取职员列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-09
	 * @param KEYWORDS:登录人姓名
	 * @throws Exception
	 */
	@RequestMapping(value="/getStaffList")
	@ResponseBody
	public Object getStaffList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-员工名
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = staffService.getStaffList(pd);	//列出员工列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
