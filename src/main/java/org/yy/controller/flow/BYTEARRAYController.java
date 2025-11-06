package org.yy.controller.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.yy.util.UuidUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.TECHNOLOGY_FLOWService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：流程图文件
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/BYTEARRAY")
public class BYTEARRAYController extends BaseController {
	
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private WorkingProcedureService WorkingProcedureService;//工艺路线工序
	
	@Autowired
	private TECHNOLOGY_FLOWService TECHNOLOGY_FLOWService;//工艺工序节点
	
	/**保存工艺工序
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/createTechnology")
	//@RequiresPermissions("BYTEARRAY:add")
	@ResponseBody
	public Object create() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String time = Tools.date2Str(new Date());
		String ProcessRoute_ID = pd.getString("ProcessRoute_ID");//工艺路线ID
		Map<String,String> cmap = new HashMap<String,String>();//节点和线
		Map<String,String> dmap = new HashMap<String,String>();
		List<JSONObject> list = new ArrayList<>();
		if(null != pd.get("TEXT_BYTE_STREAM")) {
			List<PageData> nodeAndLine = TECHNOLOGY_FLOWService.findByProcessRoute_ID(pd);//根据工艺路线id查询出所有工艺路线流程节点及连接线
			JSONObject byteArray = new JSONObject().fromObject(pd.getString("TEXT_BYTE_STREAM"));
			JSONObject nodes = byteArray.getJSONObject("nodes");//获得点
			Iterator<String> nodekeys = nodes.keys(); 
			while(nodekeys.hasNext()){	// 获得所有node的key
				String key = nodekeys.next(); 
				JSONObject jsonNode = new JSONObject().fromObject(nodes.getString(key));   
				jsonNode.put("kind", "node");
				jsonNode.put("NODE_ID", key);
				list.add(jsonNode);
				cmap.put(key, key);
			}
			JSONObject lines = byteArray.getJSONObject("lines");//获得线
			Iterator<String> linekeys = lines.keys(); 
			while(linekeys.hasNext()){// 获得所有line的key
				String key = linekeys.next(); 
				JSONObject jsonline = new JSONObject().fromObject(lines.getString(key));   
				jsonline.put("kind", "line");
				jsonline.put("NODE_ID", key);
				list.add(jsonline);
				cmap.put(key, key);
			}
			String[] idarr=new String[nodeAndLine.size()];
			if(nodeAndLine.isEmpty()) {	//如果工艺路线下没有节点和连接线
				//添加所有节点及线
				for(JSONObject o:list) {
					addChildForTechnologyFlow(ProcessRoute_ID,time,o);
				}
			} else {	//如果工艺路线下有节点和链接线，判断是否要删除该节点和连接线
				for(int i=0;i<nodeAndLine.size();i++) {
					if(cmap.containsKey(nodeAndLine.get(i).getString("NODE_ID"))) {
						for(int j = list.size() - 1; j >= 0; j--){
							if(nodeAndLine.get(i).getString("NODE_ID").equals(list.get(j).get("NODE_ID"))){
								list.remove(list.get(j));
							}
						}
						//添加或者修改节点和线，需要判断是添加节点和线还是该修改节点和线
						//根据工艺路线ID和节点ID查询是否存在数据，
						if(nodeAndLine.get(i).getString("NODE_KIND").equals("node")) {	//节点
							JSONObject j = new JSONObject().fromObject(nodes.getString(nodeAndLine.get(i).getString("NODE_ID")));
							j.put("ProcessRoute_ID", ProcessRoute_ID);
							j.put("NODE_ID", nodeAndLine.get(i).getString("NODE_ID"));
							j.put("kind", "node");
							j.put("FCREATOR", nodeAndLine.get(i).getString("FCREATOR"));
							j.put("CREATE_TIME", nodeAndLine.get(i).getString("CREATE_TIME"));
							j.put("TECHNOLOGY_FLOW_ID", nodeAndLine.get(i).getString("TECHNOLOGY_FLOW_ID"));
							editChildForTechnologyFlow(ProcessRoute_ID,time,j);
						} else {	//线
							JSONObject j = new JSONObject().fromObject(lines.getString(nodeAndLine.get(i).getString("NODE_ID")));
							j.put("ProcessRoute_ID", ProcessRoute_ID);
							j.put("NODE_ID", nodeAndLine.get(i).getString("NODE_ID"));
							j.put("kind", "line");
							j.put("FCREATOR", nodeAndLine.get(i).getString("FCREATOR"));
							j.put("CREATE_TIME", nodeAndLine.get(i).getString("CREATE_TIME"));
							j.put("TECHNOLOGY_FLOW_ID", nodeAndLine.get(i).getString("TECHNOLOGY_FLOW_ID"));
							editChildForTechnologyFlow(ProcessRoute_ID,time,j);
						}
						
					} else {	//流程图中没有这个节点或者连接线，删除该数据，同时删除工艺工序
						idarr[i]=nodeAndLine.get(i).getString("TECHNOLOGY_FLOW_ID");
					}
				}
				deleteChild(idarr);//删除节点和线
				for(JSONObject o:list) {//添加
					addChildForTechnologyFlow(ProcessRoute_ID,time,o);
				}
			}
			pd.put("PID", ProcessRoute_ID);
			PageData BYTEARRAYpd = BYTEARRAYService.findByPID(pd);//查询流程图文件，准备更新流程图
			if(null!=BYTEARRAYpd) {
				BYTEARRAYpd.put("TEXT_BYTE_STREAM", pd.get("TEXT_BYTE_STREAM"));
				BYTEARRAYpd.put("LAST_MODIFIED_TIME", time);
				BYTEARRAYpd.put("LAST_MODIFIER", "");//TODO
				BYTEARRAYService.edit(BYTEARRAYpd);//更新流程图
			}
		}
		map.put("result", errInfo);
		map.put("BYTEARRAY_ID", pd.get("BYTEARRAY_ID"));
		return map;
	}
	
	/**
	 * 	删除工艺工序节点或者线
	 * 	根据id删除TECHNOLOGY_FLOW下的node或者line
	 * @param jsonNode
	 * @throws Exception 
	 */
	public void deleteChild(String[] ArrayDATA_IDS) throws Exception {
		if(ArrayDATA_IDS.length>0 && !"".equals(ArrayDATA_IDS[0])){
			TECHNOLOGY_FLOWService.deleteAll(ArrayDATA_IDS);
		}
	}
	
	/**
	 * 	工艺工序修改节点或者线
	 * 	修改TECHNOLOGY_FLOW下的node或者line
	 * @param ProcessRoute_ID
	 * @param time
	 * @param jsonNode
	 * @throws Exception 
	 */
	public void editChildForTechnologyFlow(String ProcessRoute_ID,String time,JSONObject json) throws Exception {
		PageData tempnode=new PageData();
		tempnode.put("NODE_ID", json.optString("NODE_ID", ""));
		tempnode.put("NODE_NAME", json.optString("name", ""));
		tempnode.put("NODE_KIND", json.optString("kind", ""));
		tempnode.put("NODE_TYPE", json.optString("type", ""));
		tempnode.put("PHASE_TYPE", "");
		tempnode.put("JUMP_CONDITION", "");
		tempnode.put("BEGIN_NODE", json.optString("from", ""));
		tempnode.put("END_NODE", json.optString("to", ""));
		tempnode.put("FPARTICIPATOR", "");
		tempnode.put("FDES", "");
		tempnode.put("FCREATOR", "");//TODO
		tempnode.put("CREATE_TIME", time);
		tempnode.put("LAST_MODIFIER", "");//TODO
		tempnode.put("LAST_MODIFIED_TIME", time);
		tempnode.put("ProcessRoute_ID", ProcessRoute_ID);
		tempnode.put("TECHNOLOGY_FLOW_ID", json.optString("TECHNOLOGY_FLOW_ID"));
		TECHNOLOGY_FLOWService.edit(tempnode);
	}
	
	/**
	 * 	工艺工序添加节点或者线
	 * 	为TECHNOLOGY_FLOW添加node或者line
	 * @param ProcessRoute_ID
	 * @param time
	 * @param jsonNode
	 * @throws Exception 
	 */
	public void addChildForTechnologyFlow(String ProcessRoute_ID,String time,JSONObject json) throws Exception {
		PageData tempnode=new PageData();
		tempnode.put("NODE_ID", json.optString("NODE_ID", ""));
		tempnode.put("NODE_NAME", json.optString("name", ""));
		tempnode.put("NODE_KIND", json.optString("kind", ""));
		tempnode.put("NODE_TYPE", json.optString("type", ""));
		tempnode.put("PHASE_TYPE", "");
		tempnode.put("JUMP_CONDITION", "");
		tempnode.put("BEGIN_NODE", json.optString("from", ""));
		tempnode.put("END_NODE", json.optString("to", ""));
		tempnode.put("FPARTICIPATOR", "");
		tempnode.put("FDES", "");
		tempnode.put("FCREATOR", "");//TODO
		tempnode.put("CREATE_TIME", time);
		tempnode.put("LAST_MODIFIER", "");//TODO
		tempnode.put("LAST_MODIFIED_TIME", time);
		tempnode.put("ProcessRoute_ID", ProcessRoute_ID);
		tempnode.put("TECHNOLOGY_FLOW_ID", UuidUtil.get32UUID());
		TECHNOLOGY_FLOWService.save(tempnode);
	}
	
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
		pd = this.getPageData();
		pd.put("BYTEARRAY_ID", this.get32UUID());	//主键
		BYTEARRAYService.save(pd);
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
		PageData pd = new PageData();
		pd = this.getPageData();
		BYTEARRAYService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**根据pid删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteByPid")
	//@RequiresPermissions("BYTEARRAY:del")
	@ResponseBody
	public Object deleteByPid() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		BYTEARRAYService.deleteByPidAndFTYPE(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**根据pid修改JSON
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editJson")
	//@RequiresPermissions("BYTEARRAY:edit")
	@ResponseBody
	public Object editJson() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		BYTEARRAYService.editJson(pd);
		map.put("result", errInfo);
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
		BYTEARRAYService.edit(pd);
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
		List<PageData>	varList = BYTEARRAYService.list(page);	//列出BYTEARRAY列表
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
		pd = BYTEARRAYService.findById(pd);	//根据ID读取
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
			BYTEARRAYService.deleteAll(ArrayDATA_IDS);
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
		titles.add("关联ID");	//1
		titles.add("类型");	//2
		titles.add("资源文件名称");	//3
		titles.add("存储文本字节流");	//4
		titles.add("最后修改时间");	//5
		titles.add("最后修改人");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = BYTEARRAYService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FTYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("RESOURCE_FILE_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("TEXT_BYTE_STREAM"));	    //4
			vpd.put("var5", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("LAST_MODIFIER"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
