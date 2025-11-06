package org.yy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：常用工具 作者：YuanYes Q356703572 官网：356703572@qq.com
 */
public class Tools {

	/**
	 * 随机生成六位数验证码
	 * 
	 * @return
	 */
	public static int getRandomNum() {
		Random r = new Random();
		return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
	}

	/**
	 * 随机生成四位数验证码
	 * 
	 * @return
	 */
	public static int getRandomNum4() {
		Random r = new Random();
		return r.nextInt(9000) + 1000;
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	public static int getMonthDayNum(){
		// 获取当月第一天和最后一天
		Calendar cale = null;
		cale = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String firstday, lastday;
		// 获取本月的第一天
		cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		firstday = format.format(cale.getTime());
		// 获取本月的最后一天
		cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		lastday = format.format(cale.getTime());
		//System.out.println("本月第一天和最后一天分别是 ： " + firstday + " and " + lastday);
		//获取本月的总天数【会自动计算出来的，不用担心误差】
		//只需要两行代码即可：
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

		return maxDay;
	}

	/**
	 * 字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @param splitRegex
	 *            分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}
	/**
	 * 往文件里的内容（Projectpath下）
	 * @param filePath  文件路径
	 * @param content  写入的内容
	 */
	public static void writeFileCR(String fileP,String content){
		String filePath = PathUtil.getProjectpath() + fileP;
		try {
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");      
	        BufferedWriter writer=new BufferedWriter(write);          
	        writer.write(content);      
	        writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 检测KEY是否正确
	 * 
	 * @param paraname
	 *            传入参数
	 * @param FKEY
	 *            接收的 KEY
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean checkKey(String paraname, String FKEY) {
		paraname = (null == paraname) ? "" : paraname;
		return MD5.md5(paraname + DateUtil.getDays() + ",fh,").equals(FKEY);
	}

	/**
	 * 读取txt里的全部内容
	 * 
	 * @param fileP
	 *            文件路径
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String readTxtFileAll(String fileP, String encoding) {
		StringBuffer fileContent = new StringBuffer();
		try {
			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // 项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if (filePath.indexOf(":") != 1) {
				filePath = File.separator + filePath;
			}
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					fileContent.append(lineTxt);
					fileContent.append("\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return fileContent.toString();
	}

	/**
	 * 读取Projectpath某文件里的全部内容
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static String readFileAllContent(String fileP) {
		StringBuffer fileContent = new StringBuffer();
		try {
			String encoding = "utf-8";
			File file = new File(PathUtil.getProjectpath() + fileP);// 文件路径
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					fileContent.append(lineTxt);
					fileContent.append("\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件,查看此路径是否正确:" + fileP);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return fileContent.toString();
	}

	public static void main(String[] args) {
		System.out.println(getRandomNum());
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date) {
		if (notEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		} else {
			return null;
		}
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 把时间根据时、分、秒转换为时间段
	 * 
	 * @param StrDate
	 */
	public static String getTimes(String StrDate) {
		String resultTimes = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now;
		try {
			now = new Date();
			java.util.Date date = df.parse(StrDate);
			long times = now.getTime() - date.getTime();
			long day = times / (24 * 60 * 60 * 1000);
			long hour = (times / (60 * 60 * 1000) - day * 24);
			long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			StringBuffer sb = new StringBuffer();
			// sb.append("发表于：");
			if (hour > 0) {
				sb.append(hour + "小时前");
			} else if (min > 0) {
				sb.append(min + "分钟前");
			} else {
				sb.append(sec + "秒前");
			}
			resultTimes = sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultTimes;
	}

	// 该正则表达式可以匹配所有的数字 包括负数、小数
	public static Boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
		String bigStr = null;
		try {
			bigStr = new BigDecimal(str).toString();
			Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
			if (!isNum.matches()) {
				return Boolean.FALSE;
			}
		} catch (Exception e) {
			// 异常 说明包含非数字。
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 
	 * @description time2 是大的时间
	 * 
	 * @param [time1,
	 *            time2]
	 * 
	 * @return java.lang.long
	 * 
	 */
	public static long timeSubtraction(String startTime, String endTime) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		long newTime1 = simpleDateFormat.parse(endTime).getTime();
		long newTime2 = simpleDateFormat.parse(startTime).getTime();
		Long result = newTime1 - newTime2; // 获取两时间相差的毫秒数
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long hour = result % nd / nh; // 获取相差的小时数
		long min = result % nd % nh / nm; // 获取相差的分钟数
		long day = result / nd;
		return (hour * 60 + min + day * 24);
	}

	/**
	 * 两个日期相减
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int caculateTotalTime(String startTime, String endTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date = null;
		Long l = 0L;
		try {
			date = formatter.parse(startTime);
			long ts = date.getTime();
			date1 = formatter.parse(endTime);
			long ts1 = date1.getTime();

			l = (ts - ts1) / (1000 * 60 * 60 * 24);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l.intValue();
	}

	/**
	 * token生成接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String generateToken() throws Exception {
		return UuidUtil.get32UUID();
	}

	/**
	 * 复制map对象
	 * 
	 * @explain 将paramsMap中的键值对全部拷贝到resultMap中； paramsMap中的内容不会影响到resultMap（深拷贝）
	 * @param paramsMap
	 *            被拷贝对象
	 * @param resultMap
	 *            拷贝后的对象
	 */
	public static void mapCopy(Map<?, ?> paramsMap, Map<Object, Object> resultMap) {
		if (resultMap == null)
			resultMap = new HashMap<Object, Object>();
		if (paramsMap == null)
			return;

		Iterator<?> it = paramsMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");
		}
	}
}
