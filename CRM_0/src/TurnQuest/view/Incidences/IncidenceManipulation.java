package TurnQuest.view.Incidences;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class IncidenceManipulation {
    private RichInputText id;
    private RichInputText description;
    private RichSelectOneChoice owner;
    private RichTable incidTypeTable;
    private RichInputText actionID;
    private RichInputText actionStatus;
    private RichTable incActionTable;
    private RichTable branchesTable;
    private RichInputText branchDesc;
    private RichInputText deptDesc;
    private RichInputText deptID;
    private RichTable incidDeptTable;
    private RichTree systemsTree;
    private RichTable deptIncidTypesTable;
    private RichTable incidTypeLOV;
    private RichInputText incidTypeDesc;
    private RichSelectOneChoice sendMail;
    private RichSelectOneChoice priorityLevel;
    private RichSelectOneChoice escalate;
    private RichInputText escalateDays;
    private RichInputText closeDays;

    public IncidenceManipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public String AddIncidenceType() {
        try {
            id.setValue(null);
            description.setValue(null);
            owner.setValue(null);

            session.setAttribute("incdTypeCode", null);
            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "p2");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditIncidenceType() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceTypesIterator");
            RowKeySet set = incidTypeTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incdTypeCode",
                                     r.getAttribute("INCT_CODE"));
                id.setValue(r.getAttribute("INCT_SHT_DESC"));
                description.setValue(r.getAttribute("INCT_DESC"));
                owner.setValue(r.getAttribute("INCT_OWNER"));

                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "p2");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteIncidenceType() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceTypesIterator");
            RowKeySet set = incidTypeTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incdTypeCode",
                                     r.getAttribute("INCT_CODE"));
                id.setValue(r.getAttribute("INCT_SHT_DESC"));
                description.setValue(r.getAttribute("INCT_DESC"));
                owner.setValue(r.getAttribute("INCT_OWNER"));

                session.setAttribute("action", "D");
                SaveIncidenceType();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveIncidenceType() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            if (description.getValue() == null) {
                GlobalCC.sysInformation("Enter A Description.");
                return null;
            }

            String query =
                "begin TQC_INCIDENTS_WEB_PKG.save_incidence_type(?,?,?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incdTypeCode"));
            if (id.getValue() == null) {
                callStmt.setString(3, null);
            } else {
                callStmt.setString(3, id.getValue().toString());
            }
            if (description.getValue() == null) {
                callStmt.setString(4, null);
            } else {
                callStmt.setString(4, description.getValue().toString());
            }
            if (owner.getValue() == null) {
                callStmt.setString(5, null);
            } else {
                callStmt.setString(5, owner.getValue().toString());
            }

            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findIncidenceTypesIterator").executeQuery();
            GlobalCC.refreshUI(incidTypeTable);

            GlobalCC.dismissPopUp("crm", "p2");
            GlobalCC.INFORMATIONREPORTING("Record Saved Successfully");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    private String readValues() {
        //      Three ways to do Object Oriented Analysis
        //
        //      Conceptual model (Larman)
        //      Produce a “light” class diagram.
        //      CRC cards (Beck, Cunningham)
        //      Index cards and role playing.
        //       Analysis model with stereotypes (Jacobson)
        //      Boundaries, entities, control.
        //
        //      3.1 Domain Analysis
        //
        //      The process by which a software engineer learns about the domain to better understand the problem:
        //      The domain is the general field of business or technology in which the clients will use the software
        //      A domain expert is a person who has a deep  knowledge of the domain
        //
        //      Benefits of performing domain analysis:
        //      Faster development
        //      Better system
        //      Anticipation of extensions
        //
        //      Domain Analysis document
        //
        //      A.    Introduction
        //      B.    Glossary
        //      C.    General knowledge about the domain
        //      D.    Customers and users
        //      E.    The environment
        //      F.    Tasks and procedures currently performed
        //      G.  Competing software
        //      H.    Similarities to other domains
        //
        //
        //      Defining the Problem and the Scope
        //
        //      A problem can be expressed as:
        //      A difficulty the users or customers are facing,
        //      Or as an opportunity that will result in some benefit such as improved productivity or sales.
        //      The solution to the problem normally will entail developing software
        //      A good problem statement is short and succinct
        //
        //
        //      Defining the Scope
        //
        //      Narrow the scope by defining a more precise problem
        //      List all the things you might imagine the system doing
        //      Exclude some of these things if too broad
        //      Determine high-level goals if too narrow
        //
        //
        //
        //      3.4 What is a Requirement ?
        //
        //      It is a statement describing either
        //      1) an aspect of what the proposed system must do,
        //      or 2) a constraint on the system’s development.
        //      In either case it must contribute in some way towards adequately solving the customer’s problem;
        //      the set of requirements as a whole represents a negotiated agreement among the stakeholders.
        //
        //      A collection of requirements is a requirements document.


        return null;
    }

    public String AddIncidenceAction() {
        try {
            actionID.setValue(null);
            actionStatus.setValue(null);

            session.setAttribute("incdActionCode", null);
            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "p3");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditIncidenceAction() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceStatusesIterator");
            RowKeySet set = incActionTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incdActionCode",
                                     r.getAttribute("ICTY_CODE"));
                actionID.setValue(r.getAttribute("ICTY_SHT_DESC"));
                actionStatus.setValue(r.getAttribute("ICTY_DESC"));


                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "p3");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteIncidenceAction() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceStatusesIterator");
            RowKeySet set = incActionTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incdActionCode",
                                     r.getAttribute("ICTY_CODE"));
                actionID.setValue(r.getAttribute("ICTY_SHT_DESC"));
                actionStatus.setValue(r.getAttribute("ICTY_DESC"));

                session.setAttribute("action", "D");
                SaveIncidenceAction();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveIncidenceAction() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            if (actionStatus.getValue() == null) {
                GlobalCC.sysInformation("Enter A Description.");
                return null;
            }

            String query =
                "begin TQC_INCIDENTS_WEB_PKG.save_incidence_action(?,?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incdActionCode"));
            if (actionID.getValue() == null) {
                callStmt.setString(3, null);
            } else {
                callStmt.setString(3, actionID.getValue().toString());
            }
            if (actionStatus.getValue() == null) {
                callStmt.setString(4, null);
            } else {
                callStmt.setString(4, actionStatus.getValue().toString());
            }

            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findIncidenceStatusesIterator").executeQuery();
            GlobalCC.refreshUI(incActionTable);
            GlobalCC.dismissPopUp("crm", "p3");
            GlobalCC.INFORMATIONREPORTING("Record Saved successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void SystemsTreeListener(SelectionEvent selectionEvent) {
        // Add event code here...
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    systemsTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)systemsTree.getRowData();

                    session.setAttribute("incSysCode",
                                         nd.getRow().getAttribute("sysCode"));

                    session.setAttribute("BRNCode", null);
                    session.setAttribute("incDeptCode", null);

                    ADFUtils.findIterator("findIncidenceDepartmentsIterator").executeQuery();
                    GlobalCC.refreshUI(incidDeptTable);

                    ADFUtils.findIterator("findSystemBranchesIterator").executeQuery();
                    GlobalCC.refreshUI(branchesTable);

                    ADFUtils.findIterator("findIDepartmentIncidTypesIterator").executeQuery();
                    GlobalCC.refreshUI(deptIncidTypesTable);


                }
            }
        }
    }

    public String AddIncidenceDepartment() {
        try {
            deptID.setValue(null);
            deptDesc.setValue(null);
            branchDesc.setValue(null);

            session.setAttribute("BRNCode", null);
            session.setAttribute("incDeptCode", null);
            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "p4");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditIncidenceDepartment() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceDepartmentsIterator");
            RowKeySet set = incidDeptTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incDeptCode",
                                     r.getAttribute("IDEP_CODE"));
                session.setAttribute("BRNCode",
                                     r.getAttribute("IDEP_BRN_CODE"));

                deptID.setValue(r.getAttribute("IDEP_SHT_DESC"));
                deptDesc.setValue(r.getAttribute("IDEP_DESC"));
                branchDesc.setValue(r.getAttribute("BRN_NAME"));


                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "p4");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String findSystemBranch() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSystemBranchesIterator");
            RowKeySet set = branchesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("BRNCode", r.getAttribute("BRN_CODE"));
                branchDesc.setValue(r.getAttribute("BRN_NAME"));

                GlobalCC.refreshUI(branchDesc);

                GlobalCC.dismissPopUp("crm", "p5");
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteIncidenceDepartment() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceDepartmentsIterator");
            RowKeySet set = incidDeptTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incDeptCode",
                                     r.getAttribute("IDEP_CODE"));
                session.setAttribute("BRNCode",
                                     r.getAttribute("IDEP_BRN_CODE"));

                deptID.setValue(r.getAttribute("IDEP_SHT_DESC"));
                deptDesc.setValue(r.getAttribute("IDEP_DESC"));
                branchDesc.setValue(r.getAttribute("BRN_NAME"));


                session.setAttribute("action", "D");
                SaveIncidenceDepartment();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveIncidenceDepartment() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            if (deptDesc.getValue() == null) {
                GlobalCC.sysInformation("Enter A Description.");
                return null;
            }

            String query =
                "begin TQC_INCIDENTS_WEB_PKG.save_incidence_dept(?,?,?,?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("incDeptCode"));
            if (deptID.getValue() == null) {
                callStmt.setString(3, null);
            } else {
                callStmt.setString(3, deptID.getValue().toString());
            }
            if (deptDesc.getValue() == null) {
                callStmt.setString(4, null);
            } else {
                callStmt.setString(4, deptDesc.getValue().toString());
            }
            callStmt.setBigDecimal(5,
                                   (BigDecimal)session.getAttribute("BRNCode"));
            callStmt.setBigDecimal(6,
                                   (BigDecimal)session.getAttribute("incSysCode"));
            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findIncidenceDepartmentsIterator").executeQuery();
            GlobalCC.refreshUI(incidDeptTable);
            GlobalCC.dismissPopUp("crm", "p4");
            GlobalCC.INFORMATIONREPORTING("Record saved successfully");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String AddDepartmentIncidenceType() {
        try {
            incidTypeDesc.setValue(null);
            sendMail.setValue("Y");
            priorityLevel.setValue("M");
            escalate.setValue("Y");
            escalateDays.setValue(null);
            closeDays.setValue(null);

            session.setAttribute("incdTypeCode1", null);
            session.setAttribute("DeptincCode", null);
            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "p6");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditDepartmentIncidenceType() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIDepartmentIncidTypesIterator");
            RowKeySet set = deptIncidTypesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("incdTypeCode1",
                                     r.getAttribute("DEPICT_INCT_CODE"));
                session.setAttribute("DeptincCode",
                                     r.getAttribute("DEPICT_CODE"));

                incidTypeDesc.setValue(r.getAttribute("INCT_SHT_DESC"));
                sendMail.setValue(r.getAttribute("DEPICT_SEND_EMAIL"));
                priorityLevel.setValue(r.getAttribute("DEPICT_PRIORITY_LVL"));
                escalate.setValue(r.getAttribute("DEPICT_ESCALATE"));
                escalateDays.setValue(r.getAttribute("DEPICT_ESC_DAYS"));
                closeDays.setValue(r.getAttribute("DEPICT_CLOSE_DAYS"));


                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "p6");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String findIncidenceType() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceTypesIterator");
            RowKeySet set = incidTypeLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("incdTypeCode1",
                                     r.getAttribute("INCT_CODE"));
                incidTypeDesc.setValue(r.getAttribute("INCT_SHT_DESC"));

                GlobalCC.refreshUI(incidTypeDesc);

                GlobalCC.dismissPopUp("crm", "p7");
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteDepartmentIncidenceType() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIDepartmentIncidTypesIterator");
            RowKeySet set = deptIncidTypesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("incdTypeCode1",
                                     r.getAttribute("DEPICT_INCT_CODE"));
                session.setAttribute("DeptincCode",
                                     r.getAttribute("DEPICT_CODE"));

                incidTypeDesc.setValue(r.getAttribute("INCT_SHT_DESC"));
                sendMail.setValue(r.getAttribute("DEPICT_SEND_EMAIL"));
                priorityLevel.setValue(r.getAttribute("DEPICT_PRIORITY_LVL"));
                escalate.setValue(r.getAttribute("DEPICT_ESCALATE"));
                escalateDays.setValue(r.getAttribute("DEPICT_ESC_DAYS"));
                closeDays.setValue(r.getAttribute("DEPICT_CLOSE_DAYS"));


                session.setAttribute("action", "D");
                SaveDepartmentIncidenceType();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveDepartmentIncidenceType() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            if (incidTypeDesc.getValue() == null) {
                GlobalCC.sysInformation("Select An Incidence Type.");
                return null;
            }

            String query =
                "begin TQC_INCIDENTS_WEB_PKG.save_dept_incidence_type(?,?,?,?,?,?,?,?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("DeptincCode"));
            callStmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("incdTypeCode1"));
            if (incidTypeDesc.getValue() == null) {
                callStmt.setString(4, null);
            } else {
                callStmt.setString(4, incidTypeDesc.getValue().toString());
            }
            callStmt.setBigDecimal(5,
                                   (BigDecimal)session.getAttribute("incDeptCode"));
            if (sendMail.getValue() == null) {
                callStmt.setString(6, null);
            } else {
                callStmt.setString(6, sendMail.getValue().toString());
            }
            if (priorityLevel.getValue() == null) {
                callStmt.setString(7, null);
            } else {
                callStmt.setString(7, priorityLevel.getValue().toString());
            }
            if (escalate.getValue() == null) {
                callStmt.setString(8, null);
            } else {
                callStmt.setString(8, escalate.getValue().toString());
            }
            if (escalateDays.getValue() == null) {
                callStmt.setString(9, null);
            } else {
                callStmt.setString(9, escalateDays.getValue().toString());
            }
            if (closeDays.getValue() == null) {
                callStmt.setString(10, null);
            } else {
                callStmt.setString(10, closeDays.getValue().toString());
            }

            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findIDepartmentIncidTypesIterator").executeQuery();
            GlobalCC.refreshUI(deptIncidTypesTable);
            GlobalCC.dismissPopUp("crm", "p6");
            GlobalCC.INFORMATIONREPORTING("Record Saved successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void setId(RichInputText id) {
        this.id = id;
    }

    public RichInputText getId() {
        return id;
    }

    public void setDescription(RichInputText description) {
        this.description = description;
    }

    public RichInputText getDescription() {
        return description;
    }

    public void setOwner(RichSelectOneChoice owner) {
        this.owner = owner;
    }

    public RichSelectOneChoice getOwner() {
        return owner;
    }

    public void setIncidTypeTable(RichTable incidTypeTable) {
        this.incidTypeTable = incidTypeTable;
    }

    public RichTable getIncidTypeTable() {
        return incidTypeTable;
    }

    public void setActionID(RichInputText actionID) {
        this.actionID = actionID;
    }

    public RichInputText getActionID() {
        return actionID;
    }

    public void setActionStatus(RichInputText actionStatus) {
        this.actionStatus = actionStatus;
    }

    public RichInputText getActionStatus() {
        return actionStatus;
    }

    public void setIncActionTable(RichTable incActionTable) {
        this.incActionTable = incActionTable;
    }

    public RichTable getIncActionTable() {
        return incActionTable;
    }

    public void setBranchesTable(RichTable branchesTable) {
        this.branchesTable = branchesTable;
    }

    public RichTable getBranchesTable() {
        return branchesTable;
    }

    public void setBranchDesc(RichInputText branchDesc) {
        this.branchDesc = branchDesc;
    }

    public RichInputText getBranchDesc() {
        return branchDesc;
    }

    public void setDeptDesc(RichInputText deptDesc) {
        this.deptDesc = deptDesc;
    }

    public RichInputText getDeptDesc() {
        return deptDesc;
    }

    public void setDeptID(RichInputText deptID) {
        this.deptID = deptID;
    }

    public RichInputText getDeptID() {
        return deptID;
    }

    public void setIncidDeptTable(RichTable incidDeptTable) {
        this.incidDeptTable = incidDeptTable;
    }

    public RichTable getIncidDeptTable() {
        return incidDeptTable;
    }

    public void setSystemsTree(RichTree systemsTree) {
        this.systemsTree = systemsTree;
    }

    public RichTree getSystemsTree() {
        return systemsTree;
    }

    public void setDeptIncidTypesTable(RichTable deptIncidTypesTable) {
        this.deptIncidTypesTable = deptIncidTypesTable;
    }

    public RichTable getDeptIncidTypesTable() {
        return deptIncidTypesTable;
    }

    public void setIncidTypeLOV(RichTable incidTypeLOV) {
        this.incidTypeLOV = incidTypeLOV;
    }

    public RichTable getIncidTypeLOV() {
        return incidTypeLOV;
    }

    public void setIncidTypeDesc(RichInputText incidTypeDesc) {
        this.incidTypeDesc = incidTypeDesc;
    }

    public RichInputText getIncidTypeDesc() {
        return incidTypeDesc;
    }

    public void setSendMail(RichSelectOneChoice sendMail) {
        this.sendMail = sendMail;
    }

    public RichSelectOneChoice getSendMail() {
        return sendMail;
    }

    public void setPriorityLevel(RichSelectOneChoice priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public RichSelectOneChoice getPriorityLevel() {
        return priorityLevel;
    }

    public void setEscalate(RichSelectOneChoice escalate) {
        this.escalate = escalate;
    }

    public RichSelectOneChoice getEscalate() {
        return escalate;
    }

    public void setEscalateDays(RichInputText escalateDays) {
        this.escalateDays = escalateDays;
    }

    public RichInputText getEscalateDays() {
        return escalateDays;
    }

    public void setCloseDays(RichInputText closeDays) {
        this.closeDays = closeDays;
    }

    public RichInputText getCloseDays() {
        return closeDays;
    }

    public void IncidenceDeptListener(SelectionEvent selectionEvent) {
        try {
            // Add event code here...
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findIncidenceDepartmentsIterator");
            RowKeySet set = incidDeptTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("incDeptCode",
                                     r.getAttribute("IDEP_CODE"));

                ADFUtils.findIterator("findIDepartmentIncidTypesIterator").executeQuery();
                GlobalCC.refreshUI(deptIncidTypesTable);


            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public String actionHideBranch() {
        GlobalCC.dismissPopUp("crm", "p5");
        return null;
    }

    public String actionHideIncidenceType() {
        GlobalCC.dismissPopUp("crm", "p7");
        return null;
    }
}
