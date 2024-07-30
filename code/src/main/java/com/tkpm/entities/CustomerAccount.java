package com.tkpm.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "customer_account")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "fk_customer_base"))
public class CustomerAccount extends BaseAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7570950875716299948L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "identity_code")
	private String identityCode;
	
	@Column(name = "phone_number")
	private String phoneNumber;	
	
//	@OneToMany(
//			cascade = CascadeType.ALL,
//			mappedBy = "customer",
//			orphanRemoval = true)
//	private Set<Ticket> tickets;
//	
//	@OneToMany(
//			cascade = CascadeType.ALL,
//			mappedBy = "customer",
//			orphanRemoval = true)
//	private Set<Reservation> reservations;
	
	//Constructors
	public CustomerAccount() {
		super();
	}

	public CustomerAccount(String name, String identityCode, String phoneNumber) {
		super();
		this.name = name;
		this.identityCode = identityCode;
		this.phoneNumber = phoneNumber;
	}

	//Getters
	public String getName() {return name;}
	public String getIdentityCode() {return identityCode;}
	public String getPhoneNumber() {return phoneNumber;}
//	public Set<Ticket> getTickets() {return tickets;}
//	public Set<Reservation> getReservations() {return reservations;}
	
	//Setters
	public void setName(String name) {this.name = name;}
	public void setIdentityCode(String identityCode) {this.identityCode = identityCode;}
	public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}	
//	public void setTickets(Set<Ticket> tickets) {this.tickets = tickets;}
//	public void setReservations(Set<Reservation> reservations) {this.reservations = reservations;}

	
}
