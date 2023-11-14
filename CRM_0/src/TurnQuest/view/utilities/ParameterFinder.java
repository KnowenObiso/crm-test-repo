package TurnQuest.view.utilities;


import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.Connection;

import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class ParameterFinder {
    public String getParameterVal(String param) {
        Connection conn = new DBConnector().getDatabaseConnection();
        CallableStatement cstOrgDetails = null;
        String ret = "X";
        try {

            cstOrgDetails =
                    conn.prepareCall("begin ? :=tqc_parameters_pkg.get_param_varchar(?); end;");
            cstOrgDetails.setString(2, param);
            cstOrgDetails.registerOutParameter(1, OracleTypes.VARCHAR);
            cstOrgDetails.execute();

            ret = cstOrgDetails.getString(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            ret = "X";
        } finally {
            DbUtils.closeQuietly(conn, cstOrgDetails, null);
        }
        return ret;
    }
}
