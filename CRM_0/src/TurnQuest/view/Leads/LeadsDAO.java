package TurnQuest.view.Leads;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.client.Client2;
import TurnQuest.view.models.PostalCode;
import TurnQuest.view.setups.Sector;
import TurnQuest.view.setups.Town;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class LeadsDAO {
    public LeadsDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<LeadValues> findLeads() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String selectClause = "SELECT LDS_CODE,\n" +
            "      LDS_TITLE,\n" +
            "      LDS_SURNNAME,\n" +
            "      LDS_OTHERNAMES,\n" +
            "      LDS_CAMP_TEL,\n" +
            "      LDS_MOB_TEL,\n" +
            "      LDS_CAMP_CODE,(SELECT CMP_NAME FROM TQC_CAMPAIGNS WHERE CMP_CODE=LDS_CAMP_CODE)\n" +
            "      LDS_CAMP_NAME,\n" +
            "      LDS_FAX,\n" +
            "      LDS_DESIGN,\n" +
            "      LDS_EMAIL_ADDRS,\n" +
            "      LDS_RATE_TYPE,\n" +
            "      LDS_PHYSICAL_ADDRS,\n" +
            "      LDS_POSTAL_ADDRS,(SELECT COU_NAME FROM TQC_COUNTRIES WHERE COU_CODE=LDS_COU_CODE)\n" +
            "      LDS_COUNTRY,\n" +
            "      LDS_POSTAL_CODE,(SELECT STS_NAME FROM TQC_STATES WHERE STS_CODE=LDS_STATE_CODE)\n" +
            "      LDS_STATE_NAME,\n" +
            "      LDS_TWN_CODE,(SELECT TWN_NAME FROM TQC_TOWNS WHERE TWN_CODE=LDS_TWN_CODE)\n" +
            "      LDS_TOWN_NAME,\n" +
            "      LDS_COU_CODE,\n" +
            "      LDS_STATE_CODE,\n" +
            "      TO_DATE(LDS_DATE,'DD/MM/yyyy')LDS_DATE,  \n" +
            "      LDS_DESC,\n" +
            "      LDS_USR_CODE,(SELECT USR_NAME FROM TQC_USERS WHERE  USR_CODE=LDS_USR_CODE)\n" +
            "      LDS_USR_NAME,\n" +
            "      \n" +
            "      LDS_ORG_CODE,\n" +
            "     \n" +
            "     (SELECT ORG_NAME FROM TQC_ORGANIZATIONS  WHERE ORG_CODE =LDS_ORG_CODE)\n" +
            "      \n" +
            "      LDS_ORG_NAME,\n" +
            "      LDS_SYS_CODE ,(SELECT SYS_NAME FROM TQC_SYSTEMS WHERE  SYS_CODE=LDS_SYS_CODE)\n" +
            "      LDS_SYS_NAME,\n" +
            "      LDS_CONVERTED,\n" +
            "      LDS_PONT_NAME,\n" +
            "      LDS_PONT_CONRT,\n" +
            "      LDS_PONT_AMOUNT,\n" +
            "      \n" +
            "      LDS_CUR_CODE,(SELECT CUR_DESC ||'('||CUR_SYMBOL ||')' FROM TQC_CURRENCIES WHERE CUR_CODE=LDS_CUR_CODE)\n" +
            "      LDS_CUR_NAME,\n" +
            "      LDS_PONT_CLOSE_DATE,\n" +
            "      LDS_PONT_SALE_STAGE,\n" +
            "      LDS_INDUSTRY,\n" +
            "      LDS_ANN_REVENUE,\n" +
            "      LDS_NO_EMPYEE,\n" +
            "      LDS_WEB_SITE,\n" +
            "      LDS_TEAM_USR_CODE ,(SELECT USR_NAME FROM TQC_USERS WHERE  USR_CODE=LDS_TEAM_USR_CODE)\n" +
            "      LDS_TEAM_NAME,\n" +
            "      LDS_ACC_CODE,(SELECT  AGN_NAME FROM  TQC_AGENCIES WHERE AGN_CODE IN (\n" +
            "                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=LDS_ACC_CODE)\n" +
            "                            UNION\n" +
            "                           SELECT  CLNT_NAME  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (\n" +
            "                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=LDS_ACC_CODE)\n" +
            "                            UNION\n" +
            "                            SELECT  SPR_NAME  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (\n" +
            "                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=LDS_ACC_CODE)\n" +
            "                            )  \n" +
            "      LDS_ACCOUNT_NAME,    \n" +
            "      LDS_LSTS_CODE,(SELECT LSTS_DESC FROM TQC_LEADS_STATUSES WHERE LSTS_CODE=LDS_LSTS_CODE)\n" +
            "      LDS_LSTS_DESC,   \n" +
            "      LDS_LDSRC_CODE,(  SELECT LDSRC_DESC  FROM TQC_LEADS_SOURCES WHERE LDSRC_CODE=LDS_LDSRC_CODE)\n" +
            "      LDS_LDSRC_DESC,\n" +
            "      LDS_PROD_CODE,(SELECT TQC_CAMPAIGN_CURSOR.get_product_desc(LDS_SYS_CODE,LDS_PROD_CODE) FROM DUAL)     \n" +
            "      LDS_PROD_NAME,\n" +
            "      LDS_DIV_CODE,(SELECT DIV_NAME   FROM TQC_DIVISIONS WHERE DIV_CODE=LDS_DIV_CODE)\n" +
            "      LDS_DIV_NAME, \n" +
            "      LDS_OCCUPATION,  \n" +
            "      LDS_COMP_NAME,LDS_CLNT_CODE,LDS_CLNT_SHT_DESC  \n" +
            "    FROM TQC_LEADS";

        Object whereClause = session.getAttribute("SEARCHCRITERIA");

        if (whereClause == null) {
            whereClause = "WHERE LDS_CODE=-2000";
        }
        String query =
            selectClause.concat("  ").concat(whereClause.toString());

        List<LeadValues> orgData = new ArrayList<LeadValues>();
        try {
            System.out.println("query: "+query);
            Statement callStmt = null;
            callStmt = conn.createStatement();
            callStmt.executeQuery(query);

            ResultSet rs = callStmt.getResultSet();
            int k=0;

            while (rs.next() ) {

                LeadValues DivDef = new LeadValues();
                DivDef.setLDS_CODE(rs.getBigDecimal(1));
                DivDef.setLDS_TITLE(rs.getString(2));
                DivDef.setLDS_SURNNAME(rs.getString(3));
                DivDef.setLDS_OTHERNAMES(rs.getString(4));
                DivDef.setLDS_CAMP_TEL(rs.getString(5));
                DivDef.setLDS_MOB_TEL(rs.getString(6));
                DivDef.setLDS_CMP_CODE(rs.getBigDecimal(7));
                DivDef.setLDS_CAMP_NAME(rs.getString(8));
                DivDef.setLDS_FAX(rs.getString(9));
                DivDef.setLDS_DESIGN(rs.getString(10));
                DivDef.setLDS_EMAIL_ADDRS(rs.getString(11));
                DivDef.setLDS_RATE_TYPE(rs.getString(12));
                DivDef.setLDS_PHYSICAL_ADDRS(rs.getString(13));
                DivDef.setLDS_POSTAL_ADDRS(rs.getString(14));
                DivDef.setLDS_COUNTRY(rs.getString(15));

                DivDef.setLDS_POSTAL_CODE(rs.getString(16));
                DivDef.setLDS_STATE(rs.getString(17));
                DivDef.setLDS_TWN_CODE(rs.getBigDecimal(18));
                DivDef.setLDS_TOWN_NAME(rs.getString(19));

                DivDef.setLDS_COU_CODE(rs.getBigDecimal(20));
                DivDef.setLDS_STATE_CODE(rs.getBigDecimal(21));

                DivDef.setLDS_DATE(rs.getDate(22));
                DivDef.setLDS_DESC(rs.getString(23));
                DivDef.setLDS_USR_CODE(rs.getBigDecimal(24));
                DivDef.setLDS_USR_NAME(rs.getString(25));

                DivDef.setLDS_ORG_CODE(rs.getBigDecimal(26));
                DivDef.setLDS_ORG_NAME(rs.getString(27));
                DivDef.setLDS_SYS_CODE(rs.getBigDecimal(28));
                DivDef.setLeadSysName(rs.getString(29));
                DivDef.setLDS_CONVERTED(rs.getString(30));
                DivDef.setLDS_PONT_NAME(rs.getString(31));
                DivDef.setLDS_PONT_CONRT(rs.getString(32));
                DivDef.setLDS_PONT_AMOUNT(rs.getBigDecimal(33));
                DivDef.setLDS_CUR_CODE(rs.getBigDecimal(34));
                DivDef.setLeadCurName(rs.getString(35));
                DivDef.setLDS_PONT_CLOSE_DATE(rs.getDate(36));
                DivDef.setLDS_PONT_SALE_STAGE(rs.getString(37));
                DivDef.setLDS_INDUSTRY(rs.getString(38));
                DivDef.setLDS_ANN_REVENUE(rs.getBigDecimal(39));
                DivDef.setLDS_NO_EMPYEE(rs.getBigDecimal(40));
                DivDef.setLDS_WEB_SITE(rs.getString(41));
                DivDef.setLDS_TEAM_USR_CODE(rs.getBigDecimal(42));
                DivDef.setLDS_TEAM_NAME(rs.getString(43));
                DivDef.setLDS_ACC_CODE(rs.getBigDecimal(44));
                DivDef.setLDS_ACCOUNT_NAME(rs.getString(45));
                DivDef.setLDS_LSTS_CODE(rs.getBigDecimal(46));
                DivDef.setLDS_LSTS_DESC(rs.getString(47));
                DivDef.setLDSRC_CODE(rs.getBigDecimal(48));
                DivDef.setLDSRC_DESC(rs.getString(49));
                DivDef.setLeadProdCode(rs.getBigDecimal(50));
                DivDef.setLeadProdName(rs.getString(51));
                DivDef.setLeadDivCode(rs.getBigDecimal(52));
                DivDef.setLeadDivName(rs.getString(53));
                DivDef.setOccupation(rs.getString(54));
                DivDef.setCompanyName(rs.getString(55));
                DivDef.setClientCode(rs.getBigDecimal(56));
                DivDef.setClientShtDesc(rs.getString(57));
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

    public List<LeadSource> findLeadSources() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.get_lead_sources(?); end;";

        List<LeadSource> leadSourceList = new ArrayList<LeadSource>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                LeadSource leadSource = new LeadSource();
                leadSource.setLeadSourceCode(rs.getBigDecimal(1));
                leadSource.setLeadSourceDesc(rs.getString(2));
                leadSourceList.add(leadSource);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return leadSourceList;
    }

    public List<LeadStatus> findLeadStatuses() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.get_lead_statuses(?); end;";

        List<LeadStatus> leadStatusList = new ArrayList<LeadStatus>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                LeadStatus leadStatus = new LeadStatus();
                leadStatus.setLeadStatusCode(rs.getBigDecimal(1));
                leadStatus.setLeadStatusDesc(rs.getString(2));
                leadStatusList.add(leadStatus);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return leadStatusList;
    }

    public List<LeadComment> findLeadComments() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.get_lead_comments(?,?); end;";

        List<LeadComment> leadCommentList = new ArrayList<LeadComment>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("Lead_code"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                LeadComment leadComment = new LeadComment();

                leadComment.setLeadCommentCode(rs.getBigDecimal(1));
                leadComment.setLeadComment(rs.getString(2));
                leadComment.setLeadCommentDate(rs.getDate(3));
                leadComment.setUser_name(rs.getString(4));
                leadComment.setLeadCommentDisposition(rs.getString(5));
                leadCommentList.add(leadComment);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return leadCommentList;
    }
    public List<Client2> findClientTypes() {
        List<Client2> client2List = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := TQC_CLIENTS_PKG.getClientTypes(); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        client2List = new ArrayList<Client2>();

        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, oracle.jdbc.internal.OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                Client2 client2 = new Client2();
                client2.setCLNTY_CODE(rst.getBigDecimal(1));
                client2.setCLNTY_CLNT_TYPE(rst.getString(2));
                client2.setCLNTY_CATEGORY(rst.getString(3));
                client2List.add(client2);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return client2List;
    }
    public List<PostalCode> fetchPostalCodesByTown() {
        List<PostalCode> postalCodesList = new ArrayList<PostalCode>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getPostalCodes;end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

           
               

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                
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
           
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            e.printStackTrace();
        }
        return postalCodesList;
    }
    public List<Sector> fetchAllSectors() {

        List<Sector> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_sectors(?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sectorsList = new ArrayList<Sector>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);

            statement.registerOutParameter(1, oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a Sector object with the values
                // and add it to the list
                Sector sector = new Sector();
                sector.setCode(resultSet.getString(1));
                sector.setShortDesc(resultSet.getString(2));
                sector.setName(resultSet.getString(3));
                sectorsList.add(sector);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return sectorsList;
    }
    public List<Town> fetchTownsByState() {
        List<Town> townsList = new ArrayList<Town>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.getTownsByState(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("stateCode") != null) {
                BigDecimal stateCode =
                    new BigDecimal(session.getAttribute("stateCode").toString());

                statement.registerOutParameter(1,
                                               oracle.jdbc.internal.OracleTypes.CURSOR);
                statement.setBigDecimal(2, stateCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);
                System.out.println("state code " + stateCode);
                while (resultSet.next()) {
                    System.out.println(" in loop");
                    Town town = new Town();
                    town.setCode(resultSet.getString(1));
                    town.setCountryCode(resultSet.getString(2));
                    town.setShortDesc(resultSet.getString(3));
                    town.setName(resultSet.getString(4));
                    town.setSTSCode(resultSet.getString(5));
                    town.setPostalDesc(resultSet.getString(6));
                    town.setPostalZipCode(resultSet.getString(7));
                    townsList.add(town);
                }
                statement.close();
                resultSet.close();
                connection.close();
            } else {
                if (session.getAttribute("countryCode") == null) {
                    return townsList;
                }
                DBConnector dbCon = new DBConnector();
                OracleConnection conn = null;
                conn = dbCon.getDatabaseConnection();

                query = "begin TQC_SETUPS_CURSOR.gettowns(?,?); end;";

                try {

                    OracleCallableStatement callStmt = null;
                    callStmt =
                            (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                    callStmt.registerOutParameter(1, OracleTypes.CURSOR);

                    callStmt.setBigDecimal(2,
                                           new BigDecimal(session.getAttribute("countryCode").toString()));
                    callStmt.execute();

                    OracleResultSet rs =
                        (OracleResultSet)callStmt.getObject(1);

                    while (rs.next()) {

                        Town town = new Town();
                        town.setCode(rs.getString(1));
                        town.setCountryCode(rs.getString(2));
                        town.setShortDesc(rs.getString(3));
                        town.setName(rs.getString(4));
                        town.setSTSCode(rs.getString(5));
                        townsList.add(town);

                    }
                    rs.close();
                    callStmt.close();
                    conn.commit();
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return townsList;
    }
    public List<LeadActivity> findLeadActivities() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_SETUPS_CURSOR.get_lead_activities(?,?); end;";

        List<LeadActivity> leadActivityList = new ArrayList<LeadActivity>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("Lead_code"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                LeadActivity leadActivity = new LeadActivity();

                leadActivity.setLeadActivityCode(rs.getBigDecimal(1));
                leadActivity.setActivityCode(rs.getBigDecimal(2));
                leadActivity.setActivitySubject(rs.getString(3));
                leadActivity.setActivityLocation(rs.getString(4));
                leadActivityList.add(leadActivity);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return leadActivityList;
    }
}
