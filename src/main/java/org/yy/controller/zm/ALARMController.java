package org.yy.controller.zm;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.*;
import org.yy.entity.PageData;
import org.yy.service.zm.ALARMService;

import javax.servlet.http.HttpServletResponse;

/**
 * 说明：报警
 * 作者：YuanYe
 * 时间：2021-10-13
 */
@Controller
@RequestMapping("/alarm")
public class ALARMController extends BaseController {

    @Autowired
    private ALARMService alarmService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
//	@RequiresPermissions("alarm:add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ALARM_ID", this.get32UUID());    //主键
        alarmService.save(pd);
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
//	@RequiresPermissions("alarm:del")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        alarmService.delete(pd);
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
//	@RequiresPermissions("alarm:edit")
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        alarmService.edit(pd);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 报警处理
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/handle")
    @ResponseBody
    public Object handle(@RequestParam(value = "file",required = false) MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        String FPFFILEPATH = "";
        if (null != file && !file.isEmpty()) {
            String dateName = String.valueOf(new Date().getTime() + new Random(100).nextInt());
            String ffile = DateUtil.getDays(), fileName = "";

            String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
            fileName = FileUpload.fileUp(file, filePath, dateName);// 执行上传
            FPFFILEPATH += Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
        }
        pd.put("FILE",FPFFILEPATH);

        pd.put("HANDLENAME",Jurisdiction.getName());
        pd.put("HANDLETIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        pd.put("IFHANDLE","已处理");

        alarmService.handle(pd);
        map.put("result", errInfo);
        return map;
    }

    /**下载模版
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/download")
    public Object downExcel(HttpServletResponse response)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String,Object> map = new HashMap<String,Object>();
        String FPFFILENAME = pd.getString("FFILENAME");
        String FPFFILEPATH = pd.getString("FFILE");
        FileDownload.fileDownload(response, PathUtil.getProjectpath() + FPFFILEPATH, FPFFILENAME);
        return map;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
//	@RequiresPermissions("alarm:list")
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
        List<PageData> varList = alarmService.list(page);    //列出ALARM列表
        for (PageData var : varList){
            String ZMLOOP_NAME = var.getString("LOOP_NAME");
            String NYLOOP_NAME = var.getString("NYLOOP_NAME");
            if("".equals(ZMLOOP_NAME) || null == ZMLOOP_NAME){
                var.put("LOOP_NAME",NYLOOP_NAME);
            }else {
                var.put("LOOP_NAME",ZMLOOP_NAME);
            }
        }
        map.put("varList", varList);
        map.put("page", page);
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
//	@RequiresPermissions("alarm:edit")
    @ResponseBody
    public Object goEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = alarmService.findById(pd);    //根据ID读取
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
//	@RequiresPermissions("alarm:del")
    @ResponseBody
    public Object deleteAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (Tools.notEmpty(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            alarmService.deleteAll(ArrayDATA_IDS);
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
        titles.add("回路名称");    //1
        titles.add("报警级别");
        titles.add("报警内容");    //2
        titles.add("报警时间");    //3
        titles.add("处理状态");
        dataMap.put("titles", titles);
        List<PageData> varOList = alarmService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("LOOP_NAME"));        //1
            vpd.put("var2", varOList.get(i).getString("PRIORITY"));
            vpd.put("var3", varOList.get(i).getString("ALARM_CONTENT"));        //2
            vpd.put("var4", varOList.get(i).getString("ALARM_TIME"));        //3
            vpd.put("var5", varOList.get(i).getString("IFHANDLE"));
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
