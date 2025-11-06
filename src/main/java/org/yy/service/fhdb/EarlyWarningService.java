package org.yy.service.fhdb;

import java.util.List;

import org.springframework.stereotype.Service;
import org.yy.entity.PageData;

/**
 * 说明：预警接口
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public interface EarlyWarningService {
	/**
	 * v1 陈春光 20210531 获取已经超期柜体预警数据 
	 * @param pd
	 * @return
	 */
	List<PageData> getOverduedCabinetList(PageData pd); 
	
	
	/**
	 * v1 陈春光 20210531 获取即将超期柜体预警数据 
	 * @param pd
	 * @return
	 */
	List<PageData> getWillOverdueCabinetList(PageData pd); 
	
	
	/**
	 * v1 陈春光 20210531 获取已经超期采购预警数据 
	 * @param pd
	 * @return
	 */
	List<PageData> getOverduePurchaseList(PageData pd); 
	
	
	/**
	 * v1 陈春光 20210531 获取即将超期采购预警数据 
	 * @param pd
	 * @return
	 */
	List<PageData> getWillOverduePurchaseList(PageData pd); 
	
	
	/**
	 * v1 陈春光 20210531 获取已经下推车间装配的，但是没有BOM或者柜体的名单预警数据 
	 * @param pd
	 * @return
	 */
	List<PageData> getLoadedButNoBOMOrDrawingList(PageData pd); 
}
