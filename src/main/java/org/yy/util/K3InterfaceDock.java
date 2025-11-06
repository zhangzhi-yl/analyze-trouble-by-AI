package org.yy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class K3InterfaceDock {

	public static String GetK3Public(String params[]) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://WWW.YUANYE.COM/cgyhService/YYMETHOD.cgYYPARAMS"
				.replaceAll("WWW.YUANYE.COM", Const.K3SERVERIPANDPORTJK).replaceAll("YYMETHOD", params[0])
				.replaceAll("YYPARAMS", setParams(params)));
		post.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(post);
		String content = EntityUtils.toString(response.getEntity());
		return content;
	}

	public static String GetK3PublicApi(String params1,String params2) throws ClientProtocolException, IOException {  
		String result = null;
		String content = null;
	    HttpClient client = HttpClients.createDefault();
	    URIBuilder builder = new URIBuilder();
	    URI uri = null;
	    try {
	        uri = builder.setScheme("http").setHost("172.188.10.11:8080").setPath("/cgyhService/getK3POrequest.cg").build();
	        HttpPost post = new HttpPost(uri);
	        //设置请求头
	        post.setHeader("Content-Type", "application/json");
	        //String params1="ConnectString={Persist Security Info=True;Provider=SQLOLEDB.1;User ID=sa;Password=yl_0323;Data Source= YulongYunhai;Initial Catalog=AIS20140120174606};UserName=Administrator;UserID=16394;DBMS Name=Microsoft SQL Server;DBMS Version=2000/2005;SubID=super;AcctType=gy;Setuptype=Industry;Language=chs;IP=;K3Version=;MachineName= YulongYunhai";
	        //String params2="{\"Page1\":[{\"FPlanCategory\":\"标准\",\"FBizType\":\"外购入库\",\"FDeptID\":\"000\",\"FBillNo\":\"\",\"Fdate\":\"2020/7/1\",\"Fnote\":\"Note test\",\"FRequesterID\":\"011\",\"FBillerID\":\"Administrator\"}],\"Page2\":[{\"Fauxqty\":10,\"FAuxPropID\":\"\",\"FSecUnitID\":\"\",\"FSecCoefficient\":0,\"FSecQty\":0,\"FAPurchTime\":\"2020/7/1\",\"FLeadTime\":1,\"FFetchTime\":\"2020/7/2\",\"FMTONo\":\"\",\"FItemID\":\"1.01.000.00002\",\"FUnitID\":\"个\",\"FSupplyID\":\"1.112\",\"FPlanMode\":\"7507\"},{\"Fauxqty\":20,\"FAuxPropID\":\"\",\"FSecUnitID\":0,\"FSecCoefficient\":0,\"FSecQty\":0,\"FAPurchTime\":\"2020/7/1\",\"FLeadTime\":1,\"FFetchTime\":\"2020/7/2\",\"FMTONo\":\"\",\"FItemID\":\"1.01.001\",\"FUnitID\":\"KG\",\"FSupplyID\":0,\"FPlanMode\":\"7507\"}]}";
	        params2 = URLEncoder.encode(params2, "UTF-8");
	        //设置请求体
	        post.setEntity(new StringEntity(params1+"yyy,yyy"+params2));
	        //获取返回信息
	        HttpResponse response = client.execute(post);
	        result = response.toString();
	        content = EntityUtils.toString(response.getEntity());
	    } catch (Exception e) {
	    	content="RetCode:500|Success=False|Message:接口请求失败!!";
	    }
		return content; 
	    }  
	

	public static String setParams(String params[]) throws ClientProtocolException, IOException {
		String ParamStr = "";
		for (int i = 1; i < params.length; i++) {
			if (i == 1) {
				ParamStr = "?params" + (i) + "=" + params[i];
			} else {
				ParamStr += "&params" + (i) + "=" + params[i];
			}
		}
		return ParamStr;
	}

	/*public static void main(String[] args) throws ClientProtocolException, IOException {
		//String[] params = { "getMaterial", "1.01.01.001", "AAA", "1111111" };
		String params2="{\"Page1\":[{\"FPlanCategory\":\"标准\",\"FBizType\":\"外购入库\",\"FDeptID\":\"000\",\"FBillNo\":\"\",\"Fdate\":\"2020/7/1\",\"Fnote\":\"Note test\",\"FRequesterID\":\"011\",\"FBillerID\":\"Administrator\"}],\"Page2\":[{\"Fauxqty\":10,\"FAuxPropID\":\"\",\"FSecUnitID\":\"\",\"FSecCoefficient\":0,\"FSecQty\":0,\"FAPurchTime\":\"2020/7/1\",\"FLeadTime\":1,\"FFetchTime\":\"2020/7/2\",\"FMTONo\":\"\",\"FItemID\":\"1.01.000.00002\",\"FUnitID\":\"个\",\"FSupplyID\":\"1.112\",\"FPlanMode\":\"7507\"},{\"Fauxqty\":20,\"FAuxPropID\":\"\",\"FSecUnitID\":0,\"FSecCoefficient\":0,\"FSecQty\":0,\"FAPurchTime\":\"2020/7/1\",\"FLeadTime\":1,\"FFetchTime\":\"2020/7/2\",\"FMTONo\":\"\",\"FItemID\":\"1.01.001\",\"FUnitID\":\"KG\",\"FSupplyID\":0,\"FPlanMode\":\"7507\"}]}";
		String[] params = { "getK3POrequest"};
		System.out.println(GetK3Public(params));
	}*/
	
	/*public static void main(String[] args) throws ClientProtocolException, IOException {
		 String[] params={"getProductStorage",Tools.date2Str(new Date(),"yyyy-MM-dd"),"1"};
		 System.out.println(GetK3PublicWarehousing(params));
	 }*/
	  
	  //查询新增入库
	  public static String GetK3PublicWarehousing(String params[]) throws ClientProtocolException, IOException {   
	  	  HttpClient httpClient = new DefaultHttpClient();  
	  	  HttpPost post = new HttpPost("http://WWW.YUANYE.COM/cgyhService/YYMETHOD.cgYYPARAMS"
						  			  .replaceAll("WWW.YUANYE.COM", Const.K3SERVERIPANDPORTJK).replaceAll("YYMETHOD", params[0])
						  			  .replaceAll("YYPARAMS", setParams(params)));  
	  	  post.setHeader("Content-type", "application/json");  
	  	  HttpResponse response = httpClient.execute(post);  
	  	  String content = EntityUtils.toString(response.getEntity()); 
	  	  return content;   
	  }
}