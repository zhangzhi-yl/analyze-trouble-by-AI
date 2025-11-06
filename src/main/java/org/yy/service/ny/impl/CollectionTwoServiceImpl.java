package org.yy.service.gzny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.CollectionTwoMapper;
import org.yy.service.ny.CollectionTwoService;

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
public class CollectionTwoServiceImpl implements CollectionTwoService {

    @Autowired
    CollectionTwoMapper collectionTwoMapper;

    @Override
    public void saveMultiRate(List<PageData> list) throws Exception {
        collectionTwoMapper.saveMultiRate(list);
    }

    @Override
    public void deleteAll() throws Exception {
        collectionTwoMapper.deleteAll();
    }

    @Override
    public List<PageData> getMultiRatedatalistPage(Page page) throws Exception {
        return collectionTwoMapper.getMultiRatedatalistPage(page);
    }

    @Override
    public List<PageData> plcList(PageData pd) throws Exception {
        return collectionTwoMapper.plcList(pd);
    }
    @Override
    public List<PageData> plcPageList(Page page) throws Exception {
        return collectionTwoMapper.plcdatalistPage(page);
    }

    @Override
    public List<PageData> monthMultiRate(PageData pd) throws Exception {
        return collectionTwoMapper.monthMultiRate(pd);
    }

    @Override
    public List<PageData> quarterMultiRate(PageData pd) throws Exception {
        return collectionTwoMapper.quarterMultiRate(pd);
    }

    @Override
    public List<PageData> yearMultiRate(PageData pd) throws Exception {
        return collectionTwoMapper.yearMultiRate(pd);
    }

    @Override
    public List<PageData> monitorList(Page page) throws Exception {
        return collectionTwoMapper.monitordatalistPage(page);
    }

    @Override
    public List<PageData> monitorAllList(PageData pd) throws Exception {
        return collectionTwoMapper.monitorAllList(pd);
    }

    @Override
    public List<PageData> lineLossList(PageData pd) throws Exception {
        return collectionTwoMapper.lineLossList(pd);
    }

    @Override
    public List<PageData> BigMultiRate(PageData pd) throws Exception {
        return collectionTwoMapper.BigMultiRate(pd);
    }

    @Override
    public List<PageData> useEnergyData(PageData pd) throws Exception {
        return collectionTwoMapper.useEnergyData(pd);
    }
}

