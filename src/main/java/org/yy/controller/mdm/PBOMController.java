package org.yy.controller.mdm;

import net.sf.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.entity.PageData;
import org.yy.service.mdm.PBOMService;

/** 
 * 说明：配方管理
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 */
@Controller
@RequestMapping("/pbom")
public class PBOMController extends BaseController {
	
	@Autowired
	private PBOMService pbomService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("pbom:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PBOM_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		pd.put("FEXTEND2", "NO");//是否发布
		if(null!=pd.getString("PARENT_ID")&&"0".equals(pd.getString("PARENT_ID"))) {
			pd.put("PARENT_CODE", pd.getString("MAT_CODE"));//母件代码
			pd.put("ROOT_NODE_CODE", pd.getString("MAT_CODE"));//根节点代码
			pd.put("FEXTEND1", pd.getString("PBOM_ID"));//根节点主键
		}
		pbomService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("pbom:del")
	@ResponseBody
	public Object delete(@RequestParam String PBOM_ID) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd.put("PBOM_ID", PBOM_ID);
		pbomService.delete(pd);
		/*if(pbomService.listByParentId(PBOM_ID).size() > 0){		//判断是否有子级，是：不允许删除
			errInfo = "error";
		}else{
			pbomService.delete(pd);
		}*/
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("pbom:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pbomService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("pbom:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");								//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String PBOM_ID = null == pd.get("PBOM_ID")?"":pd.get("PBOM_ID").toString();
		pd.put("PBOM_ID", PBOM_ID);					//当作上级ID
		page.setPd(pd);
		PageData pdSuper=new PageData();
		Page pageSuper=new Page();
		pdSuper.put("PBOM_ID", PBOM_ID);
		pdSuper.put("FLAST", "FLAST");
		pageSuper.setPd(pdSuper);
		List<PageData>	varListSuper = pbomService.list(pageSuper);			//列出上层BOM列表
		List<PageData>	varList = pbomService.list(page);			//列出子层PBOM列表
		if("".equals(PBOM_ID) || "0".equals(PBOM_ID)) {
			map.put("PARENT_ID", "0");											//上级ID
		}else {
			map.put("PARENT_ID", pbomService.findById(pd).getString("PARENT_ID"));	//上级ID
		}
		map.put("varList", varList);
		map.put("varListSuper", varListSuper);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 鏄剧ず鍒楄〃ztree
	 * @return
	 */
	@RequestMapping(value="/listTree")
	//@RequiresPermissions("pbom:list")
	@ResponseBody
	public Object listTree()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//鍏抽敭璇嶆绱㈡潯浠�
		if(null==KEYWORDS||"".equals(KEYWORDS)) {
			pd.put("KEYWORDS", "VUENULL");
		}
		pd.put("parentId", "0");
		JSONArray arr = JSONArray.fromObject(pbomService.listTree(pd));
		String json = arr.toString();
		json = json.replaceAll("PBOM_ID", "id")
				.replaceAll("PARENT_ID", "pId")
				.replaceAll("FFNAME", "name")
				.replaceAll("subPBOM", "nodes")
				.replaceAll("hasPBOM", "checked")
				.replaceAll("treeurl", "url");
		map.put("zTreeNodes", json);
		map.put("result", errInfo);
		return map;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	//@RequiresPermissions("pbom:add")
	@ResponseBody
	public Object goAdd()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pds = new PageData();
		pd = this.getPageData();
		String PBOM_ID = null == pd.get("PBOM_ID")?"":pd.get("PBOM_ID").toString();
		pd.put("PBOM_ID", PBOM_ID);					//上级ID
		pds=pbomService.findById(pd);//查询上级信息
		if(null!=pds) {//判断是否顶级 null为顶级 不为null为子集
			pd.put("PARENT_ID", pds.getString("PBOM_ID"));//母件ID
			pd.put("PARENT_CODE", pds.getString("MAT_CODE"));//母件代码
			pd.put("ROOT_NODE_CODE", pds.getString("ROOT_NODE_CODE"));//根节点代码
			pd.put("FLEVEL", Integer.parseInt(pds.get("FLEVEL").toString())+1);//层次
			pd.put("FVERSIONS", pds.getString("FVERSIONS"));//版本号
			pd.put("FEXTEND1", pds.getString("FEXTEND1"));//根节点主键
		}else {
			pd.put("PARENT_ID", "0");//母件ID
			pd.put("PARENT_CODE", "");//母件代码
			pd.put("ROOT_NODE_CODE", "");//根节点代码
			pd.put("FLEVEL", "0");//层次
			pd.put("FVERSIONS", "1");//版本号
			pd.put("FEXTEND1", "");//根节点主键
		}
		map.put("pds",pds);					//传入上级所有信息
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**
	 * 选择通用BOM ztree
	 * @return
	 */
	@RequestMapping(value="/listTreeUniversal")
	//@RequiresPermissions("pbom:list")
	@ResponseBody
	public Object listTreeUniversal()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						
		if(null==KEYWORDS||"".equals(KEYWORDS)) {
			pd.put("KEYWORDS", "VUENULL");
		}
		pd.put("parentId", "0");
		JSONArray arr = JSONArray.fromObject(pbomService.listTree(pd));
		String json = arr.toString();
		json = json.replaceAll("PBOM_ID", "id")
				.replaceAll("PARENT_ID", "pId")
				.replaceAll("FFNAME", "name")
				.replaceAll("subPBOM", "nodes")
				.replaceAll("hasPBOM", "checked")
				.replaceAll("treeurl", "url");
		map.put("zTreeNodes", json);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("pbom:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = pbomService.findById(pd);									//根据ID读取		
		map.put("pd", pd);																//放入视图容器
		pd.put("PBOM_ID",pd.get("PARENT_ID").toString());					//用作上级信息
		map.put("pds",pbomService.findById(pd));							//传入上级所有信息
		map.put("result", errInfo);
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
		titles.add("产品名称");	//1
		titles.add("辅单位");	//2
		titles.add("名称");	//3
		titles.add("层次");	//4
		titles.add("物料代码");	//5
		titles.add("物料名称");	//6
		titles.add("数量");	//7
		titles.add("单位");	//8
		titles.add("数量上限");	//9
		titles.add("数量下限");	//10
		titles.add("规格");	//11
		titles.add("母件ID");	//12
		titles.add("母件代码");	//13
		titles.add("根节点代码");	//14
		titles.add("版本号");	//15
		titles.add("使用状态");	//16
		titles.add("标准号");	//17
		titles.add("转发号");	//18
		titles.add("创建人");	//19
		titles.add("创建时间");	//20
		titles.add("配方执行日期");	//21
		titles.add("批准人");	//22
		titles.add("配方指标1");	//23
		titles.add("配方指标2");	//24
		titles.add("配方指标3");	//25
		titles.add("配方指标4");	//26
		titles.add("扩展字段1");	//27
		titles.add("扩展字段2");	//28
		titles.add("扩展字段3");	//29
		titles.add("扩展字段4");	//30
		titles.add("扩展字段5");	//31
		dataMap.put("titles", titles);
		List<PageData> varOList = pbomService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PRODUCT_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("AUXILIARY_UNIT"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FLEVEL"));	    //4
			vpd.put("var5", varOList.get(i).getString("MAT_CODE"));	    //5
			vpd.put("var6", varOList.get(i).getString("MAT_NAME"));	    //6
			vpd.put("var7", varOList.get(i).get("FQTY").toString());	//7
			vpd.put("var8", varOList.get(i).getString("FUNIT"));	    //8
			vpd.put("var9", varOList.get(i).get("QTY_H").toString());	//9
			vpd.put("var10", varOList.get(i).get("QTY_L").toString());	//10
			vpd.put("var11", varOList.get(i).getString("FSPECS"));	    //11
			vpd.put("var12", varOList.get(i).getString("PARENT_ID"));	    //12
			vpd.put("var13", varOList.get(i).getString("PARENT_CODE"));	    //13
			vpd.put("var14", varOList.get(i).getString("ROOT_NODE_CODE"));	    //14
			vpd.put("var15", varOList.get(i).getString("FVERSIONS"));	    //15
			vpd.put("var16", varOList.get(i).getString("FSTATE"));	    //16
			vpd.put("var17", varOList.get(i).getString("STANDARD_NUMBER"));	    //17
			vpd.put("var18", varOList.get(i).getString("FORWARD_NO"));	    //18
			vpd.put("var19", varOList.get(i).getString("FCREATOR"));	    //19
			vpd.put("var20", varOList.get(i).getString("CREATE_TIME"));	    //20
			vpd.put("var21", varOList.get(i).getString("RECIPE_EXECUTION_DATE"));	    //21
			vpd.put("var22", varOList.get(i).getString("FAPPROVER"));	    //22
			vpd.put("var23", varOList.get(i).getString("BOM_KPI1"));	    //23
			vpd.put("var24", varOList.get(i).getString("BOM_KPI2"));	    //24
			vpd.put("var25", varOList.get(i).getString("BOM_KPI3"));	    //25
			vpd.put("var26", varOList.get(i).getString("BOM_KPI4"));	    //26
			vpd.put("var27", varOList.get(i).getString("FEXTEND1"));	    //27
			vpd.put("var28", varOList.get(i).getString("FEXTEND2"));	    //28
			vpd.put("var29", varOList.get(i).getString("FEXTEND3"));	    //29
			vpd.put("var30", varOList.get(i).getString("FEXTEND4"));	    //30
			vpd.put("var31", varOList.get(i).getString("FEXTEND5"));	    //31
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**消耗产出模板phase用
	 * 显示列表ztree
	 * @return
	 */
	
	@RequestMapping(value="/listTreeNew")
	@ResponseBody
	public Object listTreeNew()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//鍏抽敭璇嶆绱㈡潯浠�
		if(null==KEYWORDS||"".equals(KEYWORDS)) {
			pd.put("KEYWORDS", "VUENULL");
		}
		pd.put("parentId", "0");
		JSONArray arr = JSONArray.fromObject(pbomService.listTreeNew(pd));
		String json = arr.toString();
		json = json.replaceAll("PBOM_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("FFNAME", "name").replaceAll("subPBOM", "nodes").replaceAll("hasPBOM", "checked").replaceAll("treeurl", "url111");
		map.put("zTreeNodes", json);
		map.put("result", errInfo);
		return map;
	}
	
	/**发布
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/issue")
	@ResponseBody
	public Object issue()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			PageData pbomPd = pbomService.findById(pd);									//根据ID读取		
			pd.put("FEXTEND1", pbomPd.getString("PBOM_ID"));//配方顶级主键
			pd.put("FVERSIONS", Double.parseDouble(pbomPd.getString("FVERSIONS").toString())+0.1);//版本号
			pd.put("FEXTEND2", "YES");//发布状态
			pbomService.editVersions(pd);//更新配方版本号
			pbomService.editState(pd);//更新配方发布状态
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errInfo = "error";
		}
		map.put("pd", pd);																//放入视图容器
		map.put("result", errInfo);
		return map;
	}	
	
	/**取消发布
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/notIssue")
	@ResponseBody
	public Object notIssue()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd.put("FEXTEND1", pd.getString("PBOM_ID"));//配方顶级主键
			pd.put("FEXTEND2", "NO");//发布状态
			List<PageData>	varList = pbomService.listAllId(pd);//通过顶级ID查询列表
			if(varList.size()>0) {//判断列表条数是否大于0
				for (int i = 0; i < varList.size(); i++) {//如果大于0循环列表里数据插入到归档表中
					PageData pbomPd = varList.get(i);
					pbomPd.put("PBOM_ID", pbomPd.getString("PBOM_ID")+pbomPd.getString("FVERSIONS"));
					pbomPd.put("FCREATOR", Jurisdiction.getName());//创建人
					pbomPd.put("CREATE_TIME",Tools.date2Str(new Date()));//创建时间
					pbomService.saveGD(pbomPd);//归档配方 往归档表里插入数据
				}
			}
			pbomService.editState(pd);//更新配方发布状态
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errInfo = "error";
		}
		map.put("pd", pd);																//放入视图容器
		map.put("result", errInfo);
		return map;
	}	
	
	/**复制
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/copy")
	@ResponseBody
	public Object copy() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		pd = this.getPageData();
		//新增配方信息
		String newRootId =  this.get32UUID();				//新的根节点id
		pd.put("PBOM_ID",newRootId);	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		pd.put("FEXTEND2", "NO");//是否发布
		if(null!=pd.getString("PARENT_ID")&&"0".equals(pd.getString("PARENT_ID"))) {
			pd.put("PARENT_CODE", pd.getString("MAT_CODE"));//母件代码
			pd.put("ROOT_NODE_CODE", pd.getString("MAT_CODE"));//根节点代码
			pd.put("FEXTEND1", pd.getString("PBOM_ID"));//根节点主键
		}
		pbomService.save(pd);
		String PBOMX_ID=pd.getString("PBOMX_ID");
		PageData pdX=new PageData();
		pdX.put("FEXTEND1", PBOMX_ID);//根节点ID
		//pdX.put("PBOM_ID", PBOMX_ID);//根节点ID
		//pdX=pbomService.findById(pdX);
		//插入配方明细
		String PRODUCT_NO=pd.getString("MAT_CODE");
		List<PageData> copylist = pbomService.listAllCopy(pdX);	//根据根节查询所有
		for(PageData copypd:copylist){						//添加复制表数据
			copypd.put("ROOT_NODE_CODE", PRODUCT_NO);//根节点
			copypd.put("FEXTEND1", newRootId);//根节点主键
			if("0".equals(copypd.get("PARENT_ID").toString().trim())){
				continue;
			}
			if("1".equals(copypd.getString("FLEVEL").toString().trim())){
				copypd.put("PARENT_CODE", PRODUCT_NO);
				copypd.put("PARENT_ID", newRootId);
			}
			copypd.put("FEXTEND2", "NO");
			pbomService.savecopy(copypd);//临时表插入中间数据
		}
		
		for(int i=0;i<copylist.size();i++) {				//更新主键和PARENT_ID
			PageData copypd = copylist.get(i);
			if("0".equals(copypd.get("PARENT_ID").toString().trim())) {
				copypd.put("NEW_PBOM_ID", newRootId);
			} else {
				copypd.put("NEW_PBOM_ID", this.get32UUID());
			}
			pbomService.updateBomIdAndPid(copypd);
		}
		List<PageData> list = pbomService.copylistAll(pd);	//根据新根节点id查询全部复制数据
		String dateStr = Tools.date2Str(new Date());
		for(PageData p:list) {
			p.put("FCREATOR", Jurisdiction.getName());
			p.put("CREATE_TIME", dateStr);
			pbomService.save(p);
		}
		pbomService.deletecopylistAll(pd);
		map.put("result", errInfo);
		return map;
	}
	/**更换母件
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/changeParent")
	@ResponseBody
	public Object changeParent() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		pd = this.getPageData();
		PageData pdParent=new PageData();
		pdParent.put("PBOM_ID", pd.getString("PARENT_ID"));
		pdParent=pbomService.findById(pdParent);
		PageData pdNow=new PageData();
		pdNow.put("PBOM_ID", pd.getString("PBOM_ID"));
		pdNow=pbomService.findById(pdNow);
		//更换母件-更新本级父级节点和层次
		int FLEVELParent=Integer.parseInt(pdParent.get("FLEVEL").toString());
		int FLEVELNow=Integer.parseInt(pdNow.get("FLEVEL").toString());
		int FLEVEL=FLEVELNow-(FLEVELNow-FLEVELParent-1);
		PageData pdUpNow=new PageData();
		pdUpNow.put("PBOM_ID", pd.getString("PBOM_ID"));
		pdUpNow.put("FLEVEL",FLEVEL);
		pdUpNow.put("PARENT_ID", pdParent.getString("PBOM_ID"));
		pdUpNow.put("FEXTEND1", pdParent.getString("FEXTEND1"));
		pdUpNow.put("ROOT_NODE_CODE", pdParent.getString("ROOT_NODE_CODE"));
		pdUpNow.put("PARENT_CODE", pdParent.getString("MAT_CODE"));
		pbomService.updateParent(pdUpNow);
		//更新子级根节点和层次
		PageData pdSon=new PageData();
		pdSon.put("parentId", pd.getString("PBOM_ID"));
		pdSon.put("DIFFLEVEL", FLEVELNow-FLEVELParent-1);
		pdSon.put("FEXTEND1", pdParent.getString("FEXTEND1"));
		pdSon.put("ROOT_NODE_CODE", pdParent.getString("ROOT_NODE_CODE"));
		pbomService.updateRoot(pdSon);
		map.put("result", errInfo);
		return map;
	}
}
