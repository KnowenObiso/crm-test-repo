package TurnQuest.view.scheduler;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerConfig {

    private static String KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

    public Scheduler getScheduler(ServletContextEvent contextEvent) {


       ServletContext servletContext = contextEvent.getServletContext(); 
      Enumeration list =  contextEvent.getServletContext().getAttributeNames();
      while(list.hasMoreElements()){
          System.out.println(list.nextElement());
      }
        StdSchedulerFactory factory =
            (StdSchedulerFactory)servletContext.getAttribute(KEY);
        Scheduler quartzScheduler = null;

        try {
             if(factory!=null) {
                quartzScheduler = factory.getScheduler("MyQuartzScheduler"); 
             }
        } catch (SchedulerException e) {
            quartzScheduler = null;
        }
        return quartzScheduler;
    }


    public Scheduler getScheduler(ServletContext servletContext) {

        StdSchedulerFactory factory =
            (StdSchedulerFactory)servletContext.getAttribute(KEY);
        Scheduler quartzScheduler = null;

        try {
            if(factory!=null) {
               quartzScheduler = factory.getScheduler("MyQuartzScheduler"); 
            }
        } catch (SchedulerException e) {
            quartzScheduler = null;
        }
        return quartzScheduler;
    }


}
