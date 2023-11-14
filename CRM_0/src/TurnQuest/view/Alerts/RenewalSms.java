package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.DBConnector;

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


public class RenewalSms {
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            boolean boolVal;
            boolVal = isRenTimes();
            if (boolVal == true) {
                getPendingOneMonthsRen();
                getPendingTwoMonthsRen();
                getPendingTwoDaysRen();
            } else {
                getPendingOneMonthsRen();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }


    }

    public void getPendingOneMonthsRen() {
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
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
    }

    public void getPendingTwoMonthsRen() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getPoliciesTwoMonthsRen;end;";
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
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
    }

    public void getPendingTwoDaysRen() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getPoliciesTwoDaysRen;end;";
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
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
    }
    //  public void getPendingTwoMonthsRen(){
    //       String telNumber;
    //      String message;
    //        String url = null;
    //        URL urls;
    //        HttpURLConnection conns=null;
    //        int responseCode;
    //        int count=0;
    //        DBConnector datahandler = new DBConnector();
    //        //DBConnector dbConnector = new DBConnector();
    //        OracleConnection connection = null;
    //        OracleCallableStatement stmt = null;
    //        OracleResultSet rs,rst = null;
    //        connection = datahandler.getDatabaseConnection();
    //        try{
    //        String query1 =
    //            "begin ? := tqc_setups_cursor.getPoliciesTwoMonthsRen; end;";
    //        stmt =
    //        (OracleCallableStatement)connection.prepareCall(query1);
    //        stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //        stmt.execute();
    //        rs = (OracleResultSet)stmt.getObject(1);
    //        while (rs.next()) {
    //
    //            telNumber=rs.getString(6);
    //            String clientName=rs.getString(5);
    //             String phoneNumber = null;
    //            message="Your Policy"+" "+rs.getString(1)+" "+"is due for renewal on "+"  "+rs.getString(3);
    //              if (telNumber != null) {
    //                  phoneNumber = telNumber;
    //              }
    //              String regex = "^[+0-9]{10,25}$";
    //              Pattern pattern = Pattern.compile(regex);
    //              Matcher matcher = null;
    //              boolean matches = false;
    //            if (phoneNumber != null) {
    //                  matcher = pattern.matcher(phoneNumber);
    //                  matches = matcher.matches();
    //              }
    //           // connection = datahandler.getDatabaseConnection();
    //                      String query13 =
    //                          "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
    //                      stmt =
    //            (OracleCallableStatement)connection.prepareCall(query13);
    //                      stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //                      stmt.execute();
    //                      rst = (OracleResultSet)stmt.getObject(1);
    //                      int j = 0;
    //            try{
    //                      while (rst.next()) {
    //                          String messages = message;
    //                          if (message == null||telNumber==null) {
    //
    //                          } else
    //                              messages = java.net.URLEncoder.encode(messages);
    //                          url = rst.getString(2);
    //                          url = url.replace("[USERNAME]", rst.getString(3));
    //                          url = url.replace("[PASSWORD]", rst.getString(4));
    //                          url = url.replace("[SOURCE]", rst.getString(5));
    //                          url = url.replace("[MESSAGE]", messages);
    //                          url =
    //                         url.replace("[DESTINATION]", telNumber);
    //                          j++;
    //                      }
    //               System.out.println("this is sms url tesssst"+url);
    //                      rst.close();
    //          Timer timer = new Timer(true);
    //          InterruptTimerTask interruptTimerTask =
    //              new InterruptTimerTask(Thread.currentThread());
    //          timer.schedule(interruptTimerTask, 1000);
    //
    //              urls = new URL(url);
    //              conns = (HttpURLConnection)urls.openConnection();
    //              conns.setDoOutput(false);
    //              conns.setDoInput(true);
    //           //   res = conns.getResponseMessage();
    //            responseCode= conns.getResponseCode();
    //             } catch (Exception e) {
    //            responseCode=204;
    //              e.printStackTrace();
    //          }
    //                count++;
    //          }
    //          rs.close();
    //          stmt.close();
    //          connection.close();
    //        }catch(Exception e) {
    //e.printStackTrace();
    //        }finally{
    //            DbUtils.closeQuietly(connection, stmt, null);
    //          }
    //
    //
    //
    //      return ;
    //      }
    //  public void getPendingOneMonthsRen(){
    //
    //      String telNumber;
    //      String message;
    //        String url = null;
    //        URL urls;
    //        HttpURLConnection conns=null;
    //        int responseCode;
    //        int count=0;
    //        DBConnector datahandler = new DBConnector();
    //        //DBConnector dbConnector = new DBConnector();
    //        OracleConnection connection = null;
    //        OracleCallableStatement stmt = null;
    //        ResultSet rs,rst = null;
    //        connection = datahandler.getDatabaseConnection();
    //        try{
    //        String query1 =
    //            "begin ? := tqc_setups_cursor.getPoliciesOneMonthsRen; end;";
    //        stmt =
    //        (OracleCallableStatement)connection.prepareCall(query1);
    //        stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //        stmt.execute();
    //        rs = (OracleResultSet)stmt.getObject(1);
    //        while (rs.next()) {
    //            System.out.print("Gets here onadiowe");
    //            telNumber=rs.getString(6);
    //            String clientName=rs.getString(5);
    //             String phoneNumber = null;
    //            message="Your Policy"+rs.getString(1)+"is due for renewal on "+rs.getString(3);
    //              if (telNumber != null) {
    //                  phoneNumber = telNumber;
    //              }
    //              String regex = "^[+0-9]{10,25}$";
    //              Pattern pattern = Pattern.compile(regex);
    //              Matcher matcher = null;
    //              boolean matches = false;
    //            if (phoneNumber != null) {
    //                  matcher = pattern.matcher(phoneNumber);
    //                  matches = matcher.matches();
    //              }
    //           // connection = datahandler.getDatabaseConnection();
    //                      String query13 =
    //                          "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
    //                      stmt =
    //            (OracleCallableStatement)connection.prepareCall(query13);
    //                      stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //                      stmt.execute();
    //                      rst = (OracleResultSet)stmt.getObject(1);
    //                      int j = 0;
    //            try{
    //                      while (rst.next()) {
    //                          String messages = message;
    //                          if (message == null||telNumber==null) {
    //
    //                          } else
    //                              messages = java.net.URLEncoder.encode(messages);
    //                          url = rst.getString(2);
    //                          url = url.replace("[USERNAME]", rst.getString(3));
    //                          url = url.replace("[PASSWORD]", rst.getString(4));
    //                          url = url.replace("[SOURCE]", rst.getString(5));
    //                          url = url.replace("[MESSAGE]", messages);
    //                          url =
    //                         url.replace("[DESTINATION]", telNumber);
    //                          j++;
    //                      }
    //               System.out.println("this is sms url tesssst"+url);
    //                      rst.close();
    //              urls = new URL(url);
    //              conns = (HttpURLConnection)urls.openConnection();
    //              conns.setDoOutput(false);
    //              conns.setDoInput(true);
    //           //   res = conns.getResponseMessage();
    //            responseCode= conns.getResponseCode();
    //             } catch (Exception e) {
    //            responseCode=204;
    //              e.printStackTrace();
    //          }
    //                count++;
    //          }
    //          rs.close();
    //          stmt.close();
    //          connection.close();
    //        }catch(Exception e) {
    //  e.printStackTrace();
    //  }		          finally{
    //            DbUtils.closeQuietly(connection, stmt, null);
    //          }
    //
    //
    //
    //      return ;
    //      }
    //  public void getPendingTwoDaysRen(){
    //
    //      String telNumber;
    //      String message;
    //        String url = null;
    //        URL urls;
    //        HttpURLConnection conns=null;
    //        int responseCode;
    //        int count=0;
    //        DBConnector datahandler = new DBConnector();
    //        //DBConnector dbConnector = new DBConnector();
    //        OracleConnection connection = null;
    //        OracleCallableStatement stmt = null;
    //        OracleResultSet rs,rst = null;
    //        connection = datahandler.getDatabaseConnection();
    //        try{
    //        String query1 =
    //            "begin ? := tqc_setups_cursor.getPoliciesOneMonthsRen; end;";
    //        stmt =
    //        (OracleCallableStatement)connection.prepareCall(query1);
    //        stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //        stmt.execute();
    //        rs = (OracleResultSet)stmt.getObject(1);
    //        while (rs.next()) {
    //              telNumber=rs.getString(6);
    //            String clientName=rs.getString(5);
    //             String phoneNumber = null;
    //            message="Your Policy"+rs.getString(1)+"is due for renewal on "+rs.getString(3);
    //              if (telNumber != null) {
    //                  phoneNumber = telNumber;
    //              }
    //              String regex = "^[+0-9]{10,25}$";
    //              Pattern pattern = Pattern.compile(regex);
    //              Matcher matcher = null;
    //              boolean matches = false;
    //            if (phoneNumber != null) {
    //                  matcher = pattern.matcher(phoneNumber);
    //                  matches = matcher.matches();
    //              }
    //            //connection = datahandler.getDatabaseConnection();
    //                      String query13 =
    //                          "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
    //                      stmt =
    //            (OracleCallableStatement)connection.prepareCall(query13);
    //                      stmt.registerOutParameter(1, OracleTypes.CURSOR);
    //                      stmt.execute();
    //                      rst = (OracleResultSet)stmt.getObject(1);
    //                      int j = 0;
    //            try{
    //                      while (rst.next()) {
    //                          String messages = message;
    //                          if (message == null||telNumber==null) {
    //
    //                          } else
    //                              messages = java.net.URLEncoder.encode(messages);
    //                          url = rst.getString(2);
    //                          url = url.replace("[USERNAME]", rst.getString(3));
    //                          url = url.replace("[PASSWORD]", rst.getString(4));
    //                          url = url.replace("[SOURCE]", rst.getString(5));
    //                          url = url.replace("[MESSAGE]", messages);
    //                          url =
    //                         url.replace("[DESTINATION]", telNumber);
    //                          j++;
    //                      }
    //               System.out.println("this is sms url tesssst"+url);
    //                      rst.close();
    //                    urls = new URL(url);
    //              conns = (HttpURLConnection)urls.openConnection();
    //              conns.setDoOutput(false);
    //              conns.setDoInput(true);
    //           //   res = conns.getResponseMessage();
    //            responseCode= conns.getResponseCode();
    //             } catch (Exception e) {
    //            responseCode=204;
    //              e.printStackTrace();
    //          }
    //                count++;
    //          }
    //          rs.close();
    //          stmt.close();
    //          connection.close();
    //        }catch(Exception e) {
    //  e.printStackTrace();
    //        }finally{
    //            DbUtils.closeQuietly(connection, stmt, null);
    //          }
    //
    //
    //
    //      return ;
    //      }

    public boolean isRenTimes() {
        String site = "NONE";
        site = new Util().getRenTimes();
        if (site.toString().equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String makeTrigger(String cronExpression) {
        try {


            //          SchedulerConfig config=new SchedulerConfig();
            //            Scheduler sched = config.getScheduler();
            //            sched.start();
            //          JobKey sampleKey=new JobKey("Renewal Alert", "TurnQuestJobs");
            //          TriggerKey triggerKey=new TriggerKey("Renewal Alert"+"Trig", "TurnQuestJobs");
            //          JobDetail jobDetail=JobBuilder.newJob(RenewalSms.class).withIdentity(sampleKey).build();
            //            jobDetail.getJobDataMap().put("type", "Renewal Alert");
            //          Trigger trigger=TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            //            sched.scheduleJob(jobDetail, trigger);


        } catch (Exception e) {
            //  e.printStackTrace();
        }
        return null;
    }
}
