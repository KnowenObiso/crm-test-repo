package TurnQuest.view.Alerts;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.LOVDAO;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.bpm.Bpmw;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;

import org.apache.log4j.Logger;

import org.jbpm.api.Configuration;
import org.jbpm.api.Deployment;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.job.Job;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.cfg.JbpmConfiguration;


public class JBPMEngine {
    
    final static Logger logger = Logger.getLogger(JBPMEngine.class);
    
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public JBPMEngine() { 
       
    }

    public static JBPMEngine getInstance() { 
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        JBPMEngine instance = (JBPMEngine) session.getAttribute("BpmInstance");
            if(instance == null) {
                    instance = new JBPMEngine();
                    session.setAttribute("BpmInstance", instance);
            }
            return instance;
    }
    public static void clearInstance() {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("BpmInstance");
    }

    private RepositoryService repositoryService = null;
    private ExecutionService executionService = null;
    private TaskService taskService = null;
    private IdentityService identityService = null;
    private ManagementService managementService = null;
    private ProcessEngine processEngine = null;


    public void setExecutionService(ExecutionService ptr) {
        executionService = ptr;
    }

    public ExecutionService getExecutionService() {
        return executionService;
    }

    public void setTaskService(TaskService ptr) {
        taskService = ptr;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setIdentityService(IdentityService identityService) {
        identityService = identityService;
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public String startWorkFlowInstance() {//initialize engine!
            try {
                if (!TicketsApp()) { 
                   return null;
                }   
                if (processEngine == null) {
                    processEngine = new Configuration().setResource("jbpm.cfg.xml")
                                            .buildProcessEngine();
                } 
                if (executionService == null) {
                        executionService = processEngine.getExecutionService();
                }
                if (taskService == null) {
                        taskService = processEngine.getTaskService();
                }

                if (identityService == null) {
                        identityService = processEngine.getIdentityService();
                }

                if (repositoryService == null) { 
                        repositoryService = processEngine.getRepositoryService(); 
                        List<String> found= new ArrayList<String>();
                        for(Deployment deployment: repositoryService.createDeploymentQuery().list()){ //Search for deployments if missing restore them 
                                for(String resource: repositoryService.getResourceNames(deployment.getId())){
                                        System.out.println("Repository Resource: "+resource); 
                                        found.add(resource);
                                }
                        }
                        if(!found.contains("ClientProcess.jpdl.xml")){//not found create!
                                System.out.println("Creating Resource: ClientProcess.jpdl.xml"); 
                                repositoryService.createDeployment().addResourceFromClasspath("ClientProcess.jpdl.xml").deploy(); 
                        }
                        if(!found.contains("AgentProcess.jpdl.xml")){//not found create!
                                System.out.println("Creating Resource: AgentProcess.jpdl.xml"); 
                                repositoryService.createDeployment().addResourceFromClasspath("AgentProcess.jpdl.xml").deploy(); 
                        } 
                } 
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            return null;
        }

    public String endWorkFlowInstance(JbpmConfiguration jbpmConfig) {
        System.out.println("CLOSING BPM CONFIG.....");
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

    public String createTask(String Assignee, String taskName, int priority) {
        Task task = taskService.newTask();
        task.setAssignee(Assignee);
        task.setName(taskName);
        task.setPriority(priority);
        return task.getExecutionId();
    }

    public String completeTask(String taskID) {
        taskService.completeTask(taskID);
        return null;
    }

    /**
     *
     *
     * @return PID of the workflow.
     */
    public String startWorkflow(String workflowDefinitionId,
                                Map<String, Object> parameters) {
        String pid = null;
        try {
            ProcessInstance processInstance =
                executionService.startProcessInstanceByKey(workflowDefinitionId,
                                                           parameters);
            pid = processInstance.getId();
            //processInstance.findActiveExecutionIn()
        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return pid;
    }

    public String findTaskName(String pid) {
        String taskName = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.activityName(pid).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            taskName = task.getActivityName();
            k++;
        }
        return taskName;

    }

    public String findTaskID(String pid) {
        String taskID = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.activityName(pid).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            taskID = task.getId();
            k++;
        }

        return taskID;

    }

    public Task findTask(String pid) {
        Task task = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.activityName(pid).list();
        int k = 0;
        while (k < Tasks.size()) {
            task = Tasks.get(k);
            k++;
        }
        return task;
    }

    public String reassignTask(String executionID,
                               String username) throws Exception {

        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.processInstanceId(executionID).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            task.setAssignee(username);
            taskService.saveTask(task);
            k++;
        }

        return null;
    }

    public List<Task> findTaskList(String userName) {
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskService.findPersonalTasks(userName);
        return Tasks;
    }

    public String takeTask(String userName, String ProcessID,
                           Map<String, Object> parameters) {
        List<Task> Tasks = new ArrayList<Task>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Tasks = taskQuery.processInstanceId(ProcessID).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            String taskExecId = task.getExecutionId();
            if (ProcessID.equalsIgnoreCase(taskExecId)) {
                taskService.assignTask(task.getId(), userName);
                taskService.completeTask(task.getId(), parameters);
            }
            k++;
        }
        return null;
    }

    public String resetProcessInstanceVariable(String processInstanceId,
                                               String variableName,
                                               Object value) {
        executionService.setVariable(processInstanceId, variableName, value);
        return null;
    }

    public String findProcessInstanceByExecution(String processInstanceId) {
        ProcessInstance processinstance = null;
        Execution execution =
            executionService.findExecutionById(processInstanceId).getProcessInstance();
        System.out.println(execution.getId());
        processinstance =
                executionService.signalExecutionById(processInstanceId);
        return processinstance.getId();
    }

    public List<Task> findTasksForWorkflowPath(String pathId) {
        List<Task> tasks = new ArrayList<Task>();
        TaskQuery taskQuery =
            taskService.createTaskQuery().processInstanceId(pathId);
        if (taskQuery == null) {

        } else {
            tasks = taskQuery.list();
        }
        return tasks;

    }

    public String signalProcessInstanceExecution(String processInstanceId) {
        executionService.signalExecutionById(processInstanceId);
        //executionService.signalExecutionById(arg0, arg1)
        return null;
    }

    public String signalProcessInstanceExecutionMap(String processInstanceId,
                                                    Map<String, Object> parameters) {
        // executionService.signalExecutionById(processInstanceId);
        executionService.signalExecutionById(processInstanceId, parameters);
        return null;
    }


    public Object findProcessInstanceVariable(String processInstanceId,
                                              String variableName) {
        return executionService.getVariable(processInstanceId, variableName);
    }


    public String startProcessInstanceById(String processInstanceId) {
        executionService.startProcessInstanceById(processInstanceId);
        return null;
    }

    public String findProcessInstanceState(String processInstanceId) {
        //ProcessInstanceQuery processInstanceq = executionService.findExecutionById(processInstanceId);
        ProcessInstance processInstance =
            executionService.findProcessInstanceById(processInstanceId);
        return processInstance.getState();
    }

    public String endProcessInstanceById(String processInstanceId,
                                         String State) {
        executionService.endProcessInstance(processInstanceId, State);
        ProcessInstance processInstance =
            executionService.findProcessInstanceById(processInstanceId);

        return processInstance.getState();
    }

    public String signalProcessInstanceExecution(String processInstanceVariables,
                                                 Map<String, Object> parameters) {
        ProcessInstance processInstance =
            executionService.signalExecutionById(processInstanceVariables,
                                                 parameters);
        return processInstance.getId();

    }

    public List<Task> findPersonalTasks(String userName) {
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskService.findPersonalTasks(userName);
        return Tasks;
    }

    public List<Task> findGroupTasks(String userName) {
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskService.findGroupTasks(userName);
        return Tasks;
    }

    public String reDeploy() {
        JBPMEngine jbpmEngine = JBPMEngine.getInstance();
        jbpmEngine.startWorkFlowInstance();
        ProcessEngine processEngine =
            new Configuration().setResource("jbpm.cfg.xml").buildProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        executionService = processEngine.getExecutionService();
        taskService = processEngine.getTaskService();
        repositoryService.createDeployment().addResourceFromClasspath("ClientProcessDefinition.jpdl.xml").deploy();
        return null;
    }

    public Task findActiveTaskByProcessId(String jbpmProcessId) {
        taskService = processEngine.getTaskService();
        List<Task> lstTasks =
            taskService.createTaskQuery().processInstanceId(jbpmProcessId).list();
        System.out.println("lstTasks: " + lstTasks.size());
        if (lstTasks != null && lstTasks.size() > 0)
            return lstTasks.get(0);
        else
            return null;
    }

    public String findProcessInstanceTaskId(String pid) {
        String taskID = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.processInstanceId(pid).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            taskID = task.getId();
            k++;
        }
        return taskID;

    }

    public String findProcessInstanceTaskName(String pid) {
        String taskName = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> Tasks = new ArrayList<Task>();
        Tasks = taskQuery.processInstanceId(pid).list();
        int k = 0;
        while (k < Tasks.size()) {
            Task task = Tasks.get(k);
            taskName = task.getName();
            k++;
        }
        return taskName;

    }

    public String findProcessInstanceTimer(String pid) {
        String jobId = null;
        List<Job> jobs =
            managementService.createJobQuery().processInstanceId(pid).list();
        int k = 0;
        while (k < jobs.size()) {
            Job job = jobs.get(k);
            jobId = job.getId();
            k++;
        }
        return jobId;

    }

    public String saveTaskAssignee(String assignee, String taskId) {
        taskService.assignTask(taskId, assignee);
        return null;
    }

    public Task createNewTask(String assignee, String description,
                              Date dueDate, String name) {
        Task task = taskService.newTask();
        task.setDescription(description);
        task.setAssignee(assignee);
        task.setDuedate(dueDate);
        task.setName(name);
        taskService.saveTask(task);
        return task;
    }

    public String completeTaskByTaskId(String taskID) {
        //NextTask();
        System.out.println("taskID==" + taskID);
        taskService.completeTask(taskID);
        return null;
    }

    public String deleteTicket(String ticketCode) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {
            String Query = "begin TQC_WEB_PKG.delete_task(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, ticketCode);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String saveTicketDetails(String pid, String taskID,
                                    String taskName) {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = (OracleConnection)datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;


        try {

            BigDecimal entityCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ticketEntityCode"));
            String processShtDesc =
                GlobalCC.checkNullValues(session.getAttribute("ProcessShtDesc"));
            String sysModule =
                GlobalCC.checkNullValues(session.getAttribute("sysModule"));

            if (pid == null) {
                GlobalCC.errorValueNotEntered("Error: Process Instance Not provided!");
                return null;
            }

            if (taskID == null) {
                GlobalCC.errorValueNotEntered("Error: Task  ID provided!");
                return null;
            }
            if (taskName == null) {
                GlobalCC.errorValueNotEntered("Error: Task Name Not provided!");
                return null;
            }
            if (processShtDesc == null) {
                GlobalCC.errorValueNotEntered("Error: ProcessShtDesc not provided!");
                return null;
            }

            if (sysModule == null) {
                GlobalCC.errorValueNotEntered("Error: System Module not provided!");
                return null;
            }
            String mktProposalNext =
                "begin TQC_WEB_PKG.save_process_dtls(?,?,?,?,?,?,?,?,?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(mktProposalNext);
            cst.setString(1, taskID); //ticket code
            cst.setInt(2, GlobalCC.sysCode); //sys Code
            cst.setString(3,
                          (String)session.getAttribute("sysModule")); //sys Module
            cst.setBigDecimal(4, entityCode); //e.g Clnt Code
            cst.setString(5, processShtDesc); //
            cst.setString(6, (String)session.getAttribute("Username")); //user
            cst.setString(7, taskName); //remarks
            cst.setBigDecimal(8,
                              (BigDecimal)session.getAttribute("transNo")); //trans No
            cst.setString(9, pid); //process Id
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String findProcessInstanceWhetherStarted(String ticketCode,
                                                    String type) {
        String started;
        started = null;
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        List<Bpmw> tasks = new ArrayList<Bpmw>();
        try {

            String userTasks =
                "begin ? := TQC_WEB_CURSOR.GetTicketCodeDetails(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(userTasks);
            System.out.println("ticketCode" + ticketCode);
            System.out.println("type" + type);

            //register out
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, ticketCode);
            cst.setString(3, type);
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            while (rs.next()) {
                Bpmw workflow = new Bpmw();
                workflow.setTaskID(rs.getString(4));
                workflow.setExecutionID(rs.getString(3));
                started = rs.getString(3);
                workflow.setTaskCreationDate(rs.getDate(7));
                workflow.setTaskPriority(rs.getInt(9));
                workflow.setActivityName(rs.getString(6));
                ////////////////////////////////////////////jaymo//////////////
                session.setAttribute("activityName", rs.getString(6));
                session.setAttribute("ticketID", rs.getString(4));
                workflow.setAssignee(rs.getString(8));
                session.setAttribute("currentAssignee", rs.getString(8));
                System.out.println(session.getAttribute("currentAssignee"));
                workflow.setTaskVariableID(rs.getBigDecimal(5));
                workflow.setTaskProcess(rs.getString(2));
                tasks.add(workflow);
            }
            rs.close();
            cst.close();
            conn.commit();
            conn.close();
            System.out.println("started" + started);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return started;

    }

    public String CheckUserRights() {
        String ans = null;
        DBConnector dbConn = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = dbConn.getDatabaseConnection();
            OracleCallableStatement cst = null;

            String connectionPackage =
                "begin ? := tqc_interfaces_pkg.check_user_rights(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(connectionPackage);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, (String)session.getAttribute("Username"));
            cst.setInt(3, GlobalCC.sysCode);
            cst.setString(4, (String)session.getAttribute("ProcessShtDesc"));
            cst.setString(5,
                          (String)session.getAttribute("ProcessAreaShtDesc"));
            cst.setString(6,
                          (String)session.getAttribute("ProcessSubAShtDesc"));
            cst.setBigDecimal(7,
                              (BigDecimal)session.getAttribute("authAmount"));
            cst.setString(8, (String)session.getAttribute("debitCredit"));
            cst.execute();
            ans = cst.getString(1);
            cst.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return ans;

    }

    /**
     *FindProcessDetails - Find Details about a Certain Process.
     * @return
     **/

    public String FindProcessDetails() {
        DBConnector myConn = DBConnector.getInstance();
        Connection conn = myConn.getDatabaseConnection();
        CallableStatement cst = null;
        ResultSet rst = null;
        try {
            String polQuery = "begin TQC_WEB_PKG.get_process_dtls(?,?,?);end;";
            cst = conn.prepareCall(polQuery);
            cst.setInt(1, GlobalCC.sysCode);
            cst.setString(2, (String)session.getAttribute("ProcessShtDesc"));
            cst.registerOutParameter(3, OracleTypes.CURSOR);

            cst.execute();
            rst = (ResultSet)cst.getObject(3);
            while (rst.next()) {
                session.setAttribute("ProcessBPMDef", rst.getString(4));
                System.out.println("ProcessBPMDef: " + rst.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, rst);
        }
        return null;
    }

    public Boolean checkTaskCompletion(String taskid, String shtDesc) {

        String MyTask = "N";
        DBConnector dbConn = DBConnector.getInstance();
        CallableStatement cst = null;
        Connection conn = null;
        try {
            conn = dbConn.getDatabaseConnection();
            String Complete =
                "BEGIN tqc_web_pkg.check_task_completion(?,?,?,?);END;";
            cst = conn.prepareCall(Complete);
            cst.setString(1, shtDesc);
            cst.setString(2, taskid);
            cst.setInt(3, GlobalCC.sysCode);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();

            MyTask = GlobalCC.checkNullValues(cst.getString(4));

            cst.close();
            conn.close();
            if (!"Y".equalsIgnoreCase(MyTask)) {
                String Message =
                    "the Task Selected Does not Correspond to the Activity being Performed. Cannot Complete";
                GlobalCC.INFORMATIONREPORTING(Message);
                return false;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return "Y".equalsIgnoreCase(MyTask);
    }

    public String reAssignTicket(String taskID, String username) {

        DBConnector dbConn = DBConnector.getInstance();
        Connection conn = dbConn.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            String Complete = "BEGIN tqc_web_pkg.reassign_task(?,?,?,?);END;";
            cst = conn.prepareCall(Complete);
            cst.setString(1, taskID);
            cst.setString(2, username);
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.execute();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return null;
    }

    public String processBpmTicket(String id, String processId, String type,
                                   String assignee, String sysModule,
                                   String workFlowType) {
        try {
            //  JBPMEngine bpm = new JBPMEngine();
            if (processId == null) {
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("type", type);
                variables.put("id", id);
                variables.put("assignee", assignee);
                session.setAttribute("decision", "Yes");
                processId = startWorkflow(workFlowType, variables);
            }
            String name = null;
            System.out.println("Process Id");
            System.out.println(processId);
            name = findProcessInstanceTaskName(processId);
            System.out.println("jaymo =====" + name);
            session.setAttribute("activityName", name);
            String taskID = null;
            taskID = findProcessInstanceTaskId(processId);
            System.out.println("Task Id");
            System.out.println(taskID);
            session.setAttribute("activityName", name);
            session.setAttribute("ticketID", taskID);
            session.setAttribute("processInstance", processId);
            session.setAttribute("module", sysModule);
            String active = null;
            active = "Y";
            if (taskID != null) {
                //                saveTicketDetails(processId, id, taskID, sysModule, name,
                //                                  active);
                saveTicketDetails(processId, taskID, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    /**
     *StartNewWorkflowInstance - Initiates a particular BPM Process
     * @return
     **/
    public String StartNewWorkflowInstance() {
        //  //System.out.println("Starting Reinstatement Process 3");
        DBConnector myConn = DBConnector.getInstance();
        Connection conn = null;
        String delink = "N";
        CallableStatement cst = null;
        String reinAuto = "No";
        String workFlow = null;
        String process = null;
        Map map = new HashMap();
        try {
            conn = myConn.getDatabaseConnection();
            clearProcessSession(); //clear process session
            FindProcessDetails(); //fetch "ProcessBPMDef" and "ProcessPDL"

            map.put("ClientStatus", session.getAttribute("ClientStatus"));
            map.put("AgentStatus", session.getAttribute("AgentStatus"));
            map.put("ServiceStatus", session.getAttribute("ServiceStatus"));
            map.put("assignee", (String)session.getAttribute("Username"));

            process =
                    GlobalCC.checkNullValues(session.getAttribute("ProcessBPMDef"));
            String bstring = null ;
            if(session.getAttribute("Username") != null){
                 bstring =  (String)session.getAttribute("Username");
                }

            workFlow =
                    startNewTicket(bstring, process,
                                   map);
        } catch (Exception e) {
          e.printStackTrace();
        }

        return workFlow;
    }

    public String startNewTicket(String user, String process,
                                 Map map) throws Exception {
        String processId = null;
        String taskId = null;
        String taskName = null;
        try {
            startWorkFlowInstance();
            
            if (process != null) {
                    ProcessInstance pinst = executionService.startProcessInstanceByKey(process, map); 
                    if (pinst != null) {
                            processId = pinst.getId();
                    }
            }
            
            Task task = null;
            //find active task
            List<Task> lstTasks =
                taskService.createTaskQuery().processInstanceId(processId).list();
            System.out.println("lstTasks: " + lstTasks.size());
            if (lstTasks != null && lstTasks.size() > 0) {
                task = lstTasks.get(0);
            }

            taskId = task.getId();
            taskName = task.getName();
            taskService.saveTask(task);
            taskService.assignTask(taskId, user);

            session.setAttribute("taskID", taskId);
            session.setAttribute("ticketID", taskId);
            session.setAttribute("activityName", taskName);

            session.setAttribute("executionID", processId);
            session.setAttribute("processInstance", processId);

            session.setAttribute("ProcessBPMDef", process);
            session.setAttribute("ProcessPDL", process);


            System.out.println("Loaded Task: " + taskId + " {" + taskName +
                               "} ");
            //CreateProcessAttributes(taskId, user, processId, taskName);
            saveTicketDetails(processId, taskId, taskName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }


    public ProcessInstance startProcessInstanceByKey(String strProcessName,
                                                     Map<String, Object> variables) {
        return executionService.startProcessInstanceByKey(strProcessName,
                                                          variables);
    }

    /**
     *CompleteTask - Complete the Current Task and Transition to the Next Task.
     * @return
     **/
    public String CompleteTask() {

        try {

            String CurrTask =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));
            String processId =
                GlobalCC.checkNullValues(session.getAttribute("executionID"));

            System.out.println("Task ID: " + CurrTask);
            System.out.println("Process ID: " + processId);

            if (CurrTask == null) {
                GlobalCC.INFORMATIONREPORTING("No Task Selected");
                return null;
            }

            if (processId == null) {
                GlobalCC.INFORMATIONREPORTING("No Process defined!");
                return null;
            }
            
            
            //init params
            Map map = new HashMap();
            map.put("ClientStatus", session.getAttribute("ClientStatus"));
            map.put("AgentStatus", session.getAttribute("AgentStatus"));
            map.put("ServiceStatus", session.getAttribute("ServiceStatus"));
            map.put("assignee", session.getAttribute("Username"));
            
            if (processEngine == null) {
                startWorkFlowInstance();
            }  
            taskService.setVariables(CurrTask, map);

            taskService.completeTask(CurrTask);

            Task task = null;
            List<Task> lstTasks =
                taskService.createTaskQuery().processInstanceId(processId).list();

            System.out.println("lstTasks: " + lstTasks.size());

            if (lstTasks != null && lstTasks.size() > 0) {
                task = lstTasks.get(0);
            }
            if (task == null) {

            } else {
                String taskId = task.getId();
                String taskName = task.getName();

                taskService.saveTask(task);
                taskService.assignTask(taskId,
                                       (String)session.getAttribute("Username"));

                session.setAttribute("taskID", taskId);
                session.setAttribute("ticketID", taskId);
                session.setAttribute("activityName", taskName);

                System.out.println("Loaded Task: " + taskId + " {" + taskName +
                                   "} "); 
                saveTicketDetails(processId, taskId, taskName);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearProcessSession() {
        session.removeAttribute("taskID");
        session.removeAttribute("ticketID");
        session.removeAttribute("activityName");
        session.removeAttribute("executionID");
        session.removeAttribute("processInstance");
        session.removeAttribute("ProcessBPMDef");
        session.removeAttribute("ProcessPDL");
        session.removeAttribute("workflowID");
    }
    // is ticketing engine turned on

    public Boolean TicketsApp() {
        String val = GlobalCC.getSysParamValue("TICKETS_APP");
        return "Y".equalsIgnoreCase(val);
    }

    public String closeTicket(String level, String code) {
        DBConnector connector = DBConnector.getInstance();
        Connection conn = connector.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            String msgQuery;
            msgQuery = "begin  tqc_web_pkg.close_tickets(?,?); end;";
            cst = conn.prepareCall(msgQuery);
            cst.setString(1, level);
            cst.setString(2, code);
            cst.execute();
            conn.commit();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String getCurrentEntityTask() {

        String taskId = null;
       

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;

        try {
            if(!TicketsApp()){
                 return null;  
            }
            String Username =
                (String)session.getAttribute("Username"); //This cursor expects the name and not the code.
            String processShtdesc =
                GlobalCC.checkNullValues(session.getAttribute("ticketProcessShtDesc"));
            BigDecimal entityCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ticketEntityCode"));
            
                if (Username == null) {
                    GlobalCC.errorValueNotEntered("Error: Please login");
                    return null;
                }

                if (processShtdesc == null) {
                    GlobalCC.errorValueNotEntered("Error: No Process Defined!");
                    return null;
                }

                if (entityCode == null) {
                    GlobalCC.errorValueNotEntered("Error: No Entity Selected!");
                    return null;
                }

                String userTasks =
                    "begin ? := TQC_WEB_PKG.get_entity_tckt_dtls(?,?,?,?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(userTasks);
                int k = 0;
                //register out
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setString(2, Username);
                cst.setInt(3, GlobalCC.sysCode); //v_tckt_code
                cst.setString(4, processShtdesc);
                cst.setBigDecimal(5, entityCode);

                cst.execute();

                OracleResultSet rs = (OracleResultSet)cst.getObject(1);

                if (rs.next()) {

                    //initialize engine;
                    clearProcessSession();

                    session.setAttribute("resetSessionMap", "N");

                    LOVDAO lov = new LOVDAO(); //clear

                    lov.reinitializeVariables();

                    String module = null;
                    String activityName = null;
                    String ticketType = null;
                    String processShtDesc = null;
                    String pid = null;
                    String entityName = null;
                    String entityRefNo = null;
                    String processPDL = null;
                    String ScreenName = null;

                    module = GlobalCC.checkNullValues(rs.getString("sysmodule")); 
                   
                    activityName = GlobalCC.checkNullValues(rs.getString("tckt_name"));
                    ticketType =
                            GlobalCC.checkNullValues(rs.getString("tckt_type"));
                    processShtDesc =
                            GlobalCC.checkNullValues(rs.getString("sprc_sht_desc"));
                    processPDL =
                            GlobalCC.checkNullValues(rs.getString("sprc_jpdl_desc"));
                    taskId = GlobalCC.checkNullValues(rs.getString("tckt_code"));
                    pid =
            GlobalCC.checkNullValues(rs.getString("tckt_process_id"));
                    entityName =
                            GlobalCC.checkNullValues(rs.getString("entity_name"));
                    entityRefNo =
                            GlobalCC.checkNullValues(rs.getString("entity_ref_no"));
                    ScreenName =
                            GlobalCC.checkNullValues(rs.getString("scrn_name"));

                    System.out.println("module ====================" + module);
                    System.out.println("activityName ====================" +
                                       activityName);
                    System.out.println("taskId ====================" + taskId);
                    System.out.println("processShtDesc ====================" +
                                       processShtDesc);
                    System.out.println("ProcessId ====================" + pid);
                    System.out.println("entityCode ====================" +
                                       entityCode);
                    System.out.println("entityName ====================" +
                                       entityName);
                    System.out.println("entityRefNo ====================" +
                                       entityRefNo);
                    System.out.println("processPDL ====================" +
                                       processPDL);
                    System.out.println("ScreenName ====================" +
                                       ScreenName);
                    System.out.println("ticketType ====================" +
                                       ticketType);

                    if (ScreenName == null) {
                        GlobalCC.errorValueNotEntered("Error: Navigation Screen not Defined!");
                        return null;
                    }

                    if (taskId == null) {
                        GlobalCC.errorValueNotEntered("Error: Navigation Task not Defined!");
                        return null;
                    }

                    if (processShtDesc == null) {
                        GlobalCC.errorValueNotEntered("Error: Navigation Process not Defined!");
                        return null;
                    }


                    //session.setAttribute("viewMyticket", "Y");

                    session.setAttribute("taskID", taskId);
                    session.setAttribute("ticketID", taskId);

                    session.setAttribute("executionID", pid);
                    session.setAttribute("processInstance", pid);

                    session.setAttribute("module", module);
                    session.setAttribute("sysModule", module);

                    session.setAttribute("ProcessBPMDef", processPDL);
                    session.setAttribute("ProcessPDL", processPDL);
                    session.setAttribute("ProcessShtDesc", processShtDesc);
                    session.setAttribute("ProcessAreaShtDesc", GlobalCC.Access);
                    session.setAttribute("ProcessSubAShtDesc", GlobalCC.Access);

                    session.setAttribute("activityName", activityName);

                    session.setAttribute("clientName", entityName);
                    session.setAttribute("agentName", entityName);
                    session.setAttribute("serviceProviderName", entityName);

                    session.setAttribute("ticketEntityCode", entityCode);
                    session.setAttribute("AgentCode", entityCode);
                    session.setAttribute("agentCode", entityCode);
                    session.setAttribute("serviceProviderCode", entityCode);
                    session.setAttribute("clientCode", entityCode);
                    session.setAttribute("ClientCode", entityCode);
                    session.setAttribute("prpCode", entityCode);

                    //step 1 check task point and fetch process details.

                    findProcessInstanceWhetherStarted(taskId, ticketType);

                    //step 4. transition ticket at this point if necessary.
                    //bpm.StartNewWorkflowInstance(processPDL);

                }

                rs.close();
                cst.close();
                conn.commit();
                conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskId;
    }
}
