package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;


//import javax.activation.DataSource;


public class GlobalMail {
    public GlobalMail() {
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


    public String sendMail(String sender, String receiver, String cc,
                           String subject, String content) {
        String success = null;
        GlobalCC GlobalCC = new GlobalCC();
        OracleConnection conn;
        conn = getDatabaseConnection();
        String emailVal = null;
        String passVal = null;
        String hostVal = null;
        String portVal = null;
        String emailQuery ="SELECT  MAIL_USERNAME, MAIL_PASSWORD, MAIL_HOST,MAIL_PORT FROM TQC_SYSTEM_MAILS WHERE MAIL_IN_OUT = 'O'";

        OracleCallableStatement cst = null;
        try {
            cst = (OracleCallableStatement)conn.prepareCall(emailQuery);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();

            while (rs.next()) {
                emailVal = rs.getString(1);
                passVal = rs.getString(2);
                hostVal = rs.getString(3);
                portVal = rs.getString(4);
            }
            cst.close();
            rs.close();
            if (emailVal == null) {
                GlobalCC.errorValueNotEntered("EMAILS_FROM Not Defined Please Check");
                return null;
            }
            if (passVal == null) {
                GlobalCC.errorValueNotEntered("EMAILS_FROM_PASSWORD Not Defined Please Check");
                return null;
            }

            conn.close();

            Properties props = new Properties();
                    props.put("mail.smtp.host", hostVal);
                    props.put("mail.smtp.socketFactory.port", portVal);
                    props.put("mail.smtp.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", portVal);
                    props.put("mail.user", emailVal);
                    props.put("mail.password", passVal);
            props.put("mail.smtp.socketFactory.fallback", "true");
           
            Authenticator auth = new MailAuthenticator();
           
            Session mailSession = Session.getInstance(props, auth);
    
            Transport transport = null;

           MimeMessage message = new MimeMessage(mailSession);
           message.setFrom(new InternetAddress(sender));//change accordingly
           if (cc != null) {
               message.setRecipients(Message.RecipientType.CC,
                                     InternetAddress.parse(cc));
           }else{
               message.addRecipient(Message.RecipientType.TO,new InternetAddress(receiver));

           }
           message.setSubject(subject);
            message.setText(content);
           Transport.send(message);
            success = "success";
        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }

        return success;
    }

    public String sendMailDoc(String sender, String receiver, String cc,
                              String subject, String content, String filename,
                              String contentFilename, File file,
                              String contentType) {

        GlobalMail GlobalMail = new GlobalMail();
        String success = null;
        String mailhost = null;
        Object bcc = null;
        String emailVal = null;
        String mailTypeVal = null;
        String username = null;
        String password = null;
        String port = null;

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        String hrQuery3 =
            " SELECT  MAIL_TYPE,MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD, MAIL_SECURE,MAIL_PORT FROM TQC_SYSTEM_MAILS WHERE MAIL_IN_OUT = 'O'";


        OracleCallableStatement callStmt3 = null;
        try {
            callStmt3 = (OracleCallableStatement)conn.prepareCall(hrQuery3);
            OracleResultSet rs = (OracleResultSet)callStmt3.executeQuery();
            while (rs.next()) {
                mailTypeVal = rs.getString(1);
                mailhost = rs.getString(2);
                emailVal = rs.getString(3);
                username = rs.getString(3);
                password = rs.getString(4);
                port = rs.getString(6);
            }
        if (contentType != null) {
            if (contentType.equalsIgnoreCase("text/plain")) {
                contentType = "application/octet-stream";
            }
        }
        callStmt3 = (OracleCallableStatement)conn.prepareCall(hrQuery3);
        rs = (OracleResultSet)callStmt3.executeQuery();
        while (rs.next()) {
            mailhost = rs.getString(1);
        }
        if (mailhost == null) {
            GlobalCC.errorValueNotEntered("MAILHOST Not Defined Please Check");
            return null;
        }
        callStmt3.close();
        conn.close();

        
        if (contentType == null) {
            contentType = "application/pdf";
        }
        Properties props = new Properties();
        Session mailSession = null;
        if (username != null && password != null && username != "" &&
            password != "") {
            Authenticator auth = new MailAuthenticator();
            System.out.println("Authenticate");
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.user", username);
            props.put("mail.password", password);
            

            mailSession = Session.getInstance(props, auth);
        } else {
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.port", port);
            mailSession = Session.getInstance(props);
        }

        Transport transport = null;
            MimeMessage message = new MimeMessage(mailSession);
                      message.setFrom(new InternetAddress(sender));//change accordingly
                      message.addRecipient(Message.RecipientType.TO,new InternetAddress(receiver));
                      message.setSubject(subject);

                      BodyPart messageBodyPart = new MimeBodyPart();

                      messageBodyPart.setText(content);

                      Multipart multipart = new MimeMultipart();

                      multipart.addBodyPart(messageBodyPart);

                      messageBodyPart = new MimeBodyPart();
                      javax.activation.DataSource source = new javax.activation.FileDataSource(filename);
                      messageBodyPart.setDataHandler(new javax.activation.DataHandler(source));
                      messageBodyPart.setFileName(filename);
                      multipart.addBodyPart(messageBodyPart);

                      // Send the complete message parts
                      message.setContent(multipart);
                      //send message
                      transport.send(message);

                      System.out.println("message sent successfully");

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }


        return success;
    }

    public byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length &&
               (numRead = is.read(bytes, offset, bytes.length - offset)) >=
               0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " +
                                  file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
