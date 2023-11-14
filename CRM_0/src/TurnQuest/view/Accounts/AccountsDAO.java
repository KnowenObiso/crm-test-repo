package TurnQuest.view.Accounts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class AccountsDAO {

    public AccountsDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public List<AccountType> fingAccountTypes() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.search_account_types(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String accountid = "";
        try {
            if (session.getAttribute("accounttypeid") != null)
                accountid = (String)session.getAttribute("accounttypeid");
            else
                accountid = "0";
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (accountid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(accountid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);


            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(5));
                acctype.setSmapping(rst.getString(3));
                acctype.setCmapping(rst.getString(2));
                acctype.setWrate(rst.getBigDecimal(4));
                acctype.setOrate(rst.getBigDecimal(10));
                acctype.setCrate(rst.getBigDecimal(6));
                acctype.setAcformat(rst.getString(11)); // account short  description
                acctype.setVatrate(rst.getBigDecimal(12));
                acctype.setACT_CODE(rst.getBigDecimal(1));
                acctype.setAccountFormat(rst.getString(13));
                acctype.setOdl_code(rst.getString(14));
                acctype.setRank(rst.getString(15));
                acctype.setMgrNoInFix(rst.getString(16));
                acctype.setACT_COMMISION_LEVY_RATE(rst.getBigDecimal(17));
                //added column
                accounttypes.add(acctype);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return accounttypes;
    }

    public List<AccountType> findSubclassClauses() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.search_account_types(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        try {

            accounttypes = new ArrayList<AccountType>();
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, null);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setSmapping(rst.getString(3));
                acctype.setCmapping(rst.getString(2));
                acctype.setWrate(rst.getBigDecimal(4));
                acctype.setOrate(rst.getBigDecimal(10));
                acctype.setCrate(rst.getBigDecimal(6));
                acctype.setAcformat(rst.getString(11));
                acctype.setVatrate(rst.getBigDecimal(12));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAgents() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.get_agns_by_acttype(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String accountid = "";
        try {
            if (session.getAttribute("accounttypeid") != null)
                accountid = (String)session.getAttribute("accounttypeid");
            else
                accountid = "0";
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (accountid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(accountid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setSmapping(rst.getString(4));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findAgentsRegistration() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.get_agency_registration(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        try {
            
            BigDecimal accountid = GlobalCC.checkBDNullValues(session.getAttribute("agencyCode"));
            
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, accountid); 
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setYear(rst.getString(3));
                acctype.setRegno(rst.getString(4));
                acctype.setWef(rst.getDate(5));
                acctype.setWet(rst.getDate(6));
                acctype.setAccepted(rst.getString(7));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findAgentsDirectors() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "SELECT adir_code, adir_agn_code, adir_year, adir_name, adir_qualifications, adir_pct_holdg, adir_designation, adir_phone_number,\n" + 
            "      adir_principle , adir_cou_code, cou_name\n" + 
            "    FROM tqc_agency_directors,tqc_countries\n" + 
            "      WHERE adir_agn_code = :v_agn_code AND adir_cou_code=cou_code(+)";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        BigDecimal  agnCode =null;
        try {
            agnCode =GlobalCC.checkBDNullValues(session.getAttribute("agencyCode"));
            accounttypes = new ArrayList<AccountType>();
            if(agnCode!=null) {
                conn = (OracleConnection)OracleConnection.getDatabaseConnection();
                query=query.replaceAll(":v_agn_code",agnCode.toString());
                stmt = (OracleCallableStatement)conn.prepareCall(query); 
                OracleResultSet rst = (OracleResultSet)stmt.executeQuery();;
                while (rst.next()) {
                    AccountType acctype = new AccountType();
                    acctype.setId(rst.getString(1));
                    acctype.setYear(rst.getString(3));
                    acctype.setName(rst.getString(4));
                    acctype.setQualification(rst.getString(5));
                    acctype.setShareholding(rst.getBigDecimal(6));
                    acctype.setDesignation(rst.getString(7));
                    acctype.setPhoneNumber(rst.getString(8));
                    acctype.setPrincipleDirecor(rst.getString(9));
                    acctype.setNationality(rst.getString(10));
                    acctype.setCountry(rst.getString(11));
                    accounttypes.add(acctype);
                }
                rst.close();
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findAgentsReferees() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.get_tqc_agency_referees(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
       
        try {
            BigDecimal accountid =  GlobalCC.checkBDNullValues(session.getAttribute("agencyCode"));
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, accountid);
                
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(2));
                acctype.setPhyaddress(rst.getString(3));
                acctype.setPostaddress(rst.getString(4));
                acctype.setIdno(rst.getString(9));
                acctype.setEmail(rst.getString(7));
                acctype.setTelno(rst.getString(8));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findAccountBranches() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "SELECT brn_name , TO_CHAR (brn_code)\n" + 
        "       FROM tqc_branches, tqc_regions\n" + 
        "      WHERE brn_reg_code = reg_code\n" + 
        "        AND brn_reg_code = nvl(:v_reg_code,brn_reg_code) \n" ;
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        //String accountid="";
        try { 
          
            BigDecimal regCode = GlobalCC.checkBDNullValues(session.getAttribute("regionCode")); 
            query=query.replaceAll(":v_reg_code", regCode!=null?regCode.toString(): "null");
            System.out.println("query: "+query);
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query); 
            OracleResultSet rst = (OracleResultSet)stmt.executeQuery();
            
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(2));
                acctype.setName(rst.getString(1));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }
    
    public List<AccountType> findAccountRegions() {
        List<AccountType> regions = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.find_regions(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        //String accountid="";
        try {
            regions = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setRegionCode(rst.getString(2));
                acctype.setRegionName(rst.getString(1));
                regions.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return regions;
    }

    public List<AccountType> findAccountSBUs() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.fetch_sbus(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        //String accountid="";
        try {
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, "U");
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                //select a.OSD_NAME COVERAGE_AREA,b.OSD_NAME SPOKE,c.OSD_NAME SBU,a.OSD_ID COVERAGE_CODE,b.OSD_ID SPOKE_CODE,b.OSD_ID SBU_CODE
                AccountType acctype = new AccountType();
                acctype.setSBU(rst.getString("SBU"));
                acctype.setSBU_CODE(rst.getBigDecimal("SBU_CODE"));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findServiceproviders() {
        List<AccountType> accounttypes = null;
        BigDecimal agentcode =
            new BigDecimal(session.getAttribute("agencyCode").toString());
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.find_serv_providers(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        //String accountid="";
        try {
            accounttypes = new ArrayList<AccountType>();
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, agentcode);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setSpr_code(rst.getBigDecimal(1));
                acctype.setSpr_sht_desc(rst.getString(2));
                acctype.setSpr_name(rst.getString(3));
                accounttypes.add(acctype);

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAccountCountries() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_COMMON_CURSORS.getcountries(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        try {
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(3));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findAccountTowns() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_COMMON_CURSORS.getTowns(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        if (session.getAttribute("countryId") == null) {
            GlobalCC.INFORMATIONREPORTING("Invalid country. Select Country to continue");
            return accounttypes;
        }
        try {

            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            BigDecimal countrycode =
                new BigDecimal((String)session.getAttribute("countryId"));
            stmt.setBigDecimal(1, countrycode);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(4));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    /**
     * Fetches a List of all <code>Agency</code> classes from the database.
     *
     * @return A List of all <code>Agency</code> objects/records.
     */
    public List<Agency> fetchAllAgencies() {

        List<Agency> agenciesList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_agencies(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection OracleConnection = null;

        try {
            agenciesList = new ArrayList<Agency>();
            OracleConnection =
                    (OracleConnection)dbConnector.getDatabaseConnection();
            statement =
                    (OracleCallableStatement)OracleConnection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a AgencyHoldingCompany object with
                // the values and add it to the list
                Agency agency = new Agency();
                agency.setCode(resultSet.getString(1));
                agency.setName(resultSet.getString(2));
                agenciesList.add(agency);
            }

            resultSet.close();
            statement.close();
            OracleConnection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return agenciesList;
    }

    public List<AccountType> findAgencyClasses() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_AGENCIES_CURSORS.get_agency_classes(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(2));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findSector() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_COMMON_CURSORS.get_sectors(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(3));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAgencyholdingCo() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_AGENCIES_CURSORS.get_agency_holdco(?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setName(rst.getString(2));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


    public List<AccountType> findwebUserAccounts() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.get_account_contacts_sys(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        String accountid = "";
        try {

            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (session.getAttribute("agencyCode") != null)
                accountid = (String)session.getAttribute("agencyCode");
            else
                accountid = "0";
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (accountid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(accountid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setUsername(rst.getString(4));
                acctype.setName(rst.getString(3));
                acctype.setEmail(rst.getString(13));
                acctype.setPersonalrank(rst.getString(6));
                acctype.setCreatedby(rst.getString(7));
                acctype.setDatecreated(rst.getDate(8));
                acctype.setAllowlogin(rst.getString(9));
                acctype.setStatus(rst.getString(10));
                acctype.setReset(rst.getString(11));
                acctype.setTelNo(rst.getString(12));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findEmbassywebUserAccounts() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_CURSORS.get_embassy_contacts(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();

        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, session.getAttribute("countryCode"));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setUsername(rst.getString(4));
                acctype.setName(rst.getString(3));
                acctype.setEmail(rst.getString(13));
                acctype.setPersonalrank(rst.getString(6));
                acctype.setCreatedby(rst.getString(7));
                acctype.setDatecreated(rst.getDate(8));
                acctype.setAllowlogin(rst.getString(9));
                acctype.setStatus(rst.getString(10));
                acctype.setReset(rst.getString(11));

                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAccountTypesAndAccounts() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.search_account_types(?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        String accountid = "";
        List<AccountValues> accounttypes2 = null;
        try {
            if (session.getAttribute("accounttypeid") != null)
                accountid = (String)session.getAttribute("accounttypeid");
            else
                accountid = "0";
            accounttypes = new ArrayList<AccountType>();
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (accountid.equals("0"))
                stmt.setBigDecimal(1, null);
            else
                stmt.setBigDecimal(1, new BigDecimal(accountid));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(2);
            while (rst.next()) {
                accounttypes2 = new ArrayList<AccountValues>();
                AccountType acctype = new AccountType();
                acctype.setId(rst.getString(1));
                acctype.setSmapping(rst.getString(3));
                acctype.setCmapping(rst.getString(2));
                acctype.setWrate(rst.getBigDecimal(4));
                acctype.setOrate(rst.getBigDecimal(10));
                acctype.setCrate(rst.getBigDecimal(6));
                acctype.setAcformat(rst.getString(11));
                acctype.setVatrate(rst.getBigDecimal(12));
                acctype.setACT_CODE(rst.getBigDecimal(1));
                acctype.setType("P");

                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_CURSORS.get_agns_by_acttype(?,?);end;");
                stmt2.setBigDecimal(1, rst.getBigDecimal(1));
                stmt2.registerOutParameter(2, OracleTypes.CURSOR);
                stmt2.execute();
                OracleResultSet rs = (OracleResultSet)stmt2.getObject(2);
                //accounttypes2.clear();
                while (rs.next()) {
                    AccountValues acctype2 = new AccountValues();
                    acctype2.setId(rs.getString(1));
                    acctype2.setName(rs.getString(4));
                    acctype2.setAccCode(rs.getBigDecimal(1));
                    acctype2.setType("S");
                    accounttypes2.add(acctype2);
                }
                acctype.setProduces(accounttypes2);
                accounttypes.add(acctype);
                rs.close();
                stmt2.close();
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findManagerBranch() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin ? := TQC_AGENCIES_CURSORS.fetchBranchAgencies; end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();

        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setBranchAgnCode(rst.getBigDecimal(1));
                acctype.setBrnCode(rst.getBigDecimal(2));
                acctype.setBranchName(rst.getString(3));
                acctype.setAgentCode(rst.getString(4));
                acctype.setBraShtDesc(rst.getString(5));
                acctype.setAgnShtDesc(rst.getString(6));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAllBranch() {
        List<AccountType> accounttypes = null;
        BigDecimal organizationCode;
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin ? := TQC_AGENCIES_CURSORS.fetchAllBranches(?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        organizationCode =
                new BigDecimal(session.getAttribute("orgCode").toString());
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, organizationCode);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setBrnCode(rst.getBigDecimal(1));
                acctype.setBranchName(rst.getString(2));
                acctype.setBrnShtDesc(rst.getString(3));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findBranchUnits() {
        Rendering renderer=new Rendering();
        List<AccountType> accounttypes = null;
        BigDecimal organizationCode;
        DBConnector OracleConnection = new DBConnector();
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        OracleResultSet rst=null;
        try {
            
            conn = OracleConnection.getDatabaseConnection();
            BigDecimal regionCode = GlobalCC.checkBDNullValues(session.getAttribute("regionCode"));
            BigDecimal branchCode = GlobalCC.checkBDNullValues(session.getAttribute("branchCode"));
            
            System.out.println("Accounts branchCode: "+branchCode);
            System.out.println("kkbranchCode: " +session.getAttribute("branchCode"));
            
            System.out.println("renderer.isREGION_UNIT_DEPEDENCY()"+renderer.isREGION_UNIT_DEPEDENCY()+"   "+(regionCode!=null));
            if(renderer.isREGION_UNIT_DEPEDENCY() && regionCode!=null)
            {
                String query = "begin ? := TQC_SETUPS_CURSOR.getBranchUnitsPerRegion(?); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.setBigDecimal(2, regionCode);  
                stmt.execute();
                 rst = (OracleResultSet)stmt.getObject(1);
                }
            else
            {
                 //KAVAGI: this query pulls all the branch units when branch is not selected and filters by branch only if the branch is selected.
                  String query = "SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name\n" + 
                  "           FROM tqc_branch_units\n" + 
                  "          WHERE bru_status = 'A'" +
                  " AND bru_brn_code=NVL(:v_brn_code,bru_brn_code)";
                 
                   query=query.replaceAll(":v_brn_code",branchCode!=null?branchCode.toString():"null");
                   System.out.println("query: "+query);
                    
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    rst = (OracleResultSet)stmt.executeQuery();
            }
            
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setBruCode(rst.getBigDecimal(1));
                acctype.setBruBrnCode(rst.getBigDecimal(2));
                acctype.setBruShtDesc(rst.getString(3));
                acctype.setBruName(rst.getString(4));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAgentTypes() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.get_Agent_Type(?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, session.getAttribute("accType"));
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setType(rst.getString(1));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

    public List<AccountType> findAgentGroups() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.get_Agent_Groups; end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                acctype.setAccountGroups(rst.getString(1));
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }
    public List<AccountType> fetchAccountTypes() {
        List<AccountType> accounttypes = new ArrayList<AccountType>();
        DBConnector db = new DBConnector();
             OracleCallableStatement stmt = null;
             OracleConnection conn = null;
        try {
            if(!GlobalCC.tableExists("tqc_account_types")) {
                return accounttypes;
            }
            String query =
            "SELECT act_code, act_account_type, act_type_sht_desc FROM tqc_account_types";
            conn = db.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rst = (OracleResultSet)stmt.executeQuery();

            while (rst.next()) {
                AccountType a = new AccountType();
                a.setACT_CODE(rst.getBigDecimal(1));
                a.setACT_ACCOUNT_TYPE(rst.getString(2));
                a.setACT_TYPE_SHT_DESC(rst.getString(3));
                //added column
                accounttypes.add(a);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return accounttypes;
    }
public List<Sacco> fetchSaccos() {
        List<Sacco> saccos = new ArrayList<Sacco>();
        DBConnector db = new DBConnector();
             OracleCallableStatement stmt = null;
             OracleConnection conn = null;
        try {
          
            String query = "SELECT * FROM tqc_saccos";
            conn = db.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rst = (OracleResultSet)stmt.executeQuery(); 
            while (rst.next()) {
                Sacco a = new Sacco();
                a.setSaccoCode(rst.getBigDecimal("SACC_CODE"));
                a.setSaccoName(rst.getString("SACC_NAME"));
                a.setSaccoShortDesc(rst.getString("SACC_SHT_DESC"));
                a.setSaccoPhysicalAddress(rst.getString("SACC_PHYSICAL_ADDRESS"));
                a.setSaccoPostalAddress(rst.getString("SACC_POSTAL_ADDRESS"));
                a.setSaccoTownCode(rst.getBigDecimal("SACC_TWN_CODE"));
                a.setSaccoCountryCode(rst.getBigDecimal("SACC_COU_CODE")); 
                a.setSaccoEmail(rst.getString("SACC_EMAIL_ADDRESS"));
                a.setSaccoWebAddress(rst.getString("SACC_WEB_ADDRESS"));
                a.setSaccoPostalAddress(rst.getString("SACC_ZIP"));
                a.setSaccoContactPerson(rst.getString("SACC_CONTACT_PERSON"));
                a.setSaccoContactTitle(rst.getString("SACC_CONTACT_TITLE"));
                a.setSaccoTel1(rst.getString("SACC_TEL1"));
                a.setSaccoTel2(rst.getString("SACC_TEL2"));
                a.setSaccoFax(rst.getString("SACC_FAX"));
                a.setSaccoPIN(rst.getString("SACC_PIN"));
                a.setSaccoAccountNo(rst.getString("SACC_ACC_NO"));
                a.setSaccoStatus(rst.getString("SACC_STATUS"));
                a.setSaccoDateCreated(rst.getString("SACC_DATE_CREATED"));
                a.setSaccoCreatedBy(rst.getString("SACC_CREATED_BY"));
                a.setSaccoBranchCode(rst.getBigDecimal("SACC_BRN_CODE"));
                a.setSaccoTownName(rst.getString("SACC_TOWN"));
                a.setSaccoCountryName(rst.getString("SACC_COUNTRY"));
                a.setSaccoSms(rst.getString("SACC_SMS_TEL"));
                a.setSaccoLicensed(rst.getString("SACC_LICENSED"));
                a.setSaccoBankAccountNo(rst.getString("SACC_BANK_ACC_NO"));
                a.setSaccoUniquePrefix(rst.getString("SACC_UNIQUE_PREFIX"));
                a.setSaccoStateCode(rst.getBigDecimal("SACC_STATE_CODE"));
                a.setSaccoRegulatorNo(rst.getString("SACC_REGULATOR_NUMBER"));
                //added column
                saccos.add(a);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return saccos;
    }


    public List<AccountType> fetchEntities() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := tqc_entities_cursor.get_entities_fnc(?,?,?,?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, GlobalCC.checkNullValues(session.getAttribute("entityPin")));
            stmt.setObject(3, GlobalCC.checkNullValues(session.getAttribute("entityName")));
            stmt.setObject(4, GlobalCC.checkNullValues(session.getAttribute("entityShortDesc")));
            stmt.setObject(5, GlobalCC.checkNullValues(session.getAttribute("entityCode")));
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                

                   acctype.setEntShtDesc(rst.getString(1));
                   acctype.setEntName(rst.getString(2));
                   acctype.setEntPin(rst.getString(3)); 
                   acctype.setEntCode(rst.getBigDecimal(4));
                   acctype.setEntPosatalAddress(rst.getString(5));
                   acctype.setEntEmailAddress(rst.getString(6));
                   acctype.setEntSmsTel(rst.getString(7)); 
                  acctype.setNewRelation("C");
             
                accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }


   /* public List<AccountType> fetchEntitiesChild() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := tqc_entities_cursor.get_entities_chld_fnc(?,?,?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, GlobalCC.checkNullValues(session.getAttribute("entityPin")));
            stmt.setObject(3, GlobalCC.checkNullValues(session.getAttribute("entityName")));
            stmt.setObject(4, GlobalCC.checkNullValues(session.getAttribute("entityShortDesc")));
           
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                

               acctype.setAgnShtDesc(rst.getString(1));
               acctype.setAgnName(rst.getString(2));
               acctype.setAgnPin(rst.getString(3)); 
               acctype.setEntCode(rst.getBigDecimal(4));
               accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }*/
    
    public List<AccountType> fetchEntitiesRelations() {
        List<AccountType> accounttypes = null;
        DBConnector OracleConnection = new DBConnector();
        String query = "begin ? := tqc_entities_cursor.get_entities_rels_fnc(?,?,?,?); end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        accounttypes = new ArrayList<AccountType>();
        try {

            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, GlobalCC.checkNullValues(session.getAttribute("entityPin")));
            stmt.setObject(3, GlobalCC.checkNullValues(session.getAttribute("entityName")));
            stmt.setObject(4, GlobalCC.checkNullValues(session.getAttribute("entityShortDesc")));
            stmt.setObject(5, GlobalCC.checkNullValues(session.getAttribute("entityCode")));
           
            stmt.execute();
            OracleResultSet rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                AccountType acctype = new AccountType();
                

               acctype.setRelation(rst.getString(1));
               acctype.setAgnShtDesc(rst.getString(2));
               acctype.setAgnName(rst.getString(3)); 
               acctype.setAgnPin(rst.getString(4)); 
                acctype.setAgnEmail(rst.getString(5));
                acctype.setAgnTel(rst.getString(6)); 
                acctype.setAgnBnkAccNo(rst.getString(7)); 
                acctype.setEntCode(rst.getBigDecimal(8));
             
               accounttypes.add(acctype);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return accounttypes;
    }

}
