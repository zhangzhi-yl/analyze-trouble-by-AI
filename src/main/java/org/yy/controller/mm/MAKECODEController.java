package org.yy.controller.mm;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.STORAGE_UNITService;
import org.yy.service.mom.Unit_InfoService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WH_LocationService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 说明：制码 作者：YuanYes QQ356703572 时间：2020-12-08 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/makecode")
public class MAKECODEController extends BaseController {

	@Autowired
	private MAKECODEService MAKECODEService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private Unit_InfoService Unit_InfoService;
	@Autowired
	private MAT_BASICService MAT_BASICService;
	@Autowired
	private EQM_BASEService eqm_baseService;
	@Autowired
	private WC_StationService wc_stationService;
	@Autowired
	private WH_LocationService wh_locationService;
	@Autowired
	private STORAGE_UNITService storage_unitService;
	
	@RequestMapping(value = "/matDetail")
	@ResponseBody
	public Object matDetail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 物料id
		String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
		// 辅助属性
		String SPropKey = pd.getString("SPropKey");
		PageData matParam = new PageData();
		matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
		pd = MAT_BASICService.findById(matParam);
		if(null == pd){
			map.put("result", "failed");
			map.put("msg", "该物料不存在");
			return map;
		}
		String ForOne = "否";
		if ("是".equals(pd.getString("UNIQUE_CODE_WHETHER"))) {
			ForOne = "是";
		}
		
		PageData pdMx = new PageData();
		pdMx.put("ForOne", ForOne);
		pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
		pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

		PageData unitMainParam = new PageData();
		unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
		PageData unitMain = Unit_InfoService.findById(unitMainParam);
		String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
		pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

		PageData unitAuxParam = new PageData();
		unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
		PageData unitAux = Unit_InfoService.findById(unitAuxParam);

		String MAT_AUX_UNIT = "默认包装";
		if (null != unitAux) {
			MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
		}
		pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
		pdMx.put("SPropKey", SPropKey);
		pdMx.put("MakeTime", Tools.date2Str(new Date()));
		map.put("result", errInfo);
		map.put("pd", pdMx);
		return map;
	}

	/**
	 * 打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/print")
	@ResponseBody
	public Object print() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 物料id
		String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
		// 份数
		int FNum = Integer.parseInt(pd.getString("FNum"));
		// 物料数量
		String MAT_SPECS_QTY = pd.getString("MAT_SPECS_QTY");
		// 辅助属性
		String SPropKey = pd.getString("SPropKey");
		// 锅次号
		String Wok_NUM = pd.getString("Wok_NUM");
		if(StringUtils.isEmpty(Wok_NUM)) {
			Wok_NUM = "";
		}
		PageData matParam = new PageData();
		matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
		pd = MAT_BASICService.findById(matParam);
		String Ftype = "类型码";
		if ("是".equals(pd.getString("UNIQUE_CODE_WHETHER"))) {
			Ftype = "唯一码";
		}
		pd.put("Ftype", Ftype);
		PageData pdCode = new PageData();
		List<PageData> varList = new ArrayList<PageData>();
		BigDecimal FCode = new BigDecimal(0);

		if ("类型码".equals(Ftype)) {
			pdCode = MAKECODEService.getCode(pd);
			String encode = "L,YL," + pd.getString("MAT_CODE") + ",YL," + MAT_SPECS_QTY + ",YL," + SPropKey+ ",YL,"+Wok_NUM;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", MAT_SPECS_QTY + MAT_MAIN_UNIT);
				pdMx.put("Wok_NUM", Wok_NUM);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKey);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				varList.add(pdMx);
			}
		} else if ("唯一码".equals(Ftype)) {
			pdCode = MAKECODEService.getCode(pd);
			FCode = new BigDecimal(pdCode.get("FCode").toString());
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				FCode = FCode.add(new BigDecimal(1));
				String encode = "W,YL," + FCode + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL," + SPropKey+",YL,"+Wok_NUM;
				pdMx.put("Encode", generateQRCodeImage(encode).getString("encode"));
				pdMx.put("FOrder", i);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);
				pdMx.put("Wok_NUM", Wok_NUM);
				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKey);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				pdMx.put("SerialNum", FCode);
				varList.add(pdMx);
			}
			pd.put("FCode", FCode);
			MAKECODEService.editCode(pd);
		}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", Ftype);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生成二维码 输入text 返回base64
	 * 
	 * @return
	 * @throws Exception
	 */

	public PageData generateQRCodeImage(String text) throws Exception {
		// String text = this.getPageData().getString("text");
		PageData pd = new PageData();
		Integer width = 300;
		Integer height = 300;
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, "2");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		pd.put("text", text);
		pd.put("encode", Base64Utils.encodeToString(outputStream.toByteArray()));
		outputStream.close();
		return pd;
	}
	
	/**
	 * 根据参数生成并返回二维码base64编码
	 * @param text 二维码内容
	 * @param width 二维码宽高
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getBase64QRCode")
	@ResponseBody
	public Object getBase64QRCode() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd=this.getPageData();
		List<PageData> list = MAT_BASICService.getListByMatCode(pd.getString("MAT_CODE"));
		if(CollectionUtils.isNotEmpty(list)) {
			PageData mat = list.get(0);
		// 物料id
		String MAT_BASIC_ID = mat.getString("MAT_BASIC_ID");
		
		// 物料数量
		String MAT_SPECS_QTY = pd.getString("MAT_SPECS_QTY");
		// 辅助属性
		String SPropKey = pd.getString("SPropKey");
		String WokNum = pd.getString("WokNum");
		if(StringUtils.isEmpty(WokNum)) {
			WokNum = "";			
		}
				PageData matParam = new PageData();
				matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
				pd = MAT_BASICService.findById(matParam);
				String Ftype = "类型码";
				if ("是".equals(mat.getString("UNIQUE_CODE_WHETHER"))) {
					Ftype = "唯一码";
				}
				pd.put("Ftype", Ftype);
				PageData pdCode = new PageData();
				List<PageData> varList = new ArrayList<PageData>();
				BigDecimal FCode = new BigDecimal(0);
				PageData pdMx = new PageData();
				String encode = "";
				if ("类型码".equals(Ftype)) {
					pdCode = MAKECODEService.getCode(pd);
					encode = "L,YL," + pd.getString("MAT_CODE") + ",YL," + MAT_SPECS_QTY + ",YL," 
					+ SPropKey+ ",YL," + WokNum ;
					String code = generateQRCodeImage(encode).getString("encode");
						pdMx.put("Encode", code);
						pdMx.put("FOrder", 1);

						pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
						pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

						PageData unitMainParam = new PageData();
						unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
						PageData unitMain = Unit_InfoService.findById(unitMainParam);
						String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
						pdMx.put("MAT_SPECS_QTY", MAT_SPECS_QTY + MAT_MAIN_UNIT);

						PageData unitAuxParam = new PageData();
						unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
						PageData unitAux = Unit_InfoService.findById(unitAuxParam);

						String MAT_AUX_UNIT = "默认包装";
						if (null != unitAux) {
							MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
						}
						pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
						pdMx.put("SPropKey", SPropKey);
						pdMx.put("MakeTime", Tools.date2Str(new Date()));

						varList.add(pdMx);
				} else if ("唯一码".equals(Ftype)) {
					pdCode = MAKECODEService.getCode(pd);
					FCode = new BigDecimal(pdCode.get("FCode").toString());
					
					FCode = FCode.add(new BigDecimal(1));
					encode = "W,YL," + FCode + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL," + SPropKey+ ",YL," + WokNum ;
					pdMx.put("Encode", generateQRCodeImage(encode).getString("encode"));
					pdMx.put("FOrder", 1);

					pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
					pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

					PageData unitMainParam = new PageData();
					unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
					PageData unitMain = Unit_InfoService.findById(unitMainParam);
					String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
					pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

					PageData unitAuxParam = new PageData();
					unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
					PageData unitAux = Unit_InfoService.findById(unitAuxParam);

					String MAT_AUX_UNIT = "默认包装";
					if (null != unitAux) {
						MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
					}
					pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
					pdMx.put("SPropKey", SPropKey);
					pdMx.put("MakeTime", Tools.date2Str(new Date()));

					pdMx.put("SerialNum", FCode);
					varList.add(pdMx);
					pd.put("FCode", FCode);
					MAKECODEService.editCode(pd);
				}
		Integer width = Integer.parseInt(pd.containsKey("width") && null!=pd.get("width")?pd.getString("width"):"300");
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, "2");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(encode, BarcodeFormat.QR_CODE, width, width, hints);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		pd.put("text", encode);
		pd.put("encode", Base64Utils.encodeToString(outputStream.toByteArray()));
		outputStream.close();
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions("MAKECODE:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MAKECODEService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@RequiresPermissions("MAKECODE:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MAKECODEService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@RequiresPermissions("MAKECODE:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = MAKECODEService.list(page); // 列出MAKECODE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@RequiresPermissions("MAKECODE:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MAKECODEService.findById(pd); // 根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@RequiresPermissions("MAKECODE:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MAKECODEService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("类型"); // 1
		titles.add("码"); // 2
		titles.add("创建时间"); // 3
		titles.add("创建人"); // 4
		dataMap.put("titles", titles);
		List<PageData> varOList = MAKECODEService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("Ftype")); // 1
			vpd.put("var2", varOList.get(i).get("FCode").toString()); // 2
			vpd.put("var3", varOList.get(i).getString("FCreatetime")); // 3
			vpd.put("var4", varOList.get(i).getString("FCreator")); // 4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}
	/**
	 * 员工打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printStaff")
	@ResponseBody
	public Object printStaff() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 人员id
		String STAFF_ID = pd.getString("STAFF_ID");
		// 份数
		//int FNum = Integer.parseInt(pd.getString("FNum"));
		int FNum =1;

		PageData matParam = new PageData();
		matParam.put("STAFF_ID", STAFF_ID);
		pd = staffService.findById(matParam);
		PageData pdCode = new PageData();
		List<PageData> varList = new ArrayList<PageData>();
			String encode = pd.getString("NAME") + "/" + pd.getString("USER_ID") ;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);
				pdMx.put("NAME", pd.get("NAME"));
				pdMx.put("USER_ID", pd.get("USER_ID"));
				pdMx.put("BIANMA", pd.get("BIANMA"));
				pdMx.put("MakeTime", Tools.date2Str(new Date()));
				varList.add(pdMx);
			}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "员工标签制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", STAFF_ID);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 设备打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printEqm")
	@ResponseBody
	public Object printEqm() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 设备id
		String EQM_BASE_ID = pd.getString("EQM_BASE_ID");
		// 份数
		//int FNum = Integer.parseInt(pd.getString("FNum"));
		int FNum =1;

		PageData matParam = new PageData();
		matParam.put("EQM_BASE_ID", EQM_BASE_ID);
		pd = eqm_baseService.findById(matParam);
		List<PageData> varList = new ArrayList<PageData>();
			String encode = pd.getString("FNAME") + ",YL," + pd.getString("FIDENTIFY") ;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);
				pdMx.put("FNAME", pd.get("FNAME"));
				pdMx.put("FIDENTIFY", pd.get("FIDENTIFY"));
				pdMx.put("MakeTime", Tools.date2Str(new Date()));
				varList.add(pdMx);
			}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "设备标签制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", EQM_BASE_ID);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 工作站打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printStation")
	@ResponseBody
	public Object printStation() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 工作站id
		String WC_STATION_ID = pd.getString("WC_STATION_ID");
		// 份数
		//int FNum = Integer.parseInt(pd.getString("FNum"));
		int FNum =1;

		PageData matParam = new PageData();
		matParam.put("WC_STATION_ID", WC_STATION_ID);
		pd = wc_stationService.findById(matParam);
		List<PageData> varList = new ArrayList<PageData>();
			//String encode = pd.getString("WC_WORKCENTER_NAME") + ",YL," + pd.getString("WC_WORKCENTER_CODE")+ ",YL," + pd.getString("FNAME")+ ",YL," + pd.getString("FCODE") ;
		    String encode =  pd.getString("FNAME")+ "/" + pd.getString("FCODE") ;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);
				pdMx.put("FNAME", pd.get("FNAME"));
				pdMx.put("FCODE", pd.get("FCODE"));
				pdMx.put("WC_WORKCENTER_NAME", pd.get("WC_WORKCENTER_NAME"));
				pdMx.put("WC_WORKCENTER_CODE", pd.get("WC_WORKCENTER_CODE"));
				pdMx.put("MakeTime", Tools.date2Str(new Date()));
				varList.add(pdMx);
			}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "工作站标签制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", WC_STATION_ID);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 库位打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printLocation")
	@ResponseBody
	public Object printLocation() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 库位id
		String WH_LOCATION_ID = pd.getString("WH_LOCATION_ID");
		// 份数
		//int FNum = Integer.parseInt(pd.getString("FNum"));
		int FNum =1;

		PageData matParam = new PageData();
		matParam.put("WH_LOCATION_ID", WH_LOCATION_ID);
		pd = wh_locationService.findById(matParam);
		List<PageData> varList = new ArrayList<PageData>();
			String encode = pd.getString("WH_STORAGEAREA_NAME") + ",YL," + pd.getString("WH_STORAGEAREA_CODE")+ ",YL," + pd.getString("FNAME")+ ",YL," + pd.getString("FCODE") ;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);
				pdMx.put("FNAME", pd.get("FNAME"));
				pdMx.put("FCODE", pd.get("FCODE"));
				pdMx.put("WH_STORAGEAREA_NAME", pd.get("WH_STORAGEAREA_NAME"));
				pdMx.put("WH_STORAGEAREA_CODE", pd.get("WH_STORAGEAREA_CODE"));
				pdMx.put("MakeTime", Tools.date2Str(new Date()));
				varList.add(pdMx);
			}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "库位标签制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", WH_LOCATION_ID);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 容器打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printVessel")
	@ResponseBody
	public Object printVessel() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 容器id
		String STORAGE_UNIT_ID = pd.getString("STORAGE_UNIT_ID");
		// 份数
		//int FNum = Integer.parseInt(pd.getString("FNum"));
		int FNum =1;

		PageData matParam = new PageData();
		matParam.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
		pd = storage_unitService.findById(matParam);
		PageData pdCode = new PageData();
		List<PageData> varList = new ArrayList<PageData>();
			String encode = pd.getString("FCODE") ;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);
				pdMx.put("FNAME", pd.get("FNAME"));
				pdMx.put("FCODE", pd.get("FCODE"));
				pdMx.put("MakeTime", Tools.date2Str(new Date()));
				varList.add(pdMx);
			}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "容器标签制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", STORAGE_UNIT_ID);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 样品条码打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printSample")
	@ResponseBody
	public Object printSample() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		String time = Tools.date2Str(new Date());
		PageData pd = new PageData();
		pd = this.getPageData();
		// 份数
		int FNum = Integer.parseInt(pd.getString("FNum"));
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 1; i <= FNum; i++) {
			pd.put("Ftype", "样品码");
			PageData pdCode = MAKECODEService.getCode(pd);
			BigDecimal FCode = new BigDecimal(pdCode.get("FCode").toString());
			FCode = FCode.add(new BigDecimal(1));
			if(FCode.compareTo(new BigDecimal(99999))==1) {
				FCode = new BigDecimal(10000);
			}
			pd.put("FCode", FCode);
			pd.put("FCreatetime", time);
			pd.put("FCreator", staffid);
			MAKECODEService.editCode(pd);
			String code = "样品" + System.currentTimeMillis() + FCode ;
			String encode = generateQRCodeImage(code).getString("encode");
			PageData pdMx = new PageData();
			pdMx.put("Encode", encode);
			pdMx.put("FCODE", code);
			pdMx.put("FOrder", i);
			pdMx.put("MakeTime", time);
			varList.add(pdMx);
		}
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 原料打印
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/printRaw")
	@ResponseBody
	public Object printRaw() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 物料id
		String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
		// 份数
		int FNum = Integer.parseInt(pd.getString("FNum"));
		// 物料数量
		String MAT_SPECS_QTY = pd.getString("MAT_SPECS_QTY");
		// 辅助属性
		String SPropKey = pd.getString("SPropKey");
		// 锅次号
		String Wok_NUM = pd.getString("Wok_NUM");
		PageData matParam = new PageData();
		matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
		pd = MAT_BASICService.findById(matParam);
		String Ftype = "类型码";
		if ("是".equals(pd.getString("UNIQUE_CODE_WHETHER"))) {
			Ftype = "唯一码";
		}
		pd.put("Ftype", Ftype);
		PageData pdCode = new PageData();
		List<PageData> varList = new ArrayList<PageData>();
		BigDecimal FCode = new BigDecimal(0);

		if ("类型码".equals(Ftype)) {
			pdCode = MAKECODEService.getCode(pd);
			String encode = "L,YL," + pd.getString("MAT_CODE") + ",YL," + MAT_SPECS_QTY + ",YL," + SPropKey;
			String code = generateQRCodeImage(encode).getString("encode");
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				pdMx.put("Encode", code);
				pdMx.put("FOrder", i);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", MAT_SPECS_QTY + MAT_MAIN_UNIT);
				pdMx.put("Wok_NUM", Wok_NUM);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKey);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				varList.add(pdMx);
			}
		} else if ("唯一码".equals(Ftype)) {
			pdCode = MAKECODEService.getCode(pd);
			FCode = new BigDecimal(pdCode.get("FCode").toString());
			for (int i = 1; i <= FNum; i++) {
				PageData pdMx = new PageData();
				FCode = FCode.add(new BigDecimal(1));
				String encode = "W,YL," + FCode + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL," + SPropKey;
				pdMx.put("Encode", generateQRCodeImage(encode).getString("encode"));
				pdMx.put("FOrder", i);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);
				pdMx.put("Wok_NUM", Wok_NUM);
				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKey);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				pdMx.put("SerialNum", FCode);
				varList.add(pdMx);
			}
			pd.put("FCode", FCode);
			MAKECODEService.editCode(pd);
		}

		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FCreatetime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FCreator", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "制码");// 功能项
		pdOp.put("OperationType", "保存");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", Ftype);
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
}
