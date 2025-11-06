package org.yy.service.momp.epm.planinstance.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PlanInstanceMapper;
import org.yy.service.momp.epm.planinstance.PlanInstanceService;


/** 
 * 说明： 项目工单接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PlanInstanceServiceImpl implements PlanInstanceService{

	@Autowired
	private PlanInstanceMapper planinstanceMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		planinstanceMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		planinstanceMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		planinstanceMapper.edit(pd);
	}
	public void deleteJH(PageData pd)throws Exception{
		planinstanceMapper.deleteJH(pd);
	}
	public PageData findUsefulPlanCount(PageData pd)throws Exception{
		return planinstanceMapper.findUsefulPlanCount(pd);
	}
	public void editPAP(PageData pd)throws Exception{
		planinstanceMapper.editPAP(pd);
	}
	public void updatePlanState(PageData pd)throws Exception{
		planinstanceMapper.updatePlanState(pd);
	}
	public PageData findXM(PageData pd)throws Exception{
		return planinstanceMapper.findXM(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return planinstanceMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return planinstanceMapper.listAll(pd);
	}
	public List<PageData> listUserAll(PageData pd)throws Exception{
		return planinstanceMapper.listUserAll(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return planinstanceMapper.findById(pd);
	}
	public PageData findBySno(PageData pd)throws Exception{
		return planinstanceMapper.findBySno(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		planinstanceMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

