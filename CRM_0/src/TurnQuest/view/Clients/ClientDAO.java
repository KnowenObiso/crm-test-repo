package TurnQuest.view.Clients;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Divisions.Division;
import TurnQuest.view.models.PostalCode;

import java.math.BigDecimal;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class ClientDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<client> findUserByAgencyCode() {

        String query = "begin ? := tqc_clients_pkg.get_clients(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<client> clientList = new ArrayList<client>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            while (rs.next()) {
                client client1 = new client();
                client1.setClientCode(rs.getBigDecimal(1));
                client1.setPINNumber(rs.getString(2));
                client1.setPostalAddress(rs.getString(3));
                client1.setTelphoneOne(rs.getString(4));
                client1.setOthernames(rs.getString(5));
                client1.setFullname(rs.getString(6));
                client1.setIdRegNumber(rs.getString(7));
                client1.setShortDescription(rs.getString(8));
                client1.setZIPCode(rs.getString(9));

                clientList.add(client1);
            }
            rs.close();
            conn.close();
            cst.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return clientList;
    }

    /**
     * Fetches all <code>Division</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Division</code> objects/records
     * fetched from the database.
     */
    public List<Division> fetchDivisionByBranch() {

        List<Division> divisionsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin tqc_clients_pkg.get_divisions(?,?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            divisionsList = new ArrayList<Division>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("branchCode") != null) {
                statement.setString(1,
                                    ""); //(BigDecimal)session.getAttribute("branchCode"));
            } else {
                statement.setString(1, "");
            }

            statement.registerOutParameter(2,
                                           oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a Division object with the
                // values and add it to the list
                Division division = new Division();

                division.setDIV_NAME(resultSet.getString(1));
                division.setDIV_CODE(new BigDecimal(resultSet.getString(2)));

                divisionsList.add(division);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return divisionsList;
    }

    public List<ClientTable> findClientTableAttributes() {

        String query =
            "begin ? := TQC_PRODUCT_PKG.getClientAttColumns(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<ClientTable> clientTableList = new ArrayList<ClientTable>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            while (rs.next()) {
                ClientTable clientTable = new ClientTable();
                clientTable.setColName(rs.getString(1));
                clientTable.setColDescription(rs.getString(2));
                clientTable.setTableName(rs.getString(3));
                clientTableList.add(clientTable);
            }
            rs.close();
            conn.close();
            cst.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return clientTableList;
    }
    
    public List<PostalCode> fetchPostalCodesByTown() {
        List<PostalCode> postalCodesList = new ArrayList<PostalCode>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getPostalCodesByTown(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("townCode") != null) {
                BigDecimal twnCode =
                    new BigDecimal(session.getAttribute("townCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, twnCode);
                statement.execute();
                ResultSet resultSet = (ResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    PostalCode postalCode = new PostalCode();
                    postalCode.setPstCode(resultSet.getBigDecimal(1));
                    postalCode.setPstTownCode(resultSet.getBigDecimal(2));
                    postalCode.setPstDesc(resultSet.getString(3));
                    postalCode.setPstZipCode(resultSet.getString(4));

                    postalCodesList.add(postalCode);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            e.printStackTrace();
        }
        return postalCodesList;
    }
    
}
