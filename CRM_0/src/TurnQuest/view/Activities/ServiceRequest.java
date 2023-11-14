package TurnQuest.view.Activities;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.reports.ReportEngine;
import TurnQuest.view.utilities.CSVtoADFTableProcessor;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

import java.io.IOException;

import java.math.BigDecimal;

import java.net.FileNameMap;
import java.net.URLConnection;

import java.security.Security;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.render.ClientEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

//import TurnQuest.view.commons.GlobalCC;


public class ServiceRequest {
    private RichTable categories;
    private RichInputNumberSpinbox catCode;
    private RichPanelCollection panelCollSearch;
    private RichInputText catName;
    private RichInputNumberSpinbox catValid;
    private RichSelectBooleanRadio clntRd;
    private RichSelectBooleanRadio agentRd;
    private RichSelectBooleanRadio serProRd;
    private RichPanelFormLayout SEARCHHOLDER;
    private RichPanelLabelAndMessage statusHolder;
    private RichPanelLabelAndMessage searchClientType;
    private RichPanelBox searchCriteria;
    private RichPanelBox acc360;
    private RichOutputText nameDesc;
    private RichOutputText shtDesc;
    private RichOutputText accNoDesc;
    private RichOutputText telDesc;
    private RichOutputText emailDesc;
    private RichOutputText addressDesc;
    private RichTable individialTab;
    private RichTable groupTab;
    private RichTable generalTab;
    private RichInputText serCat;
    private RichSelectOneChoice reqSource;
    private RichSelectOneChoice reqAccType;
    private RichInputText reqAccount;
    private RichInputText reqSummary;
    private RichInputText reqDesc;
    private RichInputDate reqDate;

    private RichInputDate receiveDate;

    private RichInputText reqAssignee;
    private RichSelectOneChoice reqOwnerAccType;
    private RichInputText reqOwnerAccount;
    private RichInputDate reqDueDate;
    private RichSelectOneChoice reqStatus;
    private RichInputDate reqResDate;
    private RichInputText reqSolution;
    private RichTable users;
    private RichTable accounts;
    private RichTable clientPending;
    private RichTable allRequests;
    private RichInputText outServ;
    private RichInputText outHost;
    private RichInputNumberSpinbox outPort;
    private RichInputText outUser;
    private RichInputText outPasword;
    private RichInputText outEmail;
    private RichSelectOneChoice inType;
    private RichInputText inServ;
    private RichInputText inHost;
    private RichInputNumberSpinbox inPort;
    private RichInputText inUse;
    private RichInputText inPass;
    private RichInputText inEmail;
    private RichSelectOneChoice outType;
    private RichSelectBooleanCheckbox outSecure;
    private RichSelectBooleanCheckbox inSecure;
    private RichSelectOneChoice reqCommMode;
    private RichTable categoryBranchesTbl;
    private RichInputText branch;
    private RichTable txtDepartments;
    private RichInputText departmentVal;
    private RichSelectOneChoice txtCatType;
    private RichSelectBooleanCheckbox txtDefault;
    private RichInputText txtTypeName;
    private RichInputText txtShtDesc;
    private RichTable requestTypeTbl;
    private RichInputText txtType;
    private RichInputText txtTsrPolicyNo;
    private RichTable requestCategoriesTbl;
    private RichTable policiesTbl;
    private RichInputText txtComments;
    private RichTable agentsLOV;
    private RichTable serviceProviderTbl;
    private RichSelectOneChoice txtrequestStatus;
    private RichSelectBooleanCheckbox txtDefaultVal;
    private RichSelectBooleanRadio othersRd;
    private RichInputText txtName;
    private RichInputText txtTelephoneNumber;
    private RichInputText txtEmailAddress;
    private RichInputText txtPhysicalAddress;
    private RichInputText txtIdNumber;
    private RichSelectOneRadio txtOthersType;
    private RichTable othersTbl;
    private RichSelectBooleanRadio intReqRd;
    private RichTable selectInternalDep;
    private RichSelectOneChoice txtSecondaryCommMode;
    private RichInputText txtReasonsChange;
    private RichOutputLabel reasonForChangeTbl;
    private RichPanelBox agencyTab;
    private RichPanelBox providerTab;
    private RichInputText primaryCommMode;
    private RichInputText secondaryCommMode;
    private RichInputText requestCommMode;
    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private static String fileContent;
    private String filetype;
    private String reqRefNumber;
    private RichTable serviceReqTbl;
    private RichTable clientCommentsTbl;
    private RichTable clntCommentsTbl;

    private RichInputText txtClientComments;
    private RichInputText txtSolution;
    private RichCommandButton btnSaveComments;
    private RichCommandButton btnSaveServiceReq;
    private RichOutputText lblCommMode;

    private RichInputText reqReporter;
    private RichTable reporterAcc;
    private RichInputDate captureDate;
    private RichInputText closedBy;
    private RichInputText txtReasonForUpdate;
    private RichPanelGroupLayout searchFormHolder;
    private RichInputText txtSrchSectorName;
    private RichTable tblSectorPop; 
    private RichInputText txtsearchCriteriaValue;
    private RichInputText txtSearchShtDesc;
    private RichInputText txtSearchName;
    private RichInputText txtOtherName;
    private RichInputText txtSearchOtherName;
    private RichInputText txtSearchSector;
    private RichInputText txtSearchShortDesc;
    private RichSelectOneChoice txtSearchStatus;
    private RichPanelLabelAndMessage txtSearchClientType;
    private RichSelectOneChoice txtSearchClntType;

    private Object sectorName;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichCommandButton btnActionEditGroupClient;
    private RichTable tblClientGroup;
    private RichCommandButton btnActionDeleteGroupClient;
    private RichCommandButton btnRemoveClientFromGroup;
    private RichTable tblClientGrpMembers;
    private RichInputText txtGrpCode;
    private RichInputText txtGrpName;
    private RichInputNumberSpinbox txtGrpMinimum;
    private RichInputNumberSpinbox txtGrpMax;
    private RichCommandButton btnsaveGrp;
    private RichTable tblClientPop;
    private RichSelectBooleanCheckbox columnSelect;
    private RichSelectBooleanCheckbox grpMemberSelect;
    private RichCommandButton btnSelectAllGrpMembers;
    private RichCommandButton resetContainer;
    private RichPanelLabelAndMessage resetSearchContainer;
    private RichInputNumberSpinbox txtBranchMgrSeqNo;
    private RichCommandButton btnAddGrpMember;
    private RichCommandButton btnDeselectAll;
    private RichCommandButton btnSelectAll;
    private RichPanelCollection tblClientHolder; 
    private RichInputDate clntDateCreatedFrom;
    private RichInputDate clntDateCreatedTo;

    private RichColumn checkboxCol;
    private RichColumn editCol;
    private RichSelectBooleanRadio rbtnDateCreated;
    private RichSelectBooleanRadio rbtnClientType;
    private RichSelectBooleanRadio rbtnStatus;
    private RichSelectBooleanRadio rbtnSector;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichSelectBooleanRadio rbtnSearchAccountNo;
    private RichSelectBooleanRadio rbtnExactNmBig;
    private RichSelectBooleanRadio rbtnPartNmInOrder;
    private RichSelectBooleanRadio rbtnAnyPartOfFirAndOthNm;
    private RichSelectBooleanRadio rbtnBegPartOfAnyNm;
    private RichSelectBooleanRadio rbtnPartOfAnyNameInOrder;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnBegPartOfFstNmAndOName;
    private RichInputText txtSearchPhysical;
    private RichInputText txtSearchPostal;
    private RichSelectBooleanRadio rbtnCustomerId;
    private RichSelectBooleanRadio rbtnIncome;
    private RichInputText txtSearchOldId;
    private RichSelectBooleanRadio rbtnOldNames;
    private RichCommandButton btnSectorLov;
    private RichTable tblAccountOfficers;
    private RichCommandButton btnEditAccOfficer;
    private RichOutputLabel lbCount;
    private RichTable tblAccClients;
    private RichCommandButton btnChangeAccOfficer;
    private RichTable tblUserList;
    private RichInputText baseClientBackingBean1;
    private RichInputText txtAccountNo;

    private HtmlPanelGrid gridClientSearchDetails;
    private RichSelectBooleanRadio rbtnShortDescLeg;
    private RichSelectBooleanRadio rdbnDateOfBirth;
    private RichInputDate txtDateOfBirth;
    private RichSelectBooleanRadio rbtnIdNumber;
    private RichInputText txtIdNum;
    private RichSelectBooleanRadio txtClientPolNumber;
    private RichSelectBooleanRadio sbrClaimNo;
    private RichInputText clientPolicyNumber;
    private RichInputText txtClaimNo;
    private RichSelectBooleanRadio txtPinNumber;
    private RichInputText pinNumber;
    private RichSelectBooleanRadio txtPassPortNumber;
    private RichInputText txtPassport;
    private RichInputText txtTelNo;
    private RichSelectBooleanRadio txtTelNoRadio;
    private RichSelectBooleanRadio txtVehicleRegNoRadio; 
    private RichInputText txtOldAccountNo;
    private RichInputText txtVehicleRegNo; 
    private RichSelectBooleanRadio rbtnSearchOldAccountNo;

    public ServiceRequest() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public void setCategories(RichTable categories) {
        this.categories = categories;
    }

    public RichTable getCategories() {
        return categories;
    }

    public String newCat() {

        catCode.setValue(null);
        catName.setValue(null);
        catValid.setValue(null);
        reqAssignee.setValue(null);
        reqAssignee.setLabel(null);
        //        departmentVal.setLabel(null);
        //        txtCatType.setLabel(null);
        txtDefault.setLabel(null);
        txtDefault.setSelected(false);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categories" + "').show(hints);");

        return null;
    }

    public String editCat() {
        Object key = categories.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        catCode.setValue(r.getAttribute("tsrcCode"));
        catName.setValue(r.getAttribute("tsrcName"));
        catValid.setValue(r.getAttribute("tsrcValidity"));
        reqAssignee.setValue(r.getAttribute("usrName"));
        session.setAttribute("categoryBranch", r.getAttribute("brnCode"));
        branch.setValue(r.getAttribute("brnName"));
        if (r.getAttribute("usrCode") != null) {
            reqAssignee.setLabel(r.getAttribute("usrCode").toString());
        }
        //     departmentVal.setValue(r.getAttribute("depName"));
        //      txtCatType.setValue(r.getAttribute("tsrcType"));
        if (r.getAttribute("tsrcDefault") != null) {
            if (r.getAttribute("tsrcDefault").equals("Y")) {
                txtDefault.setSelected(true);
            } else {
                txtDefault.setSelected(false);
            }
        } else {
            txtDefault.setSelected(false);
        }
        //
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categories" + "').show(hints);");
        return null;
    }

    public String deleteCat() {
        Object key = categories.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.categoryProc(?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, "D");
            stmt.setBigDecimal(2, (BigDecimal)r.getAttribute("tsrcCode"));
            stmt.setString(3, (String)r.getAttribute("tsrcName"));
            stmt.setBigDecimal(4, (BigDecimal)r.getAttribute("tsrcValidity"));
            stmt.setString(5, null);
            stmt.setString(6, null);
            //          stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRequestCatsIterator").executeQuery();
            GlobalCC.refreshUI(categories);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setCatCode(RichInputNumberSpinbox catCode) {
        this.catCode = catCode;
    }

    public RichInputNumberSpinbox getCatCode() {
        return catCode;
    }

    public void setCatName(RichInputText catName) {
        this.catName = catName;
    }

    public RichInputText getCatName() {
        return catName;
    }

    public void setCatValid(RichInputNumberSpinbox catValid) {
        this.catValid = catValid;
    }

    public RichInputNumberSpinbox getCatValid() {
        return catValid;
    }

    public String saveCategory() {
        String code = null;
        String name = null;
        String validity = null;
        String user = null;
        BigDecimal categoryBranch;
        code = GlobalCC.checkNullValues(catCode.getValue());
        name = GlobalCC.checkNullValues(catName.getValue());
        validity = GlobalCC.checkNullValues(catValid.getValue());
        user = GlobalCC.checkNullValues(reqAssignee.getValue());
        if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name");
            return null;
        }
        if (user == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Assignee");
            return null;
        }
        //        if (validity == null) {
        //            GlobalCC.errorValueNotEntered("Error Value Missing: Validity Period");
        //            return null;
        //        }
        if (user != null) {
            user = reqAssignee.getLabel();
        }
        if (session.getAttribute("categoryBranch") != null) {
            categoryBranch =
                    new BigDecimal(session.getAttribute("categoryBranch").toString());
        } else {
            categoryBranch = null;
        }

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.categoryProc(?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (code == null) {
                stmt.setString(1, "A");
            } else {
                stmt.setString(1, "E");
            }
            stmt.setString(2, code);
            stmt.setString(3, name);
            stmt.setString(4, validity);
            stmt.setString(5, user);
            stmt.setObject(6, categoryBranch);
            stmt.setObject(7, session.getAttribute("srtCode"));
            //
            //          stmt.setObject(7, session.getAttribute("depCode"));
            //          stmt.setObject(8, txtCatType.getValue());
            if (txtDefault.isSelected()) {
                stmt.setObject(8, "Y");
            } else {
                stmt.setObject(8, "N");
            }
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRequestCatsIterator").executeQuery();
            GlobalCC.refreshUI(categories);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:categories" + "').hide(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public String accTypeSelected() {
        if (clntRd.isSelected()) {
            session.setAttribute("accountType", "C");
            if (session.getAttribute("grpCode") != null) {
                session.setAttribute("grpCode", null);

                session.setAttribute("_search", "false");
                List<UIComponent> child = panelCollSearch.getChildren();
                UIComponent comp = child.get(0);
                for (int i = 0; i < child.size(); i++) {
                    comp = child.get(i);

                    if (comp.getId().equalsIgnoreCase("t11")) {

                    }
                }

                List<UIComponent> children = SEARCHHOLDER.getChildren();
                UIComponent component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("it19") ||
                        component.getId().equalsIgnoreCase("it20") ||
                        component.getId().equalsIgnoreCase("it21") ||
                        component.getId().equalsIgnoreCase("it18") ||
                        component.getId().equalsIgnoreCase("it25") ||
                        component.getId().equalsIgnoreCase("it26")) {
                        RichInputText rpt = (RichInputText)component;
                        rpt.setValue(null);

                        GlobalCC.refreshUI(rpt);
                    }
                    if (component.getId().equalsIgnoreCase("id4") ||
                        component.getId().equalsIgnoreCase("id5")) {


                        RichInputDate rsc = (RichInputDate)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                children = statusHolder.getChildren();
                component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("soc2")) {


                        RichSelectOneChoice rsc =
                            (RichSelectOneChoice)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                children = searchClientType.getChildren();
                component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("soc1")) {


                        RichSelectOneChoice rsc =
                            (RichSelectOneChoice)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                session.setAttribute("searchCriteria", null);
            }

            ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:searchClientPop" + "').show(hints);");

            // session.setAttribute("myTrigger", "myTrigger");
            //ADFUtils.findIterator("fetchAllClientsByTriggerIterator").executeQuery();
            //tblClients.setRendered(true);
        } else if (agentRd.isSelected()) {
            session.setAttribute("accountType", "A");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:agentsLOV" + "').show(hints);");

        } else if (serProRd.isSelected()) {
            session.setAttribute("accountType", "SP");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:servProvLov" + "').show(hints);");
        } else if (othersRd.isSelected()) {
            session.setAttribute("accountType", "O");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "crm:p2" +
                                 "').show(hints);");
        } else if (intReqRd.isSelected()) {
            session.setAttribute("accountType", "IN");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "crm:p4" +
                                 "').show(hints);");
        } else {
            GlobalCC.errorValueNotEntered("No Account Type Selected. Please Check");

        }
        // Add event code here...
        return null;
    }

    public void setClntRd(RichSelectBooleanRadio clntRd) {
        this.clntRd = clntRd;
    }

    public RichSelectBooleanRadio getClntRd() {
        return clntRd;
    }

    public void setAgentRd(RichSelectBooleanRadio agentRd) {
        this.agentRd = agentRd;
    }

    public RichSelectBooleanRadio getAgentRd() {
        return agentRd;
    }

    public void setSerProRd(RichSelectBooleanRadio serProRd) {
        this.serProRd = serProRd;
    }

    public RichSelectBooleanRadio getSerProRd() {
        return serProRd;
    }

    public void setPanelCollSearch(RichPanelCollection panelCollSearch) {
        this.panelCollSearch = panelCollSearch;
    }

    public RichPanelCollection getPanelCollSearch() {
        return panelCollSearch;
    }

    public void setSEARCHHOLDER(RichPanelFormLayout SEARCHHOLDER) {
        this.SEARCHHOLDER = SEARCHHOLDER;
    }

    public RichPanelFormLayout getSEARCHHOLDER() {
        return SEARCHHOLDER;
    }

    public void setStatusHolder(RichPanelLabelAndMessage statusHolder) {
        this.statusHolder = statusHolder;
    }

    public RichPanelLabelAndMessage getStatusHolder() {
        return statusHolder;
    }

    public void setSearchClientType(RichPanelLabelAndMessage searchClientType) {
        this.searchClientType = searchClientType;
    }

    public RichPanelLabelAndMessage getSearchClientType() {
        return searchClientType;
    }

    public void setSearchCriteria(RichPanelBox searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public RichPanelBox getSearchCriteria() {
        return searchCriteria;
    }

    public void setAcc360(RichPanelBox acc360) {
        this.acc360 = acc360;
    }

    public RichPanelBox getAcc360() {
        return acc360;
    }

    //    public String clientSelected() {
    //
    //        List<UIComponent> children = panelCollSearch.getChildren();
    //        UIComponent component = children.get(0);
    //
    //        RichTable rpt = null;
    //        for (int i = 0; i < children.size(); i++) {
    //            component = children.get(i);
    //
    //            if (component.getId().equalsIgnoreCase("t11")) {
    //                rpt = (RichTable)component;
    //
    //
    //            }
    //        }
    //        try{
    ////        DBConnector connection = new DBConnector();
    ////        String query = null;
    ////        OracleConnection conn = null;
    ////        OracleCallableStatement stmt = null;
    ////        try {
    ////            conn = connection.getDatabaseConnection();
    ////
    ////
    //           Object key2 = rpt.getSelectedRowData();
    //           JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
    //              if (nodeBinding != null) {
    //                  GlobalCC.INFORMATIONREPORTING("No record selected");
    //                  return null;
    //                  }
    ////
    ////            if (nodeBinding != null) {
    ////
    ////                // Set the country code to be used to fetch the towns
    ////                session.setAttribute("clntCode",
    ////                                     new BigDecimal(nodeBinding.getAttribute("code").toString()));
    ////                stmt = null;
    ////                query =
    ////                        "begin TQC_SERVICE_REQUESTS.serviceRequestsProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
    ////
    ////                stmt = (OracleCallableStatement)conn.prepareCall(query);
    ////                System.out.println(session.getAttribute("clntCode"));
    ////                stmt.setString(1, "A");
    ////                stmt.registerOutParameter(2, OracleTypes.NUMBER);
    ////                stmt.setInt(3, 1);
    ////                stmt.setString(4, "Enquiry");
    ////                stmt.setString(5, "C");
    ////                stmt.setObject(6, session.getAttribute("clntCode"));
    ////                stmt.setObject(7, GlobalCC.parseDate(new Date()));
    ////                stmt.setBigDecimal(8,
    ////                                   (BigDecimal)session.getAttribute("UserCode"));
    ////                stmt.setString(9, "U");
    ////                stmt.setBigDecimal(10,
    ////                                   (BigDecimal)session.getAttribute("UserCode"));
    ////                stmt.setString(11, "Open");
    ////                stmt.setObject(12, GlobalCC.parseDate(new Date()));
    ////                stmt.setObject(13, GlobalCC.parseDate(new Date()));
    ////                stmt.setString(14, "Enquiry");
    ////                stmt.setString(15, "Enquiry");
    ////                stmt.setString(16, null);
    ////              stmt.setString(17, null);
    ////              stmt.setString(18, null);
    ////              stmt.setString(19, null);
    ////              stmt.execute();
    ////                session.setAttribute("tsrCode", stmt.getBigDecimal(2));
    ////                stmt.close();
    ////                conn.commit();
    ////                conn.close();
    //
    //                shtDesc.setValue(nodeBinding.getAttribute("shortDesc"));
    //                if (nodeBinding.getAttribute("name") == null) {
    //                    nameDesc.setValue(nodeBinding.getAttribute("name"));
    //                } else if (nodeBinding.getAttribute("otherNames") == null) {
    //                    nameDesc.setValue(nodeBinding.getAttribute("otherNames"));
    //                } else {
    //                    nameDesc.setValue(nodeBinding.getAttribute("name") + " " +
    //                                      nodeBinding.getAttribute("otherNames"));
    //                }
    //                session.setAttribute("accountName", nameDesc.getValue());
    //                session.setAttribute("accountCode",
    //                                     session.getAttribute("clntCode"));
    //                addressDesc.setValue(nodeBinding.getAttribute("physicalAddress"));
    //                accNoDesc.setValue(nodeBinding.getAttribute("accountNum"));
    //                emailDesc.setValue(nodeBinding.getAttribute("email"));
    //                telDesc.setValue(nodeBinding.getAttribute("phone1"));
    //                searchCriteria.setRendered(false);
    //                acc360.setRendered(true);
    //
    //        } catch (Exception e) {
    //            GlobalCC.EXCEPTIONREPORTING(e);
    //            return null;
    //        }
    //        return null;
    //    }

    public String clientSelected() {

        List<UIComponent> children = panelCollSearch.getChildren();
        UIComponent component = children.get(0);

        RichTable rpt = null;
        for (int i = 0; i < children.size(); i++) {
            component = children.get(i);

            if (component.getId().equalsIgnoreCase("t11")) {
                rpt = (RichTable)component;
            }
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.serviceRequestsProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            Object key2 = rpt.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {

                // Set the country code to be used to fetch the towns
                session.setAttribute("clntCode",
                                     new BigDecimal(nodeBinding.getAttribute("code").toString()));
                session.setAttribute("preferredCommMode",
                                     nodeBinding.getAttribute("defaultModeOfComm"));
                session.setAttribute("preferredTel",
                                     nodeBinding.getAttribute("sms"));
                session.setAttribute("preferredEmail",
                                     nodeBinding.getAttribute("email"));
                /*stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "A");
                stmt.registerOutParameter(2, OracleTypes.NUMBER);
                stmt.setInt(3, 1);
                stmt.setString(4, "Enquiry");
                stmt.setString(5, "C");
                stmt.setObject(6, session.getAttribute("clntCode"));
                stmt.setObject(7, GlobalCC.parseDate(new Date()));
                stmt.setBigDecimal(8,
                                   (BigDecimal)session.getAttribute("UserCode"));
                stmt.setString(9, "U");
                stmt.setBigDecimal(10,
                                   (BigDecimal)session.getAttribute("UserCode"));
                stmt.setString(11, "Open");
                stmt.setObject(12, GlobalCC.parseDate(new Date()));
                stmt.setObject(13, GlobalCC.parseDate(new Date()));
                stmt.setString(14, "Enquiry");
                stmt.setString(15, "Enquiry");
                stmt.setString(16, null);
                stmt.execute();
                session.setAttribute("tsrCode", stmt.getBigDecimal(2));
                stmt.close();
                conn.commit();
                conn.close();*/

                shtDesc.setValue(nodeBinding.getAttribute("shortDesc"));
                if (nodeBinding.getAttribute("name") == null) {
                    nameDesc.setValue(nodeBinding.getAttribute("name"));
                } else if (nodeBinding.getAttribute("otherNames") == null) {
                    nameDesc.setValue(nodeBinding.getAttribute("otherNames"));
                } else {
                    nameDesc.setValue(nodeBinding.getAttribute("name") + " " +
                                      nodeBinding.getAttribute("otherNames"));
                }
                session.setAttribute("TSR_POLICY_NO",
                                     nodeBinding.getAttribute("shortDesc"));
                session.setAttribute("accountName", nameDesc.getValue());
                session.setAttribute("accountCode",
                                     session.getAttribute("clntCode"));
                session.setAttribute("accountCodeVal",
                                     session.getAttribute("clntCode"));

                addressDesc.setValue(nodeBinding.getAttribute("physicalAddress"));
                accNoDesc.setValue(nodeBinding.getAttribute("accountNum"));
                emailDesc.setValue(nodeBinding.getAttribute("email"));
                telDesc.setValue(nodeBinding.getAttribute("phone1"));
                searchCriteria.setRendered(false);
                acc360.setRendered(true);
                ADFUtils.findIterator("fetchRequestPoliciesIterator").executeQuery();
                GlobalCC.refreshUI(policiesTbl);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setNameDesc(RichOutputText nameDesc) {
        this.nameDesc = nameDesc;
    }

    public RichOutputText getNameDesc() {
        return nameDesc;
    }

    public void setShtDesc(RichOutputText shtDesc) {
        this.shtDesc = shtDesc;
    }

    public RichOutputText getShtDesc() {
        return shtDesc;
    }

    public void setAccNoDesc(RichOutputText accNoDesc) {
        this.accNoDesc = accNoDesc;
    }

    public RichOutputText getAccNoDesc() {
        return accNoDesc;
    }

    public void setTelDesc(RichOutputText telDesc) {
        this.telDesc = telDesc;
    }

    public RichOutputText getTelDesc() {
        return telDesc;
    }

    public void setEmailDesc(RichOutputText emailDesc) {
        this.emailDesc = emailDesc;
    }

    public RichOutputText getEmailDesc() {
        return emailDesc;
    }

    public void setAddressDesc(RichOutputText addressDesc) {
        this.addressDesc = addressDesc;
    }

    public RichOutputText getAddressDesc() {
        return addressDesc;
    }

    public void setIndividialTab(RichTable individialTab) {
        this.individialTab = individialTab;
    }

    public RichTable getIndividialTab() {
        return individialTab;
    }

    public void setGroupTab(RichTable groupTab) {
        this.groupTab = groupTab;
    }

    public RichTable getGroupTab() {
        return groupTab;
    }

    public void setGeneralTab(RichTable generalTab) {
        this.generalTab = generalTab;
    }

    public RichTable getGeneralTab() {
        return generalTab;
    }

    public String newCase() {

        session.setAttribute("action", "A");
        serCat.setValue(null);
        serCat.setLabel(null);
        reqSource.setValue(null);
        reqAccType.setValue(session.getAttribute("accountType"));
        reqAccount.setValue(session.getAttribute("accountName"));


        reqSummary.setValue(null);
        reqDesc.setValue(null);
        reqDate.setValue(new Date());

        // Reference CRM-1502
        receiveDate.setValue(new Date());
        // Reference CRM-1502

        reqOwnerAccount.setValue(session.getAttribute("Username"));
        reqOwnerAccount.setLabel(session.getAttribute("UserCode").toString());
        reqReporter.setValue(session.getAttribute("Username"));
        reqReporter.setLabel(session.getAttribute("UserCode").toString());
        reqOwnerAccType.setValue("U");
        // reqOwnerAccount.setValue(session.getAttribute("Username"));
        //reqOwnerAccount.setLabel(session.getAttribute("UserCode").toString());
        reqDueDate.setValue(new Date());
        captureDate.setValue(new Date());
        reqStatus.setValue("Open");
        reqResDate.setValue(null);
        reqSolution.setValue(null);
        reqDate.setDisabled(false);
        reqResDate.setDisabled(true);

        if (session.getAttribute("preferredCommMode") != null &&
            session.getAttribute("accountType").toString().equalsIgnoreCase("C")) {
            reqCommMode.setValue(session.getAttribute("preferredCommMode").toString());
            txtSecondaryCommMode.setValue(session.getAttribute("preferredCommMode").toString());
            GlobalCC.refreshUI(reqCommMode);
            GlobalCC.refreshUI(txtSecondaryCommMode);

            if (session.getAttribute("preferredCommMode").toString().equalsIgnoreCase("EMAIL")) {
                if (session.getAttribute("preferredEmail") != null) {
                    primaryCommMode.setValue(session.getAttribute("preferredEmail"));
                    secondaryCommMode.setValue(session.getAttribute("preferredEmail"));
                    GlobalCC.refreshUI(primaryCommMode);
                    GlobalCC.refreshUI(secondaryCommMode);
                }
            }

            if (session.getAttribute("preferredCommMode").toString().equalsIgnoreCase("SMS")) {
                if (session.getAttribute("preferredTel") != null) {
                    primaryCommMode.setValue(session.getAttribute("preferredTel"));
                    secondaryCommMode.setValue(session.getAttribute("preferredTel"));
                    GlobalCC.refreshUI(primaryCommMode);
                    GlobalCC.refreshUI(secondaryCommMode);
                }
            }


        }  else if (session.getAttribute("preferrComMod") != null &&
            session.getAttribute("accountType").toString().equalsIgnoreCase("A")) {
            reqCommMode.setValue(session.getAttribute("preferrComMod").toString());
            txtSecondaryCommMode.setValue(session.getAttribute("preferrComMod").toString());
            GlobalCC.refreshUI(reqCommMode);
            GlobalCC.refreshUI(txtSecondaryCommMode);

            if (session.getAttribute("preferrComMod").toString().equalsIgnoreCase("EMAIL")) {
                if (session.getAttribute("preferEmail") != null) {
                    primaryCommMode.setValue(session.getAttribute("preferEmail"));
                    secondaryCommMode.setValue(session.getAttribute("preferEmail"));
                    GlobalCC.refreshUI(primaryCommMode);
                    GlobalCC.refreshUI(secondaryCommMode);
                }
            }

            if (session.getAttribute("preferrComMod").toString().equalsIgnoreCase("SMS")) {
                if (session.getAttribute("preferTel") != null) {
                    primaryCommMode.setValue(session.getAttribute("preferTel"));
                    secondaryCommMode.setValue(session.getAttribute("preferTel"));
                    GlobalCC.refreshUI(primaryCommMode);
                    GlobalCC.refreshUI(secondaryCommMode);
                }
            }


        } else {

            reqCommMode.setValue(null);
            txtSecondaryCommMode.setValue(null);
            primaryCommMode.setValue(null);
            secondaryCommMode.setValue(null);
            GlobalCC.refreshUI(reqCommMode);
            GlobalCC.refreshUI(txtSecondaryCommMode);
            GlobalCC.refreshUI(primaryCommMode);
            GlobalCC.refreshUI(secondaryCommMode);
        }


        // Reference CRM-1502
        // receiveDate.setValue(null);
        // Reference CRM-1502

        txtComments.setValue(null);
        txtType.setValue(null);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:servRequest" + "').show(hints);");
        return null;
    }

    public void setSerCat(RichInputText serCat) {
        this.serCat = serCat;
    }

    public RichInputText getSerCat() {
        return serCat;
    }

    public void setReqSource(RichSelectOneChoice reqSource) {
        this.reqSource = reqSource;
    }

    public RichSelectOneChoice getReqSource() {
        return reqSource;
    }

    public void setReqAccType(RichSelectOneChoice reqAccType) {
        this.reqAccType = reqAccType;
    }

    public RichSelectOneChoice getReqAccType() {
        return reqAccType;
    }

    public void setReqAccount(RichInputText reqAccount) {
        this.reqAccount = reqAccount;
    }

    public RichInputText getReqAccount() {
        return reqAccount;
    }

    public void setReqSummary(RichInputText reqSummary) {
        this.reqSummary = reqSummary;
    }

    public RichInputText getReqSummary() {
        return reqSummary;
    }

    public void setReqDesc(RichInputText reqDesc) {
        this.reqDesc = reqDesc;
    }

    public RichInputText getReqDesc() {
        return reqDesc;
    }

    public void setReqDate(RichInputDate reqDate) {
        this.reqDate = reqDate;
    }

    public RichInputDate getReqDate() {
        return reqDate;
    }

    public void setReqAssignee(RichInputText reqAssignee) {
        this.reqAssignee = reqAssignee;
    }

    public RichInputText getReqAssignee() {
        return reqAssignee;
    }

    public void setReqOwnerAccType(RichSelectOneChoice reqOwnerAccType) {
        this.reqOwnerAccType = reqOwnerAccType;
    }

    public RichSelectOneChoice getReqOwnerAccType() {
        return reqOwnerAccType;
    }

    public void setReqOwnerAccount(RichInputText reqOwnerAccount) {
        this.reqOwnerAccount = reqOwnerAccount;
    }

    public RichInputText getReqOwnerAccount() {
        return reqOwnerAccount;
    }

    public void setReqDueDate(RichInputDate reqDueDate) {
        this.reqDueDate = reqDueDate;
    }

    public RichInputDate getReqDueDate() {
        return reqDueDate;
    }

    public void setReqStatus(RichSelectOneChoice reqStatus) {
        this.reqStatus = reqStatus;
    }

    public RichSelectOneChoice getReqStatus() {
        return reqStatus;
    }

    public void setReqResDate(RichInputDate reqResDate) {
        this.reqResDate = reqResDate;
    }

    public RichInputDate getReqResDate() {
        return reqResDate;
    }

    public void setReqSolution(RichInputText reqSolution) {
        this.reqSolution = reqSolution;
    }

    public RichInputText getReqSolution() {
        return reqSolution;
    }

    public String categorySelected() {
        Object key = categories.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        serCat.setValue(r.getAttribute("tsrcName"));
        serCat.setLabel(r.getAttribute("tsrcCode").toString());

        String reqDateVal = GlobalCC.checkNullValues(reqDate.getValue());

        //        if (reqDateVal.contains(":")) {
        //            reqDateVal =
        //                    GlobalCC.addDate(reqDateVal, new Integer(r.getAttribute("tsrcValidity").toString()));
        //        } else {
        //            reqDateVal =
        //                    GlobalCC.addDateTwo(reqDateVal, new Integer(r.getAttribute("tsrcValidity").toString()));
        //
        //        }
        //reqDueDate.setValue(reqDateVal);
        GlobalCC.refreshUI(serCat);
        //   GlobalCC.refreshUI(reqDueDate);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:category" +
                             "').hide(hints);");
        return null;
    }

    public String cancelCategory() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categoryType" + "').hide(hints);");
        return null;
    }

    public void setUsers(RichTable users) {
        this.users = users;
    }

    public RichTable getUsers() {
        return users;
    }

    public String cancelUser() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:users" +
                             "').hide(hints);");
        return null;
    }

    public String userSelected() {
        Object key = users.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        reqAssignee.setValue(r.getAttribute("username"));
        reqAssignee.setLabel(r.getAttribute("userCode").toString());
        GlobalCC.refreshUI(reqAssignee);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:users" +
                             "').hide(hints);");
        return null;
    }

    public void setAccounts(RichTable accounts) {
        this.accounts = accounts;
    }

    public RichTable getAccounts() {
        return accounts;
    }

    public String saveOwner() {
        Object key = accounts.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        reqOwnerAccount.setValue(r.getAttribute("accountName"));
        if (r.getAttribute("accountCode") != null) {
            reqOwnerAccount.setLabel(r.getAttribute("accountCode").toString());
        }

        GlobalCC.refreshUI(reqOwnerAccount);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:accounts" +
                             "').hide(hints);");
        return null;
    }


    public String saveReporter() {
        Object key = reporterAcc.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        if (r != null) {
            reqReporter.setValue(r.getAttribute("accountName"));
            //                reqReporter.setLabel(r.getAttribute("accountCode").toString());
        }

        GlobalCC.refreshUI(reqReporter);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:reporterAccounts" + "').hide(hints);");

        return null;
    }


    public String cancelReporter() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:reporterAccounts" + "').hide(hints);");
        return null;
    }

    public String cancelAccount() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:accounts" +
                             "').hide(hints);");
        return null;
    }

    public String launchAccount() {
        String ownType = GlobalCC.checkNullValues(reqOwnerAccType.getValue());
        if (ownType == null) {
            ownType = "U";
        }
        session.setAttribute("type", ownType);
        ADFUtils.findIterator("findAccountsIterator").executeQuery();
        GlobalCC.refreshUI(accounts);

        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor)accounts.getFilterModel();
        Set featureSet = new HashSet();
        featureSet.add(FilterableQueryDescriptor.FilterFeature.CASE_INSENSITIVE);
        Map filterFeatures = new HashMap();
        ((HashMap)filterFeatures).put("accountType", featureSet);
        ((HashMap)filterFeatures).put("accountName", featureSet);
        queryDescriptor.setFilterFeatures(filterFeatures);
        HashMap<String, Object> criteris = new HashMap<String, Object>();
        criteris.put("accountName", "%Search%");
        queryDescriptor.setFilterCriteria(criteris);
        accounts.setFilterVisible(true);
        accounts.queueEvent(new QueryEvent(accounts, queryDescriptor));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:accounts" +
                             "').show(hints);");
        return null;
    }


    public String launchReport() {

        String ownType = GlobalCC.checkNullValues(reqOwnerAccType.getValue());
        if (ownType == null) {
            ownType = "U";
        }
        session.setAttribute("type", ownType);
        ADFUtils.findIterator("findAccountsIterator").executeQuery();
        GlobalCC.refreshUI(reporterAcc);

        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor)reporterAcc.getFilterModel();
        Set featureSet = new HashSet();
        featureSet.add(FilterableQueryDescriptor.FilterFeature.CASE_INSENSITIVE);
        Map filterFeatures = new HashMap();
        ((HashMap)filterFeatures).put("accountType", featureSet);
        ((HashMap)filterFeatures).put("accountName", featureSet);
        queryDescriptor.setFilterFeatures(filterFeatures);
        HashMap<String, Object> criteris = new HashMap<String, Object>();
        criteris.put("accountName", "%Search%");
        queryDescriptor.setFilterCriteria(criteris);
        reporterAcc.setFilterVisible(true);
        reporterAcc.queueEvent(new QueryEvent(reporterAcc, queryDescriptor));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:reporterAccounts" + "').show(hints);");
        return null;
    }

    public String launchAssignee() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:users" +
                             "').show(hints);");
        return null;
    }

    public String launchCategory() {
        if (txtType.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Please select category Type first");
            return null;
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:category" +
                             "').show(hints);");
        return null;
    }

    public void statusChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Closed")) {
                closedBy.setValue(session.getAttribute("Username").toString());
                GlobalCC.refreshUI(closedBy);
                reqResDate.setValue(new java.util.Date());
                reqResDate.setDisabled(true);
                reqSolution.setDisabled(false);
            } else {
                reqResDate.setDisabled(true);
                reqSolution.setDisabled(false);
                closedBy.setValue(null);
                GlobalCC.refreshUI(closedBy);
            }
            GlobalCC.refreshUI(reqSolution);
            GlobalCC.refreshUI(reqResDate);


        }
    }

    public String saveServiceReq() {

        String policyNoVal =
            GlobalCC.checkNullValues(txtTsrPolicyNo.getValue());
        BigDecimal catVal = GlobalCC.checkBDNullValues(serCat.getLabel());

        String typeVal = GlobalCC.checkNullValues(txtType.getValue());
        String source = GlobalCC.checkNullValues(reqSource.getValue());
        String accType = GlobalCC.checkNullValues(reqAccType.getValue());
        BigDecimal accCode = (BigDecimal)session.getAttribute("accountCode");
        String summary = GlobalCC.checkNullValues(reqSummary.getValue());
        String desc = GlobalCC.checkNullValues(reqDesc.getValue());
        String assignee = GlobalCC.checkNullValues(reqAssignee.getValue());
        String ownType = GlobalCC.checkNullValues(reqOwnerAccType.getValue());
        String owner = GlobalCC.checkNullValues(reqOwnerAccount.getValue());
        String reporter = GlobalCC.checkNullValues(reqReporter.getValue());
        String dueDate = GlobalCC.checkNullValues(reqDueDate.getValue());
        String statusVal = GlobalCC.checkNullValues(reqStatus.getValue());
        String resDate = GlobalCC.checkNullValues(reqResDate.getValue());
        String solu = GlobalCC.checkNullValues(reqSolution.getValue());
        String rcvDate = GlobalCC.checkNullValues(receiveDate.getValue());
        Rendering renderer = new Rendering();
        BigDecimal catValue;

        if (policyNoVal == null && "C".equalsIgnoreCase(accType) &&
            renderer.isSERVICE_POLICY_NO_VISIBLE() &&
            renderer.isSERVICE_POLICY_NO_REQUIRED()) { //only for client
            GlobalCC.errorValueNotEntered("Select Policy No!");
            return null;
        }
        if (typeVal == null) {
            GlobalCC.errorValueNotEntered("Select Category!");
            return null;
        }

        if (source == null) {
            GlobalCC.errorValueNotEntered("Select Source!");
            return null;
        }


        if (summary == null) {
            GlobalCC.errorValueNotEntered("Enter Summary!");
            return null;
        }

        if (desc == null) {
            GlobalCC.errorValueNotEntered("Enter Description");
            return null;
        }

        if (rcvDate == null && renderer.isSERVICE_RECEIVE_DATE_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Select Receive Date!");
            return null;
        }
        if (assignee == null) {
            GlobalCC.errorValueNotEntered("Select Assignee");
            return null;
        }
        BigDecimal asignee = null;
        if (reqAssignee.getLabel() != null) {
            asignee = new BigDecimal(reqAssignee.getLabel());
        }
        //assignee = reqAssignee.getLabel();

        if (ownType == null) {
            GlobalCC.errorValueNotEntered("Select Owner Type");
            return null;
        }

        if (owner == null) {
            GlobalCC.errorValueNotEntered("Select Owner");
            return null;
        }

        // CRM-1502
        FormUi formUi = new FormUi();
        if (!formUi.validate("serviceRequestTab")) { //main validation engine
            return null;
        }
        // END. CRM-1502

        owner = GlobalCC.checkNullValues(reqOwnerAccount.getLabel());
        BigDecimal ownerr = null;
        if (owner != null)
            ownerr = new BigDecimal(owner);

        String reqDateVal = GlobalCC.checkNullValues(reqDate.getValue());
        //        if (reqDateVal.contains(":")) {
        //            reqDateVal = GlobalCC.parseDateTime(reqDateVal);
        //        } else {
        //            reqDateVal = GlobalCC.upDateParseDateTime(reqDateVal);
        //        }

        if (dueDate.contains(":")) {
            dueDate = GlobalCC.parseDate(dueDate);
        } else if (dueDate.contains("/")) {
            dueDate = GlobalCC.upDateParseDateTwo(dueDate);
        } else {
            dueDate = GlobalCC.upDateParseDate(dueDate);
        }

        if (statusVal == null) {
            GlobalCC.errorValueNotEntered("Select Status");
            return null;
        }

        if (statusVal.equalsIgnoreCase("Closed")) {

            //String assigneer = null;
           //String reporterr = null;
            
            if (reqReporter.getValue() != null) {
                reporter = reqReporter.getValue().toString();
            }
            if (reqAssignee.getValue() != null) {
                assignee = reqAssignee.getValue().toString();
            }

            if (reqReporter.getValue() != null ||
                reqAssignee.getValue() != null) {

//                if (!session.getAttribute("Username").toString().equalsIgnoreCase(reporter) &&
//                    !session.getAttribute("Username").toString().equalsIgnoreCase(assignee)) {
//                    GlobalCC.INFORMATIONREPORTING("you are not permitted to close this request");
//                    return null;
//                }
            } 
            String username = GlobalCC.checkNullValues(session.getAttribute("Username"));
            String closedByAssignee = GlobalCC.checkNullValues(session.getAttribute("closedByAssignee"));
            
            if (username.equalsIgnoreCase(reporter) && closedByAssignee == null){
                GlobalCC.INFORMATIONREPORTING("The initiator/reporter can only close the ticket if the assignee has closed it.");
                return null;
            }
            
            if (resDate == null) {
                GlobalCC.errorValueNotEntered("Enter Resolution Date");
                return null;
            }
            if (resDate.contains(":")) {
                resDate = GlobalCC.parseDate(resDate);
            } else {
                resDate = GlobalCC.upDateParseDate(resDate);
            }
            if (solu == null) {
                GlobalCC.errorValueNotEntered("Enter Solution");
                return null;
            }
        }
        String commValue = GlobalCC.checkNullValues(reqCommMode.getValue());
        if (commValue == null) {
            GlobalCC.errorValueNotEntered("Select Preferred Communication Mode");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.serviceRequestsProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, session.getAttribute("action"));
            stmt.setBigDecimal(2, (BigDecimal)session.getAttribute("tsrCode"));
            stmt.setBigDecimal(3, catVal);
            stmt.setString(4, source);
            stmt.setObject(5, session.getAttribute("accountType"));
            stmt.setObject(6, session.getAttribute("accountCodeVal"));
            stmt.setTimestamp(7, GlobalCC.extractTime(reqDate));
            stmt.setBigDecimal(8, asignee);
            stmt.setString(9, ownType);
            stmt.setBigDecimal(10, ownerr);
            stmt.setString(11, statusVal);
            stmt.setDate(12, GlobalCC.extractDate(reqDueDate));
            stmt.setDate(13, null);
            stmt.setString(14, summary);
            stmt.setString(15, desc);
            stmt.setString(16, solu);
            stmt.setString(17, null);
            stmt.setString(18, commValue);
            stmt.setObject(19, txtComments.getValue());
            stmt.setObject(20, session.getAttribute("srtCode"));
            stmt.setObject(21, txtSecondaryCommMode.getValue());
            stmt.setObject(21, txtSecondaryCommMode.getValue());
            stmt.setObject(22, policyNoVal);
            stmt.setDate(23, GlobalCC.extractDate(receiveDate));
            stmt.setString(24, reporter);
            stmt.setDate(25, GlobalCC.extractDate(captureDate));
            stmt.setObject(26, closedBy.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();

            GlobalCC.sysInformation("Service Request Updated.");

            GlobalCC.hidePopup("crm:servRequest");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        //Either Way pull the Table
        if (allRequests != null) {
            //ADFUtils.findIterator("findClientPendingRequestsIterator").executeQuery();
            ADFUtils.findIterator("findClientRequestsIterator").executeQuery();
            ADFUtils.findIterator("findClientPendingRequestsIterator").executeQuery();
            GlobalCC.refreshUI(allRequests);
            GlobalCC.refreshUI(clientPending);
        }
        return null;
    }

    public void setClientPending(RichTable clientPending) {
        this.clientPending = clientPending;
    }

    public RichTable getClientPending() {
        return clientPending;
    }

    public String viewClientPending() {
        Object key = clientPending.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        session.setAttribute("action", "E");
        serCat.setValue(r.getAttribute("tsrcName"));
        serCat.setLabel(r.getAttribute("tsrcCode").toString());
        session.setAttribute("tsrCode", r.getAttribute("tsrCode"));
        reqSource.setValue(r.getAttribute("tsrType"));
        reqAccType.setValue(r.getAttribute("accType"));
        session.setAttribute("accountCode", r.getAttribute("accCode"));

        session.setAttribute("accountName", r.getAttribute("accountName"));
        reqAccount.setValue(session.getAttribute("accountName"));
        reqSummary.setValue(r.getAttribute("summary"));
        reqDesc.setValue(r.getAttribute("desc"));
        reqDate.setValue(r.getAttribute("tsrDate"));
        reqAssignee.setValue(r.getAttribute("assigneeDesc"));
        reqAssignee.setLabel(r.getAttribute("assignee").toString());
        reqOwnerAccType.setValue(r.getAttribute("ownType"));
        reqOwnerAccount.setValue(r.getAttribute("owner"));
        reqOwnerAccount.setLabel(r.getAttribute("ownerCode").toString());
        reqDueDate.setValue(r.getAttribute("dueDate"));
        reqStatus.setValue(r.getAttribute("status"));
        reqResDate.setValue(r.getAttribute("dueDate"));
        reqSolution.setValue(r.getAttribute("solution"));
        txtComments.setValue(r.getAttribute("comments"));
        
        if(reqReporter !=null)
            reqReporter.setValue(r.getAttribute("tsr_reporter"));
        
        if (receiveDate != null) {
            receiveDate.setValue(r.getAttribute("TSR_RECEIVE_DATE"));
        }
        if (txtTsrPolicyNo != null)
            txtTsrPolicyNo.setValue(r.getAttribute("TSR_POLICY_NO"));
        session.setAttribute("reqRefNo", r.getAttribute("requestRefNumber"));
        session.setAttribute("srtCode", r.getAttribute("tsrSrtCode"));
        //reqSolution.setValue(r.getAttribute("tsrSrtCode"));
        if (r.getAttribute("srtDesc") != null) {
            txtType.setValue(r.getAttribute("srtDesc"));
        } else {
            txtType.setValue(null);
        }
        if (r.getAttribute("status").toString().equalsIgnoreCase("Open")) {
            reqResDate.setDisabled(true);
            reqSolution.setDisabled(false);
        } else {
            reqResDate.setDisabled(false);
            reqSolution.setDisabled(false);
        }
        
        session.setAttribute("closedByAssignee", r.getAttribute("closedByAssignee"));
        session.setAttribute("closedByReporter", r.getAttribute("closedByReporter"));
        session.setAttribute("closedBy", r.getAttribute("closedBy"));
        
        session.setAttribute("accountCodeVal", r.getAttribute("accCode"));
        GlobalCC.showPopup("crm:servRequest");
        return null;
    }

    public void setAllRequests(RichTable allRequests) {
        this.allRequests = allRequests;
    }

    public RichTable getAllRequests() {
        return allRequests;
    }

    public String allRequests() {
        Object key = allRequests.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        txtType.setValue(r.getAttribute("srtDesc"));
        serCat.setValue(r.getAttribute("tsrcName"));
        serCat.setLabel(r.getAttribute("tsrcCode").toString());
        session.setAttribute("tsrCode", r.getAttribute("tsrCode"));
        reqSource.setValue(r.getAttribute("tsrType"));
        reqAccType.setValue(r.getAttribute("accType"));
        session.setAttribute("accountCode", r.getAttribute("accCode"));
        session.setAttribute("accountName", r.getAttribute("accountName"));
        reqAccount.setValue(session.getAttribute("accountName"));
        reqSummary.setValue(r.getAttribute("summary"));
        reqDesc.setValue(r.getAttribute("desc"));
        reqDate.setValue(r.getAttribute("tsrDate"));
        reqAssignee.setValue(r.getAttribute("assigneeDesc"));
        if (r.getAttribute("assignee") != null) {
            reqAssignee.setLabel(r.getAttribute("assignee").toString());
        }

        reqOwnerAccType.setValue(r.getAttribute("ownType"));
        reqOwnerAccount.setValue(r.getAttribute("owner"));
        reqOwnerAccount.setLabel(r.getAttribute("ownerCode").toString());
        reqDueDate.setValue(new Date());

        reqCommMode.setValue(r.getAttribute("tsrMode"));
        txtSecondaryCommMode.setValue(r.getAttribute("tsrMode"));

        reqStatus.setValue(r.getAttribute("tsrMode"));
        reqResDate.setValue(r.getAttribute("dueDate"));
        reqSolution.setValue(r.getAttribute("solution"));
        txtTsrPolicyNo.setValue(r.getAttribute("TSR_POLICY_NO"));
        if (receiveDate != null) {
            receiveDate.setValue(r.getAttribute("TSR_RECEIVE_DATE"));
        }

        reqCommMode.setDisabled(true);
        txtSecondaryCommMode.setDisabled(true);

        btnSaveServiceReq.setDisabled(true);
        reqSource.setDisabled(true);
        serCat.setDisabled(true);
        txtType.setDisabled(true);
        reqAccType.setDisabled(true);
        reqAccount.setDisabled(true);
        reqSummary.setDisabled(true);
        reqDesc.setDisabled(true);
        reqDate.setDisabled(true);
        reqAssignee.setDisabled(true);
        reqOwnerAccType.setDisabled(true);
        reqOwnerAccount.setDisabled(true);
        reqDueDate.setDisabled(true);
        reqStatus.setDisabled(true);
        reqResDate.setDisabled(true);
        reqSolution.setDisabled(true);
        txtComments.setDisabled(true);

        GlobalCC.showPopup("crm:servRequest");
        return null;
    }

    public void setOutServ(RichInputText outServ) {
        this.outServ = outServ;
    }

    public RichInputText getOutServ() {
        return outServ;
    }

    public void setOutHost(RichInputText outHost) {
        this.outHost = outHost;
    }

    public RichInputText getOutHost() {
        return outHost;
    }

    public void setOutPort(RichInputNumberSpinbox outPort) {
        this.outPort = outPort;
    }

    public RichInputNumberSpinbox getOutPort() {
        return outPort;
    }

    public void setOutUser(RichInputText outUser) {
        this.outUser = outUser;
    }

    public RichInputText getOutUser() {
        return outUser;
    }

    public void setOutPasword(RichInputText outPasword) {
        this.outPasword = outPasword;
    }

    public RichInputText getOutPasword() {
        return outPasword;
    }

    public void setOutEmail(RichInputText outEmail) {
        this.outEmail = outEmail;
    }

    public RichInputText getOutEmail() {
        return outEmail;
    }

    public String saveOut() {

        String server = GlobalCC.checkNullValues(outServ.getValue());
        String host = GlobalCC.checkNullValues(outHost.getValue());
        String port = GlobalCC.checkNullValues(outPort.getValue());
        String user = GlobalCC.checkNullValues(outUser.getValue());
        String pass = GlobalCC.checkNullValues(outPasword.getValue());
        String email = GlobalCC.checkNullValues(outEmail.getValue());
        String type = GlobalCC.checkNullValues(outType.getValue());
        String secure = "N";
        if (outSecure.isSelected()) {
            secure = "Y";
        } else {
            secure = "N";
        }
        if (type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Type");
            return null;
        }
        if (server == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Server");
            return null;
        }
        if (host == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Host");
            return null;
        }
        if (email == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From Email");
            return null;
        }

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.outgoingMailProc(?,?,?,?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, type);
            stmt.setString(2, secure);
            stmt.setString(3, server);
            stmt.setString(4, host);
            stmt.setString(5, port);
            stmt.setString(6, user);
            stmt.setString(7, pass);
            stmt.setString(8, email);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Successfully Updated");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public String testOut() {
        String server = GlobalCC.checkNullValues(outServ.getValue());
        String host = GlobalCC.checkNullValues(outHost.getValue());
        String port = GlobalCC.checkNullValues(outPort.getValue());
        String user = GlobalCC.checkNullValues(outUser.getValue());
        String pass = GlobalCC.checkNullValues(outPasword.getValue());
        String email = GlobalCC.checkNullValues(outEmail.getValue());
        String type = GlobalCC.checkNullValues(outType.getValue());
        String secure = "N";
        if (outSecure.isSelected()) {
            secure = "Y";
        } else {
            secure = "N";
        }
        if (type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Type");
            return null;
        }
        if (server == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Server");
            return null;
        }
        if (host == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Host");
            return null;
        }
        if (email == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From Email");
            return null;
        }
        try {
            System.out.println(type);
            String success = null;
            if (type.equalsIgnoreCase("GOOGLE")) {
                GlobalCC mail = new GlobalCC();
                //String success = null;
                // success =
                //        mail.applicationEmail(email, null, "Test Email", "This is an Email Test from TurnQuest");
                GlobalCC globalCC = new GlobalCC();
                success =
                        globalCC.applicationEmail(email, null, "Test Email", "This is a test email from TurnQuest");
                if (success != null) {
                    GlobalCC.sysInformation("Test Email Sussessfully sent to:" +
                                            email);
                }
                return null;
            }


            /*Properties props = new Properties();
            Session mailSession = null;
            System.out.println(user);
            System.out.println(pass);
            if (user != null && pass != null && user != "" && pass != "") {
                Authenticator auth = new MailAuthenticator();
                System.out.println("Authenticate");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.trust", "*");

                mailSession = Session.getInstance(props, auth);
            } else {
                //Authenticator auth = new MailAuthenticator();
                // System.out.println("Authenticate");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                //props.put("mail.smtp.starttls.enable", "true");
                //props.put("mail.smtp.auth", "true");
                // props.put("mail.smtp.ssl.trust", "*");

                mailSession = Session.getInstance(props);
            }

            Message message = new MimeMessage(mailSession);
            System.out.println("message");
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(email));
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("This is a test Message", "text/html");
            mp.addBodyPart(htmlPart);
            // mp.
            message.setSubject("TEST Message");
            //message.setText(content);
            message.setContent(mp);
            Transport.send(message);*/
            GlobalCC globalCC = new GlobalCC();
            success =
                    globalCC.applicationEmail(email, null, "Test Email", "This is an Email Test from TurnQuest");
            if (success.equalsIgnoreCase("success")) {
                GlobalCC.sysInformation("Test Email Sussessfully sent to:" +
                                        email);
            }

        } catch (Exception e) {

            GlobalCC.EXCEPTIONREPORTING(e);

        }

        return null;
    }

    public String saveIn() {
        String type = GlobalCC.checkNullValues(inType.getValue());
        String server = GlobalCC.checkNullValues(inServ.getValue());
        String host = GlobalCC.checkNullValues(inHost.getValue());
        String port = GlobalCC.checkNullValues(inPort.getValue());
        String user = GlobalCC.checkNullValues(inUse.getValue());
        String pass = GlobalCC.checkNullValues(inPass.getValue());
        String secure = "N";
        if (inSecure.isSelected()) {
            secure = "Y";
        } else {
            secure = "N";
        }
        if (type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Type");
            return null;
        }
        if (server == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Server");
            return null;
        }
        if (host == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Host");
            return null;
        }
        if (port == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Port");
            return null;
        }
        if (user == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From User");
            return null;
        }
        if (pass == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From Password");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.incomingMailProc(?,?,?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, secure);
            stmt.setString(2, server);
            stmt.setString(3, host);
            stmt.setString(4, port);
            stmt.setString(5, user);
            stmt.setString(6, pass);
            stmt.setString(7, type);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Successfully Updated");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public String testIn() {
        String type = GlobalCC.checkNullValues(inType.getValue());
        String server = GlobalCC.checkNullValues(inServ.getValue());
        String host = GlobalCC.checkNullValues(inHost.getValue());
        String port = GlobalCC.checkNullValues(inPort.getValue());
        String user = GlobalCC.checkNullValues(inUse.getValue());
        String pass = GlobalCC.checkNullValues(inPass.getValue());
        String secure = "N";
        if (inSecure.isSelected()) {
            secure = "Y";
        } else {
            secure = "N";
        }
        if (type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Type");
            return null;
        }
        if (server == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Server");
            return null;
        }
        if (host == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Host");
            return null;
        }
        if (port == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Port");
            return null;
        }
        if (user == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From User");
            return null;
        }
        if (pass == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:From Password");
            return null;
        }
        Session session = null;
        Store store = null;
        Folder folder = null;

        try {
            System.out.println("--------------processing mails started-----------------");
            session = Session.getDefaultInstance(System.getProperties(), null);
            System.out.println("getting the session for accessing email.");
            //store = session.getStore("pop3");
            Properties props = System.getProperties();
            if (secure.equalsIgnoreCase("Y")) {
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            }

            if (type.equalsIgnoreCase("imap")) {
                if (secure.equalsIgnoreCase("Y")) {
                    props.setProperty("mail.imap.socketFactory.class",
                                      "javax.net.ssl.SSLSocketFactory");
                    props.setProperty("mail.imap.ssl", "true");
                } else {
                    props.setProperty("mail.imap.ssl", "false");
                }
                props.setProperty("mail.imap.socketFactory.fallback", "false");
                props.setProperty("mail.imap.port", port);
                props.setProperty("mail.imap.socketFactory.port", port);
                props.setProperty("mail.imap.host", host);

            }
            props.setProperty("protocal", type);
            props.setProperty("host", host);
            props.setProperty("port", port);
            int portVal = Integer.parseInt(props.getProperty("port"));
            session = Session.getDefaultInstance(props, null);
            // store = session.getStore(type);
            // URLName url = new URLName()
            URLName urln =
                new URLName(props.getProperty("protocal"), props.getProperty("host"),
                            portVal, null, user, pass);

            // URLName urln = new URLName(props.getProperty("protocal"), props.getProperty("host"), port, null, user, pass);
            System.out.println("Started connecting to mail inbox ... ");
            if (type.equalsIgnoreCase("imap")) {
                store = new IMAPSSLStore(session, urln);
            } else {
                store = new POP3SSLStore(session, urln);
            }
            //store.connect("imap.gmail.com",993, "turnquestservices@gmail.com", "afoguarantor");
            store.connect(host, new Integer(port), user, pass);
            System.out.println("Connection established with IMAP server.");
            // Get a handle on the default folder
            folder = store.getDefaultFolder();
            System.out.println("Getting the Inbox folder.");
            // Retrieve the "Inbox"
            folder = folder.getFolder("inbox");
            //Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_WRITE);
            folder.close(true);
            store.close();
            GlobalCC.sysInformation("Test Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setInType(RichSelectOneChoice inType) {
        this.inType = inType;
    }

    public RichSelectOneChoice getInType() {
        return inType;
    }

    public void setInServ(RichInputText inServ) {
        this.inServ = inServ;
    }

    public RichInputText getInServ() {
        return inServ;
    }

    public void setInHost(RichInputText inHost) {
        this.inHost = inHost;
    }

    public RichInputText getInHost() {
        return inHost;
    }

    public void setInPort(RichInputNumberSpinbox inPort) {
        this.inPort = inPort;
    }

    public RichInputNumberSpinbox getInPort() {
        return inPort;
    }

    public void setInUse(RichInputText inUse) {
        this.inUse = inUse;
    }

    public RichInputText getInUse() {
        return inUse;
    }

    public void setInPass(RichInputText inPass) {
        this.inPass = inPass;
    }

    public RichInputText getInPass() {
        return inPass;
    }

    public void setInEmail(RichInputText inEmail) {
        this.inEmail = inEmail;
    }

    public RichInputText getInEmail() {
        return inEmail;
    }

    public String runClntBizRpt() {
        ReportEngine rpt = new ReportEngine();
        return rpt.reportOne(new BigDecimal("1369"));

    }

    public void setOutType(RichSelectOneChoice outType) {
        this.outType = outType;
    }

    public RichSelectOneChoice getOutType() {
        return outType;
    }

    public void setOutSecure(RichSelectBooleanCheckbox outSecure) {
        this.outSecure = outSecure;
    }

    public RichSelectBooleanCheckbox getOutSecure() {
        return outSecure;
    }

    public void setInSecure(RichSelectBooleanCheckbox inSecure) {
        this.inSecure = inSecure;
    }

    public RichSelectBooleanCheckbox getInSecure() {
        return inSecure;
    }

    public void setReqCommMode(RichSelectOneChoice reqCommMode) {
        this.reqCommMode = reqCommMode;
    }

    public RichSelectOneChoice getReqCommMode() {
        return reqCommMode;
    }

    public void setCategoryBranchesTbl(RichTable categoryBranchesTbl) {
        this.categoryBranchesTbl = categoryBranchesTbl;
    }

    public RichTable getCategoryBranchesTbl() {
        return categoryBranchesTbl;
    }

    public String addCategoryBranch() {
        Object key = categoryBranchesTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("categoryBranch", r.getAttribute("brnCode"));
        branch.setValue(r.getAttribute("brnName"));
        GlobalCC.refreshUI(branch);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categories" + "').show(hints);");
        return null;
    }

    public void setBranch(RichInputText branch) {
        this.branch = branch;
    }

    public RichInputText getBranch() {
        return branch;
    }

    public String getBranches() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:defaultBranchPOP" + "').show(hints);");
        return null;
    }

    public String getDepartments() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:p1" +
                             "').show(hints);");
        return null;
    }

    public void setTxtDepartments(RichTable txtDepartments) {
        this.txtDepartments = txtDepartments;
    }

    public RichTable getTxtDepartments() {
        return txtDepartments;
    }

    public String selectDepartments() {
        Object key = txtDepartments.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("depCode", r.getAttribute("depCode"));
        departmentVal.setValue(r.getAttribute("depName"));
        GlobalCC.refreshUI(branch);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categories" + "').show(hints);");
        return null;
    }

    public void setDepartmentVal(RichInputText departmentVal) {
        this.departmentVal = departmentVal;
    }

    public RichInputText getDepartmentVal() {
        return departmentVal;
    }

    public void setTxtCatType(RichSelectOneChoice txtCatType) {
        this.txtCatType = txtCatType;
    }

    public RichSelectOneChoice getTxtCatType() {
        return txtCatType;
    }

    public void setTxtDefault(RichSelectBooleanCheckbox txtDefault) {
        this.txtDefault = txtDefault;
    }

    public RichSelectBooleanCheckbox getTxtDefault() {
        return txtDefault;
    }

    public String newServRequestType() {
        session.setAttribute("action", "A");
        txtTypeName.setValue(null);
        txtShtDesc.setValue(null);
        txtReasonsChange.setRendered(false);
        reasonForChangeTbl.setRendered(false);
        GlobalCC.refreshUI(txtReasonsChange);
        GlobalCC.refreshUI(reasonForChangeTbl);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categoriesTypePop" + "').show(hints);");
        return null;
    }

    public String editServRequestType() {
        session.setAttribute("action", "E");
        Object key = requestTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        txtTypeName.setValue(r.getAttribute("srtDesc"));
        txtShtDesc.setValue(r.getAttribute("srtShtDesc"));
        txtReasonsChange.setRendered(true);
        reasonForChangeTbl.setRendered(true);
        GlobalCC.refreshUI(txtReasonsChange);
        GlobalCC.refreshUI(reasonForChangeTbl);
        session.setAttribute("srtCode", r.getAttribute("srtCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categoriesTypePop" + "').show(hints);");
        //      GlobalCC.refreshUI(txtTypeName);
        //      GlobalCC.refreshUI(txtShtDesc);
        return null;
    }

    public String deleteServrequestTypes() {
        Object key = requestTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.update_serv_request_types(?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, "D");
            stmt.setObject(2, r.getAttribute("srtCode"));

            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRequestTypesIterator").executeQuery();
            GlobalCC.refreshUI(requestTypeTbl);
            GlobalCC.sysInformation("Record Successfully Updated");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:categoriesTypePop" + "').hide(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setTxtTypeName(RichInputText txtTypeName) {
        this.txtTypeName = txtTypeName;
    }

    public RichInputText getTxtTypeName() {
        return txtTypeName;
    }

    public void setTxtShtDesc(RichInputText txtShtDesc) {
        this.txtShtDesc = txtShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtShtDesc;
    }

    public String saveCategoryType() {
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.update_serv_request_types(?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (session.getAttribute("action").equals("E") &&
            txtReasonsChange.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Please enter the reason for change");
            return null;
        }
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, session.getAttribute("action"));
            if (session.getAttribute("action").equals("A")) {
                stmt.setObject(2, null);
            } else {
                stmt.setObject(2, session.getAttribute("srtCode"));
            }
            stmt.setObject(3, txtTypeName.getValue());
            stmt.setObject(4, txtShtDesc.getValue());
            stmt.setObject(5, txtReasonsChange.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRequestTypesIterator").executeQuery();
            GlobalCC.refreshUI(requestTypeTbl);
            GlobalCC.sysInformation("Record Successfully Updated");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:categoriesTypePop" + "').hide(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setRequestTypeTbl(RichTable requestTypeTbl) {
        this.requestTypeTbl = requestTypeTbl;
    }

    public RichTable getRequestTypeTbl() {
        return requestTypeTbl;
    }

    public void selectRequestType(SelectionEvent selectionEvent) {
        Object key = requestTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return;
        }
        session.setAttribute("srtCode", r.getAttribute("srtCode"));
        ADFUtils.findIterator("findRequestCatsIterator").executeQuery();
        GlobalCC.refreshUI(categories);

    }

    public void setTxtType(RichInputText txtType) {
        this.txtType = txtType;
    }

    public RichInputText getTxtType() {
        return txtType;
    }

    public String launchIncidentCategory() {
        GlobalCC.showPopup("crm:categoryType");
        return null;
    }

    public void setRequestCategoriesTbl(RichTable requestCategoriesTbl) {
        this.requestCategoriesTbl = requestCategoriesTbl;
    }

    public RichTable getRequestCategoriesTbl() {
        return requestCategoriesTbl;
    }

    public String selectCategoryType() {
        Object key = requestCategoriesTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        txtType.setValue(r.getAttribute("srtDesc"));
        GlobalCC.refreshUI(txtType);
        session.setAttribute("srtCode", r.getAttribute("srtCode"));
        ADFUtils.findIterator("findRequestCatsIterator").executeQuery();
        GlobalCC.refreshUI(categories);
        GlobalCC.hidePopup("crm:categoryType");
        return null;
    }

    public String selectPolicyNo() {
        Object key = policiesTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("TSR_POLICY_NO", r.getAttribute("TSR_POLICY_NO"));
        txtTsrPolicyNo.setValue(r.getAttribute("TSR_POLICY_NO"));
        GlobalCC.refreshUI(txtTsrPolicyNo);
        ADFUtils.findIterator("findRequestCatsIterator").executeQuery();
        GlobalCC.refreshUI(policiesTbl);
        GlobalCC.hidePopup("crm:policyNoPop");
        return null;
    }

    public void setTxtComments(RichInputText txtComments) {
        this.txtComments = txtComments;
    }

    public RichInputText getTxtComments() {
        return txtComments;
    }

    public void setAgentsLOV(RichTable agentsLOV) {
        this.agentsLOV = agentsLOV;
    }

    public RichTable getAgentsLOV() {
        return agentsLOV;
    }

    public void agentSelectedDblClick(ClientEvent clientEvent) {
        AgentSelected();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "AdfPage.PAGE.findComponent('" + "crm:agentsLOV" +
                             "').hide();");
    }

    public String AgentSelected() {
        reqCommMode.setValue(null);
        txtSecondaryCommMode.setValue(null);
        primaryCommMode.setValue(null);
        secondaryCommMode.setValue(null);
        GlobalCC.refreshUI(reqCommMode);
        GlobalCC.refreshUI(txtSecondaryCommMode);
        GlobalCC.refreshUI(primaryCommMode);
        GlobalCC.refreshUI(secondaryCommMode);

        Object key2 = agentsLOV.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        session.setAttribute("AgntShtDesc", r.getAttribute("agntShtDesc"));
        session.setAttribute("clntCode", r.getAttribute("agntCode"));
        session.setAttribute("agntCode", r.getAttribute("agntCode"));
        session.setAttribute("AgntCommAllowed",
                             r.getAttribute("agntCommAllowed"));
        session.setAttribute("AgntName", r.getAttribute("agntName"));
        // session.setAttribute("sprCode", r.getAttribute("sprCode"));
        session.setAttribute("AgntName", r.getAttribute("agntName"));
        shtDesc.setValue(r.getAttribute("agntShtDesc"));
        nameDesc.setValue(r.getAttribute("agntName"));
        session.setAttribute("accountName", nameDesc.getValue());
        session.setAttribute("accountCodeVal", r.getAttribute("agntCode"));
        addressDesc.setValue(r.getAttribute("agnPhysicalAddress"));
        emailDesc.setValue(r.getAttribute("agnEmailAddress"));
        telDesc.setValue(r.getAttribute("agnSmsNumber"));

        session.setAttribute("preferEmail", r.getAttribute("agnEmailAddress"));
        session.setAttribute("preferTel", r.getAttribute("agnSmsNumber"));
        session.setAttribute("preferrComMod",
                             r.getAttribute("agnDefCommMode"));

        searchCriteria.setRendered(false);
        acc360.setRendered(true);
        agencyTab.setRendered(true);

        GlobalCC.refreshUI(agencyTab);
        //          agentDescription.setValue(session.getAttribute("AgntName"));
        //
        //          String Comm = (String)session.getAttribute("AgntCommAllowed");
        //          if (Comm == null) {
        //              commAllowedCBox.setSelected(false);
        //          } else if (Comm.equalsIgnoreCase("N")) {
        //              commAllowedCBox.setSelected(false);
        //          } else {
        //              commAllowedCBox.setSelected(true);
        //          }
        //          ADFUtils.findIterator("findAgentAccountsIterator").executeQuery();
        //          GlobalCC.refreshUI(agencyAccountsTable);
        //          txtAgentAccounts.setValue(null);
        //          session.removeAttribute("agntAcCode");
        //          session.removeAttribute("agntAccShtDec");

        //      String tieagent;
        //      try {
        //          tieagent = getParameter();
        //      } catch (SQLException e) {
        //          tieagent="N";
        //      }
        //      if(tieagent.equalsIgnoreCase("N")){    }
        //      else if(tieagent.equalsIgnoreCase("Y")){
        //          session.removeAttribute("BranchName");
        //          session.removeAttribute("UserBranchName");
        //          branchDescription.setValue(null);
        //      }
        //        ADFUtils.findIterator("findSubAgentsIterator").executeQuery();
        //        GlobalCC.refreshUI(policyContainer);
        return null;
    }

    public void setServiceProviderTbl(RichTable serviceProviderTbl) {
        this.serviceProviderTbl = serviceProviderTbl;
    }

    public RichTable getServiceProviderTbl() {
        return serviceProviderTbl;
    }

    public String selectServiceProvider() {
        Object key = serviceProviderTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("clntCode", r.getAttribute("sprCode"));
        session.setAttribute("sprCode", r.getAttribute("sprCode"));
        session.setAttribute("sprName", r.getAttribute("sprName"));
        shtDesc.setValue(r.getAttribute("sprShtDesc"));
        nameDesc.setValue(r.getAttribute("sprName"));
        session.setAttribute("accountName", nameDesc.getValue());
        session.setAttribute("accountCodeVal", r.getAttribute("sprCode"));
        addressDesc.setValue(r.getAttribute("sprPhysicalAddress"));
        emailDesc.setValue(r.getAttribute("sprEmail"));
        telDesc.setValue(r.getAttribute("sprPhoneNumber"));
        searchCriteria.setRendered(false);
        acc360.setRendered(true);
        providerTab.setRendered(true);
        GlobalCC.refreshUI(providerTab);

        return null;
    }

    public void setTxtrequestStatus(RichSelectOneChoice txtrequestStatus) {
        this.txtrequestStatus = txtrequestStatus;
    }

    public RichSelectOneChoice getTxtrequestStatus() {
        return txtrequestStatus;
    }

    public void selectrequestStatus(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            session.setAttribute("requestStatus", txtrequestStatus.getValue());

        }
        ADFUtils.findIterator("findOverdueRequestsIterator").executeQuery();
        GlobalCC.refreshUI(clientPending);

    }

    public String goToSrDetails() {
        Object key = clientPending.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("sprShtDesc", r.getAttribute("sprShtDesc"));
        session.setAttribute("agnPhysicalAddress",
                             r.getAttribute("agnPhysicalAddress"));
        session.setAttribute("agnEmailAddress",
                             r.getAttribute("agnEmailAddress"));
        session.setAttribute("agnSmsNumber", r.getAttribute("agnSmsNumber"));
        session.setAttribute("accountName", r.getAttribute("accountName"));
        session.setAttribute("clntCode", r.getAttribute("accCode"));
        session.setAttribute("accountType", r.getAttribute("accType"));


        try {
            session.setAttribute("fromTracking", "Y");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("serviceRequests.jspx");
        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        } finally {

        }
        return null;
    }

    public String backTotracking() {
        try {
            //  session.setAttribute("fromTracking", "Y");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("requestTrack.jspx");
        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        } finally {

        }
        return null;
    }


    public String goToSrLanding() {
        String accountType;
        Object key = clientPending.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("sprShtDesc", r.getAttribute("sprShtDesc"));
        session.setAttribute("agnPhysicalAddress",
                             r.getAttribute("agnPhysicalAddress"));
        session.setAttribute("agnEmailAddress",
                             r.getAttribute("agnEmailAddress"));
        session.setAttribute("agnSmsNumber", r.getAttribute("agnSmsNumber"));
        session.setAttribute("accountName", r.getAttribute("accountName"));
        session.setAttribute("clntCode", r.getAttribute("accCode"));

        //  session.setAttribute("clntCode", r.getAttribute("accTypeDesc"));
        if (r.getAttribute("accTypeDesc") != null) {
            accountType = (String)r.getAttribute("accTypeDesc");
            if (accountType.equals("CLIENT")) {
                session.setAttribute("accountType", "C");
            } else if (accountType.equals("AGENT")) {
                session.setAttribute("accountType", "A");
            } else if (accountType.equals("SERVICE PROVIDER")) {
                session.setAttribute("accountType", "SP");
            } else if (accountType.equals("OTHERS")) {
                session.setAttribute("accountType", "O");
            } else if (accountType.equals("INTERNAL")) {
                session.setAttribute("accountType", "IN");
            }
        } else {
            accountType = null;
        }
        //FOR LMS
        session.setAttribute("prpCode", new BigDecimal("1111111"));
        session.setAttribute("clientType", "I");
        session.setAttribute("clientCode", session.getAttribute("clntCode"));
        session.setAttribute("clientShtDesc", "CLIENT_SHT_DESC");

        try {
            session.setAttribute("fromTracking", "Y");
            session.setAttribute("fromHome", "Y");
            //            FacesContext fc = FacesContext.getCurrentInstance();
            //            fc.getExternalContext().redirect("serviceRequests.jspx");

            FacesContext fc = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc,
                                                                "/serviceRequests.jspx");
            fc.setViewRoot(viewRoot);
            fc.renderResponse();
        } catch (Exception ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        } finally {

        }
        return null;
    }

    public String reAssign() {
        String accountType;
        Object key = clientPending.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("sprShtDesc", r.getAttribute("sprShtDesc"));
        session.setAttribute("agnPhysicalAddress",
                             r.getAttribute("agnPhysicalAddress"));
        session.setAttribute("agnEmailAddress",
                             r.getAttribute("agnEmailAddress"));
        session.setAttribute("agnSmsNumber", r.getAttribute("agnSmsNumber"));
        session.setAttribute("accountName", r.getAttribute("accountName"));
        session.setAttribute("clntCode", r.getAttribute("accCode"));
        session.setAttribute("tsrCode", r.getAttribute("tsrCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "fms:users" +
                             "').show(hints);");
        return null;
    }

    public String backToHome() {
        try {
            //  session.setAttribute("fromTracking", "Y");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("home.jspx");
        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        } finally {

        }
        return null;
    }

    public void setTxtDefaultVal(RichSelectBooleanCheckbox txtDefaultVal) {
        this.txtDefaultVal = txtDefaultVal;
    }

    public RichSelectBooleanCheckbox getTxtDefaultVal() {
        return txtDefaultVal;
    }

    public void setOthersRd(RichSelectBooleanRadio othersRd) {
        this.othersRd = othersRd;
    }

    public RichSelectBooleanRadio getOthersRd() {
        return othersRd;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtTelephoneNumber(RichInputText txtTelephoneNumber) {
        this.txtTelephoneNumber = txtTelephoneNumber;
    }

    public RichInputText getTxtTelephoneNumber() {
        return txtTelephoneNumber;
    }

    public void setTxtEmailAddress(RichInputText txtEmailAddress) {
        this.txtEmailAddress = txtEmailAddress;
    }

    public RichInputText getTxtEmailAddress() {
        return txtEmailAddress;
    }

    public void setTxtPhysicalAddress(RichInputText txtPhysicalAddress) {
        this.txtPhysicalAddress = txtPhysicalAddress;
    }

    public RichInputText getTxtPhysicalAddress() {
        return txtPhysicalAddress;
    }

    public void setTxtIdNumber(RichInputText txtIdNumber) {
        this.txtIdNumber = txtIdNumber;
    }

    public RichInputText getTxtIdNumber() {
        return txtIdNumber;
    }

    public String saveServiceRequest() {
        // Add event code here...
        return null;
    }

    public String saveOthersDetails() {
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.update_serv_req_other_dtls(?,?,?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, "A");
            stmt.registerOutParameter(2, OracleTypes.NUMBER);
            stmt.setObject(3, txtName.getValue());
            stmt.setObject(4, txtTelephoneNumber.getValue());
            stmt.setObject(5, txtEmailAddress.getValue());
            stmt.setObject(6, txtPhysicalAddress.getValue());
            stmt.setObject(7, txtIdNumber.getValue());
            stmt.execute();
            session.setAttribute("clntCode", stmt.getBigDecimal(2));
            stmt.close();
            conn.commit();
            conn.close();
            //GlobalCC.sysInformation("Record Successfully Updated");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "crm:p1" +
                                 "').hide(hints);");
            session.setAttribute("sprName", txtName.getValue());
            shtDesc.setValue(null);
            nameDesc.setValue(txtName.getValue());
            session.setAttribute("accountName", txtName.getValue());
            session.setAttribute("accountCodeVal",
                                 session.getAttribute("clntCode"));
            addressDesc.setValue(txtPhysicalAddress.getValue());
            emailDesc.setValue(txtEmailAddress.getValue());
            telDesc.setValue(txtTelephoneNumber.getValue());
            searchCriteria.setRendered(false);
            acc360.setRendered(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTxtOthersType(RichSelectOneRadio txtOthersType) {
        this.txtOthersType = txtOthersType;
    }

    public RichSelectOneRadio getTxtOthersType() {
        return txtOthersType;
    }

    public void selectOtherTypes(DialogEvent dialogEvent) {
        if (txtOthersType.getValue() != null) {
            if (txtOthersType.getValue().equals("N")) {
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:p1" + "').show(hints);");
            } else {
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:p3" + "').show(hints);");

            }
        } else {

        }
    }

    public void setOthersTbl(RichTable othersTbl) {
        this.othersTbl = othersTbl;
    }

    public RichTable getOthersTbl() {
        return othersTbl;
    }

    public String selectOthersDetails() {
        Object key = othersTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        session.setAttribute("clntCode", r.getAttribute("sridCode"));
        session.setAttribute("sprName", r.getAttribute("sridName"));
        shtDesc.setValue(null);
        nameDesc.setValue(r.getAttribute("sridName"));
        session.setAttribute("accountName", r.getAttribute("sridName"));
        session.setAttribute("accountCodeVal", r.getAttribute("sridCode"));
        addressDesc.setValue(r.getAttribute("sridPhysicalAddress"));
        emailDesc.setValue(r.getAttribute("sridEmailAddress"));
        telDesc.setValue(r.getAttribute("sridTelephone"));
        searchCriteria.setRendered(false);
        acc360.setRendered(true);
        return null;
    }

    public void setIntReqRd(RichSelectBooleanRadio intReqRd) {
        this.intReqRd = intReqRd;
    }

    public RichSelectBooleanRadio getIntReqRd() {
        return intReqRd;
    }

    public String selectInternalOrder() {
        Object key = selectInternalDep.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {
            connection = dbConnector.getDatabaseConnection();

            String query1 =
                "begin  tqc_setups_cursor.organization_details(?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, null);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                addressDesc.setValue(rs.getString(8));
                emailDesc.setValue(rs.getString(7));
                telDesc.setValue(rs.getString(12));
            }
            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        session.setAttribute("dltCode", r.getAttribute("code"));
        session.setAttribute("clntCode", r.getAttribute("code"));
        session.setAttribute("dltDesc", r.getAttribute("dltDesc"));
        // session.setAttribute("dltDesc", r.getAttribute("dltDesc"));
        ///accountType
        // accountType

        // shtDesc.setValue(r.getAttribute("sprShtDesc"));
        nameDesc.setValue(r.getAttribute("dltDesc"));
        session.setAttribute("accountName", "Internal");
        session.setAttribute("accountCodeVal", r.getAttribute("code"));

        searchCriteria.setRendered(false);
        acc360.setRendered(true);

        return null;
    }

    public void setSelectInternalDep(RichTable selectInternalDep) {
        this.selectInternalDep = selectInternalDep;
    }

    public RichTable getSelectInternalDep() {
        return selectInternalDep;
    }

    private String getOrgdetails() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {
            connection = dbConnector.getDatabaseConnection();

            String query1 =
                "begin  TQC_CLIENTS_PKG.organization_details(?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, null);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {

            }
            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return null;
    }

    public void setTxtSecondaryCommMode(RichSelectOneChoice txtSecondaryCommMode) {
        this.txtSecondaryCommMode = txtSecondaryCommMode;
    }

    public RichSelectOneChoice getTxtSecondaryCommMode() {
        return txtSecondaryCommMode;
    }

    public void setTxtReasonsChange(RichInputText txtReasonsChange) {
        this.txtReasonsChange = txtReasonsChange;
    }

    public RichInputText getTxtReasonsChange() {
        return txtReasonsChange;
    }

    public void setReasonForChangeTbl(RichOutputLabel reasonForChangeTbl) {
        this.reasonForChangeTbl = reasonForChangeTbl;
    }

    public RichOutputLabel getReasonForChangeTbl() {
        return reasonForChangeTbl;
    }

    public void setAgencyTab(RichPanelBox agencyTab) {
        this.agencyTab = agencyTab;
    }

    public RichPanelBox getAgencyTab() {
        return agencyTab;
    }

    public void setProviderTab(RichPanelBox providerTab) {
        this.providerTab = providerTab;
    }

    public RichPanelBox getProviderTab() {
        return providerTab;
    }

    public String ticketReAssign() {
        Object key = users.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SERVICE_REQUESTS.setrequestAssignee(?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, session.getAttribute("tsrCode"));
            stmt.setObject(2, r.getAttribute("userCode"));
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findAllActiveRequestsIterator").executeQuery();
            GlobalCC.refreshUI(clientPending);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "fms:users" +
                                 "').hide(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String primaryCommucicationMode() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try {
            connection = dbConnector.getDatabaseConnection();

            String query1 =
                "begin  ? := TQC_CLIENTS_PKG.getPreferedCommMode(?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("clntCode"));
            stmt.setString(3, (String)session.getAttribute("vModeIn"));

            stmt.execute();

            String vMode = stmt.getString(1);
            System.out.println("vMode " + vMode);
            session.setAttribute("CommMode", vMode);
            if (vMode == null) {
                System.out.println("Got here vMode == " + vMode);
                //display popup.
                lblCommMode.setValue(reqCommMode.getValue() + ": ");
                GlobalCC.refreshUI(lblCommMode);
                GlobalCC.showPop("crm:popCommMode");
            }
            stmt.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return null;

    }

    public void selectPrimaryCommMode(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...
        if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("EMAIL")) {
            System.out.println();
            session.setAttribute("vModeIn", "EMAIL");
            primaryCommucicationMode();
            primaryCommMode.setValue(session.getAttribute("CommMode"));
            GlobalCC.refreshUI(primaryCommMode);


        } else if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("SMS") ||
                   GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("PHONE-CALL")) {
            session.setAttribute("vModeIn", "TEL");
            primaryCommucicationMode();
            primaryCommMode.setValue(session.getAttribute("CommMode"));
            GlobalCC.refreshUI(primaryCommMode);

        } else if (GlobalCC.checkNullValues(reqCommMode.getValue()) == null) {
            //display popup.
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:popCommMode" + "').hide(hints);");
        }
    }

    public void setPrimaryCommMode(RichInputText primaryCommMode) {
        this.primaryCommMode = primaryCommMode;
    }

    public RichInputText getPrimaryCommMode() {
        return primaryCommMode;
    }

    public void setSecondaryCommMode(RichInputText secondaryCommMode) {
        this.secondaryCommMode = secondaryCommMode;
    }

    public RichInputText getSecondaryCommMode() {
        return secondaryCommMode;
    }

    public void primaryCommMode(ValueChangeEvent valueChangeEvent) {
        // Add event code here...


        if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("EMAIL")) {
            session.setAttribute("vModeIn", "EMAIL");
        } else if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("SMS")) {
            session.setAttribute("vModeIn", "TEL");
        } else if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("LETTER")) {
            session.setAttribute("vModeIn", "LETTER");
        } else if (GlobalCC.checkNullValues(reqCommMode.getValue()).equalsIgnoreCase("PHONE-CALL")) {
            session.setAttribute("vModeIn", "PHONE");
        }
        session.setAttribute("vModeType", "PRI");
        primaryCommucicationMode();

        String commMode =
            GlobalCC.checkNullValues(session.getAttribute("CommMode"));
        if (commMode != null) {
            primaryCommMode.setValue(commMode);
            GlobalCC.refreshUI(primaryCommMode);
        }
    }

    public void secondaryCommMode(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (GlobalCC.checkNullValues(txtSecondaryCommMode.getValue()).equalsIgnoreCase("EMAIL")) {
            session.setAttribute("vModeIn", "EMAIL");
        } else if (GlobalCC.checkNullValues(txtSecondaryCommMode.getValue()).equalsIgnoreCase("SMS")) {
            session.setAttribute("vModeIn", "TEL");
        } else if (GlobalCC.checkNullValues(txtSecondaryCommMode.getValue()).equalsIgnoreCase("LETTER")) {
            session.setAttribute("vModeIn", "LETTER");
        } else if (GlobalCC.checkNullValues(txtSecondaryCommMode.getValue()).equalsIgnoreCase("PHONE-CALL")) {
            session.setAttribute("vModeIn", "PHONE");
        }
        session.setAttribute("vModeType", "SEC");
        primaryCommucicationMode();

        String commMode =
            GlobalCC.checkNullValues(session.getAttribute("CommMode"));
        if (commMode != null) {
            secondaryCommMode.setValue(commMode);
            GlobalCC.refreshUI(secondaryCommMode);
        }
    }

    public void setRequestCommMode(RichInputText requestCommMode) {
        this.requestCommMode = requestCommMode;
    }

    public RichInputText getRequestCommMode() {
        return requestCommMode;
    }

    public String newCommMode() {
        try {
            String newMode =
                GlobalCC.checkNullValues(requestCommMode.getValue());
            String comments = GlobalCC.checkNullValues(txtComments.getValue());
            session.setAttribute("CommMode", newMode);
            comments =
                    "Communication Mode: " + session.getAttribute("vModeIn") +
                    " " + newMode + ", " + comments;
            txtComments.setValue(comments.replace("null", ""));
            GlobalCC.refreshUI(txtComments);

            if ("PRI" == session.getAttribute("vModeType")) {
                //primaryCommMode.setValue(session.getAttribute("CommMode"));
                //GlobalCC.refreshUI(primaryCommMode);
            }
            if ("SEC" == session.getAttribute("vModeType")) {
                //secondaryCommMode.setValue(session.getAttribute("CommMode"));
                //GlobalCC.refreshUI(secondaryCommMode);
            }

            GlobalCC.hidePopup("crm:popCommMode");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String exitCommModePop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "AdfPage.PAGE.findComponent('" +
                             "crm:popCommMode" + "').hide();");
        return null;
    }

    public void validateDueDate(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        Date today = new Date();
        Date dueDate1 = new Date();
        DateFormat formatter;

        String dueDate = GlobalCC.checkNullValues(reqDueDate.getValue());
        if (dueDate.contains(":")) {
            dueDate = GlobalCC.parseDate(dueDate);
        } else if (dueDate.contains("/")) {
            dueDate = GlobalCC.upDateParseDateTwo(dueDate);
        } else {
            dueDate = GlobalCC.upDateParseDate(dueDate);
        }
        try {
            formatter = new SimpleDateFormat("dd/MMMM/yyyy");
            dueDate1 = formatter.parse(dueDate);
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (dueDate1.before(today)) {
            GlobalCC.errorValueNotEntered("Due Date selected is less that Todays Date. Please Check.");

        }
    }

    public void uploadDoc(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            try {
                CSVtoADFTableProcessor CSVManip = new CSVtoADFTableProcessor();
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimeType =
                    fileNameMap.getContentTypeFor(_file.getFilename());
                CSVManip.uploadServReqDocuments(uploadedFile.getInputStream(),
                                                _file.getFilename(), mimeType);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
    }

    public void setUpFile(RichInputFile upFile) {
        this.upFile = upFile;
    }

    public RichInputFile getUpFile() {
        return upFile;
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

    public static void setFileContent(String fileContent) {
        ServiceRequest.fileContent = fileContent;
    }

    public static String getFileContent() {
        return fileContent;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setReqRefNumber(String reqRefNumber) {
        this.reqRefNumber = reqRefNumber;
    }

    public String getReqRefNumber() {
        return reqRefNumber;
    }

    public void selectServiceRequest(SelectionEvent selectionEvent) {
        Object key = clientPending.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        session.setAttribute("reqRefNo", r.getAttribute("requestRefNumber"));
        ADFUtils.findIterator("serviceRequestDocsIterator").executeQuery();
        //new GeneralDAO().getServiceRequestDocs();
        GlobalCC.refreshUI(serviceReqTbl);


        session.setAttribute("tsrCode", r.getAttribute("tsrCode"));
        ADFUtils.findIterator("findServiceReqClientCommentsIterator").executeQuery();
        //new GeneralDAO().getServiceRequestDocs();
        GlobalCC.refreshUI(clntCommentsTbl);

    }


    public String generateDoc() {
        Object key2 = serviceReqTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        String id = (String)r.getAttribute("id");
        String[] ids = id.split(";");
        String finalId = "";
        if (ids.length > 1) {
            finalId = ids[0];
        } else
            finalId = id;

        Connection conn = null;
        String url = "";
        String user = "";
        String pass = "";
        DBConnector datahandler = new DBConnector();
        conn = datahandler.getDatabaseConnection();
        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;
        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setInt(2, 0);
            cstSections.execute();
            sectionsRS = (ResultSet)cstSections.getObject(1);
            if (sectionsRS.next()) {
                url = sectionsRS.getString(2);
                user = sectionsRS.getString(3);
                pass = sectionsRS.getString(4);

            }
            String[] urls = url.split("/");
            String toPrint =
                urls[0] + "//" + urls[2] + "/" + "/OpenAnnotate/login/unencrypted.htm?username=" +
                user + "&password=" + pass + "&docbase=Company Home&docId=" +
                finalId;
            System.out.println(toPrint);
            session.setAttribute("toPrint", toPrint);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "crm:p201" +
                                 "').show(hints);");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return null;

    }

    public void setServiceReqTbl(RichTable serviceReqTbl) {
        this.serviceReqTbl = serviceReqTbl;
    }

    public RichTable getServiceReqTbl() {
        return serviceReqTbl;
    }

    public void setClientCommentsTbl(RichTable clientCommentsTbl) {
        this.clientCommentsTbl = clientCommentsTbl;
    }

    public RichTable getClientCommentsTbl() {
        return clientCommentsTbl;
    }

    public void setClntCommentsTbl(RichTable clntCommentsTbl) {
        this.clntCommentsTbl = clntCommentsTbl;
    }

    public RichTable getClntCommentsTbl() {
        return clntCommentsTbl;
    }

    public String actionEditComments() {
        Object key2 = clntCommentsTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtClientComments.setValue(nodeBinding.getAttribute("srcClientComment"));
            txtSolution.setValue(nodeBinding.getAttribute("srcSolution"));

            session.setAttribute("srcCode",
                                 nodeBinding.getAttribute("srcCode"));
            session.setAttribute("srcTsrCode",
                                 nodeBinding.getAttribute("srcTsrCode"));
            session.setAttribute("srcClientComment",
                                 nodeBinding.getAttribute("srcClientComment"));


            btnSaveComments.setText("Save");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:clntCommentsPop" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

    }

    public void setTxtClientComments(RichInputText txtClientComments) {
        this.txtClientComments = txtClientComments;
    }

    public RichInputText getTxtClientComments() {
        return txtClientComments;
    }

    public void setTxtSolution(RichInputText txtSolution) {
        this.txtSolution = txtSolution;
    }

    public RichInputText getTxtSolution() {
        return txtSolution;
    }

    public void setBtnSaveComments(RichCommandButton btnSaveComments) {
        this.btnSaveComments = btnSaveComments;
    }

    public RichCommandButton getBtnSaveComments() {
        return btnSaveComments;
    }

    public String saveEscalations() {

        DBConnector dbConnect = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;

        try {
            conn = dbConnect.getDatabaseConnection();
            String queryString =
                "begin tqc_service_requests.servReqClntCommentProc(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(queryString);
            cst.setString(1, "E");
            cst.setObject(2, session.getAttribute("srcCode"));
            cst.setObject(3, session.getAttribute("srcClientComment"));
            cst.setObject(4, txtSolution.getValue());
            cst.setObject(5, session.getAttribute("srcTsrCode"));


            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.hidePopup("crm:clntCommentsPop");
            GlobalCC.sysInformation("Save Successfull");
            ADFUtils.findIterator("findServiceReqClientCommentsIterator").executeQuery();

            GlobalCC.refreshUI(clntCommentsTbl);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
    
    

    public String cancelCommentsPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:clntCommentsPop" + "').hide(hints);");
        return null;
    }


    public void setLblCommMode(RichOutputText lblCommMode) {
        this.lblCommMode = lblCommMode;
    }

    public RichOutputText getLblCommMode() {
        return lblCommMode;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setTxtTsrPolicyNo(RichInputText txtTsrPolicyNo) {
        this.txtTsrPolicyNo = txtTsrPolicyNo;
    }

    public RichInputText getTxtTsrPolicyNo() {
        return txtTsrPolicyNo;
    }

    public void setPoliciesTbl(RichTable policiesTbl) {
        this.policiesTbl = policiesTbl;
    }

    public RichTable getPoliciesTbl() {
        return policiesTbl;
    }

    public void setBtnSaveServiceReq(RichCommandButton btnSaveServiceReq) {
        this.btnSaveServiceReq = btnSaveServiceReq;
    }

    public RichCommandButton getBtnSaveServiceReq() {
        return btnSaveServiceReq;
    }

    public void setReceiveDate(RichInputDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public RichInputDate getReceiveDate() {
        return receiveDate;
    }

    public void setReqReporter(RichInputText reqReporter) {
        this.reqReporter = reqReporter;
    }

    public RichInputText getReqReporter() {
        return reqReporter;
    }

    public void setReporterAcc(RichTable reporterAcc) {
        this.reporterAcc = reporterAcc;
    }

    public RichTable getReporterAcc() {
        return reporterAcc;
    }

    public void setCaptureDate(RichInputDate captureDate) {
        this.captureDate = captureDate;
    }

    public RichInputDate getCaptureDate() {
        return captureDate;
    }

    public void setClosedBy(RichInputText closedBy) {
        this.closedBy = closedBy;
    }

    public RichInputText getClosedBy() {
        return closedBy;
    }
    
    public String actionAcceptSector() {
            //            Object key2 = tblSectorPop.getSelectedRowData();
            //            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            //
            //            if (nodeBinding != null) {
            //                txtSectorName.setValue(nodeBinding.getAttribute("name"));
            //
            //                txtSectorCode.setValue(nodeBinding.getAttribute("code"));
            //                session.setAttribute("sectorCode",
            //                                     nodeBinding.getAttribute("code"));
            //                session.setAttribute("sectorName",
            //                                     nodeBinding.getAttribute("name"));
            //                txtSrchSectorName.setValue(session.getAttribute("sectorName"));
            //
            //                ADFUtils.findIterator("findOccupationIterator").executeQuery();
            //                ADFUtils.findIterator("findSpecializationsIterator").executeQuery();
            //
            //                GlobalCC.refreshUI(txtSrchSectorName);
            //                GlobalCC.refreshUI(txtSectorName);
            //                GlobalCC.refreshUI(txtSectorCode);
            //                GlobalCC.refreshUI(txtCltOccupation);
            //                GlobalCC.refreshUI(occupationTbl);
            //                GlobalCC.refreshUI(specializationTbl);
            //                GlobalCC.hidePopup("pt1:sectorPop");
            //
            //            }
            return null;
        }

    public void setTxtReasonForUpdate(RichInputText txtReasonForUpdate) {
        this.txtReasonForUpdate = txtReasonForUpdate;
    }

    public RichInputText getTxtReasonForUpdate() {
        return txtReasonForUpdate;
    }

    public void setSearchFormHolder(RichPanelGroupLayout searchFormHolder) {
        this.searchFormHolder = searchFormHolder;
    }

    public RichPanelGroupLayout getSearchFormHolder() {
        return searchFormHolder;
    }

    public void setTxtSrchSectorName(RichInputText txtSrchSectorName) {
        this.txtSrchSectorName = txtSrchSectorName;
    }

    public RichInputText getTxtSrchSectorName() {
        return txtSrchSectorName;
    }

    public void setTblSectorPop(RichTable tblSectorPop) {
        this.tblSectorPop = tblSectorPop;
    }

    public RichTable getTblSectorPop() {
        return tblSectorPop;
    }
    Rendering renderer = new Rendering();

    public void actionSearchCriterialistener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            RichSelectOneChoice myComp =
                (RichSelectOneChoice)evt.getComponent();
            session.setAttribute("searchCriteria", myComp.getValue());


        }

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

    public String actionAcceptSearchCriteria() {
        java.sql.Date dob = null;
        java.util.Date dateBirth;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String name = null;
        String oName = null;
        String shtDesc = null;
        String clntType = null;
        String status = null;
        String postalAddr = null;
        String physicalAddr = null;
        String oldId = null;
        String pin;
        String sector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        String criteria = null;
        String criteria2 = null;
        String searchName = null;
        String searchOName = null;
        String searchPostalAddr = null;
        String searchPhysicalAddr = null;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dFrom = null;
        String dTo = null;
        String fromDate = null;
        String searchOldId = null;
        String searchOldNames = null;
        String dateOfBirth;
        String idNumber;
        String policyNumber;
        String passport;
        String searchShtDesc =
            GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        String searchAccountNo =
            GlobalCC.checkNullValues(txtAccountNo.getValue()); //client account NO.

        String searchOldAccountNo =
            GlobalCC.checkNullValues(txtOldAccountNo.getValue()); //client Old account NO.


        String telNo = GlobalCC.checkNullValues(txtTelNo.getValue());
        String searchClntType =
            GlobalCC.checkNullValues(session.getAttribute("searchClntType"));
        String searchStatus =
            GlobalCC.checkNullValues(session.getAttribute("searchClntStatus"));
        String searchSector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        searchPhysicalAddr =
                GlobalCC.checkNullValues(txtSearchPhysical.getValue());
        searchPostalAddr =
                GlobalCC.checkNullValues(txtSearchPostal.getValue());
        
        String claimNo = GlobalCC.checkNullValues(txtClaimNo.getValue());
        
        String vehicleRegNo = GlobalCC.checkNullValues(txtVehicleRegNo.getValue()); 

        if (rbtnCustomerId.isSelected()) {
            searchOldNames =
                    GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        }


        session.setAttribute("searchCriteria", null);
        session.setAttribute("searchCriteria2", null);


        if (clntDateCreatedFrom.getValue() != null &&
            !(clntDateCreatedFrom.getValue().equals(""))) {
            String date1 = df.format(clntDateCreatedFrom.getValue());

            fromDate =
                    GlobalCC.parseNormalDate(clntDateCreatedFrom.getValue().toString());

        }

        String toDate = null; //new Date();
        if (clntDateCreatedTo.getValue() != null &&
            !(clntDateCreatedTo.getValue().equals(""))) {
            String date2 = df.format(clntDateCreatedTo.getValue());

            toDate =
                    GlobalCC.parseNormalDate(clntDateCreatedTo.getValue().toString());

        }
        if (txtDateOfBirth.getValue() != null) {
            dateOfBirth = GlobalCC.extractDate(txtDateOfBirth).toString();
        } else {
            dateOfBirth = null;
        }
        if (pinNumber.getValue() != null) {
            pin = pinNumber.getValue().toString();
        } else {
            pin = null;
        }
        if (txtPassport.getValue() != null) {
            passport = txtPassport.getValue().toString();
        } else {
            passport = null;
        }
        searchName = GlobalCC.checkNullValues(txtSearchName.getValue());

        if (searchName == null || searchName == "") {

            searchName = null;

        } else if (searchName.trim().length() < 1) {
            searchName = null;
        }

        searchOName = GlobalCC.checkNullValues(txtSearchOtherName.getValue());
        if (searchOName == null || searchOName == "") {

            searchOName = null;

        } else if (searchOName.trim().length() < 1) {
            searchOName = null;
        }
        if (searchPhysicalAddr == null) {

        } else if (searchPhysicalAddr.trim().length() < 1) {
            searchPhysicalAddr = null;
        }
        if (searchPostalAddr == null) {
            searchPostalAddr = null;
        } else if (searchPostalAddr.trim().length() < 1) {
            searchPostalAddr = null;
        }
        if (searchOldId != null) {
            if (searchOldId.trim().length() < 1) {
                searchOldId = null;
            }
        }

        if (searchName != null) {
            name = "'" + searchName + "'";

        }
        if (searchOName != null) {
            oName = "'" + searchOName + "'";
        }
        if (searchShtDesc != null) {
            shtDesc = "'" + searchShtDesc + "'";
        }
        if (searchClntType != null) {
            clntType = "'" + searchClntType + "'";
            clntType = "'" + searchClntType + "'";
        }
        if (searchStatus != null) {
            status = "'" + searchStatus + "'";
        }
        if (fromDate != null) {
            dFrom = "'" + fromDate + "'";
        }
        if (toDate != null) {
            dTo = "'" + toDate + "'";
        }
        if (searchPhysicalAddr != null) {
            physicalAddr = "'" + searchPhysicalAddr + "'";
        }
        if (searchPostalAddr != null) {
            postalAddr = "'" + searchPostalAddr + "'";
        }
        if (searchOldId != null) {
            oldId = "'" + searchOldId + "'";
        }

        if (txtIdNum.getValue() != null) {
            idNumber = txtIdNum.getValue().toString();
        } else {
            idNumber = null;
        }
        if (clientPolicyNumber.getValue() != null) {
            policyNumber = clientPolicyNumber.getValue().toString();
        } else {
            policyNumber = null;
        }

        if (rbtnSearchAccountNo.isSelected()) {
            if (searchAccountNo == null || searchAccountNo == "") {
                GlobalCC.EXCEPTIONREPORTING("Specify  Client Account Number");
                return null;
            }

            criteria = "WHERE CLNT_ACCNT_NO = '" + searchAccountNo + "'";

        }


        else if (rbtnSearchOldAccountNo.isSelected()) {
            if (searchOldAccountNo == null || searchOldAccountNo == "") {
                GlobalCC.EXCEPTIONREPORTING("Specify  Client Old Account Number");
                return null;
            }

            criteria =
                    "WHERE CLNT_OLD_ACCNT_NO = '" + searchOldAccountNo + "'";

        }


        else if (rbtnPartOfAnyName.isSelected()) {
            // criteria
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (status == null && clntType == null && sector == null &&
                       dFrom == null && dTo == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')";

            } else if (clntType == null && sector == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        " CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +

                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else if (clntType == null && sector != null && status != null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        "            nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "  CLNT_STATUS=NVL(" +

                        status + " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else {
                criteria =
                        "  WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" +
                        "            AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                        sector + ",0) \n" +
                        "            and CLNT_TYPE=nvl(" + clntType +

                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }


        }


        else if (rbtnOldNames.isSelected()) {
            // criteria
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (status == null && clntType == null && sector == null &&
                       dFrom == null && dTo == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')";

            } else if (clntType == null && sector == null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        " CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +

                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else if (clntType == null && sector != null && status != null) {
                criteria =
                        "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" + "            AND (" +
                        "            nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "  CLNT_STATUS=NVL(" +

                        status + " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            } else {
                criteria =
                        "  WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA)'))||'%'\n" +
                        "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                        name + ",'HAKUNA'))||'%' OR " +
                        "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                        oName + ",'HAKUNA)'))||'%'" +
                        " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                        ",'HAKUNA'))||'%')" +
                        "            AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                        sector + ",0) \n" +
                        "            and CLNT_TYPE=nvl(" + clntType +

                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)" +
                        " AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }


        }


        else if (rbtnExactName.isSelected()) {
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (name != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME||' '||CLNT_OTHER_NAMES) LIKE '%'||UPPER(" +
                            name + ")||'%' )";

                } else if (status == null && clntType == null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy'))";
                } else if (status == null && clntType != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "  AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            " AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND ( \n" +
                            "  CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else {
                    criteria =
                            "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) " +
                            "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                            ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "           AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                }

            } else if (oName != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )";

                } else if (status == null && clntType == null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" +
                            "AND (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy'))";
                } else if (status == null && clntType != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            "            and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "  AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector != null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            " AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else if (clntType == null && status != null &&
                           sector == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )" + "AND ( \n" +
                            "  CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                } else {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") " + ") " + "AND (nvl(CLNT_SEC_CODE,0) = nvl(" +
                            sector + ",0) \n" +
                            "          and CLNT_TYPE=nvl(" + clntType +
                            ",CLNT_TYPE) \n" +
                            "           AND CLNT_STATUS=NVL(" + status +
                            " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                            dFrom +
                            ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" +
                            dTo + ",'01/01/2099'),'dd/mm/yyyy')))";
                }
            }

        }

        //beginning part  of first and other name

        else if (rbtnSector.isSelected()) {
            if (sector == null) {
                GlobalCC.INFORMATIONREPORTING("Sector  Required::");
                return null;
            }
            if (clntType == null && dFrom == null && dTo == null) {
                criteria =
                        "WHERE (nvl(CLNT_SEC_CODE," + 0 + ") = nvl(" + sector +
                        ",0))";
            } else {
                criteria =
                        "WHERE (nvl(CLNT_SEC_CODE," + 0 + ") = nvl(" + sector +
                        ",0))" + "AND (          CLNT_TYPE=nvl(" + clntType +
                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnStatus.isSelected()) {
            if (status == null) {
                GlobalCC.INFORMATIONREPORTING("Client Status  Required:");
                return null;
            }
            if (clntType == null && sector == null && dFrom == null &&
                dTo == null) {
                criteria =
                        " WHERE  (  CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS))";
            } else {

                criteria =
                        " WHERE  (  CLNT_STATUS=NVL(" + status + " ,CLNT_STATUS))" +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "         AND  (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnClientType.isSelected()) {
            if (clntType == null) {
                GlobalCC.INFORMATIONREPORTING("Client Type  Required");
                return null;
            }
            if (status == null && sector == null && dFrom == null &&
                dTo == null) {
                criteria =
                        " WHERE (  CLNT_TYPE=nvl(" + clntType + ",CLNT_TYPE)) ";
            } else {
                criteria =
                        " WHERE (  CLNT_TYPE=nvl(" + clntType + ",CLNT_TYPE)) " +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS) AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(nvl(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        }

        else if (rbtnShortDesc.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')";
            } else {
                criteria =
                        " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')" +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "            and CLNT_TYPE=nvl(" + clntType +
                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnShortDescLeg.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        " WHERE ( UPPER( CLNT_OLD_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')";
            } else {
                criteria =
                        " WHERE ( UPPER( CLNT_OLD_SHT_DESC ) LIKE '%'||NVL(UPPER(" +
                        shtDesc + "),'HAKUNA')||'%')" +
                        "AND (nvl(CLNT_SEC_CODE,0) = nvl(" + sector +
                        ",0) \n" +
                        "            and CLNT_TYPE=nvl(" + clntType +
                        ",CLNT_TYPE) \n" +
                        "           AND CLNT_STATUS=NVL(" + status +
                        " ,CLNT_STATUS)AND (CLNT_DATE_CREATED BETWEEN to_date(NVL(" +
                        dFrom +
                        ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                        ",'01/01/2099'),'dd/mm/yyyy')))";
            }
        } else if (rbtnPhySicalAddr.isSelected()) {

            if (physicalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Physical Address  Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";
            } else if (status == null && clntType == null && sector != null) {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";
            } else {
                criteria =
                        "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" +
                        physicalAddr + ")||'%')";

            }
        } else if (rbtnPostalAddr.isSelected()) {

            if (postalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Postal Address Required");
                return null;
            }
            if (status == null && clntType == null && sector == null &&
                dFrom == null && dTo == null) {
                criteria =
                        "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                        ")||'%')";
            } else {
                criteria =
                        "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                        ")||'%')";
            }
        } else if (rbtnCustomerId.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Old Customer Id  Required");
                return null;
            }
            criteria = ",tqc_clients_log \n" +
                    "where UPPER(tqc_clients.CLNT_CODE)=UPPER(tqc_clients_log.CLNT_CODE) \n" +
                    "AND  UPPER(CLNT_PREV_SHT_DESC) LIKE '%'||UPPER(" +
                    shtDesc + ")||'%' \n";

        } else if (rdbnDateOfBirth.isSelected()) {
            criteria =
                    "WHERE(CLNT_DOB=to_date(NVL(" + "'" + dateOfBirth + "'" +
                    ",'1900-10-10'),'RRRR-MM-DD'))";
        } else if (rbtnIdNumber.isSelected()) {
            criteria =
                    "WHERE(CLNT_ID_REG_NO=NVL(" + "'" + idNumber + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        }else if (txtClientPolNumber.isSelected()) {
            
            criteria = "WHERE (  \n" + 
                        "                clnt_code IN ( \n" + 
                        "                        SELECT gin_policies.pol_prp_code FROM gin_policies WHERE  gin_policies.pol_policy_no = NVL(" + "'" + policyNumber + "'" + ",'HAKUNA')\n" + 
                        "                            UNION \n" + 
                        "                        SELECT lms_policies.pol_prp_code FROM lms_policies WHERE  lms_policies.pol_policy_no = NVL(" + "'" + policyNumber + "'" + ",'HAKUNA')\n" + 
                        "                   ) \n" + 
                        "            )";

        } else if (txtPinNumber.isSelected()) {
            criteria = "WHERE(CLNT_PIN=NVL(" + "'" + pin + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        } else if (txtPassPortNumber.isSelected()) {
            criteria =
                    "WHERE(CLNT_PASSPORT_NO=NVL(" + "'" + passport + "'" + ",'HAKUNA'))";
            //          "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + txtIdNum +
            //          ")||'%')";
        } else if (txtTelNoRadio.isSelected()) {

            if (telNo != null) {
                if (!telNo.matches("^[\\+]{0,1}[0-9\\-]+$")) {
                    GlobalCC.errorValueNotEntered("Invalid Telephone No!");
                    return null;
                }
            }
            criteria =
                    " WHERE ( UPPER(CLNT_SMS_TEL) LIKE '%'|| UPPER(NVL('" + telNo +
                    "','HAKUNA'))||'%'\n" +
                    "            OR UPPER(CLNT_TEL) LIKE '%'|| UPPER(NVL('" +
                    telNo + "','HAKUNA'))||'%' OR " +
                    "UPPER(CLNT_TEL2) LIKE '%'|| UPPER(NVL('" + telNo +
                    "','HAKUNA'))||'%')";
        } else if (sbrClaimNo.isSelected() && claimNo != null) {

            //criteria = "WHERE (( UPPER(CMB_CLAIM_NO) LIKE '%'||'" + claimNo + "'||'%') OR  ( UPPER(CLM_NO) LIKE '%'||'" + claimNo + "'||'%') )";
            criteria =
                    "WHERE CLNT_CODE=(select CMB_CLIENT_PRP_CODE from gin_claim_master_bookings where CMB_CLAIM_NO ='" +
                    claimNo + "')";
        }

        else if (rbtnDateCreated.isSelected()) {

            criteria =
                    "WHERE (  CLNT_DATE_CREATED BETWEEN to_date(NVL(" + dFrom +
                    ",'01/01/1900'),'dd/mm/yyyy') AND to_date(NVL(" + dTo +
                    ",'01/01/2099'),'dd/mm/yyyy'))";


        }else if (txtVehicleRegNoRadio.isSelected() && vehicleRegNo != null) {

            criteria =
                    "WHERE CLNT_CODE IN (SELECT ipu_prp_code from gin_insured_property_unds WHERE ipu_property_id ='" +
                    vehicleRegNo + "')";
        }


        else {

            GlobalCC.INFORMATIONREPORTING("Put Search Criteria parameters::");
            return null;
        }

        System.out.println(criteria);
        session.setAttribute("searchCriteria", criteria);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;

    }

    public String actionNewGroup() {
        txtGrpCode.setValue(null);
        txtGrpName.setValue(null);
        txtGrpMinimum.setValue(null);
        txtGrpMax.setValue(null);
        btnsaveGrp.setText("Save");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addGrpClient" + "').show(hints);");
        return null;
    }

    public String actionEditGroupClient() {

        if (session.getAttribute("grpCode") != null) {

            Object key2 = tblClientGroup.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            txtGrpCode.setValue(null);
            txtGrpName.setValue(null);
            txtGrpMinimum.setValue(null);
            txtGrpMax.setValue(null);

            if (nodeBinding != null) {
                txtGrpCode.setValue(nodeBinding.getAttribute("grp_Code"));
                txtGrpName.setValue(nodeBinding.getAttribute("grp_Name"));
                txtGrpMinimum.setValue(nodeBinding.getAttribute("grp_Minimum"));
                txtGrpMax.setValue(nodeBinding.getAttribute("grp_Maximum"));
                btnsaveGrp.setText("update");

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').show(hints);");

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected::");
                return null;
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }
        return null;
    }

    public void actionConfirmDeleteClntGrp(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteGroupClient();
        }
    }

    public String actionConfirmDeleteClntGrpMember(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            return null;
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            if (checkIfAnyTableRowselected(tblClientGrpMembers) == true) {

                RowKeySet rks = new RowKeySetImpl();

                int rowcount = tblClientGrpMembers.getRowCount();

                for (int i = 0; i < rowcount; i++) {
                    tblClientGrpMembers.setRowIndex(i);
                    Object key = tblClientGrpMembers.getRowKey();
                    tblClientGrpMembers.setRowKey(key);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();


                    if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn = null;
                        OracleCallableStatement statement = null;
                        try {
                            conn = dbConnector.getDatabaseConnection();
                            String query =
                                "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";
                            statement =
                                    (OracleCallableStatement)conn.prepareCall(query);
                            statement.setString(1, "D");
                            statement.setBigDecimal(2,
                                                    new BigDecimal(nodeBinding.getAttribute("grpd_Code").toString()));
                            statement.setBigDecimal(3, null);
                            statement.setBigDecimal(4, null);
                            statement.execute();

                            statement.close();
                            conn.commit();
                            conn.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                            return null;
                        }
                    }
                }
                //btnRemoveClientFromGroup.setDisabled(true);
                GlobalCC.refreshUI(btnRemoveClientFromGroup);
                GlobalCC.INFORMATIONREPORTING("Client(s) Deleted Successfully");
                // ADFUtils.findIterator("fetchAllClientsIterator").executeQuery();
                // GlobalCC.refreshUI(tblClientPop);
                ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);
                return null;


            } else {
                GlobalCC.INFORMATIONREPORTING("No  Record  Selected");
                return null;
            }
        }
        return null;
    }

    public String actionDeleteGroupClient() {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;


        if (nodeBinding != null) {


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("grpCode").toString()));
                cst.setString(3, null);
                cst.setBigDecimal(4, null);
                cst.setBigDecimal(5, null);

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
                btnActionDeleteGroupClient.setDisabled(true);
                GlobalCC.refreshUI(btnActionDeleteGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected::");
            return null;
        }
    }

    public String actionHideUserGroupLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addClientGrpPopt" + "').hide(hints);");
        return null;
    }

    public String actionResetSearch() {

        txtSearchName.setValue(null);
        txtSearchOtherName.setValue(null);
        txtSearchSector.setValue(null);
        txtSearchShortDesc.setValue(null);
        txtSearchStatus.setValue(null);
        txtSearchClntType.setValue(null);
        txtSearchPhysical.setValue(null);
        txtSearchPostal.setValue(null);
        txtTelNo.setValue(null);
        txtVehicleRegNo.setValue(null); 
        
        session.setAttribute("sectorCode", null);
        clntDateCreatedFrom.setValue(null);
        clntDateCreatedTo.setValue(null);

        session.setAttribute("sectorName", null);
        session.setAttribute("searchClntStatus", null);
        session.setAttribute("searchClntType", null);
        session.setAttribute("sectorCode", null);
        List<UIComponent> children = resetSearchContainer.getChildren();
        UIComponent component = children.get(0);
        RichInputText sector = (RichInputText)component;
        sector.setValue(null);
        //activate components
        txtSearchName.setDisabled(true);
        txtTelNo.setDisabled(true);
        txtSearchOtherName.setDisabled(true);
        txtSearchSector.setDisabled(true);
        txtSearchShortDesc.setDisabled(true);
        txtSearchStatus.setDisabled(true);
        txtSearchClntType.setDisabled(true);
        txtSearchPhysical.setDisabled(true);
        txtSearchPostal.setDisabled(true);
        clntDateCreatedFrom.setDisabled(true);
        clntDateCreatedTo.setDisabled(true);
        txtAccountNo.setDisabled(true);
        txtVehicleRegNo.setDisabled(true); 

        //refresh radio buttons
        rbtnPartOfAnyName.setSelected(false);
        rbtnExactName.setSelected(false);
        rbtnSector.setSelected(false);
        rbtnStatus.setSelected(false);
        rbtnClientType.setSelected(false);
        rbtnShortDesc.setSelected(false);
        rbtnShortDescLeg.setSelected(false);
        rbtnDateCreated.setSelected(false);
        rbtnPhySicalAddr.setSelected(false);
        rbtnPostalAddr.setSelected(false);
        rbtnOldNames.setSelected(false);
        rbtnCustomerId.setSelected(false);
        rbtnSearchAccountNo.setSelected(false);
        txtVehicleRegNoRadio.setSelected(false); 

        GlobalCC.refreshUI(rbtnSearchAccountNo);
        GlobalCC.refreshUI(rbtnPartOfAnyName);
        GlobalCC.refreshUI(rbtnExactName);
        GlobalCC.refreshUI(rbtnSector);
        GlobalCC.refreshUI(rbtnStatus);
        GlobalCC.refreshUI(rbtnClientType);
        GlobalCC.refreshUI(rbtnShortDesc);
        GlobalCC.refreshUI(rbtnShortDescLeg);
        GlobalCC.refreshUI(rbtnDateCreated);
        GlobalCC.refreshUI(rbtnPhySicalAddr);
        GlobalCC.refreshUI(rbtnPostalAddr);
        GlobalCC.refreshUI(rbtnOldNames);
        GlobalCC.refreshUI(rbtnCustomerId);
        //refesh components
        GlobalCC.refreshUI(txtAccountNo);
        GlobalCC.refreshUI(sector);
        GlobalCC.refreshUI(txtSearchName);
        GlobalCC.refreshUI(txtSearchOtherName);
        GlobalCC.refreshUI(txtSearchSector);
        GlobalCC.refreshUI(txtSearchShortDesc);
        GlobalCC.refreshUI(txtSearchStatus);
        GlobalCC.refreshUI(txtSearchClntType);
        GlobalCC.refreshUI(clntDateCreatedFrom);
        GlobalCC.refreshUI(clntDateCreatedTo);
        GlobalCC.refreshUI(txtSearchPhysical);
        GlobalCC.refreshUI(txtSearchPostal);
        GlobalCC.refreshUI(txtVehicleRegNo);
        GlobalCC.refreshUI(txtVehicleRegNoRadio); 
        
        
        session.setAttribute("searchCriteria", null);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;
    }

    public String actionAddClientToGroup() {
        if (session.getAttribute("grpCode") != null) {
            actionResetSearch();
            btnSelectAll.setVisible(true);
            btnDeselectAll.setVisible(true);
            btnAddGrpMember.setVisible(true);
            session.setAttribute("_search", "addGroup");
            checkboxCol.setVisible(true);
            GlobalCC.refreshUI(tblClientPop);
            GlobalCC.refreshUI(btnSelectAll);
            GlobalCC.refreshUI(btnDeselectAll);
            GlobalCC.refreshUI(btnAddGrpMember);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:searchClientPop" + "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionShowConfirmDelete() {
        if (session.getAttribute("grpCode") != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientGrp" +
                                 "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionSaveGroupClientele() {

        if (btnsaveGrp.getText().equalsIgnoreCase("Save")) {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());

            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            }
            if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            }
            if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();


                GlobalCC.INFORMATIONREPORTING("Record Successfully saved");
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        } else {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());
            String code = GlobalCC.checkNullValues(txtGrpCode.getValue());
            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            } else if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            } else if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            } else if (code == null)

            {
                GlobalCC.errorValueNotEntered("Grp Code required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(code));
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Updated");
                btnActionEditGroupClient.setDisabled(false);
                GlobalCC.refreshUI(btnActionEditGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }


    }

    public boolean checkIfAnyRowSelected() {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = tblClientPop.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();

            if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                count = count + 1;
            }

        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkIfAnyTableRowselected(RichTable table) {


        RowKeySet rks = new RowKeySetImpl();


        int rowcount = table.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)table.getRowData();

            if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                count = count + 1;
            }

        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String selectAll() {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = tblClientPop.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();
            columnSelect.setSelected(true);
            GlobalCC.refreshUI(columnSelect);
        }
        return null;

    }

    public String deselectAll() {

        RowKeySet rks = new RowKeySetImpl();
        if (checkIfAnyTableRowselected(tblClientPop)) {
            int rowcount = tblClientPop.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                tblClientPop.setRowIndex(i);
                Object key = tblClientPop.getRowKey();
                tblClientPop.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientPop.getRowData();

                columnSelect.setSelected(false);
                GlobalCC.refreshUI(columnSelect);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) selected::");
        }
        return null;

    }

    public String actionAcceptClientLov() {
        if (checkIfAnyRowSelected() == true) {
            Object xy = null;
            RowKeySet rks = new RowKeySetImpl();

            int rowcount = tblClientPop.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblClientPop.setRowIndex(i);
                Object key = tblClientPop.getRowKey();
                tblClientPop.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientPop.getRowData();


                Object selected = nodeBinding.getAttribute("selected");

                if (selected.toString().equalsIgnoreCase("true")) {
                    if (session.getAttribute("grpCode") != null) {

                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn = null;
                        OracleCallableStatement statement = null;
                        try {
                            conn = dbConnector.getDatabaseConnection();
                            String query =
                                "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";

                            statement =
                                    (OracleCallableStatement)conn.prepareCall(query);
                            statement.setString(1, "A");
                            statement.setBigDecimal(2, null);
                            statement.setBigDecimal(3,
                                                    new BigDecimal(nodeBinding.getAttribute("code").toString()));
                            statement.setBigDecimal(4,
                                                    new BigDecimal(session.getAttribute("grpCode").toString()));
                            statement.execute();

                            statement.close();
                            conn.commit();
                            conn.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                            return null;
                        }


                    } else {
                        GlobalCC.INFORMATIONREPORTING("First Select Client Group ::");
                        return null;
                    }


                }

            }

            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);
            ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(tblClientPop);


            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;

        }
    }

    public void actionTblClientGroupListener(SelectionEvent selectionEvent) {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpCode",
                                 nodeBinding.getAttribute("grp_Code"));
            btnActionEditGroupClient.setDisabled(false);
            btnActionDeleteGroupClient.setDisabled(false);
            //ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(btnActionEditGroupClient);
            GlobalCC.refreshUI(btnActionDeleteGroupClient);


            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);


        }
    }

    public String actionDeleteClntFromGroup() {


        if (checkIfAnyTableRowselected(tblClientGrpMembers) == true) {


            //session.setAttribute("grpdCode",  nodeBinding.getAttribute("grpd_Code"));

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientGrpMember" +
                                 "').show(hints);");


            return null;


        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }
    }


    public void actiontblGrpMemberListener(SelectionEvent selectionEvent) {

        Object key2 = tblClientGrpMembers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpdCode",
                                 nodeBinding.getAttribute("grpd_Code"));
            btnRemoveClientFromGroup.setDisabled(false);

            GlobalCC.refreshUI(btnRemoveClientFromGroup);

        }
    } 

    public void setTxtsearchCriteriaValue(RichInputText txtsearchCriteriaValue) {
        this.txtsearchCriteriaValue = txtsearchCriteriaValue;
    }

    public RichInputText getTxtsearchCriteriaValue() {
        return txtsearchCriteriaValue;
    }

    public void setTxtSearchShtDesc(RichInputText txtSearchShtDesc) {
        this.txtSearchShtDesc = txtSearchShtDesc;
    }

    public RichInputText getTxtSearchShtDesc() {
        return txtSearchShtDesc;
    }

    public void setTxtSearchName(RichInputText txtSearchName) {
        this.txtSearchName = txtSearchName;
    }

    public RichInputText getTxtSearchName() {
        return txtSearchName;
    }

    public void setTxtOtherName(RichInputText txtOtherName) {
        this.txtOtherName = txtOtherName;
    }

    public RichInputText getTxtOtherName() {
        return txtOtherName;
    }

    public void setTxtSearchOtherName(RichInputText txtSearchOtherName) {
        this.txtSearchOtherName = txtSearchOtherName;
    }

    public RichInputText getTxtSearchOtherName() {
        return txtSearchOtherName;
    }

    public void setTxtSearchSector(RichInputText txtSearchSector) {
        this.txtSearchSector = txtSearchSector;
    }

    public RichInputText getTxtSearchSector() {
        return txtSearchSector;
    }

    public void setTxtSearchShortDesc(RichInputText txtSearchShortDesc) {
        this.txtSearchShortDesc = txtSearchShortDesc;
    }

    public RichInputText getTxtSearchShortDesc() {
        return txtSearchShortDesc;
    }

    public void setTxtSearchStatus(RichSelectOneChoice txtSearchStatus) {
        this.txtSearchStatus = txtSearchStatus;
    }

    public RichSelectOneChoice getTxtSearchStatus() {
        return txtSearchStatus;
    }

    public void setTxtSearchClientType(RichPanelLabelAndMessage txtSearchClientType) {
        this.txtSearchClientType = txtSearchClientType;
    }

    public RichPanelLabelAndMessage getTxtSearchClientType() {
        return txtSearchClientType;
    }

    public void setTxtSearchClntType(RichSelectOneChoice txtSearchClntType) {
        this.txtSearchClntType = txtSearchClntType;
    }

    public RichSelectOneChoice getTxtSearchClntType() {
        return txtSearchClntType;
    }

    public void setRbtnExactNmBig(RichSelectBooleanRadio rbtnExactNmBig) {
        this.rbtnExactNmBig = rbtnExactNmBig;
    }

    public RichSelectBooleanRadio getRbtnExactNmBig() {
        return rbtnExactNmBig;
    }

    public void setRbtnPartNmInOrder(RichSelectBooleanRadio rbtnPartNmInOrder) {
        this.rbtnPartNmInOrder = rbtnPartNmInOrder;
    }

    public RichSelectBooleanRadio getRbtnPartNmInOrder() {
        return rbtnPartNmInOrder;
    }

    public void setRbtnAnyPartOfFirAndOthNm(RichSelectBooleanRadio rbtnAnyPartOfFirAndOthNm) {
        this.rbtnAnyPartOfFirAndOthNm = rbtnAnyPartOfFirAndOthNm;
    }

    public RichSelectBooleanRadio getRbtnAnyPartOfFirAndOthNm() {
        return rbtnAnyPartOfFirAndOthNm;
    }

    public void setRbtnBegPartOfAnyNm(RichSelectBooleanRadio rbtnBegPartOfAnyNm) {
        this.rbtnBegPartOfAnyNm = rbtnBegPartOfAnyNm;
    }

    public RichSelectBooleanRadio getRbtnBegPartOfAnyNm() {
        return rbtnBegPartOfAnyNm;
    }

    public void setRbtnPartOfAnyNameInOrder(RichSelectBooleanRadio rbtnPartOfAnyNameInOrder) {
        this.rbtnPartOfAnyNameInOrder = rbtnPartOfAnyNameInOrder;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyNameInOrder() {
        return rbtnPartOfAnyNameInOrder;
    }

    public void setRbtnExactName(RichSelectBooleanRadio rbtnExactName) {
        this.rbtnExactName = rbtnExactName;
    }

    public RichSelectBooleanRadio getRbtnExactName() {
        return rbtnExactName;
    }

    public void setRbtnPartOfAnyName(RichSelectBooleanRadio rbtnPartOfAnyName) {
        this.rbtnPartOfAnyName = rbtnPartOfAnyName;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyName() {
        return rbtnPartOfAnyName;
    }

    public void setRbtnBegPartOfFstNmAndOName(RichSelectBooleanRadio rbtnBegPartOfFstNmAndOName) {
        this.rbtnBegPartOfFstNmAndOName = rbtnBegPartOfFstNmAndOName;
    }

    public RichSelectBooleanRadio getRbtnBegPartOfFstNmAndOName() {
        return rbtnBegPartOfFstNmAndOName;
    }

    public String actionsearchClient() {
        // Add event code here...
        return null;
    }

    public void setSectorName(Object sectorName) {
        this.sectorName = session.getAttribute("sectorName");
    }

    public Object getSectorName() {
        return session.getAttribute("sectorName");
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
    }


    public String actionCreateNewGroup() {
        // Add event code here...
        return null;
    }

    public String actionEditGroupClients() {
        // Add event code here...
        return null;
    }

    public void setBtnActionEditGroupClient(RichCommandButton btnActionEditGroupClient) {
        this.btnActionEditGroupClient = btnActionEditGroupClient;
    }

    public RichCommandButton getBtnActionEditGroupClient() {
        return btnActionEditGroupClient;
    }

    public void setTblClientGroup(RichTable tblClientGroup) {
        this.tblClientGroup = tblClientGroup;
    }

    public RichTable getTblClientGroup() {
        return tblClientGroup;
    }

    public void setBtnActionDeleteGroupClient(RichCommandButton btnActionDeleteGroupClient) {
        this.btnActionDeleteGroupClient = btnActionDeleteGroupClient;
    }

    public RichCommandButton getBtnActionDeleteGroupClient() {
        return btnActionDeleteGroupClient;
    }

    public void setBtnRemoveClientFromGroup(RichCommandButton btnRemoveClientFromGroup) {
        this.btnRemoveClientFromGroup = btnRemoveClientFromGroup;
    }

    public RichCommandButton getBtnRemoveClientFromGroup() {
        return btnRemoveClientFromGroup;
    }

    public void setTblClientGrpMembers(RichTable tblClientGrpMembers) {
        this.tblClientGrpMembers = tblClientGrpMembers;
    }

    public RichTable getTblClientGrpMembers() {
        return tblClientGrpMembers;
    }

    public void setTxtGrpCode(RichInputText txtGrpCode) {
        this.txtGrpCode = txtGrpCode;
    }

    public RichInputText getTxtGrpCode() {
        return txtGrpCode;
    }

    public void setTxtGrpName(RichInputText txtGrpName) {
        this.txtGrpName = txtGrpName;
    }

    public RichInputText getTxtGrpName() {
        return txtGrpName;
    }

    public void setTxtGrpMinimum(RichInputNumberSpinbox txtGrpMinimum) {
        this.txtGrpMinimum = txtGrpMinimum;
    }

    public RichInputNumberSpinbox getTxtGrpMinimum() {
        return txtGrpMinimum;
    }

    public void setTxtGrpMax(RichInputNumberSpinbox txtGrpMax) {
        this.txtGrpMax = txtGrpMax;
    }

    public RichInputNumberSpinbox getTxtGrpMax() {
        return txtGrpMax;
    }

    public void setBtnsaveGrp(RichCommandButton btnsaveGrp) {
        this.btnsaveGrp = btnsaveGrp;
    }

    public RichCommandButton getBtnsaveGrp() {
        return btnsaveGrp;
    }

    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
    }

    public void setColumnSelect(RichSelectBooleanCheckbox columnSelect) {
        this.columnSelect = columnSelect;
    }

    public RichSelectBooleanCheckbox getColumnSelect() {
        return columnSelect;
    }

    public void cboSearchStatusListener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            session.setAttribute("searchClntStatus", null);

            String searchStatus =
                GlobalCC.checkNullValues(txtSearchStatus.getValue());
            session.setAttribute("searchClntStatus", searchStatus);

        }
    }

    public void cboSearchClntTypelistener(ValueChangeEvent evt) {
        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {


            String type =
                GlobalCC.checkNullValues(txtSearchClntType.getValue());
            session.setAttribute("searchClntType", type);

        }
    }

    public String actionSelectAllGrpmembers() {
        RowKeySet rks = new RowKeySetImpl();
        int count = 0;
        int rowcount = tblClientGrpMembers.getRowCount();
        if (rowcount > 0) {

            for (int i = 0; i < rowcount; i++) {
                tblClientGrpMembers.setRowIndex(i);
                Object key = tblClientGrpMembers.getRowKey();
                tblClientGrpMembers.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();
                nodeBinding.setAttribute("selected", true);
                grpMemberSelect.setSelected(true);
                GlobalCC.refreshUI(grpMemberSelect);
                count++;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No Records::");
        }
        return null;

    }

    public String actionDeSelectAllGrpmembers() {
        RowKeySet rks = new RowKeySetImpl();
        int count = 0;
        if (checkIfAnyTableRowselected(tblClientGrpMembers)) {
            int rowcount = tblClientGrpMembers.getRowCount();
            if (rowcount > 0) {

                for (int i = 0; i < rowcount; i++) {
                    tblClientGrpMembers.setRowIndex(i);
                    Object key = tblClientGrpMembers.getRowKey();
                    tblClientGrpMembers.setRowKey(key);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)tblClientGrpMembers.getRowData();

                    grpMemberSelect.setSelected(false);
                    GlobalCC.refreshUI(grpMemberSelect);
                    count++;
                }

            } else {
                GlobalCC.INFORMATIONREPORTING("No Records::");
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) Selected::");
        }
        return null;

    }


    public void setGrpMemberSelect(RichSelectBooleanCheckbox grpMemberSelect) {
        this.grpMemberSelect = grpMemberSelect;
    }

    public RichSelectBooleanCheckbox getGrpMemberSelect() {
        return grpMemberSelect;
    }


    public void setBtnSelectAllGrpMembers(RichCommandButton btnSelectAllGrpMembers) {
        this.btnSelectAllGrpMembers = btnSelectAllGrpMembers;
    }

    public RichCommandButton getBtnSelectAllGrpMembers() {
        return btnSelectAllGrpMembers;
    }


    public void setResetContainer(RichCommandButton resetContainer) {
        this.resetContainer = resetContainer;
    }

    public RichCommandButton getResetContainer() {
        return resetContainer;
    }

    public void setResetSearchContainer(RichPanelLabelAndMessage resetSearchContainer) {
        this.resetSearchContainer = resetSearchContainer;
    }

    public RichPanelLabelAndMessage getResetSearchContainer() {
        return resetSearchContainer;
    }

    public void setTxtBranchMgrSeqNo(RichInputNumberSpinbox txtBranchMgrSeqNo) {
        this.txtBranchMgrSeqNo = txtBranchMgrSeqNo;
    }

    public RichInputNumberSpinbox getTxtBranchMgrSeqNo() {
        return txtBranchMgrSeqNo;
    }

    public void setBtnAddGrpMember(RichCommandButton btnAddGrpMember) {
        this.btnAddGrpMember = btnAddGrpMember;
    }

    public RichCommandButton getBtnAddGrpMember() {
        return btnAddGrpMember;
    }

    public void setBtnDeselectAll(RichCommandButton btnDeselectAll) {
        this.btnDeselectAll = btnDeselectAll;
    }

    public RichCommandButton getBtnDeselectAll() {
        return btnDeselectAll;
    }

    public void setBtnSelectAll(RichCommandButton btnSelectAll) {
        this.btnSelectAll = btnSelectAll;
    }

    public RichCommandButton getBtnSelectAll() {
        return btnSelectAll;
    }

    public void setTblClientHolder(RichPanelCollection tblClientHolder) {
        this.tblClientHolder = tblClientHolder;
    }

    public RichPanelCollection getTblClientHolder() {
        return tblClientHolder;
    }

    public void setClntDateCreatedFrom(RichInputDate clntDateCreatedFrom) {
        this.clntDateCreatedFrom = clntDateCreatedFrom;
    }

    public RichInputDate getClntDateCreatedFrom() {
        return clntDateCreatedFrom;
    }

    public void setClntDateCreatedTo(RichInputDate clntDateCreatedTo) {
        this.clntDateCreatedTo = clntDateCreatedTo;
    }

    public RichInputDate getClntDateCreatedTo() {
        return clntDateCreatedTo;
    }

    public void setRbtnDateCreated(RichSelectBooleanRadio rbtnDateCreated) {
        this.rbtnDateCreated = rbtnDateCreated;
    }

    public RichSelectBooleanRadio getRbtnDateCreated() {
        return rbtnDateCreated;
    }

    public void actionConfirmDeleteAuditor(DialogEvent dialogEvent) {
        // Add event code here...
    }

    public void setCheckboxCol(RichColumn checkboxCol) {
        this.checkboxCol = checkboxCol;
    }

    public RichColumn getCheckboxCol() {
        return checkboxCol;
    }

    public void setEditCol(RichColumn editCol) {
        this.editCol = editCol;
    }

    public RichColumn getEditCol() {
        return editCol;
    }

    public String test() {

        return null;
    }

    public void setRbtnClientType(RichSelectBooleanRadio rbtnClientType) {
        this.rbtnClientType = rbtnClientType;
    }

    public RichSelectBooleanRadio getRbtnClientType() {
        return rbtnClientType;
    }

    public void setRbtnStatus(RichSelectBooleanRadio rbtnStatus) {
        this.rbtnStatus = rbtnStatus;
    }

    public RichSelectBooleanRadio getRbtnStatus() {
        return rbtnStatus;
    }

    public void setRbtnSector(RichSelectBooleanRadio rbtnSector) {
        this.rbtnSector = rbtnSector;
    }

    public RichSelectBooleanRadio getRbtnSector() {
        return rbtnSector;
    }

    public String actionEditPrintServer() {
        // Add event code here...
        return null;
    }

    public String actionDeletePrintServer() {
        // Add event code here...
        return null;
    }

    public String actionSavePrinterServer() {
        // Add event code here...
        return null;
    }

    public void setRbtnPhySicalAddr(RichSelectBooleanRadio rbtnPhySicalAddr) {
        this.rbtnPhySicalAddr = rbtnPhySicalAddr;
    }

    public RichSelectBooleanRadio getRbtnPhySicalAddr() {
        return rbtnPhySicalAddr;
    }

    public void setRbtnPostalAddr(RichSelectBooleanRadio rbtnPostalAddr) {
        this.rbtnPostalAddr = rbtnPostalAddr;
    }

    public RichSelectBooleanRadio getRbtnPostalAddr() {
        return rbtnPostalAddr;
    }

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setRbtnCustomerId(RichSelectBooleanRadio rbtnCustomerId) {
        this.rbtnCustomerId = rbtnCustomerId;
    }

    public RichSelectBooleanRadio getRbtnCustomerId() {
        return rbtnCustomerId;
    }

    public void setRbtnIncome(RichSelectBooleanRadio rbtnIncome) {
        this.rbtnIncome = rbtnIncome;
    }

    public RichSelectBooleanRadio getRbtnIncome() {
        return rbtnIncome;
    }

    public void setTxtSearchOldId(RichInputText txtSearchOldId) {
        this.txtSearchOldId = txtSearchOldId;
    }

    public RichInputText getTxtSearchOldId() {
        return txtSearchOldId;
    }

    public void setRbtnOldNames(RichSelectBooleanRadio rbtnOldNames) {
        this.rbtnOldNames = rbtnOldNames;
    }

    public RichSelectBooleanRadio getRbtnOldNames() {
        return rbtnOldNames;
    }

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (rbtnSearchAccountNo.isSelected()) {

                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(false);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                clientPolicyNumber.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                clientPolicyNumber.setValue(null);
                txtSearchPostal.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtIdNum.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(btnSectorLov);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);


            }

            else if (rbtnSearchOldAccountNo.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);

                txtOldAccountNo.setDisabled(false);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                clientPolicyNumber.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                clientPolicyNumber.setValue(null);
                txtSearchPostal.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtIdNum.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                GlobalCC.refreshUI(btnSectorLov);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            }


            else if (rbtnCustomerId.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                GlobalCC.refreshUI(btnSectorLov);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(false);
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                txtSearchName.setLabel("SurName");
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnShortDesc.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);

                txtSearchShortDesc.setDisabled(false);

                txtSearchName.setLabel("SurName");
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnShortDescLeg.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);

                txtSearchShortDesc.setDisabled(false);

                txtSearchName.setLabel("SurName");
                txtSearchStatus.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                pinNumber.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnClientType.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);

                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchClntType.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnStatus.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtSearchStatus.setDisabled(false);
                txtSearchStatus.setValue(null);
                txtAccountNo.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchPostal.setDisabled(true);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);
                txtSearchName.setLabel("SurName");
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(false);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPhySicalAddr.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPostalAddr.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPostal.setDisabled(false);

                pinNumber.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);

            } else if (rbtnSector.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);

                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);

                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                txtSearchSector.setDisabled(false);
                txtSearchName.setLabel("SurName");

                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                btnSectorLov.setDisabled(false);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnPartOfAnyName.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);

                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);

                txtSearchName.setDisabled(false);
                txtSearchName.setLabel("SurName");

                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnOldNames.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setLabel("SurName");
                txtSearchName.setDisabled(false);


                txtSearchOtherName.setDisabled(false);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null); 
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnExactName.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);

                txtSearchName.setDisabled(false);
                txtSearchName.setLabel("Full Name");

                txtSearchOtherName.setDisabled(true);

                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtPassport.setDisabled(true);
                txtPassport.setValue(null);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnDateCreated.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                btnSectorLov.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                txtSearchName.setLabel("SurName");

                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);

                clntDateCreatedFrom.setDisabled(false);


                clntDateCreatedTo.setDisabled(false);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(false);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(false);
                txtSearchStatus.setDisabled(false);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                btnSectorLov.setDisabled(false);
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rdbnDateOfBirth.isSelected()) {
                txtDateOfBirth.setDisabled(false);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (rbtnIdNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(false);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                txtSearchName.setLabel("SurName");
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtClientPolNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(true);
                clientPolicyNumber.setDisabled(false);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                pinNumber.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtPinNumber.isSelected()) {
                pinNumber.setDisabled(false);
                txtDateOfBirth.setDisabled(true);
                txtIdNum.setDisabled(true);
                clientPolicyNumber.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);
                btnSectorLov.setDisabled(true);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                txtSearchName.setLabel("SurName");
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtPassport.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtPassPortNumber.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(false);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtClaimNo.setDisabled(true);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (txtTelNoRadio.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtClaimNo.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(false);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            } else if (sbrClaimNo.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(false);
                // txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                txtVehicleRegNo.setDisabled(true);
                txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            }else if (txtVehicleRegNoRadio.isSelected()) {
                txtDateOfBirth.setDisabled(true);
                txtPassport.setDisabled(true);
                txtAccountNo.setDisabled(true);
                txtAccountNo.setValue(null);
                clientPolicyNumber.setDisabled(true);
                clientPolicyNumber.setValue(null);
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchSector.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchStatus.setValue(null);
                txtSearchClntType.setValue(null);

                txtSearchPostal.setValue(null);
                session.setAttribute("sectorCode", null);
                clntDateCreatedFrom.setValue(null);
                clntDateCreatedTo.setValue(null);
                txtSearchPostal.setDisabled(true);
                session.setAttribute("sectorName", null);
                session.setAttribute("searchClntStatus", null);
                session.setAttribute("searchClntType", null);
                session.setAttribute("sectorCode", null);
                List<UIComponent> children =
                    resetSearchContainer.getChildren();
                UIComponent component = children.get(0);
                RichInputText sector = (RichInputText)component;
                sector.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchSector.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchClntType.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                txtSearchName.setLabel("SurName");
                txtSearchPhysical.setDisabled(true);
                txtPassport.setValue(null);
                pinNumber.setDisabled(true);
                txtSearchPostal.setDisabled(true);

                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtIdNum.setDisabled(true);
                txtTelNo.setDisabled(true);
                txtTelNo.setValue(null);
                txtClaimNo.setDisabled(true);
                // txtClaimNo.setValue(null);
                txtOldAccountNo.setValue(null);
                txtOldAccountNo.setDisabled(true);
                
                txtVehicleRegNo.setDisabled(false);
                //txtVehicleRegNo.setValue(null);
                GlobalCC.refreshUI(gridClientSearchDetails);
            }

        }
    }


    public void setBtnSectorLov(RichCommandButton btnSectorLov) {
        this.btnSectorLov = btnSectorLov;
    }

    public RichCommandButton getBtnSectorLov() {
        return btnSectorLov;
    }

    public void setTblAccountOfficers(RichTable tblAccountOfficers) {
        this.tblAccountOfficers = tblAccountOfficers;
    }

    public RichTable getTblAccountOfficers() {
        return tblAccountOfficers;
    }

    public void tblAccountOfficersListener(SelectionEvent selectionEvent) {

        Object key2 = tblAccountOfficers.getSelectedRowData();

        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String userCode = nodeBinding.getAttribute("userCode").toString();
            session.setAttribute("sysUserCode", userCode);
            btnEditAccOfficer.setDisabled(false);

            GlobalCC.refreshUI(btnEditAccOfficer);
            ADFUtils.findIterator("fetchAllClientsByAccountOfficerIterator").executeQuery();
            lbCount.setValue(session.getAttribute("client_count"));
            GlobalCC.refreshUI(lbCount);
            GlobalCC.refreshUI(tblAccClients);


        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected:");
        }
    }

    public String btnEditAccOfficer() {
        // Add event code here...
        return null;
    }

    public void setBtnEditAccOfficer(RichCommandButton btnEditAccOfficer) {
        this.btnEditAccOfficer = btnEditAccOfficer;
    }

    public RichCommandButton getBtnEditAccOfficer() {
        return btnEditAccOfficer;
    }

    public String actionEditAccOffice() {
        if (session.getAttribute("sysUserCode") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:UsersPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("NO RECORD SELECTED");
        }

        return null;
    }

    public void setLbCount(RichOutputLabel lbCount) {
        this.lbCount = lbCount;
    }

    public RichOutputLabel getLbCount() {
        return lbCount;
    }

    public void setTblAccClients(RichTable tblAccClients) {
        this.tblAccClients = tblAccClients;
    }

    public RichTable getTblAccClients() {
        return tblAccClients;
    }

    public String actionChangeAccOfficer() {
        Object key2 = tblUserList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            String userCode = nodeBinding.getAttribute("userCode").toString();
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_CLIENTS_PKG.UpdateAccountOfficer(?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.VARCHAR);
                cst.setBigDecimal(2,
                                  session.getAttribute("sysUserCode") == null ?
                                  null :
                                  new BigDecimal(session.getAttribute("sysUserCode").toString()));
                cst.setBigDecimal(3,
                                  new BigDecimal(userCode == null ? null : userCode));
                cst.execute();
                String err = cst.getString(1);
                cst.close();
                conn.commit();
                conn.close();

                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                    return null;
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Successfully updated");

                    session.setAttribute("sysUserCode", null);
                    btnEditAccOfficer.setDisabled(true);
                    lbCount.setValue(null);


                    ADFUtils.findIterator("fetchAllClientsByAccountOfficerIterator").executeQuery();
                    ADFUtils.findIterator("FindAccountOfficersIterator").executeQuery();
                    GlobalCC.refreshUI(btnEditAccOfficer);
                    GlobalCC.refreshUI(tblAccountOfficers);
                    GlobalCC.refreshUI(tblAccClients);
                }

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }
        GlobalCC.dismissPopUp("pt1", "accountOfficersPop");
        GlobalCC.dismissPopUp("pt1", "UsersPop");

        return null;
    }

    public void setBtnChangeAccOfficer(RichCommandButton btnChangeAccOfficer) {
        this.btnChangeAccOfficer = btnChangeAccOfficer;
    }

    public RichCommandButton getBtnChangeAccOfficer() {
        return btnChangeAccOfficer;
    }

    public void setTblUserList(RichTable tblUserList) {
        this.tblUserList = tblUserList;
    }

    public RichTable getTblUserList() {
        return tblUserList;
    }

    public void setBaseClientBackingBean1(RichInputText baseClientBackingBean1) {
        this.baseClientBackingBean1 = baseClientBackingBean1;
    }

    public RichInputText getBaseClientBackingBean1() {
        return baseClientBackingBean1;
    }

    public void setTxtAccountNo(RichInputText txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public RichInputText getTxtAccountNo() {
        return txtAccountNo;
    }


    public void setRbtnSearchAccountNo(RichSelectBooleanRadio rbtnSearchAccountNo) {
        this.rbtnSearchAccountNo = rbtnSearchAccountNo;
    }

    public RichSelectBooleanRadio getRbtnSearchAccountNo() {
        return rbtnSearchAccountNo;
    }

    public void rbtnAccountNoChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setGridClientSearchDetails(HtmlPanelGrid gridClientSearchDetails) {
        this.gridClientSearchDetails = gridClientSearchDetails;
    }

    public HtmlPanelGrid getGridClientSearchDetails() {
        return gridClientSearchDetails;
    }


    public void enterKeyPressed(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            String action = actionAcceptSearchCriteria();
        }
    }

    public void setRbtnShortDescLeg(RichSelectBooleanRadio rbtnShortDescLeg) {
        this.rbtnShortDescLeg = rbtnShortDescLeg;
    }

    public RichSelectBooleanRadio getRbtnShortDescLeg() {
        return rbtnShortDescLeg;
    }

    public void setRdbnDateOfBirth(RichSelectBooleanRadio rdbnDateOfBirth) {
        this.rdbnDateOfBirth = rdbnDateOfBirth;
    }

    public RichSelectBooleanRadio getRdbnDateOfBirth() {
        return rdbnDateOfBirth;
    }

    public void setTxtDateOfBirth(RichInputDate txtDateOfBirth) {
        this.txtDateOfBirth = txtDateOfBirth;
    }

    public RichInputDate getTxtDateOfBirth() {
        return txtDateOfBirth;
    }

    public void setRbtnIdNumber(RichSelectBooleanRadio rbtnIdNumber) {
        this.rbtnIdNumber = rbtnIdNumber;
    }

    public RichSelectBooleanRadio getRbtnIdNumber() {
        return rbtnIdNumber;
    }

    public void setTxtIdNum(RichInputText txtIdNum) {
        this.txtIdNum = txtIdNum;
    }

    public RichInputText getTxtIdNum() {
        return txtIdNum;
    }

    public void setTxtClientPolNumber(RichSelectBooleanRadio txtClientPolNumber) {
        this.txtClientPolNumber = txtClientPolNumber;
    }

    public RichSelectBooleanRadio getTxtClientPolNumber() {
        return txtClientPolNumber;
    }

    public void setClientPolicyNumber(RichInputText clientPolicyNumber) {
        this.clientPolicyNumber = clientPolicyNumber;
    }

    public RichInputText getClientPolicyNumber() {
        return clientPolicyNumber;
    }

    public void setTxtPinNumber(RichSelectBooleanRadio txtPinNumber) {
        this.txtPinNumber = txtPinNumber;
    }

    public RichSelectBooleanRadio getTxtPinNumber() {
        return txtPinNumber;
    }

    public void setPinNumber(RichInputText pinNumber) {
        this.pinNumber = pinNumber;
    }

    public RichInputText getPinNumber() {
        return pinNumber;
    }

    public void setTxtPassPortNumber(RichSelectBooleanRadio txtPassPortNumber) {
        this.txtPassPortNumber = txtPassPortNumber;
    }

    public RichSelectBooleanRadio getTxtPassPortNumber() {
        return txtPassPortNumber;
    }

    public void setTxtPassport(RichInputText txtPassport) {
        this.txtPassport = txtPassport;
    }

    public RichInputText getTxtPassport() {
        return txtPassport;
    }

    public void setTxtTelNo(RichInputText txtTelNo) {
        this.txtTelNo = txtTelNo;
    }

    public RichInputText getTxtTelNo() {
        return txtTelNo;
    }

    public void setTxtTelNoRadio(RichSelectBooleanRadio txtTelNoRadio) {
        this.txtTelNoRadio = txtTelNoRadio;
    }

    public RichSelectBooleanRadio getTxtTelNoRadio() {
        return txtTelNoRadio;
    }

    public void setTxtClaimNo(RichInputText txtClaimNo) {
        this.txtClaimNo = txtClaimNo;
    }

    public RichInputText getTxtClaimNo() {
        return txtClaimNo;
    }

    public void setSbrClaimNo(RichSelectBooleanRadio sbrClaimNo) {
        this.sbrClaimNo = sbrClaimNo;
    }

    public RichSelectBooleanRadio getSbrClaimNo() {
        return sbrClaimNo;
    }

    public void setTxtOldAccountNo(RichInputText txtOldAccountNo) {
        this.txtOldAccountNo = txtOldAccountNo;
    }

    public RichInputText getTxtOldAccountNo() {
        return txtOldAccountNo;
    }

    public void setRbtnSearchOldAccountNo(RichSelectBooleanRadio rbtnSearchOldAccountNo) {
        this.rbtnSearchOldAccountNo = rbtnSearchOldAccountNo;
    }

    public RichSelectBooleanRadio getRbtnSearchOldAccountNo() {
        return rbtnSearchOldAccountNo;
    }

    public void setTxtVehicleRegNo(RichInputText txtVehicleRegNo) {
        this.txtVehicleRegNo = txtVehicleRegNo;
    }

    public RichInputText getTxtVehicleRegNo() {
        return txtVehicleRegNo;
    }

    public void setTxtVehicleRegNoRadio(RichSelectBooleanRadio txtVehicleRegNoRadio) {
        this.txtVehicleRegNoRadio = txtVehicleRegNoRadio;
    }

    public RichSelectBooleanRadio getTxtVehicleRegNoRadio() {
        return txtVehicleRegNoRadio;
    }

    public Object getSectorName1() {
        return sectorName;
    }


    public void setRenderer(Rendering renderer) {
        this.renderer = renderer;
    }

    public Rendering getRenderer() {
        return renderer;
    }
}
