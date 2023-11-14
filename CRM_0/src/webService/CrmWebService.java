package webService;


import TurnQuest.view.Connect.DBConnector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


@WebService(targetNamespace = "http://tempuri.org/")
public class CrmWebService {

    public CrmWebService() {
        super();
    }

    @WebMethod
    public String CheckClient(String clientShtDesc) {
        String exists = "N";
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin ? := TQC_CLIENTS_PKG.checkClientExists(?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.VARCHAR);
            statement.setObject(2, clientShtDesc);
            statement.execute();
            exists = statement.getString(1);
            statement.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    public String StringCreateClients(String firstName, String middleName,
                                      String surName, String clientIdRegNo) {
        String exists = "N";
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin ? := TQC_CLIENTS_PKG.createwebserviceclient(?,?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.VARCHAR);
            statement.setObject(2, firstName);
            statement.setObject(3, middleName);
            statement.setObject(4, surName);
            statement.setObject(5, clientIdRegNo);
            statement.execute();
            exists = statement.getString(1);
            statement.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }
}
