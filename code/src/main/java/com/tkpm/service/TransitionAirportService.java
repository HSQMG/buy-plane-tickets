package com.tkpm.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.tkpm.dao.TransitionDAO;
import com.tkpm.entities.Flight;
import com.tkpm.entities.Transition;

//Using enum for applying Singleton Pattern
public enum TransitionAirportService {

	INSTANCE;
	
	private TransitionDAO transitionDAO;
	
	private TransitionAirportService() {
		transitionDAO = TransitionDAO.INSTANCE;
	}
	
	//Create new transition airport
	public Transition createTransition(Transition transitionAirport) {
		return transitionDAO.create(transitionAirport);
	}
	
	//Update an transition airport
	public Transition updateTransition(Transition transitionAirport) {
		return transitionDAO.update(transitionAirport);
	}
	
	//Delete an transition airport by the given id
	public int deleteTransition(Integer id) {
		return transitionDAO.delete(id);
	}
	
	//Find all transition airports in database
	public Set<Transition> findAllTransitions() {
		
		//Using set, because query in DAO only return list
		return new TreeSet<>(transitionDAO.findAll());	
	}
	
	//Find transition airport by id
	public Transition findTransitionById(Integer id) {
		return transitionDAO.find(id);
	}

	public List<Transition> findTransitionForFlight(Flight flight) {
		return transitionDAO.find(flight);
	}
	
	public List<Transition> createTransitions(List<Transition> transitions) {
		return transitionDAO.create(transitions);
	}
	
	public List<Transition> createOrUpdateTransitions(List<Transition> transitions) {
		return transitionDAO.createOrUpdate(transitions);
	}

	public int deleteTransitions(List<Integer> ids) {
		return transitionDAO.delete(ids);
	}

	//ids: id of the AIRPORT, not the transition
	public int removeAirportFieldWithGivenIds(List<Integer> ids) {
		return transitionDAO.setAirportNull(ids);
	}
}
 