package org.yy.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.yy.entity.PageData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONUtil {

    /**
     * JSON转PageData
     * @param jsonStr json字符串
     * @return
     */
    public static PageData JSONToPageData(String jsonStr){
        PageData map = new PageData();
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<PageData> list = new ArrayList<PageData>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(JSONToPageData(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
}
