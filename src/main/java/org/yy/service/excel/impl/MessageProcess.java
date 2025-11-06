package org.yy.service.excel.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.WorkSheetEntity;
import org.yy.mapper.dsno1.excel.ExcelMapper;
import org.yy.service.excel.IMessageProcess;
import org.yy.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mars
 * @date 2020/10/28
 * @description
 */
@Service
public class MessageProcess implements IMessageProcess {

    @Autowired
    ExcelMapper excelMapper;


    @Override
    public void process(String wbId, JSONObject message) {
        //获取操作名
        String action = message.getStr("t");
        //获取sheet的index值
        String index = message.getStr("i");

        //如果是复制sheet，index的值需要另取
        if ("shc".equals(action)) {
            index = message.getJSONObject("v").getStr("copyindex");
        }

        //如果是删除sheet，index的值需要另取
        if ("shd".equals(action)) {
            index = message.getJSONObject("v").getStr("deleIndex");
        }

        //如果是恢复sheet，index的值需要另取
        if ("shre".equals(action)) {
            index = message.getJSONObject("v").getStr("reIndex");
        }
        //WorkSheetEntity ws = workSheetRepository.findByindexAndwbId(index, wbId);
        WorkSheetEntity ws = new WorkSheetEntity();


        List<PageData> sheet = new ArrayList<>();
//        PageData getCellData = new PageData();
//        getCellData.put("gridKey",wbId);
//        List<PageData> cellDataList = excelMapper.getExcelCellData(getCellData);
//        JSONArray cellData = new JSONArray();
//        //cellData.addAll(cellDataList);
//        for (PageData cellPd:cellDataList){
//            cellData.add(cellPd);
//        }

        switch (action) {
            //单个单元格刷新
            case "v":
                singleCellRefresh(message, wbId);
                break;
//            //范围单元格刷新
            case "rv":
                rangeCellRefresh(message, wbId);
                break;
//            //config操作
            case "cg":
                configRefresh(wbId,  message);
               break;
//            //通用保存
            case "all":
                allRefresh(wbId, message);
                break;
//            //函数链操作
           case "fc":
               ws = calcChainRefresh(ws, message);
                break;
//            //删除行或列
//            case "drc":
//                ws = drcRefresh(ws, message);
//                break;
//            //增加行或列
//            case "arc":
//                ws = arcRefresh(ws, message);
//                break;
//            //清除筛选
//            case "fsc":
//                ws = fscRefresh(ws, message);
//                break;
//            //恢复筛选
//            case "fsr":
//                ws = fscRefresh(ws, message);
//                break;
//            //新建sheet
            case "sha":
                shaRefresh(wbId, message);
                break;
//            //切换到指定sheet
//            case "shs":
//                shsRefresh(wbId, message);
//                break;
//            //复制sheet
//            case "shc":
//                ws = shcRefresh(ws, message);
//                break;
//            //修改工作簿名称
//            case "na":
//                naRefresh(wbId, message);
//                break;
//            //删除sheet
//            case "shd":
//                ws.setDeleteStatus(1);
//                break;
//            //删除sheet后恢复操作
//            case "shre":
//                ws.setDeleteStatus(0);
//                break;
//            //调整sheet位置
//            case "shr":
//                shrRefresh(wbId, message);
//                break;
//            //sheet属性(隐藏或显示)
//            case "sh":
//                ws = shRefresh(ws, message);
//                break;
            default:
                break;
        }
        if (ObjectUtil.isNull(ws)) {
            return;
        }
        //workSheetRepository.save(ws);

    }

    private JSONArray JSONArrayCellData(String wbId) {
        PageData getCellData = new PageData();
        getCellData.put("gridKey", wbId);
        List<PageData> cellDataList = excelMapper.getExcelCellData(getCellData);
        JSONArray cellData = new JSONArray();
        //cellData.addAll(cellDataList);
        for (PageData cellPd : cellDataList) {
            cellData.add(cellPd);
        }
        return cellData;
    }

    /**
     * 单个单元格刷新
     *
     * @param
     * @param message
     * @return
     */
    private void singleCellRefresh(JSONObject message, String gridKey) {
        //String t = message.getStr("t");
        String i = message.getStr("i");
        String r = message.getStr("r");
        String c = message.getStr("c");
        JSONObject ct=null;
        if(message.get("v")==null) {
        	ct = message.getJSONObject("v").getJSONObject("ct");
        }else {
        	if(UuidUtil.getStrType(message.get("v").toString())==3) {
            	
            }else {
            	ct = message.getJSONObject("v").getJSONObject("ct");
            }
        }
		
        String v=null;
        String m=null;
        String f=null;
        String bl=null;
        String fc=null;
        String bg=null;
        String fs=null;
        String ff=null;
        String tb=null;
        String ht=null;
		try {
			v = message.getJSONObject("v").getStr("v");
			m = message.getJSONObject("v").getStr("m");
			f = message.getJSONObject("v").getStr("f");
			bl = message.getJSONObject("v").getStr("bl");
			fc = message.getJSONObject("v").getStr("fc");
			bg = message.getJSONObject("v").getStr("bg");
			fs = message.getJSONObject("v").getStr("fs");
			ff = message.getJSONObject("v").getStr("ff");
			tb = message.getJSONObject("v").getStr("tb");
			ht = message.getJSONObject("v").getStr("ht");
		} catch (Exception e) {
			
		}
        if(f==null) {
        	f="";
        }

        boolean ifOperation1 = "".equals(i) && "".equals(r) && "".equals(c) && "".equals(gridKey);
        boolean ifOperation2 = (null != i && null != r && null != c && null != gridKey);
        if (!ifOperation1 && ifOperation2) {

            PageData OneCellDataParam = new PageData();
            OneCellDataParam.put("i", i);
            OneCellDataParam.put("r", r);
            OneCellDataParam.put("c", c);
            OneCellDataParam.put("v", v);
            OneCellDataParam.put("m", m);
            OneCellDataParam.put("gridKey", gridKey);
            if (null == ct) {
                OneCellDataParam.put("v", v);
                OneCellDataParam.put("fa", "General");
                OneCellDataParam.put("t", "n");
                if(v==null) {//文本框不设置值先设置样式会走这块
                	OneCellDataParam.put("v", "");
                    OneCellDataParam.put("f", f);
                    OneCellDataParam.put("bl", bl);
                    OneCellDataParam.put("fc", fc);
                    OneCellDataParam.put("bg", bg);
                    OneCellDataParam.put("fs", fs);
                    OneCellDataParam.put("ff", ff);
                    OneCellDataParam.put("tb", tb);
                    OneCellDataParam.put("ht", ht);
                }
            } else {
                String fa = ct.getStr("fa");
                String t = ct.getStr("t");
                OneCellDataParam.put("fa", fa);
                OneCellDataParam.put("t", t);
                OneCellDataParam.put("f", f);
                OneCellDataParam.put("bl", bl);
                OneCellDataParam.put("fc", fc);
                OneCellDataParam.put("bg", bg);
                OneCellDataParam.put("fs", fs);
                OneCellDataParam.put("ff", ff);
                OneCellDataParam.put("tb", tb);
                OneCellDataParam.put("ht", ht);
            }

            List<PageData> ifCellDataList = new ArrayList<>();
            ifCellDataList = excelMapper.getIfExcelCellData(OneCellDataParam);

            PageData Data = excelMapper.getDataByIndex(OneCellDataParam);
            OneCellDataParam.put("DATA_ID", Data.get("DATA_ID").toString());

            if (ifCellDataList.size() > 0) {
                if ("".equals(v) || null == v) {
                	//System.out.println(UuidUtil.getStrType(message.getJSONObject("v").toString()));
                	//System.out.println(message.getJSONObject("v").toString());
                	if(message.get("v")==null) {
                		excelMapper.delCellDataOne(OneCellDataParam);
                	}else {
                		if (UuidUtil.getStrType(message.get("v").toString())==3) {//外层v如果是字符串就是修改，如果是json对象就是删除
    						OneCellDataParam.put("v", message.getStr("v"));
    						OneCellDataParam.put("m", message.getStr("v"));
    						excelMapper.updataCellDataOne(OneCellDataParam);
    					} else {
    						excelMapper.delCellDataOne(OneCellDataParam);
    					}
                	}
                    /*try {
						if (message.getStr("v").length() > 0) {//修改文本框级联被更改的
							OneCellDataParam.put("v", message.getStr("v"));
							OneCellDataParam.put("m", message.getStr("v"));
							excelMapper.updataCellDataOne(OneCellDataParam);
						} else {
							excelMapper.delCellDataOne(OneCellDataParam);
						}
					} catch (Exception e) {
						excelMapper.delCellDataOne(OneCellDataParam);
					}*/
                } else {
                    //System.out.println("更新"+gridKey);
                    excelMapper.updataCellDataOne(OneCellDataParam);
                }
            } else {
                //System.out.println("新增"+gridKey);
                OneCellDataParam.put("CELLDATA_ID", UuidUtil.get32UUID());
                excelMapper.saveCellDataOne(OneCellDataParam);
            }
        }
    }


    /**
     * 范围单元格刷新
     *
     * @param
     * @param message
     * @return
     */
    private void rangeCellRefresh(JSONObject message, String gridKey) {
        JSONArray rowArray = message.getJSONObject("range").getJSONArray("row");
        JSONArray columnArray = message.getJSONObject("range").getJSONArray("column");
        JSONArray vArray = message.getJSONArray("v");
        int countRowIndex = 0;

        List<PageData> allEditCellDataList = new ArrayList<>();
        List<PageData> allSaveCellDataList = new ArrayList<>();
        List<PageData> allDelCellDataList = new ArrayList<>();

        //遍历行列，进行增删改列表封装
        for (int ri = (int) rowArray.get(0); ri <= (int) rowArray.get(1); ri++) {
            int countColumnIndex = 0;
            for (int ci = (int) columnArray.get(0); ci <= (int) columnArray.get(1); ci++) {
                List<String> flag = new ArrayList<>();
                Object newCell = JSONUtil.parseArray(vArray.get(countRowIndex)).get(countColumnIndex);
                if (null != newCell) {
                    JSONObject collectData = Objects.requireNonNull(Objects.requireNonNull(JSONUtil.createObj().put("r", ri)).put("c", ci)).put("v", newCell);
                    //解析
                    String i = message.getStr("i");
                    assert collectData != null;
                    String r = collectData.getStr("r");
                    String c = collectData.getStr("c");
                    JSONObject v1 = collectData.getJSONObject("v");
                    if (null != v1) {
                        JSONObject ct = collectData.getJSONObject("v").getJSONObject("ct");
                        String v = collectData.getJSONObject("v").getStr("v");
                        String m = collectData.getJSONObject("v").getStr("m");
                        String f = collectData.getJSONObject("v").getStr("f");
                        String bl = collectData.getJSONObject("v").getStr("bl");
                        String fc = collectData.getJSONObject("v").getStr("fc");
                        String bg = collectData.getJSONObject("v").getStr("bg");
                        String fs = collectData.getJSONObject("v").getStr("fs");
                        String ff = collectData.getJSONObject("v").getStr("ff");
                        String tb = collectData.getJSONObject("v").getStr("tb");
                        String ht = collectData.getJSONObject("v").getStr("ht");
                        if(f==null) { f=""; }
                        if(bl==null) { bl=""; }
                        if(fc==null) { fc=""; }
                        if(bg==null) { bg=""; }
                        if(fs==null) { fs=""; }
                        if(ff==null) { ff=""; }
                        if(tb==null) { tb=""; }
                        if(ht==null) { ht=""; }

                        boolean ifOperation1 = "".equals(i) && "".equals(r) && "".equals(c) && "".equals(gridKey);
                        boolean ifOperation2 = (null != i && null != r && null != c && null != gridKey);
                        if (!ifOperation1 && ifOperation2) {

                            PageData AllCellDataParam = new PageData();
                            AllCellDataParam.put("i", i);
                            AllCellDataParam.put("r", r);
                            AllCellDataParam.put("c", c);
                            AllCellDataParam.put("v", v);
                            AllCellDataParam.put("m", m);
                            AllCellDataParam.put("f", f);
                            AllCellDataParam.put("bl",bl);
                            AllCellDataParam.put("fc", fc);
                            AllCellDataParam.put("bg", bg);
                            AllCellDataParam.put("fs", fs);
                            AllCellDataParam.put("ff", ff);
                            AllCellDataParam.put("tb", tb);
                            AllCellDataParam.put("ht", ht);
                            AllCellDataParam.put("gridKey", gridKey);
                            if(ct==null) {
                            	break;
                            }
                            String fa = ct.getStr("fa");
                            String t = ct.getStr("t");
                            if (("".equals(fa) && "".equals(t)) || (null != fa && null != t)) {
                                AllCellDataParam.put("fa", "General");
                                AllCellDataParam.put("t", "n");
                            } else {
                                AllCellDataParam.put("fa", fa);
                                AllCellDataParam.put("t", t);
                            }

                            List<PageData> ifCellDataList = new ArrayList<>();
                            ifCellDataList = excelMapper.getIfExcelCellData(AllCellDataParam);

                            PageData Data = excelMapper.getDataByIndex(AllCellDataParam);
                            AllCellDataParam.put("DATA_ID", Data.get("DATA_ID").toString());

                            if (ifCellDataList.size() > 0) {
                                if ("".equals(v) || null == v) {
                                    //System.out.println("删除"+gridKey);
                                    allDelCellDataList.add(AllCellDataParam);
                                } else {
                                    //System.out.println("更新"+gridKey);
                                    allEditCellDataList.add(AllCellDataParam);
                                }
                            } else {
                                //System.out.println("新增"+gridKey);
                                AllCellDataParam.put("CELLDATA_ID", UuidUtil.get32UUID());
                                allSaveCellDataList.add(AllCellDataParam);
                            }
                        }
                    }
                }
                countColumnIndex++;
            }
            countRowIndex++;
        }

        //调用Mapper
        if (allDelCellDataList.size() > 0) {
            excelMapper.delAllCellData(allDelCellDataList);
        }
        if (allEditCellDataList.size() > 0) {
            System.out.println(allEditCellDataList);
            excelMapper.editAllCellData(allEditCellDataList);
        }
        if (allSaveCellDataList.size() > 0) {
            //System.out.println(allSaveCellDataList);
            excelMapper.saveAllCellData(allSaveCellDataList);
        }
    }


    /**
     * config更新
     *
     * @param ws
     * @param message
     * @return
     */
/*    private WorkSheetEntity configRefresh(WorkSheetEntity ws, JSONObject message) {
        JSONObject v = message.getJSONObject("v");
        JSONObject newConfig = JSONUtil.createObj().put(message.getStr("k"), v);
        if (ws.getData().getJSONObject("config").isEmpty()) {
            ws.getData().put("config", newConfig);
        } else {
            ws.getData().getJSONObject("config").put(message.getStr("k"), v);
        }

        return ws;
    }*/
    private void configRefresh(String wbId,  JSONObject message) {
    	  PageData allRefresh = new PageData();
          String k = message.getStr("k");
          String bfo = "";
          allRefresh.put("k",k);
          PageData getIdParam = new PageData();
          getIdParam.put("gridKey",wbId);
          PageData Options =  excelMapper.getExcelOptions(getIdParam);
          Options.put("index", message.getStr("i"));
    	
    	JSONObject v = message.getJSONObject("v");
    	JSONObject newConfig = JSONUtil.createObj().put(message.getStr("k"), v);
    	if(newConfig.getStr("columnlen")!=null) {
    		String columnlen=newConfig.getStr("columnlen").toString();
    		Options.put("columnlen", columnlen);
    		excelMapper.updataColumnlen(Options);
    	}
    	if(newConfig.getStr("rowlen")!=null) {
    		String rowlen=newConfig.getStr("rowlen").toString();
    		Options.put("rowlen", rowlen);
    		excelMapper.updataRowlen(Options);
    	}
    	if(newConfig.getStr("merge")!=null) {
    		String merge=newConfig.getStr("merge").toString();
    		Options.put("merge", merge);
    		excelMapper.updataMerge(Options);
    	}
    	if(k.equals("borderInfo")) {
    		Options.put("borderInfo", message.getJSONArray("v").toString());
    		excelMapper.updataBorderInfo(Options);
    	}
    	/*if(newConfig.getStr("borderInfo")!=null) {
    		String borderInfo=newConfig.getStr("borderInfo").toString();
    		Options.put("borderInfo", borderInfo);
    		excelMapper.updataBorderInfo(Options);
    	}*/
    	/*if (ws.getData().getJSONObject("config").isEmpty()) {
    		ws.getData().put("config", newConfig);
    	} else {
    		ws.getData().getJSONObject("config").put(message.getStr("k"), v);
    	}*/
    	
    	//return ws;
    }


    /**
     * 通用保存
     *
     * @param
     * @param message
     * @return
     */
    private void allRefresh(String wbId, JSONObject message) {
        PageData allRefresh = new PageData();
        String k = message.getStr("k");
        allRefresh.put("k",k);
        PageData getIdParam = new PageData();
        getIdParam.put("gridKey",wbId);
        PageData Options =  excelMapper.getExcelOptions(getIdParam);

        if("name".equals(k)){   //k为name   为修改sheet名称
            String v = message.getStr("v");
            String i = message.getStr("i");
            allRefresh.put("v",v);
            allRefresh.put("i",i);
            allRefresh.put("OPTIONS_ID",Options.get("OPTIONS_ID").toString());
            excelMapper.editSheetName(allRefresh);
        }else {
        	JSONObject newConfig = message.getJSONObject("v");
        	Options.put("index", message.getStr("i"));
        	if(newConfig.getStr("merge")!=null) {
        		System.out.println(newConfig.getStr("merge").toString());
        		String merge=newConfig.getStr("merge").toString();
        		Options.put("merge", merge);
        		excelMapper.updataMerge(Options);
        	}
        }
//        if (!message.getJSONObject("v").isEmpty()) {
//
//        }
    }


    /**
     * 函数链操作
     *
     * @param ws
     * @param message
     * @return
     */
    private WorkSheetEntity calcChainRefresh(WorkSheetEntity ws, JSONObject message) {
        JSONObject value = message.getJSONObject("v");
       /* if (!ws.getData().containsKey("calcChain")) {
            ws.getData().put("calcChain", new JSONArray());
        }
        JSONArray calcChain = ws.getData().getJSONArray("calcChain");
        if ("add".equals(message.getStr("op"))) {
            calcChain.add(value);
        } else if ("update".equals(message.getStr("op"))) {
            calcChain.remove(calcChain.get(message.getInt(message.getStr("pos"))));
            calcChain.add(value);
        } else if ("del".equals(message.getStr("op"))) {
            calcChain.remove(calcChain.get(message.getInt(message.getStr("pos"))));
        }*/
        return ws;
    }


    /**
     * 删除行或列
     *
     * @param ws
     * @param message
     * @return
     */
//    private WorkSheetEntity drcRefresh(WorkSheetEntity ws, JSONObject message) {
//        JSONArray celldata = ObjectUtil.cloneByStream(ws.getData().getJSONArray("celldata"));
//        int index = message.getJSONObject("v").getInt("index");
//        int len = message.getJSONObject("v").getInt("len");
//        if ("r".equals(message.getStr("rc"))) {
//            ws.getData().put("row", ws.getData().getInt("row") - len);
//        } else {
//            ws.getData().put("column", ws.getData().getInt("column") - len);
//        }
//        for (Object cell : celldata) {
//            JSONObject jsonObject = JSONUtil.parseObj(cell);
//            if ("r".equals(message.getStr("rc"))) {
//                //删除行所在区域的内容
//                if (jsonObject.getInt("r") >= index && jsonObject.getInt("r") < index + len) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                }
//                //增加大于 最大删除行的的行号
//                if (jsonObject.getInt("r") >= index + len) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("r", jsonObject.getInt("r") - len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//            } else {
//                //删除列所在区域的内容
//                if (jsonObject.getInt("c") >= index && jsonObject.getInt("c") < index + len) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                }
//                //增加大于 最大删除列的的列号
//                if (jsonObject.getInt("c") >= index + len) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("c", jsonObject.getInt("c") - len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//            }
//        }
//
//        return ws;
//    }


    /**
     * 增加行或列,暂未实现插入数据的情况
     *
     * @param ws
     * @param message
     * @return
     */
//    private WorkSheetEntity arcRefresh(WorkSheetEntity ws, JSONObject message) {
//        JSONArray celldata = ObjectUtil.cloneByStream(ws.getData().getJSONArray("celldata"));
//        int index = message.getJSONObject("v").getInt("index");
//        int len = message.getJSONObject("v").getInt("len");
//
//        for (Object cell : celldata) {
//            JSONObject jsonObject = JSONUtil.parseObj(cell);
//            if ("r".equals(message.getStr("rc"))) {
//                //如果是增加行，且是向左增加
//                if (jsonObject.getInt("r") >= index && "lefttop".equals(message.getJSONObject("v").getStr("direction"))) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("r", jsonObject.getInt("r") + len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//                //如果是增加行，且是向右增加
//                if (jsonObject.getInt("r") > index && "rightbottom".equals(message.getJSONObject("v").getStr("direction"))) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("r", jsonObject.getInt("r") + len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//
//
//            } else {
//                //如果是增加列，且是向上增加
//                if (jsonObject.getInt("c") >= index && "lefttop".equals(message.getJSONObject("v").getStr("direction"))) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("c", jsonObject.getInt("c") + len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//                //如果是增加列，且是向下增加
//                if (jsonObject.getInt("c") > index && "rightbottom".equals(message.getJSONObject("v").getStr("direction"))) {
//                    ws.getData().getJSONArray("celldata").remove(jsonObject);
//                    jsonObject.put("c", jsonObject.getInt("c") + len);
//                    ws.getData().getJSONArray("celldata").add(jsonObject);
//                }
//
//            }
//        }
//        JSONArray vArray = message.getJSONObject("v").getJSONArray("data");
//        if ("r".equals(message.getStr("rc"))) {
//            ws.getData().put("row", ws.getData().getInt("row") + len);
//            for (int r = 0; r < vArray.size(); r++) {
//                for (int c = 0; c < JSONUtil.parseArray(vArray.get(0)).size(); c++) {
//                    if (JSONUtil.parseArray(vArray.get(r)).get(c) == null) {
//                        continue;
//                    }
//                    JSONObject newCell = JSONUtil.createObj().put("r", r + index).put("c", c).put("v", JSONUtil.parseArray(vArray.get(r)).get(c));
//                    ws.getData().getJSONArray("celldata").add(newCell);
//                }
//            }
//
//        } else {
//            ws.getData().put("column", ws.getData().getInt("column") + len);
//            for (int r = 0; r < vArray.size(); r++) {
//                for (int c = 0; c < JSONUtil.parseArray(vArray.get(0)).size(); c++) {
//                    if (JSONUtil.parseArray(vArray.get(r)).get(c) == null) {
//                        continue;
//                    }
//                    JSONObject newCell = JSONUtil.createObj().put("r", r).put("c", c + index).put("v", JSONUtil.parseArray(vArray.get(r)).get(c));
//                    ws.getData().getJSONArray("celldata").add(newCell);
//                }
//            }
//        }
//
//
//        return ws;
//    }


    /**
     * 筛选操作
     *
     * @param ws
     * @param message
     * @return
     */
//    private WorkSheetEntity fscRefresh(WorkSheetEntity ws, JSONObject message) {
//
//        if (message.getJSONObject("v").isEmpty()) {
//            ws.getData().remove("filter");
//            ws.getData().remove("filter_select");
//        } else {
//            ws.getData().put("filter", message.getJSONObject("v").getJSONArray("filter"));
//            ws.getData().put("filter_select", message.getJSONObject("v").getJSONObject("filter_select"));
//        }
//        return ws;
//    }


    /**
     * 新建sheet
     *
     * @param wbId
     * @param message
     * @return
     */
    private void shaRefresh(String wbId, JSONObject message) {
        JSONObject v = message.getJSONObject("v");
        String name = v.getStr("name");
        String status = v.getStr("status");
        String order = v.getStr("order");
        String index = v.getStr("index");
        PageData getIdParam = new PageData();
        getIdParam.put("gridKey",wbId);
        PageData Options =  excelMapper.getExcelOptions(getIdParam);
        PageData saveSheet = new PageData();
        saveSheet.put("DATA_ID",UuidUtil.get32UUID());
        saveSheet.put("name",name);
        saveSheet.put("status",status);
        saveSheet.put("order",order);
        saveSheet.put("index",index);
        saveSheet.put("OPTIONS_ID",Options.get("OPTIONS_ID").toString());

        excelMapper.saveSheet(saveSheet);
    }


    /**
     * 复制sheet
     *
     * @param ws
     * @param message
     * @return
     */
//    private WorkSheetEntity shcRefresh(WorkSheetEntity ws, JSONObject message) {
//
//        String index = message.getStr("i");
//        ws.setId(IdUtil.simpleUUID());
//        ws.getData().put("index", index);
//        ws.getData().put("name", message.getJSONObject("v").getStr("name"));
//
//        return ws;
//    }

    /**
     * 调整sheet位置
     *
     * @param wbId
     * @param message
     */
//    private void shrRefresh(String wbId, JSONObject message) {
//        List<WorkSheetEntity> allSheets = workSheetRepository.findAllBywbId(wbId);
//
//        allSheets.forEach(sheet -> {
//            sheet.getData().put("order", message.getJSONObject("v").getInt(sheet.getData().getStr("index")));
//            workSheetRepository.save(sheet);
//        });
//
//    }

    /**
     * 切换到指定sheet
     *
     * @param ws
     * @param message
     * @return
     */
//    private void shsRefresh(String wbId, JSONObject message) {
//        WorkSheetEntity lastWs = workSheetRepository.findBystatusAndwbId(1, wbId);
//        lastWs.getData().put("status", 0);
//        WorkSheetEntity thisWs = workSheetRepository.findByindexAndwbId(message.getStr("v"), wbId);
//        thisWs.getData().put("status", 1);
//        workSheetRepository.save(lastWs);
//        workSheetRepository.save(thisWs);
//    }


    /**
     * sheet属性(隐藏或显示)
     *
     * @param wbId
     * @param message
     */
//    private WorkSheetEntity shRefresh(WorkSheetEntity ws, JSONObject message) {
//        Integer hideStatus = message.getInt("v");
//        ws.getData().put("hide", hideStatus);
//
//        WorkSheetEntity curWs = new WorkSheetEntity();
//
//        if ("hide".equals(message.getStr("op"))) {
//            ws.getData().put("status", 0);
//            String cur = message.getStr("cur");
//            curWs = workSheetRepository.findByindexAndwbId(cur, ws.getWbId());
//            curWs.getData().put("status", 1);
//
//        } else {
//            curWs = workSheetRepository.findBystatusAndwbId(1, ws.getWbId());
//            curWs.getData().put("status", 0);
//        }
//
//        workSheetRepository.save(curWs);
//        return ws;
//    }

    /**
     * 修改工作簿名称
     *
     * @param wbId
     * @param message
     * @return
     */
//    private void naRefresh(String wbId, JSONObject message) {
//        Optional<WorkBookEntity> wb = workBookRepository.findById(wbId);
//        if (wb.isPresent()) {
//            WorkBookEntity workBookEntity = wb.get();
//            workBookEntity.getOption().put("title", message.getStr("v"));
//            workBookRepository.save(workBookEntity);
//        }
//    }

}
