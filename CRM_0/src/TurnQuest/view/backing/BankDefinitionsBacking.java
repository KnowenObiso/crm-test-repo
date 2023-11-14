package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.DBConnector;

import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.ResultSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class BankDefinitionsBacking {
    private Rendering renderer = new Rendering();
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblBanks;
    private RichTable tblBankBranch;
    private RichTable tblBankTerritory;
    private RichTable tblCountryCode;
    private RichInputText txtBnkCode;
    private RichInputText txtBnkBankName;
    private RichInputText txtBnkRemark;
    private RichInputText txtBnkShortDesc;
    private RichInputText txtBnkDdrCode;
    private RichInputText txtBnkFormatDesc;
    private RichInputText txtBnkForwadingBankCode;
    private RichInputText txtBnkKbaCode;
    private RichCommandButton btnSaveUpdateBank;
    private RichInputText txtBbrCode;
    private RichInputText txtBbrBnkCode;
    private RichInputText txtBbrBranchName;
    private RichInputText txtBbrShortDesc;
    private RichInputText txtBbrRemarks;
    private RichInputText txtBbrRefCode;
    private RichSelectOneChoice txtBbrEFTSupport;
    private RichSelectOneChoice txtBbrDDSupport;
    private RichInputDate txtBbrDateCreated;
    private RichInputText txtBbrCreatedBy;
    private RichInputText txtBbrPhysicalAddress;
    private RichInputText txtBbrPostalAddress;
    private RichInputText txtBbrKBACode;
    private RichCommandButton btnSaveUpdateBankBranch;
    private RichInputText txtBnkDdrDesc;
    private RichInputText txtBnkFwdBankName;
    private RichTable tblForwadingBanks;
    private RichTable tblDDReports;
    private RichPanelBox panelBankDetails;
    private RichCommandButton btnEditBankBranch;
    private RichCommandButton btnEditBankTerritory;
    private RichCommandButton btnDeleteBnkBranch;
    private RichCommandButton btnDeleteBankTerritory;
    private RichCommandButton btnNewBank;
    private RichCommandButton btnEditBank;
    private RichCommandButton btnDeleteBank;
    private RichCommandButton btnNewBankBranch;
    private RichCommandButton btnNewBankTerritory;

    private RichPanelGroupLayout grpEmploymentDetails;
    private RichSelectOneChoice txtBnkEFTSupport;
    private RichSelectOneChoice txtClassification;
    private RichInputNumberSpinbox txtCharacterNo;
    private RichInputNumberSpinbox txtAdministrativeCharge;
    private RichSelectOneChoice txtNegotiatedBank;
    private RichTable bankTbl;
    private RichInputText txtBank;
    private RichInputText txtBankName;
    private RichInputText txtBankTableName;
    private RichPanelBox bankTab;
    private RichPanelBox bankDtlsTempTab;
    private RichInputFile uploadedPicture;
    private UploadedFile pictureFile;
    private String filename;
    private long filesize;
    private static String fileContent;
    private static InputStream fileStream;
    private RichInputDate txtWef;
    private RichInputDate txtWet;
    private RichImage bankLogo;
    private RichInputText txtBankTownName;
    private RichInputText txtBankTownCode;
    private RichTable tblTowns;

    private RichCommandButton btnSaveBankTerritory;
    private RichInputText txtBnktCode;
    private RichInputText txtBnktTerritoryName;
    private RichInputText txtBnktShtDesc;
    private RichInputNumberSpinbox txtBnktBnkCode;

    private RichInputNumberSpinbox txtBbrBnktCode;
    private RichInputText txtBbrBnktShtDesc;
    private RichInputText txtBbrEmail;
    private RichInputText txtBbrPersonName;
    private RichInputText txtBbrPersonPhone;
    private RichInputText txtBbrPersonEmail;
    private RichInputText txtBbrCountryCode;
    private RichSelectOneChoice selectBnkStatus;
    private RichInputText txtCountry;
    private RichInputNumberSpinbox txtMaxCharacterNo;
    private RichInputNumberSpinbox txtMinCharacterNo;
    private RichTable countryTbl;
    private RichTable bancassurancelov;
    private RichInputText txtBbbCode;
    private RichInputText txtBbbBrnCode;
    private RichInputText txtBbbBranchName;
    private RichInputText txtBbbEmail;
    private RichInputText txtBbbShortDesc;
    private RichInputText txtBbbRemarks;
    private RichInputText txtBbrTownName;
    private RichInputText txtBbbTownCode;
    private RichInputText txtBbbTownName;
    private RichInputDate txtBbbDateCreated;
    private RichInputText txtBbbCreatedBy;
    private RichInputText txtBbbPersonName;
    private RichInputText txtBbbCountryCode;
    private RichInputText txtBbbPersonPhone;
    private RichInputText txtBbbPersonEmail;
    private RichInputText txtBbbPhysicalAddress;
    private RichInputText txtBbbPostalAddress;
    private RichCommandButton btnSaveUpdateBancassuranceBranch;
    private RichInputText txtBbbKBACode;
    private RichCommandButton btnAddAssuranceBranch;
    private RichCommandButton btnEditAssuranceBranch;
    private RichCommandButton btnDeleteAssuranceBranch;
    private RichTable tblbackAssuranceTowns;
    private RichTable tbbankAssurancelCountryCode;

    public BankDefinitionsBacking() {
    }


    public void setTblBanks(RichTable tblBanks) {
        this.tblBanks = tblBanks;
    }

    public RichTable getTblBanks() {
        return tblBanks;
    }

    public void setTblBankBranch(RichTable tblBankBranch) {
        this.tblBankBranch = tblBankBranch;
    }

    public RichTable getTblBankBranch() {
        return tblBankBranch;
    }

    public void setTxtBnkCode(RichInputText txtBnkCode) {
        this.txtBnkCode = txtBnkCode;
    }

    public RichInputText getTxtBnkCode() {
        return txtBnkCode;
    }

    public void setTxtBnkBankName(RichInputText txtBnkBankName) {
        this.txtBnkBankName = txtBnkBankName;
    }

    public RichInputText getTxtBnkBankName() {
        return txtBnkBankName;
    }

    public void setTxtBnkRemark(RichInputText txtBnkRemark) {
        this.txtBnkRemark = txtBnkRemark;
    }

    public RichInputText getTxtBnkRemark() {
        return txtBnkRemark;
    }

    public void setTxtBnkShortDesc(RichInputText txtBnkShortDesc) {
        this.txtBnkShortDesc = txtBnkShortDesc;
    }

    public RichInputText getTxtBnkShortDesc() {
        return txtBnkShortDesc;
    }

    public void setTxtBnkDdrCode(RichInputText txtBnkDdrCode) {
        this.txtBnkDdrCode = txtBnkDdrCode;
    }

    public RichInputText getTxtBnkDdrCode() {
        return txtBnkDdrCode;
    }

    public void setTxtBnkFormatDesc(RichInputText txtBnkFormatDesc) {
        this.txtBnkFormatDesc = txtBnkFormatDesc;
    }

    public RichInputText getTxtBnkFormatDesc() {
        return txtBnkFormatDesc;
    }

    public void setTxtBnkForwadingBankCode(RichInputText txtBnkForwadingBankCode) {
        this.txtBnkForwadingBankCode = txtBnkForwadingBankCode;
    }

    public RichInputText getTxtBnkForwadingBankCode() {
        return txtBnkForwadingBankCode;
    }

    public void setTxtBnkKbaCode(RichInputText txtBnkKbaCode) {
        this.txtBnkKbaCode = txtBnkKbaCode;
    }

    public RichInputText getTxtBnkKbaCode() {
        return txtBnkKbaCode;
    }

    public void setBtnSaveUpdateBank(RichCommandButton btnSaveUpdateBank) {
        this.btnSaveUpdateBank = btnSaveUpdateBank;
    }

    public RichCommandButton getBtnSaveUpdateBank() {
        return btnSaveUpdateBank;
    }

    public void setTxtBbrCode(RichInputText txtBbrCode) {
        this.txtBbrCode = txtBbrCode;
    }

    public RichInputText getTxtBbrCode() {
        return txtBbrCode;
    }

    public void setTxtBbrBnkCode(RichInputText txtBbrBnkCode) {
        this.txtBbrBnkCode = txtBbrBnkCode;
    }

    public RichInputText getTxtBbrBnkCode() {
        return txtBbrBnkCode;
    }

    public void setTxtBbrBranchName(RichInputText txtBbrBranchName) {
        this.txtBbrBranchName = txtBbrBranchName;
    }

    public RichInputText getTxtBbrBranchName() {
        return txtBbrBranchName;
    }

    public void setTxtBbrShortDesc(RichInputText txtBbrShortDesc) {
        this.txtBbrShortDesc = txtBbrShortDesc;
    }

    public RichInputText getTxtBbrShortDesc() {
        return txtBbrShortDesc;
    }

    public void setTxtBbrRemarks(RichInputText txtBbrRemarks) {
        this.txtBbrRemarks = txtBbrRemarks;
    }

    public RichInputText getTxtBbrRemarks() {
        return txtBbrRemarks;
    }

    public void setTxtBbrRefCode(RichInputText txtBbrRefCode) {
        this.txtBbrRefCode = txtBbrRefCode;
    }

    public RichInputText getTxtBbrRefCode() {
        return txtBbrRefCode;
    }

    public void setTxtBbrEFTSupport(RichSelectOneChoice txtBbrEFTSupport) {
        this.txtBbrEFTSupport = txtBbrEFTSupport;
    }

    public RichSelectOneChoice getTxtBbrEFTSupport() {
        return txtBbrEFTSupport;
    }

    public void setTxtBbrDDSupport(RichSelectOneChoice txtBbrDDSupport) {
        this.txtBbrDDSupport = txtBbrDDSupport;
    }

    public RichSelectOneChoice getTxtBbrDDSupport() {
        return txtBbrDDSupport;
    }

    public void setTxtBbrDateCreated(RichInputDate txtBbrDateCreated) {
        this.txtBbrDateCreated = txtBbrDateCreated;
    }

    public RichInputDate getTxtBbrDateCreated() {
        return txtBbrDateCreated;
    }

    public void setTxtBbrCreatedBy(RichInputText txtBbrCreatedBy) {
        this.txtBbrCreatedBy = txtBbrCreatedBy;
    }

    public RichInputText getTxtBbrCreatedBy() {
        return txtBbrCreatedBy;
    }

    public void setTxtBbrPhysicalAddress(RichInputText txtBbrPhysicalAddress) {
        this.txtBbrPhysicalAddress = txtBbrPhysicalAddress;
    }

    public RichInputText getTxtBbrPhysicalAddress() {
        return txtBbrPhysicalAddress;
    }

    public void setTxtBbrPostalAddress(RichInputText txtBbrPostalAddress) {
        this.txtBbrPostalAddress = txtBbrPostalAddress;
    }

    public RichInputText getTxtBbrPostalAddress() {
        return txtBbrPostalAddress;
    }

    public void setTxtBbrKBACode(RichInputText txtBbrKBACode) {
        this.txtBbrKBACode = txtBbrKBACode;
    }

    public RichInputText getTxtBbrKBACode() {
        return txtBbrKBACode;
    }

    public void setBtnSaveUpdateBankBranch(RichCommandButton btnSaveUpdateBankBranch) {
        this.btnSaveUpdateBankBranch = btnSaveUpdateBankBranch;
    }

    public RichCommandButton getBtnSaveUpdateBankBranch() {
        return btnSaveUpdateBankBranch;
    }

    public void setTxtBnkDdrDesc(RichInputText txtBnkDdrDesc) {
        this.txtBnkDdrDesc = txtBnkDdrDesc;
    }

    public RichInputText getTxtBnkDdrDesc() {
        return txtBnkDdrDesc;
    }

    public void setTxtBnkFwdBankName(RichInputText txtBnkFwdBankName) {
        this.txtBnkFwdBankName = txtBnkFwdBankName;
    }

    public RichInputText getTxtBnkFwdBankName() {
        return txtBnkFwdBankName;
    }

    public void setTblForwadingBanks(RichTable tblForwadingBanks) {
        this.tblForwadingBanks = tblForwadingBanks;
    }

    public RichTable getTblForwadingBanks() {
        return tblForwadingBanks;
    }

    public void setTblDDReports(RichTable tblDDReports) {
        this.tblDDReports = tblDDReports;
    }

    public RichTable getTblDDReports() {
        return tblDDReports;
    }

    public void tblBanksListener(SelectionEvent selectionEvent) {
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)tblBanks.getSelectedRowData();

        if (nodeBinding != null) {
            session.setAttribute("bankCode",
                                 nodeBinding.getAttribute("bankCode"));
            bankLogo.setSource("/bankimagesservlet?id=" +
                               nodeBinding.getAttribute("bankCode"));

            btnDeleteBank.setDisabled(false);
            btnEditBank.setDisabled(false);
            btnNewBankTerritory.setDisabled(false);
            session.setAttribute("branchCode", null);
            GlobalCC.refreshUI(btnDeleteBank);
            GlobalCC.refreshUI(btnEditBank);
            GlobalCC.refreshUI(btnNewBankTerritory);
            ADFUtils.findIterator("fetchBankTerritoryByBankCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankTerritory);
            ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankBranch);
            ADFUtils.findIterator("fetchBancassuranceByBranchCodeIterator").executeQuery();
            GlobalCC.refreshUI(bancassurancelov);
        }
    }

    public void clearBankFields() {
        txtBnkCode.setValue(null);
        txtBnkBankName.setValue(null);
        txtBnkRemark.setValue(null);
        txtBnkShortDesc.setValue(null);
        txtBnkDdrCode.setValue(null);
        txtBnkDdrDesc.setValue(null);
        txtBnkFormatDesc.setValue(null);
        txtBnkForwadingBankCode.setValue(null);
        txtBnkFwdBankName.setValue(null);
        txtBnkKbaCode.setValue(null);
        txtBnkEFTSupport.setValue(null);
        txtCharacterNo.setValue(null);
        txtMaxCharacterNo.setValue(null);
        txtMinCharacterNo.setValue(null);
        session.removeAttribute("bnkcountrycode");
        txtClassification.setValue(null);
        txtCountry.setValue(null);
        txtNegotiatedBank.setValue(null);
        txtAdministrativeCharge.setValue(null);
        selectBnkStatus.setValue("A");
        btnSaveUpdateBank.setText("Save");
    }

    public String actionNewBank() {
        clearBankFields();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:bankPop" +
                             "').show(hints);");
        return null;
    }

    public String actionEditBank() {
        Object key2 = tblBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkCode.setValue(nodeBinding.getAttribute("bankCode"));
            txtBnkBankName.setValue(nodeBinding.getAttribute("bankName"));
            txtBnkRemark.setValue(nodeBinding.getAttribute("remarks"));
            txtBnkShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtBnkDdrCode.setValue(nodeBinding.getAttribute("DDRCode"));
            txtBnkDdrDesc.setValue(nodeBinding.getAttribute("ddReportFormat"));
            txtBnkFormatDesc.setValue(nodeBinding.getAttribute("DDFormatDesc"));
            txtBnkForwadingBankCode.setValue(nodeBinding.getAttribute("forwardingBankCode"));
            txtBnkFwdBankName.setValue(nodeBinding.getAttribute("fwdBankName"));
            txtBnkKbaCode.setValue(nodeBinding.getAttribute("KBACode"));
            txtBnkEFTSupport.setValue(nodeBinding.getAttribute("eftSupported"));
            txtClassification.setValue(nodeBinding.getAttribute("classType"));
            txtCharacterNo.setValue(nodeBinding.getAttribute("characterNo"));
            txtMaxCharacterNo.setValue(nodeBinding.getAttribute("characterMaxNo"));
            txtMinCharacterNo.setValue(nodeBinding.getAttribute("characterMinNo"));
            session.setAttribute("bnkcountrycode",
                                 nodeBinding.getAttribute("countryCode"));
            txtCountry.setValue(nodeBinding.getAttribute("countryName"));
            txtNegotiatedBank.setValue(nodeBinding.getAttribute("negotiatedBank"));
            txtAdministrativeCharge.setValue(nodeBinding.getAttribute("administativeAmnt"));
            txtWef.setValue(nodeBinding.getAttribute("bnkWef"));
            txtWet.setValue(nodeBinding.getAttribute("bnkWet"));
            selectBnkStatus.setValue(nodeBinding.getAttribute("bnkStatus"));
            btnSaveUpdateBank.setText("Edit");
            bankLogo.setSource("/bankimagesservlet?id=" +
                               nodeBinding.getAttribute("bankCode"));
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:bankPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteBank() {
        Object key2 = tblBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String bankCode = nodeBinding.getAttribute("bankCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2, new BigDecimal(bankCode));
                statement.setString(3, null);
                statement.setString(4, null);
                statement.setString(5, null);
                statement.setString(6, null);
                statement.setString(7, null);
                statement.setString(8, null);
                statement.setString(9, null);
                statement.setString(10, null);
                statement.setString(11, null);
                statement.setObject(12, null);
                statement.setObject(13, null);
                statement.setObject(14, null);
                statement.setBlob(15, fileStream);

                statement.setObject(16, null);

                statement.setObject(17, null);
                statement.registerOutParameter(18, OracleTypes.VARCHAR);
                statement.setObject(19, null);
                statement.setObject(20, null);
                statement.setObject(21, null);
                statement.setObject(22, null);
                //  statement.setObject(23, null);
                statement.execute();
                String errMsg = statement.getString(18);
                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                GlobalCC.refreshUI(tblBanks);

                ADFUtils.findIterator("fetchBankTerritoryByBankCodeIterator").executeQuery();
                ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
                GlobalCC.refreshUI(tblBankTerritory);
                GlobalCC.refreshUI(tblBankBranch);
                btnDeleteBank.setDisabled(true);
                btnEditBank.setDisabled(true);
                GlobalCC.refreshUI(btnDeleteBank);
                GlobalCC.refreshUI(btnEditBank);

                if (errMsg == null) {
                    String message = " Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } else {
                    GlobalCC.INFORMATIONREPORTING(errMsg);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateBank() {
        Rendering renderer = new Rendering();
        if (btnSaveUpdateBank.getText().equals("Edit")) {
            actionUpdateBank();
        } else {
            String bankCode = GlobalCC.checkNullValues(txtBnkCode.getValue());
            String name = GlobalCC.checkNullValues(txtBnkBankName.getValue());
            String remark = GlobalCC.checkNullValues(txtBnkRemark.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtBnkShortDesc.getValue());
            String ddrCode =
                GlobalCC.checkNullValues(txtBnkDdrCode.getValue());
            String formatDesc =
                GlobalCC.checkNullValues(txtBnkFormatDesc.getValue());
            String fwdBankCode =
                GlobalCC.checkNullValues(txtBnkForwadingBankCode.getValue());
            String kbaCode =
                GlobalCC.checkNullValues(txtBnkKbaCode.getValue());
            String eftSupport =
                GlobalCC.checkNullValues(txtBnkEFTSupport.getValue());
            String slctBnkStatus =
                GlobalCC.checkNullValues(selectBnkStatus.getValue());
            String characterNo =
                GlobalCC.checkNullValues(txtCharacterNo.getValue());
            String characterMaxNo =
                GlobalCC.checkNullValues(txtMaxCharacterNo.getValue());
            String characterMinNo =
                GlobalCC.checkNullValues(txtMinCharacterNo.getValue());
            String countryCode =
                GlobalCC.checkNullValues(session.getAttribute("bnkcountrycode"));

            if (characterNo == null && renderer.isBNK_ACC_NO_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Number of Characterse is Empty");
                return null;
            }
            if (characterMaxNo == null &&
                renderer.isBNK_ACC_MAX_NO_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Maximum Number of Characters is Empty");
                return null;

            }
            if (characterMinNo == null &&
                renderer.isBNK_ACC_MIN_NO_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Minimum Number of Characters is Empty");
                return null;

            }
            if (countryCode == null && renderer.isBNK_COU_CODE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank Country is Empty");
                return null;

            }
            if (name == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank Name is Empty");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);

                    statement.setString(1, "A");
                    statement.setBigDecimal(2,
                                            bankCode == null ? null : new BigDecimal(bankCode));
                    statement.setString(3, name);
                    statement.setString(4, remark);
                    statement.setString(5, shortDesc);
                    statement.setBigDecimal(6,
                                            ddrCode == null ? null : new BigDecimal(ddrCode));
                    statement.setString(7, formatDesc);
                    statement.setBigDecimal(8,
                                            fwdBankCode == null ? null : new BigDecimal(fwdBankCode));
                    statement.setBigDecimal(9,
                                            kbaCode == null ? null : new BigDecimal(kbaCode));
                    statement.setString(10, eftSupport);
                    if (txtClassification.getValue() != null) {
                        statement.setObject(11, txtClassification.getValue());
                    } else {
                        statement.setObject(11, null);
                    }
                    statement.setObject(12, characterNo);
                    statement.setObject(13, txtNegotiatedBank.getValue());
                    statement.setObject(14,
                                        txtAdministrativeCharge.getValue());
                    UploadedFile _file =
                        (UploadedFile)uploadedPicture.getValue();

                    System.out.println("_file2 " + _file);
                    InputStream inp = null;
                    boolean isOk = false;
                    //client image

                    if (_file != null) {
                        isOk =
GlobalCC.validateUploadedImgFile(fileStream, "Logo Image", fileContent);
                        if (isOk == false) {
                            uploadedPicture.setValue(null);
                            GlobalCC.refreshUI(uploadedPicture);
                            // uploadSignature.setValue(null);
                            // GlobalCC.refreshUI(uploadSignature);
                            return null;

                        } else {
                            inp = _file.getInputStream();
                        }
                    }

                    statement.setBlob(15, fileStream);
                    if (GlobalCC.checkNullValues(txtWef.getValue()) != null) {
                        if (GlobalCC.checkNullValues(txtWef.getValue()).contains(":")) {
                            statement.setObject(16,
                                                GlobalCC.parseDate(txtWef.getValue()));
                        } else {
                            statement.setObject(16,
                                                GlobalCC.upDateParseDate(txtWef.getValue().toString()));
                        }

                    } else {
                        statement.setObject(16, null);
                    }
                    if (GlobalCC.checkNullValues(txtWet.getValue()) != null) {
                        if (GlobalCC.checkNullValues(txtWet.getValue()).contains(":")) {
                            statement.setObject(17,
                                                GlobalCC.parseDate(txtWet.getValue()));
                        } else {
                            statement.setObject(17,
                                                GlobalCC.upDateParseDate(txtWet.getValue().toString()));
                        }

                    } else {
                        statement.setObject(17, null);
                    }
                    statement.registerOutParameter(18, OracleTypes.VARCHAR);
                    statement.setString(19, slctBnkStatus);
                    statement.setString(20, characterMaxNo);
                    statement.setString(21, characterMinNo);
                    statement.setString(22, countryCode);
                    statement.execute();
                    String errMsg = statement.getString(18);
                    statement.close();
                    conn.commit();
                    conn.close();

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:bankPop" + "').hide(hints);");

                    ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                    GlobalCC.refreshUI(tblBanks);

                    ADFUtils.findIterator("fetchBankTerritoryByBankCodeIterator").executeQuery();
                    ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();

                    GlobalCC.refreshUI(tblBankTerritory);
                    GlobalCC.refreshUI(tblBankBranch);
                    btnDeleteBank.setDisabled(true);
                    btnEditBank.setDisabled(true);
                    GlobalCC.refreshUI(btnDeleteBank);
                    GlobalCC.refreshUI(btnEditBank);
                    if (errMsg == null) {
                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    } else {
                        GlobalCC.INFORMATIONREPORTING(errMsg);
                    }
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateBank() {
        String bankCode = GlobalCC.checkNullValues(txtBnkCode.getValue());
        String name = GlobalCC.checkNullValues(txtBnkBankName.getValue());
        String remark = GlobalCC.checkNullValues(txtBnkRemark.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtBnkShortDesc.getValue());
        String ddrCode = GlobalCC.checkNullValues(txtBnkDdrCode.getValue());
        String formatDesc =
            GlobalCC.checkNullValues(txtBnkFormatDesc.getValue());
        String fwdBankCode =
            GlobalCC.checkNullValues(txtBnkForwadingBankCode.getValue());
        String kbaCode = GlobalCC.checkNullValues(txtBnkKbaCode.getValue());
        String eftSupport =
            GlobalCC.checkNullValues(txtBnkEFTSupport.getValue());
        String slctBnkStatus =
            GlobalCC.checkNullValues(selectBnkStatus.getValue());
        String characterNo =
            GlobalCC.checkNullValues(txtCharacterNo.getValue());
        String characterMaxNo =
            GlobalCC.checkNullValues(txtMaxCharacterNo.getValue());
        String characterMinNo =
            GlobalCC.checkNullValues(txtMinCharacterNo.getValue());
        String countryCode =
            GlobalCC.checkNullValues(session.getAttribute("bnkcountrycode"));

        if (characterNo == null && renderer.isBNK_ACC_NO_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Number of Characterse is Empty");
            return null;
        }
        if (characterMaxNo == null && renderer.isBNK_ACC_MAX_NO_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Maximum Number of Characters is Empty");
            return null;

        }
        if (characterMinNo == null && renderer.isBNK_ACC_MIN_NO_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank A/C Minimum Number of Characters is Empty");
            return null;

        }
        if (countryCode == null && renderer.isBNK_COU_CODE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank Country is Empty");
            return null;

        }

        if (bankCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank Code is Empty");
            return null;

        } else if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank Name is Empty");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                statement.setString(1, "E");
                statement.setBigDecimal(2,
                                        bankCode == null ? null : new BigDecimal(bankCode));
                statement.setString(3, name);
                statement.setString(4, remark);
                statement.setString(5, shortDesc);
                statement.setBigDecimal(6,
                                        ddrCode == null ? null : new BigDecimal(ddrCode));
                statement.setString(7, formatDesc);
                statement.setBigDecimal(8,
                                        fwdBankCode == null ? null : new BigDecimal(fwdBankCode));
                statement.setBigDecimal(9,
                                        kbaCode == null ? null : new BigDecimal(kbaCode));
                statement.setString(10, eftSupport);
                if (txtClassification.getValue() != null) {
                    statement.setObject(11, txtClassification.getValue());
                } else {
                    statement.setObject(11, null);
                }
                statement.setObject(12, characterNo);
                statement.setObject(13, txtNegotiatedBank.getValue());
                statement.setObject(14, txtAdministrativeCharge.getValue());
                UploadedFile _file = (UploadedFile)uploadedPicture.getValue();

                System.out.println("_file2 " + _file);
                InputStream inp = null;
                boolean isOk = false;
                //client image

                if (_file != null) {
                    isOk =
GlobalCC.validateUploadedImgFile(fileStream, "Logo Image", fileContent);
                    if (isOk == false) {
                        uploadedPicture.setValue(null);
                        GlobalCC.refreshUI(uploadedPicture);
                        // uploadSignature.setValue(null);
                        // GlobalCC.refreshUI(uploadSignature);
                        return null;

                    } else {
                        inp = _file.getInputStream();
                    }
                }

                statement.setBlob(15, fileStream);
                if (GlobalCC.checkNullValues(txtWef.getValue()) != null) {
                    if (GlobalCC.checkNullValues(txtWef.getValue()).contains(":")) {
                        statement.setObject(16,
                                            GlobalCC.parseDate(txtWef.getValue()));
                    } else {
                        statement.setObject(16,
                                            GlobalCC.upDateParseDate(txtWef.getValue().toString()));
                    }

                } else {
                    statement.setObject(16, null);
                }
                if (GlobalCC.checkNullValues(txtWet.getValue()) != null) {
                    if (GlobalCC.checkNullValues(txtWet.getValue()).contains(":")) {
                        statement.setObject(17,
                                            GlobalCC.parseDate(txtWet.getValue()));
                    } else {
                        statement.setObject(17,
                                            GlobalCC.upDateParseDate(txtWet.getValue().toString()));
                    }

                } else {
                    statement.setObject(17, null);
                }
                statement.registerOutParameter(18, OracleTypes.VARCHAR);
                statement.setString(19, slctBnkStatus);
                statement.setString(20, characterMaxNo);
                statement.setString(21, characterMinNo);
                statement.setString(22, countryCode);
                statement.setString(23,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();
                String errMsg = statement.getString(18);
                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:bankPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                GlobalCC.refreshUI(tblBanks);

                ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
                GlobalCC.refreshUI(tblBankBranch);
                btnNewBank.setDisabled(false);
                btnDeleteBank.setDisabled(true);
                btnEditBank.setDisabled(true);
                GlobalCC.refreshUI(btnDeleteBank);
                GlobalCC.refreshUI(btnEditBank);
                if (errMsg == null) {
                    String message = "New Record Updated Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } else {
                    GlobalCC.INFORMATIONREPORTING(errMsg);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void clearBankBranchFields() {
        txtBbrCode.setValue(null);
        txtBbrBnkCode.setValue(session.getAttribute("bankCode"));
        txtBbrBranchName.setValue(null);
        txtBbrRemarks.setValue(null);
        txtBbrShortDesc.setValue(null);
        txtBbrRefCode.setValue(null);
        txtBbrEmail.setValue(null);
        txtBbrPersonName.setValue(null);
        txtBbrPersonPhone.setValue(null);
        txtBbrPersonEmail.setValue(null);
        txtBbrCountryCode.setValue(null);
        //txtBbrEFTSupport.setValue(null);
        //txtBbrDDSupport.setValue(null);
        txtBbrDateCreated.setValue(null);
        txtBbrCreatedBy.setValue(session.getAttribute("Username"));
        txtBbrPhysicalAddress.setValue(null);
        txtBbrPostalAddress.setValue(null);
        txtBbrKBACode.setValue(null);
        btnSaveUpdateBankBranch.setText("Save");
    }

    public String actionNewBankBranch() {
        if (session.getAttribute("bankCode") != null) {
            clearBankBranchFields();
            GlobalCC.showPop("crm:bankBranchPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Bank Record to proceed.");
            return null;
        }
        return null;
    }

    public void clearBankTerritoryFields() {
        txtBnktTerritoryName.setValue(null);
        txtBnktShtDesc.setValue(null);
        txtBnktCode.setValue(null);
        btnSaveBankTerritory.setText("Add");
    }

    public String actionNewBankTerritory() {
        try {
            if (session.getAttribute("bankCode") != null) {
                txtBnktTerritoryName.setValue(null);
                txtBnktShtDesc.setValue(null);
                txtBnktCode.setValue(null);
                btnSaveBankTerritory.setText("Add");
                GlobalCC.showPopup("crm:bankTerritoryPop");
            } else {
                GlobalCC.INFORMATIONREPORTING("You need to select an existing Bank Record to proceed.");
                return null;
            }
        } catch (Exception e) {
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }
        return null;
    }

    public String actionEditBankTerritory() {
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)tblBankTerritory.getSelectedRowData();
        if (nodeBinding != null) {
            txtBnktTerritoryName.setValue(nodeBinding.getAttribute("bankTerritoryName"));
            txtBnktShtDesc.setValue(nodeBinding.getAttribute("bankTerritoryShtDesc"));
            txtBnktCode.setValue(nodeBinding.getAttribute("bankTerritoryCode"));
            btnSaveBankTerritory.setText("Edit");
            btnSaveUpdateBankBranch.setText("Edit");
            GlobalCC.showPopup("crm:bankTerritoryPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionEditBankBranch() {
        Object key2 = tblBankBranch.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBbrCode.setValue(nodeBinding.getAttribute("branchCode"));
            txtBbrBnkCode.setValue(nodeBinding.getAttribute("branchBankCode"));
            txtBbrBranchName.setValue(nodeBinding.getAttribute("branchName"));
            txtBbrRemarks.setValue(nodeBinding.getAttribute("remarks"));
            txtBbrShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtBbrRefCode.setValue(nodeBinding.getAttribute("refCode"));
            txtBbrEFTSupport.setValue(nodeBinding.getAttribute("EFTSupported"));
            txtBbrDDSupport.setValue(nodeBinding.getAttribute("DDSupported"));
            txtBbrDateCreated.setValue(nodeBinding.getAttribute("dateCreated"));
            txtBbrCreatedBy.setValue(nodeBinding.getAttribute("createdBy"));
            txtBbrPhysicalAddress.setValue(nodeBinding.getAttribute("physicalAddress"));
            txtBbrPostalAddress.setValue(nodeBinding.getAttribute("postalAddress"));
            txtBbrKBACode.setValue(nodeBinding.getAttribute("KBACode"));
            txtBbrEmail.setValue(nodeBinding.getAttribute("branchEmail"));
            txtBbrPersonName.setValue(nodeBinding.getAttribute("branchPersonName"));
            txtBbrPersonPhone.setValue(nodeBinding.getAttribute("branchPersonPhone"));
            txtBbrPersonEmail.setValue(nodeBinding.getAttribute("branchPersonEmail"));
            txtBbrCountryCode.setValue(nodeBinding.getAttribute("countryCode"));

            btnSaveUpdateBankBranch.setText("Edit");
            GlobalCC.showPop("crm:bankBranchPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteBankBranch() {

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;

        try {
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblBankBranch.getSelectedRowData();

            String bbrCode = nodeBinding.getAttribute("branchCode").toString();
            if (nodeBinding == null) {
                GlobalCC.INFORMATIONREPORTING("You need to select an existing Bank Record to proceed.");
                return null;
            }


            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "D");
            statement.setBigDecimal(2, new BigDecimal(bbrCode));
            statement.setBigDecimal(3, null);
            statement.setString(4, null);
            statement.setString(5, null);
            statement.setString(6, null);
            statement.setString(7, null);
            statement.setString(8, null);
            statement.setString(9, null);
            statement.setDate(10, null);
            statement.setString(11, null);
            statement.setString(12, null);
            statement.setString(13, null);
            statement.setString(14, null);
            statement.registerOutParameter(15, OracleTypes.VARCHAR);
            statement.setString(16, null);

            statement.execute();
            String errMsg = statement.getString(15);
            statement.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankBranch);
            btnNewBankBranch.setDisabled(false);
            btnDeleteBnkBranch.setDisabled(true);
            btnEditBankBranch.setDisabled(true);
            GlobalCC.refreshUI(btnDeleteBnkBranch);
            GlobalCC.refreshUI(btnEditBankBranch);

            if (errMsg == null) {
                String message = " Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
            } else {
                GlobalCC.INFORMATIONREPORTING(errMsg);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }

        return null;

    }


    public String actionDeleteBankTerritory() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        BigDecimal territoryCode = null;
        try {
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblBankTerritory.getSelectedRowData();
            if (nodeBinding == null) {
                GlobalCC.INFORMATIONREPORTING("You need to select an existing Bank Record to proceed.");
                return null;
            }
            territoryCode =
                    GlobalCC.checkBDNullValues(nodeBinding.getAttribute("bankTerritoryCode"));
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.bank_territory_prc(?,?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "D");
            statement.setBigDecimal(2, territoryCode);
            statement.setString(3, null);
            statement.setString(4, null);
            statement.setString(5, null);
            statement.execute();
            conn.commit();
            conn.close();
            statement.close();

            String message = " Record DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

            ADFUtils.findIterator("fetchBankTerritoryByBankCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankTerritory);
            ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankBranch);

            btnNewBankTerritory.setDisabled(false);
            btnDeleteBankTerritory.setDisabled(true);
            btnEditBankTerritory.setDisabled(true);

            GlobalCC.refreshUI(btnNewBankTerritory);
            GlobalCC.refreshUI(btnDeleteBankTerritory);
            GlobalCC.refreshUI(btnEditBankTerritory);
        } catch (Exception e) {
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }


        return null;
    }


    public String actionSaveUpdateBankBranch() {
        if (btnSaveUpdateBankBranch.getText().equals("Edit")) {
            actionUpdateBankBranch();
        } else {
            String branchCode =
                GlobalCC.checkNullValues(txtBbrCode.getValue());
            String branchBankCode =
                GlobalCC.checkNullValues(txtBbrBnkCode.getValue());
            String branchName =
                GlobalCC.checkNullValues(txtBbrBranchName.getValue());
            String remarks =
                GlobalCC.checkNullValues(txtBbrRemarks.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtBbrShortDesc.getValue());
            String refCode =
                GlobalCC.checkNullValues(txtBbrRefCode.getValue());
            String eftSupport =
                GlobalCC.checkNullValues(txtBbrEFTSupport.getValue());
            String ddSupport =
                GlobalCC.checkNullValues(txtBbrDDSupport.getValue());
            String dateCreated =
                GlobalCC.checkNullValues(txtBbrDateCreated.getValue());
            String createdBy =
                GlobalCC.checkNullValues(txtBbrCreatedBy.getValue());
            String physicalAddress =
                GlobalCC.checkNullValues(txtBbrPhysicalAddress.getValue());
            String postalAddress =
                GlobalCC.checkNullValues(txtBbrPostalAddress.getValue());
            String kbaCode =
                GlobalCC.checkNullValues(txtBbrKBACode.getValue());
            String bbrEmail = GlobalCC.checkNullValues(txtBbrEmail.getValue());
            String bbrPersonName =
                GlobalCC.checkNullValues(txtBbrPersonName.getValue());
            String bbrPersonPhone =
                GlobalCC.checkNullValues(txtBbrPersonPhone.getValue());
            String bbrPersonEmail =
                GlobalCC.checkNullValues(txtBbrPersonEmail.getValue());
            String bbrCountryCode =
                GlobalCC.checkNullValues(txtBbrCountryCode.getValue());

            BigDecimal territoryCode =
                GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

            if (branchBankCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Bank has not been selected.");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (bbrEmail == null && renderer.isBBR_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Branch Email is Empty");
                return null;

            } else if (bbrPersonName == null &&
                       renderer.isBBR_PERSON_NAME_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Name is Empty");
                return null;
            } else if (bbrPersonEmail == null &&
                       renderer.isBBR_PERSON_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Email is Empty");
                return null;
            } else if (bbrCountryCode == null &&
                       renderer.isBBR_PERSON_PHONE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
                return null;
            } else if (bbrPersonPhone == null &&
                       renderer.isBBR_PERSON_PHONE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Phone is Empty");
                return null;
            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {

                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpDateCreated = new Date();
                    if (txtBbrDateCreated.getValue() != null &&
                        !(txtBbrDateCreated.getValue().equals(""))) {
                        String date1 = df.format(txtBbrDateCreated.getValue());
                        tmpDateCreated = df.parse(date1);
                    }

                    statement.setString(1, "A");
                    statement.setBigDecimal(2,
                                            branchCode == null ? null : new BigDecimal(branchCode));
                    statement.setBigDecimal(3,
                                            branchBankCode == null ? null : new BigDecimal(branchBankCode));
                    statement.setString(4, branchName);
                    statement.setString(5, remarks);
                    statement.setString(6, shortDesc);
                    statement.setString(7, refCode);
                    statement.setString(8, eftSupport);
                    statement.setString(9, ddSupport);
                    statement.setDate(10,
                                      tmpDateCreated == null ? null : new java.sql.Date(tmpDateCreated.getTime()));
                    statement.setString(11,
                                        session.getAttribute("Username").toString());
                    statement.setString(12, physicalAddress);
                    statement.setString(13, postalAddress);
                    statement.setString(14, kbaCode);
                    statement.registerOutParameter(15, OracleTypes.VARCHAR);
                    statement.setBigDecimal(16, territoryCode);
                    statement.setString(17, bbrEmail);
                    statement.setString(18, bbrPersonName);
                    statement.setString(19, bbrPersonEmail);
                    statement.setString(20, bbrCountryCode);
                    statement.setString(21, bbrPersonPhone);

                    statement.execute();
                    String errMsg = statement.getString(15);
                    statement.close();
                    conn.commit();
                    conn.close();

                    GlobalCC.hidePopup("crm:bankBranchPop");
                    ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
                    GlobalCC.refreshUI(tblBankBranch);

                    btnNewBankBranch.setDisabled(false);
                    btnDeleteBnkBranch.setDisabled(true);
                    btnEditBankBranch.setDisabled(true);

                    GlobalCC.refreshUI(btnNewBankBranch);
                    GlobalCC.refreshUI(btnDeleteBnkBranch);
                    GlobalCC.refreshUI(btnEditBankBranch);

                    if (errMsg == null) {
                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    } else {
                        GlobalCC.INFORMATIONREPORTING(errMsg);
                        GlobalCC.EXCEPTIONREPORTING(errMsg);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                    GlobalCC.INFORMATIONREPORTING(e.getMessage());
                }
            }
        }
        return null;
    }


    public String actionUpdateBankBranch() {
        String branchCode = GlobalCC.checkNullValues(txtBbrCode.getValue());
        String branchBankCode =
            GlobalCC.checkNullValues(txtBbrBnkCode.getValue());
        String branchName =
            GlobalCC.checkNullValues(txtBbrBranchName.getValue());
        String remarks = GlobalCC.checkNullValues(txtBbrRemarks.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtBbrShortDesc.getValue());
        String refCode = GlobalCC.checkNullValues(txtBbrRefCode.getValue());
        String eftSupport =
            GlobalCC.checkNullValues(txtBbrEFTSupport.getValue());
        String ddSupport =
            GlobalCC.checkNullValues(txtBbrDDSupport.getValue());
        String dateCreated =
            GlobalCC.checkNullValues(txtBbrDateCreated.getValue());
        String createdBy =
            GlobalCC.checkNullValues(txtBbrCreatedBy.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtBbrPhysicalAddress.getValue());
        String postalAddress =
            GlobalCC.checkNullValues(txtBbrPostalAddress.getValue());
        String kbaCode = GlobalCC.checkNullValues(txtBbrKBACode.getValue());
        String bbrEmail = GlobalCC.checkNullValues(txtBbrEmail.getValue());
        String bbrPersonName =
            GlobalCC.checkNullValues(txtBbrPersonName.getValue());
        String bbrPersonPhone =
            GlobalCC.checkNullValues(txtBbrPersonPhone.getValue());
        String bbrPersonEmail =
            GlobalCC.checkNullValues(txtBbrPersonEmail.getValue());
        String bbrCountryCode =
            GlobalCC.checkNullValues(txtBbrCountryCode.getValue());

        BigDecimal territoryCode =
            GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

        if (territoryCode == null && renderer.isBANK_TERRITORY_APPLICABLE()) {
            GlobalCC.INFORMATIONREPORTING("Select Bank Territory!");
            return null;
        }

        if (branchCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch Code is Empty.");
            return null;

        } else if (branchBankCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank has not been selected.");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (bbrEmail == null && renderer.isBBR_EMAIL_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch Email is Empty");
            return null;

        } else if (bbrPersonName == null &&
                   renderer.isBBR_PERSON_NAME_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Name is Empty");
            return null;
        } else if (bbrPersonEmail == null &&
                   renderer.isBBR_PERSON_EMAIL_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Email is Empty");
            return null;
        } else if (bbrCountryCode == null &&
                   renderer.isBBR_PERSON_PHONE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
            return null;
        } else if (bbrPersonPhone == null &&
                   renderer.isBBR_PERSON_PHONE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Phone is Empty");
            return null;
        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpDateCreated = new Date();
                if (txtBbrDateCreated.getValue() != null &&
                    !(txtBbrDateCreated.getValue().equals(""))) {
                    String date1 = df.format(txtBbrDateCreated.getValue());
                    tmpDateCreated = df.parse(date1);
                }

                statement.setString(1, "E");
                statement.setBigDecimal(2,
                                        branchCode == null ? null : new BigDecimal(branchCode));
                statement.setBigDecimal(3,
                                        branchBankCode == null ? null : new BigDecimal(branchBankCode));
                statement.setString(4, branchName);
                statement.setString(5, remarks);
                statement.setString(6, shortDesc);
                statement.setString(7, refCode);
                statement.setString(8, eftSupport);
                statement.setString(9, ddSupport);
                statement.setDate(10,
                                  tmpDateCreated == null ? null : new java.sql.Date(tmpDateCreated.getTime()));
                statement.setString(11,
                                    session.getAttribute("Username").toString());
                statement.setString(12, physicalAddress);
                statement.setString(13, postalAddress);
                statement.setString(14, kbaCode);

                statement.registerOutParameter(15, OracleTypes.VARCHAR);

                statement.setBigDecimal(16, territoryCode);
                statement.setString(17, bbrEmail);
                statement.setString(18, bbrPersonName);
                statement.setString(19, bbrPersonEmail);
                statement.setString(20, bbrCountryCode);
                statement.setString(21, bbrPersonPhone);
                statement.execute();
                String errMsg = statement.getString(15);
                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:bankBranchPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
                GlobalCC.refreshUI(tblBankBranch);
                btnNewBankBranch.setDisabled(false);
                btnDeleteBnkBranch.setDisabled(true);
                btnEditBankBranch.setDisabled(true);

                GlobalCC.refreshUI(btnNewBankBranch);
                GlobalCC.refreshUI(btnDeleteBnkBranch);
                GlobalCC.refreshUI(btnEditBankBranch);

                if (errMsg == null) {
                    String message = "New Record Updated Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } else {
                    GlobalCC.INFORMATIONREPORTING(errMsg);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }


    public String actionSaveBankTerritory() {
        String action =
            btnSaveBankTerritory.getText().equals("Edit") ? "E" : "A";
        BigDecimal territoryCode =
            GlobalCC.checkBDNullValues(txtBnktCode.getValue());
        BigDecimal territoryBankCode =
            GlobalCC.checkBDNullValues(session.getAttribute("bankCode"));
        String territoryName =
            GlobalCC.checkNullValues(txtBnktTerritoryName.getValue());
        String shortDesc = GlobalCC.checkNullValues(txtBnktShtDesc.getValue());

        if (territoryBankCode.compareTo(BigDecimal.ZERO) == 0) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank has not been selected.");
            return null;
        }
        if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;
        }
        if (territoryName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Banck Territory Name not entered!.");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin   tqc_setups_pkg.bank_territory_prc(?,?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);

            statement.setString(1, action);
            statement.setBigDecimal(2,
                                    action.equals("E") ? territoryCode : null);
            statement.setString(3, territoryName);
            statement.setString(4, shortDesc);
            statement.setBigDecimal(5, territoryBankCode);

            statement.execute();
            conn.commit();

            GlobalCC.hidePopup("crm:bankTerritoryPop");
            ADFUtils.findIterator("fetchBankTerritoryByBankCodeIterator").executeQuery();
            GlobalCC.refreshUI(tblBankTerritory);

            btnNewBankTerritory.setDisabled(false);
            btnDeleteBankTerritory.setDisabled(true);
            btnEditBankTerritory.setDisabled(true);

            GlobalCC.refreshUI(btnNewBankTerritory);
            GlobalCC.refreshUI(btnDeleteBankTerritory);
            GlobalCC.refreshUI(btnEditBankTerritory);

            String message =
                action.equals("E") ? "Record Updated Successfully!" :
                "New Record Added Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }
        return null;
    }

    public String actionShowDirectDebitReports() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:directDebitReportsPop" + "').show(hints);");
        return null;
    }

    public String actionShowTowns() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:TownsPop" +
                             "').show(hints);");
        return null;
    }

    public String actionShowBankAssuranceTowns() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:bankAssuranceTowns" + "').show(hints);");
        return null;
    }

    public String countryCodes() {
        GlobalCC.showPopup("crm:countryCodesPopUp");

        return null;
    }

    public String bankAssuranceCountryCodes() {
        GlobalCC.showPopup("crm:bancassurancecountries");
        return null;
    }

    public String actionShowForwadingBanks() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:forwadingBanksPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptForwadingBank() {
        Object key2 = tblForwadingBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkForwadingBankCode.setValue(nodeBinding.getAttribute("bankCode"));
            txtBnkFwdBankName.setValue(nodeBinding.getAttribute("bankName"));
        }

        GlobalCC.refreshUI(panelBankDetails);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:forwadingBanksPop" + "').hide(hints);");
        return null;
    }

    public String actionAcceptDdReport() {
        Object key2 = tblDDReports.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkDdrCode.setValue(nodeBinding.getAttribute("ddrCode"));
            txtBnkDdrDesc.setValue(nodeBinding.getAttribute("ddrReportDesc"));
        }

        GlobalCC.refreshUI(panelBankDetails);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:directDebitReportsPop" + "').hide(hints);");
        return null;
    }

    public String actionShowDeleteBanks() {
        Object key2 = tblBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            GlobalCC.showPopup("crm:confirmDeleteBank");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

    }

    public String actionShowDeleteBankBranch() {
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)tblBankBranch.getSelectedRowData();
        if (nodeBinding != null) {
            GlobalCC.showPopup("crm:confirmDeleteBankBranch");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
        }
        return null;
    }

    public String actionShowDeleteBankTerritory() {
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)tblBankTerritory.getSelectedRowData();
        if (nodeBinding != null) {
            GlobalCC.showPopup("crm:confirmDeleteBankTerritory");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
        }
        return null;
    }

    public void setPanelBankDetails(RichPanelBox panelBankDetails) {
        this.panelBankDetails = panelBankDetails;
    }

    public RichPanelBox getPanelBankDetails() {
        return panelBankDetails;
    }


    public void actionConfirmDeleteBank(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String actionDeleteBank = actionDeleteBank();
        }

        // Add event code here...
    }

    public void actionConfirmDeleteBankBranch(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            //do nothing
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String actionDeleteBank = actionDeleteBankBranch();
        }
    }

    public void actionConfirmDeleteBankTerritory(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            String actionDeleteBank = actionDeleteBankTerritory();
        }
    }

    public void actiontblBankBranchSelected(SelectionEvent selectionEvent) {
        btnDeleteBnkBranch.setDisabled(false);
        btnEditBankBranch.setDisabled(false);
        GlobalCC.refreshUI(btnDeleteBnkBranch);
        GlobalCC.refreshUI(btnEditBankBranch);
        btnAddAssuranceBranch.setDisabled(false);
//        btnEditAssuranceBranch.setDisabled(false);
//        btnDeleteAssuranceBranch.setDisabled(false);
        GlobalCC.refreshUI(btnAddAssuranceBranch);
//        GlobalCC.refreshUI(btnEditAssuranceBranch);
//        GlobalCC.refreshUI(btnDeleteAssuranceBranch);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)tblBankBranch.getSelectedRowData();
        session.setAttribute("branchCode",
                             nodeBinding.getAttribute("branchCode").toString());
        txtBbbBrnCode.setValue(nodeBinding.getAttribute("branchCode"));
        ADFUtils.findIterator("fetchBancassuranceByBranchCodeIterator").executeQuery();
        GlobalCC.refreshUI(bancassurancelov);
    }


    public void actionTableBankTerritorySelected(SelectionEvent selectionEvent) {
        try {
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblBankTerritory.getSelectedRowData();
            if (nodeBinding != null) {
                session.setAttribute("territoryCode",
                                     nodeBinding.getAttribute("bankTerritoryCode"));
                ADFUtils.findIterator("fetchBankBranchByTerritoryCodeIterator").executeQuery();
                GlobalCC.refreshUI(tblBankBranch);
                btnDeleteBankTerritory.setDisabled(false);
                btnEditBankTerritory.setDisabled(false);
                btnNewBankTerritory.setDisabled(false);
                btnNewBankBranch.setDisabled(false);
                GlobalCC.refreshUI(btnNewBankBranch);
                GlobalCC.refreshUI(btnDeleteBankTerritory);
                GlobalCC.refreshUI(btnEditBankTerritory);
                GlobalCC.refreshUI(btnNewBankTerritory);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }
    }


    public void setBtnEditBankBranch(RichCommandButton btnEditBankBranch) {
        this.btnEditBankBranch = btnEditBankBranch;
    }

    public RichCommandButton getBtnEditBankBranch() {
        return btnEditBankBranch;
    }

    public void setBtnDeleteBnkBranch(RichCommandButton btnDeleteBnkBranch) {
        this.btnDeleteBnkBranch = btnDeleteBnkBranch;
    }

    public RichCommandButton getBtnDeleteBnkBranch() {
        return btnDeleteBnkBranch;
    }

    public void setBtnEditBank(RichCommandButton btnEditBank) {
        this.btnEditBank = btnEditBank;
    }

    public RichCommandButton getBtnEditBank() {
        return btnEditBank;
    }

    public void setBtnDeleteBank(RichCommandButton btnDeleteBank) {
        this.btnDeleteBank = btnDeleteBank;
    }

    public RichCommandButton getBtnDeleteBank() {
        return btnDeleteBank;
    }

    public void setBtnNewBankBranch(RichCommandButton btnNewBankBranch) {
        this.btnNewBankBranch = btnNewBankBranch;
    }

    public RichCommandButton getBtnNewBankBranch() {
        return btnNewBankBranch;
    }

    public void setGrpEmploymentDetails(RichPanelGroupLayout grpEmploymentDetails) {
        this.grpEmploymentDetails = grpEmploymentDetails;
    }

    public RichPanelGroupLayout getGrpEmploymentDetails() {
        return grpEmploymentDetails;
    }

    public String actionCancelFwdingBnk() {
        GlobalCC.dismissPopUp("crm", "forwadingBanksPop");
        return null;
    }

    public String actionCancelDD() {
        GlobalCC.dismissPopUp("crm", "directDebitReportsPop");

        return null;
    }

    public void setTxtBnkEFTSupport(RichSelectOneChoice txtBnkEFTSupport) {
        this.txtBnkEFTSupport = txtBnkEFTSupport;
    }

    public RichSelectOneChoice getTxtBnkEFTSupport() {
        return txtBnkEFTSupport;
    }

    public void setTxtClassification(RichSelectOneChoice txtClassification) {
        this.txtClassification = txtClassification;
    }

    public RichSelectOneChoice getTxtClassification() {
        return txtClassification;
    }

    public void setTxtCharacterNo(RichInputNumberSpinbox txtCharacterNo) {
        this.txtCharacterNo = txtCharacterNo;
    }

    public RichInputNumberSpinbox getTxtCharacterNo() {
        return txtCharacterNo;
    }

    public void setTxtAdministrativeCharge(RichInputNumberSpinbox txtAdministrativeCharge) {
        this.txtAdministrativeCharge = txtAdministrativeCharge;
    }

    public RichInputNumberSpinbox getTxtAdministrativeCharge() {
        return txtAdministrativeCharge;
    }

    public void setTxtNegotiatedBank(RichSelectOneChoice txtNegotiatedBank) {
        this.txtNegotiatedBank = txtNegotiatedBank;
    }

    public RichSelectOneChoice getTxtNegotiatedBank() {
        return txtNegotiatedBank;
    }

    public void setBankTbl(RichTable bankTbl) {
        this.bankTbl = bankTbl;
    }

    public RichTable getBankTbl() {
        return bankTbl;
    }

    public String selectBankDtls() {
        Object key = bankTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a bank");
            return null;
        }
        session.setAttribute("bankCode", session.getAttribute("bankCode"));
        txtBank.setValue(r.getAttribute("bankName"));
        GlobalCC.refreshUI(txtBank);
        return null;
    }

    public void setTxtBank(RichInputText txtBank) {
        this.txtBank = txtBank;
    }

    public RichInputText getTxtBank() {
        return txtBank;
    }

    public void setTxtBankName(RichInputText txtBankName) {
        this.txtBankName = txtBankName;
    }

    public RichInputText getTxtBankName() {
        return txtBankName;
    }

    public void setTxtBankTableName(RichInputText txtBankTableName) {
        this.txtBankTableName = txtBankTableName;
    }

    public RichInputText getTxtBankTableName() {
        return txtBankTableName;
    }

    public String selectNextTransition() {
        if (bankTab.isRendered()) {
            if (txtBank.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select bank");
                return null;
            }
            if (txtBankTableName.getValue() == null) {

                GlobalCC.INFORMATIONREPORTING("Please select a table");
                return null;
            }
            Integer TotalColumns =
                ValidateTableName(txtBankTableName.getValue().toString());
            if (TotalColumns == 0) {
                GlobalCC.errorValueNotEntered("The Table is Invalid");
                return null;
            }

        }
        return null;
    }

    public void setBankTab(RichPanelBox bankTab) {
        this.bankTab = bankTab;
    }

    public RichPanelBox getBankTab() {
        return bankTab;
    }

    public void setBankDtlsTempTab(RichPanelBox bankDtlsTempTab) {
        this.bankDtlsTempTab = bankDtlsTempTab;
    }

    public RichPanelBox getBankDtlsTempTab() {
        return bankDtlsTempTab;
    }

    public Integer ValidateTableName(String TableName) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        conn = (OracleConnection)dbConnector.getDatabaseConnection();
        Integer TotalColumns = 0;
        try {
            String CountQuery =
                "SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME='" +
                TableName + "'";
            OracleCallableStatement cst1 = null;
            cst1 = (OracleCallableStatement)conn.prepareCall(CountQuery);
            ResultSet rs1 = cst1.executeQuery();
            while (rs1.next()) {
                TotalColumns = rs1.getInt(1);
            }
            cst1.close();
            rs1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return TotalColumns;
    }

    public void setUploadedPicture(RichInputFile uploadedPicture) {
        this.uploadedPicture = uploadedPicture;
    }

    public RichInputFile getUploadedPicture() {
        return uploadedPicture;
    }

    public void ImageUploadedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

    }

    public void setPictureFile(UploadedFile pictureFile) {
        if (pictureFile != null) {
            this.filename = pictureFile.getFilename();
            this.filesize = pictureFile.getLength();
            this.fileContent = pictureFile.getContentType();
            try {
                fileStream = pictureFile.getInputStream();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.pictureFile = pictureFile;
    }

    public UploadedFile getPictureFile() {
        return pictureFile;
    }

    public void setTxtWef(RichInputDate txtWef) {
        this.txtWef = txtWef;
    }

    public RichInputDate getTxtWef() {
        return txtWef;
    }

    public void setTxtWet(RichInputDate txtWet) {
        this.txtWet = txtWet;
    }

    public RichInputDate getTxtWet() {
        return txtWet;
    }

    public void setBankLogo(RichImage bankLogo) {
        this.bankLogo = bankLogo;
    }

    public RichImage getBankLogo() {
        return bankLogo;
    }

    public void setTxtBankTownName(RichInputText txtBankTownName) {
        this.txtBankTownName = txtBankTownName;
    }

    public RichInputText getTxtBankTownName() {
        return txtBankTownName;
    }

    public void setTxtBankTownCode(RichInputText txtBankTownCode) {
        this.txtBankTownCode = txtBankTownCode;
    }

    public RichInputText getTxtBankTownCode() {
        return txtBankTownCode;
    }

    public void setTblTowns(RichTable tblTowns) {
        this.tblTowns = tblTowns;
    }

    public RichTable getTblTowns() {
        return tblTowns;
    }

    public String acceptTownSelect() {

        Object key = tblTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBankTownCode.setValue(nodeBinding.getAttribute("code"));
            txtBankTownName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtBankTownName);
            GlobalCC.refreshUI(txtBankTownCode);
        }
        GlobalCC.dismissPopUp("crm", "TownsPop");
        return null;

    }

    public String hideTowns() {
        // Add event code here...
        GlobalCC.dismissPopUp("crm", "TownsPop");
        return null;
    }

    public String actionAcceptCountryCode() {
        Object key = tblCountryCode.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBbrCountryCode.setValue(nodeBinding.getAttribute("countryCode"));
            GlobalCC.refreshUI(txtBbrCountryCode);
        }
        GlobalCC.dismissPopUp("crm", "countryCodesPopUp");
        return null;
    }

    public String hideCountryPopUp() {
        GlobalCC.dismissPopUp("crm", "countryCodesPopUp");
        return null;
    }


    public void setBtnSaveBankTerritory(RichCommandButton btnSaveBankTerritory) {
        this.btnSaveBankTerritory = btnSaveBankTerritory;
    }

    public RichCommandButton getBtnSaveBankTerritory() {
        return btnSaveBankTerritory;
    }

    public void setBtnNewBankTerritory(RichCommandButton btnNewBankTerritory) {
        this.btnNewBankTerritory = btnNewBankTerritory;
    }

    public RichCommandButton getBtnNewBankTerritory() {
        return btnNewBankTerritory;
    }

    public void setTblBankTerritory(RichTable tblBankTerritory) {
        this.tblBankTerritory = tblBankTerritory;
    }

    public RichTable getTblBankTerritory() {
        return tblBankTerritory;
    }

    public void setBtnEditBankTerritory(RichCommandButton btnEditBankTerritory) {
        this.btnEditBankTerritory = btnEditBankTerritory;
    }

    public RichCommandButton getBtnEditBankTerritory() {
        return btnEditBankTerritory;
    }

    public void setBtnDeleteBankTerritory(RichCommandButton btnDeleteBankTerritory) {
        this.btnDeleteBankTerritory = btnDeleteBankTerritory;
    }

    public RichCommandButton getBtnDeleteBankTerritory() {
        return btnDeleteBankTerritory;
    }

    public void setTxtBnktCode(RichInputText txtBnktCode) {
        this.txtBnktCode = txtBnktCode;
    }

    public RichInputText getTxtBnktCode() {
        return txtBnktCode;
    }

    public void setTxtBnktTerritoryName(RichInputText txtBnktTerritoryName) {
        this.txtBnktTerritoryName = txtBnktTerritoryName;
    }

    public RichInputText getTxtBnktTerritoryName() {
        return txtBnktTerritoryName;
    }

    public void setTxtBnktShtDesc(RichInputText txtBnktShtDesc) {
        this.txtBnktShtDesc = txtBnktShtDesc;
    }

    public RichInputText getTxtBnktShtDesc() {
        return txtBnktShtDesc;
    }

    public void setTxtBnktBnkCode(RichInputNumberSpinbox txtBnktBnkCode) {
        this.txtBnktBnkCode = txtBnktBnkCode;
    }

    public RichInputNumberSpinbox getTxtBnktBnkCode() {
        return txtBnktBnkCode;
    }

    public void setTxtBbrBnktCode(RichInputNumberSpinbox txtBbrBnktCode) {
        this.txtBbrBnktCode = txtBbrBnktCode;
    }

    public RichInputNumberSpinbox getTxtBbrBnktCode() {
        return txtBbrBnktCode;
    }

    public void setTxtBbrBnktShtDesc(RichInputText txtBbrBnktShtDesc) {
        this.txtBbrBnktShtDesc = txtBbrBnktShtDesc;
    }

    public RichInputText getTxtBbrBnktShtDesc() {
        return txtBbrBnktShtDesc;
    }

    public void setBtnNewBank(RichCommandButton btnNewBank) {
        this.btnNewBank = btnNewBank;
    }

    public RichCommandButton getBtnNewBank() {
        return btnNewBank;
    }

    public void setTxtBbrEmail(RichInputText txtBbrEmail) {
        this.txtBbrEmail = txtBbrEmail;
    }

    public RichInputText getTxtBbrEmail() {
        return txtBbrEmail;
    }

    public void setTxtBbrPersonName(RichInputText txtBbrPersonName) {
        this.txtBbrPersonName = txtBbrPersonName;
    }

    public RichInputText getTxtBbrPersonName() {
        return txtBbrPersonName;
    }

    public void setTxtBbrPersonPhone(RichInputText txtBbrPersonPhone) {
        this.txtBbrPersonPhone = txtBbrPersonPhone;
    }

    public RichInputText getTxtBbrPersonPhone() {
        return txtBbrPersonPhone;
    }

    public void setTxtBbrPersonEmail(RichInputText txtBbrPersonEmail) {
        this.txtBbrPersonEmail = txtBbrPersonEmail;
    }

    public RichInputText getTxtBbrPersonEmail() {
        return txtBbrPersonEmail;
    }

    public void setTxtBbrCountryCode(RichInputText txtBbrCountryCode) {
        this.txtBbrCountryCode = txtBbrCountryCode;
    }

    public RichInputText getTxtBbrCountryCode() {
        return txtBbrCountryCode;
    }

    public void setTblCountryCode(RichTable tblCountryCode) {
        this.tblCountryCode = tblCountryCode;
    }

    public RichTable getTblCountryCode() {
        return tblCountryCode;
    }

    public void setSelectBnkStatus(RichSelectOneChoice selectBnkStatus) {
        this.selectBnkStatus = selectBnkStatus;
    }

    public RichSelectOneChoice getSelectBnkStatus() {
        return selectBnkStatus;
    }

    public void setTxtCountry(RichInputText txtCountry) {
        this.txtCountry = txtCountry;
    }

    public RichInputText getTxtCountry() {
        return txtCountry;
    }

    public void setTxtMaxCharacterNo(RichInputNumberSpinbox txtMaxCharacterNo) {
        this.txtMaxCharacterNo = txtMaxCharacterNo;
    }

    public RichInputNumberSpinbox getTxtMaxCharacterNo() {
        return txtMaxCharacterNo;
    }

    public void setTxtMinCharacterNo(RichInputNumberSpinbox txtMinCharacterNo) {
        this.txtMinCharacterNo = txtMinCharacterNo;
    }

    public RichInputNumberSpinbox getTxtMinCharacterNo() {
        return txtMinCharacterNo;
    }

    public void setcountryTbl(RichTable countryTbl) {
        this.countryTbl = countryTbl;
    }

    public RichTable getcountryTbl() {
        return countryTbl;
    }

    public String selectCountry() {
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtCountry.setValue(r.getAttribute("couName"));
        session.setAttribute("bnkcountrycode", r.getAttribute("couCode"));
        GlobalCC.refreshUI(txtCountry);
        GlobalCC.hidePopup("crm:countryPopup");
        return null;
    }

    public void setBancassurancelov(RichTable bancassurancelov) {
        this.bancassurancelov = bancassurancelov;
    }

    public RichTable getBancassurancelov() {
        return bancassurancelov;
    }

    public void setTxtBbbCode(RichInputText txtBbbCode) {
        this.txtBbbCode = txtBbbCode;
    }

    public RichInputText getTxtBbbCode() {
        return txtBbbCode;
    }

    public void setTxtBbbBrnCode(RichInputText txtBbbBrnCode) {
        this.txtBbbBrnCode = txtBbbBrnCode;
    }

    public RichInputText getTxtBbbBrnCode() {
        return txtBbbBrnCode;
    }

    public void setTxtBbbBranchName(RichInputText txtBbbBranchName) {
        this.txtBbbBranchName = txtBbbBranchName;
    }

    public RichInputText getTxtBbbBranchName() {
        return txtBbbBranchName;
    }

    public void setTxtBbbEmail(RichInputText txtBbbEmail) {
        this.txtBbbEmail = txtBbbEmail;
    }

    public RichInputText getTxtBbbEmail() {
        return txtBbbEmail;
    }

    public void setTxtBbbShortDesc(RichInputText txtBbbShortDesc) {
        this.txtBbbShortDesc = txtBbbShortDesc;
    }

    public RichInputText getTxtBbbShortDesc() {
        return txtBbbShortDesc;
    }

    public void setTxtBbbRemarks(RichInputText txtBbbRemarks) {
        this.txtBbbRemarks = txtBbbRemarks;
    }

    public RichInputText getTxtBbbRemarks() {
        return txtBbbRemarks;
    }

    public void setTxtBbrTownName(RichInputText txtBbrTownName) {
        this.txtBbrTownName = txtBbrTownName;
    }

    public RichInputText getTxtBbrTownName() {
        return txtBbrTownName;
    }

    public void setTxtBbbTownCode(RichInputText txtBbbTownCode) {
        this.txtBbbTownCode = txtBbbTownCode;
    }

    public RichInputText getTxtBbbTownCode() {
        return txtBbbTownCode;
    }

    public void setTxtBbbTownName(RichInputText txtBbbTownName) {
        this.txtBbbTownName = txtBbbTownName;
    }

    public RichInputText getTxtBbbTownName() {
        return txtBbbTownName;
    }

    public void setTxtBbbDateCreated(RichInputDate txtBbbDateCreated) {
        this.txtBbbDateCreated = txtBbbDateCreated;
    }

    public RichInputDate getTxtBbbDateCreated() {
        return txtBbbDateCreated;
    }

    public void setTxtBbbCreatedBy(RichInputText txtBbbCreatedBy) {
        this.txtBbbCreatedBy = txtBbbCreatedBy;
    }

    public RichInputText getTxtBbbCreatedBy() {
        return txtBbbCreatedBy;
    }

    public void setTxtBbbPersonName(RichInputText txtBbbPersonName) {
        this.txtBbbPersonName = txtBbbPersonName;
    }

    public RichInputText getTxtBbbPersonName() {
        return txtBbbPersonName;
    }

    public void setTxtBbbCountryCode(RichInputText txtBbbCountryCode) {
        this.txtBbbCountryCode = txtBbbCountryCode;
    }

    public RichInputText getTxtBbbCountryCode() {
        return txtBbbCountryCode;
    }

    public void setTxtBbbPersonPhone(RichInputText txtBbbPersonPhone) {
        this.txtBbbPersonPhone = txtBbbPersonPhone;
    }

    public RichInputText getTxtBbbPersonPhone() {
        return txtBbbPersonPhone;
    }

    public void setTxtBbbPersonEmail(RichInputText txtBbbPersonEmail) {
        this.txtBbbPersonEmail = txtBbbPersonEmail;
    }

    public RichInputText getTxtBbbPersonEmail() {
        return txtBbbPersonEmail;
    }

    public void setTxtBbbPhysicalAddress(RichInputText txtBbbPhysicalAddress) {
        this.txtBbbPhysicalAddress = txtBbbPhysicalAddress;
    }

    public RichInputText getTxtBbbPhysicalAddress() {
        return txtBbbPhysicalAddress;
    }

    public void setTxtBbbPostalAddress(RichInputText txtBbbPostalAddress) {
        this.txtBbbPostalAddress = txtBbbPostalAddress;
    }

    public RichInputText getTxtBbbPostalAddress() {
        return txtBbbPostalAddress;
    }

    public void setBtnSaveUpdateBancassuranceBranch(RichCommandButton btnSaveUpdateBancassuranceBranch) {
        this.btnSaveUpdateBancassuranceBranch =
                btnSaveUpdateBancassuranceBranch;
    }

    public RichCommandButton getBtnSaveUpdateBancassuranceBranch() {
        return btnSaveUpdateBancassuranceBranch;
    }

    public String actionSaveUpdateBancassuranceBranch() {
        if (btnSaveUpdateBancassuranceBranch.getText().equals("Edit")) {
            actionUpdateBancassuranceBranch();
        } else {
            String branchCode =
                GlobalCC.checkNullValues(txtBbbCode.getValue());
            String branchBankCode =
                GlobalCC.checkNullValues(txtBbbBrnCode.getValue());
            String branchName =
                GlobalCC.checkNullValues(txtBbbBranchName.getValue());
            String remarks =
                GlobalCC.checkNullValues(txtBbbRemarks.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtBbbShortDesc.getValue());
            String dateCreated =
                GlobalCC.checkNullValues(txtBbbDateCreated.getValue());
            String createdBy =
                GlobalCC.checkNullValues(txtBbbCreatedBy.getValue());
            String physicalAddress =
                GlobalCC.checkNullValues(txtBbbPhysicalAddress.getValue());
            String postalAddress =
                GlobalCC.checkNullValues(txtBbbPostalAddress.getValue());
            String kbaCode =
                GlobalCC.checkNullValues(txtBbbKBACode.getValue());
            String bbrEmail = GlobalCC.checkNullValues(txtBbbEmail.getValue());
            String bbrPersonName =
                GlobalCC.checkNullValues(txtBbbPersonName.getValue());
            String bbrPersonPhone =
                GlobalCC.checkNullValues(txtBbbPersonPhone.getValue());
            String bbrPersonEmail =
                GlobalCC.checkNullValues(txtBbbPersonEmail.getValue());
            String bbrCountryCode =
                GlobalCC.checkNullValues(txtBbbCountryCode.getValue());

            BigDecimal territoryCode =
                GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

            if (branchBankCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Branch has not been selected.");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (bbrEmail == null && renderer.isBBR_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Branch Email is Empty");
                return null;

            } else if (bbrPersonName == null &&
                       renderer.isBBR_PERSON_NAME_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Name is Empty");
                return null;
            } else if (bbrPersonEmail == null &&
                       renderer.isBBR_PERSON_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Email is Empty");
                return null;
            } else if (bbrCountryCode == null &&
                       renderer.isBBR_PERSON_PHONE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
                return null;
            } else if (bbrPersonPhone == null &&
                       renderer.isBBR_PERSON_PHONE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Phone is Empty");
                return null;
            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {

                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.bancassurance_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpDateCreated = new Date();
                    if (txtBbbDateCreated.getValue() != null &&
                        !(txtBbbDateCreated.getValue().equals(""))) {
                        String date1 = df.format(txtBbbDateCreated.getValue());
                        tmpDateCreated = df.parse(date1);
                    }

                    statement.setString(1, "A");
                    statement.setBigDecimal(2,
                                            branchCode == null ? null : new BigDecimal(branchCode));
                    statement.setBigDecimal(3,
                                            branchBankCode == null ? null : new BigDecimal(branchBankCode));
                    statement.setString(4, branchName);
                    statement.setString(5, remarks);
                    statement.setString(6, shortDesc);
                    statement.setDate(7,
                                      tmpDateCreated == null ? null : new java.sql.Date(tmpDateCreated.getTime()));
                    statement.setString(8,
                                        session.getAttribute("Username").toString());
                    statement.setString(9, physicalAddress);
                    statement.setString(10, postalAddress);
                    statement.setString(11, kbaCode);
                    statement.registerOutParameter(12, OracleTypes.VARCHAR);
                    statement.setBigDecimal(13, territoryCode);
                    statement.setString(14, bbrEmail);
                    statement.setString(15, bbrPersonName);
                    statement.setString(16, bbrPersonEmail);
                    statement.setString(17, bbrCountryCode);
                    statement.setString(18, bbrPersonPhone);

                    statement.execute();
                    String errMsg = statement.getString(12);
                    statement.close();
                    conn.commit();
                    conn.close();

                    GlobalCC.hidePopup("crm:BancassuranceBranch");

                    // fixing branchCode Overwrite by the ISession branchCode.
                    Object key = tblBankBranch.getSelectedRowData();
                    JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
                    session.setAttribute("branchCode",
                                         nodeBinding.getAttribute("branchCode"));
                    // end


                    ADFUtils.findIterator("fetchBancassuranceByBranchCodeIterator").executeQuery();
                    GlobalCC.refreshUI(bancassurancelov);

                    btnAddAssuranceBranch.setDisabled(false);
                    btnDeleteAssuranceBranch.setDisabled(true);
                    btnEditAssuranceBranch.setDisabled(true);

                    GlobalCC.refreshUI(btnAddAssuranceBranch);
                    GlobalCC.refreshUI(btnDeleteAssuranceBranch);
                    GlobalCC.refreshUI(btnEditAssuranceBranch);

                    if (errMsg == null) {
                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    } else {
                        GlobalCC.INFORMATIONREPORTING(errMsg);
                        GlobalCC.EXCEPTIONREPORTING(errMsg);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                    GlobalCC.INFORMATIONREPORTING(e.getMessage());
                }
            }
        }
        return null;
    }

    public void setTxtBbbKBACode(RichInputText txtBbbKBACode) {
        this.txtBbbKBACode = txtBbbKBACode;
    }

    public RichInputText getTxtBbbKBACode() {
        return txtBbbKBACode;
    }

    /**
     * Fixes the issue with clearing the banc assuarance fields.
     */
    public void clearBancassuranceBranch() {
        txtBbbCode.setValue(null);
        txtBbbBranchName.setValue(null);
        txtBbbRemarks.setValue(null);
        txtBbbShortDesc.setValue(null);
        txtBbbDateCreated.setValue(null);
        txtBbbCreatedBy.setValue(null);
        txtBbbPhysicalAddress.setValue(null);
        txtBbbPostalAddress.setValue(null);
        txtBbbKBACode.setValue(null);
        txtBbbEmail.setValue(null);
        txtBbbPersonName.setValue(null);
        txtBbbPersonPhone.setValue(null);
        txtBbbPersonEmail.setValue(null);
        txtBbbCountryCode.setValue(null);
        txtBbbTownName.setValue(null);
    }

    public String actionAddBancassuranceBranch() {
        // fixing branchCode Overwrite by the ISession branchCode.
        Object key = tblBankBranch.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;

        try {
            if (nodeBinding.getAttribute("branchCode") != null) {
                txtBnktTerritoryName.setValue(null);
                txtBnktShtDesc.setValue(null);
                txtBnktCode.setValue(null);
                btnSaveUpdateBancassuranceBranch.setText("Add");
                clearBancassuranceBranch();
                GlobalCC.showPopup("crm:BancassuranceBranch");
            } else {
                GlobalCC.INFORMATIONREPORTING("You need to select an existing Branch Record to proceed.");
                return null;
            }
        } catch (Exception e) {
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }
        return null;
    }

    public String actionEditBancassuranceBranch() {
        Object key2 = bancassurancelov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBbbCode.setValue(nodeBinding.getAttribute("branchCode"));
            txtBbbBrnCode.setValue(nodeBinding.getAttribute("branchBankCode"));
            txtBbbBranchName.setValue(nodeBinding.getAttribute("branchName"));
            txtBbbRemarks.setValue(nodeBinding.getAttribute("remarks"));
            txtBbbShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtBbbDateCreated.setValue(nodeBinding.getAttribute("dateCreated"));
            txtBbbCreatedBy.setValue(nodeBinding.getAttribute("createdBy"));
            txtBbbPhysicalAddress.setValue(nodeBinding.getAttribute("physicalAddress"));
            txtBbbPostalAddress.setValue(nodeBinding.getAttribute("postalAddress"));
            txtBbbKBACode.setValue(nodeBinding.getAttribute("KBACode"));
            txtBbbEmail.setValue(nodeBinding.getAttribute("branchEmail"));
            txtBbbPersonName.setValue(nodeBinding.getAttribute("branchPersonName"));
            txtBbbPersonPhone.setValue(nodeBinding.getAttribute("branchPersonPhone"));
            txtBbbPersonEmail.setValue(nodeBinding.getAttribute("branchPersonEmail"));
            txtBbbCountryCode.setValue(nodeBinding.getAttribute("countryCode"));

            btnSaveUpdateBancassuranceBranch.setText("Edit");
            GlobalCC.showPop("crm:BancassuranceBranch");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteBancassuranceBranch() {
        Object key2 = bancassurancelov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteBancassurance" +
                                 "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
    }

    public void setBtnAddAssuranceBranch(RichCommandButton btnAddAssuranceBranch) {
        this.btnAddAssuranceBranch = btnAddAssuranceBranch;
    }

    public RichCommandButton getBtnAddAssuranceBranch() {
        return btnAddAssuranceBranch;
    }

    public void setBtnEditAssuranceBranch(RichCommandButton btnEditAssuranceBranch) {
        this.btnEditAssuranceBranch = btnEditAssuranceBranch;
    }

    public RichCommandButton getBtnEditAssuranceBranch() {
        return btnEditAssuranceBranch;
    }

    public void setBtnDeleteAssuranceBranch(RichCommandButton btnDeleteAssuranceBranch) {
        this.btnDeleteAssuranceBranch = btnDeleteAssuranceBranch;
    }

    public RichCommandButton getBtnDeleteAssuranceBranch() {
        return btnDeleteAssuranceBranch;
    }

    public void confirmDeleteBancassurance(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String actionDeleteBank = actionDeleteBancassurance();
        }

    }

    public String actionDeleteBancassurance() {
        Object key2 = bancassurancelov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String branchCode =
                nodeBinding.getAttribute("branchCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.bancassurance_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2, new BigDecimal(branchCode));
                statement.setString(3, null);
                statement.setString(4, null);
                statement.setString(5, null);
                statement.setString(6, null);
                statement.setString(7, null);
                statement.setObject(8, null);
                statement.setObject(9, null);
                statement.setObject(11, null);
                statement.setObject(13, null);
                statement.setObject(14, null);
                statement.setObject(15, null);
                statement.setObject(16, null);
                statement.setObject(17, null);
                statement.setObject(18, null);
                statement.setObject(10, null);

                statement.registerOutParameter(12, OracleTypes.VARCHAR);

                statement.execute();
                String errMsg = statement.getString(12);
                statement.close();
                conn.commit();
                conn.close();

                // fixing branchCode Overwrite by the ISession branchCode.
                Object key = tblBankBranch.getSelectedRowData();
                JUCtrlValueBinding binding = (JUCtrlValueBinding)key;
                session.setAttribute("branchCode",
                                     binding.getAttribute("branchCode"));
                // end

                ADFUtils.findIterator("fetchBancassuranceByBranchCodeIterator").executeQuery();
                GlobalCC.refreshUI(bancassurancelov);
                btnDeleteAssuranceBranch.setDisabled(true);
                btnEditAssuranceBranch.setDisabled(true);
                GlobalCC.refreshUI(btnDeleteAssuranceBranch);
                GlobalCC.refreshUI(btnEditAssuranceBranch);

                if (errMsg == null) {
                    String message = " Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } else {
                    GlobalCC.INFORMATIONREPORTING(errMsg);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionUpdateBancassuranceBranch() {
        String branchCode = GlobalCC.checkNullValues(txtBbbCode.getValue());
        String branchBankCode =
            GlobalCC.checkNullValues(txtBbbBrnCode.getValue());
        String branchName =
            GlobalCC.checkNullValues(txtBbbBranchName.getValue());
        String remarks = GlobalCC.checkNullValues(txtBbbRemarks.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtBbbShortDesc.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtBbbPhysicalAddress.getValue());
        String postalAddress =
            GlobalCC.checkNullValues(txtBbbPostalAddress.getValue());
        String kbaCode = GlobalCC.checkNullValues(txtBbbKBACode.getValue());
        String bbrEmail = GlobalCC.checkNullValues(txtBbbEmail.getValue());
        String bbrPersonName =
            GlobalCC.checkNullValues(txtBbbPersonName.getValue());
        String bbrPersonPhone =
            GlobalCC.checkNullValues(txtBbbPersonPhone.getValue());
        String bbrPersonEmail =
            GlobalCC.checkNullValues(txtBbbPersonEmail.getValue());
        String bbrCountryCode =
            GlobalCC.checkNullValues(txtBbbCountryCode.getValue());

        BigDecimal territoryCode =
            GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

        if (territoryCode == null && renderer.isBANK_TERRITORY_APPLICABLE()) {
            GlobalCC.INFORMATIONREPORTING("Select Branch Territory!");
            return null;
        }

        if (branchCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch Code is Empty.");
            return null;

        } else if (branchBankCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch has not been selected.");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (bbrEmail == null && renderer.isBBR_EMAIL_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch Email is Empty");
            return null;

        } else if (bbrPersonName == null &&
                   renderer.isBBR_PERSON_NAME_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Name is Empty");
            return null;
        } else if (bbrPersonEmail == null &&
                   renderer.isBBR_PERSON_EMAIL_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Email is Empty");
            return null;
        } else if (bbrCountryCode == null &&
                   renderer.isBBR_PERSON_PHONE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
            return null;
        } else if (bbrPersonPhone == null &&
                   renderer.isBBR_PERSON_PHONE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Person Phone is Empty");
            return null;
        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.bancassurance_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpDateCreated = new Date();
                if (txtBbbDateCreated.getValue() != null &&
                    !(txtBbbDateCreated.getValue().equals(""))) {
                    String date1 = df.format(txtBbbDateCreated.getValue());
                    tmpDateCreated = df.parse(date1);
                }

                statement.setString(1, "E");
                statement.setBigDecimal(2,
                                        branchCode == null ? null : new BigDecimal(branchCode));
                statement.setBigDecimal(3,
                                        branchBankCode == null ? null : new BigDecimal(branchBankCode));
                statement.setString(4, branchName);
                statement.setString(5, remarks);
                statement.setString(6, shortDesc);
                statement.setDate(7,
                                  tmpDateCreated == null ? null : new java.sql.Date(tmpDateCreated.getTime()));
                statement.setString(8,
                                    session.getAttribute("Username").toString());
                statement.setString(9, physicalAddress);
                statement.setString(10, postalAddress);
                statement.setString(11, kbaCode);

                statement.registerOutParameter(12, OracleTypes.VARCHAR);

                statement.setBigDecimal(13, territoryCode);
                statement.setString(14, bbrEmail);
                statement.setString(15, bbrPersonName);
                statement.setString(16, bbrPersonEmail);
                statement.setString(17, bbrCountryCode);
                statement.setString(18, bbrPersonPhone);
                statement.execute();
                String errMsg = statement.getString(12);
                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:BancassuranceBranch" +
                                     "').hide(hints);");

                // fixing branchCode Overwrite by the ISession branchCode.
                Object key = tblBankBranch.getSelectedRowData();
                JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
                session.setAttribute("branchCode",
                                     nodeBinding.getAttribute("branchCode"));
                // end

                ADFUtils.findIterator("fetchBancassuranceByBranchCodeIterator").executeQuery();
                GlobalCC.refreshUI(bancassurancelov);
                btnAddAssuranceBranch.setDisabled(false);
                btnDeleteAssuranceBranch.setDisabled(false);
                btnEditAssuranceBranch.setDisabled(false);
                GlobalCC.refreshUI(btnDeleteAssuranceBranch);
                GlobalCC.refreshUI(btnEditAssuranceBranch);
                GlobalCC.refreshUI(btnAddAssuranceBranch);

                if (errMsg == null) {
                    String message = "New Record Updated Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } else {
                    GlobalCC.INFORMATIONREPORTING(errMsg);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }


    public void setTblbackAssuranceTowns(RichTable tblbackAssuranceTowns) {
        this.tblbackAssuranceTowns = tblbackAssuranceTowns;
    }

    public RichTable getTblbackAssuranceTowns() {
        return tblbackAssuranceTowns;
    }

    public String acceptBankAssuranceTownSelect() {

        Object key = tblbackAssuranceTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBbbTownCode.setValue(nodeBinding.getAttribute("code"));
            txtBbbTownName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtBbbTownCode);
            GlobalCC.refreshUI(txtBbbTownName);
        }
        GlobalCC.dismissPopUp("crm", "bankAssuranceTowns");
        return null;
    }

    public String hideTowns2() {
        // Add event code here...
        GlobalCC.dismissPopUp("crm", "bankAssuranceTowns");
        return null;
    }

    public void setTbbankAssurancelCountryCode(RichTable tbbankAssurancelCountryCode) {
        this.tbbankAssurancelCountryCode = tbbankAssurancelCountryCode;
    }

    public RichTable getTbbankAssurancelCountryCode() {
        return tbbankAssurancelCountryCode;
    }

    public String actionAcceptBankAssuranceCountryCode() {
        Object key = tbbankAssurancelCountryCode.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtBbbCountryCode.setValue(r.getAttribute("countryCode"));
        GlobalCC.refreshUI(txtBbbCountryCode);
        GlobalCC.hidePopup("crm:bancassurancecountries");
        return null;
    }

    public String hideCountryPopUp2() {
        GlobalCC.dismissPopUp("crm", "bancassurancecountries");
        return null;
    }

    public void actionTblBancAssuranceSelected(SelectionEvent selectionEvent) {
        btnAddAssuranceBranch.setDisabled(false);
        btnEditAssuranceBranch.setDisabled(false);
        btnDeleteAssuranceBranch.setDisabled(false);
        GlobalCC.refreshUI(btnAddAssuranceBranch);
        GlobalCC.refreshUI(btnEditAssuranceBranch);
        GlobalCC.refreshUI(btnDeleteAssuranceBranch);
    }
}
