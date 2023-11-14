/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.provider;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.utilities.CSVtoADFTableProcessor;
import TurnQuest.view.web.IprsSetupsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.net.FileNameMap;
import java.net.URLConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * The base backing bean for all the service provider related pages. Includes
 * properties and methods that map to given  UI components in the relevant
 * pages of the service providers.
 *
 * @author Frankline Ogongi
 */
public class ServiceProviderBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private Rendering renderer = new Rendering();
    private IprsSetupsApi iprsApi = null;

    /** An array of selectItem that will fill the ServiceProviderType selectOneChoice */
    public SelectItem[] elements = null;

    /** An array of selectItem that will fill the ServiceProviderSystems selectManyShuttle */
    public SelectItem[] sysElements = null;

    /** Variable to hold the selected Service Provider Type Value */
    public String selectedValue;

    /** Variable to hold the selected Service Provider System Value */
    public String selectedSystemValue;

    private BindingContainer bindings;
    private RichPanelBox requiredDocsBox;
    private RichPanelBox panelProviderDetails;
    private RichTree treeServiceProviders;
    private RichInputText txtProviderCode;
    private RichInputText txtProviderShortDesc;
    private RichInputText txtProviderName;
    private RichInputText txtProviderPhysicalAddress;
    private RichInputText txtProviderPostalAddress;
    private RichInputText txtProviderCountryCode;
    private RichInputText txtProviderTownCode;
    private RichInputText txtProviderType;
    private RichInputText txtProviderPhone;
    private RichInputText txtProviderFax;
    private RichInputText txtProviderEmail;
    private RichInputText txtProviderTitle;
    private RichInputText txtProviderZip;
    private RichInputDate txtProviderWef;
    private RichInputDate txtProviderWet;
    private RichInputText txtProviderContact;
    private RichInputText txtProviderAimsCode;
    private RichInputText txtProviderBankAccountNum;
    private RichInputText txtProviderCreatedBy;
    private RichInputDate txtProviderDateCreated;
    private RichInputText txtProviderStatusRemarks;
    private RichInputText txtProviderTrsOccup;
    private RichInputText txtProviderProffBody;
    private RichInputText txtProviderPin;
    private RichInputText txtProviderDocPhone;
    private RichInputText txtProviderDocEmail;
    private RichSelectOneChoice txtProviderStatus;
    private RichTable tblCountryPop;
    private RichInputText txtProviderCountryName;
    private RichTable tblTownPop;
    private RichInputText txtProviderTownName;
    private RichCommandButton btnCreateUpdateServiceProvider;
    private RichCommandButton btnCancelServiceProvider;
    private RichTable tblProviderActivities;
    private RichInputText txtProvActivityCode;
    private RichInputText txtProvActivityDesc;
    private RichInputText txtProvActivityTypeCode;
    private RichInputText txtProvActivityType;
    private RichInputText txtProvActivityProviderCode;
    private RichInputText txtProvActivityProvider;
    private RichSelectOneChoice txtProvActivityMainAct;
    private RichCommandButton btnCreateUpdateProviderActivity;
    private RichTable tblProviderTypePop;
    private RichTable tblProviderPop;
    private RichTree treeProvUnassignedSys;
    private RichTree treeProvAssignedSys;
    private RichInputText txtSelectedProvSysCode;
    private RichPanelBox panelProvSystems;
    private RichCommandButton btnAddProvSystem;
    private RichCommandButton btnRemoveProvSystem;
    private RichOutputText textToShow;
    private RichTable tblServiceProviderType;
    private RichInputText txtServiceProviderTypeCode;
    private RichInputText txtServiceProviderTypeName;
    private RichTable servActivitiesLov;
    private RichPanelTabbed mainDetailTab;
    private RichPanelBox panelBoxOuter;
    private RichInputText txtGLAccount;
    private RichTable tblSpGLAcccount;
    private RichTable tblBanks;
    private RichInputText txtBankName;
    private RichInputText txtSmsPrefix;
    private RichInputText txtBankBranchCode;
    private RichInputText txtBankCode;
    private RichInputText txtBankBranch;
    private RichTable tblBankBranches;
    private RichSelectOneChoice txtInhouse;
    private RichTable tbl1;
    private RichInputText txtSms;
    private RichTable serviceProviderTbl;
    private RichInputText txtContactPerson;
    private RichInputText txtContPersonPhone;
    private RichInputText txtInvoiceNumber;
    private RichInputText txtHoldingCompanies;
    private RichTable holdingCompanyTbl;
    private RichInputText txtContact;
    private RichInputText txtContactMobile;
    private RichInputText txtContactEmail;
    private RichInputText txtContactTel;
    private RichInputText txtBussinessPersons;
    private RichTable bussinessPersonTbl;
    private RichInputText txtTelPayPrefix;
    private RichTable prefixTbl;
    private RichTable smsPrefixTbl;
    private RichInputText txtTelPay;
    private RichSelectOneChoice defaultProvider;
    private RichInputText txtBnkBrn;
    private RichInputText txtBnkAccOfficer;
    private RichInputText txtBnkAccNumber;
    private RichInputText txtBnkAccName;
    private RichInputText txtBnkTelNo;
    private RichInputText txtBnkCellNo;
    private RichInputText txtBnkCurrency;
    private RichSelectOneChoice txtBnkDefault;
    private RichCommandButton saveBankDetailsBtn;
    private RichPopup bankdetailsPopUp;
    private RichTable tblBnkDetails;
    private RichInputText bnkCurrencyDesc;
    private RichTable tblCurrencyPop;
    private RichTable tblBankBranchPop;
    private RichInputText txtBnkBrnCode;
    private RichTable tblAccountOfficer;
    private RichTable bankBranchTbl;
    private RichInputText txtBnkIBAN;
    private RichSelectOneChoice txtBnkStatus;


    private RichTable tblReqDocsList;
    private RichInputText txtDocCodePop;
    private RichInputText txtReqDocCodePop;
    private RichInputText txtReqDocNamePop;
    private RichInputText txtDocAgnCodePop;
    private RichSelectOneChoice txtDocSubmittedPop;
    private RichInputDate txtDocDateSubmittedPop;
    private RichInputText txtDocRefNumPop;
    private RichInputText txtDocRemarkPop;
    private RichInputText txtDocUserReceivedPop;
    private RichCommandButton btnSaveRequiredDoc;
    private RichShowDetailItem agentRecDocstab;

    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private static String fileContent;
    private String filetype;
    private RichTable spRecDocsLov;

    private RichInputText txtPostalCode;
    private RichInputText txtRegNo;
    
    private RichInputText txtProvContacCode;
    private RichInputText txtProvName;
    private RichInputText txtProvTitle;
    private RichInputText txtProvOfficeTelNo;
    private RichInputText txtProvMobileNo;
    private RichInputText txtProvEmail;
    
    private RichOutputText txtProvEmailLabel;
    
    private RichCommandButton btnCreateUpdateProviderContacts;
    private RichShowDetailItem serviceProviderContactTab;
    private RichTable tblProviderTitles;
    
    private RichTable tblProvContDetails;
    private RichInputText txtShortDesc;
    private RichSelectOneChoice txtIdType;
    private RichInputText txtIdNo;
    private RichDialog dlgIprsComp;
    
    private RichInputText  txtTqIdNumber;
    private RichInputText  txtTqPinNo;
    private RichInputText  txtTqPassport;
    private RichInputText  txtTqDob;
    private RichInputText  txtTqFullNames;
    private RichInputText  txtTqGender;
    private RichInputText  txtIprsIdNumber;
    private RichInputText  txtIprsPinNo;
    private RichInputText  txtIprsPassport;
    private RichInputText  txtIprsDob;
    private RichInputText  txtIprsFullNames;
    private RichInputText  txtIprsGender; 
    
   


    /**
     * Default Class Constructor
     */
    public ServiceProviderBacking() {
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setElements(SelectItem[] elements) {
        this.elements = elements;
    }

    /**
     * Populate the selectOneChoice with the ServiceProviderType values
     *
     * @return
     */
    public SelectItem[] getElements() {

        BindingContainer bindings = getBindings();
        DCIteratorBinding iterator =
            (DCIteratorBinding)bindings.get("fetchAllServiceProviderTypesIterator");
        int length =
            iterator.getRangeSize() > 0 ? iterator.getRangeSize() : new Long(iterator.getEstimatedRowCount()).intValue();
        elements = new SelectItem[length];
        SelectItem item = null;

        for (int i = 0; i < length; i++) {
            Row row = iterator.getRowAtRangeIndex(i);

            if (row != null) {
                item = new SelectItem();
                item.setValue(row.getAttribute("code").toString());
                item.setLabel(row.getAttribute("name").toString());
            }
            elements[i] = item;
        }
        return elements;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
     
     
   
    
    public String actionCreateUpdateProviderContacts() {              
                
                
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;       
        String val = null;
       
        BigDecimal shortd = null;
        if (session.getAttribute("serviceProviderCode").toString() != null) {

            val = session.getAttribute("serviceProviderCode").toString();

        }
                
                String  sprCode = val;
                String sprContName = GlobalCC.checkNullValues(txtProvName.getValue());
                String sprContTitle = GlobalCC.checkNullValues(txtProvTitle.getValue());
                String sprContTelNo = GlobalCC.checkNullValues(txtProvOfficeTelNo.getValue());
                String sprContMobNo = GlobalCC.checkNullValues(txtProvMobileNo.getValue());
                String sprContEmail = GlobalCC.checkNullValues(txtProvEmail.getValue());
               
        
                if(txtShortDesc.getValue() != null){
                        
                    shortd = new BigDecimal(txtShortDesc.getValue().toString());
                }else{
                        shortd = null;
                    }
            
                String option=null;              
        if (sprContEmail== null) {
                   GlobalCC.errorValueNotEntered("Error Value Missing: Email!");
                   return null;
               }        
                
        if (sprContName== null) {
                   GlobalCC.errorValueNotEntered("Error Value Missing: Name!");
                   return null;
               }
        if (sprCode == null) {
                   GlobalCC.errorValueNotEntered("Error Value Missing:Select Valid Service Provider");
                   return null;
               }

        if (btnCreateUpdateProviderContacts.getText().equalsIgnoreCase("Update")) {
            option = "E";
            /*BigDecimal shortDescc=null;
            if(this.session.getAttribute("shortDescc")==null){              
               shortDescc = new BigDecimal(this.session.getAttribute("shortDescc").toString()) ;
              
            }else{shortDescc=null;}*/
        } else if (btnCreateUpdateProviderContacts.getText().equalsIgnoreCase("Save")) {
            option = "A";
        }
        try {
            
            
           // BigDecimal shortDescc=null;
            
            DBConnector connection = new DBConnector();
            conn = connection.getDatabaseConnection();
            query =
                    "begin tqc_setups_pkg.spr_contact_persons_prc(?,?,?,?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query);
           
           /* if(this.session.getAttribute("shortDescc")==null){
               // System.out.println("shortDescc"+shortDescc);
               shortDescc = new BigDecimal(this.session.getAttribute("shortDescc").toString()) ;
            }else{shortDescc=null;}
            */
            
            stmt.setString(1, option);           
            stmt.setBigDecimal(2, shortd);
            stmt.setString(3, sprCode);
            stmt.setString(4, sprContName);
            stmt.setString(5, sprContTitle);
            stmt.setString(6, sprContTelNo);
            stmt.setString(7, sprContMobNo);
            stmt.setString(8, sprContEmail);
        
            stmt.execute();

            stmt.close();
            conn.close();
            
            txtProvContacCode.setValue(null);
            txtProvName.setValue(null);
            txtProvTitle.setValue(null);
            txtProvOfficeTelNo.setValue(null);
            txtProvMobileNo.setValue(null);
            txtProvEmail.setValue(null);
            
            GlobalCC.refreshUI(txtProvContacCode);
            GlobalCC.refreshUI(txtProvName);
            GlobalCC.refreshUI(txtProvTitle);
            GlobalCC.refreshUI(txtProvOfficeTelNo);
            GlobalCC.refreshUI(txtProvMobileNo);
            GlobalCC.refreshUI(txtProvEmail);
            
           
            
            GlobalCC.INFORMATIONREPORTING("Contact Details Successfully Created!");                 
                        
                    
            GlobalCC.hidePopup("pt1:titlePop");  
                        
           
            ADFUtils.findIterator("fetchProviderContactsIterator").executeQuery();
            GlobalCC.refreshUI(tblProvContDetails);
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }
        return null;
    }
    
    public String getSelectedValue() {
        return selectedValue;
    }

    /**
     * The selectMany component's value is bound to this. This sets what is
     * available for selection.
     *
     * @param sysElements
     */
    public void setSysElements(SelectItem[] sysElements) {
        this.sysElements = sysElements;
    }

    /**
     * Populate the selectManyShuttle with the ServiceProviderSystem values
     *
     * @return Array of SelectItems
     */
    public SelectItem[] getSysElements() {

        BindingContainer bindings = getBindings();
        DCIteratorBinding iterator =
            (DCIteratorBinding)bindings.get("fetchAllSystemsIterator");
        int length =
            iterator.getRangeSize() > 0 ? iterator.getRangeSize() : new Long(iterator.getEstimatedRowCount()).intValue();
        sysElements = new SelectItem[length];
        SelectItem item = null;

        for (int i = 0; i < length; i++) {
            Row row = iterator.getRowAtRangeIndex(i);

            if (row != null) {
                item = new SelectItem();
                item.setValue(row.getAttribute("code").toString());
                item.setLabel(row.getAttribute("name").toString());
            }
            sysElements[i] = item;
        }
        return sysElements;
    }

    public void setSelectedSystemValue(String selectedSystemValue) {
        this.selectedSystemValue = selectedSystemValue;
    }

    /**
     * The selectManyShuttle component's value is bound to this. This returns
     * what was selected.
     *
     * @return String Provider System that is selected in the selectMany
     * component.
     */
    public String getSelectedSystemValue() {
        return selectedSystemValue;
    }

    /**
     * Get the page bindings
     *
     * @return
     */
    public BindingContainer getBindings() {
        if (this.bindings == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            this.bindings =
                    (BindingContainer)fc.getApplication().evaluateExpressionGet(fc,
                                                                                "#{bindings}",
                                                                                BindingContainer.class);
        }
        return this.bindings;
    }

    public void setBindings(BindingContainer bindings) {
        this.bindings = bindings;
    }

    public BindingContainer getBindings1() {
        return bindings;
    }

    public void setTreeServiceProviders(RichTree treeServiceProviders) {
        this.treeServiceProviders = treeServiceProviders;
    }

    public RichTree getTreeServiceProviders() {
        return treeServiceProviders;
    }

    public void setTxtProviderCode(RichInputText txtProviderCode) {
        this.txtProviderCode = txtProviderCode;
    }

    public RichInputText getTxtProviderCode() {
        return txtProviderCode;
    }

    public void setTxtProviderShortDesc(RichInputText txtProviderShortDesc) {
        this.txtProviderShortDesc = txtProviderShortDesc;
    }

    public RichInputText getTxtProviderShortDesc() {
        return txtProviderShortDesc;
    }

    public void setTxtProviderName(RichInputText txtProviderName) {
        this.txtProviderName = txtProviderName;
    }

    public RichInputText getTxtProviderName() {
        return txtProviderName;
    }

    public void setTxtProviderPhysicalAddress(RichInputText txtProviderPhysicalAddress) {
        this.txtProviderPhysicalAddress = txtProviderPhysicalAddress;
    }

    public RichInputText getTxtProviderPhysicalAddress() {
        return txtProviderPhysicalAddress;
    }

    public void setTxtProviderPostalAddress(RichInputText txtProviderPostalAddress) {
        this.txtProviderPostalAddress = txtProviderPostalAddress;
    }

    public RichInputText getTxtProviderPostalAddress() {
        return txtProviderPostalAddress;
    }

    public void setTxtProviderCountryCode(RichInputText txtProviderCountryCode) {
        this.txtProviderCountryCode = txtProviderCountryCode;
    }

    public RichInputText getTxtProviderCountryCode() {
        return txtProviderCountryCode;
    }

    public void setTxtProviderTownCode(RichInputText txtProviderTownCode) {
        this.txtProviderTownCode = txtProviderTownCode;
    }

    public RichInputText getTxtProviderTownCode() {
        return txtProviderTownCode;
    }

    public void setTxtProviderType(RichInputText txtProviderType) {
        this.txtProviderType = txtProviderType;
    }

    public RichInputText getTxtProviderType() {
        return txtProviderType;
    }

    public void setTxtProviderPhone(RichInputText txtProviderPhone) {
        this.txtProviderPhone = txtProviderPhone;
    }

    public RichInputText getTxtProviderPhone() {
        return txtProviderPhone;
    }

    public void setTxtProviderFax(RichInputText txtProviderFax) {
        this.txtProviderFax = txtProviderFax;
    }

    public RichInputText getTxtProviderFax() {
        return txtProviderFax;
    }

    public void setTxtProviderEmail(RichInputText txtProviderEmail) {
        this.txtProviderEmail = txtProviderEmail;
    }

    public RichInputText getTxtProviderEmail() {
        return txtProviderEmail;
    }

    public void setTxtProviderTitle(RichInputText txtProviderTitle) {
        this.txtProviderTitle = txtProviderTitle;
    }

    public RichInputText getTxtProviderTitle() {
        return txtProviderTitle;
    }

    public void setTxtProviderZip(RichInputText txtProviderZip) {
        this.txtProviderZip = txtProviderZip;
    }

    public RichInputText getTxtProviderZip() {
        return txtProviderZip;
    }

    public void setTxtProviderWef(RichInputDate txtProviderWef) {
        this.txtProviderWef = txtProviderWef;
    }

    public RichInputDate getTxtProviderWef() {
        return txtProviderWef;
    }

    public void setTxtProviderWet(RichInputDate txtProviderWet) {
        this.txtProviderWet = txtProviderWet;
    }

    public RichInputDate getTxtProviderWet() {
        return txtProviderWet;
    }

    public void setTxtProviderContact(RichInputText txtProviderContact) {
        this.txtProviderContact = txtProviderContact;
    }

    public RichInputText getTxtProviderContact() {
        return txtProviderContact;
    }

    public void setTxtProviderAimsCode(RichInputText txtProviderAimsCode) {
        this.txtProviderAimsCode = txtProviderAimsCode;
    }

    public RichInputText getTxtProviderAimsCode() {
        return txtProviderAimsCode;
    }


    public void setTxtProviderBankAccountNum(RichInputText txtProviderBankAccountNum) {
        this.txtProviderBankAccountNum = txtProviderBankAccountNum;
    }

    public RichInputText getTxtProviderBankAccountNum() {
        return txtProviderBankAccountNum;
    }

    public void setTxtProviderCreatedBy(RichInputText txtProviderCreatedBy) {
        this.txtProviderCreatedBy = txtProviderCreatedBy;
    }

    public RichInputText getTxtProviderCreatedBy() {
        return txtProviderCreatedBy;
    }

    public void setTxtProviderDateCreated(RichInputDate txtProviderDateCreated) {
        this.txtProviderDateCreated = txtProviderDateCreated;
    }

    public RichInputDate getTxtProviderDateCreated() {
        return txtProviderDateCreated;
    }

    public void setTxtProviderStatusRemarks(RichInputText txtProviderStatusRemarks) {
        this.txtProviderStatusRemarks = txtProviderStatusRemarks;
    }

    public RichInputText getTxtProviderStatusRemarks() {
        return txtProviderStatusRemarks;
    }

    public void setTxtProviderTrsOccup(RichInputText txtProviderTrsOccup) {
        this.txtProviderTrsOccup = txtProviderTrsOccup;
    }

    public RichInputText getTxtProviderTrsOccup() {
        return txtProviderTrsOccup;
    }

    public void setTxtProviderProffBody(RichInputText txtProviderProffBody) {
        this.txtProviderProffBody = txtProviderProffBody;
    }

    public RichInputText getTxtProviderProffBody() {
        return txtProviderProffBody;
    }

    public void setTxtProviderPin(RichInputText txtProviderPin) {
        this.txtProviderPin = txtProviderPin;
    }

    public RichInputText getTxtProviderPin() {
        return txtProviderPin;
    }

    public void setTxtProviderDocPhone(RichInputText txtProviderDocPhone) {
        this.txtProviderDocPhone = txtProviderDocPhone;
    }

    public RichInputText getTxtProviderDocPhone() {
        return txtProviderDocPhone;
    }

    public void setTxtProviderDocEmail(RichInputText txtProviderDocEmail) {
        this.txtProviderDocEmail = txtProviderDocEmail;
    }

    public RichInputText getTxtProviderDocEmail() {
        return txtProviderDocEmail;
    }

    public void setTxtProviderStatus(RichSelectOneChoice txtProviderStatus) {
        this.txtProviderStatus = txtProviderStatus;
    }

    public RichSelectOneChoice getTxtProviderStatus() {
        return txtProviderStatus;
    }

    public void setTblCountryPop(RichTable tblCountryPop) {
        this.tblCountryPop = tblCountryPop;
    }

    public RichTable getTblCountryPop() {
        return tblCountryPop;
    }

    public void setTxtProviderCountryName(RichInputText txtProviderCountryName) {
        this.txtProviderCountryName = txtProviderCountryName;
    }

    public RichInputText getTxtProviderCountryName() {
        return txtProviderCountryName;
    }

    public void setTblTownPop(RichTable tblTownPop) {
        this.tblTownPop = tblTownPop;
    }

    public RichTable getTblTownPop() {
        return tblTownPop;
    }

    public void setPanelProviderDetails(RichPanelBox panelProviderDetails) {
        this.panelProviderDetails = panelProviderDetails;
    }

    public RichPanelBox getPanelProviderDetails() {
        return panelProviderDetails;
    }

    public void setTxtProviderTownName(RichInputText txtProviderTownName) {
        this.txtProviderTownName = txtProviderTownName;
    }

    public RichInputText getTxtProviderTownName() {
        return txtProviderTownName;
    }

    public void setBtnCreateUpdateServiceProvider(RichCommandButton btnCreateUpdateServiceProvider) {
        this.btnCreateUpdateServiceProvider = btnCreateUpdateServiceProvider;
    }

    public RichCommandButton getBtnCreateUpdateServiceProvider() {
        return btnCreateUpdateServiceProvider;
    }

    public void setBtnCancelServiceProvider(RichCommandButton btnCancelServiceProvider) {
        this.btnCancelServiceProvider = btnCancelServiceProvider;
    }

    public RichCommandButton getBtnCancelServiceProvider() {
        return btnCancelServiceProvider;
    }

    /**
     * This method is called whenever a Service Provider Type is selected.
     * It populates the <code>treeServiceProviders</code> with all the service
     * providers that belong to that service provider type.
     *
     * @param valueChangeEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener.
     */
    public void serviceProviderChangeListener(ValueChangeEvent valueChangeEvent) {

        /*Integer provTypeIndex = new Integer(valueChangeEvent.getNewValue().toString());
        DCIteratorBinding provTypeListIter = ADFUtils.findIterator("fetchAllServiceProviderTypesIterator");
        Row provTypeRow = provTypeListIter.getRowAtRangeIndex(provTypeIndex.intValue());
        String currentProvType = (String)provTypeRow.getAttribute("code");

        String serviceProviderTypeCode = currentProvType;*/
        String serviceProviderTypeCode =
            valueChangeEvent.getNewValue().toString();
        session.setAttribute("serviceProviderTypeCode",
                             serviceProviderTypeCode);
        java.lang.System.out.println("Provider Type Code : " +
                                     serviceProviderTypeCode);

        ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
        GlobalCC.refreshUI(treeServiceProviders);


    }

    /**
     * This method is called whenever a Service Provider is selected in the
     * <code>treeServiceProviders</code> UI component. It populates the
     * detail section with information regarding that service Provider.
     *
     * @param selectionEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener of the
     * <code>treeServiceProviders</code>
     */
    
     public void treeProviderContSelectionListener(SelectionEvent selectionEvent) {
          
            Object key2 = tblProvContDetails.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            if(nodeBinding==null){
            this.session.setAttribute("shortDescc",nodeBinding.getAttribute("provCode"));
            
            }
        }
    
    public void treeServiceProviderSelectionListener(SelectionEvent selectionEvent) {
        session.setAttribute("PayeeType", "S");
        // Update the detail section with the Service Provider details
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeServiceProviders.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeServiceProviders.getRowData();

                    String serviceProviderCode =
                        (String)nd.getRow().getAttribute("code");
                    session.setAttribute("serviceProviderCode",
                                         serviceProviderCode);
                    session.setAttribute("ServiceProviderCode",
                                         serviceProviderCode);
                    session.setAttribute("ClientCode", serviceProviderCode);
                    session.setAttribute("bactAccCode", serviceProviderCode);

                    txtInhouse.setValue(nd.getRow().getAttribute("inhouse"));
                    txtProviderCode.setValue(nd.getRow().getAttribute("code"));
                    txtProviderShortDesc.setValue(nd.getRow().getAttribute("shortDesc"));
                    txtProviderName.setValue(nd.getRow().getAttribute("name"));
                    session.setAttribute("providerName",
                                         nd.getRow().getAttribute("name"));
                    txtProviderPhysicalAddress.setValue(nd.getRow().getAttribute("physicalAddress"));
                    txtProviderPostalAddress.setValue(nd.getRow().getAttribute("postalAddress"));
                    txtProviderCountryCode.setValue(nd.getRow().getAttribute("countryCode"));
                    txtProviderCountryName.setValue(nd.getRow().getAttribute("countryName"));
                    txtProviderTownCode.setValue(nd.getRow().getAttribute("townCode"));
                    txtProviderTownName.setValue(nd.getRow().getAttribute("townName"));
                    txtProviderType.setValue(nd.getRow().getAttribute("providerTypeCode"));
                    txtProviderPhone.setValue(nd.getRow().getAttribute("phone"));
                    txtProviderFax.setValue(nd.getRow().getAttribute("fax"));
                    txtProviderEmail.setValue(nd.getRow().getAttribute("email"));
                    txtProviderTitle.setValue(nd.getRow().getAttribute("title"));
                    txtProviderZip.setValue(nd.getRow().getAttribute("zip"));
                    session.setAttribute("zipCode",
                                         nd.getRow().getAttribute("zip"));
                    txtProviderWef.setValue(nd.getRow().getAttribute("wef"));
                    txtProviderWet.setValue(nd.getRow().getAttribute("wet"));
                    txtProviderContact.setValue(nd.getRow().getAttribute("contact"));
                    txtProviderAimsCode.setValue(nd.getRow().getAttribute("aimsCode"));
                    txtBankBranchCode.setValue(nd.getRow().getAttribute("bankBranchCode"));
                    txtProviderBankAccountNum.setValue(nd.getRow().getAttribute("bankAccNo"));
                    txtProviderCreatedBy.setValue(nd.getRow().getAttribute("createdBy"));
                    txtProviderDateCreated.setValue(nd.getRow().getAttribute("dateCreated"));
                    txtProviderStatusRemarks.setValue(nd.getRow().getAttribute("statusRemarks"));
                    txtProviderStatus.setValue(nd.getRow().getAttribute("status"));
                    txtProviderPin.setValue(nd.getRow().getAttribute("PINNumber"));
                    txtProviderTrsOccup.setValue(nd.getRow().getAttribute("trsOccupation"));
                    txtProviderProffBody.setValue(nd.getRow().getAttribute("proffBody"));
                    txtProviderPin.setValue(nd.getRow().getAttribute("PIN"));
                    txtProviderDocPhone.setValue(nd.getRow().getAttribute("docPhone"));
                    txtProviderDocEmail.setValue(nd.getRow().getAttribute("docEmail"));
                    txtBankName.setValue(nd.getRow().getAttribute("bankName"));
                    txtBankBranch.setValue(nd.getRow().getAttribute("bankBranchName"));
                    txtBankCode.setValue(nd.getRow().getAttribute("bankCode"));
                    txtSms.setValue(nd.getRow().getAttribute("smsNumber"));
                    txtInvoiceNumber.setValue(nd.getRow().getAttribute("invoiceNumber"));
                    session.setAttribute("CLNT_CODE",
                                         nd.getRow().getAttribute("clientCode"));

                    txtHoldingCompanies.setValue(nd.getRow().getAttribute("clientName"));
                    txtContactPerson.setValue(nd.getRow().getAttribute("contactName"));
                    txtContactEmail.setValue(nd.getRow().getAttribute("contactEmail"));
                    txtContPersonPhone.setValue(nd.getRow().getAttribute("contactPhone"));
                    txtContactTel.setValue(nd.getRow().getAttribute("contactTel"));

                    txtPostalCode.setValue(nd.getRow().getAttribute("postalcode"));
                    txtRegNo.setValue(nd.getRow().getAttribute("regNo"));
                    
                    txtIdType.setValue(nd.getRow().getAttribute("idType"));
                    txtIdNo.setValue(nd.getRow().getAttribute("idNo"));


                    session.setAttribute("bpnCode",
                                         nd.getRow().getAttribute("sprBpnCode"));
                    txtBussinessPersons.setValue(nd.getRow().getAttribute("bpnName"));

                    try {
                        String prefix = null;
                        String smNo = null;
                        int t = 0;
                        if (nd.getRow().getAttribute("telPay") != null) {
                            if (nd.getRow().getAttribute("zip") != null) {
                                prefix =
                                        nd.getRow().getAttribute("telPay").toString().replace(nd.getRow().getAttribute("zip").toString(),
                                                                                              "");
                                prefix = prefix.replace("+", "0");
                                if (txtTelPayPrefix.isVisible()) {
                                    smNo = prefix.substring(4);
                                    prefix = prefix.substring(0, 4);
                                } else {
                                    smNo = prefix;
                                }

                            }
                        } else {


                        }

                        // if(nodeBinding.getAttribute("couZipCode"))

                        txtTelPayPrefix.setValue(prefix);
                        /* upDateQualificationsRecomms((BigDecimal)row.getAttribute("qualId"),
                                      comments, null);*/
                        txtTelPay.setValue(smNo);
                    } catch (Exception e) {

                    }

                    GlobalCC.refreshUI(panelProviderDetails);
                    ADFUtils.findIterator("fetchActivitiesByProviderIterator").executeQuery();

                    // Fetch the systems
                    ADFUtils.findIterator("fetchProviderUnassignedSystemsIterator").executeQuery();
                    GlobalCC.refreshUI(treeProvUnassignedSys);
                    ADFUtils.findIterator("fetchProviderAssignedSystemsIterator").executeQuery();
                    GlobalCC.refreshUI(treeProvAssignedSys);

                    GlobalCC.refreshUI(tblProviderActivities);
                    ADFUtils.findIterator("fetchSpRequiredDocsIterator").executeQuery();
                    GlobalCC.refreshUI(spRecDocsLov);
                    mainDetailTab.setVisible(true);
                    GlobalCC.refreshUI(panelBoxOuter);
                    
                    
                    ADFUtils.findIterator("fetchProviderContactsIterator").executeQuery();
                    GlobalCC.refreshUI(tblProvContDetails);
                }
            }

            btnCreateUpdateServiceProvider.setText("Update");
        }
    }

    /**
     * Assign the country selected from the popup to the input text fields.
     *
     * @return
     */
    public String actionSelectCountryPop() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchAllCountriesInfoIterator");
        RowKeySet set = tblCountryPop.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {

            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            txtProviderCountryName.setValue(rows.getAttribute("name"));
            txtProviderCountryCode.setValue(rows.getAttribute("code"));
            txtProviderTownCode.setValue(null);
            txtProviderTownName.setValue(null);

            // Set the country code to be used to fetch the towns
            session.setAttribute("countryCode", rows.getAttribute("code"));

            ADFUtils.findIterator("fetchTownsByCountryIterator").executeQuery();
            GlobalCC.refreshUI(tblTownPop);
            GlobalCC.refreshUI(txtProviderCountryName);
            GlobalCC.refreshUI(txtProviderTownName);
        }
        GlobalCC.dismissPopUp("pt1", "countryPop");
        return null;
    }

    /**
     * Opens up a popup dialog for selecting the towns, but only if a country
     * has been selected.
     *
     * @return
     */
    public String actionShowTownPopup() {

        // Check if a country code exists
        if (txtProviderCountryCode.getValue() == null ||
            txtProviderCountryCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Country first!");

        } else {

            // Open the popup dialog to display towns
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:townPop" + "').show(hints);");
        }
        return null;
    }

    /**
     * Assign the town selected from the popup to the input text fields.
     *
     * @return
     */
    public String actionSelectTownPop() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchTownsByCountryIterator");
        RowKeySet set = tblTownPop.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {

            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            txtProviderTownName.setValue(rows.getAttribute("name"));
            txtProviderTownCode.setValue(rows.getAttribute("code"));
            txtPostalCode.setValue(rows.getAttribute("postalZipCode"));
            GlobalCC.refreshUI(txtPostalCode);
            GlobalCC.refreshUI(txtProviderTownName);

        }
        GlobalCC.dismissPopUp("pt1", "townPop");
        return null;
    }

    /**
     * Prepare the text fields to allow a new Service Provider to be added.
     *
     * @return null
     */
    public String actionNewServiceProvider() {

        session.setAttribute("PayeeType", "S");

        // Check if a service provider type has been selected
        session.removeAttribute("serviceProviderIprsValidated");

        if (session.getAttribute("serviceProviderTypeCode") == null ||
            session.getAttribute("serviceProviderTypeCode").equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Service Provider first!");

        } else {
            
            txtProviderContact.setDisabled(false);
            
            if("Y".equalsIgnoreCase(GlobalCC.getSysParamValue("MULTI_CONTACT_PERSONS_PER_SPR"))){
                    
                txtContactPerson.setVisible(false);
                txtContactEmail.setVisible(false);
                
                                      
            } else{
                
                    serviceProviderContactTab.setVisible(false);
                    txtContactPerson.setVisible(true);
                    txtContactEmail.setVisible(true);
                }
            session.removeAttribute("bactAccCode");
            txtGLAccount.setValue(null);
            txtProviderCode.setValue("");
            txtProviderShortDesc.setValue("");
            txtProviderName.setValue("");
            txtProviderPhysicalAddress.setValue("");
            txtProviderPostalAddress.setValue("");
            txtProviderCountryCode.setValue("");
            txtProviderCountryName.setValue("");
            txtProviderTownCode.setValue("");
            txtProviderTownName.setValue("");
            txtProviderType.setValue(session.getAttribute("serviceProviderTypeCode"));
            txtProviderPhone.setValue("");
            txtProviderFax.setValue("");
            txtProviderEmail.setValue("");
            txtProviderTitle.setValue("");
            txtProviderZip.setValue("");
            txtProviderWef.setValue("");
            txtBussinessPersons.setValue("");
            txtProviderWet.setValue("");
            txtProviderContact.setValue("");
            txtProviderAimsCode.setValue("");
            txtBankBranchCode.setValue("");
            txtProviderBankAccountNum.setValue("");
            //txtProviderCreatedBy.setValue( "" );
            //txtProviderDateCreated.setValue( "" );
            txtProviderStatusRemarks.setValue("");
            txtProviderStatus.setValue("");
            txtProviderTrsOccup.setValue("");
            txtProviderProffBody.setValue("");
            txtProviderPin.setValue("");
            txtProviderDocPhone.setValue("");
            txtProviderDocEmail.setValue("");
            txtHoldingCompanies.setValue("");
            txtInvoiceNumber.setValue("");
            txtTelPayPrefix.setValue("");
            txtTelPay.setValue("");
            session.removeAttribute("fromGisMultiProv");
            session.removeAttribute("CLNT_CODE");
            btnCreateUpdateServiceProvider.setText("Save");
            //btnCreateUpdateServiceProvider.setDisabled(false);
            //btnCancelServiceProvider.setDisabled(false);

            mainDetailTab.setVisible(true);
            GlobalCC.refreshUI(panelBoxOuter);
        }
        return null;
    }

    public String actionEditServiceProvider() {

        btnCreateUpdateServiceProvider.setText("Update");
        //btnCreateUpdateServiceProvider.setDisabled(false);
        //btnCancelServiceProvider.setDisabled(false);
        return null;
    }

    public String actionDeleteServiceProvider() {

        // Open the popup dialog to confirm delete action
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:confirmProviderDeletePop" +
                             "').show(hints);");
        return null;
    }

    /**
     * Update a service provider
     *
     * @return null
     */
    public String actionCreateUpdateServiceProvider() {
        
       
        session.setAttribute("PayeeType", "S");
        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateServiceProvider.getText().equals("Save")) {
            //GlobalCC.INFORMATIONREPORTING("txtIdType "+txtIdType.getValue());
            
            // Proceed to save
            session.setAttribute("sprName", txtProviderName.getValue());
            String status = actionCreateNewServiceProvider();

        } else {

            // Proceed to do an update
            String provCode =
                GlobalCC.checkNullValues(txtProviderCode.getValue());
            String provShortDesc =
                GlobalCC.checkNullValues(txtProviderShortDesc.getValue());
            String provName =
                GlobalCC.checkNullValues(txtProviderName.getValue());
            String provPhysicalAddress =
                GlobalCC.checkNullValues(txtProviderPhysicalAddress.getValue());
            String provStatus =
                GlobalCC.checkNullValues(txtProviderStatus.getValue());
            String provGLAccount =
                GlobalCC.checkNullValues(txtGLAccount.getValue());
            String inhouse = GlobalCC.checkNullValues(txtInhouse.getValue());
            String providerPIN =
                GlobalCC.checkNullValues(txtProviderPin.getValue());

            String providerReNo =
                GlobalCC.checkNullValues(txtRegNo.getValue());
            String providerPostalCode =
                GlobalCC.checkNullValues(txtPostalCode.getValue());

            String zipCode =
                GlobalCC.checkNullValues(session.getAttribute("zipCode"));
            
            String idType =GlobalCC.checkNullValues(txtIdType.getValue());
            String idNo =GlobalCC.checkNullValues(txtIdNo.getValue());

            Rendering rendering = new Rendering();
            boolean mand = rendering.isInvoiceMand();

            if (mand == true && txtInvoiceNumber.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select Invoice Number");
                return null;
            }
            if (providerPIN == null && rendering.isSP_PIN_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider PIN");
                return null;
            }
            if (inhouse == null) {
                inhouse = "N";
            }
            if (provCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Code to update");
                return null;

            } /*else if (provShortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Short Description");
                return null;

            } */ else if (provName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Name");
                return null;

            } else if (provPhysicalAddress == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Physical Address");
                return null;

            } else if (provStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter if Active or not");
                return null;

            } else if (txtProviderWef.getValue() == null ||
                       txtProviderWef.getValue().equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WEF Date");
                return null;

            } else if (txtProviderWet.getValue() == null ||
                       txtProviderWet.getValue().equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WET Date");
                return null;

            } else {
                //form fields validations
                FormUi formUi=new FormUi();
                if(!formUi.validate("serviceProviderTab")){//main validation engine
                              return null;
                 }

                String Query =
                    "begin TQC_SETUPS_PKG.service_providers_prc(" + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                // provShortDesc

                try {
                    // Take care of all the date fields on the form. Make sure they are not null.
                    // If the dates are null, then default to the current date.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date newWetDate = null; //new Date();
                    if (txtProviderWet.getValue() != null &&
                        !(txtProviderWet.getValue().equals(""))) {
                        String date2 = df.format(txtProviderWet.getValue());
                        newWetDate = df.parse(date2);
                    }

                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(provCode));
                    cst.setString(3,
                                  provShortDesc == null ? null : provShortDesc);
                    cst.setString(4,
                                  (provName != null) ? provName.toUpperCase() :
                                  null);
                    cst.setString(5, provPhysicalAddress);
                    cst.setString(6,
                                  (String)txtProviderPostalAddress.getValue());
                    cst.setString(7, (String)txtProviderTownCode.getValue());
                    cst.setString(8,
                                  (String)txtProviderCountryCode.getValue());
                    cst.setString(9, (String)txtProviderType.getValue());
                    cst.setString(10, (String)txtProviderPhone.getValue());
                    cst.setString(11, (String)txtProviderFax.getValue());
                    cst.setString(12, (String)txtProviderEmail.getValue());
                    cst.setString(13, (String)txtProviderTitle.getValue());
                    cst.setString(14, (String)txtProviderZip.getValue());
                    cst.setDate(15,
                                new java.sql.Date(((java.util.Date)txtProviderWef.getValue()).getTime()));
                    cst.setDate(16,
                                newWetDate != null ? new java.sql.Date(newWetDate.getTime()) :
                                null);
                    /*cst.setDate(16,
                                new java.sql.Date(((java.util.Date)txtProviderWet.getValue()).getTime()));*/
                    cst.setString(17, (String)txtProviderContact.getValue());
                    cst.setString(18, (String)txtProviderAimsCode.getValue());
                    cst.setString(19,
                                  GlobalCC.checkNullValues(txtBankBranchCode.getValue()));
                    cst.setString(20,
                                  (String)txtProviderBankAccountNum.getValue());
                    cst.setString(21,
                                  (String)session.getAttribute("Username"));
                    cst.setDate(22,
                                GlobalCC.extractDate(txtProviderDateCreated));
                    cst.setString(23,
                                  (String)txtProviderStatusRemarks.getValue());
                    cst.setString(24, provStatus);
                    cst.setString(25, (String)txtProviderPin.getValue());
                    cst.setString(26, (String)txtProviderTrsOccup.getValue());
                    cst.setString(27, (String)txtProviderProffBody.getValue());
                    cst.setString(28, providerPIN);
                    cst.setString(29, (String)txtProviderDocPhone.getValue());
                    cst.setString(30, (String)txtProviderDocEmail.getValue());
                    cst.setString(31, provGLAccount);
                    cst.setString(32, inhouse);

                    String smsNo =
                        GlobalCC.checkSmsNo(zipCode, txtSmsPrefix, txtSms);

                    cst.setString(33, smsNo);
                    cst.setString(34, (String)txtContactPerson.getValue());
                    cst.setString(35, (String)txtContPersonPhone.getValue());
                    cst.setObject(36, txtInvoiceNumber.getValue());
                    cst.setObject(37, session.getAttribute("CLNT_CODE"));
                    cst.setObject(38, session.getAttribute("bpnCode"));
                    cst.setObject(39, txtContactEmail.getValue());
                    cst.setObject(40, txtContactTel.getValue());

                    String telPayNo =
                        GlobalCC.checkSmsNo(zipCode, txtTelPayPrefix,
                                            txtTelPay);

                    cst.setString(41, telPayNo);
                    String defaultProv =
                        GlobalCC.checkNullValues(defaultProvider.getValue());
                    cst.setString(42, defaultProv);
                    cst.setString(43, providerReNo);
                    cst.setString(44, providerPostalCode);
                    cst.setString(45, idType);
                    cst.setString(46, idNo);
                    String validated = GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated"));
                    cst.setString(47,validated);
                    cst.execute();

                    ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
                    GlobalCC.refreshUI(treeServiceProviders);

                    cst.close();
                    conn.close();

                    String message = "Service Provider UPDATED successfully!";
                    txtPostalCode.setValue(null);
                    showMessagePopup(message);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * Create a new service provider
     *
     * @return Success message
     */
    public String actionCreateNewServiceProvider() {
        Rendering renderer = new Rendering();
        // Checking only the required fields
        String provShortDesc =
            GlobalCC.checkNullValues(txtProviderShortDesc.getValue());
        String provName = GlobalCC.checkNullValues(txtProviderName.getValue());
        String provPhysicalAddress =
            GlobalCC.checkNullValues(txtProviderPhysicalAddress.getValue());
        String provStatus =
            GlobalCC.checkNullValues(txtProviderStatus.getValue());
        String provGLAccount =
            GlobalCC.checkNullValues(txtGLAccount.getValue());
        String inhouse = GlobalCC.checkNullValues(txtInhouse.getValue());
        String providerPIN =
            GlobalCC.checkNullValues(txtProviderPin.getValue());

        String providerPostalCode =
            GlobalCC.checkNullValues(txtPostalCode.getValue());
        String providerReNo = GlobalCC.checkNullValues(txtRegNo.getValue());
        
        String idType =GlobalCC.checkNullValues(txtIdType.getValue());
        String idNo =GlobalCC.checkNullValues(txtIdNo.getValue());

        //   System.out.println("");
        if (inhouse == null) {
            inhouse = "N";
        }
        if (providerPIN == null && renderer.isSP_PIN_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider PIN");
            return null;
        }
        /* if (provShortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Short Description");
            return null;

        }*/ else if (provName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Name");
            return null;

        } else if (provPhysicalAddress == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Physical Address");
            return null;

        } else if (provStatus == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter if Active or not");
            return null;

        } else if (txtProviderWef.getValue() == null ||
                   txtProviderWef.getValue().equals("")) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WEF Date");
            return null;

            /*} else if (txtProviderWet.getValue() == null ||
                   txtProviderWet.getValue().equals("")) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WET Date");
            return null;*/

        }
        session.setAttribute("countSprVal", null);
        ADFUtils.findIterator("fetchExistingServiceProvIterator").executeQuery();
        GlobalCC.refreshUI(serviceProviderTbl);
        session.setAttribute("fromGisMultiProv", "Y");
        //      if(session.getAttribute("fromGisMultiProv")!=null){
        if (session.getAttribute("countSprVal") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmSaveServiceProv').show(hints);");

            return null;
        }
        //    }
        
        /* *******************form fields validations****************** */
        FormUi formUi=new FormUi();
        if(!formUi.validate("serviceProviderTab")){//main validation engine
                      return null;
        }
        
        String Query =
            "begin TQC_SETUPS_PKG.service_providers_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            // Take care of all the date fields on the form. Make sure they are not null.
            // If the dates are null, then default to the current date.
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date newWetDate = null; //new Date();
            if (txtProviderWet.getValue() != null &&
                !(txtProviderWet.getValue().equals(""))) {
                String date2 = df.format(txtProviderWet.getValue());
                newWetDate = df.parse(date2);
            }

            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, "A");
            cst.setBigDecimal(2, null);
            cst.setString(3, provShortDesc);
            cst.setString(4, provName);
            cst.setString(5, provPhysicalAddress);
            cst.setString(6, (String)txtProviderPostalAddress.getValue());
            cst.setString(7, (String)txtProviderTownCode.getValue());
            cst.setString(8, (String)txtProviderCountryCode.getValue());
            cst.setObject(9, txtProviderType.getValue());
            cst.setString(10, (String)txtProviderPhone.getValue());
            cst.setString(11, (String)txtProviderFax.getValue());
            cst.setString(12, (String)txtProviderEmail.getValue());
            cst.setString(13, (String)txtProviderTitle.getValue());
            cst.setString(14, (String)txtProviderZip.getValue());
            cst.setDate(15,
                        new java.sql.Date(((java.util.Date)txtProviderWef.getValue()).getTime()));
            cst.setDate(16,
                        newWetDate != null ? new java.sql.Date(newWetDate.getTime()) :
                        null);
            /*cst.setDate(16,
                            new java.sql.Date(((java.util.Date)txtProviderWet.getValue()).getTime()));*/
            cst.setString(17, (String)txtProviderContact.getValue());
            cst.setString(18, (String)txtProviderAimsCode.getValue());
            cst.setString(19,
                          GlobalCC.checkNullValues(txtBankBranchCode.getValue()));
            cst.setString(20, (String)txtProviderBankAccountNum.getValue());
            cst.setString(21, (String)session.getAttribute("Username"));
            cst.setDate(22, new java.sql.Date(new Date().getTime()));
            //cst.setDate(22, new java.sql.Date(((java.util.Date)txtProviderDateCreated.getValue()).getTime()));
            cst.setString(23, (String)txtProviderStatusRemarks.getValue());
            cst.setString(24, (String)txtProviderStatus.getValue());
            cst.setString(25, (String)txtProviderPin.getValue());
            cst.setString(26, (String)txtProviderTrsOccup.getValue());
            cst.setString(27, (String)txtProviderProffBody.getValue());
            cst.setString(28, providerPIN);
            cst.setString(29, (String)txtProviderDocPhone.getValue());
            cst.setString(30, (String)txtProviderDocEmail.getValue());
            cst.setString(31, provGLAccount);
            cst.setString(32, inhouse);
            if (txtSms.getValue() == null) {
                cst.setString(33, null);
            } else {
                cst.setString(33, txtSms.getValue().toString());
            }
            cst.setString(34, (String)txtContactPerson.getValue());
            cst.setString(35, (String)txtContPersonPhone.getValue());
            cst.setObject(36, txtInvoiceNumber.getValue());
            cst.setObject(37, session.getAttribute("CLNT_CODE"));
            cst.setObject(38, session.getAttribute("bpnCode"));
            cst.setObject(39, txtContactEmail.getValue());
            cst.setObject(40, txtContactTel.getValue());
            String sms = GlobalCC.checkNullValues(txtTelPay.getValue());

            String prefix =
                GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
            if (txtTelPayPrefix.isVisible()) {
                if (prefix != null) {
                    //                    Row row =
                    //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                    //                    if (row.getAttribute("prefix") != null) {
                    //                        prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }
                    //sms = prefix + "" + sms;
                    if (session.getAttribute("zipCode") == null) {
                        sms = prefix + "" + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                    }
                    // }
                }
            } else {
                if (sms != null) {
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    }
                }
            }

            cst.setString(41, sms);
            cst.setString(42, (String)defaultProvider.getValue());

            cst.setString(43, providerReNo);
            cst.setString(44, providerPostalCode);
            cst.setString(45, idType);
            cst.setString(46, idNo);
            cst.setString(47, null);
            cst.execute();

            ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(treeServiceProviders);

            cst.close();
            conn.close();

            String message = "Service Provider CREATED successfully!";
            txtPostalCode.setValue(null);
            showMessagePopup(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return "Success";
    }

    public String actionCancelServiceProvider() {
        // Add event code here...
        return null;
    }

    /**
     * Proceed to delete the service provider
     *
     * @return
     */
    public String actionConfirmDeleteServiceProvider() {

        // Proceed to do delete
        String provCode = GlobalCC.checkNullValues(txtProviderCode.getValue());
        String provShortDesc =
            GlobalCC.checkNullValues(txtProviderShortDesc.getValue());
        String provName = GlobalCC.checkNullValues(txtProviderName.getValue());
        String provPhysicalAddress =
            GlobalCC.checkNullValues(txtProviderPhysicalAddress.getValue());

        String Query =
            "begin TQC_SETUPS_PKG.service_providers_prc(" + "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            // Take care of all the date fields on the form. Make sure they are not null.
            // If the dates are null, then default to the current date.
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date newWetDate = null; //new Date();
            if (txtProviderWet.getValue() != null &&
                !(txtProviderWet.getValue().equals(""))) {
                String date2 = df.format(txtProviderWet.getValue());
                newWetDate = df.parse(date2);
            }

            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, "D");
            cst.setBigDecimal(2, new BigDecimal(provCode));
            cst.setString(3, provShortDesc);
            cst.setString(4, provName);
            cst.setString(5, provPhysicalAddress);
            cst.setString(6, (String)txtProviderPostalAddress.getValue());
            cst.setString(7, (String)txtProviderTownCode.getValue());
            cst.setString(8, (String)txtProviderCountryCode.getValue());
            cst.setString(9, (String)txtProviderType.getValue());
            cst.setString(10, (String)txtProviderPhone.getValue());
            cst.setString(11, (String)txtProviderFax.getValue());
            cst.setString(12, (String)txtProviderEmail.getValue());
            cst.setString(13, (String)txtProviderTitle.getValue());
            cst.setString(14, (String)txtProviderZip.getValue());
            cst.setDate(15,
                        new java.sql.Date(((java.util.Date)txtProviderWef.getValue()).getTime()));
            cst.setDate(16,
                        newWetDate != null ? new java.sql.Date(newWetDate.getTime()) :
                        null);
            /*cst.setDate(16,
                        new java.sql.Date(((java.util.Date)txtProviderWet.getValue()).getTime()));*/
            cst.setString(17, (String)txtProviderContact.getValue());
            cst.setString(18, (String)txtProviderAimsCode.getValue());
            cst.setString(19,
                          GlobalCC.checkNullValues(txtBankBranchCode.getValue()));
            cst.setString(20, (String)txtProviderBankAccountNum.getValue());
            cst.setString(21, (String)session.getAttribute("Username"));
            cst.setDate(22,
                        new java.sql.Date(((java.util.Date)txtProviderDateCreated.getValue()).getTime()));
            cst.setString(23, (String)txtProviderStatusRemarks.getValue());
            cst.setString(24, (String)txtProviderStatus.getValue());
            cst.setString(25, (String)txtProviderPin.getValue());
            cst.setString(26, (String)txtProviderTrsOccup.getValue());
            cst.setString(27, (String)txtProviderProffBody.getValue());
            cst.setString(28, (String)txtProviderPin.getValue());
            cst.setString(29, (String)txtProviderDocPhone.getValue());
            cst.setString(30, (String)txtProviderDocEmail.getValue());
            cst.setString(31, null);
            cst.setString(32, null);
            cst.setString(33, null);
            cst.setString(34, null);
            cst.setString(35, null);
            cst.setString(36, null);
            cst.setString(37, null);
            cst.setObject(38, null);
            cst.setObject(39, null);
            cst.setObject(40, null);
            cst.setObject(41, null);
            cst.setObject(42, null);
            cst.setObject(43, null);
            cst.setObject(44, null);
            cst.setObject(45, null);
            cst.setObject(46, null);
            cst.setObject(47, null);
            
            cst.execute();

            cst.close();
            conn.close();

            String message = "Service Provider DELETED successfully!";
            showMessagePopup(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        // Clear the fields and disable the buttons
        txtProviderCode.setValue("");
        txtProviderShortDesc.setValue("");
        txtProviderName.setValue("");
        txtProviderPhysicalAddress.setValue("");
        txtProviderPostalAddress.setValue("");
        txtProviderCountryCode.setValue("");
        txtProviderCountryName.setValue("");
        txtProviderTownCode.setValue("");
        txtProviderTownName.setValue("");
        txtProviderType.setValue(session.getAttribute("serviceProviderTypeCode"));
        txtProviderPhone.setValue("");
        txtProviderFax.setValue("");
        txtProviderEmail.setValue("");
        txtProviderTitle.setValue("");
        txtProviderZip.setValue("");
        txtProviderWef.setValue("");
        txtProviderWet.setValue("");
        txtProviderContact.setValue("");
        txtProviderAimsCode.setValue("");
        txtBankBranchCode.setValue("");
        txtProviderBankAccountNum.setValue("");
        //txtProviderCreatedBy.setValue( "" );
        //txtProviderDateCreated.setValue( "" );
        txtProviderStatusRemarks.setValue("");
        txtProviderStatus.setValue("");
        txtProviderTrsOccup.setValue("");
        txtProviderProffBody.setValue("");
        txtProviderPin.setValue("");
        txtProviderDocPhone.setValue("");
        txtProviderDocEmail.setValue("");
        txtGLAccount.setValue(null);


        //btnCreateUpdateServiceProvider.setDisabled(true);
        //btnCancelServiceProvider.setDisabled(true);


        ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
        GlobalCC.refreshUI(treeServiceProviders);
        return null;
    }

    public void setTblProviderActivities(RichTable tblProviderActivities) {
        this.tblProviderActivities = tblProviderActivities;
    }

    public RichTable getTblProviderActivities() {
        return tblProviderActivities;
    }

    public String actionNewProviderActivity() {
        String val = null;
        if (session.getAttribute("serviceProviderCode").toString() != null) {

            val = session.getAttribute("serviceProviderCode").toString();

        }

        txtProvActivityCode.setValue("");
        txtProvActivityDesc.setValue("");
        txtProvActivityTypeCode.setValue(session.getAttribute("serviceProviderTypeCode").toString());
        txtProvActivityType.setValue(session.getAttribute("serviceProviderType"));
        txtProvActivityProviderCode.setValue(val);

        txtProvActivityProvider.setValue(session.getAttribute("providerName"));
        txtProvActivityMainAct.setValue("");

        btnCreateUpdateProviderActivity.setText("Save");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:providerActivityPop').show(hints);");
        return null;
    }

    public String actionEditProviderActivity() {
        Object key2 = tblProviderActivities.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtProvActivityCode.setValue(nodeBinding.getAttribute("code"));
            txtProvActivityDesc.setValue(nodeBinding.getAttribute("description"));
            txtProvActivityTypeCode.setValue(session.getAttribute("serviceProviderTypeCode"));
            txtProvActivityType.setValue(session.getAttribute("serviceProviderType"));
            txtProvActivityProviderCode.setValue(nodeBinding.getAttribute("serviceProviderCode"));
            txtProvActivityProvider.setValue(nodeBinding.getAttribute("serviceProviderShortDesc"));
            txtProvActivityMainAct.setValue(nodeBinding.getAttribute("mainAct"));

            btnCreateUpdateProviderActivity.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:providerActivityPop').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteProviderActivity() {
        Object key2 = tblProviderActivities.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String activityCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("code"));
            String activityDesc =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("description"));
            String provTypeCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("providerTypeCode"));
            String provTypeDesc =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("providerTypeShortDesc"));
            String provCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("serviceProviderCode"));
            String provDesc =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("serviceProviderShortDesc"));
            String provMainAct =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("mainAct"));

            String Query =
                "begin TQC_SETUPS_PKG.service_prov_activities_prc(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            activityCode = nodeBinding.getAttribute("code").toString();

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2, new BigDecimal(activityCode));
                cst.setString(3, provTypeCode);
                cst.setString(4, provTypeDesc);
                cst.setString(5, provCode);
                cst.setString(6, provDesc);
                cst.setString(7, provMainAct);
                cst.setString(8, activityDesc);
                cst.setBigDecimal(9,
                                  (BigDecimal)session.getAttribute("servActivityCode"));
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchActivitiesByProviderIterator").executeQuery();
                GlobalCC.refreshUI(tblProviderActivities);

                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }
    
    
    public String actionDeleteProviderContacts() {
        Object key2 = tblProvContDetails.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            
            BigDecimal shortDescc=null;
            String sprContName = GlobalCC.checkNullValues(nodeBinding.getAttribute("provName"));
            String sprContTitle = GlobalCC.checkNullValues(nodeBinding.getAttribute("provTitle"));
            String sprContTelNo = GlobalCC.checkNullValues(nodeBinding.getAttribute("provOfficeTelNo"));
            String sprContMobNo = GlobalCC.checkNullValues(nodeBinding.getAttribute("provMobileNo"));
            String sprContEmail = GlobalCC.checkNullValues(nodeBinding.getAttribute("provEmail"));
            
           
                 shortDescc = new BigDecimal(nodeBinding.getAttribute("provCode").toString());
           

            String Query =
            "begin tqc_setups_pkg.spr_contact_persons_prc(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            String sprCode = session.getAttribute("serviceProviderCode").toString();

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");   
                cst.setBigDecimal(2, shortDescc);
                cst.setString(3, sprCode);
                cst.setString(4, sprContName);
                cst.setString(5, sprContTitle);
                cst.setString(6, sprContTelNo);
                cst.setString(7, sprContMobNo);
                cst.setString(8, sprContEmail);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchProviderContactsIterator").executeQuery();
                GlobalCC.refreshUI(tblProvContDetails);

                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }
    

    public void setTxtProvActivityCode(RichInputText txtProvActivityCode) {
        this.txtProvActivityCode = txtProvActivityCode;
    }

    public RichInputText getTxtProvActivityCode() {
        return txtProvActivityCode;
    }

    public void setTxtProvActivityDesc(RichInputText txtProvActivityDesc) {
        this.txtProvActivityDesc = txtProvActivityDesc;
    }

    public RichInputText getTxtProvActivityDesc() {
        return txtProvActivityDesc;
    }

    public void setTxtProvActivityTypeCode(RichInputText txtProvActivityTypeCode) {
        this.txtProvActivityTypeCode = txtProvActivityTypeCode;
    }

    public RichInputText getTxtProvActivityTypeCode() {
        return txtProvActivityTypeCode;
    }

    public void setTxtProvActivityType(RichInputText txtProvActivityType) {
        this.txtProvActivityType = txtProvActivityType;
    }

    public RichInputText getTxtProvActivityType() {
        return txtProvActivityType;
    }

    public void setTxtProvActivityProviderCode(RichInputText txtProvActivityProviderCode) {
        this.txtProvActivityProviderCode = txtProvActivityProviderCode;
    }

    public RichInputText getTxtProvActivityProviderCode() {
        return txtProvActivityProviderCode;
    }

    public void setTxtProvActivityProvider(RichInputText txtProvActivityProvider) {
        this.txtProvActivityProvider = txtProvActivityProvider;
    }

    public RichInputText getTxtProvActivityProvider() {
        return txtProvActivityProvider;
    }

    public void setTxtProvActivityMainAct(RichSelectOneChoice txtProvActivityMainAct) {
        this.txtProvActivityMainAct = txtProvActivityMainAct;
    }

    public RichSelectOneChoice getTxtProvActivityMainAct() {
        return txtProvActivityMainAct;
    }

    public void setBtnCreateUpdateProviderActivity(RichCommandButton btnCreateUpdateProviderActivity) {
        this.btnCreateUpdateProviderActivity = btnCreateUpdateProviderActivity;
    }

    public RichCommandButton getBtnCreateUpdateProviderActivity() {
        return btnCreateUpdateProviderActivity;
    }

    public String actionAcceptProviderType() {

        Object key2 = tblProviderTypePop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtProvActivityType.setValue(nodeBinding.getAttribute("name"));
            txtProvActivityTypeCode.setValue(nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtProvActivityType);
            GlobalCC.refreshUI(txtProvActivityTypeCode);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:providerActivityPop').show(hints);");
        }
        GlobalCC.dismissPopUp("pt1", "activityProviderTypePop");
        return null;
    }

    public String actionAcceptProvider() {
        Object key2 = tblProviderPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtProvActivityProvider.setValue(nodeBinding.getAttribute("name"));
            txtProvActivityProviderCode.setValue(nodeBinding.getAttribute("code"));

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:providerActivityPop').show(hints);");
        }
        GlobalCC.dismissPopUp("pt1", "activityProviderPop");
        return null;
    }

    public void setTblProviderTypePop(RichTable tblProviderTypePop) {
        this.tblProviderTypePop = tblProviderTypePop;
    }

    public RichTable getTblProviderTypePop() {
        return tblProviderTypePop;
    }

    public void setTblProviderPop(RichTable tblProviderPop) {
        this.tblProviderPop = tblProviderPop;
    }

    public RichTable getTblProviderPop() {
        return tblProviderPop;
    }

    /**
     * Update an existing Provider Activity record to the database with the
     * new values.
     *
     * @return null
     */
    public String actionCreateUpdateProviderActivity() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateProviderActivity.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewProviderActivity();

        } else {

            // Proceed to do an update
            // Checking only the required fields
            String activityCode =
                GlobalCC.checkNullValues(txtProvActivityCode.getValue());
            String activityDesc =
                GlobalCC.checkNullValues(txtProvActivityDesc.getValue());
            String provTypeCode =
                GlobalCC.checkNullValues(txtProvActivityTypeCode.getValue());
            String provTypeDesc =
                GlobalCC.checkNullValues(txtProvActivityType.getValue());
            String provCode =
                GlobalCC.checkNullValues(txtProvActivityProviderCode.getValue());
            String provDesc =
                GlobalCC.checkNullValues(txtProvActivityProvider.getValue());
            String provMainAct =
                GlobalCC.checkNullValues(txtProvActivityMainAct.getValue());


            if (activityCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: The Activity Code is missing");
                return null;

            }
            if (activityDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Activity Description");
                return null;

            } else if (provTypeCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Type Code");
                return null;

            } else if (provMainAct == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select whether main act or not");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.service_prov_activities_prc(?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(activityCode));
                    cst.setString(3, provTypeCode);
                    cst.setString(4, provTypeDesc);
                    cst.setString(5, provCode);
                    cst.setString(6, provDesc);
                    cst.setString(7, provMainAct);
                    cst.setString(8, activityDesc);
                    cst.setBigDecimal(9,
                                      (BigDecimal)session.getAttribute("servActivityCode"));
                    cst.execute();

                    ADFUtils.findIterator("fetchActivitiesByProviderIterator").executeQuery();
                    GlobalCC.refreshUI(tblProviderActivities);

                    cst.close();
                    conn.close();
                    GlobalCC.dismissPopUp("pt1", "providerActivityPop");

                    String message = "Service Provider UPDATED successfully!";
                    GlobalCC.sysInformation(message);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }

        return null;
    }

    /**
     * Save a new Provider Activity record to the database
     *
     * @return Success message
     */
    public String actionCreateNewProviderActivity() {

        // Checking only the required fields
        String activityDesc =
            GlobalCC.checkNullValues(txtProvActivityDesc.getValue());
        String provTypeCode =
            GlobalCC.checkNullValues(txtProvActivityTypeCode.getValue());
        String provTypeDesc =
            GlobalCC.checkNullValues(txtProvActivityType.getValue());
        String provCode =
            GlobalCC.checkNullValues(txtProvActivityProviderCode.getValue());
        String provDesc =
            GlobalCC.checkNullValues(txtProvActivityProvider.getValue());
        String provMainAct =
            GlobalCC.checkNullValues(txtProvActivityMainAct.getValue());


        if (activityDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Activity Description");
            return null;

        } else if (provTypeCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Type Code");
            return null;

        } else if (provTypeDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Provider Type Description");
            return null;

        } else if (provCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Provider Code");
            return null;

        } else if (provDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Provider Description");
            return null;

        } else if (provMainAct == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select whether main act or not");
            return null;

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.service_prov_activities_prc(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, provTypeCode);
                cst.setString(4, provTypeDesc);
                cst.setString(5, provCode);
                cst.setString(6, provDesc);
                cst.setString(7, provMainAct);
                cst.setString(8, activityDesc);
                cst.setBigDecimal(9,
                                  (BigDecimal)session.getAttribute("servActivityCode"));
                cst.execute();

                ADFUtils.findIterator("fetchActivitiesByProviderIterator").executeQuery();
                GlobalCC.refreshUI(tblProviderActivities);

                cst.close();
                conn.close();
                GlobalCC.dismissPopUp("pt1", "providerActivityPop");
                String message = "New Service Provider saved successfully!";
                GlobalCC.sysInformation(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    public void setTreeProvUnassignedSys(RichTree treeProvUnassignedSys) {
        this.treeProvUnassignedSys = treeProvUnassignedSys;
    }

    public RichTree getTreeProvUnassignedSys() {
        return treeProvUnassignedSys;
    }

    public void setTreeProvAssignedSys(RichTree treeProvAssignedSys) {
        this.treeProvAssignedSys = treeProvAssignedSys;
    }

    public RichTree getTreeProvAssignedSys() {
        return treeProvAssignedSys;
    }

    public void setTxtSelectedProvSysCode(RichInputText txtSelectedProvSysCode) {
        this.txtSelectedProvSysCode = txtSelectedProvSysCode;
    }

    public RichInputText getTxtSelectedProvSysCode() {
        return txtSelectedProvSysCode;
    }

    public void setPanelProvSystems(RichPanelBox panelProvSystems) {
        this.panelProvSystems = panelProvSystems;
    }

    public RichPanelBox getPanelProvSystems() {
        return panelProvSystems;
    }

    public void setBtnAddProvSystem(RichCommandButton btnAddProvSystem) {
        this.btnAddProvSystem = btnAddProvSystem;
    }

    public RichCommandButton getBtnAddProvSystem() {
        return btnAddProvSystem;
    }

    public String processAddProvSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("serviceProviderCode").equals(null) ||
            (session.getAttribute("serviceProviderCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a provider first.");
        }
        if (GlobalCC.checkNullValues(txtSelectedProvSysCode.getValue()) ==
            null) {
            GlobalCC.errorValueNotEntered("You need to select a provider first.");
            return null;
        }

        if (txtSelectedProvSysCode.getValue().equals(null) ||
            txtSelectedProvSysCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_setups_pkg.grant_prov_System(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("serviceProviderCode").toString()));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedProvSysCode.getValue().toString()));
                statement.setString(3,
                                    session.getAttribute("Username").toString());
                statement.execute();
                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedProvSysCode.setValue(null);

        // Fetch the systems
        ADFUtils.findIterator("fetchProviderUnassignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeProvUnassignedSys);
        ADFUtils.findIterator("fetchProviderAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeProvAssignedSys);
        return null;
    }

    public String processRemoveProvSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("serviceProviderCode").equals(null) ||
            (session.getAttribute("serviceProviderCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a provider first.");
        }

        if (GlobalCC.checkNullValues(txtSelectedProvSysCode.getValue()) ==
            null) {
            GlobalCC.errorValueNotEntered("You need to select a provider first.");
            return null;
        }

        if (txtSelectedProvSysCode.getValue().equals(null) ||
            txtSelectedProvSysCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_setups_pkg.revoke_prov_System(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("serviceProviderCode").toString()));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedProvSysCode.getValue().toString()));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedProvSysCode.setValue(null);

        // Fetch the systems
        ADFUtils.findIterator("fetchProviderUnassignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeProvUnassignedSys);
        ADFUtils.findIterator("fetchProviderAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeProvAssignedSys);
        return null;
    }

    public void setBtnRemoveProvSystem(RichCommandButton btnRemoveProvSystem) {
        this.btnRemoveProvSystem = btnRemoveProvSystem;
    }

    public RichCommandButton getBtnRemoveProvSystem() {
        return btnRemoveProvSystem;
    }

    public void treeProvUnassignedSysListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeProvUnassignedSys.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeProvUnassignedSys.getRowData();

                    txtSelectedProvSysCode.setValue(nd.getRow().getAttribute("code"));

                }
            }
        }
        GlobalCC.refreshUI(panelProvSystems);
    }

    public void treeProvAssignedSysListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeProvAssignedSys.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeProvAssignedSys.getRowData();

                    txtSelectedProvSysCode.setValue(nd.getRow().getAttribute("code"));

                }
            }
        }
        GlobalCC.refreshUI(panelProvSystems);
    }

    /**
     * Called upon from time to time to display various messages from the
     * server i.e. Completing of successful edit, delete or edit.
     */
    public void showMessagePopup(String msgToDisplay) {
        textToShow.setValue(null);
        textToShow.setValue(msgToDisplay);

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent source = btnCreateUpdateServiceProvider;
        String alignId = source.getClientId(context);
        String popupId = "pt1:ServerMessage";

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("var hints = {}; ").append("hints[AdfRichPopup.HINT_ALIGN_ID] = '").append(alignId).append("'; ").append("hints[AdfRichPopup.HINT_ALIGN] = AdfRichPopup.ALIGN_AFTER_START; ").append("popup.show(hints);}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public void setTextToShow(RichOutputText textToShow) {
        this.textToShow = textToShow;
    }

    public RichOutputText getTextToShow() {
        return textToShow;
    }

    public void setTblServiceProviderType(RichTable tblServiceProviderType) {
        this.tblServiceProviderType = tblServiceProviderType;
    }

    public RichTable getTblServiceProviderType() {
        return tblServiceProviderType;
    }

    public String actionAcceptserviceProvider() {
        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            
            if (nodeBinding.getAttribute("name").equals("INVESTIGATOR") && "Y".equalsIgnoreCase(GlobalCC.getSysParamValue("HIDEN_SPR_INVESTIGATOR_FIELDS")) ) {
                                      txtProviderFax.setVisible(false);
                                      txtProviderZip.setVisible(false);
                                      txtProviderAimsCode.setVisible(false);
                                      txtProviderTrsOccup.setVisible(false);
                                      GlobalCC.refreshUI(txtProviderFax);
                                      GlobalCC.refreshUI(txtProviderZip);
                                      GlobalCC.refreshUI(txtProviderAimsCode);
                                      GlobalCC.refreshUI(txtProviderTrsOccup);
                                  } else {
                                      txtProviderFax.setVisible(true);
                                      txtProviderZip.setVisible(true);
                                      txtProviderAimsCode.setVisible(true);
                                      txtProviderTrsOccup.setVisible(true);
                                      GlobalCC.refreshUI(txtProviderFax);
                                      GlobalCC.refreshUI(txtProviderZip);
                                      GlobalCC.refreshUI(txtProviderAimsCode);
                                      GlobalCC.refreshUI(txtProviderTrsOccup);
                                  }
            
            txtServiceProviderTypeName.setValue(nodeBinding.getAttribute("name"));

            txtServiceProviderTypeCode.setValue(nodeBinding.getAttribute("code"));


            session.setAttribute("serviceProviderTypeCode",
                                 nodeBinding.getAttribute("code"));
            session.setAttribute("serviceProviderType",
                                 nodeBinding.getAttribute("name"));
            session.setAttribute("bactAccountType", "S");
            session.setAttribute("vSptCode", nodeBinding.getAttribute("code"));


            ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();


            GlobalCC.refreshUI(treeServiceProviders);

            GlobalCC.refreshUI(txtServiceProviderTypeName);


            GlobalCC.refreshUI(servActivitiesLov);


        }


        GlobalCC.dismissPopUp("pt1", "serviceProvPop");
        return null;
    }


    public String actionAcceptserviceProvider2() {
        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtServiceProviderTypeName.setValue(nodeBinding.getAttribute("name"));
            txtServiceProviderTypeCode.setValue(nodeBinding.getAttribute("code"));

            // session.setAttribute("serviceProviderTypeCode",nodeBinding.getAttribute("sptCode"));
            //  session.setAttribute("serviceProviderType",
            //                      nodeBinding.getAttribute("name"));
            session.setAttribute("bactAccountType", "S");
            session.setAttribute("vSptCode",
                                 nodeBinding.getAttribute("sptCode"));

            ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(treeServiceProviders);

            GlobalCC.refreshUI(txtServiceProviderTypeName);


            GlobalCC.refreshUI(servActivitiesLov);

        }
        GlobalCC.dismissPopUp("pt1", "serviceProvPop");
        return null;
    }

    public void setTxtServiceProviderTypeCode(RichInputText txtServiceProviderTypeCode) {
        this.txtServiceProviderTypeCode = txtServiceProviderTypeCode;
    }

    public RichInputText getTxtServiceProviderTypeCode() {
        return txtServiceProviderTypeCode;
    }

    public void setTxtServiceProviderTypeName(RichInputText txtServiceProviderTypeName) {
        this.txtServiceProviderTypeName = txtServiceProviderTypeName;
    }

    public RichInputText getTxtServiceProviderTypeName() {
        return txtServiceProviderTypeName;
    }

    public void setServActivitiesLov(RichTable servActivitiesLov) {
        this.servActivitiesLov = servActivitiesLov;
    }

    public RichTable getServActivitiesLov() {
        return servActivitiesLov;
    }

    public void selectedServActivies(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            DCIteratorBinding binder =
                ADFUtils.findIterator("fetchAllServiceProviderTypesActivitiesIterator");
            RowKeySet set = servActivitiesLov.getSelectedRowKeys();
            Iterator row = set.iterator();

            while (row.hasNext()) {

                List data = (List)row.next();
                Key key = (Key)data.get(0);
                binder.setCurrentRowWithKey(key.toStringFormat(true));
                Row r = binder.getCurrentRow();
                txtProvActivityDesc.setValue(r.getAttribute("name"));
                session.setAttribute("servActivityCode",
                                     new BigDecimal(r.getAttribute("code").toString()));
                GlobalCC.refreshUI(txtProvActivityDesc);
            }
        }
    }

    public void setMainDetailTab(RichPanelTabbed mainDetailTab) {
        this.mainDetailTab = mainDetailTab;
    }

    public RichPanelTabbed getMainDetailTab() {
        return mainDetailTab;
    }

    public void setPanelBoxOuter(RichPanelBox panelBoxOuter) {
        this.panelBoxOuter = panelBoxOuter;
    }

    public RichPanelBox getPanelBoxOuter() {
        return panelBoxOuter;
    }

    public void setTxtGLAccount(RichInputText txtGLAccount) {
        this.txtGLAccount = txtGLAccount;
    }

    public RichInputText getTxtGLAccount() {
        return txtGLAccount;
    }

    public String actionShowGlAccounts() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:SpGlAccountPop" + "').show(hints);");
        return null;
    }


    public String actionAcceptGlAccount() {
        Object key2 = tblSpGLAcccount.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtGLAccount.setValue(nodeBinding.getAttribute("accNumber"));
            GlobalCC.refreshUI(txtGLAccount);

        }
        GlobalCC.dismissPopUp("pt1", "SpGlAccountPop");

        return null;
    }


    public void setTblSpGLAcccount(RichTable tblSpGLAcccount) {
        this.tblSpGLAcccount = tblSpGLAcccount;
    }

    public RichTable getTblSpGLAcccount() {
        return tblSpGLAcccount;
    }

    public String actionAcceptBank() {
        Object key = tblBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBankCode.setValue(nodeBinding.getAttribute("bankCode"));
            txtBankName.setValue(nodeBinding.getAttribute("bankName"));
            txtBankBranchCode.setValue(null);
            txtBankBranch.setValue(null);
            GlobalCC.refreshUI(tblBankBranches);
            GlobalCC.refreshUI(txtBankName);
            GlobalCC.refreshUI(txtBankCode);
            GlobalCC.refreshUI(txtBankBranch);
        }

        GlobalCC.dismissPopUp("pt1", "BanksPop");
        return null;
    }

    public String actionAcceptBankBranch() {


        Object key = tblBankBranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBankBranchCode.setValue(nodeBinding.getAttribute("branchCode"));
            txtBankBranch.setValue(nodeBinding.getAttribute("branchName"));
            GlobalCC.refreshUI(txtBankBranchCode);
            GlobalCC.refreshUI(txtBankBranch);
        }
        GlobalCC.dismissPopUp("pt1", "BankBranchesPOP");

        return null;
    }

    public void setTblBanks(RichTable tblBanks) {
        this.tblBanks = tblBanks;
    }

    public RichTable getTblBanks() {
        return tblBanks;
    }

    public void setTxtBankName(RichInputText txtBankName) {
        this.txtBankName = txtBankName;
    }

    public RichInputText getTxtBankName() {
        return txtBankName;
    }

    public void setTxtBankBranchCode(RichInputText txtBankBranchCode) {
        this.txtBankBranchCode = txtBankBranchCode;
    }

    public RichInputText getTxtBankBranchCode() {
        return txtBankBranchCode;
    }

    public void setTxtBankCode(RichInputText txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public RichInputText getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtBankBranch(RichInputText txtBankBranch) {
        this.txtBankBranch = txtBankBranch;
    }

    public RichInputText getTxtBankBranch() {
        return txtBankBranch;
    }

    public void setTblBankBranches(RichTable tblBankBranches) {
        this.tblBankBranches = tblBankBranches;
    }

    public RichTable getTblBankBranches() {
        return tblBankBranches;
    }

    public String actionShowBankBranches() {
        if (txtBankCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("You have not Selected a Bank");
            return null;
        }
        session.setAttribute("bankCode", txtBankCode.getValue());
        ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
        GlobalCC.showPopUp("pt1", "BankBranchesPOP");
        return null;
    }

    public void setTxtInhouse(RichSelectOneChoice txtInhouse) {
        this.txtInhouse = txtInhouse;
    }

    public RichSelectOneChoice getTxtInhouse() {
        return txtInhouse;
    }

    public void setTbl1(RichTable tbl1) {
        this.tbl1 = tbl1;
    }

    public RichTable getTbl1() {
        return tbl1;
    }

    public void setTxtSms(RichInputText txtSms) {
        this.txtSms = txtSms;
    }

    public RichInputText getTxtSms() {
        return txtSms;
    }

    public void setServiceProviderTbl(RichTable serviceProviderTbl) {
        this.serviceProviderTbl = serviceProviderTbl;
    }

    public RichTable getServiceProviderTbl() {
        return serviceProviderTbl;
    }

    public void saveServiceProviders(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            session.setAttribute("fromGisMultiProv", "Y");
            actionCreateNewSpr();

        }
    }

    public void setTxtContactPerson(RichInputText txtContactPerson) {
        this.txtContactPerson = txtContactPerson;
    }

    public RichInputText getTxtContactPerson() {
        return txtContactPerson;
    }

    public void setTxtContPersonPhone(RichInputText txtContPersonPhone) {
        this.txtContPersonPhone = txtContPersonPhone;
    }

    public RichInputText getTxtContPersonPhone() {
        return txtContPersonPhone;
    }

    public void setTxtInvoiceNumber(RichInputText txtInvoiceNumber) {
        this.txtInvoiceNumber = txtInvoiceNumber;
    }

    public RichInputText getTxtInvoiceNumber() {
        return txtInvoiceNumber;
    }

    public void setTxtHoldingCompanies(RichInputText txtHoldingCompanies) {
        this.txtHoldingCompanies = txtHoldingCompanies;
    }

    public RichInputText getTxtHoldingCompanies() {
        return txtHoldingCompanies;
    }

    public String selectHoldingCompanies() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:holdingCompaniesPop" + "').show(hints);");
        return null;
    }

    public String actionSelectedHoldingCompanies() {
        Object key = holdingCompanyTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;

        }
        session.setAttribute("CLNT_CODE", r.getAttribute("CLNT_CODE"));
        txtHoldingCompanies.setValue(r.getAttribute("CLNT_NAME"));
        GlobalCC.refreshUI(txtHoldingCompanies);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:holdingCompaniesPop" + "').hide(hints);");
        return null;
    }

    public void setHoldingCompanyTbl(RichTable holdingCompanyTbl) {
        this.holdingCompanyTbl = holdingCompanyTbl;
    }

    public String actionCreateNewSpr() {

        // Checking only the required fields
        String provShortDesc =
            GlobalCC.checkNullValues(txtProviderShortDesc.getValue());
        String provName = GlobalCC.checkNullValues(txtProviderName.getValue());
        String provPhysicalAddress =
            GlobalCC.checkNullValues(txtProviderPhysicalAddress.getValue());
        String provStatus =
            GlobalCC.checkNullValues(txtProviderStatus.getValue());
        String provGLAccount =
            GlobalCC.checkNullValues(txtGLAccount.getValue());
        String inhouse = GlobalCC.checkNullValues(txtInhouse.getValue());
        //   System.out.println("");
        if (inhouse == null) {
            inhouse = "N";
        }

        /*if (provShortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Short Description");
            return null;

        } */ else if (provName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type the Provider Name");
            return null;

        } else if (provPhysicalAddress == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Physical Address");
            return null;

        } else if (provStatus == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter if Active or not");
            return null;

        } else if (txtProviderWef.getValue() == null ||
                   txtProviderWef.getValue().equals("")) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WEF Date");
            return null;

            /*} else if (txtProviderWet.getValue() == null ||
                 txtProviderWet.getValue().equals("")) {
          GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WET Date");
          return null;*/

        }

        String Query =
            "begin TQC_SETUPS_PKG.service_providers_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            // Take care of all the date fields on the form. Make sure they are not null.
            // If the dates are null, then default to the current date.
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date newWetDate = null; //new Date();
            if (txtProviderWet.getValue() != null &&
                !(txtProviderWet.getValue().equals(""))) {
                String date2 = df.format(txtProviderWet.getValue());
                newWetDate = df.parse(date2);
            }

            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, "A");
            cst.setBigDecimal(2, null);
            cst.setString(3, provShortDesc);
            cst.setString(4, provName);
            cst.setString(5, provPhysicalAddress);
            cst.setString(6, (String)txtProviderPostalAddress.getValue());
            cst.setString(7, (String)txtProviderTownCode.getValue());
            cst.setString(8, (String)txtProviderCountryCode.getValue());
            cst.setString(9, (String)txtProviderType.getValue());
            cst.setString(10, (String)txtProviderPhone.getValue());
            cst.setString(11, (String)txtProviderFax.getValue());
            cst.setString(12, (String)txtProviderEmail.getValue());
            cst.setString(13, (String)txtProviderTitle.getValue());
            cst.setString(14, (String)txtProviderZip.getValue());
            cst.setDate(15,
                        new java.sql.Date(((java.util.Date)txtProviderWef.getValue()).getTime()));
            cst.setDate(16,
                        newWetDate != null ? new java.sql.Date(newWetDate.getTime()) :
                        null);
            /*cst.setDate(16,
                          new java.sql.Date(((java.util.Date)txtProviderWet.getValue()).getTime()));*/
            cst.setString(17, (String)txtProviderContact.getValue());
            cst.setString(18, (String)txtProviderAimsCode.getValue());
            cst.setString(19,
                          GlobalCC.checkNullValues(txtBankBranchCode.getValue()));
            cst.setString(20, (String)txtProviderBankAccountNum.getValue());
            cst.setString(21, (String)session.getAttribute("Username"));
            cst.setDate(22, new java.sql.Date(new Date().getTime()));
            //cst.setDate(22, new java.sql.Date(((java.util.Date)txtProviderDateCreated.getValue()).getTime()));
            cst.setString(23, (String)txtProviderStatusRemarks.getValue());
            cst.setString(24, (String)txtProviderStatus.getValue());
            cst.setString(25, (String)txtProviderPin.getValue());
            cst.setString(26, (String)txtProviderTrsOccup.getValue());
            cst.setString(27, (String)txtProviderProffBody.getValue());
            cst.setString(28, (String)txtProviderPin.getValue());
            cst.setString(29, (String)txtProviderDocPhone.getValue());
            cst.setString(30, (String)txtProviderDocEmail.getValue());
            cst.setString(31, provGLAccount);
            cst.setString(32, inhouse);
            if (txtSms.getValue() == null) {
                cst.setString(33, null);
            } else {
                cst.setString(33, txtSms.getValue().toString());
            }
            cst.setString(34, (String)txtContactPerson.getValue());
            cst.setString(35, (String)txtContPersonPhone.getValue());
            cst.setObject(36, txtInvoiceNumber.getValue());
            cst.setObject(37, session.getAttribute("CLNT_CODE"));
            cst.setObject(38, session.getAttribute("bpnCode"));
            cst.setObject(39, txtContactEmail.getValue());
            cst.setObject(40, txtContactTel.getValue());
            String sms = GlobalCC.checkNullValues(txtTelPay.getValue());

            String prefix =
                GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
            if (txtTelPayPrefix.isVisible()) {
                if (prefix != null) {
                    //                    Row row =
                    //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                    //                    if (row.getAttribute("prefix") != null) {
                    //                        prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }
                    //sms = prefix + "" + sms;
                    if (session.getAttribute("zipCode") == null) {
                        sms = prefix + "" + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                    }
                    // }
                }
            } else {
                if (sms != null) {
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    }
                }
            }
     String defPro=null;
            if(defaultProvider.getValue()!=null){
                    defPro = defaultProvider.getValue().toString();
            }else{defPro=null;}
            cst.setString(41, sms);
           cst.setString(42,defPro );
             cst.setObject(43, null);
            cst.setObject(44, null);
            cst.setObject(45, null);
            cst.setObject(46, null);
            cst.setObject(47, null);
            cst.execute();

            ADFUtils.findIterator("fetchAllServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(treeServiceProviders);

            cst.close();
            conn.close();

            String message = "Service Provider CREATED successfully!";
            showMessagePopup(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return "Success";
    }

    public RichTable getHoldingCompanyTbl() {
        return holdingCompanyTbl;
    }

    public void setTxtContact(RichInputText txtContact) {
        this.txtContact = txtContact;
    }

    public RichInputText getTxtContact() {
        return txtContact;
    }

    public void setTxtContactMobile(RichInputText txtContactMobile) {
        this.txtContactMobile = txtContactMobile;
    }

    public RichInputText getTxtContactMobile() {
        return txtContactMobile;
    }

    public void setTxtContactEmail(RichInputText txtContactEmail) {
        this.txtContactEmail = txtContactEmail;
    }

    public RichInputText getTxtContactEmail() {
        return txtContactEmail;
    }

    public void setTxtContactTel(RichInputText txtContactTel) {
        this.txtContactTel = txtContactTel;
    }

    public RichInputText getTxtContactTel() {
        return txtContactTel;
    }

    public void setTxtBussinessPersons(RichInputText txtBussinessPersons) {
        this.txtBussinessPersons = txtBussinessPersons;
    }

    public RichInputText getTxtBussinessPersons() {
        return txtBussinessPersons;
    }

    public String selectBussinessPersons() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:bussinessPersonPop" + "').show(hints);");

        // Add event code here...
        return null;
    }

    public void setBussinessPersonTbl(RichTable bussinessPersonTbl) {
        this.bussinessPersonTbl = bussinessPersonTbl;
    }

    public RichTable getBussinessPersonTbl() {
        return bussinessPersonTbl;
    }

    public String selectBussinessPerson() {
        Object key = bussinessPersonTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;

        }
        session.setAttribute("bpnCode", r.getAttribute("bpnCode"));
        txtBussinessPersons.setValue(r.getAttribute("bpnName"));
        GlobalCC.refreshUI(txtBussinessPersons);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:bussinessPersonPop" + "').hide(hints);");

        return null;
    }

    public void setTxtTelPayPrefix(RichInputText txtTelPayPrefix) {
        this.txtTelPayPrefix = txtTelPayPrefix;
    }

    public RichInputText getTxtTelPayPrefix() {
        return txtTelPayPrefix;
    }

    public void setPrefixTbl(RichTable prefixTbl) {
        this.prefixTbl = prefixTbl;
    }

    public RichTable getPrefixTbl() {
        return prefixTbl;
    }

    public String selectPrefix() {
        Object key = prefixTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        txtTelPayPrefix.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtTelPayPrefix);
        GlobalCC.hidePopup("pt1:prefixPOP");
        return null;
    }

    public String selectSmsPrefix() {
        Object key = smsPrefixTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        txtSmsPrefix.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtSmsPrefix);
        GlobalCC.hidePopup("pt1:SmsPrefixPOP");
        return null;
    }

    public void setTxtTelPay(RichInputText txtTelPay) {
        this.txtTelPay = txtTelPay;
    }

    public RichInputText getTxtTelPay() {
        return txtTelPay;
    }


    public void setDefaultProvider(RichSelectOneChoice defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public RichSelectOneChoice getDefaultProvider() {
        return defaultProvider;
    }

    public void setTxtSmsPrefix(RichInputText txtSmsPrefix) {
        this.txtSmsPrefix = txtSmsPrefix;
    }

    public RichInputText getTxtSmsPrefix() {
        return txtSmsPrefix;
    }

    public void setSmsPrefixTbl(RichTable smsPrefixTbl) {
        this.smsPrefixTbl = smsPrefixTbl;
    }

    public RichTable getSmsPrefixTbl() {
        return smsPrefixTbl;
    }

    public String newBankDetailsAction() {
        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        } else {
            txtBnkAccOfficer.setValue(null);
            txtBnkBrn.setValue(null);
            txtBnkAccName.setValue(null);
            txtBnkAccNumber.setValue(null);
            txtBnkCellNo.setValue(null);
            txtBnkTelNo.setValue(null);
            txtBnkCurrency.setValue(null);
            bnkCurrencyDesc.setValue(null);
            txtBnkBrnCode.setValue(null);
            session.removeAttribute("bactUserCode");
            txtBnkDefault.setValue("N");
            txtBnkStatus.setValue("A");
            txtBnkIBAN.setValue(null);
            saveBankDetailsBtn.setText("Save");
            GlobalCC.showPop("pt1:bankdetailsPop");
            GlobalCC.refreshUI(bankdetailsPopUp);
        }
        return null;
    }

    public String editBankDetailsAction() {
        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        } else {
            Object key2 = tblBnkDetails.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                saveBankDetailsBtn.setText("Update");
                txtBnkAccOfficer.setValue(nodeBinding.getAttribute("BACT_ACCOUNT_OFFICER"));
                txtBnkBrn.setValue(nodeBinding.getAttribute("BACT_BANK_BRANCH"));
                txtBnkAccName.setValue(nodeBinding.getAttribute("BACT_NAME"));
                txtBnkAccNumber.setValue(nodeBinding.getAttribute("BACT_ACCOUNT_NO"));
                txtBnkCellNo.setValue(nodeBinding.getAttribute("BACT_CELL_NOS"));
                txtBnkTelNo.setValue(nodeBinding.getAttribute("BACT_TEL_NOS"));
                txtBnkDefault.setValue(nodeBinding.getAttribute("BACT_DEFAULT"));
                txtBnkCurrency.setValue(nodeBinding.getAttribute("BACT_CUR_CODE"));
                bnkCurrencyDesc.setValue(nodeBinding.getAttribute("BACT_CURRENCY"));
                txtBnkBrnCode.setValue(nodeBinding.getAttribute("BACT_BBR_CODE"));
                session.setAttribute("bactUserCode",
                                     nodeBinding.getAttribute("BACT_USER_CODE"));
                txtBnkIBAN.setValue(nodeBinding.getAttribute("BACT_IBAN"));
                txtBnkStatus.setValue(nodeBinding.getAttribute("BACT_STATUS"));

                GlobalCC.showPop("pt1:bankdetailsPop");
                GlobalCC.refreshUI(bankdetailsPopUp);

            }
        }
        return null;
    }

    public String deleteBankDetailsAction() {
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;

        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        }
        Object key2 = tblBnkDetails.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String v_bact_code =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("BACT_CODE"));
            DBConnector dbConnector = new DBConnector();
            conn = dbConnector.getDatabaseConnection();
            try {
                DBConnector connection = new DBConnector();
                conn = connection.getDatabaseConnection();
                query =
                        "begin tqc_setups_pkg.bank_details_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "D");
                stmt.setString(2, v_bact_code);
                stmt.setString(3, null);
                stmt.setString(4, null);
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setString(7, null);
                stmt.setString(8, null);
                stmt.setString(9, null);
                stmt.setString(10, null);
                stmt.setString(11, null);
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.execute();

                stmt.close();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Bank Details Successfully Deleted!");
                ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblBnkDetails);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected!");
        }

        return null;
    }

    public String saveBankDetailsAction() {
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;
        String bnkBrn = GlobalCC.checkNullValues(txtBnkBrnCode.getValue());
        String bnkAccOfficer =
            GlobalCC.checkNullValues(session.getAttribute("bactUserCode"));
        String bnkAccNumber =
            GlobalCC.checkNullValues(txtBnkAccNumber.getValue());
        String bnkAccName = GlobalCC.checkNullValues(txtBnkAccName.getValue());
        String bnkTelNo = GlobalCC.checkNullValues(txtBnkTelNo.getValue());
        String bnkCellNo = GlobalCC.checkNullValues(txtBnkCellNo.getValue());
        String bnkCurrency =
            GlobalCC.checkNullValues(txtBnkCurrency.getValue());
        String bnkDefault = GlobalCC.checkNullValues(txtBnkDefault.getValue());
        String bactAccountType =
            GlobalCC.checkNullValues(session.getAttribute("bactAccountType"));
        String bactAccCode =
            GlobalCC.checkNullValues(session.getAttribute("bactAccCode"));
        String bnkIBAN = GlobalCC.checkNullValues(txtBnkIBAN.getValue());
        String bnkStatus = GlobalCC.checkNullValues(txtBnkStatus.getValue());
        String v_bact_code = null;
        String option = null;
        Rendering renderer = new Rendering();

        if (renderer.isSP_IBAN_REQUIRED() && bnkIBAN == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: IBAN is required!");
            return null;
        }

        if (saveBankDetailsBtn.getText().equalsIgnoreCase("update")) {
            option = "E";
            Object key2 = tblBnkDetails.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            v_bact_code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("BACT_CODE"));
        } else if (saveBankDetailsBtn.getText().equalsIgnoreCase("Save")) {
            option = "A";
        }
        FormUi formUi=new FormUi();
        if(!formUi.validate("sPBankTab")){//main validation engine
                                          return null;
                                          }
        try {
            
            DBConnector connection = new DBConnector();
            conn = connection.getDatabaseConnection();
            query =
                    "begin tqc_setups_pkg.bank_details_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, option);
            stmt.setString(2, v_bact_code);
            stmt.setString(3, bnkAccName);
            stmt.setString(4, bnkAccNumber);
            stmt.setString(5, bnkBrn);
            stmt.setString(6, bnkAccOfficer);
            stmt.setString(7, bnkCellNo);
            stmt.setString(8, bnkTelNo);
            stmt.setString(9, bactAccountType);
            stmt.setString(10, bnkDefault);
            stmt.setString(11, bnkCurrency);
            stmt.setString(12, bactAccCode);
            stmt.setString(13, bnkIBAN);
            stmt.setString(14, bnkStatus);
            stmt.execute();

            stmt.close();
            conn.close();
            GlobalCC.INFORMATIONREPORTING("Bank Details Successfully Created!");
            GlobalCC.hidePopup("pt1:bankdetailsPop");
            ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();
            GlobalCC.refreshUI(tblBnkDetails);
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }
        return null;
    }

    public String actionAcceptCurrency() {
        Object key2 = tblCurrencyPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkCurrency.setValue(nodeBinding.getAttribute("code"));
            bnkCurrencyDesc.setValue(nodeBinding.getAttribute("description"));
        }

        GlobalCC.refreshUI(txtBnkCurrency);
        GlobalCC.refreshUI(bnkCurrencyDesc);
        GlobalCC.hidePopup("pt1:currenciesPop");

        return null;
    }
    

    public String bankBranchSelected() {
        
        
        Object key2 = tblBankBranchPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkBrn.setValue(nodeBinding.getAttribute("branchName"));
            txtBnkBrnCode.setValue(nodeBinding.getAttribute("bankBranchCode"));
            GlobalCC.refreshUI(txtBnkBrnCode);
            GlobalCC.refreshUI(txtBnkBrn);
        }
        GlobalCC.hidePopup("pt1:bankBranchPop");

        return null;
    }
    
    
    public String titleSelected() {
        Object key2 = tblProviderTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtProvTitle.setValue(nodeBinding.getAttribute("shortDesc"));           
            GlobalCC.refreshUI(txtProvTitle);           
        }
        GlobalCC.hidePopup("pt1:titlePop");

        return null;
    }


    public String actionAcceptAccountOfficer() {


        Object key2 = tblAccountOfficer.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            txtBnkAccOfficer.setValue(nodeBinding.getAttribute("username"));
            session.setAttribute("bactUserCode",
                                 nodeBinding.getAttribute("userCode"));
            GlobalCC.refreshUI(txtBnkAccOfficer);
        }
        GlobalCC.hidePopup("pt1:accountOfficersPop");
        return null;
    }

    public void setTblBnkDetails(RichTable tblBnkDetails) {
        this.tblBnkDetails = tblBnkDetails;
    }

    public RichTable getTblBnkDetails() {
        return tblBnkDetails;
    }

    public void setBankdetailsPopUp(RichPopup bankdetailsPopUp) {
        this.bankdetailsPopUp = bankdetailsPopUp;
    }

    public RichPopup getBankdetailsPopUp() {
        return bankdetailsPopUp;
    }

    public void setTxtBnkBrn(RichInputText txtBnkBrn) {
        this.txtBnkBrn = txtBnkBrn;
    }

    public RichInputText getTxtBnkBrn() {
        return txtBnkBrn;
    }

    public void setTxtBnkAccOfficer(RichInputText txtBnkAccOfficer) {
        this.txtBnkAccOfficer = txtBnkAccOfficer;
    }

    public RichInputText getTxtBnkAccOfficer() {
        return txtBnkAccOfficer;
    }

    public void setTxtBnkAccNumber(RichInputText txtBnkAccNumber) {
        this.txtBnkAccNumber = txtBnkAccNumber;
    }

    public RichInputText getTxtBnkAccNumber() {
        return txtBnkAccNumber;
    }

    public void setTxtBnkAccName(RichInputText txtBnkAccName) {
        this.txtBnkAccName = txtBnkAccName;
    }

    public RichInputText getTxtBnkAccName() {
        return txtBnkAccName;
    }

    public void setTxtBnkTelNo(RichInputText txtBnkTelNo) {
        this.txtBnkTelNo = txtBnkTelNo;
    }

    public RichInputText getTxtBnkTelNo() {
        return txtBnkTelNo;
    }

    public void setTxtBnkCellNo(RichInputText txtBnkCellNo) {
        this.txtBnkCellNo = txtBnkCellNo;
    }

    public RichInputText getTxtBnkCellNo() {
        return txtBnkCellNo;
    }

    public void setTxtBnkCurrency(RichInputText txtBnkCurrency) {
        this.txtBnkCurrency = txtBnkCurrency;
    }

    public RichInputText getTxtBnkCurrency() {
        return txtBnkCurrency;
    }

    public void setTxtBnkDefault(RichSelectOneChoice txtBnkDefault) {
        this.txtBnkDefault = txtBnkDefault;
    }

    public RichSelectOneChoice getTxtBnkDefault() {
        return txtBnkDefault;
    }

    public void setSaveBankDetailsBtn(RichCommandButton saveBankDetailsBtn) {
        this.saveBankDetailsBtn = saveBankDetailsBtn;
    }

    public RichCommandButton getSaveBankDetailsBtn() {
        return saveBankDetailsBtn;
    }

    public void setTblCurrencyPop(RichTable tblCurrencyPop) {
        this.tblCurrencyPop = tblCurrencyPop;
    }

    public RichTable getTblCurrencyPop() {
        return tblCurrencyPop;
    }

    public void setTblBankBranchPop(RichTable tblBankBranchPop) {
        this.tblBankBranchPop = tblBankBranchPop;
    }

    public RichTable getTblBankBranchPop() {
        return tblBankBranchPop;
    }

    public void setTblAccountOfficer(RichTable tblAccountOfficer) {
        this.tblAccountOfficer = tblAccountOfficer;
    }

    public RichTable getTblAccountOfficer() {
        return tblAccountOfficer;
    }

    public void setTxtBnkBrnCode(RichInputText txtBnkBrnCode) {
        this.txtBnkBrnCode = txtBnkBrnCode;
    }

    public RichInputText getTxtBnkBrnCode() {
        return txtBnkBrnCode;
    }

    public void setBnkCurrencyDesc(RichInputText bnkCurrencyDesc) {
        this.bnkCurrencyDesc = bnkCurrencyDesc;
    }

    public RichInputText getBnkCurrencyDesc() {
        return bnkCurrencyDesc;
    }

    public void setBankBranchTbl(RichTable bankBranchTbl) {
        this.bankBranchTbl = bankBranchTbl;
    }

    public RichTable getBankBranchTbl() {
        return bankBranchTbl;
    }

    public void setTxtBnkIBAN(RichInputText txtBnkIBAN) {
        this.txtBnkIBAN = txtBnkIBAN;
    }

    public RichInputText getTxtBnkIBAN() {
        return txtBnkIBAN;
    }

    public void setTxtBnkStatus(RichSelectOneChoice txtBnkStatus) {
        this.txtBnkStatus = txtBnkStatus;
    }

    public RichSelectOneChoice getTxtBnkStatus() {
        return txtBnkStatus;
    }

    public void setTblReqDocsList(RichTable tblReqDocsList) {
        this.tblReqDocsList = tblReqDocsList;
    }

    public RichTable getTblReqDocsList() {
        return tblReqDocsList;
    }

    public void setTxtDocCodePop(RichInputText txtDocCodePop) {
        this.txtDocCodePop = txtDocCodePop;
    }

    public RichInputText getTxtDocCodePop() {
        return txtDocCodePop;
    }

    public void setTxtReqDocCodePop(RichInputText txtReqDocCodePop) {
        this.txtReqDocCodePop = txtReqDocCodePop;
    }

    public RichInputText getTxtReqDocCodePop() {
        return txtReqDocCodePop;
    }

    public void setTxtReqDocNamePop(RichInputText txtReqDocNamePop) {
        this.txtReqDocNamePop = txtReqDocNamePop;
    }

    public RichInputText getTxtReqDocNamePop() {
        return txtReqDocNamePop;
    }

    public void setTxtDocAgnCodePop(RichInputText txtDocAgnCodePop) {
        this.txtDocAgnCodePop = txtDocAgnCodePop;
    }

    public RichInputText getTxtDocAgnCodePop() {
        return txtDocAgnCodePop;
    }

    public void setTxtDocSubmittedPop(RichSelectOneChoice txtDocSubmittedPop) {
        this.txtDocSubmittedPop = txtDocSubmittedPop;
    }

    public RichSelectOneChoice getTxtDocSubmittedPop() {
        return txtDocSubmittedPop;
    }

    public void setTxtDocDateSubmittedPop(RichInputDate txtDocDateSubmittedPop) {
        this.txtDocDateSubmittedPop = txtDocDateSubmittedPop;
    }

    public RichInputDate getTxtDocDateSubmittedPop() {
        return txtDocDateSubmittedPop;
    }

    public void setTxtDocRefNumPop(RichInputText txtDocRefNumPop) {
        this.txtDocRefNumPop = txtDocRefNumPop;
    }

    public RichInputText getTxtDocRefNumPop() {
        return txtDocRefNumPop;
    }

    public void setTxtDocRemarkPop(RichInputText txtDocRemarkPop) {
        this.txtDocRemarkPop = txtDocRemarkPop;
    }

    public RichInputText getTxtDocRemarkPop() {
        return txtDocRemarkPop;
    }

    public void setTxtDocUserReceivedPop(RichInputText txtDocUserReceivedPop) {
        this.txtDocUserReceivedPop = txtDocUserReceivedPop;
    }

    public RichInputText getTxtDocUserReceivedPop() {
        return txtDocUserReceivedPop;
    }

    public void setBtnSaveRequiredDoc(RichCommandButton btnSaveRequiredDoc) {
        this.btnSaveRequiredDoc = btnSaveRequiredDoc;
    }

    public RichCommandButton getBtnSaveRequiredDoc() {
        return btnSaveRequiredDoc;
    }

    public void setAgentRecDocstab(RichShowDetailItem agentRecDocstab) {
        this.agentRecDocstab = agentRecDocstab;
    }

    public RichShowDetailItem getAgentRecDocstab() {
        return agentRecDocstab;
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
        ServiceProviderBacking.fileContent = fileContent;
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

    public String actionNewRequiredDoc() {

        if (session.getAttribute("serviceProviderCode") != null) {
            txtDocCodePop.setValue(null);
            txtReqDocCodePop.setValue(null);
            txtReqDocNamePop.setValue(null);
            txtDocAgnCodePop.setValue(session.getAttribute("serviceProviderCode"));

            session.setAttribute("spDocSubmitted", "N");
            session.setAttribute("spDocId", null);

            txtDocDateSubmittedPop.setValue(null);
            txtDocRefNumPop.setValue(null);
            txtDocRemarkPop.setValue(null);
            txtDocUserReceivedPop.setValue(session.getAttribute("Username"));

            btnSaveRequiredDoc.setText("Save");

            // Open the popup dialog for required documents
            GlobalCC.showPopup("pt1:requiredDocsPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Agent Selected:");
        }
        return null;
    }


    public String actionNewServProCont() {

       String val = null;
        if (session.getAttribute("serviceProviderCode") == null) {
        
        
            GlobalCC.errorValueNotEntered("Error : Select Service Provider First!");

          
            
        }else{
                val = session.getAttribute("serviceProviderCode").toString();
                 
                 btnCreateUpdateProviderContacts.setText("Save");
                txtProvContacCode.setValue(val);
                txtProvEmail.setDisabled(false);
                  ExtendedRenderKitService erkService =
                      Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                         ExtendedRenderKitService.class);
                  erkService.addScript(FacesContext.getCurrentInstance(),
                                       "var hints = {autodismissNever:false}; " +
                                       "AdfPage.PAGE.findComponent('" +
                                       "pt1:providerContactPop').show(hints);");
            }
        return null;
    }

    public String actionEditServProCont() {
        String val = null;
         if (session.getAttribute("serviceProviderCode") == null) {
         
         
             GlobalCC.errorValueNotEntered("Error : Select Service Provider First!");

           
             
         }else{
             
                 val = session.getAttribute("serviceProviderCode").toString();
             
                
                
                  
                 Object key2 = tblProvContDetails.getSelectedRowData();
                 JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

                 if (nodeBinding != null) {
                  btnCreateUpdateProviderContacts.setText("Update");                 
                  txtShortDesc.setValue(nodeBinding.getAttribute("provCode"));
                 // session.setAttribute("shortDescc",nodeBinding.getAttribute("provCode"));
                  txtProvName.setValue(nodeBinding.getAttribute("provName"));
                  txtProvTitle.setValue(nodeBinding.getAttribute("provTitle"));
               
                  txtProvOfficeTelNo.setValue(nodeBinding.getAttribute("provOfficeTelNo"));    
                  txtProvMobileNo.setValue(nodeBinding.getAttribute("provMobileNo"));
                  txtProvEmail.setValue(nodeBinding.getAttribute("provEmail"));
                
                   //  txtProvEmailLabel.setVisible(false);
                  
                GlobalCC.refreshUI(txtProvEmail);
                     GlobalCC.refreshUI(txtProvMobileNo);
                     GlobalCC.refreshUI(txtProvOfficeTelNo);
                     GlobalCC.refreshUI(txtProvTitle);
                     GlobalCC.refreshUI(txtProvName);
                            
                   ExtendedRenderKitService erkService =
                       Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                          ExtendedRenderKitService.class);
                   erkService.addScript(FacesContext.getCurrentInstance(),
                                        "var hints = {autodismissNever:false}; " +
                                        "AdfPage.PAGE.findComponent('" +
                                        "pt1:providerContactPop').show(hints);");
                 }else{
                         GlobalCC.errorValueNotEntered("Error : Select Record To Edit!");
                     }
             }
         return null;
    }

  

    public String actionEditRequiredDoc() {
        Object key2 = spRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_CODE"));
            txtReqDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_RDOC_CODE"));
            txtReqDocNamePop.setValue(nodeBinding.getAttribute("ROC_DESC"));
            txtDocAgnCodePop.setValue(nodeBinding.getAttribute("CDOCR_CLNT_CODE"));
            txtDocSubmittedPop.setValue(nodeBinding.getAttribute("CDOCR_SUBMITED"));

            session.setAttribute("spDocSubmitted",
                                 nodeBinding.getAttribute("CDOCR_SUBMITED"));
            session.setAttribute("spDocId",
                                 nodeBinding.getAttribute("CDOCR_DOCID"));

            txtDocDateSubmittedPop.setValue(nodeBinding.getAttribute("CDOCR_DATE_S"));
            txtDocRefNumPop.setValue(nodeBinding.getAttribute("CDOCR_REF_NO"));
            txtDocRemarkPop.setValue(nodeBinding.getAttribute("CDOCR_RMRK"));
            txtDocUserReceivedPop.setValue(nodeBinding.getAttribute("CDOCR_USER_RECEIVD"));

            btnSaveRequiredDoc.setText("Edit");

            // Open the popup dialog for required documents
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:requiredDocsPop" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actiondeleteRequiredDoc() {
        Object key2 = spRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            GlobalCC.showPop("pt1:confirmDeleteSpRequiredDocs");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }

        return null;
    }

    public String actionAcceptRequireddDoc() {
        Object key2 = tblReqDocsList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtReqDocCodePop.setValue(nodeBinding.getAttribute("code"));
            txtReqDocNamePop.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtReqDocNamePop);
            // Open the popup dialog for required documents
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:requiredDocsPop" + "').show(hints);");
        }
        GlobalCC.dismissPopUp("pt1", "requiredDocsListPop");
        return null;
    }

    public String actionRejectRequiredDoc() {
        // Open the popup dialog for required documents
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:requiredDocsPop" + "').show(hints);");
        return null;
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
                CSVManip.uploadSpDocuments(uploadedFile.getInputStream(),
                                           _file.getFilename(), mimeType);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            GlobalCC.refreshUI(requiredDocsBox);
            GlobalCC.showPop("pt1:requiredDocsPop");
        }
    }


    public void providerTypeChangeListener(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            if (valueChangeEvent.getNewValue().equals("DOCTOR")) {
                txtPostalCode.setVisible(true);
                GlobalCC.refreshUI(txtPostalCode);
            } else {
                txtPostalCode.setVisible(false);
                GlobalCC.refreshUI(txtPostalCode);
            }
        }

    }

    public String actionSaveRequiredDoc() {
        // Check if the user wishes to SAVE or UPDATE
        String action = "A";
        if (btnSaveRequiredDoc.getText().equals("Edit")) {
            action = "E";
        } else {
            action = "A";
        }
        BigDecimal docCode =
            GlobalCC.checkBDNullValues(txtDocCodePop.getValue());
        String docReqDocCode =
            GlobalCC.checkNullValues(txtReqDocCodePop.getValue());
        String docSpCode =
            GlobalCC.checkNullValues(session.getAttribute("serviceProviderCode"));
        String docSubmitted =
            GlobalCC.checkNullValues(session.getAttribute("spDocSubmitted"));
        String docDateSubmit =
            GlobalCC.checkNullValues(txtDocDateSubmittedPop.getValue());
        String docRefNum =
            GlobalCC.checkNullValues(txtDocRefNumPop.getValue());
        String docRemark =
            GlobalCC.checkNullValues(txtDocRemarkPop.getValue());

        if (docReqDocCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Required Document is Empty!");
            return null;

        }
        if (docSpCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: You need to select a Service Provider!");
            return null;
        }
        if (docDateSubmit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Date Submitted!");
            return null;
        }
        String Query =
            "begin TQC_WEB_PKG.save_sp_docs(?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {

            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, action);
            cst.setBigDecimal(2, docCode);
            cst.setString(3, docReqDocCode);
            cst.setString(4, docSpCode);
            cst.setString(5, docSubmitted);
            cst.setDate(6, GlobalCC.extractDate(txtDocDateSubmittedPop));
            cst.setString(7, docRefNum);
            cst.setString(8, docRemark);
            cst.setString(9, (String)session.getAttribute("Username"));
            cst.setString(10, (String)session.getAttribute("spDocId"));
            cst.registerOutParameter(11, OracleTypes.VARCHAR);
            cst.registerOutParameter(12, OracleTypes.VARCHAR);


            cst.execute();
            String status = GlobalCC.checkNullValues(cst.getString(11));
            String msg = GlobalCC.checkNullValues(cst.getString(12));
            cst.close();
            conn.close();
            if (!"S".equals(status)) {
                GlobalCC.errorValueNotEntered(msg);
                return null;
            }
            ADFUtils.findIterator("fetchSpRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(spRecDocsLov);
            GlobalCC.dismissPopUp("pt1", "requiredDocsPop");
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String actionEditAgentRequiredDoc() {
        actionSaveRequiredDoc();
        return null;
    }

    public String saveServiceProvRecDocs() {
        actionSaveRequiredDoc();
        return null;
    }

    public void setSpRecDocsLov(RichTable spRecDocsLov) {
        this.spRecDocsLov = spRecDocsLov;
    }

    public RichTable getSpRecDocsLov() {
        return spRecDocsLov;
    }

    public String viewDoc() {
        Object key2 = spRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        String id = (String)r.getAttribute("CDOCR_DOCID");
        String[] ids = id.split(";");
        String finalId = "";
        if (ids.length > 1) {
            finalId = ids[0];
        } else
            finalId = id;

        if (!new Rendering().isAnnotatedDoc()) {
            try {
                ServletContext servletContext =
                    (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();

                HttpServletRequest request =
                    (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String urlss =
                    request.getScheme() + "://" + request.getServerName() +
                    ":" + request.getServerPort() +
                    servletContext.getContextPath();
                java.lang.System.out.println("Url " + urlss);
                String finalUrl = urlss + "/viewer.html?";

                CmisObject object =
                    new CSVtoADFTableProcessor().getCmisSession().getObject(finalId);
                Document document = (Document)object;
                String filename = document.getName();
                InputStream inputStream =
                    document.getContentStream().getStream();
                String file = "/Reports/" + filename;
                String filerpt = "/Reports/" + filename;

                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext sc =
                    (ServletContext)context.getExternalContext().getContext();
                file = sc.getRealPath(file);
                File toUpload = new File(file);
                OutputStream out;

                out = new FileOutputStream(toUpload);
                int read = 0;
                byte[] bytes = new byte[inputStream.available()];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
                finalUrl = finalUrl + "file=" + urlss + filerpt;
                session.setAttribute("toPrint", finalUrl);
                java.lang.System.out.println(finalUrl);

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:p201" + "').show(hints);");

            } catch (Exception e) {

            }

        } else {

            Connection conn = null;
            String url = "";
            String user = "";
            String pass = "";
            DBConnector datahandler = DBConnector.getInstance();
            conn = datahandler.getDatabaseConnection();
            CallableStatement cstSections = null;
            ResultSet sectionsRS = null;
            try {
                cstSections =
                        conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
                cstSections.registerOutParameter(1,
                                                 oracle.jdbc.OracleTypes.CURSOR);
                cstSections.setInt(2, 37);
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
                    user + "&password=" + pass +
                    "&docbase=Company Home&docId=" + finalId;
                java.lang.System.out.println(toPrint);
                session.setAttribute("toPrint", toPrint);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:p201" + "').show(hints);");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(conn, cstSections, sectionsRS);
            }
        }
        return null;

    }
    /*public void actionConfirmDeleteRequiredDocs(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            Object key2 = spRecDocsLov.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            String Query =
                "begin TQC_WEB_PKG.saveClientDocuments(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {

                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(nodeBinding.getAttribute("CDOCR_CODE").toString()));
                cst.setBigDecimal(3,
                                  new BigDecimal(nodeBinding.getAttribute("CDOCR_RDOC_CODE").toString()));
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setDate(6, null);
                cst.setString(7, null);
                cst.setString(8, null);
                cst.setString(9, session.getAttribute("Username").toString());

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

            ADFUtils.findIterator("fetchSpRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(spRecDocsLov);
        }

    }*/

    public void actionConfirmDeleteRequiredDocs(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            Object key2 = spRecDocsLov.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            String Query =
                "begin TQC_WEB_PKG.save_sp_docs(?,?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;
            BigDecimal docCode =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("CDOCR_CODE"));
            try {

                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2, docCode);
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setDate(6, null);
                cst.setString(7, null);
                cst.setString(8, null);
                cst.setString(9, null);
                cst.setString(10, null);
                cst.registerOutParameter(11, OracleTypes.VARCHAR);
                cst.registerOutParameter(12, OracleTypes.VARCHAR);


                cst.execute();
                String status = GlobalCC.checkNullValues(cst.getString(11));
                String msg = GlobalCC.checkNullValues(cst.getString(12));
                conn.commit();
                cst.close();
                conn.close();
                if (!"S".equals(status)) {
                    GlobalCC.errorValueNotEntered(msg);
                    return;
                }
                ADFUtils.findIterator("fetchSpRequiredDocsIterator").executeQuery();
                GlobalCC.refreshUI(spRecDocsLov);
                GlobalCC.dismissPopUp("pt1", "confirmDeleteSpRequiredDocs");
                GlobalCC.INFORMATIONREPORTING(msg);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

            ADFUtils.findIterator("fetchSpRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(spRecDocsLov);


        }

    }

    public void setRequiredDocsBox(RichPanelBox requiredDocsBox) {
        this.requiredDocsBox = requiredDocsBox;
    }

    public RichPanelBox getRequiredDocsBox() {
        return requiredDocsBox;
    }

    public void setTxtPostalCode(RichInputText txtPostalCode) {
        this.txtPostalCode = txtPostalCode;
    }

    public RichInputText getTxtPostalCode() {
        return txtPostalCode;
    }

    public void setTxtRegNo(RichInputText txtRegNo) {
        this.txtRegNo = txtRegNo;
    }

    public RichInputText getTxtRegNo() {
        return txtRegNo;
    }


    public void setServiceProviderContactTab(RichShowDetailItem serviceProviderContactTab) {
        this.serviceProviderContactTab = serviceProviderContactTab;
    }

    public RichShowDetailItem getServiceProviderContactTab() {
        return serviceProviderContactTab;
    }

    public void setBtnCreateUpdateProviderContacts(RichCommandButton btnCreateUpdateProviderContacts) {
        this.btnCreateUpdateProviderContacts = btnCreateUpdateProviderContacts;
    }

    public RichCommandButton getBtnCreateUpdateProviderContacts() {
        return btnCreateUpdateProviderContacts;
    }

    public void setTxtProvContacCode(RichInputText txtProvContacCode) {
        this.txtProvContacCode = txtProvContacCode;
    }

    public RichInputText getTxtProvContacCode() {
        return txtProvContacCode;
    }

    public void setTxtProvName(RichInputText txtProvName) {
        this.txtProvName = txtProvName;
    }

    public RichInputText getTxtProvName() {
        return txtProvName;
    }

 

    public void setTxtProvOfficeTelNo(RichInputText txtProvOfficeTelNo) {
        this.txtProvOfficeTelNo = txtProvOfficeTelNo;
    }

    public RichInputText getTxtProvOfficeTelNo() {
        return txtProvOfficeTelNo;
    }

    public void setTxtProvMobileNo(RichInputText txtProvMobileNo) {
        this.txtProvMobileNo = txtProvMobileNo;
    }

    public RichInputText getTxtProvMobileNo() {
        return txtProvMobileNo;
    }

    public void setTxtProvEmail(RichInputText txtProvEmail) {
        this.txtProvEmail = txtProvEmail;
    }

    public RichInputText getTxtProvEmail() {
        return txtProvEmail;
    }


    public void setTxtProvTitle(RichInputText txtProvTitle) {
        this.txtProvTitle = txtProvTitle;
    }

    public RichInputText getTxtProvTitle() {
        return txtProvTitle;
    }


    public void setTblProviderTitles(RichTable tblProviderTitles) {
        this.tblProviderTitles = tblProviderTitles;
    }

    public RichTable getTblProviderTitles() {
        return tblProviderTitles;
    }

    public void setTblProvContDetails(RichTable tblProvContDetails) {
        this.tblProvContDetails = tblProvContDetails;
    }

    public RichTable getTblProvContDetails() {
        return tblProvContDetails;
    }

    public void setTxtProvEmailLabel(RichOutputText txtProvEmailLabel) {
        this.txtProvEmailLabel = txtProvEmailLabel;
    }

    public RichOutputText getTxtProvEmailLabel() {
        return txtProvEmailLabel;
    }

    public void setTxtShortDesc(RichInputText txtShortDesc) {
        this.txtShortDesc = txtShortDesc;
    }

    public RichInputText getTxtShortDesc() {
        return txtShortDesc;
    }

    public void setTxtIdType(RichSelectOneChoice txtIdType) {
        this.txtIdType = txtIdType;
    }

    public RichSelectOneChoice getTxtIdType() {
        return txtIdType;
    }

    public void setTxtIdNo(RichInputText txtIdNo) {
        this.txtIdNo = txtIdNo;
    }

    public RichInputText getTxtIdNo() {
        return txtIdNo;
    }
    public void enterSPId(ValueChangeEvent valueChangeEvent) {  
        fetchWebDetails(); 
    } 
    
    public String fetchWebDetails(){

        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        String id = GlobalCC.checkNullValues(txtIdNo.getValue());
        String type = GlobalCC.checkNullValues(txtIdType.getValue());
        String idno = null;
        String passport = null; 
        if( id != null && type!=null && renderer.isIprsApp()){
            if("Id/Registration No".equals(type)){
                idno = id;
            }
            if("Passport No".equals(type)){
                passport = id;
            }
            session.setAttribute("Iprs_Account_Type","SP"); 
            session.setAttribute("tq_passport",null); 
            session.setAttribute("tq_idno",null); 
            
            if(idno != null  ){
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(idno);
                if(idno.length()>=7 && matcher.matches()){ 
                     session.setAttribute("tq_idno",idno);   
                }
            }
            if(passport!=null ){
                passport = passport.toUpperCase();
                Pattern pattern = Pattern.compile("[A-Z]{1}[0-9]{6,12}([A-Z]{0,1})");
                Matcher matcher = pattern.matcher(passport); 
                if(passport.length()>=7 && matcher.matches()){
                    session.setAttribute("tq_passport",passport); 
                }
            }
            iprsApi.clearIprsFields();
            iprsApi.popIprsFields(dlgIprsComp);
            
            String dob = GlobalCC.checkNullValues(session.getAttribute("iprs_dob"));
            if(dob!=null){ 
                java.util.Date date = GlobalCC.getDate(dob, iprsApi.IPRS_JAVA_DATE_FMT);
                txtProviderDateCreated.setValue(date);
                txtProviderDateCreated.setDisabled(true);
            }
            List<String> fullNames = new ArrayList<String>();
            
            String surname = GlobalCC.checkNullValues(session.getAttribute("iprs_surname"));
            if(surname != null){
                fullNames.add(surname); 
            }
            String firstName=GlobalCC.checkNullValues(session.getAttribute("iprs_firstname"));
            if(firstName!=null){
               fullNames.add(firstName); 
            }
            String otherNames=GlobalCC.checkNullValues(session.getAttribute("iprs_othernames"));
            if(otherNames != null){ 
                fullNames.add(otherNames);
            } 
            if(fullNames.size()>0){
                txtContactPerson.setValue(GlobalCC.join(fullNames, " "));
                txtContactPerson.setDisabled(true);
            }
            String gender=GlobalCC.checkNullValues(session.getAttribute("iprs_gender"));
            if(gender != null){
             //  txtGender.setValue(gender);
             //  txtGender.setDisabled(true);
            }
            session.setAttribute("serviceProviderIprsValidated", "Y");
            GlobalCC.refreshUI(mainDetailTab);
        }
        return null; 
    }

    public String actionLoadIprsSpDtls() {

        BigDecimal SpCode = null;
        try {
            Authorization auth = new Authorization();
            String process = "AMA";
            String processArea = "AMSP";
            String processSubArea = "IVDT";
            String AccessGranted =
                auth.checkUserRights(process, processArea, processSubArea,
                                     null, null);
            if (!"Y".equalsIgnoreCase(AccessGranted) ) {
                GlobalCC.accessDenied();
                return null;
            }
            SpCode = GlobalCC.checkBDNullValues(session.getAttribute("serviceProviderCode"));
            if (SpCode == null) {
                return null;
            }
            IprsSetupsApi iprsApi = new IprsSetupsApi(session);
            iprsApi.load("SP", SpCode, dlgIprsComp); 
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } 
        return null;
    }

    public String actionUpdateTQRecord() {
        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        return iprsApi.saveTQFields(dlgIprsComp);
    }

    public void setDlgIprsComp(RichDialog dlgIprsComp) {
        this.dlgIprsComp = dlgIprsComp;
    }

    public RichDialog getDlgIprsComp() {
        return dlgIprsComp;
    }

    public void setTxtTqIdNumber(RichInputText txtTqIdNumber) {
        this.txtTqIdNumber = txtTqIdNumber;
    }

    public RichInputText getTxtTqIdNumber() {
        return txtTqIdNumber;
    }

    public void setTxtTqPinNo(RichInputText txtTqPinNo) {
        this.txtTqPinNo = txtTqPinNo;
    }

    public RichInputText getTxtTqPinNo() {
        return txtTqPinNo;
    }

    public void setTxtTqPassport(RichInputText txtTqPassport) {
        this.txtTqPassport = txtTqPassport;
    }

    public RichInputText getTxtTqPassport() {
        return txtTqPassport;
    }

    public void setTxtTqDob(RichInputText txtTqDob) {
        this.txtTqDob = txtTqDob;
    }

    public RichInputText getTxtTqDob() {
        return txtTqDob;
    }

    public void setTxtTqFullNames(RichInputText txtTqFullNames) {
        this.txtTqFullNames = txtTqFullNames;
    }

    public RichInputText getTxtTqFullNames() {
        return txtTqFullNames;
    }

    public void setTxtTqGender(RichInputText txtTqGender) {
        this.txtTqGender = txtTqGender;
    }

    public RichInputText getTxtTqGender() {
        return txtTqGender;
    }

    public void setTxtIprsIdNumber(RichInputText txtIprsIdNumber) {
        this.txtIprsIdNumber = txtIprsIdNumber;
    }

    public RichInputText getTxtIprsIdNumber() {
        return txtIprsIdNumber;
    }

    public void setTxtIprsPinNo(RichInputText txtIprsPinNo) {
        this.txtIprsPinNo = txtIprsPinNo;
    }

    public RichInputText getTxtIprsPinNo() {
        return txtIprsPinNo;
    }

    public void setTxtIprsPassport(RichInputText txtIprsPassport) {
        this.txtIprsPassport = txtIprsPassport;
    }

    public RichInputText getTxtIprsPassport() {
        return txtIprsPassport;
    }

    public void setTxtIprsDob(RichInputText txtIprsDob) {
        this.txtIprsDob = txtIprsDob;
    }

    public RichInputText getTxtIprsDob() {
        return txtIprsDob;
    }

    public void setTxtIprsFullNames(RichInputText txtIprsFullNames) {
        this.txtIprsFullNames = txtIprsFullNames;
    }

    public RichInputText getTxtIprsFullNames() {
        return txtIprsFullNames;
    }

    public void setTxtIprsGender(RichInputText txtIprsGender) {
        this.txtIprsGender = txtIprsGender;
    }

    public RichInputText getTxtIprsGender() {
        return txtIprsGender;
    }
    public boolean istxtSurnameDisabled() { 
                String disabled =
                    GlobalCC.checkNullValues(session.getAttribute("FX_SP_SURNAME.disabled")); 
                                String iprs_validated =
                    GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated")); 
                return ("Y".equalsIgnoreCase(disabled) || "Y".equalsIgnoreCase(iprs_validated));
            }
                public boolean istxtOtherNamesDisabled() { 
                String disabled =
                    GlobalCC.checkNullValues(session.getAttribute("FX_SP_OTHER_NAMES.disabled")); 
                                String iprs_validated =
                    GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated")); 
                return ("Y".equalsIgnoreCase(disabled) || "Y".equalsIgnoreCase(iprs_validated));
            }
                
                public boolean istxtFullNameDisabled() { 
                String disabled =
                    GlobalCC.checkNullValues(session.getAttribute("FX_SP_FULLNAME.disabled")); 
                                String iprs_validated =
                    GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated")); 
                return ("Y".equalsIgnoreCase(disabled) || "Y".equalsIgnoreCase(iprs_validated));
            }
                public boolean istxtGenderDisabled() { 
                String disabled =
                    GlobalCC.checkNullValues(session.getAttribute("FX_SP_GENDER.disabled")); 
                                String iprs_validated =
                    GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated")); 
                return ("Y".equalsIgnoreCase(disabled) || "Y".equalsIgnoreCase(iprs_validated));
            }
                public boolean istxtDobDisabled() { 
                String disabled =
                    GlobalCC.checkNullValues(session.getAttribute("FX_SP_DOB.disabled")); 
                                String iprs_validated =
                    GlobalCC.checkNullValues(session.getAttribute("serviceProviderIprsValidated")); 
                return ("Y".equalsIgnoreCase(disabled) || "Y".equalsIgnoreCase(iprs_validated));
            }
}
