package TurnQuest.view.bpm;


import TurnQuest.view.Alerts.JBPMEngine;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;

import org.jbpm.pvm.internal.cfg.JbpmConfiguration;


public class TaskManipulation {
    private RichTable myTickets;
    private RichTable userTickets;
    private RichTable systemUsers;
    private RichInputText taskAssignee;
    private RichInputText taskDescription;
    private RichSelectOneChoice taskArea;
    private RichSelectOneChoice systemModule;
    private RichSelectOneChoice activity;
    private RichInputText proposalNumberAndClient;
    private RichInputText propolNo;
    private RichInputText client;
    private RichInputText refNo;
    private RichTable proposals;
    private RichPanelBox attachTicketPnl;
    private RichInputText taskDesc;
    private RichInputText usersDesc;
    private RichTable tasksTable;
    private RichPanelGroupLayout polLovDetails;
    private RichPanelBox paramPnl;
    private RichInputText polNo;
    private RichOutputLabel propLbl;
    private RichOutputLabel polLbl;
    private RichCommandButton singleTask;
    private RichCommandButton multiTasks;
    private RichSelectOneChoice tktType;
    private RichInputText txtAssignRemarks; 

    public TaskManipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
  
        public void setSystemUsers(RichTable systemUsers) {
            this.systemUsers = systemUsers;
        }

        public RichTable getSystemUsers() {
            return systemUsers;
        }   

    public void setMyTickets(RichTable myTickets) {
        this.myTickets = myTickets;
    }

    public RichTable getMyTickets() {
        return myTickets;
    }

    public String viewTicket() {   
        
        try {
            DBConnector datahandler = null;
            datahandler = new DBConnector();
            //initialize engine;
            JBPMEngine bpm = JBPMEngine.getInstance();
 
            session.setAttribute("resetSessionMap", "N");
            
            LOVDAO lov = new LOVDAO();//clear
            lov.reinitializeVariables();
            
            Object key2 = userTickets.getSelectedRowData();
            
            if (key2 == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

            if (row == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }

            String module = null;
            String activityName = null;
            String ticketType = null;
            String processShtDesc = null;
            String taskId= null;
            String pid=null;
            String entityCode=null;
            String entityName=null;
            String entityRefNo=null;
            String processPDL=null;
            String ScreenName = null;
                  
            module = GlobalCC.checkNullValues(row.getAttribute("sysModuleCode"));
            activityName = GlobalCC.checkNullValues(row.getAttribute("ticketName"));
            ticketType = GlobalCC.checkNullValues(row.getAttribute("ticketType"));
            processShtDesc = GlobalCC.checkNullValues(row.getAttribute("ticketProcessShtDesc"));
            processPDL =GlobalCC.checkNullValues(row.getAttribute("ticketProcessPDL"));
            taskId = GlobalCC.checkNullValues(row.getAttribute("taskID"));
            pid = GlobalCC.checkNullValues(row.getAttribute("ticketProcessId"));
            entityCode = GlobalCC.checkNullValues(row.getAttribute("ticketEntityCode"));
            entityName = GlobalCC.checkNullValues(row.getAttribute("ticketEntityName"));
            entityRefNo = GlobalCC.checkNullValues(row.getAttribute("ticketEntityRefNo"));
            ScreenName = GlobalCC.checkNullValues(row.getAttribute("ticketScreen"));

            System.out.println("module ====================" + module);
            System.out.println("activityName ====================" +activityName);
            System.out.println("taskId ====================" +taskId);
            System.out.println("processShtDesc ====================" +processShtDesc);
            System.out.println("ProcessId ====================" +pid);
            System.out.println("entityCode ====================" +entityCode);
            System.out.println("entityName ====================" +entityName);
            System.out.println("entityRefNo ====================" +entityRefNo);
            System.out.println("processPDL ====================" +processPDL);
            System.out.println("ScreenName ====================" +ScreenName);
            System.out.println("ticketType ====================" +ticketType);
            
            if (ScreenName == null) {
                GlobalCC.errorValueNotEntered("Error: Navidateion Screen not Defined!");
                return null;
            }
            
            if (taskId == null) {
                GlobalCC.errorValueNotEntered("Error: Navidateion Task not Defined!");
                return null;
            }
            
            if (processShtDesc == null) {
                GlobalCC.errorValueNotEntered("Error: Navidateion Process not Defined!");
                return null;
            }
            
            if (module == null) {
                GlobalCC.errorValueNotEntered("Error: Navidateion Module not Defined!");
                return null;
            }
            
            session.setAttribute("viewMyticket", "Y"); 
            
            session.setAttribute("taskID",taskId);
            session.setAttribute("ticketID",taskId);
            
            session.setAttribute("executionID",pid);
            session.setAttribute("processInstance",pid);
            
            session.setAttribute("module", module);
            session.setAttribute("sysModule", module); 
            
            session.setAttribute("ProcessBPMDef", processPDL);
            session.setAttribute("ProcessPDL", processPDL);
            session.setAttribute("ProcessShtDesc",processShtDesc);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Access);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Access); 
            
            session.setAttribute("activityName",activityName); 
            
            session.setAttribute("clientName", entityName);
            session.setAttribute("agentName", entityName);
            session.setAttribute("serviceProviderName", entityName);
            
            
            session.setAttribute("ticketEntityCode",entityCode); 
            session.setAttribute("AgentCode", entityCode); 
            session.setAttribute("agentCode", entityCode); 
            session.setAttribute("serviceProviderCode",entityCode);
            session.setAttribute("clientCode", entityCode);
            session.setAttribute("ClientCode",entityCode); 
            session.setAttribute("prpCode", entityCode); 
            
            //step 1 check task point and fetch process details.
            
            bpm.findProcessInstanceWhetherStarted(taskId,ticketType);
            
            //step 2. authorize process
            
            String AccessGranted =
                bpm.CheckUserRights();
            if(!("Y".equalsIgnoreCase(AccessGranted))) { 
                GlobalCC.accessDenied();
                return null;
            }
            //step 4. transition ticket at this point if necessary.
            //bpm.StartNewWorkflowInstance(processPDL);
            
            //step 5. navidate to the screen.
        
            FacesContext.getCurrentInstance().getExternalContext().redirect(ScreenName); 

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }

        return null;
    }
   
    public void setUserTickets(RichTable userTickets) {
        this.userTickets = userTickets;
    }

    public RichTable getUserTickets() {
        return userTickets;
    }

    public String reAssignTask() {
        Object key2 = userTickets.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        System.out.println("taskID: "+row.getAttribute("taskID"));
        session.setAttribute("ticketID", row.getAttribute("taskID"));
        session.setAttribute("taskID", row.getAttribute("taskID")); 
        
        GlobalCC.showPopup("fms:popReAssignTaskUsers");
        return null;
    }
    
    public String reAssignMultiTask() {
        Object key2 = userTickets.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;
  
        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        singleTask.setRendered(false);
        multiTasks.setRendered(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(singleTask);
        AdfFacesContext.getCurrentInstance().addPartialTarget(multiTasks);
        GlobalCC.showPopup("demoTemplate:users");
        session.setAttribute("ticketID", row.getAttribute("ticketCode"));
        session.setAttribute("taskID", row.getAttribute("ticketCode"));
  
  
        return null;
    }
    
  public void toggleSelectTicket(ValueChangeEvent evt) {
      int count = userTickets.getRowCount();
      if (evt.getNewValue() != evt.getOldValue()) {
          if (evt.getNewValue().equals(true)) {
              for (int m = 0; m < count; m++) {
                  JUCtrlValueBinding nodeBinding =
                      (JUCtrlValueBinding)userTickets.getRowData(m);
                  nodeBinding.setAttribute("tckSelected", true);
              }
          } else {
              for (int m = 0; m < count; m++) {
                  JUCtrlValueBinding nodeBinding =
                      (JUCtrlValueBinding)userTickets.getRowData(m);
                  nodeBinding.setAttribute("tckSelected", false);
              }
          }
          
          AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
      }
  }

    
  public void selectTicket(ValueChangeEvent evt) {      
      if (evt.getNewValue() != evt.getOldValue()) {
          JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)userTickets.getRowData();
          System.out.println("tckSelected = ");
          System.out.println(nodeBinding.getAttribute("tckSelected"));
          if (evt.getNewValue().equals(true)) {
                  nodeBinding.setAttribute("tckSelected", true);
          } else {
                  nodeBinding.setAttribute("tckSelected", false);
          }
          System.out.println("tckSelected II = ");
          System.out.println(nodeBinding.getAttribute("tckSelected"));
          
          AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
      }
  }

    public String saveTaskAssignee() {
            String assignee = null;
            String taskId = null;
            String remarks = null;
            
            Object key2 = systemUsers.getSelectedRowData();
            if (key2 == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

            if (row == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            
            assignee = GlobalCC.checkNullValues(row.getAttribute("usrName"));
            taskId = GlobalCC.checkNullValues(session.getAttribute("taskID"));
            remarks = GlobalCC.checkNullValues(txtAssignRemarks.getValue());
            
            if (assignee == null) {
                GlobalCC.errorValueNotEntered("Error: Please Select User!");
                return null;
            }
            if (taskId == null) {
                GlobalCC.errorValueNotEntered("Error: Please Select Task!");
                return null;
            } 

            JBPMEngine bpm = JBPMEngine.getInstance();
            bpm.saveTaskAssignee(assignee, taskId);
            
            DBConnector datahandler = null;
            datahandler = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement cst = null;
            
            try {
                conn = datahandler.getDatabaseConnection(); 
                String mktProposalNext =
                    "begin Tqc_Web_Pkg.Assign_Ticket ( ?, ?, ?, ?, ?, ? ); end;";
                cst = (OracleCallableStatement)conn.prepareCall(mktProposalNext);
                //bpm.saveTaskAssignee(assignee, taskId);
                cst.registerOutParameter(1, OracleTypes.VARCHAR); 
                cst.registerOutParameter(2, OracleTypes.VARCHAR); 
                cst.setString(3, taskId); 
                cst.setString(4, assignee); 
                cst.setString(5, remarks); 
                cst.setString(6, session.getAttribute("Username").toString());
                cst.execute(); 
                conn.commit();
                String status=cst.getString(1); 
                String msg=cst.getString(2); 
                if ( "S".equals(status) ){
                    GlobalCC.hidePopup("fms:popReAssignTaskUsers");
                    session.removeAttribute("taskID");
                    session.removeAttribute("activityName"); 
                }
                GlobalCC.INFORMATIONREPORTING(msg); 
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            } finally {
                DbUtils.closeQuietly(conn, cst, null);
            }
            ADFUtils.findIterator("fetchUserTasksIterator").executeQuery();
            GlobalCC.refreshUI(userTickets); 
            return null;
        } 

    public String saveTicketAssignee() {
        
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        
        try {
            OracleCallableStatement cst = null;

            String mktProposalNext =
                "begin TQC_WEB_PKG.reassign_task(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(mktProposalNext);


            Object key2 = systemUsers.getSelectedRowData();
            if (key2 == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            JUCtrlValueBinding row = (JUCtrlValueBinding)key2;
    
            if (row == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            String assignee = null;
            String taskId = null;
    
            assignee = GlobalCC.checkNullValues(row.getAttribute("usrName"));
    //        taskId = session.getAttribute("ticketID").toString();
            taskId = GlobalCC.checkNullValues(session.getAttribute("ticketID"));
    
            if (taskId != null && assignee != null) {
                //bpm.saveTaskAssignee(assignee, taskId);
                cst.setString(1, taskId); 
                cst.setString(2, assignee); 
                cst.setString(3, "REASSIGNED"); 
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
            }
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        } finally {
            //bpm.endWorkFlowInstance(jbpmConfig);
        }
        return null;
    }

    public String saveMultiTaskAssignee() {
        Object key2 = systemUsers.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        String assignee = null;
        String taskId = null;

        assignee = ((String)row.getAttribute("usrName")).toString();

        //JBPMEngine bpm = new JBPMEngine();
        //JbpmConfiguration jbpmConfig = new JbpmConfiguration();
        //bpm.startWorkFlowInstance();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        
        int m = 0;
        try {
            OracleCallableStatement cst = null;

            String mktProposalNext =
                "begin TQC_WEB_PKG.reassign_task(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(mktProposalNext);
            
            Boolean select = false;

            while (m < userTickets.getRowCount()) {

                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)userTickets.getRowData(m);
                select =
                        (Boolean)nodeBinding.getAttribute("tckSelected");
                taskId = 
                       ((BigDecimal)nodeBinding.getAttribute("ticketCode")).toString();
                if (select) {
                      if (taskId != null && assignee != null) {
                          //System.out.println("am here !!!!!");
                          //bpm.saveTaskAssignee(assignee, taskId);
                          cst.setString(1, taskId); 
                          cst.setString(2, assignee); 
                          cst.setString(3, "REASSIGNED"); 
                          cst.execute();
                      }
                }
              m++;
            }
            cst.close();
            //conn.commit();
            conn.close();
        //bpm.endWorkFlowInstance(jbpmConfig);
        }catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        } finally {
            //bpm.endWorkFlowInstance(jbpmConfig);
        }

        ADFUtils.findIterator("findUserTasksIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
        return null;
    }


    public String reAssignAllTasks() {
        // Add event code here...
        return null;
    }

    public void setTaskAssignee(RichInputText taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public RichInputText getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskDescription(RichInputText taskDescription) {
        this.taskDescription = taskDescription;
    }

    public RichInputText getTaskDescription() {
        return taskDescription;
    }

//    public String newSystemTask() {
//        String assignee = null;
//        String description = null;
//        Date dueDate = null;
//        String area = null;
//        String name = null;
//        String taskId = null;
//        assignee = taskAssignee.getLabel();
//        description = GlobalCC.checkNullValues(taskDescription.getValue());
//        area = GlobalCC.checkNullValues(taskArea.getValue());
//        if (area == null) {
//            GlobalCC.errorValueNotEntered("Error Missing Value:Task Area");
//            return null;
//        }
//        if (GlobalCC.checkNullValues(taskAssignee.getValue()) == null) {
//            GlobalCC.errorValueNotEntered("Error Missing Value:Task Assignee");
//            return null;
//        }
//        if (description == null) {
//            GlobalCC.errorValueNotEntered("Error Missing Value:Task Description");
//            return null;
//        }
//
//        if (area.equalsIgnoreCase("Q")) {
//            name = "New Quotation";
//        }
//
//        JBPMEngine bpm = new JBPMEngine();
//        taskId = bpm.createNewTask(assignee, description, dueDate, name);
//        if (taskId != null) {
//            bpm.saveTicketDetails(null, null, taskId, name, description, "Y");
//        }
//        try {
//            GlobalCC.viewContext("myTickets");
//            //FacesContext.getCurrentInstance().getExternalContext().redirect("myTickets.jspx");
//        } catch (Exception e) {
//            GlobalCC.EXCEPTIONREPORTING(null, e);
//        }
//
//        return null;
//    }

    public String sysUserSelected() {
        String assignee = null;
        String taskId = null;
        Object key2 = systemUsers.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        assignee = ((BigDecimal)row.getAttribute("usrCode")).toString();
        taskAssignee.setValue(row.getAttribute("usrName"));
        taskAssignee.setLabel(assignee);

        /*  JBPMEngine bpm = new JBPMEngine();
        bpm.saveTaskAssignee(assignee, taskId);
        ADFUtils.findIterator("findUserTasksIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
        try {
            //  FacesContext.getCurrentInstance().getExternalContext().redirect("myTickets.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn,e);
        }*/
        return null;
    }

    public void setTaskArea(RichSelectOneChoice taskArea) {
        this.taskArea = taskArea;
    }

    public RichSelectOneChoice getTaskArea() {
        return taskArea;
    }

    public String checkCurrentTicket(String intendedTckt) throws Exception {
        try {
          
          //NOTE: Let the process calling this procedure handle the exception. 
          //This will ensure the calling process exits with the correct message.
            
          BigDecimal ticketID = new BigDecimal(session.getAttribute("ticketID").toString());
          //String currentAssignee = (String)session.getAttribute("currentAssignee");
          String currentAssignee = GlobalCC.getCurrentAssignee(ticketID);
          String userName = (String)session.getAttribute("Username");
                  
          System.out.print("ticketID = ");
          System.out.println(ticketID);
          System.out.print("userName = ");
          System.out.println(userName);
          System.out.print("currentAssignee = ");
          System.out.println(currentAssignee);
          
          if(!currentAssignee.equalsIgnoreCase(userName)) {
            throw new Exception("The current ticket is assigned to " + currentAssignee + ". You cannot process this ticket at the moment.");
          }
          
          String ticket = (String)session.getAttribute("activityName");
            System.out.println("ticket ==== "+ticket+" :: "+intendedTckt);
          if (intendedTckt.equalsIgnoreCase(ticket)) {
              return "Y";
          } else {
              return "N";
          }
          
        } catch(Exception e) {
          throw e; //Let the process calling this procedure handle the exception. This will ensure the process exits with the correct message.
        }
    }

    public String assignTask() {
        GlobalCC.showPopup("demoTemplate:users");
        return null;
    }

    public void SystemModuleListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            if (systemModule.getValue() == null) {
                session.setAttribute("TicketModule", null);
            } else if (systemModule.getValue().toString().equalsIgnoreCase("E")) {
                session.setAttribute("TicketModule",
                                     (systemModule.getValue().toString()));
                session.setAttribute("process", "UPL");
                session.setAttribute("transCode", "PE");
                session.setAttribute("propRnd", "No");
            } else if (systemModule.getValue().toString().equalsIgnoreCase("L")) {
                session.setAttribute("TicketModule",
                                     (systemModule.getValue().toString()));
                session.setAttribute("process", "UPL");
                session.setAttribute("transCode", "PE");
                session.setAttribute("propRnd", "No");
            } else if (systemModule.getValue().toString().equalsIgnoreCase("U")) {
                session.setAttribute("TicketModule",
                                     (systemModule.getValue().toString()));
                session.setAttribute("process", "UPL");
                session.setAttribute("transCode", "EP");
                session.setAttribute("propRnd", "No");
            } else if (systemModule.getValue().toString().equalsIgnoreCase("M")) {
                session.setAttribute("TicketModule",
                                     (systemModule.getValue().toString()));
                session.setAttribute("process", "MPR");
                session.setAttribute("transCode", "EP");
                session.setAttribute("propRnd", "Yes");
            } else if (systemModule.getValue().toString().equalsIgnoreCase("UP")) {
                session.setAttribute("TicketModule",
                                     (systemModule.getValue().toString()));
                session.setAttribute("process", "UPR");
                session.setAttribute("transCode", "EP");
                session.setAttribute("propRnd", "Yes");
            }
        }
        paramPnl.setRendered(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(paramPnl);
        attachTicketPnl.setRendered(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(attachTicketPnl);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tasksTable);
        session.setAttribute("ProductCode", null);

    }

    public void setSystemModule(RichSelectOneChoice systemModule) {
        this.systemModule = systemModule;
    }

    public RichSelectOneChoice getSystemModule() {
        return systemModule;
    }

    public void setActivity(RichSelectOneChoice activity) {
        this.activity = activity;
    }

    public RichSelectOneChoice getActivity() {
        return activity;
    }

    public void setProposalNumberAndClient(RichInputText proposalNumberAndClient) {
        this.proposalNumberAndClient = proposalNumberAndClient;
    }

    public RichInputText getProposalNumberAndClient() {
        return proposalNumberAndClient;
    }

    public void setPropolNo(RichInputText propolNo) {
        this.propolNo = propolNo;
    }

    public RichInputText getPropolNo() {
        return propolNo;
    }

    public void setClient(RichInputText client) {
        this.client = client;
    }

    public RichInputText getClient() {
        return client;
    }

    public void setRefNo(RichInputText refNo) {
        this.refNo = refNo;
    }

    public RichInputText getRefNo() {
        return refNo;
    }

    public String newSelectPolicy() {
        GlobalCC.showPopup("demoTemplate:propPol");
        ADFUtils.findIterator("findProposalLOVIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(proposals);
        return null;
    }

    public String newAssignee() {
        GlobalCC.showPopup("demoTemplate:users");
        return null;
    }

    public String newTask() {
        GlobalCC.showPopup("demoTemplate:tasks");
        return null;
    }

    public void proposalNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String propolNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("propolNo", propolNoVal);
        }
    }
    
    public void policyNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String polNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("polNo", polNoVal);
        }
    }

    public void clientNameChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientNameVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("Name", clientNameVal);
        }
    }

    public void refNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String refNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("polRefNo", refNoVal);
        }
    }
    

//    public String endrPolicySelected() {
//        Object key2 = proposals.getSelectedRowData();
//        if (key2 == null) {
//            GlobalCC.errorValueNotEntered("No Record Selected");
//            return null;
//        }
//        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
//        if (r == null) {
//            GlobalCC.errorValueNotEntered("No Record Selected");
//            return null;
//        }
//
//        session.setAttribute("productType", r.getAttribute("productType"));
//        session.setAttribute("productCode", r.getAttribute("productCode"));
//        session.setAttribute("ecmProductCode", r.getAttribute("productCode"));
//        session.setAttribute("proposalNumber",
//                             r.getAttribute("proposalNumber"));
//        session.setAttribute("ecmPolNo", r.getAttribute("proposalNumber"));
//        session.setAttribute("policyProposalCode",
//                             r.getAttribute("proposalCode"));
//        session.setAttribute("polCode", r.getAttribute("proposalCode"));
//        session.setAttribute("proposalClient",
//                             r.getAttribute("proposalClient"));
//        session.setAttribute("clientCode", r.getAttribute("clientCode"));
//        session.setAttribute("ClientCode", r.getAttribute("prpCode"));
//        session.setAttribute("polClient", r.getAttribute("prpCode"));
//        session.setAttribute("prdCode", r.getAttribute("productCode"));
//        session.setAttribute("proposaleffectiveDate",
//                             r.getAttribute("proposalEffectiveDate"));
//        session.setAttribute("prodAllowRenwal",
//                             r.getAttribute("prodAllowRenwal"));
//        if ((String)r.getAttribute("productType") == "PN" ||
//            (String)r.getAttribute("productType") == "AN" ||
//            (String)r.getAttribute("productType") == "IN") {
//            if ((String)r.getAttribute("polGrpLifeRider") == "Y") {
//                session.setAttribute("medicaldecision", "Yes");
//                session.setAttribute("reindecision", "Yes");
//            } else {
//                session.setAttribute("medicaldecision", "No");
//                session.setAttribute("reindecision", "No");
//            }
//        } else {
//            session.setAttribute("medicaldecision", "Yes");
//            session.setAttribute("reindecision", "Yes");
//        }
//        String showProdOption =
//            GlobalCC.countProdOptions((BigDecimal)r.getAttribute("productCode"));
//        String unitLinked =
//            GlobalCC.findUnitLinked((BigDecimal)r.getAttribute("proposalCode"));
//        String grpLifeRider =
//            GlobalCC.findGrpLifeRider((BigDecimal)r.getAttribute("proposalCode"));
//        String invest =
//            GlobalCC.findInvestAllPremium((BigDecimal)r.getAttribute("proposalCode"));
//
//        String escAllowed =
//            GlobalCC.findEscAllowed((BigDecimal)r.getAttribute("proposalCode"),(BigDecimal)session.getAttribute("productCode"));
//        ;
//        session.setAttribute("escAllowed", escAllowed);
//        session.setAttribute("investAllPremium", invest);
//        session.setAttribute("unitLinked", unitLinked);
//        session.setAttribute("grpLifeRider", grpLifeRider);
//        session.setAttribute("endorsementCode",
//                             r.getAttribute("endorsementCode"));
//        session.setAttribute("prpCode", r.getAttribute("prpCode"));
//        session.setAttribute("transactionNumber",
//                             r.getAttribute("transactionNumber"));
//        session.setAttribute("transNo", r.getAttribute("transactionNumber"));
//        proposalNumberAndClient.setValue(r.getAttribute("proposalNumber"));
//        AdfFacesContext.getCurrentInstance().addPartialTarget(proposalNumberAndClient);
//        GlobalCC.hidePopup("demoTemplate:propPol");
//        return null;
//    }

    public void setProposals(RichTable proposals) {
        this.proposals = proposals;
    }

    public RichTable getProposals() {
        return proposals;
    }

    public void setAttachTicketPnl(RichPanelBox attachTicketPnl) {
        this.attachTicketPnl = attachTicketPnl;
    }

    public RichPanelBox getAttachTicketPnl() {
        return attachTicketPnl;
    }

    public void setTaskDesc(RichInputText taskDesc) {
        this.taskDesc = taskDesc;
    }

    public RichInputText getTaskDesc() {
        return taskDesc;
    }

    public void setUsersDesc(RichInputText usersDesc) {
        this.usersDesc = usersDesc;
    }

    public RichInputText getUsersDesc() {
        return usersDesc;
    }

    public String taskAssignee() {
        Object key2 = systemUsers.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        String assignee = null;

        assignee = ((BigDecimal)row.getAttribute("usrCode")).toString();
        session.setAttribute("AssigneeCode", row.getAttribute("usrCode"));
        session.setAttribute("TaskAssignee", row.getAttribute("usrName"));
        usersDesc.setValue(row.getAttribute("usrName"));
        AdfFacesContext.getCurrentInstance().addPartialTarget(usersDesc);
        GlobalCC.hidePopup("demoTemplate:users");

        return null;
    }

    public void setTasksTable(RichTable tasksTable) {
        this.tasksTable = tasksTable;
    }

    public RichTable getTasksTable() {
        return tasksTable;
    }

    public String findTaskSelected() {
        try {

            Object key2 = tasksTable.getSelectedRowData();
            if (key2 == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

            if (row == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            session.setAttribute("taskName", row.getAttribute("taskName"));
            taskDesc.setValue(row.getAttribute("taskName"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(taskDesc);
            GlobalCC.hidePopup("demoTemplate:tasks");
            //            DCIteratorBinding dciter =
            //                ADFUtils.findIterator("ReadJPDLXmlIterator");
            //            RowKeySet set = tasksTable.getSelectedRowKeys();
            //            Iterator rowKeySetIter = set.iterator();
            //
            //            while (rowKeySetIter.hasNext()) {
            //                List l = (List)rowKeySetIter.next();
            //                Key key = (Key)l.get(0);
            //                dciter.setCurrentRowWithKey(key.toStringFormat(true));
            //
            //                Row r = dciter.getCurrentRow();
            //                session.setAttribute("taskName", r.getAttribute("taskName"));
            //                taskDesc.setValue(r.getAttribute("taskName"));
            //                AdfFacesContext.getCurrentInstance().addPartialTarget(taskDesc);
            //            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String searchPolicy() {
        String propolNoVal = GlobalCC.checkNullValues(propolNo.getValue());
        String clientNameVal = GlobalCC.checkNullValues(client.getValue());
        String refNoVal = GlobalCC.checkNullValues(refNo.getValue());
        String polNoVal = GlobalCC.checkNullValues(polNo.getValue());
        session.setAttribute("propolNo", propolNoVal);
        session.setAttribute("Name", clientNameVal);
        session.setAttribute("polRefNo", refNoVal);
        session.setAttribute("polNo", polNoVal);
        ADFUtils.findIterator("findProposalLOVIterator").executeQuery();
        if (polLovDetails != null) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(polLovDetails);
        }
        if (proposals != null) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(polLovDetails);
        }
        System.out.println("Ree looad...propolNoVal=" + propolNoVal +
                           " clientNameVal=" + clientNameVal);
        return null;
    }

    public void setPolLovDetails(RichPanelGroupLayout polLovDetails) {
        this.polLovDetails = polLovDetails;
    }

    public RichPanelGroupLayout getPolLovDetails() {
        return polLovDetails;
    }

//    public String AttachTicket() {
//
//        JBPMEngine bpm = new JBPMEngine();
//        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
////        jbpmConfig = bpm.startWorkFlowInstance();
//          bpm.startWorkFlowInstance();
//        try {
//            String CurrentTask = null;
//            boolean status = false;
//            String id = null;
//            String taskID1 = null;
//
//            if (session.getAttribute("taskName") == null) {
//                GlobalCC.errorValueNotEntered("Select A Task");
//                return null;
//            }
//
//            if (session.getAttribute("TaskAssignee") == null) {
//                GlobalCC.errorValueNotEntered("Select A User to Assign");
//                return null;
//            }
//
//            String Module = (String)session.getAttribute("TicketModule");
//
//            session.setAttribute("CurrentStatus", Module);
//
//            if (Module.equalsIgnoreCase("P") || Module.equalsIgnoreCase("E") ||
//                Module.equalsIgnoreCase("UP") ||
//                Module.equalsIgnoreCase("L") || Module.equalsIgnoreCase("M")) {
//                status =
//                        checkTicketIfExists(session.getAttribute("policyProposalCode").toString(),
//                                            Module);
//
//                id = session.getAttribute("policyProposalCode").toString();
//            } else if (Module.equalsIgnoreCase("Q")) {
//                status =
//                        checkTicketIfExists(session.getAttribute("QuoteCode").toString(),
//                                            Module);
//
//                id = session.getAttribute("QuoteCode").toString();
//            } else if (Module.equalsIgnoreCase("C")) {
//                status =
//                        checkTicketIfExists(session.getAttribute("ClaimNo").toString(),
//                                            Module);
//
//                id = session.getAttribute("ClaimNo").toString();
////                if (status) {
////                    GlobalCC.errorValueNotEntered("The Claim Selected has another Ticket Attached to it");
////                    return null;
////                }
//            }
//                if (status) {
//                  String name1 = null;
//                  String processId = null;
//                  String enabled = GlobalCC.findProcessFlowEnabled();
//                  if (enabled.equalsIgnoreCase("Y") || enabled == null) {
//                    processId = bpm.findProcessInstanceWhetherStarted(id, "UW"); 
//                    if (processId !=null){
//                    name1 = bpm.findProcessInstanceTaskName(processId);
//                    taskID1 = bpm.findProcessInstanceTaskId(processId);
//                    session.setAttribute("activityName1", name1);
//                    
////                      if (taskID != null) {
////                          bpm.deleteTicket(taskID);
////                      }
//                    }
//                  }
//                    
//                }
//            
//
//            //We will need to create a For loop, to loop thru the nodes and return a Result...
//            DCIteratorBinding dciter =
//                ADFUtils.findIterator("ReadJPDLXmlIterator");
//            int rowCount = tasksTable.getRowCount();
//
//            if (rowCount == 0) {
//                GlobalCC.errorValueNotEntered("There are No Task Points for this Process Flow.");
//                return null;
//            }
//            String processId = null;
//            String name = null;
//            String taskID = null;
//            Map<String, Object> variables = null;
//            String processDef = null;
//            if (Module.equalsIgnoreCase("M")) {
//                variables = new HashMap<String, Object>();
//                variables.put("type", "mktProp");
//                variables.put("id", "0");
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("AssigneeCode")).toString());
//                session.setAttribute("lauchdecision", "No");
//                session.setAttribute("awaitdecision", "Yes");
//                session.setAttribute("decision", "Yes");
//                processDef = "mktProposal1";
//            } else if (Module.equalsIgnoreCase("L")) {
//                variables = new HashMap<String, Object>();
//                variables.put("type", "LoanProcessing");
//                variables.put("id",
//                              ((BigDecimal)session.getAttribute("policyProposalCode")).toString());
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("AssigneeCode")).toString());
//                processDef = "LoanProcessing1";
//            } else if (Module.equalsIgnoreCase("U")) {
//                variables = new HashMap<String, Object>();
//                variables.put("type", "ProposalUnderwriting");
//                variables.put("id", "0");
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("AssigneeCode")).toString());
//
//                session.setAttribute("newPropdecision", "No");
//                session.setAttribute("medicaldecision", "Yes");
//                session.setAttribute("reindecision", "Yes");
//                processDef = "ProposalUnderwriting1";
//            } else if (Module.equalsIgnoreCase("UP")) {
//                variables = new HashMap<String, Object>();
//                variables.put("type", "ProposalUnderwriting");
//                variables.put("id", "0");
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("AssigneeCode")).toString());
//
//                session.setAttribute("newPropdecision", "No");
//                session.setAttribute("medicaldecision", "Yes");
//                session.setAttribute("reindecision", "Yes");
//                processDef = "ProposalUnderwriting1";
//            } else if (Module.equalsIgnoreCase("E")) {
//                variables = new HashMap<String, Object>();
//                variables.put("type", "Endorsements");
//                variables.put("id",
//                              ((BigDecimal)session.getAttribute("endorsementCode")).toString());
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("AssigneeCode")).toString());
//
//                session.setAttribute("medicaldecision", "Yes");
//                processDef = "Endorsements1";
//            }
//            processId = bpm.startWorkflow(processDef, variables);
//
//            if (processId == null) {
//                GlobalCC.sysInformation("There was a Problem Encountered when initializing the Process Flow" +
//                                              "Engine.Process Id is Null!!. Please Contact your Administrator/Restart CRM Datasource");
//                return null;
//            }
//            name = bpm.findProcessInstanceTaskName(processId);
//            taskID = bpm.findProcessInstanceTaskId(processId);
//            session.setAttribute("activityName", name);
//            System.out.print("theactivityName==" +
//                             session.getAttribute("activityName"));
//            session.setAttribute("ticketID", taskID);
//            session.setAttribute("processInstance", processId);
//            System.out.print("ticketID==" + session.getAttribute("ticketID"));
//
//
//            if (GlobalCC.checkNullValues(session.getAttribute("ticketID"))== null) {
//                GlobalCC.sysInformation("There was a Problem Encountered when initializing the Process Flow" +
//                                              "Engine.Ticket Id is Null!!. Please Contact your Administrator/Restart CRM Datasource");
//                return null;
//            }
//            
//            String TaskName = (String)session.getAttribute("taskName");
//            System.out.println("Selected Task ============= " + TaskName);
//            String currname = null;
//            String existingTicket = (String)session.getAttribute("activityName1");
//          System.out.println("Selected Task ============= " + existingTicket);
//            if(TaskName.equalsIgnoreCase(existingTicket)){
//                  GlobalCC.sysInformation("U can't attach to the same ticket!! - Current ticket is :"+existingTicket
//                                          +" ,and you are trying to assign to :"+TaskName);
//                  return null;
//            }
//
//            do {
//                currname = (String)session.getAttribute("activityName");
//                System.out.println("currname == "+currname+" ; TaskName = "+TaskName);
//                if (TaskName.equalsIgnoreCase(currname)) {
//                    if (Module.equalsIgnoreCase("E")){
//                        if(TaskName.equalsIgnoreCase("Underwrite Policy")){                  
//                          if (taskID1 != null) {
//                          bpm.deleteTicket(taskID1);
//                          }
//                          currentTcktProcessing("ENDORSEMENT");
//                        }
//                      }
//                    if(Module.equalsIgnoreCase("M")){
//                      if(TaskName.equalsIgnoreCase("Marketing Data Entry")){                  
//                          if (taskID1 != null) {
//                          bpm.deleteTicket(taskID1);
//                          }
//                        currentTcktProcessing("MKTPROP");
//                      }
//                    }
//                    if(Module.equalsIgnoreCase("UP")){
//                      if(TaskName.equalsIgnoreCase("Underwrite Proposal")){                  
//                          if (taskID1 != null) {
//                          bpm.deleteTicket(taskID1);
//                          }
//                        currentTcktProcessing("U/W-PROP");
//                      }
//                    }
//                    if(Module.equalsIgnoreCase("L")){
//                      if(TaskName.equalsIgnoreCase("Capture Policy Loan Details")){                  
//                          if (taskID1 != null) {
//                          bpm.deleteTicket(taskID1);
//                          }
//                        currentTcktProcessing("LOAN");
//                      }
//                    }
//                  break;
//                }
//                else{
//                    if (taskID1 != null) {
//                        bpm.deleteTicket(taskID1);
//                    }
//                    if (Module.equalsIgnoreCase("M")) {
//                        tcktProcessing("MKTPROP");
//                    } else if (Module.equalsIgnoreCase("U")) {
//                        tcktProcessing("U/W-PROP");
//                    } else if (Module.equalsIgnoreCase("UP")) {
//                        tcktProcessing("U/W-PROP");
//                    } else if (Module.equalsIgnoreCase("E")) {
//                        tcktProcessing("ENDORSEMENT");
//                    } else if (Module.equalsIgnoreCase("L")) {
//                        System.out.println("Current Task ============= " +
//                                           (String)session.getAttribute("activityName"));
//                        tcktProcessing("LOAN");
//                    } else {
//                        break;
//                    }
//                }
//                System.out.println("TaskName == "+TaskName);
//                    
//            } 
//            while (!TaskName.equalsIgnoreCase(currname));{
//            GlobalCC.sysInformation("A Ticket has Successfully been attached");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            GlobalCC.EXCEPTIONREPORTING(null, e);
//        } 
//        finally {
//            bpm.endWorkFlowInstance(jbpmConfig);
//        }
//        return null;
//    }

    public boolean checkTicketIfExists(String code, String level) {

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {
            String msgQuery =
                "begin ? := TQC_WEB_CURSOR.check_ticket_status(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(msgQuery);
            cst.setString(2, code);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(3, level);
            cst.execute();
            if (cst.getString(1) == null ||
                cst.getString(1).equalsIgnoreCase("N")) {
                return false;
            }
            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return true;
    }

//    public void tcktProcessing(String ProcessDef) {
//        String active = null;
//        String processId = (String)session.getAttribute("processInstance");
//        String taskID = GlobalCC.checkNullValues(session.getAttribute("ticketID"));//(String)session.getAttribute("ticketID");
//        String name = (String)session.getAttribute("activityName");
//        JBPMEngine bpm = new JBPMEngine();
//        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
//        bpm.startWorkFlowInstance();
//     //   jbpmConfig = bpm.startWorkFlowInstance();
//        try {
//            if (processId != null) {
//                if (taskID != null) {
//                    bpm.completeTaskByTaskId(taskID);
//                }
//                active = "N";
//                if (taskID != null) {
//                    bpm.saveTicketDetails(processId,
//                                          ((BigDecimal)session.getAttribute("policyProposalCode")).toString(),
//                                          taskID, ProcessDef, name, active);
//                }
//
//                name = bpm.findProcessInstanceTaskName(processId);
//                System.out.println("name" + name);
//                taskID = bpm.findProcessInstanceTaskId(processId);
//                System.out.println("taskID" + taskID);
//                session.setAttribute("activityName", name);
//                session.setAttribute("ticketID", taskID);
//                session.setAttribute("processInstance", processId);
//
//                active = "Y";
//                name = bpm.findProcessInstanceTaskName(processId);
//                taskID = bpm.findProcessInstanceTaskId(processId);
//                if (taskID != null) {
//                    bpm.saveTicketDetails(processId,
//                                          ((BigDecimal)session.getAttribute("policyProposalCode")).toString(),
//                                          taskID, ProcessDef, name, active);
//                }
//
//
//            }
//        } catch (Exception e) {
//            GlobalCC.EXCEPTIONREPORTING(null, e);
//        } finally {
//            bpm.endWorkFlowInstance(jbpmConfig);
//        }
//    }
    
  public void currentTcktProcessing(String ProcessDef) {
      String active = null;
      String processId = (String)session.getAttribute("processInstance");
      String taskID = GlobalCC.checkNullValues(session.getAttribute("ticketID"));//(String)session.getAttribute("ticketID");
      String name = (String)session.getAttribute("activityName");
      JBPMEngine bpm = JBPMEngine.getInstance();
      JbpmConfiguration jbpmConfig = new JbpmConfiguration();
//      jbpmConfig = bpm.startWorkFlowInstance();
     bpm.startWorkFlowInstance();
      try {
          if (processId != null) {
              name = bpm.findProcessInstanceTaskName(processId);
              System.out.println("name" + name);
              taskID = bpm.findProcessInstanceTaskId(processId);
              System.out.println("taskID" + taskID);
              session.setAttribute("activityName", name);
              session.setAttribute("ticketID", taskID);
              session.setAttribute("processInstance", processId);

              active = "Y";
              name = bpm.findProcessInstanceTaskName(processId);
              taskID = bpm.findProcessInstanceTaskId(processId);
              if (taskID != null) {
                  bpm.saveTicketDetails(processId, taskID, name);
              }
          }
      } catch (Exception e) {
          GlobalCC.EXCEPTIONREPORTING(null, e);
      } finally {
          bpm.endWorkFlowInstance(jbpmConfig);
      }
  }

    public void setParamPnl(RichPanelBox paramPnl) {
        this.paramPnl = paramPnl;
    }

    public RichPanelBox getParamPnl() {
        return paramPnl;
    }

    public void setPolNo(RichInputText polNo) {
        this.polNo = polNo;
    }

    public RichInputText getPolNo() {
        return polNo;
    }

    public void setPropLbl(RichOutputLabel propLbl) {
        this.propLbl = propLbl;
    }

    public RichOutputLabel getPropLbl() {
        return propLbl;
    }

    public void setPolLbl(RichOutputLabel polLbl) {
        this.polLbl = polLbl;
    }

    public RichOutputLabel getPolLbl() {
        return polLbl;
    }

    public void setSingleTask(RichCommandButton singleTask) {
        this.singleTask = singleTask;
    }

    public RichCommandButton getSingleTask() {
        return singleTask;
    }

    public void setMultiTasks(RichCommandButton multiTasks) {
        this.multiTasks = multiTasks;
    }

    public RichCommandButton getMultiTasks() {
        return multiTasks;
    }

    public void setTktType(RichSelectOneChoice tktType) {
        this.tktType = tktType;
    }

    public RichSelectOneChoice getTktType() {
        return tktType;
    }
    
    public void tktTypeChanged(ValueChangeEvent event) {
        if(event.getOldValue() != event.getNewValue()){
          session.setAttribute("tktType", event.getNewValue());            
          ADFUtils.findIterator("fetchUserTasksIterator").executeQuery();
          AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
        }
    }
    
    public void commonTktTypeChanged(ValueChangeEvent event) {
        if(event.getOldValue() != event.getNewValue()){
          session.setAttribute("tktType", event.getNewValue());            
          ADFUtils.findIterator("findCommonUserTasksIterator").executeQuery();
          AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);
        }
    }
    public String endWorkFlowInstance(JbpmConfiguration jbpmConfig) {
            System.out.println(" CLOSING BPM CONFIG.....");
            if (jbpmConfig != null) {

                try {
                    jbpmConfig.close();
                    System.out.println("closed");
                } catch (Exception e) {
                    jbpmConfig = null;
                    System.out.println("closed");
                    e.printStackTrace();
                }
            }
            return null;
        }
    
    

    public void setTxtAssignRemarks(RichInputText txtAssignRemarks) {
        this.txtAssignRemarks = txtAssignRemarks;
    }

    public RichInputText getTxtAssignRemarks() {
        return txtAssignRemarks;
    }
    
    public String clientWorkFlow() {

            try {
               
                //------------------------ 
                session.setAttribute("ProcessShtDesc",GlobalCC.clientProcess);
                session.setAttribute("ProcessAreaShtDesc", GlobalCC.Access);
                session.setAttribute("ProcessSubAShtDesc", GlobalCC.Access);
                session.setAttribute("sysModule", "C");
                session.setAttribute("ClientStatus", "D");
                session.setAttribute("ticketEntityCode",session.getAttribute("ClientCode"));
                session.setAttribute("viewMyticket", "Y");
                
                JBPMEngine wf = JBPMEngine.getInstance();  
                
                if(!wf.TicketsApp()){ 
                    return "success"; 
                }
                //start BPM Process...
                //String AccessGranted = wf.CheckUserRights();
                
               // if (!("Y".equalsIgnoreCase(AccessGranted))) { 
                     //GlobalCC.accessDenied();
                     //return null;
                 //}

            String workFlowId = null;
            workFlowId = wf.StartNewWorkflowInstance();
        
                    session.setAttribute("workflowID", workFlowId);
                
                
                workFlowId = GlobalCC.checkNullValues(workFlowId);
                
                if (workFlowId==null) {
                    GlobalCC.EXCEPTIONREPORTING("Failed To Create WorkFlow!");
                    return null;
                }
                return "success"; 
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            
            return null;

        }
    public String agentWorkFlow() {

            try {
               
                //------------------------ 
                session.setAttribute("ProcessShtDesc",GlobalCC.agentProcess);
                session.setAttribute("ProcessAreaShtDesc", GlobalCC.Access);
                session.setAttribute("ProcessSubAShtDesc", GlobalCC.Access);
                session.setAttribute("sysModule", "A");
                session.setAttribute("AgentStatus", "DRAFT");
                session.setAttribute("ticketEntityCode",session.getAttribute("AgentCode"));
                session.setAttribute("viewMyticket", "Y");
                
                JBPMEngine wf = JBPMEngine.getInstance();  
                
                if(!wf.TicketsApp()){ 
                    return "success"; 
                }
                //start BPM Process...
                //String AccessGranted = wf.CheckUserRights();
                
                // if (!("Y".equalsIgnoreCase(AccessGranted))) { 
                //    GlobalCC.accessDenied();
                //     return null;
                // } 
                
                String workFlowId =
                    wf.StartNewWorkflowInstance();
                session.setAttribute("workflowID", workFlowId);
                
                workFlowId = GlobalCC.checkNullValues(workFlowId);
                
                if (workFlowId == null) {
                    GlobalCC.EXCEPTIONREPORTING("Failed To Create WorkFlow!");
                    return null;
                }
                return "success"; 
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            
            return null;

        }
    public String fetchClientStatus() {
        
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        
        
        try {
            OracleCallableStatement cst = null;
            BigDecimal clientCode=GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
            conn = datahandler.getDatabaseConnection();
            String query =
                "begin tqc_clients_pkg.get_client_status(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1, OracleTypes.VARCHAR); 
            cst.setBigDecimal(2, clientCode);  
            cst.execute();
            session.setAttribute("ClientStatus",cst.getString(1));
            cst.close();
            conn.commit();
            conn.close();
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        } finally {
            //bpm.endWorkFlowInstance(jbpmConfig);
        }
        return null;
    }
    
    public String closeTicket()
      {
        DBConnector connector = DBConnector.getInstance();
        
        Connection conn = connector.getDatabaseConnection();
        CallableStatement cst = null;
        JUCtrlValueBinding r = null;
        Boolean Accept = Boolean.valueOf(false);
        int i = 0;
        int count = 0;
        try
        {
          String msgQuery;
          while (i < this.userTickets.getRowCount())
          {
            Accept = Boolean.valueOf(false);
            r = (JUCtrlValueBinding)this.userTickets.getRowData(i);
            if (r != null)
            {
              Accept = (Boolean)r.getAttribute("tckSelected");
              if (Accept.booleanValue())
              {
                msgQuery = "begin  tqc_web_pkg.close_tickets(?,?); end;";
                cst = conn.prepareCall(msgQuery);
                cst.setString(1, "ANY");
                cst.setString(2, r.getAttribute("taskID").toString());
                cst.execute();
                conn.commit();
                count++;
              }
            }
            i++;
          }
          if (count == 0)
          {
            GlobalCC.errorValueNotEntered("No Ticket Selected for Closer....");
            return null;
          }
          DbUtils.closeQuietly(conn, cst, null);

        ADFUtils.findIterator("fetchUserTasksIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(userTickets);  
        }
        catch (SQLException ex)
        {
          GlobalCC.EXCEPTIONREPORTING(ex);
        }
        finally
        {
          DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
      }
}
