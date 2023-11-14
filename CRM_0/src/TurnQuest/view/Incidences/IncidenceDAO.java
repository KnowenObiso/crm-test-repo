package TurnQuest.view.Incidences;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class IncidenceDAO {
    public IncidenceDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<IncidenceValues> findIncidenceTypes() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_INCIDENTS_WEB_CURSOR.find_incidence_types(?); end;";

        List<IncidenceValues> orgData = new ArrayList<IncidenceValues>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                IncidenceValues DivDef = new IncidenceValues();

                DivDef.setINCT_CODE(rs.getBigDecimal(1));
                DivDef.setINCT_SHT_DESC(rs.getString(2));
                DivDef.setINCT_DESC(rs.getString(3));
                DivDef.setINCT_OWNER(rs.getString(4));
                DivDef.setINCT_OWNER_DESC(rs.getString(5));


                orgData.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<IncidenceValues> findIncidenceStatuses() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_INCIDENTS_WEB_CURSOR.getIncActions(?); end;";

        List<IncidenceValues> orgData = new ArrayList<IncidenceValues>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                IncidenceValues DivDef = new IncidenceValues();


                DivDef.setICTY_CODE(rs.getBigDecimal(1));
                DivDef.setICTY_SHT_DESC(rs.getString(2));
                DivDef.setICTY_DESC(rs.getString(3));
                DivDef.setICTY_DATE(rs.getDate(4));


                orgData.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<IncidenceValues> findIncidenceDepartments() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_INCIDENTS_WEB_CURSOR.FindIncDepartmentsBySystem(?,?); end;";

        List<IncidenceValues> orgData = new ArrayList<IncidenceValues>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incSysCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                IncidenceValues DivDef = new IncidenceValues();


                DivDef.setIDEP_CODE(rs.getBigDecimal(1));
                DivDef.setIDEP_SHT_DESC(rs.getString(2));
                DivDef.setIDEP_DESC(rs.getString(3));
                DivDef.setIDEP_BRN_CODE(rs.getBigDecimal(4));
                DivDef.setBRN_SHT_DESC(rs.getString(5));
                DivDef.setBRN_NAME(rs.getString(6));


                orgData.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<IncidenceValues> findSystemBranches() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_INCIDENTS_WEB_CURSOR.find_system_branches(?,?); end;";

        List<IncidenceValues> orgData = new ArrayList<IncidenceValues>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incSysCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                IncidenceValues DivDef = new IncidenceValues();

                DivDef.setBRN_CODE(rs.getBigDecimal(1));
                DivDef.setBRN_SHT_DESC(rs.getString(2));
                DivDef.setBRN_NAME(rs.getString(3));

                orgData.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<IncidenceValues> findIDepartmentIncidTypes() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_INCIDENTS_WEB_CURSOR.FindIncTypes(?,?); end;";

        List<IncidenceValues> orgData = new ArrayList<IncidenceValues>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incDeptCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                IncidenceValues DivDef = new IncidenceValues();


                DivDef.setDEPICT_CODE(rs.getBigDecimal(1));
                DivDef.setDEPICT_INCT_CODE(rs.getBigDecimal(2));
                DivDef.setDEPICT_IDEP_CODE(rs.getBigDecimal(3));
                DivDef.setINCT_SHT_DESC(rs.getString(4));
                DivDef.setDEPICT_SEND_EMAIL(rs.getString(5));
                DivDef.setDEPICT_PRIORITY_LVL(rs.getString(6));
                DivDef.setDEPICT_ESCALATE(rs.getString(7));
                DivDef.setDEPICT_ESC_DAYS(rs.getBigDecimal(8));
                DivDef.setDEPICT_CLOSE_DAYS(rs.getBigDecimal(9));
                DivDef.setINCT_DESC(rs.getString(10));


                orgData.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }
}
