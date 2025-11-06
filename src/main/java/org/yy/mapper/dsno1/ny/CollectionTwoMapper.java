package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

public interface CollectionTwoMapper {

    //复费率临时表操作
    void saveMultiRate(List<PageData> list);
    void deleteAll();
    List<PageData> getMultiRatedatalistPage(Page page);

    /**
     * 参数列表
     * @param pd
     * @return
     */
    List<PageData> plcList(PageData pd);

    /**
     * 参数列表分页
     * @param page
     * @return
     */
    List<PageData> plcdatalistPage(Page page);

    /**
     * 月复费率
     * @param pd
     * @return
     */
    List<PageData> monthMultiRate(PageData pd);

    /**
     * 季度复费率
     * @param pd
     * @return
     */
    List<PageData> quarterMultiRate(PageData pd);

    /**
     * 年复费率
     * @param pd
     * @return
     */
    List<PageData> yearMultiRate(PageData pd);

    /**
     * 实时监控
     * @param page
     * @return
     */
    List<PageData> monitordatalistPage(Page page);

    /**
     * 实时监控
     * @param pd
     * @return
     */
    List<PageData> monitorAllList(PageData pd);

    /**
     * 线路损耗==plc参数值获取
     * @param pd
     * @return
     */
    List<PageData> lineLossList(PageData pd);

    /**
     * 电尖峰谷首页图表
     * @param pd
     * @return
     */
    List<PageData> BigMultiRate(PageData pd);

    /**
     * 首页耗能数据
     * @param pd
     * @return
     */
    List<PageData> useEnergyData(PageData pd);

}
