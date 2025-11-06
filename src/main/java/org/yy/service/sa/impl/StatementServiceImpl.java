package org.yy.service.sa.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.sa.StatementMapper;
import org.yy.service.sa.StatementService;

/** 
 * 说明： 报表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StatementServiceImpl implements StatementService{

	@Autowired
	private StatementMapper StatementMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		StatementMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		StatementMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		StatementMapper.edit(pd);
	}
	
	/**项目视角列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> XMSJlist(Page page)throws Exception{
		return StatementMapper.datalistPageXMSJ(page);
	}
	
	/**销售订单列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> XSDDlist(Page page)throws Exception{
		return StatementMapper.datalistPageXSDD(page);
	}
	
	/**计划工单列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDlist(Page page)throws Exception{
		return StatementMapper.datalistPageJHGD(page);
	}
	
	/**子计划工单列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDMXlist(Page page)throws Exception{
		return StatementMapper.datalistPageJHGDMX(page);
	}
	
	/**计划工单工时统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JHGDTimeWorklist(Page page)throws Exception{
		return StatementMapper.datalistPageJHGDTimeWork(page);
	}
	
	/**产品生产数量统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumlist(Page page)throws Exception{
		return StatementMapper.datalistPageCPSCNum(page);
	}
	
	/**产品实际生产数量明细统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumOnelist(Page page)throws Exception{
		return StatementMapper.datalistPageCPSCNumOne(page);
	}
	
	/**产品报废生产数量明细统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPSCNumTwolist(Page page)throws Exception{
		return StatementMapper.datalistPageCPSCNumTwo(page);
	}
	
	/**个人工时统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> staffWorklist(Page page)throws Exception{
		return StatementMapper.datalistPagestaffWork(page);
	}
	
	/**个人工时明细统计列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> staffWorkMxlist(Page page)throws Exception{
		return StatementMapper.datalistPagestaffWorkMx(page);
	}
	
	/**异常情况汇总列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> YCQKlist(Page page)throws Exception{
		return StatementMapper.datalistPageYCQK(page);
	}
	
	/**产品质量情况汇总列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> CPZLlist(Page page)throws Exception{
		return StatementMapper.datalistPageCPZL(page);
	}
	
	/**报废明细列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> BFlist(Page page)throws Exception{
		return StatementMapper.datalistPageBF(page);
	}
	
	/**返修明细列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> FXlist(Page page)throws Exception{
		return StatementMapper.datalistPageFX(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return StatementMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return StatementMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		StatementMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**任务工时列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> RWGSlist(Page page)throws Exception{
		return StatementMapper.datalistPageRWGS(page);
	}
	/**
	 * 质检质量报表
	 */
	@Override
	public List<PageData> QAStatement(PageData pd) throws Exception {		
		return StatementMapper.QAStatement(pd);
	}
}

