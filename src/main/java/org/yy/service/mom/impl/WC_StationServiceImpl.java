package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WC_StationMapper;
import org.yy.service.mom.WC_StationService;

/** 
 * 说明： 工作站管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WC_StationServiceImpl implements WC_StationService{

	@Autowired
	private WC_StationMapper wc_stationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wc_stationMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wc_stationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wc_stationMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wc_stationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wc_stationMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wc_stationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wc_stationMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据工作中心ID查询工作站总数
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	@Override
	public PageData findCount(PageData pd) throws Exception {
		return wc_stationMapper.findCount(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return wc_stationMapper.findCountByCode(pd);
	}
	
	/**删除文件
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delFile(PageData pd) throws Exception {
		wc_stationMapper.delFile(pd);
	}
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delTp(PageData pd) throws Exception {
		wc_stationMapper.delTp(pd);
	}

	/**修改作业指导书名称路径字段数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editFile(PageData pd) throws Exception {
		wc_stationMapper.editFile(pd);
	}
	
	/**获取附件路径
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPath(PageData pd)throws Exception{
		return wc_stationMapper.findByPath(pd);
	}
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return wc_stationMapper.findByFCODE(page);
	}
	
	/**根据车间和工位名称获取工位ID
	 * @param pd
	 * @throws Exception
	 */
	public PageData getStationId(PageData pd)throws Exception{
		return wc_stationMapper.getStationId(pd);
	}
}

