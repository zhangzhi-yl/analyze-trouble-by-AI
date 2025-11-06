package org.yy.entity;

import java.util.Date;

public class StationTime {
	private Date time;
	private String station;
	private String applicableDate;
	
	
	public String getApplicableDate() {
		return applicableDate;
	}
	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	@Override
	public String toString() {
		return "StationTime [time=" + time + ", station=" + station + ", applicableDate=" + applicableDate + "]";
	}
	
}
