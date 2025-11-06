package org.yy.opc.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.*;
import org.openscada.opc.lib.da.ItemState;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class JiVariantUtil {
    private static final Log log = LogFactory.getLog(JiVariantUtil.class);

    /** java的数值、对象、字符类型的前缀 */
    public static final String BASE_TYPE_PRDFIX = "java.lang";
    /** 时间类型的前缀 */
    public static final String DATE_TYPE_PRDFIX = "java.util";

    /**
     * 解析服务器返回的数据，转换为客户端格式的数据
     * @param itemId 数据主键
     * @param itemState
     * @return
     * @throws Exception
     */
    public static DataItem parseValue(String itemId, ItemState itemState) throws Exception{
        Map<String, Object> value = getValue(itemState.getValue());
        return new DataItem(
                itemId,
                value.get("type").toString(),
                value.get("value"),
                itemState.getQuality(),
                itemState.getTimestamp().getTime(),
                DateUtil.getRecentMoment()
        );
    }

    /**
     * 提取JIVariant的值，转换为java.lang下的对象   <br>
     *
     * JIVariant有如下的返回格式：   <br>
     * JIArray objectAsArray = jiVariant.getObjectAsArray();   <br>
     * IJIUnsigned objectAsUnsigned = jiVariant.getObjectAsUnsigned();   <br>
     * IJIComObject objectAsComObject = jiVariant.getObjectAsComObject();   <br>
     * JIVariant objectAsVariant = jiVariant.getObjectAsVariant();   <br>
     * JIString objectAsString = jiVariant.getObjectAsString();   <br>
     * @param jiVariant
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getValue(JIVariant jiVariant) throws Exception{
        Object newValue ;
        Object oldValue = jiVariant.getObject();
        String typeName = oldValue.getClass().getTypeName();
        if(typeName.startsWith(BASE_TYPE_PRDFIX) || typeName.startsWith(DATE_TYPE_PRDFIX)){
            newValue = jiVariant.getObject();
        }else if(oldValue instanceof JIArray){
            newValue = jiVariant.getObjectAsArray();
        }else if(oldValue instanceof IJIUnsigned){
            newValue = jiVariant.getObjectAsUnsigned().getValue();
        }else if(oldValue instanceof IJIComObject){
            newValue = jiVariant.getObjectAsComObject();
        }else if(oldValue instanceof JIString){
            newValue = jiVariant.getObjectAsString().getString();
        }else if(oldValue instanceof JIVariant){
            newValue = jiVariant.getObjectAsVariant();
        }else{
            newValue = oldValue;

            log.error(String.format("无法解析服务器的数据类型'{}'！原始数据：{}", typeName, oldValue.toString()));
        }

        HashMap<String, Object> result = new HashMap<>(2);
        result.put("type", newValue.getClass().getSimpleName());
        result.put("value", newValue);
        return result;
    }



    public static Object parseVariant(JIVariant jiVariant) throws Exception{
        Object res=0;
        // 如果读到是short类型的值
        int type = jiVariant.getType();
        if (type==JIVariant.VT_EMPTY){
            res = 0;
        }
        if (type==JIVariant.VT_ERROR){
            res = 0;
        }
        //2-byte signed integer
        if (type == JIVariant.VT_I2) {
            short n = 0;
            try {
                n = jiVariant.getObjectAsShort();
            } catch (JIException e) {
                e.printStackTrace();
            }
            //System.out.println("-----short类型值： " + n);
             res = n;
        }
        //4-byte signed integer
        if (type==JIVariant.VT_I4){
            short n = 0;
            n = jiVariant.getObjectAsShort();
            res = n;
        }
        if (type== JIVariant.VT_I8){
            res = jiVariant.getObjectAsLong();
        }
        //1-byte signed integer
        if (type==JIVariant.VT_I1){
            res = jiVariant.getObjectAsInt();
        }
        //1-byte unsigned integer
        if (type==JIVariant.VT_UI1){
            res = jiVariant.getObjectAsUnsigned().getValue().intValue();
        }
        //2-byte unsigned integer
        if (type==JIVariant.VT_UI2){
            res = jiVariant.getObjectAsUnsigned().getValue().intValue();
        }
        if (type==JIVariant.VT_UI4){
            res = jiVariant.getObjectAsUnsigned().getValue().intValue();
        }
        if (type==JIVariant.VT_BOOL){
            if(jiVariant.getObjectAsBoolean()){
                res = 1;
            }else {
                res = 0;
            }

        }
        if (type==JIVariant.VT_R4){
            res = jiVariant.getObjectAsFloat();
        }
        if (type == JIVariant.VT_R8){
            BigDecimal bigDecimal = BigDecimal.valueOf(jiVariant.getObjectAsDouble()).setScale(4,BigDecimal.ROUND_HALF_UP);
            DecimalFormat decimalFormat = new DecimalFormat("#.000");
            res = decimalFormat.format(bigDecimal).toString();
        }
        //array
        if (type==8209){
            res = jiVariant.getObjectAsArray();
        }
        // 如果读到是字符串类型的值
        if(jiVariant.getType() == JIVariant.VT_BSTR) {  // 字符串的类型是8
            JIString value = null;
            try {
                value = jiVariant.getObjectAsString();
            } catch (JIException e) {
                e.printStackTrace();
            } // 按字符串读取
            String str = value.getString(); // 得到字符串
            System.out.println("-----String类型值： " + str);
            res = str;
        }
//        if(jiVariant.getType() == JIVariant.VT_UI2) {  // 字符的类型是18
//            IJIUnsigned value ;
//            try {
//                value = jiVariant.getObjectAsUnsigned();
//                System.out.println("-----unsigned Char类型值： " + value.getValue().shortValue());
//               res = value.getValue().shortValue();
//            } catch (JIException e) {
//                e.printStackTrace();
//            }
//        }
        return res;
    }
}
