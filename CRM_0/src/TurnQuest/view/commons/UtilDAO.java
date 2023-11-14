package TurnQuest.view.commons;


import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;


public class UtilDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public UtilDAO() {
        super();
    }

    public static String fetchCountryName(String countryCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_specific_country(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String countryName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setString(1, countryCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                countryName = resultSet.getString(3);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return countryName;
    }

    public static String fetchTownName(String townCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_town(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String townName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setString(1, townCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                townName = resultSet.getString(4);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return townName;
    }

    public static String fetchBranchName(String branchCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_branch(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String branchName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setString(1, branchCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                branchName = resultSet.getString(4);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return branchName;
    }

    public static String fetchSectorName(String sectorCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_specific_sectors(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String sectorName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setString(1, sectorCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                sectorName = resultSet.getString(3);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return sectorName;
    }

    public static String fetchAgencyClassName(String agencyClassCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_specific_agency_classes(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String agencyClassName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setString(1, agencyClassCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                agencyClassName = resultSet.getString(2);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return agencyClassName;
    }

    public static String fetchHoldingCompanyName(String holdingCompanyCode) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin TQC_SETUPS_CURSOR.get_specific_holding_company(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String holdingCompanyName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);


            statement.setString(1, holdingCompanyCode);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                holdingCompanyName = resultSet.getString(2);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        }
        return holdingCompanyName;
    }
    
    public static String fetchUnitName(String UnitCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_unit(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String UnitName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1, GlobalCC.checkBDNullValues(UnitCode));
            statement.registerOutParameter(2, OracleTypes.VARCHAR);
            statement.execute();
			
            UnitName = statement.getString(2);  
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        } 
        return UnitName;
    }
    public static String fetchBussinesPerson(String personCode) {
            DBConnector dbConnector = new DBConnector();
            String query = "begin TQC_SETUPS_CURSOR.get_specific_bizPerson(?,?);end;";
            CallableStatement statement = null;
            OracleConnection connection = null;
            String BussinesPerson = "";

            try {
                connection = dbConnector.getDatabaseConnection();
                statement = connection.prepareCall(query);
                statement.setBigDecimal(1, GlobalCC.checkBDNullValues(personCode));
                statement.registerOutParameter(2, OracleTypes.VARCHAR);
                statement.execute();
                            
                BussinesPerson = statement.getString(2);  
                statement.close();
                connection.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            } 
            return BussinesPerson;
        }
	public static String fetchAgentName(String AgentCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_agent(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String AgentName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query); 
            statement.setBigDecimal(1, GlobalCC.checkBDNullValues(AgentCode)); 
            statement.registerOutParameter(2, OracleTypes.VARCHAR);
            statement.execute();
			
            AgentName = statement.getString(2); 
 
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        } 
        return AgentName;
    }
    public static String fetchBankBranchName(String BankBranchCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_BankBranch(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String BankBranchName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query); 
            statement.setBigDecimal(1, GlobalCC.checkBDNullValues(BankBranchCode)); 
            statement.registerOutParameter(2, OracleTypes.VARCHAR);
            statement.execute();
			
            BankBranchName = statement.getString(2);  
            
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        } 
        return BankBranchName;
    }
    public static String fetchSBUName(String SBUCode) {
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_specific_sbu(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        String SBUName = "";

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query); 
            statement.setBigDecimal(1, GlobalCC.checkBDNullValues(SBUCode)); 
            statement.registerOutParameter(2, OracleTypes.VARCHAR);
            statement.execute();
                        
            SBUName = statement.getString(2); 
        
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING( e);
        } 
        return SBUName;
        }
}
