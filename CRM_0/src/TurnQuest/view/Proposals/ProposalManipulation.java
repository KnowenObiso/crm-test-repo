package TurnQuest.view.Proposals;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.event.ItemEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ProposalManipulation {
    private RichPanelBox transactionDetails;
    private RichPanelBox selectTransaction;
    private RichCommandButton proposalTransSelected;
    private RichSelectBooleanRadio newProp;
    private RichSelectBooleanRadio editProp;
    private RichSelectBooleanRadio conProp;
    private RichSelectBooleanRadio reinProp;
    private RichSelectBooleanRadio canProp;
    private RichSelectBooleanRadio enProp;
    private RichInputDate objEffectiveDate;
    private RichSelectBooleanCheckbox jointProp;
    private RichPanelGroupLayout dtls;
    private RichTable propPol;
    private RichInputText payee;
    private RichTable pensTab;
    private RichTable invTab;
    private RichTable unallocTab;
    private RichPanelGroupLayout propAuthDtls;
    private RichShowDetailItem authTabbed;
    private RichTable pendStatuses;
    private RichInputNumberSpinbox staCode;
    private RichInputDate staDate;
    private RichSelectOneChoice staReason;
    private RichSelectOneChoice staStatus;
    private RichInputDate staDateSorted;
    private RichInputNumberSpinbox grossAmt;
    private RichInputNumberSpinbox pensGrossAmt;
    private RichInputNumberSpinbox penAmt;
    private RichInputNumberSpinbox pensTaxAmt;
    private RichInputNumberSpinbox chckCommAmt;
    private RichInputNumberSpinbox pensNetAmt;
    private RichInputNumberSpinbox stampDuty;
    private RichInputNumberSpinbox totMeds;
    private RichInputNumberSpinbox totNetAmt;
    private RichPanelGroupLayout cancDtls;
    private RichOutputText receiptAmount;
    private RichOutputText totalRefundAmount;
    private RichOutputText medAmount;
    private RichOutputText checkCommAmount;
    private RichOutputText totalCommClawback;
    private RichOutputText adminCharge;
    private RichTable lifeAssureds;
    private RichInputText objOccupationDesc;
    private RichInputText lifeAssuredDesc;
    private RichInputText assuredDesc;
    private RichPanelGroupLayout proposalDetails;
    private RichSelectOneChoice jointProposal;
    private RichInputNumberSpinbox monthlyIncome;
    private RichInputDate newEffectiveDate;
    private RichSelectOneChoice payFreqCode;
    private RichInputText branchName;
    private RichInputNumberSpinbox term;
    private RichInputNumberSpinbox lifeRiderFactor;
    private RichInputText jointAssuredDesc;
    private RichInputNumberSpinbox morgageInterest;
    private RichInputNumberSpinbox retirementAge;
    private RichInputNumberSpinbox childAge;
    private RichInputNumberSpinbox contribution;
    private RichInputText proposalNo;
    private RichTable proposalClients;
    private RichTable retages;
    private RichTable prodOptTab;
    private RichInputText popDesc;
    private RichTable branchesTab;
    private RichInputNumberSpinbox brnCode;
    private RichSelectOneChoice lifeRider;
    private RichInputNumberSpinbox assuredPrpCode;
    private RichInputNumberSpinbox lifeAssuredPrpCode;
    private RichInputNumberSpinbox jointAssuredPrpCode;
    private RichInputDate dateSigned;
    private RichInputNumberSpinbox coCode;
    private RichTable clientOccupationsTab;
    private RichInputDate debitDate;
    private RichInputNumberSpinbox bcaCode;
    private RichTable busCategoriesTab;
    private RichInputText bcaDesc;
    private RichInputNumberSpinbox agnCode;
    private RichInputText agentName;
    private RichTable agentsTab;
    private RichInputNumberSpinbox popCode;
    private RichSelectOneChoice pprCoinsurance;
    private RichSelectOneChoice pprCoinsureLeader;
    private RichInputText clientName;
    private RichPanelGroupLayout clientLovDetails;
    private RichTable clientExistingPolsTable;
    private RichPopup polTermPop;
    private RichTable polTermTab;
    private RichTable lifeRates;
    private RichInputNumberSpinbox lsfCode;

    private String prodLifeRider;


    public ProposalManipulation() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    /**
     *Proposal Transaction Type Selected
     * @return
     */
    public String proposalTransSelected() {
        String effectiveDateValue = null;
        if (session.getAttribute("newProposal") == null) {
            session.setAttribute("newProposal", false);
        }
        if (session.getAttribute("editProposal") == null) {
            session.setAttribute("editProposal", false);
        }
        if (session.getAttribute("conProposal") == null) {
            session.setAttribute("conProposal", false);
        }
        if (session.getAttribute("canProposal") == null) {
            session.setAttribute("canProposal", false);
        }

        if (session.getAttribute("reinProposal") == null) {
            session.setAttribute("reinProposal", false);
        }

        /*  if (((Boolean)session.getAttribute("newProposal") == false) &&
            ((Boolean)session.getAttribute("editProposal") == false) &&
            ((Boolean)session.getAttribute("conProposal") == false) &&
            !(reinProp.isSelected()) &&  ((Boolean)session.getAttribute("canProposal") == false) &&
            !(enProp.isSelected())) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectTrans);
            return GlobalCC.failure;
        }*/
        String transCode = null;


        /**
         * Effective Date Required for all transactions Except proposal Enquiry
         */
        if ((Boolean)session.getAttribute("newProposal") == true) {

            // Initialize Effective Date
            effectiveDateValue =
                    GlobalCC.checkNullValues(objEffectiveDate.getValue());

            if (effectiveDateValue == null) {
                GlobalCC.errorValueNotEntered(GlobalCC.enterEffectiveDate);
                return GlobalCC.failure;
            }

            session.setAttribute("effectiveDate",
                                 GlobalCC.parseDate(effectiveDateValue));
        } else {
            if (session.getAttribute("policyProposalCode") == null) {
                GlobalCC.errorValueNotEntered("Error: Select Proposal");
                return null;
            }
        }
        /**
         * If New Business; Select Product
         */
        if ((Boolean)session.getAttribute("newProposal") == true) {
            BigDecimal prodCode =
                (BigDecimal)session.getAttribute("productCode");
            if (prodCode == null) {
                GlobalCC.errorValueNotEntered(GlobalCC.selectProduct);
                return GlobalCC.failure;
            }
            //check if proposal is allowed from underwriting
            String allow = GlobalCC.AllowPropInit(prodCode, "UW");
            if (allow.equalsIgnoreCase("N")) {
                return null;
            }


        } else if ((Boolean)session.getAttribute("editProposal") == true) {


        } else if ((Boolean)session.getAttribute("canProposal") == true) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("propCancel.jspx");
                return null;
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }
        }

        String conMessage = null;
        if ((Boolean)session.getAttribute("conProposal") == true) {
            //UndManipulation con = new UndManipulation();
            //conMessage = con.PolicyConversion();

        }


        if ((Boolean)session.getAttribute("reinProposal") == true) {
            DBConnector datahandler = null;
            datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            if (session.getAttribute("policyProposalCode") == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Proposal");
                return null;
            }

            BigDecimal polCodeVal =
                (BigDecimal)session.getAttribute("policyProposalCode");
            transCode = "PT";

            try {
                String reinQuery =
                    "begin LMS_WEB_PKG.prop_reinstatement(?,?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(reinQuery);
                cst.setBigDecimal(1, polCodeVal);
                cst.setString(2, transCode);
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        /**
         * Redirect to the Underwriting Screen.
         */
        try {
            if (conMessage == null) {
                if ((Boolean)session.getAttribute("newProposal") == true) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("newProposal.jspx");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("propPol.jspx");
                }

            } else {
                GlobalCC.errorValueNotEntered("Proposal Conversion Error");
            }


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return GlobalCC.success;
    }

    public String ProposalCancellation() {
        //String transCode = (String)session.getAttribute("transCode"); //PN
        session.setAttribute("ProcessSubAShtDesc", "PN");

        return null;
    }

    public void propTransValueChanged(ValueChangeEvent valueChangeEvent) {
        if (newProp == null) {

        } else {
            if ((newProp.isSelected()) &&
                (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())) {
                session.setAttribute("newProposal", true);
                session.setAttribute("editProposal", false);
                session.setAttribute("conProposal", false);
                session.setAttribute("canProposal", false);
                session.setAttribute("reinProposal", false);
                session.setAttribute("enProposal", false);
                session.setAttribute("transCode", "NP");
            }
        }

        if ((canProp.isSelected()) &&
            (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())) {
            session.setAttribute("newProposal", false);
            session.setAttribute("editProposal", false);
            session.setAttribute("conProposal", false);
            session.setAttribute("canProposal", true);
            session.setAttribute("reinProposal", false);
            session.setAttribute("enProposal", false);
        }
        if ((reinProp.isSelected()) &&
            (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())) {
            session.setAttribute("newProposal", false);
            session.setAttribute("editProposal", false);
            session.setAttribute("conProposal", false);
            session.setAttribute("canProposal", false);
            session.setAttribute("reinProposal", true);
            session.setAttribute("enProposal", false);
        }
        if ((enProp.isSelected()) &&
            (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue())) {
            session.setAttribute("newProposal", false);
            session.setAttribute("editProposal", false);
            session.setAttribute("conProposal", false);
            session.setAttribute("canProposal", false);
            session.setAttribute("reinProposal", false);
            session.setAttribute("enProposal", true);
        }
        // Add event code here...
    }

    public void termValueChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            DBConnector dbConn = new DBConnector();
            OracleConnection conn = null;
            conn = dbConn.getDatabaseConnection();

            OracleCallableStatement cst = null;
            try {

                String sysQuery =
                    "begin LMS_WEB_PKG_MKT.Get_pol_term(?,?,?,?,?,?,?,?,?,?,?,?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(sysQuery);

                //register out
                cst.setString(1, (String)session.getAttribute("payFreqCode"));
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("productCode"));
                cst.setString(3, null);
                cst.setBigDecimal(4, (BigDecimal)session.getAttribute("anb"));
                cst.setString(5, null);
                cst.setString(6, null);
                cst.registerOutParameter(7, OracleTypes.NUMBER);
                cst.setString(8, evt.getNewValue().toString());
                cst.setString(9, null);
                cst.setString(10, null);
                cst.setString(11, null);
                cst.registerOutParameter(12, OracleTypes.VARCHAR);
                cst.execute();
                lifeRiderFactor.setValue(cst.getString(7));
                String visible = cst.getString(12);
                if (visible != null) {
                    if (visible.equalsIgnoreCase("Y")) {
                        session.setAttribute("lcf", true);
                    } else {
                        session.setAttribute("lcf", false);
                    }
                }
                // lifeFact = cst.getString(3);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }

    public void childAgeChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            BigDecimal childAgeVal =
                GlobalCC.checkBDNullValues(childAge.getValue());
            if (childAgeVal != null) {
                session.setAttribute("childAge", childAgeVal);
            }
            //System.out.println("childAge=" + childAgeVal);
            term.setValue(null);
            GlobalCC.refreshUI(term);
        }
    }

    public String searchClient() {
        String clientNameVal = GlobalCC.checkNullValues(clientName.getValue());
        if (clientNameVal != null) {
            session.setAttribute("Name", clientNameVal);
            ADFUtils.findIterator("findLifeAssuredsIterator").executeQuery();
            GlobalCC.refreshUI(clientLovDetails);
        } else {
            GlobalCC.errorValueNotEntered("Enter Client Surname");
            return null;
        }

        return null;
    }

    public void clientNameChanged(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            String clientNameVal = GlobalCC.checkNullValues(evt.getNewValue());
            session.setAttribute("Name", clientNameVal);
            ADFUtils.findIterator("findLifeAssuredsIterator").executeQuery();
            GlobalCC.refreshUI(clientLovDetails);
        }
    }


    public String newSelectAssured() {
        GlobalCC.showPopup("demoTemplate:clients");
        session.setAttribute("clientOption", "AS");
        return null;
    }

    public String newSelectLifeAssured() {
        GlobalCC.showPopup("demoTemplate:clients");
        session.setAttribute("clientOption", "LA");
        return null;
    }

    public String lifeAssuredSelected() {
        RowKeySet rowKeySet = lifeAssureds.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        lifeAssureds.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)lifeAssureds.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        try {

            if (session.getAttribute("effectiveDate") == null) {
                if (session.getAttribute("ClientCode") == null) {
                    session.setAttribute("anb",
                                         GlobalCC.findAnb((BigDecimal)r.getAttribute("prpCode"),
                                                          "N", null, null,
                                                          session.getAttribute("effDate").toString()));

                } else {
                    session.setAttribute("anb",
                                         GlobalCC.findAnb((BigDecimal)session.getAttribute("ClientCode"),
                                                          "N", null, null,
                                                          (String)session.getAttribute("effDate").toString()));

                }
            } else {

                if (session.getAttribute("ClientCode") == null) {
                    session.setAttribute("anb",
                                         GlobalCC.findAnb((BigDecimal)r.getAttribute("prpCode"),
                                                          "N", null, null,
                                                          (String)session.getAttribute("effectiveDate")));

                } else {
                    session.setAttribute("anb",
                                         GlobalCC.findAnb((BigDecimal)session.getAttribute("ClientCode"),
                                                          "N", null, null,
                                                          (String)session.getAttribute("effectiveDate")));

                }

            }

            String clientOption = (String)session.getAttribute("clientOption");
            if (clientOption.equalsIgnoreCase("AS")) {
                session.setAttribute("assuredClientName",
                                     r.getAttribute("clientName"));
                session.setAttribute("assuredPrpCode",
                                     r.getAttribute("prpCode"));
                session.setAttribute("ClientCode",
                                     r.getAttribute("prpCode"));
                assuredPrpCode.setValue(r.getAttribute("prpCode"));
                assuredDesc.setValue(r.getAttribute("clientName"));
            } else if (clientOption.equalsIgnoreCase("LA")) {
                session.setAttribute("lifeAssuredClientName",
                                     r.getAttribute("clientName"));
                session.setAttribute("lifeAssuredPrpCode",
                                     r.getAttribute("prpCode"));
                lifeAssuredPrpCode.setValue(r.getAttribute("prpCode"));
                lifeAssuredDesc.setValue(r.getAttribute("clientName"));
                coCode.setValue(r.getAttribute("occupationCode"));
                session.setAttribute("occupationCode",
                                     r.getAttribute("occupationCode"));
                objOccupationDesc.setValue(r.getAttribute("occupationalDesc"));
            }

            ADFUtils.findIterator("findClientExistingPoliciesIterator").executeQuery();
            GlobalCC.refreshUI(proposalDetails);


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return null;
    }

    public String launchRates() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:lcf" + "').show(hints);");
        session.setAttribute("term",
                             GlobalCC.checkNullValues(term.getValue()));
        ADFUtils.findIterator("findLifeCoverFactorsIterator").executeQuery();
        GlobalCC.refreshUI(lifeRates);
        return null;
    }

    public String lifeRatesSelected() {
        Object rows = lifeRates.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)rows;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        lifeRiderFactor.setValue(r.getAttribute("lsfRate"));
        lsfCode.setValue(r.getAttribute("lsfCode"));
        session.setAttribute("lsfCode", r.getAttribute("lsfCode"));
        //  session.setAttribute("lsfApplyTo", r.getAttribute("lsfApplyTo"));
        GlobalCC.refreshUI(lifeRiderFactor);
        GlobalCC.refreshUI(lsfCode);
        return null;
    }

    public String viewExistingPolicy() {
        try {
            JUCtrlValueBinding row =
                (JUCtrlValueBinding)clientExistingPolsTable.getRowData();
            BigDecimal polCode = (BigDecimal)row.getAttribute("polCode");
            session.setAttribute("policyProposalCode", polCode);
            session.setAttribute("enquiryMode", true);
            session.setAttribute("transCode", "PE");
            session.setAttribute("module", "UNDER");
            session.setAttribute("productType",
                                 row.getAttribute("productType"));
            String lifeRider = GlobalCC.findGrpLifeRider(polCode);
            session.setAttribute("grpLifeRider", lifeRider);
            if (row.getAttribute("policyNumber") != null) {
                session.setAttribute("endorse", false);
                session.setAttribute("process", "UPL");
            } else {
                session.setAttribute("enProposal", true);
                session.setAttribute("process", "UPR");
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("propPol.jspx");

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return null;
    }

    public String newSelectClientOccupation() {
        GlobalCC.showPopup("demoTemplate:occupations");
        return null;
    }

    public String selectClientOccupation() {
        RowKeySet rowKeySet = clientOccupationsTab.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        clientOccupationsTab.setRowKey(key2);
        JUCtrlValueBinding r =
            (JUCtrlValueBinding)clientOccupationsTab.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("occupationDesc",
                             r.getAttribute("occupationalDesc"));
        session.setAttribute("occupationCode",
                             r.getAttribute("occupationCode"));
        objOccupationDesc.setValue(r.getAttribute("occupationalDesc"));
        coCode.setValue(r.getAttribute("occupationCode"));
        GlobalCC.refreshUI(objOccupationDesc);
        GlobalCC.refreshUI(coCode);
        return null;
    }

    public void jointPropChange(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            session.setAttribute("yesNoValues",
                                 GlobalCC.checkNullValues(jointProposal.getValue()));
        }
    }

    public String newSelectjointLifeAssured() {
        GlobalCC.showPopup("demoTemplate:jointLifeAssured");
        return null;
    }

    public String jointAssuredSelected() {
        RowKeySet rowKeySet = proposalClients.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        proposalClients.setRowKey(key2);
        JUCtrlValueBinding r =
            (JUCtrlValueBinding)proposalClients.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        session.setAttribute("joinAssuredClientName",
                             r.getAttribute("clientName"));
        session.setAttribute("jointAssuredprpCode", r.getAttribute("prpCode"));
        jointAssuredPrpCode.setValue(r.getAttribute("prpCode"));
        jointAssuredDesc.setValue(r.getAttribute("clientName"));
        GlobalCC.refreshUI(jointAssuredDesc);
        GlobalCC.refreshUI(jointAssuredPrpCode);

        return null;
    }

    public String newSelectRetireAge() {
        if (session.getAttribute("assuredPrpCode") == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectlifeAssured);
            return GlobalCC.failure;
        }
        GlobalCC.showPopup("demoTemplate:retAge");
        ADFUtils.findIterator("findRetireAgesIterator").executeQuery();
        GlobalCC.refreshUI(retages);
        return null;
    }

    public String retAgeSelected() {
        RowKeySet rowKeySet = retages.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        retages.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)retages.getRowData();
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        retirementAge.setValue(r.getAttribute("retAge"));
        DBConnector dbConn = new DBConnector();
        OracleConnection conn = null;
        conn = dbConn.getDatabaseConnection();

        OracleCallableStatement cst = null;
        try {

            String sysQuery =
                "begin LMS_WEB_PKG_MKT.Get_pol_term(?,?,?,?,?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(sysQuery);

            //register out
            cst.setString(1, (String)session.getAttribute("payFreqCode"));
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("productCode"));
            cst.setString(3, null);
            cst.setBigDecimal(4, (BigDecimal)session.getAttribute("anb"));
            cst.setBigDecimal(5, (BigDecimal)r.getAttribute("retAge"));
            cst.setString(6, null);
            cst.setString(7,
                          GlobalCC.checkNullValues(lifeRiderFactor.getValue()));
            cst.registerOutParameter(8, OracleTypes.NUMBER);
            cst.setString(9, null);
            cst.setString(10, null);
            cst.setString(11, null);
            cst.registerOutParameter(12, OracleTypes.VARCHAR);
            cst.execute();
            term.setValue(cst.getString(8));
            String visible = cst.getString(12);
            if (visible != null) {
                if (visible.equalsIgnoreCase("Y")) {
                    session.setAttribute("lcf", true);
                } else {
                    session.setAttribute("lcf", false);
                }
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String newSelectProdOption() {
        if (session.getAttribute("productCode") == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Product");
            return null;
        }
        GlobalCC.showPopup("demoTemplate:prodOptpop");
        return null;
    }

    public String selectProdOption() {

        RowKeySet rowKeySet = prodOptTab.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        prodOptTab.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)prodOptTab.getRowData();
        session.setAttribute("popCode", r.getAttribute("popCode"));
        popCode.setValue(r.getAttribute("popCode"));
        popDesc.setValue(r.getAttribute("popDesc"));
        GlobalCC.refreshUI(popDesc);
        GlobalCC.refreshUI(popCode);
        return null;
    }

    public String newSelectBranch() {
        GlobalCC.showPopup("demoTemplate:branches");
        return null;
    }

    public String selectBranch() {
        RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        branchesTab.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)branchesTab.getRowData();
        session.setAttribute("sysBranchCode", r.getAttribute("brnCode"));
        brnCode.setValue(r.getAttribute("brnCode"));
        branchName.setValue(r.getAttribute("brnName"));
        GlobalCC.refreshUI(branchName);
        GlobalCC.refreshUI(brnCode);
        ADFUtils.findIterator("findAgentsIterator").executeQuery();
        GlobalCC.refreshUI(agentsTab);
        return null;
    }

    public String newSelectAgent() {
        if (session.getAttribute("sysBranchCode") == null) {
            GlobalCC.errorValueNotEntered("Select Branch to proceed");
            return null;
        }
        GlobalCC.showPopup("demoTemplate:agents");
        return null;
    }

    public String selectAgent() {
        RowKeySet rowKeySet = agentsTab.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        agentsTab.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)agentsTab.getRowData();
        session.setAttribute("agentCode", r.getAttribute("agentCode"));
        agnCode.setValue(r.getAttribute("agentCode"));
        agentName.setValue(r.getAttribute("agentName"));
        GlobalCC.refreshUI(agnCode);
        GlobalCC.refreshUI(agentName);
        return null;
    }

    public String newSelectBusinessCategory() {
        GlobalCC.showPopup("demoTemplate:buscategory");
        return null;
    }

    public String selectBusinessCategory() {
        RowKeySet rowKeySet = busCategoriesTab.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        busCategoriesTab.setRowKey(key2);
        JUCtrlValueBinding r =
            (JUCtrlValueBinding)busCategoriesTab.getRowData();
        //  session.setAttribute("bcaCode", r.getAttribute("bcaCode"));
        bcaCode.setValue(r.getAttribute("bcaCode"));
        bcaDesc.setValue(r.getAttribute("bcaDesc"));
        GlobalCC.refreshUI(bcaCode);
        GlobalCC.refreshUI(bcaDesc);
        return null;
    }

    public String generateProposalNo() {
        String success = "N";
        String brnCodeVal = GlobalCC.checkNullValues(brnCode.getValue());
        BigDecimal prodCodeVal =
            (BigDecimal)session.getAttribute("productCode");
        if (brnCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Branch");
            return null;
        }
        if (prodCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: product");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        String query = "begin LMS_WEB_PKG_MKT.Create_Uw_Prop_No(?,?,?); end;";
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, brnCodeVal);
            cst.setBigDecimal(3, prodCodeVal);
            cst.execute();
            if (cst.getString(1) == null) {
                GlobalCC.errorValueNotEntered("Error: No Proposal No Generated");
                return null;
            }
            proposalNo.setValue(cst.getString(1));
            session.setAttribute("proposalNumber", cst.getString(1));
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.refreshUI(proposalNo);
            GlobalCC.sysInformation("Proposal Number Generated Successfully");
            success = "Y";
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String updateNewProposal() {
        String retire = null;
        String morgage = null;
        String monthly = null;
        String lifeRiderVal = null;
        String lifeFactor = null;
        String polterm = null;
        String jointProp = GlobalCC.checkNullValues(jointProposal.getValue());
        String effectiveDate = null;
        BigDecimal childAgeVal = null;
        String assuredPrpCodeVal = null;
        String lifeAssuredPrpCodeVal = null;
        String jointAssuredPrpCodeVal = null;
        String pprCoinsuranceVal = null;
        String pprCoinsureLeaderVal = null;


        retire = GlobalCC.checkNullValues(retirementAge.getValue());
        morgage = GlobalCC.checkNullValues(morgageInterest.getValue());
        monthly = GlobalCC.checkNullValues(monthlyIncome.getValue());
        lifeRiderVal = GlobalCC.checkNullValues(lifeRider.getValue());
        lifeFactor = GlobalCC.checkNullValues(lifeRiderFactor.getValue());
        polterm = GlobalCC.checkNullValues(term.getValue());
        childAgeVal = GlobalCC.checkBDNullValues(childAge.getValue());
        effectiveDate = GlobalCC.checkNullValues(newEffectiveDate.getValue());
        String proposalNoVal = GlobalCC.checkNullValues(proposalNo.getValue());
        BigDecimal productCodeVal =
            (BigDecimal)session.getAttribute("productCode");
        assuredPrpCodeVal =
                GlobalCC.checkNullValues(assuredPrpCode.getValue());
        lifeAssuredPrpCodeVal =
                GlobalCC.checkNullValues(lifeAssuredPrpCode.getValue());
        jointAssuredPrpCodeVal =
                GlobalCC.checkNullValues(jointAssuredPrpCode.getValue());
        String effectiveDateVal =
            GlobalCC.checkNullValues(newEffectiveDate.getValue());
        String brnCodeVal = GlobalCC.checkNullValues(brnCode.getValue());
        String dateSignedVal = GlobalCC.checkNullValues(dateSigned.getValue());
        String coCodeVal = GlobalCC.checkNullValues(coCode.getValue());
        coCodeVal =
                (coCodeVal == null) ? GlobalCC.checkNullValues(session.getAttribute("occupationCode")) :
                coCodeVal;

        String debitDateVal = GlobalCC.checkNullValues(debitDate.getValue());
        String bcaCodeVal = GlobalCC.checkNullValues(bcaCode.getValue());
        String agnCodeVal = GlobalCC.checkNullValues(agnCode.getValue());
        String popCodeVal = GlobalCC.checkNullValues(popCode.getValue());
        String contributionVal =
            GlobalCC.checkNullValues(contribution.getValue());
        BigDecimal anbVal = (BigDecimal)session.getAttribute("anb");
        String payFreqCode = (String)session.getAttribute("payFreqCode");
        pprCoinsuranceVal =
                GlobalCC.checkNullValues(pprCoinsurance.getValue());
        pprCoinsureLeaderVal =
                GlobalCC.checkNullValues(pprCoinsureLeader.getValue());
        String lsfCodeVal =
            GlobalCC.checkNullValues(session.getAttribute("lsfCode"));
        if (pprCoinsuranceVal != null) {
            if (pprCoinsuranceVal.equalsIgnoreCase("Y")) {
                if (pprCoinsureLeaderVal == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Coinsure Leader?");
                    return null;
                }
            } else {
                pprCoinsureLeaderVal = null;
            }
        } else {
            pprCoinsureLeaderVal = null;
        }
        if (payFreqCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Frequency Of Payment");
            return null;
        }
        if (debitDateVal != null) {
            if (debitDateVal.contains(":")) {
                debitDateVal = GlobalCC.parseDate(debitDateVal);
            } else {
                debitDateVal = GlobalCC.upDateParseDate(debitDateVal);
            }
        }

        if (dateSignedVal != null) {
            if (dateSignedVal.contains(":")) {
                dateSignedVal = GlobalCC.parseDate(dateSignedVal);
            } else {
                dateSignedVal = GlobalCC.upDateParseDate(dateSignedVal);
            }
        }
        if (effectiveDateVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Effective Date");
            return null;
        } else {
            if (effectiveDateVal.contains(":")) {
                effectiveDateVal = GlobalCC.parseDate(effectiveDateVal);
            }
        }

        if (session.getAttribute("assuredPrpCode") == null) {
            GlobalCC.errorValueNotEntered(GlobalCC.selectlifeAssured);
            return GlobalCC.failure;
        }
        if (proposalNoVal == null) {
            GlobalCC.errorValueNotEntered("Error: Generate Proposal No to proceed");
            return null;
        }

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        String query =
            "begin LMS_WEB_PKG_MKT.Update_policy(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.NUMBER);
            cst.registerOutParameter(2, OracleTypes.NUMBER);
            cst.setString(3, proposalNoVal);
            cst.registerOutParameter(4, OracleTypes.NUMBER);
            cst.setBigDecimal(5, productCodeVal);
            cst.setString(6, assuredPrpCodeVal);
            cst.setString(7, lifeAssuredPrpCodeVal);
            cst.setString(8, jointAssuredPrpCodeVal);
            cst.setString(9, effectiveDateVal);
            cst.setString(10, effectiveDateVal);
            cst.setString(11, payFreqCode);
            cst.setString(12, polterm);
            cst.setString(13, brnCodeVal);
            cst.setString(14, null); //maturity date
            cst.setString(15, jointProp);
            cst.setBigDecimal(16, anbVal);
            cst.setString(17, monthly);
            cst.setString(18, dateSignedVal);
            cst.setString(19, coCodeVal);
            cst.setString(20, morgage);
            cst.setString(21, lifeRiderVal);
            cst.setString(22, lifeFactor);
            cst.setString(23, debitDateVal);
            cst.setString(24, bcaCodeVal);
            cst.setString(25, agnCodeVal);
            cst.setString(26, popCodeVal);
            cst.setString(27, contributionVal);
            cst.setString(28, retire);
            cst.registerOutParameter(29, OracleTypes.NUMBER);
            cst.setString(30, pprCoinsuranceVal);
            cst.setString(31, pprCoinsureLeaderVal);
            cst.setString(32, lsfCodeVal);
            cst.execute();

            BigDecimal polCode = cst.getBigDecimal(1);
            BigDecimal transNo = cst.getBigDecimal(2);
            BigDecimal endorseCode = cst.getBigDecimal(29);
            if (polCode == null || transNo == null || endorseCode == null) {
                GlobalCC.errorValueNotEntered("Error Updating Proposal");
                return null;
            }
            session.setAttribute("policyProposalCode", polCode);
            session.setAttribute("polCode", polCode);
            session.setAttribute("transactionNumber", transNo);
            session.setAttribute("transNo", transNo);
            session.setAttribute("endorsementCode", endorseCode);
            session.setAttribute("endorseType", "NB");
            cst.close();
            conn.commit();
            conn.close();

            FacesContext.getCurrentInstance().getExternalContext().redirect("propPol.jspx");

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return GlobalCC.success;

    }

    public String cancelNewProposal() {
        try {
            session.removeAttribute("productCode");
            session.removeAttribute("effectiveDate");
            FacesContext.getCurrentInstance().getExternalContext().redirect("proposalTrans.jspx");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        return null;
    }

    public String processPropCancellation() {
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        BigDecimal transNoVal = (BigDecimal)session.getAttribute("transNo");

        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Proposal Code");
            return null;
        }
        if (transNoVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Transaction No");
            return null;
        }
        String success = "N";
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        String cancelProp =
            "begin LMS_WEB_PKG.processpropcanc(?,?,?,?,?,?,?); end;";
        try {
            cst = (OracleCallableStatement)conn.prepareCall(cancelProp);
            cst.setBigDecimal(1, polCodeVal);
            cst.registerOutParameter(2, OracleTypes.NUMBER);
            cst.registerOutParameter(3, OracleTypes.NUMBER);
            cst.registerOutParameter(4, OracleTypes.NUMBER);
            cst.registerOutParameter(5, OracleTypes.NUMBER);
            cst.registerOutParameter(6, OracleTypes.NUMBER);
            cst.registerOutParameter(7, OracleTypes.NUMBER);
            cst.execute();
            totalRefundAmount.setValue(cst.getBigDecimal(2));
            receiptAmount.setValue(cst.getBigDecimal(3));
            medAmount.setValue(cst.getBigDecimal(4));
            checkCommAmount.setValue(cst.getBigDecimal(5));
            totalCommClawback.setValue(cst.getBigDecimal(6));
            adminCharge.setValue(cst.getBigDecimal(7));
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findPropPolIterator").executeQuery();
            GlobalCC.refreshUI(dtls);
            GlobalCC.refreshUI(propAuthDtls);
            success = "Y";
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return success;
    }


    public String cancelProposal() {
        String success = processPropCancellation();
        if (success.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered("Canncellation Not Process Successfully!! Retry");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        BigDecimal transNoVal = (BigDecimal)session.getAttribute("transNo");
        String receiptAmountVal = null;
        String totalRefundAmountVal = null;
        String medAmountVal = null;
        String checkCommAmountVal = null;
        String totalCommClawbackVal = null;
        String adminChargeVal = null;
        String launchVal = "Y";

        receiptAmountVal = GlobalCC.checkNullValues(receiptAmount.getValue());
        totalRefundAmountVal =
                GlobalCC.checkNullValues(totalRefundAmount.getValue());
        medAmountVal = GlobalCC.checkNullValues(medAmount.getValue());
        checkCommAmountVal =
                GlobalCC.checkNullValues(checkCommAmount.getValue());
        totalCommClawbackVal =
                GlobalCC.checkNullValues(totalCommClawback.getValue());
        adminChargeVal = GlobalCC.checkNullValues(adminCharge.getValue());

        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Proposal Code");
            return null;
        }
        if (transNoVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Transaction No");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;

        String cancelProp =
            "begin LMS_WEB_PKG.Authproposalcanc(?,?,?,?,?,?,?); end;";
        try {
            cst = (OracleCallableStatement)conn.prepareCall(cancelProp);
            cst.setBigDecimal(1, polCodeVal);
            cst.setBigDecimal(2, transNoVal);
            cst.setString(3, totalRefundAmountVal);
            cst.setString(4, receiptAmountVal);
            cst.setString(5, medAmountVal);
            cst.setString(6, totalCommClawbackVal);
            cst.setString(7, launchVal);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Proposal Cancellation Successful");
            ADFUtils.findIterator("findPropPolIterator").executeQuery();
            GlobalCC.refreshUI(dtls);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String launchTerm() {
        ADFUtils.findIterator("findPolTermsIterator").executeQuery();
        GlobalCC.refreshUI(polTermPop);
        GlobalCC.showPopup("demoTemplate:polTermPop");
        return null;
    }

    public String selectPolTerm() {
        Object value = polTermTab.getSelectedRowData();
        if (value == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        JUCtrlValueBinding r = (JUCtrlValueBinding)value;

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        term.setValue(r.getAttribute("polTerm"));
        GlobalCC.hidePopup("demoTemplate:polTermPop");
        GlobalCC.refreshUI(term);
        return null;
    }

    public void fetchCancDetails(ItemEvent evt) {
        try {
            RichShowDetailItem comp = (RichShowDetailItem)evt.getComponent();
            if (comp.isDisclosed()) {
                processPropCancellation();
                // ADFUtils.findIterator("findPropCancDtlsIterator").executeQuery();
                GlobalCC.refreshUI(propAuthDtls); //refresh controls
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }


    public void updateRemarks(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            BigDecimal endorseCode =
                (BigDecimal)session.getAttribute("endorsementCode");
            if (endorseCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Endorsement Code");
            } else {

                DBConnector dbConnector = new DBConnector();
                OracleConnection conn;
                conn = dbConnector.getDatabaseConnection();
                OracleCallableStatement lgcallStmt = null;
                String propCancel =
                    "begin LMS_WEB_PKG.UpdateEndrDtls(?,?,?); end;";
                try {
                    lgcallStmt =
                            (OracleCallableStatement)conn.prepareCall(propCancel);
                    lgcallStmt.setBigDecimal(1, endorseCode);
                    lgcallStmt.setString(2, null);
                    lgcallStmt.setString(3, (String)evt.getNewValue());
                    lgcallStmt.execute();
                    lgcallStmt.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
    }

    public void updatePayee(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            BigDecimal endorseCode =
                (BigDecimal)session.getAttribute("endorsementCode");
            if (endorseCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Endorsement Code");
            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn;
                conn = dbConnector.getDatabaseConnection();
                OracleCallableStatement lgcallStmt = null;
                String query = "begin LMS_WEB_PKG.UpdateEndrDtls(?,?,?); end;";
                try {
                    lgcallStmt =
                            (OracleCallableStatement)conn.prepareCall(query);
                    lgcallStmt.setBigDecimal(1, endorseCode);
                    lgcallStmt.setString(2, (String)evt.getNewValue());
                    lgcallStmt.setString(3, null);
                    lgcallStmt.execute();
                    lgcallStmt.close();
                    conn.close();
                    // System.out.print("endorseCode:" + endorseCode);
                    // System.out.print("New Value:" + evt.getNewValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }

        }
    }

    public void updateProposalRefundAmount(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue()) {
            RichInputNumberSpinbox myComp =
                (RichInputNumberSpinbox)evt.getComponent();
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            OracleCallableStatement lgcallStmt = null;

            try {
                String propCancel =
                    "begin LMS_WEB_PKG.UpdatePropRefundAmt(?,?); end;";
                lgcallStmt =
                        (OracleCallableStatement)conn.prepareCall(propCancel);
                lgcallStmt.setString(1, myComp.getLabel());
                lgcallStmt.setBigDecimal(2, (BigDecimal)evt.getNewValue());
                lgcallStmt.execute();
                lgcallStmt.close();
                conn.commit();
                conn.close();


            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }


    public String processProposalRefund() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String flag = "N";
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }

        try {
            String query =
                "begin LMS_WEB_PKG.ProcessProposalRefund(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(2);

            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Proposal Refund Processed Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }


    public String authorizeProposalRefund() {
        String userName = (String)session.getAttribute("Username");
        String process = (String)session.getAttribute("ProcessShtDesc");
        String processArea =
            (String)session.getAttribute("ProcessAreaShtDesc");
        String processSubArea = "PROPRAUTH";
        BigDecimal amount = (BigDecimal)session.getAttribute("sumAssured");
        String drCr = (String)session.getAttribute("debitCredit");
        String rights =
            GlobalCC.checkUserRights(userName, process, processArea,
                                     processSubArea, amount, drCr);
        rights = (rights == null) ? "N" : rights;
        if (rights.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        String flag = "N";
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        try {
            String query = "begin LMS_WEB_PKG.AuthProposalRefund(?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, null);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(3);
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Proposal Refund Authorized Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }


    public void updateRctRefndAmt(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            RichInputNumberSpinbox myComp =
                (RichInputNumberSpinbox)valueChangeEvent.getComponent();
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            OracleCallableStatement lgcallStmt = null;
            String propCancel =
                "begin LMS_WEB_PKG.UpdateRecptRefundAmt(?,?); end;";
            try {
                lgcallStmt =
                        (OracleCallableStatement)conn.prepareCall(propCancel);
                //bind the variables

                lgcallStmt.setString(1, myComp.getLabel());
                lgcallStmt.setString(2,
                                     valueChangeEvent.getNewValue().toString());
                lgcallStmt.execute();
                lgcallStmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }

    public String processPolicyRefund() {
        String flag = "N";
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {
            String query = "begin LMS_WEB_PKG.ProcessPolicyRefund(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(2);
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Processed Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String AuthPolicyRefund() {
        String userName = (String)session.getAttribute("Username");
        String process = (String)session.getAttribute("ProcessShtDesc");
        String processArea =
            (String)session.getAttribute("ProcessAreaShtDesc");
        String processSubArea = "PREMRAUTH";
        BigDecimal amount = (BigDecimal)session.getAttribute("sumAssured");
        String drCr = (String)session.getAttribute("debitCredit");
        String rights =
            GlobalCC.checkUserRights(userName, process, processArea,
                                     processSubArea, amount, drCr);
        rights = (rights == null) ? "N" : rights;
        if (rights.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
            return null;
        }
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        String flag = processPolicyRefund();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query = "begin LMS_WEB_PKG.AuthPolicyRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.setString(4, flag);
            cst.execute();
            cst.close();
            conn.close();

            GlobalCC.sysInformation("Authorization Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String processPensRefund() {
        String flag = "N";
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query =
                "begin LMS_WEB_PKG.ProcessPensionRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(4);
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Refund Processed Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String AuthPensRefund() {
        String userName = (String)session.getAttribute("Username");
        String process = (String)session.getAttribute("ProcessShtDesc");
        String processArea =
            (String)session.getAttribute("ProcessAreaShtDesc");
        String processSubArea = "PENSRAUTH";
        BigDecimal amount = (BigDecimal)session.getAttribute("sumAssured");
        String drCr = (String)session.getAttribute("debitCredit");
        String rights =
            GlobalCC.checkUserRights(userName, process, processArea,
                                     processSubArea, amount, drCr);
        rights = (rights == null) ? "N" : rights;
        if (rights.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
            return null;
        }
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        String flag = processPensRefund();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {
            String query =
                "begin LMS_WEB_PKG.AuthPensionRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.setString(4, flag);
            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Authorization Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String processInvRefund() {
        String flag = "N";
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query = "begin LMS_WEB_PKG.ProcessInvRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(4);
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Refund Processed Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String AuthInvRefund() {
        String userName = (String)session.getAttribute("Username");
        String process = (String)session.getAttribute("ProcessShtDesc");
        String processArea =
            (String)session.getAttribute("ProcessAreaShtDesc");
        String processSubArea = "INVRAUTH";
        BigDecimal amount = (BigDecimal)session.getAttribute("sumAssured");
        String drCr = (String)session.getAttribute("debitCredit");
        String rights =
            GlobalCC.checkUserRights(userName, process, processArea,
                                     processSubArea, amount, drCr);
        rights = (rights == null) ? "N" : rights;
        if (rights.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered(GlobalCC.taskRights);
            return null;
        }
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }

        String flag = processInvRefund();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query = "begin LMS_WEB_PKG.AuthInvRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.setString(4, flag);
            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Authorization Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String processUnallocRefund() {
        String flag = "N";
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query =
                "begin LMS_WEB_PKG.ProcessUnallocatedRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            flag = cst.getString(4);
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Unallocated Refund Process Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public String AuthUnallocRefund() {
        String payeeVal = GlobalCC.checkNullValues(payee.getValue());
        if (payeeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Payee");
            return null;
        }
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        if (polCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Policy Code");
            return null;
        }
        String flag = processUnallocRefund();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;

        try {
            String query =
                "begin LMS_WEB_PKG.AuthUnallocatedRefund(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, polCodeVal);
            cst.setString(2, payeeVal);
            cst.setString(3, null);
            cst.setString(4, flag);
            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.sysInformation("Unallocation Refund Authorization Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return flag;
    }

    public void setDtls(RichPanelGroupLayout dtls) {
        this.dtls = dtls;
    }

    public RichPanelGroupLayout getDtls() {
        return dtls;
    }

    public void setPropPol(RichTable propPol) {
        this.propPol = propPol;
    }

    public RichTable getPropPol() {
        return propPol;
    }

    public void setPayee(RichInputText payee) {
        this.payee = payee;
    }

    public RichInputText getPayee() {
        return payee;
    }

    public void setPensTab(RichTable pensTab) {
        this.pensTab = pensTab;
    }

    public RichTable getPensTab() {
        return pensTab;
    }

    public void setInvTab(RichTable invTab) {
        this.invTab = invTab;
    }

    public RichTable getInvTab() {
        return invTab;
    }

    public void setUnallocTab(RichTable unallocTab) {
        this.unallocTab = unallocTab;
    }

    public RichTable getUnallocTab() {
        return unallocTab;
    }

    public String processCancel() {
        BigDecimal polCodeVal =
            (BigDecimal)session.getAttribute("policyProposalCode");
        BigDecimal endorseCodeVal =
            (BigDecimal)session.getAttribute("endorsementCode");
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        String computed = "N";


        String cancelProp =
            "begin LMS_WEB_PKG.PolCancellationVars(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try {
            cst = (OracleCallableStatement)conn.prepareCall(cancelProp);
            cst.setBigDecimal(1, polCodeVal);
            cst.setBigDecimal(2, polCodeVal);
            cst.registerOutParameter(3, OracleTypes.NUMBER);
            cst.registerOutParameter(4, OracleTypes.NUMBER);
            cst.registerOutParameter(5, OracleTypes.NUMBER);
            cst.registerOutParameter(6, OracleTypes.NUMBER);
            cst.registerOutParameter(7, OracleTypes.NUMBER);
            cst.registerOutParameter(8, OracleTypes.NUMBER);
            cst.registerOutParameter(9, OracleTypes.NUMBER);
            cst.registerOutParameter(10, OracleTypes.NUMBER);
            cst.registerOutParameter(11, OracleTypes.NUMBER);
            cst.registerOutParameter(12, OracleTypes.NUMBER);
            cst.registerOutParameter(13, OracleTypes.NUMBER);
            cst.registerOutParameter(14, OracleTypes.NUMBER);
            cst.registerOutParameter(15, OracleTypes.NUMBER);
            cst.registerOutParameter(16, OracleTypes.NUMBER);
            cst.registerOutParameter(17, OracleTypes.VARCHAR);
            cst.registerOutParameter(18, OracleTypes.NUMBER);
            cst.registerOutParameter(19, OracleTypes.NUMBER);
            cst.registerOutParameter(20, OracleTypes.NUMBER);
            cst.execute();
            computed = cst.getString(17);
            if (grossAmt != null) {
                grossAmt.setValue(cst.getBigDecimal(3));
                pensGrossAmt.setValue(cst.getBigDecimal(4));
                penAmt.setValue(cst.getBigDecimal(7));
                pensTaxAmt.setValue(cst.getBigDecimal(9));
                pensNetAmt.setValue(cst.getBigDecimal(10));
                chckCommAmt.setValue(cst.getBigDecimal(11));
                stampDuty.setValue(cst.getBigDecimal(12));
                totMeds.setValue(cst.getBigDecimal(13));
                totNetAmt.setValue(cst.getBigDecimal(15));
                GlobalCC.refreshUI(cancDtls);
            }
            cst.close();
            conn.close();

            GlobalCC.sysInformation("Processing Successful");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return computed;
    }

    public String authCancel() {
        String computed = processCancel();
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;

        String cancelProp =
            "begin LMS_WEB_PKG.AuthPolicyCancellation(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try {
            cst = (OracleCallableStatement)conn.prepareCall(cancelProp);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("endorsementCode"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("transNo"));
            cst.registerOutParameter(3, OracleTypes.NUMBER);
            cst.registerOutParameter(4, OracleTypes.NUMBER);
            cst.registerOutParameter(5, OracleTypes.NUMBER);
            cst.registerOutParameter(6, OracleTypes.NUMBER);
            cst.registerOutParameter(7, OracleTypes.NUMBER);
            cst.registerOutParameter(8, OracleTypes.NUMBER);
            cst.registerOutParameter(9, OracleTypes.NUMBER);
            cst.registerOutParameter(10, OracleTypes.NUMBER);
            cst.registerOutParameter(11, OracleTypes.NUMBER);
            cst.registerOutParameter(12, OracleTypes.NUMBER);
            cst.registerOutParameter(13, OracleTypes.NUMBER);
            cst.registerOutParameter(14, OracleTypes.NUMBER);
            cst.setString(15, computed);
            cst.registerOutParameter(16, OracleTypes.VARCHAR);
            cst.execute();
            if (grossAmt != null) {
                grossAmt.setValue(cst.getBigDecimal(3));
                pensGrossAmt.setValue(cst.getBigDecimal(4));
                penAmt.setValue(cst.getBigDecimal(7));
                pensTaxAmt.setValue(cst.getBigDecimal(9));
                pensNetAmt.setValue(cst.getBigDecimal(10));
                chckCommAmt.setValue(cst.getBigDecimal(11));
                stampDuty.setValue(cst.getBigDecimal(12));
                totMeds.setValue(cst.getBigDecimal(13));
                totNetAmt.setValue(cst.getBigDecimal(14));
                GlobalCC.refreshUI(cancDtls);
            }
            cst.close();
            conn.close();
            session.setAttribute("authorised", true);
            GlobalCC.sysInformation("Authorization Successful");
            ADFUtils.findIterator("findPropPolIterator").executeQuery();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String launchPendStatus() {
        GlobalCC.showPopup("demoTemplate:pendStatus");
        return null;
    }

    public String newStatus() {
        staCode.setValue(null);
        staDate.setValue(null);
        staStatus.setValue("P");
        staDateSorted.setValue(null);
        GlobalCC.showPopup("demoTemplate:status");
        return null;
    }

    public String editStatus() {
        RowKeySet rowKeySet = pendStatuses.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        pendStatuses.setRowKey(key2);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)pendStatuses.getRowData();
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        staCode.setValue(nodeBinding.getAttribute("prpsCode"));
        staDate.setValue(nodeBinding.getAttribute("prpsDate"));
        staStatus.setValue(nodeBinding.getAttribute("prpsStatus"));
        staDateSorted.setValue(nodeBinding.getAttribute("prpsDateSorted"));
        RowSet myRowSet;
        myRowSet =
                (RowSet)ADFUtils.findIterator("findPendingStatusIterator").findRowsByAttributeValue("pgsDesc",
                                                                                                    true,
                                                                                                    nodeBinding.getAttribute("pendingReason"));

        Row row = myRowSet.next();

        // while(myRowSet.next()){
        String keyValue = null;
        keyValue = row.getKey().toString();
        keyValue = keyValue.replace("oracle.jbo.Key[", "");
        keyValue = keyValue.replace("]", "");
        keyValue = keyValue.replaceAll(" ", "");
        staReason.setValue(Integer.parseInt(keyValue));
        ADFUtils.findIterator("findPendingStatusIterator").setCurrentRowWithKeyValue(keyValue);
        GlobalCC.showPopup("demoTemplate:status");
        return null;
    }

    public String saveStatus() {
        String idVal = null;
        String dateVal = null;
        String statusVal = null;
        String dateSortVal = null;
        String reasonVal = null;
        idVal = GlobalCC.checkNullValues(staCode.getValue());
        dateVal = GlobalCC.checkNullValues(staDate.getValue());
        statusVal = GlobalCC.checkNullValues(staStatus.getValue());
        dateSortVal = GlobalCC.checkNullValues(staDateSorted.getValue());
        reasonVal = GlobalCC.checkNullValues(staReason.getValue());

        if (reasonVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Reason");
            return null;
        }
        if (statusVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Status");
            return null;
        }
        if (dateVal != null) {
            if (dateVal.contains(":")) {
                dateVal = GlobalCC.parseDate(dateVal);
            } else {
                dateVal = GlobalCC.upDateParseDate(dateVal);
            }
        }
        if (dateSortVal != null) {
            if (dateVal.contains(":")) {
                dateSortVal = GlobalCC.parseDate(dateSortVal);
            } else {
                dateSortVal = GlobalCC.upDateParseDate(dateSortVal);
            }
        }

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;

        try {
            String reinQuery =
                "begin LMS_WEB_PKG.UpdatePendingStatuses(?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(reinQuery);
            cst.setString(1, idVal);
            cst.setString(2, statusVal);
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("policyProposalCode"));
            Row row =
                ADFUtils.findIterator("findPendingStatusIterator").getRowAtRangeIndex(new Integer(reasonVal));
            cst.setBigDecimal(4, (BigDecimal)row.getAttribute("pgsCode"));
            cst.execute();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findPropPendingStatusIterator").executeQuery();
            GlobalCC.refreshUI(pendStatuses);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void fetchCancDetails(DisclosureEvent disclosureEvent) {
        try {
            if (authTabbed.isDisclosed()) {
                processPropCancellation();
                // ADFUtils.findIterator("findPropCancDtlsIterator").executeQuery();
                GlobalCC.refreshUI(propAuthDtls); //refresh controls
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }


    public void setGrossAmt(RichInputNumberSpinbox grossAmt) {
        this.grossAmt = grossAmt;
    }

    public RichInputNumberSpinbox getGrossAmt() {
        return grossAmt;
    }

    public void setPensGrossAmt(RichInputNumberSpinbox pensGrossAmt) {
        this.pensGrossAmt = pensGrossAmt;
    }

    public RichInputNumberSpinbox getPensGrossAmt() {
        return pensGrossAmt;
    }

    public void setPenAmt(RichInputNumberSpinbox penAmt) {
        this.penAmt = penAmt;
    }

    public RichInputNumberSpinbox getPenAmt() {
        return penAmt;
    }

    public void setPensTaxAmt(RichInputNumberSpinbox pensTaxAmt) {
        this.pensTaxAmt = pensTaxAmt;
    }

    public RichInputNumberSpinbox getPensTaxAmt() {
        return pensTaxAmt;
    }

    public void setChckCommAmt(RichInputNumberSpinbox chckCommAmt) {
        this.chckCommAmt = chckCommAmt;
    }

    public RichInputNumberSpinbox getChckCommAmt() {
        return chckCommAmt;
    }

    public void setPensNetAmt(RichInputNumberSpinbox pensNetAmt) {
        this.pensNetAmt = pensNetAmt;
    }

    public RichInputNumberSpinbox getPensNetAmt() {
        return pensNetAmt;
    }

    public void setStampDuty(RichInputNumberSpinbox stampDuty) {
        this.stampDuty = stampDuty;
    }

    public RichInputNumberSpinbox getStampDuty() {
        return stampDuty;
    }

    public void setTotMeds(RichInputNumberSpinbox totMeds) {
        this.totMeds = totMeds;
    }

    public RichInputNumberSpinbox getTotMeds() {
        return totMeds;
    }

    public void setTotNetAmt(RichInputNumberSpinbox totNetAmt) {
        this.totNetAmt = totNetAmt;
    }

    public RichInputNumberSpinbox getTotNetAmt() {
        return totNetAmt;
    }

    public void setCancDtls(RichPanelGroupLayout cancDtls) {
        this.cancDtls = cancDtls;
    }

    public RichPanelGroupLayout getCancDtls() {
        return cancDtls;
    }

    public void setReceiptAmount(RichOutputText receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public RichOutputText getReceiptAmount() {
        return receiptAmount;
    }

    public void setTotalRefundAmount(RichOutputText totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public RichOutputText getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setMedAmount(RichOutputText medAmount) {
        this.medAmount = medAmount;
    }

    public RichOutputText getMedAmount() {
        return medAmount;
    }

    public void setCheckCommAmount(RichOutputText checkCommAmount) {
        this.checkCommAmount = checkCommAmount;
    }

    public RichOutputText getCheckCommAmount() {
        return checkCommAmount;
    }

    public void setTotalCommClawback(RichOutputText totalCommClawback) {
        this.totalCommClawback = totalCommClawback;
    }

    public RichOutputText getTotalCommClawback() {
        return totalCommClawback;
    }

    public void setAdminCharge(RichOutputText adminCharge) {
        this.adminCharge = adminCharge;
    }

    public RichOutputText getAdminCharge() {
        return adminCharge;
    }


    public void setLifeAssureds(RichTable lifeAssureds) {
        this.lifeAssureds = lifeAssureds;
    }

    public RichTable getLifeAssureds() {
        return lifeAssureds;
    }

    public void setObjOccupationDesc(RichInputText objOccupationDesc) {
        this.objOccupationDesc = objOccupationDesc;
    }

    public RichInputText getObjOccupationDesc() {
        return objOccupationDesc;
    }

    public void setLifeAssuredDesc(RichInputText lifeAssuredDesc) {
        this.lifeAssuredDesc = lifeAssuredDesc;
    }

    public RichInputText getLifeAssuredDesc() {
        return lifeAssuredDesc;
    }

    public void setAssuredDesc(RichInputText assuredDesc) {
        this.assuredDesc = assuredDesc;
    }

    public RichInputText getAssuredDesc() {
        return assuredDesc;
    }

    public void setProposalDetails(RichPanelGroupLayout proposalDetails) {
        this.proposalDetails = proposalDetails;
    }

    public RichPanelGroupLayout getProposalDetails() {
        return proposalDetails;
    }

    public void setJointProposal(RichSelectOneChoice jointProposal) {
        this.jointProposal = jointProposal;
    }

    public RichSelectOneChoice getJointProposal() {
        return jointProposal;
    }

    public void setMonthlyIncome(RichInputNumberSpinbox monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public RichInputNumberSpinbox getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setNewEffectiveDate(RichInputDate newEffectiveDate) {
        this.newEffectiveDate = newEffectiveDate;
    }

    public RichInputDate getNewEffectiveDate() {
        return newEffectiveDate;
    }

    public void setPayFreqCode(RichSelectOneChoice payFreqCode) {
        this.payFreqCode = payFreqCode;
    }

    public RichSelectOneChoice getPayFreqCode() {
        return payFreqCode;
    }

    public void setBranchName(RichInputText branchName) {
        this.branchName = branchName;
    }

    public RichInputText getBranchName() {
        return branchName;
    }

    public void setTerm(RichInputNumberSpinbox term) {
        this.term = term;
    }

    public RichInputNumberSpinbox getTerm() {
        return term;
    }

    public void setLifeRiderFactor(RichInputNumberSpinbox lifeRiderFactor) {
        this.lifeRiderFactor = lifeRiderFactor;
    }

    public RichInputNumberSpinbox getLifeRiderFactor() {
        return lifeRiderFactor;
    }

    public void setJointAssuredDesc(RichInputText jointAssuredDesc) {
        this.jointAssuredDesc = jointAssuredDesc;
    }

    public RichInputText getJointAssuredDesc() {
        return jointAssuredDesc;
    }

    public void setMorgageInterest(RichInputNumberSpinbox morgageInterest) {
        this.morgageInterest = morgageInterest;
    }

    public RichInputNumberSpinbox getMorgageInterest() {
        return morgageInterest;
    }

    public void setRetirementAge(RichInputNumberSpinbox retirementAge) {
        this.retirementAge = retirementAge;
    }

    public RichInputNumberSpinbox getRetirementAge() {
        return retirementAge;
    }

    public void setChildAge(RichInputNumberSpinbox childAge) {
        this.childAge = childAge;
    }

    public RichInputNumberSpinbox getChildAge() {
        return childAge;
    }

    public void setContribution(RichInputNumberSpinbox contribution) {
        this.contribution = contribution;
    }

    public RichInputNumberSpinbox getContribution() {
        return contribution;
    }

    public void setProposalNo(RichInputText proposalNo) {
        this.proposalNo = proposalNo;
    }

    public RichInputText getProposalNo() {
        return proposalNo;
    }


    public void setProposalClients(RichTable proposalClients) {
        this.proposalClients = proposalClients;
    }

    public RichTable getProposalClients() {
        return proposalClients;
    }

    public void setRetages(RichTable retages) {
        this.retages = retages;
    }

    public RichTable getRetages() {
        return retages;
    }

    public void setProdOptTab(RichTable prodOptTab) {
        this.prodOptTab = prodOptTab;
    }

    public RichTable getProdOptTab() {
        return prodOptTab;
    }

    public void setPopDesc(RichInputText popDesc) {
        this.popDesc = popDesc;
    }

    public RichInputText getPopDesc() {
        return popDesc;
    }


    public void setBranchesTab(RichTable branchesTab) {
        this.branchesTab = branchesTab;
    }

    public RichTable getBranchesTab() {
        return branchesTab;
    }


    public void setBrnCode(RichInputNumberSpinbox brnCode) {
        this.brnCode = brnCode;
    }

    public RichInputNumberSpinbox getBrnCode() {
        return brnCode;
    }

    public void setTransactionDetails(RichPanelBox transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public RichPanelBox getTransactionDetails() {
        return transactionDetails;
    }

    public void setSelectTransaction(RichPanelBox selectTransaction) {
        this.selectTransaction = selectTransaction;
    }

    public RichPanelBox getSelectTransaction() {
        return selectTransaction;
    }

    public void setProposalTransSelected(RichCommandButton proposalTransSelected) {
        this.proposalTransSelected = proposalTransSelected;
    }

    public RichCommandButton getProposalTransSelected() {
        return proposalTransSelected;
    }


    public void setNewProp(RichSelectBooleanRadio newProp) {
        this.newProp = newProp;
    }

    public RichSelectBooleanRadio getNewProp() {
        return newProp;
    }

    public void setEditProp(RichSelectBooleanRadio editProp) {
        this.editProp = editProp;
    }

    public RichSelectBooleanRadio getEditProp() {
        return editProp;
    }

    public void setConProp(RichSelectBooleanRadio conProp) {
        this.conProp = conProp;
    }

    public RichSelectBooleanRadio getConProp() {
        return conProp;
    }

    public void setReinProp(RichSelectBooleanRadio reinProp) {
        this.reinProp = reinProp;
    }

    public RichSelectBooleanRadio getReinProp() {
        return reinProp;
    }

    public void setCanProp(RichSelectBooleanRadio canProp) {
        this.canProp = canProp;
    }

    public RichSelectBooleanRadio getCanProp() {
        return canProp;
    }

    public void setEnProp(RichSelectBooleanRadio enProp) {
        this.enProp = enProp;
    }

    public RichSelectBooleanRadio getEnProp() {
        return enProp;
    }


    public void setObjEffectiveDate(RichInputDate effectiveDate) {
        this.objEffectiveDate = effectiveDate;
    }

    public RichInputDate getObjEffectiveDate() {
        return objEffectiveDate;
    }

    public void setJointProp(RichSelectBooleanCheckbox jointProp) {
        this.jointProp = jointProp;
    }

    public RichSelectBooleanCheckbox getJointProp() {
        return jointProp;
    }

    public void setLifeRider(RichSelectOneChoice lifeRider) {
        this.lifeRider = lifeRider;
    }

    public RichSelectOneChoice getLifeRider() {
        return lifeRider;
    }

    public void setAssuredPrpCode(RichInputNumberSpinbox assuredPrpCode) {
        this.assuredPrpCode = assuredPrpCode;
    }

    public RichInputNumberSpinbox getAssuredPrpCode() {
        return assuredPrpCode;
    }

    public void setLifeAssuredPrpCode(RichInputNumberSpinbox lifeAssuredPrpCode) {
        this.lifeAssuredPrpCode = lifeAssuredPrpCode;
    }

    public RichInputNumberSpinbox getLifeAssuredPrpCode() {
        return lifeAssuredPrpCode;
    }

    public void setJointAssuredPrpCode(RichInputNumberSpinbox jointAssuredPrpCode) {
        this.jointAssuredPrpCode = jointAssuredPrpCode;
    }

    public RichInputNumberSpinbox getJointAssuredPrpCode() {
        return jointAssuredPrpCode;
    }

    public void setDateSigned(RichInputDate dateSigned) {
        this.dateSigned = dateSigned;
    }

    public RichInputDate getDateSigned() {
        return dateSigned;
    }

    public void setCoCode(RichInputNumberSpinbox coCode) {
        this.coCode = coCode;
    }

    public RichInputNumberSpinbox getCoCode() {
        return coCode;
    }

    public void setClientOccupationsTab(RichTable clientOccupationsTab) {
        this.clientOccupationsTab = clientOccupationsTab;
    }

    public RichTable getClientOccupationsTab() {
        return clientOccupationsTab;
    }


    public void setDebitDate(RichInputDate debitDate) {
        this.debitDate = debitDate;
    }

    public RichInputDate getDebitDate() {
        return debitDate;
    }

    public void setBcaCode(RichInputNumberSpinbox bcaCode) {
        this.bcaCode = bcaCode;
    }

    public RichInputNumberSpinbox getBcaCode() {
        return bcaCode;
    }


    public void setPropAuthDtls(RichPanelGroupLayout propAuthDtls) {
        this.propAuthDtls = propAuthDtls;
    }

    public RichPanelGroupLayout getPropAuthDtls() {
        return propAuthDtls;
    }

    public void setAuthTabbed(RichShowDetailItem authTabbed) {
        this.authTabbed = authTabbed;
    }

    public RichShowDetailItem getAuthTabbed() {
        return authTabbed;
    }

    public void setPendStatuses(RichTable pendStatuses) {
        this.pendStatuses = pendStatuses;
    }

    public RichTable getPendStatuses() {
        return pendStatuses;
    }

    public void setStaCode(RichInputNumberSpinbox staCode) {
        this.staCode = staCode;
    }

    public RichInputNumberSpinbox getStaCode() {
        return staCode;
    }

    public void setStaDate(RichInputDate staDate) {
        this.staDate = staDate;
    }

    public RichInputDate getStaDate() {
        return staDate;
    }

    public void setStaReason(RichSelectOneChoice staReason) {
        this.staReason = staReason;
    }

    public RichSelectOneChoice getStaReason() {
        return staReason;
    }

    public void setStaStatus(RichSelectOneChoice staStatus) {
        this.staStatus = staStatus;
    }

    public RichSelectOneChoice getStaStatus() {
        return staStatus;
    }

    public void setStaDateSorted(RichInputDate staDateSorted) {
        this.staDateSorted = staDateSorted;
    }

    public RichInputDate getStaDateSorted() {
        return staDateSorted;
    }

    public void setBusCategoriesTab(RichTable busCategoriesTab) {
        this.busCategoriesTab = busCategoriesTab;
    }

    public RichTable getBusCategoriesTab() {
        return busCategoriesTab;
    }

    public void setBcaDesc(RichInputText bcaDesc) {
        this.bcaDesc = bcaDesc;
    }

    public RichInputText getBcaDesc() {
        return bcaDesc;
    }

    public void setAgnCode(RichInputNumberSpinbox agnCode) {
        this.agnCode = agnCode;
    }

    public RichInputNumberSpinbox getAgnCode() {
        return agnCode;
    }

    public void setAgentName(RichInputText agentName) {
        this.agentName = agentName;
    }

    public RichInputText getAgentName() {
        return agentName;
    }


    public void setAgentsTab(RichTable agentsTab) {
        this.agentsTab = agentsTab;
    }

    public RichTable getAgentsTab() {
        return agentsTab;
    }

    public void setPopCode(RichInputNumberSpinbox popCode) {
        this.popCode = popCode;
    }

    public RichInputNumberSpinbox getPopCode() {
        return popCode;
    }

    public void setPprCoinsurance(RichSelectOneChoice pprCoinsurance) {
        this.pprCoinsurance = pprCoinsurance;
    }

    public RichSelectOneChoice getPprCoinsurance() {
        return pprCoinsurance;
    }

    public void setPprCoinsureLeader(RichSelectOneChoice pprCoinsureLeader) {
        this.pprCoinsureLeader = pprCoinsureLeader;
    }

    public RichSelectOneChoice getPprCoinsureLeader() {
        return pprCoinsureLeader;
    }

    public void setClientName(RichInputText clientName) {
        this.clientName = clientName;
    }

    public RichInputText getClientName() {
        return clientName;
    }

    public void setClientLovDetails(RichPanelGroupLayout clientLovDetails) {
        this.clientLovDetails = clientLovDetails;
    }

    public RichPanelGroupLayout getClientLovDetails() {
        return clientLovDetails;
    }

    public void setClientExistingPolsTable(RichTable clientExistingPolsTable) {
        this.clientExistingPolsTable = clientExistingPolsTable;
    }

    public RichTable getClientExistingPolsTable() {
        return clientExistingPolsTable;
    }


    public void setPolTermPop(RichPopup polTermPop) {
        this.polTermPop = polTermPop;
    }

    public RichPopup getPolTermPop() {
        return polTermPop;
    }

    public void setPolTermTab(RichTable polTermTab) {
        this.polTermTab = polTermTab;
    }

    public RichTable getPolTermTab() {
        return polTermTab;
    }

    public void setLifeRates(RichTable lifeRates) {
        this.lifeRates = lifeRates;
    }

    public RichTable getLifeRates() {
        return lifeRates;
    }

    public void setLsfCode(RichInputNumberSpinbox lsfCode) {
        this.lsfCode = lsfCode;
    }

    public RichInputNumberSpinbox getLsfCode() {
        return lsfCode;
    }

    public void setProdLifeRider(String prodLifeRider) {
        this.prodLifeRider = (String)session.getAttribute("lifeRider");
        ;
    }

    public String getProdLifeRider() {
        return (String)session.getAttribute("lifeRider");
    }
}
