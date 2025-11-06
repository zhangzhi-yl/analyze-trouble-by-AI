package org.yy.mapper.dsno1.run;

import java.util.List;

import org.yy.entity.PageData;

public interface SecondStageMapper {
	PageData getStartNodeId(PageData pd);

	List<PageData> findCount(PageData pd);
	List<PageData> findStartTask(PageData pd);
	List<PageData> findStartSubPlan(PageData pd);
	void changeState(PageData pd) ;

	void updateStateUnexecuted(PageData pd);

	List<PageData> findNextNodes(PageData pd) ;

	PageData getONodeId(PageData pageData);

	void changePhState(PageData pageData);

	List<PageData> findStartTaskNoStandard(PageData pageData2);

}
