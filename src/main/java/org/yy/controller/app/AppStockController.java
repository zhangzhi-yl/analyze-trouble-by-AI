package org.yy.controller.app;

import java.math.BigDecimal;
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
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 说明：库存查询 作者： QQ356703572 时间：2020-11-13 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appStock")
public class AppStockController extends BaseController {

	@Autowired
	private StockService StockService;

	@Autowired
	private OperationRecordService operationrecordService;// 操作记录

	@Autowired
	private StaffService staffService;// 员工

	@Autowired
	private StockOperationRecordService stockOperationRecordService;// 库存操作记录

	/**
	 * 库存查询
	 * 
	 * @param pd
	 *            ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewList")
	@ResponseBody
	public Object viewList(AppPage page, HttpServletResponse response) throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String ListType = pd.get("ListType").toString();
			if (ListType == "1" || ListType.equals("1")) {// 按仓库搜索
				pd.put("Warehouse", pd.get("KEYWORDS"));
			} else if (ListType == "2" || ListType.equals("2")) {// 按仓位搜索
				pd.put("Position", pd.get("KEYWORDS"));
			} else {// 按物料搜索
				pd.put("MAT_NAME", pd.get("KEYWORDS"));
			}

			List<PageData> varList = Lists.newArrayList();
			String orderby = "t.FCreateTime";// 创建时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}

			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(StockService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 获取仓库id和物料id和辅助属性值ID下的库存数量
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSum")
	@ResponseBody
	public Object getSum() throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData stockpd = StockService.getSum(pd);
			BigDecimal stocknum = new BigDecimal("0");// 库存数量
			if (null != stockpd && stockpd.containsKey("stockSum") && null != stockpd.get("stockSum")) {
				stocknum = new BigDecimal(stockpd.get("stockSum").toString());
			}
			return AppResult.success(stocknum, "获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

}
