package org.yy.mapper.dsno1.sa;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 报表Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-13
 * 官网：356703572@qq.com
 * @version
 */
public interface StatementMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**项目视角列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageXMSJ(Page page);
	
	/**销售订单列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageXSDD(Page page);
	
	/**计划工单列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageJHGD(Page page);
	
	/**子计划工单列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageJHGDMX(Page page);
	
	/**计划工单工时统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageJHGDTimeWork(Page page);
	
	/**产品生产数量统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageCPSCNum(Page page);
	
	/**产品实际生产数量明细统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageCPSCNumOne(Page page);
	
	/**产品报废生产数量明细统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageCPSCNumTwo(Page page);
	
	/**个人工时统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPagestaffWork(Page page);
	
	/**个人工时明细统计列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPagestaffWorkMx(Page page);
	
	/**异常情况汇总列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageYCQK(Page page);
	
	/**产品质量情况汇总列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageCPZL(Page page);
	
	/**报废明细列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageBF(Page page);
	
	/**返修明细列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageFX(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**任务工时列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageRWGS(Page page);
	
	List<PageData> QAStatement(PageData pd);
}

