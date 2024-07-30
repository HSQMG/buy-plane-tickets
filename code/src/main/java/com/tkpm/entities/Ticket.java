package com.tkpm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket implements Serializable, Comparable<Ticket> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4179448035661626991L;

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
			name = "flight_id", 
			referencedColumnName = "id",
			nullable = true)
	private Flight flight;
	
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.DETACH,
					CascadeType.REFRESH},
			fetch = FetchType.EAGER)
	@JoinColumn(
			name = "ticket_class_id", 
			referencedColumnName = "id",
			nullable = true)
	private TicketClass ticketClass;
	
	@OneToOne(
			mappedBy = "ticket",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			optional = false)
	private Reservation reservation;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "identity_code")
	private String identityCode;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "is_booked")
	private Boolean isBooked;
	
	//Constructors
	public Ticket() {
		super();
	}
	
	//Copy constructor
	public Ticket(Ticket other) {
		if (null != other.getId()) {id = other.getId();};
		flight = other.getFlight();
		ticketClass = other.getTicketClass();
		reservation = other.getReservation();
		if (null != other.getPrice()) {price = new Integer(other.getPrice());};
		if (null != other.getName()) {name = new String(other.getName());};
		if (null != other.getIdentityCode()) {identityCode = new String(other.getIdentityCode());};
		if (null != other.getPhoneNumber()) {phoneNumber = new String(other.getPhoneNumber());};
		if (null != other.getIsBooked()) {isBooked = new Boolean(other.getIsBooked());};
	}

	//Getters
	public Integer getId() {return id;}
	public Flight getFlight() {return flight;}
	public TicketClass getTicketClass() {return ticketClass;}
	public Integer getPrice() {return price;}
	public String getName() {return name;}
	public String getIdentityCode() {return identityCode;}
	public String getPhoneNumber() {return phoneNumber;}
	public Reservation getReservation() {return reservation;}
	public Boolean getIsBooked() {return isBooked;}
	 
 	//Setters
	public void setId(Integer id) {this.id = id;}
	public void setFlight(Flight flight) {this.flight = flight;}
	public void setTicketClass(TicketClass ticketClass) {this.ticketClass = ticketClass;}
	public void setPrice(Integer price) {this.price = price;}
	public void setName(String name) {this.name = name;}
	public void setIdentityCode(String identityCode) {this.identityCode = identityCode;}
	public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
	public void setReservaion(Reservation reservation) {this.reservation = reservation; }
	public void setIsBooked(Boolean isBooked) {this.isBooked = isBooked;}
	
	//Compare for using Set, by implementing Comparable
	public int compareTo(Ticket another) {
		if (null == another) return 1;
		
		int result = 
				(this.getId() > another.getId()?1:
					(this.getId() < another.getId()? -1: 0));
		
		return result;
	}

	
}
