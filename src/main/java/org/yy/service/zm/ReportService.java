package org.yy.service.zm;

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
public interface ReportService {

	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> dayElectricReport(PageData pd)throws Exception;

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> monthElectricReport(PageData pd) throws Exception;

	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> dayElectricUseReport(PageData pd) throws Exception;

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> monthElectricUseReport(PageData pd) throws Exception;

}

