package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.ClientAttribute1;

import java.math.BigDecimal;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ClientAttributeBean {
    private RichCommandButton btnNewClntAttr;
    private RichInputText tcA_CODE;
    private RichInputText txtCode;
    private RichInputText txtAttrName;
    private RichInputText txtDescription;
    private RichInputText txtPrompt;
    private RichCommandButton btnCreateUpdateAttribute;
    private RichTable tblAttributes;
    private RichSelectOneChoice chRange;
    private RichInputText txtTableName;
    private RichInputText txtColName;
    private RichTable tblColumnNames;
    private RichInputText txtColDescription;

    public ClientAttributeBean() {
    }

    public String resetClientAttributes() {
        txtCode.setValue(null);
        txtAttrName.setValue(null);
        txtDescription.setValue(null);
        txtPrompt.setValue(null);
        chRange.setValue("N");
        txtColName.setValue(null);
        txtTableName.setValue(null);
        btnCreateUpdateAttribute.setText("Save");
        return null;
    }

    public String actionNewAttributes() {
        resetClientAttributes();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:attributePop" + "').show(hints);");
        return null;
    }

    public String actionShowDeleteAttributes() {
        Object key2 = tblAttributes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteAttribute" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
        }
        return null;
    }

    public void actionConfirmedDeleteAttribute(DialogEvent dialogEvent) {
        OracleConnection conn = null;
        if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.no))) {

        } else if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.yes))) {


            Object key2 = tblAttributes.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            resetClientAttributes();
            if (nodeBinding != null) {
                String code = null;
                code =
GlobalCC.checkNullValues(nodeBinding.getAttribute("TCA_CODE"));
                if (code != null && code.trim().length() > 0) {
                    actionDeleteAttribute(code);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected::");
            }
        }
    }

    public String actionEditAttribute() {
        resetClientAttributes();
        btnCreateUpdateAttribute.setText("Update");
        Object key2 = tblAttributes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtCode.setValue(nodeBinding.getAttribute("TCA_CODE"));
            txtAttrName.setValue(nodeBinding.getAttribute("TCA_ATTRIBUTE_NAME"));
            txtDescription.setValue(nodeBinding.getAttribute("TCA_ATT_DESC"));
            txtPrompt.setValue(nodeBinding.getAttribute("TCA_PROMPT"));
            chRange.setValue(nodeBinding.getAttribute("TCA_ATT_RANGE"));
            txtTableName.setValue(nodeBinding.getAttribute("tableName"));
            txtColName.setLabel(nodeBinding.getAttribute("colName").toString());
            txtColName.setValue(nodeBinding.getAttribute("colDescription"));


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:attributePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected:");
        }
        return null;
    }

    public String actionDeleteAttribute(String code) {

        OracleConnection conn = null;
        String Query = "BEGIN Tqc_Product_Pkg.clientAttribute_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = "D";
        try {
            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_CLIENT_ATTRIBUTES_TAB",
                                                 conn);
            ArrayList templateList = new ArrayList();
            ClientAttribute1 attr = new ClientAttribute1();
            attr.setSQLTypeName("TQC_CLIENT_ATTRIBUTES_OBJ");

            attr.setTCA_CODE(code == null ? null : new BigDecimal(code));
            attr.setTCA_ATTRIBUTE_NAME(null);
            attr.setTCA_ATT_DESC(null);
            attr.setTCA_PROMPT(null);
            attr.setTCA_ATT_RANGE(null);
            attr.setTCA_ATT_TYPE_INPUT(null);
            templateList.add(attr);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());
            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {

                ADFUtils.findIterator("findClientAttributesIterator").executeQuery();
                GlobalCC.refreshUI(tblAttributes);
                GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully:");
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:attributePop" + "').hide(hints);");


            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String actionCreateUpdateAttribute() {
        String code = GlobalCC.checkNullValues(txtCode.getValue());
        String desc = GlobalCC.checkNullValues(txtDescription.getValue());
        String name = GlobalCC.checkNullValues(txtAttrName.getValue());
        String prompt = GlobalCC.checkNullValues(txtPrompt.getValue());
        String range = GlobalCC.checkNullValues(chRange.getValue());
        String tableName = GlobalCC.checkNullValues(txtTableName.getValue());
        String colName = GlobalCC.checkNullValues(txtColName.getValue());
        if (name == null || name.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("NAME REQUIRED");
            return null;
        }
        if (desc == null || desc.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("DESCRIPTION REQUIRED");
            return null;
        }
        if (prompt == null || prompt.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("PROMPT REQUIRED");
            return null;
        }
        if (range == null || range.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("RANGE REQUIRED");
            return null;
        }

        if (colName == null || colName.trim().length() < 1) {
            GlobalCC.errorValueNotEntered("CLIENT ATTRIBUTE  REQUIRED");
            return null;
        } else {
            colName = txtColName.getLabel();
        }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        String Query = "BEGIN Tqc_Product_Pkg.clientAttribute_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = null;
        String theDate = null;
        if (btnCreateUpdateAttribute.getText().equalsIgnoreCase("Update")) {
            option = "E";
            if (code == null) {
                GlobalCC.errorValueNotEntered("ERROR: CODE REQUIRED::");
            }

        } else {
            option = "A";
        }
        try {


            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_CLIENT_ATTRIBUTES_TAB",
                                                 conn);
            ArrayList templateList = new ArrayList();
            ClientAttribute1 attr = new ClientAttribute1();
            attr.setSQLTypeName("TQC_CLIENT_ATTRIBUTES_OBJ");

            attr.setTCA_CODE(code == null ? null : new BigDecimal(code));
            attr.setTCA_ATTRIBUTE_NAME(name);
            attr.setTCA_ATT_DESC(desc);
            attr.setTCA_PROMPT(prompt);
            attr.setTCA_ATT_RANGE(range);
            attr.setTCA_ATT_TYPE_INPUT(null); //input type
            attr.setColName(colName);
            attr.setTableName(tableName);
            templateList.add(attr);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());

            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {
                if (option.equalsIgnoreCase("E")) {

                    ADFUtils.findIterator("findClientAttributesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAttributes);
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:attributePop" +
                                         "').hide(hints);");
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Created Successfully:");
                    ADFUtils.findIterator("findClientAttributesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAttributes);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:attributePop" +
                                         "').hide(hints);");

                }

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void setBtnNewClntAttr(RichCommandButton btnNewClntAttr) {
        this.btnNewClntAttr = btnNewClntAttr;
    }

    public RichCommandButton getBtnNewClntAttr() {
        return btnNewClntAttr;
    }

    public void setTcA_CODE(RichInputText tcA_CODE) {
        this.tcA_CODE = tcA_CODE;
    }

    public RichInputText getTcA_CODE() {
        return tcA_CODE;
    }

    public void setTxtCode(RichInputText txtCode) {
        this.txtCode = txtCode;
    }

    public RichInputText getTxtCode() {
        return txtCode;
    }

    public void setTxtAttrName(RichInputText txtAttrName) {
        this.txtAttrName = txtAttrName;
    }

    public RichInputText getTxtAttrName() {
        return txtAttrName;
    }

    public void setTxtDescription(RichInputText txtDescription) {
        this.txtDescription = txtDescription;
    }

    public RichInputText getTxtDescription() {
        return txtDescription;
    }

    public void setTxtPrompt(RichInputText txtPrompt) {
        this.txtPrompt = txtPrompt;
    }

    public RichInputText getTxtPrompt() {
        return txtPrompt;
    }


    public void setBtnCreateUpdateAttribute(RichCommandButton btnCreateUpdateAttribute) {
        this.btnCreateUpdateAttribute = btnCreateUpdateAttribute;
    }

    public RichCommandButton getBtnCreateUpdateAttribute() {
        return btnCreateUpdateAttribute;
    }

    public void setTblAttributes(RichTable tblAttributes) {
        this.tblAttributes = tblAttributes;
    }

    public RichTable getTblAttributes() {
        return tblAttributes;
    }

    public void setChRange(RichSelectOneChoice chRange) {
        this.chRange = chRange;
    }

    public RichSelectOneChoice getChRange() {
        return chRange;
    }

    public void setTxtTableName(RichInputText txtTableName) {
        this.txtTableName = txtTableName;
    }

    public RichInputText getTxtTableName() {
        return txtTableName;
    }

    public void setTxtColName(RichInputText txtColName) {
        this.txtColName = txtColName;
    }

    public RichInputText getTxtColName() {
        return txtColName;
    }

    public String actionAcceptColumn() {
        Object key = tblColumnNames.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtColName.setValue(nodeBinding.getAttribute("colDescription"));
            txtColName.setLabel(nodeBinding.getAttribute("colName").toString());
            txtTableName.setValue(nodeBinding.getAttribute("tableName"));

        }
        GlobalCC.refreshUI(txtTableName);
        GlobalCC.refreshUI(txtColName);
        GlobalCC.dismissPopUp("crm", "CulumnNamesPop");
        return null;
    }

    public String actionCancelColumn() {
        GlobalCC.dismissPopUp("crm", "CulumnNamesPop");
        return null;
    }

    public void setTblColumnNames(RichTable tblColumnNames) {
        this.tblColumnNames = tblColumnNames;
    }

    public RichTable getTblColumnNames() {
        return tblColumnNames;
    }

    public void setTxtColDescription(RichInputText txtColDescription) {
        this.txtColDescription = txtColDescription;
    }

    public RichInputText getTxtColDescription() {
        return txtColDescription;
    }

    public String cancelAttribute() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:attributePop" + "').hide(hints);");
        return null;
    }
}
