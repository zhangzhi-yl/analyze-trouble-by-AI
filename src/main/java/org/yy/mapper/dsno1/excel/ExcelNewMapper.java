package org.yy.mapper.dsno1.excel;


import org.yy.entity.PageData;

import java.util.List;

public interface ExcelNewMapper {

    PageData getExcelOptions(PageData pd);

    List<PageData> getExcelDataByOptionsID(PageData pd);

    List<PageData> getExcelCellData(PageData pd);

    List<PageData> getIfExcelCellData(PageData pd);

    void delCellDataOne(PageData pd);

    PageData getDataByIndex(PageData pd);

    void saveCellDataOne(PageData pd);

    void updataCellDataOne(PageData pd);
    void updataColumnlen(PageData pd);
    void updataRowlen(PageData pd);
    void updataMerge(PageData pd);
    void updataBorderInfo(PageData pd);

    void delAllCellData(List<PageData> list);

    void saveAllCellData(List<PageData> list);

    void editAllCellData(List<PageData> list);

    void saveSheet(PageData pd);

    void editSheetName(PageData pd);
}
