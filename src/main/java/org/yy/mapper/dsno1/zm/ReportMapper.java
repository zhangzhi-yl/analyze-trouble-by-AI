package org.yy.mapper.dsno1.zm;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明：报表
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
public interface ReportMapper {
	
	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> dayElectricReport(PageData pd);

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> monthElectricReport(PageData pd);

	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> dayElectricUseReport(PageData pd);

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> monthElectricUseReport(PageData pd);
	
}

