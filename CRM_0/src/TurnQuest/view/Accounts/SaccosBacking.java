/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* @author dancan.kavagi@turnkeyafrica.com
*/

package TurnQuest.view.Accounts;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;


/**
 * The base backing bean for all the saccos related pages. Includes
 * properties and methods that map to given  UI components in the relevant
 * pages of the saccos.
 *
 * @author dancan.kavagi@turnkeyafrica.com
 */
public class SaccosBacking { 
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
	private RichTable tblSaccoInfo;	
        private RichInputText txtSaccoShortDesc;
	private RichInputText txtSaccoRegulatorNo;
	private RichInputText txtSaccoName;
	private RichInputText txtSaccoPhysicalAddress;
	private RichInputText txtSaccoPostalAddress;
	private RichInputText txtSaccoCountryCode;
	private RichInputText txtSaccoCountryName;
	private RichInputText txtSaccoStateCode;
	private RichPanelLabelAndMessage pmAdminRegionName;
	private RichInputText txtSaccoStateName; 
	private RichInputText txtSaccoTownName;
	private RichInputText txtSaccoEmail;
	private RichInputText txtSaccoTownCode;
	private RichInputText txtSaccoWebAddress;
	private RichInputText txtSaccoPostalCode;
	private RichInputText txtSaccoContactPerson;
	private RichInputText txtSaccoContactTitle;
	private RichInputText txtSaccoTel1;
	private RichInputText txtSaccoTel2;
	private RichInputText txtSaccoFax;
	private RichInputText txtSaccoAccountNumber;
	private RichInputText txtSaccoPIN;
	private RichSelectOneChoice txtSaccoStatus;
	private RichInputDate txtSaccoDateCreated;
        private RichInputText txtSaccoCreatedBy;
	private RichInputText txtSaccoBranchCode;
	private RichInputText txtSaccoBranchName;
	private RichInputText txtSaccoBankAccountNo;
	private RichInputText txtSaccoBankBranchCode;
	private RichInputText txtSaccoBankBranch;
	private RichInputText txtSaccoAccountNo;
	private RichInputText txtSaccoSms;
	private RichInputText txtSaccoUniquePrefix;
	private RichSelectOneChoice socSaccoLicensed;
        private RichPanelBox pbMainPanelDetail;
        private RichPanelTabbed  mainPanelTab;
	private RichCommandButton btnCreateUpdateSacco;
	private RichCommandButton btnCancelSacco;  
        private RichDialog dlgAdminRegionList;
        private RichTable tblAdminRegions;
    /**
     * Default Class Constructor
     */
	 
    public SaccosBacking() {
    }

    public void setTxtSaccoShortDesc(RichInputText txtSaccoShortDesc) {
        this.txtSaccoShortDesc = txtSaccoShortDesc;
    }

    public RichInputText getTxtSaccoShortDesc() {
        return txtSaccoShortDesc;
    }

    public void setTxtSaccoRegulatorNo(RichInputText txtSaccoRegulatorNo) {
        this.txtSaccoRegulatorNo = txtSaccoRegulatorNo;
    }

    public RichInputText getTxtSaccoRegulatorNo() {
        return txtSaccoRegulatorNo;
    }

    public void setTxtSaccoName(RichInputText txtSaccoName) {
        this.txtSaccoName = txtSaccoName;
    }

    public RichInputText getTxtSaccoName() {
        return txtSaccoName;
    }

    public void setTxtSaccoPhysicalAddress(RichInputText txtSaccoPhysicalAddress) {
        this.txtSaccoPhysicalAddress = txtSaccoPhysicalAddress;
    }

    public RichInputText getTxtSaccoPhysicalAddress() {
        return txtSaccoPhysicalAddress;
    }

    public void setTxtSaccoPostalAddress(RichInputText txtSaccoPostalAddress) {
        this.txtSaccoPostalAddress = txtSaccoPostalAddress;
    }

    public RichInputText getTxtSaccoPostalAddress() {
        return txtSaccoPostalAddress;
    }

    public void setTxtSaccoCountryCode(RichInputText txtSaccoCountryCode) {
        this.txtSaccoCountryCode = txtSaccoCountryCode;
    }

    public RichInputText getTxtSaccoCountryCode() {
        return txtSaccoCountryCode;
    }

    public void setTxtSaccoCountryName(RichInputText txtSaccoCountryName) {
        this.txtSaccoCountryName = txtSaccoCountryName;
    }

    public RichInputText getTxtSaccoCountryName() {
        return txtSaccoCountryName;
    }

    public void setTxtSaccoStateCode(RichInputText txtSaccoStateCode) {
        this.txtSaccoStateCode = txtSaccoStateCode;
    }

    public RichInputText getTxtSaccoStateCode() {
        return txtSaccoStateCode;
    }

    public void setPmAdminRegionName(RichPanelLabelAndMessage pmAdminRegionName) {
        this.pmAdminRegionName = pmAdminRegionName;
    }

    public RichPanelLabelAndMessage getPmAdminRegionName() {
        return pmAdminRegionName;
    }

    public void setTxtSaccoStateName(RichInputText txtSaccoStateName) {
        this.txtSaccoStateName = txtSaccoStateName;
    }

    public RichInputText getTxtSaccoStateName() {
        return txtSaccoStateName;
    }

    public void setTxtSaccoTownName(RichInputText txtSaccoTownName) {
        this.txtSaccoTownName = txtSaccoTownName;
    }

    public RichInputText getTxtSaccoTownName() {
        return txtSaccoTownName;
    }

    public void setTxtSaccoEmail(RichInputText txtSaccoEmail) {
        this.txtSaccoEmail = txtSaccoEmail;
    }

    public RichInputText getTxtSaccoEmail() {
        return txtSaccoEmail;
    }

    public void setTxtSaccoTownCode(RichInputText txtSaccoTownCode) {
        this.txtSaccoTownCode = txtSaccoTownCode;
    }

    public RichInputText getTxtSaccoTownCode() {
        return txtSaccoTownCode;
    }

    public void setTxtSaccoWebAddress(RichInputText txtSaccoWebAddress) {
        this.txtSaccoWebAddress = txtSaccoWebAddress;
    }

    public RichInputText getTxtSaccoWebAddress() {
        return txtSaccoWebAddress;
    }

    public void setTxtSaccoPostalCode(RichInputText txtSaccoPostalCode) {
        this.txtSaccoPostalCode = txtSaccoPostalCode;
    }

    public RichInputText getTxtSaccoPostalCode() {
        return txtSaccoPostalCode;
    }

    public void setTxtSaccoContactPerson(RichInputText txtSaccoContactPerson) {
        this.txtSaccoContactPerson = txtSaccoContactPerson;
    }

    public RichInputText getTxtSaccoContactPerson() {
        return txtSaccoContactPerson;
    }

    public void setTxtSaccoContactTitle(RichInputText txtSaccoContactTitle) {
        this.txtSaccoContactTitle = txtSaccoContactTitle;
    }

    public RichInputText getTxtSaccoContactTitle() {
        return txtSaccoContactTitle;
    }

    public void setTxtSaccoTel1(RichInputText txtSaccoTel1) {
        this.txtSaccoTel1 = txtSaccoTel1;
    }

    public RichInputText getTxtSaccoTel1() {
        return txtSaccoTel1;
    }

    public void setTxtSaccoTel2(RichInputText txtSaccoTel2) {
        this.txtSaccoTel2 = txtSaccoTel2;
    }

    public RichInputText getTxtSaccoTel2() {
        return txtSaccoTel2;
    }

    public void setTxtSaccoFax(RichInputText txtSaccoFax) {
        this.txtSaccoFax = txtSaccoFax;
    }

    public RichInputText getTxtSaccoFax() {
        return txtSaccoFax;
    }

    public void setTxtSaccoAccountNumber(RichInputText txtSaccoAccountNumber) {
        this.txtSaccoAccountNumber = txtSaccoAccountNumber;
    }

    public RichInputText getTxtSaccoAccountNumber() {
        return txtSaccoAccountNumber;
    }

    public void setTxtSaccoPIN(RichInputText txtSaccoPIN) {
        this.txtSaccoPIN = txtSaccoPIN;
    }

    public RichInputText getTxtSaccoPIN() {
        return txtSaccoPIN;
    }

    public void setTxtSaccoStatus(RichSelectOneChoice txtSaccoStatus) {
        this.txtSaccoStatus = txtSaccoStatus;
    }

    public RichSelectOneChoice getTxtSaccoStatus() {
        return txtSaccoStatus;
    }

    public void setTxtSaccoDateCreated(RichInputDate txtSaccoDateCreated) {
        this.txtSaccoDateCreated = txtSaccoDateCreated;
    }

    public RichInputDate getTxtSaccoDateCreated() {
        return txtSaccoDateCreated;
    }

    public void setTxtSaccoBranchCode(RichInputText txtSaccoBranchCode) {
        this.txtSaccoBranchCode = txtSaccoBranchCode;
    }

    public RichInputText getTxtSaccoBranchCode() {
        return txtSaccoBranchCode;
    }

    public void setTxtSaccoBranchName(RichInputText txtSaccoBranchName) {
        this.txtSaccoBranchName = txtSaccoBranchName;
    }

    public RichInputText getTxtSaccoBranchName() {
        return txtSaccoBranchName;
    }

    public void setTxtSaccoBankAccountNo(RichInputText txtSaccoBankAccountNo) {
        this.txtSaccoBankAccountNo = txtSaccoBankAccountNo;
    }

    public RichInputText getTxtSaccoBankAccountNo() {
        return txtSaccoBankAccountNo;
    }

    public void setTxtSaccoBankBranchCode(RichInputText txtSaccoBankBranchCode) {
        this.txtSaccoBankBranchCode = txtSaccoBankBranchCode;
    }

    public RichInputText getTxtSaccoBankBranchCode() {
        return txtSaccoBankBranchCode;
    }

    public void setTxtSaccoBankBranch(RichInputText txtSaccoBankBranch) {
        this.txtSaccoBankBranch = txtSaccoBankBranch;
    }

    public RichInputText getTxtSaccoBankBranch() {
        return txtSaccoBankBranch;
    }

    public void setTxtSaccoAccountNo(RichInputText txtSaccoAccountNo) {
        this.txtSaccoAccountNo = txtSaccoAccountNo;
    }

    public RichInputText getTxtSaccoAccountNo() {
        return txtSaccoAccountNo;
    }

    public void setTxtSaccoSms(RichInputText txtSaccoSms) {
        this.txtSaccoSms = txtSaccoSms;
    }

    public RichInputText getTxtSaccoSms() {
        return txtSaccoSms;
    }

    public void setTxtSaccoUniquePrefix(RichInputText txtSaccoUniquePrefix) {
        this.txtSaccoUniquePrefix = txtSaccoUniquePrefix;
    }

    public RichInputText getTxtSaccoUniquePrefix() {
        return txtSaccoUniquePrefix;
    }

    public void setSocSaccoLicensed(RichSelectOneChoice selSaccoLicensed) {
        this.socSaccoLicensed = selSaccoLicensed;
    }

    public RichSelectOneChoice getSocSaccoLicensed() {
        return socSaccoLicensed;
    }

    public void setBtnCreateUpdateSacco(RichCommandButton btnCreateUpdateSacco) {
        this.btnCreateUpdateSacco = btnCreateUpdateSacco;
    }

    public RichCommandButton getBtnCreateUpdateSacco() {
        return btnCreateUpdateSacco;
    }

    public void setBtnCancelSacco(RichCommandButton btnCancelSacco) {
        this.btnCancelSacco = btnCancelSacco;
    }

    public RichCommandButton getBtnCancelSacco() {
        return btnCancelSacco;
    }
    public void setMainPanelTab(RichPanelTabbed mainPanelTab) {
        this.mainPanelTab = mainPanelTab;
    }

    public RichPanelTabbed getMainPanelTab() {
        return mainPanelTab;
    }
    /**
     * Preparing to create a new Sacco by learing the text fields
     *
     * @return null
     */
    public String actionNewSacco() {
     /*   session.setAttribute("PayeeType", "A");
        session.removeAttribute("bactAccCode");
        txtSaccoName.setValue(null);
        
        // Check if an Account type has been selected
        if (session.getAttribute("accountTypeCode") == null ||
            session.getAttribute("accountTypeCode") == ("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select an Account Type first!");

        } else {
            // Clear all the text fileds and enable the buttons
            refreshSaccoDetailSection();


            txtAccountTypeCode.setValue(session.getAttribute("accountTypeCode"));

            btnCreateUpdateCurrentSacco.setText("Save");
            btnCreateUpdateSacco.setText("Save");
            btnCreateUpdateSacco.setDisabled(false);
            //btnCancelSacco.setDisabled(false);

            mainPanelTab.setVisible(true);
            pmAdminRegionName.setVisible(true);
            session.setAttribute("otherNames", null);
            session.setAttribute("count", null);
            session.setAttribute("branchCode", null);
            ADFUtils.findIterator("fetchAllAccountAgenciesBasedOnNamesIterator").executeQuery();
            GlobalCC.refreshUI(tbListAgencies);
            GlobalCC.refreshUI(txtSacco);
            GlobalCC.refreshUI(pbMainPanelDetail);
        }*/

        return null;
    }
    public String actionEditSacco() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }
    /**
     * Prompts the user to confirm if they really need to proceed with the
     * delete operation.
     *
     * @return null
     */
    public String actionDeleteSacco() {
        Object key2 = tblSaccoInfo.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            String saccoCode = n.getAttribute("code").toString();
            // Open the popup dialog to confirm delete action
            GlobalCC.showPopup("pt1:confirmSaccoDeletePop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Recocrd Selected");
        }
        return null;
    }
    public void actionSelectSacco(SelectionEvent selectionEvent) {
       /*try {
            session.setAttribute("PayeeType", "A");
            Object key2 = tblSaccoInfo.getSelectedRowData();
            JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

            if (n != null) {
                String saccoCode = n.getAttribute("code");
                String shortDesc = n.getAttribute("shortDesc");
                session.setAttribute("saccoCode", saccoCode);
                session.setAttribute("ClientCode", saccoCode);
                session.setAttribute("shortDesc", shortDesc);
                session.setAttribute("P_SACCO_CODE", saccoCode); 
                
                btnDeleteSacco.setDisabled(false);
                GlobalCC.refreshUI(btnDeleteSacco);
                // to avoid the session problem
                hiddenAccountCode.setValue(saccoCode);
                GlobalCC.refreshUI(panelDetailSystems);

                txtSaccoCode.setValue(n.getAttribute("code")); 
                txtSaccoShortDesc.setValue(n.getAttribute("shortDesc")); 
                txtSacco.setValue(n.getAttribute("mainSacco"));
                txtSaccoName.setValue(n.getAttribute("name"));
                txtSaccoPhysicalAddress.setValue(n.getAttribute("physicalAddress"));
                txtSaccoPostalAddress.setValue(n.getAttribute("postalAddress"));
                txtSaccoCountryCode.setValue(n.getAttribute("countryCode"));
                session.setAttribute("countryCode", txtSaccoCountryCode.getValue());
                session.setAttribute("bru_code", n.getAttribute("bru_code"));
                session.setAttribute("accountManagerCode",  n.getAttribute("accountManagerCode"));

                txtSaccoCountryName.setValue(UtilDAO.fetchCountryName((String)n.getAttribute("countryCode"))); 
                txtSaccoTownCode.setValue(n.getAttribute("townCode"));
                txtSaccoTownName.setValue(UtilDAO.fetchTownName((String)n.getAttribute("townCode")));
                txtSaccoEmail.setValue(n.getAttribute("emailAddress"));
                txtSaccoWebAddress.setValue(n.getAttribute("webAddress"));
                txtSaccoPostalCode.setValue(n.getAttribute("zip"));
                txtSaccoContactPerson.setValue(n.getAttribute("contactPerson"));
                txtSaccoContactTitle.setValue(n.getAttribute("contactTitle"));
                txtSaccoTel1.setValue(n.getAttribute("telephone1"));
                txtSaccoTel2.setValue(n.getAttribute("telephone2"));
                txtSaccoFax.setValue(n.getAttribute("fax"));
                txtSaccoAccountNumber.setValue(n.getAttribute("accountNum"));
                txtSaccoPIN.setValue(n.getAttribute("PIN")); 

                if (n.getAttribute("status") == null) {
                    txtSaccoStatus.setValue("ACTIVE");
                } else {
                    txtSaccoStatus.setValue(n.getAttribute("status"));
                }
                txtSaccoDateCreated.setValue(n.getAttribute("dateCreated"));
                txtSaccoCreatedBy.setValue(n.getAttribute("createdBy")); 
                txtSaccoChecked.setValue(n.getAttribute("checked"));
                txtSaccoCheckedBy.setValue(n.getAttribute("checkedBy"));
                txtSaccoCheckDate.setValue(n.getAttribute("checkDate"));
                txtSaccoBranchCode.setValue(n.getAttribute("branchCode"));
                session.setAttribute("branchCode", n.getAttribute("branchCode"));
                txtSaccoBranchName.setValue(UtilDAO.fetchBranchName((String)n.getAttribute("branchCode")));
                txtStatusDesc.setValue(n.getAttribute("statusDesc"));
                txtSaccoIDNum.setValue(n.getAttribute("IDNum"));
                txtSaccoContractCode.setValue(n.getAttribute("conCode"));
                txtSaccoSaccoCode.setValue(n.getAttribute("agentCode")); 
				session.setAttribute("zipCode", n.getAttribute("couZipCode"));
		        txtSaccoSms.setValue(n.getAttribute("saccoSms"));
                session.setAttribute("brnCode", n.getAttribute("brnCode"));
                txtSaccoExpiriyDate.setValue(n.getAttribute("expiriyDate"));
                txtSaccoLicenseNum.setValue(n.getAttribute("licenseNum"));
                txtSaccoRunoff.setValue(n.getAttribute("runOff"));
                selSaccoLicensed.setValue(n.getAttribute("licensed"));
                txtSaccoBankName.setValue(n.getAttribute("bankName"));
                txtSaccoBankBranchCode.setValue(n.getAttribute("bankBranchCode"));
                txtSaccoBankBranch.setValue(n.getAttribute("bankBranchName"));
                txtSaccoAccountNo.setValue(n.getAttribute("accountNo"));
                txtSaccoUniquePrefix.setValue(n.getAttribute("agentPrefix"));
                txtSaccoStateCode.setValue(n.getAttribute("agentStateCode"));
                session.setAttribute("stateCode", txtSaccoStateCode.getValue());
                txtSaccoStateName.setValue(n.getAttribute("agentStateName"));
              

                
                Object couAdminUnit = n.getAttribute("couAdminType");
                if (couAdminUnit == null) {
                    pmAdminRegionName.setVisible(false);
                    pmTownName.setVisible(false);
                } else {
                    pmAdminRegionName.setVisible(true);
                    pmTownName.setVisible(true);
                    pmAdminRegionName.setLabel(GlobalCC.formatAdminUnitSingular(couAdminUnit) +
                                                  ":");
                    dlgAdminRegionList.setTitle(GlobalCC.formatAdminUnitPlural(couAdminUnit) +
                                                " List");
                }

                // Refresh the agency details panel
                GlobalCC.refreshUI(panelSaccoDetails);
                GlobalCC.refreshUI(pbMainPanelDetail);
              
            }

            // Just in case the buttons are not disabled
            btnCreateUpdateCurrentSacco.setText("Update");
            btnCreateUpdateSacco.setText("Update");
            btnCreateUpdateSacco.setDisabled(true);
            
            //btnCancelSacco.setDisabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }

    public String saveSacco() {
         
     BigDecimal saccoCode = 
            GlobalCC.checkBDNullValues(session.getAttribute("saccoCode"));
        String agencyStatus =
            GlobalCC.checkNullValues(txtSaccoStatus.getValue());
        String accountTypeCode =
            GlobalCC.checkNullValues(session.getAttribute("accountTypeCode"));
        String shortDesc =
            GlobalCC.checkNullValues(txtSaccoShortDesc.getValue());
        String agencyName =
            GlobalCC.checkNullValues(txtSaccoName.getValue());
        agencyName=(agencyName!=null)?agencyName.toUpperCase():null;
        String agencyPin =
            GlobalCC.checkNullValues(txtSaccoPIN.getValue());
        BigDecimal branchCode =
            GlobalCC.checkBDNullValues(txtSaccoBranchCode.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtSaccoPhysicalAddress.getValue());
        String emailAddress =
            GlobalCC.checkNullValues(txtSaccoEmail.getValue());
        String telephone1 =
            GlobalCC.checkNullValues(txtSaccoTel1.getValue());
        String regionTypeName =
            GlobalCC.checkNullValues(txtSaccoStateName.getValue());
        String townName =
            GlobalCC.checkNullValues(txtSaccoTownName.getValue()); 
        String countryName =
            GlobalCC.checkNullValues(txtSaccoCountryName.getValue());  
				
        String Query = "begin TQC_SETUPS_PKG.sacco_prc(" +
				 "?,?,?,?,?,?,?,?,?,?," +
				 "?,?,?,?,?,?,?,?,?,?," +
				 "?,?,?,?,?,?,?,?,?,?" +
				"); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.registerOutParameter(1, OracleTypes.VARCHAR); //shortDesc);
				cst.setString(2, "A");  
                cst.setBigDecimal(3, saccoCode); 
                cst.setString(5,  (agencyName!=null)?agencyName.toUpperCase():null);
                cst.setString(6, (String)txtSaccoPhysicalAddress.getValue());
                cst.setString(7, (String)txtSaccoPostalAddress.getValue());
                cst.setString(8, (String)txtSaccoTownCode.getValue());
                cst.setString(9, (String)txtSaccoCountryCode.getValue());
                cst.setString(10, (String)txtSaccoEmail.getValue());
                cst.setString(11, (String)txtSaccoWebAddress.getValue());
                cst.setString(12, (String)txtSaccoPostalCode.getValue());
                cst.setString(13, (String)txtSaccoContactPerson.getValue());
                cst.setString(14, (String)txtSaccoContactTitle.getValue());
                cst.setString(15, (String)txtSaccoTel1.getValue());
                cst.setString(16, (String)txtSaccoTel2.getValue());
                cst.setString(17, (String)txtSaccoFax.getValue());
                cst.setString(18, (String)txtSaccoAccountNumber.getValue());
                cst.setString(19, (String)txtSaccoPIN.getValue()); 
                cst.setString(24, (String)txtSaccoStatus.getValue()); 
				cst.execute();
                // Refresh the tree 
                ADFUtils.findIterator("fetchSaccosIterator").executeQuery();
                GlobalCC.refreshUI(tblSaccoInfo);  
            
                // Reset the form to avoid accidental creation of a new record with
                // the same values
                refreshSaccoDetailSection();

                String message = "Sacco CREATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e); 
            }finally{
		 DbUtils.closeQuietly(conn,cst,null); 
	    }
        return null;
    }
    /**
        * Refreshes the screen in the Agency/Account detail section
        */
       public void refreshSaccoDetailSection() {
           Util u = new Util(); 
           session.setAttribute("saccoCode",null);
           txtSaccoShortDesc.setValue(null);
           txtSaccoName.setValue(null);
           txtSaccoPhysicalAddress.setValue(null);
           txtSaccoPostalAddress.setValue(null);
           txtSaccoCountryCode.setValue(session.getAttribute("COUNTRY_CODE"));
           txtSaccoCountryName.setValue(session.getAttribute("COUNTRY_NAME"));
           if (session.getAttribute("COUNTRY_NAME") != null) {
               if (session.getAttribute("COUNTRY_NAME").equals("KENYA")) {
                   pmAdminRegionName.setLabel("County Name");
               }
           } 
           String adminRegionType = GlobalCC.checkNullValues(session.getAttribute("ADMIN_REG_TYPE"));
           if (adminRegionType != null) { 
               pmAdminRegionName.setLabel(GlobalCC.formatAdminUnitSingular(adminRegionType));
               dlgAdminRegionList.setTitle((GlobalCC.formatAdminUnitPlural(adminRegionType) + " List"));
               txtSaccoStateCode.setValue(null);
               txtSaccoStateName.setValue(null);
               txtSaccoTownCode.setValue(null);
               txtSaccoTownName.setValue(null);
               pmAdminRegionName.setVisible(true);
           }
           txtSaccoTownCode.setValue(null);
           txtSaccoStateCode.setValue(null);
           txtSaccoStateName.setValue(null);
           txtSaccoTownName.setValue(null);
           txtSaccoEmail.setValue(null);
           txtSaccoWebAddress.setValue(null);
           txtSaccoPostalCode.setValue(null);
           txtSaccoContactPerson.setValue(null);
           txtSaccoContactTitle.setValue(null);
           txtSaccoTel1.setValue(null);
           txtSaccoTel2.setValue(null);
           txtSaccoFax.setValue(null);
           txtSaccoAccountNumber.setValue(null);
           txtSaccoPIN.setValue(null);  
           txtSaccoStatus.setValue(null);
           txtSaccoDateCreated.setValue(null);
           txtSaccoCreatedBy.setValue(null); 
		   
           if (u.defaultUserBranch().equals("N")) {
               txtSaccoBranchCode.setValue(null);
               txtSaccoBranchName.setValue(null);
           } else {
               txtSaccoBranchCode.setValue(session.getAttribute("branchCode"));
               txtSaccoBranchName.setValue(session.getAttribute("branchName"));
           }
         
           txtSaccoSms.setValue(null); 
           socSaccoLicensed.setValue(null); 
           txtSaccoBankBranch.setValue(null);
           txtSaccoBankBranchCode.setValue(null);
           txtSaccoAccountNo.setValue(null);
           txtSaccoUniquePrefix.setValue(null);
       }	
    public String actionShowStates() {
       if (txtSaccoCountryCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Country first to proceed.");
            return null;
        } else { 
            session.setAttribute("countryCode",  txtSaccoCountryCode.getValue());
            ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
            GlobalCC.refreshUI(tblAdminRegions); 
            GlobalCC.showPopup( "pt1:statesPop");
        }
        /* */
        return null;
    }

    public String actionAcceptState() {
     /*   Object key2 = tblAdminRegions.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            
           BigDecimal stateCode= GlobalCC.checkBDNullValues(n.getAttribute("stateCode"));
           if(stateCode==null) {
               GlobalCC.INFORMATIONREPORTING("Please Select Item First!");
               return null;
           }
            txtSaccoStateCode.setValue(stateCode);
            txtSaccoStateName.setValue(n.getAttribute("stateName"));
            
            session.setAttribute("stateCode",  stateCode);

            txtSaccoTownCode.setValue(null);
            txtSaccoTownName.setValue(null);

            ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
            GlobalCC.refreshUI(tbTownListing);
            GlobalCC.refreshUI(txtSaccoStateName);
        }
        GlobalCC.dismissPopUp("pt1", "statesPop");
       */
        return null;
    }
    public String actionShowTownPopup() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }
    public String actionShowAccounts() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }
    public String actionShowBranches() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }    
	public String actionShowBankBranches() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }
    public String actionCreateUpdateSacco() {
        GlobalCC.showPopup( "pt1:accountTypesPop" );
        return null;
    }

    public void setTxtSaccoCreatedBy(RichInputText txtSaccoCreatedBy) {
        this.txtSaccoCreatedBy = txtSaccoCreatedBy;
    }

    public RichInputText getTxtSaccoCreatedBy() {
        return txtSaccoCreatedBy;
    }

    public void setTblSaccoInfo(RichTable tblSaccoInfo) {
        this.tblSaccoInfo = tblSaccoInfo;
    }

    public RichTable getTblSaccoInfo() {
        return tblSaccoInfo;
    }

    public void setPbMainPanelDetail(RichPanelBox pbMainPanelDetail) {
        this.pbMainPanelDetail = pbMainPanelDetail;
    }

    public RichPanelBox getPbMainPanelDetail() {
        return pbMainPanelDetail;
    }

    public void setDlgAdminRegionList(RichDialog dlgAdminRegionList) {
        this.dlgAdminRegionList = dlgAdminRegionList;
    }

    public RichDialog getDlgAdminRegionList() {
        return dlgAdminRegionList;
    }
    public void setTblAdminRegions(RichTable tblAdminRegions) {
            this.tblAdminRegions = tblAdminRegions;
        }

        public RichTable getTblAdminRegions() {
            return tblAdminRegions;
        }
}
