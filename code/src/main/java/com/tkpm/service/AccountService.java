package com.tkpm.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.tkpm.dao.AccountDAO;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.Flight;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.TicketClass;

//Using enum for applying Singleton Pattern
public enum AccountService {

	INSTANCE;
	
	public static final int CANCELLABLE = 0;
	public static final int UNCANCELLABLE = 1;
	
	private AccountDAO accountDAO;
	private ReservationService reservationService;
	private PolicyService policyService;
	
	private AccountService() {
		accountDAO = AccountDAO.INSTANCE;
		reservationService = ReservationService.INSTANCE;
		policyService = PolicyService.INSTANCE;
	}
	
	//Create new account
	public BaseAccount createAccount(BaseAccount account) {
		return accountDAO.create(account);
	}
	
	//Update an account
	public BaseAccount updateAccount(BaseAccount account) {
		return accountDAO.update(account);
	}
	
	//Delete an account by the given id
	public int deleteAccount(Integer id, Class accountClass) {
		return accountDAO.delete(id, accountClass);
	}
	
	//Find all accounts in database with the given class
	public Set<BaseAccount> findAllAccounts(Class accountClass) {
		
		//Using set, because query in DAO only return list
		return new TreeSet<>(accountDAO.findAll(accountClass));
		
	}
	
	//Find account by id
	public BaseAccount findAccountById(Integer id, Class accountClass) {
		return accountDAO.find(id, accountClass);
	}
	
//	public Reservation book(
//			Flight flight, TicketClass ticketClass, CustomerAccount customer,
//			String name, String identityCode, String phoneNumber) {
//		
//		//Check if there is late to book ticket
//		if (policyService.isLateToBook(flight)) {
//			return null;
//		}
//		
//		//Get the list of bookable reservation
//		List<Reservation> reservations = reservationService.findAvailableReservationsFromFlight(flight, ticketClass);
//		
//		//Return null if the flight had been fullfill
//		if (null == reservations || reservations.isEmpty()) {
//			return null;
//		}
//		
//		//Random a reservation in the list
//		Random random = new Random();
//		Reservation bookedReservation = reservations.get(random.nextInt(reservations.size()));
//		
//		//Mark the reservation as booked
//		bookedReservation.getTicket().setIsBooked(true);
//		
//		//Set information in reservation and ticket
//		bookedReservation.setBookDate(LocalDate.now());
//		bookedReservation.setCustomer(customer);
//		bookedReservation.getTicket().setCustomer(customer);
//		bookedReservation.getTicket().setName(name);
//		bookedReservation.getTicket().setIdentityCode(identityCode);
//		bookedReservation.getTicket().setPhoneNumber(phoneNumber);
//		
//		//Using list to reuse the code in service
//		List<Reservation> bookedReservations = new ArrayList<>();
//		bookedReservations.add(bookedReservation);
//		 
//		//Update the reservation (this also update the ticket, because of Cascade setup in entity)
//		reservationService.updateReservations(bookedReservations);
//		
//		return bookedReservation;
//	}
	
	//Cancel a reservation
//	public int cancel(Reservation reservation) {
//		
//		//Return 1 to reject cancelling
//		if (policyService.isLateToCancel(reservation.getTicket().getFlight())) {
//			return UNCANCELLABLE;
//		}
//		
//		//Mark the ticket as has not booking yet
//		reservation.getTicket().setIsBooked(false);
//		
//		//Clear data in the ticket and reservation
//		reservation.setBookDate(null);
//		reservation.setAccount(null);
//		reservation.getTicket().setName(null);
//		reservation.getTicket().setIdentityCode(null);
//		reservation.getTicket().setPhoneNumber(null);
//		
//		//Using list to reuse the code in service
//		List<Reservation> reservations = new ArrayList<>();
//		reservations.add(reservation);
//				 
//		//Update the reservation (this also update the ticket, because of Cascade setup in entity)
//		reservationService.updateReservations(reservations);
//		
//		//Return 0 if cancellable
//		return CANCELLABLE;
//	}
}
 