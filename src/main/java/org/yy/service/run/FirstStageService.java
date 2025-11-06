package org.yy.service.run;

import java.util.List;

import org.yy.entity.PageData;

public interface FirstStageService {

	public List<PageData> findCount(PageData pd);

	public void changeState(PageData pd);

	public void updateStateUnexecuted(PageData pd);

	public List<PageData> findNextNodes(PageData pd);

	public void changeOState(PageData pageData);
	
	public void setPwoStatusGoing(PageData pageData);
	
	public void setPwoStatusDone(PageData pageData);
}
