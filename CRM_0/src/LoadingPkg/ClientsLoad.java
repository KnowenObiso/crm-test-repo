package LoadingPkg;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.client.AgentLoadingTab;
import TurnQuest.view.client.ClientLoadingTab;

import com.Ostermiller.util.CSVParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ClientsLoad {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private String filetype;
    private RichTable clientsDataTbl;
    private RichInputText txtClientType;
    private RichSelectOneChoice txtClientCategory;
    private RichTable txtClientTypeTbl;
    private RichInputText txtClientShtDesc;
    private RichInputText txtClientNames;
    private RichInputText txtClientOthernames;
    private RichInputText txtRegNumber;
    private RichInputText txtClientsPhyAddress;
    private RichInputNumberSpinbox txtTownCode;
    private RichInputNumberSpinbox countryCode;
    private RichInputText txtAccountNumber;
    private RichInputDate txtClientCoverFrom;
    private RichInputDate txtClientCoverTo;
    private RichSelectOneChoice txtClientTypes;
    private RichInputText txtCreatedBy;
    private RichInputDate txtDateCreated;
    private RichInputNumberSpinbox txtClientCodes;
    private RichInputDate txtClientDateOfBirth;
    private RichSelectOneChoice txtDirectClient;
    private RichSelectOneChoice txtAgentStatus;
    private RichSelectBooleanCheckbox rowChecked;
    private RichTable agentsLoadingTbl;
    private RichInputText txtAccountCode;
    private RichInputText txtAgentShtDesc;
    private RichInputText txtAgentName;
    private RichInputText txtPhysicalAddress;
    private RichInputDate txtPostalAddress;
    private RichInputNumberSpinbox txtTownCodeAgents;
    private RichInputNumberSpinbox txtCountryCode;
    private RichInputText txtEmailAddress;
    private RichInputText txtContactPerson;
    private RichInputText txtAccountNumberAgents;
    private RichSelectOneChoice txtCreditAllowed;
    private RichInputText txtCreatedByAgent;
    private RichInputText txtCreditRating;
    private RichInputDate txtCreateddateAgent;
    private RichInputText txtpostalAddressAgnt;
    private RichInputText txtBranchname;
    private RichTable selectBankBranch;
    private RichTable branchesTbl;
    private RichSelectOneChoice optionSelected;
    private RichSelectOneChoice txtSystemType;
    private RichCommandButton selectAllClients;
    private RichSelectBooleanCheckbox checked;
    private RichSelectOneChoice txtSystemName;
    private RichSelectBooleanCheckbox selected;
    private RichSelectBooleanCheckbox select;
    private RichInputText txtTownName;
    private RichInputText txtPostalAddressClient;
    private RichInputText txtTelNumber;
    private RichInputText txtEmailAddresscln;
    private RichInputText txtFaxNumber;
    private RichInputText txtTelNumber2;
    private RichInputText txtAccountType;
    private RichInputText txtAccountName;
    private RichInputText txtTownNamed;
    private RichInputText txtIdNumber;
    private RichInputText txtPhoneNumber;
    private RichInputText txtFaxNumbers;
    private RichInputText txtEmailAddresss;
    private RichInputDate txtPostedDate;
    private RichSelectBooleanCheckbox rowSelect;

    public ClientsLoad() {
    }

    public void setUpFile(RichInputFile upFile) {
        this.upFile = upFile;
    }

    public RichInputFile getUpFile() {
        return upFile;
    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            val = new java.sql.Date(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public void fileChangeListenerClntAgn(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            try {
                processLoadingClnt(uploadedFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fileChangeListenerAgn(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            System.out.println("File name " + this.filename);

            try {
                processLoadingAgnt(uploadedFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processLoadingClnt(InputStream csvFile) {
        String[][] csvvalues = null;
        int count = 0;
        java.util.Date dateBirth;
        String dateOfBirth = null;
        List RiskInfo = new LinkedList();
        ARRAY array = null;
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            csvvalues = CSVParser.parse(new InputStreamReader(csvFile));

            for (int i = 1; i < csvvalues.length; i++) {

                if (i == 0) {
                } else {

                    ClientLoadingTab loadClient = new ClientLoadingTab();
                    java.sql.Date dob = null;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    if (csvvalues[i][11] != null) {
                        try {
                            dateOfBirth = csvvalues[i][11].toString();
                            dateBirth = sdf1.parse(dateOfBirth);
                            dob = new java.sql.Date((dateBirth).getTime());
                        } catch (Exception e) {
                            dob = null;
                        }
                    } else {
                        dob = null;
                    }

                    loadClient.setCln_clnt_sht_desc(csvvalues[i][0]);
                    loadClient.setClientTownname(csvvalues[i][1]);
                    loadClient.setCln_clnt_name(csvvalues[i][2]);
                    loadClient.setCln_clnt_other_names(csvvalues[i][3]);
                    loadClient.setCln_clnt_physical_addrs(csvvalues[i][4]);
                    loadClient.setCln_clnt_postal_addrs(csvvalues[i][5]);
                    loadClient.setCln_clnt_tel(csvvalues[i][6]);
                    loadClient.setCln_clnt_tel2(csvvalues[i][7]);
                    loadClient.setFaxNumber(csvvalues[i][8]);
                    loadClient.setEmailAddress(csvvalues[i][9]);
                    loadClient.setCln_clnt_id_reg_no(csvvalues[i][10]);
                    loadClient.setCln_clnt_dob(dob);
                    loadClient.setClientType(csvvalues[i][12]);
                    RiskInfo.add(loadClient);

                }
            }


            String query =
                "begin TQC_LOADING_UTILITIES.tqc_clntAgnt_loading_prc(?,?,?); end;";

            cst = conn.prepareCall(query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_CLNT_LOADING_TBL", conn);
            array = new ARRAY(descriptor, conn, RiskInfo.toArray());
            cst.setString(1, "A");
            cst.setArray(2, array);
            cst.setString(3, "N");
            cst.execute();
            cst.close();
            conn.close();

            ADFUtils.findIterator("findLoadedClientsIterator").executeQuery();
            GlobalCC.refreshUI(clientsDataTbl);

            GlobalCC.INFORMATIONREPORTING("Data Successfully inserted...");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
    }


    public void processLoadingAgnt(InputStream csvFile) {
        String[][] csvvalues = null;
        String DateCreatedStr = null;
        String CheckDateStr = null;
        //String dateCreatedStr=null;
        java.util.Date CheckDateStrUtil;
        java.util.Date dateCreatedUtil = null;
        BigDecimal accountCode;
        List RiskInfo = new LinkedList();
        ARRAY array = null;
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            csvvalues = CSVParser.parse(new InputStreamReader(csvFile));

            for (int i = 1; i < csvvalues.length; i++) {

                if (i == 1) {
                } else {

                    AgentLoadingTab loadAgent = new AgentLoadingTab();
                    java.sql.Date dateCreated = null;
                    java.sql.Date checkDate = null;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                    if (csvvalues[i][11] != null) {
                        System.out.println("Date passed" + csvvalues[i][11]);
                        DateCreatedStr = csvvalues[i][11].toString();
                        if (DateCreatedStr.equals("")) {
                            dateCreated = null;
                        } else {
                            System.out.println("Date passed too" +
                                               DateCreatedStr);
                            dateCreatedUtil = sdf1.parse(DateCreatedStr);
                            dateCreated =
                                    new java.sql.Date((dateCreatedUtil).getTime());
                        }
                    } else {
                        dateCreated = null;
                    }
                    if (csvvalues[i][12] != null) {
                        CheckDateStr = csvvalues[i][12].toString();
                        if (CheckDateStr.equals("")) {
                            checkDate = null;
                        } else {

                            CheckDateStrUtil = sdf1.parse(CheckDateStr);
                            checkDate =
                                    new java.sql.Date((CheckDateStrUtil).getTime());
                        }
                    } else {
                        checkDate = null;
                    }
                    loadAgent.setAgentAccountCode(csvvalues[i][0]);
                    loadAgent.setAgentAccountType(csvvalues[i][1]);
                    loadAgent.setAgentAccountName(csvvalues[i][2]);
                    loadAgent.setAgentPhysicalAddress(csvvalues[i][3]);
                    loadAgent.setAgentPostalAddress(csvvalues[i][4]);
                    loadAgent.setAgentTownName(csvvalues[i][5]);
                    loadAgent.setAgentRegCode(csvvalues[i][6]);
                    loadAgent.setAgentContactPerson(csvvalues[i][7]);
                    loadAgent.setAgentTelNumber(csvvalues[i][8]);
                    loadAgent.setAgentFaxNumber(csvvalues[i][9]);
                    loadAgent.setAgentEmailAddress(csvvalues[i][10]);
                    loadAgent.setAgentDateCreated(dateCreated);
                    loadAgent.setAgentCeckDate(checkDate);
                    loadAgent.setBranchName(csvvalues[i][13]);
                    RiskInfo.add(loadAgent);

                }
            }
            String query =
                "begin TQC_LOADING_UTILITIES.tqc_Agnt_loading_prc(?,?,?); end;";

            cst = conn.prepareCall(query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_AGNT_LOADING_TBL", conn);
            array = new ARRAY(descriptor, conn, RiskInfo.toArray());
            cst.setString(1, "A");
            cst.setArray(2, array);
            cst.setString(3, "N");
            cst.execute();
            cst.close();
            conn.close();

            ADFUtils.findIterator("findLoadedAgentsIterator").executeQuery();
            GlobalCC.refreshUI(agentsLoadingTbl);

            GlobalCC.INFORMATIONREPORTING("Data Successfully inserted...");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
    }

    public void setClientsDataTbl(RichTable clientsDataTbl) {
        this.clientsDataTbl = clientsDataTbl;
    }

    public RichTable getClientsDataTbl() {
        return clientsDataTbl;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setTxtClientType(RichInputText txtClientType) {
        this.txtClientType = txtClientType;
    }

    public RichInputText getTxtClientType() {
        return txtClientType;
    }

    public void setTxtClientCategory(RichSelectOneChoice txtClientCategory) {
        this.txtClientCategory = txtClientCategory;
    }

    public RichSelectOneChoice getTxtClientCategory() {
        return txtClientCategory;
    }

    public void setTxtClientTypeTbl(RichTable txtClientTypeTbl) {
        this.txtClientTypeTbl = txtClientTypeTbl;
    }

    public RichTable getTxtClientTypeTbl() {
        return txtClientTypeTbl;
    }

    public void setTxtClientShtDesc(RichInputText txtClientShtDesc) {
        this.txtClientShtDesc = txtClientShtDesc;
    }

    public RichInputText getTxtClientShtDesc() {
        return txtClientShtDesc;
    }

    public void setTxtClientNames(RichInputText txtClientNames) {
        this.txtClientNames = txtClientNames;
    }

    public RichInputText getTxtClientNames() {
        return txtClientNames;
    }

    public void setTxtClientOthernames(RichInputText txtClientOthernames) {
        this.txtClientOthernames = txtClientOthernames;
    }

    public RichInputText getTxtClientOthernames() {
        return txtClientOthernames;
    }

    public void setTxtRegNumber(RichInputText txtRegNumber) {
        this.txtRegNumber = txtRegNumber;
    }

    public RichInputText getTxtRegNumber() {
        return txtRegNumber;
    }


    public void setTxtClientsPhyAddress(RichInputText txtClientsPhyAddress) {
        this.txtClientsPhyAddress = txtClientsPhyAddress;
    }

    public RichInputText getTxtClientsPhyAddress() {
        return txtClientsPhyAddress;
    }

    public void setTxtTownCode(RichInputNumberSpinbox txtTownCode) {
        this.txtTownCode = txtTownCode;
    }

    public RichInputNumberSpinbox getTxtTownCode() {
        return txtTownCode;
    }

    public void setCountryCode(RichInputNumberSpinbox countryCode) {
        this.countryCode = countryCode;
    }

    public RichInputNumberSpinbox getCountryCode() {
        return countryCode;
    }

    public void setTxtAccountNumber(RichInputText txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }

    public RichInputText getTxtAccountNumber() {
        return txtAccountNumber;
    }

    public void setTxtClientCoverFrom(RichInputDate txtClientCoverFrom) {
        this.txtClientCoverFrom = txtClientCoverFrom;
    }

    public RichInputDate getTxtClientCoverFrom() {
        return txtClientCoverFrom;
    }

    public void setTxtClientCoverTo(RichInputDate txtClientCoverTo) {
        this.txtClientCoverTo = txtClientCoverTo;
    }

    public RichInputDate getTxtClientCoverTo() {
        return txtClientCoverTo;
    }

    public void setTxtClientTypes(RichSelectOneChoice txtClientTypes) {
        this.txtClientTypes = txtClientTypes;
    }

    public RichSelectOneChoice getTxtClientTypes() {
        return txtClientTypes;
    }

    public void setTxtCreatedBy(RichInputText txtCreatedBy) {
        this.txtCreatedBy = txtCreatedBy;
    }

    public RichInputText getTxtCreatedBy() {
        return txtCreatedBy;
    }


    public void setTxtDateCreated(RichInputDate txtDateCreated) {
        this.txtDateCreated = txtDateCreated;
    }

    public RichInputDate getTxtDateCreated() {
        return txtDateCreated;
    }

    public void setTxtClientCodes(RichInputNumberSpinbox txtClientCodes) {
        this.txtClientCodes = txtClientCodes;
    }

    public RichInputNumberSpinbox getTxtClientCodes() {
        return txtClientCodes;
    }

    public String editClient() {
        Object key2 = clientsDataTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("action", "E");
        session.setAttribute("CLN_CODE", r.getAttribute("CLN_CODE"));
        txtClientShtDesc.setValue(r.getAttribute("CLN_CLNT_SHT_DESC"));
        txtClientNames.setValue(r.getAttribute("CLN_CLNT_NAME"));
        txtClientOthernames.setValue(r.getAttribute("CLN_CLNT_OTHER_NAMES"));
        txtClientDateOfBirth.setValue(r.getAttribute("CLN_CLNT_DOB"));
        txtTownName.setValue(r.getAttribute("CLN_CLNT_TWN_CODE"));
        txtClientsPhyAddress.setValue(r.getAttribute("CLN_CLNT_PHYSICAL_ADDRS"));
        txtPostalAddressClient.setValue(r.getAttribute("CLN_CLNT_POSTAL_ADDRS"));
        txtEmailAddresscln.setValue(r.getAttribute("CLN_CLNT_CNTCT_EMAIL_1"));
        txtTelNumber.setValue(r.getAttribute("CLN_CLNT_TEL"));
        txtFaxNumber.setValue(r.getAttribute("CLN_CLNT_FAX"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:editClientPopup" + "').show(hints);");
        return null;
    }

    public String editAgent() {
        Object key2 = agentsLoadingTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("action", "E");
        session.setAttribute("agntCode", r.getAttribute("agnlCode"));
        txtAccountCode.setValue(r.getAttribute("agnlAccountCode"));
        txtAccountType.setValue(r.getAttribute("agnlaccountType"));
        txtAccountName.setValue(r.getAttribute("agnlaccountName"));
        txtPhysicalAddress.setValue(r.getAttribute("agnlphysicalAddress"));
        txtpostalAddressAgnt.setValue(r.getAttribute("agnlpostalAddress"));
        txtTownNamed.setValue(r.getAttribute("agnlTownName"));
        txtIdNumber.setValue(r.getAttribute("agnlregCode"));
        txtContactPerson.setValue(r.getAttribute("agnlcontactperson"));
        txtPhoneNumber.setValue(r.getAttribute("agnltelephoneNumber"));
        txtFaxNumbers.setValue(r.getAttribute("agnlfaxNumber"));
        txtEmailAddress.setValue(r.getAttribute("agnlemailAddress"));
        txtCreateddateAgent.setValue(r.getAttribute("agnldateCreated"));
        txtPostedDate.setValue(r.getAttribute("agnlcheckDate"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:editAgentPopup" + "').show(hints);");
        return null;
    }

    public String saveClientDetails() throws SQLException, ParseException {
        String ClientShtDesc;
        String ClientNames;
        String ClientOthernames;
        String ClientPostalAddress;
        String ClientPhysicalAddressAddress;
        String ClientTelephoneNumber1;
        String ClientTelephoneNumber2;
        String PhysicalAddress;
        String emailAddresscln;
        String faxNumber;
        String townName;
        String RegNumber;
        BigDecimal CLN_CODE = null;

        if (session.getAttribute("CLN_CODE") == null ||
            session.getAttribute("CLN_CODE").equals("")) {
            CLN_CODE = null;
        } else {
            CLN_CODE =
                    new BigDecimal(session.getAttribute("CLN_CODE").toString());
        }
        if (txtClientShtDesc.getValue() == null) {
            ClientShtDesc = null;
        } else {
            ClientShtDesc = txtClientShtDesc.getValue().toString();
        }
        if (txtClientNames.getValue() == null) {
            ClientNames = null;
        } else {
            ClientNames = txtClientNames.getValue().toString();
        }
        if (txtTownName.getValue() == null) {
            townName = null;
        } else {
            townName = txtTownName.getValue().toString();
        }

        if (txtClientOthernames.getValue() == null) {
            ClientOthernames = null;
        } else {
            ClientOthernames = txtClientOthernames.getValue().toString();
        }
        if (txtRegNumber.getValue() == null) {
            RegNumber = null;
        } else {
            RegNumber = txtRegNumber.getValue().toString();
        }
        if (txtClientsPhyAddress.getValue() == null) {
            PhysicalAddress = null;
        } else {
            PhysicalAddress = txtClientsPhyAddress.getValue().toString();
        }
        if (txtPostalAddressClient.getValue() == null) {
            ClientPostalAddress = null;
        } else {
            ClientPostalAddress = txtPostalAddressClient.getValue().toString();
        }
        if (txtTelNumber.getValue() == null) {
            ClientTelephoneNumber1 = null;
        } else {
            ClientTelephoneNumber1 = txtTelNumber.getValue().toString();
        }
        if (txtTelNumber2.getValue() == null) {
            ClientTelephoneNumber2 = null;
        } else {
            ClientTelephoneNumber2 = txtTelNumber2.getValue().toString();
        }
        if (txtFaxNumber.getValue() == null) {
            faxNumber = null;
        } else {
            faxNumber = txtFaxNumber.getValue().toString();
        }
        if (txtEmailAddresscln.getValue() == null) {
            emailAddresscln = null;
        } else {
            emailAddresscln = txtEmailAddresscln.getValue().toString();
        }
        String ClientDetails =
            "begin TQC_LOADING_UTILITIES.saveClientLoadingDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cstss = null;
        OracleConnection conn = null;

        try {
            conn = (OracleConnection)connector.getDatabaseConnection();
            cstss = (OracleCallableStatement)conn.prepareCall(ClientDetails);

            cstss.setString(1, (String)session.getAttribute("action"));
            cstss.setBigDecimal(2, CLN_CODE);
            System.out.println("This is client code" + CLN_CODE);
            cstss.setString(3, ClientShtDesc);
            cstss.setString(4, townName);
            cstss.setString(5, ClientNames);
            cstss.setString(6, ClientOthernames);
            cstss.setString(7, ClientPostalAddress);
            cstss.setString(8, PhysicalAddress);
            cstss.setString(9, ClientTelephoneNumber1);
            cstss.setString(10, ClientTelephoneNumber1);
            cstss.setString(11, faxNumber);
            cstss.setString(12, emailAddresscln);
            cstss.setString(13, RegNumber);
            cstss.setDate(14, GlobalCC.extractDate(txtClientDateOfBirth));
            cstss.execute();
            ADFUtils.findIterator("findLoadedClientsIterator").executeQuery();
            GlobalCC.refreshUI(clientsDataTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:editClientPopup" + "').hide(hints);");
        return null;
    }

    public String saveAgentsDetails() throws SQLException, ParseException {
        BigDecimal agntCode = null;
        String accountCode;
        String accountType;
        String accountName;
        String physicalAddress;
        String postalAddressAgnt;
        String emailAddress;
        String townNamed;
        String idNumber;
        String contactPerson;
        String phoneNumber;
        String faxNumbers;
        String branchName;
        if (txtAccountCode.getValue() == null) {
            accountCode = null;
        } else {
            accountCode = txtAccountCode.getValue().toString();
        }
        if (session.getAttribute("agnlCodes") == null ||
            session.getAttribute("agnlCodes").equals("")) {
            agntCode = null;
        } else {
            agntCode =
                    new BigDecimal(session.getAttribute("agnlCodes").toString());
        }
        if (txtAccountType.getValue() == null) {
            accountType = null;
        } else {
            accountType = txtAccountType.getValue().toString();
        }
        if (txtAccountName.getValue() == null) {
            accountName = null;
        } else {
            accountName = txtAccountName.getValue().toString();
        }
        if (txtpostalAddressAgnt.getValue() == null) {
            postalAddressAgnt = null;
        } else {
            postalAddressAgnt = txtpostalAddressAgnt.getValue().toString();
        }
        if (txtPhysicalAddress.getValue() == null) {
            physicalAddress = null;
        } else {
            physicalAddress = txtPhysicalAddress.getValue().toString();
        }
        if (txtEmailAddress.getValue() == null) {
            emailAddress = null;
        } else {
            emailAddress = txtEmailAddress.getValue().toString();
        }

        if (txtTownNamed.getValue() == null) {
            townNamed = null;
        } else {
            townNamed = txtTownNamed.getValue().toString();
        }
        if (txtIdNumber.getValue() == null) {
            idNumber = null;
        } else {
            idNumber = txtIdNumber.getValue().toString();
        }
        if (txtPhoneNumber.getValue() == null) {
            phoneNumber = null;
        } else {
            phoneNumber = txtPhoneNumber.getValue().toString();
        }
        if (txtContactPerson.getValue() == null) {
            contactPerson = null;
        } else {
            contactPerson = txtContactPerson.getValue().toString();
        }
        if (txtFaxNumbers.getValue() == null) {
            faxNumbers = null;
        } else {
            faxNumbers = txtFaxNumbers.getValue().toString();
        }
        if (txtBranchname.getValue() == null) {
            branchName = null;
        } else {
            branchName = txtBranchname.getValue().toString();
        }


        String ClientDetails =
            "begin TQC_LOADING_UTILITIES.saveAgentDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cstss = null;
        OracleConnection conn = null;

        try {
            conn = (OracleConnection)connector.getDatabaseConnection();
            cstss = (OracleCallableStatement)conn.prepareCall(ClientDetails);

            cstss.setString(1, (String)session.getAttribute("action"));
            cstss.setBigDecimal(2, agntCode);
            System.out.println("This is client code" + agntCode);
            cstss.setString(3, accountCode);
            cstss.setString(4, accountType);
            cstss.setString(5, accountName);
            cstss.setString(6, physicalAddress);
            cstss.setString(7, postalAddressAgnt);
            cstss.setString(8, townNamed);
            cstss.setString(9, idNumber);
            cstss.setString(10, contactPerson);
            cstss.setString(11, phoneNumber);
            cstss.setString(12, faxNumbers);
            cstss.setString(13, emailAddress);
            cstss.setDate(14, extractDate(txtCreateddateAgent));
            cstss.setDate(15, extractDate(txtPostedDate));
            cstss.setString(16, branchName);
            cstss.execute();
            ADFUtils.findIterator("findLoadedAgentsIterator").executeQuery();
            GlobalCC.refreshUI(agentsLoadingTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:editAgentPopup" + "').hide(hints);");
        return null;
    }

    public void setTxtClientDateOfBirth(RichInputDate txtClientDateOfBirth) {
        this.txtClientDateOfBirth = txtClientDateOfBirth;
    }

    public RichInputDate getTxtClientDateOfBirth() {
        return txtClientDateOfBirth;
    }

    public void setTxtDirectClient(RichSelectOneChoice txtDirectClient) {
        this.txtDirectClient = txtDirectClient;
    }

    public RichSelectOneChoice getTxtDirectClient() {
        return txtDirectClient;
    }

    public void setTxtAgentStatus(RichSelectOneChoice txtAgentStatus) {
        this.txtAgentStatus = txtAgentStatus;
    }

    public RichSelectOneChoice getTxtAgentStatus() {
        return txtAgentStatus;
    }

    public String deleteClient() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement stmt = null;
        int rowCount = clientsDataTbl.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Client To Delete!");
            return null;
        }
        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if ((Boolean)r.getAttribute("checked") == true) {
                    String Query =
                        "begin TQC_LOADING_UTILITIES.saveClientLoadingDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                    stmt = conn.prepareCall(Query);
                    stmt.setString(1, "D");
                    stmt.setBigDecimal(2,
                                       (BigDecimal)r.getAttribute("CLN_CODE"));
                    stmt.setString(3, null);
                    stmt.setString(4, null);
                    stmt.setString(5, null);
                    stmt.setString(6, null);
                    stmt.setDate(7, null);
                    stmt.setString(8, null);
                    stmt.setString(9, null);
                    stmt.setString(10, null);
                    stmt.setString(11, null);
                    stmt.setString(12, null);
                    stmt.setDate(13, null);
                    stmt.setDate(14, null);
                    stmt.execute();

                }
            }
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findLoadedClientsIterator").executeQuery();
            GlobalCC.refreshUI(clientsDataTbl);
            GlobalCC.INFORMATIONREPORTING("Deletion Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String deleteAgent() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement stmt = null;
        int rowCount = agentsLoadingTbl.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Transaction To Clear!");
            return null;
        }
        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;

                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if ((Boolean)r.getAttribute("checked") == true) {
                    System.out.println("This is it" +
                                       r.getAttribute("agnlCode"));
                    String Query =
                        "begin TQC_LOADING_UTILITIES.saveAgentDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                    stmt = conn.prepareCall(Query);
                    stmt.setString(1, "D");
                    stmt.setBigDecimal(2,
                                       (BigDecimal)r.getAttribute("agnlCode"));
                    stmt.setString(3, null);
                    stmt.setString(4, null);
                    stmt.setString(5, null);
                    stmt.setString(6, null);
                    stmt.setDate(7, null);
                    stmt.setString(8, null);
                    stmt.setString(9, null);
                    stmt.setString(10, null);
                    stmt.setString(11, null);
                    stmt.setString(12, null);
                    stmt.setDate(13, null);
                    stmt.setDate(14, null);
                    stmt.setString(15, null);
                    stmt.setString(16, null);
                    stmt.execute();
                }
            }
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findLoadedAgentsIterator").executeQuery();
            GlobalCC.refreshUI(agentsLoadingTbl);
            GlobalCC.INFORMATIONREPORTING("Deletion Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setRowChecked(RichSelectBooleanCheckbox rowChecked) {
        this.rowChecked = rowChecked;
    }

    public RichSelectBooleanCheckbox getRowChecked() {
        return rowChecked;
    }

    public String loadClientAction() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement cst = null;

        int rowCount = clientsDataTbl.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Client To Load!");
            return null;
        }
        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if ((Boolean)r.getAttribute("checked") == true) {
                    String Query =
                        "begin TQC_LOADING_UTILITIES.load_clients_prc(?,?,?,?); end;";

                    datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();
                    cst = conn.prepareCall(Query);
                    cst.setBigDecimal(1,
                                      new BigDecimal(r.getAttribute("CLN_CODE").toString()));
                    cst.setString(2, (String)session.getAttribute("Username"));
                    if (r.getAttribute("selected").equals(true)) {
                        cst.setString(3, "GIS");
                    } else {
                        cst.setString(3, null);
                    }
                    if (r.getAttribute("select").equals(true)) {
                        cst.setString(4, "LMS");
                    } else {
                        cst.setString(4, null);
                    }
                    cst.execute();

                }
            }
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findLoadedClientsIterator").executeQuery();
            GlobalCC.refreshUI(clientsDataTbl);
            GlobalCC.INFORMATIONREPORTING("Loading Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String loadAgentAction() {
        JUCtrlValueBinding r = null;
        Connection conn = null;
        int rowCount2 = agentsLoadingTbl.getRowCount();
        int count = 0;
        System.out.println("this is row count" + rowCount2);
        for (int i = 0; i < rowCount2; i++) {
            Boolean Accept = false;
            r = (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            Accept = (Boolean)r.getAttribute("checked");
            if (Accept)
                count++;
        }
        if (count == 0) {
            GlobalCC.INFORMATIONREPORTING("Select Data to load");
            return null;
        }
        try {
            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
                if (r == null)
                    continue;
                Accept = (Boolean)r.getAttribute("checked");
                if ((Boolean)r.getAttribute("checked") == true) {
                    String Query =
                        "begin TQC_LOADING_UTILITIES.load_agents_prc(?,?,?,?); end;";
                    CallableStatement cst = null;
                    DBConnector datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();
                    cst = conn.prepareCall(Query);
                    cst.setBigDecimal(1,
                                      new BigDecimal(r.getAttribute("agnlCode").toString()));
                    cst.setString(2, (String)session.getAttribute("Username"));
                    if (r.getAttribute("selected").equals(true)) {
                        cst.setString(3, "GIS");
                    } else {
                        cst.setString(3, null);
                    }
                    if (r.getAttribute("select").equals(true)) {
                        cst.setString(4, "LMS");
                    } else {
                        cst.setString(4, null);
                    }
                    cst.execute();
                }

            }
            conn.close();

            ADFUtils.findIterator("findLoadedAgentsIterator").executeQuery();
            GlobalCC.refreshUI(agentsLoadingTbl);
            GlobalCC.INFORMATIONREPORTING("Loading of data Successfull");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.INFORMATIONREPORTING("Error Loading Agent");
        }


        return null;
    }

    public void setAgentsLoadingTbl(RichTable agentsLoadingTbl) {
        this.agentsLoadingTbl = agentsLoadingTbl;
    }

    public RichTable getAgentsLoadingTbl() {
        return agentsLoadingTbl;
    }

    public void selectAgentToEdit(SelectionEvent selectionEvent) {
        Object key2 = agentsLoadingTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
        }
        session.setAttribute("agntCode", r.getAttribute("agntCode"));
    }

    public void setTxtAccountCode(RichInputText txtAccountCode) {
        this.txtAccountCode = txtAccountCode;
    }

    public RichInputText getTxtAccountCode() {
        return txtAccountCode;
    }

    public void setTxtAgentShtDesc(RichInputText txtAgentShtDesc) {
        this.txtAgentShtDesc = txtAgentShtDesc;
    }

    public RichInputText getTxtAgentShtDesc() {
        return txtAgentShtDesc;
    }

    public void setTxtAgentName(RichInputText txtAgentName) {
        this.txtAgentName = txtAgentName;
    }

    public RichInputText getTxtAgentName() {
        return txtAgentName;
    }

    public void setTxtPhysicalAddress(RichInputText txtPhysicalAddress) {
        this.txtPhysicalAddress = txtPhysicalAddress;
    }

    public RichInputText getTxtPhysicalAddress() {
        return txtPhysicalAddress;
    }

    public void setTxtPostalAddress(RichInputDate txtPostalAddress) {
        this.txtPostalAddress = txtPostalAddress;
    }

    public RichInputDate getTxtPostalAddress() {
        return txtPostalAddress;
    }

    public void setTxtTownCodeAgents(RichInputNumberSpinbox txtTownCodeAgents) {
        this.txtTownCodeAgents = txtTownCodeAgents;
    }

    public RichInputNumberSpinbox getTxtTownCodeAgents() {
        return txtTownCodeAgents;
    }

    public void setTxtCountryCode(RichInputNumberSpinbox txtCountryCode) {
        this.txtCountryCode = txtCountryCode;
    }

    public RichInputNumberSpinbox getTxtCountryCode() {
        return txtCountryCode;
    }

    public void setTxtEmailAddress(RichInputText txtEmailAddress) {
        this.txtEmailAddress = txtEmailAddress;
    }

    public RichInputText getTxtEmailAddress() {
        return txtEmailAddress;
    }

    public void setTxtContactPerson(RichInputText txtContactPerson) {
        this.txtContactPerson = txtContactPerson;
    }

    public RichInputText getTxtContactPerson() {
        return txtContactPerson;
    }

    public void setTxtAccountNumberAgents(RichInputText txtAccountNumberAgents) {
        this.txtAccountNumberAgents = txtAccountNumberAgents;
    }

    public RichInputText getTxtAccountNumberAgents() {
        return txtAccountNumberAgents;
    }

    public void setTxtCreditAllowed(RichSelectOneChoice txtCreditAllowed) {
        this.txtCreditAllowed = txtCreditAllowed;
    }

    public RichSelectOneChoice getTxtCreditAllowed() {
        return txtCreditAllowed;
    }

    public void setTxtCreatedByAgent(RichInputText txtCreatedByAgent) {
        this.txtCreatedByAgent = txtCreatedByAgent;
    }

    public RichInputText getTxtCreatedByAgent() {
        return txtCreatedByAgent;
    }

    public void setTxtCreditRating(RichInputText txtCreditRating) {
        this.txtCreditRating = txtCreditRating;
    }

    public RichInputText getTxtCreditRating() {
        return txtCreditRating;
    }

    public void setTxtCreateddateAgent(RichInputDate txtCreateddateAgent) {
        this.txtCreateddateAgent = txtCreateddateAgent;
    }

    public RichInputDate getTxtCreateddateAgent() {
        return txtCreateddateAgent;
    }

    public void setTxtpostalAddressAgnt(RichInputText txtpostalAddressAgnt) {
        this.txtpostalAddressAgnt = txtpostalAddressAgnt;
    }

    public RichInputText getTxtpostalAddressAgnt() {
        return txtpostalAddressAgnt;
    }

    public void setTxtBranchname(RichInputText txtBranchname) {
        this.txtBranchname = txtBranchname;
    }

    public RichInputText getTxtBranchname() {
        return txtBranchname;
    }

    public void setSelectBankBranch(RichTable selectBankBranch) {
        this.selectBankBranch = selectBankBranch;
    }

    public RichTable getSelectBankBranch() {
        return selectBankBranch;
    }

    public void selectBranchDetails(SelectionEvent selectionEvent) {
        Object key2 = branchesTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
        }
        session.setAttribute("branchCodelov", r.getAttribute("branchCodelov"));
        session.setAttribute("branchName", r.getAttribute("branchName"));

    }

    public void setBranchesTbl(RichTable branchesTbl) {
        this.branchesTbl = branchesTbl;
    }

    public RichTable getBranchesTbl() {
        return branchesTbl;
    }

    public void selectBranch(DialogEvent dialogEvent) {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:BranchPopup" + "').hide(hints);");
        erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:editAgentPopup" + "').show(hints);");
        txtBranchname.setValue(session.getAttribute("branchName"));
        GlobalCC.refreshUI(txtBranchname);
    }

    public void setValues(ValueChangeEvent valueChangeEvent) {
        if (optionSelected.getValue().equals("N")) {
            session.setAttribute("options", "N");
        } else if (optionSelected.getValue().equals("L")) {
            session.setAttribute("options", "L");
        } else if (optionSelected.getValue().equals("A")) {
            session.setAttribute("options", "A");
        }
        ADFUtils.findIterator("findLoadedAgentsIterator").executeQuery();
        GlobalCC.refreshUI(agentsLoadingTbl);
    }

    public void setValuesClient(ValueChangeEvent valueChangeEvent) {
        if (optionSelected.getValue().equals("N")) {
            session.setAttribute("options", "N");
        } else if (optionSelected.getValue().equals("L")) {
            session.setAttribute("options", "Y");
        } else if (optionSelected.getValue().equals("A")) {
            session.setAttribute("options", "A");
        }
        ADFUtils.findIterator("findLoadedClientsIterator").executeQuery();
        GlobalCC.refreshUI(clientsDataTbl);
    }

    public void setOptionSelected(RichSelectOneChoice optionSelected) {
        this.optionSelected = optionSelected;
    }

    public RichSelectOneChoice getOptionSelected() {
        return optionSelected;
    }

    public void setTxtSystemType(RichSelectOneChoice txtSystemType) {
        this.txtSystemType = txtSystemType;
    }

    public RichSelectOneChoice getTxtSystemType() {
        return txtSystemType;
    }

    public void setSelectAllClients(RichCommandButton selectAllClients) {
        this.selectAllClients = selectAllClients;
    }

    public RichCommandButton getSelectAllClients() {
        return selectAllClients;
    }

    public String selectAllAction() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            r.setAttribute("selected", true);
            rowChecked.setSelected(true);
            GlobalCC.refreshUI(selected);
            i++;
        }
        return null;
    }

    public String selectAllActionLms() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            r.setAttribute("select", true);
            select.setSelected(true);
            GlobalCC.refreshUI(select);
            i++;
        }
        return null;
    }

    public String unselectAllGisAction() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("selected", false);
            selected.setSelected(false);
            GlobalCC.refreshUI(selected);

            i++;
        }
        return null;
    }

    public String unselectAllLmsAction() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            if (r == null)
                continue;

            r.setAttribute("select", false);
            select.setSelected(false);
            GlobalCC.refreshUI(select);
            i++;
        }
        return null;
    }

    public String selectAllActionAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            r.setAttribute("selected", true);
            selected.setSelected(true);
            GlobalCC.refreshUI(selected);
            i++;
        }
        return null;
    }

    public String selectAllActionLmsAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            r.setAttribute("select", true);
            select.setSelected(true);
            GlobalCC.refreshUI(select);
            i++;
        }
        return null;
    }

    public String unselectAllGisActionAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("selected", false);
            selected.setSelected(false);
            GlobalCC.refreshUI(selected);

            i++;
        }
        return null;
    }

    public String unselectAllLmsActionAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("select", false);
            select.setSelected(false);
            GlobalCC.refreshUI(select);

            i++;
        }
        return null;
    }

    public void setChecked(RichSelectBooleanCheckbox checked) {
        this.checked = checked;
    }

    public RichSelectBooleanCheckbox getChecked() {
        return checked;
    }

    public void setTxtSystemName(RichSelectOneChoice txtSystemName) {
        this.txtSystemName = txtSystemName;
    }

    public RichSelectOneChoice getTxtSystemName() {
        return txtSystemName;
    }

    public void setSelected(RichSelectBooleanCheckbox selected) {
        this.selected = selected;
    }

    public RichSelectBooleanCheckbox getSelected() {
        return selected;
    }

    public void setSelect(RichSelectBooleanCheckbox select) {
        this.select = select;
    }

    public RichSelectBooleanCheckbox getSelect() {
        return select;
    }

    public void setTxtTownName(RichInputText txtTownName) {
        this.txtTownName = txtTownName;
    }

    public RichInputText getTxtTownName() {
        return txtTownName;
    }

    public void setTxtPostalAddressClient(RichInputText txtPostalAddressClient) {
        this.txtPostalAddressClient = txtPostalAddressClient;
    }

    public RichInputText getTxtPostalAddressClient() {
        return txtPostalAddressClient;
    }

    public void setTxtTelNumber(RichInputText txtTelNumber) {
        this.txtTelNumber = txtTelNumber;
    }

    public RichInputText getTxtTelNumber() {
        return txtTelNumber;
    }

    public void setTxtEmailAddresscln(RichInputText txtEmailAddresscln) {
        this.txtEmailAddresscln = txtEmailAddresscln;
    }

    public RichInputText getTxtEmailAddresscln() {
        return txtEmailAddresscln;
    }

    public void setTxtFaxNumber(RichInputText txtFaxNumber) {
        this.txtFaxNumber = txtFaxNumber;
    }

    public RichInputText getTxtFaxNumber() {
        return txtFaxNumber;
    }

    public void setTxtTelNumber2(RichInputText txtTelNumber2) {
        this.txtTelNumber2 = txtTelNumber2;
    }

    public RichInputText getTxtTelNumber2() {
        return txtTelNumber2;
    }

    public String selectAllActions() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            r.setAttribute("checked", true);
            rowChecked.setSelected(true);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public String unselectAllAction() {
        int i = 0;
        while (i < clientsDataTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)clientsDataTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("checked", false);
            rowChecked.setSelected(false);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public void setTxtAccountType(RichInputText txtAccountType) {
        this.txtAccountType = txtAccountType;
    }

    public RichInputText getTxtAccountType() {
        return txtAccountType;
    }

    public void setTxtAccountName(RichInputText txtAccountName) {
        this.txtAccountName = txtAccountName;
    }

    public RichInputText getTxtAccountName() {
        return txtAccountName;
    }

    public void setTxtTownNamed(RichInputText txtTownNamed) {
        this.txtTownNamed = txtTownNamed;
    }

    public RichInputText getTxtTownNamed() {
        return txtTownNamed;
    }

    public void setTxtIdNumber(RichInputText txtIdNumber) {
        this.txtIdNumber = txtIdNumber;
    }

    public RichInputText getTxtIdNumber() {
        return txtIdNumber;
    }

    public void setTxtPhoneNumber(RichInputText txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public RichInputText getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public void setTxtFaxNumbers(RichInputText txtFaxNumbers) {
        this.txtFaxNumbers = txtFaxNumbers;
    }

    public RichInputText getTxtFaxNumbers() {
        return txtFaxNumbers;
    }

    public void setTxtEmailAddresss(RichInputText txtEmailAddresss) {
        this.txtEmailAddresss = txtEmailAddresss;
    }

    public RichInputText getTxtEmailAddresss() {
        return txtEmailAddresss;
    }

    public void setTxtPostedDate(RichInputDate txtPostedDate) {
        this.txtPostedDate = txtPostedDate;
    }

    public RichInputDate getTxtPostedDate() {
        return txtPostedDate;
    }

    public void selectAgent(SelectionEvent selectionEvent) {
        Object key2 = agentsLoadingTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
        }
        session.setAttribute("agnlCodes", r.getAttribute("agnlCode"));
    }

    public String selectAllAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            r.setAttribute("checked", true);
            rowChecked.setSelected(true);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public String unSelectAllAgents() {
        int i = 0;
        while (i < agentsLoadingTbl.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agentsLoadingTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("checked", false);
            rowChecked.setSelected(false);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public void setRowSelect(RichSelectBooleanCheckbox rowSelect) {
        this.rowSelect = rowSelect;
    }

    public RichSelectBooleanCheckbox getRowSelect() {
        return rowSelect;
    }

    public void downloadTemplate(FacesContext facesContext,
                                 OutputStream outputStream) {
        String path = null;
        path =  GlobalCC.getSysParamValue("AGENT_LOADIND_TEMP_PATH");
        if(path != null){
                 
                final File myFile =  new File(path);
                try {
                    
                    final byte[] bytes = IOUtils.toByteArray(new FileInputStream(myFile));
                    outputStream.write(bytes);
                    
                    outputStream.flush();
                    outputStream.close();
                    facesContext.responseComplete();
                } catch (IOException e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
        }else{
               GlobalCC.INFORMATIONREPORTING("Please define report path");
             }
       
       
    }
    
    
    public void downloadClientTemplate(FacesContext facesContext,
                                 OutputStream outputStream) {
        String path = null;
        path =  GlobalCC.getSysParamValue("CLIENT_LOADING_TEMP_PATH");
        if(path != null){
                 
                final File myFile =  new File(path);
                try {
                    
                    final byte[] bytes = IOUtils.toByteArray(new FileInputStream(myFile));
                    outputStream.write(bytes);
                    
                    outputStream.flush();
                    outputStream.close();
                    facesContext.responseComplete();
                } catch (IOException e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
        }else{
               GlobalCC.INFORMATIONREPORTING("Please define report path");
             }
       
       
    }
}
