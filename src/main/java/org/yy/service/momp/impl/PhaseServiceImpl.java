package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PhaseMapper;
import org.yy.service.momp.PhaseService;

/** 
 * 说明： phase库结构接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-16
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PhaseServiceImpl implements PhaseService{

	@Autowired
	private PhaseMapper phaseMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		phaseMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		phaseMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		phaseMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return phaseMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return phaseMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return phaseMapper.findById(pd);
	}
	public PageData count(PageData pd)throws Exception{
		return phaseMapper.count(pd);
	}
	public PageData findbycode(PageData pd)throws Exception{
		return phaseMapper.findbycode(pd);
	}
	public PageData findbyBM(PageData pd)throws Exception{
		return phaseMapper.findbyBM(pd);
	}
	public PageData findbyPassword(PageData pd)throws Exception{
		return phaseMapper.findbyPassword(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		phaseMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

