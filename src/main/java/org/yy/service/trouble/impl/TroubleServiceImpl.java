package org.yy.service.trouble.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.trouble.TroubleDefineMapper;
import org.yy.mapper.dsno1.trouble.TroubleMapper;
import org.yy.service.trouble.SafetyAlarmService;
import org.yy.service.trouble.TroubleLocalDefineService;
import org.yy.service.trouble.TroubleLocalService;
import org.yy.service.trouble.TroubleService;
import org.yy.util.*;

import javax.imageio.ImageIO;

/** 
 * 说明： 隐患管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2025-09-24
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TroubleServiceImpl implements TroubleService{

	@Autowired
	private TroubleMapper troubleMapper;
	@Autowired
	private TroubleLocalService troublelocalService;
	@Autowired
	private TroubleDefineMapper troubledefineMapper;
	@Autowired
	private TroubleLocalDefineService troublelocaldefineService;
	@Autowired
	private SafetyAlarmService safetyalarmService;

	@Override
	public void pushAlarmMsg() throws Exception {
		PageData pd = new PageData();
		pd.put("STATUS", "分析结束");
		pd.put("RESULT_STATUS","告警");
		List<PageData> varList = troubleMapper.listAll(pd);
		for (PageData pageData : varList) {
			try {
				pushMsg(pageData);
			}catch (Exception e){

			}
		}
	}
	public void pushMsg(PageData hseTrouble) throws Exception {
		List<PageData> defines =troubledefineMapper.selectByLocalId(hseTrouble);//获取对应隐患定义列表
		String defineNames = Optional.ofNullable(defines)
				.orElse(Collections.emptyList())
				.stream()
				.filter(Objects::nonNull) // 过滤null元素
				.filter(obj -> obj instanceof Map) // 确保是Map类型
				.map(obj -> (PageData) obj) // 转换为Map
				.map(map -> map.get("FNAME")) // 提取目标字段值
				.filter(Objects::nonNull) // 过滤字段值为null的情况
				.map(String::valueOf) // 转换为字符串
				.collect(Collectors.joining(","));
		String msg = "【隐患点位】："+hseTrouble.get("LOCAL_NAME") +"\n"+
				"【隐患问题】："+defineNames+"\n"
				+ "【分析结果】："+hseTrouble.get("ANALYSIS_DESCRIB")+"\n"
				+ "【处置建议】："+hseTrouble.get("SUGGESTION");
//		AIHttpUtils.pushMsg(msg);

		net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
		jsonObject.accumulate("subject", "隐患提醒")//待办标题(必填)
				.accumulate("creatorId", "15546480217")//创建者的unionId(非必填)
				.accumulate("msg",msg)//待办备注描述(非必填)
				.accumulate("dueTime", "")//截止时间(非必填)
				.accumulate("isOnlyShowExecutor", "true")//生成的待办是否仅展示在执行者的待办列表中true/false(非必填)
				.accumulate("priority", 20)//优先级10：较低；20：普通；30：紧急；40：非常紧急(非必填)
				.accumulate("isAll", "false");//是否全员接收true/false
		String strDataXsck=jsonObject.toString().replaceAll("null","\"\"");
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		PostMethod postMethod = new PostMethod("https://hook.jijyun.cn/v1/accept/data/webhook_accept_first/smUr9gXB1NvbYdk3GCEHch8zuF4O0tqS");
//		PostMethod postMethod = new PostMethod("https://hook.jijyun.cn/v1/accept/data/developer_open_1000736/ZGV2ZWxvcGVyX29wZW5qaWpfMTAwMzMyM18xMDAyODI1?apikey=FLvPrXMl5SkiH0zEpofhZWj9t1I2qVgn");

		postMethod.addRequestHeader("Content-type", "application/json; charset=utf-8");
		byte[] requestBytes = strDataXsck.getBytes("utf-8"); // 将参数转为二进制流
		InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,requestBytes.length);
		RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,requestBytes.length, "application/json; charset=utf-8"); // 请求体
		postMethod.setRequestEntity(requestEntity);   // 将参数放入请求体
		String errorInfo = "";
//		try {
//			int code = httpClient.executeMethod(postMethod);
//			if (code == 200){
//				errorInfo = postMethod.getResponseBodyAsString();
//				System.out.println("result:" + errorInfo);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		hseTrouble.put("TROUBLELOCAL_ID",hseTrouble.get("LOCAL_ID"));
		PageData hseTroubleLocal = troublelocalService.findById(hseTrouble);

		PageData safetyalarm = new PageData();
		safetyalarm.put("SAFETYALARM_ID", UuidUtil.get32UUID());	//主键
		safetyalarm.put("TROUBLE_ID",hseTrouble.get("TROUBLE_ID"));
		safetyalarm.put("ALARM_PEOPLE",hseTroubleLocal.get("RESPONSIBLE_PEOPLE"));
		safetyalarm.put("CREATE_TIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		safetyalarm.put("ALARM_CONTENT",msg);
		safetyalarm.put("ALARM_STATE","未处理");
		safetyalarmService.save(safetyalarm);
		hseTrouble.put("PUSH_MSG",msg);
		hseTrouble.put("STATUS","已告警");
		hseTrouble.put("REPORT_TIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		troubleMapper.edit(hseTrouble);
	}
	/**
	 * 定时分析摄像头采集画面
	 * @throws Exception
	 */
	@Override
	public void analyzeTask() throws Exception {
		PageData pd = new PageData();
		pd.put("STATUS", "待分析");//获取所有待分析的数据
		List<PageData> varList = troubleMapper.listAll(pd);
		String[] KEY_ANALYSIS = new String[10];
		KEY_ANALYSIS[0] = "app-6ah1Zwp7cMUXfKnWviatBsMH";
		KEY_ANALYSIS[1] = "app-T5DCScvkVJVr35vKgEpaEljS";
		KEY_ANALYSIS[2] = "app-kOxXunT6fRCI7xF7nTfzxlGq";
		KEY_ANALYSIS[3] = "app-7cuuSBB92uwbQFbtHPDUcA4E";
		KEY_ANALYSIS[4] = "app-uclMAQHjJ6hUsCFSKUWHfMJv";
		KEY_ANALYSIS[5] = "app-aydpL5aYkuiAAHwxFW4pgLcq";
		KEY_ANALYSIS[6] = "app-16DWiZolgVkyyi6SXSmwjT6e";
		KEY_ANALYSIS[7] = "app-Q2FP1aXCSpAOnGmVJpBP7T4b";
		KEY_ANALYSIS[8] = "app-Cfs6Ksm9i3bk7BYPgB306u0D";
		KEY_ANALYSIS[9] = "app-OGC3sHFXVqQljFRWiEePE01f";
//		for(int i=0;i<varList.size();i++){
//			PageData pageData = varList.get(i);
//			analyze(pageData,KEY_ANALYSIS[i]);
//		}
//		if(varList.size()>0){
//			PageData pageData = varList.get(0);
//
//		}
//		int loopCount = 5;
		Thread[] threads = new Thread[varList.size()]; // 存储线程引用，方便等待

		for (int i = 0; i < varList.size(); i++) {
			final int index = i;
			PageData pageData = varList.get(i);
			String $KEY_ANALYSIS = KEY_ANALYSIS[i];
			threads[i] = new Thread(() -> {
				try {
					analyze(pageData,$KEY_ANALYSIS);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			threads[i].start(); // 启动线程
		}

		// 等待所有线程完成
		for (Thread thread : threads) {
			thread.join(); // 阻塞当前线程，直到子线程执行完毕
		}
	}

	public void analyze(PageData hseTrouble,String KEY_ANALYSIS) throws Exception{
		PageData troubledefine = new PageData();
		List<PageData> defines =troubledefineMapper.selectByLocalId(hseTrouble);//获取对应隐患定义列表
		String defineNames = Optional.ofNullable(defines)
				.orElse(Collections.emptyList())
				.stream()
				.filter(Objects::nonNull) // 过滤null元素
				.filter(obj -> obj instanceof Map) // 确保是Map类型
				.map(obj -> (PageData) obj) // 转换为Map
				.map(map -> map.get("FNAME")) // 提取目标字段值
				.filter(Objects::nonNull) // 过滤字段值为null的情况
				.map(String::valueOf) // 转换为字符串
				.collect(Collectors.joining(","));
//		String defineNames = defines.stream().map(HseTroubleDefine::getName).collect(Collectors.joining(","));
		hseTrouble.put("ANALYSIS_START_TIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if(hseTrouble.get("IMG_URL") == null ){
			return;
		}
		JSONObject result = AIHttpUtils.analyze(hseTrouble.get("IMG_URL") == null ? "" : hseTrouble.get("IMG_URL").toString(),defineNames,KEY_ANALYSIS);
		hseTrouble.put("ANALYSIS_TEXT",result.toJSONString());
		hseTrouble.put("LEVEL",result.getString("level"));
		hseTrouble.put("RESULT_STATUS",result.getString("status"));
		hseTrouble.put("ANALYSIS_DESCRIB",result.getString("desc"));
		hseTrouble.put("SUGGESTION",result.getString("suggestion"));
		hseTrouble.put("DEFINE_NAMES",result.getString("type"));
		hseTrouble.put("ANALYSIS_END_TIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		hseTrouble.put("STATUS","分析结束");
		troubleMapper.edit(hseTrouble);
	}
	/**
	 * 定时获取摄像头照片，创建数据
	 */
	@Override
	public void createTroubleByUrl() throws Exception{
			PageData pd = new PageData();
			pd.put("CATCH_SWITCH", "true");//获取所有需要抓取的
			List<PageData> varList = troublelocalService.listAll(pd);
			for (int i = 0; i < varList.size(); i++) {
				PageData pageData = varList.get(i);
				String url = pageData.get("URL").toString();
				String CATCH_INTERVAL = pageData.get("CATCH_INTERVAL").toString();
				// 默认值1小时
				if (StringUtils.isBlank(CATCH_INTERVAL)) {
					CATCH_INTERVAL = "1小时";
				}
				// 转换为分钟的数字
				Integer catchIntervalMinute;
				if (CATCH_INTERVAL.contains("分钟")) {
					catchIntervalMinute = Integer.parseInt(CATCH_INTERVAL.replace("分钟", ""));
				}else{
					catchIntervalMinute = Integer.parseInt(CATCH_INTERVAL.replace("小时", ""))*60;
				}
				if (StringUtils.isNotBlank(url)) {
					List<PageData> list = troubleMapper.selectHseTroubleByCatchIntervalMinute(pageData.get("TROUBLELOCAL_ID").toString(),catchIntervalMinute);
					if (!list.isEmpty()) {
						continue;
					}
					Map<String, String> urlMap = parseUrl(url);
					System.out.println(urlMap.toString());
					Thread.sleep(300);
					String image = getImage(urlMap,i);
					PageData hseTrouble = new PageData();
					hseTrouble.put("TROUBLE_ID", UuidUtil.get32UUID());
					hseTrouble.put("LOCAL_ID", pageData.get("TROUBLELOCAL_ID"));
					hseTrouble.put("IMG_URL", image);
					hseTrouble.put("STATUS", "待分析");
					hseTrouble.put("CREATE_BY","系统自动生成");
					hseTrouble.put("CREATE_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
					hseTrouble.put("IS_ERROR","否");
					troubleMapper.save(hseTrouble);

				}




			}
	}



	public Map<String, String> parseUrl(String urlString) {
		try {
			// 创建URL对象
			URL url = new URL(urlString);

			// 获取用户信息部分（格式为username:password）
			String userInfo = url.getUserInfo();
			String username = "";
			String password = "";

			if (userInfo != null && userInfo.contains(":")) {
				String[] userPass = userInfo.split(":", 2);
				username = userPass[0];
				if (userPass.length > 1) {
					password = userPass[1];
				}
			}

			// 获取主机名（这里是IP地址）
			String ip = url.getHost();

			// 获取路径部分
			String path = url.getPath();

			// 输出解析结果
			Map<String, String> resultMap = new HashMap<>();
			resultMap.put("username",username);
			resultMap.put("password",password);
			resultMap.put("ip",ip);
			resultMap.put("path",path);
			return resultMap;

		} catch (MalformedURLException e) {
			System.out.println("URL格式不正确: " + e.getMessage());
		}
		return null;
	}
	public String getImage(Map<String, String> stringStringMap,int i){
		String username = stringStringMap.get("username");
		String password = stringStringMap.get("password");
		String ip = stringStringMap.get("ip");
		String path = stringStringMap.get("path");
		try
		{
			RequestConfig requestConfig = RequestConfig.custom()
					.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.DIGEST))
					.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.DIGEST))
					.build();

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials(username, password, "", ""));

			HttpClient httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credentialsProvider)
					.setDefaultRequestConfig(requestConfig)
					.build();
			HttpHost target = new HttpHost(ip, 80, "http");
			HttpGet httpget = new HttpGet(path);
			HttpResponse r = httpclient.execute(target, httpget);
			HttpEntity e = r.getEntity();
			byte[] decode = EntityUtils.toByteArray(e);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);
			BufferedImage image = ImageIO.read(inputStream);
			ImageIO.write(image, "jpg", new File("D:\\testGetImage\\test.jpg"));
			MultipartFile convert = FileUtilHSE.convert("D:\\testGetImage\\test.jpg");
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = "D:/bushu11033/" + Const.FILEPATHFILE + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(convert, filePath, dateName);// 执行上传
			String url = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
			String allUrl = "http://172.16.30.11:11033/"+url;
			return allUrl;

		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}





	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		troubleMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		troubleMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		troubleMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return troubleMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return troubleMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return troubleMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		troubleMapper.deleteAll(ArrayDATA_IDS);
	}



}

