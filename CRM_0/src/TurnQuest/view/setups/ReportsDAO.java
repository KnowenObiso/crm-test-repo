package TurnQuest.view.setups;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


public class ReportsDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public ReportsDAO() {
        super();
    }

    public List<Report> fetchSystemRptModules() {
        List<Report> reportsList = new ArrayList<Report>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= TQC_SETUPS_CURSOR.getSystemRptModules;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Report report = new Report();
                report.setSrmCode(resultSet.getBigDecimal(1));
                report.setSrmName(resultSet.getString(2));
                report.setSrmDesc(resultSet.getString(3));
                report.setSrmSysCode(resultSet.getBigDecimal(4));
                report.setSrmSysName(resultSet.getString(5));
                reportsList.add(report);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return reportsList;
    }

    public List<Report> fetchSystemRptSubModules() {
        List<Report> reportsList = new ArrayList<Report>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getsystemrptsubmodules(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("srmCode"));
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Report report = new Report();
                report.setRsmCode(resultSet.getBigDecimal(1));
                report.setRsmName(resultSet.getString(2));
                report.setRsmDesc(resultSet.getString(3));
                reportsList.add(report);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return reportsList;
    }

    public List<Report> fetchUnAssignedReports() {
        List<Report> reportsList = new ArrayList<Report>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getReportsUnAssigned(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("rsmCode"));
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Report report = new Report();
                report.setRptCode(resultSet.getBigDecimal(1));
                report.setRptName(resultSet.getString(2));
                report.setRptDesc(resultSet.getString(3));
                report.setRptActive(resultSet.getString(4));
                report.setSelected(false);
                reportsList.add(report);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return reportsList;
    }

    public List<Report> fetchAssignedReports() {
        List<Report> reportsList = new ArrayList<Report>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getReportsAssigned(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("rsmCode"));
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                Report report = new Report();
                report.setRptCode(resultSet.getBigDecimal(1));
                report.setRptName(resultSet.getString(2));
                report.setRptDesc(resultSet.getString(3));
                report.setRptActive(resultSet.getString(4));
                report.setSelected(false);
                reportsList.add(report);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return reportsList;
    }
}
