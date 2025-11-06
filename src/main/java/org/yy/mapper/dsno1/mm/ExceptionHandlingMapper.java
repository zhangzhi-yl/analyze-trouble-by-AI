package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 异常处理Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
public interface ExceptionHandlingMapper{

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
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	List<PageData> AppList(AppPage page);
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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	/**
	 * 修改异常处理记录的异常判别状态
	 */
	void EditDisposeType(PageData pd);
	/**
	 * 获取异常下异常处理记录中最后一条
	 * @param pd
	 * @return
	 */
	PageData getReorder(PageData pd);
	/**
	 * 异常处理完成,修改异常判别状态为已处理
	 */
	void FinishException(PageData pd);
}

