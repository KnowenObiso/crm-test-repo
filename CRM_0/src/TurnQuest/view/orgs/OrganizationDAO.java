package TurnQuest.view.orgs;


import TurnQuest.view.Agents.Agent;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.IQuery;
import TurnQuest.view.Base.IQueryAction;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Branches.Branch;
import TurnQuest.view.Branches.Region;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Divisions.Division;
import TurnQuest.view.models.BranchName;
import TurnQuest.view.setups.AdministrativeRegion;
import TurnQuest.view.setups.District;
import TurnQuest.view.setups.Town;

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

import org.apache.commons.dbutils.DbUtils;


public class OrganizationDAO {
    private Rendering renderer = new Rendering();

    public OrganizationDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

//    public List<Organization> findBranchAgents() {
//        DBConnector dbCon = new DBConnector();
//        OracleConnection conn = null;
//        conn = dbCon.getDatabaseConnection();
//        OracleResultSet rs = null;
//        OracleCallableStatement callStmt = null;
//
//        String query =
//            "select bra_code,bra_sht_desc,bra_name" + "from TQC_BRANCH_AGENCIES,tqc_branches where brn_code = bra_brn_code ";
//        List<Organization> orgData = new ArrayList<Organization>();
//
//        try {
//            callStmt = (OracleCallableStatement)conn.prepareCall(query);
//            rs = (OracleResultSet)callStmt.executeQuery();
//            if (rs != null) {
//                while (rs.next()) {
//                    Organization orgDef = new Organization();
//                    orgDef.setBraAgnCode(rs.getBigDecimal(1));
//                    orgDef.setBraAgnShtDesc(rs.getString(2));
//                    orgDef.setBraAgnShtDesc(rs.getString(3));
//                    orgData.add(orgDef);
//                }
//            }
//
//
//        } catch (Exception e) {
//            GlobalCC.EXCEPTIONREPORTING(conn, e);
//        } finally {
//            DbUtils.closeQuietly(conn, callStmt, rs);
//        }
//        return orgData;
//    }

    public List<Organization> findOrganization() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        OracleResultSet rs = null;
        OracleCallableStatement callStmt = null;

        String query = " SELECT  org_code, \n" +
            "                org_sht_desc, \n" +
            "                org_name, \n" +
            "                org_addrs, \n" +
            "                org_twn_code,\n" +
            "                org_cou_code, \n" +
            "                org_email_addrs, \n" +
            "                org_phy_addrs, \n" +
            "                org_cur_code,\n" +
            "                org_zip, \n" +
            "                org_fax, \n" +
            "                org_tel1, \n" +
            "                org_tel2, \n" +
            "                org_rpt_logo,\n" +
            "                org_motto, \n" +
            "                org_pin_no, \n" +
            "                org_ed_code, \n" +
            "                org_item_acc_code,\n" +
            "                org_other_name, \n" +
            "                org_type, \n" +
            "                org_web_brn_code, \n" +
            "                org_web_addrs,\n" +
            "                org_agn_code, \n" +
            "                org_directors, \n" +
            "                org_lang_code, \n" +
            "                org_avatar,\n" +
            "                cur_symbol,\n" +
            "                cur_desc,\n" +
            "                cou_name,\n" +
            "                twn_name, \n" +
            "                org_sts_code,\n" +
            "                sts_name state_name, \n" +
            "                org_grp_logo,\n" +
            "                agn_name manager, \n" +
            "                org_vat_number,\n" +
            "                cou_admin_reg_type, \n" +
            "                org_mobile1, \n" +
            "                org_mobile2, \n" +
            "                cou_zip_code,\n" +
            "                org_cert_sign,\n" +
            "                org_bnk_code, \n" +
            "                org_bbr_code, \n" +
            "                org_bank_account_no, \n" +
            "                org_bank_account_name, \n" +
            "                org_swift_code,\n" +
            "                bnk_bank_name org_bank_name,\n" +
            "                bbr_branch_name org_bank_branch\n" +
            "           FROM tqc_organizations,tqc_agencies,tqc_countries,tqc_states,\n" +
            "           tqc_towns,tqc_currencies,tqc_banks,tqc_bank_branches\n" +
            "          WHERE\n" +
            "           org_code = NVL(:v_org_code, org_code) \n" +
            "            AND org_bnk_code = bnk_code(+)\n" +
            "            AND org_cur_code = cur_code(+)\n" +
            "            AND org_bbr_code = bbr_code(+)\n" +
            "            AND org_agn_code = agn_code(+)\n" +
            "            AND org_cou_code = cou_code(+)\n" +
            "            AND org_sts_code = sts_code(+)\n" +
            "            and org_twn_code = twn_code(+)\n" +
            "            AND org_cou_code = cou_code ";

        List<Organization> orgData = new ArrayList<Organization>();
        try {
            query = query.replaceAll(":v_org_code", "null");
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            rs = (OracleResultSet)callStmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    Organization orgDef = new Organization();
                    orgDef.setOrgCode(rs.getBigDecimal(1));
                    orgDef.setOrgShortDesc(rs.getString(2));
                    orgDef.setOrgName(rs.getString(3));
                    orgDef.setOrgAddrs(rs.getString(4));
                    orgDef.setOrgTwnCode(rs.getBigDecimal(5));
                    orgDef.setOrgCouCode(rs.getBigDecimal(6));
                    orgDef.setOrgEmail(rs.getString(7));
                    orgDef.setOrgPhyAddrs(rs.getString(8));
                    orgDef.setOrgCurCode(rs.getBigDecimal(9));
                    orgDef.setOrgZip(rs.getString(10));
                    orgDef.setOrgFax(rs.getString(11));
                    orgDef.setOrgTel1(rs.getString(12));
                    orgDef.setOrgTel2(rs.getString(13));
                    //orgDef.setOrgRptLogo(rs.getString(14));
                    orgDef.setOrgMotto(rs.getString(15));
                    orgDef.setOrgPinNo(rs.getString(16));
                    orgDef.setOrgEdCode(rs.getString(17));
                    orgDef.setOrgItemAccCode(rs.getString(18));
                    orgDef.setOrgOtherName(rs.getString(19));
                    orgDef.setOrgType(rs.getString(20));
                    orgDef.setOrgWebBrnCode(rs.getBigDecimal(21));
                    orgDef.setOrgWebAddrs(rs.getString(22));
                    orgDef.setOrgAgnCode(rs.getBigDecimal(23));
                    orgDef.setOrgDirectors(rs.getString(24));
                    orgDef.setOrgLangCode(rs.getBigDecimal(25));
                    // orgDef.setOrgAvatar(rs.getString(26));

                    orgDef.setCurSymbol(rs.getString(27));
                    orgDef.setCurDesc(rs.getString(28));
                    orgDef.setCouName(rs.getString(29));
                    orgDef.setTwnName(rs.getString(30));
                    orgDef.setOrgStsCode(rs.getBigDecimal(31));
                    orgDef.setStateName(rs.getString(32));
                    orgDef.setAgnName(rs.getString(34));
                    orgDef.setVatNumber(rs.getString(35));
                    orgDef.setAdministrativeType(rs.getString(36));
                    orgDef.setMobile1(rs.getString(37));
                    orgDef.setMobile2(rs.getString(38));
                    orgDef.setCouZipCode(rs.getBigDecimal(39));
                    // orgDef.setOrgGrpLogo(rs.getString(33));
                    //bANK dETAILS
                    orgDef.setORG_BANK_ACCOUNT_NAME(rs.getString("ORG_BANK_ACCOUNT_NAME"));
                    orgDef.setORG_BANK_ACCOUNT_NO(rs.getString("ORG_BANK_ACCOUNT_NO"));
                    orgDef.setORG_SWIFT_CODE(rs.getString("ORG_SWIFT_CODE"));
                    orgDef.setORG_BNK_CODE(rs.getBigDecimal("ORG_BNK_CODE"));
                    orgDef.setORG_BBR_CODE(rs.getBigDecimal("ORG_BBR_CODE"));
                    orgDef.setORG_BANK_NAME(rs.getString("ORG_BANK_NAME"));
                    orgDef.setORG_BANK_BRANCH(rs.getString("ORG_BANK_BRANCH"));
                    orgData.add(orgDef);
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return orgData;
    }

    /**
     * Fetches a list of all Organizations, together with the relevant details
     * such as country name, currency, town etc.
     *
     * @return A List of Organizations
     */
    public List<Organization> fetchOrganizationInfo() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_SETUPS_CURSOR.organization_details(?,?); end;";

        List<Organization> orgData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setString(2, null);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization orgDef = new Organization();
                orgDef.setOrgCode(rs.getBigDecimal(1));
                orgDef.setOrgShortDesc(rs.getString(2));
                orgDef.setOrgName(rs.getString(3));
                orgDef.setOrgAddrs(rs.getString(4));
                orgDef.setOrgTwnCode(rs.getBigDecimal(5));
                orgDef.setOrgCouCode(rs.getBigDecimal(6));
                orgDef.setOrgEmail(rs.getString(7));
                orgDef.setOrgPhyAddrs(rs.getString(8));
                orgDef.setOrgCurCode(rs.getBigDecimal(9));
                orgDef.setOrgZip(rs.getString(10));
                orgDef.setOrgFax(rs.getString(11));
                orgDef.setOrgTel1(rs.getString(12));
                orgDef.setOrgTel2(rs.getString(13));
                //orgDef.setOrgRptLogo(rs.getString(14));
                orgDef.setOrgMotto(rs.getString(15));
                orgDef.setOrgPinNo(rs.getString(16));
                orgDef.setOrgEdCode(rs.getString(17));
                orgDef.setOrgItemAccCode(rs.getString(18));
                orgDef.setOrgOtherName(rs.getString(19));
                orgDef.setOrgType(rs.getString(20));
                orgDef.setOrgWebBrnCode(rs.getBigDecimal(21));
                orgDef.setOrgWebAddrs(rs.getString(22));
                orgDef.setOrgAgnCode(rs.getBigDecimal(23));
                orgDef.setOrgDirectors(rs.getString(24));
                orgDef.setOrgLangCode(rs.getBigDecimal(25));
                //orgDef.setOrgAvatar(rs.getString(26));

                orgDef.setCurSymbol(rs.getString(27));
                orgDef.setCurDesc(rs.getString(28));
                orgDef.setCouName(rs.getString(29));
                orgDef.setTwnName(rs.getString(30));
                orgDef.setOrgStsCode(rs.getBigDecimal(31));
                orgDef.setStateName(rs.getString(32));

                orgData.add(orgDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }

    public List<Organization> findCountries() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.getcountries(?,?,?); end;";

        List<Organization> countriesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setString(2, null);
            callStmt.setString(3, null);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization countriesDef = new Organization();
                countriesDef.setCouCode(rs.getBigDecimal(1));
                countriesDef.setCouShortDesc(rs.getString(2));
                countriesDef.setCouName(rs.getString(3));
                countriesDef.setCouBaseCurr(rs.getString(4));
                countriesDef.setCouNationality(rs.getString(6));
                countriesDef.setAdministrativeType(rs.getString(8));
                countriesDef.setCouZipCode(rs.getBigDecimal(7));
                countriesData.add(countriesDef);

            }
            rs.close();
            callStmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return countriesData;
    }


    public List<Organization> findKpiTasks() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.getKpiTask(?); end;";

        List<Organization> KPIData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Organization KpiDef = new Organization();
                KpiDef.setKpiTask(rs.getString(1));
                KpiDef.setKpiTcktCode(rs.getBigDecimal(2));
                KpiDef.setKpiSubTask(rs.getString(3));
                KpiDef.setKpidbId(rs.getBigDecimal(4));
                KPIData.add(KpiDef);
            }
            rs.close();
            callStmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return KPIData;
    }

    public List<AdministrativeRegion> fetchaAdminRegionsByCountry() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<AdministrativeRegion> administrativeRegionList =
            new ArrayList<AdministrativeRegion>();

        String query = "begin TQC_SETUPS_CURSOR.get_admin_regions(?,?); end;";

        if (session.getAttribute("countryCode") != null) {
            BigDecimal countryCode =
                new BigDecimal(session.getAttribute("countryCode").toString());
            try {
                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.registerOutParameter(1, OracleTypes.CURSOR);
                callStmt.setBigDecimal(2, countryCode);
                callStmt.execute();
                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {
                    AdministrativeRegion administrativeRegion =
                        new AdministrativeRegion();
                    administrativeRegion.setRegCode(rs.getBigDecimal(1));
                    administrativeRegion.setRegShortDesc(rs.getString(2));
                    administrativeRegion.setRegName(rs.getString(3));
                    administrativeRegion.setRegCouCode(rs.getBigDecimal(4));

                    administrativeRegionList.add(administrativeRegion);

                }

                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return administrativeRegionList;
    }

    public List<Organization> findTowns() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.gettowns(?,?); end;";

        List<Organization> townsData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("COUCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization townsDef = new Organization();
                townsDef.setTwnCode(rs.getBigDecimal(1));
                townsDef.setTwnCouCode(rs.getBigDecimal(2));
                townsDef.setTwnShortDesc(rs.getString(3));
                townsDef.setTwnName(rs.getString(4));
                townsDef.setTwnStsCode(rs.getBigDecimal(5));
                // holds town postal code
                townsDef.setTwnPostalCode(rs.getBigDecimal(7));

                townsData.add(townsDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return townsData;
    }

    public List<Town> fetchTownsByRegion() {
        List<Town> townsList = new ArrayList<Town>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getTownsByAdminRegion(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("adminRegionCode") != null) {
                BigDecimal stateCode =
                    (BigDecimal)session.getAttribute("adminRegionCode");

                statement.registerOutParameter(1,
                                               oracle.jdbc.internal.OracleTypes.CURSOR);
                statement.setBigDecimal(2, stateCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    Town town = new Town();
                    town.setCode(resultSet.getString(1));
                    town.setCountryCode(resultSet.getString(2));
                    town.setShortDesc(resultSet.getString(3));
                    town.setName(resultSet.getString(4));


                    townsList.add(town);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return townsList;
    }

    public List<Organization> findCurrencies() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.currencies(?); end;";

        List<Organization> currenciesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            // callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("COUCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization currenciesDef = new Organization();
                currenciesDef.setCurCode(rs.getBigDecimal(1));
                currenciesDef.setCurSymbol(rs.getString(2));
                currenciesDef.setCurDesc(rs.getString(3));
                currenciesDef.setCurRnd(rs.getString(4));
                currenciesData.add(currenciesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return currenciesData;
    }


    public List<Organization> findBankRegions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            " SELECT BNKR_CODE,BNKR_ORG_CODE,BNKR_SHT_DESC,BNKR_NAME,BNKR_WEF,BNKR_WET, \n" +
            "         BNKR_REG_CODE,BNKR_AGN_CODE,agn_name  manager\n" +
            "       FROM tqc_bank_regions,tqc_agencies\n" +
            "       where BNKR_AGN_CODE=AGN_CODE(+) ";

        List<Organization> regionsData = new ArrayList<Organization>();
        try {
            if (!GlobalCC.tableExists("tqc_bank_regions")) {
                return regionsData;
            }
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();

            while (rs.next()) {
                Organization region = new Organization();
                region.setBnkrCode(rs.getBigDecimal(1));
                region.setBnkrOrgCode(rs.getBigDecimal(2));
                region.setBnkrShtDesc(rs.getString(3));
                region.setBnkrName(rs.getString(4));
                region.setBnkrWef(rs.getDate(5));
                region.setBnkrWet(rs.getDate(6));
                region.setBnkrRegCode(rs.getBigDecimal(7));
                region.setBnkrAgnCode(rs.getBigDecimal(8));
                region.setBnkrManager(rs.getString(9));
                regionsData.add(region);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return regionsData;
    }


    public List<Organization> findRegions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        BigDecimal divCode = null;

        if (session.getAttribute("divisionCode") != null) {
            divCode = (BigDecimal)session.getAttribute("divisionCode");
        }

        String query = "begin TQC_SETUPS_CURSOR.regions(?,?); end;";

        System.out.println("ORGCODE: " + session.getAttribute("ORGCode"));

        BigDecimal ORGCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ORGCode"));

        List<Organization> regionsData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2, ORGCode);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization regionsDef = new Organization();
                regionsDef.setRegCode(rs.getBigDecimal(1));
                regionsDef.setRegOrgCode(rs.getBigDecimal(2));
                regionsDef.setRegShortDesc(rs.getString(3));
                regionsDef.setRegName(rs.getString(4));
                regionsDef.setRegWef(rs.getDate(5));
                regionsDef.setRegWet(rs.getDate(6));
                regionsDef.setRegAgnCode(rs.getBigDecimal(7));
                regionsDef.setRegPostLevel(rs.getString(8));
                regionsDef.setRegMngrAllowed(rs.getString(9));
                regionsDef.setRegOverideCommEarned(rs.getString(10));
                regionsDef.setRegManager(rs.getString(11));
                regionsDef.setRegBrnMngrSeq_no(rs.getString(12));
                regionsDef.setRegAgnSeqNo(rs.getString(13));

                regionsData.add(regionsDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return regionsData;
    }

    public List<Organization> findBranches() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.get_branches(?,?); end;";

        List<Organization> branchesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("REGCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchesDef = new Organization();
                branchesDef.setBrnCode(rs.getBigDecimal(1));
                branchesDef.setBrnShortDesc(rs.getString(2));
                branchesDef.setBrnRegCode(rs.getBigDecimal(3));
                branchesDef.setBrnName(rs.getString(4));
                branchesDef.setBrnPhyAddrs(rs.getString(5));
                branchesDef.setBrnEmail(rs.getString(6));
                branchesDef.setBrnPostAddrs(rs.getString(7));
                branchesDef.setBrnTwnCode(rs.getBigDecimal(8));
                branchesDef.setBrnCouCode(rs.getBigDecimal(9));
                branchesDef.setBrnContact(rs.getString(10));
                branchesDef.setBrnManager(rs.getString(11));
                branchesDef.setBrnTel(rs.getString(12));
                branchesDef.setBrnFax(rs.getString(13));
                branchesDef.setBrnGenPolClm(rs.getString(14));
                branchesDef.setBrnBnsCode(rs.getBigDecimal(15));
                branchesDef.setBrnAgnCode(rs.getBigDecimal(16));
                branchesDef.setBrnPostLevel(rs.getString(17));
                branchesDef.setBrnMngrAllowed(rs.getString(18));
                branchesDef.setBrnOverideCommEarned(rs.getString(19));
                branchesDef.setBrnBrnMngrSeq_no(rs.getString(20));
                branchesDef.setBrnAgnSeqNo(rs.getString(21));
                branchesDef.setPolicySeq(rs.getString(22));
                branchesDef.setPropSeq(rs.getString(23));
                branchesDef.setRef(rs.getString(24));

                branchesDef.setBrnTownName(rs.getString("BRN_TOWN_NAME"));
                branchesDef.setBrnPostCode(rs.getString("BRN_POST_CODE"));


                /*   branchesDef.setTxtAgencyDevExc(rs.getString("BRN_AGENCY_DEV_EXC"));
                branchesDef.setTxtEarnOveride(rs.getString("BRN_ERN_OVER_BS"));
                branchesDef.setTxtWEF(rs.getDate("BRN_WEF"));
                branchesDef.setTxtWET(rs.getDate("BRN_WET"));
                branchesDef.setTxtRegionCode(rs.getBigDecimal("BRN_REGION_CODE"));*/


                //  branchesDef.setBrnAgnPolPrefix(rs.getString(22));
                branchesDef.setBrnAdeCode(rs.getBigDecimal(25));
                branchesDef.setBrnAdeName(rs.getString(26) == null ? "NONE" :
                                          rs.getString(26));


                branchesData.add(branchesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchesData;
    }

    public List<Organization> findBranchContacts() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_SETUPS_CURSOR.getBranchContacts(?); end;";

        List<Organization> branchesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchesDef = new Organization();
                branchesDef.setTbcCode(rs.getBigDecimal(1));
                branchesDef.setTbcName(rs.getString(2));
                branchesDef.setTbcDesignation(rs.getString(3));
                branchesDef.setTbcMobile(rs.getString(4));
                branchesDef.setTbcPhone(rs.getString(5));
                branchesDef.setTbcIdNumber(rs.getString(6));
                branchesDef.setTbcPhysicalAdd(rs.getString(7));
                branchesDef.setTbcEmailAdd(rs.getString(8));


                branchesData.add(branchesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchesData;
    }

    public List<Organization> findOrgBranches() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.get_Orgbranches(?,?,?); end;";

        List<Organization> branchesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("ORGCode"));
            callStmt.setObject(3, session.getAttribute("REGCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchesDef = new Organization();
                branchesDef.setBrnCode(rs.getBigDecimal(1));
                branchesDef.setBrnShortDesc(rs.getString(2));
                branchesDef.setBrnRegCode(rs.getBigDecimal(3));
                branchesDef.setBrnName(rs.getString(4));
                branchesDef.setBrnPhyAddrs(rs.getString(5));
                branchesDef.setBrnEmail(rs.getString(6));
                branchesDef.setBrnPostAddrs(rs.getString(7));
                branchesDef.setBrnTwnCode(rs.getBigDecimal(8));
                branchesDef.setBrnCouCode(rs.getBigDecimal(9));
                branchesDef.setBrnContact(rs.getString(10));
                branchesDef.setBrnManager(rs.getString(11));
                branchesDef.setBrnTel(rs.getString(12));
                branchesDef.setBrnFax(rs.getString(13));
                branchesDef.setBrnGenPolClm(rs.getString(14));
                branchesDef.setBrnBnsCode(rs.getBigDecimal(15));
                branchesDef.setBrnAgnCode(rs.getBigDecimal(16));
                branchesDef.setBrnPostLevel(rs.getString(17));
                branchesDef.setBrnMngrAllowed(rs.getString(18));
                branchesDef.setBrnOverideCommEarned(rs.getString(19));
                branchesDef.setBrnBrnMngrSeq_no(rs.getString(20));
                branchesDef.setBrnAgnSeqNo(rs.getString(21));
                branchesData.add(branchesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchesData;
    }

    public List<Organization> findBranchUnits() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_SETUPS_CURSOR.get_branch_agency_units(?,?,?); end;";

        List<Organization> branchUnitsData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("braCode"));
            callStmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchUnitsDef = new Organization();
                branchUnitsDef.setBruCode(rs.getBigDecimal(1));
                branchUnitsDef.setBruBrnCode(rs.getBigDecimal(2));
                branchUnitsDef.setBruShortDesc(rs.getString(3));
                branchUnitsDef.setBruName(rs.getString(4));
                branchUnitsDef.setBruSupervisor(rs.getString(5));
                branchUnitsDef.setBruStatus(rs.getString(6));
                branchUnitsDef.setBruAgnCode(rs.getBigDecimal(7));
                branchUnitsDef.setBruBraCode(rs.getBigDecimal(8));
                branchUnitsDef.setBruManager(rs.getString(9));
                branchUnitsDef.setBruPostLevel(rs.getString(10));
                branchUnitsDef.setBruMngrAllowed(rs.getString(11));
                branchUnitsDef.setBruOverideCommEarned(rs.getString(12));
                branchUnitsDef.setUnitspolicySeq(rs.getString(15));
                branchUnitsDef.setUnitspropSeq(rs.getString(15));
                branchUnitsDef.setTxtAgencyName(rs.getString("BRU_AGENCY_NAME"));
                branchUnitsDef.setTxtTeamManagerSequenceNo(rs.getBigDecimal("BRU_TEAMMANAGER_SEQUENCE_NO"));
                branchUnitsDef.setTxtAgentsSequenceNo(rs.getBigDecimal("BRU_AGENTS_SEQUENCE_NO"));

                branchUnitsDef.setBruAgnSeqNo(rs.getString(13));
                branchUnitsData.add(branchUnitsDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchUnitsData;
    }


    public List<Organization> findBranchNames() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.branch_names(?); end;";

        List<Organization> branchNamesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            //callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchNamesDef = new Organization();
                branchNamesDef.setBnsCode(rs.getBigDecimal(1));
                branchNamesDef.setBnsShortDesc(rs.getString(2));
                branchNamesDef.setBnsName(rs.getString(3));
                branchNamesDef.setBnsPhyAddrs(rs.getString(4));
                branchNamesDef.setBnsEmail(rs.getString(5));
                branchNamesDef.setBnsPostAddrs(rs.getString(6));
                branchNamesDef.setBnsTwnCode(rs.getBigDecimal(7));
                branchNamesDef.setBnsCouCode(rs.getBigDecimal(8));
                branchNamesDef.setBnsContact(rs.getString(9));
                branchNamesDef.setBnsManager(rs.getString(10));
                branchNamesDef.setBnsTel(rs.getString(11));
                branchNamesDef.setBnsFax(rs.getString(12));
                branchNamesDef.setBnsPostalCode(rs.getBigDecimal(13));

                branchNamesData.add(branchNamesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchNamesData;
    }

    public List<Organization> findBranchAgencies() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin TQC_SETUPS_CURSOR.get_branch_agencies(?,?); end;";

        List<Organization> branchAgenciesData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization branchAgenciesDef = new Organization();
                branchAgenciesDef.setBraCode(rs.getBigDecimal(1));
                branchAgenciesDef.setBraBrnCode(rs.getBigDecimal(2));
                branchAgenciesDef.setBraShortDesc(rs.getString(3));
                branchAgenciesDef.setBraName(rs.getString(4));
                branchAgenciesDef.setBraStatus(rs.getString(5));
                branchAgenciesDef.setBraManager(rs.getString(6));
                branchAgenciesDef.setBraAgnCode(rs.getBigDecimal(7));
                branchAgenciesDef.setBraPostLevel(rs.getString(8));
                branchAgenciesDef.setBraMngrAllowed(rs.getString(9));
                branchAgenciesDef.setBraOverideCommEarned(rs.getString(10));
                branchAgenciesDef.setBraBrnMngrSeq_no(rs.getString(11));
                branchAgenciesDef.setBraAgnSeqNo(rs.getString(12));
                branchAgenciesDef.setAgencypolicySeq(rs.getString(13));
                branchAgenciesDef.setAgencypropSeq(rs.getString(14));
                branchAgenciesDef.setShtDescPrefix(rs.getString(15));
                branchAgenciesDef.setAgnTxtCombuss(rs.getString(16));
                branchAgenciesData.add(branchAgenciesDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchAgenciesData;
    }

    public List<Organization> fetchUnitAgents() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        String query = "SELECT agn_code,agn_sht_desc,agn_name  \n" +
            "             FROM tqc_agencies,tqc_account_types  \n" +
            "             WHERE  + \n" +
            "              act_code = agn_act_code \n" +
            "              and (act_type_sht_desc = 'IA' )\n" +
            "            and agn_bru_code = :v_bru_code\n" +
            "            UNION\n" +
            "SELECT agn_code,agn_sht_desc,agn_name  \n" +
            "             FROM lms_agencies,tqc_account_types  \n" +
            "             WHERE  + \n" +
            "              act_code = agn_act_code \n" +
            "              and (act_type_sht_desc = 'IA' )\n" +
            "            and agn_bru_code = :v_bru_code";

        List<Organization> unitAgents = new ArrayList<Organization>();

        try {
            BigDecimal bruCode =
                GlobalCC.checkBDNullValues(session.getAttribute("bruCode"));
            query =
                    query.replaceAll(":v_bru_code", (bruCode != null) ? bruCode.toString() :
                                                    "null");
            System.out.println("query: " + query);
            conn = dbCon.getDatabaseConnection();
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();
            while (rs.next()) {
                Organization unitAgent = new Organization();
                unitAgent.setUnaCode(rs.getBigDecimal(1));
                unitAgent.setUnaShortDesc(rs.getString(2));
                unitAgent.setUnaName(rs.getString(3));
                unitAgents.add(unitAgent);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return unitAgents;
    }

    public List<Organization> fetchTranferableItems() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        List<Organization> items = new ArrayList<Organization>();
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        String query =
            "begin ? := Tqc_Agencies_Cursors.fetch_transferable_items(?,?); end;";

        try {
            conn = dbCon.getDatabaseConnection();

            BigDecimal parentCode =
                GlobalCC.checkBDNullValues(session.getAttribute("tt_code"));
            String transferType =
                GlobalCC.checkNullValues(session.getAttribute("tt_trans_type"));

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2, parentCode);
            callStmt.setString(3, transferType);
            callStmt.execute();
            rs = (OracleResultSet)callStmt.getObject(1);
            if (rs != null) {
                while (rs.next()) {
                    Organization org = new Organization();
                    org.setTtiTtCode((BigDecimal)session.getAttribute("tt_code"));
                    org.setTtiItemCode(rs.getBigDecimal("ttx_item_code"));
                    org.setTtiItemName(rs.getString("ttx_item_name"));
                    org.setTtiItemShtDesc(rs.getString("ttx_item_sht_desc"));
                    org.setTtiFromCode(rs.getBigDecimal("ttx_from_code"));
                    org.setTtiFromName(rs.getString("ttx_from_name"));
                    org.setTtiItemType(rs.getString("ttx_item_type"));
                    items.add(org);
                }
                rs.close();
            }
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return items;
    }

    public List<Organization> fetchTranferedItems() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;

        List<Organization> items = new ArrayList<Organization>();
        String query =
            "SELECT tti_code,tti_tt_code, tti_item_code, tti_item_name, tti_item_sht_desc, tti_item_type " +
            " FROM tqc_transfers_items,tqc_transfers " +
            " WHERE tt_authorized <> ? " + "   AND tti_tt_code = tt_code " +
            "   AND tt_code = ?  " + "   AND tt_trans_type = ?";

        List<Organization> branchAgenciesData = new ArrayList<Organization>();
        try {
            conn = dbCon.getDatabaseConnection();
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, "Y");
            callStmt.setBigDecimal(2,
                                   GlobalCC.checkBDNullValues(session.getAttribute("tt_code")));
            callStmt.setString(3,
                               GlobalCC.checkNullValues(session.getAttribute("tt_trans_type")));
            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Organization org = new Organization();
                    org.setTtiCode(rs.getBigDecimal("tti_code"));
                    org.setTtiTtCode(rs.getBigDecimal("tti_tt_code"));
                    org.setTtiItemCode(rs.getBigDecimal("tti_item_code"));
                    org.setTtiItemName(rs.getString("tti_item_name"));
                    org.setTtiItemShtDesc(rs.getString("tti_item_sht_desc"));
                    ;
                    org.setTtiItemType(rs.getString("tti_item_type"));
                    items.add(org);
                }
                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return items;
    }

    /**
     * Gets the list of all Agency Development Executives.
     *
     * @return list of all Agency Development Executives
     */
    public List<Organization> findAgencyDevelopmentExecutives() {
        DBConnector dbHandler = new DBConnector();
        List<Organization> ades = new ArrayList<Organization>();
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;

        if (session.getAttribute("accountTypeCode") == null)
            return ades;

        String query =
            "begin tqc_setups_cursor.agency_development_executives(?,?); END;";
        try {
            conn = dbHandler.getDatabaseConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(query);
            cstmt.setObject(1,
                            String.valueOf(session.getAttribute("accountTypeCode")));
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.execute();

            OracleResultSet rs = (OracleResultSet)cstmt.getObject(2);
            while (rs.next()) {
                Organization ade = new Organization();
                ade.setAgnCode(rs.getBigDecimal(1));
                ade.setAgnShortDesc(rs.getString(2));
                ade.setAgnName(rs.getString(3));

                ades.add(ade);
            }

            rs.close();
            cstmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return ades;
    }

    public List<Organization> findManagers() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.managers(?); end;";

        List<Organization> managersData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            //callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization managersDef = new Organization();
                managersDef.setAgnCode(rs.getBigDecimal(1));
                managersDef.setAgnShortDesc(rs.getString(2));
                managersDef.setAgnName(rs.getString(3));
                managersDef.setAgnTwnName(rs.getString(4));
                managersData.add(managersDef);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return managersData;
    }

    public List<Organization> findAgencyManagers() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.agencyManagers(?); end;";

        List<Organization> managersData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            //callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization managersDef = new Organization();
                managersDef.setAgnCode(rs.getBigDecimal(1));
                managersDef.setAgnShortDesc(rs.getString(2));
                managersDef.setAgnName(rs.getString(3));
                managersDef.setAgnTwnName(rs.getString(4));

                managersData.add(managersDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return managersData;
    }

    public List<Organization> findUnitManagers() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.unitManagers(?); end;";

        List<Organization> managersData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            //callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization managersDef = new Organization();
                managersDef.setAgnCode(rs.getBigDecimal(1));
                managersDef.setAgnShortDesc(rs.getString(2));
                managersDef.setAgnName(rs.getString(3));
                managersDef.setAgnTwnName(rs.getString(4));

                managersData.add(managersDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return managersData;
    }

    public List<Organization> findOrgManagers() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_SETUPS_CURSOR.agencies(?); end;";

        List<Organization> managersData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            //callStmt.setBigDecimal(2, (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Organization managersDef = new Organization();
                managersDef.setAgnCode(rs.getBigDecimal(1));
                managersDef.setAgnShortDesc(rs.getString(2));
                managersDef.setAgnName(rs.getString(3));

                managersData.add(managersDef);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return managersData;
    }

    public List<Organization> fetchUserUnassignedBranches() {
        String query =
            "begin ? := tqc_roles_cursor.Get_user_OrgsFiltered(?); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                List<Region> regionsList = new ArrayList<Region>();
                Organization org = new Organization();
                org.setOrgCode(rs.getBigDecimal(1));
                org.setOrgShortDesc(rs.getString(2));
                org.setOrgName(rs.getString(3));
                org.setNodeType("P");

                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.GetRegionsFiltered(?,?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.setBigDecimal(3,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
                stmt2.execute();
                OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                while (rs2.next()) {
                    List<Branch> branchesList = new ArrayList<Branch>();
                    Region region = new Region();
                    region.setRegionCode(rs2.getBigDecimal(1));
                    region.setRegOrgCode(rs2.getBigDecimal(2));
                    region.setRegionName(rs2.getString(3));
                    region.setNodeType("S");

                    OracleCallableStatement stmt3 =
                        (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_unassigned_branches(?,?); end;");

                    stmt3.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt3.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                    stmt3.setBigDecimal(3, rs2.getBigDecimal(1));
                    stmt3.execute();
                    OracleResultSet rs3 = (OracleResultSet)stmt3.getObject(1);

                    while (rs3.next()) {
                        Branch branch = new Branch();
                        branch.setBranchCode(rs3.getBigDecimal(1)); // Branch code
                        branch.setRegCode(rs3.getBigDecimal(3)); // Region Code
                        branch.setBranchName(rs3.getString(4)); // Branch name

                        branch.setNodeType("SB");
                        branchesList.add(branch);
                    }
                    region.setBranchesList(branchesList);
                    regionsList.add(region);
                }
                org.setRegionsList(regionsList);
                orgList.add(org);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }

    public List<Branch> fetchUnassignedBranchesByRegion(BigDecimal regionCode,
                                                        BigDecimal userCode) {
        List<Branch> branchesList = new ArrayList<Branch>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_ROLES_CURSOR.get_user_unassigned_branches(?,?); end;";

        if (regionCode != null && userCode != null) {
            try {
                OracleCallableStatement callStmt = null;
                callStmt =
                        (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                callStmt.registerOutParameter(1, OracleTypes.CURSOR);

                callStmt.setBigDecimal(2, userCode);
                callStmt.setBigDecimal(3, regionCode);
                callStmt.execute();
                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {
                    Branch branch = new Branch();
                    branch.setBranchCode(rs.getBigDecimal(1)); // Branch code
                    branch.setRegCode(rs.getBigDecimal(3)); // Region Code
                    branch.setBranchName(rs.getString(4)); // Branch name
                    branchesList.add(branch);
                }

                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return branchesList;
    }

    public List<Branch> fetchAssignedBranchesByRegion(BigDecimal regionCode,
                                                      BigDecimal userCode) {
        List<Branch> branchesList = new ArrayList<Branch>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_ROLES_CURSOR.get_user_assigned_branches(?,?); end;";

        if (regionCode != null && userCode != null) {
            try {
                OracleCallableStatement callStmt = null;
                callStmt =
                        (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                callStmt.registerOutParameter(1, OracleTypes.CURSOR);

                callStmt.setBigDecimal(2, userCode);
                callStmt.setBigDecimal(3, regionCode);
                callStmt.execute();
                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {
                    Branch branch = new Branch();
                    branch.setBranchCode(rs.getBigDecimal(1)); // Branch code
                    branch.setRegCode(rs.getBigDecimal(3)); // Region Code
                    branch.setBranchName(rs.getString(4)); // Branch name
                    branchesList.add(branch);
                }

                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return branchesList;
    }

    public List<Organization> fetchUserAssignedBranches() {
        String query =
            "begin ? := tqc_roles_cursor.Get_user_assigned_Orgs(?); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                List<Region> regionsList = new ArrayList<Region>();
                Organization org = new Organization();
                org.setOrgCode(rs.getBigDecimal(1));
                org.setOrgShortDesc(rs.getString(2));
                org.setOrgName(rs.getString(3));
                org.setNodeType("P");

                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.Get_User_assigned_Regions(?,?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.setBigDecimal(3,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
                stmt2.execute();
                OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                while (rs2.next()) {
                    List<Branch> branchesList = new ArrayList<Branch>();
                    Region region = new Region();
                    region.setRegionCode(rs2.getBigDecimal(1));
                    region.setRegOrgCode(rs2.getBigDecimal(2));
                    region.setRegionName(rs2.getString(3));
                    region.setNodeType("S");

                    OracleCallableStatement stmt3 =
                        (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_branches(?,?); end;");

                    stmt3.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt3.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                    stmt3.setBigDecimal(3, rs2.getBigDecimal(1));
                    stmt3.execute();
                    OracleResultSet rs3 = (OracleResultSet)stmt3.getObject(1);

                    while (rs3.next()) {
                        Branch branch = new Branch();
                        branch.setBranchCode(rs3.getBigDecimal(1)); // Branch code
                        branch.setRegCode(rs3.getBigDecimal(3)); // Region Code
                        branch.setBranchName(rs3.getString(4)); // Branch name
                        branch.setNodeType("SB");
                        branchesList.add(branch);
                    }
                    region.setBranchesList(branchesList);
                    regionsList.add(region);
                }
                org.setRegionsList(regionsList);
                orgList.add(org);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }

    public List<Organization> fetchUserUnassignedDivisions() {
        String query =
            "begin ? := tqc_roles_cursor.Get_user_assigned_Orgs(?); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                List<Region> regionsList = new ArrayList<Region>();
                Organization org = new Organization();
                org.setOrgCode(rs.getBigDecimal(1));
                org.setOrgShortDesc(rs.getString(2));
                org.setOrgName(rs.getString(3));
                org.setNodeType("P");

                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_regions(?,?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.setBigDecimal(3,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
                stmt2.execute();
                OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                while (rs2.next()) {
                    List<Branch> branchesList = new ArrayList<Branch>();
                    Region region = new Region();
                    region.setRegionCode(rs2.getBigDecimal(1));
                    region.setRegOrgCode(rs2.getBigDecimal(2));
                    region.setRegionName(rs2.getString(3));
                    region.setNodeType("S");

                    OracleCallableStatement stmt3 =
                        (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_branches(?,?); end;");

                    stmt3.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt3.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                    stmt3.setBigDecimal(3, rs2.getBigDecimal(1));
                    stmt3.execute();
                    OracleResultSet rs3 = (OracleResultSet)stmt3.getObject(1);

                    while (rs3.next()) {
                        List<Division> divisionsList =
                            new ArrayList<Division>();
                        Branch branch = new Branch();
                        branch.setBranchCode(rs3.getBigDecimal(1));
                        branch.setRegCode(rs3.getBigDecimal(3));
                        branch.setBranchName(rs3.getString(4));
                        branch.setNodeType("SB");

                        OracleCallableStatement stmt4 =
                            (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_unassigned_divisions(?,?); end;");

                        stmt4.registerOutParameter(1, OracleTypes.CURSOR);
                        stmt4.setBigDecimal(2,
                                            (BigDecimal)session.getAttribute("sysUserCode"));
                        stmt4.setBigDecimal(3, rs3.getBigDecimal(1));
                        stmt4.execute();
                        OracleResultSet rs4 =
                            (OracleResultSet)stmt4.getObject(1);

                        while (rs4.next()) {
                            Division division = new Division();
                            division.setDIV_CODE(rs4.getBigDecimal(1));
                            division.setDIV_NAME(rs4.getString(2));
                            division.setDIV_SHT_DESC(rs4.getString(3));
                            division.setDIV_DIVISION_STATUS(rs4.getString(4));
                            division.setNodeType("SBB");
                            divisionsList.add(division);
                        }

                        branch.setDivisionsList(divisionsList);
                        branchesList.add(branch);
                    }
                    region.setBranchesList(branchesList);
                    regionsList.add(region);
                }
                org.setRegionsList(regionsList);
                orgList.add(org);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }

    public List<Organization> fetchUserAssignedDivisions() {
        String query =
            "begin ? := tqc_roles_cursor.get_user_assigned_div_orgs(?); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                List<Region> regionsList = new ArrayList<Region>();
                Organization org = new Organization();
                org.setOrgCode(rs.getBigDecimal(1));
                org.setOrgShortDesc(rs.getString(2));
                org.setOrgName(rs.getString(3));
                org.setNodeType("P");

                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_div_regions(?,?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.setBigDecimal(3,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
                stmt2.execute();
                OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                while (rs2.next()) {
                    List<Branch> branchesList = new ArrayList<Branch>();
                    Region region = new Region();
                    region.setRegionCode(rs2.getBigDecimal(1));
                    region.setRegOrgCode(rs2.getBigDecimal(2));
                    region.setRegionName(rs2.getString(3));
                    region.setNodeType("S");

                    OracleCallableStatement stmt3 =
                        (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_div_branches(?,?); end;");

                    stmt3.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt3.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                    stmt3.setBigDecimal(3, rs2.getBigDecimal(1));
                    stmt3.execute();
                    OracleResultSet rs3 = (OracleResultSet)stmt3.getObject(1);

                    while (rs3.next()) {
                        List<Division> divisionsList =
                            new ArrayList<Division>();
                        Branch branch = new Branch();
                        branch.setBranchCode(rs3.getBigDecimal(1)); // Branch code
                        branch.setRegCode(rs3.getBigDecimal(3)); // Region Code
                        branch.setBranchName(rs3.getString(4)); // Branch name
                        branch.setNodeType("SB");

                        OracleCallableStatement stmt4 =
                            (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_divisions(?,?); end;");

                        stmt4.registerOutParameter(1, OracleTypes.CURSOR);
                        stmt4.setBigDecimal(2,
                                            (BigDecimal)session.getAttribute("sysUserCode"));
                        stmt4.setBigDecimal(3, rs3.getBigDecimal(1));
                        stmt4.execute();

                        OracleResultSet rs4 =
                            (OracleResultSet)stmt4.getObject(1);

                        while (rs4.next()) {
                            Division division = new Division();
                            division.setDIV_CODE(rs4.getBigDecimal(1));
                            division.setDIV_NAME(rs4.getString(2));
                            division.setDIV_SHT_DESC(rs4.getString(3));
                            division.setDIV_DIVISION_STATUS(rs4.getString(4));
                            division.setNodeType("SBB");
                            divisionsList.add(division);
                        }

                        branch.setDivisionsList(divisionsList);
                        branchesList.add(branch);
                        rs4.close();
                    }
                    region.setBranchesList(branchesList);
                    regionsList.add(region);

                    rs3.close();
                }
                org.setRegionsList(regionsList);
                orgList.add(org);
                rs2.close();

            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }

    public List<Division> fetchOrgDivisions() {
        List<Division> divisionsList = new ArrayList<Division>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_WEB_CURSOR.get_org_divisions(?); end;";

        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);


            if (session.getAttribute("ORGCode") == null) {
                return null;
            } else {
                callStmt.setObject(2, session.getAttribute("ORGCode"));

                callStmt.execute();

                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {

                    Division DivDef = new Division();

                    DivDef.setDIV_CODE(rs.getBigDecimal(1));
                    DivDef.setDIV_NAME(rs.getString(2));
                    DivDef.setDIV_SHT_DESC(rs.getString(3));
                    DivDef.setDIV_DIVISION_STATUS(rs.getString(4));
                    DivDef.setDivOrder(rs.getString(5));
                    DivDef.setDivDirectorCode(rs.getString(6));
                    DivDef.setDivDirectorName(rs.getString(7));
                    DivDef.setDivAssDirectorCode(rs.getString(8));
                    DivDef.setDivAssDirectorName(rs.getString(9));
                    divisionsList.add(DivDef);

                }
                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            }
            //session.removeAttribute("ORGCode");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divisionsList;
    }

    public List<BranchName> fetchBranchNames() {
        List<BranchName> bnsList = new ArrayList<BranchName>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_SETUPS_CURSOR.getBranchNames(); end;";

        try {

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                BranchName bns = new BranchName();
                bns.setBns_code(rs.getBigDecimal(1));
                bns.setBns_sht_desc(rs.getString(2));
                bns.setBns_name(rs.getString(3));
                bns.setBns_phy_addrs(rs.getString(4));
                bns.setBns_email_addrs(rs.getString(6));
                bns.setBns_post_addrs(rs.getString(5));
                bns.setBns_twn_code(rs.getBigDecimal(7));
                bns.setBns_cou_code(rs.getBigDecimal(8));
                bns.setBns_contact(rs.getString(9));
                bns.setBns_manager(rs.getString(10));
                bns.setBns_tel(rs.getString(11));
                bns.setBns_fax(rs.getString(12));
                bns.setState_code(rs.getBigDecimal(13));
                bns.setTownName(rs.getString(14));
                bns.setCountryName(rs.getString(15));
                bns.setStateName(rs.getString(16));
                bns.setAccType(rs.getString(17));
                bns.setRegion(rs.getString(18));
                bns.setBnsPostalCod(rs.getBigDecimal("BNS_POST_CODE"));
                bnsList.add(bns);
            }


            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return bnsList;
    }


    public List<Agent> fetchFaAgents() {
        List<Agent> bnsList = new ArrayList<Agent>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_SETUPS_CURSOR.getFaAgents(); end;";

        try {

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Agent bns = new Agent();
                bns.setFa_agn_org_code(rs.getBigDecimal(1));
                bns.setFa_agn_sht_desc(rs.getString(2));
                bns.setFa_team_code(rs.getBigDecimal(3));
                bns.setFa_agn_name(rs.getString(4));
                bns.setFa_agent_code(rs.getBigDecimal(5));
                bnsList.add(bns);
            }


            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return bnsList;
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

    public List<District> fetchDistrictsByState() {
        List<District> districtsList = new ArrayList<District>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getdistrictsByState(?);end;";
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
                    District dist = new District();
                    dist.setCode(resultSet.getString(1));
                    dist.setCountryCode(resultSet.getString(2));
                    dist.setShortDesc(resultSet.getString(3));
                    dist.setName(resultSet.getString(4));
                    dist.setSTSCode(resultSet.getString(5));
                    districtsList.add(dist);
                }
                statement.close();
                resultSet.close();
                connection.close();
            } else {
                if (session.getAttribute("countryCode") == null) {
                    return districtsList;
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

                        District dist = new District();
                        dist.setCode(rs.getString(1));
                        dist.setCountryCode(rs.getString(2));
                        dist.setShortDesc(rs.getString(3));
                        dist.setName(rs.getString(4));
                        dist.setSTSCode(rs.getString(5));
                        districtsList.add(dist);

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
        return districtsList;
    }


    public List<Town> fetchTownsWithZipCodesByStateDir() {
        List<Town> townsList = new ArrayList<Town>();

        String query =
            "SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name,\n" +
            "                  twn_sts_code, pst_desc, pst_zip_code\n" +
            "             FROM tqc_towns, tqc_postal_codes\n" +
            "            WHERE \n" +
            "            twn_code=PST_TWN_CODE(+)  " +
            "           AND twn_sts_code =nvl(:v_state_code,twn_sts_code)  \n" +
            "         ORDER BY twn_name";

        try {
            BigDecimal stateCode =
                GlobalCC.checkBDNullValues(session.getAttribute("stateCode"));
            query =
                    query.replaceAll(":v_state_code", stateCode != null ? stateCode.toString() :
                                                      "null");
            System.out.println("query: " + query);
            IQuery.fetchResult(query, townsList, new IQueryAction() {
                    public void fetch(ResultSet rs, Object o) {
                        try {
                            while (rs.next()) {
                                List<Town> townsList = (List<Town>)o;
                                Town town = new Town();
                                town.setCode(rs.getString(1));
                                town.setCountryCode(rs.getString(2));
                                town.setShortDesc(rs.getString(3));
                                town.setName(rs.getString(4));
                                town.setSTSCode(rs.getString(5));
                                town.setPostalDesc(rs.getString(6));
                                town.setPostalZipCode(rs.getString(7));
                                townsList.add(town);
                                System.out.println("Code: " + town.getCode() +
                                                   " Name: " + town.getName());
                            }
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(e);
                        }
                    }
                });
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return townsList;
    }

    public List<Town> fetchTownsWithZipCodesByState() {
        List<Town> townsList = new ArrayList<Town>();

        String query =
            "SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name,\n" +
            "                  twn_sts_code, pst_desc, pst_zip_code\n" +
            "             FROM tqc_towns, tqc_postal_codes\n" +
            "            WHERE \n" +
            "            twn_code=PST_TWN_CODE(+)  " +
            "           AND twn_sts_code =nvl(:v_state_code,twn_sts_code)  \n" +
            "         ORDER BY twn_name";

        try {
            BigDecimal stateCode =
                GlobalCC.checkBDNullValues(session.getAttribute("stateCode"));
            query =
                    query.replaceAll(":v_state_code", stateCode != null ? stateCode.toString() :
                                                      "null");
            System.out.println("query: " + query);
            IQuery.fetchResult(query, townsList, new IQueryAction() {
                    public void fetch(ResultSet rs, Object o) {
                        try {
                            while (rs.next()) {
                                List<Town> townsList = (List<Town>)o;
                                Town town = new Town();
                                town.setCode(rs.getString(1));
                                town.setCountryCode(rs.getString(2));
                                town.setShortDesc(rs.getString(3));
                                town.setName(rs.getString(4));
                                town.setSTSCode(rs.getString(5));
                                town.setPostalDesc(rs.getString(6));
                                town.setPostalZipCode(rs.getString(7));
                                townsList.add(town);
                                System.out.println("Code: " + town.getCode() +
                                                   " Name: " + town.getName());
                            }
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(e);
                        }
                    }
                });
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return townsList;
    }


    public List<Town> fetchTownsWithZipCodesByStateSign() {
        List<Town> townsList = new ArrayList<Town>();

        String query =
            "SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name,\n" +
            "                  twn_sts_code, pst_desc, pst_zip_code\n" +
            "             FROM tqc_towns, tqc_postal_codes\n" +
            "            WHERE \n" +
            "            twn_code=PST_TWN_CODE(+)  " +
            "           AND twn_sts_code =nvl(:v_state_code,twn_sts_code)  \n" +
            "         ORDER BY twn_name";

        try {
            BigDecimal stateCode1 =
                GlobalCC.checkBDNullValues(session.getAttribute("stateCodeClnt"));
            query =
                    query.replaceAll(":v_state_code", stateCode1 != null ? stateCode1.toString() :
                                                      "null");
            System.out.println("query: " + query);
            IQuery.fetchResult(query, townsList, new IQueryAction() {
                    public void fetch(ResultSet rs, Object o) {
                        try {
                            while (rs.next()) {
                                List<Town> townsList = (List<Town>)o;
                                Town town = new Town();
                                town.setCode(rs.getString(1));
                                town.setCountryCode(rs.getString(2));
                                town.setShortDesc(rs.getString(3));
                                town.setName(rs.getString(4));
                                town.setSTSCode(rs.getString(5));
                                town.setPostalDesc(rs.getString(6));
                                town.setPostalZipCode(rs.getString(7));
                                townsList.add(town);
                                System.out.println("Code: " + town.getCode() +
                                                   " Name: " + town.getName());
                            }
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(e);
                        }
                    }
                });
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return townsList;
    }

    public List<Town> fetchTownsWithZipCodesByStateClnt() {
        List<Town> townsList = new ArrayList<Town>();

        String query =
            "SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name,\n" +
            "                  twn_sts_code, pst_desc, pst_zip_code\n" +
            "             FROM tqc_towns, tqc_postal_codes\n" +
            "            WHERE \n" +
            "            twn_code=PST_TWN_CODE(+)  " +
            "           AND twn_sts_code =nvl(:v_state_code,twn_sts_code)  \n" +
            "         ORDER BY twn_name";

        try {
            BigDecimal stateCode1 =
                GlobalCC.checkBDNullValues(session.getAttribute("stateCode1"));
            query =
                    query.replaceAll(":v_state_code", stateCode1 != null ? stateCode1.toString() :
                                                      "null");
            System.out.println("query: " + query);
            IQuery.fetchResult(query, townsList, new IQueryAction() {
                    public void fetch(ResultSet rs, Object o) {
                        try {
                            while (rs.next()) {
                                List<Town> townsList = (List<Town>)o;
                                Town town = new Town();
                                town.setCode(rs.getString(1));
                                town.setCountryCode(rs.getString(2));
                                town.setShortDesc(rs.getString(3));
                                town.setName(rs.getString(4));
                                town.setSTSCode(rs.getString(5));
                                town.setPostalDesc(rs.getString(6));
                                town.setPostalZipCode(rs.getString(7));
                                townsList.add(town);
                                System.out.println("Code: " + town.getCode() +
                                                   " Name: " + town.getName());
                            }
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(e);
                        }
                    }
                });
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return townsList;
    }


    public List<Town> fetchTowns() {
        List<Town> townsList = new ArrayList<Town>();
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.get_All_towns;end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.registerOutParameter(1,
                                           oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {

                Town town = new Town();
                town.setCode(resultSet.getString(1));
                town.setCountryCode(resultSet.getString(2));
                town.setShortDesc(resultSet.getString(3));
                town.setName(resultSet.getString(4));
                townsList.add(town);
            }
            statement.close();
            resultSet.close();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return townsList;
    }

    public List<Organization> fetchDestLovItems() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;


        List<Organization> itemsData = new ArrayList<Organization>();

        try {
            conn = dbCon.getDatabaseConnection();
            String query =
                "select org_code,org_name from tqc_organizations\n" +
                " where 'ROT' = :v_trans_type\n " + "union\n" +
                " select reg_code,reg_name from tqc_regions\n" +
                "  where 'BRT' = :v_trans_type\n" +
                "union\n" +
                " select brn_code, brn_name from tqc_branches\n" +
                "  where 'ABT' = :v_trans_type \n" +
                "union\n" +
                "  select bra_code, bra_name from tqc_branch_agencies\n" +
                "   where 'UAT'= :v_trans_type  \n" +
                "union\n" +
                " select bru_code,bru_name from tqc_branch_units\n" +
                "  where 'AUT' = :v_trans_type" +
                "union\n" +
                "select brn_code,brn_name from tqc_branches\n" + 
                "  where 'UBT' = :v_trans_type ";

            query =
                    query.replaceAll(":v_trans_type", "'" + session.getAttribute("tt_trans_type") +
                                     "'");
            System.out.println("sql: " + query);
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            rs = (OracleResultSet)callStmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Organization item = new Organization();
                    item.setDestCode(rs.getBigDecimal(1));
                    item.setDestName(rs.getString(2));
                    itemsData.add(item);
                }
                rs.close();
            }
            System.out.println("records: " + itemsData.size());
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return itemsData;
    }

    public List<Organization> fetchOrgBankNames() {
        List<Organization> items = new ArrayList<Organization>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        String query = "select bnk_code,bnk_bank_name from tqc_banks";

        try {
            conn = dbCon.getDatabaseConnection();
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            rs = (OracleResultSet)callStmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    Organization item = new Organization();
                    item.setBnkCode(rs.getBigDecimal(1));
                    item.setBnkName(rs.getString(2));
                    items.add(item);
                }
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return items;
    }

    public List<Organization> fetchOrgBankBranches() {
        List<Organization> items = new ArrayList<Organization>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        String query =
            "select bbr_code,bbr_branch_name from tqc_bank_branches\n" +
            " where bbr_bnk_code = ?";

        try {
            conn = dbCon.getDatabaseConnection();
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setBigDecimal(1,
                                   GlobalCC.checkBDNullValues(session.getAttribute("ORG_BNK_CODE")));
            rs = (OracleResultSet)callStmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Organization item = new Organization();
                    item.setBbrCode(rs.getBigDecimal(1));
                    item.setBbrName(rs.getString(2));
                    items.add(item);
                }
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return items;
    }

    public List<Organization> findAgentIntermediaries() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        OracleCallableStatement callStmt = null;

        List<Organization> AgnIntermediaries = new ArrayList<Organization>();
        String query =
            "begin TQ_LMS.lms_web_cursor_setup2.agent_intermediaries(?,?); end;";

        try {
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   GlobalCC.checkBDNullValues(session.getAttribute("actCode")));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Organization container = new Organization();
                container.setIntCode(rs.getBigDecimal(1));
                container.setIntActCode(rs.getBigDecimal(2));
                container.setIntActType(rs.getString(3));
                container.setIntType(rs.getString(4));
                container.setIntName(rs.getString(5));
                container.setIntDesc(rs.getString(6));
                container.setIntPhysicalAddr(rs.getString(7));
                container.setIntPostalAddr(rs.getString(8));
                container.setIntTelNo(rs.getString(9));
                container.setIntFax(rs.getString(10));
                container.setIntMobile(rs.getString(11));
                container.setIntBank(rs.getString(12));
                container.setIntBbrCode(rs.getBigDecimal(13));
                container.setIntBankAccNo(rs.getString(14));
                container.setIntBankAccName(rs.getString(15));
                container.setIntbankAccType(rs.getString(16));
                container.setIntWef(rs.getDate(17));
                container.setIntWet(rs.getDate(18));


                AgnIntermediaries.add(container);
            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return AgnIntermediaries;
    }
}
