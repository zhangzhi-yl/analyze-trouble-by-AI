package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.REPAIR_WORKORDERMapper;
import org.yy.service.mdm.REPAIR_WORKORDERService;

/** 
 * 说明： 报修工单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class REPAIR_WORKORDERServiceImpl implements REPAIR_WORKORDERService{

	@Autowired
	private REPAIR_WORKORDERMapper repair_workorderMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		repair_workorderMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		repair_workorderMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		repair_workorderMapper.edit(pd);
	}
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateState(PageData pd)throws Exception{
		repair_workorderMapper.updateState(pd);
	}
	
	/**根据流程实例ID修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateStateByPROC_INST_ID(PageData pd)throws Exception{
		repair_workorderMapper.updateStateByPROC_INST_ID(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return repair_workorderMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return repair_workorderMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return repair_workorderMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		repair_workorderMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 查询最大单号
	 * @return pd
	 */
	public PageData findMaxBillNo() throws Exception{
		return repair_workorderMapper.findMaxBillNo();
	}
	/**
	 * 根据设备标识查询top1 
	 * @param pd
	 * @return pd
	 */
	public PageData findByIdentify(PageData pd) throws Exception{
		return repair_workorderMapper.findByIdentify(pd);
	}

	/**
	 * 修改Fopinion
	 */
	@Override
	public void updateFopinion(PageData pd) throws Exception {
		repair_workorderMapper.updateFopinion(pd);
	}
	
	/**
	 * 修改Fopinion1
	 */
	@Override
	public void updateFopinion1(PageData pd) throws Exception {
		repair_workorderMapper.updateFopinion1(pd);
	}

	/**
	 * 手机端列表
	 */
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return repair_workorderMapper.AppList(page);
	}
}

