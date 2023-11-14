package TurnQuest.view.Campaigns;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class campaignBacking {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable tblCampaigns;
    private RichDialog newEditCampaign;
    private RichPanelBox pnBoxCampaignDetails;
    private RichInputText txtCampName;
    private RichInputText txtCampObjective;
    private RichInputText txtCampDescription;
    private RichInputText txtCampSystenName;
    private RichInputDate dtCampFrom;
    private RichInputDate dtCampTo;
    private RichInputText txtCampOrgName;
    private RichInputNumberSpinbox noCampBudget;
    private RichInputNumberSpinbox noCampActualCost;
    private RichInputNumberSpinbox noCampExpCost;
    private RichInputNumberSpinbox noCampExpRevenue;
    private RichInputText txtCampCurrencyName;
    private RichInputText txtCampSponsor;
    private RichInputText txtCampType;
    private RichInputText txtCampStatus;
    private RichInputNumberSpinbox noCampImpressions;
    private RichInputText txtCampSysCode;
    private RichInputText txtCampOrgCode;
    private RichInputText txtCampUserCode;
    private RichInputText txtCampTeamCode;
    private RichInputText txtCampProdCode;
    private RichInputText txtCampCurrencyCode;
    private RichInputText txtCampStatusCode;
    private RichTable tblSystems;
    private RichTable tblUsers;
    private RichTable tblStatuses;
    private RichTable tblOrganisations;
    private RichTable tblCurrencies;
    private RichInputText txtCampCode;
    private RichInputText txtCampUser;
    private RichInputText txtCampTeam;
    private RichTable tblProducts;
    private RichInputText txtCampProductName;
    private RichTable tblTeams;
    private RichTable tblActivitiesLOV;
    private RichCommandButton btnEditCampAct;
    private RichInputText txtCampActCode;
    private RichTable tblCampActivities;
    private RichTable targetsTable;
    private RichTable campaignTargetTable;
    private RichSelectOneChoice messageType;
    private RichTable templatesTable;
    private RichSelectOneChoice sendToAll;
    private RichInputText subject;
    private RichInputText body;
    private RichTable campaignMessageTable;
    private RichInputText txtEvent;
    private RichInputText txtVenue;
    private RichInputDate txtExecutionTime;
    private RichTable campaignProductTbl;
    private RichSelectBooleanCheckbox rowChecked;

    public campaignBacking() {
    }

    public String actionNewCampaign() {
        session.setAttribute("action", "A");
        newEditCampaign.setTitle("New Campaign");
        txtCampTeam.setValue(null);
        txtCampProductName.setValue(null);
        txtCampUser.setValue(null);
        txtCampCode.setValue(null);
        txtCampCode.setValue(null);
        txtCampName.setValue(null);
        txtCampObjective.setValue(null);
        txtCampDescription.setValue(null);
        txtCampSystenName.setValue(null);
        dtCampFrom.setValue(null);
        dtCampTo.setValue(null);
        txtCampOrgName.setValue(null);
        noCampBudget.setValue(null);
        noCampActualCost.setValue(null);
        noCampExpCost.setValue(null);
        noCampExpRevenue.setValue(null);
        txtCampCurrencyName.setValue(null);
        txtCampSponsor.setValue(null);
        txtCampType.setValue(null);
        txtCampStatus.setValue(null);
        noCampImpressions.setValue(null);
        txtCampSysCode.setValue(null);
        txtCampOrgCode.setValue(null);
        txtCampUserCode.setValue(null);
        txtCampTeamCode.setValue(null);
        txtCampProdCode.setValue(null);
        txtCampCurrencyCode.setValue(null);
        txtCampStatusCode.setValue(null);
        txtVenue.setValue(null);
        txtExecutionTime.setValue(null);
        txtEvent.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditCampaignPop" + "').show(hints);");

        return null;
    }

    public String actionEditCampaign() {
        session.setAttribute("action", "E");
        Object key = tblCampaigns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a campaign to edit"));
            return null;
        }

        newEditCampaign.setTitle("Edit Campaign");
        txtCampUserCode.setValue(nodeBinding.getAttribute("campUserCode"));
        txtCampUser.setValue(nodeBinding.getAttribute("campUserName"));
        txtCampTeamCode.setValue(nodeBinding.getAttribute("campTeamUserCode"));
        txtCampTeam.setValue(nodeBinding.getAttribute("campTeamName"));
        txtCampName.setValue(nodeBinding.getAttribute("campName"));
        txtCampObjective.setValue(nodeBinding.getAttribute("campObjective"));
        txtCampDescription.setValue(nodeBinding.getAttribute("campDescription"));
        txtCampSystenName.setValue(nodeBinding.getAttribute("campSysName"));
        dtCampFrom.setValue(nodeBinding.getAttribute("campDate"));
        dtCampTo.setValue(nodeBinding.getAttribute("campExpCloseDate"));
        txtCampOrgName.setValue(nodeBinding.getAttribute("campOrgName"));
        noCampBudget.setValue(nodeBinding.getAttribute("campBudgetedCost"));
        noCampActualCost.setValue(nodeBinding.getAttribute("campActualCost"));
        noCampExpCost.setValue(nodeBinding.getAttribute("campExpCost"));
        noCampExpRevenue.setValue(nodeBinding.getAttribute("campExpRevenue"));
        txtCampCurrencyName.setValue(nodeBinding.getAttribute("currencyName"));
        txtCampSponsor.setValue(nodeBinding.getAttribute("campSponsor"));
        txtCampType.setValue(nodeBinding.getAttribute("campType"));
        txtCampStatus.setValue(nodeBinding.getAttribute("campStatus"));
        noCampImpressions.setValue(nodeBinding.getAttribute("campImpressionCount"));

        txtCampSysCode.setValue(nodeBinding.getAttribute("campSysCode"));
        txtCampOrgCode.setValue(nodeBinding.getAttribute("campOrgCode"));
        txtCampUserCode.setValue(nodeBinding.getAttribute("campUserCode"));
        txtCampTeamCode.setValue(nodeBinding.getAttribute("campTeamUserCode"));
        txtCampProdCode.setValue(nodeBinding.getAttribute("campProdCode"));
        txtCampCurrencyCode.setValue(nodeBinding.getAttribute("currencyCode"));
        txtCampProductName.setValue(nodeBinding.getAttribute("campProdName"));
        txtVenue.setValue(nodeBinding.getAttribute("venue"));
        txtExecutionTime.setValue(nodeBinding.getAttribute("eventTime"));
        txtEvent.setValue(nodeBinding.getAttribute("event"));
        //txtCampStatusCode.setValue(nodeBinding.getAttribute("activitysubject"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditCampaignPop" + "').show(hints);");

        return null;
    }

    public String actionDeleteCampaign() {
        session.setAttribute("action", "D");
        Object key = tblCampaigns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a campaign to Delete"));
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_PKG.TQC_CAMPAIGNS_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "D");
            stmt.setObject(2, txtCampCode.getValue());

            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.setDate(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, null);
            stmt.setDate(13, null);
            stmt.setString(14, null); //V_CMP_TAR_AUDIANCE
            stmt.setString(15, null); //V_CMP_TAR_SIZE
            stmt.setString(16, null); //V_CMP_NUM_SENT
            stmt.setString(17, null);
            stmt.setString(18, null);
            stmt.setString(19, null);
            stmt.setString(20, null);
            stmt.setString(21, null); //V_CMP_EXPT_SALES_CNT
            stmt.setString(22, null); //V_CMP_ACTL_SALES_CNT
            stmt.setString(23, null); //V_CMP_ACTL_RESPONSE_CNT
            stmt.setString(24, null); //V_CMP_EXPT_RESPONSE_CNT
            stmt.setString(25, null); //V_CMP_EXPT_ROI
            stmt.setString(26, null); ///V_CMP_ACTL_ROI
            stmt.setString(27, null);
            stmt.setString(28, null);
            stmt.setString(29, null);
            stmt.setString(30, null);
            stmt.setString(31, null);
            stmt.setString(32, null);
            stmt.setString(33, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findCampaignsIterator").executeQuery();
            GlobalCC.refreshUI(tblCampaigns);

            String message = "Record Deleted Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;

    }

    public void setTblCampaigns(RichTable tblCampaigns) {
        this.tblCampaigns = tblCampaigns;
    }

    public RichTable getTblCampaigns() {
        return tblCampaigns;
    }

    public void setNewEditCampaign(RichDialog newEditCampaign) {
        this.newEditCampaign = newEditCampaign;
    }

    public RichDialog getNewEditCampaign() {
        return newEditCampaign;
    }

    public void setPnBoxCampaignDetails(RichPanelBox pnBoxCampaignDetails) {
        this.pnBoxCampaignDetails = pnBoxCampaignDetails;
    }

    public RichPanelBox getPnBoxCampaignDetails() {
        return pnBoxCampaignDetails;
    }

    public void setTxtCampName(RichInputText txtCampName) {
        this.txtCampName = txtCampName;
    }

    public RichInputText getTxtCampName() {
        return txtCampName;
    }

    public void setTxtCampObjective(RichInputText txtCampObjective) {
        this.txtCampObjective = txtCampObjective;
    }

    public RichInputText getTxtCampObjective() {
        return txtCampObjective;
    }


    public void setTxtCampDescription(RichInputText txtCampDescription) {
        this.txtCampDescription = txtCampDescription;
    }

    public RichInputText getTxtCampDescription() {
        return txtCampDescription;
    }

    public void setTxtCampSystenName(RichInputText txtCampSystenName) {
        this.txtCampSystenName = txtCampSystenName;
    }

    public RichInputText getTxtCampSystenName() {
        return txtCampSystenName;
    }

    public String actionShowSystems() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campSystemsPop" + "').show(hints);");

        return null;
    }

    public String actionShowProducts() {
        if (txtCampSysCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a system first"));
            return null;
        }

        ADFUtils.findIterator("findCampaignProductsIterator").executeQuery();
        GlobalCC.refreshUI(tblProducts);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampProductsPop" + "').show(hints);");
        return null;
    }

    public void setDtCampFrom(RichInputDate dtCampFrom) {
        this.dtCampFrom = dtCampFrom;
    }

    public RichInputDate getDtCampFrom() {
        return dtCampFrom;
    }

    public void setDtCampTo(RichInputDate dtCampTo) {
        this.dtCampTo = dtCampTo;
    }

    public RichInputDate getDtCampTo() {
        return dtCampTo;
    }

    public String actionShowuserGroups() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userTeamsPop" + "').show(hints);");
        return null;
    }

    public void setTxtCampOrgName(RichInputText txtCampOrgName) {
        this.txtCampOrgName = txtCampOrgName;
    }

    public RichInputText getTxtCampOrgName() {
        return txtCampOrgName;
    }

    public String actionShowOrganisations() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampOrganisationsPOP" + "').show(hints);");
        return null;
    }

    public void setNoCampBudget(RichInputNumberSpinbox noCampBudget) {
        this.noCampBudget = noCampBudget;
    }

    public RichInputNumberSpinbox getNoCampBudget() {
        return noCampBudget;
    }

    public void setNoCampActualCost(RichInputNumberSpinbox noCampActualCost) {
        this.noCampActualCost = noCampActualCost;
    }

    public RichInputNumberSpinbox getNoCampActualCost() {
        return noCampActualCost;
    }

    public void setNoCampExpCost(RichInputNumberSpinbox noCampExpCost) {
        this.noCampExpCost = noCampExpCost;
    }

    public RichInputNumberSpinbox getNoCampExpCost() {
        return noCampExpCost;
    }

    public void setNoCampExpRevenue(RichInputNumberSpinbox noCampExpRevenue) {
        this.noCampExpRevenue = noCampExpRevenue;
    }

    public RichInputNumberSpinbox getNoCampExpRevenue() {
        return noCampExpRevenue;
    }

    public void setTxtCampCurrencyName(RichInputText txtCampCurrencyName) {
        this.txtCampCurrencyName = txtCampCurrencyName;
    }

    public RichInputText getTxtCampCurrencyName() {
        return txtCampCurrencyName;
    }

    public String actionShowCurrencies() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CamCurrenciesPop" + "').show(hints);");

        return null;
    }

    public void setTxtCampSponsor(RichInputText txtCampSponsor) {
        this.txtCampSponsor = txtCampSponsor;
    }

    public RichInputText getTxtCampSponsor() {
        return txtCampSponsor;
    }

    public void setTxtCampType(RichInputText txtCampType) {
        this.txtCampType = txtCampType;
    }

    public RichInputText getTxtCampType() {
        return txtCampType;
    }

    public void setTxtCampStatus(RichInputText txtCampStatus) {
        this.txtCampStatus = txtCampStatus;
    }

    public RichInputText getTxtCampStatus() {
        return txtCampStatus;
    }

    public String actionShowStatuses() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:statusesPop" + "').show(hints);");
        return null;
    }

    public void setNoCampImpressions(RichInputNumberSpinbox noCampImpressions) {
        this.noCampImpressions = noCampImpressions;
    }

    public RichInputNumberSpinbox getNoCampImpressions() {
        return noCampImpressions;
    }

    public void setTxtCampSysCode(RichInputText txtCampSysCode) {
        this.txtCampSysCode = txtCampSysCode;
    }

    public RichInputText getTxtCampSysCode() {
        return txtCampSysCode;
    }

    public void setTxtCampOrgCode(RichInputText txtCampOrgCode) {
        this.txtCampOrgCode = txtCampOrgCode;
    }

    public RichInputText getTxtCampOrgCode() {
        return txtCampOrgCode;
    }

    public void setTxtCampUserCode(RichInputText txtCampUserCode) {
        this.txtCampUserCode = txtCampUserCode;
    }

    public RichInputText getTxtCampUserCode() {
        return txtCampUserCode;
    }

    public void setTxtCampTeamCode(RichInputText txtCampTeamCode) {
        this.txtCampTeamCode = txtCampTeamCode;
    }

    public RichInputText getTxtCampTeamCode() {
        return txtCampTeamCode;
    }

    public void setTxtCampProdCode(RichInputText txtCampProdCode) {
        this.txtCampProdCode = txtCampProdCode;
    }

    public RichInputText getTxtCampProdCode() {
        return txtCampProdCode;
    }

    public void setTxtCampCurrencyCode(RichInputText txtCampCurrencyCode) {
        this.txtCampCurrencyCode = txtCampCurrencyCode;
    }

    public RichInputText getTxtCampCurrencyCode() {
        return txtCampCurrencyCode;
    }

    public void setTxtCampStatusCode(RichInputText txtCampStatusCode) {
        this.txtCampStatusCode = txtCampStatusCode;
    }

    public RichInputText getTxtCampStatusCode() {
        return txtCampStatusCode;
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblUsers(RichTable tblUsers) {
        this.tblUsers = tblUsers;
    }

    public RichTable getTblUsers() {
        return tblUsers;
    }

    public String actionAcceptSystem() {
        Object key = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampSysCode.setValue(nodeBinding.getAttribute("code"));
            txtCampSystenName.setValue(nodeBinding.getAttribute("name"));
            txtCampProdCode.setValue(null);
            txtCampProductName.setValue(null);
            GlobalCC.refreshUI(pnBoxCampaignDetails);
            session.setAttribute("sysCode", txtCampSysCode.getValue());
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campSystemsPop" + "').hide(hints);");


        return null;
    }

    public String actionCancelSytem() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campSystemsPop" + "').hide(hints);");
        return null;
    }

    public String actioAcceptUser() {
        Object key = tblUsers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampUserCode.setValue(nodeBinding.getAttribute("userCode"));
            txtCampUser.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campUsersPop" + "').hide(hints);");

        return null;
    }

    public String actionCancelUser() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campUsersPop" + "').hide(hints);");

        return null;
    }

    public String actionAcceptUserTeam() {
        Object key = tblTeams.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampTeamCode.setValue(nodeBinding.getAttribute("userCode"));
            txtCampTeam.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userTeamsPop" + "').hide(hints);");

        return null;
    }

    public String actionCancelUserTeam() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userTeamsPop" + "').hide(hints);");
        return null;
    }

    public void setTblStatuses(RichTable tblStatuses) {
        this.tblStatuses = tblStatuses;
    }

    public RichTable getTblStatuses() {
        return tblStatuses;
    }

    public String actionAccceptStatus() {
        Object key = tblStatuses.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampStatus.setValue(nodeBinding.getAttribute("statusDecription"));

            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:statusesPop" + "').hide(hints);");


        return null;

    }

    public String actionCancelStatus() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:statusesPop" + "').hide(hints);");


        return null;
    }

    public void setTblOrganisations(RichTable tblOrganisations) {
        this.tblOrganisations = tblOrganisations;
    }

    public RichTable getTblOrganisations() {
        return tblOrganisations;
    }

    public String actionAccceptOrg() {
        Object key = tblOrganisations.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampOrgCode.setValue(nodeBinding.getAttribute("orgCode"));
            txtCampOrgName.setValue(nodeBinding.getAttribute("orgName"));
            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampOrganisationsPOP" + "').hide(hints);");


        return null;
    }

    public String actionCancelOrg() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampOrganisationsPOP" + "').hide(hints);");
        return null;
    }

    public void setTblCurrencies(RichTable tblCurrencies) {
        this.tblCurrencies = tblCurrencies;
    }

    public RichTable getTblCurrencies() {
        return tblCurrencies;
    }

    public String actionAcceptCurrency() {
        Object key = tblCurrencies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampCurrencyCode.setValue(nodeBinding.getAttribute("code"));
            txtCampCurrencyName.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CamCurrenciesPop" + "').hide(hints);");

        return null;
    }

    public String actionCancelCurrency() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CamCurrenciesPop" + "').hide(hints);");

        return null;
    }

    public void setTxtCampCode(RichInputText txtCampCode) {
        this.txtCampCode = txtCampCode;
    }

    public RichInputText getTxtCampCode() {
        return txtCampCode;
    }

    public String actionSaveCampaign() {
        String event;
        String venue;

        if (txtEvent.getValue() != null) {
            event = txtEvent.getValue().toString();
        } else {
            event = null;
        }

        if (txtVenue.getValue() != null) {
            venue = txtVenue.getValue().toString();
        } else {
            venue = null;
        }
        String campCode = GlobalCC.checkNullValues(txtCampCode.getValue());
        String campName = GlobalCC.checkNullValues(txtCampName.getValue());
        String campObjective =
            GlobalCC.checkNullValues(txtCampObjective.getValue());
        String campDesc =
            GlobalCC.checkNullValues(txtCampDescription.getValue());
        String sysCode = GlobalCC.checkNullValues(txtCampSysCode.getValue());
        String orgCode = GlobalCC.checkNullValues(txtCampOrgCode.getValue());
        String userCode = GlobalCC.checkNullValues(txtCampUserCode.getValue());
        String teamCode = GlobalCC.checkNullValues(txtCampTeamCode.getValue());
        String prodCode = GlobalCC.checkNullValues(txtCampProdCode.getValue());
        String currencyCode =
            GlobalCC.checkNullValues(txtCampCurrencyCode.getValue());
        String campStatus = GlobalCC.checkNullValues(txtCampStatus.getValue());
        String budget = GlobalCC.checkNullValues(noCampBudget.getValue());
        String actualCost =
            GlobalCC.checkNullValues(noCampActualCost.getValue());
        String expCost = GlobalCC.checkNullValues(noCampExpCost.getValue());
        String expRevenue =
            GlobalCC.checkNullValues(noCampExpRevenue.getValue());
        String sponsor = GlobalCC.checkNullValues(txtCampSponsor.getValue());
        String campType = GlobalCC.checkNullValues(txtCampType.getValue());
        String campImpressions =
            GlobalCC.checkNullValues(noCampImpressions.getValue());
        java.sql.Date dateFrom = GlobalCC.extractDate(dtCampFrom);
        java.sql.Date dateTo = GlobalCC.extractDate(dtCampTo);

        if (campName == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter Campaign Name"));
            return null;
        }
        if (sysCode == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a System"));
            return null;
        }
        if (orgCode == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select an Organisation"));
            return null;
        }
        if (userCode == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Assign a user to the campaign"));
            return null;
        }
        GlobalCC.dismissPopUp("crm", "newEditCampaignPop");

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_PKG.TQC_CAMPAIGNS_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (campCode == null) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, campCode);
            }
            stmt.setString(3, userCode);
            stmt.setString(4, teamCode);
            stmt.setDate(5, dateFrom);
            stmt.setString(6, sponsor);
            stmt.setString(7, orgCode);
            stmt.setString(8, prodCode);
            stmt.setString(9, sysCode);
            stmt.setString(10, campName);
            stmt.setString(11, campType);
            stmt.setString(12, campStatus);
            stmt.setDate(13, dateTo);
            stmt.setString(14, null); //V_CMP_TAR_AUDIANCE
            stmt.setString(15, null); //V_CMP_TAR_SIZE
            stmt.setString(16, null); //V_CMP_NUM_SENT
            stmt.setString(17, budget);
            stmt.setString(18, actualCost);
            stmt.setString(19, expCost);
            stmt.setString(20, expRevenue);
            stmt.setString(21, null); //V_CMP_EXPT_SALES_CNT
            stmt.setString(22, null); //V_CMP_ACTL_SALES_CNT
            stmt.setString(23, null); //V_CMP_ACTL_RESPONSE_CNT
            stmt.setString(24, null); //V_CMP_EXPT_RESPONSE_CNT
            stmt.setString(25, null); //V_CMP_EXPT_ROI
            stmt.setString(26, null); ///V_CMP_ACTL_ROI
            stmt.setString(27, campDesc);
            stmt.setString(28, currencyCode);
            stmt.setString(29, campObjective);
            stmt.setString(30, campImpressions);
            stmt.setString(31, event);
            stmt.setString(32, venue);
            stmt.setTimestamp(33, GlobalCC.extractTime(txtExecutionTime));
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findCampaignsIterator").executeQuery();
            GlobalCC.refreshUI(tblCampaigns);

            String message = "Record Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }


        return null;
    }

    public String actionCancelCampaign() {
        // Add event code here...
        return null;
    }

    public void actionTblCampaignsSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Object key = tblCampaigns.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            txtCampCode.setValue(nodeBinding.getAttribute("campCode"));
            session.setAttribute("CAMPAIGN_CODE", txtCampCode.getValue());
            ADFUtils.findIterator("findCampaignActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(tblCampActivities);

            ADFUtils.findIterator("findCampaignMessagesIterator").executeQuery();
            GlobalCC.refreshUI(campaignMessageTable);

            ADFUtils.findIterator("findCampaignTargetsIterator").executeQuery();
            GlobalCC.refreshUI(campaignTargetTable);

            ADFUtils.findIterator("findAllProductsIterator").executeQuery();
            GlobalCC.refreshUI(campaignProductTbl);
        }
    }

    public void setTxtCampUser(RichInputText txtCampUser) {
        this.txtCampUser = txtCampUser;
    }

    public RichInputText getTxtCampUser() {
        return txtCampUser;
    }

    public void setTxtCampTeam(RichInputText txtCampTeam) {
        this.txtCampTeam = txtCampTeam;
    }

    public RichInputText getTxtCampTeam() {
        return txtCampTeam;
    }

    public void setTblProducts(RichTable tblProducts) {
        this.tblProducts = tblProducts;
    }

    public RichTable getTblProducts() {
        return tblProducts;
    }

    public String actionAcceptProduct() {
        Object key = tblProducts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtCampProdCode.setValue(nodeBinding.getAttribute("productCode"));
            txtCampProductName.setValue(nodeBinding.getAttribute("prodDesc"));
            GlobalCC.refreshUI(pnBoxCampaignDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampProductsPop" + "').hide(hints);");
        return null;
    }

    public String actionCancelProduct() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampProductsPop" + "').hide(hints);");
        return null;
    }

    public String actionShowUsers() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:campUsersPop" + "').show(hints);");
        return null;
    }

    public void setTxtCampProductName(RichInputText txtCampProductName) {
        this.txtCampProductName = txtCampProductName;
    }

    public RichInputText getTxtCampProductName() {
        return txtCampProductName;
    }

    public void setTblTeams(RichTable tblTeams) {
        this.tblTeams = tblTeams;
    }

    public RichTable getTblTeams() {
        return tblTeams;
    }

    public String actionNewCampAct() {
        txtCampActCode.setValue(null);
        if (txtCampCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a campaign to add an activity"));
            return null;
        }
        ADFUtils.findIterator("findUndefinedCampaignActivitiesIterator").executeQuery();
        GlobalCC.showPopUp("crm", "campActivitiesPop");

        return null;
    }

    public void setTblActivitiesLOV(RichTable tblActivitiesLOV) {
        this.tblActivitiesLOV = tblActivitiesLOV;
    }

    public RichTable getTblActivitiesLOV() {
        return tblActivitiesLOV;
    }

    public String SaveCampaignTarget() {
        Object key = targetsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            String actCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("accountCode"));

            if (actCode == null) {
                GlobalCC.sysInformation("Select An Account");
                return null;
            }


            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CAMPAIGN_PKG.save_campaign_targets(?,?,?,?,?);end;";
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            try {
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setString(1, "A");
                stmt.setBigDecimal(2, null);
                stmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
                stmt.setBigDecimal(4, (BigDecimal)nodeBinding.getAttribute("accountCode"));
                stmt.setObject(5, nodeBinding.getAttribute("accountType"));
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findCampaignTargetsIterator").executeQuery();
                GlobalCC.refreshUI(campaignTargetTable);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
                return null;
            }


            return null;
        }
        return null;
    }

    public String DeleteCampaignTarget() {
        Object key = campaignTargetTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            String actCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("CMT_CODE"));

            if (actCode == null) {
                GlobalCC.sysInformation("Select An Account");
                return null;
            }


            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CAMPAIGN_PKG.save_campaign_targets(?,?,?,?,?);end;";
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            try {
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setString(1, "D");
                stmt.setBigDecimal(2,
                                   (BigDecimal)nodeBinding.getAttribute("CMT_CODE"));
                stmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
                stmt.setBigDecimal(4, null);
                stmt.setObject(5, null);
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findCampaignTargetsIterator").executeQuery();
                GlobalCC.refreshUI(campaignTargetTable);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
                return null;
            }


            return null;
        }
        return null;
    }


    public String actionAcceptActivity() {
        Object key = tblActivitiesLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            String actCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("activityCode"));
            String campActCode =
                GlobalCC.checkNullValues(txtCampActCode.getValue());
            String campCode = GlobalCC.checkNullValues(txtCampCode.getValue());


            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CAMPAIGN_PKG.TQC_CAMP_ACTIVITIES_PRC(?,?,?,? );end;";
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            try {
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                if (campActCode == null) {
                    stmt.setString(1, "A");
                    stmt.setString(2, null);
                } else {
                    stmt.setString(1, "E");
                    stmt.setString(2, campActCode);
                }
                stmt.setString(3, campCode);
                stmt.setString(4, actCode);
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findCampaignActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(tblCampActivities);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
                return null;
            }


            return null;
        }
        return null;
    }

    public void setBtnEditCampAct(RichCommandButton btnEditCampAct) {
        this.btnEditCampAct = btnEditCampAct;
    }

    public RichCommandButton getBtnEditCampAct() {
        return btnEditCampAct;
    }

    public void setTxtCampActCode(RichInputText txtCampActCode) {
        this.txtCampActCode = txtCampActCode;
    }

    public RichInputText getTxtCampActCode() {
        return txtCampActCode;
    }

    public String actionEditCampAct() {
        txtCampActCode.setValue(btnEditCampAct.getShortDesc());
        GlobalCC.showPopUp("crm", "campActivitiesPop");

        return null;
    }

    public void setTblCampActivities(RichTable tblCampActivities) {
        this.tblCampActivities = tblCampActivities;
    }

    public RichTable getTblCampActivities() {
        return tblCampActivities;
    }

    public String actionDeleteCampAct() {

        if (txtCampCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a campaign first"));
            return null;
        }
        Object key = tblCampActivities.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            String campActCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("campActCode"));

            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CAMPAIGN_PKG.TQC_CAMP_ACTIVITIES_PRC(?,?,?,? );end;";
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            try {
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "D");
                stmt.setString(2, campActCode);
                stmt.setString(3, null);
                stmt.setString(4, null);
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findCampaignActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(tblCampActivities);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();

                return null;
            }
        }
        return null;
    }


    public void setTargetsTable(RichTable targetsTable) {
        this.targetsTable = targetsTable;
    }

    public RichTable getTargetsTable() {
        return targetsTable;
    }

    public void setCampaignTargetTable(RichTable campaignTargetTable) {
        this.campaignTargetTable = campaignTargetTable;
    }

    public RichTable getCampaignTargetTable() {
        return campaignTargetTable;
    }

    public void setMessageType(RichSelectOneChoice messageType) {
        this.messageType = messageType;
    }

    public RichSelectOneChoice getMessageType() {
        return messageType;
    }

    public void MessageTypeListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            session.setAttribute("sysCode", new BigDecimal("0"));
            if (messageType.getValue() == null) {
                session.setAttribute("messageType", "EMAIL");
                ADFUtils.findIterator("fetchMessageTemplatesByTypeIterator").executeQuery();
                GlobalCC.refreshUI(templatesTable);

            } else if (messageType.getValue().toString().equalsIgnoreCase("EMAIL")) {
                session.setAttribute("messageType", "EMAIL");
                ADFUtils.findIterator("fetchMessageTemplatesByTypeIterator").executeQuery();
                GlobalCC.refreshUI(templatesTable);

            } else if (messageType.getValue().toString().equalsIgnoreCase("SMS")) {
                session.setAttribute("messageType", "SMS");
                ADFUtils.findIterator("fetchMessageTemplatesByTypeIterator").executeQuery();
                GlobalCC.refreshUI(templatesTable);

            } else if (messageType.getValue().toString().equalsIgnoreCase("FAX")) {
                session.setAttribute("messageType", "FAX");
                ADFUtils.findIterator("fetchMessageTemplatesByTypeIterator").executeQuery();
                GlobalCC.refreshUI(templatesTable);

            }

        }
    }

    public String AddCampaignMessage() {


        try {
            messageType.setValue("EMAIL");
            subject.setValue(null);
            body.setValue(null);
            sendToAll.setValue("Y");
            session.setAttribute("messageType", "EMAIL");

            if (txtCampCode.getValue() == null) {
                GlobalCC.EXCEPTIONREPORTING(new Exception("Select a campaign to add an activity"));
                return null;
            }
            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "p2");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditCampaignMessage() {
        try {

            Object key = campaignMessageTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            if (nodeBinding != null) {
                session.setAttribute("cmsgCode",
                                     nodeBinding.getAttribute("CMSG_CODE"));
                session.setAttribute("messageType",
                                     nodeBinding.getAttribute("CMSG_TYPE"));

                sendToAll.setValue(nodeBinding.getAttribute("CMSG_SEND_ALL"));
                subject.setValue(nodeBinding.getAttribute("CMSG_SUBJ"));
                body.setValue(nodeBinding.getAttribute("CMSG_BODY"));

                ADFUtils.findIterator("fetchMessageTemplatesByTypeIterator").executeQuery();
                GlobalCC.refreshUI(templatesTable);

                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "p2");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String findMessageTemplate() {
        try {

            Object key = templatesTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            if (nodeBinding != null) {

                subject.setValue(nodeBinding.getAttribute("msgtShtDesc"));
                body.setValue(nodeBinding.getAttribute("msgtMsg"));

                GlobalCC.refreshUI(subject);
                GlobalCC.refreshUI(body);
                GlobalCC.dismissPopUp("crm", "p3");


            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteCampaignMessage() {
        try {

            Object key = campaignMessageTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            if (nodeBinding != null) {
                session.setAttribute("cmsgCode",
                                     nodeBinding.getAttribute("CMSG_CODE"));
                session.setAttribute("messageType",
                                     nodeBinding.getAttribute("CMSG_TYPE"));

                sendToAll.setValue(nodeBinding.getAttribute("CMSG_SEND_ALL"));
                subject.setValue(nodeBinding.getAttribute("CMSG_SUBJ"));
                body.setValue(nodeBinding.getAttribute("CMSG_BODY"));

                session.setAttribute("action", "D");
                SaveCampaignMessage();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveCampaignMessage() {


        if (messageType.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a Message Type"));
            return null;
        }
        if (body.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter A Message Body"));
            return null;
        }

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_CAMPAIGN_PKG.save_campaign_message(?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, (String)session.getAttribute("action"));
            stmt.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("cmsgCode"));
            stmt.setString(3, (String)session.getAttribute("messageType"));
            if (subject.getValue() == null) {
                stmt.setString(4, null);
            } else {
                stmt.setString(4, subject.getValue().toString());
            }
            if (body.getValue() == null) {
                stmt.setString(5, null);
            } else {
                stmt.setString(5, body.getValue().toString());
            }
            if (sendToAll.getValue() == null) {
                stmt.setString(6, null);
            } else {
                stmt.setString(6, sendToAll.getValue().toString());
            }
            stmt.setBigDecimal(7,
                               (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));

            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("findCampaignMessagesIterator").executeQuery();
            GlobalCC.refreshUI(campaignMessageTable);
            GlobalCC.dismissPopUp("crm", "p2");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
        return null;
    }

    public String SendEmailMessages() {
        DBConnector connection = new DBConnector();

        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {

            Object key = campaignMessageTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            if (nodeBinding != null) {
                session.setAttribute("cmsgCode",
                                     nodeBinding.getAttribute("CMSG_CODE"));

                String query =
                    "begin TQC_CAMPAIGN_PKG.send_Campaign_msg_email(?,?,?);end;";
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setBigDecimal(1,
                                   (BigDecimal)session.getAttribute("cmsgCode"));
                stmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("CAMPAIGN_CODE"));
                stmt.setString(3, (String)session.getAttribute("Username"));

                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
                GlobalCC.sysInformation("Message Successfully Sent.");
            }
            ADFUtils.findIterator("findCampaignMessagesIterator").executeQuery();
            GlobalCC.refreshUI(campaignMessageTable);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTemplatesTable(RichTable templatesTable) {
        this.templatesTable = templatesTable;
    }

    public RichTable getTemplatesTable() {
        return templatesTable;
    }

    public void setSendToAll(RichSelectOneChoice sendToAll) {
        this.sendToAll = sendToAll;
    }

    public RichSelectOneChoice getSendToAll() {
        return sendToAll;
    }

    public void setSubject(RichInputText subject) {
        this.subject = subject;
    }

    public RichInputText getSubject() {
        return subject;
    }

    public void setBody(RichInputText body) {
        this.body = body;
    }

    public RichInputText getBody() {
        return body;
    }

    public void setCampaignMessageTable(RichTable campaignMessageTable) {
        this.campaignMessageTable = campaignMessageTable;
    }

    public RichTable getCampaignMessageTable() {
        return campaignMessageTable;
    }

    public String actionNewCampTarget() {
        ADFUtils.findIterator("findUndefinedTargetAccountsIterator").executeQuery();
        GlobalCC.showPopUp("crm", "p1");
        return null;
    }

    public void setTxtEvent(RichInputText txtEvent) {
        this.txtEvent = txtEvent;
    }

    public RichInputText getTxtEvent() {
        return txtEvent;
    }

    public void setTxtVenue(RichInputText txtVenue) {
        this.txtVenue = txtVenue;
    }

    public RichInputText getTxtVenue() {
        return txtVenue;
    }

    public void setTxtExecutionTime(RichInputDate txtExecutionTime) {
        this.txtExecutionTime = txtExecutionTime;
    }

    public RichInputDate getTxtExecutionTime() {
        return txtExecutionTime;
    }

    public String addNewproduct() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CampProductsPop" + "').show(hints);");
        // Add event code here...
        return null;
    }

    public String deleteProduct() {
        DBConnector connection = new DBConnector();

        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {

            Object key = campaignProductTbl.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            if (nodeBinding != null) {
                String query =
                    "begin TQC_CAMPAIGN_PKG.savecampaignproducts(?,?,?,?,?);end;";
                conn = connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                stmt.setObject(1, "D");
                stmt.setObject(2, nodeBinding.getAttribute("cproCode"));
                stmt.setObject(3, null);
                stmt.setObject(4, null);
                stmt.setObject(5, null);
                stmt.execute();
                stmt.close();
                conn.commit();
                conn.close();
            }
            ADFUtils.findIterator("findAllProductsIterator").executeQuery();
            GlobalCC.refreshUI(campaignProductTbl);
            GlobalCC.INFORMATIONREPORTING("product(s) Deleted successfully");


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setCampaignProductTbl(RichTable campaignProductTbl) {
        this.campaignProductTbl = campaignProductTbl;
    }

    public RichTable getCampaignProductTbl() {
        return campaignProductTbl;
    }

    public String saveSelectedProducts() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement cst = null;

        int rowCount = tblProducts.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Product Selected!");
            return null;
        }
        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)tblProducts.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if (Accept) {
                    String Query =
                        "begin TQC_CAMPAIGN_PKG.savecampaignproducts(?,?,?,?,?); end;";

                    datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();
                    cst = conn.prepareCall(Query);
                    cst.setObject(1, "A");
                    cst.setObject(2, null);
                    cst.setObject(3, r.getAttribute("productCode"));
                    cst.setObject(4, r.getAttribute("productShortDesc"));
                    cst.setObject(5, session.getAttribute("CAMPAIGN_CODE"));
                    cst.execute();

                }
            }
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findAllProductsIterator").executeQuery();
            GlobalCC.refreshUI(campaignProductTbl);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:CampProductsPop" + "').hide(hints);");
            GlobalCC.INFORMATIONREPORTING("product(s) saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return null;
    }

    public void setRowChecked(RichSelectBooleanCheckbox rowChecked) {
        this.rowChecked = rowChecked;
    }

    public RichSelectBooleanCheckbox getRowChecked() {
        return rowChecked;
    }
}
