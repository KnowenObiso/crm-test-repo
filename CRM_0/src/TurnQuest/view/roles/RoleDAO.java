/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.roles;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.SysProcessSubAreaLevel;
import TurnQuest.view.models.SysProcessSubAreaLimit;
import TurnQuest.view.models.UserObj;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public class RoleDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public RoleDAO() {
    }

    public List<Role> findSysRoles() {

        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        OracleCallableStatement cst = null;
        List<Role> roles = new ArrayList<Role>();
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

            roles = new ArrayList<Role>();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleCode(rs.getBigDecimal(1));
                role.setRoleName(rs.getString(2));
                role.setRoleCrtDate(rs.getDate(3));
                role.setRoleStatus(rs.getString(5));
                role.setRoleShtDesc(rs.getString(4));
                roles.add(role);
            }
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return roles;
    }

    public List<Role> findSysProcesses() {
        String query =
            "begin ? := tqc_roles_cursor.Get_System_Processes(?); end;";
        OracleCallableStatement cst = null;
        List<Role> roles = new ArrayList<Role>();
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

            roles = new ArrayList<Role>();
            while (rs.next()) {
                Role role = new Role();
                role.setProcessCode(rs.getBigDecimal(1));
                role.setProcessName(rs.getString(2));
                roles.add(role);
            }
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return roles;
    }

    public List<Role> findRoleAreas() {
        String query = "begin ? := tqc_roles_cursor.Get_Role_Areas(?,?); end;";

        String subQuery =
            "begin ? := tqc_roles_cursor.Get_Role_SubAreas(?,?); end;";
        OracleCallableStatement cst = null;
        OracleCallableStatement cstSub = null;
        List<Role> roles = new ArrayList<Role>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("processCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("processRoleCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            roles = new ArrayList<Role>();
            while (rs.next()) {
                Role role = new Role();
                cstSub = null;
                cstSub = (OracleCallableStatement)conn.prepareCall(subQuery);
                cstSub.registerOutParameter(1, OracleTypes.CURSOR);

                role.setProcessAreaCode(rs.getBigDecimal(1));
                role.setProcessArea(rs.getString(2));
                role.setProcessRoleAreaCode(rs.getBigDecimal(3));
                role.setRoleProcessCode(rs.getBigDecimal(4));
                if (rs.getBigDecimal(3) == null) {
                    role.setProcessAreaSelected(false);
                } else {
                    role.setProcessAreaSelected(true);
                }
                role.setAreaSubArea("A");
                cstSub.setBigDecimal(2, rs.getBigDecimal(1));
                cstSub.setBigDecimal(3, rs.getBigDecimal(3));
                cstSub.execute();
                OracleResultSet rsSub = (OracleResultSet)cstSub.getObject(1);
                List<SubRole> subRole = new ArrayList<SubRole>();
                while (rsSub.next()) {
                    SubRole subroleArea = new SubRole();
                    subroleArea.setProcessRoleSubAreaCode(rsSub.getBigDecimal(1));
                    subroleArea.setProcessSubAreaCode(rsSub.getBigDecimal(2));
                    subroleArea.setProcessSubArea(rsSub.getString(3));
                    subroleArea.setProcessSubAreaType(rsSub.getString(4));
                    subroleArea.setProcessSubAreaDebitLimit(rsSub.getBigDecimal(5));
                    subroleArea.setProcessSubAredCreditLimit(rsSub.getBigDecimal(6));
                    if (rsSub.getBigDecimal(1) == null) {
                        subroleArea.setProcessSubAreaSelected(false);
                    } else {
                        subroleArea.setProcessSubAreaSelected(true);
                    }
                    subroleArea.setAreaSubArea("S");
                    subRole.add(subroleArea);
                }
                rsSub.close();

                role.setSubAreas(subRole);
                roles.add(role);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return roles;


    }

    public List<SubRole> findSystemSubAreas() {
        String query =
            "begin ? := tqc_setups_cursor.getSystemSubAreas(?); end;";
        OracleCallableStatement cst = null;
        List<SubRole> subRole = new ArrayList<SubRole>();
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

            while (rs.next()) {
                SubRole subroleArea = new SubRole();
                subroleArea.setProcessSubAreaCode(rs.getBigDecimal(1));
                subroleArea.setProcessAreaCode(rs.getBigDecimal(2));
                subroleArea.setProcessCode(rs.getBigDecimal(3));
                subroleArea.setProcessSubArea(rs.getString(4));
                subroleArea.setProcessSubAreaType(rs.getString(5));
                subroleArea.setProcessSubAreaShtDesc(rs.getString(6));
                subRole.add(subroleArea);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return subRole;


    }

    public List<SubRole> findSubAreaAuthLevels() {

        String query =
            "begin ? := tqc_setups_cursor.getsubAreaLevels(?); end;";
        OracleCallableStatement cst = null;
        List<SubRole> subRole = new ArrayList<SubRole>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sprsaCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                SubRole subroleArea = new SubRole();
                subroleArea.setTqualCode(rs.getBigDecimal(1));
                subroleArea.setSprsaCode(rs.getBigDecimal(2));
                subroleArea.setLevelId(rs.getBigDecimal(3));
                subroleArea.setSrlsCode(rs.getBigDecimal(4));
                subroleArea.setSrlsName(rs.getString(5));
                subroleArea.setSubArea(rs.getString(6));
                subRole.add(subroleArea);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return subRole;
    }

    public List<SysProcessSubAreaLimit> fetchSysProcessSubAreaLimits() {
        List<SysProcessSubAreaLimit> processLimitsList =
            new ArrayList<SysProcessSubAreaLimit>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPrcssSubAreaLmts(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("sprsaCode") != null) {
                BigDecimal sprsaCode =
                    new BigDecimal(session.getAttribute("sprsaCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sprsaCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SysProcessSubAreaLimit subAreaLimit =
                        new SysProcessSubAreaLimit();
                    subAreaLimit.setSpsatCode(resultSet.getBigDecimal(1));
                    subAreaLimit.setSpsatSprsaCode(resultSet.getBigDecimal(2));
                    subAreaLimit.setSpsatNoOfLevel(resultSet.getBigDecimal(3));
                    subAreaLimit.setSpsatMinLimit(resultSet.getBigDecimal(4));
                    subAreaLimit.setSpsatMaxLimit(resultSet.getBigDecimal(5));

                    processLimitsList.add(subAreaLimit);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return processLimitsList;
    }

    public List<SysProcessSubAreaLevel> fetchSysProcessSubAreaLevels() {
        List<SysProcessSubAreaLevel> processLevelsList =
            new ArrayList<SysProcessSubAreaLevel>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getSysPrcssSubAreaLvls(?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("spsatCode") != null) {
                BigDecimal spsatCode =
                    new BigDecimal(session.getAttribute("spsatCode").toString());

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, spsatCode);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    SysProcessSubAreaLevel subAreaLevel =
                        new SysProcessSubAreaLevel();
                    subAreaLevel.setSpsalCode(resultSet.getBigDecimal(1));
                    subAreaLevel.setSpsalSprsaCode(resultSet.getBigDecimal(2));
                    subAreaLevel.setSpsalSpsatCode(resultSet.getBigDecimal(3));
                    subAreaLevel.setSpsalLevel(resultSet.getBigDecimal(4));
                    subAreaLevel.setSpsalApproverType(resultSet.getString(5));
                    subAreaLevel.setSpsalApproverId(resultSet.getBigDecimal(6));
                    subAreaLevel.setUserName(resultSet.getString(7));

                    processLevelsList.add(subAreaLevel);
                }
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return processLevelsList;
    }

    public List<UserObj> fetchUsersByTypeGroup() {
        List<UserObj> usersList = new ArrayList<UserObj>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getUsersByTypeGroup();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                UserObj user = new UserObj();
                user.setUsrCode(resultSet.getBigDecimal(1));
                user.setUsrUsername(resultSet.getString(2));
                user.setUsrName(resultSet.getString(3));
                //TODO : Add the rest of the fields returned by the function

                usersList.add(user);
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return usersList;
    }

    public List<UserObj> fetchUsersByTypeUser() {
        List<UserObj> usersList = new ArrayList<UserObj>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getUsersByTypeUser();end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                UserObj user = new UserObj();
                user.setUsrCode(resultSet.getBigDecimal(1));
                user.setUsrUsername(resultSet.getString(2));
                user.setUsrName(resultSet.getString(3));
                //TODO : Add the rest of the fields returned by the function

                usersList.add(user);
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return usersList;
    }

    public List<SubRole> findSystemProcessAreas() {
        System.out.println("Iterator executed");
        OracleCallableStatement cst = null;
        List<SubRole> subRole = new ArrayList<SubRole>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = datahandler.getDatabaseConnection();
          
          String query =
              "SELECT srpsa_code, sprsa_code,sprsa_sub_area ||' - ('||sprsa_sht_desc||')'  sprsa_sub_area  , sprsa_type,\n" + 
              "                srpsa_debit_limit, srpsa_credit_limit,\n" + 
              "                DECODE (srpsa_sprsa_code, NULL, 'N', 'Y') s_area_assigned\n" + 
              "           FROM tqc_sys_processes,\n" + 
              "                tqc_sys_process_areas,\n" + 
              "                tqc_sys_process_sub_areas,\n" + 
              "                (SELECT srpsa_code, srprc_sprc_code, srpra_sprca_code,\n" + 
              "                        srpsa_sprsa_code, srpsa_debit_limit,\n" + 
              "                        srpsa_credit_limit                 --,SRPSA_SRPRA_CODE\n" + 
              "                   FROM tqc_sys_roles_processes,\n" + 
              "                        tqc_sys_roles_prcs_area,\n" + 
              "                        tqc_sys_roles_prcs_s_area\n" + 
              "                  WHERE srprc_code = srpra_srprc_code\n" + 
              "                    AND srpra_code = srpsa_srpra_code\n" + 
              "                    AND srprc_srls_code = :v_role_code\n" + 
              "                     --AND SRPRA_SPRCA_CODE = :v_sprca_code\n" + 
              "                )\n" + 
              "          WHERE sprc_code = sprca_sprc_code\n" + 
              "            AND sprca_code = sprsa_sprca_code\n" + 
              "            AND sprsa_sprc_code = srprc_sprc_code(+)\n" + 
              "            AND sprsa_sprca_code = srpra_sprca_code(+)\n" + 
              "            AND sprsa_code = srpsa_sprsa_code(+)\n" + 
              "            AND sprsa_visible = 'Y'\n" + 
              "            AND SPRCA_SPRC_CODE = :v_sprc_code\n" + 
              "            AND sprca_code = :v_sprca_code";
            
            BigDecimal roleCode=GlobalCC.checkBDNullValues(session.getAttribute("processRoleCode"));
            BigDecimal areaCode=GlobalCC.checkBDNullValues(session.getAttribute("processAreaCode"));
            BigDecimal processCode=GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
            
            
            query=query.replaceAll(":v_role_code",roleCode!=null?roleCode.toString():"null");
            query=query.replaceAll(":v_sprca_code",areaCode!=null?areaCode.toString():"null");
            query=query.replaceAll(":v_sprc_code",processCode!=null?processCode.toString():"null");
           
            cst = (OracleCallableStatement)conn.prepareCall(query); 
            OracleResultSet rs = (OracleResultSet) cst.executeQuery();
            while (rs.next()) {
                SubRole areas = new SubRole();
                areas.setProcessRoleSubAreaCode(rs.getBigDecimal(2));
                areas.setProcessSubAreaCode(rs.getBigDecimal(2));
                areas.setProcessSubArea(rs.getString(3));
                areas.setProcessSubAreaType(rs.getString(4));
                areas.setProcessSubAreaDebitLimit(rs.getBigDecimal(5));
                areas.setProcessSubAredCreditLimit(rs.getBigDecimal(6));
                System.out.println("DATA executed" + rs.getBigDecimal(6));
                if (rs.getString(7).equalsIgnoreCase("N")) {
                    areas.setProcessSubAreaSelected(false);
                } else {
                    areas.setProcessSubAreaSelected(true);
                }
                areas.setAreaSubArea("S");
                subRole.add(areas);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return subRole;
    }

    public List<Role> findSystemRoles() {
        System.out.println("findSystemRoles: ");
        OracleCallableStatement cst = null;
        List<Role> rolesList = new ArrayList<Role>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        
        try {
            BigDecimal sysCode=GlobalCC.checkBDNullValues(session.getAttribute("sysCode"));
            conn = datahandler.getDatabaseConnection();
            String roleQuery=" SELECT srls_code, srls_name, srls_crt_date, srls_sht_desc, \n" + 
            "                srls_status,decode(srls_authorized,'Y','YES','N','NO') srls_authorized\n" + 
            "           FROM tqc_sys_roles\n" + 
            "          WHERE srls_sys_code = :v_syscode AND srls_visible<>'N' ";
            roleQuery=roleQuery.replaceAll(":v_syscode", sysCode!=null?sysCode.toString():"null");
            System.out.println("query: "+roleQuery);
            
            cst = (OracleCallableStatement)conn.prepareCall(roleQuery); 
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                    Role roles = new Role();
                    roles.setRoleCode(rs.getBigDecimal(1));
                    roles.setProcessCode(sysCode); // The system code
                    roles.setRoleName(rs.getString(2));
                    roles.setRoleCrtDate(rs.getDate(3));
                    roles.setRoleShtDesc(rs.getString(4));
                    roles.setRoleStatus(rs.getString(5));
                    roles.setRoleAuthorized(rs.getString(6));
                    roles.setNodeType("S");
                    rolesList.add(roles);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return rolesList;
    }
    
    public List<SubRole> findSystemRoleAreas() {
        System.out.println("Iterator executed");
        
        OracleCallableStatement cst = null;
        List<SubRole> subRole = new ArrayList<SubRole>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
              conn = datahandler.getDatabaseConnection();
              String query =
                            "SELECT sprca_code,  sprca_area || ' - ('||sprca_sht_desc||')' sprca_area, srpra_sprca_code, srpra_code,\n" + 
                            "                srprc_code,\n" + 
                            "                DECODE (srpra_sprca_code, NULL, 'N', 'Y') area_assigned\n" + 
                            "           FROM tqc_sys_processes,\n" + 
                            "                tqc_sys_process_areas,\n" + 
                            "                (SELECT srprc_sprc_code, srpra_sprca_code, srpra_code,\n" + 
                            "                        srprc_code\n" + 
                            "                   FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area\n" + 
                            "                  WHERE srprc_code = srpra_srprc_code\n" + 
                            "                    AND srprc_srls_code = :v_role_code)\n" + 
                            "          WHERE sprc_code = sprca_sprc_code\n" + 
                            "            AND sprca_sprc_code = :v_sprc_code\n" + 
                            "            AND sprca_sprc_code = srprc_sprc_code(+)\n" + 
                            "            AND sprca_code = srpra_sprca_code(+)\n" + 
                            "            AND sprca_visible='Y'";
            BigDecimal processCode=GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
            BigDecimal roleCode=GlobalCC.checkBDNullValues(session.getAttribute("processRoleCode"));
            query=query.replaceAll(":v_role_code",roleCode!=null?roleCode.toString():"null");
            query=query.replaceAll(":v_sprc_code",processCode!=null?processCode.toString():"null");
            cst = (OracleCallableStatement)conn.prepareCall(query); 
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                SubRole areas = new SubRole();
                areas.setProcessAreaCode(rs.getBigDecimal(1));
                areas.setProcessArea(rs.getString(2));
                areas.setProcessRoleAreaCode(rs.getBigDecimal(3));
                areas.setRoleProcessCode(rs.getBigDecimal(4));
                areas.setProcessCode((BigDecimal)session.getAttribute("processCode"));
                if (rs.getString(6).equalsIgnoreCase("N")) {
                    areas.setProcessAreaSelected(false);
                } else if (rs.getString(6).equalsIgnoreCase("Y")) {
                    areas.setProcessAreaSelected(true);
                }
                areas.setAreaSubArea("A");
                subRole.add(areas);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return subRole;
    }
    public List<Role> findSystemUserRights() {
        System.out.println("findSystemUserRights: ");
        OracleCallableStatement cst = null;
        List<Role> rolesList = new ArrayList<Role>();
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        
        try {
            BigDecimal sysCode=GlobalCC.checkBDNullValues(session.getAttribute("sysCode"));
            conn = datahandler.getDatabaseConnection();
            String roleQuery=" SELECT mkc_code,SPRC_PROCESS||'-'||sprsa_sub_area sprsa_sub_area, mkc_status, nvl(mkc_applicable_area,'S')\n" + 
            "           FROM tqc_sys_processes,\n" + 
            "                tqc_sys_process_areas,\n" + 
            "                tqc_sys_process_sub_areas,\n" +
            "                tqc_maker_checker_configs\n" +
            "          WHERE mkc_sys_code = :v_syscode and mkc_sprc_code=sprc_code\n" + 
            "            and mkc_sprca_code=sprca_code\n" + 
            "            and  mkc_sprsa_code=sprsa_code ";
            roleQuery=roleQuery.replaceAll(":v_syscode", sysCode!=null?sysCode.toString():"null");
            System.out.println("query: "+roleQuery);
            
            cst = (OracleCallableStatement)conn.prepareCall(roleQuery); 
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();
            while (rs.next()) {
                    Role roles = new Role();
                    roles.setMkcCode(rs.getBigDecimal(1));
                   // roles.setProcessCode(sysCode); // The system code
                    roles.setSprsaSubArea(rs.getString(2));
                    //roles.setMkcStatus(rs.getString(3));
                    roles.setMkcStatus(rs.getString(3).equals("ACTIVE") ? true :  false);
                    roles.setMkcApplicableArea(rs.getString(4));
                    roles.setNodeType("S");
                    rolesList.add(roles);
            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return rolesList;
    }
}
