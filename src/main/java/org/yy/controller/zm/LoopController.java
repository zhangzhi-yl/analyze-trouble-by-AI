package org.yy.controller.zm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.opc.OPCService;
import org.yy.opc.utils.JiVariantUtil;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.mom.AreaService;
import org.yy.service.mom.SiteService;
import org.yy.service.zm.*;
import org.yy.task.Task;
import org.yy.util.*;
import org.yy.entity.PageData;

/**
 * 说明：回路管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-11
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/loop")
public class LoopController extends BaseController {

    @Autowired
    private LoopService loopService;

    @Autowired
    private LoopTimeService loopTimeService;

    @Autowired
    private LogService logService;

    @Autowired
    private Task task;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private PLCService plcService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;

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

                pd.put("WORKSHOP_ID", area.getString("AREA_ID"));
                List<PageData> TopList = loopService.listAll(pd);
                for (PageData top : TopList) {
                    top.put("id", top.getString("LOOP_ID"));
                    top.put("label", top.getString("FName"));
                    top.put("children", new ArrayList<>());
                }

                area.put("id", area.getString("AREA_ID"));
                area.put("label", area.getString("FNAME"));
                area.put("children", TopList);
            }

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
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
    //@RequiresPermissions("loop:add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("LOOP_ID", this.get32UUID());    //主键
        pd.put("Creator", Jurisdiction.getName());
        pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pd.put("FStatus", "0");
        pd.put("FTimeStatus", "0");
        loopService.save(pd);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 校验名称重复
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/checkName")
    @ResponseBody
    public Object checkName() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String FName = pd.getString("FName");                        //关键词检索条件
        if (Tools.notEmpty(FName)) pd.put("FName", FName.trim());
        List<PageData> eqList = loopService.listAll(pd);
        if (eqList.size() > 0) {
            errInfo = "201";
        }
        map.put("result", errInfo);                //返回结果
        return map;
    }

    /**
     * 删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    //@RequiresPermissions("loop:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        loopService.delete(pd);

        //级联删除时间数据
        loopTimeService.deleteByLoop(pd);
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
    //@RequiresPermissions("loop:edit")
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
     * 修改定时状态
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editTimeStatus")
    //@RequiresPermissions("loop:edit")
    @ResponseBody
    public Object editTimeStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        loopService.editTimeStatus(pd);

        String content = "";
        if ("1".equals(pd.getString("FTimeStatus"))) {
            content = "开启回路定时功能";
        } else if ("0".equals(pd.getString("FTimeStatus"))) {
            content = "关闭回路定时功能";
        }

        //开启场景定时功能时进行一次开关校验
        task.control();

        PageData loop = loopService.findById(pd);
        logService.save(content, loop.getString("FName"), "-");
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
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object list(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String KEYWORDS = pd.getString("KEYWORDS");                        //关键词检索条件
        if (Tools.notEmpty(KEYWORDS)) pd.put("KEYWORDS", KEYWORDS.trim());
        page.setPd(pd);
        List<PageData> varList = loopService.list(page);    //列出Loop列表
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }



    /**
     * 列表（根据逗号分隔ID查询数据分页列表）
     * v1 20211012 sunlz 创建
     * v1 20211013 sunlz 嵌套子表list
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/loopList")
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object loopList(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        //处理逗号分割id
        String ID = "(";
        if (!"".equals(pd.getString("Loop")) && null != pd.getString("Loop")) {
            String[] array = pd.get("Loop").toString().split(",");
            if (array.length != 0) {
                for (int i = 0; i < array.length; i++) {
                    ID = ID + "'" + array[i] + "',";
                    if (i == array.length - 1) {
                        ID = ID.substring(0, ID.length() - 1);
                    }
                }
                ID += ")";
                pd.put("IDS", ID);
            }
        }

        page.setPd(pd);

        List<PageData> timeList;
        //列出Loop列表
        List<PageData> varList = loopService.loopList(page);
        //列出LoopTime列表
        List<PageData> loopTimeList = loopTimeService.listAll(pd);
        //循环比对
        for (PageData loop : varList) {
            timeList = new ArrayList<>();
            String id = loop.getString("LOOP_ID");
            for (PageData loopTime : loopTimeList) {
                String loopId = loopTime.getString("Loop_ID");
                if (id.equals(loopId)) {
                    timeList.add(loopTime);
                }
            }
            loop.put("timeList", timeList);
        }
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 列表全部
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/listAll")
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object listAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = loopService.listAll(pd);    //列出Loop列表
        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 获取回路列表
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getLoopList")
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object getLoopList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = loopService.getLoopList(pd);    //列出Loop列表
        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }
    /**
     * 列表全部(分组下拉)
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/listAllGroupSelect")
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object listAllGroupSelect() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = loopService.listAll(pd);    //列出Loop列表
        List<PageData> resultList = new ArrayList<>();

        List<PageData> lightList = new ArrayList<>();
        List<PageData> airList = new ArrayList<>();
        List<PageData> electricList = new ArrayList<>();

        //分组
        for (PageData var : varList){

            PageData pageData = new PageData();

            if("照明".equals(var.getString("FType"))){
                pageData.put("label",var.getString("FName"));
                pageData.put("value",var.getString("LOOP_ID"));
                lightList.add(pageData);
            }else if("空调".equals(var.getString("FType"))){
                pageData.put("label",var.getString("FName"));
                pageData.put("value",var.getString("LOOP_ID"));
                airList.add(pageData);
            }else if("插座".equals(var.getString("FType"))){
                pageData.put("label",var.getString("FName"));
                pageData.put("value",var.getString("LOOP_ID"));
                electricList.add(pageData);
            }
        }

        PageData group = new PageData();
        group.put("label","照明");
        group.put("options",lightList);
        resultList.add(group);

        group = new PageData();
        group.put("label","空调");
        group.put("options",airList);
        resultList.add(group);

        group = new PageData();
        group.put("label","插座");
        group.put("options",electricList);
        resultList.add(group);

        map.put("varList", resultList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 对象全部
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/listAllObj")
    //@RequiresPermissions("loop:list")
    @ResponseBody
    public Object listAllObj() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<PageData> loopList = loopService.listAll(pd);    //列出Loop列表
        List<PageData> groupList = groupService.listAll(pd);    //列出Group列表
        List<PageData> sceneList = sceneService.listAll(pd);    //列出Scene列表

        PageData var = new PageData();
        var.put("label", "回路");
        List<PageData> loopNameList = new ArrayList<>();
        for (PageData loop : loopList) {
            PageData loopName = new PageData();
            loopName.put("value", loop.getString("FName"));
            loopName.put("label", loop.getString("FName"));
            loopNameList.add(loopName);
        }
        var.put("options", loopNameList);
        varList.add(var);

        var = new PageData();
        var.put("label", "分组");
        List<PageData> groupNameList = new ArrayList<>();
        for (PageData group : groupList) {
            PageData groupName = new PageData();
            groupName.put("value", group.getString("FName"));
            groupName.put("label", group.getString("FName"));
            groupNameList.add(groupName);
        }
        var.put("options", groupNameList);
        varList.add(var);

        var = new PageData();
        var.put("label", "场景");
        List<PageData> sceneNameList = new ArrayList<>();
        for (PageData scene : sceneList) {
            PageData sceneName = new PageData();
            sceneName.put("value", scene.getString("FName"));
            sceneName.put("label", scene.getString("FName"));
            sceneNameList.add(sceneName);
        }
        var.put("options", sceneNameList);
        varList.add(var);

        map.put("varList", varList);
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
    //@RequiresPermissions("loop:edit")
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
    //@RequiresPermissions("loop:del")
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
    //@RequiresPermissions("toExcel")
    public ModelAndView exportExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("名称");    //1
        titles.add("编码");    //2
        titles.add("区域");    //3
        titles.add("状态");    //4
        titles.add("变量");    //5
        titles.add("创建人");    //6
        titles.add("创建时间");    //7
        titles.add("修改人");    //8
        titles.add("最后修改时间");    //9
        titles.add("车间");    //10
        dataMap.put("titles", titles);
        List<PageData> varOList = loopService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("FName"));        //1
            vpd.put("var2", varOList.get(i).getString("FCode"));        //2
            vpd.put("var3", varOList.get(i).getString("Region"));        //3
            vpd.put("var4", varOList.get(i).getString("FStatus").equals("0") ? "关闭" : "开启");        //4
            vpd.put("var5", varOList.get(i).getString("FVariable"));        //5
            vpd.put("var6", varOList.get(i).getString("Creator"));        //6
            vpd.put("var7", varOList.get(i).getString("CreateTime"));        //7
            vpd.put("var8", varOList.get(i).getString("Modified"));        //8
            vpd.put("var9", varOList.get(i).getString("ModifiedTime"));        //9
            vpd.put("var10", varOList.get(i).getString("AreaName"));        //10
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
