package com.tkpm.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight implements Serializable, Comparable<Flight> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -151010987493990509L;

	@OneToOne(
			mappedBy = "flight",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			optional = false)
	private FlightDetail detail;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.DETACH,
					CascadeType.REFRESH},
			fetch = FetchType.EAGER)
	@JoinColumn(
			name = "departure_airport_id", 
			referencedColumnName = "id",
			nullable = true)
	private Airport departureAirport;
	
	@OneToMany(
			cascade = CascadeType.ALL,
			mappedBy = "flight",
			orphanRemoval = true)
	private Set<Ticket> tickets;
	
//	@OneToMany(
//			cascade = CascadeType.ALL,
//			mappedBy = "flight",
//			orphanRemoval = true)
//	private Set<Reservation> reservations;
//	
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.DETACH,
					CascadeType.REFRESH},
			fetch = FetchType.EAGER)
	@JoinColumn(
			name = "arrival_airport_id", 
			referencedColumnName = "id",
			nullable = true)
	private Airport arrivalAirport;
	
//	@ManyToMany(
//			cascade = {
//					CascadeType.PERSIST,
//					CascadeType.MERGE,
//					CascadeType.DETACH,
//					CascadeType.REFRESH},
//			fetch = FetchType.EAGER)
//	@JoinTable(
//			name = "flight_transition",
//			joinColumns = @JoinColumn(name = "flight_id"),
//			inverseJoinColumns = @JoinColumn(name = "transition_airport_id"))
	@OneToMany(
			cascade = CascadeType.ALL,
			mappedBy = "flight",
			orphanRemoval = true)
	private List<Transition> transitions;
	
	@Column(name = "date_time")
	private LocalDateTime dateTime;
	
	//Constructors
	public Flight() {
		super();
		
	}

	//Getters
	public Integer getId() {return id; }
	public Airport getDepartureAirport() {return departureAirport;}
	public Airport getArrivalAirport() {return arrivalAirport;}
	public List<Transition> getTransitions() {return transitions;}
	public LocalDateTime getDateTime() {return dateTime;}
	public FlightDetail getDetail() {return detail;}
	public Set<Ticket> getTickets() {return tickets;}
//	public Set<Reservation> getReservations() {return reservations;}

	//Setters
	public void setId(Integer id) {this.id = id;}
	public void setDepartureAirport(Airport departureAirport) {this.departureAirport = departureAirport;}
	public void setArrivalAirport(Airport arrivalAirport) {this.arrivalAirport = arrivalAirport;}
	public void setTransitions(List<Transition> transitions) {this.transitions = transitions;}
	public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
	public void setDetail(FlightDetail detail) {this.detail = detail;}
	public void setTickets(Set<Ticket> tickets) {this.tickets = tickets;}
//	public void setReservations(Set<Reservation> reservations) {this.reservations = reservations; }
//	
	//Compare for using Set, by implementing Comparable
	public int compareTo(Flight another) {
		if (null == another) return 1;
		
		int result = 
				(this.getId() > another.getId()?1:
					(this.getId() < another.getId()? -1: 0));
		
		return result;
	}
	
}
