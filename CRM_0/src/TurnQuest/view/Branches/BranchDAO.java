package TurnQuest.view.Branches;


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


public class BranchDAO {
    public BranchDAO() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Branch> findOrganizations() {

        String query = "begin ? := tqc_roles_cursor.GetOrganizations(); end;";
        OracleCallableStatement cst = null;
        List<Branch> organizations = new ArrayList<Branch>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            organizations = new ArrayList<Branch>();
            int k;
            k = 0;
            while (rs.next()) {
                Branch branch = new Branch();
                branch.setOrgCode(rs.getBigDecimal(1));
                branch.setOrgShtDesc(rs.getString(2));
                branch.setOrgName(rs.getString(3));
                organizations.add(branch);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return organizations;
    }

    public List<Organization> fetchUserUnassignedBranches() {
        String query = "begin ? := tqc_roles_cursor.GetOrganizations(); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
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
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.GetRegions(?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
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
                        branch.setBranchCode(rs.getBigDecimal(1)); // Branch code
                        branch.setRegCode(rs.getBigDecimal(3)); // Region Code
                        branch.setBranchName(rs.getString(4)); // Branch name
                        branch.setNodeType("SB");
                        branchesList.add(branch);
                    }
                    rs3.close();
                    stmt3.close();
                    region.setBranchesList(branchesList);
                    regionsList.add(region);
                }
                rs2.close();
                stmt2.close();
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

    public List<Organization> fetchUserAssignedBranches() {
        String query = "begin ? := tqc_roles_cursor.GetOrganizations(); end;";
        OracleCallableStatement cst = null;
        List<Organization> orgList = new ArrayList<Organization>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
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
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.GetRegions(?); end;");

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
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
                        branch.setBranchCode(rs.getBigDecimal(1)); // Branch code
                        branch.setRegCode(rs.getBigDecimal(3)); // Region Code
                        branch.setBranchName(rs.getString(4)); // Branch name
                        branch.setNodeType("SB");
                        branchesList.add(branch);
                    }
                    region.setBranchesList(branchesList);
                    regionsList.add(region);
                    rs3.close();
                    stmt3.close();
                }
                org.setRegionsList(regionsList);
                orgList.add(org);
                stmt2.close();
                rs2.close();
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return orgList;
    }

    public List<Branch> findRegions() {

        String query = "begin ? := tqc_roles_cursor.GetRegions(?); end;";
        OracleCallableStatement cst = null;
        List<Branch> regions = new ArrayList<Branch>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("orgCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            regions = new ArrayList<Branch>();
            int k;
            k = 0;
            while (rs.next()) {
                Branch branch = new Branch();
                branch.setRegCode(rs.getBigDecimal(1));
                branch.setRegOrgCode(rs.getBigDecimal(2));
                branch.setRegName(rs.getString(3));
                regions.add(branch);
            }
            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return regions;
    }

    public List<Branch> fetchAllBranches() {

        String query = "begin ? := tqc_roles_cursor.GetBranches(?); end;";
        OracleCallableStatement cst = null;
        List<Branch> branchesList = new ArrayList<Branch>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("regCode"));
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            branchesList = new ArrayList<Branch>();
            int k;
            k = 0;
            while (rs.next()) {
                Branch branch = new Branch();
                branch.setRegCode(rs.getBigDecimal(1));
                branch.setRegOrgCode(rs.getBigDecimal(3));
                branch.setRegName(rs.getString(4));
                branchesList.add(branch);
            }
            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return branchesList;
    }

    public List<Branch> findUserBranches() {

        String query =
            "begin ? := tqc_roles_cursor.get_user_branches(?); end;";
        OracleCallableStatement cst = null;
        List<Branch> regions = new ArrayList<Branch>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));

            /*if(session.getAttribute("branch_code") == null) {
                cst.setBigDecimal(3,null);
            }else{
                cst.setBigDecimal(3, (BigDecimal)session.getAttribute("branch_code"));
            }*/
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            regions = new ArrayList<Branch>();
            int k;
            k = 0;
            while (rs.next()) {
                Branch branch = new Branch();
                branch.setUsrBranchCode(rs.getBigDecimal(1));
                branch.setBranchCode(rs.getBigDecimal(2));
                branch.setUsrCode(rs.getBigDecimal(3));
                branch.setUsrStatus(rs.getString(4));
                branch.setUsrDftBranch(rs.getString(5));
                branch.setBranchName(rs.getString(6));
                regions.add(branch);
            }
            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return regions;
    }
}
