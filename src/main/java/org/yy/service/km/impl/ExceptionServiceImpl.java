package org.yy.service.km.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.ExceptionMapper;
import org.yy.service.km.ExceptionService;

/**
 * 说明： 异常接口实现类 作者：YuanYes Q356703572 时间：2020-11-09 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class ExceptionServiceImpl implements ExceptionService {

	@Autowired
	private ExceptionMapper ExceptionMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		ExceptionMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		ExceptionMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		ExceptionMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return ExceptionMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return ExceptionMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return ExceptionMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		ExceptionMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 下发异常
	 */
	public void goLssue(PageData pd) throws Exception {
		ExceptionMapper.goLssue(pd);
	}

	@Override
	public List<PageData> datalistPageHandling(Page page) throws Exception {
		return ExceptionMapper.datalistPageHandling(page);
	}

	@Override
	public List<PageData> listAllHandling(PageData pd) throws Exception {
		return ExceptionMapper.listAllHandling(pd);
	}

	@Override
	public PageData getHandling(PageData pd) throws Exception {
		return ExceptionMapper.getHandling(pd);
	}

	@Override
	public void FinishException(PageData pd) throws Exception {
		ExceptionMapper.FinishException(pd);
	}

	@Override
	public List<PageData> Applist(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return ExceptionMapper.AppList(page);
	}

	@Override
	public List<PageData> getEQMList(PageData pd) throws Exception {
		return ExceptionMapper.getEQMList(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.km.ExceptionService#listSC(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listSC(Page page) throws Exception {
		return ExceptionMapper.listSClistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.km.ExceptionService#setRead(org.yy.entity.PageData)
	 */
	@Override
	public void setRead(PageData pd) throws Exception {
		ExceptionMapper.setRead(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.km.ExceptionService#listHis(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listHis(Page page) throws Exception {
		return ExceptionMapper.listHislistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.km.ExceptionService#listXM(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listXM(Page page) throws Exception {
		return ExceptionMapper.listXMlistPage(page);
	}

}
