package org.yy.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.app.AppService;
import org.yy.service.mdm.EQM_POINT_INSPECTService;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.mom.TEMBILL_EXECUTETICKService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 设备点巡检
 * @author YULONG
 *
 */
@RestController
@RequestMapping("/appEqmPointInspect")
public class AppEqmPointInspectController extends BaseController{
	@Autowired
	private AppService appService;
	@Autowired
	private EQM_POINT_INSPECTService eqm_point_inspectService;
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	@Autowired
	private TEMBILL_EXECUTETICKService tembill_executetickService;
	/**
	 * 设备点巡检
	 */
	@RequestMapping("/eqmPointInspectList")
	public Object eqmPointInspectList(AppPage page) {
		try {
			PageData pd = new PageData();
			List<PageData> varList = Lists.newArrayList();
			pd = this.getPageData();
			// 获取数据
			if (null != pd) {
				String orderby = "CREATE_TIME";
				if (Tools.notEmpty(pd.getString("orderby"))) {
					orderby = pd.getString("orderby");
				}
				String sort = "desc";
				if ("asc".equals(pd.getString("sort"))) {
					sort = "asc";
				}

				page.setPd(pd);
				PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
				PageInfo<PageData> pageInfo = new PageInfo<>(
						eqm_point_inspectService.appEqmPointInspectList(page));
				varList = pageInfo.getList();
				page.setTotalPage(pageInfo.getPages());
				page.setTotalResult(pageInfo.getTotal());
			}
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	 /**去修改页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/findById")
		@ResponseBody
		public Object findById(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				pd = eqm_point_inspectService.findById(pd);	
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		/**
		 * 设备点巡检明细
		 */
		@RequestMapping("/eqmPointInspectListMx")
		public Object eqmPointInspectListMx(AppPage page) {
			try {
				PageData pd = new PageData();
				List<PageData> varList = Lists.newArrayList();
				pd = this.getPageData();
				// 获取数据
				if (null != pd) {
					String orderby = "cast(SORT  as int)";
					if (Tools.notEmpty(pd.getString("orderby"))) {
						orderby = pd.getString("orderby");
					}
					String sort = "asc";
					if ("desc".equals(pd.getString("sort"))) {
						sort = "desc";
					}

					page.setPd(pd);
					PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
					PageInfo<PageData> pageInfo = new PageInfo<>(
							tembill_executemxService.eqmPointInspectListMx(page));
					varList = pageInfo.getList();
					page.setTotalPage(pageInfo.getPages());
					page.setTotalResult(pageInfo.getTotal());
				}
				return AppResult.success(varList, page);
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		 /**完成
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/editStatus")
		@ResponseBody
		public Object editStatus(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
			
				pd = this.getPageData();
				pd.put("FOPERATOR",pd.getString("FOPERATOR"));					//获取当前登录人的中文名称
				pd.put("FIDENTIFIED",pd.getString("FIDENTIFIED"));					//获取当前登录人的中文名称
				
				eqm_point_inspectService.editStatusx(pd);
				return AppResult.success(pd, "完成成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		/**更新反馈内容
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/setFeedback")
		@ResponseBody
		public Object setFeedback() throws Exception{
			PageData pd = new PageData();
			PageData mxPd = new PageData();
			PageData tickPd = new PageData();
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			try {
	        if(null!=pd.getString("TEMBILL_EXECUTEMX_ID")&&!"".equals(pd.getString("TEMBILL_EXECUTEMX_ID"))) {
	        	mxPd.put("TEMBILL_EXECUTEMX_ID", pd.getString("TEMBILL_EXECUTEMX_ID"));
	        	mxPd=tembill_executemxService.findById(mxPd);//查询明细数据
	        	tickPd.put("TEMBILL_EXECUTE_ID", mxPd.getString("TEMBILL_EXECUTE_ID"));//主表id
	        	tickPd.put("FTICK_TIME", Tools.date2Str(new Date()));//反馈时间
	        	tickPd.put("FTICK_PERSON", pd.get("FTICK_PERSON"));//反馈人
	        	tickPd.put("FTICK_CAPTION", mxPd.getString("CAPTION"));//反馈标题
	        	tickPd.put("FTICK_MATTER", pd.getString("BEAR"));//反馈内容
	        	tickPd.put("TEMBILL_EXECUTETICK_ID", this.get32UUID());//主键id
	        	tembill_executetickService.save(tickPd);
			}
			pd.put("FLASTUPDATEPEOPLE",pd.get("FTICK_PERSON"));
			String FLASTUPDATETIME=Tools.date2Str(new Date());
			String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"',"+"FLASTUPDATEPEOPLE='"+pd.getString("FLASTUPDATEPEOPLE")+"',"+"FLASTUPDATETIME='"+FLASTUPDATETIME+"'"+" WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
			//String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"' WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
			pd.put("chatType", chatType);
			tembill_executemxService.setFeedback(pd);
			return AppResult.success(pd, "保存成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}	
}
