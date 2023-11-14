package TurnQuest.view;


import TurnQuest.view.Alerts.SendBirthdayMessages;
import TurnQuest.view.Alerts.clientsSms;
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
import oracle.jdbc.OracleTypes;


public class MySmsListener implements ServletContextListener {
    ServletContext context;

    public void contextInitialized(ServletContextEvent contextEvent) {
        try {
            clientsSms alerts = new clientsSms();
            alerts.makeTrigger();

            SendBirthdayMessages messages = new SendBirthdayMessages();
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rst = null;

            conn = dbConnector.getLoginConnection();
            String query1 =
                "begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setString(2, "SEND_BIRTHDAY_MESSAGES");
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                System.out.println("This is it" + rst.getString(2));
                messages.makeTrigger(rst.getString(2));
                System.out.println("This is it");
            }
            rst.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
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
