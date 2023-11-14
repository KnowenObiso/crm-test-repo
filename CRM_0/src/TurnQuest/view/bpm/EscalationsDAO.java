package TurnQuest.view.bpm;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;

import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.pvm.internal.model.OpenProcessDefinition;


public class EscalationsDAO {
    public EscalationsDAO() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    protected RepositoryService repositoryService;
    protected ExecutionService executionService;
    protected TaskService taskService;
    protected IdentityService identityService;

    public List<Escalations> findSystemProcesses() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query = "begin TQC_SETUPS_CURSOR.get_systems(?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setSysCode(rs.getBigDecimal(1));
                escalate.setSysShortDesc(rs.getString(2));
                escalate.setSysName(rs.getString(3));
                escalate.setType("S");
                escalate.setName(rs.getString(3));

                List<SysProcess> values = new ArrayList<SysProcess>();
                String sql =
                    "begin ? := TQC_SETUPS_CURSOR.getSystemProcesses(?); end;";
                OracleCallableStatement stmt2 =
                    (OracleCallableStatement)conn.prepareCall(sql);

                stmt2.registerOutParameter(1, OracleTypes.CURSOR);
                stmt2.setBigDecimal(2, rs.getBigDecimal(1));
                stmt2.execute();
                OracleResultSet rst = (OracleResultSet)stmt2.getObject(1);
                while (rst.next()) {
                    SysProcess sysProcess = new SysProcess();
                    sysProcess.setDeployment(rst.getBigDecimal(1));
                    sysProcess.setSysCode(rst.getBigDecimal(2));
                    sysProcess.setJpdlDesc(rst.getString(3));
                    sysProcess.setType("SP");
                    sysProcess.setName(rst.getString(3));
                    values.add(sysProcess);
                }
                escalate.setSysProcess(values);
                orgData.add(escalate);
                rst.close();
                stmt2.close();

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


    
    public List<Escalations> findProcessDefActivities() {

        List<Escalations> orgData = new ArrayList<Escalations>();
        if (session.getAttribute("deployment") == null) {
            return orgData;
        }
        Connection conn = null;
        try {
            DBConnector dbconn = new DBConnector();
            conn = dbconn.getDatabaseConnection();
            ProcessEngine processEngine =
                new Configuration().setResource("jbpm.cfg.xml").buildProcessEngine();

            processEngine.setJdbcConnection(conn);
            repositoryService = processEngine.getRepositoryService();


            int k = 0;
            while (k <
                   repositoryService.createProcessDefinitionQuery().deploymentId(session.getAttribute("deployment").toString()).list().size()) {
                //repositoryService.createProcessDefinitionQuery().list().g
                OpenProcessDefinition processDef =
                    (OpenProcessDefinition)repositoryService.createProcessDefinitionQuery().list().get(k);

                int t = 0;
                while (t < processDef.getActivities().size()) {
                    Escalations escalate = new Escalations();
                    escalate.setActivityType(processDef.getActivities().get(t).getType());
                    escalate.setActivityName(processDef.getActivities().get(t).getName());
                    if (!processDef.getActivities().get(t).getType().equalsIgnoreCase("start") &&
                        !processDef.getActivities().get(t).getType().equalsIgnoreCase("end")) {
                        orgData.add(escalate);
                    }
                    t++;
                }
                k++;
            }


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return orgData;
    }

    public List<Escalations> findEscalationLevels() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getEscalations(?,?,?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("sysCode"));
            callStmt.setObject(3, session.getAttribute("jpdlName"));
            callStmt.setObject(4, session.getAttribute("activity"));
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setCode(rs.getBigDecimal(1));
                escalate.setSysCode(rs.getBigDecimal(2));
                escalate.setJpdlName(rs.getString(3));
                escalate.setActivityName(rs.getString(4));
                escalate.setLevel(rs.getBigDecimal(5));
                escalate.setUserCode(rs.getBigDecimal(6));
                escalate.setUsername(rs.getString(7));
                escalate.setDuration(rs.getBigDecimal(8));
                escalate.setCcUser(rs.getBigDecimal(9));
                escalate.setCcUsername(rs.getString(10));
                orgData.add(escalate);
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

    public List<Escalations> findEscalationLevelsOne() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getEscalations(?,?,?,?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("sysCode"));
            callStmt.setObject(3, session.getAttribute("jpdlName"));
            callStmt.setObject(4, session.getAttribute("activity"));
            callStmt.setObject(5, 1);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            int k = 0;
            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setCode(rs.getBigDecimal(1));
                escalate.setSysCode(rs.getBigDecimal(2));
                escalate.setJpdlName(rs.getString(3));
                escalate.setActivityName(rs.getString(4));
                escalate.setLevel(rs.getBigDecimal(5));
                escalate.setUserCode(rs.getBigDecimal(6));
                escalate.setUsername(rs.getString(7));
                escalate.setDuration(rs.getBigDecimal(8));
                escalate.setCcUser(rs.getBigDecimal(9));
                escalate.setCcUsername(rs.getString(10));
                escalate.setSelect(true);
                k++;
                orgData.add(escalate);
            }
            if (k == 0) {
                Escalations escalate = new Escalations();
                escalate.setSelect(false);
                orgData.add(escalate);
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

    public List<Escalations> findEscalationLevelsTwo() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getEscalations(?,?,?,?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("sysCode"));
            callStmt.setObject(3, session.getAttribute("jpdlName"));
            callStmt.setObject(4, session.getAttribute("activity"));
            callStmt.setObject(5, 2);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            int k = 0;
            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setCode(rs.getBigDecimal(1));
                escalate.setSysCode(rs.getBigDecimal(2));
                escalate.setJpdlName(rs.getString(3));
                escalate.setActivityName(rs.getString(4));
                escalate.setLevel(rs.getBigDecimal(5));
                escalate.setUserCode(rs.getBigDecimal(6));
                escalate.setUsername(rs.getString(7));
                escalate.setDuration(rs.getBigDecimal(8));
                escalate.setCcUser(rs.getBigDecimal(9));
                escalate.setCcUsername(rs.getString(10));
                escalate.setSelect(true);
                k++;
                orgData.add(escalate);
            }
            if (k == 0) {
                Escalations escalate = new Escalations();
                escalate.setSelect(false);
                orgData.add(escalate);
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

    public List<Escalations> findEscalationLevelsThree() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getEscalations(?,?,?,?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("sysCode"));
            callStmt.setObject(3, session.getAttribute("jpdlName"));
            callStmt.setObject(4, session.getAttribute("activity"));
            callStmt.setObject(5, 3);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            int k = 0;
            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setCode(rs.getBigDecimal(1));
                escalate.setSysCode(rs.getBigDecimal(2));
                escalate.setJpdlName(rs.getString(3));
                escalate.setActivityName(rs.getString(4));
                escalate.setLevel(rs.getBigDecimal(5));
                escalate.setUserCode(rs.getBigDecimal(6));
                escalate.setUsername(rs.getString(7));
                escalate.setDuration(rs.getBigDecimal(8));
                escalate.setCcUser(rs.getBigDecimal(9));
                escalate.setCcUsername(rs.getString(10));
                escalate.setSelect(true);
                k++;
                orgData.add(escalate);
            }
            if (k == 0) {
                Escalations escalate = new Escalations();
                escalate.setSelect(false);
                orgData.add(escalate);
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

    public List<Escalations> findEscalationLevelsFour() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query =
            "begin ?:= TQC_SETUPS_CURSOR.getEscalations(?,?,?,?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setObject(2, session.getAttribute("sysCode"));
            callStmt.setObject(3, session.getAttribute("jpdlName"));
            callStmt.setObject(4, session.getAttribute("activity"));
            callStmt.setObject(5, 4);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            int k = 0;
            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setCode(rs.getBigDecimal(1));
                escalate.setSysCode(rs.getBigDecimal(2));
                escalate.setJpdlName(rs.getString(3));
                escalate.setActivityName(rs.getString(4));
                escalate.setLevel(rs.getBigDecimal(5));
                escalate.setUserCode(rs.getBigDecimal(6));
                escalate.setUsername(rs.getString(7));
                escalate.setDuration(rs.getBigDecimal(8));
                escalate.setCcUser(rs.getBigDecimal(9));
                escalate.setCcUsername(rs.getString(10));
                escalate.setSelect(true);
                k++;
                orgData.add(escalate);
            }
            if (k == 0) {
                Escalations escalate = new Escalations();
                escalate.setSelect(false);
                orgData.add(escalate);
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
    public List<Escalations> findServiceReqEscalations() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Escalations> orgData = new ArrayList<Escalations>();
        try {
           
            String commentsQuery =
                    "begin ? := TQC_SERVICE_REQUESTS.getservreqscalations; end;";
            cst = (OracleCallableStatement)conn.prepareCall(commentsQuery);

            //register out
            cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            

            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setSreCode(rs.getBigDecimal(1));
                escalate.setSreSrtCode(rs.getBigDecimal(2));
                escalate.setSreLvlDuration(rs.getBigDecimal(3));
                escalate.setSreLvlOneAssignee(rs.getBigDecimal(4));
                escalate.setSreLvlTwoDuration(rs.getBigDecimal(5));
                escalate.setSreLvlTwoAssignee(rs.getBigDecimal(6));
                escalate.setSreSrtName(rs.getString(7));
                escalate.setSreLvlOneAssigneeName(rs.getString(8));
                escalate.setSreLvlTwoAssigneeName(rs.getString(9));

                orgData.add(escalate);
            }
            rs.close();
            cst.close();
            conn.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return orgData;
    }
    public List<Escalations> findSystems() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        List<Escalations> orgData = new ArrayList<Escalations>();
        String query = "begin TQC_SETUPS_CURSOR.get_systems(?); end;";

        //List<memoValues> MemoValues=null;
        try {
            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);

            while (rs.next()) {
                Escalations escalate = new Escalations();
                escalate.setSysCode(rs.getBigDecimal(1));
                escalate.setSysShortDesc(rs.getString(2));
                escalate.setSysName(rs.getString(3));
                escalate.setName(rs.getString(3)); 
                orgData.add(escalate); 
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
    public List<Escalations> findEscalationPoints() {
         List<Escalations>levels=fetchEscalationPointsLevel( null);
         return levels;
    }   
    public  List<Escalations> fetchEscalationPointsLevel(BigDecimal vCode) {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
       
        OracleResultSet rs =null;
        List<Escalations> orgData = new ArrayList<Escalations>();
        
        String query =
            "SELECT distinct epoint_code, epoint_name || ' - ' || a.usr_username,epoint_sys_code, epoint_assignee, a.usr_username assignee_name,epoint_to, b.usr_username to_name,epoint_cc,c.usr_username cc_name,epoint_duration\n" + 
            "                       FROM tqc_escalation_points,tqc_users a,tqc_users b,tqc_users c\n" + 
            "                          WHERE  \n" + 
            "                           (epoint_to = :v_epoint_to OR (epoint_to IS NULL and  :v_epoint_to IS NULL) ) \n" + 
            "                          AND epoint_to = b.usr_code(+) \n" + 
            "                          AND epoint_assignee = a.usr_code \n" + 
            "                          AND epoint_cc = c.usr_code(+) \n" + 
            "                          AND epoint_status='A'";

        //List<memoValues> MemoValues=null;
        try {
            conn = dbCon.getDatabaseConnection();
            if(!GlobalCC.tableExists("tqc_escalation_points")) {
                return orgData;
            } 
            query=query.replaceAll(":v_epoint_to",vCode!=null?vCode.toString():"null");
            System.out.println("query_L0: "+query);
          
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            
           rs= (OracleResultSet)callStmt.executeQuery();

                while (rs.next()) { 
                    Escalations escalate = new Escalations(); 
                    escalate.setCode(rs.getBigDecimal(1));
                    escalate.setName(rs.getString(2));
                    escalate.setSysCode(rs.getBigDecimal(3));
                    escalate.setUserCode(rs.getBigDecimal(4));
                    escalate.setUsername(rs.getString(5));
                    escalate.setToUserCode(rs.getBigDecimal(6));
                    escalate.setToUsername(rs.getString(7)); 
                    escalate.setCcUser(rs.getBigDecimal(8));
                    escalate.setCcUsername(rs.getString(9)); 
                    escalate.setDuration(rs.getBigDecimal(10));
                    BigDecimal usrCode=GlobalCC.checkBDNullValues(escalate.getUserCode());
                    if(usrCode!=null) {
                        List<Escalations>levels=fetchEscalationPointsLevel( usrCode);
                        System.out.println("levels: "+levels.size());
                        if(!levels.isEmpty()) {
                            escalate.setLevels(levels);    
                        } 
                    } 
                    orgData.add(escalate);
                    System.out.println("Code: "+escalate.getCode()+ " Name: "+escalate.getName()); 
                }
           
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }finally{ 
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return orgData;
    }   
}
