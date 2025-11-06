package org.yy.service.excel;

import cn.hutool.json.JSONObject;

/**
 * @author Mars
 * @date 2020/10/28
 * @description
 */
public interface IMessageNewProcess {


    /**
     * 对updateurl发来的信息进行处理
     * @param message
     * @return
     */
    void process(String gridKey,JSONObject message);
}
