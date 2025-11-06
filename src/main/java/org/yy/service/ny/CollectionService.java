package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface CollectionService {

    /**
     * 保存录入数据
     * @param pd
     */
    public void saveInput(PageData pd)throws Exception;

    /**
     * 编辑录入数据
     * @param pd
     */
    public void editInput(PageData pd)throws Exception;

    /**
     * 根据时间戳查询数据
     * @param pd
     * @return
     */
    List<PageData> findByTime(PageData pd)throws Exception;

    /**
     * 数据采集列表
     * @param pd
     * @return
     */
    public List<PageData> plcList(PageData pd)throws Exception;

    /**
     * 获取最大参数值
     * @param
     * @return
     */
    public List<PageData> getDayList(PageData pd)throws Exception;
    public List<PageData> getWeekList(PageData pd)throws Exception;
    public List<PageData> getMonthList(PageData pd)throws Exception;
    public List<PageData> getYearList(PageData pd)throws Exception;
    public List<PageData> getQuarterList(PageData pd)throws Exception;

    /**
     * 保存查询结果至临时表
     * @param list
     */
    public void saveAll(List<PageData> list)throws Exception;
    public void saveAllOne(List<PageData> list)throws Exception;
    public void saveAllTwo(List<PageData> list)throws Exception;
    public void saveAllThree(List<PageData> list)throws Exception;
    public void saveAllFour(List<PageData> list)throws Exception;
    public void saveAllFive(List<PageData> list)throws Exception;
    public void saveAllSix(List<PageData> list)throws Exception;

    /**
     * 清空临时表
     */
    public void deleteAll()throws Exception;
    public void deleteTableAll(PageData pd)throws Exception;

    /**
     * 获取临时表分页数据
     * @param page
     * @return
     */
    public List<PageData> getResult(Page page)throws Exception;

    public List<PageData>  getLoopdatalistPage(Page page)throws Exception;

    /**
     * 获取临时表分页数据  耗能排行耗能排序
     * @param page
     * @return
     */
    public List<PageData> rankList(PageData pd)throws Exception;

    /**
     * 获取临时表全部数据
     * @param pd
     * @return
     */
    public List<PageData> getResultAllList(PageData pd)throws Exception;

    /**
     * 获取临时表全部数据
     * @param
     * @return
     */
    public List<PageData> getResultList()throws Exception;

    /**
     * 获取单件能耗趋势列表
     * @param
     * @return
     */
    public List<PageData> getOnlyConsume()throws Exception;

    /**
     * 获取临时表统计数据（全部月份）
     * @param
     * @return
     */
    public List<PageData> getResultAllMonthList()throws Exception;
    public List<PageData> getResultTableAllMonthList(PageData pd)throws Exception;
    public List<PageData> getResultTableList(PageData pd)throws Exception;

    /**
     * 获取临时表回路分组列表
     * @param
     * @return
     */
    public List<PageData> getResultGroupByName()throws Exception;

    /**
     * 选中回路总功耗
     * @return
     */
    public List<PageData> getAnalysisList()throws Exception;

    /**
     * 回路分类总功耗
     * @return
     */
    public List<PageData> getAnalysisAllList()throws Exception;

    /**
     * 获取天排行数据
     * @return
     */
    public List<PageData> getDayRank(PageData pd)throws Exception;

    /**
     * 获取月排行数据
     * @return
     */
    public List<PageData> getMonthRank(PageData pd)throws Exception;

    /**
     * 获取季排行数据
     * @return
     */
    public List<PageData> getQuarterRank(PageData pd)throws Exception;

    /**
     * 获取年排行数据
     * @return
     */
    public List<PageData> getYearRank(PageData pd)throws Exception;

    /**
     * 同比列表
     * @return
     */
    public List<PageData> grewList(PageData pd)throws Exception;

    /**
     * 保存查询结果至临时表 同比环比
     * @param list
     */
    public void saveGrewAll(List<PageData> list)throws Exception;

    /**
     * 清空临时表
     */
    public void deleteAllGrew()throws Exception;

    /**
     * 获取临时表分页数据
     * @param page
     * @return
     */
    public List<PageData> getGrewdatalistPage(Page page)throws Exception;

    /**
     * 获取临时表全部数据
     * @param pd
     * @return
     */
    public List<PageData> getGrewList(PageData pd)throws Exception;

}

