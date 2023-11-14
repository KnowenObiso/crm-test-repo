package TurnQuest.view.Base;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;


public class MailAuthenticator extends Authenticator {
    public MailAuthenticator() {
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

    protected PasswordAuthentication getPasswordAuthentication() {
        GlobalCC GlobalCC = new GlobalCC();
        OracleConnection conn;
        conn = getDatabaseConnection();
        String emailVal = null;
        String passVal = null;
        String emailQuery =
            "SELECT  MAIL_USERNAME, MAIL_PASSWORD, MAIL_SECURE FROM TQC_SYSTEM_MAILS WHERE MAIL_IN_OUT = 'O'";

        OracleCallableStatement cst = null;
        try {
            cst = (OracleCallableStatement)conn.prepareCall(emailQuery);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();

            while (rs.next()) {
                emailVal = rs.getString(1);
                passVal = rs.getString(2);
            }
            cst.close();
            rs.close();
            System.out.println();
            System.out.println(emailVal);
            System.out.println(passVal);
            if (emailVal == null) {
                GlobalCC.errorValueNotEntered("EMAILS_FROM Not Defined Please Check");
                return null;
            }
            if (passVal == null) {
                GlobalCC.errorValueNotEntered("EMAILS_FROM_PASSWORD Not Defined Please Check");
                return null;
            }

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return new PasswordAuthentication(emailVal, passVal);
    }
}
