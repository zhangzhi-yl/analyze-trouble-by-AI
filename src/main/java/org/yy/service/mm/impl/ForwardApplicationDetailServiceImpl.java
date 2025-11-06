package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.ForwardApplicationDetailMapper;
import org.yy.service.mm.ForwardApplicationDetailService;

/** 
 * 说明： 发运申请明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ForwardApplicationDetailServiceImpl implements ForwardApplicationDetailService{

	@Autowired
	private ForwardApplicationDetailMapper ForwardApplicationDetailMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ForwardApplicationDetailMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ForwardApplicationDetailMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ForwardApplicationDetailMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ForwardApplicationDetailMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ForwardApplicationDetailMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ForwardApplicationDetailMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ForwardApplicationDetailMapper.deleteAll(ArrayDATA_IDS);
	}

	/**关联行关闭发运申请明细
	 * @param pd
	 */
	@Override
	public void rowCloseByForwardId(PageData pd) throws Exception {
		ForwardApplicationDetailMapper.rowCloseByForwardId(pd);
	}

	/**生成行号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRowNum(PageData pd) throws Exception {
		return ForwardApplicationDetailMapper.getRowNum(pd);
	}

	/**行关闭/反行关闭
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		ForwardApplicationDetailMapper.rowClose(pd);
	}
	
}

