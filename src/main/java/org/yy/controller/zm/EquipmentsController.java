package org.yy.controller.zm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.zm.EQM_FILESService;
import org.yy.service.zm.PLCService;
import org.yy.util.*;
import org.yy.entity.PageData;
import org.yy.service.zm.EquipmentsService;

/**
 * 说明：照明
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-08
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/equipments")
public class EquipmentsController extends BaseController {

    @Autowired
    private EquipmentsService equipmentService;

    @Autowired
    private EQM_FILESService eqm_filesService;

    @Autowired
    private PLCService plcService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 保存
     * v2 20211011 sunlz 附件上传
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
//	@RequiresPermissions("equipment:add")
    @ResponseBody
    public Object add(@RequestParam(value = "PATH", required = false) MultipartFile file,@RequestParam(value = "PATHOPEN", required = false) MultipartFile fileOPEN,@RequestParam(value = "PATHCLOSE", required = false) MultipartFile fileCLOSE) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("EQUIPMENT_ID", this.get32UUID());    //主键
        pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pd.put("Creator", Jurisdiction.getName());
        pd.put("FStatus", "0");
        //上传开启图片
        if (null != fileOPEN && !fileOPEN.isEmpty()) {
            if (null != pd.getString("FileNameOpen") && !"".equals(pd.getString("FileNameOpen"))) {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                if (null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))) {
                    String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
                    String fileNamereal = pd.getString("FileNameOpen").substring(0, pd.getString("FileNameOpen").indexOf(".")); // 文件上传路径
                    fileName = FileUpload.fileUp(fileOPEN, filePath, fileNamereal + dateName);// 执行上传
                    String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
                    pd.put("FileOPEN", FPFFILEPATH);
                } else {
                    errInfo = "201";  //设备主键未获取到
                }
            } else {
                errInfo = "202";   //附件名称未获取到
            }
        }
        //上传关闭图片
        if (null != fileCLOSE && !fileCLOSE.isEmpty()) {
            if (null != pd.getString("FileNameClose") && !"".equals(pd.getString("FileNameClose"))) {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                if (null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))) {
                    String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
                    String fileNamereal = pd.getString("FileNameClose").substring(0, pd.getString("FileNameClose").indexOf(".")); // 文件上传路径
                    fileName = FileUpload.fileUp(fileCLOSE, filePath, fileNamereal + dateName);// 执行上传
                    String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
                    pd.put("FileCLOSE", FPFFILEPATH);
                } else {
                    errInfo = "201";  //设备主键未获取到
                }
            } else {
                errInfo = "202";   //附件名称未获取到
            }
        }
        equipmentService.save(pd);
        //附带的文件上传至从表
        if (null != file && !file.isEmpty()) {
            if (null != pd.getString("FileName") && !"".equals(pd.getString("FileName"))) {
                PageData filePd = new PageData();
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                if (null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))) {
                    String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
                    String fileNamereal = pd.getString("FileName").substring(0, pd.getString("FileName").indexOf(".")); // 文件上传路径
                    fileName = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
                    String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
                    filePd.put("FilePath", FPFFILEPATH);
                    filePd.put("Creator", Jurisdiction.getName());    //创建人
                    filePd.put("CreateTime", pd.getString("CreateTime"));    //创建时间
                    filePd.put("EQUIPMENT_ID", pd.getString("EQUIPMENT_ID"));    //设备主键
                    filePd.put("EQM_FILES_ID", this.get32UUID());    //主键
                    filePd.put("FName", pd.getString("FileName"));    //文件名称
                    filePd.put("FileType", pd.getString("FileType"));    //文件类型

                    //保存附件信息
                    eqm_filesService.save(filePd);

                } else {
                    errInfo = "201";  //设备主键未获取到
                }
            } else {
                errInfo = "202";   //附件名称未获取到
            }
        }

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
        if (Tools.notEmpty(FName)) {
            pd.put("FName", FName.trim());
        }
        List<PageData> eqList = equipmentService.listAll(pd);
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
//	@RequiresPermissions("equipment:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        equipmentService.delete(pd);
        eqm_filesService.deleteByEqm(pd);  //级联删除附件
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
//	@RequiresPermissions("equipment:edit")
    @ResponseBody
    public Object edit(@RequestParam(value = "PATHOPEN", required = false) MultipartFile fileOPEN,@RequestParam(value = "PATHCLOSE", required = false) MultipartFile fileCLOSE) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        //上传开启图片
        if (null != fileOPEN && !fileOPEN.isEmpty()) {
            if (null != pd.getString("FileNameOpen") && !"".equals(pd.getString("FileNameOpen"))) {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                if (null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))) {
                    String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
                    String fileNamereal = pd.getString("FileNameOpen").substring(0, pd.getString("FileNameOpen").indexOf(".")); // 文件上传路径
                    fileName = FileUpload.fileUp(fileOPEN, filePath, fileNamereal + dateName);// 执行上传
                    String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
                    pd.put("FileOPEN", FPFFILEPATH);
                } else {
                    errInfo = "201";  //设备主键未获取到
                }
            } else {
                errInfo = "202";   //附件名称未获取到
            }
        }else {
            pd.put("FileOPEN", pd.getString("PATHOPEN"));
        }
        //上传关闭图片
        if (null != fileCLOSE && !fileCLOSE.isEmpty()) {
            if (null != pd.getString("FileNameClose") && !"".equals(pd.getString("FileNameClose"))) {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                if (null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))) {
                    String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
                    String fileNamereal = pd.getString("FileNameClose").substring(0, pd.getString("FileNameClose").indexOf(".")); // 文件上传路径
                    fileName = FileUpload.fileUp(fileCLOSE, filePath, fileNamereal + dateName);// 执行上传
                    String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
                    pd.put("FileCLOSE", FPFFILEPATH);
                } else {
                    errInfo = "201";  //设备主键未获取到
                }
            } else {
                errInfo = "202";   //附件名称未获取到
            }
        }else {
            pd.put("FileCLOSE", pd.getString("PATHCLOSE"));
        }
        equipmentService.edit(pd);
        map.put("result", errInfo);
        return map;
    }
//    public Object edit() throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String errInfo = "success";
//        PageData pd = new PageData();
//        pd = this.getPageData();
//        equipmentService.edit(pd);
//        map.put("result", errInfo);
//        return map;
//    }
    /**
     *开关设备数量
     */
    @RequestMapping(value = "/openCount")
    @ResponseBody
    public Object openCount() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> sumcountList = equipmentService.getCount(pd);
        map.put("sumcountList", sumcountList);
        map.put("result", errInfo);
        return map;
    }
    /**
     *设备故障
     */
    @RequestMapping(value = "/equipmentFail")
    @ResponseBody
    public Object equipmentFail() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> sumcountList = equipmentService.getFailCount(pd);
        map.put("sumcountList", sumcountList);
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
//	@RequiresPermissions("equipment:list")
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
        String faults = pd.getString("faults");
        if (Tools.notEmpty(faults)) {
            String newDate=new SimpleDateFormat("yyyy-MM").format(new Date());
            if(Integer.parseInt(faults)>=10){
                faults=newDate.substring(0,5)+faults;
            }else{
                faults=newDate.substring(0,5)+"0"+faults;
            }
            pd.put("faults", faults.trim());
        }
        page.setPd(pd);
        List<PageData> varList = equipmentService.list(page);    //列出Equipment列表
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
//	@RequiresPermissions("equipment:list")
    @ResponseBody
    public Object listAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = equipmentService.listAll(pd);    //列出Equipment列表
        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 列表
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/airConditionerList")
    @ResponseBody
    public Object airConditionerList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("FType", "空调");
        //列出Equipment列表
        List<PageData> varList = equipmentService.listAll(pd);

        PageData getPlc = new PageData();
        getPlc.put("ifUse", "ifUse");
        getPlc.put("FModel", "只读");
        getPlc.put("FType", "空调");
        //plc参数列表
        List<PageData> plcList = plcService.listAll(getPlc);

        //从redis读取plc数据并保存
        for (PageData plc : plcList){
            String PLC_ID = plc.get("PLC_ID").toString();
            plc.put("value",stringRedisTemplate.opsForValue().get(PLC_ID));
        }

        for (PageData air : varList) {

            air.put("pattern", "自动");
            air.put("windSpeed", "自动");
            air.put("settingTemperature", "0");
            air.put("actualTemperature", "0");
            air.put("faultCode", "0");

            for (PageData plc : plcList) {
                String ParamType = plc.getString("ParamType");
                //筛选回路下的参数
                if (air.getString("LOOP_ID").equals(plc.getString("LOOP_ID"))) {

                    if ("模式".equals(ParamType)) {
                        String pattern = plc.get("value").toString();
                        if ("0".equals(pattern)) {
                            air.put("pattern", "自动");
                        } else if ("1".equals(pattern)) {
                            air.put("pattern", "送风");
                        } else if ("2".equals(pattern)) {
                            air.put("pattern", "制冷");
                        } else if ("3".equals(pattern)) {
                            air.put("pattern", "除湿");
                        } else if ("4".equals(pattern)) {
                            air.put("pattern", "制热");
                        }

                    } else if ("风速".equals(ParamType)) {
                        String windSpeed = plc.get("value").toString();
                        if ("0".equals(windSpeed)) {
                            air.put("windSpeed", "自动");
                        } else if ("1".equals(windSpeed)) {
                            air.put("windSpeed", "低风");
                        } else if ("2".equals(windSpeed)) {
                            air.put("windSpeed", "中风");
                        } else if ("3".equals(windSpeed)) {
                            air.put("windSpeed", "高风");
                        }
                    } else if ("设定温度".equals(ParamType)) {
                        air.put("settingTemperature", Integer.parseInt(plc.get("value").toString()));
                    } else if ("实际温度".equals(ParamType)) {
                        air.put("actualTemperature", plc.get("value").toString());
                    } else if ("故障代码".equals(ParamType)) {
                        air.put("faultCode", plc.get("value").toString());
                    }
                }
            }
        }

        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 空调
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/airConditioner")
    @ResponseBody
    public Object airConditioner() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();

        pd = this.getPageData();

        PageData equipment = equipmentService.findById(pd);
        String LOOP_ID = equipment.getString("LOOP_ID");

        PageData getPlc = new PageData();
        getPlc.put("ifUse", "ifUse");
        getPlc.put("FModel", "只读");
        getPlc.put("FType", "空调");
        getPlc.put("loop_id", LOOP_ID);
        List<PageData> plcList = plcService.listAll(getPlc);    //plc参数列表

        //从redis读取plc数据并保存
        for (PageData plc : plcList){
            String PLC_ID = plc.get("PLC_ID").toString();
            plc.put("value",stringRedisTemplate.opsForValue().get(PLC_ID));
        }

        //设置默认数据
        PageData air = new PageData();
        air.put("pattern", "自动");
        air.put("windSpeed", "自动");
        air.put("settingTemperature", "0");
        air.put("actualTemperature", "0");
        air.put("faultCode", "0");
        air.put("LOOP_ID", equipment.getString("LOOP_ID"));
        air.put("LoopName", equipment.getString("LoopName"));
        air.put("Location", equipment.getString("Location"));
        air.put("FName", equipment.getString("FName"));
        air.put("LoopStatus", equipment.getString("LoopStatus"));

        //比较ID，若相同则将对应值存入设备信息
        if (null != LOOP_ID && !"".equals(LOOP_ID)) {
            for (PageData plc : plcList) {
                String ParamType = plc.getString("ParamType");
                //筛选回路下的参数
                if (LOOP_ID.equals(plc.getString("LOOP_ID"))) {

                    if ("模式".equals(ParamType)) {
                        air.put("pattern", plc.get("value").toString());
                    } else if ("风速".equals(ParamType)) {
                        air.put("windSpeed", plc.get("value").toString());
                    } else if ("设定温度".equals(ParamType)) {
                        air.put("settingTemperature", plc.get("value").toString());
                    } else if ("实际温度".equals(ParamType)) {
                        air.put("actualTemperature", plc.get("value").toString());
                    } else if ("故障代码".equals(ParamType)) {
                        air.put("faultCode", plc.get("value").toString());
                    }
                }
            }
        }

        map.put("pd", air);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/getByLoop")
//	@RequiresPermissions("equipment:list")
    @ResponseBody
    public Object getByLoop(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> varList = equipmentService.getByLoop(page);    //列出Equipment列表
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 故障数量
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/faultCount")
    @ResponseBody
    public Object faultCount() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<String> timeList = new ArrayList<>();
        List<Integer> valList = new ArrayList<>();
        try {
            List<PageData> varList = equipmentService.getMonthList(pd);
            for(PageData var:varList){
                timeList.add(var.get("FTime").toString());
                valList.add(Integer.parseInt(var.get("FValue").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            errInfo = "程序错误!请联系管理员";
        }
        if (timeList.size() == 0) {
            timeList.add("01");
            timeList.add("02");
            timeList.add("03");
            timeList.add("04");
            timeList.add("05");
            timeList.add("06");
            timeList.add("07");
            timeList.add("08");
            timeList.add("09");
            timeList.add("10");
            timeList.add("11");
            timeList.add("12");
        }
        if (valList.size() == 0) {
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
            valList.add(0);
        }

        map.put("timeList", timeList);
        map.put("valList", valList);
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
//	@RequiresPermissions("equipment:edit")
    @ResponseBody
    public Object goEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = equipmentService.findById(pd);    //根据ID读取
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
//	@RequiresPermissions("equipment:del")
    @ResponseBody
    public Object deleteAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (Tools.notEmpty(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            equipmentService.deleteAll(ArrayDATA_IDS);
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
        titles.add("设备名称");    //1
        titles.add("设备编号");    //2
        titles.add("回路名称");    //3
        titles.add("设备状态");    //4
        titles.add("横坐标");    //5
        titles.add("列坐标");    //6
        titles.add("规格型号");    //7
        titles.add("厂家");    //8
        titles.add("位置");    //9
        titles.add("备注");    //9
        dataMap.put("titles", titles);
        List<PageData> varOList = equipmentService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("FName"));        //1
            vpd.put("var2", varOList.get(i).getString("FNumber"));        //2
            vpd.put("var3", varOList.get(i).getString("LoopName"));        //3
            vpd.put("var4", varOList.get(i).getString("IfWarning"));        //4
            vpd.put("var5", varOList.get(i).getString("FRow"));        //5
            vpd.put("var6", varOList.get(i).getString("FCol"));        //6
            vpd.put("var7", varOList.get(i).getString("FModel"));        //7
            vpd.put("var8", varOList.get(i).getString("Manufactor"));        //8
            vpd.put("var9", varOList.get(i).getString("Location"));        //8
            vpd.put("var10", varOList.get(i).getString("Remarks"));        //9
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
