package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.scheduler.SchedulerConfig;
import TurnQuest.view.serviceRequests.EmailServiceRequests;

import java.io.File;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.jbpm.api.task.Task;

import org.quartz.CronScheduleBuilder;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.Job;
import org.quartz.JobBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;


//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobBuilder;
//import org.quartz.JobKey;
//import org.quartz.TriggerBuilder;
//import org.quartz.TriggerKey;
//import org.quartz.impl.matchers.GroupMatcher;


//import monfox.toolkit.snmp.agent.impl.Task;


public class SystemAlerts implements Job {
    
    public SystemAlerts() {
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

            // userVaraibleInitialization(conn);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return conn;
    }

    public String removeJob(String jobname) {
        String success = null;
        try {
            SchedulerFactory schedFact = new StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();

            sched.start();
            JobKey jobKey = new JobKey(jobname, jobname);
            //List<String> myJobs = new ArrayList<String>();

            //sched.unscheduleJob(jobname, jobname);
            unscheduleJob(jobname);
            if(sched!=null) {
                 sched.deleteJob(jobKey);
            } 
            success = "success";
            System.out.println(success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    /* public String removeJob(String jobname) {
        String success = null;
        try {
            SchedulerConfig config=new SchedulerConfig();
            Scheduler sched = config.getScheduler();
            sched.start();
            //sched.unscheduleJob(jobname, jobname);
            unscheduleJob(jobname);
            success = "success";
            System.out.println(success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }*/

    //   public boolean checkJobExists(String jobname,ServletContext context)  {
    //       try{
    //     SchedulerConfig config=new SchedulerConfig();
    //       Scheduler sched = config.getScheduler(context);
    //       List<String> triggerGrps=sched.getTriggerGroupNames();
    //     for(String stTriggerGrp : triggerGrps){
    //               GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(stTriggerGrp);
    //               Set<TriggerKey> keys = sched.getTriggerKeys(groupMatcher);
    //               for (TriggerKey k : keys) {
    //                   if(k.getName().equalsIgnoreCase(jobname+"Trig")){
    //                    if(sched.checkExists(k))
    //                     return true;
    //                   }
    //               }
    //        }
    //       }
    //       catch(SchedulerException ex){
    //         return false;
    //       }
    //     return false;
    //   }
    //


    public boolean unscheduleJob(String jobname) {
        try {
            SchedulerConfig config = new SchedulerConfig();
            ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
             ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            if(sched!=null) {
                    List<String> triggerGrps = sched.getTriggerGroupNames();
                for (String stTriggerGrp : triggerGrps) {
                    GroupMatcher<TriggerKey> groupMatcher =
                        GroupMatcher.groupEquals(stTriggerGrp);
                    Set<TriggerKey> keys = sched.getTriggerKeys(groupMatcher);
                    for (TriggerKey k : keys) {
                        if (k.getName().equalsIgnoreCase(jobname + "Trig")) {
                            if (sched.checkExists(k))
                                sched.unscheduleJob(k);
                        }
                    }
                  }
            }            
        } catch (SchedulerException ex) {
            return false;
        }
        return false;
    }

    public void stopJob(String jobname) {
        try {
            SchedulerConfig config = new SchedulerConfig();
            ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
             ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            
            if(sched!=null) {
                List<String> triggerGrps = sched.getTriggerGroupNames();
                   for (String stTriggerGrp : triggerGrps) {
                    GroupMatcher<TriggerKey> groupMatcher =
                        GroupMatcher.groupEquals(stTriggerGrp);
                    Set<TriggerKey> keys = sched.getTriggerKeys(groupMatcher);
                    for (TriggerKey k : keys) {
                        if (k.getName().equalsIgnoreCase(jobname + "Trig")) {
                            if (sched.checkExists(k)) {
                                sched.unscheduleJob(k);
                            }
                        }
                    }
                } 
            }
            
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }

    }

    //  public String startJobs(String jobname, String cronExpression,ServletContext context) {
    //      try {
    //        SchedulerConfig config=new SchedulerConfig();
    //          Scheduler sched = config.getScheduler(context);
    //          sched.start();
    //        JobKey sampleKey=new JobKey(jobname, "TurnQuestJobs");
    //        TriggerKey triggerKey=new TriggerKey(jobname+"Trig", "TurnQuestJobs");
    //        JobDetail jobDetail=JobBuilder.newJob(SystemAlerts.class).withIdentity(sampleKey).build();
    //          jobDetail.getJobDataMap().put("type", "BirthDay Alert");
    //        Trigger trigger=TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
    //          sched.scheduleJob(jobDetail, trigger);
    //          System.out.println(jobname);
    //      } catch (Exception e) {
    //          e.printStackTrace();
    //      }
    //      return null;
    //  }


    public String makeTrigger(String jobname, String cronExpression,ServletContext context,int i) {
        try {
            SchedulerConfig config = new SchedulerConfig();
            Scheduler sched = config.getScheduler(context);
            JobKey sampleKey = new JobKey(jobname, "TurnQuestJobs");
            TriggerKey triggerKey = new TriggerKey(jobname + "Trig", "TurnQuestJobs");
            JobDetail jobDetail =
                JobBuilder.newJob(SystemAlerts.class).withIdentity(sampleKey).build();
            jobDetail.getJobDataMap().put("type", "BirthDay Alert");
            Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            sched.scheduleJob(jobDetail, trigger);
            sched.start();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   

    public void scheduleJob(String jobname, String cronExpression) {
        try {
            
            SchedulerFactory sf = new StdSchedulerFactory();
            org.quartz.Scheduler sched = sf.getScheduler();
            
            JobDetail job =
                newJob(SystemAlerts.class)
                .withIdentity(jobname, jobname)
                .usingJobData(jobname,jobname)
                .build();

            System.out.println("jobname: "+jobname+" cronExpression: "+cronExpression);
            Trigger trigger =
                newTrigger()
                .withIdentity(jobname, jobname)
                .startNow()
                .withSchedule(cronSchedule(cronExpression))  //4 * * * * ? *
                .forJob(jobname,jobname)
                .build();
            
            boolean exist = sched.checkExists(job.getKey());
            if (exist) {
                System.out.println("Job Exists");
                sched.deleteJob(job.getKey());
            }
            sched.scheduleJob(job, trigger);
            sched.start();
            
            //System.out.println("job.getKey(): "+job.getKey());
            //System.out.println("trigger.getNextFireTime(): "+trigger.getNextFireTime());
            //System.out.println("sched.checkExists(job.getKey()): "+sched.checkExists(job.getKey()));


            JobDetail detail = sched.getJobDetail(job.getKey());
            //sched.getTriggersOfJob(arg0)
            List<? extends Trigger> triggers =
                sched.getTriggersOfJob(job.getKey());

            //var myTrigger = triggers.Where(f => f.Key.Name == "SecondTrigger").SingleOrDefault();

            int k = 0;
            while (k < triggers.size()) {
                System.out.println("triggers["+k+"]'s nextFireTimeUtc"+triggers.get(k).getNextFireTime());
                k++;
            }
            // System.out.println(nextFireTime);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("This is OK");
        String url = null;
        String msgUrl = null;
        String username = null;
        String password = null;
        String source = null;
        String destination = null;
        String message = null;
        String smsCode = null;
        System.out.println("Gata");
        //List alerts=new ArrayList();
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        System.out.println(jobName);
        String query = "begin ? := TQC_SETUPS_CURSOR.get_alerts(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            System.out.println("This is OK 3");
            conn = getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, jobName);
            cst.execute();
            OracleResultSet rst = (OracleResultSet)cst.getObject(1);
            OracleResultSet resultset = (OracleResultSet)cst.getObject(1);
            String successMsg = null;
            String failMsg = null;
            String jobType = null;
            String succeedUserEmail = null;
            String assigneeEmail = null;
            String failUserEmail = null;
            String assignee = null;
            BigDecimal assigneeUserCode = null;
            String content = null;
            String code = null;
            BigDecimal sysCode = null;
            Object success = null;
            String assigneeType = "U";
            System.out.println("This is OK 4");
            while (rst.next()) {
                /*  AlertBean alert = new AlertBean();
                alert.setQtJobName(rst.getString(1));
                  alert.setQtDescription(rst.getString(2));
                  alert.setQtNextFireTime(rst.getDate(3));
                  alert.setQtPrevFireTime(rst.getDate(4));
                  alert.setQtStartTime(rst.getTimestamp(5));
                  alert.setQtEndTime(rst.getDate(6));
                  alert.setQtCode(rst.getBigDecimal(7));
                  alert.setQtSysCode(rst.getBigDecimal(8));
                  alert.setQtReccurenceInterval(rst.getString(9));
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
                  alerts.add(alert);*/
                assigneeUserCode = rst.getBigDecimal(11);
                sysCode = rst.getBigDecimal(8);
                assignee = rst.getString(19);
                code = rst.getString(16);
                jobType = rst.getString(15);
                assigneeEmail = rst.getString(25);
                failUserEmail = rst.getString(26);
                succeedUserEmail = rst.getString(27);
                assigneeType = rst.getString(31);
                content = rst.getString(22);
                successMsg = rst.getString(24);
                failMsg = rst.getString(23);
                if (assigneeType != null && assigneeType.equals("G")) {
                    getGroupUsers(assigneeUserCode, jobName, jobType,
                                  successMsg, code, succeedUserEmail,
                                  failUserEmail, failMsg, assignee, sysCode,
                                  successMsg, conn);
                    continue;
                }
            }
            System.out.println("This is OK 5");
            // rst.close();
            cst.close();
            conn.close();

            if (jobName.equalsIgnoreCase("INCOMING_EMAIL")) {
                conn = getDatabaseConnection();
                EmailServiceRequests esr = new EmailServiceRequests();
                esr.chechEmails(conn);
                conn.close();
            } else if (jobName.equalsIgnoreCase("OUTGOING_SMS")) {

                conn = getDatabaseConnection();

                cst = null;
                String medicalQuery =
                    "begin ? := TQC_SETUPS_CURSOR.getDefaultSmsSettings; end;";
                cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

                //register out
                cst.registerOutParameter(1, OracleTypes.CURSOR);

                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    msgUrl = rs.getString(2);
                    username = rs.getString(3);
                    password = rs.getString(4);
                    source = rs.getString(5);
                }

                //  rs.close();
                cst.close();
                System.out.println("This is OK 6");
                medicalQuery =
                        "begin ? := TQC_SETUPS_CURSOR.getPendingSms; end;";
                cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

                //register out
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.execute();
                rs = (OracleResultSet)cst.getObject(1);
                while (rs.next()) {
                    smsCode = rs.getString(1);
                    destination = rs.getString(2);
                    message = rs.getString(3);
                    url = msgUrl;
                    url =
url.replace("[USERNAME]", URLEncoder.encode(username));
                    url =
url.replace("[PASSWORD]", URLEncoder.encode(password));
                    url = url.replace("[SOURCE]", URLEncoder.encode(source));
                    url =
url.replace("[DESTINATION]", URLEncoder.encode(destination));
                    url = url.replace("[MESSAGE]", URLEncoder.encode(message));
                    URL url2 = new URL(url);
                    //URLEncoder.encode(arg0, arg1)
                    HttpURLConnection connection =
                        (HttpURLConnection)url2.openConnection();
                    connection.setDoOutput(false);
                    connection.setDoInput(true);

                    System.out.println("connection :" + connection);
                    String res = connection.getResponseMessage();

                    System.out.println("res :" + res);
                    medicalQuery =
                            "UPDATE TQC_SMS_MESSAGES SET SMS_STATUS = '" +
                            res + "' WHERE SMS_CODE = " + smsCode + " ";
                    cst =
(OracleCallableStatement)conn.prepareCall(medicalQuery);
                    cst.execute();
                    System.out.println("This is OK 7");

                    int rsCode;
                    rsCode = connection.getResponseCode();
                    if (rsCode == HttpURLConnection.HTTP_OK) {
                        connection.disconnect();
                    }
                }
                cst.close();
                conn.close();


            } else if (jobType == null) {
                return;
            } else if (jobType.equalsIgnoreCase("A")) {
                if (assigneeType.equalsIgnoreCase("U"))
                    success =
                            GlobalCC.applicationEmail(assigneeEmail, null, jobName,
                                                      content);
                else {
                    List<String> receivers =
                        getGroupedUsersEmails(assigneeUserCode);
                    for (int i = 0; i < receivers.size(); i++)
                        success =
                                GlobalCC.applicationEmail(receivers.get(i), null,
                                                          jobName, content);
                }
                GlobalCC.applicationEmail(assigneeEmail, null, jobName,
                                          content);
                System.out.println("Success " + assigneeEmail + " - " +
                                   jobName + " - " + content);
            } else if (jobType.equalsIgnoreCase("R")) {

                query =
                        "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                cst = null;
                conn = getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setString(2, null);
                cst.setString(3, "RPT");
                cst.setString(4, code);
                cst.execute();
                rst = (OracleResultSet)cst.getObject(1);
                String location = null;
                String desc = null;
                while (rst.next()) {
                    desc = rst.getString(3);
                    location = rst.getString(4);
                }
                //rst.close();
                cst.close();
                conn.close();
                ReportAlert rpt = new ReportAlert();
                File file = null;
                file = rpt.SaveReportOne(new BigDecimal(code), location);
                if (file != null) {

                    if (assigneeType.equalsIgnoreCase("U"))
                        success =
                                GlobalCC.applicationEmailDoc(assigneeEmail, null,
                                                             desc,
                                                             "Attached :" +
                                                             desc,
                                                             file.getAbsolutePath());
                    else {
                        List<String> receivers =
                            getGroupedUsersEmails(assigneeUserCode);
                        for (int i = 0; i < receivers.size(); i++)
                            success =
                                    GlobalCC.applicationEmailDoc(receivers.get(i),
                                                                 null, desc,
                                                                 "Attached :" +
                                                                 desc,
                                                                 file.getAbsolutePath());
                    }


                }
                System.out.println("This is OK 9");
            } else if (jobType.equalsIgnoreCase("W")) {

                query =
                        "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                cst = null;
                conn = getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setString(2, null);
                cst.setString(3, null);
                cst.setString(4, code);
                cst.execute();
                rst = (OracleResultSet)cst.getObject(1);
                String location = null;
                String desc = null;
                while (rst.next()) {
                    desc = rst.getString(3);
                    location = rst.getString(4);
                    System.out.println(rst.getString(2));
                    System.out.println(desc);
                    System.out.println(location);
                }
                // rst.close();
                cst.close();
                conn.close();
                HttpSession session =
                    (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                JBPMEngine bpm = JBPMEngine.getInstance();
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("assignee", assignee);
                String processID = null;
                processID = bpm.startWorkflow(location, variables);

                bpm.signalProcessInstanceExecution(processID);
                List<Task> Tasks = new ArrayList<Task>();
                Tasks = bpm.findTasksForWorkflowPath(processID);
                int i = 0;
                for (Iterator iter = Tasks.iterator(); iter.hasNext(); ) {
                    Task task = Tasks.get(i);
                    variables.put("taskid", task.getId());
                    variables.put("instance", processID);
                    variables.put("sysCode", sysCode);
                    iter.next();
                    i++;
                    CreateProcessAttributes(variables);
                }

                GlobalCC.applicationEmail(assignee, null, jobName, content);
                System.out.println("Success " + assignee + " - " + jobName +
                                   " - " + content);
            } else if (jobType.equalsIgnoreCase("J")) {

                query =
                        "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                cst = null;
                conn = getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setString(2, null);
                cst.setString(3, "J");
                cst.setString(4, code);
                cst.execute();
                resultset = (OracleResultSet)cst.getObject(1);
                String functionQuery = null;
                while (resultset.next()) {
                    functionQuery = resultset.getString(4);
                    String routineName;
                    routineName = resultset.getString(3);
                    if (functionQuery.contains("(?)")) {
                        System.out.println("This is content and email" +
                                           assigneeEmail + "content" +
                                           content);
                        if (functionQuery != null) {
                            cst = null;
                            conn = getDatabaseConnection();
                            cst =
(OracleCallableStatement)conn.prepareCall(functionQuery);
                            cst.registerOutParameter(1, OracleTypes.CURSOR);
                            cst.setString(2, null);
                            cst.execute();
                            rst = null;
                            rst = (OracleResultSet)cst.getObject(1);
                            while (rst.next()) {
                                //assigneeEmail = rst.getString(1);
                                GlobalCC.applicationEmail(assigneeEmail, null,
                                                          routineName,
                                                          successMsg);
                            }
                            //resultset.close();
                            //cst.close();
                        }

                    }
                }
                // rst.close();
                //cst.close();
                if (functionQuery != null) {
                    cst = null;
                    conn = getDatabaseConnection();
                    cst =
(OracleCallableStatement)conn.prepareCall(functionQuery);
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.execute();
                    rst = null;
                    resultset = (OracleResultSet)cst.getObject(1);
                    while (resultset.next()) {
                        jobName = resultset.getString(1);
                        assigneeEmail = resultset.getString(2);
                        content = resultset.getString(3);
                        GlobalCC.applicationEmail(assigneeEmail, null, jobName,
                                                  content);
                    }
                    // rst.close();
                    cst.close();
                }

                conn.close();

            }
            System.out.println("This is OK 11");
            System.out.println("success= " + success);
            if (success == null) {
                System.out.println("Sending failure message= ");
                GlobalCC.applicationEmail(failUserEmail, null, jobName,
                                          failMsg);
            } else {
                System.out.println("Sending success message= ");
                GlobalCC.applicationEmail(succeedUserEmail, null, jobName,
                                          successMsg);
            }
            System.out.println("End of Execution");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

    }

    private String CreateProcessAttributes(Map ret) {
        OracleConnection conn = getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {


            String Updatewkflw =
                "BEGIN tqc_Web_Pkg.save_process_attributes(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);END;";

            cst = (OracleCallableStatement)conn.prepareCall(Updatewkflw);
            cst.setObject(1, ret.get("taskid"));
            cst.setObject(2, ret.get("sysCode"));
            cst.setString(3, "A");
            cst.setBigDecimal(4, null); //Client Code.
            cst.setBigDecimal(5, null); //Agent Code.
            cst.setBigDecimal(6, null); //Policy Code.
            cst.setString(7, null); //Policy Number
            cst.setString(8, null); //Claim No.
            cst.setBigDecimal(9, null); //Quotation Code.
            cst.setObject(10, ret.get("assignee")); //Username.
            cst.setString(11, null);
            cst.setObject(12, ret.get("instance")); //Process ID.
            cst.setString(13, null); //Quotation Number.
            cst.setBigDecimal(14, null); //Endorsement Code.
            cst.setString(15, null); //Product Short Desc.
            cst.setString(16, null); //Ticket Assignee.
            cst.setString(17, null); //Ticket Remarks.
            cst.setString(18, null); //Endorsement Type.
            cst.setBigDecimal(19,
                              null); //was this - (BigDecimal)session.getAttribute("transactionNumber"));

            cst.execute();
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getGroupedUsersEmails(BigDecimal groupCode) {

        List<String> mailList = new ArrayList<String>();
        SystemAlerts dbConnector = new SystemAlerts();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;

        String jobquery =
            "begin ? := TQC_SETUPS_CURSOR.getGroupedUsersEmails(?); end;";

        try {
            conn = dbConnector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(jobquery);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setObject(2, groupCode);
            cst.executeQuery();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                mailList.add(rs.getString(1));
            }


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return mailList;
    }

    public String formatMessage(String template, String[] inputs) {


        String output = template;

        for (int i = 0; i < inputs.length; i++) {
            int left = output.indexOf("[");
            int right = output.indexOf("]");
            String toReplace =
                output.copyValueOf(output.toCharArray(), left, right - left +
                                   1);
            output = output.replace(toReplace, inputs[i]);
        }


        return output;
    }

    private String getGroupUsers(BigDecimal assigneeUserCode, String jobName,
                                 String jobType, String content, String code,
                                 String succeedUserEmail, String failUserEmail,
                                 String failMsg, String assignee,
                                 BigDecimal sysCode, String successMsg,
                                 OracleConnection conn) {
        String url = null;
        String msgUrl = null;
        String username = null;
        String password = null;
        String source = null;
        String destination = null;
        String message = null;
        String smsCode = null;
        Object success = null;
        String assigneeType;
        String assigneeEmail;
        OracleResultSet resultset = null;
        OracleResultSet rsts = null;
        String query;
        String Query = "begin ? :=TQC_SETUPS_CURSOR.getGroupUsers(?); end;";
        //DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        //OracleConnection conn = null;

        try {

            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.registerOutParameter(1,
                                     oracle.jdbc.internal.OracleTypes.CURSOR);
            cst.setObject(2, assigneeUserCode);
            cst.execute();

            rsts = (OracleResultSet)cst.getObject(1);
            while (rsts.next()) {
                assigneeType = rsts.getString(4);
                assigneeEmail = rsts.getString(3);

                if (jobName.equalsIgnoreCase("INCOMING_EMAIL")) {
                    conn = getDatabaseConnection();
                    EmailServiceRequests esr = new EmailServiceRequests();
                    esr.chechEmails(conn);
                    conn.close();
                } else if (jobName.equalsIgnoreCase("OUTGOING_SMS")) {

                    conn = getDatabaseConnection();

                    cst = null;
                    String medicalQuery =
                        "begin ? := TQC_SETUPS_CURSOR.getDefaultSmsSettings; end;";
                    cst =
(OracleCallableStatement)conn.prepareCall(medicalQuery);

                    //register out
                    cst.registerOutParameter(1, OracleTypes.CURSOR);

                    cst.execute();
                    OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                    while (rs.next()) {
                        msgUrl = rs.getString(2);
                        username = rs.getString(3);
                        password = rs.getString(4);
                        source = rs.getString(5);
                    }

                    rs.close();
                    cst.close();
                    System.out.println("This is OK 6");
                    medicalQuery =
                            "begin ? := TQC_SETUPS_CURSOR.getPendingSms; end;";
                    cst =
(OracleCallableStatement)conn.prepareCall(medicalQuery);

                    //register out
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.execute();
                    rs = (OracleResultSet)cst.getObject(1);
                    while (rs.next()) {
                        smsCode = rs.getString(1);
                        destination = rs.getString(2);
                        message = rs.getString(3);
                        url = msgUrl;
                        url =
url.replace("[USERNAME]", URLEncoder.encode(username));
                        url =
url.replace("[PASSWORD]", URLEncoder.encode(password));
                        url =
url.replace("[SOURCE]", URLEncoder.encode(source));
                        url =
url.replace("[DESTINATION]", URLEncoder.encode(destination));
                        url =
url.replace("[MESSAGE]", URLEncoder.encode(message));
                        URL url2 = new URL(url);
                        //URLEncoder.encode(arg0, arg1)
                        HttpURLConnection connection =
                            (HttpURLConnection)url2.openConnection();
                        connection.setDoOutput(false);
                        connection.setDoInput(true);

                        System.out.println("connection :" + connection);
                        String res = connection.getResponseMessage();

                        System.out.println("res :" + res);
                        medicalQuery =
                                "UPDATE TQC_SMS_MESSAGES SET SMS_STATUS = '" +
                                res + "' WHERE SMS_CODE = " + smsCode + " ";
                        cst =
(OracleCallableStatement)conn.prepareCall(medicalQuery);
                        cst.execute();
                        System.out.println("This is OK 7");

                        int rsCode;
                        rsCode = connection.getResponseCode();
                        if (rsCode == HttpURLConnection.HTTP_OK) {
                            connection.disconnect();
                        }
                    }
                    cst.close();
                    conn.close();


                } else if (jobType == null) {
                    return null;
                } else if (jobType.equalsIgnoreCase("A")) {
                    if (assigneeType.equalsIgnoreCase("U"))
                        success =
                                GlobalCC.applicationEmail(assigneeEmail, null,
                                                          jobName, content);
                    else {
                        List<String> receivers =
                            getGroupedUsersEmails(assigneeUserCode);
                        for (int i = 0; i < receivers.size(); i++)
                            success =
                                    GlobalCC.applicationEmail(receivers.get(i),
                                                              null, jobName,
                                                              content);
                    }
                    GlobalCC.applicationEmail(assigneeEmail, null, jobName,
                                              content);
                    System.out.println("Success " + assigneeEmail + " - " +
                                       jobName + " - " + content);
                } else if (jobType.equalsIgnoreCase("R")) {

                    query =
                            "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                    cst = null;
                    conn = getDatabaseConnection();

                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.setString(2, null);
                    cst.setString(3, "RPT");
                    cst.setString(4, code);
                    cst.execute();
                    resultset = (OracleResultSet)cst.getObject(1);
                    String location = null;
                    String desc = null;
                    while (resultset.next()) {
                        desc = resultset.getString(3);
                        location = resultset.getString(4);
                    }
                    resultset.close();
                    cst.close();
                    conn.close();
                    ReportAlert rpt = new ReportAlert();
                    File file = null;
                    file = rpt.SaveReportOne(new BigDecimal(code), location);
                    if (file != null) {

                        if (assigneeType.equalsIgnoreCase("U"))
                            success =
                                    GlobalCC.applicationEmailDoc(assigneeEmail,
                                                                 null, desc,
                                                                 "Attached :" +
                                                                 desc,
                                                                 file.getAbsolutePath());
                        else {
                            List<String> receivers =
                                getGroupedUsersEmails(assigneeUserCode);
                            for (int i = 0; i < receivers.size(); i++)
                                success =
                                        GlobalCC.applicationEmailDoc(receivers.get(i),
                                                                     null,
                                                                     desc,
                                                                     "Attached :" +
                                                                     desc,
                                                                     file.getAbsolutePath());
                        }


                    }
                    System.out.println("This is OK 9");
                } else if (jobType.equalsIgnoreCase("W")) {

                    query =
                            "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                    cst = null;
                    conn = getDatabaseConnection();

                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.setString(2, null);
                    cst.setString(3, null);
                    cst.setString(4, code);
                    cst.execute();
                    resultset = null;
                    resultset = (OracleResultSet)cst.getObject(1);
                    String location = null;
                    String desc = null;
                    while (resultset.next()) {
                        desc = resultset.getString(3);
                        location = resultset.getString(4);
                        System.out.println(resultset.getString(2));
                        System.out.println(desc);
                        System.out.println(location);
                    }
                    resultset.close();
                    cst.close();
                    conn.close();
                    HttpSession session =
                        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                    JBPMEngine bpm = JBPMEngine.getInstance();
                    Map<String, Object> variables =
                        new HashMap<String, Object>();
                    variables.put("assignee", assignee);
                    String processID = null;
                    processID = bpm.startWorkflow(location, variables);

                    bpm.signalProcessInstanceExecution(processID);
                    List<Task> Tasks = new ArrayList<Task>();
                    Tasks = bpm.findTasksForWorkflowPath(processID);
                    int i = 0;
                    for (Iterator iter = Tasks.iterator(); iter.hasNext(); ) {
                        Task task = Tasks.get(i);
                        variables.put("taskid", task.getId());
                        variables.put("instance", processID);
                        variables.put("sysCode", sysCode);
                        iter.next();
                        i++;
                        CreateProcessAttributes(variables);
                    }

                    GlobalCC.applicationEmail(assignee, null, jobName,
                                              content);
                    System.out.println("Success " + assignee + " - " +
                                       jobName + " - " + content);
                } else if (jobType.equalsIgnoreCase("J")) {
                    System.out.println("This is the J alert");

                    query =
                            "begin ? := TQC_SETUPS_CURSOR.getExecutionObjects(?,?,?); end;";
                    cst = null;
                    conn = getDatabaseConnection();

                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.setString(2, null);
                    cst.setString(3, "J");
                    cst.setString(4, code);
                    cst.execute();
                    // resultset = null;
                    resultset = (OracleResultSet)cst.getObject(1);
                    String functionQuery = null;
                    System.out.println("This is the J alert exxx");
                    while (resultset.next()) {
                        System.out.println("This is the J alert ENTER HERE");
                        functionQuery = resultset.getString(4);
                        String routineName;
                        routineName = resultset.getString(3);
                        System.out.println("This is the J alert resultset.getBigDecimal(2)" +
                                           resultset.getBigDecimal(2));
                        System.out.println("This is the J alert functionQuery" +
                                           functionQuery);
                        if (functionQuery.contains("?")) {
                            System.out.println("This is the J alert ENTER HERE THEN");
                            if (functionQuery != null) {
                                cst = null;
                                conn = getDatabaseConnection();
                                cst =
(OracleCallableStatement)conn.prepareCall(functionQuery);
                                cst.registerOutParameter(1,
                                                         OracleTypes.CURSOR);
                                cst.setString(2, null);
                                cst.execute();
                                // resultset = null;
                                resultset = (OracleResultSet)cst.getObject(1);
                                while (resultset.next()) {
                                    System.out.println("This is the J alert finally");
                                    //assigneeEmail = resultset.getString(1);
                                    System.out.println("This is the J alert assigneeEmail" +
                                                       assigneeEmail +
                                                       "routineName" +
                                                       routineName +
                                                       "content" + content);
                                    success =
                                            GlobalCC.applicationEmail(assigneeEmail,
                                                                      null,
                                                                      routineName,
                                                                      content);
                                    System.out.println("This is the J alert success/failure" +
                                                       success);
                                }
                                //resultset.close();
                                // cst.close();
                            }

                        }
                    }
                    resultset.close();
                    cst.close();
                    if (functionQuery != null &&
                        !functionQuery.contains("?")) {
                        cst = null;
                        conn = getDatabaseConnection();
                        cst =
(OracleCallableStatement)conn.prepareCall(functionQuery);
                        cst.registerOutParameter(1, OracleTypes.CURSOR);
                        cst.execute();
                        resultset = null;
                        resultset = (OracleResultSet)cst.getObject(1);
                        while (resultset.next()) {
                            jobName = resultset.getString(1);
                            assigneeEmail = resultset.getString(2);
                            content = resultset.getString(3);
                            success =
                                    GlobalCC.applicationEmail(assigneeEmail, null,
                                                              jobName,
                                                              content);
                        }
                        resultset.close();
                        cst.close();
                    }

                    conn.close();

                }
                System.out.println("This is OK 11");
                System.out.println("success= " + success);
                if (success == null) {
                    System.out.println("sending failure message= ");
                    GlobalCC.applicationEmail(failUserEmail, null, jobName,
                                              failMsg);
                } else {
                    System.out.println("sending success message= ");
                    GlobalCC.applicationEmail(succeedUserEmail, null, jobName,
                                              successMsg);
                }
            }
            cst.close();
            //      conn.commit();
            // conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
