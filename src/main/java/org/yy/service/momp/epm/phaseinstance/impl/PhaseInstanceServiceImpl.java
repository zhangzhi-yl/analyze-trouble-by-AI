package org.yy.service.momp.epm.phaseinstance.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PhaseInstanceMapper;
import org.yy.service.momp.epm.phaseinstance.PhaseInstanceService;



/** 
 * 说明： 项目阶段
 * 创建时间：2018-12-18
 * @version
 */
@Service("phaseinstanceService")
public class PhaseInstanceServiceImpl implements PhaseInstanceService{

	@Autowired
	private PhaseInstanceMapper phaseInstanceMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		phaseInstanceMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		phaseInstanceMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		phaseInstanceMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return phaseInstanceMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return phaseInstanceMapper.listAll(pd);
	}
	public List<PageData> listAllGante(PageData pd)throws Exception{
		return phaseInstanceMapper.listAllGante(pd);
	}
	public List<PageData> listGante(PageData pd)throws Exception{
		return phaseInstanceMapper.listGante(pd);
	}
	public List<PageData> listGante2(PageData pd)throws Exception{
		return phaseInstanceMapper.listGante2(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return phaseInstanceMapper.findById(pd);
	}
	public PageData findPhaseID(PageData pd)throws Exception{
		return phaseInstanceMapper.findPhaseID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		phaseInstanceMapper.deleteAll(ArrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.momp.epm.phaseinstance.PhaseInstanceService#listGante3(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listGante3(PageData pd) throws Exception {
		return phaseInstanceMapper.listGante3(pd);
	}
	
}

