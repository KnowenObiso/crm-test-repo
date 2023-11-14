package TurnQuest.view.setups;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


public class DepartmentsDAO {
    public DepartmentsDAO() {
        super();
    }

    public List<Departments> fetchDepartmentDtls() {
        List<Departments> departsList = new ArrayList<Departments>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= TQC_SETUPS_CURSOR.getdepartments;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                Departments dep = new Departments();

                dep.setDepCode(rs.getBigDecimal(1));
                dep.setDepShtDesc(rs.getString(2));
                dep.setDepName(rs.getString(3));
                dep.setDepWef(rs.getDate(4));
                dep.setDepWet(rs.getDate(5));
                dep.setDepUsrCode(rs.getBigDecimal(6));
                dep.setUserName(rs.getString(7));
                dep.setUsersName(rs.getString(8));
                departsList.add(dep);
            }
            statement.close();
            rs.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return departsList;
    }

    public List<Departments> findUsers() {
        List<Departments> departsList = new ArrayList<Departments>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ?:= TQC_SETUPS_CURSOR.getUsers;end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet)statement.getObject(1);

            while (rs.next()) {
                Departments dep = new Departments();
                dep.setDepUsrCode(rs.getBigDecimal(1));
                dep.setUserName(rs.getString(2));
                dep.setUsersName(rs.getString(3));
                departsList.add(dep);
            }
            statement.close();
            rs.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return departsList;
    }
}
