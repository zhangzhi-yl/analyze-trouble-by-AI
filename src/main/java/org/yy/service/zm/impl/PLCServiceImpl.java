package org.yy.service.zm.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.PLCMapper;
import org.yy.service.zm.PLCService;

/**
 * 说明： PLC参数配置接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 */
@Service
@Transactional //开启事物
public class PLCServiceImpl implements PLCService {

    @Autowired
    private PLCMapper plcMapper;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd) throws Exception {
        plcMapper.save(pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd) throws Exception {
        plcMapper.delete(pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd) throws Exception {
        plcMapper.edit(pd);
    }

    /**
     * 获取空闲字段
     *
     * @param pd
     * @throws Exception
     */
    @Override
    public List<PageData> getField(PageData pd) throws Exception {
        return plcMapper.getField(pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page) throws Exception {
        return plcMapper.datalistPage(page);
    }

    /**
     * 启用列表(全部)
     *
     * @param
     * @throws Exception
     */
    @Override
    public List<PageData> useList() throws Exception {
        return plcMapper.useList();
    }

    /**
     * 启用列表(全部)回路筛选
     *
     * @param
     * @throws Exception
     */
    @Override
    public List<PageData> useListByLoop(PageData pd) throws Exception {
        return plcMapper.useListByLoop(pd);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    public List<PageData> listAll(PageData pd) throws Exception {
        return plcMapper.listAll(pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception {
        return plcMapper.findById(pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        plcMapper.deleteAll(ArrayDATA_IDS);
    }

    /**
     * 存储开关记录
     *
     * @param pd
     */
    @Override
    public void saveRecord(List<PageData> list) throws Exception {
        plcMapper.saveRecord(list);
    }

    /**
     * 获取开关时长
     *
     * @param pd
     * @return
     */
    @Override
    public List<PageData> getDuration(PageData pd) throws Exception {
        return plcMapper.getDuration(pd);
    }

    /**
     * 清空存储字段数据
     * @param pd
     */
    @Override
    public void deletePlcData(PageData pd) throws Exception {
        plcMapper.deletePlcData(pd);
    }

}

