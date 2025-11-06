package org.yy.mapper.dsno1.km;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 异常Mapper 作者：YuanYes QQ356703572 时间：2020-11-09 官网：356703572@qq.com
 * 
 * @version
 */
public interface ExceptionMapper {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	List<PageData> AppList(AppPage page);

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 下发异常
	 */
	void goLssue(PageData pd);

	/**
	 * 异常处理记录列表(带分页)
	 * 
	 * @param page
	 * @return
	 */
	List<PageData> datalistPageHandling(Page page);

	/**
	 * 异常处理记录（不带分页，用于导出）
	 */
	List<PageData> listAllHandling(PageData pd);

	/**
	 * 异常处理记录查看
	 */
	PageData getHandling(PageData pd);

	void FinishException(PageData pd);

	/**
	 * 设备列表
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> getEQMList(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listSClistPage(Page page);

	/**
	 * @param pd
	 */
	void setRead(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listHislistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listXMlistPage(Page page);
}
