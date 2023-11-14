package TurnQuest.view.Settings2;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.IQuery;
import TurnQuest.view.Base.IQueryAction;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.setups.Occupation;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class SettingsDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<SettingsValues> findCountries() {

        String query = "begin ? := tqc_clients_pkg.get_countries(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Countries = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Countries = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues country = new SettingsValues();

                country.setCountryCode(rs.getBigDecimal(1));
                country.setCountryShtDesc(rs.getString(2));
                country.setCountryName(rs.getString(3));
                Countries.add(country);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return Countries;
    }

    public List<SettingsValues> findTowns() {

        String query = "begin ? := tqc_clients_pkg.get_towns(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Town = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.setBigDecimal(2, LOVDAO.CountryCode);
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Town = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues Towns = new SettingsValues();

                Towns.setTownCode(rs.getBigDecimal(1));
                Towns.setPostalDesc(rs.getString(2));
                Towns.setTownShtDesc(rs.getString(3));
                Towns.setTownName(rs.getString(4));
                Towns.setPostalZIPCode(rs.getString(5));
                Town.add(Towns);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Town;
    }

    public List<SettingsValues> findSectors() {

        String query = "begin ? := tqc_clients_pkg.get_sectors(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Sectors = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Sectors = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues sector = new SettingsValues();

                sector.setSectorShtDesc(rs.getString(1));
                sector.setSectorName(rs.getString(2));
                sector.setSectorCode(rs.getBigDecimal(3));
                Sectors.add(sector);

            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return Sectors;
    }

    public List<SettingsValues> findBanks() {

        String query = "begin ? := tqc_clients_pkg.get_banks(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Banks = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Banks = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues Bank = new SettingsValues();

                Bank.setBankName(rs.getString(1));
                Bank.setBranchName(rs.getString(2));
                Bank.setBankBranchCode(rs.getBigDecimal(3));
                Banks.add(Bank);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Banks;
    }

    public List<SettingsValues> findRequiredDocs() {

        String query = "begin ? := tqc_web_cursor.getRequiredDocs(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Countries = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            Countries = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues country = new SettingsValues();

                country.setRDOC_CODE(rs.getBigDecimal(1));
                country.setRDOC_SHT_DESC(rs.getString(2));
                country.setROC_DESC(rs.getString(3));
                country.setROC_MANDATORY(rs.getString(4));
                country.setROC_DATE_ADDED(rs.getDate(5));

                Countries.add(country);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Countries;
    }

    public List<SettingsValues> findClientRequiredDocs() {

        String query = "begin ? := tqc_web_cursor.get_client_docs(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Countries = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setObject(2, session.getAttribute("ClientCode"));

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Countries = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues country = new SettingsValues();


                country.setCDOCR_CODE(rs.getBigDecimal(1));
                country.setCDOCR_RDOC_CODE(rs.getBigDecimal(2));
                country.setCDOCR_CLNT_CODE(rs.getBigDecimal(3));
                country.setCDOCR_SUBMITED(rs.getString(4));
                country.setCDOCR_DATE_S(rs.getDate(5));
                country.setCDOCR_REF_NO(rs.getString(6));
                country.setCDOCR_RMRK(rs.getString(7));
                country.setCDOCR_USER_RECEIVD(rs.getString(8));
                country.setROC_DESC(rs.getString(9));

                Countries.add(country);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }


        return Countries;
    }

    public List<SettingsValues> findAccountManagers() {

        String query = "begin ? := tqc_web_cursor.get_acc_mangrs(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<SettingsValues> Countries = new ArrayList<SettingsValues>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Countries = new ArrayList<SettingsValues>();

            while (rs.next()) {
                SettingsValues country = new SettingsValues();

                country.setUSR_USERNAME(rs.getString(1));
                country.setUSR_NAME(rs.getString(2));
                country.setUSR_CODE(rs.getBigDecimal(3));

                Countries.add(country);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Countries;
    }


    public List<Occupation> findSectorOccupations() {

        List<Occupation> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "SELECT   sectors.*\n" + 
        "             FROM (SELECT NULL sec_code, NULL sec_sht_desc, NULL sec_name, NULL occ_name, NULL occ_code\n" + 
        "                     FROM DUAL\n" + 
        "                   UNION\n" + 
        "                   SELECT sec_code, sec_sht_desc, sec_name, occ_name,\n" + 
        "                          occ_code\n" + 
        "                     FROM tqc_sectors, tqc_occupations\n" + 
        "                    WHERE OCC_CODE IN (SELECT OCC_CODE FROM tqc_sector_occupations so WHERE so.OCC_SEC_CODE = SEC_CODE)) sectors\n" + 
        "         ORDER BY sectors.sec_name NULLS FIRST";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            sectorsList = new ArrayList<Occupation>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            ResultSet resultSet = (ResultSet)statement.executeQuery();

            while (resultSet.next()) {
                // For every row, create a Sector object with the values
                // and add it to the list
                Occupation sector = new Occupation();
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
    
    
    public List<Occupation> findAllOccupations() {

        List<Occupation> sectorsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin TQC_SETUPS_CURSOR.get_all_occupations(?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;

        try {
            BigDecimal sectCode=GlobalCC.checkBDNullValues(session.getAttribute("SectCode"));
            sectorsList = new ArrayList<Occupation>();
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            statement.setBigDecimal(1, sectCode);
            statement.registerOutParameter(2,  oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet)statement.getObject(2);

            while (resultSet.next()) {
                // For every row, create a Sector object with the values
                // and add it to the list
                Occupation sector = new Occupation();
                sector.setCode(resultSet.getString(1));
                sector.setShortDesc(resultSet.getString(2));
                sector.setName(resultSet.getString(3));
                sector.setSelected(false);
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
    public List<Occupation> fetchSelectedOccupations() {

        List<Occupation> sectorsList = null;

        String query = "SELECT occ_code, occ_sht_desc, occ_name\n" + 
        "             FROM tqc_occupations\n" + 
        "             WHERE OCC_CODE IN (SELECT OCC_CODE FROM tqc_sector_occupations WHERE OCC_SEC_CODE = :v_sect_code )";
 
        try {
                 sectorsList = new ArrayList<Occupation>(); 
                 BigDecimal sectCode=GlobalCC.checkBDNullValues(session.getAttribute("SectCode"));
                 query=query.replaceAll(":v_sect_code", sectCode!=null?sectCode.toString(): "-1");
                 System.out.println("query: "+query);
                IQuery.fetchResult(query,sectorsList,new IQueryAction() {  
                                   public void fetch(ResultSet rs,Object o) {
                                           try{
                                           while (rs.next()) { 
                                              List<Occupation> sectorsList=(List<Occupation>)o;
                                                Occupation sector = new Occupation();
                                                sector.setCode(rs.getString(1));
                                                sector.setShortDesc(rs.getString(2));
                                                sector.setName(rs.getString(3));
                                                sector.setSelected(false);
                                                sectorsList.add(sector);
                                                //System.out.println("Code: "+sector.getCode()+ " Name: "+sector.getName()); 
                                           }
                                                } catch (Exception e) {
                                                GlobalCC.EXCEPTIONREPORTING( e);
                                            }
                                    }
                           });  

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return sectorsList;
    }

}
