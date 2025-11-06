package org.yy.mapper.dsno1.run;

import java.util.List;

import org.yy.entity.PageData;

public interface FirstStageMapper {
	List<PageData> findCount(PageData pd);

	void changeState(PageData pd);

	void updateStateUnexecuted(PageData pd);

	List<PageData> findNextNodes(PageData pd);

	void changeOState(PageData pageData);
	
	void setPwoStatusGoing(PageData pageData);
	
	void setPwoStatusDone(PageData pageData);
}
