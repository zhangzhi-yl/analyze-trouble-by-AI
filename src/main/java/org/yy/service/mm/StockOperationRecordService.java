package org.yy.service.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库存操作记录接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-17
 * 官网：356703572@qq.com
 * @version
 */
public interface StockOperationRecordService{

	/**添加库存操作记录
	 * @param Stock_ID  库存id
	 * @param DataSources 数据来源
	 * @param ActualCount	数量
	 * @param WarehouseID		仓库
	 * @param PositionID		库位
	 * @param TType				类型
	 * @param ItemID			物料id
	 * @param FCreatePersonID			职员id
	 * @throws Exception
	 */
	public void add(String Stock_ID,String DataSources,String ActualCount,
			String WarehouseID,String PositionID,String TType,String ItemID,String FCreatePersonID)throws Exception;
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

