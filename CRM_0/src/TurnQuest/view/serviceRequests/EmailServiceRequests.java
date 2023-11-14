package TurnQuest.view.serviceRequests;


import TurnQuest.view.Alerts.SystemAlerts;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

import java.io.IOException;

import java.math.BigDecimal;

import java.security.Security;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.FlagTerm;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class EmailServiceRequests {
    public EmailServiceRequests() {
        super();
    }

    public String chechEmails(OracleConnection conn) {
        String type = null;
        String host = null;
        String port = null;
        String user = null;
        String pass = null;
        String secure = null;
        Session session = null;
        Store store = null;
        Folder folder = null;

        String emailQuery =
            "SELECT  MAIL_USERNAME, MAIL_PASSWORD, MAIL_HOST,MAIL_PORT,MAIL_TYPE,MAIL_SECURE FROM TQC_SYSTEM_MAILS WHERE MAIL_IN_OUT = 'I'";

        String mainRsvdWd =
            "begin ? := TQC_SETUPS_CURSOR.getMainReservedWord(?); end; ";
        String otherRsvdWd =
            "begin ? := TQC_SETUPS_CURSOR.getMainReservedWord(?); end; ";

        OracleCallableStatement cst = null;
        OracleCallableStatement cstrsvd = null;
        try {
            cst = (OracleCallableStatement)conn.prepareCall(emailQuery);
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();

            while (rs.next()) {
                user = rs.getString(1);
                pass = rs.getString(2);
                host = rs.getString(3);
                port = rs.getString(4);
                type = rs.getString(5);
                secure = rs.getString(6);
            }
            cst.close();
            rs.close();
            if (user == null || "".equals(user)) {
                GlobalCC.errorValueNotEntered("Outgoing Email Value Not Defined Please Check");
                conn.close();
                return null;
            }
            if (pass == null  || "".equals(pass)) {
                GlobalCC.errorValueNotEntered("EMAILS_FROM_PASSWORD Not Defined Please Check");
                conn.close();
                return null;
            }

            System.out.println("--------------processing mails started-----------------");
            session = Session.getDefaultInstance(System.getProperties(), null);
            System.out.println("getting the session for accessing email.");
            //store = session.getStore("pop3");
            Properties props = System.getProperties();
            if (secure.equalsIgnoreCase("Y")) {
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            }

            if (type.equalsIgnoreCase("imap")) {
                if (secure.equalsIgnoreCase("Y")) {
                    props.setProperty("mail.imap.socketFactory.class",
                                      "javax.net.ssl.SSLSocketFactory");
                    props.setProperty("mail.imap.ssl", "true");
                } else {
                    props.setProperty("mail.imap.ssl", "false");
                }
                props.setProperty("mail.imap.socketFactory.fallback", "false");
                props.setProperty("mail.imap.port", port);
                props.setProperty("mail.imap.socketFactory.port", port);
                props.setProperty("mail.imap.host", host);

            }
            props.setProperty("protocal", type);
            props.setProperty("host", host);
            props.setProperty("port", port);
            int portVal = Integer.parseInt(props.getProperty("port"));
            session = Session.getDefaultInstance(props, null);
            URLName urln =
                new URLName(props.getProperty("protocal"), props.getProperty("host"),
                            portVal, null, user, pass);
            System.out.println("Started connecting to mail inbox ... ");
            if (type.equalsIgnoreCase("imap")) {
                store = new IMAPSSLStore(session, urln);
            } else {
                store = new POP3SSLStore(session, urln);
            }
            store.connect(host, new Integer(port), user, pass);
            System.out.println("Connection established with IMAP server.");
            // Get a handle on the default folder
            folder = store.getDefaultFolder();
            System.out.println("Getting the Inbox folder.");
            // Retrieve the "Inbox"
            folder = folder.getFolder("inbox");
            //Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_WRITE);
            System.out.println("Message Count Found " +
                               folder.getMessageCount());
            System.out.println("New Message Count " +
                               folder.getNewMessageCount());
            System.out.println("Unread Message Count " +
                               folder.getUnreadMessageCount());
            System.out.println("=========================================");
            Message[] newmessages = folder.getMessages();
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message messages[] = folder.search(ft);
            try {
                int i = 0;
                for (Message message : messages) {

                    Multipart mp = (Multipart)messages[i].getContent();
                    Object p = mp.getBodyPart(i).getContent();
                    String q = p.toString(); //object has the body content
                    messages[i].getFrom();
                    System.out.println("SUBJECT");
                    System.out.println(messages[i].getSubject());
                    System.out.println("BODY");
                    System.out.println(q); //prints the body
                    Address[] adds = messages[i].getAllRecipients();
                    String addressFrom = null;
                    int k = 0;
                    while (k < adds.length) {
                        if (k > 0) {
                            addressFrom =
                                    addressFrom + "," + adds[k].toString();
                        } else {
                            addressFrom = adds[k].toString();
                        }

                        k++;
                    }

                    // Check Which System It Is
                    cst =
(OracleCallableStatement)conn.prepareCall(mainRsvdWd);
                    cst.registerOutParameter(1, OracleTypes.CURSOR);
                    cst.setObject(2, null);
                    cst.execute();
                    rs = (OracleResultSet)cst.getObject(1);
                    cstrsvd =
                            (OracleCallableStatement)conn.prepareCall(otherRsvdWd);
                    cstrsvd.registerOutParameter(1, OracleTypes.CURSOR);
                    while (rs.next()) {
                        if (q.contains(rs.getString(7)) ||
                            messages[i].getSubject().contains(rs.getString(7))) {
                            // Check For Reserved Words
                            cstrsvd.setObject(2, rs.getBigDecimal(2));
                            cstrsvd.execute();
                            OracleResultSet rsRsvd =
                                (OracleResultSet)cstrsvd.getObject(1);
                            String catVal = "1";
                            String summary = messages[i].getSubject();
                            String desc = q;
                            String assignee = rs.getString(10);
                            String assigneeType = rs.getString(11);
                            String DateVal = (new Date()).toString();
                            String assigneeEmail = rs.getString(12);
                            String content =
                                "A Service Request From " + addressFrom +
                                " Regarding " + summary +
                                " has been Assigned to you. Please Deal";

                            int duration = rs.getInt(9);
                            DateVal = GlobalCC.parseDate(DateVal);
                            String dueDate = null;

                            int c = 0;
                            while (rsRsvd.next()) {
                                assignee = rsRsvd.getString(10);
                                duration = rsRsvd.getInt(9);
                                assigneeType = rsRsvd.getString(11);
                                assigneeEmail = rsRsvd.getString(12);
                                c++;
                            }
                            Calendar cal = GregorianCalendar.getInstance();
                            cal.setTime(new Date());
                            cal.add(cal.DATE, duration);
                            dueDate = cal.toString();
                            createServiceRequest(catVal, summary, desc,
                                                 assignee, DateVal, dueDate,
                                                 assigneeType, content,
                                                 assigneeEmail);
                            rsRsvd.close();
                            cstrsvd.close();
                            messages[i].setFlag(Flags.Flag.SEEN, true);
                        } else {
                            messages[i].setFlag(Flags.Flag.SEEN, false);
                        }
                    }
                    cst.close();
                    rs.close();

                    i++;
                }
            } catch (IOException e) {
            }
            conn.close();
            folder.close(true);
            store.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String createServiceRequest(String catVal, String summary,
                                       String desc, String assignee,
                                       String reqDateVal, String dueDate,
                                       String assigneeType, String content,
                                       String assigneeEmail) {

        String source = "Email";
        String accType = null;
        BigDecimal accCode = null;
        String ownType = null;
        String owner = null;
        if (reqDateVal.contains(":")) {
            reqDateVal = GlobalCC.parseDate(reqDateVal);
        } else {
            reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
        }
        if (dueDate.contains(":")) {
            dueDate = GlobalCC.parseDate(dueDate);
        } else if (dueDate.contains("/")) {
            dueDate = GlobalCC.upDateParseDateTwo(dueDate);
        } else {
            dueDate = GlobalCC.upDateParseDate(dueDate);
        }
        String statusVal = "Open";
        String resDate = null;
        String solu = null;
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.serviceRequestsProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "A");
            stmt.setBigDecimal(2, null);
            stmt.setString(3, catVal);
            stmt.setString(4, source);
            stmt.setString(5, accType);
            stmt.setBigDecimal(6, accCode);
            stmt.setString(7, reqDateVal);
            stmt.setString(8, assignee);
            stmt.setString(9, "U");
            stmt.setString(10, owner);
            stmt.setString(11, statusVal);
            stmt.setString(12, dueDate);
            stmt.setString(13, resDate);
            stmt.setString(14, summary);
            stmt.setString(15, desc);
            stmt.setString(16, solu);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            if (assigneeType.equalsIgnoreCase("U"))
                GlobalCC.applicationEmail(assigneeEmail, null,
                                          "SERVICE_REQUEST(INCOMING_EMAIL)",
                                          content);
            else {
                SystemAlerts sysAl = new SystemAlerts();
                List<String> receivers =
                    sysAl.getGroupedUsersEmails(new BigDecimal(assignee));
                for (int i = 0; i < receivers.size(); i++)
                    GlobalCC.applicationEmail(receivers.get(i), null,
                                              "SERVICE_REQUEST(INCOMING_EMAIL)",
                                              content);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }
}
