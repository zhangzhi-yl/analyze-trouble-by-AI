package org.yy.controller.zm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.yy.opc.utils.OPCUtil;
import org.yy.util.*;
import org.yy.entity.PageData;
import org.yy.service.zm.PLCService;

/**
 * 说明：PLC参数配置
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/plc")
public class PLCController extends BaseController {

    @Autowired
    private PLCService plcService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
    //@RequiresPermissions("plc:add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        boolean plc = OPCUtil.check(pd);
        if (plc) {
            pd.put("PLC_ID", this.get32UUID());    //主键
            pd.put("Creator", Jurisdiction.getName());
            pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            plcService.save(pd);
        }else {
            errInfo = "不存在该点位";
        }
        map.put("result", errInfo);
        return map;
    }

    /**
     * 获取空闲字段
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getField")
    @ResponseBody
    public Object getField() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {
            varList = plcService.getField(pd);    //列出空闲字段列表
        } else {
            errInfo = "201";
        }

        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 检验是否为IPV4地址
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check")
    //@RequiresPermissions("plc:add")
    @ResponseBody
    public Object check() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String ip = pd.getString("Address");
        boolean isip = IPUtil.isValidIpv4Addr(ip);
        if (isip) {
            errInfo = "success";
        } else {
            errInfo = "201";
        }
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
    //@RequiresPermissions("plc:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData plc = plcService.findById(pd);
        if(null != plc){
            if("是".equals(plc.getString("IfSave"))){
                plcService.deletePlcData(plc);
            }
            plcService.delete(pd);
        }else {
            errInfo = "201";
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
    //@RequiresPermissions("plc:edit")
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        boolean plc = OPCUtil.check(pd);
        if (plc) {
            pd.put("Modified", Jurisdiction.getName());
            pd.put("ModifiedTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            plcService.edit(pd);
        }else {
            errInfo = "不存在该点位";
        }
        map.put("result", errInfo);
        return map;
    }

    /**
     * 开关时长
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getDuration")
    @ResponseBody
    public Object getDuration() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> plcList = plcService.useList();

        List<PageData> onPrarmList = new ArrayList<>();
        List<PageData> offPrarmList = new ArrayList<>();
        List<Double> offValueList = new ArrayList<>();
        List<Double> onValueList = new ArrayList<>();
        List<String> loopList = new ArrayList<>();


        if (plcList.size() > 0) {

            for (PageData plc : plcList) {
                PageData offPrarm = new PageData();
                PageData onPrarm = new PageData();
                String field = "ISNULL(COUNT(" + plc.getString("SaveField") + "),0) " + plc.getString("SaveField") + " ";
                String off = "AND " + plc.getString("SaveField") + "= 0 ";
                String on = "AND " + plc.getString("SaveField") + "= 1 ";
                offPrarm.put("QueryField", field);
                offPrarm.put("FStatus", off);
                offPrarm.put("FTime", pd.getString("FTime"));
                offPrarm.put("DateType", pd.getString("DateType"));
                onPrarm.put("QueryField", field);
                onPrarm.put("FStatus", on);
                onPrarm.put("FTime", pd.getString("FTime"));
                onPrarm.put("DateType", pd.getString("DateType"));
                offPrarmList.add(offPrarm);
                onPrarmList.add(onPrarm);
                loopList.add(plc.getString("LoopName"));
            }


            List<PageData> offList = new ArrayList<>();
            List<PageData> onList = new ArrayList<>();
            if (onPrarmList.size() == offPrarmList.size()) {

                for (int i = 0; i < offPrarmList.size(); i++) {
                    PageData onParam = onPrarmList.get(i);
                    PageData offParam = offPrarmList.get(i);
                    List<PageData> offDurationList = plcService.getDuration(offParam);
                    List<PageData> onDurationList = plcService.getDuration(onParam);
                    offList.addAll(offDurationList);
                    onList.addAll(onDurationList);
                }
            }

            if (offList.size() > 0 && onList.size() > 0) {
                for (PageData plc : plcList) {
                    for (int i = 0; i < offList.size(); i++) {
                        PageData on = onList.get(i);
                        PageData off = offList.get(i);
                        String onValue = "";
                        String offValue = "";
                        try {
                            onValue = on.get(plc.getString("SaveField")).toString();
                            offValue = off.get(plc.getString("SaveField")).toString();
                        } catch (Exception e) {
                            continue;
                        }
                        onValueList.add(Double.parseDouble(String.format("%.2f", Double.parseDouble(onValue) / 60)));
                        offValueList.add(Double.parseDouble(String.format("%.2f", Double.parseDouble(offValue) / 60)));
                    }
                }
            }

        } else {
            errInfo = "没有正在启用的PLC配置";
        }

        map.put("onValueList", onValueList);
        map.put("offValueList", offValueList);
        map.put("loopList", loopList);
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
    //@RequiresPermissions("plc:list")
    @ResponseBody
    public Object list(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String KEYWORDS = pd.getString("KEYWORDS");                        //关键词检索条件
        if (Tools.notEmpty(KEYWORDS)) pd.put("KEYWORDS", KEYWORDS.trim());
        page.setPd(pd);
        List<PageData> varList = plcService.list(page);    //列出PLC列表
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 列表
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/listAll")
    //@RequiresPermissions("plc:list")
    @ResponseBody
    public Object listAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = plcService.listAll(pd);    //列出PLC列表
        for (PageData var : varList){
            var.put("ParamName",var.getString("ParaName"));
        }
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
    //@RequiresPermissions("plc:edit")
    @ResponseBody
    public Object goEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = plcService.findById(pd);    //根据ID读取
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
    //@RequiresPermissions("plc:del")
    @ResponseBody
    public Object deleteAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (Tools.notEmpty(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            plcService.deleteAll(ArrayDATA_IDS);
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
        titles.add("参数名称");    //1
        titles.add("地址");    //2
        titles.add("连接方式");    //3
        titles.add("符号名");    //4
        titles.add("回路");
        titles.add("使用状态");    //5
        titles.add("备注");    //6
        titles.add("创建时间");    //7
        titles.add("创建人");    //8
//        titles.add("修改人");    //9
//        titles.add("最后修改时间");    //10
        dataMap.put("titles", titles);
        List<PageData> varOList = plcService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("ParaName"));        //1
            vpd.put("var2", varOList.get(i).getString("Address"));        //2
            vpd.put("var3", varOList.get(i).getString("ConnectType"));        //3
            vpd.put("var4", varOList.get(i).getString("SymbolName"));        //4
            vpd.put("var5", varOList.get(i).getString("LoopName"));        //5
            vpd.put("var6", varOList.get(i).getString("FStatus"));        //5
            vpd.put("var7", varOList.get(i).getString("Remarks"));        //6
            vpd.put("var8", varOList.get(i).getString("CreateTime"));        //7
            vpd.put("var9", varOList.get(i).getString("Creator"));        //8
            vpd.put("var10", varOList.get(i).getString("Modified"));        //9
            vpd.put("var11", varOList.get(i).getString("ModifiedTime"));        //10
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
