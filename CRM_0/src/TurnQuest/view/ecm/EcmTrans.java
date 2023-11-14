package TurnQuest.view.ecm;


import TurnQuest.view.Alerts.JBPMEngine;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.math.BigInteger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleConnection;

import org.alfresco.cmis.client.AlfrescoDocument;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;


public class EcmTrans {
   
    private RichSelectOneRadio txtTransTypes;
    private RichInputText txtTransNo;
    private RichTable proposals;
    private RichTable claimsEnEd;
    private RichInputText propolNo;
    private RichInputText polNo;
    private RichInputText client;
    private RichInputText clientPhone;
    private RichInputDate clientDob;
    private RichInputText clientIdNo;
    private RichInputText refNo;
    private RichPanelGroupLayout polLovDetails;
    private RichPopup claimsPopUp;
    private RichInputText clmNoSearch;
    private RichInputText clmPolNoSearch;
    private RichInputText clmClientSearch;
    private RichInputText clmCausSearch;
    private RichTable quotations;

    public EcmTrans() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

//    public String createDmsQuote() {
//
//        JBPMEngine bpm = new JBPMEngine();
//        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
//        bpm.startWorkFlowInstance();
//        session.setAttribute("ProcessShtDesc", "OQUOT");
//        session.setAttribute("ProcessAreaShtDesc", "NQ");
//        session.setAttribute("ProcessSubAShtDesc", "NQA");
//        session.setAttribute("authAmount", null);
//        session.setAttribute("debitCredit", null);
//        String rights = bpm.CheckUserRights();
//        rights = (rights == null) ? "N" : rights;
//        if (rights.equalsIgnoreCase("N")) {
//            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
//            return null;
//        }
//
//        try {
//            session.setAttribute("docName", null);
//            session.setAttribute("dateCreated", null);
//            session.setAttribute("createdBy", null);
//            session.setAttribute("version", null);
//            session.setAttribute("sysModule", "Q");
//            session.setAttribute("module", "QUOTE");
//            session.setAttribute("viewTrans", false);
//            session.setAttribute("viewSource", true);
//            session.setAttribute("quoteTrans", "NQ");
//            session.setAttribute("editMode", false);
//            session.setAttribute("process", "LOAN");
//            session.setAttribute("quotPol", "Q");
//            session.setAttribute("filter", null);
//            // session.setAttribute("enquiryMode", false);
//            session.removeAttribute("quoteCode");
//            String enabled = GlobalCC.findProcessFlowEnabled();
//            if (enabled.equalsIgnoreCase("Y") || enabled == null) {
//                Map<String, Object> variables = new HashMap<String, Object>();
//                variables.put("type", "quote");
//                variables.put("id", session.getAttribute("quoteCode"));
//                variables.put("assignee",
//                              ((BigDecimal)session.getAttribute("UserCode")).toString());
//                session.setAttribute("decision", "Yes");
//                String processId = null;
//                processId =
//                        bpm.findProcessInstanceWhetherStarted(GlobalCC.checkNullValues(session.getAttribute("quoteCode")),
//                                                              "Q");
//                if (processId == null) {
//                    processId = bpm.startWorkflow("quotations", variables);
//                    String name = null;
//                    name = bpm.findProcessInstanceTaskName(processId);
//                    String taskID = null;
//                    taskID = bpm.findProcessInstanceTaskId(processId);
//                    session.setAttribute("activityName", name);
//                    session.setAttribute("ticketID", taskID);
//                    session.setAttribute("processInstance", processId);
//                    String active = null;
//                    active = "Y";
//
//                    bpm.saveTicketDetails(processId,
//                                          GlobalCC.checkNullValues(session.getAttribute("quoteCode")),
//                                          taskID, "QUOTE", name, active);
//                }
//            }
//            GlobalCC.viewContext("quotations");
//        } catch (Exception e) {
//            e.printStackTrace();
//            GlobalCC.EXCEPTIONREPORTING(null, e);
//
//        } finally {
//            bpm.endWorkFlowInstance(jbpmConfig);
//        }
//        return null;
//    }

//    public String createDmsPolicy() {
//
//
//        session.setAttribute("ProcessShtDesc", "OUW");
//        session.setAttribute("ProcessAreaShtDesc", "PROP");
//        session.setAttribute("ProcessSubAShtDesc", "PROPA");
//        session.setAttribute("newProposal", true);
//        session.setAttribute("authAmount", null);
//        session.setAttribute("debitCredit", null);
//        session.setAttribute("sysDate", new java.util.Date());
//
//        JBPMEngine bpm = new JBPMEngine();
//        JbpmConfiguration jbpmConfig = new JbpmConfiguration();
//        bpm.startWorkFlowInstance();
//        String rights = bpm.CheckUserRights();
//        rights = (rights == null) ? "N" : rights;
//        if (rights.equalsIgnoreCase("N")) {
//            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
//            return null;
//        }
//        session.setAttribute("sysModule", "U");
//        session.setAttribute("process", "UPR");
//        session.setAttribute("proposal", true);
//        session.setAttribute("filter", null);
//        session.setAttribute("propRnd", "Yes");
//
//        String processId = null;
//        String id = "0";
//        try {
//            if ((Boolean)session.getAttribute("newProposal") == true) {
//                session.setAttribute("newPropdecision", "Yes");
//
//
//                String enabled = GlobalCC.findProcessFlowEnabled();
//                if (enabled.equalsIgnoreCase("Y") || enabled == null) {
//                    Map<String, Object> variables =
//                        new HashMap<String, Object>();
//                    variables.put("type", "ProposalUnderwriting");
//                    variables.put("id", "0");
//                    variables.put("assignee",
//                                  ((BigDecimal)session.getAttribute("UserCode")).toString());
//
//                    session.setAttribute("decision", "Yes");
//                    processId =
//                            bpm.startWorkflow("ProposalUnderwriting1", variables);
//                    System.out.println("Id: " + processId);
//                    String name = null;
//                    name = bpm.findProcessInstanceTaskName(processId);
//                    String taskID = null;
//                    taskID = bpm.findProcessInstanceTaskId(processId);
//                    session.setAttribute("activityName", name);
//                    session.setAttribute("ticketID", taskID);
//                    session.setAttribute("processInstance", processId);
//                    String active = null;
//                    active = "Y";
//                    if (taskID != null) {
//                        bpm.saveTicketDetails(processId, id, taskID,
//                                              "U/W-PROP", name, active);
//                    }
//                }
//
//            }
//
//            GlobalCC.viewContext("proposalTrans");
//        } catch (Exception e) {
//            e.printStackTrace();
//            GlobalCC.EXCEPTIONREPORTING(null, e);
//
//        } finally {
//            bpm.endWorkFlowInstance(jbpmConfig);
//        }
//
//        return null;
//    }

    public String createDmsClaim() {

        session.setAttribute("ProcessShtDesc", "OCLM");
        session.setAttribute("ProcessAreaShtDesc", "CLM");
        session.setAttribute("ProcessSubAShtDesc", "CLMA");
        session.setAttribute("authAmount", null);
        session.setAttribute("debitCredit", null);
        
        JBPMEngine bpm = JBPMEngine.getInstance();
        String rights = bpm.CheckUserRights();
        rights = (rights == null) ? "N" : rights;
        if (rights.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
            return null;
        }

        session.setAttribute("process", "CLM");
        session.setAttribute("filter", null);
        try {

            GlobalCC.viewContext("claimTrans");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);

        }
        return null;
    }

    public String createDmsLoan() {


      session.setAttribute("ProcessShtDesc", "OCLM");
      session.setAttribute("ProcessAreaShtDesc", "LN");
      session.setAttribute("ProcessSubAShtDesc", "LNA");
      session.setAttribute("authAmount", null);
      session.setAttribute("debitCredit", null);
        JBPMEngine bpm = JBPMEngine.getInstance();

      String rights = bpm.CheckUserRights();
      rights = (rights == null) ? "N" : rights;
      if (rights.equalsIgnoreCase("N")) {
          GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
          return null;
      }
      session.setAttribute("process", "LOAN");
      session.setAttribute("docType", "LOAN");
      session.setAttribute("filter", null);
      session.setAttribute("propRnd", "No");
        try {

          GlobalCC.viewContext("loanAdministration");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);

        }
        return null;
    }

    public String createDmsSurrender() {
        
      session.setAttribute("ProcessShtDesc", "OCLM");
      session.setAttribute("ProcessAreaShtDesc", "SUR");
      session.setAttribute("ProcessSubAShtDesc", "SURA");
      session.setAttribute("authAmount", null);
      session.setAttribute("debitCredit", null);
      
      JBPMEngine bpm = JBPMEngine.getInstance();
      String rights = bpm.CheckUserRights();
      rights = (rights == null) ? "N" : rights;
      if (rights.equalsIgnoreCase("N")) {
          GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
          return null;
      }
      session.setAttribute("process", "SUR");
      session.setAttribute("docType", "SURREN");
      session.setAttribute("filter", null);
      session.setAttribute("propRnd", "No");
//      session.setAttribute("butpayallowed",
//                           GlobalCC.findSysParam("ALLOW_SURRENDER_BUTPAY",
//                                                 null));
        try {

          GlobalCC.viewContext("polSurrenderTrans");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);

        }
        return null;
    }


    public String startTransaction() {
        txtTransNo.setValue(null);
        GlobalCC.showPopup("demoTemplate:p1");
        return null;
    }

    public void policyNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String polNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("polNo", polNoVal);
            //ADFUtils.findIterator("findProposalLOVIterator").executeQuery();
            //AdfFacesContext.getCurrentInstance().addPartialTarget(polLovDetails);
        }
    }

    public void clientNameChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientNameVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("Name", clientNameVal);
        }
    }

    public void clientDobChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientDobVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("clientDob", clientDobVal);
        }
    }

    public void clientIdNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientIdVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("clientId", clientIdVal);
        }
    }

    public void refNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String refNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("polRefNo", refNoVal);
        }
    }


    public void clientPhoneChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientPhoneVal =
                GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("clientPhone", clientPhoneVal);
        }
    }

    public String searchPolicy() {
        String propolNoVal = GlobalCC.checkNullValues(propolNo.getValue());
        String clientNameVal = GlobalCC.checkNullValues(client.getValue());
        String refNoVal = GlobalCC.checkNullValues(refNo.getValue());
        String polNoVal = GlobalCC.checkNullValues(polNo.getValue());
        String IdNoVal = GlobalCC.checkNullValues(clientIdNo.getValue());
        String PhoneNoVal = GlobalCC.checkNullValues(clientPhone.getValue());
        String clientDobVal = GlobalCC.checkNullValues(clientDob.getValue());
        if (clientDobVal != null) {
            if (clientDobVal.contains(":")) {
                clientDobVal = GlobalCC.parseDate(clientDobVal);
            } else {
                clientDobVal = GlobalCC.upDateParseDate(clientDobVal);
            }
        }
        session.setAttribute("propolNo", propolNoVal);
        session.setAttribute("Name", clientNameVal);
        session.setAttribute("polRefNo", refNoVal);
        session.setAttribute("polNo", polNoVal);
        session.setAttribute("clientDob", clientDobVal);
        session.setAttribute("clientPhone", PhoneNoVal);
        session.setAttribute("clientId", IdNoVal);
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


    public void proposalNoChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String propolNoVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("propolNo", propolNoVal);
            //ADFUtils.findIterator("findProposalLOVIterator").executeQuery();
            //AdfFacesContext.getCurrentInstance().addPartialTarget(polLovDetails);
        }
    }

    public void attachDocument(DialogEvent dialogEvent) {
        String docid = (String)session.getAttribute("dmsDocId");
        EcmUtil ecmUtil = new EcmUtil();
        if (txtTransNo.getValue() == null) {
            GlobalCC.errorValueNotEntered("Select Transaction...");
            return;
        }

        if (docid == null) {
            GlobalCC.errorValueNotEntered("The Document has already been attached to other document.....");
            return;
        }
        if (docid != null) {
            Session ecmSession = ecmUtil.Authentification();
            if (ecmSession == null) {

            } else {

                if (txtTransTypes.getValue().equals("P")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordPolicyDocument";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);
                    properties.put("tqlms:policynumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:clientname",
                                   session.getAttribute("proposalClient"));
                    alfDoc.updateProperties(properties);


                } else if (txtTransTypes.getValue().equals("Q")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordQuotationDocs";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);
                    properties.put("tqlms:QuotQuotationNumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:QuotClientName",
                                   session.getAttribute("proposalClient"));
//                    properties.put("tqlms:QuotDate",
//                                   session.getAttribute("quoDate"));
                    properties.put("tqlms:QuotProductName",
                                   session.getAttribute("product"));
                    alfDoc.updateProperties(properties);

                } else if (txtTransTypes.getValue().equals("C")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordClaimDocument";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);

                    properties.put("tqlms:claimnumber",
                                   session.getAttribute("claimNumber"));
                    properties.put("tqlms:cpolicynumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:cclientname",
                                   session.getAttribute("proposalClient"));
//                    properties.put("tqlms:claimdate",
//                                   session.getAttribute("dateReported"));
                    // properties.put("tqgib:claimDeptIntermediaryName",(String)session.getAttribute("AgntShtDesc"));
                    alfDoc.updateProperties(properties);

                } else if (txtTransTypes.getValue().equals("S")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordClaimDocument";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);
                    properties.put("tqlms:cpolicynumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:cclientname",
                                   session.getAttribute("client"));
//                    properties.put("tqlms:claimdate",
//                                   session.getAttribute("dateReported"));
                    // properties.put("tqgib:claimDeptIntermediaryName",(String)session.getAttribute("AgntShtDesc"));
                    alfDoc.updateProperties(properties);

                } else if (txtTransTypes.getValue().equals("L")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordClaimDocument";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);
                    //                    properties.put("tqlms:claimnumber",
                    //                                   (String)session.getAttribute("lnNo"));
                    properties.put("tqlms:cpolicynumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:cclientname",
                                   session.getAttribute("proposalClient"));
//                    properties.put("tqlms:claimdate",
//                                   session.getAttribute("dateReported"));
                    // properties.put("tqgib:claimDeptIntermediaryName",(String)session.getAttribute("AgntShtDesc"));
                    alfDoc.updateProperties(properties);

                } else if (txtTransTypes.getValue().equals("M")) {
                    Document document = (Document)ecmSession.getObject(docid);
                    AlfrescoDocument alfDoc = (AlfrescoDocument)document;
                    String aspectname = "P:tqlms:tqordClaimDocument";
                    alfDoc.addAspect(aspectname);
                    Map<String, Object> properties =
                        new HashMap<String, Object>();
                    // properties.put("cmis:secondaryObjectTypeIds", aspects);
                    properties.put("tqlms:cpolicynumber",
                                   session.getAttribute("ecmPolNo"));
                    properties.put("tqlms:cclientname",
                                   session.getAttribute("proposalClient"));
                    // properties.put("tqgib:claimDeptIntermediaryName",(String)session.getAttribute("AgntShtDesc"));
                    alfDoc.updateProperties(properties);

                }


                DBConnector dbConn = new DBConnector();
                OracleConnection conn = null;
                conn = dbConn.getDatabaseConnection();
                String query =
                    "update lms_dms_documents  set dd_processed = 'Y' where dd_doc_id = ?";
                PreparedStatement stmt = null;
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, docid);
                    stmt.execute();
                    conn.close();
                    session.removeAttribute("dmsDocId");

                } catch (SQLException e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            }
        }
    }


    public String endrPolicySelected() {
        Object key2 = proposals.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        txtTransNo.setValue(r.getAttribute("proposalNumber"));
        AdfFacesContext.getCurrentInstance().addPartialTarget(txtTransNo);
        session.setAttribute("endorsementCode",
                             r.getAttribute("endorsementCode"));
        session.setAttribute("productType", r.getAttribute("productType"));
        session.setAttribute("productCode", r.getAttribute("productCode"));
        session.setAttribute("ecmProductCode", r.getAttribute("productCode"));
        session.setAttribute("proposalNumber",
                             r.getAttribute("proposalNumber"));
        session.setAttribute("ecmPolNo", r.getAttribute("proposalNumber"));
        session.setAttribute("policyProposalCode",
                             r.getAttribute("proposalCode"));
        session.setAttribute("polCode", r.getAttribute("proposalCode"));
        session.setAttribute("proposalClient",
                             r.getAttribute("proposalClient"));
        session.setAttribute("clientCode", r.getAttribute("clientCode"));
        session.setAttribute("ClientCode", r.getAttribute("prpCode"));
        session.setAttribute("polClient", r.getAttribute("prpCode"));
        String statusVal = (String)r.getAttribute("status");
        session.setAttribute("prdCode", r.getAttribute("productCode"));
        session.setAttribute("proposaleffectiveDate",
                             r.getAttribute("proposalEffectiveDate"));
        session.setAttribute("prodAllowRenwal",
                             r.getAttribute("prodAllowRenwal"));
        GlobalCC.hidePopup("demoTemplate:propPol");
        return null;
    }


    public String cancPolicySelected() {
        GlobalCC.hidePopup("demoTemplate:propPol");
        return null;
    }

//    Public String Getclaimclientsviewstring() {
//        If (Session.Getattribute("filter") == Null) {
//            Globalprocessor.Filterview(Claimsened, "clientName");
//            Session.Setattribute("filter", "Y");
//        }
//        Return "Values";
//    }

    public void clmNoSearchEvt(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())
            if (clmNoSearch.getValue() != null) {
                session.setAttribute("clmNoSearch",
                                     clmNoSearch.getValue().toString());
            }
        ADFUtils.findIterator("findClaimsIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(claimsEnEd);
    }


    public void clmPolNoSearchEvt(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())
            if (clmPolNoSearch.getValue() != null) {
                session.setAttribute("clmPolNoSearch",
                                     clmPolNoSearch.getValue().toString());
            }
        ADFUtils.findIterator("findClaimsIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(claimsEnEd);
    }

    public void clmClientSearchEvt(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())
            if (clmClientSearch.getValue() != null) {
                session.setAttribute("clmClientSearch",
                                     clmClientSearch.getValue().toString());
            }
        ADFUtils.findIterator("findClaimsIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(claimsEnEd);
    }


    public String clmSearchBtnAction() {
        // Add event code here...
        if (clmNoSearch.getValue() != null) {
            session.setAttribute("clmNoSearch",
                                 clmNoSearch.getValue().toString());
        }
        if (clmPolNoSearch.getValue() != null) {
            session.setAttribute("clmPolNoSearch",
                                 clmPolNoSearch.getValue().toString());
        }
        if (clmClientSearch.getValue() != null) {
            session.setAttribute("clmClientSearch",
                                 clmClientSearch.getValue().toString());
        }
        if (clmCausSearch.getValue() != null) {
            session.setAttribute("clmCausSearch",
                                 clmCausSearch.getValue().toString());
        }
        ADFUtils.findIterator("findClaimsIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(claimsEnEd);
        return null;
    }


    public void clmCausSearch(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())
            if (clmCausSearch.getValue() != null) {
                session.setAttribute("clmCausSearch",
                                     clmCausSearch.getValue().toString());
            }
        ADFUtils.findIterator("findClaimsIterator").executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(claimsEnEd);

    }


    public String claimSelected() {
        Object key2 = claimsEnEd.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        try {
            txtTransNo.setValue(r.getAttribute("clmNo"));
            session.setAttribute("cNotCode", r.getAttribute("CNoteCode"));
            session.setAttribute("claimNumber", r.getAttribute("clmNo"));
            session.setAttribute("prpCode", r.getAttribute("prpCode"));
            session.setAttribute("authprpCode", r.getAttribute("prpCode"));
            session.setAttribute("transNo", r.getAttribute("ltrTransNo"));
            session.setAttribute("proposalNumber",
                                 r.getAttribute("polPolicyNo"));
            session.setAttribute("ecmPolNo", r.getAttribute("polPolicyNo"));
            session.setAttribute("proposalClient",
                                 r.getAttribute("clientName"));
            session.setAttribute("dateReported",
                                 r.getAttribute("clmDateClaimReported"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(txtTransNo);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        GlobalCC.hidePopup("demoTemplate:claim");

        return null;
    }

    public String quotationSelected() {
        Object key2 = quotations.getSelectedRowData();
        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        session.setAttribute("quoteCode", r.getAttribute("quoteCode"));
        session.setAttribute("quoteNumber", r.getAttribute("quoteNo"));
        session.setAttribute("clientName", r.getAttribute("client"));
        session.setAttribute("prpCode", r.getAttribute("prpCode"));
        session.setAttribute("proposalClient", r.getAttribute("client"));
        session.setAttribute("ecmPolNo", r.getAttribute("quoteNo"));
        // session.setAttribute("clientCode", GlobalCC.findClientCode((BigDecimal)r.getAttribute("prpCode")));
        txtTransNo.setValue((String)session.getAttribute("quoteNumber"));
        session.setAttribute("quoDate", r.getAttribute("quoDate"));
        session.setAttribute("product", r.getAttribute("quoProdDesc"));
        AdfFacesContext.getCurrentInstance().addPartialTarget(txtTransNo);
        session.setAttribute("filter", null);
        session.setAttribute("QuoProdType", r.getAttribute("quoProdType"));

        GlobalCC.hidePopup("demoTemplate:quotation");
        return null;
    }

    public String cancQuoSelected() {
        GlobalCC.hidePopup("demoTemplate:quotation");
        return null;
    }


    public String cancClaimSelected() {
        GlobalCC.hidePopup("demoTemplate:claim");

        return null;
    }

//    public String getQuoteClientsViewString() {
//        if (session.getAttribute("filter") == null) {
//            GlobalProcessor.filterView(quotations, "client");
//            session.setAttribute("filter", "Y");
//        }
//        return "Values";
//    }

    public String showTrans() {
        if (txtTransTypes.getValue().equals("P")) {
            session.setAttribute("transCode", "EP");
            session.setAttribute("process", "UPL");
            GlobalCC.showPopup("demoTemplate:propPol");
        } else if (txtTransTypes.getValue().equals("Q")) {
            session.setAttribute("viewTrans", true);
            session.setAttribute("viewSource", true);
            session.setAttribute("quoteTrans", "RQ");
            session.setAttribute("editMode", true);
            session.removeAttribute("quoteCode");
            GlobalCC.showPopup("demoTemplate:quotation");
        } else if (txtTransTypes.getValue().equals("C")) {
            session.setAttribute("newClaimtrans", false);
            session.setAttribute("editClaimTrans", true);
            session.setAttribute("viewClaimTrans", false);
            session.setAttribute("authClaim", "N");
            session.setAttribute("ProcessSubAShtDesc", "ECLM");
            GlobalCC.showPopup("demoTemplate:claim");
        } else if (txtTransTypes.getValue().equals("S")) {
            session.setAttribute("transCode", "SE");
            session.setAttribute("process", "SUR");
            GlobalCC.showPopup("demoTemplate:propPol");
        } else if (txtTransTypes.getValue().equals("L")) {
            session.setAttribute("transCode", "LA");
            session.setAttribute("process", "LOAN");
            GlobalCC.showPopup("demoTemplate:propPol");
        }
        return null;
    }

    public void setTxtTransTypes(RichSelectOneRadio txtTransTypes) {
        this.txtTransTypes = txtTransTypes;
    }

    public RichSelectOneRadio getTxtTransTypes() {
        return txtTransTypes;
    }

    public void setTxtTransNo(RichInputText txtTransNo) {
        this.txtTransNo = txtTransNo;
    }

    public RichInputText getTxtTransNo() {
        return txtTransNo;
    }

    public void setProposals(RichTable proposals) {
        this.proposals = proposals;
    }

    public RichTable getProposals() {
        return proposals;
    }

    public void setClaimsEnEd(RichTable claimsEnEd) {
        this.claimsEnEd = claimsEnEd;
    }

    public RichTable getClaimsEnEd() {
        return claimsEnEd;
    }

    public void setPropolNo(RichInputText propolNo) {
        this.propolNo = propolNo;
    }

    public RichInputText getPropolNo() {
        return propolNo;
    }

    public void setPolNo(RichInputText polNo) {
        this.polNo = polNo;
    }

    public RichInputText getPolNo() {
        return polNo;
    }

    public void setClient(RichInputText client) {
        this.client = client;
    }

    public RichInputText getClient() {
        return client;
    }

    public void setClientPhone(RichInputText clientPhone) {
        this.clientPhone = clientPhone;
    }

    public RichInputText getClientPhone() {
        return clientPhone;
    }

    public void setClientDob(RichInputDate clientDob) {
        this.clientDob = clientDob;
    }

    public RichInputDate getClientDob() {
        return clientDob;
    }

    public void setClientIdNo(RichInputText clientIdNo) {
        this.clientIdNo = clientIdNo;
    }

    public RichInputText getClientIdNo() {
        return clientIdNo;
    }

    public void setRefNo(RichInputText refNo) {
        this.refNo = refNo;
    }

    public RichInputText getRefNo() {
        return refNo;
    }

    public void setPolLovDetails(RichPanelGroupLayout polLovDetails) {
        this.polLovDetails = polLovDetails;
    }

    public RichPanelGroupLayout getPolLovDetails() {
        return polLovDetails;
    }

    public void setClaimsPopUp(RichPopup claimsPopUp) {
        this.claimsPopUp = claimsPopUp;
    }

    public RichPopup getClaimsPopUp() {
        return claimsPopUp;
    }

    public void setClmNoSearch(RichInputText clmNoSearch) {
        this.clmNoSearch = clmNoSearch;
    }

    public RichInputText getClmNoSearch() {
        return clmNoSearch;
    }

    public void setClmPolNoSearch(RichInputText clmPolNoSearch) {
        this.clmPolNoSearch = clmPolNoSearch;
    }

    public RichInputText getClmPolNoSearch() {
        return clmPolNoSearch;
    }

    public void setClmClientSearch(RichInputText clmClientSearch) {
        this.clmClientSearch = clmClientSearch;
    }

    public RichInputText getClmClientSearch() {
        return clmClientSearch;
    }

    public void setClmCausSearch(RichInputText clmCausSearch) {
        this.clmCausSearch = clmCausSearch;
    }

    public RichInputText getClmCausSearch() {
        return clmCausSearch;
    }

    public void setQuotations(RichTable quotations) {
        this.quotations = quotations;
    }

    public RichTable getQuotations() {
        return quotations;
    }
  
    public static String UploadFile(Session session, Folder ParentFolder,
                      String FileNameInDms, File file, String FileMimeType,
                      String aspectTypes,List<EcmProps> props)
                      throws FileNotFoundException {
  
          Map<String, Object> properties = new HashMap<String, Object>();
          //properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
           properties.put(PropertyIds.OBJECT_TYPE_ID,aspectTypes);
          //properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
          properties.put(PropertyIds.NAME, FileNameInDms);
                   for(EcmProps property:props)
                   properties.put(property.getName(), property.getValue());
              
             // properties.put("tqgib:UNWRDeptPolicyNumber", "101404010211");
              List<Ace> addAces = new LinkedList<Ace>();
              List<Ace> removeAces = new LinkedList<Ace>();
              List<Policy> policies = new LinkedList<Policy>();
              ContentStream contentStream;
              
              System.out.println("The file size " + String.valueOf(file.length()));
              contentStream = new ContentStreamImpl(FileNameInDms,
                              BigInteger.valueOf(file.length()), FileMimeType,
                              new FileInputStream(file));
  
              Document newDocument = ParentFolder.createDocument(properties,
                              contentStream, VersioningState.MAJOR, policies, addAces,
                              removeAces, session.getDefaultContext());
              return (newDocument.getName());
      }
}
