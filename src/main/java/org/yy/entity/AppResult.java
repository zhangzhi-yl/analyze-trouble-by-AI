package org.yy.entity;

import java.util.List;

import org.yy.controller.app.AppPage;

public class AppResult {
	private String result;
	private String msg;
	private Object data;
	private AppPage page;

	public AppPage getPage() {
		return page;
	}

	public void setPage(AppPage page) {
		this.page = page;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static AppResult success(Object data) {
		AppResult successResult = new AppResult();
		successResult.setMsg("success");
		successResult.setResult("success");
		successResult.setData(data);
		return successResult;
	}

	public static AppResult success(Object data, String msg) {
		AppResult successResult = new AppResult();
		successResult.setMsg(msg);
		successResult.setResult("success");
		successResult.setData(data);
		return successResult;
	}
	
	public static AppResult success(String result, String msg) {
		AppResult successResult = new AppResult();
		successResult.setMsg(msg);
		successResult.setResult(result);
		return successResult;
	}
	
	public static AppResult success(Object data, String msg, String result) {
		AppResult successResult = new AppResult();
		successResult.setMsg(msg);
		successResult.setResult(result);
		successResult.setData(data);
		return successResult;
	}

	public static AppResult success(List<PageData> data, AppPage page) {
		AppResult successResult = new AppResult();
		successResult.setMsg("success");
		successResult.setResult("success");
		successResult.setData(data);
		successResult.setPage(page);
		return successResult;
	}

	public static AppResult failed(String msg) {
		AppResult failedResult = new AppResult();
		failedResult.setMsg(msg);
		failedResult.setResult("failed");
		return failedResult;
	}

	public static AppResult failed(String msg, String result) {
		AppResult failedResult = new AppResult();
		failedResult.setMsg(msg);
		failedResult.setResult(result);
		return failedResult;
	}

}
