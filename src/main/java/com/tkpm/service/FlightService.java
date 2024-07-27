package com.tkpm.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.tkpm.dao.FlightDAO;
import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.TicketClass;
import com.tkpm.entities.Transition;

//Using enum for applying Singleton Pattern
public enum FlightService {

	INSTANCE;
	
	private FlightDAO flightDAO;
	private TransitionAirportService transitionService;
	private TicketClassService ticketClassService;
	private TicketService ticketService;
	
	private FlightService() {
		flightDAO = FlightDAO.INSTANCE;
		transitionService = TransitionAirportService.INSTANCE;
		ticketClassService = TicketClassService.INSTANCE;
		ticketService = TicketService.INSTANCE;
	}
	
	//Create new flight
	public Flight createFlight(Flight flight) {
		
		//Get transitions from flight
		List<Transition> transitions = flight.getTransitions();
		
		//Emptinize the transitions
		flight.setTransitions(null);
		
		//Create the flight first
		flight = flightDAO.create(flight);
		
		//Create the transition if the transitions are not empty
		if (null != transitions && !transitions.isEmpty()) {
			
			//Set the flight for thoose transition
			for (Transition trans: transitions) {
				trans.setFlight(flight);
			}
			
			transitions = transitionService.createTransitions(transitions);
			flight.setTransitions(transitions);
		}
		
		//Create the tickets base on flight information
		FlightDetail detail = flight.getDetail();
		
		int firstClassSeatSize = detail.getNumberOfFirstClassSeat();
		int secondClassSeatSize = detail.getNumberOfSecondClassSeat();
		
		//Create first class ticket
		if (firstClassSeatSize > 0) {
			TicketClass class1 = ticketClassService.findTicketClassByName("1");
			Ticket ticket = new Ticket();
			ticket.setFlight(flight);
			ticket.setTicketClass(class1);
			ticket.setIsBooked(false);
			ticket.setPrice(detail.getPriceOfFirstClassSeat());
			
			//Using linked list for faster insertion
			List<Ticket> tickets = new LinkedList<>();
			for (int i = 0; i < firstClassSeatSize; ++i) {
				tickets.add(new Ticket(ticket));
			}
			
			ticketService.createTickets(tickets);
		}
		
		//Create second class ticket
		if (secondClassSeatSize > 0) {
			TicketClass class2 = ticketClassService.findTicketClassByName("2");
			Ticket ticket = new Ticket();
			ticket.setFlight(flight);
			ticket.setTicketClass(class2);
			ticket.setIsBooked(false);
			ticket.setPrice(detail.getPriceOfSecondClassSeat());
			
			//Using linked list for faster insertion
			List<Ticket> tickets = new LinkedList<>();
			for (int i = 0; i < secondClassSeatSize; ++i) {
				tickets.add(new Ticket(ticket));
			}
			
			ticketService.createTickets(tickets);
		}
		
		return flight;
	}
	
	//Update an flight
	public Flight updateFlight(Flight flight) {
		
		//Update the transitions first
		List<Transition> transitions = flight.getTransitions();
		
		for (Transition transition: transitions) {
			transition.setFlight(flight);
		}
		transitions = transitionService.createOrUpdateTransitions(transitions);
		
		//Delete the old tickets
		Set<Ticket> deleteTickets = ticketService.findTicketFromFlight(flight);
		List<Integer> deleteIds = deleteTickets
				.stream()
				.map(ticket -> ticket.getId())
				.collect(Collectors.toList());		
		ticketService.deleteTickets(deleteIds);
		
		//Create the tickets base on flight information
		FlightDetail detail = flight.getDetail();		
		int firstClassSeatSize = detail.getNumberOfFirstClassSeat();
		int secondClassSeatSize = detail.getNumberOfSecondClassSeat();
		
		//Create first class ticket
		if (firstClassSeatSize > 0) {
			TicketClass class1 = ticketClassService.findTicketClassByName("1");
			Ticket ticket = new Ticket();
			ticket.setFlight(flight);
			ticket.setTicketClass(class1);
			ticket.setIsBooked(false);
			ticket.setPrice(detail.getPriceOfFirstClassSeat());
					
			//Using linked list for faster insertion
			List<Ticket> tickets = new LinkedList<>();
			for (int i = 0; i < firstClassSeatSize; ++i) {
				tickets.add(new Ticket(ticket));
			}			
			ticketService.createTickets(tickets);
		}
				
		//Create second class ticket
		if (secondClassSeatSize > 0) {
			TicketClass class2 = ticketClassService.findTicketClassByName("2");
			Ticket ticket = new Ticket();
			ticket.setFlight(flight);
			ticket.setTicketClass(class2);
			ticket.setIsBooked(false);
			ticket.setPrice(detail.getPriceOfSecondClassSeat());
					
			//Using linked list for faster insertion
			List<Ticket> tickets = new LinkedList<>();
			for (int i = 0; i < secondClassSeatSize; ++i) {
				tickets.add(new Ticket(ticket));
			}
			ticketService.createTickets(tickets);
		}
		
		return flightDAO.update(flight);
	}
	
	//Delete an flight by the given id
	public int deleteFlight(Integer id) {
		return flightDAO.delete(id);
	}
	
	//Find all flights in database
	public Set<Flight> findAllFlights() {
		
		//Using set, because query in DAO only return list
		return new TreeSet<>(flightDAO.findAll());
		
	}
	
	public List<Flight> findFlightByCriterias(
			Airport departureAirport,
			Airport arrivalAirport,
			//LocalDateTime datetime,
			LocalDate startDate,
			LocalDate endDate) {
		
		Set<Flight> flights = findAllFlights(); 
		
		if (null != departureAirport) {
			flights = flights
					.stream()
					.filter(flight -> departureAirport.equals(flight.getDepartureAirport()))
					.collect(Collectors.toSet());
		}
		
		if (null != arrivalAirport) {
			flights = flights
					.stream()
					.filter(flight -> arrivalAirport.equals(flight.getArrivalAirport()))
					.collect(Collectors.toSet());
		}
		
//		if (null != datetime) {
//			flights = flights
//					.stream()
//					.filter(flight -> flight.getDateTime().equals(datetime))
//					.collect(Collectors.toSet());
//		}
//		
		if (null != startDate && null != endDate) {

			flights = flights
					.stream()
					.filter(flight -> 
									(flight.getDateTime().toLocalDate().isAfter(startDate) ||
											flight.getDateTime().toLocalDate().isEqual(startDate)) &&
									(flight.getDateTime().toLocalDate().isBefore(endDate) || 
											flight.getDateTime().toLocalDate().isEqual(endDate)))
					.collect(Collectors.toSet());
			
		} else if (null != startDate) {
			
			flights = flights
					.stream()
					.filter(flight -> 
								flight.getDateTime().toLocalDate().isAfter(startDate) ||
								flight.getDateTime().toLocalDate().isEqual(startDate))
					.collect(Collectors.toSet());
			
		} else if (null != endDate) {
			
			flights = flights
					.stream()
					.filter(flight -> 
								flight.getDateTime().toLocalDate().isBefore(endDate) || 
								flight.getDateTime().toLocalDate().isEqual(endDate))
					.collect(Collectors.toSet());
			
		}
		
		return new ArrayList<>(flights);
	}
	
	//Find flight by id
	public Flight findFlightById(Integer id) {
		return flightDAO.find(id);
	}

	public FlightDetail findFlightDetailByFlight(Flight flight) {
		return flightDAO.findDetailById(flight.getId());
	}

	public int deleteFlights(List<Integer> ids) {
		return flightDAO.delete(ids); 
	}
	
	public List<Flight> getFlightForReport(List<Integer> ids) {
		return flightDAO.findForReport(ids);
	}

	//ids: id of the AIRPORT, not the transition
	public int removeAirportFieldWithGivenIds(List<Integer> ids) {
		return flightDAO.setAirportNull(ids);
	}
}
 