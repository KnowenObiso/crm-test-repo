
package TurnQuest.view.Usr;


import TurnQuest.view.Agents.Agent;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.UserSystem;
import TurnQuest.view.roles.Role;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class UserDAO {
    public UserDAO() {
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<User> FindAccountOfficers() {

        String query = "begin tqc_roles_cursor.get_AccountOfficers(?);end;";
        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<User>();
            int k = 0;
            while (rs.next()) {
                User user = new User();
                user.setUserCode(rs.getBigDecimal(1));
                if (k == 0) {
                    //  session.setAttribute("sysUserCode", rs.getBigDecimal(1));
                }
                user.setUsername(rs.getString(2));
                user.setUserFullname(rs.getString(3));
                user.setUserEmail(rs.getString(4));
                user.setUserPersonnelRank(rs.getString(5));
                user.setUserDateCreated(rs.getDate(6));
                user.setUserType(GlobalCC.decodeUserType(rs.getString(7)));
                user.setUserStatus(GlobalCC.decodeUserStatus(rs.getString(8)));
                user.setUserPassReset(GlobalCC.decodeReset(rs.getString(9)));
                user.setUserPerId(rs.getString(10));
                users.add(user);
                k++;
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return users;
    }
    
    
    public List<User> fetchKpis() {
        List<User> KPIList = new ArrayList<User>();
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        conn = dbCon.getDatabaseConnection();

        String query = "begin ? := TQC_SETUPS_CURSOR.getKPI(); end;";

        try {

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();

            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                User kpi = new User();
                kpi.setKpiCode(rs.getBigDecimal(1));
                kpi.setKpiUserGroupCode(rs.getBigDecimal(2));
                 kpi.setKpiTaskCode(rs.getBigDecimal(3));
                kpi.setKpiSubTaskCode(rs.getBigDecimal(4));
                 kpi.setKpiTask(rs.getString(5));
                kpi.setKpiSubTask(rs.getString(6));   
                kpi.setKpiParameter(rs.getString(7));  
                kpi.setKpiUnit(rs.getString(8));  
                kpi.setKpiComment(rs.getString(9));  
                KPIList.add(kpi);
            }


            rs.close();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return KPIList;
    }

    public List<User> findUsers() {

        String query = "SELECT   usr_code, usr_username, usr_name, usr_email,\n" + 
        "                     usr_personel_rank, usr_dt_created, usr_type, usr_status,\n" + 
        "                     usr_pwd_reset, usr_per_id, usr_acct_mgr,\n" + 
        "                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,\n" + 
        "                     usr_security_answer,usr_created_by, sact_code, sact_description,usr_authorized_by, decode(NVL(usr_authorized,'N'),'Y','Yes','N','No'),usr_updated_by,usr_updated_date,usr_pin\n" + 
        "                FROM tqc_users, tqc_security_questions, lms_sub_account_types\n" + 
        "               WHERE usr_secq_code = secq_code(+)\n" + 
        "                 AND usr_sact_code = sact_code(+)\n" + 
        "                 AND  usr_type ='U' \n" + 
        "                 AND :v_date IS NULL \n" + 
        "UNION\n" + 
        "SELECT   usr_code, usr_username, usr_name, usr_email,\n" + 
        "                     usr_personel_rank, usr_dt_created, usr_type, usr_status,\n" + 
        "                     usr_pwd_reset, usr_per_id, usr_acct_mgr,\n" + 
        "                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,\n" + 
        "                     usr_security_answer,usr_created_by, sact_code, sact_description,usr_authorized_by, decode(NVL(usr_authorized,'N'),'Y','Yes','N','No'),usr_updated_by,usr_updated_date,usr_pin\n" + 
        "                FROM tqc_users, tqc_security_questions, lms_sub_account_types\n" + 
        "               WHERE usr_secq_code = secq_code(+)\n" + 
        "                 AND usr_sact_code = sact_code(+)\n" + 
        "                 AND  usr_type ='U' \n" + 
        "                 AND (usr_dt_created LIKE '%' || TO_DATE (:v_date, 'dd/mm/yyyy') || '%') and ( :v_date IS NOT NULL )\n" + 
        "            ORDER BY usr_code DESC";
        
        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        String vDate=GlobalCC.checkNullValues(session.getAttribute("idUserCreatedDate"));
        OracleConnection conn = null;
        try {
            query=query.replaceAll(":v_date", vDate!=null?"'"+vDate.toString()+"'":"null");
            System.out.println("query: "+query);
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = (OracleResultSet) cst.executeQuery();

            users = new ArrayList<User>();
            int k = 0;
            while (rs.next()) {
                User user = new User();
                user.setUserCode(rs.getBigDecimal(1));
                if (k == 0) {
                    //  session.setAttribute("sysUserCode", rs.getBigDecimal(1));
                }
                user.setUsername(rs.getString(2));
                user.setUserFullname(rs.getString(3));
                user.setUserEmail(rs.getString(4));
                user.setUserPersonnelRank(rs.getString(5));
                user.setUserDateCreated(rs.getDate(6));
                user.setUserType(GlobalCC.decodeUserType(rs.getString(7)));
                user.setUserStatus(rs.getString(8));
                user.setUserPassReset(GlobalCC.decodeReset(rs.getString(9)));
                user.setUserPerId(rs.getString(10));
                user.setUserAccManager(rs.getString(11));
                user.setPhoneNumber(rs.getString(12));
                user.setSysWef(rs.getDate(13));
                user.setSysWet(rs.getDate(14));
                user.setUsrSecurityQuestion(rs.getString(15));
                user.setUsrSecurityAnswer(rs.getString(16));
                user.setUserCreatedBy(rs.getString(17));
                user.setUSR_SACT_CODE(rs.getBigDecimal(18));
                user.setSACT_DESCRIPTION(rs.getString(19));
                user.setUserAuthorizedBy(rs.getString(20));
                user.setUserAuthorized(rs.getString(21));
                user.setUpdatedBy(rs.getString(22));
                user.setUpdatedDate(rs.getDate(23));
                user.setPin(rs.getString(24));
                users.add(user);
                k++;
            }
            //  session.setAttribute("usrGrpDate",null);
            cst.close(); 
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return users;
    }


    public List<User> findUserSubAcctTypes() {

        String query = "begin  tqc_setups_cursor.find_life_subacc_types(?); end;";
        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR); //
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            users = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setSACT_CODE(rs.getBigDecimal(1));
                user.setSACT_SHT_DESC(rs.getString(2));
                user.setSACT_DESCRIPTION(rs.getString(3));
                users.add(user);
            }
            cst.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }finally{
            DbUtils.closeQuietly(conn,cst,null);
        }
        return users;
    }
    public List<User> findDomainUsers() {


        List<User> users = new ArrayList<User>();
        Hashtable env = new Hashtable();
        String[] username =
            session.getAttribute("Username").toString().split("@");
        //String[] ldapDomain =
        //    session.getAttribute("LDAP_SERVER").toString().split("\\.");
        String[] ldapDomain = null;
        if (username.length > 1) {
            ldapDomain = username[1].split("\\.");
        }
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap+.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,
                "ldap://" + session.getAttribute("LDAP_SERVER"));
        DirContext ctx = null;
        NamingEnumeration results = null;
        try {

            if (ldapDomain == null) {
                ctx = new InitialDirContext(env);
            } else {
                ctx = (DirContext)session.getAttribute("ldapCtx");
            }
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (ldapDomain == null) {
                results = ctx.search("", "(objectclass=person)", controls);
            } else {
                String searchString = "";
                int k = 0;
                while (k < ldapDomain.length) {
                    if (k == 0) {
                        searchString = searchString + "DC=" + ldapDomain[k];
                    } else {
                        searchString = searchString + ",DC=" + ldapDomain[k];
                    }

                    k++;
                }

                results =
                        ctx.search(searchString, "(objectclass=person)", controls);
            }
            while (results.hasMore()) {
                User user = new User();
                SearchResult searchResult = (SearchResult)results.next();
                Attributes attributes = searchResult.getAttributes();
                //Attribute attr = attributes.get("cn");
                //String cn = (String)attr.get();
                for (NamingEnumeration ae = attributes.getAll(); ae.hasMore();
                ) {
                    Attribute attrw = (Attribute)ae.next();
                    if (attrw.getID().equalsIgnoreCase("mail")) {
                        for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                             user.setUserEmail(e.next().toString()))
                            ;
                    } else if (attrw.getID().equalsIgnoreCase("uid")) {
                        for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                             user.setUsername(e.next().toString()))
                            ;
                    } else if (attrw.getID().equalsIgnoreCase("displayName")) {
                        for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                             user.setUserFullname(e.next().toString()))
                            ;
                    } else if (attrw.getID().equalsIgnoreCase("userPrincipalName")) {
                        for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                             user.setUsername(e.next().toString()))
                            ;
                    }


                    //System.out.println("attribute: " + attrw.getID());

                    //for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                    //     System.out.println("value: " + e.next()))
                    //    ;

                }
                //System.out.println("(user.getUsername(): " + user.getUsername());
                if (user.getUsername() != null) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            //GlobalCC.EXCEPTIONREPORTING("Invalid Login Credentials");
            e.printStackTrace();
        }
        return users;
    }

    public List<User> findAllIndividualUsers() {


        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            String query =
                "begin ?:=tqc_roles_cursor.get_all_individual_users();end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR); //
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


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
                users.add(user);
            }
            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return users;
    }

    public List<User> findAllUsersNotInGroup() {

        String query = "begin tqc_roles_cursor.get_usersNotInGrp(?,?);end;";
        OracleCallableStatement cst = null;
        List<User> users = new ArrayList<User>();
        if (session.getAttribute("grpUserCode") != null) {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            try {

                conn = datahandler.getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1,
                                         OracleTypes.CURSOR); //authorization code
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("grpUserCode").toString()));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                users = new ArrayList<User>();
                int k = 0;
                while (rs.next()) {
                    User user = new User();
                    user.setUserCode(rs.getBigDecimal(1));
                    if (k == 0) {
                        //  session.setAttribute("sysUserCode", rs.getBigDecimal(1));
                    }
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
                    k++;
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return users;
    }


    public List<User> findAllSystems() {

        String query = "begin ? := tqc_roles_cursor.Get_Systems(); end;";
        OracleCallableStatement cst = null;
        List<User> systems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);


            while (rs.next()) {
                User user = new User();
                user.setSysCode(rs.getBigDecimal(1));
                user.setSysShtDesc(rs.getString(2));
                user.setSysName(rs.getString(3));
                user.setSysActive(rs.getString(4));
                user.setNodeType("P");
                systems.add(user);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return systems;
    }


    public List<User> findSystems() {

        String query = "begin ? := tqc_roles_cursor.Get_Systems; end;";
        OracleCallableStatement cst = null;
        List<User> systems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            systems = new ArrayList<User>();
            while (rs.next()) {
                List<Role> rolesList = new ArrayList<Role>();
                User user = new User();
                user.setSysCode(rs.getBigDecimal(1));
                user.setSysShtDesc(rs.getString(2));
                user.setSysName(rs.getString(3));
                user.setSysActive(rs.getString(4));
                user.setNodeType("P");
                BigDecimal sysCode=GlobalCC.checkBDNullValues(user.getSysCode());
                String roleQuery=" SELECT srls_code, srls_name, srls_crt_date, srls_sht_desc, \n" + 
                "                srls_status,decode(srls_authorized,'Y','YES','N','NO') srls_authorized \n" + 
                "           FROM tqc_sys_roles\n" + 
                "          WHERE srls_sys_code = :v_syscode ";
                roleQuery=roleQuery.replaceAll(":v_syscode", (sysCode!=null)? sysCode.toString() : "null");
                System.out.println("query: "+roleQuery);
                OracleCallableStatement stmt2 = (OracleCallableStatement)conn.prepareCall(roleQuery);
 
                OracleResultSet rs2 = (OracleResultSet)stmt2.executeQuery();

                while (rs2.next()) {
                    Role roles = new Role();
                    roles.setRoleCode(rs2.getBigDecimal(1));
                    roles.setProcessCode(user.getSysCode()); // The system code
                    roles.setRoleName(rs2.getString(2));
                    roles.setRoleCrtDate(rs2.getDate(3));
                    roles.setRoleShtDesc(rs2.getString(4));
                    roles.setRoleStatus(rs2.getString(5));
                    roles.setRoleAuthorized(rs2.getString(6));
                    roles.setNodeType("S");
                    rolesList.add(roles);
                }
                user.setRolesList(rolesList);
                systems.add(user);

                stmt2.close();
                rs2.close();
            }
            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return systems;
    }
   
    public List<User> findUserSystems() {

        String query = "begin ? := tqc_roles_cursor.Get_User_Systems(?); end;";
        OracleCallableStatement cst = null;
        List<User> userSystems = new ArrayList<User>();
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

            userSystems = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setUserSysCode(rs.getBigDecimal(1));
                user.setSysCode(rs.getBigDecimal(2));
                user.setSysShtDesc(rs.getString(3));
                user.setSysWef(rs.getDate(4));
                user.setSysName(rs.getString(5));
                user.setSysWet(rs.getDate(6));
                userSystems.add(user);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return userSystems;
    }

    public List<User> fetchUnassignedUserSystems() {

        String query =
            "begin ? := tqc_roles_cursor.get_user_unassigned_systems(?); end;";
        OracleCallableStatement cst = null;
        List<User> unassignedSystems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("sysUserCode") != null) {
            try {
                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("sysUserCode"));
                System.out.println(session.getAttribute("sysUserCode"));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));
                    unassignedSystems.add(user);
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return unassignedSystems;
    }

    public List<User> fetchAssignedUserSystems() {

        String query =
            "begin ? := tqc_roles_cursor.get_user_assigned_systems(?); end;";
        OracleCallableStatement cst = null;
        List<User> assignedSystems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("sysUserCode") != null) {
            try {
                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("sysUserCode"));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));
                    assignedSystems.add(user);
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return assignedSystems;
    }

    public List<UserSystem> fetchAllUserAssignedSystems() {
        String query =
            "begin ? := TQC_ROLES_CURSOR.getAllUserAssignedSystems(?); end;";
        OracleCallableStatement cst = null;
        List<UserSystem> userSystemsList = new ArrayList<UserSystem>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("sysUserCode") != null) {
            try {
                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("sysUserCode").toString()));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    UserSystem userSystem = new UserSystem();
                    userSystem.setSysCode(rs.getBigDecimal(1));
                    userSystem.setSysShtDesc(rs.getString(2));
                    userSystem.setSysName(rs.getString(3));
                    userSystem.setSysActive(rs.getString(4));
                    userSystem.setUsysCode(rs.getBigDecimal(5));
                    userSystem.setUsysUsrCode(rs.getBigDecimal(6));
                    userSystem.setUsysSysCode(rs.getBigDecimal(7));
                    userSystem.setUsysWef(rs.getDate(8));
                    userSystem.setUsysWet(rs.getDate(9));
                    userSystem.setUsysSpostCode(rs.getBigDecimal(10));
                    userSystem.setSpostDesc(rs.getString(11));

                    userSystemsList.add(userSystem);
                }
                cst.close();
                rs.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return userSystemsList;
    }

    public List<User> findAllSystemUsers() {

        String query = "begin ? := tqc_roles_cursor.Get_system_Users(?); end;";
        OracleCallableStatement cst = null;
        List<User> userSystems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            userSystems = new ArrayList<User>();
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
                userSystems.add(user);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return userSystems;
    }

    public List<User> fetchUnassignedUserRoles() {
        String query =
            "begin ? := tqc_roles_cursor.get_UnAssignedSystems_withRole(?); end;";
        OracleCallableStatement cst = null;
        List<User> systems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("sysUserCode") != null) {
            try {

                conn = datahandler.getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1,
                                         OracleTypes.CURSOR); //authorization code
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("sysUserCode"));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                while (rs.next()) {
                    List<Role> rolesList = new ArrayList<Role>();
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));

                    user.setNodeType("P");
                  
                    BigDecimal userCode=GlobalCC.checkBDNullValues(session.getAttribute("sysUserCode"));
                    BigDecimal sysCode=GlobalCC.checkBDNullValues(rs.getBigDecimal(1));
                  
                    String roleQuery="SELECT srls_code, srls_sys_code, srls_name, srls_status,NULL,NULL\n" + 
                    "           FROM tqc_sys_roles\n" + 
                    "          WHERE srls_status = 'A' \n" + 
                    "            AND srls_sys_code = :v_sys_code\n" + 
                    "            AND NOT EXISTS (\n" + 
                    "                   SELECT usrr_usr_code, usrr_srls_code\n" + 
                    "                     FROM tqc_sys_user_roles\n" + 
                    "                    WHERE tqc_sys_roles.srls_code =\n" + 
                    "                                             tqc_sys_user_roles.usrr_srls_code\n" + 
                    "                      AND usrr_usr_code = :v_user_code)\n" + 
                    "            AND ( tqc_parameters_pkg.get_param_varchar('ROLE_MAKER_CHECKER_APP')!='Y')\n" + 
                    "    UNION SELECT srls_code, srls_sys_code, srls_name, srls_status,NULL,NULL\n" + 
                    "           FROM tqc_sys_roles\n" + 
                    "          WHERE srls_status = 'A' \n" + 
                    "            AND srls_sys_code = :v_sys_code\n" + 
                    "            AND NOT EXISTS (\n" + 
                    "                   SELECT usrr_usr_code, usrr_srls_code\n" + 
                    "                     FROM tqc_sys_user_roles\n" + 
                    "                    WHERE tqc_sys_roles.srls_code =\n" + 
                    "                                             tqc_sys_user_roles.usrr_srls_code\n" + 
                    "                      AND usrr_usr_code = :v_user_code)\n" + 
                    "           AND ( srls_authorized='Y' AND tqc_parameters_pkg.get_param_varchar('ROLE_MAKER_CHECKER_APP')='Y')          \n" + 
                    "            ORDER BY srls_name";
                  
                    roleQuery=roleQuery.replaceAll(":v_sys_code", sysCode!=null?sysCode.toString(): "null");
                    roleQuery=roleQuery.replaceAll(":v_user_code", userCode!=null?userCode.toString(): "null");
                     System.out.println("roleQuery: "+roleQuery);
                    OracleCallableStatement stmt2 =  (OracleCallableStatement)conn.prepareCall(roleQuery);

                    OracleResultSet rs2 = (OracleResultSet)stmt2.executeQuery();

                    while (rs2.next()) {
                        Role roles = new Role();
                        roles.setRoleCode(rs2.getBigDecimal(1));
                        roles.setProcessCode(rs2.getBigDecimal(2)); // The system code
                        roles.setRoleName(rs2.getString(3));
                        roles.setRoleStatus(rs2.getString(4));
                        roles.setNodeType("S");
                        rolesList.add(roles);
                    }
                    user.setRolesList(rolesList);
                    systems.add(user);

                    stmt2.close();
                    rs2.close();
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return systems;
    }

    public List<User> fetchAssignedUserRoles() {
        String query =
            "begin ? := tqc_roles_cursor.get_AssignedSystems_withRole(?); end;";
        OracleCallableStatement cst = null;
        List<User> systems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (session.getAttribute("sysUserCode") != null) {
            try {

                conn = datahandler.getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("sysUserCode"));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                systems = new ArrayList<User>();
                while (rs.next()) {
                    List<Role> rolesList = new ArrayList<Role>();
                    User user = new User();
                    user.setSysCode(rs.getBigDecimal(1));
                    user.setSysShtDesc(rs.getString(2));
                    user.setSysName(rs.getString(3));
                    user.setSysActive(rs.getString(4));
                    user.setNodeType("P");

                    OracleCallableStatement stmt2 =
                        (OracleCallableStatement)conn.prepareCall("begin ? := tqc_roles_cursor.get_user_assigned_roles(?,?); end;");

                    stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt2.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                    stmt2.setBigDecimal(3, rs.getBigDecimal(1));
                    stmt2.execute();
                    OracleResultSet rs2 = (OracleResultSet)stmt2.getObject(1);

                    while (rs2.next()) {
                        Role roles = new Role();
                        roles.setRoleCode(rs2.getBigDecimal(1));
                        roles.setProcessCode(rs2.getBigDecimal(2)); // The system code
                        roles.setRoleName(rs2.getString(3));
                        roles.setRoleStatus(rs2.getString(4));
                        roles.setWetDate(rs2.getDate(5));
                        roles.setNodeType("S");
                        roles.setWefDate(rs2.getDate(6));

                        rolesList.add(roles);
                    }
                    user.setRolesList(rolesList);
                    systems.add(user);

                    stmt2.close();
                    rs2.close();
                }

                cst.close();
                rs.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return systems;
    }

    public List<Role> fetchAllSystemRoles() {

        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        OracleCallableStatement cst = null;
        List<Role> sysRolesList = new ArrayList<Role>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            sysRolesList = new ArrayList<Role>();
            while (rs.next()) {
                Role role = new Role();

                role.setRoleCode(rs.getBigDecimal(1));
                role.setRoleName(rs.getString(2));
                role.setRoleCrtDate(rs.getDate(3));
                role.setRoleShtDesc(rs.getString(4));
                role.setRoleStatus(rs.getString(5));

                sysRolesList.add(role);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return sysRolesList;
    }

    public List<Role> fetchAllUserRoles() {

        String query = "begin ? := tqc_roles_cursor.Get_User_Roles(?,?); end;";
        OracleCallableStatement cst = null;
        List<Role> userRolesList = new ArrayList<Role>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();


            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            userRolesList = new ArrayList<Role>();
            while (rs.next()) {
                Role role = new Role();

                role.setRoleName(rs.getString(3));
                role.setRoleCrtDate(rs.getDate(5));
                role.setRoleStatus(rs.getString(6));
                role.setRoleCode(rs.getBigDecimal(7));
                role.setRoleShtDesc(rs.getString(8));

                userRolesList.add(role);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return userRolesList;
    }

    public List<UtilObject> fetchUserNonDefaultBranches() {

        String query =
            "begin ? := tqc_roles_cursor.get_nondefault_user_branch(?); end;";
        OracleCallableStatement cst = null;
        List<UtilObject> branchesList = new ArrayList<UtilObject>();
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

            branchesList = new ArrayList<UtilObject>();
            while (rs.next()) {
                UtilObject branch = new UtilObject();

                branch.setOrgCode(rs.getString(1));
                branch.setOrgName(rs.getString(2));
                branch.setRegionCode(rs.getString(3));
                branch.setRegionName(rs.getString(4));
                branch.setBranchCode(rs.getString(5));
                branch.setBranchName(rs.getString(6));

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

    public List<UtilObject> fetchUserNonDefaultDivisions() {

        String query =
            "begin ? := tqc_roles_cursor.get_nondefault_user_division(?); end;";
        OracleCallableStatement cst = null;
        List<UtilObject> divisionsList = new ArrayList<UtilObject>();
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

            divisionsList = new ArrayList<UtilObject>();
            while (rs.next()) {
                UtilObject division = new UtilObject();
                division.setOrgCode(rs.getString(1));
                division.setOrgName(rs.getString(2));
                division.setRegionCode(rs.getString(3));
                division.setRegionName(rs.getString(4));
                division.setBranchCode(rs.getString(5));
                division.setBranchName(rs.getString(6));
                division.setDivisionCode(rs.getString(7));
                division.setDivisionName(rs.getString(8));
                divisionsList.add(division);
            }

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return divisionsList;
    }

    /**
     * Fetches all <code>Personnel</code> objects/records from the
     * database and returns them in a list.
     *
     * @return A list of <code>Personnel</code> objects/records
     * fetched from the database.
     */
    public List<Personnel> fetchAllPersonnels() {

        List<Personnel> personnelsList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_COMMON_CURSORS.get_personnel(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            personnelsList = new ArrayList<Personnel>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                // For every row, create a personnel object with the
                // values and add it to the list
                Personnel personnel = new Personnel();
                personnel.setPerId(resultSet.getBigDecimal(1));
                personnel.setPerFullNames(resultSet.getString(2));
                personnel.setConCode(resultSet.getString(3));
                personnelsList.add(personnel);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return personnelsList;
    }

    /**
     * Get the personnel name corresponding to the given personnel Id.
     *
     * @param personnelId The personnel id to be used to match the personnel
     * name
     * @return the personnel name
     */
    public String getPersonnelName(String personnelId) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_COMMON_CURSORS.get_personnel_name(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String personnelName = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (personnelId != null) {
                statement.registerOutParameter(1, OracleTypes.VARCHAR);
                statement.setBigDecimal(2, new BigDecimal(personnelId));
                statement.execute();

                personnelName = statement.getString(1);

                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return personnelName;
    }


    public List<User> findUsersGroupMembers() {
        List<User> users = new ArrayList<User>();

        if (session.getAttribute("grpUserCode") != null) {


            String query =
                "begin ? := TQC_SETUPS_CURSOR.getGroupsMembers(?);end;";
            OracleCallableStatement cst = null;

            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            try {

                conn = datahandler.getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1,
                                         OracleTypes.CURSOR); //authorization code
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("grpUserCode").toString()));
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                users = new ArrayList<User>();
                int k = 0;
                while (rs.next()) {
                    User user = new User();

                    user.setUserCode(rs.getBigDecimal(1));
                    user.setUsername(rs.getString(2));
                    user.setUserFullname(rs.getString(3));
                    user.setGusr_Code(rs.getBigDecimal(4));
                    user.setGrpUsrCode(rs.getBigDecimal(5));
                    user.setUserSelected(false);
                    users.add(user);
                    k++;
                }

                cst.close();
                rs.close();
                conn.close();
                return users;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }
        return users;
    }

    public List<User> fetchAllSystems() {

        String query = "begin ? := tqc_roles_cursor.Get_Systems; end;";
        OracleCallableStatement cst = null;
        List<User> systems = new ArrayList<User>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            systems = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setSysCode(rs.getBigDecimal(1));
                user.setSysShtDesc(rs.getString(2));
                user.setSysName(rs.getString(3));
                user.setSysActive(rs.getString(4));

                systems.add(user);
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return systems;
    }
    public List<User> fetchAllUsers() {

            DBConnector datahandler = null;
            datahandler = new DBConnector();
            OracleConnection conn = null;
            conn = datahandler.getDatabaseConnection();

            OracleCallableStatement cst = null;
            List<User> usrs = new ArrayList<User>();
            try {

                String telQuoteQuery = "begin ? := TQC_WEB_CURSOR.Get_Users; end;";
                cst = (OracleCallableStatement)conn.prepareCall(telQuoteQuery);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.execute();
                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                int count=0;
                while (rs.next()) {
                    User usr = new User();
                    usr.setUsrCode(rs.getBigDecimal(1));
                    usr.setUsrName(rs.getString(2));
                    usr.setUsrFullname(rs.getString(3));
                    usrs.add(usr);
                    count++;
                }
                System.out.println("Total Users: "+count);
                rs.close();
                cst.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

            return usrs;

        }   
}
