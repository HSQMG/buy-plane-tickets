package com.tkpm.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "policy")
public class Policy implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4279708656068899232L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "datatype")
	private String datatype;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "is_applied")
	private Boolean isApplied;
	
	public Policy() {
		//do nothing
	}

	//Getters
	public Integer getId() {return id;}
	public String getName() {return name;}
	public String getDatatype() {return datatype;}
	public String getValue() {return value;}
	public Boolean getIsApplied() {return isApplied;}

	//Setters
	public void setId(Integer id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setDatatype(String datatype) {this.datatype = datatype;}
	public void setValue(String value) {this.value = value;}
	public void setIsApplied(Boolean isApplied) {this.isApplied = isApplied;}
	
	
}
