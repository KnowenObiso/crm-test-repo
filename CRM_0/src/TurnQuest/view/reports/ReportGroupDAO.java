package TurnQuest.view.reports;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Divisions.Division;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class ReportGroupDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<ReportGroup> fetchAllReportGroups() {

        List<ReportGroup> reportGroupList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:=TQC_SETUPS_CURSOR.get_all_report_groups();end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            reportGroupList = new ArrayList<ReportGroup>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                ReportGroup reportGroup = new ReportGroup();

                reportGroup.setRptGroupCode(rs.getString(1));
                reportGroup.setRptGroupName(rs.getString(2));

                reportGroupList.add(reportGroup);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return reportGroupList;
    }

    public List<ReportGroupDivision> fetchReportGroupDivision() {

        List<ReportGroupDivision> reportGroupDivisionList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_rptGroupDiv(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            reportGroupDivisionList = new ArrayList<ReportGroupDivision>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("REPORT_GROUP_CODE"));
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(2);

            while (rs.next()) {
                ReportGroupDivision reportGroupDivision =
                    new ReportGroupDivision();

                reportGroupDivision.setRptGroupDivCode(rs.getString(1));
                reportGroupDivision.setRptGroupCode(rs.getString(2));
                reportGroupDivision.setDivCode(rs.getString(3));
                reportGroupDivision.setDivName(rs.getString(4));
                reportGroupDivision.setDivOrder(rs.getString(5));
                reportGroupDivisionList.add(reportGroupDivision);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return reportGroupDivisionList;
    }

    public List<Division> findUndefinedRptGroupDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_SETUPS_CURSOR.getUndefinedRptGrpDivisions(?,?); end;";

        List<Division> divisionList = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setObject(1, session.getAttribute("REPORT_GROUP_CODE"));
            callStmt.registerOutParameter(2, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(2);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setDIV_CODE(rs.getBigDecimal(1));
                DivDef.setDIV_NAME(rs.getString(2));
                DivDef.setDIV_SHT_DESC(rs.getString(3));
                DivDef.setDIV_DIVISION_STATUS(rs.getString(4));
                DivDef.setDivOrder(rs.getString(5));
                divisionList.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divisionList;
    }

    public List<Division> findRatingOrganizations() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ?:= TQC_SETUPS_CURSOR.get_ratings; end;";

        List<Division> divisionList = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            ResultSet rs = (ResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setRorgCode(rs.getBigDecimal(1));
                DivDef.setRorgDesc(rs.getString(2));
                DivDef.setRorgShtDesc(rs.getString(3));
                divisionList.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divisionList;
    }

    public List<Division> findRatingStartndards() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ?:= TQC_SETUPS_CURSOR.get_rating_starndards(?); end;";

        List<Division> divisionList = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("rorgCode"));
            callStmt.execute();
            ResultSet rs = (ResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division DivDef = new Division();
                DivDef.setOrsCode(rs.getBigDecimal(1));
                DivDef.setOrsDesc(rs.getString(2));
                divisionList.add(DivDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divisionList;
    }
}
