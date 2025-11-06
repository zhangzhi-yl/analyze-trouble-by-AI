package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.ElectricTypeMapper;
import org.yy.service.ny.ElectricTypeService;

import java.util.List;

/** 
 * 说明： 产量接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ElectricTypeServiceImpl implements ElectricTypeService {

    @Autowired
    private ElectricTypeMapper electrictypeMapper;

    /**新增
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd)throws Exception{
        electrictypeMapper.save(pd);
    }

    /**删除
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd)throws Exception{
        electrictypeMapper.delete(pd);
    }

    /**修改
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd)throws Exception{
        electrictypeMapper.edit(pd);
    }

    /**列表
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page)throws Exception{
        return electrictypeMapper.datalistPage(page);
    }

    /**列表(全部)
     * @param pd
     * @throws Exception
     */
    public List<PageData> listAll(PageData pd)throws Exception{
        return electrictypeMapper.listAll(pd);
    }

    /**通过id获取数据
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd)throws Exception{
        return electrictypeMapper.findById(pd);
    }

    /**批量删除
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
        electrictypeMapper.deleteAll(ArrayDATA_IDS);
    }
}

