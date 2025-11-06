package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.PRESALEPLANTWOMapper;
import org.yy.service.project.manager.PRESALEPLANTWOService;

/** 
 * 说明： 售前方案计划二级明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PRESALEPLANTWOServiceImpl implements PRESALEPLANTWOService{

	@Autowired
	private PRESALEPLANTWOMapper presaleplantwoMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		presaleplantwoMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		presaleplantwoMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		presaleplantwoMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return presaleplantwoMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return presaleplantwoMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return presaleplantwoMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		presaleplantwoMapper.deleteAll(pd);
	}
	
	/**读取出错删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteFBTwo(PageData pd)throws Exception{
		presaleplantwoMapper.deleteFBTwo(pd);
	}
	
	/**获取材料费、技术费、主要成本
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listProchance(PageData pd)throws Exception{
		return presaleplantwoMapper.listProchance(pd);
	}
	
	/**获取各个工时费用
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByJHNew(PageData pd)throws Exception{
		return presaleplantwoMapper.findByJHNew(pd);
	}
	
	/**依据一级明细ID获取二级明细工时数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getHourByID(PageData pd)throws Exception{
		return presaleplantwoMapper.getHourByID(pd);
	}
}

