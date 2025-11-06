package org.yy.controller.ny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.mom.AreaService;
import org.yy.service.mom.SiteService;
import org.yy.service.ny.NYLoopService;
import org.yy.service.ny.NYPLCService;
import org.yy.service.ny.NY_EquipmentsService;
import org.yy.service.zm.EquipmentsService;
import org.yy.service.zm.PLCService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.RandomColor;
import org.yy.util.Tools;

import java.util.*;

/**
 * 说明：回路/支路管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/nyLoop")
public class NYLoopController extends BaseController {

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;    //车间管理service

    @Autowired
    private NY_EquipmentsService equipmentsService;

    @Autowired
    private NYPLCService plcService;

    /**
     * 保存
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
//	@RequiresPermissions("loop:add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData loop = loopService.findByCode(pd);
        if(null == loop){
            pd.put("Creator", Jurisdiction.getName());
            pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            pd.put("LOOP_ID", this.get32UUID());    //主键
            loopService.save(pd);

            //更新编码表  值加一
//            PageData edit = new PageData();
//            String NowValue = pd.get("NowValue").toString();
//            edit.put("ParamType",NowValue);
//            edit.put("NowValue",0);
//            loopService.editCodeNumByType(edit);
        }else {
            errInfo = "编号重复";
        }

        map.put("result", errInfo);
        return map;
    }

    /**
     * 生成编码
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getCode")
    @ResponseBody
    public Object getCode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String type = pd.getString("ParamType");
        PageData code = loopService.getCodeNumByType(pd);

        if(null != code){
            if("T_PatraElectric".equals(type)){
                code.put("FCode","E_"+code.get("NowValue").toString());
            }else if("T_PatraWater".equals(type)){
                code.put("FCode","W_"+code.get("NowValue").toString());
            }else if("T_PatraGas".equals(type)){
                code.put("FCode","G_"+code.get("NowValue").toString());
            }
        }

        map.put("pd", code);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
//	@RequiresPermissions("loop:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("PID", pd.getString("LOOP_ID"));

        List<PageData> childList = loopService.listAll(pd);
        if (childList.size() == 0) {
            loopService.delete(pd);
        } else {
            errInfo = "err";
        }

        map.put("result", errInfo);                //返回结果
        return map;
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
//	@RequiresPermissions("loop:edit")
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("Modified", Jurisdiction.getName());
        pd.put("ModifiedTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        loopService.edit(pd);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
//	@RequiresPermissions("loop:list")
    @ResponseBody
    public Object list(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("LOOP_ID", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
        page.setPd(pd);
        List<PageData> varList = loopService.list(page);    //列出Loop列表
        for (PageData var : varList) {
            if (null != var.getString("DeptName1") && !"".equals(var.getString("DeptName1"))) {
                var.put("DeptName", var.getString("DeptName1"));
            } else if (null != var.getString("DeptName2") && !"".equals(var.getString("DeptName2"))) {
                var.put("DeptName", var.getString("DeptName2"));
            } else if (null != var.getString("DeptName3") && !"".equals(var.getString("DeptName3"))) {
                var.put("DeptName", var.getString("DeptName3"));
            }
            StringBuilder equName = new StringBuilder();
            if (null != var.getString("Extend1") && !"".equals(var.getString("Extend1"))) {
                PageData getEqu = handleIDS(var.getString("Extend1"));

                List<PageData> equList = equipmentsService.listAll(getEqu);
                for (PageData equ : equList) {
                    equName.append(equ.getString("FName")).append(",");
                }
                var.put("EquName", equName.substring(0, equName.toString().length() - 1));
            }

        }
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    private static PageData handleIDS(String IDS) {
        PageData ids = new PageData();
        //处理逗号分割id
        String ID = "(";
        if (!"".equals(IDS) && null != IDS) {
            String[] array = IDS.split(",");
            if (array.length != 0) {
                for (int i = 0; i < array.length; i++) {
                    ID = ID + "'" + array[i] + "',";
                    if (i == array.length - 1) {
                        ID = ID.substring(0, ID.length() - 1);
                    }
                }
                ID += ")";
            }
        } else {
            ID = "";
        }
        ids.put("IDS", ID);
        return ids;
    }

    /**
     * 全部tree
     *
     * @return
     */
    @RequestMapping(value = "/SiteTree")
    @ResponseBody
    public Object SiteTree() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> tree = new ArrayList<>();
        List<PageData> siteList = siteService.listAll(pd);
        for (PageData site : siteList) {
            PageData child = new PageData();
            List<PageData> areaList = areaService.listAll(site);
            for (PageData area : areaList) {

                pd.put("PID", area.getString("AREA_ID"));
                List<PageData> TopList = loopService.listAll(pd);
                pd.put("PID", "");
                List<PageData> AllList = loopService.listAll(pd);
                for (PageData top : TopList) {
                    top.put("id", top.getString("LOOP_ID"));
                    top.put("label", top.getString("FName"));
                    top.put("children", getChildren(top, AllList));
                }

                area.put("id", area.getString("AREA_ID"));
                area.put("label", area.getString("FNAME"));
                area.put("children", TopList);
            }

            //向子级添加回路树表数据
            pd.put("PID", site.getString("DM_SITE_ID"));
            List<PageData> TopList = loopService.listAll(pd);
            pd.put("PID", "");
            List<PageData> AllList = loopService.listAll(pd);
            for (PageData top : TopList) {
                top.put("id", top.getString("LOOP_ID"));
                top.put("label", top.getString("FName"));
                top.put("children", getChildren(top, AllList));
            }

            areaList.addAll(TopList);
            //向子级添加从表数据
            child.put("label", site.getString("FNAME"));
            child.put("id", site.getString("DM_SITE_ID"));
            child.put("children", areaList);
            tree.add(child);
        }

        map.put("DTree", tree);
        return map;
    }

    /**
     * 回路tree
     *
     * @return
     */
    @RequestMapping(value = "/loopTree")
    @ResponseBody
    public Object loopTree() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd.put("PID", "0");
        List<PageData> TopList = loopService.listAll(pd);
        pd.put("PID", "");
        List<PageData> AllList = loopService.listAll(pd);
        for (PageData top : TopList) {
            top.put("label", top.getString("FName"));
            top.put("children", getChildren(top, AllList));
        }

        map.put("DTree", TopList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 全部tree
     *
     * @return
     */
    @RequestMapping(value = "/EnergyFlow")
    @ResponseBody
    public Object EnergyFlow() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String SaveTable = pd.getString("SaveTable");
        String startTime = pd.getString("startTime");
        String endTime = pd.getString("endTime");

        pd.putAll(handleIDS(pd.getString("LOOP_ID")));

        List<PageData> flowList = new ArrayList<>();
        List<PageData> colorList = new ArrayList<>();

        //工厂列表
        List<PageData> siteList = siteService.listTopAll(pd);
        if (siteList.size() > 0) {
            for (PageData site : siteList) {
                //车间列表
                List<PageData> areaList = areaService.listAll(site);
                for (PageData area : areaList) {
                    //顶级回路列表
                    pd.put("PID", area.getString("AREA_ID"));
                    List<PageData> TopList = loopService.listAll(pd);
                    //全部回路列表
                    pd.put("PID", "");
                    pd.put("SaveTable",SaveTable);
                    List<PageData> AllList = loopService.listAll(pd);
                    for (PageData top : TopList) {

                        //获取顶级回路能耗
                        double topValue = 0.0;
                        topValue = getFieldValue(top.getString("LOOP_ID"),SaveTable,startTime,endTime);

                        //获取车间至顶级回路流向list
                        PageData flowLoop = new PageData();
                        flowLoop.put("source", area.getString("FNAME"));
                        flowLoop.put("target", top.getString("FName"));
                        flowLoop.put("value", topValue);
                        flowList.add(flowLoop);

                        //获取顶级回路名称及颜色
                        PageData colorTop = new PageData();
                        colorTop.put("name",top.getString("FName"));
                        PageData color = new PageData();
                        color.put("color", RandomColor.getColor());
                        colorTop.put("itemStyle",color);
                        colorList.add(colorTop);

                        //将递归获取的list加入总list
                        colorList.addAll(getColor(top,AllList));
                        flowList.addAll(getFlow(top, AllList,SaveTable,startTime,endTime));
                    }
                    //获取车间级名称颜色
                    PageData colorArea = new PageData();
                    colorArea.put("name",area.getString("FNAME"));
                    PageData color = new PageData();
                    color.put("color", RandomColor.getColor());
                    colorArea.put("itemStyle",color);
                    colorList.add(colorArea);

                    double areaValue = 0.0;
                    areaValue = getFieldValue(area.getString("AREA_ID"),SaveTable,startTime,endTime);

                    //获取工厂至车间流向list
                    PageData flowSite = new PageData();
                    flowSite.put("source", site.getString("FNAME"));
                    flowSite.put("target", area.getString("FNAME"));
                    flowSite.put("value", areaValue);
                    flowList.add(flowSite);
                }
                //获取工厂级名称及颜色
                PageData colorSite = new PageData();
                colorSite.put("name",site.getString("FNAME"));
                PageData color = new PageData();
                color.put("color", RandomColor.getColor());
                colorSite.put("itemStyle",color);
                colorList.add(colorSite);

                pd.put("PID", site.getString("DM_SITE_ID"));
                List<PageData> TopList = loopService.listAll(pd);
                pd.put("PID", "");
                pd.put("SaveTable",SaveTable);
                List<PageData> AllList = loopService.listAll(pd);
                for (PageData top : TopList) {

                    //获取顶级回路能耗
                    double loopValue = 0.0;
                    loopValue = getFieldValue(top.getString("LOOP_ID"),SaveTable,startTime,endTime);

                    //获取工厂至回路流向list
                    PageData flowSite = new PageData();
                    flowSite.put("source", site.getString("FNAME"));
                    flowSite.put("target", top.getString("FName"));
                    flowSite.put("value", loopValue);
                    flowList.add(flowSite);
                    flowList.addAll(getFlow(top, AllList,SaveTable,startTime,endTime));
                    //获取工厂子回路名称及颜色
                    PageData colorArea = new PageData();
                    colorArea.put("name",top.getString("FName"));
                    PageData color1 = new PageData();
                    color1.put("color", RandomColor.getColor());
                    colorArea.put("itemStyle",color1);
                    colorList.add(colorArea);
                    colorList.addAll(getColor(top,AllList));
                }
            }

        } else {
            pd.put("AREAIDS",pd.getString("IDS"));
            List<PageData> areaList = areaService.listAll(pd);
            if (areaList.size() > 0) {
                for (PageData area : areaList) {
                    //顶级回路列表
                    pd.put("PID", area.getString("AREA_ID"));
                    List<PageData> TopList = loopService.listAll(pd);
                    //全部回路列表
                    pd.put("PID", "");
                    pd.put("SaveTable",SaveTable);
                    List<PageData> AllList = loopService.listAll(pd);
                    for (PageData top : TopList) {

                        //获取顶级回路能耗
                        double loopValue = 0.0;
                        loopValue = getFieldValue(top.getString("LOOP_ID"),SaveTable,startTime,endTime);

                        //获取车间至顶级回路流向list
                        PageData flowLoop = new PageData();
                        flowLoop.put("source", area.getString("FNAME"));
                        flowLoop.put("target", top.getString("FName"));
                        flowLoop.put("value", loopValue);
                        flowList.add(flowLoop);

                        //获取顶级回路名称及颜色
                        PageData colorTop = new PageData();
                        colorTop.put("name",top.getString("FName"));
                        PageData color = new PageData();
                        color.put("color", RandomColor.getColor());
                        colorTop.put("itemStyle",color);
                        colorList.add(colorTop);

                        //将递归获取的list加入总list
                        colorList.addAll(getColor(top,AllList));
                        flowList.addAll(getFlow(top, AllList,SaveTable,startTime,endTime));
                    }
                    //获取车间级名称颜色
                    PageData colorArea = new PageData();
                    colorArea.put("name",area.getString("FNAME"));
                    PageData color = new PageData();
                    color.put("color", RandomColor.getColor());
                    colorArea.put("itemStyle",color);
                    colorList.add(colorArea);
                }

                siteList = siteService.listAll(new PageData());
                for (PageData site : siteList){
                    pd.put("LOOPIDS",pd.getString("IDS"));
                    pd.put("PID", site.getString("DM_SITE_ID"));
                    List<PageData> TopList = loopService.listAll(pd);
                    pd.put("PID", "");
                    pd.put("LOOPIDS","");
                    pd.put("SaveTable",SaveTable);
                    List<PageData> AllList = loopService.listAll(pd);
                    for (PageData top : TopList) {
                        //获取子回路流向list
                        flowList.addAll(getFlow(top, AllList,SaveTable,startTime,endTime));
                        //获取工厂子回路名称及颜色
                        PageData colorArea = new PageData();
                        colorArea.put("name",top.getString("FName"));
                        PageData color1 = new PageData();
                        color1.put("color", RandomColor.getColor());
                        colorArea.put("itemStyle",color1);
                        colorList.add(colorArea);
                        colorList.addAll(getColor(top,AllList));
                    }
                }

            }else {
                //顶级回路列表
                pd.put("LOOPIDS",pd.getString("IDS"));
                List<PageData> TopList = loopService.listAll(pd);
                //全部回路列表
                pd.put("LOOPIDS", "");
                pd.put("SaveTable",SaveTable);
                List<PageData> AllList = loopService.listAll(pd);
                for (PageData top : TopList) {

                    //获取顶级回路名称及颜色
                    PageData colorTop = new PageData();
                    colorTop.put("name",top.getString("FName"));
                    PageData color = new PageData();
                    color.put("color", RandomColor.getColor());
                    colorTop.put("itemStyle",color);
                    colorList.add(colorTop);

                    //将递归获取的list加入总list
                    colorList.addAll(getColor(top,AllList));
                    flowList.addAll(getFlow(top, AllList,SaveTable,startTime,endTime));
                    for(int i =0;i<colorList.size()-1;i++){
                        for(int j=colorList.size()-1;j>i;j--){
                            if(colorList.get(i).getString("name").equals(colorList.get(j).getString("name"))) {
                                colorList.remove(j);
                            }
                        }
                    }
                }
            }
        }
        map.put("colorList",colorList);
        map.put("flowList",flowList);
        map.put("result",errInfo);
        return map;
    }

    public Double getFieldValue(String id,String table,String startTime,String endTime)throws Exception{
        //获取车间能耗参数及能耗最大值
        PageData param = new PageData();
        Double value = 0.0;
        param.put("Loop_ID",id);
        param.put("SaveTable",table);
        List<PageData> areaPlcList = plcService.findUseEnergyByLoop(param);
        if (areaPlcList.size() > 0){
            PageData areaPlc = areaPlcList.get(0);
            String SaveField = areaPlc.getString("SaveField");
            PageData getValue = new PageData();
            getValue.put("QueryField",SaveField);
            getValue.put("SaveTable",table);
            //结束日期最大值
            getValue.put("time",endTime);
            PageData valueEndPd = plcService.getFieldMaxValue(getValue);
            double endValue = Double.parseDouble(valueEndPd.get("value").toString());
            //开始日期最小值
            getValue.put("time",startTime);
            PageData valueStartPd = plcService.getFieldMinValue(getValue);
            double startValue = Double.parseDouble(valueStartPd.get("value").toString());

            value = Double.parseDouble(String.format("%.2f",endValue-startValue));
        }

        return value;
    }

    public List<PageData> getFlow(PageData root, List<PageData> all,String table,String startTime,String endTime) throws Exception{

        List<PageData> children = new ArrayList<>();

        String PARENT_ID = root.getString("LOOP_ID");

        for (PageData loop : all) {
            if (PARENT_ID.equals(loop.getString("Parent_ID"))) {
                children.addAll(getFlow(loop, all,table,startTime,endTime));

                //获取顶级回路能耗
                double loopValue = 0.0;
                loopValue = getFieldValue(loop.getString("LOOP_ID"),table,startTime,endTime);

                PageData flowLoop = new PageData();
                flowLoop.put("source", root.getString("FName"));
                flowLoop.put("target", loop.getString("FName"));
                flowLoop.put("value", loopValue);
                children.add(flowLoop);
            }
        }
        return children;
    }
    public List<PageData> getColor(PageData root, List<PageData> all) {

        List<PageData> colorList = new ArrayList<>();

        String PARENT_ID = root.getString("LOOP_ID");

        for (PageData loop : all) {
            if (PARENT_ID.equals(loop.getString("Parent_ID"))) {
                colorList.addAll(getColor(loop, all));
                PageData colorLoop = new PageData();
                colorLoop.put("name",loop.getString("FName"));
                PageData lcolor = new PageData();
                lcolor.put("color", RandomColor.getColor());
                colorLoop.put("itemStyle",lcolor);
                colorList.add(colorLoop);
            }
        }
        return colorList;
    }
    /**
     * 递归获取子菜单
     *
     * @param root 当前菜单
     * @param all  总的数据
     * @return 子菜单
     */
    public List<PageData> getChildren(PageData root, List<PageData> all) {

        List<PageData> children = new ArrayList<>();

        String PARENT_ID = root.getString("LOOP_ID");

        for (PageData loop : all) {
            if (PARENT_ID.equals(loop.getString("Parent_ID"))) {
                loop.put("id", loop.getString("LOOP_ID"));
                loop.put("label", loop.getString("FName"));
                loop.put("children", getChildren(loop, all));
                children.add(loop);
            }
        }
        return children;
    }

    /**
     * 去修改页面获取数据
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
//	@RequiresPermissions("loop:edit")
    @ResponseBody
    public Object goEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = loopService.findById(pd);    //根据ID读取
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
//	@RequiresPermissions("loop:del")
    @ResponseBody
    public Object deleteAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (Tools.notEmpty(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            loopService.deleteAll(ArrayDATA_IDS);
            errInfo = "success";
        } else {
            errInfo = "error";
        }
        map.put("result", errInfo);                //返回结果
        return map;
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
//	@RequiresPermissions("toExcel")
    public ModelAndView exportExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("LOOPIDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("回路名称");    //1
        titles.add("回路编码");    //2
        titles.add("上级回路");    //3
        titles.add("区域");    //4
		titles.add("包含设备");	//5
        titles.add("创建人");    //6
        titles.add("创建时间");    //7
        titles.add("修改人");    //8
        titles.add("最后修改时间");    //9
        dataMap.put("titles", titles);
        List<PageData> varOList = loopService.listAll(pd);
        for (PageData var : varOList) {
            if (null != var.getString("DeptName1") && !"".equals(var.getString("DeptName1"))) {
                var.put("DeptName", var.getString("DeptName1"));
            } else if (null != var.getString("DeptName2") && !"".equals(var.getString("DeptName2"))) {
                var.put("DeptName", var.getString("DeptName2"));
            } else if (null != var.getString("DeptName3") && !"".equals(var.getString("DeptName3"))) {
                var.put("DeptName", var.getString("DeptName3"));
            }
            StringBuilder equName = new StringBuilder();
            if (null != var.getString("Extend1") && !"".equals(var.getString("Extend1"))) {
                PageData getEqu = handleIDS(var.getString("Extend1"));

                List<PageData> equList = equipmentsService.listAll(getEqu);
                for (PageData equ : equList) {
                    equName.append(equ.getString("FName")).append(",");
                }
                var.put("EquName", equName.substring(0, equName.toString().length() - 1));
            }

        }
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("FName"));        //1
            vpd.put("var2", varOList.get(i).getString("FCode"));        //2
            vpd.put("var3", varOList.get(i).getString("DeptName"));        //3
            vpd.put("var4", varOList.get(i).getString("Region"));        //4
            vpd.put("var5", varOList.get(i).getString("EquName"));        //5
			vpd.put("var6", varOList.get(i).getString("Creator"));	    //6
            vpd.put("var7", varOList.get(i).getString("CreateTime"));        //7
            vpd.put("var8", varOList.get(i).getString("Modified"));        //8
            vpd.put("var9", varOList.get(i).getString("ModifiedTime"));        //9
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
