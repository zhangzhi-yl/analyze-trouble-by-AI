package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库位管理接口
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 * @version
 */
public interface WH_LocationService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	public List<PageData> locationList(PageData pd)throws Exception;
	
	/**手机端库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	public List<PageData> appList(PageData pd)throws Exception;
	
	/**根据code数组查询列表，库存查询用
	 * @param ArrayDATA_IDS
	 * @throws Exception	SELECT * FROM MWMS_WH_LOCATION WHERE FCODE IN ('Z010202-1','Z010201-1','Z010203-1');
	 */
	public List<PageData> locationList(Page page)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception;
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**根据库区ID查询库位总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;

	/**获取仓位列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getLocationList(PageData pd)throws Exception;
	
	/**扫码验证
	 * @param pd
	 * @throws Exception
	 */
	public PageData locationScanVerify(PageData pd)throws Exception;
}

