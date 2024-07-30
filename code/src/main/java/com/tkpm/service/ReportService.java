package com.tkpm.service;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tkpm.entities.Flight;
import com.tkpm.wrapper.FlightStatisticWrapper;
import com.tkpm.wrapper.report.BaseReport;

public enum ReportService {
	INSTANCE;
	
	private FlightService flightService;
	
	private ReportService() {
		flightService = FlightService.INSTANCE;
	}
	
	public BaseReport getReportByMonth(Month month, int year) {
		
		BaseReport report = null;
		
		//Filtering the flights by month and year, then get the id for each flight
		Set<Flight> flights = flightService.findAllFlights();
		
		List<Flight> filterFlights = flights
				.stream()
				.filter(flight -> 
					flight.getDateTime().getMonth().equals(month) &&
					flight.getDateTime().getYear() == year)
				.collect(Collectors.toList());
		
		List<Integer> ids = filterFlights
				.stream()
				.map(flight -> flight.getId())
				.collect(Collectors.toList());
		
		//Fetch ticket for each filtered flight
		filterFlights = flightService.getFlightForReport(ids);
		
		//Get the wrappers
		List<FlightStatisticWrapper> wrappers = filterFlights
				.stream()
				.map(flight -> new FlightStatisticWrapper(flight))
				.collect(Collectors.toList());
		
		//Setup report
		report = new BaseReport(wrappers);
		report.setMonth(month)
			.setYear(year);
		
		return report;
	}
	
	public List<BaseReport> getReportByYear(int year) {
		
		List<BaseReport> reports = new LinkedList<>();
		
		
		Set<Flight> flights = flightService.findAllFlights();

		//Filtering the flights by each month in year, then get the wrapper for each flight
		final int NUMBER_OF_MONTH = 12;
		for (int i = 1; i <= NUMBER_OF_MONTH; ++i) {
			Month month = Month.of(i);
			
			List<Flight> filterFlights = flights
					.stream()
					.filter(flight -> 
						flight.getDateTime().getMonth().equals(month) &&
						flight.getDateTime().getYear() == year)
					.collect(Collectors.toList());
			
			List<Integer> ids = filterFlights
					.stream()
					.map(flight -> flight.getId())
					.collect(Collectors.toList());
			
			//Fetch ticket for each filtered flight
			filterFlights = flightService.getFlightForReport(ids);
			
			//Get the wrappers
			List<FlightStatisticWrapper> wrappers = filterFlights
					.stream()
					.map(flight -> new FlightStatisticWrapper(flight))
					.collect(Collectors.toList());
			
			//Setup report
			BaseReport report = new BaseReport(wrappers);
			report
			.setMonth(month)
			.setYear(year);
		
			reports.add(report);
		}
		
		return reports;
	}
}
