package TurnQuest.view.Clients1;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class AppClientsDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<AppClients> findSimilarClients() {

        String query =
            "begin ? := tqc_clients_pkg.clients_QRY(?,?,?,?,?,?,?); end;";
        OracleCallableStatement cst1 = null;
        OracleConnection conn = null;
        List<AppClients> users = new ArrayList<AppClients>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst1 = (OracleCallableStatement)conn.prepareCall(query);
            cst1.registerOutParameter(1,
                                      OracleTypes.CURSOR); //authorization code

            cst1.setString(2, (String)session.getAttribute("FirstName"));
            cst1.setString(3, (String)session.getAttribute("MiddleName"));
            cst1.setString(4, (String)session.getAttribute("LastName"));
            cst1.setString(5, (String)session.getAttribute("PostalAddress"));
            cst1.setString(6, null);
            cst1.setString(7, (String)session.getAttribute("PINNumber"));
            cst1.setString(8, (String)session.getAttribute("SearchCriteria"));

            cst1.execute();

            OracleResultSet rs = (OracleResultSet)cst1.getObject(1);

            users = new ArrayList<AppClients>();
            int k;
            k = 0;
            while (rs.next()) {
                AppClients client = new AppClients();
                client.setClientCode(rs.getBigDecimal(1));
                client.setPINNumber(rs.getString(2));
                client.setPostalAddress(rs.getString(3));
                client.setTelphoneOne(rs.getString(4));
                client.setOthernames(rs.getString(5));
                client.setFullname(rs.getString(6));
                client.setIdRegNumber(rs.getString(7));
                client.setShortDescription(rs.getString(8));
                client.setZIPCode(rs.getString(9));

                users.add(client);
            }

            cst1.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return users;


    }

    public List<AppClients> findUnallocatedClientSystems() {

        String query =
            "begin ? := tqc_clients_pkg.get_clnt_unallcted_sys(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppClients> users = new ArrayList<AppClients>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("ClientCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<AppClients>();
            int k;
            k = 0;
            while (rs.next()) {
                AppClients client = new AppClients();

                client.setUnallocatedSystemCode(rs.getBigDecimal(1));
                client.setUnallocatedSystemShtDesc(rs.getString(2));
                client.setUnallocatedSystemName(rs.getString(3));

                users.add(client);
            }

            cst.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return users;


    }

    public List<AppClients> findallocatedClientSystems() {

        String query =
            "begin ? := tqc_clients_pkg.get_clnt_allcted_sys(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppClients> users = new ArrayList<AppClients>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("ClientCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<AppClients>();
            int k;
            k = 0;
            while (rs.next()) {
                AppClients client = new AppClients();

                client.setAllocatefSystemCode(rs.getBigDecimal(1));
                client.setAllocatedSystemShtDesc(rs.getString(2));
                client.setAllocatedSystemName(rs.getString(3));

                users.add(client);
            }
            cst.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return users;


    }

    public List<AppClients> findclientwebaccounts() {

        String query =
            "begin ? := tqc_clients_pkg.get_client_web_accounts(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppClients> users = new ArrayList<AppClients>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("ClientCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<AppClients>();
            int k;
            k = 0;
            while (rs.next()) {
                AppClients client = new AppClients();

                client.setClientAccCode(rs.getBigDecimal(1));
                client.setClientAccUserName(rs.getString(2));
                client.setClientAccName(rs.getString(10));
                client.setClientAccPersonelRank(rs.getString(5));
                client.setClientAccCreatedBy(rs.getString(9));
                client.setClientAccDateCreated(rs.getDate(6));
                client.setClientAccLoginAllowed(rs.getString(4));
                client.setClientAccStatus(rs.getString(7));

                users.add(client);
            }
            cst.close();
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return users;


    }

}
