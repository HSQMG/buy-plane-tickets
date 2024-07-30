package com.tkpm.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transition_airport")
public class Transition implements Serializable, Comparable<Transition> {

	private static final long serialVersionUID = -6050259972242077937L;

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
			name = "airport_id", 
			referencedColumnName = "id",
			nullable = true)
	private Airport airport;
	
//	@ManyToMany(
//			cascade = {
//					CascadeType.PERSIST,
//					CascadeType.MERGE,
//					CascadeType.DETACH,
//					CascadeType.REFRESH},
//			fetch = FetchType.EAGER)
//	@JoinTable(
//			name = "flight_transition",
//			joinColumns = @JoinColumn(name = "transition_airport_id"),
//			inverseJoinColumns = @JoinColumn(name = "flight_id"))
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
	
	@Column(name = "transition_time")
	private Integer transitionTime;	//Minutes
	
	@Column(name = "note")
	private String note;

	//Constructors
	public Transition() {
		super();
	}

	//Getters
	public Integer getId() {return id;}
	public Airport getAirport() {return airport;}
	public Flight getFlight() {return flight;}
	public Integer getTransitionTime() {return transitionTime;}
	public String getNote() {return note;}
	
	//Setters
	public void setId(Integer id) {this.id = id;}	
	public void setAirport(Airport airport) {this.airport = airport;}
	public void setFlight(Flight flight) {this.flight = flight;}
	public void setTransitionTime(Integer transitionTime) {this.transitionTime = transitionTime;}
	public void setNote(String note) {this.note = note;}
	
	//Compare for using Set, by implementing Comparable
	public int compareTo(Transition another) {
		if (null == another) return 1;
		
		int result = 
				(this.getId() > another.getId()?1:
					(this.getId() < another.getId()? -1: 0));
		
		return result;
	}
}
