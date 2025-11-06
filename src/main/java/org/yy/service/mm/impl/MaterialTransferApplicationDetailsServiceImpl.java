package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.MaterialTransferApplicationDetailsMapper;
import org.yy.service.mm.MaterialTransferApplicationDetailsService;

/** 
 * 说明： 物料转移申请明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MaterialTransferApplicationDetailsServiceImpl implements MaterialTransferApplicationDetailsService{

	@Autowired
	private MaterialTransferApplicationDetailsMapper MaterialTransferApplicationDetailsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.delete(pd);
	}
	
	/**根据转移申请单id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByMTA_ID(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.deleteByMTA_ID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.edit(pd);
	}
	
	/**修改行关闭状态
	 * @param pd
	 * @throws Exception
	 */
	public void editLineClose(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.editLineClose(pd);
	}
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editFStatus(PageData pd)throws Exception{
		MaterialTransferApplicationDetailsMapper.editFStatus(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MaterialTransferApplicationDetailsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MaterialTransferApplicationDetailsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MaterialTransferApplicationDetailsMapper.findById(pd);
	}
	
	/**通过父级id获取明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return MaterialTransferApplicationDetailsMapper.findCount(pd);
	}
	
	/**合并转入列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> mergeTransferInlist(PageData pd)throws Exception{
		return MaterialTransferApplicationDetailsMapper.mergeTransferInlist(pd);
	}
	
	/**转入审核列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> auditInList(PageData pd)throws Exception{
		return MaterialTransferApplicationDetailsMapper.auditInList(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MaterialTransferApplicationDetailsMapper.deleteAll(ArrayDATA_IDS);
	}

	/**手机转入转出明细
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> stockTransferListMx(AppPage page) throws Exception {
		return MaterialTransferApplicationDetailsMapper.stockTransferListMx(page);	
	}

	
	@Override
	public PageData findByRowNumAndStockList_ID(PageData prepd) throws Exception {
		return MaterialTransferApplicationDetailsMapper.findByRowNumAndStockList_ID(prepd);	
	}

	/**手机扫码插入记录
	 * @param pd
	 */
	@Override
	public void saveJL(PageData pd) throws Exception {
		MaterialTransferApplicationDetailsMapper.saveJL(pd);
	}

	@Override
	public List<PageData> GET_WLZY_ZHUISU_listPage(Page page) {
		return MaterialTransferApplicationDetailsMapper.GET_WLZY_ZHUISU_listPage(page);
	}
	
}

