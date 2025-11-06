package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.MaterialTransferApplicationFormMapper;
import org.yy.service.mm.MaterialTransferApplicationFormService;

import com.github.pagehelper.PageInfo;

/** 
 * 说明： 物料转移申请单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MaterialTransferApplicationFormServiceImpl implements MaterialTransferApplicationFormService{

	@Autowired
	private MaterialTransferApplicationFormMapper MaterialTransferApplicationFormMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MaterialTransferApplicationFormMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MaterialTransferApplicationFormMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MaterialTransferApplicationFormMapper.edit(pd);
	}
	
	/**编辑运转状态：待转，运行，关闭，下发操作
	 * @param pd
	 * @throws Exception
	 */
	public void editRunningState(PageData pd)throws Exception{
		MaterialTransferApplicationFormMapper.editRunningState(pd);
	}
	
	/**更新审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editAuditMark(PageData pd)throws Exception{
		MaterialTransferApplicationFormMapper.editAuditMark(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MaterialTransferApplicationFormMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MaterialTransferApplicationFormMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MaterialTransferApplicationFormMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByFNum(PageData pd)throws Exception{
		return MaterialTransferApplicationFormMapper.findCountByFNum(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MaterialTransferApplicationFormMapper.deleteAll(ArrayDATA_IDS);
	}

	/**手机移库列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listTransfer(AppPage page) throws Exception {
		return MaterialTransferApplicationFormMapper.listTransfer(page);
	}
	
}

