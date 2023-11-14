package TurnQuest.view.Divisions;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.orgs.Organization;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class DivisionsDAO {
    public DivisionsDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Division> findDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_WEB_CURSOR.getDivisions(?); end;";

        List<Division> orgData = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setString(2, null);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setDIV_CODE(rs.getBigDecimal(1));
                DivDef.setDIV_NAME(rs.getString(2));
                DivDef.setDIV_SHT_DESC(rs.getString(3));
                DivDef.setDIV_DIVISION_STATUS(rs.getString(4));
                DivDef.setDivOrder(rs.getString(5));
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

    public List<Organization> findDivisionsByOrg() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        Organization orgDefin = new Organization();
        
        
        String query = "begin TQC_SETUPS_CURSOR.organizations(?,?); end;";

        List<Organization> orgData = new ArrayList<Organization>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2, null);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                List<Division> divisionsList = new ArrayList<Division>();
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
                orgDef.setOrgAvatar(rs.getBlob(26));

                orgDef.setCurSymbol(rs.getString(27));
                orgDef.setCurDesc(rs.getString(28));
                orgDef.setCouName(rs.getString(29));
                orgDef.setTwnName(rs.getString(30));

                BigDecimal orgCde=rs.getBigDecimal(1);
                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := TQC_WEB_CURSOR.get_org_divisions(?); end;");


                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, orgCde);
                stmt2.execute();
                OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                while (rs2.next()) {
                    Division DivDef = new Division();
                    DivDef.setOrgCode(rs.getBigDecimal(1));
                    DivDef.setDIV_CODE(rs2.getBigDecimal(1));
                    DivDef.setDIV_NAME(rs2.getString(2));
                    DivDef.setDIV_SHT_DESC(rs2.getString(3));
                    DivDef.setDIV_DIVISION_STATUS(rs2.getString(4));
                    DivDef.setNodeType("S");

                    divisionsList.add(DivDef);


                }
                rs2.close();
                stmt2.close();
                orgDef.setDivisionList(divisionsList);
                orgData.add(orgDef);
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


    /* String query = "begin ? := tqc_roles_cursor.GetOrganizations(); end;";
        OracleCallableStatement  cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();

            OracleResultSet  rs = (OracleResultSet )cst.getObject(1);

            while (rs.next()) {
                List<Division> divisionsList = new ArrayList<Division>();
                Organization org = new Organization();
                org.setOrgCode(rs.getBigDecimal(1));
                org.setOrgShortDesc(rs.getString(2));
                org.setOrgName(rs.getString(3));
                org.setNodeType("P");

                OracleCallableStatement  stmt2 =
                    conn.prepareCall("begin ? := TQC_WEB_CURSOR.get_org_divisions(?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, org.getOrgCode());
                stmt2.execute();
                OracleResultSet  rs2 = (OracleResultSet )stmt2.getObject(1);

                while (rs2.next()) {
                    Division DivDef = new Division();

                    DivDef.setDIV_CODE(rs.getBigDecimal(1));
                    DivDef.setDIV_NAME(rs.getString(2));
                    DivDef.setDIV_SHT_DESC(rs.getString(3));
                    DivDef.setDIV_DIVISION_STATUS(rs.getString(4));
                    DivDef.setNodeType("S");

                    divisionsList.add(DivDef);
                }

                org.setDivisionList(divisionsList);
                orgList.add(org);
            }
            rs.close();
            cst.close();
            //conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }*/

    public List<Division> findSubDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_WEB_CURSOR.getSubDivisions(?); end;";

        List<Division> orgData = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);

            BigDecimal divCode =
                (BigDecimal)session.getAttribute("divisionCode");

            if (divCode == null) {
                return null;
            } else {
                callStmt.setBigDecimal(2, divCode);
                callStmt.execute();

                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {

                    Division DivDef = new Division();

                    DivDef.setSDIV_CODE(rs.getBigDecimal(1));
                    DivDef.setSDIV_NAME(rs.getString(2));
                    DivDef.setSDIV_SHT_DESC(rs.getString(3));
                    DivDef.setSDIV_DIV_CODE(rs.getBigDecimal(4));

                    orgData.add(DivDef);

                }

                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            }

            //session.removeAttribute("divisionCode");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgData;
    }


    public List<Division> findBranchDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_WEB_CURSOR.getbranchDivisions(?); end;";

        List<Division> orgData = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("divisionCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setBDIV_CODE(rs.getBigDecimal(1));
                DivDef.setBRN_NAME(rs.getString(2));
                DivDef.setDIV_NAME(rs.getString(3));
                DivDef.setBDIV_WEF(rs.getDate(4));
                DivDef.setBDIV_WET(rs.getDate(4));

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

    public List<Division> fetchDivisionsByBranch() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_WEB_CURSOR.getDivisionsByBrnch(?); end;";

        List<Division> orgData = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("BRNCode"));

            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setBDIV_CODE(rs.getBigDecimal(1));
                DivDef.setBRN_NAME(rs.getString(2));
                DivDef.setDIV_NAME(rs.getString(3));
                DivDef.setBDIV_WEF(rs.getDate(4));
                DivDef.setBDIV_WET(rs.getDate(5));
                DivDef.setDIV_CODE(rs.getBigDecimal(6));

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

    public List<Division> findBranchUnassignedDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query =
            "begin ? := TQC_WEB_CURSOR.get_brn_unassgnd_div(?,?); end;";

        List<Division> divList = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);

            BigDecimal orgCode = (BigDecimal)session.getAttribute("ORGCode");
            BigDecimal branchCode =
                (BigDecimal)session.getAttribute("BRNCode");

            if (orgCode == null || branchCode == null) {
                return null;
            } else {

                callStmt.setBigDecimal(2, orgCode);
                callStmt.setBigDecimal(3, branchCode);
                callStmt.execute();

                OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

                while (rs.next()) {

                    Division DivDef = new Division();

                    DivDef.setDIV_CODE(rs.getBigDecimal(1));
                    DivDef.setDIV_NAME(rs.getString(2));
                    DivDef.setDIV_SHT_DESC(rs.getString(3));
                    DivDef.setDIV_DIVISION_STATUS(rs.getString(4));

                    divList.add(DivDef);

                }
                rs.close();
                callStmt.close();
                conn.commit();
                conn.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divList;
    }

    public List<Division> findBranches() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin TQC_COMMON_CURSORS.getorgbranches(?,?); end;";

        List<Division> orgData = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, null);
            callStmt.registerOutParameter(2, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(2);

            while (rs.next()) {

                Division DivDef = new Division();

                DivDef.setBRN_CODE(rs.getBigDecimal(1));
                DivDef.setBRN_NAME(rs.getString(4));

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

    public List<Division> fetchUserDivisions() {

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_WEB_CURSOR.get_user_divisions(?); end;";

        List<Division> userDivisions = new ArrayList<Division>();
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("sysUserCode"));
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {

                Division userDiv = new Division();

                userDiv.setUserDiv_code(rs.getBigDecimal(1));
                userDiv.setUserDiv_userCode(rs.getBigDecimal(2));
                userDiv.setUserDiv_divCode(rs.getBigDecimal(3));
                userDiv.setDIV_CODE(rs.getBigDecimal(4));
                userDiv.setDIV_NAME(rs.getString(5));
                userDiv.setDIV_SHT_DESC(rs.getString(6));
                userDiv.setDIV_DIVISION_STATUS(rs.getString(7));
                userDiv.setUserDiv_default(rs.getString(8));

                userDivisions.add(userDiv);

            }
            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return userDivisions;
    }
}
