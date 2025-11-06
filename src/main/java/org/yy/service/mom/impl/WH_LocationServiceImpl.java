package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WH_LocationMapper;
import org.yy.service.mom.WH_LocationService;

/** 
 * 说明： 库位管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-08
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WH_LocationServiceImpl implements WH_LocationService{

	@Autowired
	private WH_LocationMapper wh_locationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wh_locationMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wh_locationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wh_locationMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wh_locationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wh_locationMapper.listAll(pd);
	}
	
	/**库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	public List<PageData> locationList(PageData pd)throws Exception{
		return wh_locationMapper.locationList(pd);
	}
	
	/**手机端库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	public List<PageData> appList(PageData pd)throws Exception{
		return wh_locationMapper.appList(pd);
	}
	
	/**根据code数组查询列表，库存查询用
	 * @param ArrayDATA_IDS
	 * @throws Exception	SELECT * FROM MWMS_WH_LOCATION WHERE FCODE IN ('Z010202-1','Z010201-1','Z010203-1');
	 */
	@Override
	public List<PageData> locationList(Page page) throws Exception {
		return wh_locationMapper.locationlistPage(page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wh_locationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wh_locationMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据库区ID查询库位总数
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findCount(PageData pd) throws Exception {
		return wh_locationMapper.findCount(pd);
	}

	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findByCode(PageData pd) throws Exception {
		return wh_locationMapper.findByCode(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return wh_locationMapper.findCountByCode(pd);
	}

	/**获取仓位列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getLocationList(PageData pd) throws Exception {
		return wh_locationMapper.getLocationList(pd);
	}
	
	/**扫码验证
	 * @param pd
	 * @throws Exception
	 */
	public PageData locationScanVerify(PageData pd)throws Exception{
		return wh_locationMapper.locationScanVerify(pd);
	}
}

