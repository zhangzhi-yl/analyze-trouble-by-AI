package org.yy.controller.mm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.StockService;

/**
 * 二维码查询
 * 
 * @author chen
 *
 */
@Controller
@RequestMapping("/QRCodeSearch")
public class QRCodeSearchController extends BaseController {

	@Autowired
	private StockService stockService;
	@Autowired
	private MAT_BASICService MAT_BASICService;

	/**
	 * 二维码查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search")
	@ResponseBody
	public Object search(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		// 根据 二维码 、区域、物料获取数据
		List<PageData> varList = stockService.getMaterialInfo4QrcodeSearch(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 调整二维码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/adjustment")
	@ResponseBody
	public Object adjustment() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
		String MAT_CODE = pd.getString("MAT_CODE");
		List<PageData> listByMatCode = MAT_BASICService.getListByMatCode(MAT_CODE);
		if (!CollectionUtils.isEmpty(listByMatCode)) {
			throw new RuntimeException("该物料二维码已经存在，请修改后提交");
		}
		PageData findPageData = new PageData();
		findPageData.put("MAT_BASIC_ID", MAT_BASIC_ID);
		PageData findById = MAT_BASICService.findById(findPageData);
		findById.put("MAT_CODE", MAT_CODE);
		MAT_BASICService.edit(findById);
		map.put("result", errInfo);
		return map;
	}
}
