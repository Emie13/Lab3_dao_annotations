package dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import entities.Employee;

public class DaoEmployee {

	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;
	
	static {
		Configuration configuration = new Configuration().configure();
		configuration.addAnnotatedClass(Employee.class);

		// Build a service registry fron the properties of the configuration –
		// This is new in Hibernate 4 !
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

		// Build the SessionFactory !
		factory = configuration.buildSessionFactory(serviceRegistry);
	}
			
	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;

		try {
			tx = session.beginTransaction();
			Employee employee = new Employee();
			employee.setFirstName(fname);
			employee.setLastName(lname);
			employee.setSalary(salary);
			employeeID = (Integer) session.save(employee);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			
		} finally {
			session.close();
		}
		return employeeID;
	}

	/* Method to READ all the employees */
	public List<Employee> listEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Employee> employees= null;
		try {
			tx = session.beginTransaction();
			employees = session.createQuery("FROM Employee").list();		
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employees;
	}

	public void updateEmployee(Integer EmployeeID, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */ 
	public void deleteEmployee(Integer EmployeeID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			session.delete(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	

	
}
