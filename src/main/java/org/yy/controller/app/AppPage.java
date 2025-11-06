package org.yy.controller.app;

import org.yy.entity.PageData;

public class AppPage {
	private int showCount = 10; 				//每页显示记录数
	private int totalPage;				//总页数
	private long totalResult;			//总记录数
	private int currentPage = 1;			//当前页
	private PageData pd = new PageData();
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public long getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(long totalResult) {
		this.totalResult = totalResult;
	}
	public int getCurrentPage() {
		if(currentPage<=0)
			currentPage = 1;
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public PageData getPd() {
		return pd;
	}
	public void setPd(PageData pd) {
		this.pd = pd;
	}
	
}
