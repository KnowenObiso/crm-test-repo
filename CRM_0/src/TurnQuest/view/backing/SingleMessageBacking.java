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

package TurnQuest.view.backing;


import TurnQuest.view.Alerts.SystemAlerts;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.sql.CallableStatement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;

import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class SingleMessageBacking {

    private RichInputText txtSmsMsg;
    private RichInputText txtSmsCode;
    private RichInputText txtSmsSysCode;
    private RichInputText txtSmsSysName;
    private RichInputText txtSmsSysModule;
    private RichInputText txtSmsClientCode;
    private RichInputText txtSmsClientName;
    private RichInputText txtSmsAgnCode;
    private RichInputText txtSmsAgnName;
    private RichInputText txtSmsQuotCode;
    private RichInputText txtSmsQuotNo;
    private RichInputText txtSmsPolCode;
    private RichInputText txtSmsPolNo;
    private RichInputText txtSmsClaimNo;
    private RichInputText txtSmsTelNo;
    private RichSelectOneChoice txtSmsStatus;
    private RichInputText txtSmsPreparedBy;
    private RichInputDate txtSmsPreparedDate;
    private RichInputDate txtSmsSendDate;
    private RichInputText txtSmsUserCode;
    private RichInputText txtSmsMessageTemplate;
    private RichInputText txtSmsMsgTemplateCode;
    private RichInputText txtSearchShortDesc;
    private RichInputText txtSearchName;
    private RichInputText txtSearchOtherName;
    private RichInputText txtSearchPostal;
    private RichInputText txtSearchPhysical;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichSelectBooleanRadio rbtnCustomerId;

    private RichTable tblClientPop;
    private RichPanelCollection singleMessageBacking;
    private RichTable tblmMsgTemplate;
    private RichTable tblSytems;
    private RichInputText smsDesc;
    private RichInputText smsUrl;
    private RichInputText smsUsername;
    private RichInputText smsPassword;
    private RichInputText smsSource;
    private RichInputNumberSpinbox smsDest;
    private RichInputText smsMessage;
    private RichTable providers;
    private RichInputNumberSpinbox smsCode;
    private RichSelectOneChoice smsDefault;
    private RichColumn checkboxCol;
    private RichSelectBooleanCheckbox columnSelect;
    private RichTable tblAccountTypesPop;
    private RichTable tblAgencyInfo;
    private RichInputText txtMainAccountType;
    private RichCommandButton accTypeBtn;
    private RichSelectBooleanRadio rdOther;
    private RichSelectBooleanRadio rdClient;
    private RichColumn agentSelect;
    private RichInputDate txtFutureDate;

    public SingleMessageBacking() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (rbtnShortDesc.isSelected()) {
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                if (txtSearchShortDesc.isDisabled()) {
                    txtSearchShortDesc.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchShortDesc);

                }

                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchName);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
                GlobalCC.refreshUI(txtSearchOtherName);


            }

            else if (rbtnPhySicalAddr.isSelected()) {

                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);

                txtSearchShortDesc.setValue(null);


                txtSearchPostal.setValue(null);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                txtSearchShortDesc.setDisabled(true);

                if (txtSearchPhysical.isDisabled()) {
                    txtSearchPhysical.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchPhysical);

                }

                txtSearchPostal.setDisabled(true);


                GlobalCC.refreshUI(txtSearchName);
                GlobalCC.refreshUI(txtSearchOtherName);

                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPostal);
            } else if (rbtnPostalAddr.isSelected()) {

                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                if (txtSearchPostal.isDisabled()) {
                    txtSearchPostal.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchPostal);

                }
                GlobalCC.refreshUI(txtSearchName);
                GlobalCC.refreshUI(txtSearchOtherName);

                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);

            }

            else if (rbtnExactName.isSelected()) {

                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);


                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                if (txtSearchName.isDisabled()) {
                    txtSearchName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchName);

                }
                if (txtSearchOtherName.isDisabled()) {
                    txtSearchOtherName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchOtherName);

                }

                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
            } else if (rbtnPartOfAnyName.isSelected()) {

                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);


                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                if (txtSearchName.isDisabled()) {
                    txtSearchName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchName);

                }
                if (txtSearchOtherName.isDisabled()) {
                    txtSearchOtherName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchOtherName);

                }

                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
            }


        }
    }

    public String actionAcceptSearchCriteria() {

        String name = null;
        String oName = null;
        String shtDesc = null;
        String clntType = null;
        String status = null;
        String postalAddr = null;
        String physicalAddr = null;
        String oldId = null;
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

        String searchShtDesc =
            GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
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


        session.setAttribute("searchCriteria", null);
        session.setAttribute("searchCriteria2", null);


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

        if (searchName != null) {
            name = "'" + searchName + "'";

        }
        if (searchOName != null) {
            oName = "'" + searchOName + "'";
        }
        if (searchShtDesc != null) {
            shtDesc = "'" + searchShtDesc + "'";
        }

        if (searchPhysicalAddr != null) {
            physicalAddr = "'" + searchPhysicalAddr + "'";
        }
        if (searchPostalAddr != null) {
            postalAddr = "'" + searchPostalAddr + "'";
        }


        if (rbtnPartOfAnyName.isSelected()) {
            // criteria
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            }
            criteria =
                    "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA)'))||'%'\n" +
                    "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA'))||'%' OR " +
                    "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" + oName +
                    ",'HAKUNA)'))||'%'" +
                    " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                    ",'HAKUNA'))||'%')";


        } else if (rbtnExactName.isSelected()) {
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (name != null) {


                criteria = "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) ";


            } else if (oName != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )";


                }

                //beginning part  of first and other name


            }
        } else if (rbtnShortDesc.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }

            criteria =
                    " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" + shtDesc +
                    "),'HAKUNA')||'%')";

        } else if (rbtnPhySicalAddr.isSelected()) {

            if (physicalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Physical Address  Required");
                return null;
            }

            criteria =
                    "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" + physicalAddr +
                    ")||'%')";


        } else if (rbtnPostalAddr.isSelected()) {

            if (postalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Postal Address Required");
                return null;
            }

            criteria =
                    "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                    ")||'%')";

        }

        else {
            GlobalCC.INFORMATIONREPORTING("Choose criteria::");
            return null;
        }

        session.setAttribute("searchCriteria", criteria);


        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        // List<UIComponent> children =tblClientHolder.getChildren();
        //  UIComponent component = children.get(0);
        //  RichTable  table=(RichTable) component;

        // GlobalCC.refreshUI(table);
        GlobalCC.refreshUI(tblClientPop);

        return null;


    }

    public String actionResetSearch() {

        txtSearchName.setValue(null);
        txtSearchOtherName.setValue(null);
        txtSearchShortDesc.setValue(null);
        txtSearchPhysical.setValue(null);
        txtSearchPostal.setValue(null);

        //activate components
        txtSearchName.setDisabled(true);
        txtSearchOtherName.setDisabled(true);
        txtSearchShortDesc.setDisabled(true);
        txtSearchPhysical.setDisabled(true);
        txtSearchPostal.setDisabled(true);

        //refresh radio buttons
        rbtnPartOfAnyName.setSelected(false);
        rbtnExactName.setSelected(false);
        rbtnShortDesc.setSelected(false);
        rbtnPhySicalAddr.setSelected(false);
        rbtnPostalAddr.setSelected(false);


        GlobalCC.refreshUI(rbtnPartOfAnyName);
        GlobalCC.refreshUI(rbtnExactName);
        GlobalCC.refreshUI(rbtnShortDesc);
        GlobalCC.refreshUI(rbtnPhySicalAddr);
        GlobalCC.refreshUI(rbtnPostalAddr);


        //refesh components
        GlobalCC.refreshUI(txtSearchName);
        GlobalCC.refreshUI(txtSearchOtherName);

        GlobalCC.refreshUI(txtSearchShortDesc);

        GlobalCC.refreshUI(txtSearchPhysical);
        GlobalCC.refreshUI(txtSearchPostal);
        session.setAttribute("searchCriteria", null);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;
    }

    public String actionShowSearch() {

        if (rdOther.isSelected()) {
            if (session.getAttribute("accountTypeCode") == null) {
                GlobalCC.errorValueNotEntered("Error: Please Select Account Type");
                return null;
            }
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:accounts" + "').show(hints);");
        } else if (rdClient.isSelected()) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:searchClientPop" + "').show(hints);");
        }
        return null;

    }

    public String actionShowMeassageTemplate() {
        String sysCode = GlobalCC.checkNullValues(txtSmsSysCode.getValue());
        if (sysCode != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:msgTemplatePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        return null;

    }

    public String actionAcceptMessageTemplate() {
        Object key2 = tblmMsgTemplate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSmsMsgTemplateCode.setValue(nodeBinding.getAttribute("msgtCode"));
            txtSmsMsg.setValue(nodeBinding.getAttribute("msgtMsg"));
            txtSmsMessageTemplate.setValue(nodeBinding.getAttribute("msgtShtDesc"));
            GlobalCC.refreshUI(txtSmsMessageTemplate);
            GlobalCC.refreshUI(txtSmsMsg);
            GlobalCC.dismissPopUp("crm", "msgTemplatePop");

        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        return null;

    }

    public String actionAcceptClient() {
        int k = 0;

        Object clientCode = null;
        Object clientName = null;
        Object smsVal = null;
        List<Map> clients = new ArrayList<Map>();

        while (k < tblClientPop.getRowCount()) {
            Object key2 = tblClientPop.getRowData(k);
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                //System.out.println(nodeBinding.getAttribute("code"));
                boolean select = (Boolean)nodeBinding.getAttribute("select");
                if (select == true) {
                    Map<String, Object> clientDetails =
                        new HashMap<String, Object>();
                    clientDetails.put("code",
                                      nodeBinding.getAttribute("code"));
                    clientDetails.put("name",
                                      nodeBinding.getAttribute("name") + " " +
                                      nodeBinding.getAttribute("otherNames"));
                    clientDetails.put("sms", nodeBinding.getAttribute("sms"));
                    if (clientCode == null) {
                        clientCode = nodeBinding.getAttribute("code");
                        if (nodeBinding.getAttribute("name") != null) {
                            clientName = nodeBinding.getAttribute("name");
                        }
                        if (nodeBinding.getAttribute("sms") != null) {
                            smsVal = nodeBinding.getAttribute("sms");
                        }
                    } else {
                        clientCode =
                                clientCode + "," + nodeBinding.getAttribute("code");
                        clientName =
                                clientName + "," + nodeBinding.getAttribute("name") +
                                " " + nodeBinding.getAttribute("otherNames");
                        smsVal =
                                smsVal + "," + nodeBinding.getAttribute("sms");


                    }
                    clients.add(clientDetails);
                    clientDetails = null;
                }

            } else {
                GlobalCC.dismissPopUp("crm", "searchClientPop");
                GlobalCC.INFORMATIONREPORTING("First Select System ::");
                return null;
            }
            k++;
        }
        session.setAttribute("multiAccount", clients);
        clients = null;
        GlobalCC.dismissPopUp("crm", "searchClientPop");
        clientName = clientName.toString().replace("null", "");
        smsVal = smsVal.toString().replace("null", "");
        txtSmsClientCode.setValue(clientCode);
        txtSmsClientName.setValue(clientName);
        txtSmsTelNo.setValue(smsVal);
        GlobalCC.refreshUI(txtSmsClientCode);
        GlobalCC.refreshUI(txtSmsClientName);
        GlobalCC.refreshUI(txtSmsTelNo);
        return null;

    }

    public String actionAcceptSystems() {
        Object key2 = tblSytems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSmsSysName.setValue(nodeBinding.getAttribute("sysName"));
            txtSmsSysCode.setValue(nodeBinding.getAttribute("sysCode"));
            session.setAttribute("sysCode",
                                 nodeBinding.getAttribute("sysCode"));

            ADFUtils.findIterator("findMsgTemplateIterator").executeQuery();
            GlobalCC.refreshUI(txtSmsSysName);
            GlobalCC.dismissPopUp("crm", "systemsPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        return null;

    }

    public String actionShowSystems() {


        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').show(hints);");
        return null;

    }

    public boolean checkIfAnyTableRowselected(RichTable table,
                                              String attribute) {

        RowKeySet rks = new RowKeySetImpl();
        int rowcount = table.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)table.getRowData();
            if (nodeBinding != null) {
                if (nodeBinding.getAttribute(attribute).toString().equalsIgnoreCase("true")) {

                    count = count + 1;
                }
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String selectAll(RichTable table,
                            RichSelectBooleanCheckbox columnBinding) {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = table.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);

            columnBinding.setSelected(true);
            GlobalCC.refreshUI(columnBinding);
        }
        return null;

    }

    public String deselectAll(RichTable table,
                              RichSelectBooleanCheckbox columnBinding,
                              String attribute) {

        if (checkIfAnyTableRowselected(table, attribute)) {
            int rowcount = table.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                table.setRowIndex(i);
                Object key = table.getRowKey();
                table.setRowKey(key);


                columnBinding.setSelected(false);
                GlobalCC.refreshUI(columnBinding);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) selected::");
        }
        return null;

    }

    public void setTxtSmsMsg(RichInputText txtSmsMsg) {
        this.txtSmsMsg = txtSmsMsg;
    }

    public RichInputText getTxtSmsMsg() {
        return txtSmsMsg;
    }

    public void txtSmsMsgListener(ValueChangeEvent valueChangeEvent) {
        System.out.println("Msg is : " + valueChangeEvent.getNewValue());
    }

    public void setTxtSmsCode(RichInputText txtSmsCode) {
        this.txtSmsCode = txtSmsCode;
    }

    public RichInputText getTxtSmsCode() {
        return txtSmsCode;
    }

    public void setTxtSmsSysCode(RichInputText txtSmsSysCode) {
        this.txtSmsSysCode = txtSmsSysCode;
    }

    public RichInputText getTxtSmsSysCode() {
        return txtSmsSysCode;
    }

    public void setTxtSmsSysName(RichInputText txtSmsSysName) {
        this.txtSmsSysName = txtSmsSysName;
    }

    public RichInputText getTxtSmsSysName() {
        return txtSmsSysName;
    }

    public void setTxtSmsSysModule(RichInputText txtSmsSysModule) {
        this.txtSmsSysModule = txtSmsSysModule;
    }

    public RichInputText getTxtSmsSysModule() {
        return txtSmsSysModule;
    }

    public void setTxtSmsClientCode(RichInputText txtSmsClientCode) {
        this.txtSmsClientCode = txtSmsClientCode;
    }

    public RichInputText getTxtSmsClientCode() {
        return txtSmsClientCode;
    }

    public void setTxtSmsClientName(RichInputText txtSmsClientName) {
        this.txtSmsClientName = txtSmsClientName;
    }

    public RichInputText getTxtSmsClientName() {
        return txtSmsClientName;
    }

    public void setTxtSmsAgnCode(RichInputText txtSmsAgnCode) {
        this.txtSmsAgnCode = txtSmsAgnCode;
    }

    public RichInputText getTxtSmsAgnCode() {
        return txtSmsAgnCode;
    }

    public void setTxtSmsAgnName(RichInputText txtSmsAgnName) {
        this.txtSmsAgnName = txtSmsAgnName;
    }

    public RichInputText getTxtSmsAgnName() {
        return txtSmsAgnName;
    }

    public void setTxtSmsQuotCode(RichInputText txtSmsQuotCode) {
        this.txtSmsQuotCode = txtSmsQuotCode;
    }

    public RichInputText getTxtSmsQuotCode() {
        return txtSmsQuotCode;
    }

    public void setTxtSmsQuotNo(RichInputText txtSmsQuotNo) {
        this.txtSmsQuotNo = txtSmsQuotNo;
    }

    public RichInputText getTxtSmsQuotNo() {
        return txtSmsQuotNo;
    }

    public void setTxtSmsPolCode(RichInputText txtSmsPolCode) {
        this.txtSmsPolCode = txtSmsPolCode;
    }

    public RichInputText getTxtSmsPolCode() {
        return txtSmsPolCode;
    }

    public void setTxtSmsPolNo(RichInputText txtSmsPolNo) {
        this.txtSmsPolNo = txtSmsPolNo;
    }

    public RichInputText getTxtSmsPolNo() {
        return txtSmsPolNo;
    }

    public void setTxtSmsClaimNo(RichInputText txtSmsClaimNo) {
        this.txtSmsClaimNo = txtSmsClaimNo;
    }

    public RichInputText getTxtSmsClaimNo() {
        return txtSmsClaimNo;
    }

    public void setTxtSmsTelNo(RichInputText txtSmsTelNo) {
        this.txtSmsTelNo = txtSmsTelNo;
    }

    public RichInputText getTxtSmsTelNo() {
        return txtSmsTelNo;
    }

    public void setTxtSmsStatus(RichSelectOneChoice txtSmsStatus) {
        this.txtSmsStatus = txtSmsStatus;
    }

    public RichSelectOneChoice getTxtSmsStatus() {
        return txtSmsStatus;
    }

    public void setTxtSmsPreparedBy(RichInputText txtSmsPreparedBy) {
        this.txtSmsPreparedBy = txtSmsPreparedBy;
    }

    public RichInputText getTxtSmsPreparedBy() {
        return txtSmsPreparedBy;
    }

    public void setTxtSmsPreparedDate(RichInputDate txtSmsPreparedDate) {
        this.txtSmsPreparedDate = txtSmsPreparedDate;
    }

    public RichInputDate getTxtSmsPreparedDate() {
        return txtSmsPreparedDate;
    }

    public void setTxtSmsSendDate(RichInputDate txtSmsSendDate) {
        this.txtSmsSendDate = txtSmsSendDate;
    }

    public RichInputDate getTxtSmsSendDate() {
        return txtSmsSendDate;
    }

    public void setTxtSmsUserCode(RichInputText txtSmsUserCode) {
        this.txtSmsUserCode = txtSmsUserCode;
    }

    public RichInputText getTxtSmsUserCode() {
        return txtSmsUserCode;
    }

    public void setTxtSmsMessageTemplate(RichInputText txtSmsMessageTemplate) {
        this.txtSmsMessageTemplate = txtSmsMessageTemplate;
    }

    public RichInputText getTxtSmsMessageTemplate() {
        return txtSmsMessageTemplate;
    }

    public void setTxtSmsMsgTemplateCode(RichInputText txtSmsMsgTemplateCode) {
        this.txtSmsMsgTemplateCode = txtSmsMsgTemplateCode;
    }

    public RichInputText getTxtSmsMsgTemplateCode() {
        return txtSmsMsgTemplateCode;
    }

    public void setTxtSearchShortDesc(RichInputText txtSearchShortDesc) {
        this.txtSearchShortDesc = txtSearchShortDesc;
    }

    public RichInputText getTxtSearchShortDesc() {
        return txtSearchShortDesc;
    }

    public void setTxtSearchName(RichInputText txtSearchName) {
        this.txtSearchName = txtSearchName;
    }

    public RichInputText getTxtSearchName() {
        return txtSearchName;
    }

    public void setTxtSearchOtherName(RichInputText txtSearchOtherName) {
        this.txtSearchOtherName = txtSearchOtherName;
    }

    public RichInputText getTxtSearchOtherName() {
        return txtSearchOtherName;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setRbtnPartOfAnyName(RichSelectBooleanRadio rbtnPartOfAnyName) {
        this.rbtnPartOfAnyName = rbtnPartOfAnyName;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyName() {
        return rbtnPartOfAnyName;
    }


    public void setRbtnExactName(RichSelectBooleanRadio rbtnExactName) {
        this.rbtnExactName = rbtnExactName;
    }

    public RichSelectBooleanRadio getRbtnExactName() {
        return rbtnExactName;
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
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


    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
    }

    public String tblClientPop() {
        // Add event code here...
        return null;
    }

    public void setSingleMessageBacking(RichPanelCollection singleMessageBacking) {
        this.singleMessageBacking = singleMessageBacking;
    }

    public RichPanelCollection getSingleMessageBacking() {
        return singleMessageBacking;
    }

    public void setTblmMsgTemplate(RichTable tblmMsgTemplate) {
        this.tblmMsgTemplate = tblmMsgTemplate;
    }

    public RichTable getTblmMsgTemplate() {
        return tblmMsgTemplate;
    }

    public void setTblSytems(RichTable tblSytems) {
        this.tblSytems = tblSytems;
    }

    public RichTable getTblSytems() {
        return tblSytems;
    }

    public void setSmsDesc(RichInputText smsDesc) {
        this.smsDesc = smsDesc;
    }

    public RichInputText getSmsDesc() {
        return smsDesc;
    }

    public void setSmsUrl(RichInputText smsUrl) {
        this.smsUrl = smsUrl;
    }

    public RichInputText getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUsername(RichInputText smsUsername) {
        this.smsUsername = smsUsername;
    }

    public RichInputText getSmsUsername() {
        return smsUsername;
    }

    public void setSmsPassword(RichInputText smsPassword) {
        this.smsPassword = smsPassword;
    }

    public RichInputText getSmsPassword() {
        return smsPassword;
    }

    public void setSmsSource(RichInputText smsSource) {
        this.smsSource = smsSource;
    }

    public RichInputText getSmsSource() {
        return smsSource;
    }

    public void setSmsDest(RichInputNumberSpinbox smsDest) {
        this.smsDest = smsDest;
    }

    public RichInputNumberSpinbox getSmsDest() {
        return smsDest;
    }

    public void setSmsMessage(RichInputText smsMessage) {
        this.smsMessage = smsMessage;
    }

    public RichInputText getSmsMessage() {
        return smsMessage;
    }

    public String testOut() {

        String desc = GlobalCC.checkNullValues(smsDesc.getValue());
        String url = GlobalCC.checkNullValues(smsUrl.getValue());
        String username = GlobalCC.checkNullValues(smsUsername.getValue());
        String password = GlobalCC.checkNullValues(smsPassword.getValue());
        String source = GlobalCC.checkNullValues(smsSource.getValue());
        String destination = GlobalCC.checkNullValues(smsDest.getValue());
        String message = GlobalCC.checkNullValues(smsMessage.getValue());
        try {
            if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Description");
                return null;
            }
            if (url == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:URL");
                return null;
            }

            if (username == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Username");
                return null;
            }
            if (password == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Password");
                return null;
            }
            if (source == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Source");
                return null;
            }
            if (destination == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Destination");
                return null;
            }
            if (message == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Message");
                return null;
            }

            url = url.replace("[USERNAME]", URLEncoder.encode(username));
            url = url.replace("[PASSWORD]", URLEncoder.encode(password));
            url = url.replace("[SOURCE]", URLEncoder.encode(source));
            url = url.replace("[DESTINATION]", URLEncoder.encode(destination));
            url = url.replace("[MESSAGE]", URLEncoder.encode(message));
            URL url2 = new URL(url);
            //URLEncoder.encode(arg0, arg1)
            HttpURLConnection connection =
                (HttpURLConnection)url2.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);

            System.out.println("connection :" + connection);
            String res = connection.getResponseMessage();

            System.out.println("res :" + res);

            int code = connection.getResponseCode();
            System.out.println("res :" + code);
            if (code == HttpURLConnection.HTTP_OK) {
                GlobalCC.sysInformation("Test SMS Sussessfully sent to:" +
                                        destination);
                connection.disconnect();
            } else {
                GlobalCC.errorValueNotEntered("Test SMS Not sent:" + res);
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String saveOut() {
        String code = GlobalCC.checkNullValues(smsCode.getValue());
        String desc = GlobalCC.checkNullValues(smsDesc.getValue());
        String url = GlobalCC.checkNullValues(smsUrl.getValue());
        String username = GlobalCC.checkNullValues(smsUsername.getValue());
        String password = GlobalCC.checkNullValues(smsPassword.getValue());
        String source = GlobalCC.checkNullValues(smsSource.getValue());
        String defaultVal = GlobalCC.checkNullValues(smsDefault.getValue());
        if (defaultVal == null) {
            defaultVal = "N";
        }
        if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Description");
            return null;
        }
        if (url == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:URL");
            return null;
        }

        if (username == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Username");
            return null;
        }
        if (password == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Password");
            return null;
        }
        if (source == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Source");
            return null;
        }


        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.outgoingSmsProc(?,?,?,?,?,?,?,?); end;";
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
            stmt.setString(3, desc);
            stmt.setString(4, url);
            stmt.setString(5, username);
            stmt.setString(6, password);
            stmt.setString(7, source);
            stmt.setString(8, defaultVal);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Successfully Updated");
            ADFUtils.findIterator("findOutgoingSMSIterator").executeQuery();
            GlobalCC.refreshUI(providers);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public String sendMessage() {

        if (session.getAttribute("multiAccount") == null) {
            GlobalCC.errorValueNotEntered("Error: No Client/Agent Selected");
            return null;
        }
        String message = GlobalCC.checkNullValues(txtSmsMsg.getValue());
        String url = null;
        String username = null;
        String password = null;
        String source = null;
        if (message == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Message");
            return null;
        }

        List<Map> accounts = new ArrayList<Map>();
        accounts = (List<Map>)session.getAttribute("multiAccount");
        Map<String, Object> accDetails = new HashMap<String, Object>();
        int t = 0;
        Object code = null;
        Object name = null;
        Object phoneNumber = null;
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        String saveQuery =
            "begin TQC_SMS_PKG.SEND_SMS_MSG(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        String settingsQuery =
            "begin ? := TQC_SETUPS_CURSOR.getDefaultSmsSettings; end;";
        String statusQuery = null;
        try {
            while (t < accounts.size()) {
                accDetails = accounts.get(t);
                code = accDetails.get("code");
                name = accDetails.get("name");
                phoneNumber = accDetails.get("sms");
                String regex = "^[+0-9]{10,25}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = null;
                boolean matches = false;
                if (phoneNumber != null) {
                    matcher = pattern.matcher(phoneNumber.toString());
                    matches = matcher.matches();
                }

                if (message == null || !matches || phoneNumber == null ||
                    name == null) {
                    GlobalCC.errorValueNotEntered("Selected Message and/or Phone Number for Client: " +
                                                  name + " Phone Number: " +
                                                  phoneNumber + "is Invalid.");
                    return null;
                }

                cst = (OracleCallableStatement)conn.prepareCall(saveQuery);
                //register out
                cst.setObject(1, code); //client Code
                cst.setObject(2, "0"); //Agent Code
                cst.setObject(3, null); //Quote Code
                cst.setObject(4, null); //Quote No
                cst.setObject(5, null); //Pol code
                cst.setObject(6, null); //Pol No
                cst.setObject(7, null); //Claim No
                cst.setObject(8, phoneNumber); //Tel No
                cst.setObject(9, message); //Sms Text
                cst.setObject(10, session.getAttribute("sysCode")); //Sys Code
                cst.setObject(11, "SR"); //Sys Mod
                cst.setObject(12, session.getAttribute("Username")); //user
                cst.registerOutParameter(13, OracleTypes.NUMBER); //Sms Code
                cst.execute();
                String smsCode = cst.getString(13);

                cst = null;

                // Get Settings
                cst = (OracleCallableStatement)conn.prepareCall(settingsQuery);
                //register out
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.execute();

                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                while (rs.next()) {
                    url = rs.getString(2);
                    username = rs.getString(3);
                    password = rs.getString(4);
                    source = rs.getString(5);

                }
                rs.close();
                cst.close();
				
				if(url==null)
                {
                        GlobalCC.sysInformation("Please configure SMS settings(Gateway)!");
                        return null;
                    }
					
                url = url.replace("[USERNAME]", URLEncoder.encode(username));
                url = url.replace("[PASSWORD]", URLEncoder.encode(password));
                url = url.replace("[SOURCE]", URLEncoder.encode(source));
                url =
url.replace("[DESTINATION]", URLEncoder.encode(phoneNumber.toString()));
                url = url.replace("[MESSAGE]", URLEncoder.encode(message));
                URL url2 = new URL(url);
                //URLEncoder.encode(arg0, arg1)
                HttpURLConnection connection =
                    (HttpURLConnection)url2.openConnection();
                connection.setDoOutput(false);
                connection.setDoInput(true);

                System.out.println("connection :" + connection);
                String res = connection.getResponseMessage();

                System.out.println("res :" + res);

                //Update Status
                statusQuery =
                        "UPDATE TQC_SMS_MESSAGES SET SMS_STATUS = '" + res +
                        "' WHERE SMS_CODE = " + smsCode + " ";
                cst = (OracleCallableStatement)conn.prepareCall(statusQuery);
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {

                    connection.disconnect();
                }

                cst.execute();
                cst.close();


                code = null;
                name = null;
                phoneNumber = null;
                t++;
            }

            accDetails = null;
            accounts = null;
            session.removeAttribute("multiAccount");
            txtSmsClientCode.setValue(null);
            txtSmsClientName.setValue(null);
            txtSmsTelNo.setValue(null);
            txtSmsMsgTemplateCode.setValue(null);
            txtSmsMsg.setValue(null);
            txtSmsMessageTemplate.setValue(null);
            GlobalCC.refreshUI(txtSmsMessageTemplate);
            GlobalCC.refreshUI(txtSmsMsgTemplateCode);
            GlobalCC.refreshUI(txtSmsMsg);
            GlobalCC.refreshUI(txtSmsClientCode);
            GlobalCC.refreshUI(txtSmsClientName);
            GlobalCC.refreshUI(txtSmsTelNo);

            conn.close();
            GlobalCC.sysInformation("SMS(s) Sussessfully sent");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String saveMessage() {

        if (session.getAttribute("multiAccount") == null) {
            GlobalCC.errorValueNotEntered("Error: No Client/Agent Selected");
            return null;
        }
        String message = GlobalCC.checkNullValues(txtSmsMsg.getValue());

        if (message == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing:Message");
            return null;
        }

        List<Map> accounts = new ArrayList<Map>();
        accounts = (List<Map>)session.getAttribute("multiAccount");
        Map<String, Object> accDetails = new HashMap<String, Object>();
        int t = 0;
        Object code = null;
        Object name = null;
        Object phoneNumber = null;
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();

        OracleCallableStatement cst = null;
        String saveQuery =
            "begin TQC_SMS_PKG.SEND_SMS_MSG(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try {
            while (t < accounts.size()) {
                accDetails = accounts.get(t);
                code = accDetails.get("code");
                name = accDetails.get("name");
                phoneNumber = accDetails.get("sms");
                String regex = "^[+0-9]{10,25}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = null;
                boolean matches = false;
                if (phoneNumber != null) {
                    matcher = pattern.matcher(phoneNumber.toString());
                    matches = matcher.matches();
                }
                if (!matches || phoneNumber == null) {
                    t++;
                    continue;

                }
                if (message == null || !matches || phoneNumber == null ||
                    name == null) {
                    GlobalCC.errorValueNotEntered("Selected Message and/or Phone Number for Client: " +
                                                  name + " Phone Number: " +
                                                  phoneNumber + "is Invalid.");
                    return null;
                }

                cst = (OracleCallableStatement)conn.prepareCall(saveQuery);
                //register out
                cst.setObject(1, code); //client Code
                cst.setObject(2, "0"); //Agent Code
                cst.setObject(3, null); //Quote Code
                cst.setObject(4, null); //Quote No
                cst.setObject(5, null); //Pol code
                cst.setObject(6, null); //Pol No
                cst.setObject(7, null); //Claim No
                cst.setObject(8, phoneNumber); //Tel No
                cst.setObject(9, message); //Sms Text
                cst.setObject(10, session.getAttribute("sysCode")); //Sys Code
                cst.setObject(11, "SR"); //Sys Mod
                cst.setObject(12, session.getAttribute("Username")); //user
                cst.registerOutParameter(13, OracleTypes.NUMBER); //Sms Code
                cst.execute();
                String smsCode = cst.getString(13);

                cst.close();
                code = null;
                name = null;
                phoneNumber = null;
                t++;
            }

            accDetails = null;
            accounts = null;

            session.removeAttribute("multiAccount");
            txtSmsClientCode.setValue(null);
            txtSmsClientName.setValue(null);
            txtSmsTelNo.setValue(null);
            txtSmsMsgTemplateCode.setValue(null);
            txtSmsMsg.setValue(null);
            txtSmsMessageTemplate.setValue(null);
            GlobalCC.refreshUI(txtSmsMessageTemplate);
            GlobalCC.refreshUI(txtSmsMsgTemplateCode);
            GlobalCC.refreshUI(txtSmsMsg);
            GlobalCC.refreshUI(txtSmsClientCode);
            GlobalCC.refreshUI(txtSmsClientName);
            GlobalCC.refreshUI(txtSmsTelNo);
            saveAction(conn);
            conn.close();
            GlobalCC.sysInformation("SMS Sussessfully saved for Future Sending");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String saveAction(OracleConnection conn) {

        try {

            String success = null;
            String qtCodeVal = null;
            String jobName =
                GlobalCC.checkNullValues(txtFutureDate.getValue());
            if (jobName != null) {
                jobName = GlobalCC.parseDate(txtFutureDate.getValue());
            } else {
                return null;
            }
            String assignee = session.getAttribute("UserCode").toString();
            String jobType = "A";
            String jobDesc = "Future SMS Message Alert";
            String notFailUser = session.getAttribute("UserCode").toString();
            String notSuccUser = session.getAttribute("UserCode").toString();

            String notFailTemp = "131";
            String notSuccTemp = "132";
            String statusVal = "A";
            String threshTypeVal = null;
            String threshValVal = null;
            String repeat = "N";
            String execObj = "1";


            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(GlobalCC.extractDate(txtFutureDate));
            int hour = cal.get(cal.HOUR_OF_DAY);
            int min = cal.get(cal.MINUTE);
            int year = cal.get(cal.YEAR);
            int month = cal.get(cal.MONTH) + 1;
            int day = cal.get(cal.DAY_OF_MONTH);
            int sec = cal.get(cal.SECOND);
            String cronjob = "";
            if (repeat.equals("N")) {
                cronjob =
                        sec + " " + min + " " + hour + " " + day + " " + month +
                        " ? " + " " + year;

            }
            // System.out.println(cronjob);
            conn = new DBConnector().getDatabaseConnection();
            CallableStatement cst3 = null;
            String Complete =
                "BEGIN tqc_setups_pkg.create_alerts(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);END;";
            cst3 = conn.prepareCall(Complete);
            cst3.setString(1, jobName);
            cst3.setString(2, jobDesc);
            cst3.setTimestamp(3, GlobalCC.extractTime(txtFutureDate));
            cst3.setTimestamp(4, null);
            System.out.println("This is the one ring" + qtCodeVal);
            cst3.setObject(5, qtCodeVal);
            cst3.setObject(6, session.getAttribute("sysCode"));
            cst3.setString(7, "NONE");
            cst3.setObject(8, assignee);
            cst3.setObject(9, notFailUser);
            cst3.setObject(10, notSuccUser);
            cst3.setObject(11, jobType);
            cst3.setObject(12, execObj); //(String)txtJobTemplate.getValue());
            cst3.setString(13, notFailTemp);
            cst3.setString(14, notSuccTemp);
            cst3.setString(15, "A");
            cst3.setString(16, cronjob);
            cst3.setString(17, statusVal);
            cst3.setString(18, repeat);
            cst3.setString(19, threshTypeVal);
            cst3.setString(20, threshValVal);
            cst3.execute();
            cst3.close();
            SystemAlerts alerts = new SystemAlerts();
            success = alerts.removeJob(jobName);
            if (success != null) {
                 ExternalContext ex =  FacesContext.getCurrentInstance().getExternalContext();
                ServletContext context = (ServletContext)ex.getContext();
                alerts.makeTrigger(jobName, cronjob,context,1);
            } else {
                return null;
            }


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String launchNewProvider() {
        smsCode.setValue(null);
        smsDesc.setValue(null);
        smsUrl.setValue(null);
        smsUsername.setValue(null);
        smsPassword.setValue(null);
        smsSource.setValue(null);
        smsDest.setValue(null);
        smsMessage.setValue(null);
        smsDefault.setValue("N");
        GlobalCC.showPopup("crm:provider");
        return null;
    }

    public String editProvider() {
        Object key2 = providers.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        smsCode.setValue(r.getAttribute("tssCode"));
        smsDesc.setValue(r.getAttribute("tssDesc"));
        smsUrl.setValue(r.getAttribute("tssUrl"));
        smsUsername.setValue(r.getAttribute("tssUsername"));
        smsPassword.setValue(r.getAttribute("tssPassword"));
        smsSource.setValue(r.getAttribute("tssSource"));
        smsDest.setValue(null);
        smsMessage.setValue(null);
        smsDefault.setValue(r.getAttribute("tssSource"));
        GlobalCC.showPopup("crm:provider");
        return null;
    }

    public String deleteProvider() {
        Object key2 = providers.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.outgoingSmsProc(?,?,?,?,?,?,?,?); end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, "D");
            stmt.setObject(2, r.getAttribute("tssCode"));
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.setObject(6, null);
            stmt.setObject(7, null);
            stmt.setObject(8, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.sysInformation("Record Successfully Updated");
            ADFUtils.findIterator("findOutgoingSMSIterator").executeQuery();
            GlobalCC.refreshUI(providers);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setProviders(RichTable providers) {
        this.providers = providers;
    }

    public RichTable getProviders() {
        return providers;
    }

    public void setSmsCode(RichInputNumberSpinbox smsCode) {
        this.smsCode = smsCode;
    }

    public RichInputNumberSpinbox getSmsCode() {
        return smsCode;
    }

    public void setSmsDefault(RichSelectOneChoice smsDefault) {
        this.smsDefault = smsDefault;
    }

    public RichSelectOneChoice getSmsDefault() {
        return smsDefault;
    }

    public void setCheckboxCol(RichColumn checkboxCol) {
        this.checkboxCol = checkboxCol;
    }

    public RichColumn getCheckboxCol() {
        return checkboxCol;
    }

    public void setColumnSelect(RichSelectBooleanCheckbox columnSelect) {
        this.columnSelect = columnSelect;
    }

    public RichSelectBooleanCheckbox getColumnSelect() {
        return columnSelect;
    }

    public void selectValueChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            RichSelectBooleanCheckbox checkValue =
                (RichSelectBooleanCheckbox)valueChangeEvent.getComponent();

            RowSet myRowSet;
            myRowSet =
                    (RowSet)ADFUtils.findIterator("fetchClientsByCriteriaIterator").findRowsByAttributeValue("code",
                                                                                                             true,
                                                                                                             checkValue.getLabel());

            Row row = myRowSet.next();

            // while(myRowSet.next()){
            String keyValue = null;
            keyValue = row.getKey().toString();
            keyValue = keyValue.replace("oracle.jbo.Key[", "");
            keyValue = keyValue.replace("]", "");
            keyValue = keyValue.replaceAll(" ", "");
            // busDiv.setValue(Integer.parseInt(keyValue));
            ADFUtils.findIterator("fetchClientsByCriteriaIterator").setCurrentRowWithKeyValue(keyValue);
            tblClientPop.setRowIndex(Integer.parseInt(keyValue));
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblClientPop.getRowData();
            if (nodeBinding != null) {
                nodeBinding.setAttribute("select",
                                         valueChangeEvent.getNewValue());
            }
        }
    }

    public String actionAcceptAccountTypes() {
        // GlobalCC.refreshUI(txtAgent);
        Object key2 = tblAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String mainAccType = nodeBinding.getAttribute("code").toString();
            txtMainAccountType.setValue(nodeBinding.getAttribute("accountType"));
            GlobalCC.refreshUI(txtMainAccountType);
            session.setAttribute("accType", nodeBinding.getAttribute("code"));
            session.setAttribute("accountTypeCode", mainAccType);
            session.setAttribute("agencyCode", null);

            // the demo tree
            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);
            System.out.println("This is the account Code" +
                               nodeBinding.getAttribute("code"));
        }
        GlobalCC.dismissPopUp("crm", "accountTypesPop");
        return null;
    }

    public void setTblAccountTypesPop(RichTable tblAccountTypesPop) {
        this.tblAccountTypesPop = tblAccountTypesPop;
    }

    public RichTable getTblAccountTypesPop() {
        return tblAccountTypesPop;
    }

    public void setTblAgencyInfo(RichTable tblAgencyInfo) {
        this.tblAgencyInfo = tblAgencyInfo;
    }

    public RichTable getTblAgencyInfo() {
        return tblAgencyInfo;
    }

    public String showAccountTypesPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:accountTypesPop" + "').show(hints);");
        return null;
    }

    public void setTxtMainAccountType(RichInputText txtMainAccountType) {
        this.txtMainAccountType = txtMainAccountType;
    }

    public RichInputText getTxtMainAccountType() {
        return txtMainAccountType;
    }

    public void setAccTypeBtn(RichCommandButton accTypeBtn) {
        this.accTypeBtn = accTypeBtn;
    }

    public RichCommandButton getAccTypeBtn() {
        return accTypeBtn;
    }

    public void clientSelectValChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("true")) {
                accTypeBtn.setDisabled(true);
                txtMainAccountType.setValue(null);
                GlobalCC.refreshUI(accTypeBtn);
                GlobalCC.refreshUI(txtMainAccountType);
            }
        }
    }

    public void otherSelectValChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("true")) {
                accTypeBtn.setDisabled(false);
                GlobalCC.refreshUI(accTypeBtn);
            }
        }
    }

    public void setRdOther(RichSelectBooleanRadio rdOther) {
        this.rdOther = rdOther;
    }

    public RichSelectBooleanRadio getRdOther() {
        return rdOther;
    }

    public void setRdClient(RichSelectBooleanRadio rdClient) {
        this.rdClient = rdClient;
    }

    public RichSelectBooleanRadio getRdClient() {
        return rdClient;
    }

    public String selectAccounts() {
        int k = 0;

        Object clientCode = null;
        Object clientName = null;
        Object smsVal = null;
        List<Map> clients = new ArrayList<Map>();

        while (k < tblAgencyInfo.getRowCount()) {
            Object key2 = tblAgencyInfo.getRowData(k);
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                //System.out.println(nodeBinding.getAttribute("code"));
                boolean select = (Boolean)nodeBinding.getAttribute("select");
                if (select == true) {
                    Map<String, Object> clientDetails =
                        new HashMap<String, Object>();
                    clientDetails.put("code",
                                      nodeBinding.getAttribute("code"));
                    clientDetails.put("name",
                                      nodeBinding.getAttribute("name"));
                    clientDetails.put("sms", nodeBinding.getAttribute("sms"));
                    if (clientCode == null) {
                        clientCode = nodeBinding.getAttribute("code");
                        if (nodeBinding.getAttribute("name") != null) {
                            clientName = nodeBinding.getAttribute("name");
                        }
                        if (nodeBinding.getAttribute("sms") != null) {
                            smsVal = nodeBinding.getAttribute("sms");
                        }
                    } else {
                        clientCode =
                                clientCode + "," + nodeBinding.getAttribute("code");
                        clientName =
                                clientName + "," + nodeBinding.getAttribute("name");
                        smsVal =
                                smsVal + "," + nodeBinding.getAttribute("sms");


                    }
                    clients.add(clientDetails);
                    clientDetails = null;
                }

            } else {
                GlobalCC.dismissPopUp("crm", "accounts");
                return null;
            }
            k++;
        }
        session.setAttribute("multiAccount", clients);
        clients = null;
        GlobalCC.dismissPopUp("crm", "accounts");
        if(clientName!=null) {
            clientName = clientName.toString().replace("null", "");
        }
        if(smsVal!=null) {
            smsVal = smsVal.toString().replace("null", "");
        }
        txtSmsClientCode.setValue(clientCode);
        txtSmsClientName.setValue(clientName);
        txtSmsTelNo.setValue(smsVal);
        GlobalCC.refreshUI(txtSmsClientCode);
        GlobalCC.refreshUI(txtSmsClientName);
        GlobalCC.refreshUI(txtSmsTelNo);
        return null;
    }

    public void accSelectValueChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue() != null) {
            RichSelectBooleanCheckbox checkValue =
                (RichSelectBooleanCheckbox)valueChangeEvent.getComponent();

            RowSet myRowSet;
            myRowSet =
                    (RowSet)ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").findRowsByAttributeValue("code",
                                                                                                                      true,
                                                                                                                      checkValue.getLabel());

            Row row = myRowSet.next();

            // while(myRowSet.next()){
            String keyValue = null;
            keyValue = row.getKey().toString();
            keyValue = keyValue.replace("oracle.jbo.Key[", "");
            keyValue = keyValue.replace("]", "");
            keyValue = keyValue.replaceAll(" ", "");
            // busDiv.setValue(Integer.parseInt(keyValue));
            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").setCurrentRowWithKeyValue(keyValue);
            tblAgencyInfo.setRowIndex(Integer.parseInt(keyValue));
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)tblAgencyInfo.getRowData();
            if (nodeBinding != null) {
                nodeBinding.setAttribute("select",
                                         valueChangeEvent.getNewValue());
            }
        }
    }

    public String ActionselectAll() {
        int i = 0;
        while (i < tblClientPop.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblClientPop.getRowData(i);
            r.setAttribute("select", true);
            columnSelect.setSelected(true);
            GlobalCC.refreshUI(columnSelect);
            i++;
        }
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);
        return null;
    }

    public String actionUnselectAll() {
        int i = 0;
        while (i < tblClientPop.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblClientPop.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("select", false);
            columnSelect.setSelected(false);
            GlobalCC.refreshUI(columnSelect);
            i++;
        }
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);
        return null;
    }

    public String selectAll() {
        int i = 0;
        while (i < tblAgencyInfo.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblAgencyInfo.getRowData(i);
            r.setAttribute("select", true);
            agentSelect.setSelected(true);
            GlobalCC.refreshUI(agentSelect);
            i++;
        }
        //ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblAgencyInfo);
        return null;
    }

    public String unselectAll() {
        int i = 0;
        while (i < tblAgencyInfo.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblAgencyInfo.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("select", false);
            agentSelect.setSelected(false);
            GlobalCC.refreshUI(agentSelect);
            i++;
        }
        //ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblAgencyInfo);
        return null;
    }

    public void setAgentSelect(RichColumn agentSelect) {
        this.agentSelect = agentSelect;
    }

    public RichColumn getAgentSelect() {
        return agentSelect;
    }

    public void setTxtFutureDate(RichInputDate txtFutureDate) {
        this.txtFutureDate = txtFutureDate;
    }

    public RichInputDate getTxtFutureDate() {
        return txtFutureDate;
    }
}
