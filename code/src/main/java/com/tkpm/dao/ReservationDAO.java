package com.tkpm.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.Reservation;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum ReservationDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private ReservationDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create list of reservations
	public List<Reservation> create(List<Reservation> reservations) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Iterate over all the reservation
			reservations.forEach(reservation -> {
				
				//Save the reservations to database
				Integer id = (Integer) session.save(reservation);
				reservation.setId(id);
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return reservations;
	}
	
	//Update list of reservations
	public List<Reservation> update(List<Reservation> reservations) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Iterate over all the reservation
			reservations.forEach(reservation -> {
				
				//Update each reservation
				session.update(reservation);
				
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return reservations;
	}
	
	//Delete an reservation by the given id
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
					"from " + Reservation.class.getName() + " " + 
					"where id in " + idSet;
		
			Query<Reservation> hql = session.createQuery(query);
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
	
	//Find all reservations in database
	public List<Reservation> findAll() {
		
		Session session = factory.getCurrentSession();
		List<Reservation> reservations = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of reservations
			reservations = session.createQuery("from " + Reservation.class.getName()).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return reservations;
		
	}
	
	//Find reservation by id
	public Reservation find(Integer id) {
		
		Session session = factory.getCurrentSession();
		Reservation reservation = null;
		
		try {
			session.beginTransaction();
			
			reservation = session.get(Reservation.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return reservation;
	}

	public Reservation update(Reservation reservation) {
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			session.update(reservation);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return reservation;
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
			
			//Query to delete
			String query = 
					"update " + Reservation.class.getName() + " r set " +
					"r.bookDate = :" + valueParam + ", " +
					"r.account = :" + valueParam + " " +
					"where r.id in " + idSet;
		
			Query<Reservation> hql = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			
			hql = hql.setParameter(valueParam, null);
			
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

	public List<Reservation> findAll(BaseAccount account) {
		
		Session session = factory.getCurrentSession();
		List<Reservation> reservations = null;
		
		try {
			session.beginTransaction();
			
			String param = "account_id";
			String query = 
						"select distinct r " +
						"from " + Reservation.class.getName() + " r " +
						"left join fetch r.ticket t " +
						"left join fetch t.flight f " +
						"left join fetch f.departureAirport " +
						"left join fetch f.arrivalAirport " +
						"where r.account.id = :" + param;
					
		
			reservations = session
					.createQuery(query, Reservation.class)
					.setParameter(param, account.getId())
					.getResultList();
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
			reservations = new ArrayList<>();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return reservations;
		
	}
}
 