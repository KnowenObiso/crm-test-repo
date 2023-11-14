package TurnQuest.view.Login;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class Authenticator {
    public Authenticator() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public String Authenticate() {
        HttpServletRequest rq =
            (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();


        //session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session.getAttribute("UserCode") == null &&
            (rq.getParameter("aid") == null)) {
            String message = "Not logged In";
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(message));

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.jspx");
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }
        } else if ((rq.getParameter("aid") != null) &&
                   session.getAttribute("UserCode") == null) {

            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement callStmt = null;
            conn = datahandler.getDatabaseConnection();
            if (conn == null) {
                String message =
                    "Error Connecting to Database. Contact Your System Administrator";
                GlobalCC.errorValueNotEntered(message);
                return null;
            }

            try {


                String jobquery =
                    "begin tqc_web_cursor.getUserName(?,?,?); end;";

                callStmt = (OracleCallableStatement)conn.prepareCall(jobquery);
                callStmt.setBigDecimal(1,
                                       new BigDecimal(rq.getParameter("aid")));
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.registerOutParameter(3, OracleTypes.DATE);
                callStmt.execute();
                session.setAttribute("Username", callStmt.getString(2));
                session.setAttribute("LoginDate", callStmt.getDate(3));
                session.setAttribute("UserCode",
                                     new BigDecimal(rq.getParameter("aid")));

                //Index ind = new Index();
                // ind.actionSystemLogout();
                callStmt.close();
                conn.commit();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                DbUtils.closeQuietly(conn, callStmt, null);
            }

        } else {

            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "must-revalidate");
            response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
        }
        return null;
    }


    public String browseAuthenticate() {
        session.removeAttribute("Username");
        session.removeAttribute("UserCode");

        return null;
    }

}

