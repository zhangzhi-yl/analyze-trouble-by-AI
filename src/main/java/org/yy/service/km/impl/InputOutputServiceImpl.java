package org.yy.service.km.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.InputOutputMapper;
import org.yy.mapper.dsno1.km.ProductionBOMMapper;
import org.yy.service.km.InputOutputService;
import org.yy.util.Tools;

/**
 * 说明： 投入产出接口实现类 作者：YuanYes Q356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class InputOutputServiceImpl implements InputOutputService {

	@Autowired
	private InputOutputMapper InputOutputMapper;
	@Autowired
	private ProductionBOMMapper ProductionBOMMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		InputOutputMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		InputOutputMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		InputOutputMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return InputOutputMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return InputOutputMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return InputOutputMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		InputOutputMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 生成序号
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getSerialNum(PageData pd) throws Exception {
		return InputOutputMapper.getSerialNum(pd);
	}

	/**
	 * 列表(根据 工艺工序实例ID获取)
	 */
	public List<PageData> getInputOutputListByWorkingProcedureExample_ID(String WorkingProcedureExample_ID)
			throws Exception {
		return InputOutputMapper.getInputOutputListByWorkingProcedureExample_ID(WorkingProcedureExample_ID);
	}

	/**
	 * 列表(根据 工BOM_ID获取)
	 */
	public List<PageData> getInputOutputListByBOM_ID(String BOM_ID) throws Exception {
		return InputOutputMapper.getInputOutputListByBOM_ID(BOM_ID);
	}

	/**
	 * 根据bomid 和 要生产的数量 获取 投入和产出的列表
	 * 
	 * @param BOM_ID
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public List<PageData> calculateInputOutputListByBomIdAndCount(String BOM_ID, Double count) throws Exception {
		List<PageData> dataList = InputOutputMapper.getInputOutputListByBOM_ID(BOM_ID);
		PageData pageData = new PageData();
		pageData.put("ProductionBOM_ID", BOM_ID);
		PageData findById = ProductionBOMMapper.findById(pageData);
		Double FcountDouble = Double.valueOf(findById.get("FCount").toString());
		Double CountDouble = new Double(count);

		BigDecimal b1 = new BigDecimal(FcountDouble.toString());
		BigDecimal b2 = new BigDecimal(CountDouble.toString());

		BigDecimal rate = b2.divide(b1, 8, BigDecimal.ROUND_HALF_UP);

		for (PageData result : dataList) {
			String DemandCount = String.valueOf(result.get("DemandCount"));
			if (Tools.notEmpty(DemandCount)) {
				BigDecimal multiply = rate.multiply(new BigDecimal(DemandCount));
				DecimalFormat decimalFormat = new DecimalFormat("#.00");
				result.put("DemandCount", decimalFormat.format(multiply));
			} else {
				result.put("DemandCount", 0.00);
			}
		}
		return dataList;
	}

	/**
	 * 行关闭/反行关闭
	 * 
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		InputOutputMapper.rowClose(pd);
	}

	/**
	 * 获取工序产出物料数量
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getOutNum(PageData pd) throws Exception {
		return InputOutputMapper.getOutNum(pd);
	}

	/**
	 * 关联行关闭投入产出明细
	 * 
	 * @param pd
	 */
	@Override
	public void deleteMxRelated(PageData pd) throws Exception {
		InputOutputMapper.deleteMxRelated(pd);
	}

	/**
	 * 删除BOM关联行关闭
	 * 
	 * @param pd
	 */
	@Override
	public void rowCloseByBomId(PageData pd) throws Exception {
		InputOutputMapper.rowCloseByBomId(pd);
	}

	@Override
	public List<PageData> listAll1(PageData pd111) {
		return InputOutputMapper.listAll1(pd111);
	}

}
