package org.yy.service.fhoa.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhoa.NoticeMapper;
import org.yy.service.fhoa.NoticeService;
/**
 * 说明： 通知公告接口实现类 作者：YuanYes Q356703572 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeMapper noticeMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		noticeMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		noticeMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		noticeMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return noticeMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return noticeMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return noticeMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		noticeMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 修改已读人员
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void editRead(PageData pd) throws Exception {
		noticeMapper.editRead(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO Auto-generated method stub
		return noticeMapper.AppList(page);
	}

	@Override
	public List<PageData> listAllWD(PageData pd) throws Exception {
		// TODO 自动生成的方法存根
		return noticeMapper.listAllWD(pd);
	}

	@Override
	public List<PageData> listRight(Page page) throws Exception {
		// TODO Auto-generated method stub
		return noticeMapper.listRight(page);
	}

}
