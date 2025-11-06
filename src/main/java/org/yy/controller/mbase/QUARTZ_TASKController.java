package org.yy.controller.mbase;

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
import org.quartz.Job;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.QuartzManager;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mbase.QUARTZ_TASKService;

/** 
 * 说明：定时任务
 * 执行方法类必需实现org.quartz.Job接口，重写execute方法实现， 在该方法实现中编写自己的任务代码实现逻辑。
 * 如果任务方法执行中出现异常必须捕获异常，应做资源回收操作且关闭任务。参照：org.yy.controller.fhdb.DbBackupQuartzJob
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/quartz_task")
public class QUARTZ_TASKController extends BaseController {
	
	@Autowired
	private QUARTZ_TASKService quartz_taskService;
	private static String JOB_GROUP_NAME = "QUARTZ_TASK_JOBGROUP_NAME";  					//任务组
    private static String TRIGGER_GROUP_NAME = "QUARTZ_TASK_TRIGGERGROUP_NAME";  			//触发器组
	
	/**新增任务
	 * @param JOBNAME	任务名称
	 * @param FHTIME 	时间规则
	 * @param EXECUTE_ARGS 传的参数,JSON字符串
	 */
	public void addJob(String JOBNAME, String FHTIME, String EXECUTE_ARGS, Class<? extends Job> clazz){
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("EXECUTE_ARGS", EXECUTE_ARGS);
		QuartzManager.addJob(JOBNAME,JOB_GROUP_NAME, JOBNAME, TRIGGER_GROUP_NAME, clazz, FHTIME ,parameter); 
	}
	
	/**删除任务
	 * @param JOBNAME 任务名称
	 */
	public void removeJob(String JOBNAME){
		QuartzManager.removeJob(JOBNAME, JOB_GROUP_NAME,JOBNAME, TRIGGER_GROUP_NAME);
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("quartz_task:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String QUARTZ_TASK_ID = this.get32UUID();
		String JOBNAME = pd.getString("JOBNAME")+"_"+Tools.date2Str(new Date(),"yyMMddHHmmss");//任务名称
		String FHTIME = pd.getString("FHTIME");									//时间规则
		//String TABLENAME = pd.getString("TABLENAME");							//表名or整库(all)
		String METHODNAME = pd.getString("METHODNAME");							//执行方法类全名
		String EXECUTE_ARGS = pd.getString("EXECUTE_ARGS");						//执行方法参数
		pd.put("QUARTZ_TASK_ID", QUARTZ_TASK_ID);								//主键
		pd.put("JOBNAME", JOBNAME);												//任务名称
		pd.put("CREATE_TIME", DateUtil.date2Str(new Date()));					//创建时间
		pd.put("STATUS", "1");													//状态
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(METHODNAME);
			this.addJob(JOBNAME, FHTIME, EXECUTE_ARGS, clazz);
			quartz_taskService.save(pd);
		}catch(java.lang.ClassNotFoundException e) {
			errInfo = "ClassNotFoundException";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("quartz_task:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		this.removeJob(quartz_taskService.findById(pd).getString("JOBNAME"));	//删除任务
		quartz_taskService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**切换状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus")
//	@RequiresPermissions("quartz_task:edit")
	@ResponseBody
	public Object changeStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		int STATUS = Integer.parseInt(pd.get("STATUS").toString());
		pd = quartz_taskService.findById(pd);			//根据ID读取
		if(STATUS == 2){
			pd.put("STATUS", 2);
			this.removeJob(pd.getString("JOBNAME"));	//删除任务
		}else{
			pd.put("STATUS", 1);
			String JOBNAME = pd.getString("JOBNAME");						//任务名称
			String FHTIME = pd.getString("FHTIME");							//时间规则
			//String TABLENAME = pd.getString("TABLENAME");					//表名or整库(all)
			//String QUARTZ_TASK_ID = pd.getString("QUARTZ_TASK_ID");			//任务记录的ID
			String METHODNAME = pd.getString("METHODNAME");					//执行方法类全名
			String EXECUTE_ARGS = pd.getString("EXECUTE_ARGS");				//执行方法参数
			try {
				@SuppressWarnings({ "unchecked" })
				Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(METHODNAME);
				this.addJob(JOBNAME, FHTIME, EXECUTE_ARGS, clazz);
			}catch(Exception e) {//java.lang.ClassNotFoundException
				errInfo = "error";
			}
		}
		quartz_taskService.changeStatus(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
//	@RequiresPermissions("quartz_task:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		this.removeJob(quartz_taskService.findById(pd).getString("JOBNAME"));
		String QUARTZ_TASK_ID = pd.getString("QUARTZ_TASK_ID");
		String JOBNAME = pd.getString("JOBNAME");//任务名称
		String FHTIME = pd.getString("FHTIME");									//时间规则
		//String TABLENAME = pd.getString("TABLENAME");							//表名or整库(all)
		String METHODNAME = pd.getString("METHODNAME");							//执行方法类全名
		String EXECUTE_ARGS = pd.getString("EXECUTE_ARGS");						//执行方法参数
		pd.put("QUARTZ_TASK_ID", QUARTZ_TASK_ID);								//主键
		pd.put("JOBNAME", JOBNAME);												//任务名称
		pd.put("CREATE_TIME", DateUtil.date2Str(new Date()));					//创建时间
		pd.put("STATUS", "1");													//状态
		try {
			@SuppressWarnings({ "unchecked" })
			Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(METHODNAME);
			this.addJob(JOBNAME, FHTIME, EXECUTE_ARGS, clazz);
			quartz_taskService.edit(pd);
		}catch(java.lang.ClassNotFoundException e) {
			errInfo = "ClassNotFoundException";
		}		
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
//	@RequiresPermissions("quartz_task:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = quartz_taskService.list(page);	//列出QUARTZ_TASK列表
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
//	@RequiresPermissions("quartz_task:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = quartz_taskService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("quartz_task:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			for(int i=0;i<ArrayDATA_IDS.length;i++){
				pd.put("QUARTZ_TASK_ID", ArrayDATA_IDS[i]);
				this.removeJob(quartz_taskService.findById(pd).getString("JOBNAME"));	//删除任务
			}
			quartz_taskService.deleteAll(ArrayDATA_IDS);
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
		titles.add("任务ID");	//1
		titles.add("任务名称");	//2
		titles.add("创建时间");	//3
		titles.add("表名");	//4
		titles.add("状态");	//5
		titles.add("时间规则");	//6
		titles.add("解释说明");	//7
		titles.add("备注");	//8
		titles.add("执行方法");	//9
		titles.add("方法参数");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = quartz_taskService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("QUARTZ_TASK_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("JOBNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("CREATE_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("TABLENAME"));	    //4
			vpd.put("var5", varOList.get(i).get("STATUS").toString());	//5
			vpd.put("var6", varOList.get(i).getString("FHTIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("TIMEEXPLAIN"));	    //7
			vpd.put("var8", varOList.get(i).getString("BZ"));	    //8
			vpd.put("var9", varOList.get(i).getString("METHODNAME"));	    //9
			vpd.put("var10", varOList.get(i).getString("EXECUTE_ARGS"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
