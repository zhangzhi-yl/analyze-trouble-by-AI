package org.yy.service.km;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 编码规则接口 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
public interface CodingRulesService {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception;

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

	/**
	 * 列表(条件查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listByCondition(PageData pd) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

	/**
	 * 根据规则类型和明细类型获取规则号 比如说 你想要 物料编号类型的 流水号那么就传 ruleType = WLBH(对应物料编号的类型) ,
	 * 
	 * @param detailType
	 * @return
	 * @throws Exception
	 */
	public Object getRuleNumByRuleType(String ruleType) throws Exception;

	/**
	 * 列表(根据规则类型和规则明细类型查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getDataByCodingRulesType(PageData pd) throws Exception;

}
