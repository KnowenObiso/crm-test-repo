package TurnQuest.view.Settings1;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class AppSettingsDAO {
    public List<AppSettings> findCountries() {

        String query = "begin ? := tqc_clients_pkg.get_countries(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppSettings> Countries = new ArrayList<AppSettings>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Countries = new ArrayList<AppSettings>();

            while (rs.next()) {
                AppSettings country = new AppSettings();

                country.setCountryCode(rs.getBigDecimal(1));
                country.setCountryShtDesc(rs.getString(2));
                country.setCountryName(rs.getString(3));
                Countries.add(country);

            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Countries;
    }

    public List<AppSettings> findTowns() {

        String query = "begin ? := tqc_clients_pkg.get_towns(?); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppSettings> Town = new ArrayList<AppSettings>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.setBigDecimal(2, LOVDAO.CountryCode);
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Town = new ArrayList<AppSettings>();

            while (rs.next()) {
                AppSettings Towns = new AppSettings();

                Towns.setTownCode(rs.getBigDecimal(1));
                Towns.setPostalDesc(rs.getString(2));
                Towns.setTownShtDesc(rs.getString(3));
                Towns.setTownName(rs.getString(4));
                //Towns.setPostalZIPCode(rs.getString(5));
                Town.add(Towns);

            }
            rs.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return Town;
    }

    public List<AppSettings> findSectors() {

        String query = "begin ? := tqc_clients_pkg.get_sectors(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppSettings> Sectors = new ArrayList<AppSettings>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Sectors = new ArrayList<AppSettings>();

            while (rs.next()) {
                AppSettings sector = new AppSettings();

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

    public List<AppSettings> findBanks() {

        String query = "begin ? := tqc_clients_pkg.get_banks(); end;";
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        List<AppSettings> Banks = new ArrayList<AppSettings>();
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code

            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            Banks = new ArrayList<AppSettings>();

            while (rs.next()) {
                AppSettings Bank = new AppSettings();

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
}
