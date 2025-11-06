/**
 * 
 */
package org.yy.service.pp;

import java.util.List;

import org.yy.entity.PageData;

/**
 * 
 * 说明： 排程接口
 * 
 * @author chen
 * @since 2020-11-16
 */
public interface APSService {

	public List<PageData> listByPlanningWorkOrderID(PageData pd);

	public PageData findProcessWorkOrderExampleById(PageData pdData);

	public void updateProcessWorkOrderExample(PageData pd);
	
	public List<PageData> getWorkorderProcessIOExampleListByProcessWorkOrderExampleID(String ProcessWorkOrderExampleID);
}
