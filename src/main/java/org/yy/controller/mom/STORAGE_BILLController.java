package org.yy.controller.mom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.STORAGE_BILLService;
import org.yy.service.mom.STORAGE_UNITService;

/** 
 * 说明：出入存储单据
 * 作者：YuanYe
 * 时间：2020-01-16
 * 
 */
@Controller
@RequestMapping("/storage_bill")
public class STORAGE_BILLController extends BaseController {
	
	@Autowired
	private STORAGE_BILLService storage_billService;
	@Autowired
	private STORAGE_UNITService storage_unitService;
	@Autowired
	private StockService stockService;
	@Autowired
	private MAT_BASICService matBasicService;
	/**出入存储单元-新增
	 * @param
	 * @author 管悦
	 * @date 2020-01-20
	 * @throws Exception
	 * @version 1.0
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ge_o:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
		pd.put("OPERATOR", Jurisdiction.getName());//操作人
		pd.put("FTIME", Tools.date2Str(new Date()));//操作时间
		try{			
			storage_billService.save(pd);
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("result", result);
			map.put("pd", pd);
			//map.put("msg", msg);
		}
		
		return map;
	}

	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("storage_bill:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		storage_billService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("storage_bill:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		storage_billService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	/**出入存储单据-列表和查询
	 * @param page
	 * @author 管悦
	 * @date 2020-01-17
	 * @throws Exception
	 * @version 1.0
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("storage_unit:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		String result = "500";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键字检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
        //页码
		if(null != pd.getString("currentPage")){
			page.setCurrentPage(Integer.parseInt(pd.getString("currentPage")));
			map.put("currentPage", pd.getString("currentPage"));
		}else{
			page.setCurrentPage(1);
			map.put("currentPage", "1");
		}		
		//每页应显示条数
		if(null != pd.getString("showCount")){
			page.setShowCount(Integer.parseInt(pd.getString("showCount")));
			map.put("showCount", pd.getString("showCount"));
		}else{
			page.setShowCount(10);
			map.put("showCount", "10");
		}
		page.setPd(pd);
		List<PageData>	varList = storage_billService.list(page);	//列出列表		
		try{			
			//总条数
			map.put("total",page.getTotalResult());
			if(varList!=null && varList.size()!=0){	
				result = "200";
			}else{
				result = "201";
			}
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", result);
		}
		return map;
	}

	
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("storage_unit:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = storage_billService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("storage_bill:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			storage_billService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("业务类型");	//1
		titles.add("子批次名称");	//2
		titles.add("物料内码");	//3
		titles.add("描述");	//4
		titles.add("存储量");	//5
		titles.add("存储单位");	//6
		titles.add("子批次状态");	//7
		titles.add("进出类型");	//8
		titles.add("时间");	//9
		titles.add("关联存储单元");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = storage_billService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TRANSACTION_TYPE"));	    //1
			vpd.put("var2", varOList.get(i).getString("SUBLOT_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("MAT_CODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDES"));	    //4
			vpd.put("var5", varOList.get(i).get("STORAGE_AMOUNT").toString());	//5
			vpd.put("var6", varOList.get(i).getString("STORAGE_UNIT"));	    //6
			vpd.put("var7", varOList.get(i).getString("SUBLOT_STATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("INPUT_OUT_TYPE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FTIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("STORAGE_UNITCLASS_ID"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	 /**扫码input新增数据
	  * 1.获取主表ID，查询主表信息
	  * 2.解读物料编码，查询物料是唯一码还是类型码
	  * 3.判断容器类型是混合还是单一，是单一的判断与主表选择物料是否一致
	  * 4.单一的要判断存量上限，超过存量上限不能新增
	  * 5.保存成功单一的要反写容器实际储存数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goScanIn")
	//@RequiresPermissions("storage_unit:edit")
	@ResponseBody
	public Object goScanIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		//String errInfo = "success";
		String errInfo = "errInfo";
		String msg = "保存失败!请检查条码是否正确";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			String STORAGE_UNIT_ID=pd.getString("STORAGE_UNIT_ID");
			String FCODE=pd.getString("FCODE");
			String FTYPE=pd.getString("FTYPE");
			String oneThingsCode = "";//唯一码
			String qrCode = "";//类型码
			String materialId = "";//物料ID
			Double count = 0.0;//数量
			String gcCode = "";//锅次
			String fzCode = "";//辅助属性
			PageData pdVessel = new PageData();//容器主表信息
			pdVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
			pdVessel=storage_unitService.findById(pdVessel);//查询容器主表信息
			PageData DetailsItempd = new PageData();
			PageData getDetailsItemPd = new PageData();
			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(FCODE.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = FCODE.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					fzCode = String.valueOf(codeSplitList.get(3));
					if(split.length==5) {
						gcCode=codeSplitList.get(4);
					}
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					DetailsItempd = stockService.getDetailsItem(pData);
					if(null!=DetailsItempd) {
						String MAT_NAME=DetailsItempd.getString("MAT_NAME");//物料名称
						String MAT_CODE=DetailsItempd.getString("MAT_CODE");//物料代码
						String MAT_UNIT=DetailsItempd.getString("MAT_UNIT");//物料单位
						String ItemID=DetailsItempd.getString("ItemID");//物料ID
						if(null!=pdVessel) {
							if("单一".equals(pdVessel.getString("BLEND_TYPE"))&&ItemID.equals(pdVessel.getString("MAT_CODE"))) {
								Double ACTUAL_QTY=0.0;
								try {
									ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
								} catch (Exception e) {
									ACTUAL_QTY = 0.0;
								}
								Double STOCK_QTY_H = Double.parseDouble(pdVessel.get("STOCK_QTY_H").toString());
								Double ACTUAL_AMOUNT = count+ACTUAL_QTY;
								if(ACTUAL_AMOUNT<=STOCK_QTY_H) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
									billPd.put("OPERATOR", Jurisdiction.getName());//操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
									billPd.put("MAT_CODE", MAT_CODE);//物料代码
									billPd.put("STORAGE_AMOUNT", count);//数量
									billPd.put("INPUT_OUT_TYPE", "input");//进出类型
									billPd.put("SUBLOT_STATE", "锁定");//状态
									billPd.put("ONLY_CODE", "W");//一物一码
									billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
									billPd.put("POT_NUMBER", gcCode);//锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
									billPd.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);//单位
									storage_billService.save(billPd);
									PageData pdEditVessel = new PageData();
									pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
									pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);	
									storage_unitService.editQty(pdEditVessel);//更新容器实际数量
									msg = "保存成功！";
									errInfo = "success";
								
								}else {
									msg = "超出容器可存储上限！";
								}
							}
							if("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
								PageData billPd = new PageData();
								billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
								billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
								billPd.put("OPERATOR", Jurisdiction.getName());//操作人
								billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
								billPd.put("MAT_CODE", MAT_CODE);//物料代码
								billPd.put("STORAGE_AMOUNT", count);//数量
								billPd.put("INPUT_OUT_TYPE", "input");//进出类型
								billPd.put("SUBLOT_STATE", "锁定");//状态
								billPd.put("ONLY_CODE", "W");//一物一码
								billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
								billPd.put("POT_NUMBER", gcCode);//锅次
								billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
								billPd.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);//单位
								storage_billService.save(billPd);
								msg = "保存成功！";
								errInfo = "success";
							}
						}
						
						
					}
					
				}
			}
			// 类型码 带出 物料码
			if ("L".equals(FCODE.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = FCODE.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					fzCode = String.valueOf(codeSplitList.get(3));
					if(split.length==5) {
						gcCode=codeSplitList.get(4);
					}
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					List<PageData> listByMatCode = matBasicService.getListByMatCode(qrCode);
					if(CollectionUtil.isNotEmpty(listByMatCode)){
						DetailsItempd = listByMatCode.get(0);
					}
					
					if(null!=DetailsItempd) {
						String MAT_NAME=DetailsItempd.getString("MAT_NAME");//物料名称
						String MAT_CODE=DetailsItempd.getString("MAT_CODE");//物料代码
						String MAT_UNIT=DetailsItempd.getString("FUNITNAME");//物料单位
						String ItemID=DetailsItempd.getString("MAT_BASIC_ID");//物料ID
						if(null!=pdVessel) {
							if("单一".equals(pdVessel.getString("BLEND_TYPE"))&&ItemID.equals(pdVessel.getString("MAT_CODE"))) {
								Double ACTUAL_QTY=0.0;
								try {
									ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
								} catch (Exception e) {
									ACTUAL_QTY = 0.0;
								}
								Double STOCK_QTY_H = Double.parseDouble(pdVessel.get("STOCK_QTY_H").toString());
								Double ACTUAL_AMOUNT = count+ACTUAL_QTY;
								if(ACTUAL_AMOUNT<=STOCK_QTY_H) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
									billPd.put("OPERATOR", Jurisdiction.getName());//操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
									billPd.put("MAT_CODE", MAT_CODE);//物料代码
									billPd.put("STORAGE_AMOUNT", count);//数量
									billPd.put("INPUT_OUT_TYPE", "input");//进出类型
									billPd.put("SUBLOT_STATE", "锁定");//状态
									billPd.put("ONLY_CODE", "L");//一物一码
									billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
									billPd.put("POT_NUMBER", gcCode);//锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
									billPd.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);//任务ID
									storage_billService.save(billPd);
									PageData pdEditVessel = new PageData();
									pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
									pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);	
									storage_unitService.editQty(pdEditVessel);//更新容器实际数量
									msg = "保存成功！";
									errInfo = "success";
								
								}else {
									msg = "超出容器可存储上限！";
								}
							}
							if("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
								PageData billPd = new PageData();
								billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
								billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
								billPd.put("OPERATOR", Jurisdiction.getName());//操作人
								billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
								billPd.put("MAT_CODE", MAT_CODE);//物料代码
								billPd.put("STORAGE_AMOUNT", count);//数量
								billPd.put("INPUT_OUT_TYPE", "input");//进出类型
								billPd.put("SUBLOT_STATE", "锁定");//状态
								billPd.put("ONLY_CODE", "L");//一物一码
								billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
								billPd.put("POT_NUMBER", gcCode);//锅次
								billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
								billPd.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);//单位
								storage_billService.save(billPd);
								msg = "保存成功！";
								errInfo = "success";
							}
						}
						
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			 errInfo = "errInfo";
			 msg = "保存失败!请检查条码是否正确";
		}
		
		//pd = storage_billService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("msg", msg);
		map.put("result", errInfo);
		return map;
	}	
	
	/**扫码out新增数据
	  * 1.获取主表ID，查询主表信息
	  * 2.解读物料编码，查询物料是唯一码还是类型码
	  * 3.判断容器类型是混合还是单一，是单一的判断与主表选择物料是否一致
	  * 4.单一的要判断存量上限，超过存量上限不能新增
	  * 5.保存成功单一的要反写容器实际储存数量
	  * 6.out的要反写消耗的状态为释放
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goScanOut")
	//@RequiresPermissions("storage_unit:edit")
	@ResponseBody
	public Object goScanOut() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		//String errInfo = "success";
		String errInfo = "errInfo";
		String msg = "保存失败!请检查条码是否正确！";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String STORAGE_UNIT_ID=pd.getString("STORAGE_UNIT_ID");
			String FCODE=pd.getString("FCODE");
			String FTYPE=pd.getString("FTYPE");
			String oneThingsCode = "";//唯一码
			String qrCode = "";//类型码
			String materialId = "";//物料ID
			Double count = 0.0;//数量
			String gcCode = "";//锅次
			String fzCode = "";//锅次
			PageData pdVessel = new PageData();//容器主表信息
			pdVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
			pdVessel=storage_unitService.findById(pdVessel);//查询容器主表信息
			PageData DetailsItempd = new PageData();
			PageData getDetailsItemPd = new PageData();
			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(FCODE.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = FCODE.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					fzCode = String.valueOf(codeSplitList.get(3));
					if(split.length==5) {
						gcCode=codeSplitList.get(4);
					}
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					DetailsItempd = stockService.getDetailsItem(pData);
					if(null!=DetailsItempd) {
						String MAT_NAME=DetailsItempd.getString("MAT_NAME");//物料名称
						String MAT_CODE=DetailsItempd.getString("MAT_CODE");//物料代码
						String MAT_UNIT=DetailsItempd.getString("MAT_UNIT");//物料单位
						String ItemID=DetailsItempd.getString("ItemID");//物料ID
						if(null!=pdVessel) {
							String OUTGOING_TYPE = pdVessel.getString("OUTGOING_TYPE");//消耗类型
							if("单一".equals(pdVessel.getString("BLEND_TYPE"))&&ItemID.equals(pdVessel.getString("MAT_CODE"))) {
								PageData qtyPd = new PageData();
								qtyPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
								qtyPd.put("MAT_CODE", MAT_CODE);
								qtyPd.put("STORAGE_AMOUNT", count);
								if(OUTGOING_TYPE.equals("先进先出")) {
									qtyPd=storage_billService.findByqtyBig(qtyPd);//先进先出查询数据
								}else {
									qtyPd=storage_billService.findByqty(qtyPd);//先进后出查询数据
								}
								if(null!=qtyPd) {
									Double ACTUAL_QTY=0.0;
									try {
										ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
									} catch (Exception e) {
										ACTUAL_QTY = 0.0;
									}
									Double ACTUAL_AMOUNT = ACTUAL_QTY-count;
									if(ACTUAL_AMOUNT>=0) {
										PageData billPd = new PageData();
										billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
										billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
										billPd.put("OPERATOR", Jurisdiction.getName());//操作人
										billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
										billPd.put("MAT_CODE", MAT_CODE);//物料代码
										billPd.put("STORAGE_AMOUNT", count);//数量
										billPd.put("INPUT_OUT_TYPE", "out");//进出类型
										billPd.put("SUBLOT_STATE", "");//状态
										billPd.put("ONLY_CODE", "W");//一物一码
										billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
										billPd.put("POT_NUMBER", gcCode);//锅次
										billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
										storage_billService.save(billPd);
										PageData pdEditVessel = new PageData();
										pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
										pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);	
										storage_unitService.editQty(pdEditVessel);//更新容器实际数量
										qtyPd.put("SUBLOT_STATE", "释放");
										storage_billService.editState(qtyPd);//更改状态
										msg = "保存成功！";
										errInfo = "success";
									
									}else {
										msg = "低于容器数量！";
									}
								}else {
									msg = "数量不足！";
								}
								
							}
							if("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
								PageData qtyPd = new PageData();
								qtyPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
								qtyPd.put("MAT_CODE", MAT_CODE);
								qtyPd.put("STORAGE_AMOUNT", count);
								qtyPd=storage_billService.findByqty(qtyPd);//先进后出查询数据
								if(null!=qtyPd) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
									billPd.put("OPERATOR", Jurisdiction.getName());//操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
									billPd.put("MAT_CODE", MAT_CODE);//物料代码
									billPd.put("STORAGE_AMOUNT", count);//数量
									billPd.put("INPUT_OUT_TYPE", "out");//进出类型
									billPd.put("SUBLOT_STATE", "");//状态
									billPd.put("ONLY_CODE", "W");//一物一码
									billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
									billPd.put("POT_NUMBER", gcCode);//锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
									storage_billService.save(billPd);
									qtyPd.put("SUBLOT_STATE", "释放");
									storage_billService.editState(qtyPd);//更改状态
									msg = "保存成功！";
									errInfo = "success";
								}else {
									msg = "数量不足！";
								}
								
							}
						}
						
						
					}
					
				}
			}
			// 类型码 带出 物料码
			if ("L".equals(FCODE.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = FCODE.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					fzCode = String.valueOf(codeSplitList.get(3));
					if(split.length==5) {
						gcCode=codeSplitList.get(4);
					}
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					DetailsItempd = stockService.getDetailsItem(pData);
					if(null!=DetailsItempd) {
						String MAT_NAME=DetailsItempd.getString("MAT_NAME");//物料名称
						String MAT_CODE=DetailsItempd.getString("MAT_CODE");//物料代码
						String MAT_UNIT=DetailsItempd.getString("MAT_UNIT");//物料单位
						String ItemID=DetailsItempd.getString("ItemID");//物料ID
						if(null!=pdVessel) {
							String OUTGOING_TYPE = pdVessel.getString("OUTGOING_TYPE");//消耗类型
							if("单一".equals(pdVessel.getString("BLEND_TYPE"))&&ItemID.equals(pdVessel.getString("MAT_CODE"))) {
								PageData qtyPd = new PageData();
								qtyPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
								qtyPd.put("MAT_CODE", MAT_CODE);
								qtyPd.put("STORAGE_AMOUNT", count);
								if(OUTGOING_TYPE.equals("先进先出")) {
									qtyPd=storage_billService.findByqtyBig(qtyPd);//先进先出查询数据
								}else {
									qtyPd=storage_billService.findByqty(qtyPd);//先进后出查询数据
								}
								if(null!=qtyPd) {
									Double ACTUAL_QTY=0.0;
									try {
										ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
									} catch (Exception e) {
										ACTUAL_QTY = 0.0;
									}
									Double ACTUAL_AMOUNT = ACTUAL_QTY-count;
									if(ACTUAL_AMOUNT>=0) {
										PageData billPd = new PageData();
										billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
										billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
										billPd.put("OPERATOR", Jurisdiction.getName());//操作人
										billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
										billPd.put("MAT_CODE", MAT_CODE);//物料代码
										billPd.put("STORAGE_AMOUNT", count);//数量
										billPd.put("INPUT_OUT_TYPE", "out");//进出类型
										billPd.put("SUBLOT_STATE", "");//状态
										billPd.put("ONLY_CODE", "L");//一物一码
										billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
										billPd.put("POT_NUMBER", gcCode);//锅次
										billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
										storage_billService.save(billPd);
										PageData pdEditVessel = new PageData();
										pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
										pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);	
										storage_unitService.editQty(pdEditVessel);//更新容器实际数量
										qtyPd.put("SUBLOT_STATE", "释放");
										storage_billService.editState(qtyPd);//更改状态
										msg = "保存成功！";
										errInfo = "success";
									
									}else {
										msg = "数量不足！";
									}
								}else {
									msg = "数量不足！";
								}
								
							}
							if("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
								PageData qtyPd = new PageData();
								qtyPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
								qtyPd.put("MAT_CODE", MAT_CODE);
								qtyPd.put("STORAGE_AMOUNT", count);
								qtyPd=storage_billService.findByqty(qtyPd);//先进后出查询数据
								if(null!=qtyPd) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID());	//主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);//主表主键
									billPd.put("OPERATOR", Jurisdiction.getName());//操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));//操作时间
									billPd.put("MAT_CODE", MAT_CODE);//物料代码
									billPd.put("STORAGE_AMOUNT", count);//数量
									billPd.put("INPUT_OUT_TYPE", "out");//进出类型
									billPd.put("SUBLOT_STATE", "");//状态
									billPd.put("ONLY_CODE", "L");//一物一码
									billPd.put("MAT_AUXILIARY", fzCode);//辅助属性
									billPd.put("POT_NUMBER", gcCode);//锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);//单位
									storage_billService.save(billPd);
									qtyPd.put("SUBLOT_STATE", "释放");
									storage_billService.editState(qtyPd);//更改状态
									msg = "保存成功！";
									errInfo = "success";
								}else {
									msg = "数量不足！";
								}
								
							}
						}
						
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			errInfo = "errInfo";
			msg = "保存失败!请检查条码是否正确！";
		}
		
		//pd = storage_billService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("msg", msg);
		map.put("result", errInfo);
		return map;
	}	
}
