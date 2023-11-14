package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.awt.Dimension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.adf.controller.ControllerContext;
import oracle.adf.model.DataControl;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.apps.xdo.delivery.DeliveryManager;
import oracle.apps.xdo.delivery.DeliveryPropertyDefinitions;
import oracle.apps.xdo.delivery.DeliveryRequest;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class GlobalCC {
    public static final String EMAIL_REGX_EXT =
        ".*\\^([a-zA-Z0-9]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-\\.]{2,6}+)\\^.*";
    public static final String PHONE_NO_EXT = ".*\\^(\\+[0-9]{2,20}+)\\^.*";

    public static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static RichOutputLabel globalConfirmMsgValue;
    static String username1;
    static String password1;
    public static final String claType = "O";

    public GlobalCC() {
    }

    public static final String emailFrom = "";
    public static final String mailhost = "";

    protected static String dialogName;

    public static final int sysCode = 0;
    private static String userName = null;
    public static final String databaseError =
        "ERROR CONNECTING TO DATABASE: CONTACT YOUR SYSTEM ADMINISTRATOR";
    public static final String incorectLogin =
        "INVALID USERNAME AND/OR PASSWORD";
    public static final String manualProposalparameterError =
        "MISSING MANUAL/AUTOMATIC PARAMETER ERROR: CONTACT YOUR SYSTEM ADMINISTRATOR";

    public static final String selectTrans = "SELECT TRANSACTION TYPE";
    public static final String enterEffectiveDate = "ENTER THE EFFECTIVE DATE";
    public static final String selectProposal = "SELECT PROPOSAL";
    public static final String selectPolicy = "SELECT POLICY";
    public static final String selectPropPol = "SELECT PROPOSAL/POLICY";
    public static final String selectProduct = "SELECT PRODUCT";
    public static final String selectBranch = "SELECT BRANCH";
    public static final String selectClient = "SELECT CLIENT";
    public static final String selectOccupation = "SELECT OCCUPATION";
    public static final String enterAgeNextBirthday =
        "ENTER THE AGE NEXT BIRTHDAY(ANB)";
    public static final String selectPremiumMask = "SELECT THE PREMIUM MASK";
    public static final String selectProductOption =
        "SELECT THE PRODUCT OPTION";
    public static final String enterPremiumSumAssured =
        "ENTER THE PREMIUM OR THE SUM ASSURED";
    public static final String taskRights =
        "You do not have sufficient rights to continue.";
    public static final String selectAgent = "SELECT AGENT";
    public static final String selectAssured = "SELECT ASSURED";
    public static final String selectlifeAssured = "SELECT LIFE ASSURED";
    public static final String selectPayMode = "SELECT PAYMENT METHOD";
    public static final String selectPayFrequency = "SELECT PAYMENT FREQUENCY";
    public static final String enterTerm = "ENTER PRODUCT TERM";


    public static final String success = "SUCCESS";
    public static final String failure = "FAILURE";
    public static final BigDecimal zeroValue = BigDecimal.ZERO;

    //PROCESS SHTS
    public static final String clientProcess = "CLNT";
    public static final String agentProcess = "AGNT";
    public static final String serviceProviderProcess = "SRVP";

    public static final String Access = "ACCS";
    public static final String Edit = "UPDT";
    public static final String Authorize = "AUTH";
    public static final String Suspend = "SPND";
    public static final String MakeReady = "MKRD";
    public static final String Deactivate = "DACT";
    public static final String Confirm = "CFRM";
    public static final String Reject = "RJCT";

    protected static int screenWidth;
    protected static int screenHeight;

    private static RichMessage more;
    private static RichMessage errorMsg;
    private static RichMessage errCode;
    private static RichMessage errName;
    private static RichMessage errText;
    private static RichMessage syserrCode;
    private static RichMessage sysErrMsg;
    private static RichMessage rcmdendation;
    private static RichMessage errStack;
    private static RichMessage callStack;
    private static RichMessage envirment;
    private static RichPopup errorPop;


    public static Timestamp extractTime(RichInputDate component) {
        Timestamp val = null;
        try {
            val = new Timestamp(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public static String extractTimeOnly(RichInputDate component) {
        String dateVal = checkNullValues(component.getValue());
        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");


        try {
            date = sdf1.parse(dateVal);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;

        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);
            conn = (OracleConnection)ds.getConnection();
            //userVaraibleInitialization(conn);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }


        return conn;
    }

    /*public static SessionFactory getSessionFactory() {
          if(sessionFactory == null){
              try {
                 sessionFactory = new Configuration().configure("tq.hibernate.cfg.xml").buildSessionFactory();
              } catch (Exception e) {
                 sessionFactory = null;
                 GlobalCC.EXCEPTIONREPORTING("Failed to connect to db." );
                 e.printStackTrace();
              }
          }
         return sessionFactory;
      }*/

    public static String parseDateTime(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String upDateParseDateTime(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }


    public static String applicationEmailDoc(String receiver, String cc,
                                             String subject, String content,
                                             String filename) {

        System.out.println("Sending attachment.");
        String success = null;
        String mailhost = null;
        String sender = null;
        Object bcc = null;
        String emailVal = null;
        String mailTypeVal = null;
        String username = null;
        String password = null;
        String port = null;
        OracleConnection conn;
        conn = getDatabaseConnection();
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
            if (mailhost == null) {
                GlobalCC.errorValueNotEntered("MAILHOST Not Defined Please Check");
                return null;
            }
            callStmt3.close();
            conn.close();

            Properties props = new Properties();
            Session mailSession = null;
            if (username != null && password != null && username != "" &&
                password != "") {
                Authenticator auth = new MailAuthenticator();
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class",
                          "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", port);
                props.put("mail.user", emailVal);
                props.put("mail.password", password);
                mailSession = Session.getInstance(props, auth);


            } else {
                //Authenticator auth = new MailAuthenticator();
                // System.out.println("Authenticate");
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.port", port);
                //props.put("mail.smtp.starttls.enable", "true");
                //props.put("mail.smtp.auth", "true");
                // props.put("mail.smtp.ssl.trust", "*");
                mailSession = Session.getInstance(props);

            }

            Message message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(emailVal));
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(receiver));


            if (cc != null) {
                message.setRecipients(Message.RecipientType.CC,
                                      InternetAddress.parse(cc));
            }
            if (bcc != null) {
                message.setRecipients(Message.RecipientType.CC,
                                      InternetAddress.parse(bcc.toString()));
            }
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(content);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            javax.activation.DataSource source =
                new javax.activation.FileDataSource(filename);
            messageBodyPart.setDataHandler(new javax.activation.DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            try {
                SSLSocketFactory factory =
                    HttpsURLConnection.getDefaultSSLSocketFactory();
                SSLSocket socket =
                    (SSLSocket)factory.createSocket(mailhost, Integer.parseInt(port));
                socket.startHandshake();
                System.out.println("Handshaking Complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Transport.send(message);
            success = "success";
        } catch (Exception e) {
            EXCEPTIONREPORTING(null, e);
        }

        return success;
    }

    public static String applicationEmailDocToMany(List<String> receiver,
                                                   String cc, String subject,
                                                   String content,
                                                   String filename) {
        String success = null;
        String mailhost = null;
        String sender = null;
        String emailVal = null;
        String mailTypeVal = null;
        String username = null;
        String password = null;
        String port = null;
        OracleConnection conn;
        conn = getDatabaseConnection();
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
            if (mailhost == null) {
                GlobalCC.errorValueNotEntered("MAILHOST Not Defined Please Check");
                return null;
            }
            callStmt3.close();
            conn.close();
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
                props.put("mail.smtps.ssl.trust", "*");
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

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(emailVal));
            for (int i = 0; i < receiver.size(); i++) {
                message.setRecipients(Message.RecipientType.TO,
                                      InternetAddress.parse(receiver.get(i)));

            }

            if (cc != null) {
                message.setRecipients(Message.RecipientType.CC,
                                      InternetAddress.parse(cc));
            }

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(filename, "application/pdf");
            mp.addBodyPart(htmlPart);
            // mp.
            message.setSubject(subject);
            message.setText(content);
            message.setContent(mp);
            Transport.send(message);
        } catch (Exception e) {
            EXCEPTIONREPORTING(null, e);
        }

        return success;
    }


    private static boolean validateEmail(String host, int portint,
                                         String email, String password) {
        boolean result = false;
        try {
            Properties props = new Properties();

            props.put("mail.smtp.host", host);
            props.put("mail.smtp.socketFactory.port", portint);
            props.put("mail.smtp.socketFactory.class",
                      "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtps.ssl.trust", "*");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", portint);
            props.put("mail.user", email);
            props.put("mail.password", password);


            Session session = Session.getDefaultInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, portint, email, password);
            System.out.println(session.getProviders());
            transport.close();
            result = true;

        } catch (AuthenticationFailedException e) {

        } catch (MessagingException e) {
            e.printStackTrace();

        } catch (Exception e) {
        }

        return result;
    }


    public static String applicationEmail(String receiver, String cc,
                                          String subject,
                                          String content) throws Exception {

        OracleConnection conn = null;
        String sendMailViaBackend =
            GlobalCC.getSysParamValue("BACKEND_SEND_MAIL");
        String success = null;
        if (!sendMailViaBackend.equalsIgnoreCase("Y")) {
            try {

                GlobalMail GlobalMail = new GlobalMail();
                Object bcc = null;
                String mailhost = null;
                String emailVal = null;
                String mailTypeVal = null;
                String username = null;
                String password = null;
                String port = null;

                conn = getDatabaseConnection();
                String hrQuery3 =
                    " SELECT  MAIL_TYPE,MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD, MAIL_SECURE,MAIL_PORT FROM TQC_SYSTEM_MAILS WHERE MAIL_IN_OUT = 'O'";


                OracleCallableStatement callStmt3 = null;

                callStmt3 =
                        (OracleCallableStatement)conn.prepareCall(hrQuery3);
                OracleResultSet rs = (OracleResultSet)callStmt3.executeQuery();
                while (rs.next()) {
                    mailTypeVal = rs.getString(1);
                    mailhost = rs.getString(2);
                    emailVal = rs.getString(3);
                    username1 = rs.getString(3);
                    password1 = rs.getString(4);
                    port = rs.getString(6);
                }
                if (mailTypeVal != null) {
                    if (mailTypeVal.equalsIgnoreCase("GOOGLE")) {
                        success =
                                GlobalMail.sendMail(emailVal, receiver, cc, subject,
                                                    content);
                        rs.close();
                        callStmt3.close();
                        conn.close();
                        success = "success";
                        return success;

                    }
                }

                if (mailhost == null) {

                    errorValueNotEntered("MAILHOST Not Defined Please Check");
                    return null;
                }
                rs.close();
                callStmt3.close();
                conn.close();

                if (mailTypeVal != null) {
                    if (mailTypeVal.equalsIgnoreCase("GOOGLE")) {
                        success =
                                GlobalMail.sendMail(emailVal, receiver, cc, subject,
                                                    content);

                        success = "success";
                        return success;

                    }
                }

                if (mailhost == null) {
                    errorValueNotEntered("MAILHOST Not Defined Please Check");
                    return null;
                }
                // create delivery manager instance
                DeliveryManager dm = new DeliveryManager();
                // create a delivery request
                DeliveryRequest req =
                    dm.createRequest(DeliveryManager.TYPE_SMTP_EMAIL);
                if (req != null) {
                    // set email subject
                    req.addProperty(DeliveryPropertyDefinitions.SMTP_SUBJECT,
                                    subject);
                    // set SMTP server host
                    req.addProperty(DeliveryPropertyDefinitions.SMTP_HOST,
                                    mailhost);
                    // set the sender email address

                    req.addProperty(DeliveryPropertyDefinitions.SMTP_FROM,
                                    emailVal); // sender);
                    // set the destination email address
                    req.addProperty(DeliveryPropertyDefinitions.SMTP_TO_RECIPIENTS,
                                    receiver);
                    req.addProperty(DeliveryPropertyDefinitions.SMTP_CC_RECIPIENTS,
                                    cc);
                    req.addProperty(DeliveryPropertyDefinitions.SMTP_BCC_RECIPIENTS,
                                    bcc);


                    req.setDocument(content, "UTF-8");
                    // submit the request
                    req.submit();
                    // close the request
                    req.close();
                    success = "success";
                }


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            // sending via backend
            DBConnector dbHandler = new DBConnector();
            String query =
                "begin ?:=tqc_email_pkg.send_email(?,?,?,?,?); end;";
            try {
                conn = dbHandler.getDatabaseConnection();
                OracleCallableStatement cstmt =
                    (OracleCallableStatement)conn.prepareCall(query);
                cstmt.registerOutParameter(1, OracleTypes.NUMBER);
                cstmt.setString(2, null);
                cstmt.setString(3, receiver);
                cstmt.setString(4, cc);
                cstmt.setString(5, subject);
                cstmt.setString(6, content);
                cstmt.execute();

                conn.commit();
                int result = cstmt.getInt(1);
                cstmt.close();
                conn.close();

                success = result == 1 ? "success" : "fail";

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return success;
    }

    public static String applicationEmailToMany(List<String> receiver,
                                                String cc, String subject,
                                                String content) {

        String mailhost = null;
        String sender = null;
        Object bcc = null;
        String success = null;
        String emailVal = null;
        String mailTypeVal = null;
        String username = null;
        String password = null;
        String port = null;

        OracleConnection conn;
        conn = getDatabaseConnection();
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
            if (mailhost == null) {
                GlobalCC.errorValueNotEntered("MAILHOST Not Defined Please Check");
                return null;
            }
            callStmt3.close();
            conn.close();

            // create delivery manager instance
            /* DeliveryManager dm = new DeliveryManager();
            // create a delivery request
            DeliveryRequest req =
                dm.createRequest(DeliveryManager.TYPE_SMTP_EMAIL);

            // set email subject
            req.addProperty(DeliveryPropertyDefinitions.SMTP_SUBJECT, subject);
            // set SMTP server host
            req.addProperty(DeliveryPropertyDefinitions.SMTP_HOST, mailhost);
            // set the sender email address
            req.addProperty(DeliveryPropertyDefinitions.SMTP_FROM, sender);
            req.addProperty(DeliveryPropertyDefinitions.SMTP_CC_RECIPIENTS,
                            cc);
            // set the document to deliver
            // req.addProperty(DeliveryPropertyDefinitions.SMTP_CONTENT_TYPE, "text/html");
            // set the document file name appeared in the email
            // req.addProperty(DeliveryPropertyDefinitions.CONTENT, content);
            req.setDocument(content, "UTF-8");
            for (int i = 0; i < receiver.size(); i++) {
                // set the destination email address
                req.addProperty(DeliveryPropertyDefinitions.SMTP_TO_RECIPIENTS,
                                receiver.get(i));
                // submit the request
                req.submit();
            }
            // close the request
            req.close();*/
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
                props.put("mail.smtps.ssl.trust", "*");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", port);
                props.put("mail.user", username);
                props.put("mail.password", password);


                mailSession = Session.getInstance(props, auth);
            } else {
                //Authenticator auth = new MailAuthenticator();
                // System.out.println("Authenticate");
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.port", port);
                //props.put("mail.smtp.starttls.enable", "true");
                //props.put("mail.smtp.auth", "true");
                // props.put("mail.smtp.ssl.trust", "*");

                mailSession = Session.getInstance(props);
            }

            Message message = new MimeMessage(mailSession);
            if (message != null) {
                message.setFrom(new InternetAddress(emailVal));
                for (int i = 0; i < receiver.size(); i++) {
                    message.setRecipients(Message.RecipientType.TO,
                                          InternetAddress.parse(receiver.get(i)));

                }

                if (cc != null) {
                    message.setRecipients(Message.RecipientType.CC,
                                          InternetAddress.parse(cc));
                }
                if (bcc != null) {
                    message.setRecipients(Message.RecipientType.CC,
                                          InternetAddress.parse(bcc.toString()));
                }
                Multipart mp = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(content, "text/html");
                mp.addBodyPart(htmlPart);
                // mp.
                message.setSubject(subject);
                //message.setText(content);
                message.setContent(mp);
                Transport.send(message);
            } else {
                GlobalCC.EXCEPTIONREPORTING("Unable to Send Message!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }


    public static String applicationEmailDoc(String sender, String receiver,
                                             String cc, String subject,
                                             String content, String filename,
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
            if (mailTypeVal != null) {
                if (mailTypeVal.equalsIgnoreCase("GOOGLE")) {
                    success =
                            GlobalMail.sendMailDoc(sender, receiver, cc, subject,
                                                   content, filename,
                                                   contentFilename, file,
                                                   contentType);
                    conn.close();
                    return success;

                }
            }
            callStmt3 = (OracleCallableStatement)conn.prepareCall(hrQuery3);
            rs = (OracleResultSet)callStmt3.executeQuery();
            while (rs.next()) {
                mailhost = rs.getString(1);
            }
            if (mailhost == null) {
                errorValueNotEntered("MAILHOST Not Defined Please Check");
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
                //Authenticator auth = new MailAuthenticator();
                // System.out.println("Authenticate");
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.port", port);
                //props.put("mail.smtp.starttls.enable", "true");
                //props.put("mail.smtp.auth", "true");
                // props.put("mail.smtp.ssl.trust", "*");

                mailSession = Session.getInstance(props);
            }

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(emailVal));
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(receiver));
            if (cc != null) {
                message.setRecipients(Message.RecipientType.CC,
                                      InternetAddress.parse(cc));
            }
            if (bcc != null) {
                message.setRecipients(Message.RecipientType.CC,
                                      InternetAddress.parse(bcc.toString()));
            }
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(contentFilename, contentType);
            mp.addBodyPart(htmlPart);
            // mp.
            message.setSubject(subject);
            message.setText(content);
            message.setContent(mp);
            Transport.send(message);
            success = "success";
        } catch (Exception e) {
            EXCEPTIONREPORTING(conn, e);
        }

        return success;
    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            if (component != null) {
                java.util.Date date = (Date)component.getValue();
                if (date != null) {
                    val = new java.sql.Date(date.getTime());
                }
            }
            System.out.println("Date val " + val);
        } catch (Exception e) {
            e.printStackTrace();
            val = null;
        }
        return val;
    }

    public static String EXCEPTIONREPORTING(Connection conn, Exception e) {
        if (e != null) {
            e.printStackTrace();
            String r = e.getMessage();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (r == null) {
                r = "Null Error Exception";
            }
            if (r.contains("TQC_ERROR_MANAGER")) {
                try {
                    if (conn != null) {
                        conn.commit();
                        String ckQuery =
                            "begin tqc_error_manager.get_error_info(?,?,?,?,?,?,?,?,?); end;";

                        OracleCallableStatement cst = null;

                        cst =
(OracleCallableStatement)conn.prepareCall(ckQuery);
                        cst.registerOutParameter(1, OracleTypes.INTEGER);
                        cst.registerOutParameter(2, OracleTypes.VARCHAR);
                        cst.registerOutParameter(3, OracleTypes.VARCHAR);
                        cst.registerOutParameter(4, OracleTypes.INTEGER);
                        cst.registerOutParameter(5, OracleTypes.VARCHAR);
                        cst.registerOutParameter(6, OracleTypes.VARCHAR);
                        cst.registerOutParameter(7, OracleTypes.VARCHAR);
                        cst.registerOutParameter(8, OracleTypes.VARCHAR);
                        cst.registerOutParameter(9, OracleTypes.VARCHAR);
                        cst.execute();

                        if (cst.getBigDecimal(1) == null) {
                            nonerrorPkg(r);
                            ExtendedRenderKitService erkService =
                                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                                   ExtendedRenderKitService.class);
                            FacesContext context =
                                FacesContext.getCurrentInstance();
                            String cID = errorPop.getClientId(context);
                            GlobalCC.showPopup(cID);
                        } else {
                            errorPkg(cst.getBigDecimal(1), cst.getString(2),
                                     cst.getString(3), cst.getBigDecimal(4),
                                     cst.getString(5), cst.getString(6),
                                     cst.getString(7), cst.getString(8),
                                     cst.getString(9));
                            ExtendedRenderKitService erkService =
                                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                                   ExtendedRenderKitService.class);
                            FacesContext context =
                                FacesContext.getCurrentInstance();
                            String cID = errorPop.getClientId(context);
                            GlobalCC.showPopup(cID);
                        }
                        cst.close();
                        conn.commit();
                        conn.close();
                    } else {
                        if (ctx != null) {
                            ctx.addMessage(null,
                                           new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                            r, r));
                        }
                    }


                } catch (Exception f) {
                    String message = f.getMessage();
                    if (ctx != null) {
                        ctx.addMessage(null,
                                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                        message, message));
                    }
                }

            } else {
                if (ctx != null) {
                    ctx.addMessage(null,
                                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    r, r));
                }
            }
        }


        return null;
    }
    

    /**
     * Close Active Conncections
     */
    public static void CloseConnections(OracleResultSet rs, OracleCallableStatement cst,
                                        OracleConnection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception rse) {
            rse.printStackTrace();
        }
        try {
            if (cst != null) {
                cst.close();
            }
        } catch (Exception sse) {
            sse.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception cse) {
            cse.printStackTrace();
        }
    }

    public static String EXCEPTIONREPORTING(OracleConnection conn,
                                            Exception e) {
        if (e != null) {
            e.printStackTrace();
            String r = e.getMessage();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (r == null) {
                r = "Null Error Exception";
            }
            if (r.contains("TQC_ERROR_MANAGER")) {
                try {
                    if (conn != null) {
                        conn.commit();
                        String ckQuery =
                            "begin tqc_error_manager.get_error_info(?,?,?,?,?,?,?,?,?); end;";

                        OracleCallableStatement cst = null;

                        cst =
(OracleCallableStatement)conn.prepareCall(ckQuery);
                        cst.registerOutParameter(1, OracleTypes.INTEGER);
                        cst.registerOutParameter(2, OracleTypes.VARCHAR);
                        cst.registerOutParameter(3, OracleTypes.VARCHAR);
                        cst.registerOutParameter(4, OracleTypes.INTEGER);
                        cst.registerOutParameter(5, OracleTypes.VARCHAR);
                        cst.registerOutParameter(6, OracleTypes.VARCHAR);
                        cst.registerOutParameter(7, OracleTypes.VARCHAR);
                        cst.registerOutParameter(8, OracleTypes.VARCHAR);
                        cst.registerOutParameter(9, OracleTypes.VARCHAR);
                        cst.execute();

                        if (cst.getBigDecimal(1) == null) {
                            nonerrorPkg(r);
                            ExtendedRenderKitService erkService =
                                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                                   ExtendedRenderKitService.class);
                            FacesContext context =
                                FacesContext.getCurrentInstance();
                            String cID = errorPop.getClientId(context);
                            GlobalCC.showPopup(cID);
                        } else {
                            errorPkg(cst.getBigDecimal(1), cst.getString(2),
                                     cst.getString(3), cst.getBigDecimal(4),
                                     cst.getString(5), cst.getString(6),
                                     cst.getString(7), cst.getString(8),
                                     cst.getString(9));
                            ExtendedRenderKitService erkService =
                                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                                   ExtendedRenderKitService.class);
                            FacesContext context =
                                FacesContext.getCurrentInstance();
                            String cID = errorPop.getClientId(context);
                            GlobalCC.showPopup(cID);
                        }
                        cst.close();
                        conn.commit();
                        conn.close();
                    } else {
                        if (ctx != null) {
                            ctx.addMessage(null,
                                           new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                            r, r));
                        }
                    }


                } catch (Exception f) {
                    String message = f.getMessage();
                    if (ctx != null) {
                        ctx.addMessage(null,
                                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                        message, message));
                    }
                }

            } else {
                if (ctx != null) {
                    ctx.addMessage(null,
                                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    r, r));
                }
            }
        }
        return null;
    }

    /**
     * Error For Raising Exception
     * @param exception
     * @return
     */
    public static String EXCEPTIONREPORTING(Exception e) {
        if (e != null) {
            e.printStackTrace();
            String m = e.getMessage();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null) {
                ctx.addMessage(null,
                               new FacesMessage(FacesMessage.SEVERITY_ERROR, m,
                                                m));
            }
        }
        return null;
    }

    public static String reportQuery(String rptCode, String paramType,
                                     String parentCode) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection connect = null;
        connect = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String Query = null;
        String rptQuery = null;
        try {
            Query =
                    "begin  TQC_WEB_CURSOR.get_report_parameters(?,?,?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(Query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, rptCode);
            cst.setString(3, paramType);
            cst.setString(4, parentCode);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                rptQuery = rs.getString(5);
                if (rs.getString(7) != null) {
                    rptQuery = rptQuery + " " + rs.getString(7);
                }
            }
            rs.close();
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return rptQuery;
    }

    public static void showPop(String pop) {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + pop +
                             "').show(hints);");
    }

    public static String EXCEPTIONREPORTING(String exception) {
        System.err.println(exception);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null,
                           new FacesMessage(FacesMessage.SEVERITY_ERROR, exception,
                                            exception));
        }
        return null;
    }

    /**
     * Error For Raising Information to Users.
     * @return
     */
    public static String INFORMATIONREPORTING(String errMessage) {
        System.out.println(errMessage);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null,
                           new FacesMessage(FacesMessage.SEVERITY_INFO, errMessage,
                                            errMessage));
        }
        return null;
    }


    /**
     *Function to raise error for Mandatory Values
     * @param errMessage
     * @return
     */

    public static String errorValueNotEntered(String errMessage) {
        System.err.println(errMessage);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null,
                           new FacesMessage(FacesMessage.SEVERITY_ERROR, errMessage,
                                            errMessage));
        }
        return null;
    }


    public static String sysInformation(String errMessage) {
        System.out.println(errMessage);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null,
                           new FacesMessage(FacesMessage.SEVERITY_INFO, errMessage,
                                            errMessage));
        }
        return null;
    }


    /**
     *Function to Check for null Values
     * @param objName
     * @return
     */
    public static String checkNullValues(Object objName) {
        String objectValue = null;
        if (objName != null) {
            if (objName.toString().trim().length() != 0) {
                objectValue = objName.toString().trim();
            }
        }
        return objectValue;
    }

    public static String getJsonValue(JSONObject json, String key) {
        String val = null;
        try {
            val = json.getString(key);
            val = GlobalCC.checkNullValues(val);
            if (val != null) {
                if ("{}".equals(val)) {
                    val = null;
                }
            }
        } catch (Exception e) {
            val = null;
        }
        return val;
    }

    public static java.sql.Date parseSQLDateTime(String someDate) {

        java.util.Date utilToday = new java.util.Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");

        java.sql.Date sqlDate = null;
        try {
            utilToday = sdf1.parse(someDate);
            long t = utilToday.getTime();
            sqlDate = new java.sql.Date(t);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        System.out.println(sqlDate);
        return sqlDate;
    }

    /**
     *Function to ParseDate
     * @param somedate
     * @return
     */
    public static String parseDate(Object someDate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        if (someDate == null)
            return null;
        String theDate = someDate != null ? someDate.toString() : null;

        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");


        try {
            date = sdf1.parse(theDate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String parseCMDate(String someDate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        if (someDate == null)
            return null;
        String theDate = someDate != null ? someDate.toString() : null;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");


        try {
            date = sdf1.parse(theDate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to ParseDate
     * @param somedate
     * @return
     */
    public static String addDate(Object someDate, int inc) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        if (someDate == null)
            return null;
        String theDate = someDate != null ? someDate.toString() : null;

        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");


        try {
            date = sdf1.parse(theDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, inc);
            date = cal.getTime();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String addDateTwo(Object someDate, int inc) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        if (someDate == null)
            return null;
        String theDate = someDate != null ? someDate.toString() : null;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");


        try {
            date = sdf1.parse(theDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, inc);
            date = cal.getTime();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String parseNormalDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to Parse Month
     * @param somedate
     * @return
     */
    public static String parseMonthDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }


    /**
     *Function to Parse Year
     * @param somedate
     * @return
     */
    public static String parseYearDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String parseMonth(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to ParseDate(Updating date after Change in interface)
     * @param somedate
     * @return string
     */
    public static String upDateParseDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMMM/yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to ParseDate(Updating date after Change in interface)
     * @param somedate
     * @return string
     */
    public static String upDateParseDateTwo(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMMM/yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }


    /**
     *Function to Decode various Payment Frequencies
     * @param freq
     * @return
     */
    public static String decodePaymentFrequency(String freq) {

        String payFreq = null;
        if (freq == null) {

        } else if (freq.equalsIgnoreCase("M")) {
            payFreq = "MONTHLY";
        } else if (freq.equalsIgnoreCase("Q")) {
            payFreq = "QUARTERLY";
        } else if (freq.equalsIgnoreCase("S")) {
            payFreq = "SEMI-ANNUALY";
        } else if (freq.equalsIgnoreCase("A")) {
            payFreq = "ANNUALLY";
        } else if (freq.equalsIgnoreCase("F")) {
            payFreq = "SINGLE PREMIUM";
        }

        return payFreq;

    }

    public static String decodeUserType(String usrType) {

        String type = null;
        if (usrType == null) {

        } else if (usrType.equalsIgnoreCase("U")) {
            type = "User";
        } else if (usrType.equalsIgnoreCase("G")) {
            type = "Group";
        }

        return type;

    }

    public static String decodeUserStatus(String usrStatus) {

        String type = null;
        if (usrStatus == null) {

        } else if (usrStatus.equalsIgnoreCase("A")) {
            type = "Active";
        } else {
            type = "InActive";
        }

        return type;

    }

    public static String decodeReset(String reset) {

        String type = null;
        if (reset == null) {

        } else if (reset.equalsIgnoreCase("N")) {
            type = "No";
        } else {
            type = "Yes";
        }

        return type;

    }

    /**
     *Function to Decode beneficiaries
     * @param ben
     * @return
     */
    public static String decodeBeneficiary(String ben) {

        String returnVal = null;
        if (ben == null) {

        } else if (ben.equalsIgnoreCase("B")) {
            returnVal = "Beneficiary";
        } else {
            returnVal = "Contigent Beneficiary";
        }

        return returnVal;
    }

    public static String decodeSex(String sex) {

        String returnVal = null;
        if (sex == null) {

        } else if (sex.equalsIgnoreCase("M")) {
            returnVal = "Male";
        } else {
            returnVal = "Female";
        }

        return returnVal;
    }

    public static String decodeYesNo(String Value) {

        String yesNoVal = null;
        if (Value == null) {

        } else if (Value.equalsIgnoreCase("Y")) {
            yesNoVal = "Yes";
        } else {
            yesNoVal = "No";
        }

        return yesNoVal;
    }

    /**
     *Function to Decode the various Payment Modes
     * @param mode
     * @return
     */
    public static String decodePaymentMode(String mode) {

        String payMode = null;
        if (mode == null) {

        } else if (mode.equalsIgnoreCase("C")) {
            payMode = "CASH";
        } else if (mode.equalsIgnoreCase("Q")) {
            payMode = "CHEQUE";
        } else if (mode.equalsIgnoreCase("S")) {
            payMode = "STANDING ORDER";
        } else if (mode.equalsIgnoreCase("K")) {
            payMode = "CHECKOFF";
        } else if (mode.equalsIgnoreCase("R")) {
            payMode = "STAFF REMITANCE";
        } else if (mode.equalsIgnoreCase("DD")) {
            payMode = "DIRECT DEBIT";
        }

        return payMode;

    }


    /**
     *Funtion to Decode Y(Yes) or N(No) Values
     * @param val
     * @return
     */
    public static String decodeYesNoValues(String val) {

        String retunVal = null;
        if (val == null) {

        } else if (val.equalsIgnoreCase("Y")) {
            retunVal = "YES";
        } else if (val.equalsIgnoreCase("N")) {
            retunVal = "NO";
        }

        return retunVal;
    }


    /**
     *Function to decode the various Endorsement Statuses
     * @param status
     * @return
     */
    public static String decodeEndorsementStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("D")) {
            decodedStatus = "Draft";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("A")) {
            decodedStatus = "Active";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("E")) {
            decodedStatus = "Endorsed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("RN")) {
            decodedStatus = "Renewed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("T")) {
            decodedStatus = "Contra'd";
            return decodedStatus;
        }
        return null;


    }

    public static String decodeMedicalsStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("E")) {
            decodedStatus = "Excempted";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("Y")) {
            decodedStatus = "Yes";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("N")) {
            decodedStatus = "No";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        }

        return decodedStatus;


    }


    /**
     *Function to Decode the various Policy Statuses
     * @param status
     * @return
     */
    public static String decodePolicyStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("D")) {
            decodedStatus = "Draft";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("A")) {
            decodedStatus = "Active";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("L")) {
            decodedStatus = "Lapsed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("R")) {
            decodedStatus = "Reinstated";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("J")) {
            decodedStatus = "Rejected/Declined";
            return decodedStatus;
        }
        return null;


    }

    /**
     * Get Netxt Day
     */
    public void nextDay() {
        Date currentNextDate = new Date();
        Date currentPreviousDate = new Date();


        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(currentNextDate);
            cal.add(Calendar.MONTH, 2);
            cal.getTime();

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(currentPreviousDate);
            cal2.add(Calendar.MONTH, -2);
            cal2.getTime();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }


    public static String nonerrorPkg(String message) {

        errText.setMessage("<html><b>ERROR NAME:</b> </html>" + message + " ");
        sysErrMsg.setMessage("<html><b>SYSTEM ERROR MESSAGE:</b>" + message +
                             " ");
        return null;
    }

    public static String errorPkg(BigDecimal errorCode, String errNameVal,
                                  String errTextVal, BigDecimal syserrCodeVal,
                                  String sysErrMsgVal, String rcmendVal,
                                  String errStackVal, String callStackVal,
                                  String enviVal) {
        String errCodeVal = null;
        String sysErrCodeValue = null;
        if (errorCode == null) {

        } else {
            errCodeVal = errorCode.toString();
        }
        if (syserrCodeVal != null) {
            sysErrCodeValue = syserrCodeVal.toString();
        }
        errCode.setMessage("<html><b>ERROR CODE: </b> </html>" + errCodeVal +
                           " ");
        errName.setMessage("<html><b>ERROR NAME: </b> </html>" + errNameVal +
                           " ");
        errText.setMessage("<html><b>ERROR TEXT: </b> </html>" + errTextVal +
                           " ");
        syserrCode.setMessage("<html><b>SYSTEM ERROR CODE: </b> </html>" +
                              sysErrCodeValue + " ");
        sysErrMsg.setMessage("<html><b>SYSTEM ERROR MESSAGE: </b> </html>" +
                             sysErrMsgVal + " ");
        rcmdendation.setMessage("<html><b>RECOMMENDATION: </b> </html>" +
                                rcmendVal + " ");
        errStack.setMessage("<html><b>ERROR STACK:</b> </html> " +
                            errStackVal + " ");
        callStack.setMessage("<html><b>ERROR CALL STACK:</b> </html> " +
                             callStackVal + " ");
        envirment.setMessage("<html><b>ENVIRONMENT:</b> </html> " + enviVal +
                             " ");
        return null;
    }


    public void setMore(RichMessage more) {
        this.more = more;
    }

    public RichMessage getMore() {
        return more;
    }

    public void setErrorMsg(RichMessage errorMsg) {
        this.errorMsg = errorMsg;
    }

    public RichMessage getErrorMsg() {
        return errorMsg;
    }

    public void setErrCode(RichMessage errCode) {
        this.errCode = errCode;
    }

    public RichMessage getErrCode() {
        return errCode;
    }

    public void setErrName(RichMessage errName) {
        this.errName = errName;
    }

    public RichMessage getErrName() {
        return errName;
    }

    public void setErrText(RichMessage errText) {
        this.errText = errText;
    }

    public RichMessage getErrText() {
        return errText;
    }

    public void setSyserrCode(RichMessage syserrCode) {
        this.syserrCode = syserrCode;
    }

    public RichMessage getSyserrCode() {
        return syserrCode;
    }

    public void setSysErrMsg(RichMessage sysErrMsg) {
        this.sysErrMsg = sysErrMsg;
    }

    public RichMessage getSysErrMsg() {
        return sysErrMsg;
    }

    public void setRcmdendation(RichMessage rcmdendation) {
        this.rcmdendation = rcmdendation;
    }

    public RichMessage getRcmdendation() {
        return rcmdendation;
    }

    public void setErrStack(RichMessage errStack) {
        this.errStack = errStack;
    }

    public RichMessage getErrStack() {
        return errStack;
    }

    public void setCallStack(RichMessage callStack) {
        this.callStack = callStack;
    }

    public RichMessage getCallStack() {
        return callStack;
    }

    public void setEnvirment(RichMessage envirment) {
        this.envirment = envirment;
    }

    public RichMessage getEnvirment() {
        return envirment;
    }


    public void setErrorPop(RichPopup errorPop) {
        GlobalCC.errorPop = errorPop;
    }

    public RichPopup getErrorPop() {
        return errorPop;
    }

    public static void CONFIRMACTION(String confirmMessage) {

        globalConfirmMsgValue.setValue(confirmMessage);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "attrs:globalConfirmDialogPop" +
                             "').show(hints);");
    }

    public Dimension getJPEGDimension(InputStream fis) throws IOException {


        /* check for SOI marker
       if (fis.read() != 255 || fis.read() != 216)
       {
           // throw new RuntimeException("SOI (Start Of Image) marker 0xff 0xd8 missing");
           GlobalCC.errorValueNotEntered("SOI (Start Of Image) marker 0xff 0xd8 missing");
            return null;

        }*/


        Dimension d = null;

        while (fis.read() == 255) {
            int marker = fis.read();
            int len = fis.read() << 8 | fis.read();

            if (marker == 192) {
                fis.skip(1);

                int height = fis.read() << 8 | fis.read();
                int width = fis.read() << 8 | fis.read();

                d = new Dimension(width, height);
                break;
            }

            fis.skip(len - 2);
        }

        fis.close();

        return d;
    }

    public static Boolean validateUploadedImg(UploadedFile file,
                                              String imgName) {
        // Add event code here...
        UploadedFile _file = file;


        if (_file.getContentType().equalsIgnoreCase("image/jpeg") ||
            _file.getContentType().equalsIgnoreCase("image/gif") ||
            _file.getContentType().equalsIgnoreCase("image/png")) {


            InputStream Reader;

            try {
                long Val = _file.getLength();

                if (Val == 0) {

                    return false;
                }
                Reader = _file.getInputStream();
                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(Reader);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                if (w <= 150.00 && h <= 150.00) {

                    return true;

                } else {
                    GlobalCC.errorValueNotEntered("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '150 x 150 px' ");

                    return false;
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.errorValueNotEntered("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                return false;
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be saved  :: Ensure the file  type is  .jpg/.gif/.png ");
            return false;
        }
    }

    public static Boolean validateUploadedImgFile(InputStream file,
                                                  String imgName,
                                                  String content) {
        // Add event code here...
        // UploadedFile _file = file;


        if (content.equalsIgnoreCase("image/jpeg") ||
            content.equalsIgnoreCase("image/gif") ||
            content.equalsIgnoreCase("image/png")) {


            try {

                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(file);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                if (w <= 150.00 && h <= 150.00) {

                    return true;

                } else {
                    GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '150 x 150 px' ");

                    return false;
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.INFORMATIONREPORTING("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                return false;
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be save  :: Ensure the file  type is  .jpg/.gif/.png");
            return false;
        }
    }


    public void setGlobalConfirmMsgValue(RichOutputLabel globalConfirmMsgValue) {
        this.globalConfirmMsgValue = globalConfirmMsgValue;
    }

    public RichOutputLabel getGlobalConfirmMsgValue() {
        return globalConfirmMsgValue;
    }

    public static void deselectAll(RichTable table,
                                   RichSelectBooleanCheckbox columnBinding,
                                   String attribute,
                                   RichCommandButton selectDeselectButton) {

        if (checkIfAnyTableRowselected(table, attribute)) {
            int rowcount = table.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                table.setRowIndex(i);
                Object key = table.getRowKey();
                table.setRowKey(key);


                columnBinding.setSelected(false);
                GlobalCC.refreshUI(columnBinding);
                selectDeselectButton.setText("Select All");
                GlobalCC.refreshUI(selectDeselectButton);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) selected::");
        }


    }

    public static boolean checkIfAnyTableRowselected(RichTable table,
                                                     String attribute) {


        int rowcount = table.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)table.getRowData();
            if (nodeBinding != null) {
                if (nodeBinding.getAttribute(attribute) != null) {
                    if (nodeBinding.getAttribute(attribute).toString().equalsIgnoreCase("true")) {
                        count = count + 1;
                    }
                }
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static void selectAll(RichTable table,
                                 RichSelectBooleanCheckbox columnBinding,
                                 RichCommandButton selectDeselectButton) {


        int rowcount = table.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            columnBinding.setSelected(true);
            GlobalCC.refreshUI(columnBinding);
            selectDeselectButton.setText("Unselect All");
            GlobalCC.refreshUI(selectDeselectButton);
        }


    }

    public static void dismissPopUp(String templateId, String popUpId) {
        if (templateId != null & popUpId != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + templateId +
                                 ":" + popUpId + "').hide(hints);");
        }
    }

    public static void showPopUp(String templateId, String popUpId) {
        if (templateId != null & popUpId != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + templateId +
                                 ":" + popUpId + "').show(hints);");
        }
    }

    public static boolean validateWefWetValues(Object wef, Object wet) {
        if (wef != null && wet == null)
            return true;
        if (wef == null && wet == null)
            return true;

        if (wef == null && wet != null)
            return false;
        //when none of the values is null
        Date someWef = (Date)wef;
        Date someWet = (Date)wet;
        if (someWef.compareTo(someWet) <= 0)
            return true;
        return false;
    }


    public static String getBusinessDate(String ddBookDate) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := Direct_Debit_Pkg.Get_business_date(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String failDate = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (ddBookDate != null) {
                statement.registerOutParameter(1, OracleTypes.VARCHAR);
                statement.setString(2, ddBookDate);
                statement.setBigDecimal(3,
                                        new BigDecimal("4")); // 4 is hardcoded on the V3 form
                statement.execute();
                failDate = statement.getString(1);
                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return failDate;
    }

    public static String accessDenied() {
        String errMessage =
            "Access Denied.Insufficient Rights.Contact your Systems Administrator";
        if (FacesContext.getCurrentInstance() == null) {
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }

    public static String getEmailFrom() {


        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT TQC_PARAMETERS_PKG.get_param_varchar('EMAILS_FROM') from dual";
        Statement statement = null;
        OracleConnection connection = null;
        String emailFrom = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                emailFrom = rs.getString(1);
            }
            statement.close();
            connection.close();
        }

        catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return emailFrom;
    }

    public static String formatAdminUnitPlural(Object adminUnitSymbol) {
        if (adminUnitSymbol == null)

            return "Administrative Units";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("S"))
            return "States";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("C"))
            return "Counties";
        if (adminUnitSymbol.toString().equalsIgnoreCase("P"))
            return "Provinces";
        if (adminUnitSymbol.toString().equalsIgnoreCase("R"))
            return "Region";
        return null;
    }

    public static String formatAdminUnitSingular(Object adminUnitSymbol) {
        if (adminUnitSymbol == null)
            return "Administrative Unit";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("S"))
            return "State";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("C"))
            return "County";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("P"))
            return "Province";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("R"))
            return "Region";
        return null;
    }

    public static HashMap getLoggedInUserDetails() {

        HashMap userDetailsMap = new HashMap();
        OracleConnection conn = getDatabaseConnection();

        String query = "begin TQC_ROLES_CURSOR.getFullUserDetails(?,?);end;";
        OracleCallableStatement stmt = null;
        ;


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, userName);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(2);
            while (rs.next()) {
                userDetailsMap.put("UserCode", rs.getString(1));
                userDetailsMap.put("UserName", rs.getString(2));
                userDetailsMap.put("UserFullName", rs.getString(3));
                userDetailsMap.put("UserEmail", rs.getString(4));
                userDetailsMap.put("UserPersonnelRank", rs.getString(5));
                userDetailsMap.put("UserDateCreated", rs.getString(6));
                userDetailsMap.put("UserType", rs.getString(7));
                userDetailsMap.put("UserStatus", rs.getString(8));
                userDetailsMap.put("UserPasswordReset", rs.getString(9));
                userDetailsMap.put("UserPersonnelId", rs.getString(10));

            }
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return userDetailsMap;
    }


    public static void setUserName(String userName) {
        GlobalCC.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    public static byte[] getBytes(InputStream is) {

        int len;
        int size = 1024;
        byte[] buf = null;
        try {
            if (is instanceof ByteArrayInputStream) {

                size = is.available();

                buf = new byte[size];
                len = is.read(buf, 0, size);
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                buf = new byte[size];
                while ((len = is.read(buf, 0, size)) != -1)
                    bos.write(buf, 0, len);
                buf = bos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf;
    }

    public static String showPopup(String popupId) {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + popupId +
                             "').show(hints);");
        return null;
    }

    /**
     * Method for hiding a popup.
     * @param popupId
     * @return
     */

    public static String hidePopup(String popupId) {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + popupId +
                             "').hide(hints);");

        return null;
    }

    public static String reportParameters(String rptCode, String paramType,
                                          String parentCode) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection connect = null;
        connect = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String Query = null;
        String rptQuery = null;
        try {
            Query =
                    "SELECT RPTP_PARAM_PROMPT, RPTP_PARAM_TYPE, RPTP_PARENT_CODE, RPTP_QUERY,RPTP_PARAM_NAME, RPTP_PARAM_CLAUSE, RPTP_USER_REQUIRED " +
                    "FROM TQC_SYS_RPT_PARAMETERS " +
                    " WHERE RPTP_RPT_CODE = ? " + " AND RPTP_PARAM_TYPE= ? " +
                    " ORDER BY RPTP_PARENT_CODE ASC NULLS FIRST,RPTP_CODE";
            connect = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)connect.prepareCall(Query);
            cst.setString(1, rptCode);
            cst.setString(2, paramType);
            // cst.setString(3, parentCode);
            cst.executeQuery();
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                rptQuery = rs.getString(4);
            }
            rs.close();
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }

        return rptQuery;
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        // a null string is invalid
        if (emailAddress == null)
            return false;

        // a string without a "@" is an invalid email address
        if (emailAddress.indexOf("@") < 0)
            return false;

        // a string without a "."  is an invalid email address
        if (emailAddress.indexOf(".") < 0)
            return false;

        if (lastEmailFieldTwoCharsOrMore(emailAddress) == false)
            return false;

        try {
            InternetAddress internetAddress =
                new InternetAddress(emailAddress);
            return true;
        } catch (AddressException ae) {
            // log exception
            return false;
        }
    }


    /**
     * Returns true if the last email field (i.e., the country code, or something
     * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
     * must be to be legal.
     */
    private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress) {
        if (emailAddress == null)
            return false;
        StringTokenizer st = new StringTokenizer(emailAddress, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        if (lastToken.length() >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public static BigDecimal checkBDNullValues(Object objName) {

        BigDecimal objectValue = null;
        if (objName == null) {

        } else {
            String val = null;
            val = objName.toString();
            if (val != null) {
                if (val.matches("[0-9\\.]+")) {
                    objectValue = new BigDecimal(val);
                }
            }
        }
        return objectValue;
    }

    public static boolean findManLifeCover(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String genProp = "N";
        boolean retVal = false;
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "SELECT PROD_MANUAL_LC_FACTOR FROM LMS_PRODUCTS WHERE PROD_CODE = ? ";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(1, prodCode);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                genProp = rs.getString(1);
            }
            if (genProp == null) {
                genProp = "N";
            }
            if (genProp.equalsIgnoreCase("Y")) {
                retVal = true;
            } else {
                retVal = false;
            }
            rs.close();
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return retVal;
    }
    
    public static String findParameter(String param, OracleConnection conn) {
            String val = "XYZ";
            OracleCallableStatement cst = null;
            try {

                String sysQuery =
                    "begin TQ_LMS.LMS_ORD_MISC.System_param_val(?,?,?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(sysQuery);
 
                cst.setString(1, param);
                cst.registerOutParameter(2, OracleTypes.VARCHAR);
                cst.setBigDecimal(3, null);
                 
                cst.execute();
                val = cst.getString(2);
                if(val==null){
                    val = "XYZ";
                }
                cst.close();
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            return val;
        }

    public static String findProdLifeRider(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String lifeFact = "N";
        OracleCallableStatement cst = null;
        try {
            String sysQuery =
                "begin ? := LMS_ORD_MISC.Check_mandatory_life_covers(?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);
            cst.setBigDecimal(2, prodCode);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.execute();
            if (cst.getString(1) != null) {
                lifeFact = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return lifeFact;
    }

    public static String findProdTermAuto(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String genProp = "N";
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "SELECT PROD_CALC_TERM_FR_RTR_AGE FROM LMS_PRODUCTS WHERE PROD_CODE = ? ";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(1, prodCode);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                genProp = rs.getString(1);
            }
            rs.close();
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return genProp;
    }

    public static String AllowPropInit(BigDecimal prodCode, String initFrom) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection connect = null;
        connect = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String Query = null;
        String success = "N";

        try {
            Query = "begin LMS_WEB_PKG_MKT.Allow_prop_initn(?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(Query);
            cst.setBigDecimal(1, prodCode);
            cst.setString(2, initFrom);
            cst.execute();
            cst.close();
            connect.commit();
            connect.close();
            success = "Y";
        } catch (Exception e) {
            success = "N";
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }

        return success;
    }

    public static BigDecimal findAnb(BigDecimal clientCode, String joint,
                                     BigDecimal jointCode, BigDecimal term,
                                     String effectiveDate) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        BigDecimal anb = null;
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin LMS_WEB_PKG_MKT.Get_age(?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(1, clientCode);
            cst.setBigDecimal(2, jointCode);
            cst.setString(3, effectiveDate);
            cst.setString(4, joint);
            cst.setBigDecimal(5, term);
            cst.registerOutParameter(6, OracleTypes.NUMBER);
            cst.execute();
            anb = cst.getBigDecimal(6);
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return anb;
    }

    public static String findGrpLifeRider(BigDecimal polCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String lifeFact = "N";
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin ? := LMS_ORD_MISC.Get_Grp_Life_Rider(?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(2, polCode);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.execute();
            if (cst.getString(1) != null) {
                lifeFact = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return lifeFact;
    }

    public static String checkUserRights(String userName, String process,
                                         String processArea,
                                         String processSubArea,
                                         BigDecimal amount, String drCr) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection connect = null;
        connect = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String Query = null;
        String rights = null;

        try {
            Query =
                    "begin ?:= TQC_INTERFACES_PKG.check_user_rights(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(Query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, userName);
            cst.setInt(3, sysCode);
            cst.setString(4, process);
            cst.setString(5, processArea);
            cst.setString(6, processSubArea);
            cst.setBigDecimal(7, amount);
            cst.setString(8, drCr);
            cst.execute();
            if (cst.getString(1) != null) {
                rights = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }

        return rights;
    }

    public static String findCalcTermRetAgeMkt(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String lifeFact = "N";
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin ? := LMS_ORD_MISC.calc_term_fr_rtr_age_Mkt(?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(2, prodCode);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.execute();
            if (cst.getString(1) != null) {
                lifeFact = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return lifeFact;
    }

    public static String findProdType(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String genProp = "N";
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "SELECT PROD_TYPE FROM LMS_PRODUCTS WHERE PROD_CODE = ? ";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setBigDecimal(1, prodCode);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                genProp = rs.getString(1);
            }
            rs.close();
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return genProp;
    }

    public static String findProductLoanable(BigDecimal prodCode,
                                             OracleConnection conn) {
        String loanable = null;
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin ? := LMS_WEB_CURSOR.productLoanable(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(sysQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setBigDecimal(2, prodCode);
            cst.execute();
            loanable = cst.getString(1);
            cst.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return loanable;
    }

    public static String findIfMortgageInterest(BigDecimal prodCode,
                                                OracleConnection conn) {
        String loanable = null;
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin ? := LMS_WEB_PKG.Display_inv_intr(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(sysQuery);

            //register out
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setBigDecimal(2, prodCode);
            cst.execute();
            loanable = cst.getString(1);
            cst.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return loanable;
    }

    public static String findProplifeCoverFact(BigDecimal prodCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String lifeFact = "N";
        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin LMS_ORD_MISC.System_prod_param_val(?,?,?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            //register out
            cst.setString(1, "USE_TERM_AS_LC_FACTOR");
            cst.setBigDecimal(2, prodCode);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            if (cst.getString(3) != null) {
                lifeFact = cst.getString(3);
            }
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return lifeFact;
    }

    public static BigDecimal extractBigDecimal(Object obj) {
        BigDecimal value = new BigDecimal("0");
        if (obj == null) {

        } else if (obj.toString().length() == 0) {

        } else {
            value = new BigDecimal(obj.toString());
        }
        return value;
    }


    public static String getSysParamValue(String paramName) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String ret = "";

        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin ? := Tqc_Parameters_Pkg.get_param_varchar(?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setString(2, paramName);
            stmt.execute();
            ret = GlobalCC.checkNullValues(stmt.getString(1));
            //System.out.println("PARAM: "+paramName+"=>"+ret);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, null);
        }
        return ret;
    }

    public static void updateEmail(Object emailCode, String status) {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;

        String ret = "";

        try {

            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin Tqc_setups_Pkg.update_email_msg(?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.setObject(1, emailCode);
            stmt.setString(2, null);
            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.setString(5, status);
            stmt.execute();
            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();

        }
    }

    public static String parseDateWithTime(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        System.out.println("Date === " + date);
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String timeStamp(String format) {
        Date curDate = new Date();
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(curDate);
    }

    public static BigDecimal BDZeroValue(String s) { //Zero Default
        if ((s != null) && ("".equals(s) != true)) {
            return new BigDecimal(s.trim());
        }
        return new BigDecimal("0");
    }

    public static BigDecimal BDNullValue(String s) { //Null Default
        if ((s != null) && ("".equals(s) != true)) {
            return new BigDecimal(s.trim());
        }
        return null;
    }

    public static boolean isEmptyStr(Object s) {
        return (checkNullValues(s) == null);
    }

    public static boolean isEmptyBD(Object b) {
        if (b != null) {
            return BigDecimal.ZERO.compareTo((BigDecimal)b) == 0;
        }
        return true;
    }

    public static Boolean validateUploadedSignImg(UploadedFile file,
                                                  String imgName) {
        // Add event code here...
        UploadedFile _file = file;


        if (_file.getContentType().equalsIgnoreCase("image/jpeg") ||
            _file.getContentType().equalsIgnoreCase("image/gif") ||
            _file.getContentType().equalsIgnoreCase("image/png")) {


            InputStream Reader;

            try {
                long Val = _file.getLength();

                if (Val == 0) {

                    return false;
                }
                Reader = _file.getInputStream();
                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(Reader);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                System.out.println("*********width***** " + w +
                                   "*******heigh****" + h);
                if (w <= 200.00 && h <= 20.00) {

                    return true;

                } else {
                    GlobalCC.errorValueNotEntered("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '200 x 20 px' ");

                    return false;
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.errorValueNotEntered("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                return false;
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be saved  :: Ensure the file  type is  .jpg/.gif/.png ");
            return false;
        }
    }

    public static String checkUploadedSignImg(UploadedFile file,
                                              String imgName) {
        // Add event code here...
        UploadedFile _file = file;
        String test = "";

        if (_file.getContentType().equalsIgnoreCase("image/jpeg") ||
            _file.getContentType().equalsIgnoreCase("image/gif") ||
            _file.getContentType().equalsIgnoreCase("image/png")) {


            InputStream Reader;

            try {
                long Val = _file.getLength();

                if (Val == 0) {

                    return "very long";
                }
                Reader = _file.getInputStream();
                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(Reader);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                test = "*********width***** " + w + "*******heigh****" + h;
                //System.out.println("*********width***** "+w +"*******heigh****"+h);
                if (w <= 200.00 && h <= 20.00) {

                    test = "good";

                } else {
                    GlobalCC.errorValueNotEntered("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '200 x 20 px' ");

                    return "Exceeds the size";
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.errorValueNotEntered("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                //  return "Erronieous file";
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be saved  :: Ensure the file  type is  .jpg/.gif/.png ");
            //return false;
        }
        return test;
    }

    public static String join(List<String> items, String delim) {
        StringBuilder sb = new StringBuilder();

        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                sb.append(items.get(i));
                if (items.size() >= 2 && i < (items.size() - 1) &&
                    delim != null) {
                    sb.append(delim);
                }
            }
        }
        return sb.toString();
    }

    public static void refreshUI(UIComponent o) {
        try {
            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            if (ctx != null && o != null) {
                ctx.addPartialTarget(o);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public static long dateDiff(Date dt1, Date dt2) {
        long diff = dt2.getTime() - dt1.getTime();
        return (long)((diff) / (1000 * 60 * 60 * 24));
    }

    public static boolean tableExists(String tableName) {
        //                            int count=0;
        //                            try{
        //                                String query ="SELECT count(1)  FROM ALL_TABLES where UPPER(table_name) = UPPER('"+tableName+"') ";
        //                                count=IQuery.fetchInt(query);
        //                                if(count<=0)
        //                                {
        //                                    GlobalCC.INFORMATIONREPORTING("Table: '"+tableName+"' doesn't exist! ");
        //                                }
        //                            }catch(Exception e)
        //                            {
        //                                    GlobalCC.EXCEPTIONREPORTING(e);
        //                            }
        //
        //                            return (count>0);
        return true; //Special case: AIICO doesn't allow access rights to SYS objects.
    }

    public static String extractSubStr(String val, String regx) {
        Pattern pattern = Pattern.compile(regx);

        Matcher matcher = pattern.matcher(val);
        if (matcher.matches()) {
            return (matcher.group(1));
        }
        return null;
    }

    public static String convertDate(Object value, String fromFmt,
                                     String toFmt) {
        Date date = null;
        String dateStr = null;
        try {
            String theDate = GlobalCC.checkNullValues(value);
            if (theDate == null) {
                return null;
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat(fromFmt);
            date = sdf1.parse(theDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat(toFmt); //"yyyy/MM/dd"
            dateStr = sdf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static java.util.Date getDate(String someDate, String dateFormat) {
        java.util.Date date = null;

        String theDate = GlobalCC.checkNullValues(someDate);
        String theFormat = GlobalCC.checkNullValues(dateFormat);

        if (theDate == null || theFormat == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(theFormat);
            date = sdf.parse(theDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String jsonValue(Object objName) {
        String objValue = GlobalCC.checkNullValues(objName);
        if (objValue != null) {
            if (!objValue.equals("{}")) {
                objValue = objValue.toString().trim();
            } else {
                objValue = null;
            }
        }
        return objValue;
    }

    public static String checkSmsNo(String zipCode, RichInputText uPrefix,
                                    RichInputText uSuffix) {
        if (uPrefix != null && uSuffix != null) {
            String prefix = GlobalCC.checkNullValues(uPrefix.getValue());
            String sms = GlobalCC.checkNullValues(uSuffix.getValue());
            String code = (zipCode != null) ? "+" + zipCode : "0";
            if (sms != null) {
                if (uPrefix.isVisible()) {
                    if (prefix != null) {
                        if (prefix.startsWith("0")) {
                            prefix = prefix.replaceFirst("0", "");
                        }
                        return (code + prefix + sms);
                    }
                } else {
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                    }
                    return code + sms;
                }
                return sms;
            }
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String viewContext(String page) {

        DCBindingContainer binding = ADFUtils.getDCBindingContainer();
        if (binding != null) {
            int t = 0;
            while (t < binding.getCtrlBindingList().size()) {
                if (binding.getCtrlBindingList().get(t).toString().contains("Iter")) {
                    DCIteratorBinding dciter =
                        ADFUtils.findIterator(binding.getCtrlBindingList().get(t).toString());
                    DataControl dc = dciter.getDataControl();
                    dc.release(dc.REL_DATA_REFS);
                }
                t++;
            }
        }
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        String viewId = page + ".jspx";
        ControllerContext controllerCtx = null;
        controllerCtx = ControllerContext.getInstance();
        String activityURL = controllerCtx.getGlobalViewActivityURL(viewId);
        try {
            LOVDAO lov = new LOVDAO();
            lov.reloadVals();
            ectx.redirect(activityURL);
        } catch (IOException e) {
            //Can't redirect
            e.printStackTrace();
        }
        /*LOVDAO lov = new LOVDAO();
         lov.reloadVals();
         FacesContext fc = FacesContext.getCurrentInstance();
         UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/"+page+".jspx");
         fc.setViewRoot(viewRoot);
         fc.renderResponse();*/

        return null;
    }

    public static String findUnitLinked(BigDecimal polCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String unitLinked = "N";
        OracleCallableStatement cst = null;
        try {
            String sysQuery = "begin ? := LMS_ORD_MISC.Get_param(?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setBigDecimal(2, polCode);
            cst.setString(3, "UNIT_LINKED");
            cst.execute();
            if (cst.getString(1) != null) {
                unitLinked = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return unitLinked;
    }

    public static String findInvestAllPremium(BigDecimal polCode) {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String invest = "N";
        OracleCallableStatement cst = null;
        try {
            String sysQuery = "begin ? := LMS_ORD_MISC.Get_param(?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setBigDecimal(2, polCode);
            cst.setString(3, "INVEST_ALL_PREMIUMS");
            cst.execute();
            if (cst.getString(1) != null) {
                invest = cst.getString(1);
            }
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return invest;
    }

    public static String countProdOptions(BigDecimal prodCode) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection connect = null;
        connect = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String Query = null;
        String valid = "N";
        try {
            Query = "begin  LMS_WEB_PKG2.count_prod_options(?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(Query);
            cst.setBigDecimal(1, prodCode);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.execute();
            valid = cst.getString(2);
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }

        return valid;
    }

    public static BigDecimal findClientCode(BigDecimal prpCode) {
        BigDecimal clientCode = null;
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        conn = dbConnector.getDatabaseConnection();
        String query = "begin LMS_WEB_CURSOR_UND.clientDetails(?,?); end;";
        OracleCallableStatement cst = null;
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, prpCode);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                clientCode = rs.getBigDecimal(1);
            }
            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return clientCode;
    }

    public static String findProcessFlowEnabled() {
        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String enabled = "N";
        OracleCallableStatement cst = null;
        try {
            String sysQuery =
                "begin LMS_ORD_MISC.System_param_val(?,?,?); end;";
            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);
            cst.setString(1, "LMS_USE_PROCESS_FLOW");
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setString(3, null);
            cst.execute();
            if (cst.getString(2) != null) {
                enabled = cst.getString(2);
            }
            cst.close();
            connect.commit();
            connect.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connect, e);
        }
        return enabled;
    }

    public static String resetSessionMap() {
        try {
            DCBindingContainer binding = ADFUtils.getDCBindingContainer();
            int t = 0;
            if (binding != null) {
                while (t < binding.getCtrlBindingList().size()) {
                    if (binding.getCtrlBindingList().get(t).toString().contains("Iter")) {
                        DCIteratorBinding dciter =
                            ADFUtils.findIterator(binding.getCtrlBindingList().get(t).toString());
                        DataControl dc = dciter.getDataControl();
                        dc.release(dc.REL_DATA_REFS);
                    }
                    t++;
                }

            }
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

                fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
            }
            if (fc.getExternalContext().getSessionMap().containsKey("LOVCC")) {

                fc.getExternalContext().getSessionMap().remove("LOVCC");
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getRequestMap().size();
            int v = 0;
            Map<String, Object> map2 =
                FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
            //FacesContext.getCurrentInstance().getExternalContext().getRequestMap().getClass()

            Set<String> set2 =
                context.getExternalContext().getRequestMap().keySet();
            Object[] obj2 = set2.toArray();
            String values2 = null;
            while (v < map2.size()) {

                values2 = obj2[v].toString();
                if (!values2.contains(".") && !values2.contains("_") &&
                    !values2.contains("ADF")) {
                    context.getExternalContext().getRequestMap().remove(values2);
                }
                v++;
            }


            FacesContext facesContext = FacesContext.getCurrentInstance();

            // some date in the past
            facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.pageFlowCache");
            facesContext.getExternalContext().getSessionMap().remove("ORA_adf_sessionScope");
            facesContext.getExternalContext().getSessionMap().remove("org.apache.myfaces.trinidadinternal.application.StateManagerImp.ACTIVE_PAGE_STATE");
            facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.sessionBasedScopes");

            Map<String, Object> map =
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            int k = 0;


            Set<String> set =
                facesContext.getExternalContext().getSessionMap().keySet();
            Object[] obj = set.toArray();
            String values = null;
            while (k < map.size()) {
                values = obj[k].toString();
                if (values.contains("VIEW_CACHE")) {
                    facesContext.getExternalContext().getSessionMap().remove(values);
                }
                k++;
            }
            k = 0;
            map =
FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            set = facesContext.getExternalContext().getSessionMap().keySet();
            obj = set.toArray();
            while (k < map.size()) {
                values = obj[k].toString();
                k++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentAssignee(BigDecimal ticketID) {

        DBConnector dbConn = new DBConnector();
        OracleConnection connect = null;
        connect = dbConn.getDatabaseConnection();
        String ticketNameModified = null;
        OracleCallableStatement cst = null;

        String currentAssignee = "";

        try {

            String sysQuery =
                "SELECT nvl(assignee_,TCKT_TO) FROM tqc_bpm_tickets, jbpm4_task WHERE dbid_ = tckt_code AND tckt_active = 'Y' AND tckt_code = ? ";

            cst = (OracleCallableStatement)connect.prepareCall(sysQuery);

            cst.setBigDecimal(1, ticketID);

            OracleResultSet rs = (OracleResultSet)cst.executeQuery();

            while (rs.next()) {
                currentAssignee = rs.getString(1);
            }

            rs.close();
            cst.close();
            connect.commit();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
            return currentAssignee;
        }
        return currentAssignee;
    }

    public static boolean validate(Object input, String vPattern) {
        String val = GlobalCC.checkNullValues(input);
        if (val != null) {
            Pattern pattern = Pattern.compile(vPattern);
            Matcher matcher = pattern.matcher(val);
            return matcher.matches();
        }
        return false;
    }

    public static String logoutUser() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;
        String value = null;
        try {
            HttpSession session =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query1 = "begin tqc_web_pkg.logout_user(?,?); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query1);
                stmt.setString(1, (String)session.getAttribute("Username"));
                stmt.setBigDecimal(2, BigDecimal.ZERO);
                stmt.execute();
                conn.close();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;

        }


        return status;
    }

    public static int getAge(java.util.Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) -
             today.get(Calendar.DAY_OF_YEAR) > 3) ||
            (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) ==
                    today.get(Calendar.MONTH)) &&
                   (birthDate.get(Calendar.DAY_OF_MONTH) >
                    today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    public static boolean contains(String[] array, String val) {
        for (String v : array) {
            if (v != null && val != null) {
                if (v.equals(val))
                    return true;
            }
        }
        return false;
    }

    public static HashMap<String, String> fetchObject(String query) {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> row = new HashMap<String, String>();
        try {
            System.out.println("query: " + query);
            conn = (Connection)datahandler.getDatabaseConnection();
            stmt = (CallableStatement)conn.prepareCall(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData mt = rs.getMetaData();
                for (int i = 1; i <= mt.getColumnCount(); i++) {
                    String key = mt.getColumnName(i).toLowerCase();
                    if (key != null) {
                        row.put(key, rs.getString(i));
                        //System.out.println(key+"=>"+rs.getString(i));
                    }
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return row;
    }

    public static boolean executeSql(String sql) {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement stmt = null;
        boolean result = false;
        try {
            System.out.println("Executing query: " + sql);
            conn = (Connection)datahandler.getDatabaseConnection();
            stmt = (CallableStatement)conn.prepareCall(sql);
            stmt.execute();
            conn.commit();
            result = true;
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, null);
        }
        return result;
    }

    public static UIComponent getUIComponent(UIComponent component,
                                             String name) {
        if (component != null)
            System.out.println(component.getId());

        List<UIComponent> items = component.getChildren();
        Iterator<UIComponent> facets = component.getFacetsAndChildren();

        if (items.size() > 0) {
            System.out.println("got childern");
            for (UIComponent item : items) {
                UIComponent found = getUIComponent(item, name);
                if (found != null) {
                    return found;
                }
                if (item.getId().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        } else if (facets.hasNext()) {
            System.out.println("got facets");
            while (facets.hasNext()) {
                UIComponent facet = facets.next();
                UIComponent found = getUIComponent(facet, name);
                if (found != null) {
                    return found;
                }
                if (facet.getId().equalsIgnoreCase(name)) {
                    return facet;
                }
            }
        }
        return null;
    }

    public static String sendSms(String descc, String msg, String user) {
        String url = null;
        String username = null;
        String password = null;
        String source = null;
        String desc = null;
        String message = null;
        String message1 = null;
        String destination = null;
        String success = null;
        String destination_email = null;


        if (descc != null || msg != null) {
            desc = descc;
            message =
                    msg.substring(msg.lastIndexOf(':') + 1).replaceAll(" ", "");


        }


        String query =
            "SELECT SUBSTR (tss_url, 1, 2008), tss_username, tss_password, tss_source  FROM tqc_system_sms";

        String query2 =
            "SELECT usr_cell_phone_no, usr_email  FROM tqc_users where usr_name = ? ";

        String query3 =
            "UPDATE TQC_USERS SET USR_SMS_CODE = ?   where usr_name = ? ";

        OracleCallableStatement cst = null;
        OracleCallableStatement cst2 = null;
        OracleCallableStatement cst3 = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        try {

            conn = datahandler.getDatabaseConnection();

            cst2 = (OracleCallableStatement)conn.prepareCall(query2);
            cst2.setString(1, user);
            OracleResultSet rs2 = (OracleResultSet)cst2.executeQuery();
            cst3 = (OracleCallableStatement)conn.prepareCall(query3);

            if (rs2.next()) {
                destination = rs2.getString(1);
                destination_email = rs2.getString(2);
            }


            cst = (OracleCallableStatement)conn.prepareCall(query);


            OracleResultSet rs = (OracleResultSet)cst.executeQuery();

            if (rs.next()) {
                url = rs.getString(1);
                username = rs.getString(2);
                password = rs.getString(3);
                source = rs.getString(4);
            }


            cst3.setString(1, message);
            cst3.setString(2, user);
            cst3.execute();

            cst.close();
            cst2.close();
            cst3.close();
            rs2.close();
            rs.close();
            conn.close();


            url = url.replace("[USERNAME]", URLEncoder.encode(username));
            url = url.replace("[PASSWORD]", URLEncoder.encode(password));
            url = url.replace("[SOURCE]", URLEncoder.encode(source));
            url = url.replace("[DESTINATION]", URLEncoder.encode(destination));
            url = url.replace("[MESSAGE]", URLEncoder.encode(msg));
            URL url2 = new URL(url);

            //URLEncoder.encode(arg0, arg1)
            HttpURLConnection connection =
                (HttpURLConnection)url2.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);

            System.out.println("connection :" + connection);
            String res = connection.getResponseMessage();

            System.out.println("res :" + res);

            int code = connection.getResponseCode();
            System.out.println("res :" + code);
            if (code == HttpURLConnection.HTTP_OK) {


                //
                //                TurnQuest.view.Base.GlobalCC.sysInformation("Verification code has been sent to:" +
                //                                                            destination);

                connection.disconnect();


            } else {
                TurnQuest.view.Base.GlobalCC.errorValueNotEntered("Test SMS Not sent:" +
                                                                  res);
                connection.disconnect();
            }


            GlobalCC globalCC = new GlobalCC();
            success =
                    globalCC.applicationEmail(destination_email, null, "Verification Code",
                                              message);
            if (success.equalsIgnoreCase("success")) {
                GlobalCC.sysInformation("Verification code has been sent to :" +
                                        destination_email + " and " +
                                        destination);
            }

        } catch (Exception e) {
            TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public static String timeStampStr(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Date date = new Date();
        date.setTime(ts.getTime());
        String formattedDate =
            new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
        return formattedDate;
    }
    
    public static boolean isBDEqual(final BigDecimal bg1, final BigDecimal bg2) {
        if(bg1 == null && bg2 == null){
            return true;
        }
        
        if(bg1 == null){
            return false;
        } else if (bg2 == null) {
            return false;
        } else {
            return bg1.compareTo(bg2) == 0;
        }
    }
}
