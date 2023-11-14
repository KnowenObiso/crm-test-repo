package TurnQuest.view;


import TurnQuest.view.Alerts.SystemAlerts;
import TurnQuest.view.Base.GlobalCC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;


public class MyPhaseListener implements ServletContextListener {
    ServletContext context;

    public void contextInitialized(ServletContextEvent contextEvent) {
        Connection conn = getDatabaseConnection();
        context = contextEvent.getServletContext();
        try {
            PreparedStatement pst =
                conn.prepareStatement("select QT_CRON_EXPRESSION,QT_JOB_NAME FROM QRTZ_TRIGGERS WHERE QT_STATUS='A'");
            ResultSet rst = pst.executeQuery();
            String success = null;
            while (rst.next()) {
                SystemAlerts alerts = new SystemAlerts();
                //                if (!alerts.checkJobExists(rst.getString(2),context)) {
                //                    alerts.startJobs(rst.getString(2), rst.getString(1),context);
                //                } else {
                //                }
            }
        } catch (SQLException ex) {
            System.out.println("Message...." + ex.getMessage());
        }

    }

    public void contextDestroyed(ServletContextEvent contextEvent) {

        System.out.println("Context Destroyed");
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
}
