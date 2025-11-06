package org.yy.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * @author xsy
 * @Date: 2025/6/20 9:12
 * @Description:
 */
@Component
public class AIHttpUtils {
    @Value("${ai.url}")
    private String url;
    @Value("${ai.keyAnalysis}")
    private String keyAnalysis;
    @Value("${ai.keyPushmsg}")
    private String keyPushmsg;
    @Value("${ai.keyReport}")
    private String keyReport;
    @Value("${ai.keyAnalysisXj}")
    private String keyAnalysisXj;
    @Value("${ai.keyReportXj}")
    private String keyReportXj;
    @Value("${ai.keyAnalysisAction}")
    private String keyAnalysisAction;
    @Value("${ai.keyWeatherForecast}")
    private String keyWeatherForecast;
    @Value("${ai.keyWeatherAction}")
    private String keyWeatherAction;
    @Value("${ai.sourceUrl}")
    private String sourceUrl;
    @Value("${ai.targetUrl}")
    private String targetUrl;
    @PostConstruct
    public void init() {
        URL = url;
//        KEY_ANALYSIS = keyAnalysis;
        KEY_PUSHMSG = keyPushmsg;
        KEY_REPORT = keyReport;
        KEY_ANALYSIS_XJ = keyAnalysisXj;
        KEY_REPORT_XJ = keyReportXj;
        KEY_ANALYSIS_ACTION = keyAnalysisAction;
        KEY_WEATHER_FORECAST = keyWeatherForecast;
        KEY_WEATHER_ACTION = keyWeatherAction;
        SOURCE_URL = sourceUrl;
        TARGET_URL = targetUrl;
    }
    // private static final String URL = "http://172.16.10.92/v1/workflows/run";
    // private static final String URL = "http://crm.iyulong.com:65001/v1/workflows/run";
    // private static String KEY_ANALYSIS = "app-sd90OzXVLiZyStB5tWHd4IBR";     //隐患分析
    // private static String KEY_PUSHMSG = "app-T9tEGsfoGNZSLJMFexdutbDp";      //告警，推送消息
    // private static String KEY_REPORT = "app-A5gZNVgIylgA7yc7XF5cf6jU";       //隐患报告
    // private static String KEY_ANALYSIS_XJ = "app-DXX3Wmbu3Jk02MFyWJAYM3Yr";  //巡检任务分析
    // private static String KEY_REPORT_XJ = "app-hcnhL9Vh1s6nQTCsgeaIul86";    //巡检任务报告
    // private static String KEY_ANALYSIS_ACTION = "app-gLHX5LgD3S7HvmqxIkXhJexw"; //关键动作排查
    // private static String KEY_WEATHER_FORECAST = "app-hNdOCZvOWm0i5JG73GEw3nqR"; //天气预报分析
    // private static String KEY_WEATHER_ACTION = "app-dD7fMjEWWyPFfVBuSXhNSMY5"; //关键动作结合天气
    private static String URL ;
//    private static String KEY_ANALYSIS;     //隐患分析
    private static String KEY_PUSHMSG;      //告警，推送消息
    private static String KEY_REPORT;       //隐患报告
    private static String KEY_ANALYSIS_XJ;  //巡检任务分析
    private static String KEY_REPORT_XJ;    //巡检任务报告
    private static String KEY_ANALYSIS_ACTION; //关键动作排查
    private static String KEY_WEATHER_FORECAST; //天气预报分析
    private static String KEY_WEATHER_ACTION; //关键动作结合天气
    public static String SOURCE_URL; //替换前URL
    public static String TARGET_URL; //替换后URL

    // "http://172.16.30.149:7010/soflyit-hadian/2025/06/20/lsd_20250620095706A005.jpg"
    public static JSONObject analyze(String imgUrl,String defineNames,String KEY_ANALYSIS) {
//        System.out.println("SOURCE_URL = " + SOURCE_URL);
//        System.out.println("TARGET_URL = " + TARGET_URL);
        imgUrl=imgUrl.replace(SOURCE_URL,TARGET_URL);
//        System.out.println("URL = " + URL);
        cn.hutool.json.JSONArray images = new cn.hutool.json.JSONArray();
        cn.hutool.json.JSONObject image = new cn.hutool.json.JSONObject();
        image.put("type","image");
        image.put("transfer_method","remote_url");
        image.put("url",imgUrl);
        images.add(image);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("define",defineNames);
        entries2.put("image",images);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
//        System.out.println(URL);
        post.header("Authorization", "Bearer " + KEY_ANALYSIS);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
//        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();
//        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
//        System.out.println("report = " + result);
        result = result.substring(result.indexOf("</think>")+8);
        JSONObject jsonResult = JSON.parseObject(result);
        System.out.println("jsonResult = " + jsonResult);
        return jsonResult;
    }
    public static boolean pushMsg(String msg) {
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("message",msg);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + KEY_PUSHMSG);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();

        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getString("status");
        System.out.println("report = " + result);
        return "succeeded".equals( result);
    }

    /**
     * desc = "| 厂区 | 位置 | 时间 | 状态 | 识别类型 | 告警等级 | 定位描述 |\n" +
     *                 "|A 厂区 | 一分区储罐区 | 2025-06-08 09:22:15 | 严重告警 | 容器泄漏 | 高危 | 储罐底部阀门密封失效，液体渗漏 |\n" +
     *                 "|A 厂区 | 三分区电缆沟 | 2025-06-10 16:35:47 | 严重告警 | 电气隐患 | 高危 | 电缆绝缘层破损，存在短路风险 |\n" +
     *                 "|B 区 | 成品仓库通道 | 2025-06-12 11:08:32 | 中度告警 | 通道堵塞 | 中危 | 消防通道堆放货物，影响应急疏散 |\n" +
     *                 "|B 区 | 压缩空气站 | 2025-06-14 15:20:59 | 中度告警 | 设备异常 | 中危 | 空压机压力表指针异常摆动，压力不稳定 |\n" +
     *                 "|C 厂区 | 废水处理池 | 2025-06-17 08:45:11 | 严重告警 | 环保隐患 | 高危 | 处理池 PH 值超标，排放水质异常 |\n" +
     *                 "|C 厂区 | 锅炉房管道 | 2025-06-19 07:10:23 | 轻度告警 | 腐蚀迹象 | 低危 | 蒸汽管道外壁局部锈迹，未穿透管壁 |\n" +
     *                 "|A 厂区 | 原料输送皮带 | 2025-06-07 14:55:30 | 中度告警 | 机械故障 | 中危 | 皮带张紧装置松动，运行时打滑 |\n" +
     *                 "|B 区 | 危化品仓库 | 2025-06-11 09:38:46 | 严重告警 | 存储违规 | 高危 | 氧化剂与还原剂混放，未分区隔离 |\n" +
     *                 "|C 厂区 | 变配电室 | 2025-06-13 16:22:05 | 中度告警 | 防护缺失 | 中危 | 绝缘胶垫老化破损，未及时更换 |\n" +
     *                 "|C 厂区 | 冷却塔周边 | 2025-06-15 10:17:33 | 轻度告警 | 环境隐患 | 低危 | 冷却塔周围杂物堆积，影响散热效果 |";
     * @param desc
     * @return
     */
    public static HashMap<String,String> getReport(String desc,String type,String date) {
        System.out.println("URL = " + URL);
        HashMap<String,String> result = report(desc,type,KEY_REPORT,date);
        return result;
    }
    public static HashMap<String,String> report(String desc,String type,String key,String date) {
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("type",type);
        entries2.put("date",date);
        entries2.put("desc",desc);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + key);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        //System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();

        //System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
        String suggestion = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("suggestion");
        //System.out.println("report = " + result);
        HashMap<String,String> resultMap = new HashMap<>();
        resultMap.put("result",result);
        resultMap.put("suggestion",suggestion);
        return resultMap;
    }

    /**
     * 巡检任务分析
     * @return
     */
    public static JSONObject taskAnalyze(String demand, String data, List<String> imgUrls){
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONArray images = new cn.hutool.json.JSONArray();
        for (String imgUrl : imgUrls) {
            cn.hutool.json.JSONObject image = new cn.hutool.json.JSONObject();
            imgUrl = imgUrl.replace(SOURCE_URL, TARGET_URL);
            image.put("type","image");
            image.put("transfer_method","remote_url");
            image.put("url",imgUrl);
            images.add(image);
        }
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("demand",demand);
        entries2.put("demand",demand);
        entries2.put("data",data);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + KEY_ANALYSIS_XJ);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();
        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
        System.out.println("report = " + result);
        result = result.substring(result.indexOf("</think>")+8);
        JSONObject jsonResult = JSON.parseObject(result);
        System.out.println("jsonResult = " + jsonResult);
        return jsonResult;
    }

    /**
     * 巡检任务报告
     * @return
     */
    public static HashMap<String,String> getTaskReport(String desc,String type,String date) {
        System.out.println("URL = " + URL);
        HashMap<String,String> result = report(desc,type,KEY_REPORT_XJ,date);
        return result;
    }

    /**
     * 关键动作排查分析 "define": "[{\"图片序号\":\"1\",\"排查点\":\"排查漏水\"},{\"图片序号\":\"2\",\"排查点\":\"不允许有出现人\"}]",
     *      * image：[{"type": "image","transfer_method": "remote_url","url": "http://172.16.30.149:7010/soflyit-hadian/2025/06/20/gdpl_20250620151454A010.jpg"}]
     * @param imgUrls
     * @return{
     * "result": "总的结果",
     * "data": [{
     *          "number": "图片序号",
     * 			"status": "状态",
     * 			"type": "识别类别",
     * 			"level": "告警等级",
     * 			"desc": "定位描述",
     * 			"suggestion": "外置建议"
     * }]
     * }
     */
    public static JSONObject actionAnalyze(String define, JSONArray imgUrls, String position){
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("define",define);
        entries2.put("image",imgUrls);
        entries2.put("position",position);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + KEY_ANALYSIS_ACTION);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();
        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
        System.out.println("report = " + result);
        result = result.substring(result.indexOf("</think>")+8);
        JSONObject jsonResult = JSON.parseObject(result);
        System.out.println("jsonResult = " + jsonResult);
        return jsonResult;
    }

    /**
    * 模拟天气预报
    * */
    public static JSONObject weatherAnalyze(){
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries.put("response_mode","blocking");
        entries.put("user","abc-123");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + KEY_WEATHER_FORECAST);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();
        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
        System.out.println("report = " + result);
//        result = result.substring(result.indexOf("</think>")+8);
        JSONObject jsonResult = JSON.parseObject(result);
        System.out.println("jsonResult = " + jsonResult);
        return jsonResult;
    }

    /**
     * 关键动作结合天气
     * */
    public static JSONObject weatherActionAnalyze(String define, JSONArray imgUrls, String position, String weather){
        System.out.println("URL = " + URL);
        cn.hutool.json.JSONObject entries = new cn.hutool.json.JSONObject();
        cn.hutool.json.JSONObject entries2 = new cn.hutool.json.JSONObject();
        entries2.put("define",define);
        entries2.put("image",imgUrls);
        entries2.put("position",position);
        entries2.put("weather",weather);
        entries.put("response_mode","blocking");
        entries.put("user","sky");
        entries.put("inputs",entries2);
        HttpRequest post = HttpUtil.createPost(URL);
        post.header("Authorization", "Bearer " + KEY_WEATHER_ACTION);
        post.header("Content-Type","application/json");
        post.timeout(120000);
        post.body(entries.toString());
        System.out.println("post.body() = " + entries.toString());
        cn.hutool.http.HttpResponse execute = post.execute();
        System.out.println("execute.body() = " + execute.body());
        JSONObject jsonObject = JSON.parseObject(execute.body());
        String result = jsonObject.getJSONObject("data").getJSONObject("outputs").getString("text");
        System.out.println("report = " + result);
        result = result.substring(result.indexOf("</think>")+8);
        JSONObject jsonResult = JSON.parseObject(result);
        System.out.println("jsonResult = " + jsonResult);
        return jsonResult;
    }
}
