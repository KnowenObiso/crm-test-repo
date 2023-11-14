package TurnQuest.view.Base;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
 
public class HibernateUtil {
 private static SessionFactory sessionFactory = null; 
 public static SessionFactory getSessionFactory() {
     if(sessionFactory == null){ 
    	 try {
               sessionFactory = new AnnotationConfiguration().configure("tq.hibernate.cfg.xml").addPackage("TurnQuest.view.models") //the fully qualified package name.addAnnotatedClass(City.class)
               .buildSessionFactory();
    	} catch (Throwable ex) {
           sessionFactory = null;
           System.err.println("Initial SessionFactory creation failed." + ex);
           throw new ExceptionInInitializerError(ex); 
    	}
     }
  return sessionFactory;
 }
 public static Session getSession() throws HibernateException {
		return getSessionFactory().openSession();
	}
}