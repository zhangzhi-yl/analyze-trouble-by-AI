package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

public interface ElectricTypeMapper {

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

}
