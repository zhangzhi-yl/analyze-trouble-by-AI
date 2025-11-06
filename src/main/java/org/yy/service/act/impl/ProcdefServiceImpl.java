package org.yy.service.act.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.act.ProcdefMapper;
import org.yy.service.act.ProcdefService;

/** 
 * 说明： 流程管理接口实现类
 * 作者：YuanYe Q356703572
 * 
 * @version
 */
@Service(value="procdefServiceImpl")
@Transactional //开启事物
public class ProcdefServiceImpl implements ProcdefService{

	@Autowired
	private ProcdefMapper procdefMapper;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return procdefMapper.datalistPage(page);
	}
	
}

