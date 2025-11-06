package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

public interface CollectionTwoService {

    public void saveMultiRate(List<PageData> list)throws Exception;
    public void deleteAll()throws Exception;
    public List<PageData> getMultiRatedatalistPage(Page page)throws Exception;

    /**
     * 参数列表
     * @param pd
     * @return
     */
    public List<PageData> plcList(PageData pd)throws Exception;

    /**
     * 参数列表分页
     * @param page
     * @return
     */
    public List<PageData> plcPageList(Page page)throws Exception;

    /**
     * 月复费率
     * @param pd
     * @return
     */
    public List<PageData> monthMultiRate(PageData pd)throws Exception;

    /**
     * 季度复费率
     * @param pd
     * @return
     */
    public List<PageData> quarterMultiRate(PageData pd)throws Exception;

    /**
     * 年复费率
     * @param pd
     * @return
     */
    public List<PageData> yearMultiRate(PageData pd)throws Exception;

    /**
     * 实时监控
     * @param pd
     * @return
     */
    public List<PageData> monitorList(Page pd)throws Exception;

    /**
     * 实时监控
     * @param pd
     * @return
     */
    public List<PageData> monitorAllList(PageData pd)throws Exception;

    /**
     * 线路损耗==plc参数值获取
     * @param pd
     * @return
     */
    public List<PageData> lineLossList(PageData pd)throws Exception;

    /**
     * 电尖峰谷首页图表
     * @param pd
     * @return
     */
    public List<PageData> BigMultiRate(PageData pd)throws Exception;

    /**
     * 首页耗能数据
     * @param pd
     * @return
     */
    public List<PageData> useEnergyData(PageData pd)throws Exception;

}
