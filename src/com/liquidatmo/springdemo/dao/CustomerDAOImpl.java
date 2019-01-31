package com.liquidatmo.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.liquidatmo.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	
	// need to infect the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public List<Customer> getCustomers() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// create a query  ... sort by last name
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
		
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		
		// return the results
		return customers;
	}


	@Override
	public void saveCustomer(Customer theCustomer) {
		
		// get the current hibernate session
		Session session = sessionFactory.getCurrentSession();
		
		// save / update the customer (finally LOL)
		session.saveOrUpdate(theCustomer);
		
	}


	@Override
	public Customer getCustomer(int theId) {
		
		// get the current hibernate session
		Session session = sessionFactory.getCurrentSession();
		
		// now retrieve a customer from db by his/her primary key
		Customer theCustomer = session.get(Customer.class, theId);
		
		return theCustomer;
	}


	@Override
	public void deleteCustomer(int theId) {
		
		// get the current hibernate session
		Session session = sessionFactory.getCurrentSession();
		
		// delete the object with primary key
		Query theQuery =
				session.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
		
		
	}


	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		
		// get the current hibernate session
		Session session = sessionFactory.getCurrentSession();
		
		Query theQuery = null;
		
		if(theSearchName !=null && theSearchName.trim().length() > 0) {
			
			// search for firstName or lastName ... case insensitive
			theQuery = session.createQuery("from Customer where lower(firstName) like:theName or"
					+ " lower(lastName) like:theName", Customer.class);
			theQuery.setParameter("theName", "%" +
					theSearchName.toLowerCase() + "%");
		}
		else {
			//theSearchName is empty ... so just get all customers
			theQuery = session.createQuery("from Customer", Customer.class);
		}
		
		List<Customer> result = theQuery.getResultList();
		return result;
	}

}
