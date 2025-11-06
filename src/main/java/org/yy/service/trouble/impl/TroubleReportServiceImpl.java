package org.yy.service.trouble.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.trouble.TroubleDefineMapper;
import org.yy.mapper.dsno1.trouble.TroubleReportMapper;
import org.yy.service.trouble.TroubleReportService;
import org.yy.service.trouble.TroubleService;
import org.yy.util.AIHttpUtils;

/** 
 * 说明： 隐患管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2025-09-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TroubleReportServiceImpl implements TroubleReportService{

	@Autowired
	private TroubleReportMapper troublereportMapper;
	@Autowired
	private TroubleService troubleService;
	@Autowired
	private TroubleDefineMapper troubledefineMapper;
	@Override
	public void analyze(PageData pd) throws Exception {
		String reportMsg = getTroubleList(pd);
		HashMap<String,String> result = AIHttpUtils.getReport(reportMsg,pd.get("REPORT_TYPE").toString(),pd.get("REPORT_DATE").toString());
		pd.put("RESULT",result.get("result"));
		pd.put("SUGGESTION",result.get("suggestion"));
		pd.put("STATUS","分析结束");
		troublereportMapper.edit(pd);
	}

	@Override
	public void analyzeByWeek(PageData pd) throws Exception {
		String reportMsg = getTroubleList(pd);
		HashMap<String,String> result = AIHttpUtils.getReport(reportMsg,pd.get("REPORT_TYPE").toString(),pd.get("REPORT_DATE").toString());
		pd.put("RESULT",result.get("result"));
		pd.put("SUGGESTION",result.get("suggestion"));
		pd.put("STATUS","分析结束");
		troublereportMapper.edit(pd);
	}

	@Override
	public void analyzeByMonth(PageData pd) throws Exception {
		String reportMsg = getTroubleList(pd);
		HashMap<String,String> result = AIHttpUtils.getReport(reportMsg,pd.get("REPORT_TYPE").toString(),pd.get("REPORT_DATE").toString());
		pd.put("RESULT",result.get("result"));
		pd.put("SUGGESTION",result.get("suggestion"));
		pd.put("STATUS","分析结束");
		troublereportMapper.edit(pd);
	}
	private String getTroubleList(PageData hseTroubleReport) throws Exception {
		PageData trouble = new PageData();
		trouble.put("dateStart",getDateStart(DateUtil.parseDate(hseTroubleReport.get("REPORT_DATE").toString()),hseTroubleReport.get("REPORT_TYPE").toString()));
		trouble.put("dateEnd",getDateEnd(DateUtil.parseDate(hseTroubleReport.get("REPORT_DATE").toString()),hseTroubleReport.get("REPORT_TYPE").toString()));
		trouble.put("RESULT_STATUS","告警");
		trouble.put("IS_ERROR","否");
		List<PageData> list = troubleService.listAll(trouble);
		if(list.isEmpty()){
			return "未发现安全隐患";
		}
		String msg = "| 厂区 | 位置 | 时间 | 状态 | 识别类型 | 告警等级 | 定位描述 |\\n";
		for(PageData hseTrouble:list){
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
			msg += "|" + hseTrouble.get("FACTORY_AREA") +
					" | " + hseTrouble.get("LOCAL_NAME") +
					" | " + hseTrouble.get("CREATE_TIME").toString() +
					" | " + hseTrouble.get("RESULT_STATUS") +
					" | " + defineNames +
					" | " + hseTrouble.get("LEVEL") +
					" | " + hseTrouble.get("ANALYSIS_DESCRIB") + " |\\n";
		}
		return msg;
	}
	public String getDateStart(Date reportDate, String reportType) {
		if(reportDate!=null) {
			if ("日报".equals(reportType)) {
				Date start = DateUtil.beginOfDay(reportDate);
				return DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
			} else if ("周报".equals(reportType)) {
				Date start = DateUtil.beginOfWeek(reportDate);
				return DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
			} else if ("月报".equals(reportType)) {
				Date start = DateUtil.beginOfMonth(reportDate);
				return DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
			}
		}
		return "";
	}
	public String getDateEnd(Date reportDate, String reportType) {
		if(reportDate!=null) {
			if ("日报".equals(reportType)) {
				Date end = DateUtil.endOfDay(reportDate);
				return DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
			} else if ("周报".equals(reportType)) {
				Date end = DateUtil.endOfWeek(reportDate);
				return DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
			} else if ("月报".equals(reportType)) {
				Date end = DateUtil.endOfMonth(reportDate);
				return DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
			}
		}
		return "";
	}
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		troublereportMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		troublereportMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		troublereportMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return troublereportMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return troublereportMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return troublereportMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		troublereportMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

