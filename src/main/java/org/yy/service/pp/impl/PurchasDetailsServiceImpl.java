package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchasDetailsMapper;
import org.yy.service.pp.PurchasDetailsService;

/** 
 * 说明： 采购申请明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchasDetailsServiceImpl implements PurchasDetailsService{

	@Autowired
	private PurchasDetailsMapper PurchasDetailsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchasDetailsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchasDetailsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchasDetailsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchasDetailsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchasDetailsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchasDetailsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchasDetailsMapper.deleteAll(ArrayDATA_IDS);
	}

	/**关联行关闭采购申请明细
	 * @param pd
	 */
	@Override
	public void rowCloseByApplyId(PageData pd) throws Exception {
		PurchasDetailsMapper.rowCloseByApplyId(pd);
	}

	/**行关闭/反行关闭
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		PurchasDetailsMapper.rowClose(pd);
	}

	/**生成行号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRowNum(PageData pd) throws Exception {
		return PurchasDetailsMapper.getRowNum(pd);
	}

	/**采购申请明细-物料列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllMat(PageData pd) throws Exception {
		return PurchasDetailsMapper.listAllMat(pd);
	}

	/**批量选择采购申请物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllCGSQ(String[] arrayDATA_IDS) throws Exception {
		return PurchasDetailsMapper.selectAllCGSQ(arrayDATA_IDS);
	}

	/**反写采购申请明细下推数量
	 * @param pdSave
	 */
	@Override
	public void calFPushCount(PageData pdSave) throws Exception {
		PurchasDetailsMapper.calFPushCount(pdSave);
	}
	
	/**修改物料清单的下推状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateFIsPush(PageData pd)throws Exception{
		PurchasDetailsMapper.updateFIsPush(pd);
	}
}

