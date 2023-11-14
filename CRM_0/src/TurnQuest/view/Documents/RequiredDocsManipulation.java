package TurnQuest.view.Documents;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class RequiredDocsManipulation {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichOutputText cdocument;
    private RichSelectOneChoice cdocSubmitted;
    private RichInputText cdocRefNo;
    private RichInputText cdocRemarks;
    private RichTable clientDocsLOV;
    private RichTable accMangrLOV;

    // new properties start here
    private RichTable tblRequiredDocs;
    private RichInputText txtReqShortDesc;
    private RichInputText txtReqDesc;
    private RichSelectOneChoice txtReqMandatory;
    private RichInputText txtReqCode;
    private RichCommandButton btnSaveUpdateReqDocuments;
    private RichOutputText textToShow;
    private RichCommandButton addRequiredDocument;
    private RichSelectOneChoice txtRegExempted;
    private RichSelectOneChoice docApplicationlevel;
    private List listOfItems = new ArrayList();

    public RequiredDocsManipulation() {
        super();
    }

    public void clearReqDocFields() {
        txtReqShortDesc.setValue(null);
        txtReqDesc.setValue(null);
        txtReqMandatory.setValue(null);
        docApplicationlevel.setValue(null);
    }

    public String AddRequiredDocument() {
        try {
            clearReqDocFields();
            btnSaveUpdateReqDocuments.setText("Save");

            // Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:reqDocsPopup" +
                                 "').show(hints);");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditRequiredDocuments() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtReqCode.setValue(nodeBinding.getAttribute("code").toString());
            txtReqShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtReqDesc.setValue(nodeBinding.getAttribute("description"));
            txtReqMandatory.setValue(nodeBinding.getAttribute("mandatory"));
            docApplicationlevel.setValue(nodeBinding.getAttribute("account_type"));
            btnSaveUpdateReqDocuments.setText("Edit");

            // Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:reqDocsPopup" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String DeleteRequiredDocuments() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_WEB_PKG.saveRequiredDocuments(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(nodeBinding.getAttribute("code").toString()));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
                cst.setString(7, null);
                cst.registerOutParameter(8, OracleTypes.VARCHAR);

                cst.execute();
                
                
                String err = cst.getString(8);

                

                ADFUtils.findIterator("fetchAllRequiredDocumentsIterator").executeQuery();
                GlobalCC.refreshUI(tblRequiredDocs);

                cst.close();
                conn.commit();
                conn.close();

                String message = "Document DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }
    public List<SelectItem> getAccountTypes() {
        List<SelectItem> accountTypes = new ArrayList<SelectItem>();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        String status = null;
        Boolean value = true;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 = " SELECT ACT_TYPE_SHT_DESC,ACT_ACCOUNT_TYPE" +
                "           FROM tqc_account_types";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();
            while (rst.next()) {
                SelectItem item = new SelectItem();
                item.setValue(rst.getString(1));
                item.setLabel(rst.getString(2));
                accountTypes.add(item);
            }
            
            SelectItem sp = new SelectItem();
            sp.setValue("SP");
            sp.setLabel("SERVICE PROVIDER");
            accountTypes.add(sp);
            SelectItem client = new SelectItem();
            client.setValue("C");
            client.setLabel("CLIENT");
            accountTypes.add(client);
            
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return accountTypes;
    }
    public void setListOfItems(List listOfItems) {
        this.listOfItems = listOfItems;
    }

    public List getListOfItems() {
        listOfItems = getAccountTypes();
        return listOfItems;
    }
    public String SaveRequiredDocuments() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveUpdateReqDocuments.getText().equals("Edit")) {
            actionEditRequiredDocument();
        } else {

            // Proceed to save
            String shortDesc =
                GlobalCC.checkNullValues(txtReqShortDesc.getValue());
            String description =
                GlobalCC.checkNullValues(txtReqDesc.getValue());
            String mandatory =
                GlobalCC.checkNullValues(txtReqMandatory.getValue());
            
            String appllevel =
                GlobalCC.checkNullValues(docApplicationlevel.getValue());
            ///   String exempted=  GlobalCC.checkNullValues(txtRegExempted.getValue());
            if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (description == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
                return null;

            } else if (mandatory == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select if Mandatory");
                return null;

            } else if(appllevel == null){
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Applies");
                return null;
            }else {
                String Query =
                    "begin TQC_WEB_PKG.saveRequiredDocuments(?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "A");
                    cst.setBigDecimal(2, null);
                    cst.setString(3, shortDesc);
                    cst.setString(4, description);
                    cst.setString(5, mandatory);
                    cst.setDate(6,
                                new java.sql.Date(new java.util.Date().getTime()));
                    cst.setString(7, appllevel);
                    cst.registerOutParameter(8, OracleTypes.VARCHAR);

                    cst.execute();

                    String err = cst.getString(8);
                    //TurnQuest.view.commons.GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchAllRequiredDocumentsIterator").executeQuery();
                    GlobalCC.refreshUI(tblRequiredDocs);

                    cst.close();
                    conn.close();
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                    } else {
                        String message = "Record CREATED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "crmTemplate:reqDocsPopup" +
                                             "').hide(hints);");
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionEditRequiredDocument() {
        String code = GlobalCC.checkNullValues(txtReqCode.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtReqShortDesc.getValue());
        String description = GlobalCC.checkNullValues(txtReqDesc.getValue());
        String mandatory =
            GlobalCC.checkNullValues(txtReqMandatory.getValue());
        String applLevel =
            GlobalCC.checkNullValues(docApplicationlevel.getValue());
        ///String exempted=  GlobalCC.checkNullValues(txtRegExempted.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Doc Code is Empty");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (description == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
            return null;

        } else if (mandatory == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select if Mandatory");
            return null;
        
        } else if (mandatory == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select if Mandatory");
            return null;
        
        } else {
            String Query =
                "begin TQC_WEB_PKG.saveRequiredDocuments(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(code));
                cst.setString(3, shortDesc);
                cst.setString(4, description);
                cst.setString(5, mandatory);
                cst.setDate(6,
                            new java.sql.Date(new java.util.Date().getTime()));
                cst.setString(7, applLevel);
                cst.registerOutParameter(8, OracleTypes.VARCHAR);

                cst.execute();

                String err = cst.getString(8);
                //TurnQuest.view.commons.GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchAllRequiredDocumentsIterator").executeQuery();
                GlobalCC.refreshUI(tblRequiredDocs);

                cst.close();
                conn.commit();
                conn.close();
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                } else {
                    String message = "Document UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crmTemplate:reqDocsPopup" +
                                         "').hide(hints);");
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String EditClientReqDocuments() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findClientRequiredDocsIterator");
            RowKeySet set = clientDocsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("cdocCode", r.getAttribute("CDOCR_CODE"));
                session.setAttribute("rdocCode",
                                     r.getAttribute("CDOCR_RDOC_CODE"));
                session.setAttribute("action", "E");
                cdocument.setValue(r.getAttribute("ROC_DESC"));
                cdocSubmitted.setValue(r.getAttribute("CDOCR_SUBMITED"));
                cdocRefNo.setValue(r.getAttribute("CDOCR_REF_NO"));
                cdocRemarks.setValue(r.getAttribute("CDOCR_RMRK"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crmTemplate:clientDocsPopup" +
                                     "').show(hints);");
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveClientDocuments() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_PKG.saveClientDocuments(?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("cdocCode"));
            cst.setBigDecimal(3, (BigDecimal)session.getAttribute("rdocCode"));
            cst.setObject(4, session.getAttribute("ClientCode"));
            if (cdocSubmitted.getValue() == null) {
                cst.setString(5, null);
            } else {
                cst.setString(5, cdocSubmitted.getValue().toString());
            }
            cst.setString(6, null);
            if (cdocRefNo.getValue() == null) {
                cst.setString(7, null);
            } else {
                cst.setString(7, cdocRefNo.getValue().toString());
            }
            if (cdocRemarks.getValue() == null) {
                cst.setString(8, null);
            } else {
                cst.setString(8, cdocRemarks.getValue().toString());
            }
            cst.setString(9, (String)session.getAttribute("Username"));
            cst.execute();
            cst.close();
            conn.close();
            session.setAttribute("cdocCode", null);
            session.setAttribute("rdocCode", null);
            ADFUtils.findIterator("findClientRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(clientDocsLOV);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setCdocument(RichOutputText cdocument) {
        this.cdocument = cdocument;
    }

    public RichOutputText getCdocument() {
        return cdocument;
    }

    public void setCdocSubmitted(RichSelectOneChoice cdocSubmitted) {
        this.cdocSubmitted = cdocSubmitted;
    }

    public RichSelectOneChoice getCdocSubmitted() {
        return cdocSubmitted;
    }

    public void setCdocRefNo(RichInputText cdocRefNo) {
        this.cdocRefNo = cdocRefNo;
    }

    public RichInputText getCdocRefNo() {
        return cdocRefNo;
    }

    public void setCdocRemarks(RichInputText cdocRemarks) {
        this.cdocRemarks = cdocRemarks;
    }

    public RichInputText getCdocRemarks() {
        return cdocRemarks;
    }

    public void setClientDocsLOV(RichTable clientDocsLOV) {
        this.clientDocsLOV = clientDocsLOV;
    }

    public RichTable getClientDocsLOV() {
        return clientDocsLOV;
    }

    public void setAccMangrLOV(RichTable accMangrLOV) {
        this.accMangrLOV = accMangrLOV;
    }

    public RichTable getAccMangrLOV() {
        return accMangrLOV;
    }

    public void setTblRequiredDocs(RichTable tblRequiredDocs) {
        this.tblRequiredDocs = tblRequiredDocs;
    }

    public RichTable getTblRequiredDocs() {
        return tblRequiredDocs;
    }

    public void setTxtReqShortDesc(RichInputText txtReqShortDesc) {
        this.txtReqShortDesc = txtReqShortDesc;
    }

    public RichInputText getTxtReqShortDesc() {
        return txtReqShortDesc;
    }

    public void setTxtReqDesc(RichInputText txtReqDesc) {
        this.txtReqDesc = txtReqDesc;
    }

    public RichInputText getTxtReqDesc() {
        return txtReqDesc;
    }

    public void setTxtReqMandatory(RichSelectOneChoice txtReqMandatory) {
        this.txtReqMandatory = txtReqMandatory;
    }

    public RichSelectOneChoice getTxtReqMandatory() {
        return txtReqMandatory;
    }

    public void setTxtReqCode(RichInputText txtReqCode) {
        this.txtReqCode = txtReqCode;
    }

    public RichInputText getTxtReqCode() {
        return txtReqCode;
    }

    public void setBtnSaveUpdateReqDocuments(RichCommandButton btnSaveUpdateReqDocuments) {
        this.btnSaveUpdateReqDocuments = btnSaveUpdateReqDocuments;
    }

    public RichCommandButton getBtnSaveUpdateReqDocuments() {
        return btnSaveUpdateReqDocuments;
    }

    public void setTextToShow(RichOutputText textToShow) {
        this.textToShow = textToShow;
    }

    public RichOutputText getTextToShow() {
        return textToShow;
    }

    /**
     * Called upon from time to time to display various messages from the
     * server i.e. Completing of successful edit, delete or edit.
     */
    public void showMessagePopup(String msgToDisplay) {
        textToShow.setValue(null);
        textToShow.setValue(msgToDisplay);

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent source = addRequiredDocument;
        String alignId = source.getClientId(context);
        String popupId = "crmTemplate:ServerMessage";

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("var hints = {}; ").append("hints[AdfRichPopup.HINT_ALIGN_ID] = '").append(alignId).append("'; ").append("hints[AdfRichPopup.HINT_ALIGN] = AdfRichPopup.ALIGN_AFTER_START; ").append("popup.show(hints);}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public void setAddRequiredDocument(RichCommandButton addRequiredDocument) {
        this.addRequiredDocument = addRequiredDocument;
    }

    public RichCommandButton getAddRequiredDocument() {
        return addRequiredDocument;
    }

    public String confirmDeleteAction() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:confirmationDialog" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void confirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DeleteRequiredDocuments();
        }
    }

    public void setTxtRegExempted(RichSelectOneChoice txtRegExempted) {
        this.txtRegExempted = txtRegExempted;
    }

    public RichSelectOneChoice getTxtRegExempted() {
        return txtRegExempted;
    }

    public void setDocApplicationlevel(RichSelectOneChoice docApplicationlevel) {
        this.docApplicationlevel = docApplicationlevel;
    }

    public RichSelectOneChoice getDocApplicationlevel() {
        return docApplicationlevel;
    }
}
