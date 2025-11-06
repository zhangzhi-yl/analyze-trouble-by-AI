package org.yy.service.km.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.StandardCapacityMapper;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.StandardCapacityService;

/**
 * 说明： 标准产能接口实现类 作者：YuanYes Q356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class StandardCapacityServiceImpl implements StandardCapacityService {

	@Autowired
	private StandardCapacityMapper standardcapacityMapper;

	@Autowired
	private CodingRulesService codingRulesService;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd) throws Exception {
		pd.put("FCREATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String FNUM = (String) codingRulesService.getRuleNumByRuleType("BZCN");
		pd.put("FNUM", FNUM);
		standardcapacityMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		standardcapacityMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd) throws Exception {
		standardcapacityMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return standardcapacityMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		return standardcapacityMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd) throws Exception {
		return standardcapacityMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		standardcapacityMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 修改状态
	 * 
	 * @throws Exception
	 */
	@Override
	public void changeStatus(PageData pd) throws Exception {
		PageData findById = standardcapacityMapper.findById(pd);
		if ("生效".equals(findById.get("FSTATUS"))) {
			findById.put("FSTATUS", "失效");
		} else {
			findById.put("FSTATUS", "生效");
		}
		standardcapacityMapper.edit(findById);

	}

	@Override
	public List<PageData> listByStationAndWP(String fSTATION, String wP) {
		
		return standardcapacityMapper.listByStationAndWP(fSTATION,wP);
	}

	@Override
	public PageData getByWP(String wP) {
		
		return standardcapacityMapper.getByWP(wP);
	}

}
