package com.tkpm.wrapper;

import java.util.Set;

import com.tkpm.entities.Flight;
import com.tkpm.entities.Ticket;

public class FlightStatisticWrapper {

	private Flight flight;
	private Integer numberOfEmptySeat;
	
	public FlightStatisticWrapper(Flight flight) {
		this.flight = flight;
		update();
	}

	// Getter and Setter
	public Flight getFlight() {return flight;}
	public void setFlights(Flight flight) {
		this.flight = flight;
		update();
	}
	
	//Update the numberOfEmptySeat after set the flight
	private FlightStatisticWrapper update() {
			
		numberOfEmptySeat = 0;
		Set<Ticket> tickets = flight.getTickets();
			
		for (Ticket ticket: tickets) {
			if (!ticket.getIsBooked()) {
				++numberOfEmptySeat;
			}
		}
		
		return this;
	}

	// Utility methods
	public Integer getNumberOfEmptySeat() {
		return numberOfEmptySeat;
	}
	public Integer getTotalNumberOfSeat() {
		return flight.getTickets().size();
	}
	public Integer getNumberOfBookedSeat() {
		return  getTotalNumberOfSeat() - getNumberOfEmptySeat();
	}
	
//	public Double getRatio() {
//		return (double)(getNumberOfBookedSeat()/getTotalNumberOfSeat());
//	}
	
	public Integer getTurnover() {
		int turnover = 0;
		
		Set<Ticket> tickets = flight.getTickets();
		
		//Iterate over all the ticket to get the booked tickets
		for (Ticket ticket: tickets) {
			if (ticket.getIsBooked()) {
				turnover += ticket.getPrice();
			}
		}
		
		return turnover;
	}
}
