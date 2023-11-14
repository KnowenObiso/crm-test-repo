package TurnQuest.view.bpm;


import TurnQuest.view.Alerts.JBPMEngine;
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
import oracle.jdbc.OracleTypes;

import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.cfg.JbpmConfiguration;


public class BpmwDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private int totalTasks;
    public List<Bpmw> findPersonalTasks() {
        String Username = (String)session.getAttribute("UserCode");
        JBPMEngine bpm = JBPMEngine.getInstance();
        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
        //jbpmConfig = bpm.startWorkFlowInstance();
         bpm.startWorkFlowInstance();
        List<Bpmw> tickets = new ArrayList<Bpmw>();
        List<Task> tasks = new ArrayList<Task>();
        tasks = bpm.findPersonalTasks(Username);
        int k = 0;
        while (k < tasks.size()) {
            Bpmw work = new Bpmw();
            Task myTask = tasks.get(k);
            work.setTaskID(myTask.getId());
            work.setExecutionID(myTask.getExecutionId());
            work.setTaskCreationDate(myTask.getCreateTime());
            work.setTaskPriority(myTask.getPriority());
            work.setActivityName(myTask.getActivityName());
            work.setAssignee(myTask.getAssignee());
            work.setTaskVariableID(new BigDecimal(bpm.findProcessInstanceVariable(myTask.getExecutionId(),
                                                                                  "id").toString()));

            work.setTaskProcess(bpm.findProcessInstanceVariable(myTask.getExecutionId(),
                                                                "type").toString());
            k++;
            tickets.add(work);
        }
       
        bpm.endWorkFlowInstance(jbpmConfig);
        return tickets;
    }

    public List<Bpmw> findUserTasks() {
          //String Username = ((BigDecimal)session.getAttribute("UserCode")).toString();
          String Username = (String)session.getAttribute("Username"); //This cursor expects the name and not the code.
          String tktType = GlobalCC.checkNullValues(session.getAttribute("tktType"));
          
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        
        OracleCallableStatement cst = null;
        List<Bpmw> tasks = new ArrayList<Bpmw>();
//        try {
//           conn = datahandler.getDatabaseConnection();
//            /*String userTasks =
//                    "begin ? := TQC_WEB_CURSOR.GetTasks(?); end;";*/
//            String userTasks =
//                "begin ? := TQC_WEB_PKG.get_usr_tckt_dtls(?,?,?,?,?,?,?); end;";
//            cst = (OracleCallableStatement)conn.prepareCall(userTasks);
//            int k = 0;
//            //register out
//            cst.registerOutParameter(1, OracleTypes.CURSOR);
//            cst.setString(2, Username);
//            cst.setString(3, null);
//            cst.setString(4, null);
//            cst.setString(5, null);
//            cst.setInt(6,  GlobalCC.sysCode);
//            cst.setString(7, GlobalCC.claType);
//            cst.setString(8, tktType);
//            
//            cst.execute();
//            
//            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
//            
//            while (rs.next()) {
//                Bpmw workflow = new Bpmw();
//                workflow.setTicketCode(rs.getBigDecimal(1));
//                workflow.setTaskProcess(rs.getString(3));
//                workflow.setTicketClientCode(rs.getBigDecimal(4));
//                workflow.setClient(rs.getString(5));
//                workflow.setAgnCode(rs.getBigDecimal(6));
//                workflow.setAgent(rs.getString(7));
//                workflow.setPolCode(rs.getBigDecimal(8));
//                workflow.setPolNumber(rs.getString(9));
//                workflow.setClaimNo(rs.getString(10));
//                workflow.setQuoteCode(rs.getBigDecimal(11));
//                workflow.setQuotationNumber(rs.getString(12));
//                workflow.setTicketBy(rs.getString(13));
//                workflow.setTicketDate(rs.getDate(14));
//                workflow.setTicketProcessId(rs.getString(15));
//                workflow.setSysModuleCode(rs.getString(16));
//                workflow.setEndrCode(rs.getBigDecimal(17));
//                
//                //workflow.setEndrType(GlobalCC.findEndrType(rs.getBigDecimal(17)));
//                
////                System.out.print("endorsement type := ");
////                System.out.println(GlobalCC.findEndrType(rs.getBigDecimal(17)));
//                System.out.println(rs.getString(23));
//                
////                System.out.print("endorsement code := ");
////                System.out.println(rs.getBigDecimal(17));
//                
//                workflow.setProdType(rs.getString(18));
//                workflow.setTicketTo(rs.getString(19));
//                workflow.setTicketRemarks(rs.getString(20));
//                workflow.setTicketName(rs.getString(21));
//               // workflow.setTicketNameModified(GlobalCC.getEndorsementTicketNameModified(workflow.getEndrCode(), workflow.getTicketName()));
//                workflow.setTicketDueDate(rs.getDate(22));
//                workflow.setEndrType(rs.getString(23));
//                workflow.setTicketTransNo(rs.getBigDecimal(24));
//                workflow.setProposalNo(rs.getString(25));
//                workflow.setTckSelected(false);
//                
//             //   workflow.setDmsDocType(rs.getString(34));
//             ///   workflow.setDmsComment(rs.getString(35));
//                
//                //workflow.setAssignmentDate(rs.getDate(33));
//                /* workflow.setTaskID(rs.getString(4));
//                workflow.setExecutionID(rs.getString(3));
//                workflow.setTaskCreationDate(rs.getDate(7));
//                workflow.setTaskPriority(rs.getInt(9));
//                workflow.setActivityName(rs.getString(6));
//                workflow.setAssignee(rs.getString(8));
//                workflow.setTaskVariableID(rs.getBigDecimal(5));
//                workflow.setTaskProcess(rs.getString(2));*/
//                k++;
//                tasks.add(workflow);                
//            }
//          
//          totalTasks = k;
//            
//          System.out.println("All picked up tasks! " + totalTasks);
//          session.setAttribute("totalUsrTsk", totalTasks);
//          rs.close();
//          cst.close();
//          conn.commit();
//          conn.close();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            GlobalCC.EXCEPTIONREPORTING(conn, e);
//        }

        return tasks;
    }

    public List<Bpmw> findCommonUserTasks() {
        String Username = null;
        String siteParam = (String)session.getAttribute("siteParam");

        if (siteParam.equalsIgnoreCase("12") )
        Username = "COMMONL";
        
        String tktType = GlobalCC.checkNullValues(session.getAttribute("tktType"));
        
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Bpmw> tasks = new ArrayList<Bpmw>();
        try {

            /*String userTasks =
                    "begin ? := TQC_WEB_CURSOR.GetTasks(?); end;";*/
            String userTasks =
                "begin ? := TQC_WEB_PKG.get_usr_tckt_dtls(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);
            int k = 0;
            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, Username);
            cst.setString(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            //cst.setInt(6, 27);
            cst.setInt(6, 26);
            cst.setString(7, GlobalCC.claType);
            cst.setString(8, tktType);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Bpmw workflow = new Bpmw();
                workflow.setTicketCode(rs.getBigDecimal(1));
                workflow.setTaskProcess(rs.getString(3));
                workflow.setTicketClientCode(rs.getBigDecimal(4));
                workflow.setClient(rs.getString(5));
                workflow.setAgnCode(rs.getBigDecimal(6));
                workflow.setAgent(rs.getString(7));
                workflow.setPolCode(rs.getBigDecimal(8));
                workflow.setPolNumber(rs.getString(9));
                workflow.setClaimNo(rs.getString(10));
                workflow.setQuoteCode(rs.getBigDecimal(11));
                workflow.setQuotationNumber(rs.getString(12));
                workflow.setTicketBy(rs.getString(13));
                workflow.setTicketDate(rs.getDate(14));
                workflow.setTicketProcessId(rs.getString(15));
                workflow.setSysModuleCode(rs.getString(16));
                workflow.setEndrCode(rs.getBigDecimal(17));
                workflow.setProdType(rs.getString(18));
                workflow.setTicketTo(rs.getString(19));
                workflow.setTicketRemarks(rs.getString(20));
                workflow.setTicketName(rs.getString(21));
               // workflow.setTicketNameModified(GlobalCC.getEndorsementTicketNameModified(workflow.getEndrCode(), workflow.getTicketName()));
                workflow.setTicketDueDate(rs.getDate(22));
                workflow.setTicketTransNo(rs.getBigDecimal(24));
                workflow.setProposalNo(rs.getString(25));
                workflow.setTckSelected(false);
                workflow.setAssignmentDate(rs.getDate(33));
                /* workflow.setTaskID(rs.getString(4));
                workflow.setExecutionID(rs.getString(3));
                workflow.setTaskCreationDate(rs.getDate(7));
                workflow.setTaskPriority(rs.getInt(9));
                workflow.setActivityName(rs.getString(6));
                workflow.setAssignee(rs.getString(8));
                workflow.setTaskVariableID(rs.getBigDecimal(5));
                workflow.setTaskProcess(rs.getString(2));*/
                k++;
                tasks.add(workflow);
                
            }
          
          totalTasks = k;
            
          System.out.println("All picked up tasks! " + totalTasks);
          session.setAttribute("totalUsrTsk", totalTasks);
          rs.close();
          cst.close();
          conn.commit();
          conn.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return tasks;
    }

    public List<Bpmw> findAllSysTasks() {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Bpmw> tasks = new ArrayList<Bpmw>();
        try {

            /*String userTasks =
                    "begin ? := TQC_WEB_CURSOR.GetTasks(?); end;";*/
            String userTasks =
                "begin ? := TQC_WEB_PKG.get_usr_tckt_dtls(?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);

            //register out
                         cst.registerOutParameter(1, OracleTypes.CURSOR);
                         cst.setString(2, null);
                         cst.setString(3, null);
                         cst.setString(4, null);
                         cst.setString(5, null);
                         cst.setInt(6, 26);
                         cst.setString(7, GlobalCC.claType);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Bpmw workflow = new Bpmw();
                workflow.setTicketCode(rs.getBigDecimal(1));
                workflow.setTaskProcess(rs.getString(3));
                workflow.setTicketClientCode(rs.getBigDecimal(4));
                workflow.setClient(rs.getString(5));
                workflow.setAgnCode(rs.getBigDecimal(6));
                workflow.setAgent(rs.getString(7));
                workflow.setPolCode(rs.getBigDecimal(8));
                workflow.setPolNumber(rs.getString(9));
                workflow.setClaimNo(rs.getString(10));
                workflow.setQuoteCode(rs.getBigDecimal(11));
                workflow.setQuotationNumber(rs.getString(12));
                workflow.setTicketBy(rs.getString(13));
                workflow.setTicketDate(rs.getDate(14));
                workflow.setTicketProcessId(rs.getString(15));
                workflow.setSysModuleCode(rs.getString(16));
                workflow.setEndrCode(rs.getBigDecimal(17));
                workflow.setProdType(rs.getString(18));
                workflow.setTicketTo(rs.getString(19));
                workflow.setTicketRemarks(rs.getString(20));
                workflow.setTicketName(rs.getString(21));
                workflow.setTicketDueDate(rs.getDate(22));
                workflow.setTicketTransNo(rs.getBigDecimal(24));
                /* workflow.setTaskID(rs.getString(4));
                workflow.setExecutionID(rs.getString(3));
                workflow.setTaskCreationDate(rs.getDate(7));
                workflow.setTaskPriority(rs.getInt(9));
                workflow.setActivityName(rs.getString(6));
                workflow.setAssignee(rs.getString(8));
                workflow.setTaskVariableID(rs.getBigDecimal(5));
                workflow.setTaskProcess(rs.getString(2));*/
                tasks.add(workflow);
            }
            rs.close();
            conn.commit();
            conn.close();
            cst.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return tasks;
    }

    public List<Bpmw> findAllTasks() {
        String Username =
            ((BigDecimal)session.getAttribute("UserCode")).toString();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<Bpmw> tasks = new ArrayList<Bpmw>();
        try {

            String userTasks =
                "begin ? := TQC_WEB_CURSOR.GetAllTasks(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, Username);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Bpmw workflow = new Bpmw();
                workflow.setTaskID(rs.getString(4));
                workflow.setExecutionID(rs.getString(3));
                workflow.setTaskCreationDate(rs.getDate(7));
                workflow.setTaskPriority(rs.getInt(9));
                workflow.setActivityName(rs.getString(6));
                workflow.setAssignee(rs.getString(8));
                workflow.setTaskVariableID(rs.getBigDecimal(5));
                workflow.setTaskProcess(rs.getString(2));
                tasks.add(workflow);
            }
            rs.close();
            conn.commit();
            conn.close();
            cst.close();


        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return tasks;
    }

    public List<Bpmw> findGroupTasks() {
        String Username = (String)session.getAttribute("UserCode");
        JBPMEngine bpm = JBPMEngine.getInstance();
        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
        //jbpmConfig = bpm.startWorkFlowInstance();
         bpm.startWorkFlowInstance();
        List<Bpmw> tickets = new ArrayList<Bpmw>();
        List<Task> tasks = new ArrayList<Task>();
        tasks = bpm.findGroupTasks(Username);
        int k = 0;
        while (k < tasks.size()) {
            Bpmw work = new Bpmw();
            Task myTask = tasks.get(k);
            work.setTaskID(myTask.getId());
            work.setExecutionID(myTask.getExecutionId());
            work.setTaskCreationDate(myTask.getCreateTime());
            work.setTaskPriority(myTask.getPriority());
            work.setActivityName(myTask.getActivityName());
            work.setAssignee(myTask.getAssignee());
            work.setTaskVariableDesc(myTask.getDescription());
            work.setTaskProcess(myTask.getName());
            k++;
            tickets.add(work);
        }
        bpm.endWorkFlowInstance(jbpmConfig);
        return tickets;
    }
    public List<Bpmw> findUserTasksByTckt() {
      String ticketID = GlobalCC.checkNullValues(session.getAttribute("ticketID"));
         // (String)session.getAttribute("ticketID");
      double tickId = Double.parseDouble(ticketID);
      BigDecimal ticketCode = new BigDecimal(tickId);
      DBConnector datahandler = null;
      datahandler = new DBConnector();
      OracleConnection conn = null;
      conn = datahandler.getDatabaseConnection();

      OracleCallableStatement cst = null;
      List<Bpmw> tasks = new ArrayList<Bpmw>();
      try {

          String userTasks =
              "begin ? := TQC_WEB_PKG.get_ticket_dtls_by_tckt(?); end;";
          cst = (OracleCallableStatement)conn.prepareCall(userTasks);

          //register out
          cst.registerOutParameter(1, OracleTypes.CURSOR);
          cst.setBigDecimal(2, ticketCode);
          cst.execute();
          OracleResultSet rs = (OracleResultSet)cst.getObject(1);
          while (rs.next()) {
              Bpmw workflow = new Bpmw();
              workflow.setTicketRemarks(rs.getString(20));
              session.setAttribute("ticketRemarks",rs.getString(20));
              tasks.add(workflow);
          }
          rs.close();
          cst.close();
          conn.commit();
          conn.close();


      } catch (Exception e) {

          GlobalCC.EXCEPTIONREPORTING(conn, e);
      }

      return tasks;
  }
    /*
    public List<BpmTask> fetchUserTasks() {
          //String Username = ((BigDecimal)session.getAttribute("UserCode")).toString();
          String Username = (String)session.getAttribute("Username"); //This cursor expects the name and not the code.
          String tktType = GlobalCC.checkNullValues(session.getAttribute("tktType"));
          
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<BpmTask> tasks = new ArrayList<BpmTask>();
        try {

            String userTasks =
                "begin ? := TQC_WEB_PKG.get_usr_tckt_dtls(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);
            int k = 0;
            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, Username);
            cst.setString(3, null);
            cst.setObject(4,  null);
            cst.setString(5,  null);
            cst.setObject(6,  GlobalCC.sysCode);
            cst.setObject(7,  null);
            cst.setString(8, tktType);
            
            cst.execute();
            
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            
            while (rs.next()) {
                BpmTask workflow = new BpmTask();
                workflow.setTaskID(rs.getString("tckt_code")); 
                workflow.setTicketBy(rs.getString("tckt_by"));
                workflow.setTicketDate(rs.getDate("tckt_date"));
                workflow.setTicketProcessId(rs.getString("tckt_process_id"));
                workflow.setTicketProcessShtDesc(rs.getString("sprc_sht_desc"));
                workflow.setTicketProcessPDL(rs.getString("sprc_jpdl_desc"));
                workflow.setTicketProcessName(rs.getString("sprc_jpdl_desc"));
                workflow.setTicketEntityCode(rs.getBigDecimal("tckt_entity_code"));
                workflow.setTicketEntityName(rs.getString("entity_name"));
                workflow.setTicketEntityRefNo(rs.getString("entity_ref_no"));
                workflow.setSysModuleCode(rs.getString("tckt_sys_module"));
                workflow.setSysModule(rs.getString("sysmodule"));
                workflow.setTicketTo(rs.getString("tckt_to"));
                workflow.setTicketRemarks(rs.getString("tckt_remarks"));
                workflow.setTicketName(rs.getString("tckt_name"));
                workflow.setTicketScreen(rs.getString("scrn_name"));
                workflow.setAssignee(rs.getString("tckt_to"));
                workflow.setTicketDueDate(rs.getDate("tckt_due_date")); 
                workflow.setTicketTransNo(rs.getString("tckt_transno")); 
                workflow.setTicketType(rs.getString("tckt_type")); 
                workflow.setTckSelected(false);
                workflow.setAssignmentDate(rs.getDate("tckt_transno"));
                tasks.add(workflow);  
                k++;
            }
          
          totalTasks = k;
            
          System.out.println("All picked up tasks! " + totalTasks);
          session.setAttribute("totalUsrTsk", totalTasks);
          rs.close();
          cst.close();
          conn.commit();
          conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return tasks;
    }  
     * */
    public List<BpmTask> fetchUserTasks() {
          //String Username = ((BigDecimal)session.getAttribute("UserCode")).toString();
          String Username = (String)session.getAttribute("Username"); //This cursor expects the name and not the code.
          String tktType = GlobalCC.checkNullValues(session.getAttribute("tktType"));
          
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        List<BpmTask> tasks = new ArrayList<BpmTask>();
        try {

            String userTasks =
                "begin ? := TQC_WEB_PKG.get_crm_tckt_dtls(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);
            int k = 0;
            /*
            1.	v_user          IN   VARCHAR2,
            2.	v_tckt_code        NUMBER DEFAULT NULL,
            3.	v_syscode		    NUMBER DEFAULT NULL,
            4.	v_tkt_type VARCHAR2 DEFAULT NULL
            */
            
            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, Username);
            cst.setString(3, null);//v_tckt_code
            cst.setObject(4,  GlobalCC.sysCode);
            cst.setString(5,  tktType);
           
            
            cst.execute();
            
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            
            while (rs.next()) {
                BpmTask workflow = new BpmTask();
                workflow.setTaskID(rs.getString("tckt_code")); 
                workflow.setTicketBy(rs.getString("tckt_by"));
                workflow.setTicketDate(rs.getDate("tckt_date"));
                workflow.setTicketProcessId(rs.getString("tckt_process_id"));
                workflow.setTicketProcessShtDesc(rs.getString("sprc_sht_desc"));
                workflow.setTicketProcessPDL(rs.getString("sprc_jpdl_desc"));
                workflow.setTicketProcessName(rs.getString("sprc_jpdl_desc"));
                workflow.setTicketEntityCode(rs.getBigDecimal("tckt_entity_code"));
                workflow.setTicketEntityName(rs.getString("entity_name"));
                workflow.setTicketEntityRefNo(rs.getString("entity_ref_no"));
                workflow.setSysModuleCode(rs.getString("tckt_sys_module"));
                workflow.setSysModule(rs.getString("sysmodule"));
                workflow.setTicketTo(rs.getString("tckt_to"));
                workflow.setTicketRemarks(rs.getString("tckt_remarks"));
                workflow.setTicketName(rs.getString("tckt_name"));
                workflow.setTicketScreen(rs.getString("scrn_name"));
                workflow.setAssignee(rs.getString("tckt_to"));
                workflow.setTicketDueDate(rs.getDate("tckt_due_date")); 
                workflow.setTicketTransNo(rs.getString("tckt_transno")); 
                workflow.setTicketType(rs.getString("tckt_type")); 
                workflow.setTckSelected(false);
                workflow.setAssignmentDate(rs.getDate("tckt_transno"));
                tasks.add(workflow);  
                k++;
            }
          
          totalTasks = k;
            
          System.out.println("All picked up tasks! " + totalTasks);
          session.setAttribute("totalUsrTsk", totalTasks);
          rs.close();
          cst.close();
          conn.commit();
          conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return tasks;
    }  
   
   
}
