package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.PROJECTMANAGERVIEWMapper;
import org.yy.service.project.manager.PROJECTMANAGERVIEWService;

/** 
 * 说明： 开工会接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-27
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PROJECTMANAGERVIEWServiceImpl implements PROJECTMANAGERVIEWService{

	@Autowired
	private PROJECTMANAGERVIEWMapper projectmanagerviewMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		projectmanagerviewMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		projectmanagerviewMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		projectmanagerviewMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return projectmanagerviewMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return projectmanagerviewMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return projectmanagerviewMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		projectmanagerviewMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**甘特列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listGanTe(PageData pd)throws Exception{
		return projectmanagerviewMapper.listGanTe(pd);
	}
	
	/**依据甘特图返回数据修改
	 * @param pd
	 * @throws Exception
	 */
	public void editGanTE(PageData pd)throws Exception{
		projectmanagerviewMapper.editGanTE(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.PROJECTMANAGERVIEWService#findByName(org.yy.entity.PageData)
	 */
	@Override
	public PageData findByName(PageData pdT) throws Exception {
		return projectmanagerviewMapper.findByName(pdT);
	}
}

