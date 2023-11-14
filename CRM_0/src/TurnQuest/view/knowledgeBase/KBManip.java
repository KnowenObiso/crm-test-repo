package TurnQuest.view.knowledgeBase;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class KBManip {
    private RichInputNumberSpinbox txtKBOrder;
    private RichInputText txtKBShortDesc;
    private RichInputText txtKBDesc;
    private RichTable tblKBContents;
    private RichInputNumberSpinbox txtKBCOrder;
    private RichInputText txtKBCContent;
    private RichInputText txtKBCId;
    private RichInputText txtKBTId;

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTree treeKBTopics;
    private RichSelectBooleanCheckbox txtMainTopic;
    private RichOutputLabel lblMainTopic;
    private RichInputText txtKBTParentId;
    private RichDialog dialogConfirmDelete;

    public KBManip() {
    }

    public void setTxtKBOrder(RichInputNumberSpinbox txtKBOrder) {
        this.txtKBOrder = txtKBOrder;
    }

    public RichInputNumberSpinbox getTxtKBOrder() {
        return txtKBOrder;
    }

    public void setTxtKBShortDesc(RichInputText txtKBShortDesc) {
        this.txtKBShortDesc = txtKBShortDesc;
    }

    public RichInputText getTxtKBShortDesc() {
        return txtKBShortDesc;
    }

    public void setTxtKBDesc(RichInputText txtKBDesc) {
        this.txtKBDesc = txtKBDesc;
    }

    public RichInputText getTxtKBDesc() {
        return txtKBDesc;
    }


    public void setTblKBContents(RichTable tblKBContents) {
        this.tblKBContents = tblKBContents;
    }

    public RichTable getTblKBContents() {
        return tblKBContents;
    }

    public void setTxtKBCOrder(RichInputNumberSpinbox txtKBCOrder) {
        this.txtKBCOrder = txtKBCOrder;
    }

    public RichInputNumberSpinbox getTxtKBCOrder() {
        return txtKBCOrder;
    }

    public void setTxtKBCContent(RichInputText txtKBCContent) {
        this.txtKBCContent = txtKBCContent;
    }

    public RichInputText getTxtKBCContent() {
        return txtKBCContent;
    }

    public void setTxtKBCId(RichInputText txtKBCId) {
        this.txtKBCId = txtKBCId;
    }

    public RichInputText getTxtKBCId() {
        return txtKBCId;
    }

    public void setTxtKBTId(RichInputText txtKBTId) {
        this.txtKBTId = txtKBTId;
    }

    public RichInputText getTxtKBTId() {
        return txtKBTId;
    }


    public String actionNewKBContent() {
        if (txtKBTId.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a KB topic to Proceed.");
            return null;
        }
        txtKBCId.setValue(null);
        txtKBCContent.setValue(null);
        txtKBCOrder.setValue(null);

        GlobalCC.showPopUp("crm", "newEditKBContentPOP");

        return null;
    }

    public String actionEditKBContent() {
        if (txtKBTId.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a KB topic to Proceed.");
            return null;
        }
        Object key = tblKBContents.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Record to  Edit");
            return null;
        }
        txtKBCId.setValue(nodeBinding.getAttribute("kbcId"));
        txtKBCContent.setValue(nodeBinding.getAttribute("kbcContent"));
        txtKBCOrder.setValue(nodeBinding.getAttribute("kbcOrder"));

        GlobalCC.showPopUp("crm", "newEditKBContentPOP");

        return null;
    }

    public String actionSaveKBContent() {
        Object kbcId = txtKBCId.getValue();


        if (txtKBCOrder.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter KB Content order.");
            return null;
        }
        if (txtKBCContent.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter KB Content .");
            return null;
        }


        DBConnector Connector = new DBConnector();
        OracleConnection conn = Connector.getDatabaseConnection();

        String query =
            "begin TQC_SERVICE_REQUESTS.kbContentProc(?,?,?,?,?); end;";

        try {
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;

            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (kbcId == null) {
                stmt.setString(1, "A");

            } else {
                stmt.setString(1, "E");
            }
            stmt.setObject(2, kbcId);
            stmt.setObject(3, txtKBTId.getValue());
            stmt.setObject(4, txtKBCOrder.getValue());
            stmt.setObject(5, txtKBCContent.getValue());
            stmt.execute();
            stmt.close();
            conn.close();

            GlobalCC.dismissPopUp("crm", "newEditKBContentPOP");
            ADFUtils.findIterator("findKbContentPerTopicIterator").executeQuery();
            GlobalCC.refreshUI(tblKBContents);
            ADFUtils.findIterator("findKbTopicsIterator").executeQuery();
            GlobalCC.refreshUI(treeKBTopics);
            GlobalCC.INFORMATIONREPORTING("Record Successfully Saved");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return null;
    }

    public String actionDeleteKBContent() {

        if (txtKBTId.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a KB topic to Proceed.");
            return null;
        }

        Object key = tblKBContents.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Record to Delete");
            return null;
        }
        txtKBCId.setValue(nodeBinding.getAttribute("kbcId"));
        dialogConfirmDelete.setShortDesc("Delete KBContent");
        GlobalCC.showPopUp("crm", "confirmDeletePop");
        return null;
    }

    public void confirmDeleteKBContent() {
        DBConnector Connector = new DBConnector();
        OracleConnection conn = Connector.getDatabaseConnection();
        String query =
            "begin TQC_SERVICE_REQUESTS.kbContentProc(?,?,?,?,?); end;";


        try {

            OracleCallableStatement stmt =
                (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, "D");
            stmt.setObject(2, txtKBCId.getValue());
            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.setString(5, null);
            stmt.execute();
            stmt.close();
            conn.close();

            ADFUtils.findIterator("findKbContentPerTopicIterator").executeQuery();
            GlobalCC.refreshUI(tblKBContents);
            GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
            txtKBCId.setValue(null);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


    }

    public String actionNewKBTopic() {
        txtKBTId.setValue(null);
        txtKBOrder.setValue(null);
        txtKBShortDesc.setValue(null);
        txtKBDesc.setValue(null);
        if (txtKBTParentId.getValue() == null) {
            txtMainTopic.setVisible(false);
            txtMainTopic.setSelected(false);
            lblMainTopic.setVisible(false);
        } else {
            txtMainTopic.setVisible(true);
            txtMainTopic.setSelected(false);
            lblMainTopic.setVisible(true);
        }

        GlobalCC.showPopUp("crm", "newEditKBTopicPop");

        return null;
    }

    public String actionEditKBTopic() {

        RowKeySet rks = treeKBTopics.getSelectedRowKeys();
        for (Object key : rks) {
            treeKBTopics.setRowKey(key);
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)treeKBTopics.getRowData();
            if (nodeBinding == null) {
                GlobalCC.EXCEPTIONREPORTING("Select a Record to  Edit");
                return null;
            }
            txtKBTId.setValue(nodeBinding.getAttribute("kbtId"));
            txtKBOrder.setValue(nodeBinding.getAttribute("kbtOrder"));
            txtKBShortDesc.setValue(nodeBinding.getAttribute("kbtShrtDesc"));
            txtKBDesc.setValue(nodeBinding.getAttribute("kbtDesc"));
            txtKBTParentId.setValue(nodeBinding.getAttribute("kbtParentId"));

            txtMainTopic.setVisible(false);
            txtMainTopic.setSelected(false);
            lblMainTopic.setVisible(false);
            GlobalCC.showPopUp("crm", "newEditKBTopicPop");
        }
        return null;
    }

    public String actionSaveKBTopic() {
        Object kbtId = txtKBTId.getValue();


        int newKbtId = -1;
        if (txtKBOrder.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter KB order.");
            return null;
        }
        if (txtKBShortDesc.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter KB short Description.");
            return null;
        }
        if (txtKBDesc.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter KB Description.");
            return null;
        }

        DBConnector Connector = new DBConnector();
        OracleConnection conn = Connector.getDatabaseConnection();

        String query =
            "begin TQC_SERVICE_REQUESTS.kbTopicProc(?,?,?,?,?,?); end;";

        try {
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            if (kbtId == null) {
                stmt.setObject(1, "A");

            } else {
                stmt.setObject(1, "E");
                newKbtId = Integer.valueOf((String)txtKBTId.getValue());
            }

            stmt.setInt(2, newKbtId);
            stmt.setObject(3, txtKBOrder.getValue());
            stmt.setObject(4, txtKBShortDesc.getValue());
            stmt.setObject(5, txtKBDesc.getValue());
            stmt.setObject(6, txtKBTParentId.getValue());
            stmt.execute();
            stmt.close();
            conn.close();

            GlobalCC.dismissPopUp("crm", "newEditKBTopicPop");
            ADFUtils.findIterator("findKbTopicsIterator").executeQuery();
            GlobalCC.refreshUI(treeKBTopics);
            GlobalCC.INFORMATIONREPORTING("Record Successfully Saved");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String actionDeleteKBTopic() {

        if (txtKBTId.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a record to delete");
            return null;
        }

        dialogConfirmDelete.setShortDesc("Delete KBTopic");
        GlobalCC.showPopUp("crm", "confirmDeletePop");
        return null;
    }

    public void confirmDeleteKBTopic() {
        DBConnector Connector = new DBConnector();
        OracleConnection conn = Connector.getDatabaseConnection();
        String query =
            "begin TQC_SERVICE_REQUESTS.kbTopicProc(?,?,?,?,?,?); end;";


        try {

            OracleCallableStatement stmt =
                (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, "D");
            stmt.setObject(2, txtKBTId.getValue());
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.setObject(6, null);
            stmt.execute();
            stmt.close();
            conn.close();

            ADFUtils.findIterator("findKbTopicsIterator").executeQuery();
            GlobalCC.refreshUI(treeKBTopics);
            GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
            txtKBTId.setValue(null);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


    }


    public String actionCancelKBtopic() {

        return null;
    }

    public void setTreeKBTopics(RichTree treeKBTopics) {
        this.treeKBTopics = treeKBTopics;
    }

    public RichTree getTreeKBTopics() {
        return treeKBTopics;
    }

    public void actionTreeKBTopicsSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rks = treeKBTopics.getSelectedRowKeys();
            for (Object key : rks) {
                treeKBTopics.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)treeKBTopics.getRowData();
                if (nodeBinding != null) {

                    txtKBTId.setValue(nodeBinding.getAttribute("kbtId"));
                    txtKBTParentId.setValue(nodeBinding.getAttribute("kbtId"));

                    session.setAttribute("KBID", txtKBTId.getValue());
                    ADFUtils.findIterator("findKbContentPerTopicIterator").executeQuery();
                    GlobalCC.refreshUI(tblKBContents);
                }
            }
        }
    }

    public void setTxtMainTopic(RichSelectBooleanCheckbox txtMainTopic) {
        this.txtMainTopic = txtMainTopic;
    }

    public RichSelectBooleanCheckbox getTxtMainTopic() {
        return txtMainTopic;
    }

    public void setLblMainTopic(RichOutputLabel lblMainTopic) {
        this.lblMainTopic = lblMainTopic;
    }

    public RichOutputLabel getLblMainTopic() {
        return lblMainTopic;
    }

    public void setTxtKBTParentId(RichInputText txtKBTParentId) {
        this.txtKBTParentId = txtKBTParentId;
    }

    public RichInputText getTxtKBTParentId() {
        return txtKBTParentId;
    }

    public void actionChangedMainTopic(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (txtMainTopic.isSelected()) {
                txtKBTParentId.setValue(null);
            }
        }
    }

    public void setDialogConfirmDelete(RichDialog dialogConfirmDelete) {
        this.dialogConfirmDelete = dialogConfirmDelete;
    }

    public RichDialog getDialogConfirmDelete() {
        return dialogConfirmDelete;
    }

    public void confirmDeletionListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            if (dialogConfirmDelete.getShortDesc().equalsIgnoreCase("Delete KBTopic"))
                confirmDeleteKBTopic();
            else if (dialogConfirmDelete.getShortDesc().equalsIgnoreCase("Delete KBContent"))
                confirmDeleteKBContent();

        }
        GlobalCC.dismissPopUp("crm", "confirmDeletePop");
    }
}
