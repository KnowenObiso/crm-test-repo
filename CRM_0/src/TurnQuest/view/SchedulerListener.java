package TurnQuest.view;


import TurnQuest.view.Alerts.SystemAlerts;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;


public class SchedulerListener implements ServletContextListener {
    ServletContext context;

    public void contextInitialized(ServletContextEvent contextEvent) {
        SystemAlerts alerts = new SystemAlerts();
        try {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            String status = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query1 =
                    "begin ? := TQC_SETUPS_CURSOR.getScheduledJobs(); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query1);
                stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                stmt.execute();
                rs = (OracleResultSet)stmt.getObject(1);
                int count=1;
                while (rs.next()) {
                    status = rs.getString(1);
                    alerts.makeTrigger(rs.getString(1), rs.getString(5),contextEvent.getServletContext(),count);
                    count++;
                }
                rs.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
                //  GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } catch (Exception ex) {
            System.out.println("Message...." + ex.getMessage());
        }

    }

    private OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;
        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);

            conn = (OracleConnection)ds.getConnection();

            System.out.println("database Connected....");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return conn;
    }

    public void contextDestroyed(ServletContextEvent contextEvent) {

        System.out.println("Context Destroyed");
    }

    public String getAllScheduledDetails() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getScheduledJobs(); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                status = rs.getString(1);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
}
