package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.ProcessRouteMapper;
import org.yy.service.km.ProcessRouteService;

/** 
 * 说明： 工艺路线接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProcessRouteServiceImpl implements ProcessRouteService{

	@Autowired
	private ProcessRouteMapper ProcessRouteMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProcessRouteMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProcessRouteMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProcessRouteMapper.edit(pd);
	}
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editStatus(PageData pd)throws Exception{
		ProcessRouteMapper.editStatus(pd);
	}
	
	/**根据工艺路线id查询是否存在引用该工艺路线的生产bom
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountProductionBomByProcessRouteId(PageData pd)throws Exception {
		return ProcessRouteMapper.findCountProductionBomByProcessRouteId(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProcessRouteMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProcessRouteMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProcessRouteMapper.findById(pd);
	}
	
	/**查询编号数据数量，编号不能重复
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return ProcessRouteMapper.findCount(pd);
	}
	
	/**查询名称数据数量，名称不能重复
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByFName(PageData pd)throws Exception{
		return ProcessRouteMapper.findCountByFName(pd);
	}
	@Override
	public PageData findCountByCabinetType(PageData pd) throws Exception {
		return ProcessRouteMapper.findCountByCabinetType(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProcessRouteMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取工艺路线列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getRouteList(PageData pd) throws Exception {
		return ProcessRouteMapper.getRouteList(pd);
	}

	
}

