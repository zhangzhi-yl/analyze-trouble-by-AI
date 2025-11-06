package org.yy.service.sa;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 报表接口 作者：YuanYes QQ356703572 时间：2021-01-13 官网：356703572@qq.com
 * 
 * @version
 */
public interface StatementService {

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
	 * 项目视角列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> XMSJlist(Page page) throws Exception;

	/**
	 * 销售订单列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> XSDDlist(Page page) throws Exception;

	/**
	 * 计划工单列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDlist(Page page) throws Exception;

	/**
	 * 子计划工单列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDMXlist(Page page) throws Exception;

	/**
	 * 计划工单工时统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDTimeWorklist(Page page) throws Exception;

	/**
	 * 产品生产数量统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumlist(Page page) throws Exception;

	/**
	 * 产品实际生产数量明细统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumOnelist(Page page) throws Exception;

	/**
	 * 产品报废生产数量明细统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumTwolist(Page page) throws Exception;

	/**
	 * 个人工时统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> staffWorklist(Page page) throws Exception;

	/**
	 * 个人工时明细统计列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> staffWorkMxlist(Page page) throws Exception;

	/**
	 * 异常情况汇总列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> YCQKlist(Page page) throws Exception;

	/**
	 * 产品质量情况汇总列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPZLlist(Page page) throws Exception;

	/**
	 * 报废明细列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> BFlist(Page page) throws Exception;

	/**
	 * 返修明细列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> FXlist(Page page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

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
	 * 任务工时列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> RWGSlist(Page page) throws Exception;
	/**
	 * 质检质量报表
	 */
	public List<PageData> QAStatement(PageData pd) throws Exception;
}
