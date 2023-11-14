package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.scheduler.SchedulerConfig;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.SQLException;

import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;


public class clientsSms implements Job {
    public clientsSms() {
        super();
    }
    // HttpSession session =
    //  (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void execute(JobExecutionContext jobExecutionContext) {
        String message = null;
        String messageToSend = null;
        String telNumber = null;
        String url = null;
        URL urls;
        HttpURLConnection conns = null;
        String res;
        String responseMessage = null;
        int responseCode = 0;
        int count = 0;
        DBConnector datahandler = new DBConnector();
        //DBConnector dbConnector = new DBConnector();
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        OracleResultSet rs = null;
        try {
            connection = datahandler.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.selectallMessages(?,?,?,?);end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, 37);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                message = rs.getString(10);
                messageToSend = rs.getString(10);
                telNumber = rs.getString(9);
                String clientName = rs.getString(15);
                String phoneNumber = null;
                if (message != null) {
                    phoneNumber = message;
                }
                String regex = "^[+0-9]{10,25}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = null;
                boolean matches = false;
                if (phoneNumber != null) {
                    matcher = pattern.matcher(phoneNumber);
                    matches = matcher.matches();
                }
                BigDecimal except = new BigDecimal(rs.getString(1).toString());

                connection = datahandler.getDatabaseConnection();
                String query13 =
                    "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
                stmt =
(OracleCallableStatement)connection.prepareCall(query13);
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.execute();
                rst = (OracleResultSet)stmt.getObject(1);
                int j = 0;

                while (rst.next()) {
                    String messages = messageToSend;
                    if (message == null || telNumber == null) {

                    } else
                        messages = java.net.URLEncoder.encode(messages);
                    url = rst.getString(2);
                    url = url.replace("[USERNAME]", rst.getString(3));
                    url = url.replace("[PASSWORD]", rst.getString(4));
                    url = url.replace("[SOURCE]", rst.getString(5));
                    url = url.replace("[MESSAGE]", messages);
                    url = url.replace("[DESTINATION]", telNumber);
                    j++;
                }

                rst.close();
                Timer timer = new Timer(true);
                InterruptTimerTask interruptTimerTask =
                    new InterruptTimerTask(Thread.currentThread());
                timer.schedule(interruptTimerTask, 1000);

                urls = new URL(url);
                conns = (HttpURLConnection)urls.openConnection();
                conns.setDoOutput(false);
                conns.setDoInput(true);
                res = conns.getResponseMessage();
                responseCode = conns.getResponseCode();
                String Binders = "begin tqc_setups_pkg.smsStatus(?,?,?); end;";
                stmt =
(OracleCallableStatement)connection.prepareCall(Binders);
                stmt.setString(1, "OK");
                stmt.setBigDecimal(2, except);
                stmt.setObject(3, responseCode);
                stmt.execute();
                stmt.close();
                int rsCode = conns.getResponseCode();
                if (rsCode == HttpURLConnection.HTTP_OK) {
                    conns.disconnect();
                }


                count++;

            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, stmt, rs);
        }
    }

    public void sendBirthdayMessages() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getPoliciesOneMonthsRen;end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                String Query =
                    "begin tqc_clients_pkg.saveRenewalMessages(?,?,?,?,?,?,?,?,?,?); end;";
                //   DBConnector connector = new DBConnector();
                // OracleCallableStatement cst = null;
                //OracleConnection conn = null;
                //  conn = connector.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(Query);

                stmt.setInt(1, 37);
                stmt.setString(2, "RN");
                stmt.setBigDecimal(3, rst.getBigDecimal(4));
                stmt.setBigDecimal(4, rst.getBigDecimal(7));
                stmt.setString(5, null);
                stmt.setString(6, rst.getString(1));
                stmt.setString(7, rst.getString(6));
                stmt.setString(8, "D");
                stmt.setDate(9, rst.getDate(3));
                stmt.setString(10, rst.getString(5));
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
    }
    //  public void execute1(JobExecutionContext jobExecutionContext) {
    //      String message=null;
    //      String messageToSend=null;
    //      String telNumber=null;
    //      String url = null;
    //      URL urls;
    //      HttpURLConnection conns=null;
    //      String res;
    //      String responseMessage=null;
    //      int responseCode=0;
    //      int count=0;
    //      DBConnector datahandler = new DBConnector();
    //      //DBConnector dbConnector = new DBConnector();
    //      OracleConnection connection = null;
    //      OracleCallableStatement stmt = null;
    //      OracleResultSet rs,rst = null;
    //        try {
    //           connection = datahandler.getDatabaseConnection();
    //              String query1 =
    //                  "begin ? := tqc_setups_cursor.selectallMessages(?); end;";
    //              stmt =
    //              (OracleCallableStatement)connection.prepareCall(query1);
    //              stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //              stmt.setObject(2, 37);
    //              stmt.execute();
    //              rs = (OracleResultSet)stmt.getObject(1);
    //              while (rs.next()) {
    //                  message =rs.getString(10);
    //                  messageToSend=rs.getString(10);
    //                  telNumber=rs.getString(9);
    //                  String clientName=rs.getString(15);
    //                   String phoneNumber = null;
    //                    if (message != null) {
    //                        phoneNumber = message;
    //                    }
    //                    String regex = "^[+0-9]{10,25}$";
    //                    Pattern pattern = Pattern.compile(regex);
    //                    Matcher matcher = null;
    //                    boolean matches = false;
    //                  if (phoneNumber != null) {
    //                        matcher = pattern.matcher(phoneNumber);
    //                        matches = matcher.matches();
    //                    }
    //                   BigDecimal except =
    //                       new BigDecimal(rs.getString(1).toString());
    //
    //                  connection = datahandler.getDatabaseConnection();
    //                            String query13 =
    //                                "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
    //                            stmt =
    //                  (OracleCallableStatement)connection.prepareCall(query13);
    //                            stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //                            stmt.execute();
    //                            rst = (OracleResultSet)stmt.getObject(1);
    //                            int j = 0;
    //
    //                            while (rst.next()) {
    //                                String messages = messageToSend;
    //                                if (message == null||telNumber==null) {
    //
    //                                } else
    //                                    messages = java.net.URLEncoder.encode(messages);
    //                                url = rst.getString(2);
    //                                url = url.replace("[USERNAME]", rst.getString(3));
    //                                url = url.replace("[PASSWORD]", rst.getString(4));
    //                                url = url.replace("[SOURCE]", rst.getString(5));
    //                                url = url.replace("[MESSAGE]", messages);
    //                                url =
    //                               url.replace("[DESTINATION]", telNumber);
    //                                j++;
    //                            }
    //
    //                            rst.close();
    //                  Timer timer = new Timer(true);
    //                InterruptTimerTask interruptTimerTask =
    //                    new InterruptTimerTask(Thread.currentThread());
    //                timer.schedule(interruptTimerTask, 1000);
    //
    //                    urls = new URL(url);
    //                    conns = (HttpURLConnection)urls.openConnection();
    //                    conns.setDoOutput(false);
    //                    conns.setDoInput(true);
    //                    res = conns.getResponseMessage();
    //                  responseCode= conns.getResponseCode();
    //                    String Binders =
    //                                "begin tqc_setups_pkg.smsStatus(?,?,?); end;";
    //                             stmt =
    //                  (OracleCallableStatement)connection.prepareCall(Binders);
    //                            stmt.setString(1, "OK");
    //                            stmt.setBigDecimal(2, except);
    //                            stmt.setObject(3, responseCode);
    //                            stmt.execute();
    //                            stmt.close();
    //                            int rsCode = conns.getResponseCode();
    //                            if (rsCode == HttpURLConnection.HTTP_OK) {
    //                                conns.disconnect();
    //                            }
    //
    //
    //                    count++;
    //
    //                }
    //            rs.close();
    //            stmt.close();
    //            connection.close();
    //        }catch(Exception e)
    //        {
    //
    //            }
    //    finally{
    //      DbUtils.closeQuietly(connection, stmt, rs);
    //    }
    //  }

    public String makeTrigger() {
        try {
            SchedulerConfig config = new SchedulerConfig();
            ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
             ServletContext contextEvent = (ServletContext)ex.getContext();
            Scheduler sched = config.getScheduler(contextEvent);
            sched.start();
            //
            //        JobKey sampleKey=new JobKey("SMS Alert", "TurnQuestJobs");
            //        TriggerKey triggerKey=new TriggerKey("SMS Alert"+"Trig", "TurnQuestJobs");
            //        JobDetail jobDetail=JobBuilder.newJob(clientsSms.class).withIdentity(sampleKey).build();
            //          jobDetail.getJobDataMap().put("type", "SMS Alert");
            //        Trigger trigger=TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")).build();
            // sched.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
