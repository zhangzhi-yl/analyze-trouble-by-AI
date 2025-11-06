package org.yy.controller.system;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;

/**
 * 电子秤读数controller
 * @author chen
 *
 */
@Controller
@RequestMapping("/weight")
public class WeighingController extends BaseController {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping(value = "/read")
	@ResponseBody
	public Object add1() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String clientIp = pd.getString("clientIp");
			String message = stringRedisTemplate.opsForValue().get(clientIp);
			String[] megs = message.split(",");
			String type1 = megs[0];
			String type2 = megs[1];
			String type3 = megs[2].substring(0, 1);// 正负
			String tvalue = replaceCharReg(megs[2].substring(1));
			String type4 = "";// 重量
			String type5 = "";// 单位
			if (megs[2].contains("kg")) {
				type4 = tvalue.replaceAll("kg", "");
				type5 = "kg";
			} else if (megs[2].contains("lb")) {
				type4 = tvalue.replaceAll("lb", "");
				type5 = "lb";
			} else {
				type4 = tvalue.replaceAll("g", "");
				type5 = "g";
			}
			pd.put("ST稳定--US不稳定--OL超载：", type1);
			pd.put("GS毛重--NT净重：", type2);
			pd.put("正负 号:", type3);
			map.put("num", type4);
			map.put("unit", type5);
			map.put("pd", pd);
			map.put("result", "success");
			return map;
		} catch (Exception e) {
			pd.put("ST稳定--US不稳定--OL超载：", "US");
			pd.put("GS毛重--NT净重：", "毛重");
			pd.put("正负 号:", "+");
			map.put("num", "0");
			map.put("unit", "g");
			map.put("pd", pd);
			map.put("result", "success");
			return map;
		}

	}

	public static String replaceCharReg(String value) {
		String result = "";

		if (value != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");

			Matcher m = p.matcher(value);

			result = m.replaceAll("");

		}
		return result;

	}
}
