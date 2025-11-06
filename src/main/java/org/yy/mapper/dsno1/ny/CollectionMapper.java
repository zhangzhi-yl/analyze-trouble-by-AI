package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface CollectionMapper {

    /**
     * 保存录入数据
     * @param pd
     */
    void saveInput(PageData pd);

    /**
     * 编辑录入数据
     * @param pd
     */
    void editInput(PageData pd);

    /**
     * 根据时间戳查询数据
     * @param pd
     * @return
     */
    List<PageData> findByTime(PageData pd);

    /**
     * 数据采集列表
     * @param page
     * @return
     */
    List<PageData> plcList(PageData pd);

    /**
     * 获取参数值
     * @param
     * @return
     */
    List<PageData> getDayList(PageData pd);
    List<PageData> getWeekList(PageData pd);
    List<PageData> getMonthList(PageData pd);
    List<PageData> getYearList(PageData pd);
    List<PageData> getQuarterList(PageData pd);

    /**
     * 保存查询结果至临时表
     * @param list
     */
    void saveAll(List<PageData> list);
    void saveAllOne(List<PageData> list);
    void saveAllTwo(List<PageData> list);
    void saveAllThree(List<PageData> list);
    void saveAllFour(List<PageData> list);
    void saveAllFive(List<PageData> list);
    void saveAllSix(List<PageData> list);

    /**
     * 清空临时表
     */
    void deleteAll();
    void deleteTableAll(PageData pd);

    /**
     * 获取临时表分页数据
     * @param page
     * @return
     */
    List<PageData> getResultdatalistPage(Page page);

    List<PageData>  getLoopdatalistPage(Page page);

    /**
     * 获取临时表分页数据  耗能排行耗能排序
     * @param page
     * @return
     */
    List<PageData> ranklist(PageData pd);

    /**
     * 获取临时表全部数据
     * @param pd
     * @return
     */
    List<PageData> getResultAllList(PageData pd);

    /**
     * 获取临时表统计数据
     * @param
     * @return
     */
    List<PageData> getResultList();

    /**
     * 获取单件能耗趋势列表
     * @param
     * @return
     */
    List<PageData> getOnlyConsume();

    /**
     * 获取临时表统计数据（全部月份）
     * @param
     * @return
     */
    List<PageData> getResultAllMonthList();
    List<PageData> getResultTableAllMonthList(PageData pd);
    List<PageData> getResultTableList(PageData pd);


    /**
     * 获取临时表回路分组列表
     * @param
     * @return
     */
    List<PageData> getResultGroupByName();

    /**
     * 选中回路总功耗
     * @return
     */
    List<PageData> getAnalysisList();

    /**
     * 回路分类总功耗
     * @return
     */
    List<PageData> getAnalysisAllList();

    /**
     * 获取天排行数据
     * @return
     */
    List<PageData> getDayRank(PageData pd);

    /**
     * 获取月排行数据
     * @return
     */
    List<PageData> getMonthRank(PageData pd);

    /**
     * 获取季排行数据
     * @return
     */
    List<PageData> getQuarterRank(PageData pd);

    /**
     * 获取年排行数据
     * @return
     */
    List<PageData> getYearRank(PageData pd);

    /**
     * 同比列表
     * @return
     */
    List<PageData> grewList(PageData pd);

    /**
     * 保存查询结果至临时表 同比环比
     * @param list
     */
    void saveGrewAll(List<PageData> list);

    /**
     * 清空临时表
     */
    void deleteAllGrew();

    /**
     * 获取临时表分页数据
     * @param page
     * @return
     */
    List<PageData> getGrewdatalistPage(Page page);

    /**
     * 获取临时表全部数据
     * @param pd
     * @return
     */
    List<PageData> getGrewList(PageData pd);

}

