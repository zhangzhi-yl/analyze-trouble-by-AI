package org.yy.service.km.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.CodingRulesDetailMapper;
import org.yy.service.km.CodingRulesDetailService;
import org.yy.util.Tools;

/**
 * 说明： 自定义编码详情接口实现类 作者：YuanYes Q356703572 时间：2020-11-06 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class CodingRulesDetailServiceImpl implements CodingRulesDetailService {

	@Autowired
	private CodingRulesDetailMapper codingrulesdetailMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd) throws Exception {
		if (Tools.isEmpty(pd.getString("STARTINGVALUE"))) {
			pd.put("STARTINGVALUE", "0");
		}
		codingrulesdetailMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		codingrulesdetailMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd) throws Exception {
		codingrulesdetailMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return codingrulesdetailMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		return codingrulesdetailMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd) throws Exception {
		return codingrulesdetailMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		codingrulesdetailMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 列表(列表根据id列表查询)
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByIds(String[] ArrayDATA_IDS) throws Exception {

		return codingrulesdetailMapper.listByIds(ArrayDATA_IDS);
	}

}
