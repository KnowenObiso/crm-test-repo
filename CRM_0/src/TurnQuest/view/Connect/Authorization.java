package TurnQuest.view.Connect;


import TurnQuest.view.Base.GlobalCC;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;


public class Authorization {
    public Authorization() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public String checkUserRights(String process, String processArea,
                                  String processSubArea, BigDecimal amount,
                                  String drCr) {
        String accessGranted = "N";
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        
        OracleCallableStatement cst = null;
        try {

            String query =
                "begin ? := TQC_INTERFACES_PKG.check_user_rights(?,?,?,?,?,?,?); end;";
			conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);

            cst.setString(2, (String)session.getAttribute("Username"));
            cst.setInt(3, GlobalCC.sysCode);
            cst.setString(4, process);
            cst.setString(5, processArea);
            cst.setString(6, processSubArea);
            cst.setBigDecimal(7, amount);
            cst.setString(8, drCr);
            cst.execute();
            accessGranted = cst.getString(1);
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accessGranted;
    }

    public String checkUserRightsNoError(String process, String processArea,
                                         String processSubArea,
                                         BigDecimal amount, String drCr) {
        String accessGranted = "N";
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {

            String query =
                "begin ? := TQC_INTERFACES_PKG.check_user_rights(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);

            cst.setString(2, (String)session.getAttribute("Username"));
            cst.setInt(3, GlobalCC.sysCode);
            cst.setString(4, process);
            cst.setString(5, processArea);
            cst.setString(6, processSubArea);
            cst.setBigDecimal(7, amount);
            cst.setString(8, drCr);
            cst.execute();
            accessGranted = cst.getString(1);
            cst.close();
            conn.close();

        } catch (Exception e) {
            return accessGranted;
        }
        return accessGranted;
    }

    public String checkUserAuthRights(String process, String processArea,
                                      String processSubArea,
                                      BigDecimal transId) {
        String accessGranted = "N";
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {

            String query =
                "begin ? := TQC_INTERFACES_PKG.check_userAuth_rights(?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, (String)session.getAttribute("Username"));
            cst.setString(3, (String)session.getAttribute("sysCode"));
            cst.setString(4, process);
            cst.setString(5, processArea);
            cst.setString(6, processSubArea);
            cst.setBigDecimal(7, transId);
            cst.execute();
            accessGranted = cst.getString(1);
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accessGranted;
    }
    public String checkMakerChekerApplicable(String process, String processArea,
                                  String processSubArea, BigDecimal amount,
                                  String drCr,
                                  String preparedBy) {
        String accessGranted = "N";
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        
        OracleCallableStatement cst = null;
        try {

            String query =
                "begin ? := TQC_INTERFACES_PKG.check_makerChecker_rights(?,?,?,?,?,?,?,?); end;";
                        conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);

            cst.setString(2, (String)session.getAttribute("Username"));
            cst.setInt(3, GlobalCC.sysCode);
            cst.setString(4, process);
            cst.setString(5, processArea);
            cst.setString(6, processSubArea);
            cst.setBigDecimal(7, amount);
            cst.setString(8, drCr);
            cst.setString(9, preparedBy);
            cst.execute();
            accessGranted = cst.getString(1);
            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accessGranted;
    }
}

