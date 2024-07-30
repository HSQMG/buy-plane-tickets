package com.tkpm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "flight_detail")
public class FlightDetail implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -389979282679597092L;

	@Id
	private Integer id;
	
	@MapsId
	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			optional = false)
	@JoinColumn(name = "flight_id")
	private Flight flight;
	
	@Column(name = "flight_time")
	private Integer flightTime;	//Minutes
	
	@Column(name = "first_class_seat_size")
	private Integer numberOfFirstClassSeat;
	
	@Column(name = "second_class_seat_size")
	private Integer numberOfSecondClassSeat;
	
	@Column(name = "first_class_seat_price")
	private Integer priceOfFirstClassSeat;
	
	@Column(name = "second_class_seat_price")
	private Integer priceOfSecondClassSeat;
	
	//Constructors
	public FlightDetail() {
		super();
		
	}

	//Getters
	public Flight getFlight() {return flight;}
	public Integer getId() {return id;}
	public Integer getFlightTime() {return flightTime;}
	public Integer getNumberOfFirstClassSeat() {return numberOfFirstClassSeat;}
	public Integer getNumberOfSecondClassSeat() {return numberOfSecondClassSeat;}
	public Integer getPriceOfFirstClassSeat() {return priceOfFirstClassSeat;}
	public Integer getPriceOfSecondClassSeat() {return priceOfSecondClassSeat;}

	//Setters
	public void setFlight(Flight flight) {this.flight = flight;}
	public void setId(Integer id) {this.id = id;}
	public void setFlightTime(Integer flightTime) {this.flightTime = flightTime;}
	public void setNumberOfFirstClassSeat(Integer numberOfFirstClassSeat) {this.numberOfFirstClassSeat = numberOfFirstClassSeat;}
	public void setNumberOfSecondClassSeat(Integer numberOfSecondClassSeat) {this.numberOfSecondClassSeat = numberOfSecondClassSeat;}	
	public void setPriceOfFirstClassSeat(Integer priceOfFirstClassSeat) {this.priceOfFirstClassSeat = priceOfFirstClassSeat;}
	public void setPriceOfSecondClassSeat(Integer priceOfSecondClassSeat) {this.priceOfSecondClassSeat = priceOfSecondClassSeat;}

	
}
