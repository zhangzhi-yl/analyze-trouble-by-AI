package org.yy.mapper.dsno1.momp;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface PhaseInstanceMapper {
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
	List<PageData> listAllGante(PageData pd);
	List<PageData> listGante(PageData pd);
	List<PageData> listGante2(PageData pd);
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	PageData findPhaseID(PageData pd);
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listGante3(PageData pd);
}
