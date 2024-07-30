package com.tkpm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tkpm.entities.TicketClass;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum TicketClassDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private TicketClassDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create new ticket class
	public TicketClass create(TicketClass ticketClass) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Save the ticketClass to database
			Integer id = (Integer) session.save(ticketClass);
			
			//Update the current id for the given ticketClass
			ticketClass.setId(id);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return ticketClass;
	}
	
	//Update an ticket class
	public TicketClass update(TicketClass ticketClass) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Update the ticket class
			session.update(ticketClass);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return ticketClass;
	}
	
	//Delete an ticket class by the given id
	public int delete(Integer id) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			TicketClass ticketClass = session.get(TicketClass.class, id);
			
			if (null !=  ticketClass) {
				session.delete(ticketClass);
			}
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
			errorCode = 1;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return errorCode;
	}
	
	//Find all ticket classes in database
	public List<TicketClass> findAll() {
		
		Session session = factory.getCurrentSession();
		List<TicketClass> ticketClasses = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of ticketClasses
			ticketClasses = session.createQuery("from " + TicketClass.class.getName()).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return ticketClasses;
		
	}
	
	//Find ticket class by id
	public TicketClass find(Integer id) {
		
		Session session = factory.getCurrentSession();
		TicketClass ticketClass = null;
		
		try {
			session.beginTransaction();
			
			ticketClass = session.get(TicketClass.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return ticketClass;
	}
	
	//Find ticket class by name
	public TicketClass find(String name) {
			
		Session session = factory.getCurrentSession();
		TicketClass ticketClass = null;
			
		try {
			session.beginTransaction();
			String param = "name";
			String query = "from " + TicketClass.class.getName() + " tc where tc.name = :" + param;
			
			ticketClass = session
					.createQuery(query, TicketClass.class)
					.setParameter(param, name)
					.setMaxResults(1)
					.stream()
					.findFirst()
					.orElse(null);
				
		} catch (Exception ex) {
				
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return ticketClass;
	}
}
 