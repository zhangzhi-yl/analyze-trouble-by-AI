package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

import com.github.pagehelper.PageInfo;

/** 
 * 说明： 物料转移申请单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferApplicationFormMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**编辑运转状态：待转，运行，关闭，下发操作
	 * @param pd
	 * @throws Exception
	 */
	void editRunningState(PageData pd);
	
	/**更新审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editAuditMark(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByFNum(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listTransfer(AppPage page);
	
}

