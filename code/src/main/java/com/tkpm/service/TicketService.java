package com.tkpm.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.tkpm.dao.TicketDAO;
import com.tkpm.entities.Flight;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.TicketClass;

//Using enum for applying Singleton Pattern
public enum TicketService {

	INSTANCE;
	
	private TicketDAO ticketDAO;
	private FlightService flightService;
	private ReservationService reservationService;
	
	private TicketService() {
		ticketDAO = TicketDAO.INSTANCE;
		flightService = FlightService.INSTANCE;
		reservationService = ReservationService.INSTANCE;
	}
	
	//Create new tickets
	public Set<Ticket> createTickets(List<Ticket> tickets) {
		
		//Then create the according reservation for each ticket
		//Use linkedlist for faster insertion
		List<Reservation> reservations = new LinkedList<>();
		
		for (Ticket ticket: tickets) {
			Reservation reservation = new Reservation();
			reservation.setTicket(ticket);
			ticket.setReservaion(reservation);
			reservations.add(reservation);
		}
		
		
		//Create the ticket first
		List<Ticket> creates = ticketDAO.create(tickets);
		
		//Then create the according reservation for each ticket
		//Use linkedlist for faster insertion

//		int index = 0;
//		for (Reservation res: reservations) {
//			res.setTicket(creates.get(index));
//			++index;
//		}
//		
//		//Create thoose reservations
//		reservationService.createReservations(reservations);
		
		Set<Ticket> result = new TreeSet<>(creates);
		return result;
	}
	
	//Update tickets
	public Set<Ticket> updateTickets(List<Ticket> tickets) {
		return new TreeSet<>(ticketDAO.update(tickets));
	}
	
	public Ticket updateTicket(Ticket ticket) {
		return ticketDAO.update(ticket);
	}
	
	//Delete tickets by the given ids
	public int deleteTickets(List<Integer> ids) {
		
		int errorCode = reservationService.deleteReservations(ids);
		errorCode += ticketDAO.delete(ids);
		
		return errorCode;
	}
	
	//Find all tickets in database
	public Set<Ticket> findAllTickets() {
		
		//Using set, because query in DAO only return list
		return new TreeSet<>(ticketDAO.findAll());
		
	}
	
	//Find tickets by id
	public Ticket findTicketById(Integer id) {
		return ticketDAO.find(id);
	}
	
	//Find all tickets from a flight
	public Set<Ticket> findTicketFromFlight(Flight flight) {
		
		return ticketDAO.find(flight);
	}
	
	//Get all the not-booked tickets with the given ticket class from a flight
	public List<Ticket> findAvailableTicketsFromFlight(Flight flight, TicketClass ticketClass) {
		
		//Get all the tickets from flight
		Set<Ticket> tickets = findTicketFromFlight(flight);
		
		//In all tickets from flight, get all the ticket with the given class and had not been booked yet
		List<Ticket> availableTickets = tickets
				.stream()
				.filter(ticket -> !ticket.getIsBooked() && ticket.getTicketClass().equals(ticketClass))
				.collect(Collectors.toList());
									
		return availableTickets;
	}
	
	public Ticket findAvailableTicketFromFlight(Flight flight, TicketClass ticketClass) {
		
		List<Ticket> tickets = findAvailableTicketsFromFlight(flight, ticketClass);
		if (null == tickets || tickets.isEmpty()) {
			return null;
		}
		
		//Randomize ticket
		Random rng = new Random();
		int index = rng.nextInt(tickets.size());
		Ticket ticket = tickets.get(index);
		
		ticket.setIsBooked(true);
		
		return updateTicket(ticket);
	}
	
	public int cancelTickets(List<Integer> ids) {
		return ticketDAO.cancel(ids);
	}

	public void setReservationService(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
}
 