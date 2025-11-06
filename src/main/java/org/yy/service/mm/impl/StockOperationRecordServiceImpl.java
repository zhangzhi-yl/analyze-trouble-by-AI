package org.yy.service.mm.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.StockOperationRecordMapper;
import org.yy.service.mm.StockOperationRecordService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

/** 
 * 说明： 库存操作记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-17
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StockOperationRecordServiceImpl implements StockOperationRecordService{

	@Autowired
	private StockOperationRecordMapper StockOperationRecordMapper;
	
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
			String WarehouseID,String PositionID,String TType,String ItemID,String FCreatePersonID)throws Exception{
		PageData opd = new PageData();
		opd.put("StockOperationRecord_ID", UuidUtil.get32UUID());
        opd.put("Stock_ID", Stock_ID);
        opd.put("DataSources", DataSources);
        opd.put("ActualCount", ActualCount);
        opd.put("WarehouseID", WarehouseID);
        opd.put("PositionID", PositionID);
        opd.put("TType", TType);
        opd.put("ItemID", ItemID);
        opd.put("FCreateTime", Tools.date2Str(new Date()));
        opd.put("FCreatePersonID", FCreatePersonID);
		StockOperationRecordMapper.save(opd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		StockOperationRecordMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		StockOperationRecordMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		StockOperationRecordMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return StockOperationRecordMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return StockOperationRecordMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return StockOperationRecordMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		StockOperationRecordMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

