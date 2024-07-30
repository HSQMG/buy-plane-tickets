package com.tkpm.view.model;

public class ReportInfoModel {

	private Integer year;
	private Integer month;
	
	public ReportInfoModel(Integer year, Integer month) {
		this.year = year;
		this.month = month;
	}

	//Getters
	public Integer getYear() {return year;}
	public Integer getMonth() {return month;}

	//Setters
	public void setYear(Integer year) {this.year = year;}
	public void setMonth(Integer month) {this.month = month;}
	
	
	
}
