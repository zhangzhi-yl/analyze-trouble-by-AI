package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WORKINGPROCEDURESELFCHECKEXAMPLEMapper;
import org.yy.service.km.WORKINGPROCEDURESELFCHECKEXAMPLEService;

/** 
 * 说明： 自检执行接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-07-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WORKINGPROCEDURESELFCHECKEXAMPLEServiceImpl implements WORKINGPROCEDURESELFCHECKEXAMPLEService{

	@Autowired
	private WORKINGPROCEDURESELFCHECKEXAMPLEMapper workingprocedureselfcheckexampleMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		workingprocedureselfcheckexampleMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		workingprocedureselfcheckexampleMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		workingprocedureselfcheckexampleMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return workingprocedureselfcheckexampleMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return workingprocedureselfcheckexampleMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return workingprocedureselfcheckexampleMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		workingprocedureselfcheckexampleMapper.deleteAll(ArrayDATA_IDS);
	}

	/**一键修改是否符合
	 * @param pd
	 */
	@Override
	public void editAll(PageData pd) throws Exception {
		workingprocedureselfcheckexampleMapper.editAll(pd);
	}
	
}

