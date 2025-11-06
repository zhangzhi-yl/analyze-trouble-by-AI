package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.PRESALEPLANONEMapper;
import org.yy.service.project.manager.PRESALEPLANONEService;


/** 
 * 说明： 售前方案计划一级明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PRESALEPLANONEServiceImpl implements PRESALEPLANONEService{

	@Autowired
	private PRESALEPLANONEMapper presaleplanoneMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		presaleplanoneMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		presaleplanoneMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		presaleplanoneMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return presaleplanoneMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return presaleplanoneMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return presaleplanoneMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		presaleplanoneMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**最大序号+1
	 * @param pd
	 * @throws Exception
	 */
	public PageData maxNum(PageData pd)throws Exception{
		return presaleplanoneMapper.maxNum(pd);
	}
	
	/**读取出错删除明细
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteFBOne(PageData pd)throws Exception{
		presaleplanoneMapper.deleteFBOne(pd);
	}
	
	/**依据售前方案ID查询一级明细柜体类型汇总信息
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getCabinByType(PageData pd)throws Exception{
		return presaleplanoneMapper.getCabinByType(pd);
	}
	
	/**依据售前方案ID查询一级明细柜体信息
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getCabin(PageData pd)throws Exception{
		return presaleplanoneMapper.getCabin(pd);
	}
}

