package org.yy.mapper.dsno1.km;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 投入产出Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
public interface InputOutputMapper{

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

	/**生成序号
	 * @param pd
	 * @return
	 */
	PageData getSerialNum(PageData pd);
	
	/**列表(根据 工艺工序实例ID获取)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getInputOutputListByWorkingProcedureExample_ID(String WorkingProcedureExample_ID);
	/**
	 * 列表(根据 工BOM_ID获取)
	 */
	List<PageData> getInputOutputListByBOM_ID(String bOM_ID);

	/**行关闭/反行关闭
	 * @param pd
	 */
	void rowClose(PageData pd);

	/**获取工序产出物料数量 (主产出)
	 * @param pd
	 * @return
	 */
	PageData getOutNum(PageData pd);

	/**关联行关闭投入产出明细
	 * @param pd
	 */
	void deleteMxRelated(PageData pd);

	/**删除BOM关联行关闭
	 * @param pd
	 */
	void rowCloseByBomId(PageData pd);

	List<PageData> listAll1(PageData pd111);
}

