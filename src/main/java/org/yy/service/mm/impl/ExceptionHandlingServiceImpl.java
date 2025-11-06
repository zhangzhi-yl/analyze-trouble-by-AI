package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.ExceptionHandlingMapper;
import org.yy.service.mm.ExceptionHandlingService;

/** 
 * 说明： 异常处理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ExceptionHandlingServiceImpl implements ExceptionHandlingService{

	@Autowired
	private ExceptionHandlingMapper ExceptionHandlingMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ExceptionHandlingMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ExceptionHandlingMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ExceptionHandlingMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ExceptionHandlingMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ExceptionHandlingMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ExceptionHandlingMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ExceptionHandlingMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 异常移交，修改异常判别状态为移交处理
	 */
	public void EditDisposeType(PageData pd) throws Exception {
		ExceptionHandlingMapper.EditDisposeType(pd);
	}
	/**
	 * 获取最大序号
	 */
	public PageData getReorder(PageData pd) throws Exception {
		return ExceptionHandlingMapper.getReorder(pd);
	}

	/**
	 * 异常处理完成,修改异常判别状态为已处理
	 */
	public void FinishException(PageData pd) throws Exception {
		ExceptionHandlingMapper.FinishException(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		return ExceptionHandlingMapper.AppList(page);
	}
	
}

