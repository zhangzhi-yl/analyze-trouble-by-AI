package org.yy.util;

import java.util.UUID;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONTokener;

/**
 * 说明：生成UUID(32位不重复的字符串)
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	public static int getStrType(String jsonStr) {
		try {
			Object json = new JSONTokener(jsonStr).nextValue();
			if (json instanceof JSONObject) {
				return 1;
			} else if (json instanceof JSONArray) {
				return 2;
			} else {
				return 3;
			} 
		} catch (Exception e) {
				return 3;
		}
	}
		

	public static void main(String[] args) {
		System.out.println(get32UUID());
	}
}
