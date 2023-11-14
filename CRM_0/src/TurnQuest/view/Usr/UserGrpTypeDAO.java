package TurnQuest.view.Usr;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Branches.Branch;
import TurnQuest.view.Connect.DBConnector;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class UserGrpTypeDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public UserGrpTypeDAO() {
        super();
    }
    
    public List<GroupType> fetchAllGroupTypes(){
        List<GroupType> groupTypes = new ArrayList<GroupType>();
        
        GroupType groupType = new GroupType();
        groupType.setTypeId("BST");
        groupType.setTypeName("Business Service Team");
        groupTypes.add(groupType);
        
        GroupType groupType1 = new GroupType();
        groupType1.setTypeId("BDE");
        groupType1.setTypeName("Business Development Executives");
        groupTypes.add(groupType1);
        
        GroupType groupType2 = new GroupType();
        groupType2.setTypeId("UW");
        groupType2.setTypeName("Underwriter");
        groupTypes.add(groupType2);
        
        GroupType groupType3 = new GroupType();
        groupType3.setTypeId("CL");
        groupType3.setTypeName("Claims");
        groupTypes.add(groupType3);
        
        GroupType groupType4 = new GroupType();
        groupType4.setTypeId("RN");
        groupType4.setTypeName("Reinsurance");
        groupTypes.add(groupType4);
        
        GroupType groupType5 = new GroupType();
        groupType5.setTypeId("MK");
        groupType5.setTypeName("Marketers");
        groupTypes.add(groupType5);
        
        return groupTypes;
    }

    public List<UserGrpType> fetchUserGroupTypes() {
        String query =
            "begin ? := tqc_setups_cursor.getusergrouptypes(); end;";
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;
        List<UserGrpType> list = new ArrayList<UserGrpType>();
        DBConnector dataHandler = new DBConnector();
        try {

            conn = dataHandler.getDatabaseConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(query);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.execute();
            OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);

            while (rs.next()) {
                UserGrpType userGrp = new UserGrpType();
                userGrp.setCode(rs.getInt(1));
                userGrp.setShortDesc(rs.getString(2));
                userGrp.setGroupType(rs.getString(3));
                userGrp.setIdSerialFormat(rs.getString(4));
                userGrp.setTypeId(rs.getString(5));
                userGrp.setBranchCode(rs.getBigDecimal(6));
                userGrp.setBranchName(rs.getString(7));

                list.add(userGrp);
            }

            cstmt.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return list;
    }


    public List<MyAccount> fetchAccountGroupTypes() {
        List<MyAccount> accounts = new ArrayList<MyAccount>();

        if (session.getAttribute("groupTypeUserCode") != null) {
            Object grpTypeUsrCode = session.getAttribute("groupTypeUserCode");
            String query =
                "begin ? := tqc_setups_cursor.getgrouptypesaccounts(?); end;";
            OracleConnection conn = null;
            OracleCallableStatement cstmt = null;
            DBConnector dbHandler = new DBConnector();
            try {
                conn = dbHandler.getDatabaseConnection();

                cstmt = (OracleCallableStatement)conn.prepareCall(query);
                cstmt.registerOutParameter(1, OracleTypes.CURSOR);
                cstmt.setInt(2, (Integer)grpTypeUsrCode);
                cstmt.execute();
                OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);
                while (rs.next()) {
                    MyAccount account = new MyAccount();
                    account.setCode(rs.getBigDecimal(1));
                    account.setGrptUsrCode(rs.getBigDecimal(2));
                    account.setType(rs.getString(3));
                    account.setAccType(rs.getString(4));
                    account.setAgnCode(rs.getBigDecimal(5));
                    account.setAgnName(rs.getString(6));
                    account.setSelect(false);

                    accounts.add(account);
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return accounts;
    }

    public List<User> findGroupUserTypesMembers() {
        List<User> users = new ArrayList<User>();
        if (session.getAttribute("groupTypeUserCode") != null) {
            Object grpTypeUsrCode = session.getAttribute("groupTypeUserCode");

            String query =
                "begin ? := TQC_SETUPS_CURSOR.getgrouptypesmembers(?); end;";
            OracleConnection conn = null;
            OracleCallableStatement cstmt = null;
            DBConnector dbHandler = new DBConnector();

            try {
                conn = dbHandler.getDatabaseConnection();

                cstmt = (OracleCallableStatement)conn.prepareCall(query);
                cstmt.registerOutParameter(1, OracleTypes.CURSOR);
                cstmt.setInt(2, (Integer)grpTypeUsrCode);
                cstmt.execute();

                // ResultSet
                OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);
                while (rs.next()) {
                    User user = new User();

                    user.setUserCode(rs.getBigDecimal(1));
                    user.setUsername(rs.getString(2));
                    user.setUserFullname(rs.getString(3));
                    user.setUserEmail(rs.getString(4));
                    user.setGtUsrcode(rs.getBigDecimal(5));
                    user.setGtUsrGrptUsrCode(rs.getBigDecimal(6));
                    
                     String teamLeader = rs.getString(7);
                    if(teamLeader.equalsIgnoreCase("Y")){
                        teamLeader = "YES";
                    } else if (teamLeader.equalsIgnoreCase("N")){
                        teamLeader = "NO";
                    } else {
                        teamLeader = "INVALID";
                    }
                    user.setTeamLeader(teamLeader);
                    user.setUserSelected(false);

                    users.add(user);
                }

                rs.close();
                cstmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return users;
    }

    public List<User> findAllUsersNotInGroupType() {
        List<User> users = new ArrayList<User>();

        String query =
            "begin TQC_ROLES_CURSOR.get_usersnotingrouptype(?, ?); end;";
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;
        DBConnector dbHandler = new DBConnector();

        try {
            conn = dbHandler.getDatabaseConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(query);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setObject(2, session.getAttribute("groupTypeUserCode"));
            cstmt.execute();
            OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);

            while (rs.next()) {
                User user = new User();
                user.setUserCode(rs.getBigDecimal(1));
                user.setUsername(rs.getString(2));
                user.setUserFullname(rs.getString(3));
                user.setUserEmail(rs.getString(4));
                user.setUserPersonnelRank(rs.getString(5));
                user.setUserDateCreated(rs.getDate(6));
                user.setUserType(GlobalCC.decodeUserType(rs.getString(7)));
                user.setUserStatus(GlobalCC.decodeUserStatus(rs.getString(8)));
                user.setUserPassReset(GlobalCC.decodeReset(rs.getString(9)));
                user.setUserPerId(rs.getString(10));
                user.setUserSelected(false);
                users.add(user);
            }

            rs.close();
            cstmt.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return users;
    }

    public List<MyAccount> findAllAccountsNotInGroupTypes() {
        List<MyAccount> accounts = new ArrayList<MyAccount>();
        //766890
        String query =
            "begin tqc_roles_cursor.get_accountsnotingrouptype(?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;
        DBConnector dbHandler = new DBConnector();
        try {
            conn = dbHandler.getDatabaseConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(query);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setObject(2, session.getAttribute("groupTypeUserCode"));
            cstmt.setObject(3, session.getAttribute("accountTypeID"));
            cstmt.execute();
            
            System.out.println("accountTypeID: " + session.getAttribute("accountTypeID"));
            System.out.println("GroupTypeUserCode: " + session.getAttribute("groupTypeUserCode"));

            OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);
            while (rs.next()) {
                MyAccount account = new MyAccount();
                account.setCode(rs.getBigDecimal(1));
                account.setType(rs.getString(2));
                account.setAccType(rs.getString(3));
                account.setAgnCode(rs.getBigDecimal(4));
                account.setAgnName(rs.getString(5));
                account.setSelect(false);

                accounts.add(account);
            }

            rs.close();
            cstmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getLocalizedMessage());
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return accounts;
    }
    
    public List<Branch> fetchAllBranches(){
        List<Branch> branches  = new ArrayList<Branch>();
        final String query = "begin ? := tqc_roles_cursor.getallbranches(); end;";
        OracleConnection conn = null;
        OracleCallableStatement cstmt = null;
        DBConnector dbHandler = new DBConnector();
        try{
            conn = dbHandler.getDatabaseConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(query);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();
            
            OracleResultSet rs = (OracleResultSet)cstmt.getObject(1);
            while(rs.next()){
                final Branch branch = new Branch();
                branch.setBranchCode(rs.getBigDecimal(1));
                branch.setBranchShtDesc(rs.getString(2));
                branch.setRegCode(rs.getBigDecimal(3));
                branch.setBranchName(rs.getString(4));
                branches.add(branch);
            }
        } catch(Exception e){
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        
        
        return branches;
    }
}
