package org.yy.controller.project.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.yy.service.project.manager.PROTEAMService;

/** 
 * 说明：项目成员
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-27
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/proteam")
public class PROTEAMController extends BaseController {
	
	@Autowired
	private PROTEAMService proteamService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("proteam:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PROTEAM_ID", this.get32UUID());	//主键
		proteamService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("proteam:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		proteamService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("proteam:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		proteamService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("proteam:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = proteamService.list(page);	//列出PROTEAM列表
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
	//@RequiresPermissions("proteam:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
        pd.put("FPTISINNER", "总经理");
        List<PageData> userList01 = proteamService.listAll(pd);    
        pd.put("FPTISINNER", "副总经理");
        List<PageData> userList02 = proteamService.listAll(pd);
        pd.put("FPTISINNER", "项目经理");
        List<PageData> userList03 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "销售职员");
        List<PageData> userList04 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "销售部长");
        List<PageData> userList05 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "技术部长");
        List<PageData> userList06 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "技术职员");
        List<PageData> userList07 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "技术组长");
        List<PageData> userList08 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "工程商务部长");
        List<PageData> userList09 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "采购部长");
        List<PageData> userList10 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "工程商务职员");
        List<PageData> userList11 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "财务部长");
        List<PageData> userList12 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "车间主任");
        List<PageData> userList13 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "物流组长");
        List<PageData> userList14 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "技术负责人");
        List<PageData> userList15 = proteamService.listAll(pd); 
       /* pd.put("FPTISINNER", "车间装配人员");
        List<PageData> userList16 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "物流组长");
        List<PageData> userList17 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "物流职员");
        List<PageData> userList18 = proteamService.listAll(pd); 
        pd.put("FPTISINNER", "技术负责人");
        List<PageData> userList19 = proteamService.listAll(pd); */
        PageData data = new PageData();
        data.put("userList01",userList01);
        data.put("userList01",userList01);
        data.put("userList02",userList02);
        data.put("userList03",userList03);
        data.put("userList04",userList04);
        data.put("userList05",userList05);
        data.put("userList06",userList06);
        data.put("userList07",userList07);
        data.put("userList08",userList08);
        data.put("userList09",userList09);
        data.put("userList10",userList10);
        data.put("userList11",userList11);
        data.put("userList12",userList12);
        data.put("userList13",userList13);
        data.put("userList14",userList14);
        data.put("userList15",userList15);
       /* data.put("userList16",userList16);
        data.put("userList17",userList17);
        data.put("userList18",userList18);
        data.put("userList19",userList19);*/
        map.put("data",data);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("proteam:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			proteamService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注2");	//1
		titles.add("备注3");	//2
		titles.add("备注4");	//3
		titles.add("备注5");	//4
		titles.add("备注6");	//5
		titles.add("备注7");	//6
		titles.add("备注8");	//7
		titles.add("备注9");	//8
		titles.add("备注10");	//9
		titles.add("备注11");	//10
		titles.add("项目ID");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = proteamService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FPTNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FPTSTAFFID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FPTISINNER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCREATOR"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCTIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FLATESTMODIFY"));	    //6
			vpd.put("var7", varOList.get(i).getString("FMODIFYTIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("FRF1"));	    //8
			vpd.put("var9", varOList.get(i).getString("FRF2"));	    //9
			vpd.put("var10", varOList.get(i).getString("FRF3"));	    //10
			vpd.put("var11", varOList.get(i).getString("EPROJECT_ID"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**按照角色变更团队组员
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/changeTeamMemberByRole")
    @ResponseBody
    public Object changeTeamMemberByRole(@RequestParam String param,String USERNAME,String NAME) throws Exception{
        //JSONArray idArray = JSONArray.fromObject(param);
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "200";
        JSONObject jsonNotice=new JSONObject().fromObject(param);
        String EPROJECT_ID = (String) jsonNotice.get("EPROJECT_ID");//项目ID
        String FPTISINNER = (String) jsonNotice.get("FPTISINNER");//角色
        //String FEPNAME = (String) jsonNotice.get("FEPNAME");//项目名称
        PageData dPd = new PageData();
        PageData epd = new PageData();
        epd=this.getPageData();
        dPd.put("EPROJECT_ID", EPROJECT_ID);
        dPd.put("FPTISINNER", FPTISINNER);
        proteamService.delete(dPd);//删除项目原有角色成员
        JSONArray userList = jsonNotice.getJSONArray("userList");//人员列表集合
		//String NM= epd.getString("NM");
		//JSONObject user2=userList.getJSONObject(0);
		//String FPTNAME =user2.getString("FPTNAME");
        for (int i = 0; i < userList.size(); i++) {
            JSONObject user=userList.getJSONObject(i);
            PageData pd = new PageData();
            pd.put("EPROJECT_ID",EPROJECT_ID);
            pd.put("FPTNAME",user.getString("FPTNAME"));
            pd.put("FPTSTAFFID",user.getString("FPTSTAFFID"));
            pd.put("FPTISINNER",FPTISINNER);
            pd.put("PROTEAM_ID", this.get32UUID()); //主键
            pd.put("FCTIME", Tools.date2Str(new Date())); //创建时间
            pd.put("FMODIFYTIME", Tools.date2Str(new Date())); //修改时间
            pd.put("FCREATOR", Jurisdiction.getName()); //创建人
            pd.put("FLATESTMODIFY", Jurisdiction.getName()); //最后修改人
            pd.put("FPIDUTYMAN", Jurisdiction.getName()); //最后修改人
            try{
                proteamService.save(pd);
                
                if("技术负责人".equals(FPTISINNER)){//变更的如是销售职员，应该联动变更销售部长
                   PageData pdX=new PageData();
                   
                }else if ("项目经理".equals(FPTISINNER)) {
                	
				}
                result = "200";
                map.put("msg","ok");
                map.put("msgText","变更成功！");
            }catch (Exception e){
                result = "500";
                map.put("msg","no");
                map.put("msgText","未知错误，请联系管理员！");
            }finally{
                map.put("result", result);
            }
        }
        map.put("result", result);
		return map;
    }
    
}
