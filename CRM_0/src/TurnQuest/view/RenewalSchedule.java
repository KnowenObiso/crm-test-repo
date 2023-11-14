package TurnQuest.view;


import TurnQuest.view.Alerts.RenewalSms;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.commons.dbutils.DbUtils;


public class RenewalSchedule implements ServletContextListener {
    public RenewalSchedule() {
        super();
    }

    ServletContext context;

    public void contextInitialized(ServletContextEvent contextEvent) {
        RenewalSms sms = new RenewalSms();
        try {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rst = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query1 =
                    "begin ? := TQC_CLIENTS_PKG.getRenParameters(?);end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query1);
                stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                stmt.setString(2, "RENEWAL_MESSAGES_TIMES");
                stmt.execute();
                rst = (OracleResultSet)stmt.getObject(1);
                while (rst.next()) {
                    sms.makeTrigger(rst.getString(2));
                }
                rst.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                GlobalCC.EXCEPTIONREPORTING(ex);
            } finally {
                DbUtils.closeQuietly(conn, stmt, rst);
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
}
