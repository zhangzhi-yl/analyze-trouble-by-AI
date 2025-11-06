package org.yy.controller.uniapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.zm.AppVersionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/uniAppVersion")
public class UniAppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    /**更新
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/update")
    @ResponseBody
    public Object goEdit(@RequestBody PageData pd) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        pd.put("APPVERSION_ID","1");

        //客户端版本
        String oldVersion = pd.getString("version");
        String[] olds = oldVersion.split(".");

        //数据库版本
        PageData version = appVersionService.findById(pd);
        String versionNum = version.getString("VERSION");
        String[] news = versionNum.split(".");

        for (int i = 0 ; i < olds.length ; i++){
            if(Integer.parseInt(news[i]) > Integer.parseInt(olds[i])){

            }
        }

//        if(newVersion > old){
//            map.put("url",version.getString("PATH"));
//        }else if(newVersion == old){
//            map.put("wgtUrl",version.getString("WGT_PATH"));
//        }
        map.put("result", errInfo);
        return map;
    }
}
