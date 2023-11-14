package org.jbpm;


import TurnQuest.view.Alerts.JBPMEngine;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;


public class Escalate implements EventListener {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private static final long serialVersionUID = 1L;

    public OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;
        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);

            conn = (OracleConnection)ds.getConnection();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return conn;
    }

    public void notify(EventListenerExecution execution) {
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        JBPMEngine bpm = JBPMEngine.getInstance();
        String taskID;
        String taskName;
        String assignee = null;
        int sysCode = 0;
        taskID =
                bpm.findProcessInstanceTaskId(execution.getProcessInstance().getId());
        taskName =
                bpm.findProcessInstanceTaskName(execution.getProcessInstance().getId());

        String query = "begin TQC_SETUPS_PKG.escalate_proc(?,?,?,?,?,?); end;";
        try {
            conn = getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, taskID); // Task Id
            cst.setObject(2, sysCode); //System Code
            //System.out.println("PROCESS DEFINITION");
            //System.out.println(execution.getProcessInstance().getId());
            cst.setObject(3,
                          execution.getProcessDefinitionId()); //Process DefinitionId
            cst.setObject(4, taskName);
            cst.registerOutParameter(5, OracleTypes.NUMBER);
            cst.registerOutParameter(6, OracleTypes.VARCHAR);
            cst.execute();
            assignee = cst.getString(6);
            bpm.saveTaskAssignee(assignee, taskID);
            // TODO  Send Email to Assignee


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
