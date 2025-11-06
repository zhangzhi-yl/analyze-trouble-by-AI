package org.yy.service.fhdb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhdb.EarlyWarningMapper;
import org.yy.service.fhdb.EarlyWarningService;
@Service
@Transactional //开启事物
public class EarlyWarningServiceImpl implements EarlyWarningService {

	@Autowired
	private EarlyWarningMapper earlyWarningMapper;

	@Override
	public List<PageData> getOverduedCabinetList(PageData pd) {

		return earlyWarningMapper.getOverduedCabinetList(pd);
	}

	@Override
	public List<PageData> getWillOverdueCabinetList(PageData pd) {

		return earlyWarningMapper.getWillOverdueCabinetList(pd);
	}

	@Override
	public List<PageData> getOverduePurchaseList(PageData pd) {

		return earlyWarningMapper.getOverduePurchaseList(pd);
	}

	@Override
	public List<PageData> getWillOverduePurchaseList(PageData pd) {

		return earlyWarningMapper.getWillOverduePurchaseList(pd);
	}

	@Override
	public List<PageData> getLoadedButNoBOMOrDrawingList(PageData pd) {

		return earlyWarningMapper.getLoadedButNoBOMOrDrawingList(pd);
	}

}
