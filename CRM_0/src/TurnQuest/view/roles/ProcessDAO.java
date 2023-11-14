package TurnQuest.view.roles;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.commons.dbutils.DbUtils;


public class ProcessDAO {
    public ProcessDAO() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public List<Process> findSystemProcess() {

        
        OracleCallableStatement prcssCst = null;
        List<Process> processRoles = new ArrayList<Process>();
        

        
        OracleCallableStatement cst = null;
        OracleCallableStatement cstSub = null;

        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        try {

            conn = datahandler.getDatabaseConnection();
            String processQuery =
              "SELECT  sprc_code, srprc_code,  sprc_process|| ' - ('||sprc_sht_desc||')' sprc_process,\n" + 
              "                  DECODE (srprc_sprc_code, NULL, 'N', 'Y') process_assigned,\n" + 
              "                  srprc_srls_code\n" + 
              "             FROM tqc_sys_processes, tqc_sys_roles_processes\n" + 
              "            WHERE sprc_code = srprc_sprc_code(+)\n" + 
              "              AND srprc_srls_code(+) = :v_rolecode\n" + 
              "              AND sprc_sys_code = :v_syscode\n" + 
              "              AND sprc_visible = 'Y'\n" + 
              "         ORDER BY sprc_process"; 
          
            BigDecimal sysCode=GlobalCC.checkBDNullValues(session.getAttribute("sysCode"));
            BigDecimal roleCode=GlobalCC.checkBDNullValues(session.getAttribute("processRoleCode"));
          
            processQuery=processQuery.replaceAll(":v_syscode",sysCode!=null?sysCode.toString():"null");
            processQuery=processQuery.replaceAll(":v_rolecode",roleCode!=null?roleCode.toString():"null");
          
            prcssCst = (OracleCallableStatement)conn.prepareCall(processQuery); 
            OracleResultSet rsPrcs = (OracleResultSet)prcssCst.executeQuery();

            processRoles = new ArrayList<Process>();
            while (rsPrcs.next()) {
                Process process = new Process();

                process.setProcessCode(rsPrcs.getBigDecimal(1));
                process.setProcessRoleCode(rsPrcs.getBigDecimal(2));
                process.setProcessName(rsPrcs.getString(3));
                process.setProcessAssigned(rsPrcs.getString(4));
                process.setAreaSubArea("P");

                if (rsPrcs.getString(4).equalsIgnoreCase("N")) {
                    process.setProcessSelected(false);
                } else {
                    process.setProcessSelected(true);
                } 
              
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
                BigDecimal processCode=GlobalCC.checkBDNullValues(process.getProcessCode());
                
                query=query.replaceAll(":v_role_code",roleCode!=null?roleCode.toString():"null");
                query=query.replaceAll(":v_sprc_code",processCode!=null?processCode.toString():"null");
                
                cst = (OracleCallableStatement)conn.prepareCall(query);
                OracleResultSet rs = (OracleResultSet)cst.executeQuery();
                List<Role> roles = new ArrayList<Role>();
                roles = new ArrayList<Role>();
                while (rs.next()) {
                    Role role = new Role();
                    cstSub = null;
                  role.setProcessAreaCode(rs.getBigDecimal(1));
                  role.setProcessArea(rs.getString(2));
                  role.setProcessRoleAreaCode(rs.getBigDecimal(3));
                  role.setRoleProcessCode(rs.getBigDecimal(4));
                  role.setProcessCode(rsPrcs.getBigDecimal(2));
                  if (rs.getString(6).equalsIgnoreCase("N")) {
                      role.setProcessAreaSelected(false);
                  } else if (rs.getString(6).equalsIgnoreCase("Y")) {
                      role.setProcessAreaSelected(true);
                  }
                  role.setAreaSubArea("A");

                  String subQuery =
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
                    
                    BigDecimal v_sprca_code=GlobalCC.checkBDNullValues(role.getProcessAreaCode());
                    
                  
                    subQuery=subQuery.replaceAll(":v_role_code",roleCode!=null?roleCode.toString():"null");
                    subQuery=subQuery.replaceAll(":v_sprca_code",v_sprca_code!=null?v_sprca_code.toString():"null");
                    subQuery=subQuery.replaceAll(":v_sprc_code",processCode!=null?processCode.toString():"null");
                    
                    //System.out.println("subQuery: "+subQuery);
                    cstSub = (OracleCallableStatement)conn.prepareCall(subQuery);
                     
                    OracleResultSet rsSub = (OracleResultSet)cstSub.executeQuery();
                    List<SubRole> subRole = new ArrayList<SubRole>();
                    while (rsSub.next()) {
                        SubRole subroleArea = new SubRole();
                        subroleArea.setProcessRoleSubAreaCode(rsPrcs.getBigDecimal(2));
                        subroleArea.setProcessSubAreaCode(rsSub.getBigDecimal(2));
                        subroleArea.setProcessSubArea(rsSub.getString(3));
                        subroleArea.setProcessSubAreaType(rsSub.getString(4));
                        subroleArea.setProcessSubAreaDebitLimit(rsSub.getBigDecimal(5));
                        subroleArea.setProcessSubAredCreditLimit(rsSub.getBigDecimal(6));
                        if (rsSub.getString(7).equalsIgnoreCase("N")) {
                            subroleArea.setProcessSubAreaSelected(false);
                        } else {
                            subroleArea.setProcessSubAreaSelected(true);
                        }
                        subroleArea.setAreaSubArea("S");
                        subRole.add(subroleArea);
                    }
                    rsSub.close();
                    cstSub.close(); 
                    role.setSubAreas(subRole);
                    roles.add(role);
                }
                rs.close();
                cst.close();
                process.setAreas(roles);
                processRoles.add(process); 
            }

            rsPrcs.close();
            prcssCst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return processRoles;

    }
}
