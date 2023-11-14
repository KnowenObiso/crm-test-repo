package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.scheduler.SchedulerConfig;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;


//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobBuilder;
//import org.quartz.JobKey;
//import org.quartz.TriggerBuilder;
//import org.quartz.TriggerKey;


public class BirthdayAlert implements Job {
    public BirthdayAlert() {
        super();
    }

    public OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;

        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);

            conn = (OracleConnection)ds.getConnection();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return conn;
    }

    public void execute(JobExecutionContext jobExecutionContext) {
        //DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = this.getDatabaseConnection();

        String content = null;
        // content = MailManipulation.messageTemplates("B_DAY");


        try {

            String alertQuery =
                "begin ? := Tqc_Sms_Pkg.getBirthDayAlerts(?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(alertQuery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, "CLIENT_BIRTHDAY");
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                content = rs.getString(5);
                content = content.replace("[NAME]", rs.getString(2));
                content = content.replace("[DATE]", rs.getString(3));

                GlobalCC.applicationEmail(rs.getString(8), null,
                                          "BIRTHDAY REMINDER", content);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public String makeTrigger() {
        try {
            SchedulerConfig config = new SchedulerConfig();
           ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
            ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            sched.start();
            //            JobKey sampleKey = new JobKey("BirthDay Alert", "TurnQuestJobs");
            //            TriggerKey triggerKey =
            //                new TriggerKey("BirthDay Alert" + "Trig", "TurnQuestJobs");
            //            JobDetail jobDetail =
            //                JobBuilder.newJob(BirthdayAlert.class).withIdentity(sampleKey).build();
            //            jobDetail.getJobDataMap().put("type", "BirthDay Alert");
            //            Trigger trigger =
            //                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule("0 33 15 ? * MON-FRI")).build();
            //            sched.scheduleJob(jobDetail, trigger);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
