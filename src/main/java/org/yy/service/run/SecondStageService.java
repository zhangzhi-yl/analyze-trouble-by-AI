package org.yy.service.run;

import java.util.List;

import org.yy.entity.PageData;

public interface SecondStageService {

	public PageData getStartNodeId(PageData pd);

	public List<PageData> findCount(PageData pd);

	public void changeState(PageData pd) ;

	public void updateStateUnexecuted(PageData pd);

	public List<PageData> findNextNodes(PageData pd) ;

	public PageData getONodeId(PageData pageData);

	public void changePhState(PageData pageData);
	
	public List<PageData> findStartTask(PageData pd);
	public List<PageData> findStartSubPlan(PageData pd);

	public List<PageData> findStartTaskNoStandard(PageData pageData2);

}
