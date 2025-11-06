package org.yy.mapper.dsno1.run;

import java.util.List;

import org.yy.entity.PageData;

public interface RUN_TASKMapper {

	List<PageData> findStartPH(PageData pd);

	List<PageData> openSubPlan(PageData pd);

}
