package TurnQuest.view.Leads;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import com.Ostermiller.util.CSVParser;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class LeadsManipulation {
    private RichInputText description;
    private RichInputText coTel;
    private RichInputText title;
    private RichInputText surname;
    private RichInputText otherNames;
    private RichInputText mobileNo;
    private RichInputText fax;
    private RichInputText email;
    private RichInputText postalAddress;
    private RichInputText postalCode;
    private RichInputText physicalAddress;
    private RichInputText source;
    private RichInputText status;
    private RichInputDate leadDate;
    private RichSelectOneChoice converted;
    private RichInputText portName;
    private RichSelectOneChoice portContr;
    private RichInputNumberSpinbox portAmount;
    private RichInputText portSale;
    private RichInputDate portCloseDate;
    private RichInputNumberSpinbox annRevenue;
    private RichInputText website;
    private RichTree leadsTree;
    private RichSelectOneChoice industry;
    private RichSelectOneChoice type;
    private RichInputText coName;
    private RichInputText leadComment;
    private RichInputDate leadCommentDate;
    private RichSelectOneChoice leadCommentDisposition;
    private RichTable leadCommentsTable;
    private RichTable leadActivitiesTable;
    private RichTable activitiesTable;
    private RichTable sourcesTable;
    private RichTable postalCodeTable;
    private RichTable statusTable;
    private RichTable tblCountryLOv;
    private RichTable tblStatesLOV;
    private RichTable tblUsersLOv;
    private RichTable tblTeamsLOV;
    private RichTable tblAccountsLOV;
    private RichInputText txtCountryCode;
    private RichInputText txtStateCode;
    private RichInputText txtTownCode;
    private RichInputText txtUserCode;
    private RichInputText txtTeamCode;
    private RichInputText txtAccountCode;
    private RichInputText txtCountryName;
    private RichInputText txtStateName;
    private RichInputText txtTownName;
    private RichInputText txtOrgName;
    private RichInputText txtUserName;
    private RichInputText txtTeamName;
    private RichInputText txtAccountName;
    private RichTable tblOrgsLOV;
    private RichInputText txtOrgCode;
    private RichTable tblTowns;
    private RichInputText desc;
    private RichTable leadStatusesTable;
    private RichTable leadSources;
    private RichTable tblLeads;
    private RichTable tblSystems;
    private RichInputText txtSystemCode;
    private RichInputText txtProductCode;
    private RichInputText txtCampaignCode;
    private RichInputText txtCurrencyCode;
    private RichTable tblProducts;
    private RichTable tblCurrencies;
    private RichInputText txtLeadCode;
    private RichInputText txtSystemName;
    private RichInputText txtProductName;
    private RichInputText txtCampaignName;
    private RichInputText txtCurrencyName;
    private RichDialog dlgNewEditLead;
    private RichInputText txtLeadSrcCode;
    private RichInputText txtLeadStatusCode;
    private RichTable tblCampaigns;

    private RichPanelBox pnBoxLeadDetails;
    private RichInputText txtSearchValue;
    private RichOutputLabel lblSearchValue;
    private RichSelectOneChoice selectCriteria;
    private RichCommandButton btnCountry;
    private RichCommandButton btnCampaign;
    private RichCommandButton btnSystems;
    private RichTable tblSystemsSearch;
    private RichCommandButton btnProducts;
    private RichCommandButton btnLeadSource;
    private RichCommandButton btnPostalCode;
    private RichCommandButton btnLeadStatus;
    private RichInputDate dtDateCreated;
    private RichCommandButton btnUsers;
    private RichCommandButton btnTeams;
    private RichCommandButton btnAccounts;
    private RichTable tblOrgDivisions;
    private RichInputText txtOrgDivCode;
    private RichInputText txtOrgDivName;
    private RichInputText txtOccupation;
    private RichInputText txtClntShtDesc;
    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private String filetype;
    private UISelectItems companyNameLabel;
    private RichOutputLabel lblSurname;
    private RichOutputLabel lblOtherNames;
    private RichOutputLabel lblCompanyName;


    public LeadsManipulation() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public void clearLeadDetails() {
        description.setValue(null);
        coName.setValue(null);
        coTel.setValue(null);
        title.setValue(null);
        surname.setValue(null);
        otherNames.setValue(null);
        mobileNo.setValue(null);
        fax.setValue(null);
        email.setValue(null);
        postalAddress.setValue(null);
        postalCode.setValue(null);
        physicalAddress.setValue(null);
        source.setValue(null);
        status.setValue(null);
        leadDate.setValue(null);
        converted.setValue(null);
        portName.setValue(null);
        portContr.setValue(null);
        portAmount.setValue(null);
        portSale.setValue(null);
        portCloseDate.setValue(null);
        annRevenue.setValue(null);
        website.setValue(null);
        industry.setValue(null);

        txtCountryCode.setValue(null);
        txtStateCode.setValue(null);
        txtTownCode.setValue(null);
        txtUserCode.setValue(null);
        txtTeamCode.setValue(null);
        txtAccountCode.setValue(null);
        txtCountryName.setValue(null);
        txtStateName.setValue(null);
        txtTownName.setValue(null);
        txtOrgName.setValue(null);
        txtUserName.setValue(null);
        txtTeamName.setValue(null);
        txtAccountName.setValue(null);
        txtOrgCode.setValue(null);
        txtOrgDivName.setValue(null);
        txtOrgDivCode.setValue(null);
        txtCurrencyName.setValue(null);
        txtCurrencyCode.setValue(null);
        txtProductCode.setValue(null);
        txtProductName.setValue(null);
        txtCampaignCode.setValue(null);
        txtCampaignName.setValue(null);
        txtSystemCode.setValue(null);
        txtSystemName.setValue(null);
        txtSearchValue.setValue(null);
        session.setAttribute("Lead_code", null);
        ADFUtils.findIterator("findLeadActivitiesIterator").executeQuery();
        GlobalCC.refreshUI(leadActivitiesTable);

        GlobalCC.refreshUI(pnBoxLeadDetails);
        GlobalCC.refreshUI(txtSearchValue);
        ADFUtils.findIterator("findLeadCommentsIterator").executeQuery();
        GlobalCC.refreshUI(leadCommentsTable);

    }

    public String AddLead() {
        clearLeadDetails();

        return null;
    }

    public String findStatusSelected() {

        Object key = statusTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.dismissPopUp("crm", "leadStatusPOP");
            return null;
        }
        if (btnLeadStatus.getShortDesc() != null) {
            txtSearchValue.setValue(nodeBinding.getAttribute("leadStatusDesc"));
            String statusCode =
                nodeBinding.getAttribute("leadStatusCode").toString();
            String searchCriteria = "WHERE LDS_LSTS_CODE=" + statusCode;
            session.setAttribute("SEARCHCRITERIA", searchCriteria);
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            txtSearchValue.setDisabled(true);
            GlobalCC.refreshUI(txtSearchValue);
            GlobalCC.refreshUI(tblLeads);
            //session.setAttribute("SEARCHCRITERIA", null);
            btnLeadStatus.setShortDesc(null);
            GlobalCC.dismissPopUp("crm", "leadStatusPOP");
            // return null;
        }
        status.setValue(nodeBinding.getAttribute("leadStatusDesc"));
        txtLeadStatusCode.setValue(nodeBinding.getAttribute("leadStatusCode"));
        GlobalCC.refreshUI(status);
        GlobalCC.dismissPopUp("crm", "leadStatusPOP");
        return null;
    }

    public String findSourceSelected() {
        Object key = sourcesTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.dismissPopUp("crm", "leadSourcePOP");
            return null;
        }

        if (btnLeadSource.getShortDesc() != null) {
            txtSearchValue.setValue(nodeBinding.getAttribute("leadSourceDesc"));
            String leadSourceCode =
                nodeBinding.getAttribute("leadSourceCode").toString();
            String searchCriteria = "WHERE LDS_LDSRC_CODE=" + leadSourceCode;
            session.setAttribute("SEARCHCRITERIA", searchCriteria);
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            txtSearchValue.setDisabled(true);
            GlobalCC.refreshUI(txtSearchValue);
            GlobalCC.refreshUI(tblLeads);
            //session.setAttribute("SEARCHCRITERIA", null);
            GlobalCC.dismissPopUp("crm", "leadSourcePOP");
            btnLeadSource.setShortDesc(null);
            //return null;
        }
        source.setValue(nodeBinding.getAttribute("leadSourceDesc"));
        txtLeadSrcCode.setValue(nodeBinding.getAttribute("leadSourceCode"));
        GlobalCC.refreshUI(source);
        GlobalCC.dismissPopUp("crm", "leadSourcePOP");
        return null;

    }

    public String findPostalCodeSelected() {
        Object key = postalCodeTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.dismissPopUp("crm", "postalCodePOP");
            return null;
        }

        
            postalCode.setValue(nodeBinding.getAttribute("pstZipCode"));            
            GlobalCC.refreshUI(postalCode);            
            GlobalCC.dismissPopUp("crm", "postalCodePOP");
           
        
        return null;

    }
    public String DeleteLeadDetails() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            if (session.getAttribute("ldsCode") == null) {
                GlobalCC.sysInformation("Select A Lead");
                return null;
            }


            String query = "begin TQC_SETUPS_PKG.delete_lead(?); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setBigDecimal(1,
                                   (BigDecimal)session.getAttribute("ldsCode"));

            callStmt.execute();

            callStmt.close();
            conn.close();

            GlobalCC.sysInformation("Lead Details Successfully Deleted.");

            session.setAttribute("ldsCode", null);

            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(leadsTree);

            description.setValue(null);
            coName.setValue(null);
            coTel.setValue(null);
            title.setValue(null);
            surname.setValue(null);
            otherNames.setValue(null);
            mobileNo.setValue(null);
            fax.setValue(null);
            email.setValue(null);
            postalAddress.setValue(null);
            postalCode.setValue(null);
            physicalAddress.setValue(null);
            source.setValue(null);
            status.setValue(null);
            leadDate.setValue(null);
            converted.setValue(null);
            portName.setValue(null);
            portContr.setValue(null);
            portAmount.setValue(null);
            portSale.setValue(null);
            portCloseDate.setValue(null);
            annRevenue.setValue(null);
            website.setValue(null);
            industry.setValue(null);

            txtCountryCode.setValue(null);
            txtStateCode.setValue(null);
            txtTownCode.setValue(null);
            txtUserCode.setValue(null);
            txtTeamCode.setValue(null);
            txtAccountCode.setValue(null);
            txtCountryName.setValue(null);
            txtStateName.setValue(null);
            txtTownName.setValue(null);
            txtOrgName.setValue(null);
            txtUserName.setValue(null);
            txtTeamName.setValue(null);
            txtAccountName.setValue(null);
            txtOrgCode.setValue(null);
            txtOrgDivName.setValue(null);
            txtOrgDivCode.setValue(null);


            session.setAttribute("ldsCode", null);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String AddLeadComments() {
        try {

            if (txtLeadCode.getValue() == null) {
                GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  add its comment"));
                return null;
            }
            session.setAttribute("lcmntCode", null);
            leadComment.setValue(null);
            leadCommentDate.setValue(null);
            leadCommentDisposition.setValue(null);

            session.setAttribute("action", "A");

            //Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "crm:p3" +
                                 "').show(hints);");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditLeadComment() {
        try {

            if (txtLeadCode.getValue() == null) {
                GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  edit its activity"));
                return null;
            }
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadCommentsIterator");
            RowKeySet set = leadCommentsTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("lcmntCode",
                                     r.getAttribute("leadCommentCode"));
                leadComment.setValue(r.getAttribute("leadComment"));
                leadCommentDate.setValue(r.getAttribute("leadCommentDate"));
                leadCommentDisposition.setValue(r.getAttribute("leadCommentDisposition"));

                session.setAttribute("action", "E");

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:p3" + "').show(hints);");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteLeadComment() {
        try {
            if (txtLeadCode.getValue() == null) {
                GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  delete its comment"));
                return null;
            }
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadCommentsIterator");
            RowKeySet set = leadCommentsTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("lcmntCode",
                                     r.getAttribute("leadCommentCode"));
                leadComment.setValue(r.getAttribute("leadComment"));
                leadCommentDate.setValue(r.getAttribute("leadCommentDate"));
                leadCommentDisposition.setValue(r.getAttribute("leadCommentDisposition"));

                session.setAttribute("action", "D");

                SaveLeadComment();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveLeadComment() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.save_lead_comment(?,?,?,?,?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setObject(2, txtLeadCode.getValue());
            callStmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("lcmntCode"));
            if (leadComment.getValue() == null) {
                callStmt.setString(4, null);
            } else {
                callStmt.setString(4, leadComment.getValue().toString());
            }

            callStmt.setDate(5, GlobalCC.extractDate(leadCommentDate));

            callStmt.setBigDecimal(6,(BigDecimal)session.getAttribute("UserCode"));
            callStmt.setObject(7, leadCommentDisposition.getValue());
            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findLeadCommentsIterator").executeQuery();
            GlobalCC.refreshUI(leadCommentsTable);
            GlobalCC.dismissPopUp("crm", "p3");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String AddLeadActivities() {
        try {

            if (txtLeadCode.getValue() == null) {
                GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  add its activity"));
                return null;
            }
            session.setAttribute("lactsCode", null);

            session.setAttribute("action", "A");

            //Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:popup1" + "').show(hints);");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteLeadActivity() {

        if (txtLeadCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  delete its activity"));
            return null;
        }
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.save_lead_activities(?,?,?,?); end;";

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadActivitiesIterator");
            RowKeySet set = leadActivitiesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.setString(1, "D");
                callStmt.setBigDecimal(2,
                                       (BigDecimal)r.getAttribute("leadActivityCode"));
                callStmt.setBigDecimal(3, null);
                callStmt.setObject(4, txtLeadCode.getValue());

                callStmt.execute();
                callStmt.close();

            }
            conn.close();
            ADFUtils.findIterator("findLeadActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(leadActivitiesTable);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String SaveLeadActivity() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.save_lead_activities(?,?,?,?); end;";

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findActivitiesIterator");
            RowKeySet set = activitiesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.setString(1, "A");
                callStmt.setBigDecimal(2,
                                       (BigDecimal)session.getAttribute("lactsCode"));
                callStmt.setObject(3, r.getAttribute("activityCode"));
                callStmt.setObject(4, txtLeadCode.getValue());

                callStmt.execute();
                callStmt.close();

            }


            ADFUtils.findIterator("findLeadActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(leadActivitiesTable);
            GlobalCC.dismissPopUp("crm", "popup1");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void setDescription(RichInputText description) {
        this.description = description;
    }

    public RichInputText getDescription() {
        return description;
    }

    public void setCoTel(RichInputText coTel) {
        this.coTel = coTel;
    }

    public RichInputText getCoTel() {
        return coTel;
    }

    public void setTitle(RichInputText title) {
        this.title = title;
    }

    public RichInputText getTitle() {
        return title;
    }

    public void setSurname(RichInputText surname) {
        this.surname = surname;
    }

    public RichInputText getSurname() {
        return surname;
    }

    public void setOtherNames(RichInputText otherNames) {
        this.otherNames = otherNames;
    }

    public RichInputText getOtherNames() {
        return otherNames;
    }

    public void setMobileNo(RichInputText mobileNo) {
        this.mobileNo = mobileNo;
    }

    public RichInputText getMobileNo() {
        return mobileNo;
    }

    public void setFax(RichInputText fax) {
        this.fax = fax;
    }

    public RichInputText getFax() {
        return fax;
    }

    public void setEmail(RichInputText email) {
        this.email = email;
    }

    public RichInputText getEmail() {
        return email;
    }

    public void setPostalAddress(RichInputText postalAddress) {
        this.postalAddress = postalAddress;
    }

    public RichInputText getPostalAddress() {
        return postalAddress;
    }

   
    public void setPhysicalAddress(RichInputText physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public RichInputText getPhysicalAddress() {
        return physicalAddress;
    }

    public void setSource(RichInputText source) {
        this.source = source;
    }

    public RichInputText getSource() {
        return source;
    }

    public void setStatus(RichInputText status) {
        this.status = status;
    }

    public RichInputText getStatus() {
        return status;
    }

    public void setLeadDate(RichInputDate leadDate) {
        this.leadDate = leadDate;
    }

    public RichInputDate getLeadDate() {
        return leadDate;
    }

    public void setConverted(RichSelectOneChoice converted) {
        this.converted = converted;
    }

    public RichSelectOneChoice getConverted() {
        return converted;
    }

    public void setPortName(RichInputText portName) {
        this.portName = portName;
    }

    public RichInputText getPortName() {
        return portName;
    }

    public void setPortContr(RichSelectOneChoice portContr) {
        this.portContr = portContr;
    }

    public RichSelectOneChoice getPortContr() {
        return portContr;
    }

    public void setPortAmount(RichInputNumberSpinbox portAmount) {
        this.portAmount = portAmount;
    }

    public RichInputNumberSpinbox getPortAmount() {
        return portAmount;
    }

    public void setPortSale(RichInputText portSale) {
        this.portSale = portSale;
    }

    public RichInputText getPortSale() {
        return portSale;
    }

    public void setPortCloseDate(RichInputDate portCloseDate) {
        this.portCloseDate = portCloseDate;
    }

    public RichInputDate getPortCloseDate() {
        return portCloseDate;
    }

    public void setAnnRevenue(RichInputNumberSpinbox annRevenue) {
        this.annRevenue = annRevenue;
    }

    public RichInputNumberSpinbox getAnnRevenue() {
        return annRevenue;
    }

    public void setWebsite(RichInputText website) {
        this.website = website;
    }

    public RichInputText getWebsite() {
        return website;
    }

    public void setLeadsTree(RichTree leadsTree) {
        this.leadsTree = leadsTree;
    }

    public RichTree getLeadsTree() {
        return leadsTree;
    }

   

    public void setCoName(RichInputText coName) {
        this.coName = coName;
    }

    public RichInputText getCoName() {
        return coName;
    }

    public void setLeadComment(RichInputText leadComment) {
        this.leadComment = leadComment;
    }

    public RichInputText getLeadComment() {
        return leadComment;
    }

    public void setLeadCommentDate(RichInputDate leadCommentDate) {
        this.leadCommentDate = leadCommentDate;
    }

    public RichInputDate getLeadCommentDate() {
        return leadCommentDate;
    }

    public void setLeadCommentsTable(RichTable leadCommentsTable) {
        this.leadCommentsTable = leadCommentsTable;
    }

    public RichTable getLeadCommentsTable() {
        return leadCommentsTable;
    }

    public void setLeadActivitiesTable(RichTable leadActivitiesTable) {
        this.leadActivitiesTable = leadActivitiesTable;
    }

    public RichTable getLeadActivitiesTable() {
        return leadActivitiesTable;
    }

    public void setActivitiesTable(RichTable activitiesTable) {
        this.activitiesTable = activitiesTable;
    }

    public RichTable getActivitiesTable() {
        return activitiesTable;
    }

    public void setSourcesTable(RichTable sourcesTable) {
        this.sourcesTable = sourcesTable;
    }

    public RichTable getSourcesTable() {
        return sourcesTable;
    }

    public void setStatusTable(RichTable statusTable) {
        this.statusTable = statusTable;
    }

    public RichTable getStatusTable() {
        return statusTable;
    }

    public void setTblCountryLOv(RichTable tblCountryLOv) {
        this.tblCountryLOv = tblCountryLOv;
    }

    public RichTable getTblCountryLOv() {
        return tblCountryLOv;
    }

    public void setTblStatesLOV(RichTable tblStatesLOV) {
        this.tblStatesLOV = tblStatesLOV;
    }

    public RichTable getTblStatesLOV() {
        return tblStatesLOV;
    }

    public String actionAcceptCountry() {
        Object key = tblCountryLOv.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            if (btnCountry.getShortDesc() != null) {
                txtSearchValue.setValue(nodeBinding.getAttribute("name"));
                String countryCode =
                    nodeBinding.getAttribute("code").toString();
                String searchCriteria = "WHERE LDS_COU_CODE=" + countryCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                //session.setAttribute("SEARCHCRITERIA", null);
                GlobalCC.dismissPopUp("crm", "CountriesPop");
                btnCountry.setShortDesc(null);
                //return null;

            }

            txtCountryCode.setValue(nodeBinding.getAttribute("code"));
            txtCountryName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtCountryName);

            txtStateCode.setValue(null);
            txtTownCode.setValue(null);
            txtStateName.setValue(null);
            txtTownName.setValue(null);
            GlobalCC.refreshUI(txtStateName);
            GlobalCC.refreshUI(txtTownName);


        }
        GlobalCC.dismissPopUp("crm", "CountriesPop");

        return null;
    }

    public String actionAcceptState() {


        Object key = tblStatesLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtStateCode.setValue(nodeBinding.getAttribute("stateCode"));
            txtStateName.setValue(nodeBinding.getAttribute("stateName"));
            GlobalCC.refreshUI(txtStateName);

        }
        GlobalCC.dismissPopUp("crm", "StatesPop");
        return null;
    }

    public String actionAcceptTown() {
        if (txtCountryCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(null,
                                        new Exception("Select a Country first"));
            return null;
        }
        Object key = tblTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtTownCode.setValue(nodeBinding.getAttribute("code"));
            txtTownName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtTownName);
        }
        GlobalCC.dismissPopUp("crm", "TownsPop");
        return null;
    }

    public void setTblUsersLOv(RichTable tblUsersLOv) {
        this.tblUsersLOv = tblUsersLOv;
    }

    public RichTable getTblUsersLOv() {
        return tblUsersLOv;
    }

    public void setTblTeamsLOV(RichTable tblTeamsLOV) {
        this.tblTeamsLOV = tblTeamsLOV;
    }

    public RichTable getTblTeamsLOV() {
        return tblTeamsLOV;
    }

    public void setTblAccountsLOV(RichTable tblAccountsLOV) {
        this.tblAccountsLOV = tblAccountsLOV;
    }

    public RichTable getTblAccountsLOV() {
        return tblAccountsLOV;
    }

    public String actionAcceptAccount() {
        Object key = tblAccountsLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {

            if (btnAccounts.getShortDesc() != null)

            {
                txtSearchValue.setValue(nodeBinding.getAttribute("accountName"));
                String accountCode =
                    nodeBinding.getAttribute("accountCode").toString();
                String searchCriteria = "WHERE LDS_ACC_CODE=" + accountCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                //session.setAttribute("SEARCHCRITERIA", null);
                GlobalCC.dismissPopUp("crm", "AccountsPop");
                btnAccounts.setShortDesc(null);
                //return null;
            }
            txtAccountCode.setValue(nodeBinding.getAttribute("accountCode"));
            txtAccountName.setValue(nodeBinding.getAttribute("accountName"));
            GlobalCC.refreshUI(txtAccountName);
        }
        GlobalCC.dismissPopUp("crm", "AccountsPop");
        return null;
    }

    public String actionAcceptTeam() {
        Object key = tblTeamsLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {

            if (btnTeams.getShortDesc() != null) {
                txtSearchValue.setValue(nodeBinding.getAttribute("username"));
                String teamCode =
                    nodeBinding.getAttribute("userCode").toString();
                String searchCriteria = "WHERE LDS_TEAM_USR_CODE=" + teamCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                // session.setAttribute("SEARCHCRITERIA", null);
                GlobalCC.dismissPopUp("crm", "TeamsPop");
                btnTeams.setShortDesc(null);
                //return null;
            }
            txtTeamCode.setValue(nodeBinding.getAttribute("userCode"));
            txtTeamName.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(txtTeamName);
        }
        GlobalCC.dismissPopUp("crm", "TeamsPop");
        return null;
    }

    public String actionAcceptUser() {
        Object key = tblUsersLOv.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            if (btnUsers.getShortDesc() != null)

            {
                txtSearchValue.setValue(nodeBinding.getAttribute("username"));
                String userCode =
                    nodeBinding.getAttribute("userCode").toString();
                String searchCriteria = "WHERE LDS_USR_CODE=" + userCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                // session.setAttribute("SEARCHCRITERIA", null);
                GlobalCC.dismissPopUp("crm", "UsersPop");
                btnUsers.setShortDesc(null);
                //return null;
            }
            txtUserCode.setValue(nodeBinding.getAttribute("userCode"));
            txtUserName.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(txtUserName);
        }
        GlobalCC.dismissPopUp("crm", "UsersPop");
        return null;
    }

    public String actionAcceptOrg() {
        Object key = tblOrgsLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtOrgCode.setValue(nodeBinding.getAttribute("orgCode"));
            txtOrgName.setValue(nodeBinding.getAttribute("orgName"));
            GlobalCC.refreshUI(txtOrgName);
            GlobalCC.refreshUI(txtOrgCode);

        }
        GlobalCC.dismissPopUp("crm", "OrganisationsPop");

        return null;
    }

    public void setTxtCountryCode(RichInputText txtCountryCode) {
        this.txtCountryCode = txtCountryCode;
    }

    public RichInputText getTxtCountryCode() {
        return txtCountryCode;
    }

    public void setTxtStateCode(RichInputText txtStateCode) {
        this.txtStateCode = txtStateCode;
    }

    public RichInputText getTxtStateCode() {
        return txtStateCode;
    }

    public void setTxtTownCode(RichInputText txtTownCode) {
        this.txtTownCode = txtTownCode;
    }

    public RichInputText getTxtTownCode() {
        return txtTownCode;
    }

    public void setTxtUserCode(RichInputText txtUserCode) {
        this.txtUserCode = txtUserCode;
    }

    public RichInputText getTxtUserCode() {
        return txtUserCode;
    }

    public void setTxtTeamCode(RichInputText txtTeamCode) {
        this.txtTeamCode = txtTeamCode;
    }

    public RichInputText getTxtTeamCode() {
        return txtTeamCode;
    }

    public void setTxtAccountCode(RichInputText txtAccountCode) {
        this.txtAccountCode = txtAccountCode;
    }

    public RichInputText getTxtAccountCode() {
        return txtAccountCode;
    }


    public void setTxtCountryName(RichInputText txtCountryName) {
        this.txtCountryName = txtCountryName;
    }

    public RichInputText getTxtCountryName() {
        return txtCountryName;
    }

    public void setTxtStateName(RichInputText txtStateName) {
        this.txtStateName = txtStateName;
    }

    public RichInputText getTxtStateName() {
        return txtStateName;
    }

    public void setTxtTownName(RichInputText txtTownName) {
        this.txtTownName = txtTownName;
    }

    public RichInputText getTxtTownName() {
        return txtTownName;
    }

    public void setTxtOrgName(RichInputText txtOrgName) {
        this.txtOrgName = txtOrgName;
    }

    public RichInputText getTxtOrgName() {
        return txtOrgName;
    }

    public void setTxtUserName(RichInputText txtUserName) {
        this.txtUserName = txtUserName;
    }

    public RichInputText getTxtUserName() {
        return txtUserName;
    }

    public void setTxtTeamName(RichInputText txtTeamName) {
        this.txtTeamName = txtTeamName;
    }

    public RichInputText getTxtTeamName() {
        return txtTeamName;
    }

    public void setTxtAccountName(RichInputText txtAccountName) {
        this.txtAccountName = txtAccountName;
    }

    public RichInputText getTxtAccountName() {
        return txtAccountName;
    }

    public void setTblOrgsLOV(RichTable tblOrgsLOV) {
        this.tblOrgsLOV = tblOrgsLOV;
    }

    public RichTable getTblOrgsLOV() {
        return tblOrgsLOV;
    }

    public void setTxtOrgCode(RichInputText txtOrgCode) {
        this.txtOrgCode = txtOrgCode;
    }

    public RichInputText getTxtOrgCode() {
        return txtOrgCode;
    }

    public void setTblTowns(RichTable tblTowns) {
        this.tblTowns = tblTowns;
    }

    public RichTable getTblTowns() {
        return tblTowns;
    }

    public String showCountries() {
        txtStateCode.setValue(null);
        txtTownCode.setValue(null);
        txtStateName.setValue(null);
        txtTownName.setValue(null);
        GlobalCC.refreshUI(txtStateName);

        GlobalCC.showPopUp("crm", "CountriesPop");
        return null;
    }

    public String showStates() {
        if (txtCountryCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(null,
                                        new Exception("Select a Country first"));
            return null;
        }
        session.setAttribute("countryCode", txtCountryCode.getValue());
        ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
        GlobalCC.showPopUp("crm", "StatesPop");
        return null;
    }

    public String showTowns() {
        if (txtCountryCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(null,
                                        new Exception("Select a Country first"));
            return null;
        }
        session.setAttribute("countryCode", txtCountryCode.getValue());
        GlobalCC.showPopUp("crm", "TownsPop");
        return null;
    }

    public String showOrgs() {

        ADFUtils.findIterator("fetchTownsByCountryIterator").executeQuery();
        GlobalCC.refreshUI(tblTowns);
        GlobalCC.showPopUp("crm", "OrganisationsPop");
        return null;
    }

    public String showUsers() {
        GlobalCC.showPopUp("crm", "UsersPop");
        return null;
    }

    public String showTeams() {
        GlobalCC.showPopUp("crm", "TeamsPop");
        return null;
    }

    public String showAccounts() {

        GlobalCC.showPopUp("crm", "AccountsPop");
        return null;
    }

    public String AddLeadSource() {
        try {
            session.setAttribute("leadSourceCode", null);
            desc.setValue(null);

            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "leadSourcePOP");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String AddLeadStatus() {
        try {
            session.setAttribute("leadStatusCode", null);
            desc.setValue(null);

            session.setAttribute("action", "A");
            GlobalCC.showPopUp("crm", "leadStatusPOP");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditLeadStatuses() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadStatusesIterator");
            RowKeySet set = leadStatusesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("leadStatusCode",
                                     r.getAttribute("leadStatusCode"));
                desc.setValue(r.getAttribute("leadStatusDesc"));

                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "leadStatusPOP");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditLeadSources() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadSourcesIterator");
            RowKeySet set = leadSources.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("leadSourceCode",
                                     r.getAttribute("leadSourceCode"));
                desc.setValue(r.getAttribute("leadSourceDesc"));

                session.setAttribute("action", "E");
                GlobalCC.showPopUp("crm", "leadSourcePOP");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteLeadStatuses() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadStatusesIterator");
            RowKeySet set = leadStatusesTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("leadStatusCode",
                                     r.getAttribute("leadStatusCode"));
                desc.setValue(r.getAttribute("leadStatusDesc"));
                session.setAttribute("action", "D");

                SaveLeadStatuses();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteLeadSources() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findLeadSourcesIterator");
            RowKeySet set = leadSources.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                session.setAttribute("leadSourceCode",
                                     r.getAttribute("leadSourceCode"));
                desc.setValue(r.getAttribute("leadSourceDesc"));
                session.setAttribute("action", "D");

                SaveLeadSources();

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String SaveLeadSources() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.save_lead_sources(?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("leadSourceCode"));
            if (desc.getValue() == null) {
                callStmt.setString(3, null);
            } else {
                callStmt.setString(3, desc.getValue().toString());
            }
            callStmt.execute();
            callStmt.close();

            ADFUtils.findIterator("findLeadSourcesIterator").executeQuery();
            GlobalCC.refreshUI(leadSources);
            GlobalCC.dismissPopUp("crm", "leadSourcePOP");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String SaveLeadStatuses() {
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.save_lead_statuses(?,?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt =
                    (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            callStmt.setString(1, (String)session.getAttribute("action"));
            callStmt.setBigDecimal(2,
                                   (BigDecimal)session.getAttribute("leadStatusCode"));
            if (desc.getValue() == null) {
                callStmt.setString(3, null);
            } else {
                callStmt.setString(3, desc.getValue().toString());
            }
            callStmt.execute();
            callStmt.close();
            conn.close();
            ADFUtils.findIterator("findLeadStatusesIterator").executeQuery();
            GlobalCC.refreshUI(leadStatusesTable);
            GlobalCC.dismissPopUp("crm", "leadStatusPOP");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setDesc(RichInputText desc) {
        this.desc = desc;
    }

    public RichInputText getDesc() {
        return desc;
    }

    public void setLeadStatusesTable(RichTable leadStatusesTable) {
        this.leadStatusesTable = leadStatusesTable;
    }

    public RichTable getLeadStatusesTable() {
        return leadStatusesTable;
    }

    public void setLeadSources(RichTable leadSources) {
        this.leadSources = leadSources;
    }

    public RichTable getLeadSources() {
        return leadSources;
    }


    public String actionDeleteLead() {
        if (txtLeadCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a Lead to  delete"));
            return null;
        }
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.delete_lead(?); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setObject(1, txtLeadCode.getValue());


            callStmt.execute();

            callStmt.close();
            conn.close();

            GlobalCC.sysInformation("Lead  Successfully Deleted.");

            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
            clearLeadDetails();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTblLeads(RichTable tblLeads) {
        this.tblLeads = tblLeads;
    }

    public RichTable getTblLeads() {
        return tblLeads;
    }

    public void actionTblLeadsSelected(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Object key = tblLeads.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            description.setValue(nodeBinding.getAttribute("LDS_DESC"));
            coName.setValue(nodeBinding.getAttribute("companyName"));
            coTel.setValue(nodeBinding.getAttribute("LDS_CAMP_TEL"));
            title.setValue(nodeBinding.getAttribute("LDS_TITLE"));
            surname.setValue(nodeBinding.getAttribute("LDS_SURNNAME"));
            otherNames.setValue(nodeBinding.getAttribute("LDS_OTHERNAMES"));
            mobileNo.setValue(nodeBinding.getAttribute("LDS_MOB_TEL"));
            fax.setValue(nodeBinding.getAttribute("LDS_FAX"));
            email.setValue(nodeBinding.getAttribute("LDS_EMAIL_ADDRS"));
            postalAddress.setValue(nodeBinding.getAttribute("LDS_POSTAL_ADDRS"));
            postalCode.setValue(nodeBinding.getAttribute("LDS_POSTAL_CODE"));
            physicalAddress.setValue(nodeBinding.getAttribute("LDS_PHYSICAL_ADDRS"));
            source.setValue(nodeBinding.getAttribute("LDSRC_DESC"));
            status.setValue(nodeBinding.getAttribute("LDS_LSTS_DESC"));
            leadDate.setValue(nodeBinding.getAttribute("LDS_DATE"));
            converted.setValue(nodeBinding.getAttribute("LDS_CONVERTED"));
            portName.setValue(nodeBinding.getAttribute("LDS_PONT_NAME"));
            portContr.setValue(nodeBinding.getAttribute("LDS_PONT_CONRT"));
            portAmount.setValue(nodeBinding.getAttribute("LDS_PONT_AMOUNT"));
            portSale.setValue(nodeBinding.getAttribute("LDS_PONT_SALE_STAGE"));
            portCloseDate.setValue(nodeBinding.getAttribute("LDS_PONT_CLOSE_DATE"));
            annRevenue.setValue(nodeBinding.getAttribute("LDS_ANN_REVENUE"));
            website.setValue(nodeBinding.getAttribute("LDS_WEB_SITE"));
            industry.setValue(nodeBinding.getAttribute("LDS_INDUSTRY"));

            txtCountryCode.setValue(nodeBinding.getAttribute("LDS_COU_CODE"));
            txtStateCode.setValue(nodeBinding.getAttribute("LDS_STATE_CODE"));
            txtTownCode.setValue(nodeBinding.getAttribute("LDS_TWN_CODE"));
            txtUserCode.setValue(nodeBinding.getAttribute("LDS_USR_CODE"));
            txtTeamCode.setValue(nodeBinding.getAttribute("LDS_TEAM_USR_CODE"));
            txtAccountCode.setValue(nodeBinding.getAttribute("LDS_ACC_CODE"));
            txtCountryName.setValue(nodeBinding.getAttribute("LDS_COUNTRY"));
            txtStateName.setValue(nodeBinding.getAttribute("LDS_STATE"));
            txtTownName.setValue(nodeBinding.getAttribute("LDS_TOWN_NAME"));
            txtOrgName.setValue(nodeBinding.getAttribute("LDS_ORG_NAME"));
            txtUserName.setValue(nodeBinding.getAttribute("LDS_USR_NAME"));
            txtTeamName.setValue(nodeBinding.getAttribute("LDS_TEAM_NAME"));
            txtAccountName.setValue(nodeBinding.getAttribute("LDS_ACCOUNT_NAME"));
            txtOrgCode.setValue(nodeBinding.getAttribute("LDS_ORG_CODE"));
            txtLeadCode.setValue(nodeBinding.getAttribute("LDS_CODE"));
            txtCampaignCode.setValue(nodeBinding.getAttribute("LDS_CMP_CODE"));
            txtProductCode.setValue(nodeBinding.getAttribute("leadProdCode"));
            txtCurrencyCode.setValue(nodeBinding.getAttribute("LDS_CUR_CODE"));
            txtSystemCode.setValue(nodeBinding.getAttribute("LDS_SYS_CODE"));
            txtCampaignName.setValue(nodeBinding.getAttribute("LDS_CAMP_NAME"));
            txtProductName.setValue(nodeBinding.getAttribute("leadProdName"));
            txtCurrencyName.setValue(nodeBinding.getAttribute("leadCurName"));
            txtSystemName.setValue(nodeBinding.getAttribute("leadSysName"));
            txtLeadSrcCode.setValue(nodeBinding.getAttribute("LDSRC_CODE"));
            txtLeadStatusCode.setValue(nodeBinding.getAttribute("LDS_LSTS_CODE"));
            txtOrgDivCode.setValue(nodeBinding.getAttribute("leadDivCode"));
            txtOrgDivName.setValue(nodeBinding.getAttribute("leadDivName"));
            txtOccupation.setValue(nodeBinding.getAttribute("occupation"));
            txtClntShtDesc.setValue(nodeBinding.getAttribute("clientShtDesc"));
            session.setAttribute("Lead_code", txtLeadCode.getValue());
            ADFUtils.findIterator("findLeadActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(leadActivitiesTable);
            ADFUtils.findIterator("findLeadCommentsIterator").executeQuery();
            GlobalCC.refreshUI(leadCommentsTable);
            GlobalCC.refreshUI(pnBoxLeadDetails);
            GlobalCC.refreshUI(txtOccupation);


        }
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public String actionAcceptSystem() {
        Object key = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            if (btnSystems.getShortDesc() != null) {
                txtSearchValue.setValue(nodeBinding.getAttribute("name"));
                String sysCode = nodeBinding.getAttribute("code").toString();
                String searchCriteria = "WHERE LDS_SYS_CODE=" + sysCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                // session.setAttribute("SEARCHCRITERIA", null);

                btnSystems.setShortDesc(null);

            }
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            txtSystemCode.setValue(nodeBinding.getAttribute("code"));
            txtSystemName.setValue(nodeBinding.getAttribute("name"));
            txtProductName.setValue(null);
            txtProductCode.setValue(null);
            GlobalCC.refreshUI(txtSystemName);
            GlobalCC.refreshUI(txtProductName);

        }
        GlobalCC.dismissPopUp("crm", "SystemsPop");
        return null;
    }

    public void setTxtSystemCode(RichInputText txtSystemCode) {
        this.txtSystemCode = txtSystemCode;
    }

    public RichInputText getTxtSystemCode() {
        return txtSystemCode;
    }

    public void setTxtProductCode(RichInputText txtProductCode) {
        this.txtProductCode = txtProductCode;
    }

    public RichInputText getTxtProductCode() {
        return txtProductCode;
    }

    public void setTxtCampaignCode(RichInputText txtCampaignCode) {
        this.txtCampaignCode = txtCampaignCode;
    }

    public RichInputText getTxtCampaignCode() {
        return txtCampaignCode;
    }

    public void setTxtCurrencyCode(RichInputText txtCurrencyCode) {
        this.txtCurrencyCode = txtCurrencyCode;
    }

    public RichInputText getTxtCurrencyCode() {
        return txtCurrencyCode;
    }

    public String showSystems() {
        GlobalCC.showPopUp("crm", "SystemsPop");
        return null;
    }

    public String showProducts() {
        if (txtSystemCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("You need to select a system first"));
            return null;
        }
        session.setAttribute("sysCode", txtSystemCode.getValue());
        ADFUtils.findIterator("findCampaignProductsIterator").executeQuery();
        GlobalCC.showPopUp("crm", "CampaignProductsPop");
        return null;
    }

    public String showCampaigns() {
        GlobalCC.showPopUp("crm", "CampaignsPOp");
        return null;
    }

    public String showCurrecies() {
        GlobalCC.showPopUp("crm", "CurrenciesPop");
        return null;
    }

    public String actionAcceptCampaign() {
        Object key = tblCampaigns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            if (btnCampaign.getShortDesc() != null)

            {
                txtSearchValue.setValue(nodeBinding.getAttribute("campName"));
                String campaignCode =
                    nodeBinding.getAttribute("campCode").toString();
                String searchCriteria = "WHERE LDS_CAMP_CODE=" + campaignCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                // session.setAttribute("SEARCHCRITERIA", null);
                // GlobalCC.dismissPopUp("crm", "CampaignsPOp");
                btnCampaign.setShortDesc(null);
                //  return null;
            }

            txtCampaignCode.setValue(nodeBinding.getAttribute("campCode"));
            txtCampaignName.setValue(nodeBinding.getAttribute("campName"));


            GlobalCC.refreshUI(txtCampaignName);
            GlobalCC.dismissPopUp("crm", "CampaignsPOp");
            //return null;
        }
        return null;
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
            if (btnProducts.getShortDesc() != null) {
                if (session.getAttribute("sysCode") == null) {
                    GlobalCC.EXCEPTIONREPORTING(new Exception("Please Select a system first"));
                    return null;
                }
                txtSearchValue.setValue(nodeBinding.getAttribute("prodDesc"));
                String productCode =
                    nodeBinding.getAttribute("productCode").toString();
                String searchCriteria =
                    "WHERE LDS_SYS_CODE =" + session.getAttribute("sysCode") +
                    " AND LDS_PROD_CODE=" + productCode;
                session.setAttribute("SEARCHCRITERIA", searchCriteria);
                ADFUtils.findIterator("findLeadsIterator").executeQuery();
                txtSearchValue.setDisabled(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(tblLeads);
                //session.setAttribute("SEARCHCRITERIA", null);
                //session.setAttribute("sysCode", null);
                GlobalCC.dismissPopUp("crm", "CampaignProductsPop");
            }
            txtProductCode.setValue(nodeBinding.getAttribute("productCode"));
            txtProductName.setValue(nodeBinding.getAttribute("prodDesc"));
            GlobalCC.refreshUI(txtProductName);
            GlobalCC.dismissPopUp("crm", "ProductSystemsPOP");
            GlobalCC.dismissPopUp("crm", "CampaignProductsPop");
            btnProducts.setShortDesc(null);
            // return null;
        }
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
            txtCurrencyCode.setValue(nodeBinding.getAttribute("code"));
            txtCurrencyName.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtCurrencyName);
        }
        GlobalCC.dismissPopUp("crm", "CurrenciesPop");
        return null;
    }

    public void setTxtLeadCode(RichInputText txtLeadCode) {
        this.txtLeadCode = txtLeadCode;
    }

    public RichInputText getTxtLeadCode() {
        return txtLeadCode;
    }

    public void setTxtSystemName(RichInputText txtSystemName) {
        this.txtSystemName = txtSystemName;
    }

    public RichInputText getTxtSystemName() {
        return txtSystemName;
    }

    public void setTxtProductName(RichInputText txtProductName) {
        this.txtProductName = txtProductName;
    }

    public RichInputText getTxtProductName() {
        return txtProductName;
    }

    public void setTxtCampaignName(RichInputText txtCampaignName) {
        this.txtCampaignName = txtCampaignName;
    }

    public RichInputText getTxtCampaignName() {
        return txtCampaignName;
    }

    public void setTxtCurrencyName(RichInputText txtCurrencyName) {
        this.txtCurrencyName = txtCurrencyName;
    }

    public RichInputText getTxtCurrencyName() {
        return txtCurrencyName;
    }

    public void setDlgNewEditLead(RichDialog dlgNewEditLead) {
        this.dlgNewEditLead = dlgNewEditLead;
    }

    public RichDialog getDlgNewEditLead() {
        return dlgNewEditLead;
    }

    public String actionSaveLead() {
        String Occupation;
        String companyName;
        
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            /*if (GlobalCC.checkNullValues(surname.getValue()) == null) {
                GlobalCC.errorValueNotEntered("Enter Surname!");
                return null;
            }*/
            if (txtOccupation.getValue() != null) {
                Occupation = txtOccupation.getValue().toString();
            } else {
                Occupation = null;
            }
            String szDesc=GlobalCC.checkNullValues(description.getValue());
            String szDescRequired=GlobalCC.checkNullValues(session.getAttribute("FX_LEAD_DESC.required")); 
            
            if (szDesc == null && szDescRequired=="Y") {
                GlobalCC.errorValueNotEntered("Enter A Description");
                return null;
            }

            if (coName.getValue() != null) {
                companyName = coName.getValue().toString();

            } else {
                companyName = null;
            }
            
            if (leadDate.getValue() == null) {
                GlobalCC.errorValueNotEntered("Enter A Lead Date");
                return null;
            }


            String query =
                "begin TQC_SETUPS_PKG.save_lead_details(" +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," + 
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?" +
                "); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setObject(1, txtLeadCode.getValue());
            callStmt.setString(2, szDesc);

            callStmt.setObject(3, txtCampaignCode.getValue());

            callStmt.setObject(4, coTel.getValue());
            callStmt.setObject(5, title.getValue());
            callStmt.setObject(6, surname.getValue());
            callStmt.setObject(7, otherNames.getValue());

            if (mobileNo.getValue() == null) {
                callStmt.setString(8, null);
            } else {
                callStmt.setString(8, mobileNo.getValue().toString());
            }
            if (fax.getValue() == null) {
                callStmt.setString(9, null);
            } else {
                callStmt.setString(9, fax.getValue().toString());
            }
            if (email.getValue() == null) {
                callStmt.setString(10, null);
            } else {
                callStmt.setString(10, email.getValue().toString());
            }
            if (postalAddress.getValue() == null) {
                callStmt.setString(11, null);
            } else {
                callStmt.setString(11, postalAddress.getValue().toString());
            }
            if (postalCode.getValue() == null) {
                callStmt.setString(12, null);
            } else {
                callStmt.setString(12, postalCode.getValue().toString());
            }
            if (physicalAddress.getValue() == null) {
                callStmt.setString(13, null);
            } else {
                callStmt.setString(13, physicalAddress.getValue().toString());
            }

            callStmt.setObject(14, txtLeadSrcCode.getValue());
            callStmt.setObject(15, txtLeadStatusCode.getValue());
            if (leadDate.getValue() == null) {
                callStmt.setString(16, null);
            } else {
                if (leadDate.getValue().toString().contains(":")) {
                    callStmt.setString(16,
                                       GlobalCC.parseDate(leadDate.getValue().toString()));
                } else {
                    callStmt.setString(16,
                                       GlobalCC.upDateParseDate(leadDate.getValue().toString()));
                }
            }
            if (converted.getValue() == null) {
                callStmt.setString(17, null);
            } else {
                callStmt.setString(17, converted.getValue().toString());
            }
            if (portName.getValue() == null) {
                callStmt.setString(18, null);
            } else {
                callStmt.setString(18, portName.getValue().toString());
            }
            if (portContr.getValue() == null) {
                callStmt.setString(19, null);
            } else {
                callStmt.setString(19, portContr.getValue().toString());
            }
            if (portAmount.getValue() == null) {
                callStmt.setString(20, null);
            } else {
                callStmt.setString(20, portAmount.getValue().toString());
            }
            if (portSale.getValue() == null) {
                callStmt.setString(21, null);
            } else {
                callStmt.setString(21, portSale.getValue().toString());
            }
            if (portCloseDate.getValue() == null) {
                callStmt.setString(22, null);
            } else {
                if (portCloseDate.getValue().toString().contains(":")) {
                    callStmt.setString(22,
                                       GlobalCC.parseDate(portCloseDate.getValue().toString()));
                } else {
                    callStmt.setString(22,
                                       GlobalCC.upDateParseDate(portCloseDate.getValue().toString()));
                }
            }
            if (annRevenue.getValue() == null) {
                callStmt.setString(23, null);
            } else {
                callStmt.setString(23, annRevenue.getValue().toString());
            }
            if (website.getValue() == null) {
                callStmt.setString(24, null);
            } else {
                callStmt.setString(24, website.getValue().toString());
            }
            if (industry.getValue() == null) {
                callStmt.setString(25, null);
            } else {
                callStmt.setString(25, getSector(industry.getValue().toString()));
            }

            callStmt.setObject(26, txtCountryCode.getValue());
            callStmt.setObject(27, txtStateCode.getValue());
            callStmt.setObject(28, txtTownCode.getValue());
            callStmt.setObject(29, txtOrgCode.getValue());
            callStmt.setObject(30, txtUserCode.getValue());
            callStmt.setObject(31, txtTeamCode.getValue());
            callStmt.setObject(32, txtAccountCode.getValue());
            callStmt.setObject(33, txtProductCode.getValue());
            callStmt.setObject(34, txtCurrencyCode.getValue());
            callStmt.setObject(35, txtSystemCode.getValue());
            callStmt.setObject(36, txtOrgDivCode.getValue());
            callStmt.setString(37, Occupation);
            callStmt.setObject(38, companyName);
            if (GlobalCC.checkNullValues(type.getValue()) == null) {
                callStmt.setString(39, null);
            } else {
                callStmt.setString(39, getClientType(GlobalCC.checkNullValues(type.getValue())));
            }
            callStmt.setString(40, GlobalCC.checkNullValues(session.getAttribute("Username")));
            callStmt.execute();

            callStmt.close();
            conn.close();
            GlobalCC.dismissPopUp("crm", "newEditLeadPop");

            GlobalCC.sysInformation("Lead Details Successfully Saved.");
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;

    }
    private String getPostalCode(String val) {
            try {
                    DCIteratorBinding listIter;
                    listIter = ADFUtils.findIterator("fetchPostalCodesByTownIterator");
                    String name = null;
                    Row datRow;
                    if (val == null) {

                    }else{
                            int curIndex = Integer.valueOf(val);
                            datRow = listIter.getRowAtRangeIndex(curIndex);
                            name = (String)datRow.getAttribute("pstZipCode");
                    }

                    System.out.println("The name we got is == " + name);

                    return name;

                    } catch(Exception e) {
                            return null;
            }

    }
    private String getSector(String val) {
            try {
                    DCIteratorBinding listIter;
                    listIter = ADFUtils.findIterator("fetchAllSectorsIterator");
                    String name = null;
                    Row datRow;
                    if (val == null) {

                    }else{
                            int curIndex = Integer.valueOf(val);
                            datRow = listIter.getRowAtRangeIndex(curIndex);
                            name = (String)datRow.getAttribute("name");
                    }

                    System.out.println("The name we got is == " + name);

                    return name;

                    } catch(Exception e) {
                            return null;
            }

    }
    private String getClientType(String val) {
            try {
                    DCIteratorBinding listIter;
                    listIter = ADFUtils.findIterator("findClientTypesIterator");
                    String name = null;
                    Row datRow;
                    if (val == null) {
                      //CHOREA
                    }else{
                            int curIndex = Integer.valueOf(val);
                            datRow = listIter.getRowAtRangeIndex(curIndex);
                            name = (String)datRow.getAttribute("CLNTY_CLNT_TYPE");
                    }

                    System.out.println("The name we got is == " + name);

                    return name;

                    } catch(Exception e) {
                            return null;
            }

    }
    public void processLeads(InputStream csvFile) {
        String[][] csvvalues = null;
        int count = 0;
        java.util.Date dateofLead;
        java.util.Date dateodClose;
        String leadDate = null;
        String closeDate = null;
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement cst = null;
        try {
            csvvalues = CSVParser.parse(new InputStreamReader(csvFile));
            
            String query =
              "begin TQC_SETUPS_PKG.save_lead_details(" +
              "?,?,?,?,?,?,?,?,?,?," +
              "?,?,?,?,?,?,?,?,?,?," + 
              "?,?,?,?,?,?,?,?,?,?," +
              "?,?,?,?,?,?,?,?,?,?" +
              "); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 1; i < csvvalues.length; i++) {

                if (i == 0) {
                } else {

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                    callStmt.setObject(1, null);
                    callStmt.setString(2, null);
                    callStmt.setObject(3, null);

                    callStmt.setObject(4, csvvalues[i][0]);
                    callStmt.setObject(5, csvvalues[i][1]);
                    callStmt.setObject(6, csvvalues[i][2]);
                    callStmt.setObject(7, csvvalues[i][3]);
                    callStmt.setString(8, csvvalues[i][4]);
                    callStmt.setString(9, csvvalues[i][5]);
                    callStmt.setString(10, csvvalues[i][6]);
                    callStmt.setString(11, csvvalues[i][7]);
                    callStmt.setString(12, csvvalues[i][8]);
                    callStmt.setString(13, csvvalues[i][9]);
                    java.sql.Date dateLead = null;
                    if (csvvalues[i][10] != null) {
                        try {
                            leadDate = csvvalues[i][10].toString();
                            dateofLead = sdf1.parse(leadDate);
                            dateLead =
                                    new java.sql.Date((dateofLead).getTime());
                        } catch (Exception e) {
                            dateLead = null;
                        }
                    } else {
                        dateLead = null;
                    }
                    callStmt.setObject(14, null);
                    callStmt.setObject(15, null); //Status Null
                    callStmt.setObject(16, dateLead);
                    callStmt.setString(17, "N");
                    callStmt.setString(18, csvvalues[i][11]);
                    callStmt.setString(19, "N");
                    callStmt.setString(20, csvvalues[i][12]);
                    callStmt.setString(21, csvvalues[i][13]);
                    java.sql.Date dateClose = null;
                    if (csvvalues[i][14] != null) {
                        try {
                            closeDate = csvvalues[i][14].toString();
                            dateodClose = sdf1.parse(closeDate);
                            dateClose =
                                    new java.sql.Date((dateodClose).getTime());
                        } catch (Exception e) {
                            dateClose = null;
                        }
                    } else {
                        dateClose = null;
                    }
                    callStmt.setObject(22, dateClose);
                    callStmt.setString(23, csvvalues[i][15]);
                    callStmt.setString(24, csvvalues[i][16]);
                    callStmt.setString(25, csvvalues[i][17]);
                    callStmt.setObject(26, null);
                    callStmt.setObject(27, null);
                    callStmt.setObject(28, null);
                    callStmt.setObject(29, null);
                    callStmt.setObject(30, null);
                    callStmt.setObject(31, null);
                    callStmt.setObject(32, null);
                    callStmt.setObject(33, null);
                    callStmt.setObject(34, null);
                    callStmt.setObject(35, null);
                    callStmt.setObject(36, null);
                    callStmt.setString(37, csvvalues[i][18]);
                    callStmt.setObject(38, null);
                    callStmt.setObject(39, csvvalues[i][19]);
                    callStmt.setObject(40, GlobalCC.checkNullValues(session.getAttribute("Username")));
                    callStmt.addBatch();
                }
            }
            callStmt.executeBatch();
            callStmt.close();
            conn.close();


            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);

            GlobalCC.INFORMATIONREPORTING("Data Successfully inserted...");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
    }

    public void fileChangeListenerLeads(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            try {
                processLeads(uploadedFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTxtLeadSrcCode(RichInputText txtLeadSrcCode) {
        this.txtLeadSrcCode = txtLeadSrcCode;
    }

    public RichInputText getTxtLeadSrcCode() {
        return txtLeadSrcCode;
    }

    public void setTxtLeadStatusCode(RichInputText txtLeadStatusCode) {
        this.txtLeadStatusCode = txtLeadStatusCode;
    }

    public RichInputText getTxtLeadStatusCode() {
        return txtLeadStatusCode;
    }

    public void setTblCampaigns(RichTable tblCampaigns) {
        this.tblCampaigns = tblCampaigns;
    }

    public RichTable getTblCampaigns() {
        return tblCampaigns;
    }

    public String actionCancelLead() {
        GlobalCC.dismissPopUp("crm", "newEditLeadPop");
        return null;
    }

    public String actionHideSources() {
        GlobalCC.dismissPopUp("crm", "leadSourcePOP");
        return null;
    }
    
    public String actionHidePostalCode() {
        GlobalCC.dismissPopUp("crm", "postalCodePOP");
        return null;
    }

    public String actionHideStatues() {
        GlobalCC.dismissPopUp("crm", "leadStatusPOP");
        return null;
    }

    public String actionHideCoutries() {
        GlobalCC.dismissPopUp("crm", "CountriesPop");
        return null;
    }

    public String actionHideStates() {
        GlobalCC.dismissPopUp("crm", "StatesPop");
        return null;
    }

    public String actionHideTowns() {
        GlobalCC.dismissPopUp("crm", "TownsPop");
        return null;
    }

    public String actionHideOrgs() {
        GlobalCC.dismissPopUp("crm", "OrganisationsPop");
        return null;
    }

    public String actionHideUsers() {
        GlobalCC.dismissPopUp("crm", "UsersPop");
        return null;
    }

    public String actionHideTeams() {
        GlobalCC.dismissPopUp("crm", "TeamsPop");
        return null;
    }

    public String actionHideAccounts() {
        GlobalCC.dismissPopUp("crm", "AccountsPop");
        return null;
    }

    public String actionHideProducts() {
        GlobalCC.dismissPopUp("crm", "CampaignProductsPop");
        return null;
    }

    public String actionHideCampaigns() {
        GlobalCC.dismissPopUp("crm", "CampaignsPOp");
        return null;
    }

    public String actionHideSystems() {
        GlobalCC.dismissPopUp("crm", "SystemsPop");
        return null;
    }

    public String actionHideCurrencies() {
        GlobalCC.dismissPopUp("crm", "CurrenciesPop");
        return null;
    }


    public void setPnBoxLeadDetails(RichPanelBox pnBoxLeadDetails) {
        this.pnBoxLeadDetails = pnBoxLeadDetails;
    }

    public RichPanelBox getPnBoxLeadDetails() {
        return pnBoxLeadDetails;
    }

    public void setTxtSearchValue(RichInputText txtSearchValue) {
        this.txtSearchValue = txtSearchValue;
    }

    public RichInputText getTxtSearchValue() {
        return txtSearchValue;
    }

    public void setLblSearchValue(RichOutputLabel lblSearchValue) {
        this.lblSearchValue = lblSearchValue;
    }

    public RichOutputLabel getLblSearchValue() {
        return lblSearchValue;
    }

    public void setSelectCriteria(RichSelectOneChoice selectCriteria) {
        this.selectCriteria = selectCriteria;
    }

    public RichSelectOneChoice getSelectCriteria() {
        return selectCriteria;
    }

    public void actionSelectCriteriaChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            System.out.println("Selected");
            Object selectedValue = null;
            if (valueChangeEvent.getNewValue() == null)
                selectedValue = valueChangeEvent.getOldValue();
            else
                selectedValue = valueChangeEvent.getNewValue();
            if (selectedValue.toString().equals("1")) { //search by surname
                clearLeadDetails();
                lblSearchValue.setValue("Surname ");
                txtSearchValue.setDisabled(false);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                // session.setAttribute("SEARCHCRITERIA", null);
                //ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
            }
            if (selectedValue.toString().equals("2")) { //search by country
                clearLeadDetails();
                lblSearchValue.setValue("Country Name");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //session.setAttribute("SEARCHCRITERIA", null);
                //ADFUtils.findIterator("findLeadsIterator").executeQuery();
                //GlobalCC.refreshUI(tblLeads);
                btnCountry.setShortDesc("SEACRH");
                GlobalCC.showPopUp("crm", "CountriesPop");

            }
            if (selectedValue.toString().equals("3")) { //search by campaign
                clearLeadDetails();
                lblSearchValue.setValue("Campaign Name");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                // session.setAttribute("SEARCHCRITERIA", null);
                // ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //session.setAttribute("SEARCHCRITERIA", null);
                //ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
                btnCampaign.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "CampaignsPOp");

            }
            if (selectedValue.toString().equals("4")) { //search by product
                clearLeadDetails();
                lblSearchValue.setValue("Product Name");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //  session.setAttribute("SEARCHCRITERIA", null);
                // ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
                btnCampaign.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "ProductSystemsPOP");

            }
            if (selectedValue.toString().equals("5")) { //search by System
                lblSearchValue.setValue("System Name");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                // session.setAttribute("SEARCHCRITERIA", null);
                //ADFUtils.findIterator("findLeadsIterator").executeQuery();
                //GlobalCC.refreshUI(tblLeads);
                btnSystems.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "SystemsPop");

            }
            if (selectedValue.toString().equals("7")) { //search by LEAD SOURCE
                clearLeadDetails();
                lblSearchValue.setValue("Lead Source");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                // session.setAttribute("SEARCHCRITERIA", null);
                // ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
                btnLeadSource.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "leadSourcePOP");

            }
            if (selectedValue.toString().equals("8")) { //search by LEAD status
                clearLeadDetails();
                lblSearchValue.setValue("Lead Status");
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                dtDateCreated.setVisible(false);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //session.setAttribute("SEARCHCRITERIA", null);
                //ADFUtils.findIterator("findLeadsIterator").executeQuery();
                //GlobalCC.refreshUI(tblLeads);
                btnLeadStatus.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "leadStatusPOP");

            }
            if (selectedValue.toString().equals("9")) { //search by LEAD date

                clearLeadDetails();
                lblSearchValue.setValue("Date Created");
                dtDateCreated.setVisible(true);
                txtSearchValue.setVisible(false);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                // session.setAttribute("SEARCHCRITERIA", null);
                // ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);

            }
            if (selectedValue.toString().equals("10")) { //search by asssigned user
                clearLeadDetails();
                lblSearchValue.setValue("Assigned To");
                dtDateCreated.setVisible(false);
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                // session.setAttribute("SEARCHCRITERIA", null);
                //  ADFUtils.findIterator("findLeadsIterator").executeQuery();
                // GlobalCC.refreshUI(tblLeads);
                btnUsers.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "UsersPop");


            }
            if (selectedValue.toString().equals("11")) { //search by asssigned Team
                lblSearchValue.setValue("Team");
                dtDateCreated.setVisible(false);
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //  session.setAttribute("SEARCHCRITERIA", null);
                //  ADFUtils.findIterator("findLeadsIterator").executeQuery();
                //  GlobalCC.refreshUI(tblLeads);
                btnTeams.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "TeamsPop");


            }
            if (selectedValue.toString().equals("12")) { //search by Account
                clearLeadDetails();
                lblSearchValue.setValue("Account");
                dtDateCreated.setVisible(false);
                txtSearchValue.setDisabled(true);
                txtSearchValue.setVisible(true);
                GlobalCC.refreshUI(txtSearchValue);
                GlobalCC.refreshUI(lblSearchValue);
                GlobalCC.refreshUI(dtDateCreated);
                //   session.setAttribute("SEARCHCRITERIA", null);
                //  ADFUtils.findIterator("findLeadsIterator").executeQuery();
                //   GlobalCC.refreshUI(tblLeads);
                btnAccounts.setShortDesc("SEARCH");
                GlobalCC.showPopUp("crm", "AccountsPop");


            }
        }
    }

    public void setBtnCountry(RichCommandButton btnCountry) {
        this.btnCountry = btnCountry;
    }


    public RichCommandButton getBtnCountry() {
        return btnCountry;
    }

    public void actionSearchValueChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            String selectedValue =
                valueChangeEvent.getNewValue().toString().trim();
            String searchCriteria =
                "WHERE UPPER(LDS_SURNNAME) like UPPER('%" + selectedValue +
                "%')";
            session.setAttribute("SEARCHCRITERIA", searchCriteria);
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
            // session.setAttribute("SEARCHCRITERIA", null);

        }
    }

    public void setBtnCampaign(RichCommandButton btnCampaign) {
        this.btnCampaign = btnCampaign;
    }

    public RichCommandButton getBtnCampaign() {
        return btnCampaign;
    }

    public void setBtnSystems(RichCommandButton btnSystems) {
        this.btnSystems = btnSystems;
    }

    public RichCommandButton getBtnSystems() {
        return btnSystems;
    }

    public String actionAcceptSearchSystem() {
        Object key = tblSystemsSearch.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            btnProducts.setShortDesc("SEARCH");
            GlobalCC.showPopUp("crm", "CampaignProductsPop");

        }
        GlobalCC.dismissPopUp("crm", "SystemsPop");
        return null;
    }

    public void setTblSystemsSearch(RichTable tblSystemsSearch) {
        this.tblSystemsSearch = tblSystemsSearch;
    }

    public RichTable getTblSystemsSearch() {
        return tblSystemsSearch;
    }

    public void setBtnProducts(RichCommandButton btnProducts) {
        this.btnProducts = btnProducts;
    }

    public RichCommandButton getBtnProducts() {
        return btnProducts;
    }

    public void setBtnLeadSource(RichCommandButton btnLeadSource) {
        this.btnLeadSource = btnLeadSource;
    }

    public RichCommandButton getBtnLeadSource() {
        return btnLeadSource;
    }

    public void setBtnLeadStatus(RichCommandButton btnLeadStatus) {
        this.btnLeadStatus = btnLeadStatus;
    }

    public RichCommandButton getBtnLeadStatus() {
        return btnLeadStatus;
    }


    public void setDtDateCreated(RichInputDate dtDateCreated) {
        this.dtDateCreated = dtDateCreated;
    }

    public RichInputDate getDtDateCreated() {
        return dtDateCreated;
    }

    public void setBtnUsers(RichCommandButton btnUsers) {
        this.btnUsers = btnUsers;
    }

    public RichCommandButton getBtnUsers() {
        return btnUsers;
    }

    public void setBtnTeams(RichCommandButton btnTeams) {
        this.btnTeams = btnTeams;
    }

    public RichCommandButton getBtnTeams() {
        return btnTeams;
    }

    public void setBtnAccounts(RichCommandButton btnAccounts) {
        this.btnAccounts = btnAccounts;
    }

    public RichCommandButton getBtnAccounts() {
        return btnAccounts;
    }


    public void dateCreatedChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            Date date = GlobalCC.extractDate(dtDateCreated);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateCreated = sdf.format(date);

            String searchCriteria =
                "WHERE LDS_DATE=TO_DATE('" + dateCreated + "','DD/MM/yyyy')";
            session.setAttribute("SEARCHCRITERIA", searchCriteria);
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
            //session.setAttribute("SEARCHCRITERIA", null);
        }
    }

    public void setTblOrgDivisions(RichTable tblOrgDivisions) {
        this.tblOrgDivisions = tblOrgDivisions;
    }

    public RichTable getTblOrgDivisions() {
        return tblOrgDivisions;
    }

    public String actionAcceptOrgDivision() {
        Object key = tblOrgDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtOrgDivCode.setValue(nodeBinding.getAttribute("DIV_CODE"));
            txtOrgDivName.setValue(nodeBinding.getAttribute("DIV_NAME"));
            GlobalCC.refreshUI(txtOrgDivName);

        }
        GlobalCC.dismissPopUp("crm", "orgDivisionsPOP");
        return null;
    }

    public void setTxtOrgDivCode(RichInputText txtOrgDivCode) {
        this.txtOrgDivCode = txtOrgDivCode;
    }

    public RichInputText getTxtOrgDivCode() {
        return txtOrgDivCode;
    }

    public void setTxtOrgDivName(RichInputText txtOrgDivName) {
        this.txtOrgDivName = txtOrgDivName;
    }

    public RichInputText getTxtOrgDivName() {
        return txtOrgDivName;
    }

    public String actionShowOrgDivisions() {
        if (txtOrgCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(null,
                                        new Exception("Select an Organization first"));
            return null;
        }
        session.setAttribute("ORGCode", txtOrgCode.getValue());
        ADFUtils.findIterator("fetchOrgDivisionsIterator").executeQuery();
        GlobalCC.showPopUp("crm", "orgDivisionsPOP");
        return null;
    }

    public String actionHideOrgDivision() {
        GlobalCC.dismissPopUp("crm", "orgDivisionsPOP");
        return null;
    }

    public void setTxtOccupation(RichInputText txtOccupation) {
        this.txtOccupation = txtOccupation;
    }

    public RichInputText getTxtOccupation() {
        return txtOccupation;
    }
    
    

    public void setTxtClntShtDesc(RichInputText txtClntShtDesc) {
        this.txtClntShtDesc = txtClntShtDesc;
    }

    public RichInputText getTxtClntShtDesc() {
        return txtClntShtDesc;
    }

    public String convertLeadtoClient() {
        if (GlobalCC.checkNullValues(txtClntShtDesc.getValue()) != null &&
            txtLeadCode.getValue() != null) {
            GlobalCC.errorValueNotEntered("Lead Already Converted to Client");
            return null;
        }
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();


            String query =
                "begin TQC_SETUPS_PKG.convert_lead_to_client(?,?); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setObject(1, txtLeadCode.getValue());
            callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
            callStmt.execute();
            txtClntShtDesc.setValue(callStmt.getString(2));
            callStmt.close();
            conn.close();

            GlobalCC.sysInformation("Lead Successfully Converted to Client");
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
            GlobalCC.refreshUI(txtClntShtDesc);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
    public String convertLeadtoProspect() {
        if (GlobalCC.checkNullValues(txtClntShtDesc.getValue()) != null &&
            txtLeadCode.getValue() != null) {
            GlobalCC.errorValueNotEntered("Lead Already Converted to Client");
            return null;
        }
        OracleConnection conn = null;
        try {
            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();


            String query =
                "begin TQC_SETUPS_PKG.convert_lead_to_prospect(?); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setObject(1, txtLeadCode.getValue());
            callStmt.execute();
            
            callStmt.close();
            conn.close();

            GlobalCC.sysInformation("Lead Successfully Converted to Prospect");
            ADFUtils.findIterator("findLeadsIterator").executeQuery();
            GlobalCC.refreshUI(tblLeads);
            GlobalCC.refreshUI(txtClntShtDesc);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
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

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public String GenerateTemplateFile() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();

            String Header = sc.getRealPath("leadsTemplate.csv");
            //String Header = Params + "\n" +
            //   Params3;

            byte barray[] = Header.getBytes();


            //Print.
            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();

            ServletOutputStream servletOutputStream;
            response.reset();
            response.resetBuffer();
            servletOutputStream = response.getOutputStream();
            BufferedOutputStream bufferedOutputStream =
                new BufferedOutputStream(servletOutputStream);

            String output = "leadsTemplate.csv";
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                               "attachment; filename=" + output + "");


            response.setContentLength(barray.length);

            bufferedOutputStream.write(barray, 0, barray.length);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            response.reset();
            response.resetBuffer();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
        return null;
    }

    public void setIndustry(RichSelectOneChoice industry) {
        this.industry = industry;
    }

    public RichSelectOneChoice getIndustry() {
        return industry;
    }

   

    public void setType(RichSelectOneChoice type) {
        this.type = type;
    }

    public RichSelectOneChoice getType() {
        return type;
    }


    public void setPostalCode(RichInputText postalCode) {
        this.postalCode = postalCode;
    }

    public RichInputText getPostalCode() {
        return postalCode;
    }

    public void setPostalCodeTable(RichTable postalCodeTable) {
        this.postalCodeTable = postalCodeTable;
    }

    public RichTable getPostalCodeTable() {
        return postalCodeTable;
    }

    public void clientTypeSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            System.out.println("val =="+valueChangeEvent.getNewValue());
        if (getClientType(type.getValue().toString()).equalsIgnoreCase("INDIVIDUAL")){
            System.out.println("val2 =="+getClientType(type.getValue().toString()));
            coName.setVisible(false);
            lblCompanyName.setVisible(false);
            surname.setVisible(true);
            lblSurname.setVisible(true);
            otherNames.setVisible(true);
            lblOtherNames.setVisible(true);
            GlobalCC.refreshUI(coName);
            GlobalCC.refreshUI(surname);
            GlobalCC.refreshUI(otherNames);
            GlobalCC.refreshUI(lblCompanyName);
            GlobalCC.refreshUI(lblSurname);
            GlobalCC.refreshUI(lblOtherNames);
        } else{
            coName.setVisible(true);
            surname.setVisible(false);
            otherNames.setVisible(false);
            System.out.println("val3 =="+getClientType(type.getValue().toString()));
            GlobalCC.refreshUI(coName);
            GlobalCC.refreshUI(surname);
            GlobalCC.refreshUI(otherNames);
            GlobalCC.refreshUI(lblCompanyName);
            GlobalCC.refreshUI(lblSurname);
            GlobalCC.refreshUI(lblOtherNames);
        }
    }
    }

   
    public void setLblSurname(RichOutputLabel lblSurname) {
        this.lblSurname = lblSurname;
    }

    public RichOutputLabel getLblSurname() {
        return lblSurname;
    }

    public void setLblOtherNames(RichOutputLabel lblOtherNames) {
        this.lblOtherNames = lblOtherNames;
    }

    public RichOutputLabel getLblOtherNames() {
        return lblOtherNames;
    }

    public void setLblCompanyName(RichOutputLabel lblCompanyName) {
        this.lblCompanyName = lblCompanyName;
    }

    public RichOutputLabel getLblCompanyName() {
        return lblCompanyName;
    }

  public void setBtnPostalCode(RichCommandButton btnPostalCode)
  {
    this.btnPostalCode = btnPostalCode;
  }

  public RichCommandButton getBtnPostalCode()
  {
    return btnPostalCode;
  }

    public void setLeadCommentDisposition(RichSelectOneChoice leadCommentDisposition) {
        this.leadCommentDisposition = leadCommentDisposition;
    }

    public RichSelectOneChoice getLeadCommentDisposition() {
        return leadCommentDisposition;
    }
}
