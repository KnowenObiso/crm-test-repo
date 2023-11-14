package TurnQuest.view.Memos;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class memoManipulation {
    private RichTree memoTypeTree;
    private RichTable subClassLOV;
    private RichInputText sclDesc;
    private RichInputText memoType;
    private RichSelectOneChoice memoAppl;
    private RichInputText memoSubject;
    private RichTree subjectTree;
    private RichInputNumberSpinbox mtypCode;
    private RichInputNumberSpinbox mtypSysCode;
    private RichSelectOneChoice mtypStatus;
    private RichInputText mtypSubCode;
    private RichPanelBox memoTypesPan;
    private RichPanelGroupLayout memoTypesPanDetails;
    private RichInputNumberSpinbox memoCode;
    private RichInputNumberSpinbox memoMtypCode;
    private RichInputNumberSpinbox memdetCode;
    private RichInputNumberSpinbox memdetMemoCode;
    private RichInputText memdetContent;
    private RichPanelGroupLayout memoDetails;
    private RichPanelBox memoDetailsPan;
    private RichInputText txtAppAreaDesc;
    private RichInputText txtAppAreaCode;
    private RichTable tblAppAreaLOV;
    private RichSelectOneChoice applLvl;

    public memoManipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void setMtypCode(RichInputNumberSpinbox mtypCode) {
        this.mtypCode = mtypCode;
    }

    public RichInputNumberSpinbox getMtypCode() {
        return mtypCode;
    }

    public void setMtypSysCode(RichInputNumberSpinbox mtypSysCode) {
        this.mtypSysCode = mtypSysCode;
    }

    public RichInputNumberSpinbox getMtypSysCode() {
        return mtypSysCode;
    }

    public void setMtypStatus(RichSelectOneChoice mtypStatus) {
        this.mtypStatus = mtypStatus;
    }

    public RichSelectOneChoice getMtypStatus() {
        return mtypStatus;
    }

    public void setMtypSubCode(RichInputText mtypSubCode) {
        this.mtypSubCode = mtypSubCode;
    }

    public RichInputText getMtypSubCode() {
        return mtypSubCode;
    }

    public void setMemoTypeTree(RichTree memoTypeTree) {
        this.memoTypeTree = memoTypeTree;
    }

    public RichTree getMemoTypeTree() {
        return memoTypeTree;
    }

    public void setSubClassLOV(RichTable subClassLOV) {
        this.subClassLOV = subClassLOV;
    }

    public RichTable getSubClassLOV() {
        return subClassLOV;
    }

    public void setSclDesc(RichInputText sclDesc) {
        this.sclDesc = sclDesc;
    }

    public RichInputText getSclDesc() {
        return sclDesc;
    }

    public void setMemoType(RichInputText memoType) {
        this.memoType = memoType;
    }

    public RichInputText getMemoType() {
        return memoType;
    }

    public void setMemoAppl(RichSelectOneChoice memoAppl) {
        this.memoAppl = memoAppl;
    }

    public RichSelectOneChoice getMemoAppl() {
        return memoAppl;
    }

    public void setMemoSubject(RichInputText memoSubject) {
        this.memoSubject = memoSubject;
    }

    public RichInputText getMemoSubject() {
        return memoSubject;
    }

    public void setSubjectTree(RichTree subjectTree) {
        this.subjectTree = subjectTree;
    }

    public RichTree getSubjectTree() {
        return subjectTree;
    }

    public void setMemoTypesPan(RichPanelBox memoTypesPan) {
        this.memoTypesPan = memoTypesPan;
    }

    public RichPanelBox getMemoTypesPan() {
        return memoTypesPan;
    }

    public void setMemoTypesPanDetails(RichPanelGroupLayout memoTypesPanDetails) {
        this.memoTypesPanDetails = memoTypesPanDetails;
    }

    public RichPanelGroupLayout getMemoTypesPanDetails() {
        return memoTypesPanDetails;
    }

    public void setMemoCode(RichInputNumberSpinbox memoCode) {
        this.memoCode = memoCode;
    }

    public RichInputNumberSpinbox getMemoCode() {
        return memoCode;
    }

    public void setMemoMtypCode(RichInputNumberSpinbox memoMtypCode) {
        this.memoMtypCode = memoMtypCode;
    }

    public RichInputNumberSpinbox getMemoMtypCode() {
        return memoMtypCode;
    }

    public void setMemdetCode(RichInputNumberSpinbox memdetCode) {
        this.memdetCode = memdetCode;
    }

    public RichInputNumberSpinbox getMemdetCode() {
        return memdetCode;
    }

    public void setMemdetMemoCode(RichInputNumberSpinbox memdetMemoCode) {
        this.memdetMemoCode = memdetMemoCode;
    }

    public RichInputNumberSpinbox getMemdetMemoCode() {
        return memdetMemoCode;
    }

    public void setMemdetContent(RichInputText memdetContent) {
        this.memdetContent = memdetContent;
    }

    public RichInputText getMemdetContent() {
        return memdetContent;
    }

    public void setMemoDetails(RichPanelGroupLayout memoDetails) {
        this.memoDetails = memoDetails;
    }

    public RichPanelGroupLayout getMemoDetails() {
        return memoDetails;
    }

    public void setMemoDetailsPan(RichPanelBox memoDetailsPan) {
        this.memoDetailsPan = memoDetailsPan;
    }

    public RichPanelBox getMemoDetailsPan() {
        return memoDetailsPan;
    }

    public void MemoListenerSelectionListener(SelectionEvent evt) {
        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = this.memoTypeTree.getSelectedRowKeys();
            Object key2 = rowKeySet.iterator().next();
            this.memoTypeTree.setRowKey(key2);

            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)this.memoTypeTree.getRowData();

            String type = (String)nodeBinding.getAttribute("type");
            if (type.equalsIgnoreCase("MT")) {
                memoTypesPan.setRendered(true);
                session.setAttribute("sysCode",
                                     nodeBinding.getAttribute("mtypSysCode"));
                session.setAttribute("mtypCode",
                                     nodeBinding.getAttribute("mtypCode"));
                mtypSysCode.setValue(nodeBinding.getAttribute("mtypSysCode"));
                mtypCode.setValue(nodeBinding.getAttribute("mtypCode"));
                memoType.setValue(nodeBinding.getAttribute("mtypMemoType"));
                sclDesc.setValue(nodeBinding.getAttribute("sclDesc"));
                txtAppAreaCode.setValue(nodeBinding.getAttribute("appAreaCode"));
                txtAppAreaDesc.setValue(nodeBinding.getAttribute("appAreaDesc"));
                applLvl.setValue(nodeBinding.getAttribute("applLvl"));
                mtypStatus.setValue(nodeBinding.getAttribute("mtypStatus"));
                mtypSubCode.setValue(nodeBinding.getAttribute("mtypSubCode"));
                session.setAttribute("sclCode",
                                     nodeBinding.getAttribute("mtypSubCode"));
                clearMemoDetailsFields();

            } else if (type.equalsIgnoreCase("S")) {
                memoTypesPan.setRendered(false);
                session.setAttribute("sysCode",
                                     nodeBinding.getAttribute("sysCode"));
            }


            ADFUtils.findIterator("findMemoSubjectDetailsIterator").executeQuery();
            ADFUtils.findIterator("fetchSystemApplicableAreasIterator").executeQuery();
            GlobalCC.refreshUI(tblAppAreaLOV);

            GlobalCC.refreshUI(subjectTree);
            GlobalCC.refreshUI(memoTypesPanDetails);
        }
    }


    public void SubjectSelectionListener(SelectionEvent evt) {
        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = this.subjectTree.getSelectedRowKeys();
            Object key2 = rowKeySet.iterator().next();
            this.subjectTree.setRowKey(key2);

            JUCtrlValueBinding r =
                (JUCtrlValueBinding)this.subjectTree.getRowData();

            session.setAttribute("memoCode", r.getAttribute("memoCode"));
            session.setAttribute("memdetCode", r.getAttribute("memdetCode"));

            memoDetailsPan.setRendered(true);
            memoSubject.setValue(r.getAttribute("memoSubject"));
            memoCode.setValue(r.getAttribute("memoCode"));
            memoMtypCode.setValue(r.getAttribute("memoMtypCode"));
            memdetCode.setValue(r.getAttribute("memdetCode"));
            memdetContent.setValue(r.getAttribute("memdetContent"));
            memdetMemoCode.setValue(r.getAttribute("memdetMemoCode"));
            sclDesc.setValue(r.getAttribute("sclDesc"));
            mtypSubCode.setValue(r.getAttribute("sclCode"));
            session.setAttribute("sclCode", r.getAttribute("sclCode"));


            GlobalCC.refreshUI(memoDetails);

        }
    }

    public void actionConfirmDeleteMemoDetails1(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DeleteMemoDetails();
        }

        // Add event code here...
    }

    public void actionConfirmDeleteMemoType1(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DeleteMemoType();
        }

        // Add event code here...
    }

    public String SubClassSelected() {
        RowKeySet rowKeySet = subClassLOV.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        subClassLOV.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)subClassLOV.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        mtypSubCode.setValue(r.getAttribute("sclCode"));
        session.setAttribute("sclCode", r.getAttribute("sclCode"));
        sclDesc.setValue(r.getAttribute("sclDesc"));


        GlobalCC.refreshUI(mtypSubCode);
        GlobalCC.refreshUI(sclDesc);
        GlobalCC.dismissPopUp("crm", "p2");

        return null;
    }

    public String AddMemoType() {
        try {
            if (session.getAttribute("sysCode") == null) {
                GlobalCC.errorValueNotEntered("Erroe Value Missing: Select A System");
                return null;
            }
            memoTypesPan.setRendered(true);
            session.setAttribute("subClassCode", null);
            session.setAttribute("mtypCode", null);
            memoType.setValue(null);
            sclDesc.setValue(null);
            if (memoAppl != null) {
                memoAppl.setValue(null);
            }
            mtypSysCode.setValue(session.getAttribute("sysCode"));
            mtypCode.setValue(null);
            mtypStatus.setValue(null);
            mtypSubCode.setValue(null);
            txtAppAreaCode.setValue(null);
            txtAppAreaDesc.setValue(null);
            session.removeAttribute("sclCode");
            ADFUtils.findIterator("findMemoSubjectDetailsIterator").executeQuery();


            GlobalCC.refreshUI(subjectTree);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveMemoType() {

        String memoTypeVal;
        String sclDescVal;
        String memoApAreaCode;
        String mtypSysCodeVal;
        String mtypCodeVal;
        String mtypStatusVal;
        String mtypSubCodeVal;
        String applLvlVal;

        memoTypeVal = GlobalCC.checkNullValues(memoType.getValue());
        sclDescVal = GlobalCC.checkNullValues(sclDesc.getValue());
        memoApAreaCode = GlobalCC.checkNullValues(txtAppAreaCode.getValue());
        mtypSysCodeVal = GlobalCC.checkNullValues(mtypSysCode.getValue());
        mtypCodeVal = GlobalCC.checkNullValues(mtypCode.getValue());
        mtypStatusVal = GlobalCC.checkNullValues(mtypStatus.getValue());
        mtypSubCodeVal = GlobalCC.checkNullValues(mtypSubCode.getValue());
        applLvlVal = GlobalCC.checkNullValues(applLvl.getValue());


        if (memoTypeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Memo Type");
            return null;
        }
        if (memoApAreaCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Application Level");
            return null;
        }
        if (mtypSysCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select System");
            return null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        try {

            String query =
                "begin TQC_WEB_PKG.save_memo_type(?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (mtypCodeVal == null) {
                cst.setString(1, "A");
            } else {
                cst.setString(1, "E");
            }
            cst.setString(2, mtypCodeVal);
            cst.setString(3, mtypSysCodeVal);
            cst.setString(4, memoTypeVal);
            cst.setString(5, memoApAreaCode);
            cst.setString(6, mtypStatusVal);
            cst.setString(7, mtypSubCodeVal);
            cst.setString(8, applLvlVal);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Successfully Saved");
            ADFUtils.findIterator("findSystemMemosIterator").executeQuery();
            GlobalCC.refreshUI(memoTypeTree);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public void selectMemoListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    memoTypeTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)memoTypeTree.getRowData();

                    Object memTyp = nd.getAttribute("mtypCode");
                    if (memTyp != null) {
                        session.setAttribute("memType", memTyp);

                    }

                }

            }
        }
    }

    public String DeleteMemoType() {
        if (session.getAttribute("mtypCode") != null) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();
                String query =
                    "begin TQC_WEB_PKG.save_memo_type(?,?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("mtypCode"));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, null);
                cst.setString(7, null);
                cst.setString(8, null);
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.sysInformation("Record Sucessfully Deleted");
                ADFUtils.findIterator("findSystemMemosIterator").executeQuery();
                GlobalCC.refreshUI(memoTypeTree);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need to select a record to proceed.");
            return null;
        }
        return null;
    }

    public void clearMemoDetailsFields() {
        memoCode.setValue(null);
        memoSubject.setValue(null);
        memoMtypCode.setValue(session.getAttribute("mtypCode"));
        memdetCode.setValue(null);
        memdetContent.setValue(null);
        memdetMemoCode.setValue(null);
    }

    public String AddMemoDetails() {
        try {
            if (session.getAttribute("mtypCode") == null) {
                GlobalCC.errorValueNotEntered("Select A Memo Type");
                return null;
            }
            memoDetailsPan.setRendered(true);
            clearMemoDetailsFields();
            GlobalCC.refreshUI(memoDetails);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveMemoDetails() {
        String memoCodeVal;
        String memoSubjectVal;
        String memoMtypCodeVal;
        String memdetCodeVal;
        String memdetContentVal;
        String memdetMemoCodeVal;


        memoCodeVal = GlobalCC.checkNullValues(memoCode.getValue());
        memoSubjectVal = GlobalCC.checkNullValues(memoSubject.getValue());
        memoMtypCodeVal = GlobalCC.checkNullValues(memoMtypCode.getValue());
        memdetCodeVal = GlobalCC.checkNullValues(memdetCode.getValue());
        memdetContentVal = GlobalCC.checkNullValues(memdetContent.getValue());


        memdetMemoCodeVal =
                GlobalCC.checkNullValues(memdetMemoCode.getValue());

        if (memoMtypCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Memo Type");
            return null;
        }
        if (memoSubjectVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Memo Subject");
            return null;
        }
        if (memdetContentVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Memo Content");
            return null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;

        try {
            conn = dbCon.getDatabaseConnection();
            String query =
                "begin TQC_WEB_PKG.save_memo_details(?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (memoCodeVal == null) {
                cst.setString(1, "A");
            } else {
                cst.setString(1, "E");
            }
            cst.setString(2, memoSubjectVal);
            cst.setString(3, memdetContentVal);
            cst.setString(4, memoCodeVal);
            cst.setString(5, memoMtypCodeVal);
            cst.setString(6, memdetCodeVal);
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();

            clearMemoDetailsFields();

            GlobalCC.sysInformation("Record Successfully Saved");

            ADFUtils.findIterator("findMemoSubjectDetailsIterator").executeQuery();
            GlobalCC.refreshUI(subjectTree);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String DeleteMemoDetails() {

        if (session.getAttribute("memoCode") != null &&
            session.getAttribute("memdetCode") != null) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();
                String query =
                    "begin TQC_WEB_PKG.save_memo_details(?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setString(2, null);
                cst.setString(3, null);
                cst.setBigDecimal(4,
                                  (BigDecimal)session.getAttribute("memoCode"));
                cst.setBigDecimal(5, null);
                cst.setBigDecimal(6,
                                  (BigDecimal)session.getAttribute("memdetCode"));
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                memoCode.setValue(null);
                memoSubject.setValue(null);
                memoMtypCode.setValue(null);
                memdetCode.setValue(null);
                memdetContent.setValue(null);
                memdetMemoCode.setValue(null);
                txtAppAreaDesc.setValue(null);
                txtAppAreaCode.setValue(null);

                GlobalCC.sysInformation("Record Successfully Deleted");
                ADFUtils.findIterator("findMemoSubjectDetailsIterator").executeQuery();
                GlobalCC.refreshUI(subjectTree);
                GlobalCC.refreshUI(memoDetails);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need to select a record to procced.");
            return null;
        }
        return null;
    }


    public void setTxtAppAreaDesc(RichInputText txtAppAreaDesc) {
        this.txtAppAreaDesc = txtAppAreaDesc;
    }

    public RichInputText getTxtAppAreaDesc() {
        return txtAppAreaDesc;
    }

    public void setTxtAppAreaCode(RichInputText txtAppAreaCode) {
        this.txtAppAreaCode = txtAppAreaCode;
    }

    public RichInputText getTxtAppAreaCode() {
        return txtAppAreaCode;
    }

    public String actionAcceptAppArea() {
        Object key = tblAppAreaLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtAppAreaCode.setValue(nodeBinding.getAttribute("sysAppAreaCode"));
            txtAppAreaDesc.setValue(nodeBinding.getAttribute("sysAppAreaDesc"));

        }
        GlobalCC.refreshUI(txtAppAreaDesc);
        GlobalCC.dismissPopUp("crm", "AppAreasPop");
        return null;
    }

    public void setTblAppAreaLOV(RichTable tblAppAreaLOV) {
        this.tblAppAreaLOV = tblAppAreaLOV;
    }

    public RichTable getTblAppAreaLOV() {
        return tblAppAreaLOV;
    }

    public String actionHideAppAreas() {
        GlobalCC.dismissPopUp("crm", "AppAreasPop");
        return null;
    }

    public String actionHideSubclass() {
        GlobalCC.dismissPopUp("crm", "p2");

        return null;
    }

    public void setApplLvl(RichSelectOneChoice applLvl) {
        this.applLvl = applLvl;
    }

    public RichSelectOneChoice getApplLvl() {
        return applLvl;
    }
}
