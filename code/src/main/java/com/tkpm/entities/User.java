package com.tkpm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {
	
	public enum USER_ROLE {
		
		Customer,
		Manager,
		Admin;
		
		public static USER_ROLE convertStringToUSER_ROLE(String str) {
			if (str.equals(Admin.name())) return Admin;
			if (str.equals(Manager.name())) return Manager;
			if (str.equals(Customer.name())) return Customer;
			return null;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2075932010644137075L;
	
	//Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "user_name")
	private String username;
	
	@Column(name = "encrypted_password")
	private String encryptedPassword;
	
	@Column(name = "role")
	private String role;
	
	@OneToOne(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			optional = false)
	private BaseAccount account;
	
	//Constructors
	public User() {
		//do nothing
	}	

	//Getters
	public Integer getId() {return id;}
	public String getUsername() {return username;}
	public String getEncryptedPassword() {return encryptedPassword;}
	public String getRole() {return role;}
	public BaseAccount getAccount() {return account;}
	
	//Setters
	public void setId(Integer id) {this.id = id;}
	public void setUsername(String username) {this.username = username;}	
	public void setEncryptedPassword(String encryptedPassword) {this.encryptedPassword = encryptedPassword;}
	public void setRole(String role) {this.role = role;}
	public void setAccount(BaseAccount account) {this.account = account;}

	@Override
	public String toString() {
		return "User [id=" + id + ", role=" + role + "]";
	}
	
	
}
