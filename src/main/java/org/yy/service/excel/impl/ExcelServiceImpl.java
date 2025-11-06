package org.yy.service.excel.impl;

import org.yy.entity.PageData;
import org.yy.mapper.dsno1.excel.ExcelMapper;
import org.yy.service.excel.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional //开启事物
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelMapper excelMapper;

    @Override
    public PageData getExcelOptions(PageData pd)  throws Exception{
        return excelMapper.getExcelOptions(pd);
    }

    @Override
    public List<PageData> getExcelDataByOptionsID(PageData pd)  throws Exception{
        return excelMapper.getExcelDataByOptionsID(pd);
    }

    @Override
    public List<PageData> getExcelCellData(PageData pd)  throws Exception{
        return excelMapper.getExcelCellData(pd);
    }
}
