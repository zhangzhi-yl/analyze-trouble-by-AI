package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_BARCODEMapper;
import org.yy.service.mbase.MAT_BARCODEService;

/** 
 * 说明： 条码资料接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-07
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_BARCODEServiceImpl implements MAT_BARCODEService{

	@Autowired
	private MAT_BARCODEMapper mat_barcodeMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_barcodeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_barcodeMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_barcodeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_barcodeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_barcodeMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_barcodeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_barcodeMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过基础资料主键删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBasic(PageData pd)throws Exception{
		mat_barcodeMapper.deleteBasic(pd);
	}
	
	/**查询是否有重复条形码
	 * @param pd
	 * @throws Exception
	 */
	public PageData countCode(PageData pd)throws Exception{
		return mat_barcodeMapper.countCode(pd);
	}
}

