package com.tkpm.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation implements Serializable, Comparable<Reservation>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6565753193438714199L;
	
	@Id
	private Integer id;
	
	@MapsId
	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			optional = false)
	@JoinColumn(name = "id")
	private Ticket ticket;
	
//	@ManyToOne(
//			cascade = {
//					CascadeType.PERSIST,
//					CascadeType.MERGE,
//					CascadeType.DETACH,
//					CascadeType.REFRESH},
//			fetch = FetchType.EAGER)
//	@JoinColumn(
//			name = "flight_id", 
//			referencedColumnName = "id",
//			nullable = true)
//	private Flight flight;
//	
//	@ManyToOne(
//			cascade = {
//					CascadeType.PERSIST,
//					CascadeType.MERGE,
//					CascadeType.DETACH,
//					CascadeType.REFRESH},
//			fetch = FetchType.EAGER)
//	@JoinColumn(
//			name = "ticket_class_id", 
//			referencedColumnName = "id",
//			nullable = true)
//	private TicketClass ticketClass;
//	
//
//	@Column(name = "price")
//	private Integer price;
//	
	
	@ManyToOne(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.DETACH,
					CascadeType.REFRESH},
			fetch = FetchType.LAZY)
	@JoinColumn(
			name = "account_id", 
			referencedColumnName = "id",
			nullable = true)
	private BaseAccount account;
	
	@Column(name = "booking_date")
	private LocalDate bookDate;
	
	//Constructors
	public Reservation() {
		super();
		
	}

	//Getters
	public Integer getId() {return id;}
	public Ticket getTicket() {return ticket;}
	public BaseAccount getAccount() {return account;}
	public LocalDate getBookDate() {return bookDate;}
	
	//Setters
	public void setId(Integer id) {this.id = id;}
	public void setTicket(Ticket ticket) {this.ticket = ticket;}
	public void setAccount(BaseAccount account) {this.account = account;}
	public void setBookDate(LocalDate bookDate) {this.bookDate = bookDate;}
	
	//Compare for using Set, by implementing Comparable
	public int compareTo(Reservation another) {
		return this.getTicket().compareTo(another.getTicket());
	}


}
