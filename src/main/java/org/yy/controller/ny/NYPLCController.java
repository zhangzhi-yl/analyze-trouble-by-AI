package org.yy.controller.ny;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.mom.AreaService;
import org.yy.service.mom.SiteService;
import org.yy.service.ny.FORMULAService;
import org.yy.service.ny.NYLoopService;
import org.yy.service.ny.NYPLCService;
import org.yy.service.ny.NY_EquipmentsService;
import org.yy.service.zm.EquipmentsService;
import org.yy.util.*;

import java.io.IOException;
import java.util.*;

/**
 * 说明：PLC参数配置
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/nyPlc")
public class NYPLCController extends BaseController {

    @Autowired
    private NYPLCService plcService;

    @Autowired
    private FORMULAService formulaService;

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private NY_EquipmentsService equipmentsService;

    /**
     * 保存
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
//	@RequiresPermissions("plc:add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("Creator", Jurisdiction.getName());
        pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pd.put("PLC_ID", this.get32UUID());    //主键

        //添加公式参数
        if("是".equals(pd.getString("isFormula")) && pd.getString("ParamType").contains("能耗") && "T_PatraElectric".equals(pd.getString("SaveTable"))){
            List<PageData> formulaList = new ArrayList<>();
            PageData formula = new PageData();

            formula.put("FORMULA_ID",this.get32UUID());
            formula.put("PLC_ID",pd.getString("PLC_ID"));

            //参数
            formula.put("temp_1",pd.getString("temp_1"));
            formulaList.add(formula);
            formula.put("temp_2",pd.getString("temp_2"));
            formulaList.add(formula);
            formula.put("pt",pd.getString("pt"));
            formulaList.add(formula);
            formula.put("ct",pd.getString("ct"));
            formulaList.add(formula);
            formulaService.save(formula);
        }

        plcService.save(pd);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 获取空闲字段
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
     * 删除
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
//	@RequiresPermissions("plc:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData plc = plcService.findById(pd);
        if (null != plc) {
            plcService.deletePlcData(plc);
            plcService.delete(pd);
        } else {
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
//	@RequiresPermissions("plc:edit")
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("Modified", Jurisdiction.getName());
        pd.put("ModifiedTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

        PageData dataBaseFormula = formulaService.findById(pd);

        //公式参数
        if("是".equals(pd.getString("isFormula")) && pd.getString("ParamType").contains("能耗") && "T_PatraElectric".equals(pd.getString("SaveTable"))){
            PageData formula = new PageData();
            formula.put("PLC_ID",pd.getString("PLC_ID"));

            //参数
            formula.put("temp_1",pd.getString("temp_1"));
            formula.put("temp_2",pd.getString("temp_2"));
            formula.put("pt",pd.getString("pt"));
            formula.put("ct",pd.getString("ct"));

            if (null == dataBaseFormula){
                formula.put("FORMULA_ID",this.get32UUID());
                formulaService.save(formula);
            }else {
                formulaService.edit(formula);
            }
        }

        plcService.edit(pd);
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
//	@RequiresPermissions("plc:list")
    @ResponseBody
    public Object list(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String KEYWORDS = pd.getString("KEYWORDS");                        //关键词检索条件
        if (Tools.notEmpty(KEYWORDS)) {
            pd.put("KEYWORDS", KEYWORDS.trim());
        }
        page.setPd(pd);
        List<PageData> varList = plcService.list(page);    //列出PLC列表
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
//	@RequiresPermissions("plc:list")
    @ResponseBody
    public Object list() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = plcService.listAll(pd);    //列出PLC列表
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
//	@RequiresPermissions("plc:edit")
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
//	@RequiresPermissions("plc:del")
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
//	@RequiresPermissions("toExcel")
    public ModelAndView exportExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("参数名称");    //1
        titles.add("参数类型");    //14
        titles.add("地址");    //2
        titles.add("连接方式");    //3
        titles.add("符号名");    //4
        titles.add("使用状态");    //5
        titles.add("所属支路");    //13
        titles.add("所属设备");    //13
        titles.add("备注");    //6
        titles.add("创建时间");    //7
        titles.add("创建人");    //8
        titles.add("修改人");    //9
        titles.add("最后修改时间");    //10
//		titles.add("存储表");	//11
//		titles.add("存储字段");	//12


//		titles.add("扩展1");	//15
//		titles.add("扩展2");	//16
//		titles.add("扩展3");	//17
        dataMap.put("titles", titles);
        List<PageData> varOList = plcService.listAll(pd);
        for (PageData var : varOList) {
            if (null != var.getString("DeptName1") && !"".equals(var.getString("DeptName1"))) {
                var.put("DeptName", var.getString("DeptName1"));
            } else if (null != var.getString("DeptName2") && !"".equals(var.getString("DeptName2"))) {
                var.put("DeptName", var.getString("DeptName2"));
            } else if (null != var.getString("DeptName3") && !"".equals(var.getString("DeptName3"))) {
                var.put("DeptName", var.getString("DeptName3"));
            }
        }
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("ParamName"));        //1
            vpd.put("var2", varOList.get(i).getString("ParamType"));        //14
            vpd.put("var3", varOList.get(i).getString("Address"));        //2
            vpd.put("var4", varOList.get(i).getString("ConnectType"));        //3
            vpd.put("var5", varOList.get(i).getString("SymbolName"));        //4
            vpd.put("var6", varOList.get(i).getString("FStatus"));        //5
            vpd.put("var7", varOList.get(i).getString("DeptName"));        //13
            vpd.put("var8", varOList.get(i).getString("EquipmentName"));        //13
            vpd.put("var9", varOList.get(i).getString("Remarks"));        //6
            vpd.put("var10", varOList.get(i).getString("CreateTime"));        //7
            vpd.put("var11", varOList.get(i).getString("Creator"));        //8
            vpd.put("var12", varOList.get(i).getString("Modified"));        //9
            vpd.put("var13", varOList.get(i).getString("ModifiedTime"));        //10
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

//    /**
//     * 导入
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/readExcel")
//    @ResponseBody
//    public Object readExcel(@RequestParam(value = "excel", required = false) MultipartFile file) throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String errInfo = "success";
//        StringBuilder message = new StringBuilder();
//        List<String> messageList = new ArrayList<>();
//        PageData pd = new PageData();
//
//        List<Integer> errorRowList = new ArrayList<>();
//        if (null != file && !file.isEmpty()) {
//            int realRowCount = 0;// 真正有数据的行数
//            // 得到工作空间
//            Workbook workbook = null;
//            try {
//                workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            int sheetIndex = 0;
//            // 得到sheet
//            Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, sheetIndex);
//            realRowCount = sheet.getPhysicalNumberOfRows();
//
//            //定义数据库操作列表
//            List<PageData> savePlcList = new ArrayList<>();
//
//            //每循环一次加入
//            StringBuilder field = new StringBuilder();
//
//            for (int rowNum = 1; rowNum <= realRowCount; rowNum++) {
//                Row row = sheet.getRow(rowNum);
//                if (ObjectExcelRead.getCellValue(sheet, row, 0).equals("end")) {
//                    break;
//                } else {
//
//                    //===============================获取Excel表格数据=============================================
//
//                    String ParamName = ObjectExcelRead.getCellValue(sheet, row, 0).trim();  //参数名称
//                    if(null == ParamName || "".equals(ParamName)){
//                        map.put("result", "参数名存在空值"); // 返回结果
//                        return map;
//                    }
//                    String Address = ObjectExcelRead.getCellValue(sheet, row, 1).trim();  //PLC地址
//                    if(null == Address || "".equals(Address)){
//                        map.put("result", "PLC地址存在空值"); // 返回结果
//                        return map;
//                    }
//                    String ConnectType = ObjectExcelRead.getCellValue(sheet, row, 2).trim();  //连接方式
//                    if(null == ConnectType || "".equals(ConnectType)){
//                        map.put("result", "连接方式存在空值"); // 返回结果
//                        return map;
//                    }
//                    String Passageway = ObjectExcelRead.getCellValue(sheet, row, 3).trim();  //通道名
//                    if(null == Passageway || "".equals(Passageway)){
//                        map.put("result", "通道名存在空值"); // 返回结果
//                        return map;
//                    }
//                    String SymbolName = ObjectExcelRead.getCellValue(sheet, row, 4).trim();  //符号名
//                    if(null == SymbolName || "".equals(SymbolName)){
//                        map.put("result", "符号名存在空值"); // 返回结果
//                        return map;
//                    }
//                    String FStatus = ObjectExcelRead.getCellValue(sheet, row, 5).trim();  //状态
//                    if(null == FStatus || "".equals(FStatus)){
//                        map.put("result", "状态存在空值"); // 返回结果
//                        return map;
//                    }
//                    String SaveTable = ObjectExcelRead.getCellValue(sheet, row, 6).trim();  //存储表名
//                    if(null == SaveTable || "".equals(SaveTable)){
//                        map.put("result", "存储表名存在空值"); // 返回结果
//                        return map;
//                    }
//                    String SaveField = ObjectExcelRead.getCellValue(sheet, row, 7).trim();  //存储字段
//                    if(null == SaveField || "".equals(SaveField)){
//                        map.put("result", "存储字段存在空值"); // 返回结果
//                        return map;
//                    }
//                    String Loop = ObjectExcelRead.getCellValue(sheet, row, 8).trim();  //所属回路
//                    if(null == Loop || "".equals(Loop)){
//                        map.put("result", "所属回路存在空值"); // 返回结果
//                        return map;
//                    }
//                    String ParamType = ObjectExcelRead.getCellValue(sheet, row, 9).trim();  //参数类型
//                    if(null == ParamType || "".equals(ParamType)){
//                        map.put("result", "参数类型存在空值"); // 返回结果
//                        return map;
//                    }
//                    String FUnit = ObjectExcelRead.getCellValue(sheet, row, 10).trim();  //单位
//                    if(null == FUnit || "".equals(FUnit)){
//                        map.put("result", "单位存在空值"); // 返回结果
//                        return map;
//                    }
//                    String Equipment = ObjectExcelRead.getCellValue(sheet, row, 11).trim();  //设备
//                    if(null == Equipment || "".equals(Equipment)){
//                        map.put("result", "设备存在空值"); // 返回结果
//                        return map;
//                    }
//                    String ParamAttribute = ObjectExcelRead.getCellValue(sheet, row, 12).trim();  //参数属性
//                    if(null == ParamAttribute || "".equals(ParamAttribute)){
//                        map.put("result", "参数属性存在空值"); // 返回结果
//                        return map;
//                    }
//
//                    //=======================================数据校验开始======================================================
//
//                    //校验IPV4地址
//                    boolean isIpv4 = IPUtil.isValidIpv4Addr(Address);
//                    if(!isIpv4){
//                        map.put("result", "PLC地址格式不正确"); // 返回结果
//                        return map;
//                    }
//
//                    //校验存储表名
//                    if(!"T_PatraElectric".equals(SaveTable) && !"T_PatraWater".equals(SaveTable) && !"T_PatraGas".equals(SaveTable)){
//                        map.put("result", "存储表名填写错误"); // 返回结果
//                        return map;
//                    }
//
//                    //校验参数类型是否属于存储表
//
//
//                    //校验参数属性是否属于参数类型
//
//
//                    //校验存储字段是否被占用
//
//
//                    //校验回路名与设备名称是否存在 存在则获取ID
//                    String Equipment_ID = "";
//                    String Loop_ID = "";
//                    PageData findLoop = new PageData();
//                    findLoop.put("FNAME",Loop);
//
//                    List<PageData> siteList = siteService.findByName(findLoop);
//                    if(siteList.size() > 0){
//                        Loop_ID = siteList.get(0).getString("DM_SITE_ID");
//                    }else {
//                        List<PageData> areaList = areaService.findByName(findLoop);
//                        if(areaList.size() > 0){
//                            Loop_ID = areaList.get(0).getString("AREA_ID");
//                        }else {
//                            List<PageData> loopList = loopService.findByName(findLoop);
//                            if(loopList.size() > 0){
//                                Loop_ID = loopList.get(0).getString("LOOP_ID");
//
//                                //根据设备名查询ID
//                                PageData findEquipment = new PageData();
//                                findEquipment.put("FNAME",Equipment);
//                                findEquipment.put("Loop_ID",Loop_ID);
//                                List<PageData> eqList = equipmentsService.findByName(findEquipment);
//                                if(eqList.size() > 0){
//                                    Equipment_ID = eqList.get(0).getString("EQUIPMENT_ID");
//                                }else {
//                                    map.put("result", "填写的回路下不存在该设备,行数为:"+rowNum); // 返回结果
//                                    return map;
//                                }
//                            }else {
//                                map.put("result", "回路不存在,行数为:"+rowNum); // 返回结果
//                                return map;
//                            }
//                        }
//                    }
//
//                    //=======================将数据表值存入数据表操作参数pd========================
//                    //存放插入及更新参数
//                    PageData savePlc = new PageData();
//
////                    savePlc.put("SampleNum", SampleNum);  //样品编号
////                    savePlc.put("FeedHouse", FeedHouse);  //养户/栋舍
////                    savePlc.put("FSource", FSource);   //猪苗/外采来源
////                    savePlc.put("FCategory", FCategory);  //猪只类别
////                    savePlc.put("FNumber", FNumber);  //耳号/栏号
////                    savePlc.put("FPhase", FPhase);  //日龄/妊娠阶段/胎次
////
////                    savePlc.put("FCtime", FCtime);  //采样时间
////
////                    savePlc.put("ZW_MY_DATE", ZW_MY_DATE);  //猪瘟疫苗免疫日期
////                    savePlc.put("ZW_YM_CZ", ZW_YM_CZ);  //猪瘟疫苗生产厂家
////                    savePlc.put("WKQ_MY_DATE", WKQ_MY_DATE);  //伪狂犬免疫日期
////                    savePlc.put("WKQ_YM_CZ", WKQ_YM_CZ);  //伪狂犬疫苗生产厂家
////                    savePlc.put("LE_MY_DATE", LE_MY_DATE);  //蓝耳疫苗免疫日期
////                    savePlc.put("LE_YM_CZ", LE_YM_CZ);  //蓝耳疫苗生产厂家
////                    savePlc.put("KTY_MY_DATE", KTY_MY_DATE);  //口蹄疫疫苗免疫日期
////                    savePlc.put("KTY_YM_CZ", KTY_YM_CZ);  //口蹄疫疫苗生产厂家
////                    savePlc.put("YH_MY_DATE", YH_MY_DATE);  //圆环疫疫苗生产厂家
////                    savePlc.put("YH_YM_CZ", YH_YM_CZ);  //圆环疫疫苗生产厂家
////
////                    savePlc.put("SampleProperties", SampleProperties);  //样品性状
////                    savePlc.put("ClinicalSymptoms", ClinicalSymptoms);  //临床症状简述
////
////                    savePlc.put("FCreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd")); //操作时间
////                    savePlc.put("FCreator", Jurisdiction.getUsername());   //操作人
////
////                    savePlc.put("TestPurpose", TestPurpose);   //检测目的
////                    savePlc.put("TestItems", TestItems);   //检测项
////
////                    savePlc.put("AntigenResult", AntigenResult);   //抗原检测结果
////                    savePlc.put("AntigenNum", AntigenNum);   //抗原判定数值
////                    savePlc.put("AntigenFResult", AntigenFResult);   //抗原复检结果
////                    savePlc.put("AntigenFNum", AntigenFNum);   //抗原复检判定数值
////                    savePlc.put("AntigenSJH", "");   //抗原试剂盒品牌
////                    savePlc.put("AntibodyOD", "");   //抗体OD值
////                    savePlc.put("AntibodyNValue", "");   //抗体阴性对照值
////                    savePlc.put("AntibodyPValue", "");   //抗体阳性对照值
////                    savePlc.put("AntibodySJH", "");   //抗体试剂盒品牌
////                    savePlc.put("FType", "抗原");   //抗体试剂盒品牌
////                    savePlc.put("FMethod", FMethod);
////
////                    //==========================结束============================================
////
////
////                    //封装批量插入list 存入主键及任务主键
////                    savePlc.put("Sample_ID", this.get32UUID());
////                    savePlc.put("InspectionTask_ID", pd.getString("InspectionTask_ID"));
////                    savePlc.put("CreateTime",0);
////                    savePlc.put("Creator",0);
////                    savePlcList.add(savePlc);
//
//                    //=======================================数据操作结束======================================================
//
//                }
//            }
//
//            //调用mapper接口进行批量操作
//            if (savePlcList.size() > 0) {
//                if (savePlcList.size() <= 50) {
//                    //把查询的结果存入合并子表中，根据自己的情况到mapper层
////                    plcService.saveAll(saveSampleList);
//                } else {
//                    // 每次插入数据库的数据量
//                    int preInsertDataCount = 50;
//                    // 可遍历的插入数据库的次数
//                    int insertSqlCount = 0;
//                    // 总数据条数
//                    int totalDataCount = savePlcList.size();
//                    if (totalDataCount % preInsertDataCount == 0) {
//                        insertSqlCount = totalDataCount / preInsertDataCount;
//                    } else {
//                        insertSqlCount = totalDataCount / preInsertDataCount + 1;
//                    }
//                    for (int i = 0; i < insertSqlCount; i++) {
//                        int startNumber = i * preInsertDataCount;
//                        int endUnmber = (i + 1) * preInsertDataCount;
//                        if (endUnmber > totalDataCount) {
//                            endUnmber = totalDataCount;
//                        }
//                        List<PageData> subListOK = savePlcList.subList(startNumber, endUnmber);
//                        //根据自己的情况到mapper层
////                        sampleInformationService.saveAll(subListOK);
//                    }
//                }
//            }
//        }
//
//        map.put("errorRowList", errorRowList); // 返回结果
//        map.put("result", errInfo); // 返回结果
//        map.put("errLog", message); // 返回结果
//        return map;
//    }


}
