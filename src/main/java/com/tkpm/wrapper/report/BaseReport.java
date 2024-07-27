package com.tkpm.wrapper.report;

import java.time.Month;
import java.time.Year;
import java.util.List;

import com.tkpm.wrapper.FlightStatisticWrapper;

public class BaseReport {

	private List<FlightStatisticWrapper> wrappers;
	private Month month;	//year report set this field to null
	private Integer year;
	
	public BaseReport(List<FlightStatisticWrapper> wrappers) {
		this.setWrappers(wrappers);
	}

	//Getters
	public List<FlightStatisticWrapper> getWrappers() {return wrappers;}
	public Month getMonth() {return month;}
	public Integer getYear() {return year;}

	//Setters
	public BaseReport setWrappers(List<FlightStatisticWrapper> wrappers) {this.wrappers = wrappers; return this;}
	public BaseReport setMonth(Month month) {this.month = month; return this;}
	public BaseReport setYear(Integer year) {this.year = year; return this;}

	
	public Integer getTurnover() {
		int turnover = 0;
		
		for (FlightStatisticWrapper wrapper: wrappers) {
			turnover += wrapper.getTurnover();
		}
		
		return turnover;
	}
}
