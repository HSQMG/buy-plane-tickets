package com.tkpm.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.tkpm.dao.UserDAO;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;

//Using enum for applying Singleton Pattern
public enum UserService {

	INSTANCE;
	
	private UserDAO userDAO;
	private ReservationService reservationService;
	
	private UserService() {
		userDAO = UserDAO.INSTANCE;
		reservationService = ReservationService.INSTANCE;
	}
	
	//Create new user
	public User createUser(User user) {
		return userDAO.create(user);
	}
	
	//Update an user
	public User updateUser(User user) {
		return userDAO.update(user);
	}
	
	//Delete an user by the given id
	public int deleteUser(Integer id) {
		return userDAO.delete(id);
	}
	
	//Find all users in database
	public List<User> findAllUsers() {
		
		return userDAO.findAll();
		
	}
	
	//Find user by id
	public User findUserById(Integer id) {
		return userDAO.find(id);
	}
	
	//Find user by username
	public User findUserByUsername(String username) {
		return userDAO.find(username);
	}
	
	//Authenticate
	public User login(User user) {
		
		//Find the user in the database by the username
		User other = this.findUserByUsername(user.getUsername());
		
		//Return null if the username is not existed
		if (null == other) {return null;}
		
		//Return null if the passwords are not matched
		if (!user.getEncryptedPassword().equals(other.getEncryptedPassword())) {return null;}
		
		//Load the account for the user
		BaseAccount account =  getExactlyAccountForUser(other);
		other.setAccount(account);
		account.setUser(other);
		
		//Return the user if ther username and password are matched
		return other;
	}

	public List<User> getUsersWithReservations(List<Integer> ids) {
		return userDAO.find(ids);
	}
	
	public int deleteUsers(List<Integer> ids) {
		//TODO:
		List<User> users = getUsersWithReservations(ids);
		
		List<Reservation> reservations = new LinkedList<>();
		
		for (User user: users) {
			reservations.addAll(user.getAccount().getReservations());
		}
		
		//Get reservations id
		List<Integer> reservation_ids = reservations
				.stream()
				.map(reservation -> reservation.getId())
				.collect(Collectors.toList());
		
		//Cancel the reservation and ticket
		reservationService.cancelReservations(reservation_ids);
		
		//Delete the user + account
		return userDAO.delete(ids);
	}

	public BaseAccount getExactlyAccountForUser(User user) {
		return userDAO.loadAccount(user, USER_ROLE.convertStringToUSER_ROLE(user.getRole()));
	}
}
 