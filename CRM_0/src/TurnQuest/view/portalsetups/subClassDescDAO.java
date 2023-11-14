package TurnQuest.view.portalsetups;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class subClassDescDAO {


    public List<SubClass> findSubClassDesc() {
        List<SubClass> subClassList = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := Gis_Web_Pkg.getsubClassDetails(); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        subClassList = new ArrayList<SubClass>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                SubClass subClass = new SubClass();
                subClass.setSclCode(rst.getBigDecimal(1));
                subClass.setSclDesc(rst.getString(2));
                subClass.setSclShtDesc(rst.getString(3));
                subClass.setSclWebSclDescription(rst.getString(4));
                subClassList.add(subClass);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return subClassList;
    }
}
