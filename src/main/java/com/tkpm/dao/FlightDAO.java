package com.tkpm.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.Transition;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum FlightDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private FlightDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create new flight
	public Flight create(Flight flight) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Store the detail
			FlightDetail detail = flight.getDetail();
			detail.setFlight(flight);
			//flight.setDetail(null);
			
			//Save the flight to database
			Integer id = (Integer) session.save(flight);
			
			//Update the current id for the given flight
			flight.setId(id);
			
			//Create the detail
			if (null != detail) {
				detail.setFlight(flight);
				id = (Integer) session.save(detail);
				detail.setId(id);
				
				flight.setDetail(detail);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return flight;
	}
	
	//Update an flight
	public Flight update(Flight flight) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Update the flight
			session.update(flight.getDetail());
			session.update(flight);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return flight;
	}
	
	//Delete an flight by the given id
	public int delete(Integer id) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			Flight flight = session.get(Flight.class, id);
			
			if (null !=  flight) {
				session.delete(flight);
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
	
	//Find all flights in database
	public List<Flight> findAll() {
		
		Session session = factory.getCurrentSession();
		List<Flight> flights = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of flights
			//flights = session.createQuery("from " + Flight.class.getName()).list();
			
			String query = 
					"select f " +
					"from " + Flight.class.getName() + " f " + 
					"left join fetch f.departureAirport " +
					"left join fetch f.arrivalAirport ";
			
			flights = session.createQuery(query, Flight.class).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return flights;
		
	}
	
	//Find flight by id
	public Flight find(Integer id) {
		
		Session session = factory.getCurrentSession();
		Flight flight = null;
		
		try {
			session.beginTransaction();
			
			flight = session.get(Flight.class, id);
//			String param = "id";
//			String query = 
//					"select f " +
//					"from " + Flight.class.getName() + " f " + 
//					"left join fetch f.tickets " +
//					"where f.id = :" + param;
//			
//			flight = session
//					.createQuery(query, Flight.class)
//					.setParameter(param, id)
//					.setMaxResults(1)
//					.stream()
//					.findFirst()
//					.orElse(null);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return flight;
	}

	public FlightDetail findDetailById(Integer id) {
		
		Session session = factory.getCurrentSession();
		FlightDetail detail = null;
		
		try {
			session.beginTransaction();
			
			detail = session.get(FlightDetail.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return detail;
	}

	public int delete(List<Integer> flightIds) {
		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		if (null == flightIds || flightIds.isEmpty()) {
			errorCode = 2;
			return errorCode;
		}
		
		try {
			session.beginTransaction();
			
			//Get param to delete ticket
			int size = flightIds.size();
			List<String> flightParams = new LinkedList<>();
			for (int i = 0; i <size; ++i) {
				flightParams.add("param" + i);
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append("(-1");	//Using -1 for dynamically without checking "," if there is only 1 element in ids 
			for (String param: flightParams) {
				builder.append(", :" + param);
			}
			builder.append(")");
			
			String flightIdSet = builder.toString();
			
			String query = 
					"select f " +
					"from " + Flight.class.getName() + " f " + 
					"left join fetch f.detail " +
					"left join fetch f.tickets " +
					"where f.id in " + flightIdSet;
			
			Query<Flight> hql = session.createQuery(query, Flight.class);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(flightParams.get(i), flightIds.get(i));
			}
			
			
			List<Flight> flights = hql.getResultList(); 
			
			//Get all the tickets from flight;
			List<Ticket> tickets = new ArrayList<>();
			for (Flight flight: flights) {
				tickets.addAll(flight.getTickets());
			}
			
			//Get the param variable for ticket
			size = tickets.size();
			List<String> ticketParams = new LinkedList<>();
			for (int i = 0; i < size; ++i) {
				ticketParams.add("param" + i);
			}
			
			//Get the binding set for the ticket;
			builder = new StringBuilder();
			builder.append("(-1");	//Using -1 for dynamically without checking "," if there is only 1 element in ids 
			for (String param: ticketParams) {
				builder.append(", :" + param);
			}
			builder.append(")");
			
			//Get the binding string for ticket id
			String ticketIdSet = builder.toString();
			
			//Delete the reservation first
			query = 
					"delete " +
					"from " + Reservation.class.getName() + " " + 
					"where id in " + ticketIdSet;
		
			Query<Reservation> hql0 = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql0 = hql0.setParameter(ticketParams.get(i), tickets.get(i).getId());
			}
			hql0.executeUpdate();
			
			//Delete the ticket after first
			query = 
					"delete " +
					"from " + Ticket.class.getName() + " " + 
					"where id in " + ticketIdSet;
		
			Query<Reservation> hql1 = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql1 = hql1.setParameter(ticketParams.get(i), tickets.get(i).getId());
			}
			hql1.executeUpdate();
			
			//Delete the flight detail
			size = flightIds.size();
			query = 
					"delete " +
					"from " + FlightDetail.class.getName() + " " + 
					"where id in " + flightIdSet;
			
			Query<Reservation> hql2 = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql2 = hql2.setParameter(flightParams.get(i), flightIds.get(i));
			}
			hql2.executeUpdate();
			
			//Delete the transition
			query = 
					"delete " +
					"from " + Transition.class.getName() + " " + 
					"where flight.id in " + flightIdSet;
			
			Query<Reservation> hql3 = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql3 = hql3.setParameter(flightParams.get(i), flightIds.get(i));
			}
			hql3.executeUpdate();
			
			
			//Delete the flight at the final
			query = 
					"delete " +
					"from " + Flight.class.getName() + " " + 
					"where id in " + flightIdSet;
			
			Query<Reservation> hql4 = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql4 = hql4.setParameter(flightParams.get(i), flightIds.get(i));
			}
			hql4.executeUpdate();
			
//			System.out.println("Before delete");
//			//Delete all the flight
//			for (Flight flight: flights) {
//				session.delete(flight);
//			}
			
			
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

	public List<Flight> findForReport(List<Integer> ids) {
		Session session = factory.getCurrentSession();
		List<Flight> flights = null;
		
		try {
			session.beginTransaction();
			
			//Get param to delete ticket
			int size = ids.size();
			List<String> params = new LinkedList<>();
			for (int i = 0; i <size; ++i) {
				params.add("param" + i);
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append("(-1");	//Using -1 for dynamically without checking "," if there is only 1 element in ids 
			for (String param: params) {
				builder.append(", :" + param);
			}
			builder.append(")");
			
			String idSet = builder.toString();
			
			String query = 
					"select distinct f " +
					"from " + Flight.class.getName() + " f " + 
					"left join fetch f.departureAirport " +
					"left join fetch f.arrivalAirport " +
					"left join fetch f.tickets " +
					"where f.id in " + idSet;
			
			Query<Flight> hql = session.createQuery(query, Flight.class);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			
			flights = hql.list();
			
		} catch (Exception ex) {
			flights = new ArrayList<>();
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return flights;
	}

	public int setAirportNull(List<Integer> ids) {

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
			
			//Query to update departure airport
			String query = 
					"update " + Flight.class.getName() + " f set " +
					"f.departureAirport = :" + valueParam + " " +
					"where f.departureAirport.id in " + idSet;
		
			Query<?> hql = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			hql = hql.setParameter(valueParam, null);
			hql.executeUpdate();
			
			//Query to update arrival airport
			query = 
					"update " + Flight.class.getName() + " f set " +
					"f.arrivalAirport = :" + valueParam + " " +
					"where f.arrivalAirport.id in " + idSet;
		
			hql = session.createQuery(query);
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
}
 