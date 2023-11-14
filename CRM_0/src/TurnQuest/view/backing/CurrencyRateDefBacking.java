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


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;

import TurnQuest.view.dao.CurrencyDAO;
import TurnQuest.view.models.ExchangeRate;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
import oracle.jdbc.OracleTypes;
import java.util.Date;

//import java.sql.Date;


public class CurrencyRateDefBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblCurrency;
    private RichTable tblCurrencyRate;
    private RichInputText txtCrtCode;
    private RichInputText txtCrtCurCode;
    private RichInputNumberSpinbox txtCrtRate;
    private RichInputDate txtCrtDate;
    private RichInputText txtCrtBaseCurCode;
    private RichCommandButton btnSaveUpdateCurrencyRate;
    private RichInputText txtCrtCurDesc;
    private RichTable tblCurrencyPop;
    private RichPanelBox panelCurrencyRate;
    private RichCommandButton btnDeleteExchangeRates;
    private RichCommandButton btnEditExchangeRates;
    private RichCommandButton btnNewExchange;
    private RichInputDate txtWetDate;
    private RichInputDate txtWefDate;
    private RichInputText txtPreparedBy;
    private RichInputText txtPreparedDate;
    private RichInputText txtAuthorizedBy;
    private RichInputText txtAuthorizedDate;
    private RichInputText txtAuthorized;
    private RichCommandButton btnAuthorizeExchangeRates;

    public CurrencyRateDefBacking() {
    }

    public void setTblCurrency(RichTable tblCurrency) {
        this.tblCurrency = tblCurrency;
    }

    public RichTable getTblCurrency() {
        return tblCurrency;
    }

    public void setTblCurrencyRate(RichTable tblCurrencyRate) {
        this.tblCurrencyRate = tblCurrencyRate;
    }

    public RichTable getTblCurrencyRate() {
        return tblCurrencyRate;
    }

    public void setTxtCrtCode(RichInputText txtCrtCode) {
        this.txtCrtCode = txtCrtCode;
    }

    public RichInputText getTxtCrtCode() {
        return txtCrtCode;
    }

    public void setTxtCrtCurCode(RichInputText txtCrtCurCode) {
        this.txtCrtCurCode = txtCrtCurCode;
    }

    public RichInputText getTxtCrtCurCode() {
        return txtCrtCurCode;
    }

    public void setTxtCrtRate(RichInputNumberSpinbox txtCrtRate) {
        this.txtCrtRate = txtCrtRate;
    }

    public RichInputNumberSpinbox getTxtCrtRate() {
        return txtCrtRate;
    }

    public void setTxtCrtDate(RichInputDate txtCrtDate) {
        this.txtCrtDate = txtCrtDate;
    }

    public RichInputDate getTxtCrtDate() {
        return txtCrtDate;
    }

    public void setTxtCrtBaseCurCode(RichInputText txtCrtBaseCurCode) {
        this.txtCrtBaseCurCode = txtCrtBaseCurCode;
    }

    public RichInputText getTxtCrtBaseCurCode() {
        return txtCrtBaseCurCode;
    }

    public void setBtnSaveUpdateCurrencyRate(RichCommandButton btnSaveUpdateCurrencyRate) {
        this.btnSaveUpdateCurrencyRate = btnSaveUpdateCurrencyRate;
    }

    public RichCommandButton getBtnSaveUpdateCurrencyRate() {
        return btnSaveUpdateCurrencyRate;
    }

    public void setTxtCrtCurDesc(RichInputText txtCrtCurDesc) {
        this.txtCrtCurDesc = txtCrtCurDesc;
    }

    public RichInputText getTxtCrtCurDesc() {
        return txtCrtCurDesc;
    }

    public void setTblCurrencyPop(RichTable tblCurrencyPop) {
        this.tblCurrencyPop = tblCurrencyPop;
    }

    public RichTable getTblCurrencyPop() {
        return tblCurrencyPop;
    }

    public void setPanelCurrencyRate(RichPanelBox panelCurrencyRate) {
        this.panelCurrencyRate = panelCurrencyRate;
    }

    public RichPanelBox getPanelCurrencyRate() {
        return panelCurrencyRate;
    }

    public void tblCurrencyListener(SelectionEvent selectionEvent) {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("currencyCode",
                                 nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
            GlobalCC.refreshUI(tblCurrencyRate);
            btnNewExchange.setDisabled(false);
            btnEditExchangeRates.setDisabled(true);
            btnDeleteExchangeRates.setDisabled(true);

            GlobalCC.refreshUI(btnNewExchange);
            GlobalCC.refreshUI(btnEditExchangeRates);
            GlobalCC.refreshUI(btnDeleteExchangeRates);


        }
    }

    public void clearCurrencyRateFields() {
        try{
            /*
             * CRM-1431.CRM-1107 This method extends sets wef  and date to current date and then extends the wet date by extension_days parameter
             * */    
        txtCrtCode.setValue(null);
        txtCrtCurCode.setValue(null);
        txtCrtCurDesc.setValue(null);
        txtCrtRate.setValue(null);
        txtPreparedBy.setValue(null);
        txtPreparedDate.setValue(null);
        txtAuthorizedBy.setValue(null);
        txtAuthorizedDate.setValue(null);
        txtAuthorized.setValue(null);
        String szExt=GlobalCC.getSysParamValue("CURRENCY_RATE_EXTENSION_DAYS");
        int extDays=Integer.parseInt(szExt);
            System.out.println("extDays: "+extDays);
        java.util.Date now=new Date();
            Calendar c1 = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
            
            java.util.Date date=new Date();
            Calendar c2 = Calendar.getInstance();
                   c2.setTime(date);
                   c2.set(Calendar.HOUR_OF_DAY, 23);
                   c2.set(Calendar.MINUTE, 59);
                   c2.set(Calendar.SECOND, 59);
                   c2.set(Calendar.MILLISECOND, 999);
                   date= c2.getTime();
            //System.out.println("date=="+date);
        c.setTime(now); // Now use today date.
        c.add(Calendar.DATE, extDays); // Adding Extension days
        java.util.Date extDate=c.getTime();
        txtCrtDate.setValue(now);
        txtWefDate.setValue(now);
        txtWetDate.setValue(date);//txtWetDate.setValue(extDate);
        txtCrtBaseCurCode.setValue(session.getAttribute("currencyCode"));

        btnSaveUpdateCurrencyRate.setText("Save");
        GlobalCC.refreshUI(panelCurrencyRate);
        }catch(Exception e){
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }
    public void wefChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try{
        String szExt=GlobalCC.getSysParamValue("CURRENCY_RATE_EXTENSION_DAYS");
        int extDays=Integer.parseInt(szExt);
            System.out.println("extDays: "+extDays);
        java.util.Date wef=(java.util.Date)txtWefDate.getValue();
        Calendar c = Calendar.getInstance();
        c.setTime(wef); // Now wef  date.
        c.add(Calendar.DATE, extDays); // Adding Extension days
        java.util.Date extDate=c.getTime();
        txtWetDate.setValue(extDate);  
        GlobalCC.refreshUI(txtWetDate);
        }catch(Exception e){
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }
     

    public String actionNewCurrencyRate() {
        if (session.getAttribute("currencyCode") != null) {
            clearCurrencyRateFields();
            GlobalCC.showPopup( "crm:currencyRatePop");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Currency Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditCurrencyRate() {
        Object key2 = tblCurrencyRate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtCrtCode.setValue(nodeBinding.getAttribute("code"));
            txtCrtCurCode.setValue(nodeBinding.getAttribute("currencyCode"));
            txtCrtCurDesc.setValue(nodeBinding.getAttribute("currencyDesc"));
            txtCrtRate.setValue(nodeBinding.getAttribute("currRate"));
            txtCrtDate.setValue(nodeBinding.getAttribute("currDate"));
            txtWefDate.setValue(nodeBinding.getAttribute("crtWefDate"));
            txtWetDate.setValue(nodeBinding.getAttribute("crtWetDate"));
            txtCrtBaseCurCode.setValue(nodeBinding.getAttribute("baseCurrencyCode"));
            txtPreparedBy.setValue(nodeBinding.getAttribute("crtCreatedBy"));
            txtPreparedDate.setValue(nodeBinding.getAttribute("crtCreatedDate")); 
            txtAuthorizedBy.setValue(nodeBinding.getAttribute("crtUpdatedBy"));
            txtAuthorizedDate.setValue(nodeBinding.getAttribute("crtUpdatedDate"));
            txtAuthorized.setValue(nodeBinding.getAttribute("crtAuthorized"));

            btnSaveUpdateCurrencyRate.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:currencyRatePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteCurrencyRatebk() {
        Object key2 = tblCurrencyRate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();
            String curCode =
                nodeBinding.getAttribute("currencyCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2,
                                        code == null ? null : new BigDecimal(code));
                statement.setBigDecimal(3,
                                        curCode == null ? null : new BigDecimal(curCode));
                statement.setBigDecimal(4, null);
                statement.setDate(5, null);
                statement.setBigDecimal(6, null);
                statement.setDate(7, null);
                statement.setDate(8, null);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyRate);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

                btnEditExchangeRates.setDisabled(true);
                btnDeleteExchangeRates.setDisabled(true);
                //Refresh the buttons
                GlobalCC.refreshUI(btnNewExchange);
                GlobalCC.refreshUI(btnEditExchangeRates);
                GlobalCC.refreshUI(btnDeleteExchangeRates);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Currency Rate Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateCurrencyRateold() {
        
        String code = GlobalCC.checkNullValues(txtCrtCode.getValue());
        String curCode = GlobalCC.checkNullValues(txtCrtCurCode.getValue());
        String rate = GlobalCC.checkNullValues(txtCrtRate.getValue());
        String date = GlobalCC.checkNullValues(txtCrtDate.getValue());
        String wefdate = GlobalCC.checkNullValues(txtWefDate.getValue());
        String wetdate = GlobalCC.checkNullValues(txtWetDate.getValue());
        String baseCurCode =
            GlobalCC.checkNullValues(txtCrtBaseCurCode.getValue());

        System.out.println("wefdate: "+wefdate);
        if (curCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Currency Code is Empty");
            return null;

        } else if (rate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Currency Rate is Empty");
            return null;

        } else if (date == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Date is Empty");
            return null;

        } else if (wefdate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Wef Date is Empty");
            return null;

        } else if (wetdate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Wet Date is Empty");
            return null;

        } else if (baseCurCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Base Currency Code is Empty");
            return null;
        } else { 
            try {
                
                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpDate = new Date();
                if (txtCrtDate.getValue() != null &&
                    !(txtCrtDate.getValue().equals(""))) {
                    String date1 = df.format(txtCrtDate.getValue());
                    tmpDate = df.parse(date1);
                } 
                CurrencyDAO currencyDao = new CurrencyDAO();
                ExchangeRate item = new ExchangeRate();
                String valCreatedBy=GlobalCC.checkNullValues(session.getAttribute("UserName"));
                String valUpdatedBy=GlobalCC.checkNullValues(session.getAttribute("UserName"));
                java.sql.Date valCreatedDate= new java.sql.Date(new java.util.Date().getTime());
                java.sql.Date valUpdatedDate= new java.sql.Date(new java.util.Date().getTime());  
                
                item.setCode(GlobalCC.checkBDNullValues(code));
                item.setCurCode(GlobalCC.checkBDNullValues(curCode));
                item.setRate(GlobalCC.checkBDNullValues(rate));
                item.setDate(GlobalCC.extractTime(txtCrtDate));
                item.setBaseCurCode(GlobalCC.checkBDNullValues(baseCurCode));
                item.setWef(GlobalCC.extractTime(txtWefDate));
                item.setWet(GlobalCC.extractTime(txtWetDate));  
                
                String success="fail";
                if (btnSaveUpdateCurrencyRate.getText().equals("Edit")) {
                
                    item.setUpdatedBy(valUpdatedBy); 
                    item.setUpdatedDate(valUpdatedDate);
                    
                    success = currencyDao.updateExchangeRate(item);
                } else {
                    item.setCreatedBy(valCreatedBy); 
                    item.setCreatedDate(valCreatedDate);
                    
                     success = currencyDao.addExchangeRate(item);
                } 
                System.out.println("txtWefDate ..."+GlobalCC.extractTime(txtWefDate));
                System.out.println("txtWetDate ..."+GlobalCC.extractTime(txtWetDate));
                System.out.println("txtCrtDate ..."+GlobalCC.extractTime(txtCrtDate));
                
                if("success".equals(success)){
                    GlobalCC.hidePopup("crm:currencyRatePop");

                    ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyRate);

                    String message = "Record Saved Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                }  
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
        
        /*if (btnSaveUpdateCurrencyRate.getText().equals("Edit")) {
            actionUpdateCurrencyRate();
        } else {
            
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?,?,?,?); end;";

            statement =
                    (OracleCallableStatement)conn.prepareCall(query);

            // Take care of all the date fields on the form.

            statement =
                    (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "A");
            statement.setBigDecimal(2, code == null ? null : new BigDecimal(code));
            statement.setBigDecimal(3,   curCode == null ? null : new BigDecimal(curCode));
            statement.setBigDecimal(4, rate == null ? null : new BigDecimal(rate));
            statement.setTimestamp(5,GlobalCC.extractTime(txtCrtDate));
            statement.setBigDecimal(6, baseCurCode == null ? null : new BigDecimal(baseCurCode)); 
            statement.setTimestamp(7,GlobalCC.extractTime(txtWefDate)); 
            statement.setTimestamp(8,GlobalCC.extractTime(txtWetDate));
            statement.setBigDecimal(9, (BigDecimal)session.getAttribute("UserCode"));
            System.out.println("User code "+session.getAttribute("UserCode"));
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();
            System.out.println("txtWefDate ..."+GlobalCC.extractTime(txtWefDate));
            System.out.println("txtWetDate ..."+GlobalCC.extractTime(txtWetDate));
            System.out.println("txtCrtDate ..."+GlobalCC.extractTime(txtCrtDate));
            
            GlobalCC.hidePopup("crm:currencyRatePop");

            ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
            GlobalCC.refreshUI(tblCurrencyRate);

            String message = "New Record ADDED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
            //btnNewExchange.setDisabled(true);
            btnEditExchangeRates.setDisabled(true);
            btnDeleteExchangeRates.setDisabled(true);
            //Refresh the buttons
            GlobalCC.refreshUI(btnNewExchange);
            GlobalCC.refreshUI(btnEditExchangeRates);
            GlobalCC.refreshUI(btnDeleteExchangeRates);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }*/
        return null;
    }

    /*public String actionUpdateCurrencyRate() {
        String code = GlobalCC.checkNullValues(txtCrtCode.getValue());
        String curCode = GlobalCC.checkNullValues(txtCrtCurCode.getValue());
        String rate = GlobalCC.checkNullValues(txtCrtRate.getValue());
        String date = GlobalCC.checkNullValues(txtCrtDate.getValue());
        String wefdate = GlobalCC.checkNullValues(txtWefDate.getValue());
        String wetdate = GlobalCC.checkNullValues(txtWetDate.getValue());
        String baseCurCode =
            GlobalCC.checkNullValues(txtCrtBaseCurCode.getValue());

      System.out.println("wefdate: "+wefdate);
        if (curCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Currency Code is Empty");
            return null;

        } else if (rate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Currency Rate is Empty");
            return null;

        } else if (date == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Date is Empty");
            return null;

        } else if (wefdate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Wef Date is Empty");
            return null;

        } else if (wetdate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Wet Date is Empty");
            return null;

        } else if (baseCurCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Base Currency Code is Empty");
            return null;
        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpDate = new Date();
                if (txtCrtDate.getValue() != null &&
                    !(txtCrtDate.getValue().equals(""))) {
                    String date1 = df.format(txtCrtDate.getValue());
                    tmpDate = df.parse(date1);
                }

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setBigDecimal(2, GlobalCC.checkBDNullValues(code));
                statement.setBigDecimal(3, GlobalCC.checkBDNullValues(curCode));
                statement.setBigDecimal(4,GlobalCC.checkBDNullValues(rate)); 
                statement.setTimestamp(5,GlobalCC.extractTime(txtCrtDate));
                statement.setBigDecimal(6, GlobalCC.checkBDNullValues(baseCurCode)); 
                statement.setTimestamp(7,GlobalCC.extractTime(txtWefDate)); 
                statement.setTimestamp(8,GlobalCC.extractTime(txtWetDate));
                statement.setBigDecimal(9, (BigDecimal)session.getAttribute("UserCode"));
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();


                System.out.println("txtWefDate ..."+GlobalCC.extractTime(txtWefDate));
                System.out.println("txtWetDate ..."+GlobalCC.extractTime(txtWetDate));
                System.out.println("txtCrtDate ..."+GlobalCC.extractTime(txtCrtDate));
                GlobalCC.hidePopup("crm:currencyRatePop");

                ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyRate);

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }*/
    public String actionUpdateCurrencyRate() {
          
            return null;
        }

    public String actionAcceptCurrency() {
        Object key2 = tblCurrencyPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtCrtCurCode.setValue(nodeBinding.getAttribute("code"));
            txtCrtCurDesc.setValue(nodeBinding.getAttribute("description"));
        }

        GlobalCC.refreshUI(panelCurrencyRate);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:currenciesPop" + "').hide(hints);");
        return null;
    }

    public String actionShowCurrenciesPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:currenciesPop" + "').show(hints);");
        return null;
    }

    public String actionShowDeleteCurrenciesPop() {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodebinding = (JUCtrlValueBinding)key2;
        if (nodebinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmationDialog" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
        }
        return null;
    }

    public void actionConfirmDeleteExchangeRate(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) { //do nothing
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteCurrencyRate();
        }
    }


    public void tblCurrencyRateListener(SelectionEvent selectionEvent) {
        btnEditExchangeRates.setDisabled(false);
        btnDeleteExchangeRates.setDisabled(false);
        //Refresh the buttons
        GlobalCC.refreshUI(btnNewExchange);
        GlobalCC.refreshUI(btnEditExchangeRates);
        GlobalCC.refreshUI(btnDeleteExchangeRates);
    }

    public void setBtnDeleteExchangeRates(RichCommandButton btnDeleteExchangeRates) {
        this.btnDeleteExchangeRates = btnDeleteExchangeRates;
    }

    public RichCommandButton getBtnDeleteExchangeRates() {
        return btnDeleteExchangeRates;
    }

    public void setBtnEditExchangeRates(RichCommandButton btnEditExchangeRates) {
        this.btnEditExchangeRates = btnEditExchangeRates;
    }

    public RichCommandButton getBtnEditExchangeRates() {
        return btnEditExchangeRates;
    }

    public void setBtnNewExchange(RichCommandButton btnNewExchange) {
        this.btnNewExchange = btnNewExchange;
    }

    public RichCommandButton getBtnNewExchange() {
        return btnNewExchange;
    }


    public void setTxtWetDate(RichInputDate txtWetDate) {
        this.txtWetDate = txtWetDate;
    }

    public RichInputDate getTxtWetDate() {
        return txtWetDate;
    }

    public void setTxtWefDate(RichInputDate tstWefDate) {
        this.txtWefDate = tstWefDate;
    }

    public RichInputDate getTxtWefDate() {
        return txtWefDate;
    }
    public String actionSaveUpdateCurrencyRate() {
        
           DBConnector dbConnector = new DBConnector();
           OracleConnection conn = null;
           java.sql.Timestamp date = null;
           String code = GlobalCC.checkNullValues(txtCrtCode.getValue());
           String curCode = GlobalCC.checkNullValues(txtCrtCurCode.getValue());
           String rate = GlobalCC.checkNullValues(txtCrtRate.getValue());
           String crtAuthorized = null;
           
           try {
                       //date = new java.sql.Date(((Date)txtCrtDate.getValue()).getTime());
                       crtAuthorized = GlobalCC.checkNullValues(txtAuthorized.getValue());
                       
                   } catch (Exception e) {
                       crtAuthorized = null;
                   }
           try {
                       //date = new java.sql.Date(((Date)txtCrtDate.getValue()).getTime());
                       date = new java.sql.Timestamp(((Date)txtCrtDate.getValue()).getTime());
                       
                   } catch (Exception e) {
                       date = null;
                   }
           //String date = GlobalCC.checkNullValues(txtCrtDate.getValue());
           //String wefdate = GlobalCC.checkNullValues(txtWefDate.getValue());
           java.sql.Timestamp wefdate = null;
           try {
                       //date = new java.sql.Date(((Date)txtCrtDate.getValue()).getTime());
                       wefdate = new java.sql.Timestamp(((Date)txtWefDate.getValue()).getTime());
                       
                   } catch (Exception e) {
                       wefdate = null;
                   }
           java.sql.Timestamp wetdate = null;
           try {
                       //date = new java.sql.Date(((Date)txtCrtDate.getValue()).getTime());
                       wetdate = new java.sql.Timestamp(((Date)txtWetDate.getValue()).getTime());
                       
                   } catch (Exception e) {
                       wetdate = null;
                   }
           String baseCurCode =
           GlobalCC.checkNullValues(txtCrtBaseCurCode.getValue());
           BigDecimal crtCode = GlobalCC.checkBDNullValues(txtCrtCode.getValue());
           String action = "";
           if (curCode == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Currency Code is Empty");
               return null;

           } else if (rate == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Currency Rate is Empty");
               return null;

           } else if (date == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Date is Empty");
               return null;

           } else if (wefdate == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Wef Date is Empty");
               return null;

           } else if (wetdate == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Wet Date is Empty");
               return null;

           } else if (baseCurCode == null) {
               GlobalCC.errorValueNotEntered("Error Value Missing: Base Currency Code is Empty");
               return null;
           } 
                   
            else { 
               try {                   
                           if (btnSaveUpdateCurrencyRate.getText().equals("Edit")) {
                                               action = "E";
                                           } else {
                                               action = "A";
                                           } 
                                         DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                                         Date tmpDate = new Date();
                                         if (txtCrtDate.getValue() != null &&
                                             !(txtCrtDate.getValue().equals(""))) {
                                             String date1 = df.format(txtCrtDate.getValue());
                                             tmpDate = df.parse(date1);
                                         } 
                   
                           String valCreatedBy=GlobalCC.checkNullValues(session.getAttribute("UserName"));
                            String valUpdatedBy=GlobalCC.checkNullValues(session.getAttribute("UserName"));
                            java.sql.Date valCreatedDate= new java.sql.Date(new java.util.Date().getTime());
                            java.sql.Date valUpdatedDate= new java.sql.Date(new java.util.Date().getTime());
               
                           conn = dbConnector.getDatabaseConnection();

                           String query = null;
                           //modify the query for treaty groups procedure
                           query =
                                   "begin TQC_SETUPS_PKG.currency_prc(?,?,?,?,?,?,?,?,?,?,?); end;";


                           OracleCallableStatement callStmt = null;
                           callStmt = (OracleCallableStatement)conn.prepareCall(query);
                           //bind the variables

                           callStmt.setString(1, action);
                           callStmt.setObject(2, code);
                           callStmt.setObject(3, curCode);
                           callStmt.setObject(4, rate);
                           callStmt.setObject(5, date);//date
                           callStmt.setObject(6, baseCurCode);
                           callStmt.setObject(7, wefdate);//wefdate
                           callStmt.setObject(8, wetdate);//wetdate
                           callStmt.setObject(9, valCreatedBy);
                           callStmt.setObject(10, crtAuthorized);
                           callStmt.registerOutParameter(11, OracleTypes.VARCHAR);


                           callStmt.execute();
                           String errMessage = null;
                           errMessage = callStmt.getString(11);

                           callStmt.close();
                           conn.commit();
                           conn.close();

                           if (!GlobalCC.isEmptyStr(errMessage)) {
                               GlobalCC.INFORMATIONREPORTING(errMessage);
                               return null;
                           }
                           GlobalCC.hidePopup("crm:currencyRatePop");
                           ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                           GlobalCC.refreshUI(tblCurrencyRate);

                       } catch (Exception e) {
                           e.printStackTrace();
                           GlobalCC.EXCEPTIONREPORTING(conn, e);
                       }        
                       
           }
           return null;
           
       }

    public void setTxtPreparedBy(RichInputText txtPreparedBy) {
        this.txtPreparedBy = txtPreparedBy;
    }

    public RichInputText getTxtPreparedBy() {
        return txtPreparedBy;
    }

    public void setTxtPreparedDate(RichInputText txtPreparedDate) {
        this.txtPreparedDate = txtPreparedDate;
    }

    public RichInputText getTxtPreparedDate() {
        return txtPreparedDate;
    }

    public void setTxtAuthorizedBy(RichInputText txtAuthorizedBy) {
        this.txtAuthorizedBy = txtAuthorizedBy;
    }

    public RichInputText getTxtAuthorizedBy() {
        return txtAuthorizedBy;
    }

    public void setTxtAuthorizedDate(RichInputText txtAuthorizedDate) {
        this.txtAuthorizedDate = txtAuthorizedDate;
    }

    public RichInputText getTxtAuthorizedDate() {
        return txtAuthorizedDate;
    }
    public String actionDeleteCurrencyRate() {
        Object key2 = tblCurrencyRate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();
            String curCode =
                nodeBinding.getAttribute("currencyCode").toString(); 
            String crtAuthorized =
                nodeBinding.getAttribute("crtAuthorized").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currency_prc(?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2,
                                        code == null ? null : new BigDecimal(code));
                statement.setBigDecimal(3,
                                        curCode == null ? null : new BigDecimal(curCode));
                statement.setObject(4, null);
                statement.setObject(5, null);
                statement.setObject(6, null);
                statement.setObject(7, null);
                statement.setObject(8, null);
                statement.setObject(9, null);
                statement.setString(10, crtAuthorized);
                statement.registerOutParameter(11, OracleTypes.VARCHAR);
                statement.execute();
                
                String errMessage = null;
                errMessage = statement.getString(11);

                statement.close();
                conn.commit();
                conn.close();
                
                if (!GlobalCC.isEmptyStr(errMessage)) {
                    GlobalCC.INFORMATIONREPORTING(errMessage);
                    return null;
                }else
                {
                        String message = "Record DELETED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    }

                ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyRate);

                

                btnEditExchangeRates.setDisabled(true);
                btnDeleteExchangeRates.setDisabled(true);
                //Refresh the buttons
                GlobalCC.refreshUI(btnNewExchange);
                GlobalCC.refreshUI(btnEditExchangeRates);
                GlobalCC.refreshUI(btnDeleteExchangeRates);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Currency Rate Record to proceed.");
            return null;
        }
        return null;
    }

    public void setTxtAuthorized(RichInputText txtAuthorized) {
        this.txtAuthorized = txtAuthorized;
    }

    public RichInputText getTxtAuthorized() {
        return txtAuthorized;
    }

    public void setBtnAuthorizeExchangeRates(RichCommandButton btnAuthorizeExchangeRates) {
        this.btnAuthorizeExchangeRates = btnAuthorizeExchangeRates;
    }

    public RichCommandButton getBtnAuthorizeExchangeRates() {
        return btnAuthorizeExchangeRates;
    }
    public String actionAuthorizeCurrencyRate() {
        Object key2 = tblCurrencyRate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();
            String curCode =
                nodeBinding.getAttribute("currencyCode").toString(); 
            String crtAuthorized =
                nodeBinding.getAttribute("crtAuthorized").toString();
            String valAuthorizedBy=GlobalCC.checkNullValues(session.getAttribute("UserName"));
            String preparedBy =
                nodeBinding.getAttribute("crtCreatedBy").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                
                Authorization auth = new Authorization();
                String process = "OP";
                String processArea = "OPCR";
                String processSubArea = "OPCRA";
                String drCr = "N";
              
                
                String AccessGranted =
                    auth.checkMakerChekerApplicable(process, processArea, processSubArea,
                                         null, drCr,preparedBy);
                if (!"Y".equalsIgnoreCase(AccessGranted)) {
                    GlobalCC.INFORMATIONREPORTING("Exchange Rate Setup Cannot Be Prepared and Authorized By Same User.");
                    return null;
                }
                
                
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currency_prc(?,?,?,?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "R");
                statement.setBigDecimal(2,
                                        code == null ? null : new BigDecimal(code));
                statement.setBigDecimal(3,
                                        curCode == null ? null : new BigDecimal(curCode));
                statement.setObject(4, null);
                statement.setObject(5, null);
                statement.setObject(6, null);
                statement.setObject(7, null);
                statement.setObject(8, null);
                statement.setObject(9, valAuthorizedBy);
                statement.setString(10, crtAuthorized);
                statement.registerOutParameter(11, OracleTypes.VARCHAR);
                statement.execute();
                
                String errMessage = null;
                errMessage = statement.getString(11);

                statement.close();
                conn.commit();
                conn.close();
                
                if (!GlobalCC.isEmptyStr(errMessage)) {
                    GlobalCC.INFORMATIONREPORTING(errMessage);
                    return null;
                }else
                {
                        String message = "Record Authorized Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    }

                ADFUtils.findIterator("fetchCurrencyRatesByBaseCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyRate);

                

                btnEditExchangeRates.setDisabled(true);
                btnDeleteExchangeRates.setDisabled(true);
                //Refresh the buttons
                GlobalCC.refreshUI(btnNewExchange);
                GlobalCC.refreshUI(btnEditExchangeRates);
                GlobalCC.refreshUI(btnDeleteExchangeRates);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select transaction to be authorized.");
            return null;
        }
        return null;
    }
}
