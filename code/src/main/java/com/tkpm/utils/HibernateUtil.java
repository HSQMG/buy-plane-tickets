package com.tkpm.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.tkpm.entities.AdminAccount;
import com.tkpm.entities.Airport;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.ManagerAccount;
import com.tkpm.entities.Policy;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.TicketClass;
import com.tkpm.entities.Transition;
import com.tkpm.entities.User;

//Using enum to implement singleton pattern, for thread safe
public enum HibernateUtil {

	//Instance of the utility
	INSTANCE;
	
	//Factory for managing session in Hibernate
	private SessionFactory sessionFactory;

	//Getter for factory
	public SessionFactory getSessionFactory() {return sessionFactory;}
	
	//Hibernate configuration file name
	//Note: the file path is /src/main/resources/hibernate.cfg.xml
	private String hibernateConfigFile = "hibernate.cfg.xml";
	
	private HibernateUtil() {
		
		try {
			
			//Build the factory, base on the properties on hibernateConfigFile
			sessionFactory = new Configuration()
					.configure(hibernateConfigFile)
					.addAnnotatedClass(AdminAccount.class)
					.addAnnotatedClass(Airport.class)
					.addAnnotatedClass(BaseAccount.class)
					.addAnnotatedClass(CustomerAccount.class)
					.addAnnotatedClass(Flight.class)
					.addAnnotatedClass(FlightDetail.class)
					.addAnnotatedClass(ManagerAccount.class)
					.addAnnotatedClass(Policy.class)
					.addAnnotatedClass(Reservation.class)
					.addAnnotatedClass(Ticket.class)
					.addAnnotatedClass(TicketClass.class)
					.addAnnotatedClass(Transition.class)
					.addAnnotatedClass(User.class)
					.buildSessionFactory();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
