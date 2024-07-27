package com.tkpm.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.tkpm.entities.Flight;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum TicketDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private TicketDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create list of tickets
	public List<Ticket> create(List<Ticket> tickets) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Iterate over all the ticket
			tickets.forEach(ticket -> {
				
				//Save the tickets to database
				Integer id = (Integer) session.save(ticket);
				
				//Update the current id for the given ticket
				ticket.setId(id);
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return new ArrayList<>(tickets);
	}
	
	//Update list of tickets
	public List<Ticket> update(List<Ticket> tickets) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Iterate over all the ticket
			tickets.forEach(ticket -> {
				
				//Update each ticket
				session.update(ticket);
				
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return tickets;
	}
	
	//Delete an ticket by the given id
	public int delete(List<Integer> ids) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			//Get param to delete reservation
			int size = ids.size();
			List<String> params = new LinkedList<>();
			for (int i = 0; i <size; ++i) {
				params.add("param" + i);
			}
			
			//Build the id set
			StringBuilder builder = new StringBuilder();
			builder.append("(-1");	//Using -1 for dynamically without checking "," if there is only 1 element in ids 
			for (String param: params) {
				builder.append(", :" + param);
			}
			builder.append(")");
			String idSet = builder.toString();
			
			//Query to delete
			String query = 
					"delete " +
					"from " + Ticket.class.getName() + " " + 
					"where id in " + idSet;
		
			Query<Ticket> hql = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			hql.executeUpdate();
			
			
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
	
	//Find all tickets in database
	public List<Ticket> findAll() {
		
		Session session = factory.getCurrentSession();
		List<Ticket> tickets = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of tickets
			tickets = session.createQuery("from " + Ticket.class.getName()).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return tickets;
		
	}
	
	//Find ticket for flgiht
	public Set<Ticket> find(Flight flight) {
		Session session = factory.getCurrentSession();
		Set<Ticket> tickets = new TreeSet<>();
		
		try {
			session.beginTransaction();
			
			String param = "id";
			String query = 
					"select t " +
					"from " + Ticket.class.getName() + " t " + 
					"left join fetch t.flight " +
					"left join fetch t.reservation " +
					"left join fetch t.ticketClass " +
					"where t.flight.id = :" + param;
			
			List<Ticket> buffer = session
					.createQuery(query, Ticket.class)
					.setParameter(param, flight.getId())
					.list();
			
			tickets = new TreeSet<>(buffer);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return tickets;
	}
	
	//Find ticket by id
	public Ticket find(Integer id) {
		
		Session session = factory.getCurrentSession();
		Ticket ticket = null;
		
		try {
			session.beginTransaction();
			
			ticket = session.get(Ticket.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return ticket;
	}

	public Ticket update(Ticket ticket) {
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			session.update(ticket);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return ticket;
	}

	public int cancel(List<Integer> ids) {
		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			//Get param to delete reservation
			int size = ids.size();
			List<String> params = new LinkedList<>();
			for (int i = 0; i <size; ++i) {
				params.add("param" + i);
			}
			
			//Build the id set
			StringBuilder builder = new StringBuilder();
			builder.append("(-1");	//Using -1 for dynamically without checking "," if there is only 1 element in ids 
			for (String param: params) {
				builder.append(", :" + param);
			}
			builder.append(")");
			String idSet = builder.toString();
			
			//Param for the value
			String valueParam = "value";
			String bookParam = "bookValue";
			
			//Query to delete
			String query = 
					"update " + Ticket.class.getName() + " t set " +
					"t.name = :" + valueParam + ", " +
					"t.identityCode = :" + valueParam + ", " +
					"t.phoneNumber = :" + valueParam + ", " +
					"t.isBooked = :" + bookParam + " " +
					"where t.id in " + idSet;
		
			Query<Reservation> hql = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			
			hql = hql.setParameter(valueParam, null);
			hql = hql.setParameter(bookParam, new Boolean(false));
			
			hql.executeUpdate();
			
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
}
 