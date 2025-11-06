package org.yy.opc.utils;

import java.util.Date;

public class DataItem {
    /** 数据主键 */
    private String itemId;
    /** 数据类型 */
    private String dataType;
    /** 数据值 */
    private Object value;
    /** 数据质量 */
    private Short quality;
    /** 数据时间 */
    private Date dataTime;
    /** 最近时刻 */
    private Date currMonment;

    public DataItem(String itemId, String type, Object value, Short quality, Date time, Date recentMoment) {
        this.itemId = itemId;
        this.dataType = type;
        this.value = value;
        this.quality = quality;
        this.dataTime = time;
        this.currMonment = recentMoment;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Short getQuality() {
        return quality;
    }

    public void setQuality(Short quality) {
        this.quality = quality;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Date getCurrMonment() {
        return currMonment;
    }

    public void setCurrMonment(Date currMonment) {
        this.currMonment = currMonment;
    }
}
