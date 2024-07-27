package com.tkpm.service;

import java.util.List;

import com.tkpm.dao.AirportDAO;
import com.tkpm.entities.Airport;

//Using enum for applying Singleton Pattern
public enum AirportService {

	INSTANCE;

	//Services
	private TransitionAirportService transitionService;
	private FlightService flightService;
	
	//DAOs
	private AirportDAO airportDAO;
	
	private AirportService() {
		airportDAO = AirportDAO.INSTANCE;
		transitionService = TransitionAirportService.INSTANCE;
		flightService = FlightService.INSTANCE;
	}
	
	//Create new airport
	public Airport createAirport(Airport airport) {
		return airportDAO.create(airport);
	}
	
	//Update an airport
	public Airport updateAirport(Airport airport) {
		return airportDAO.update(airport);
	}
	
	//Delete an airport by the given id
	public int deleteAirport(Integer id) {
		return airportDAO.delete(id);
	}
	
	//Find all airports in database
	public List<Airport> findAllAirports() {
		
		return airportDAO.findAll();
		
	}
	
	//Find airport by id
	public Airport findAirportById(Integer id) {
		return airportDAO.find(id);
	}

	public Airport findAirportByName(String name) {
		return airportDAO.find(name);
	}

	public int deleteAirports(List<Integer> ids) {
		
		// Set null for transition airport, which has airport with the same id with the delete airports
		// To avoid delete a referenced foreign key
		transitionService.removeAirportFieldWithGivenIds(ids);
		
		//Set null for transition airport, which has airport with the same id with the delete airports
		flightService.removeAirportFieldWithGivenIds(ids);
		
		return airportDAO.delete(ids);
	}
}
 