package org.yy.controller.ny;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.ny.YIELDService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 说明：产量
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/yield")
public class YIELDController extends BaseController {

    @Autowired
    private YIELDService yieldService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/aa")
    @ResponseBody
    public Object aa() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        List<PageData> varList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("0.00");

        double A00 = getValue(1);
        double A01 = getValue(2);
        double A02 = getValue(2);
        double A03 = getValue(2);
        double A04 = getValue(2);
        double A05 = getValue(2);
        double A06 = getValue(5);
        double A07 = getValue(5);
        double A08 = getValue(5);
        double A09 = getValue(5);
        double A10 = getValue(5);
        double A11 = getValue(3);
        double A12 = getValue(3);
        double A13 = getValue(3);
        double A14 = getValue(4);
        double A15 = getValue(4);
        double A16 = getValue(4);

        Random r = new Random();
        PageData v = new PageData();
        v.put("A00",String.format("%.2f",A00));
        v.put("A01",String.format("%.2f",A01));
        v.put("A02",String.format("%.2f",A02));
        v.put("A03",String.format("%.2f",A03));
        v.put("A04",String.format("%.2f",A04));
        v.put("A05",String.format("%.2f",A05));
        v.put("A06",String.format("%.2f",A06));
        v.put("A07",String.format("%.2f",A07));
        v.put("A08",String.format("%.2f",A08));
        v.put("A09",String.format("%.2f",A09));
        v.put("A10",String.format("%.2f",A10));
        v.put("A11",String.format("%.2f",A11));
        v.put("A12",String.format("%.2f",A12));
        v.put("A13",String.format("%.2f",A13));
        v.put("A14",String.format("%.2f",A14));
        v.put("A15",String.format("%.2f",A15));
        v.put("A16",String.format("%.2f",A16));
        varList.add(v);

        for (int i = 0; i < 2159; i++) {
            PageData var = new PageData();
            if(i % 2 == 1){
                var.put("A00",String.format("%.2f",A00+0.1));
                var.put("A01",String.format("%.2f",A01+0.1));
                var.put("A02",String.format("%.2f",A02+0.1));
                var.put("A03",String.format("%.2f",A03+0.1));
                var.put("A04",String.format("%.2f",A04+0.1));
                var.put("A05",String.format("%.2f",A05+0.1));
                var.put("A06",String.format("%.2f",A06+0.1));
                var.put("A07",String.format("%.2f",A07+0.1));
                var.put("A08",String.format("%.2f",A08+0.1));
                var.put("A09",String.format("%.2f",A09+0.1));
                var.put("A10",String.format("%.2f",A10+0.1));
            }else {
                for (int j = 0; j < 12; j++) {
                    double a = getValue(6);
                    double b = getValue(7);
                    double c = getValue(8);
                    if(0 == j){
                        A00 = A00+a;
                    }else if(1 == j){
                        A01 = A01+b;
                    }else if(2 == j){
                        A02 = A02+b;
                    }else if(3 == j){
                        A03 = A03+b;
                    }else if(4 == j){
                        A04 = A04+b;
                    }else if(5 == j){
                        A05 = A05+b;
                    }else if(6 == j){
                        A06 = A06+c;
                    }else if(7 == j){
                        A07 = A07+c;
                    }else if(8 == j){
                        A08 = A08+c;
                    }else if(9 == j){
                        A09 = A09+c;
                    }else if(10 == j){
                        A10 = A10+c;
                    }
                }
                var.put("A00",String.format("%.2f",A00));
                var.put("A01",String.format("%.2f",A01));
                var.put("A02",String.format("%.2f",A02));
                var.put("A03",String.format("%.2f",A03));
                var.put("A04",String.format("%.2f",A04));
                var.put("A05",String.format("%.2f",A05));
                var.put("A06",String.format("%.2f",A06));
                var.put("A07",String.format("%.2f",A07));
                var.put("A08",String.format("%.2f",A08));
                var.put("A09",String.format("%.2f",A09));
                var.put("A10",String.format("%.2f",A10));
            }
            var.put("A11",String.format("%.2f",getValue(3)));
            var.put("A12",String.format("%.2f",getValue(3)));
            var.put("A13",String.format("%.2f",getValue(3)));
            var.put("A14",String.format("%.2f",getValue(4)));
            var.put("A15",String.format("%.2f",getValue(4)));
            var.put("A16",String.format("%.2f",getValue(4)));
            varList.add(var);
        }

        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);

            //添加表中内容
            for(int row = 0;row<varList.size();row++){//数据行
                PageData var = varList.get(row);
                //创建新行
                HSSFRow newrow = sheet.createRow(row);//数据从第二行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                PageData data = varList.get(row);

                newrow.createCell(0).setCellValue(var.getString("A00"));
                newrow.createCell(1).setCellValue(var.getString("A01"));
                newrow.createCell(2).setCellValue(var.getString("A02"));
                newrow.createCell(3).setCellValue(var.getString("A03"));
                newrow.createCell(4).setCellValue(var.getString("A04"));
                newrow.createCell(5).setCellValue(var.getString("A05"));
                newrow.createCell(6).setCellValue(var.getString("A06"));
                newrow.createCell(7).setCellValue(var.getString("A07"));
                newrow.createCell(8).setCellValue(var.getString("A08"));
                newrow.createCell(9).setCellValue(var.getString("A09"));
                newrow.createCell(10).setCellValue(var.getString("A10"));
                newrow.createCell(11).setCellValue(var.getString("A11"));
                newrow.createCell(12).setCellValue(var.getString("A12"));
                newrow.createCell(13).setCellValue(var.getString("A13"));
                newrow.createCell(14).setCellValue(var.getString("A14"));
                newrow.createCell(15).setCellValue(var.getString("A15"));
                newrow.createCell(16).setCellValue(var.getString("A16"));
            }

            //输出Excel文件1
            FileOutputStream output=new FileOutputStream("D:/aa.xls");
            wb.write(output);//写入磁盘
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        map.put("result", varList);
        return map;
    }

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/bb")
    @ResponseBody
    public Object bb() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        List<PageData> varList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("0.00");

        Random r = new Random();

        for (int i = 0; i < 2160; i++) {
            PageData var = new PageData();
            var.put("A00",r.nextInt(2));
            var.put("A01",r.nextInt(2));
            var.put("A02",r.nextInt(2));

            varList.add(var);
        }

        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);

            //添加表中内容
            for(int row = 0;row<varList.size();row++){//数据行
                PageData var = varList.get(row);
                //创建新行
                HSSFRow newrow = sheet.createRow(row);//数据从第二行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                PageData data = varList.get(row);

                newrow.createCell(0).setCellValue(var.get("A00").toString());
                newrow.createCell(1).setCellValue(var.get("A01").toString());
                newrow.createCell(2).setCellValue(var.get("A02").toString());
            }

            //输出Excel文件1
            FileOutputStream output=new FileOutputStream("D:/bb.xls");
            wb.write(output);//写入磁盘
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        map.put("result", varList);
        return map;
    }

    public double getValue(int l){
        double res = 0.0;
        Random r = new Random();
        //顶级初始
        if(l ==1){
            double a = r.nextDouble()*600;
            if(a > 500 && a<600){
                res = a;
            }else {
                res = getValue(1);
            }
        }
        //次级初始
        if(l ==2){
            double a = r.nextDouble()*100;
            if(a > 80 && a<100){
                res = a;
            }else {
                res = getValue(2);
            }
        }
        //三级初始
        if(l ==5){
            double a = r.nextDouble()*80;
            if(a > 60 && a<80){
                res = a;
            }else {
                res = getValue(5);
            }
        }
        //电压
        if(l ==3){
            double a = r.nextDouble()*222;
            if(a > 218 && a<222){
                res = a;
            }else {
                res = getValue(3);
            }
        }
        //电流
        if(l ==4){
            double a = r.nextDouble();
            if(a > 0.1 && a<0.3){
                res = a;
            }else {
                res = getValue(4);
            }
        }
        //增长
        if(l ==6){
            double a = r.nextDouble()*80;
            if(a > 75 && a<80){
                res = a;
            }else {
                res = getValue(6);
            }
        }
        //增长
        if(l ==7){
            double a = r.nextDouble()*15;
            if(a > 13 && a<15){
                res = a;
            }else {
                res = getValue(7);
            }
        }
        if(l ==8){
            double a = r.nextDouble()*13;
            if(a > 11 && a<13){
                res = a;
            }else {
                res = getValue(8);
            }
        }
        return res;
    }


    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("YIELD_ID", this.get32UUID());    //主键
        yieldService.save(pd);
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
    @ResponseBody
    public Object delete() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        yieldService.delete(pd);
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
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        yieldService.edit(pd);
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
        List<PageData> varList = yieldService.list(page);    //列出YIELD列表
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
    @ResponseBody
    public Object goEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = yieldService.findById(pd);    //根据ID读取
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
    @ResponseBody
    public Object deleteAll() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (Tools.notEmpty(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            yieldService.deleteAll(ArrayDATA_IDS);
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
    public ModelAndView exportExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("车间");    //1
        titles.add("产量");    //2
        titles.add("产值");    //3
        titles.add("时间");    //4
        dataMap.put("titles", titles);
        List<PageData> varOList = yieldService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("LoopName"));        //1
            vpd.put("var2", varOList.get(i).getString("YIELD"));        //2
            vpd.put("var3", varOList.get(i).getString("OUTPUT"));        //3
            vpd.put("var4", varOList.get(i).getString("FTime"));        //4
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

}
