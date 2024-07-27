package com.tkpm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tkpm.entities.AdminAccount;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.ManagerAccount;
import com.tkpm.utils.HibernateUtil;

//Using enum for applying Singleton Pattern
public enum AccountDAO {

	INSTANCE;
	
	private SessionFactory factory;
	
	private AccountDAO() {
		factory = HibernateUtil.INSTANCE.getSessionFactory();
	}
	
	//Create new account
	public BaseAccount create(BaseAccount account) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Save the account to database
			Integer id = (Integer) session.save(account);
			
			//Update the current id for the given account
			account.setId(id);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return account;
	}
		
	//Update an account
	public BaseAccount update(BaseAccount account) {
		
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			//Update the account
			session.update(account);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return account;
	}
	
	//Delete an account by the given id and class (class is based on role)
	public int delete(Integer id, Class accountClass) {

		Session session = factory.getCurrentSession();
		int errorCode = 0;
		
		try {
			session.beginTransaction();
			
			BaseAccount account = (BaseAccount) session.get(accountClass, id);
			
			if (null !=  account) {
				session.delete(account);
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
				
	//Find all accounts in database with the given class (class is based on role)
	public List<BaseAccount> findAll(Class accountClass) {
		
		Session session = factory.getCurrentSession();
		List<BaseAccount> accounts = new ArrayList<>();
		
		try {
			session.beginTransaction();
			
			//Get the list of accounts
			accounts = session.createQuery("from " + accountClass.getName()).list();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return accounts;
		
	}
	
	//Find account by id and class (class is based on role)
	public BaseAccount find(Integer id, Class accountClass) {
		
		Session session = factory.getCurrentSession();
		BaseAccount account = null;
		
		try {
			session.beginTransaction();
			
			//Try to find the account base on role
			if (accountClass.equals(CustomerAccount.class)) {
				account = (CustomerAccount) session.get(accountClass, id);
			} 
			else if (accountClass.equals(ManagerAccount.class)){
				account = (ManagerAccount) session.get(accountClass, id);
			}
			else if (accountClass.equals(AdminAccount.class)){
				account = (AdminAccount) session.get(accountClass, id);
			}
			
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
 