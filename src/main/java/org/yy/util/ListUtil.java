package org.yy.util;

import org.yy.entity.PageData;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表拆分工具类
 * 20210820
 */
public class ListUtil {

    /**
     * 将列表拆分为一半
     * @param list 要拆分列表
     * @return list
     */
    public static List<List<PageData>> halfList(List<PageData> list) {
        List<List<PageData>> res = new ArrayList<>();

        int count = list.size() / 2; // 每段数量
        List<PageData> cutList = new ArrayList<>();// 每段List

        for (int j = 0; j < 2; j++) {
            if (j == 2 - 1) {
                cutList = list.subList(count * j, list.size());
            } else {
                cutList = list.subList(0, count * (j + 1));
            }

            res.add(cutList);
        }

        return res;
    }

    /**
     * 将列表按指定条数拆分
     * @param list 列表
     * @param size 每个列表数据条数
     * @return list
     */
    public static List<List<PageData>> cutList(List<PageData> list, int size) {

        List<List<PageData>> res = new ArrayList<>();

        if (list.size() <= size) {
            //直接存入
            res.add(list);
        } else {
            // 可遍历的拆分次数
            int insertSqlCount = 0;
            // 总数据条数
            int totalDataCount = list.size();
            if (totalDataCount % size == 0) {
                insertSqlCount = totalDataCount / size;
            } else {
                insertSqlCount = totalDataCount / size + 1;
            }
            for (int i = 0; i < insertSqlCount; i++) {
                int startNumber = i * size;
                int endUnmber = (i + 1) * size;
                if (endUnmber > totalDataCount) {
                    endUnmber = totalDataCount;
                }
                List<PageData> subListOK = list.subList(startNumber, endUnmber);
                //返回
                res.add(subListOK);
            }
        }
        return res;
    }

}
