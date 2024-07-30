package com.tkpm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admin_account")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "fk_admin_base"))
public class AdminAccount extends BaseAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40974366513543126L;

	/**
	 * 
	 */

	@Column(name = "name")
	private String name;
	
	@Column(name = "identity_code")
	private String identityCode;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	//Constructors
	public AdminAccount() {
		super();
	}

	public AdminAccount(String name, String identityCode, String phoneNumber) {
		super();
		this.name = name;
		this.identityCode = identityCode;
		this.phoneNumber = phoneNumber;
	}

	//Getters
	public String getName() {
		return name;
	}
	public String getIdentityCode() {
		return identityCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	
}
