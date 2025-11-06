package org.yy.util.weixin;

 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
 













import javax.net.ssl.HttpsURLConnection;
 













import net.sf.json.JSONObject;
 













import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


 
 
/**
 * @author flyman 2015-8-7
 */
 
public class SendWeChatMessageMes {
    // 系统properties文件名称
    private static final String EMAILCONFIG = "emailAndMsgConfig";
    // 发送消息类型
    private final static String MSGTYPE = "text";
 
    // 发送消息分组所有成员
    // private final static String TOPARTY = "@all";
    // 获取配置文件中的值
    private final static String CORPID = "wxfb8de739cb407bb3";// 需要自己申请，官网有试用企业号
   // private final static String CORPID = "wxfb8de739cb407bb3";// 需要自己申请，官网有试用企业号
    // 获取访问权限码URL
    //private final static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    private final static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wxfb8de739cb407bb3&corpsecret=X2k1qTCvUPWD1bYZWyBRTzPohFbR6ZycAXEnOtZF6W0";
    // 创建会话请求URL
    private final static String CREATE_SESSION_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
   
    // 获取接口访问权限码
    public String getAccessToken() throws Exception {
    	WebApplicationContext webctx=ContextLoader.getCurrentWebApplicationContext();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(ACCESS_TOKEN_URL);
        post.releaseConnection();
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        // NameValuePair[] param = { new NameValuePair("corpid", CORPID), new NameValuePair("corpsecret", CORPSECRET) };
        NameValuePair[] param = {};
        // 设置策略，防止报cookie错误
        DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        // 给post设置参数
        post.setRequestBody(param);
        String result = "";
        try {
            client.executeMethod(post);
            result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jasonObject = JSONObject.fromObject(result);
        result = (String) jasonObject.get("access_token");
        post.releaseConnection();
        return result;
 
    }
 
    /**
     * 企业接口向下属关注用户发送微信消息(实现方式一)
     *
     * @param touser
     *            成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，
     *            则向关注该企业应用的全部成员发送
     * @param toparty
     *            部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
     * @param totag
     *            标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
     * @param content
     *            消息内容
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    public String sendWeChatMessage(String touser, String toparty, String totag, String content) throws Exception {
        HttpClient client = new HttpClient();
        String ACCESS_TOKEN = getAccessToken();
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"touser\":" + "\"" + touser + "\",");
        sb.append("\"toparty\":" + "\"" + toparty + "\",");
        sb.append("\"totag\":" + "\"" + totag + "\",");
        sb.append("\"msgtype\":" + "\"" + "text" + "\",");
        sb.append("\"agentid\":" + "\"" + "16" + "\",");
        sb.append("\"text\":" + "{");
        sb.append("\"content\":" + "\"" + content + "\"},");
        sb.append("\"debug\":" + "\"" + "1" + "\"");
        sb.append("}");
        // 请求链接
        String url = CREATE_SESSION_URL + ACCESS_TOKEN;
        PostMethod post = new PostMethod(url);
        post.releaseConnection();
        post.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        // 设置策略，防止报cookie错误
        DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        // 给post设置参数
        post.setRequestBody(sb.toString());
        String result = "";
        try {
            client.executeMethod(post);
            result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println(result);
 
        post.releaseConnection();
 
        return result;
 
    }
 
    /**
     * 此方法可以发送任意类型消息
     *
     * @param msgType
     *            text|image|voice|video|file|news
     * @param touser
     *            成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，
     *            则向关注该企业应用的全部成员发送
     * @param toparty
     *            部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
     * @param totag
     *            标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
     * @param content
     *            msgType=text时 ,文本消息内容
     * @param mediaId
     *            msgType=image|voice|video时 ,对应消息信息ID（--------）
     * @param title
     *            msgType=news|video时，消息标题
     * @param description
     *            msgType=news|video时，消息描述
     * @param url
     *            msgType=news时，消息链接
     * @param picurl
     *            msgType=news时，图片路径
     * @param safe
     *            表示是否是保密消息，0表示否，1表示是，默认0
     * @throws Exception 
     */
    public void sendWeChatMsg(String msgType, String touser, String toparty, String totag, String content, String mediaId, String title,
            String description, String url, String picurl, String safe) throws Exception {
 
        URL uRl;
        String ACCESS_TOKEN = getAccessToken();
        // 拼接请求串
        String action = CREATE_SESSION_URL + ACCESS_TOKEN;
        // 封装发送消息请求json
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"touser\":" + "\"" + touser + "\",");
        sb.append("\"toparty\":" + "\"" + toparty + "\",");
        sb.append("\"totag\":" + "\"" + totag + "\",");
        if (msgType.equals("text")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"text\":" + "{");
            sb.append("\"content\":" + "\"" + content + "\"");
            sb.append("}");
        } else if (msgType.equals("image")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"image\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\"");
            sb.append("}");
        } else if (msgType.equals("voice")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"voice\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\"");
            sb.append("}");
        } else if (msgType.equals("video")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"video\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\",");
            sb.append("\"title\":" + "\"" + title + "\",");
            sb.append("\"description\":" + "\"" + description + "\"");
            sb.append("}");
        } else if (msgType.equals("file")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"file\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\"");
            sb.append("}");
        } else if (msgType.equals("news")) {
            sb.append("\"msgtype\":" + "\"" + msgType + "\",");
            sb.append("\"news\":" + "{");
            sb.append("\"articles\":" + "[");
            sb.append("{");
            sb.append("\"title\":" + "\"" + title + "\",");
            sb.append("\"description\":" + "\"" + description + "\",");
            sb.append("\"url\":" + "\"" + url + "\",");
            sb.append("\"picurl\":" + "\"" + picurl + "\"");
            sb.append("}");
            sb.append("]");
            sb.append("}");
        }
        sb.append(",\"safe\":" + "\"" + safe + "\",");
        sb.append("\"agentid\":" + "\"" + "16" + "\",");
        sb.append("\"debug\":" + "\"" + "1" + "\"");
        sb.append("}");
        String json = sb.toString();
        try {
 
            uRl = new URL(action);
 
            HttpsURLConnection http = (HttpsURLConnection) uRl.openConnection();
 
            http.setRequestMethod("POST");
 
            http.setRequestProperty("Content-Type",
 
            "application/json;charset=UTF-8");
 
            http.setDoOutput(true);
 
            http.setDoInput(true);
 
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//
            // 连接超时30秒
 
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //
            // 读取超时30秒
 
            http.connect();
 
            OutputStream os = http.getOutputStream();
 
            os.write(json.getBytes("UTF-8"));// 传入参数
 
            InputStream is = http.getInputStream();
 
            int size = is.available();
 
            byte[] jsonBytes = new byte[size];
 
            is.read(jsonBytes);
 
            String result = new String(jsonBytes, "UTF-8");
 
            System.out.println("请求返回结果:" + result);
 
            os.flush();
 
            os.close();
 
        } catch (Exception e) {
 
            e.printStackTrace();
 
        }
    }
 
    /**
     * 企业接口向下属关注用户发送微信消息(实现方式二)
     *
     * @param touser
     *            成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，
     *            则向关注该企业应用的全部成员发送
     * @param toparty
     *            部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
     * @param totag
     *            标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
     * @param content
     *            消息内容
     * @param safe
     *            消息是否保密
     * @return
     * @throws Exception 
     */
    public void sendWeChatMsgText(String touser, String toparty, String totag, String content, String safe) throws Exception {
    	//System.out.println("提醒内容:"+getTime());
    	//System.out.println(content);
    	//System.out.println("-------------------------------------");
        URL uRl;
        String ACCESS_TOKEN = getAccessToken();
        // 拼接请求串
        String action = CREATE_SESSION_URL + ACCESS_TOKEN;
        // 封装发送消息请求json
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"touser\":" + "\"" + touser + "\",");
        sb.append("\"toparty\":" + "\"" + toparty + "\",");
        sb.append("\"totag\":" + "\"" + totag + "\",");
 
        sb.append("\"msgtype\":" + "\"" + MSGTYPE + "\",");
        sb.append("\"text\":" + "{");
        sb.append("\"content\":" + "\"" + content + "\"");
        sb.append("}");
 
        sb.append(",\"safe\":" + "\"" + safe + "\",");
        sb.append("\"agentid\":" + "\"" + totag + "\",");
        sb.append("\"debug\":" + "\"" + "1" + "\"");
        sb.append("}");
       // System.out.println(sb.toString());
        String json = sb.toString();
        try {
 
            uRl = new URL(action);
 
            HttpsURLConnection http = (HttpsURLConnection) uRl.openConnection();
 
            http.setRequestMethod("POST");
 
            http.setRequestProperty("Content-Type",
 
            "application/json;charset=UTF-8");
 
            http.setDoOutput(true);
 
            http.setDoInput(true);
 
           // System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//
            // 连接超时30秒
 
            //System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //
            // 读取超时30秒
 
            http.connect();
 
            OutputStream os = http.getOutputStream();
 
            os.write(json.getBytes("UTF-8"));// 传入参数
 
            InputStream is = http.getInputStream();
 
            int size = is.available();
 
            byte[] jsonBytes = new byte[size];
 
            is.read(jsonBytes);
 
            String result = new String(jsonBytes, "UTF-8");
 
           // System.out.println("请求返回结果:" + result);
 
            os.flush();
 
            os.close();
 
        } catch (Exception e) {
 
            e.printStackTrace();
 
        }
    }
    public void sendWeChatMsgPersonText(String touser,String content) throws Exception {
    	//System.out.println("提醒内容:"+getTime());
    	//System.out.println(content);
    	//System.out.println("-------------------------------------");
    	String toparty="@all";
    	String totag="6";
    	String safe="0";
    	URL uRl;
    	String ACCESS_TOKEN = getAccessToken();
    	// 拼接请求串
    	String action = CREATE_SESSION_URL + ACCESS_TOKEN;
    	// 封装发送消息请求json
    	StringBuffer sb = new StringBuffer();
    	sb.append("{");
    	sb.append("\"touser\":" + "\"" + touser + "\",");
    	sb.append("\"toparty\":" + "\"" + toparty + "\",");
    	sb.append("\"totag\":" + "\"" + totag + "\",");
    	
    	sb.append("\"msgtype\":" + "\"" + MSGTYPE + "\",");
    	sb.append("\"text\":" + "{");
    	sb.append("\"content\":" + "\"" + content + "\"");
    	sb.append("}");
    	
    	sb.append(",\"safe\":" + "\"" + safe + "\",");
    	sb.append("\"agentid\":" + "\"" + "6" + "\",");
    	sb.append("\"debug\":" + "\"" + "1" + "\"");
    	sb.append("}");
    	// System.out.println(sb.toString());
    	String json = sb.toString();
    	try {
    		
    		uRl = new URL(action);
    		
    		HttpsURLConnection http = (HttpsURLConnection) uRl.openConnection();
    		
    		http.setRequestMethod("POST");
    		
    		http.setRequestProperty("Content-Type",
    				
    				"application/json;charset=UTF-8");
    		
    		http.setDoOutput(true);
    		
    		http.setDoInput(true);
    		
    		// System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//
    		// 连接超时30秒
    		
    		//System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //
    		// 读取超时30秒
    		
    		http.connect();
    		
    		OutputStream os = http.getOutputStream();
    		
    		os.write(json.getBytes("UTF-8"));// 传入参数
    		
    		InputStream is = http.getInputStream();
    		
    		int size = is.available();
    		
    		byte[] jsonBytes = new byte[size];
    		
    		is.read(jsonBytes);
    		
    		String result = new String(jsonBytes, "UTF-8");
    		
    		// System.out.println("请求返回结果:" + result);
    		
    		os.flush();
    		
    		os.close();
    		
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
    }

	public static String getTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateString = format.format(new Date());
		return currentDateString;
	}
    public static void main(String[] args) throws Exception {
    	SendWeChatMessageMes weChat = new SendWeChatMessageMes();
    	String WXNR="【费用报销审核提醒】\r\n"
				+ "报销事由：1\r\n"
				+ "报销总金额：2\r\n"
				+ "报销申请人：3\r\n"
				+ "申请人部门：4\r\n"
				+ "提醒时间：5\r\n";
	    weChat.sendWeChatMsgText("13163419783", "@all", "1000010", WXNR, "0");
       /* SendWeChatMessage weChat = new SendWeChatMessage();
        //weChat.sendWeChatMsgText("13163419782", "3", "6", "测试……", "0");
    	System.out.println(weChat.getAccessToken());*/
    }
}