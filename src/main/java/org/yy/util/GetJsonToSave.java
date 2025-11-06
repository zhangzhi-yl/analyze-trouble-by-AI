package org.yy.util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.yy.entity.PageData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * 读取标签
 * json读取结果封装为list
 * @author xhy
 */
public class GetJsonToSave{
	public List<PageData> readJson(String filepath) throws Exception {
		File jsonfile = new File(filepath);
		List<PageData> varlist = new ArrayList<>();
		try {
			String input = FileUtils.readFileToString(jsonfile, "UTF-8");
			JSONObject obj = new JSONObject(input);
			JSONObject ylbom = (JSONObject) obj.get("ylbom");
			String materiala = (String) ylbom.get("material").toString();
			Gson gson = new Gson();
			varlist = gson.fromJson(materiala, new TypeToken<List<PageData>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return varlist;
	}
}
