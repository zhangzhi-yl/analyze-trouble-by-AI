package org.yy.service.run.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.run.RUN_FINAL_INSPECTMapper;
import org.yy.service.run.RUN_FINAL_INSPECTService;

/** 
 * 说明： 终检PH执行接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-25
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class RUN_FINAL_INSPECTServiceImpl implements RUN_FINAL_INSPECTService{

	@Autowired
	private RUN_FINAL_INSPECTMapper run_final_inspectMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		run_final_inspectMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		run_final_inspectMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		run_final_inspectMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return run_final_inspectMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return run_final_inspectMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return run_final_inspectMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		run_final_inspectMapper.deleteAll(ArrayDATA_IDS);
	}
	
	
	/**修改时间戳
	 * @param pd
	 * @throws Exception
	 */
	public void editTime(PageData pd)throws Exception{
		run_final_inspectMapper.editTime(pd);
	}
}

