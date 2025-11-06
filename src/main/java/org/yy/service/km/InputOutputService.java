package org.yy.service.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 说明： 投入产出接口 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
public interface InputOutputService {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception;

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

	/**
	 * 列表(根据 工艺工序实例ID获取)
	 */
	public List<PageData> getInputOutputListByWorkingProcedureExample_ID(String WorkingProcedureExample_ID)throws Exception;
	/**
	 * 列表(根据 工BOM_ID获取)
	 */
	public List<PageData> getInputOutputListByBOM_ID(String BOM_ID)throws Exception;

	/**生成序号
	 * @param pd
	 * @return
	 */
	public PageData getSerialNum(PageData pd)throws Exception;
	
	/**
	 * 根据bomid 和 要生产的数量 获取 投入和产出的列表
	 * @param BOM_ID
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public List<PageData> calculateInputOutputListByBomIdAndCount(String BOM_ID,Double count)throws Exception;

	/**行关闭/反行关闭
	 * @param pd
	 */
	public void rowClose(PageData pd)throws Exception;

	/**获取工序产出物料数量
	 * @param pd
	 * @return
	 */
	public PageData getOutNum(PageData pd)throws Exception;

	/**关联行关闭投入产出明细
	 * @param pd
	 */
	public void deleteMxRelated(PageData pd)throws Exception;

	/**删除BOM关联行关闭
	 * @param pd
	 */
	public void rowCloseByBomId(PageData pd)throws Exception;

	public List<PageData> listAll1(PageData pd111);


}
