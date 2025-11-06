package org.yy.service.mom.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.OperationRecordMapper;
import org.yy.service.mom.OperationRecordService;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

/** 
 * 说明： 功能操作记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-04
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class OperationRecordServiceImpl implements OperationRecordService{

	@Autowired
	private OperationRecordMapper operationrecordMapper;
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**添加操作日志
	 * @param FunctionType  功能类型
	 * @param FunctionItem  功能项
	 * @param OperationType 操作类型
	 * @param DeleteTagID	删除标记ID
	 * @param FOperatorID	操作人ID
	 * @param FDescribe		操作描述
	 * @throws Exception
	 */
	public void add(String FunctionType,String FunctionItem,String OperationType,
			String DeleteTagID,String FOperatorID,String FDescribe)throws Exception{
		PageData opd = new PageData();
		opd.put("OperationRecord_ID", this.get32UUID());
        opd.put("FunctionType", FunctionType);
        opd.put("FunctionItem", FunctionItem);
        opd.put("OperationType", OperationType);
        opd.put("DeleteTagID", DeleteTagID);
        opd.put("FDescribe", FDescribe);
        opd.put("FOperatorID", FOperatorID);
        opd.put("FOperateTime", Tools.date2Str(new Date()));
        operationrecordMapper.save(opd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		operationrecordMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		operationrecordMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		operationrecordMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return operationrecordMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return operationrecordMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return operationrecordMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		operationrecordMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

