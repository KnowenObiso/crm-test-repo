package TurnQuest.view.Alerts;


import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.scheduler.SchedulerConfig;

import java.sql.SQLException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.commons.dbutils.DbUtils;

import org.quartz.JobExecutionContext;


//import org.quartz.CronScheduleBuilder;
//import org.quartz.Job;
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobKey;
//import org.quartz.Scheduler;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.quartz.TriggerKey;


public class SendBirthdayMessages {
    public void execute(JobExecutionContext jobExecutionContext) {

        boolean sendSms;
        System.out.println("This is ok njega");
        sendSms = isBirthDay();
        System.out.println("This is ok njega" + sendSms);
        if (sendSms == true) {
            System.out.println("This is ok njega kanu");
            getClientsBirthDaysDue();
            System.out.println("This is ok njega clients");
        }


    }

    public void getClientsBirthDaysDue() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        try {
            System.out.println("This is ok njega clients uy");
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getClientBirthDays;end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                String Query =
                    "begin tqc_clients_pkg.saveBirthDayMessages(?,?,?,?,?,?); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(Query);
                if (rst.getString(4) == null) {
                    continue;
                }
                stmt.setInt(1, 0);
                stmt.setString(2, "OC");
                stmt.setBigDecimal(3, rst.getBigDecimal(1));
                stmt.setString(4, rst.getString(4));
                stmt.setString(5, "D");
                stmt.setString(6, rst.getString(2));
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            // GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
    }

    public boolean isBirthDay() {
        String site = "NONE";
        site = new Util().getBirthDay();
        if (site.toString().equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String makeTrigger(String cronExpression) {
        try {

            System.out.println("This is ok");
            SchedulerConfig config = new SchedulerConfig();
            //            Scheduler sched = config.getScheduler();
            //            sched.start();
            //          JobKey sampleKey=new JobKey("BirthDay Alert", "TurnQuestJobs");
            //          TriggerKey triggerKey=new TriggerKey("BirthDay Alert"+"Trig", "TurnQuestJobs");
            //          JobDetail jobDetail=JobBuilder.newJob(SendBirthdayMessages.class).withIdentity(sampleKey).build();
            //            jobDetail.getJobDataMap().put("type", "BirthDay Alert");
            //          Trigger trigger=TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            //            sched.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            //  e.printStackTrace();
        }
        return null;
    }
}
