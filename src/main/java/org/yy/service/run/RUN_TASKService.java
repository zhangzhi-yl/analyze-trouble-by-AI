package org.yy.service.run;

import java.util.List;

import org.yy.entity.PageData;

public interface RUN_TASKService {
	List<PageData> findStartPH(PageData pd);

	List<PageData> openSubPlan(PageData pd);

}
