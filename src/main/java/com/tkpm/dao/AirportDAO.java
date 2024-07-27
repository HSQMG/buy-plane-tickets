package com.tkpm.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Ticket;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum AirportDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private AirportDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create new airport
	public Airport create(Airport airport) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Save the airport to database
			Integer id = (Integer) session.save(airport);
			
			//Update the current id for the given airport
			airport.setId(id);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return airport;
	}
	
	//Update an airport
	public Airport update(Airport airport) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Update the airport
			session.update(airport);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return airport;
	}
	
	//Delete an airport by the given id
	public int delete(Integer id) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			// Find the airport with specified ID to make sure it exists
			Airport airport = session.get(Airport.class, id);
			
			if (null !=  airport) {
				session.delete(airport);
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
	
	//Find all airports in database
	public List<Airport> findAll() {
		
		Session session = factory.getCurrentSession();
		List<Airport> airports = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			// Get the list of airports
			// This equals to "select *"
			airports = session.createQuery("from " + Airport.class.getName(), Airport.class).getResultList();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return airports;
		
	}
	
	//Find airport by id
	public Airport find(Integer id) {
		
		Session session = factory.getCurrentSession();
		Airport airport = null;
		
		try {
			session.beginTransaction();
			
			// Get airport with specified ID
			// This will automatically find the correspondent table
			airport = session.get(Airport.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return airport;
	}	

	public Airport find(String name) {
		Session session = factory.getCurrentSession();
		Airport airport = null;
		
		try {
			session.beginTransaction();
			
			// Using param to avoid sql jnjection
			String param = "name";
			String query = "from " + Airport.class.getName() + " a where a.name = :" + param;
			
			// Create query, execute, and receive the result
			airport = session
					.createQuery(query, Airport.class)
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
	
		return airport;
	}

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
					"from " + Airport.class.getName() + " " + 
					"where id in " + idSet;
		
			// Create the query but not set the parameters for it yet
			Query<Airport> hql = session.createQuery(query);
			
			// Set the list of parameters one by one to the query
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			
			// Execute the query
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
 