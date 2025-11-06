package org.yy.controller.excel;

import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.excel.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/excelonline")
public class TableController extends BaseController {

	@Autowired
	private ExcelService excelService;

	/**
	 * 获取表格基本信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public Object onlineProductExcel(@RequestParam(value = "id", defaultValue = "27") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd.put("recordId","-1");
		pd.put("userName", "Tom");
		pd.put("title","在线表格");
		map.put("pd",pd);
		map.put("result",errInfo);
		return map;
	}

	/**
	 * 获取工作簿配置
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getExcelOptions")
	@ResponseBody
	public Object getExcelOptions() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("gridKey",pd.get("gridKey"));  //唯一标识
		pd = excelService.getExcelOptions(pd);  //获取工作簿配置
		map.put("pd",pd);
		map.put("result",errInfo);
		return map;
	}

	/**
	 * 获取工作表数据
	 * @param
	 * @return
	 */
	@PostMapping("/downData")
	@ResponseBody
	public String downExcelData() throws Exception{
		/***
		 * 从数据库中读取工作簿配置  --sheet数据
		 */
		PageData pd = new PageData();
		pd = this.getPageData();
		//System.out.println("-------"+pd.get("gridKey"));
		pd.put("gridKey",pd.get("gridKey")); //唯一标识
		//pd.put("gridKey","1079500#-8803#7c45f52b7d01486d88bc53cb17dcd2c3"); //唯一标识
		pd = excelService.getExcelOptions(pd);   //获取工作簿配置
		List<PageData> excelData = new ArrayList<>();
		excelData = excelService.getExcelDataByOptionsID(pd);  //获取工作表数据

		return getDataString(excelData);
	}

	/**
	 * 将list转化为string 并获取数据表初始数据
	 * @param pdList
	 * @return
	 */
	private String getDataString(List<PageData> pdList) throws Exception{
		List<String> dataList = new ArrayList<>();
		for(PageData pd :pdList){
			String data = null;
			String celldata = null;
			String calcChain = null;
			String resultcalcChainString = "";
			String resultCellString = "";
			String name = pd.getString("name");
			String index = pd.getString("Findex");
			String order = pd.getString("Forder");
			String status = pd.getString("Fstatus");
			String DATA_ID = pd.getString("DATA_ID");
			String columnlen = pd.getString("columnlen");
			String rowlen = pd.getString("rowlen");
			String merge = pd.getString("Fmerge");
			String borderInfo = pd.getString("borderInfo");
			if(null!=DATA_ID && !"".equals(DATA_ID)){
				PageData getCell = new PageData();
				getCell.put("DATA_ID",DATA_ID);
				List<PageData> cellDataList = excelService.getExcelCellData(getCell);
				List<String> resultCellStringList = new ArrayList<>();
				List<String> resultcalcChainStringList = new ArrayList<>();
				for(PageData cellPd :cellDataList) {
					String r = cellPd.getString("r");
					String c = cellPd.getString("c");
					String v = cellPd.getString("v");
					String m = cellPd.getString("m");
					String fa = cellPd.getString("fa");
					String t = cellPd.getString("t");
					String f = cellPd.getString("f");
                    String bl = cellPd.getString("bl")==null?"":cellPd.getString("bl");
                    String fc = cellPd.getString("fc")==null?"":cellPd.getString("fc");
                    String bg = cellPd.getString("bg")==null?"":cellPd.getString("bg");
                    String fs = cellPd.getString("fs")==null?"":cellPd.getString("fs");
                    String ff = cellPd.getString("ff")==null?"":cellPd.getString("ff");
                    String tb = cellPd.getString("tb")==null?"":cellPd.getString("tb");
                    String ht = cellPd.getString("ht")==null?"":cellPd.getString("ht");
                    if(!bl.equals("")) {bl=",\"bl\":\""+bl+"\""; }
                    if(!fc.equals("")) {fc=",\"fc\":\""+fc+"\""; }
                    if(!bg.equals("")) {bg=",\"bg\":\""+bg+"\""; }
                    if(!fs.equals("")) {fs=",\"fs\":\""+fs+"\""; }
                    if(!ff.equals("")) {ff=",\"ff\":\""+ff+"\""; }
                    if(!tb.equals("")) {tb=",\"tb\":\""+tb+"\""; }
                    if(!ht.equals("")) {ht=",\"ht\":\""+ht+"\""; }
                    
					if(f==null||f.equals("")) {
						celldata = "{\"r\":"+r+",\"c\":"+c+",\"v\":{\"v\":\""+v+"\",\"m\":\""+m+"\""+bl+fc+bg+fs+ff+tb+ht+",\"ct\":{\"fa\":\""+fa+"\",\"t\":\""+t+"\"}}}";
					}else {
							celldata = "{\"r\":"+r+",\"c\":"+c+",\"v\":{\"v\":\""+v+"\",\"f\":\""+f+"\",\"m\":\""+m+"\""+bl+fc+bg+fs+ff+tb+ht+",\"ct\":{\"fa\":\""+fa+"\",\"t\":\""+t+"\"}}}";
							calcChain = "{\"r\":"+r+",\"c\":"+c+",\"index\":\""+index+"\",\"color\":\"w\",\"parent\":null,\"chidren\":{},\"times\":0}";
							resultcalcChainStringList.add(calcChain);
					}
					resultCellStringList.add(celldata);
				}
				StringBuilder cellString = new StringBuilder();
				for(int i=0;i<resultCellStringList.size();i++){
					if(i<resultCellStringList.size()-1){
						cellString.append(resultCellStringList.get(i));
						cellString.append(",");
					}else{
						cellString.append(resultCellStringList.get(i));
					}
				}
				resultCellString = "["+cellString.toString()+"]";
				
				StringBuilder calcChainString = new StringBuilder();
				for(int i=0;i<resultcalcChainStringList.size();i++){
					if(i<resultcalcChainStringList.size()-1){
						calcChainString.append(resultcalcChainStringList.get(i));
						calcChainString.append(",");
					}else{
						calcChainString.append(resultcalcChainStringList.get(i));
					}
				}
				resultcalcChainString = "["+calcChainString.toString()+"]";
			}

			data = "{\"name\":\""+name+"\",\"index\":\""+index+"\",\"order\":\""+order+"\",\"status\":\""+status+"\",\"celldata\":"+resultCellString+",\"config\":{\"columnlen\":"+columnlen+",\"rowlen\":"+rowlen+",\"merge\":"+merge+",\"borderInfo\":"+borderInfo+"},\"calcChain\":"+resultcalcChainString+"}";
			System.out.println(data);
			dataList.add(data);
		}
		StringBuilder dataString = new StringBuilder();
		String resultString = "";

		for(int i=0;i<dataList.size();i++){
			if(i<dataList.size()-1){
				dataString.append(dataList.get(i));
				dataString.append(",");
			}else{
				dataString.append(dataList.get(i));
			}
		}

		resultString = "["+dataString.toString()+"]";
		return resultString;
	}
}
