package com.tkpm.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.tkpm.entities.AdminAccount;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.ManagerAccount;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum UserDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private UserDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create new user
	public User create(User user) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Store base account
			BaseAccount account = user.getAccount();
			account.setUser(user);
			
			//Save the user to database
			Integer id = (Integer) session.save(user);
			
			//Update the current id for the given user
			user.setId(id);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return user;
	}
	
	//Update an user
	public User update(User user) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Set null for the owner's reservation first
			List<Integer> ids = user
					.getAccount()
					.getReservations()
					.stream()
					.map(reservation -> reservation.getId())
					.collect(Collectors.toList());
			
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
					"r.account = :" + valueParam + " " +
					"where r.id in " + idSet;
		
			Query<?> hql = session.createQuery(query);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}		
			hql = hql.setParameter(valueParam, null);
			hql.executeUpdate();
			
			
			//Delete the current account with given id
			//Query to delete
			String param = "id";
			query = 
					"delete " +
					"from " + BaseAccount.class.getName() + " a " + 
					"where a.id = :" + param;
			
			session
				.createQuery(query)
				.setParameter(param, user.getId())
				.executeUpdate();
					
			//Create the account with the given id
			session.save(user.getAccount());
			session.update(user);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return user;
	}
	
	//Delete an user by the given id
	public int delete(Integer id) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			User user = session.get(User.class, id);
			
			if (null !=  user) {
				session.delete(user);
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
	
	//Find all users in database
	public List<User> findAll() {
		
		Session session = factory.getCurrentSession();
		List<User> users = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of users
			users = session.createQuery("from " + User.class.getName()).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return users;
		
	}
	
	//Find user by id
	public User find(Integer id) {
		
		Session session = factory.getCurrentSession();
		User user = null;
		
		try {
			session.beginTransaction();
			
			user = session.get(User.class, id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return user;
	}
	
	//Find user by username
	public User find(String username) {
	
		Session session = factory.getCurrentSession();
		User user = null;
		
		try {
			session.beginTransaction();
			
			//Parameterize the query
			String param = "username";
			
			//Make the query
			String query = "from " + User.class.getName() + " u where u.username = :" + param;
			
			user = (User) session.createQuery(query)
					.setParameter(param, username)
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
	
		return user;	
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
			
			Class[] classes = {
					BaseAccount.class,
					User.class
			};
			
			
			//Query to delete thoose accounts
			for (Class clazz: classes) {
				
				String query = 
						"delete " +
						"from " + clazz.getName() + " " + 
						"where id in " + idSet;
				
				Query<?> hql = session.createQuery(query);
				for (int i = 0; i < size; ++i) {
					hql = hql.setParameter(params.get(i), ids.get(i));
				}
				hql.executeUpdate();
				
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

	public List<User> find(List<Integer> ids) {
		Session session = factory.getCurrentSession();
		List<User> users = null;
		
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
					"select distinct u " +
					"from " + User.class.getName() + " u " + 
					"left join fetch u.account a " +
					"left join fetch a.reservations " +
					"where u.id in " + idSet;
		
			Query<User> hql = session.createQuery(query, User.class);
			for (int i = 0; i < size; ++i) {
				hql = hql.setParameter(params.get(i), ids.get(i));
			}
			
			users = hql.getResultList();
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
			users = new ArrayList<>();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return users;
	}

	public BaseAccount loadAccount(User user, USER_ROLE role) {
		
		Session session = factory.getCurrentSession();
		BaseAccount account = null;
		
		try {
			session.beginTransaction();
		
			Class[] classes = {
					CustomerAccount.class,
					ManagerAccount.class,
					AdminAccount.class,
			};
			
			int classIndex = -1;
			for (USER_ROLE r: USER_ROLE.values()) {
				++classIndex;
				if (r.equals(role)) {
					break;
				}
			}
			
			//Query to delete
			String param = "id";
			String query = 
					"select a " +
					"from " + classes[classIndex].getName() + " a " + 
					"where a.id = :" + param;
		
			account = (BaseAccount) session
					.createQuery(query, classes[classIndex])
					.setMaxResults(1)
					.setParameter(param, user.getId())
					.getSingleResult();
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			session.getTransaction().rollback();
		
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	
		return account;
		
		
	}
}
 