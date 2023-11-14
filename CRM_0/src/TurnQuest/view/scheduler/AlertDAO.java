package TurnQuest.view.scheduler;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;


//import org.quartz.JobKey;
//import org.quartz.TriggerKey;
//import org.quartz.impl.matchers.GroupMatcher;


public class AlertDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<AlertBean> getCurrentRunningJobs() {
        List<AlertBean> alerts = new ArrayList<AlertBean>();
        try {
            SchedulerConfig config = new SchedulerConfig();
            ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
             ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            if(sched!=null) {
                for (String groupName : sched.getJobGroupNames()) {

                    for (TriggerKey jobKey :
                         sched.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {


                        //get job's trigger
                        Trigger trigger = sched.getTrigger(jobKey);

                        Date nextFireTime = trigger.getNextFireTime();
                        Date prevFireTime = trigger.getPreviousFireTime();
                        CronTrigger cron = null;
                        if (trigger instanceof CronTrigger) {
                            cron = (CronTrigger)trigger;

                        }
                        boolean isPaused =
                            sched.getTriggerState(jobKey) == Trigger.TriggerState.PAUSED;
                        AlertBean alert = new AlertBean();
                        alert.setQtJobName(trigger.getJobKey().getName());
                        alert.setQtDescription(trigger.getJobKey().getName());
                        alert.setQtNextFireTime(nextFireTime);
                        alert.setQtPrevFireTime(prevFireTime);
                        alert.setQtStatus((isPaused) ? "Paused" : "Running");
                        alert.setObjLocation((isPaused) ? "Resume" : "Pause");
                        alert.setQtCronExpresion(cron.getCronExpression());
                        alerts.add(alert);

                    }

                }   
            }

            


        } catch (Exception e) {
            //e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return alerts;
    }

    //  public boolean checkExists(List<AlertBean> alerts,JobKey key){
    //      for(AlertBean alert:alerts){
    //          if(alert.getQtJobName().equals(key.getName())){
    //            return true;
    //          }
    //      }
    //    return false;
    //  }


    public List<AlertBean> findSchedules() {
        String query = "begin ? := TQC_SETUPS_CURSOR.get_alerts(?,?); end;";
        List<AlertBean> alerts = new ArrayList<AlertBean>();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, null);
            cst.setObject(3, session.getAttribute("sysCode"));
            cst.execute();
            ResultSet rst = (ResultSet)cst.getObject(1);
            while (rst.next()) {
                AlertBean alert = new AlertBean();
                alert.setQtJobName(rst.getString(1));
                alert.setQtDescription(rst.getString(2));
                alert.setQtNextFireTime(rst.getDate(3));
                alert.setQtPrevFireTime(rst.getDate(4));
                alert.setQtStartTime(rst.getTimestamp(5));
                alert.setQtEndTime(rst.getDate(6));
                alert.setQtCode(rst.getBigDecimal(7));
                alert.setQtSysCode(rst.getBigDecimal(8));
                alert.setQtRecurrence(rst.getString(9));
                alert.setQtRecurrenceType(rst.getString(10));
                alert.setQtJobAssignee(rst.getBigDecimal(11));
                alert.setQtNotifiedFailUser(rst.getBigDecimal(12));
                alert.setQtNotifiedSuccUser(rst.getBigDecimal(13));
                alert.setQtReccurenceInterval(rst.getString(14));
                alert.setQtJobType(rst.getString(15));
                alert.setQtJobTemplate(rst.getString(16));
                alert.setQtFailNotifyTemplate(rst.getString(17));
                alert.setQtSuccNotifyTemplate(rst.getString(18));
                alert.setAssignee(rst.getString(19));
                alert.setFailUser(rst.getString(20));
                alert.setSuccUser(rst.getString(21));
                alert.setJobTemplate(rst.getString(22));
                alert.setFailTemplate(rst.getString(23));
                alert.setSuccTemplate(rst.getString(24));
                alert.setAssigneeEmail(rst.getString(25));
                alert.setFailEmail(rst.getString(26));
                alert.setSuccEmail(rst.getString(27));
                alert.setQtStatus(rst.getString(28));
                alert.setQtThreshType(rst.getString(29));
                alert.setQtThreshValue(rst.getBigDecimal(30));
                alerts.add(alert);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return alerts;
    }

    public List<AlertBean> findAllSchedules() {
        String query = "begin ? := TQC_SETUPS_CURSOR.get_alerts(); end;";
        List<AlertBean> alerts = new ArrayList<AlertBean>();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            ResultSet rst = (ResultSet)cst.getObject(1);
            while (rst.next()) {
                AlertBean alert = new AlertBean();
                alert.setQtJobName(rst.getString(1));
                alert.setQtDescription(rst.getString(2));
                alert.setQtNextFireTime(rst.getDate(3));
                alert.setQtPrevFireTime(rst.getDate(4));
                alert.setQtStartTime(rst.getTimestamp(5));
                alert.setQtEndTime(rst.getDate(6));
                alert.setQtCode(rst.getBigDecimal(7));
                alert.setQtSysCode(rst.getBigDecimal(8));
                alert.setQtRecurrence(rst.getString(9));
                alert.setQtRecurrenceType(rst.getString(10));
                alert.setQtJobAssignee(rst.getBigDecimal(11));
                alert.setQtNotifiedFailUser(rst.getBigDecimal(12));
                alert.setQtNotifiedSuccUser(rst.getBigDecimal(13));
                alert.setQtReccurenceInterval(rst.getString(14));
                alert.setQtJobType(rst.getString(15));
                alert.setQtJobTemplate(rst.getString(16));
                alert.setQtFailNotifyTemplate(rst.getString(17));
                alert.setQtSuccNotifyTemplate(rst.getString(18));
                alert.setAssignee(rst.getString(19));
                alert.setFailUser(rst.getString(20));
                alert.setSuccUser(rst.getString(21));
                alert.setJobTemplate(rst.getString(22));
                alert.setFailTemplate(rst.getString(23));
                alert.setSuccTemplate(rst.getString(24));
                alert.setAssigneeEmail(rst.getString(25));
                alert.setFailEmail(rst.getString(26));
                alert.setSuccEmail(rst.getString(27));
                alert.setQtStatus(rst.getString(28));
                alert.setQtThreshType(rst.getString(29));
                alert.setQtThreshValue(rst.getBigDecimal(30));
                alert.setQtCronExpresion(rst.getString(32));
                alerts.add(alert);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return alerts;
    }

    public List<AlertBean> findExecutionObjects() {
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?); end;";
        List<AlertBean> alerts = new ArrayList<AlertBean>();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, session.getAttribute("sysCode"));
            cst.setObject(3, session.getAttribute("type"));
            cst.execute();
            ResultSet rst = (ResultSet)cst.getObject(1);
            while (rst.next()) {
                AlertBean alert = new AlertBean();
                alert.setObjType(rst.getString(1));
                alert.setObjCode(rst.getBigDecimal(2));
                alert.setObjDesc(rst.getString(3));
                alert.setObjLocation(rst.getString(4));
                alerts.add(alert);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return alerts;
    }

}
