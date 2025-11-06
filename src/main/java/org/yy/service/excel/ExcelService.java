package org.yy.service.excel;



import org.yy.entity.PageData;

import java.util.List;

public interface ExcelService {

    PageData getExcelOptions(PageData pd) throws Exception;

    List<PageData> getExcelDataByOptionsID(PageData pd)  throws Exception;

    List<PageData> getExcelCellData(PageData pd) throws Exception;
}
