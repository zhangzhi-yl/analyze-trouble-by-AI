package org.yy.service.km.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.tools.Tool;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.CodingRulesDetailMapper;
import org.yy.mapper.dsno1.km.CodingRulesMapper;
import org.yy.service.km.CodingRulesService;
import org.yy.util.Tools;

/**
 * 说明： 编码规则接口实现类 作者：YuanYes Q356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class CodingRulesServiceImpl implements CodingRulesService {

	@Autowired
	private CodingRulesMapper codingrulesMapper;
	@Autowired
	private CodingRulesDetailMapper codingRulesDetailMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd) throws Exception {
		String dataIdsStr = pd.getString("DATA_IDS");
		List<String> dataIDList = Arrays.asList(dataIdsStr.split(","));

		String CodingRulesType = pd.getString("CODINGRULESTYPE");
		String CodingRulesName = pd.getString("CODINGRULESNAME");

		PageData pgData = new PageData();
		pgData.put("CODINGRULESTYPE", CodingRulesType);
		pgData.put("CODINGRULESNAME", CodingRulesName);

		List<PageData> listByCondition = this.listByCondition(pgData);
		if (CollectionUtils.isNotEmpty(listByCondition)) {
			throw new RuntimeException("已经存在了 " + CodingRulesName + " 该名称下 " + CodingRulesType + " 该类型的数据，请勿重复添加");
		}
		if (Tools.isEmpty(pd.getString("INITIALKEY"))) {
			pd.put("INITIALKEY", "0");
		}
		if (Tools.isEmpty(pd.getString("GETVALUE"))) {
			pd.put("GETVALUE", "0");
		}
		for (String dataID : dataIDList) {
			codingRulesDetailMapper.updateRulesIDByDetailID(dataID, String.valueOf(pd.get("CODINGRULES_ID")));
		}
		codingrulesMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		codingrulesMapper.delete(pd);

		// 删除明细表信息
		String ruleId = String.valueOf(pd.get("CODINGRULES_ID"));
		List<PageData> detailListByRuleID = codingRulesDetailMapper.getDetailListByRuleID(ruleId);
		detailListByRuleID.forEach(d -> {
			String CodingRulesDetail_ID = String.valueOf(d.get("CODINGRULESDETAIL_ID"));
			PageData pdData = new PageData();
			pdData.put("CODINGRULESDETAIL_ID", CodingRulesDetail_ID);
			codingRulesDetailMapper.delete(pdData);
		});

	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd) throws Exception {
		String dataIdsStr = pd.getString("DATA_IDS");
		if (Tools.notEmpty(dataIdsStr)) {
			List<String> dataIDList = Arrays.asList(dataIdsStr.split(","));

			String CodingRulesType = pd.getString("CODINGRULESTYPE");
			String CodingRulesName = pd.getString("CODINGRULESNAME");

			PageData pgData = new PageData();
			pgData.put("CODINGRULESTYPE", CodingRulesType);
			pgData.put("CODINGRULESNAME", CodingRulesName);

			// 判断主规则，是否存在相同的名称和类型的数据
			List<PageData> listByCondition = this.listByCondition(pgData);
			if (CollectionUtils.isNotEmpty(listByCondition)) {
				listByCondition.forEach(d -> {
					if (!d.getString("CODINGRULES_ID").equals(pd.getString("CODINGRULES_ID"))) {
						throw new RuntimeException(
								"已经存在了 " + CodingRulesName + " 该名称下 " + CodingRulesType + " 该类型的数据，请勿重复添加");
					}
				});
			}
			dataIDList.forEach(d -> {
				codingRulesDetailMapper.updateRulesIDByDetailID(d, String.valueOf(pd.get("CODINGRULES_ID")));
			});
		}
		codingrulesMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return codingrulesMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		return codingrulesMapper.listAll(pd);
	}

	/**
	 * 列表(条件查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByCondition(PageData pd) throws Exception {
		return codingrulesMapper.listByCondition(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd) throws Exception {
		List<PageData> codingRuleDetails = codingRulesDetailMapper
				.getDetailListByRuleID(String.valueOf(pd.get("CODINGRULES_ID")));
		PageData pageData = codingrulesMapper.findById(pd);
		pageData.put("codingRuleDetails", codingRuleDetails);
		return pageData;
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		codingrulesMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据规则类型获取规则号
	 * 
	 * @param detailType
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getRuleNumByRuleType(String ruleType) throws Exception {

		// 根据规则类型 查询规则详情
		PageData pgData = new PageData();
		pgData.put("CODINGRULESTYPE", ruleType);

		List<PageData> dataByCodingRulesType = this.getDataByCodingRulesType(pgData);
		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
			throw new RuntimeException("获取规则号失败，该类型数据不存在");
		}

		String returnCode = "";
		String CODINGRULEID = "";
		String ACQUISITIONTIME = "";
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (PageData pageData : dataByCodingRulesType) {
			// 根据详情类型 生成对应的编号
			CODINGRULEID = pageData.getString("CODINGRULES_ID");
			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
			String DETAILTYPE = pageData.getString("DETAILTYPE");
			String FLENGTH = pageData.getString("FLENGTH");
			String FFORMAT = pageData.getString("FFORMAT");
			String TSTEP = pageData.getString("TSTEP");
			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
			String RESETPERIOD = pageData.getString("RESETPERIOD");
			// 进制问题需要确定
			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");

			// 判断是否过期

			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
			Date parse = null;
			try {
				parse = sdFormat.parse(termDateTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long time = parse.getTime();
			long currentTimeMillis = new Date().getTime();
			if (time < currentTimeMillis) {
				throw new RuntimeException("该编码规则已过期");
			}

			// 常量类型直接返回当初的设置值
			if ("constant".equals(DETAILTYPE)) {
				returnCode += SETTINGVALUE;
			}

			// 日期类型直接返回当初的设置格式后 格式化时间返回
			if ("date".equals(DETAILTYPE)) {
				String code = "";
				String acqNow = sdFormat.format(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
				try {
					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						code = sdf.format(new Date());
					}
				}
				returnCode += code;
			}

			// 根据规则类型生成流水号
			if ("serial".equals(DETAILTYPE)) {
				String acqNow = sdFormat.format(new Date());
				// 如果每天重置，日期变更 重新设置位1开始
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						SETTINGVALUE = "0";
					}
				}
				// 如果不重置，则接着前一天的时间继续编号
				else {
					acqNow = ACQUISITIONTIME;
				}

				// 自增 步长
				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

				// 回写数据
				PageData pgRuleDetail = new PageData();
				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
				PageData findDetailById = codingRulesDetailMapper.findById(pgRuleDetail);
				findDetailById.put("SETTINGVALUE", formatNum);
				codingRulesDetailMapper.edit(findDetailById);
				// 返回結果
				returnCode += formatNum;
			}
		}
		// 更新数据
		PageData pgRule = new PageData();
		pgRule.put("CODINGRULES_ID", CODINGRULEID);
		PageData findById = codingrulesMapper.findById(pgRule);
		findById.put("GETVALUE", returnCode);
		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
		codingrulesMapper.edit(findById);
		return returnCode;
	}

	private  String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);

	}

	/**
	 * 列表(根据规则类型和规则明细类型查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getDataByCodingRulesType(PageData pd) throws Exception {
		return codingrulesMapper.getDataByCodingRulesType(pd);
	}

}
