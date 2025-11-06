package org.yy.controller.app;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.STORAGE_BILLService;
import org.yy.service.mom.STORAGE_UNITService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

/**
 * 说明：容器管理 作者： QQ356703572 时间：2020-11-13 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appStorage_unit")
public class AppSTORAGE_UNITController extends BaseController {
	@Autowired
	private StockService stockService;

	@Autowired
	private STORAGE_BILLService storage_billService;

	@Autowired
	private STORAGE_UNITService storage_unitService;
	@Autowired
	private StaffService staffService;

	/**
	 * 容器扫码校验
	 */
	@RequestMapping(value = "goVesselVerify")
	@ResponseBody
	public Object goVesselVerify(HttpServletResponse response) throws Exception {
		try {
			PageData pd = new PageData();
			PageData numpd = new PageData();
			pd = this.getPageData();
			numpd = storage_unitService.getVesselNum(pd);// 通过单号获取数量
			if (Integer.parseInt(numpd.get("NUM").toString()) > 0) {// 判断数量是否大于0，大于0存在数据进行获取
				pd = storage_unitService.getVesselVerify(pd);// 通过单号获取数据
			} else {
				return AppResult.success(pd, "操作失败", "errInfo");
			}
			return AppResult.success(pd, "操作成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 容器扫码校验
	 */
	@RequestMapping(value = "goScanIn")
	@ResponseBody
	public Object goScanIn(HttpServletResponse response) throws Exception {
		try {
			String errInfo = "errInfo";
			String msg = "保存失败!请检查条码是否正确";
			PageData pd = new PageData();
			pd = this.getPageData();
			try {
				// 获取人员账号，并根据账号查询名称
				PageData pdl = new PageData();
				String USERNAME = pd.getString("UserName");
				pdl.put("USERNAME", USERNAME);
				PageData staff = staffService.findById(pdl);
				String STORAGE_UNIT_ID = pd.getString("STORAGE_UNIT_ID");
				String FCODE = pd.getString("FCODE");

				String oneThingsCode = "";// 唯一码
				String qrCode = "";// 类型码

				Double count = 0.0;// 数量
				String gcCode = "";// 锅次
				String fzCode = "";// 辅助属性
				PageData pdVessel = new PageData();// 容器主表信息
				pdVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
				pdVessel = storage_unitService.findById(pdVessel);// 查询容器主表信息
				PageData DetailsItempd = new PageData();

				// 唯一码 带出 物料码 和 序列号
				if ("W".equals(FCODE.substring(0, 1))) {
					// 根据唯一码去库存中查找物料叫啥
					String[] split = FCODE.split(",YL,");
					if (split.length > 1) {
						List<String> codeSplitList = Lists.newArrayList(split);
						oneThingsCode = codeSplitList.get(1);
						count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
						fzCode = String.valueOf(codeSplitList.get(3));
						if (split.length == 5) {
							gcCode = codeSplitList.get(4);
						}
						PageData pData = new PageData();
						// 一物码
						pData.put("OneThingCode", oneThingsCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if (null != DetailsItempd) {
							
							String MAT_CODE = DetailsItempd.getString("MAT_CODE");// 物料代码
							String MAT_UNIT = DetailsItempd.getString("MAT_UNIT");// 物料单位
							String ItemID = DetailsItempd.getString("ItemID");// 物料ID
							if (null != pdVessel) {
								if ("单一".equals(pdVessel.getString("BLEND_TYPE"))
										&& ItemID.equals(pdVessel.getString("MAT_CODE"))) {
									Double ACTUAL_QTY = 0.0;
									try {
										ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
									} catch (Exception e) {
										ACTUAL_QTY = 0.0;
									}
									Double STOCK_QTY_H = Double.parseDouble(pdVessel.get("STOCK_QTY_H").toString());
									Double ACTUAL_AMOUNT = count + ACTUAL_QTY;
									if (ACTUAL_AMOUNT <= STOCK_QTY_H) {
										PageData billPd = new PageData();
										billPd.put("STORAGE_BILL_ID", this.get32UUID()); // 主键
										billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);// 主表主键
										billPd.put("OPERATOR", staff.getString("NAME"));// 操作人
										billPd.put("FTIME", Tools.date2Str(new Date()));// 操作时间
										billPd.put("MAT_CODE", MAT_CODE);// 物料代码
										billPd.put("STORAGE_AMOUNT", count);// 数量
										billPd.put("INPUT_OUT_TYPE", "input");// 进出类型
										billPd.put("SUBLOT_STATE", "锁定");// 状态
										billPd.put("ONLY_CODE", "W");// 一物一码
										billPd.put("MAT_AUXILIARY", fzCode);// 辅助属性
										billPd.put("POT_NUMBER", gcCode);// 锅次
										billPd.put("STORAGE_UNIT", MAT_UNIT);// 单位
										storage_billService.save(billPd);
										PageData pdEditVessel = new PageData();
										pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
										pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);
										storage_unitService.editQty(pdEditVessel);// 更新容器实际数量
										msg = "保存成功！";
										errInfo = "success";

									} else {
										msg = "超出容器可存储上限！";
									}
								}
								if ("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID()); // 主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);// 主表主键
									billPd.put("OPERATOR", staff.getString("NAME"));// 操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));// 操作时间
									billPd.put("MAT_CODE", MAT_CODE);// 物料代码
									billPd.put("STORAGE_AMOUNT", count);// 数量
									billPd.put("INPUT_OUT_TYPE", "input");// 进出类型
									billPd.put("SUBLOT_STATE", "锁定");// 状态
									billPd.put("ONLY_CODE", "W");// 一物一码
									billPd.put("MAT_AUXILIARY", fzCode);// 辅助属性
									billPd.put("POT_NUMBER", gcCode);// 锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);// 单位
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
						if (split.length == 5) {
							gcCode = codeSplitList.get(4);
						}
						PageData pData = new PageData();
						// 类型码
						pData.put("QRCode", qrCode);
						DetailsItempd = stockService.getDetailsItem(pData);
						if (null != DetailsItempd) {
							
							String MAT_CODE = DetailsItempd.getString("MAT_CODE");// 物料代码
							String MAT_UNIT = DetailsItempd.getString("MAT_UNIT");// 物料单位
							String ItemID = DetailsItempd.getString("ItemID");// 物料ID
							if (null != pdVessel) {
								if ("单一".equals(pdVessel.getString("BLEND_TYPE"))
										&& ItemID.equals(pdVessel.getString("MAT_CODE"))) {
									Double ACTUAL_QTY = 0.0;
									try {
										ACTUAL_QTY = Double.parseDouble(pdVessel.get("ACTUAL_AMOUNT").toString());
									} catch (Exception e) {
										ACTUAL_QTY = 0.0;
									}
									Double STOCK_QTY_H = Double.parseDouble(pdVessel.get("STOCK_QTY_H").toString());
									Double ACTUAL_AMOUNT = count + ACTUAL_QTY;
									if (ACTUAL_AMOUNT <= STOCK_QTY_H) {
										PageData billPd = new PageData();
										billPd.put("STORAGE_BILL_ID", this.get32UUID()); // 主键
										billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);// 主表主键
										billPd.put("OPERATOR", staff.getString("NAME"));// 操作人
										billPd.put("FTIME", Tools.date2Str(new Date()));// 操作时间
										billPd.put("MAT_CODE", MAT_CODE);// 物料代码
										billPd.put("STORAGE_AMOUNT", count);// 数量
										billPd.put("INPUT_OUT_TYPE", "input");// 进出类型
										billPd.put("SUBLOT_STATE", "锁定");// 状态
										billPd.put("ONLY_CODE", "L");// 一物一码
										billPd.put("MAT_AUXILIARY", fzCode);// 辅助属性
										billPd.put("POT_NUMBER", gcCode);// 锅次
										billPd.put("STORAGE_UNIT", MAT_UNIT);// 单位
										storage_billService.save(billPd);
										PageData pdEditVessel = new PageData();
										pdEditVessel.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);
										pdEditVessel.put("ACTUAL_AMOUNT", ACTUAL_AMOUNT);
										storage_unitService.editQty(pdEditVessel);// 更新容器实际数量
										msg = "保存成功！";
										errInfo = "success";

									} else {
										msg = "超出容器可存储上限！";
									}
								}
								if ("混合".equals(pdVessel.getString("BLEND_TYPE"))) {
									PageData billPd = new PageData();
									billPd.put("STORAGE_BILL_ID", this.get32UUID()); // 主键
									billPd.put("STORAGE_UNIT_ID", STORAGE_UNIT_ID);// 主表主键
									billPd.put("OPERATOR", staff.getString("NAME"));// 操作人
									billPd.put("FTIME", Tools.date2Str(new Date()));// 操作时间
									billPd.put("MAT_CODE", MAT_CODE);// 物料代码
									billPd.put("STORAGE_AMOUNT", count);// 数量
									billPd.put("INPUT_OUT_TYPE", "input");// 进出类型
									billPd.put("SUBLOT_STATE", "锁定");// 状态
									billPd.put("ONLY_CODE", "L");// 一物一码
									billPd.put("MAT_AUXILIARY", fzCode);// 辅助属性
									billPd.put("POT_NUMBER", gcCode);// 锅次
									billPd.put("STORAGE_UNIT", MAT_UNIT);// 单位
									storage_billService.save(billPd);
									msg = "保存成功！";
									errInfo = "success";
								}
							}

						}
					}
				}
			} catch (Exception e) {
				errInfo = "errInfo";
				msg = "保存失败!请检查条码是否正确";
			}
			return AppResult.success(pd, msg, errInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

}
