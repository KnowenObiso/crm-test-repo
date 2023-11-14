package TurnQuest.view.scheduler;


import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class AlertJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) {

        Map dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        // System.out.println("Size.."+dataMap.size());
        // RenewalAlert task = new RenewalAlert();
        // task.createRenewalTicket();

    }
}
