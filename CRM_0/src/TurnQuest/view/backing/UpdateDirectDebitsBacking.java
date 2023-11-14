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
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.DirectDebit;
import TurnQuest.view.models.Holiday;
import TurnQuest.view.reports.ReportEngine;
   
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper; 
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.render.ClientEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class UpdateDirectDebitsBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private List rows = new ArrayList();
    private List rows2 = new ArrayList();
    private List rows3 = new ArrayList();
    private RichInputText txtBankName;
    private RichInputText txtBankBranch;
    private RichInputText txtBankBct;
    private RichInputDate txtBookingDate;
    private RichTable tblBankBranches;
    private RichTable tblClientRecords;
    private RichInputText txtBankCode;
    private RichInputText txtGenYear;
    private RichTable tblGenYears;
    private RichTable tblHolidays;
    private RichTable tblDirectDebit;
    private RichTable tblDirectDebitHeader;
    private RichTable tblDirectDebitDetail;
    private RichTable tblDirectDebit2;
    private RichTable tblDirectDebitHeader2;
    private RichTable tblDirectDebitDetail2;
    private RichInputText txtDdhCode;
    private RichInputDate txtFailDate;
    private RichInputDate txtReceiptDate;
    private RichInputText txtUpdateRemarks;
    private RichInputText txtDdhDdCode;
    private RichInputText txtBbrCode;
    private RichShowDetailItem panelAuthoriseDebits;
    private RichPanelBox panelHolidays;
    private RichInputDate txtHldDate;
    private RichInputText txtHldDesc;
    private RichCommandButton btnSaveHoliday;
    private RichTable directDReport;
    private RichCommandLink txtDirectDebit;
    private RichTable table;

    private int numberOfColumns;
    private int numberOfColumns2;
    private int numberOfColumns3;

    private RichCommandButton clearFailureUpdate;
    private RichCommandButton importFailureUpdate;
    private List<SelectItem> installmentDays = new ArrayList();
    private RichSelectOneChoice txtInstallDays;
    private RichInputDate txtBookingDateTo;
    private RichSelectOneRadio bankOrBranch;
    private RichPopup myGlassPane;
    private RichInputText txtProdDesc;
    private RichTable productsTab;

    public UpdateDirectDebitsBacking() {
    }

    public void setTxtBankName(RichInputText txtBankName) {
        this.txtBankName = txtBankName;
    }

    public RichInputText getTxtBankName() {
        return txtBankName;
    }

    public void setTxtBankBranch(RichInputText txtBankBranch) {
        this.txtBankBranch = txtBankBranch;
    }

    public RichInputText getTxtBankBranch() {
        return txtBankBranch;
    }

    public void setTxtBankBct(RichInputText txtBankBct) {
        this.txtBankBct = txtBankBct;
    }

    public RichInputText getTxtBankBct() {
        return txtBankBct;
    }

    public void setTxtBookingDate(RichInputDate txtBookingDate) {
        this.txtBookingDate = txtBookingDate;
    }

    public RichInputDate getTxtBookingDate() {
        return txtBookingDate;
    }

    public void setTblBankBranches(RichTable tblBankBranches) {
        this.tblBankBranches = tblBankBranches;
    }

    public RichTable getTblBankBranches() {
        return tblBankBranches;
    }

    public void setTblClientRecords(RichTable tblClientRecords) {
        this.tblClientRecords = tblClientRecords;
    }

    public RichTable getTblClientRecords() {
        return tblClientRecords;
    }

    public void setTxtBankCode(RichInputText txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public RichInputText getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtGenYear(RichInputText txtGenYear) {
        this.txtGenYear = txtGenYear;
    }

    public RichInputText getTxtGenYear() {
        return txtGenYear;
    }

    public void setTblGenYears(RichTable tblGenYears) {
        this.tblGenYears = tblGenYears;
    }

    public RichTable getTblGenYears() {
        return tblGenYears;
    }

    public void setTblHolidays(RichTable tblHolidays) {
        this.tblHolidays = tblHolidays;
    }

    public RichTable getTblHolidays() {
        return tblHolidays;
    }

    public void setTblDirectDebit(RichTable tblDirectDebit) {
        this.tblDirectDebit = tblDirectDebit;
    }

    public RichTable getTblDirectDebit() {
        return tblDirectDebit;
    }

    public void setTblDirectDebitHeader(RichTable tblDirectDebitHeader) {
        this.tblDirectDebitHeader = tblDirectDebitHeader;
    }

    public RichTable getTblDirectDebitHeader() {
        return tblDirectDebitHeader;
    }

    public void setTblDirectDebitDetail(RichTable tblDirectDebitDetail) {
        this.tblDirectDebitDetail = tblDirectDebitDetail;
    }

    public RichTable getTblDirectDebitDetail() {
        return tblDirectDebitDetail;
    }

    public void setTblDirectDebit2(RichTable tblDirectDebit2) {
        this.tblDirectDebit2 = tblDirectDebit2;
    }

    public RichTable getTblDirectDebit2() {
        return tblDirectDebit2;
    }

    public void setTblDirectDebitHeader2(RichTable tblDirectDebitHeader2) {
        this.tblDirectDebitHeader2 = tblDirectDebitHeader2;
    }

    public RichTable getTblDirectDebitHeader2() {
        return tblDirectDebitHeader2;
    }

    public void setTblDirectDebitDetail2(RichTable tblDirectDebitDetail2) {
        this.tblDirectDebitDetail2 = tblDirectDebitDetail2;
    }

    public RichTable getTblDirectDebitDetail2() {
        return tblDirectDebitDetail2;
    }

    public void setTxtDdhCode(RichInputText txtDdhCode) {
        this.txtDdhCode = txtDdhCode;
    }

    public RichInputText getTxtDdhCode() {
        return txtDdhCode;
    }

    public void setTxtFailDate(RichInputDate txtFailDate) {
        this.txtFailDate = txtFailDate;
    }

    public RichInputDate getTxtFailDate() {
        return txtFailDate;
    }

    public void setTxtUpdateRemarks(RichInputText txtUpdateRemarks) {
        this.txtUpdateRemarks = txtUpdateRemarks;
    }

    public RichInputText getTxtUpdateRemarks() {
        return txtUpdateRemarks;
    }

    public void setTxtDdhDdCode(RichInputText txtDdhDdCode) {
        this.txtDdhDdCode = txtDdhDdCode;
    }

    public RichInputText getTxtDdhDdCode() {
        return txtDdhDdCode;
    }

    public void setTxtBbrCode(RichInputText txtBbrCode) {
        this.txtBbrCode = txtBbrCode;
    }

    public RichInputText getTxtBbrCode() {
        return txtBbrCode;
    }

    public void setPanelAuthoriseDebits(RichShowDetailItem panelAuthoriseDebits) {
        this.panelAuthoriseDebits = panelAuthoriseDebits;
    }

    public RichShowDetailItem getPanelAuthoriseDebits() {
        return panelAuthoriseDebits;
    }

    public void setPanelHolidays(RichPanelBox panelHolidays) {
        this.panelHolidays = panelHolidays;
    }

    public RichPanelBox getPanelHolidays() {
        return panelHolidays;
    }

    public void setTxtHldDate(RichInputDate txtHldDate) {
        this.txtHldDate = txtHldDate;
    }

    public RichInputDate getTxtHldDate() {
        return txtHldDate;
    }

    public void setTxtHldDesc(RichInputText txtHldDesc) {
        this.txtHldDesc = txtHldDesc;
    }

    public RichInputText getTxtHldDesc() {
        return txtHldDesc;
    }

    public void setBtnSaveHoliday(RichCommandButton btnSaveHoliday) {
        this.btnSaveHoliday = btnSaveHoliday;
    }

    public RichCommandButton getBtnSaveHoliday() {
        return btnSaveHoliday;
    }

    public String actionAcceptBankBranch() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchBankBranchesIterator");
        RowKeySet set = tblBankBranches.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            txtBankCode.setValue(r.getAttribute("bankCode"));
            session.setAttribute("bankCodeVal", r.getAttribute("bankCode"));
            txtBankName.setValue(r.getAttribute("bankName"));
            txtBankBranch.setValue(r.getAttribute("bbrBranchName"));
            txtBankBct.setValue(r.getAttribute("bctNum"));
            txtBbrCode.setValue(r.getAttribute("bbrCode"));
            GlobalCC.refreshUI(txtBankName);
            GlobalCC.refreshUI(txtBankBct);
            GlobalCC.refreshUI(txtBankBranch);
        }
        GlobalCC.dismissPopUp("crm", "bankBranchPop");
        return null;
    }

    public String actionCancelBankBranch() {
        GlobalCC.dismissPopUp("crm", "bankBranchPop");
        return null;
    }

    public String actionShowBankBranch() {
        ADFUtils.findIterator("fetchBankBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblBankBranches);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:bankBranchPop" + "').show(hints);");
        return null;
    }

    public String actionSearchRecords() {
        String bankCode =
            GlobalCC.checkNullValues(this.txtBankCode.getValue());
        String bookingDate =
            GlobalCC.checkNullValues(this.txtBookingDate.getValue());
        String bookingDateTo =
            GlobalCC.checkNullValues(this.txtBookingDateTo.getValue());
        if (this.txtInstallDays.getValue() == null &&
            txtInstallDays.isVisible()) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Specify the Install Days...");
            return null;
        }

        if (bankCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank Code is Empty. Select the BANK BRANCH to proceed.");
            return null;

        } else if (bookingDate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Specify the Booking Date to proceed");
            return null;

        } else {

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                session.setAttribute("SEARCHED", "N");

                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.searchClients(?,?,?); end;";

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

                Date newBookingDate = null; //new Date();
                String date2 = null, date3 = null;
                if (txtBookingDate.getValue() != null &&
                    !(txtBookingDate.getValue().equals(""))) {
                    date2 = df.format(txtBookingDate.getValue());
                    newBookingDate = df.parse(date2);
                }

                if (txtBookingDateTo.getValue() != null &&
                    !(txtBookingDateTo.getValue().equals(""))) {
                    date3 = df.format(txtBookingDateTo.getValue());
                }
                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setDate(2, getSqlDate(date2, "/"));
                statement.setDate(3, getSqlDate(date3, "/"));
                statement.execute();
                statement.close();
                conn.commit();
                conn.close();
                this.session.setAttribute("InstallDay",
                                          this.txtInstallDays.getValue());
                session.setAttribute("bankCode", bankCode);
                session.setAttribute("searchDate", getSqlDate(date2, "/"));
                session.setAttribute("searchDateTo", getSqlDate(date3, "/"));
                Rendering rend = new Rendering();
                if (rend.getLmsActive() != null) {
                    session.setAttribute("systemCode",
                                         27); // 27 is the LMS code
                } else {
                    session.setAttribute("systemCode",
                                         37); // 37 is the GIS code
                }

                rend = null;

                ADFUtils.findIterator("fetchClientRecordsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientRecords);

                session.setAttribute("SEARCHED", "Y");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionGenerateDebits() {
        String bankCode = GlobalCC.checkNullValues(txtBankCode.getValue());
        String bookingDate =
            GlobalCC.checkNullValues(txtBookingDate.getValue());
        String ddBbrCode = GlobalCC.checkNullValues(txtBbrCode.getValue());

        String bankAccount = GlobalCC.checkNullValues(txtBankBct.getValue());
        /*if (ddBbrCode == null && bankAccount == null) {
            ddBbrCode = GlobalCC.checkNullValues(txtBankCode.getValue());
            bankAccount = GlobalCC.checkNullValues(txtBankCode.getValue());
        }*/
        String installDays =
            GlobalCC.checkNullValues(txtInstallDays.getValue());
        if (installDays == null && txtInstallDays.isVisible() &&
            txtInstallDays.isRequired()) {
            GlobalCC.errorValueNotEntered("Select Installment Day...");
            return null;
        }
        if (bankCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Bank Code is Empty. Select the BANK BRANCH to proceed.");
            return null;

        } else if (bookingDate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Specify the Booking Date to proceed");
            return null;

        } else if (ddBbrCode == null && session.getAttribute("_b_or_bb").toString().equalsIgnoreCase("BB")) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select the BANK BRANCH to proceed");
            return null;

        } else if (bankAccount == null && session.getAttribute("_b_or_bb").toString().equalsIgnoreCase("BB")) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Specify the Company Direct Debit A/C to proceed");
            return null;

        } else if (session.getAttribute("SEARCHED") == null) {
            GlobalCC.errorValueNotEntered("First search for records to proceed.");
            return null;

        } else {
            
            if(session.getAttribute("_b_or_bb").toString().equalsIgnoreCase("B")){
                ddBbrCode = null;
            }

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.generateDirectDebitRecs(?,?,?,?,?,?,?,?,?); end;";

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

                String date2 = null;
                if (txtBookingDate.getValue() != null &&
                    !(txtBookingDate.getValue().equals(""))) {
                    date2 = df.format(txtBookingDate.getValue());
                }

                String date3 = null;
                if (txtBookingDateTo.getValue() != null &&
                    !(txtBookingDateTo.getValue().equals(""))) {
                    date3 = df.format(txtBookingDateTo.getValue());
                }

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setString(2,
                                    session.getAttribute("SEARCHED").toString());
                statement.setString(3, ddBbrCode);
                statement.setString(4, bankAccount);
                statement.setDate(5, getSqlDate(date2, "/"));
                statement.setObject(6, this.txtInstallDays.getValue());
                statement.setObject(7, getSqlDate(date3, "/"));
                statement.setString(8, bankCode);
                statement.setBigDecimal(9, (BigDecimal)session.getAttribute("productCode"));
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Debits successfully  generated::Please  don't  regenerate ");
                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                panelAuthoriseDebits.setDisclosed(true);


                ADFUtils.findIterator("fetchDirectDebitNonReceiptIterator").executeQuery();
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblDirectDebit);

            } catch (Exception e) {
                //Rreport Backend Errors
                e.printStackTrace();

                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    /** Get a String (yyyy/mm/dd) and returns a java.sql.Date.
     * It is useful to get user input and insert it in
     * the database like MySQL.
     *
     * @param strDate a String in the format dd/mm/yyyy
     * @return java.sql.Date
     */
    public static java.sql.Date getSqlDate(String strDate) {
        String[] splitedDate = strDate.split("-");
        int year = Integer.parseInt(splitedDate[0]) - 1900;
        int month = Integer.parseInt(splitedDate[1]) - 1;
        int day = Integer.parseInt(splitedDate[2]);

        return new java.sql.Date(year, month, day);
    }

    public static java.sql.Date getSqlDate(String strDate,
                                           String regexSeparator) {
        String[] splitedDate = strDate.split(regexSeparator);
        int year = Integer.parseInt(splitedDate[0]) - 1900;
        int month = Integer.parseInt(splitedDate[1]) - 1;
        int day = Integer.parseInt(splitedDate[2]);

        return new java.sql.Date(year, month, day);
    }

    public String actionShowYears() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:yearsPop" +
                             "').show(hints);");
        return null;
    }

    public String actionAcceptYear() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchHolidayYearsLovIterator");
        RowKeySet set = tblGenYears.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            txtGenYear.setValue(r.getAttribute("HYear"));
            session.setAttribute("genYear", r.getAttribute("HYear"));

            ADFUtils.findIterator("fetchHolidaysIterator").executeQuery();
            GlobalCC.refreshUI(tblHolidays);
            GlobalCC.refreshUI(panelHolidays);
        }
        GlobalCC.dismissPopUp("crm", "yearsPop");
        return null;
    }

    public String actionGenerateHolidays() {
        if (txtGenYear.getValue() != null) {
            String year = txtGenYear.getValue().toString();
            if (year.matches("\\d{4}")) {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;

                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin Direct_Debit_Pkg.Generate_Fixed_holidays(?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setBigDecimal(1,
                                            new BigDecimal(txtGenYear.getValue().toString()));
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    session.setAttribute("genYear",
                                         txtGenYear.getValue().toString());

                    ADFUtils.findIterator("fetchHolidayYearsLovIterator").executeQuery();
                    GlobalCC.refreshUI(tblGenYears);

                    ADFUtils.findIterator("fetchHolidaysIterator").executeQuery();
                    GlobalCC.refreshUI(tblHolidays);

                    GlobalCC.refreshUI(panelHolidays);
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("Invalid year. Year must be 4 digits e.g. 2010");
                return null;
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select the Year.");
        }
        return null;
    }

    public void refreshHolidayFields() {
        txtHldDate.setValue(null);
        txtHldDesc.setValue(null);

        txtHldDate.setDisabled(false);
        btnSaveHoliday.setText("Save");
    }

    public String actionNewHoliday() {
        if (session.getAttribute("genYear") != null) {
            refreshHolidayFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:holidayPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select Year.");
        }
        return null;
    }

    public String actionEditHoliday() {
        RowKeySet rowKeySet = tblHolidays.getSelectedRowKeys();

        if (rowKeySet == null) {
            return null;
        }

        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        tblHolidays.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)tblHolidays.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }

        txtHldDate.setValue(r.getAttribute("hldDate"));
        txtHldDesc.setValue(r.getAttribute("hldDesc"));

        txtHldDate.setDisabled(true);
        btnSaveHoliday.setText("Edit");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:holidayPop" + "').show(hints);");
        return null;
    }

    public String actionDeleteHoliday() {
        RowKeySet rowKeySet = tblHolidays.getSelectedRowKeys();

        if (rowKeySet == null) {
            return null;
        }

        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        tblHolidays.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)tblHolidays.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }

        String holdDate = r.getAttribute("hldDate").toString();
        String holDesc = r.getAttribute("hldDesc").toString();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.holidays_prc(?,?); end;";

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_TAB", conn);
            ArrayList holidayList = new ArrayList();
            Holiday holiday = new Holiday();
            holiday.setSQLTypeName("TQC_HOLIDAYS_OBJ");

            holiday.setHldDate(getSqlDate(r.getAttribute("hldDate").toString()));
            holiday.setHldDesc(holDesc);

            holidayList.add(holiday);
            ARRAY array = new ARRAY(descriptor, conn, holidayList.toArray());

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "D");
            statement.setArray(2, array);
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchHolidaysIterator").executeQuery();
            GlobalCC.refreshUI(tblHolidays);

            ADFUtils.findIterator("fetchHolidayYearsLovIterator").executeQuery();
            GlobalCC.refreshUI(tblGenYears);

            String message = "Record DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String actionSaveUpdateHoliday() {
        if (btnSaveHoliday.getText().equals("Edit")) {
            actionUpdateHoliday();
        } else {
            String holDate = GlobalCC.checkNullValues(txtHldDate.getValue());
            String holDesc = GlobalCC.checkNullValues(txtHldDesc.getValue());

            if (holDate == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Holiday Date is Empty");
                return null;

            } else if (holDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Holiday Description is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.holidays_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_TAB",
                                                         conn);
                    ArrayList holidayList = new ArrayList();
                    Holiday holiday = new Holiday();
                    holiday.setSQLTypeName("TQC_HOLIDAYS_OBJ");

                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat simpleDateformat =
                        new SimpleDateFormat("yyyy");

                    Date newHldDate = null; //new Date();
                    if (txtHldDate.getValue() != null &&
                        !(txtHldDate.getValue().equals(""))) {
                        String date2 = df.format(txtHldDate.getValue());
                        newHldDate = df.parse(date2);
                    }

                    if (!simpleDateformat.format(newHldDate).toString().trim().equals(session.getAttribute("genYear").toString().trim())) {
                        GlobalCC.INFORMATIONREPORTING("The selected Date is not within the specified Year. Please enter a date in " +
                                                      session.getAttribute("genYear").toString() +
                                                      " year.");
                        return null;
                    } else {
                        holiday.setHldDate(newHldDate == null ? null :
                                           new java.sql.Date(newHldDate.getTime()));
                        holiday.setHldDesc(holDesc);

                        holidayList.add(holiday);
                        ARRAY array =
                            new ARRAY(descriptor, conn, holidayList.toArray());

                        statement =
                                (OracleCallableStatement)conn.prepareCall(query);
                        statement.setString(1, "A");
                        statement.setArray(2, array);
                        statement.execute();

                        statement.close();
                        conn.commit();
                        conn.close();

                        ADFUtils.findIterator("fetchHolidaysIterator").executeQuery();
                        GlobalCC.refreshUI(tblHolidays);

                        ADFUtils.findIterator("fetchHolidayYearsLovIterator").executeQuery();
                        GlobalCC.refreshUI(tblGenYears);

                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "crm:holidayPop" +
                                             "').hide(hints);");

                        refreshHolidayFields();

                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateHoliday() {
        String holDate = GlobalCC.checkNullValues(txtHldDate.getValue());
        String holDesc = GlobalCC.checkNullValues(txtHldDesc.getValue());

        if (holDate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Holiday Date is Empty");
            return null;

        } else if (holDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Holiday Description is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query = "begin TQC_SETUPS_PKG.holidays_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_TAB", conn);
                ArrayList holidayList = new ArrayList();
                Holiday holiday = new Holiday();
                holiday.setSQLTypeName("TQC_HOLIDAYS_OBJ");

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat simpleDateformat =
                    new SimpleDateFormat("yyyy");

                Date newHldDate = null; //new Date();
                if (txtHldDate.getValue() != null &&
                    !(txtHldDate.getValue().equals(""))) {
                    String date2 = df.format(txtHldDate.getValue());
                    newHldDate = df.parse(date2);
                }

                if (!simpleDateformat.format(newHldDate).toString().trim().equals(session.getAttribute("genYear").toString().trim())) {
                    GlobalCC.INFORMATIONREPORTING("The selected Date is not within the specified Year. Please enter a date in " +
                                                  session.getAttribute("genYear").toString() +
                                                  " year.");
                    return null;
                } else {
                    holiday.setHldDate(newHldDate == null ? null :
                                       new java.sql.Date(newHldDate.getTime()));
                    holiday.setHldDesc(holDesc);

                    holidayList.add(holiday);
                    ARRAY array =
                        new ARRAY(descriptor, conn, holidayList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "E");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchHolidaysIterator").executeQuery();
                    GlobalCC.refreshUI(tblHolidays);

                    ADFUtils.findIterator("fetchHolidayYearsLovIterator").executeQuery();
                    GlobalCC.refreshUI(tblGenYears);

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:holidayPop" + "').hide(hints);");

                    refreshHolidayFields();

                    String message = "Record UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void tblDirectDebitListener(SelectionEvent selectionEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchDirectDebitNonReceiptIterator");
        RowKeySet set = tblDirectDebit.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        // refreshes the Generated Records table
        session.setAttribute("ddhCode", null);
        ADFUtils.findIterator("fetchDirectDebitDetailsIterator").executeQuery();
        GlobalCC.refreshUI(tblDirectDebitDetail);

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            String ddCode = r.getAttribute("ddCode").toString();
            String ddStatus = r.getAttribute("ddStatus").toString();
            session.setAttribute("ddCode", ddCode);
            session.setAttribute("ddStatus", ddStatus);
            session.setAttribute("refNo", r.getAttribute("ddRefNo"));
            session.setAttribute("bankCode", r.getAttribute("ddBankCode"));
            session.setAttribute("bankCodeVal", r.getAttribute("ddBankCode"));
            session.setAttribute("bankCodeVal", r.getAttribute("ddBankCode"));
            session.setAttribute("bbrCode", r.getAttribute("ddBbrCode"));
            ADFUtils.findIterator("fetchDirectDebitHeadersIterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitHeader);
        }
    }

    public void tblDirectDebitHeaderListener(SelectionEvent selectionEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchDirectDebitHeadersIterator");
        RowKeySet set = tblDirectDebitHeader.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            String ddhCode = r.getAttribute("ddhCode").toString();
            session.setAttribute("ddhCode", ddhCode);

            ADFUtils.findIterator("fetchDirectDebitDetailsIterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitDetail);
        }
    }

    public String actionDeleteDirectDebit() {
        RowKeySet rowKeySet = tblDirectDebit.getSelectedRowKeys();

        if (rowKeySet == null) {
            return null;
        }

        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        tblDirectDebit.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)tblDirectDebit.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;

        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.deleteDirectDebit(?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1,
                                session.getAttribute("Username").toString());
            statement.setBigDecimal(2,
                                    new BigDecimal(r.getAttribute("ddCode").toString()));
            statement.setString(3, r.getAttribute("ddStatus").toString());
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchDirectDebitNonReceiptIterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebit);

            ADFUtils.findIterator("fetchDirectDebitHeadersIterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitHeader);

            ADFUtils.findIterator("fetchDirectDebitDetailsIterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitDetail);

            String message = "Batch DELETED successully.";
            GlobalCC.INFORMATIONREPORTING(message);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }
        return null;
    }

    public String actionDirectDebit() {
        Rendering dd = new Rendering();
        if (dd.getLmsActive() != null) {
            actionPrintDirectDebit();
        } else {
            actionPrintDirectDebitPrepare();
            exportDirectText();
        }
        return null;
    }

    public String actionAuthorizeDirectDebit() {
        Object key2 = tblDirectDebit.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.authoriseDirectDebit(?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setBigDecimal(2,
                                        new BigDecimal(nodeBinding.getAttribute("ddCode").toString()));
                statement.setString(3,
                                    nodeBinding.getAttribute("ddStatus").toString());
                statement.execute();
                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDirectDebitNonReceiptIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit);

                ADFUtils.findIterator("fetchDirectDebitHeadersIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader);

                ADFUtils.findIterator("fetchDirectDebitDetailsIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail);

                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                String message = "Batch AUTHORIZED successfully.";
                GlobalCC.INFORMATIONREPORTING(message);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record is selected.");
            return null;
        }
        return null;
    }

    public String actionRemoveDirectDebitDetail() {
        RowKeySet rowKeySet = tblDirectDebitDetail.getSelectedRowKeys();

        if (rowKeySet == null) {
            return null;
        }

        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        tblDirectDebitDetail.setRowKey(key2);
        JUCtrlValueBinding r =
            (JUCtrlValueBinding)tblDirectDebitDetail.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("You need to select a Record first.");
            return null;
        }

        if (session.getAttribute("ddStatus") != null &&
            session.getAttribute("ddStatus").toString().equalsIgnoreCase("D")) {

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.deleteDirectDebitDetail(?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setBigDecimal(2,
                                        new BigDecimal(r.getAttribute("dddCode").toString()));
                statement.setString(3,
                                    session.getAttribute("ddStatus").toString());
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDirectDebitDetailsIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail);

                String message = "Record REMOVED successully.";
                GlobalCC.INFORMATIONREPORTING(message);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            if (session.getAttribute("ddStatus") != null) {
                GlobalCC.EXCEPTIONREPORTING("Records with status " +
                                            session.getAttribute("ddStatus").toString() +
                                            " cannot be removed.");
            } else {
                GlobalCC.INFORMATIONREPORTING("Unable to determine the Batch status. Please select a Batch first.");
            }
        }
        return null;
    }

    public void tblDirectDebit2Listener(SelectionEvent selectionEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator");
        RowKeySet set = tblDirectDebit2.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            String ddCode = r.getAttribute("ddCode").toString();
            String ddReceipted = r.getAttribute("ddReceipted").toString();
            session.setAttribute("ddCode", ddCode);
            session.setAttribute("ddReceipted", ddReceipted);
            session.setAttribute("ddRefNo", r.getAttribute("ddCode"));
            session.setAttribute("ddBookDate", r.getAttribute("ddBookDate"));

            ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitHeader2);
        }
    }

    public void tblDirectDebitHeader2Listener(SelectionEvent selectionEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator");
        RowKeySet set = tblDirectDebitHeader2.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            String ddhCode = r.getAttribute("ddhCode").toString();
            session.setAttribute("ddhCode", ddhCode);

            ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
            GlobalCC.refreshUI(tblDirectDebitDetail2);
        }
    }

    public void tblDirectDebitDebitListener(SelectionEvent selectionEvent) {
        /* DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchDirectDebitDetails2Iterator");
        RowKeySet set = tblDirectDebitHeader2.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));
            Row r = dciter.getCurrentRow();
            String ddhCode = r.getAttribute("dddCode").toString();
            session.setAttribute("dddCode", ddhCode);
        }*/
    }
    
    public void launchPopUp(ActionEvent actionEvent) {
        GlobalCC.showPopup("crm:glassPane");
    }
    
    public void allocateReceipt(ClientEvent clientEvent) {
        actionAllocateReceipt();
        GlobalCC.hidePopup("crm:glassPane");

    }

    public String actionAllocateReceipt() {
        Object key2 = tblDirectDebit2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;

        if (nodeBinding != null) {

            System.out.println("session.ddBookDate: " +
                               session.getAttribute("ddBookDate").toString());

            BigDecimal ddCode =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("ddCode"));
            String ddReceiptCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("DD_RECEIPT_NO"));
            String AllocStatus =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("DD_ALLOC_STATUS_CODE"));
            

            if (ddCode == null) {
                GlobalCC.INFORMATIONREPORTING("Please select direct debit batch!.");
                return null;
            }
            if (ddReceiptCode == null) {
                GlobalCC.INFORMATIONREPORTING("The Direct Debit Batch has no receipt. Generate a Receipt before Allocating.");
                return null;
            }
            if (AllocStatus.equalsIgnoreCase("S")) {
                GlobalCC.INFORMATIONREPORTING("The Direct Debit Batch has already been succesfully allocated");
                return null;
            }


            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                //String query =
                //    "begin TQC_SETUPS_PKG.generateReceipts(?,?,?,?,?); end;";
                String query =
                    "begin FMS_RCTS_PKG.postddreceiptallocation(?); end;";
                
                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setBigDecimal(1, ddCode);
                //statement.setString(2,
                //                    session.getAttribute("Username").toString());
                
                //statement.setString(3,
                //                    nodeBinding.getAttribute("ddReceipted").toString());
                //statement.setDate(4, new java.sql.Date( new java.util.Date(r.getAttribute("ddBookDate").toString()).getTime() ));
                //statement.setDate(4, new java.sql.Date( new SimpleDateFormat("yyyy/MM/dd").parse(r.getAttribute("ddBookDate").toString()).getTime() )); // Throws Unparsable date Exception
                //statement.setDate(4, bookingDate);
                //statement.setDate(5, receiptDate);
                statement.execute();
                statement.close();
                conn.commit();
                conn.close();
                
                


                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader2);

                ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail2);

                String message = "Receipts Allocation Complete. Please Check Status for results.";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            } finally {
                DbUtils.closeQuietly(conn, statement, null);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a record to proceed.");
            return null;
        }
        return null;
    }
    
    public String actionGenerateReceipt() {
        Object key2 = tblDirectDebit2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;

        if (nodeBinding != null) {

            System.out.println("session.ddBookDate: " +
                               session.getAttribute("ddBookDate").toString());

            java.sql.Date bookingDate =
                getSqlDate(nodeBinding.getAttribute("ddBookDate").toString());
            java.sql.Date receiptDate = GlobalCC.extractDate(txtReceiptDate);
            java.sql.Date currentDate =
                new java.sql.Date(new java.util.Date().getTime());
            BigDecimal ddCode =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("ddCode"));
            String ddReceiptCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("DD_RECEIPT_NO"));

            if (ddCode == null) {
                GlobalCC.INFORMATIONREPORTING("Please select direct debit batch!.");
                return null;
            }
            
            if (ddReceiptCode != null) {
                GlobalCC.INFORMATIONREPORTING("The Direct Debit Batch Already has a Receipt.");
                return null;
            }
            
            if (bookingDate == null) {
                GlobalCC.INFORMATIONREPORTING("Booking date not provided!.");
                return null;
            }
            if (receiptDate == null) {
                GlobalCC.INFORMATIONREPORTING("Receipt date not provided!.");
                return null;
            }
            System.out.println("bookingDate: " + bookingDate.toString());
            System.out.println("receiptDate: " + receiptDate.toString());


            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                //String query =
                //    "begin TQC_SETUPS_PKG.generateReceipts(?,?,?,?,?); end;";
                String query =
                    "begin FMS_RCTS_PKG.save_dd_receipt(?,?,?); end;";
                
                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setBigDecimal(1, ddCode);
                statement.setString(2,
                                    session.getAttribute("Username").toString());
                statement.setDate(3, receiptDate);
                //statement.setString(3,
                //                    nodeBinding.getAttribute("ddReceipted").toString());
                //statement.setDate(4, new java.sql.Date( new java.util.Date(r.getAttribute("ddBookDate").toString()).getTime() ));
                //statement.setDate(4, new java.sql.Date( new SimpleDateFormat("yyyy/MM/dd").parse(r.getAttribute("ddBookDate").toString()).getTime() )); // Throws Unparsable date Exception
                //statement.setDate(4, bookingDate);
                //statement.setDate(5, receiptDate);

                statement.execute();
                statement.close();
                conn.commit();
                conn.close();
                
                


                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader2);

                ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail2);

                String message = "Receipts GENERATED successully.";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            } finally {
                DbUtils.closeQuietly(conn, statement, null);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a record to proceed.");
            return null;
        }
        return null;
    }

    public String actionShowFailUpdatePop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ddFailureDatePop" + "').show(hints);");
        return null;
    }


    public String actionFailUpdateDDHeader() {


        Object key2 = tblDirectDebitHeader2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            if (nodeBinding == null) {
                GlobalCC.errorValueNotEntered("You need to select a Record first.");
                return null;
            }

            if (session.getAttribute("ddReceipted") != null &&
                session.getAttribute("ddReceipted").toString() == "Y") {
                GlobalCC.errorValueNotEntered("Changes to a RECEIPTED batch NOT ALLOWED.");
                return null;
            }

            if (session.getAttribute("ddhStatus") != null &&
                nodeBinding.getAttribute("ddhStatus").toString() == "F") {
                GlobalCC.errorValueNotEntered("Changes to a FAILED batch NOT ALLOWED.");
                return null;
            }

            txtDdhCode.setValue(nodeBinding.getAttribute("ddhCode").toString());
            txtDdhDdCode.setValue(nodeBinding.getAttribute("ddhDDCode").toString());
            txtFailDate.setValue(new DirectDebitDAO().getBusinessDate(getSqlDate(session.getAttribute("ddBookDate").toString(),
                                                                                 "-")));
            txtUpdateRemarks.setValue(null);
            actionShowFailUpdatePop();
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a record to proceed.");
            return null;
        }
        return null;
    }

    public String actionRelaunchDDHeader() {
        Object key2 = tblDirectDebitHeader2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.relaunchDDHeader(?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setBigDecimal(2,
                                        new BigDecimal(nodeBinding.getAttribute("ddhCode").toString()));
                statement.setString(3,
                                    nodeBinding.getAttribute("ddhStatus").toString());
                statement.setBigDecimal(4,
                                        new BigDecimal(nodeBinding.getAttribute("ddhDDCode").toString()));
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader2);

                ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail2);

                String message = "Relaunch was successful.";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionStopLaunchDDHeader() {
        Object key2 = tblDirectDebitHeader2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.stopLaunchDDHeader(?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setBigDecimal(2,
                                        new BigDecimal(nodeBinding.getAttribute("ddhCode").toString()));
                statement.setString(3,
                                    nodeBinding.getAttribute("ddhStatus").toString());
                statement.setBigDecimal(4,
                                        new BigDecimal(nodeBinding.getAttribute("ddhDDCode").toString()));
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader2);

                ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail2);

                String message = "Stop Relaunch was successful.";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionFailUpdateDDH() {
        String ddhCode = GlobalCC.checkNullValues(txtDdhCode.getValue());
        String ddhDdCode = GlobalCC.checkNullValues(txtDdhDdCode.getValue());
        String updateRemarks =
            GlobalCC.checkNullValues(txtUpdateRemarks.getValue());
        String failDate = GlobalCC.checkNullValues(txtFailDate.getValue());

        if (ddhCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: DDH_CODE is Empty");
            return null;

        } else if (ddhDdCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: DDH_DD_CODE is Empty");
            return null;

        } else if (updateRemarks == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Update Remarks are missing");
            return null;

        } else if (failDate == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Fail Date is missing");
            return null;

        } else {

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.failUpdateDDHeader(?,?,?,?,?); end;";

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

                String date2 = null;
                if (txtFailDate.getValue() != null &&
                    !(txtFailDate.getValue().equals(""))) {
                    date2 = df.format(txtFailDate.getValue());
                }

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1,
                                    session.getAttribute("Username").toString());
                statement.setBigDecimal(2, new BigDecimal(ddhCode));
                statement.setDate(3, getSqlDate(date2, "/"));
                statement.setString(4, updateRemarks);
                statement.setBigDecimal(5, new BigDecimal(ddhDdCode));
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDirectDebitAuthorisedIterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebit2);

                ADFUtils.findIterator("fetchDirectDebitHeaders2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitHeader2);

                ADFUtils.findIterator("fetchDirectDebitDetails2Iterator").executeQuery();
                GlobalCC.refreshUI(tblDirectDebitDetail2);

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:ddFailureDatePop" +
                                     "').hide(hints);");

                String message = "Update was successful.";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionCancelFailUpdateDDH() {
        // Add event code here...
        return null;
    }

    public String actionCancelHoliday() {
        // Add event code here...
        return null;
    }

    public String actionPrintDirectDebit() {
        if (session.getAttribute("ddCode") == null) {
            GlobalCC.errorValueNotEntered("Error : No Record Selected");
            return null;
        }
        BigDecimal vDdCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ddCode"));
        session.setAttribute("V_DD_CODE", vDdCode);
        DBConnector dbConnector = null;
        dbConnector = new DBConnector();
        OracleConnection conn = null;
        conn = dbConnector.getDatabaseConnection();

        OracleCallableStatement callStmt = null;
        //String filename = null;
        String bankName = null;
        try {
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("SELECT BNK_DDR_CODE,BNK_SHT_DESC,BNK_BANK_NAME FROM TQC_BANKS WHERE BNK_CODE = " +
                                                              session.getAttribute("bankCode") +
                                                              " ");

            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();
            System.out.println("bankCode: " +
                               session.getAttribute("bankCode"));
            while (rs.next()) {
                bankName = rs.getString(3).toUpperCase();
                session.setAttribute("query", rs.getInt(1));
                session.setAttribute("bnkShtDesc", rs.getString(2));
                session.setAttribute("ddFileDesc", rs.getString(3));
            }
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        dbConnector = new DBConnector();
        conn = dbConnector.getDatabaseConnection();
        String siteParam = new GlobalCC().findParameter("SITE_PARAM", conn);


        System.out.println(bankName + "session.getAttribute(\"ddCode\")" +
                           session.getAttribute("ddCode"));
        System.out.println("SITE_PARAM: " + siteParam);

        if (bankName != null && bankName.contains("EQUITY") &&
            !siteParam.equalsIgnoreCase("18")) {
            ReportEngine rpt = new ReportEngine();
            session.setAttribute("outputFormat", "xls");
            rpt.reportOne(new BigDecimal("25256"));
            rpt = null;
        } else if (bankName != null && bankName.contains("BARCLAYS") &&
                   (!session.getAttribute("CLIENT").toString().equalsIgnoreCase("EXPRESS") &&
                    !siteParam.equalsIgnoreCase("18"))) {
            ReportEngine rpt = new ReportEngine();
            session.setAttribute("outputFormat", "xls");
            rpt.reportOne(new BigDecimal("25258"));
            rpt = null;
        } else if (bankName != null && bankName.contains("CITI") &&
                   !siteParam.equalsIgnoreCase("18")) {
            ReportEngine rpt = new ReportEngine();
            session.setAttribute("outputFormat", "xls");
            rpt.reportOne(new BigDecimal("25259"));
            rpt = null;
        } else if (bankName != null && bankName.contains("CO-OP") &&
                   !siteParam.equalsIgnoreCase("18")) {
            ReportEngine rpt = new ReportEngine();
            session.setAttribute("outputFormat", "xls");
            rpt.reportOne(new BigDecimal("25260"));
            rpt = null;
        } else {
            ExtendedRenderKitService erkService =

                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:directDebt" + "').show(hints);");

            ADFUtils.findIterator("fetchDirectDebitReportIterator").executeQuery();
            GlobalCC.refreshUI(directDReport);
        }
        //Writer writer = null;

        /*try {
              String text = "The report text goes here.";

              File file = new File("write.txt");
              writer = new BufferedWriter(new FileWriter(file));
              writer.write(text);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              try {
                  if (writer != null) {
                      writer.close();
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }*/
        return null;
    }

    public String actionPrintDirectDebitPrepare() {
        if (session.getAttribute("ddCode") == null) {
            GlobalCC.errorValueNotEntered("Error : No Record Selected");
            return null;
        }
        DBConnector dbConnector = null;
        dbConnector = new DBConnector();
        OracleConnection conn = null;
        conn = dbConnector.getDatabaseConnection();

        OracleCallableStatement callStmt = null;
        //String filename = null;
        String bankName = null;
        try {
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("SELECT BNK_DDR_CODE,BNK_SHT_DESC,BNK_BANK_NAME FROM TQC_BANKS WHERE BNK_CODE = " +
                                                              session.getAttribute("bankCode") +
                                                              " ");

            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();

            while (rs.next()) {
                bankName = rs.getString(3).toUpperCase();
                session.setAttribute("query", rs.getInt(1));
                session.setAttribute("bnkShtDesc", rs.getString(2));
                session.setAttribute("ddFileDesc", rs.getString(3));
            }
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        System.out.println(session.getAttribute("bnkShtDesc"));
        System.out.println(bankName);

        //Writer writer = null;

        /*try {
            String text = "The report text goes here.";

            File file = new File("write.txt");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return null;
    }

    public void setDirectDReport(RichTable directDReport) {
        this.directDReport = directDReport;
    }

    public RichTable getDirectDReport() {
        return directDReport;
    }

    public String exportDirectText() {
        DBConnector dbConnector = null;
        dbConnector = new DBConnector();
        OracleConnection conn = null;
        conn = dbConnector.getDatabaseConnection();
        OracleCallableStatement callStmt = null;
        try {
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;");
            callStmt.setString(2, "DIRECT_DEBIT_FILE_PATH");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            Date datetime = new Date();
            String retVal = null;
            String datetimeVal = GlobalCC.parseDateTime(datetime.toString());
            datetimeVal = datetimeVal.replace("/", "");
            datetimeVal = datetimeVal.replace(":", "");
            while (rs.next()) {
                retVal = rs.getString(1);
            }
            System.out.println(retVal);
            if (retVal == null) {
                GlobalCC.errorValueNotEntered("Error Missing Parameter: DIRECT_DEBIT_FILE_PATH . Please Check");
            }
            retVal = retVal + "/" + session.getAttribute("bnkShtDesc");
            callStmt.close();

            String hrQuery = null;
            byte[] bytes = null;
            SmbFile sfile2 = new SmbFile("smb:" + retVal + "/TestFILE.txt");
            SmbFile sFileDir = new SmbFile("smb:" + retVal);
            if (!sFileDir.exists()) {
                sFileDir.mkdirs();
            }
            File tmpFile2 = new File("TMP.txt");
            // SmbFileOutputStream writer = new SmbFileOutputStream("smb:"+filename);


            BufferedWriter writer2 =
                new BufferedWriter(new FileWriter(tmpFile2));

            writer2.write("test");

            writer2.flush();
            writer2.close();

            bytes = getBytesFromFile(tmpFile2);
            SmbFileOutputStream sfileStream2 = new SmbFileOutputStream(sfile2);
            sfileStream2.write(bytes);
            sfileStream2.flush();
            sfileStream2.close();
            tmpFile2.delete();
            sfile2.delete();
            File tmpFile = new File("TMPjose.txt");
            // SmbFileOutputStream writer = new SmbFileOutputStream("smb:"+filename);
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(tmpFile));

            hrQuery =
                    "begin  ?:= TQC_SETUPS_CURSOR.getDirectDebitsData(?,?,?,?,?); end;";

            callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);

            callStmt.setObject(2, session.getAttribute("ddCode"));
            callStmt.setObject(3, session.getAttribute("refNo"));
            callStmt.setObject(4, session.getAttribute("bankCode"));
            callStmt.setObject(5, session.getAttribute("bbrCode"));
            callStmt.setObject(6, session.getAttribute("query"));
            callStmt.execute();
            rs = (OracleResultSet)callStmt.getObject(1);
            // FileOutputStream fop=new FileOutputStream(file);

            String reportNo = null;
            while (rs.next()) {

                writer.write(rs.getString(1));
                reportNo = rs.getString(2);
                writer.write("\r\n");
                //writer.newLine();
            }
            rs.close();

            writer.flush();
            writer.close();
            callStmt.close();
            bytes = null;
            bytes = getBytesFromFile(tmpFile);
            String filename = null;
            filename =
                    retVal + "/" + session.getAttribute("bnkShtDesc") + "_" +
                    datetimeVal + "_" + reportNo + ".txt";
            SmbFile sfile = new SmbFile("smb:" + filename);
            SmbFileOutputStream sfileStream = new SmbFileOutputStream(sfile);
            sfileStream.write(bytes);
            sfileStream.flush();
            sfileStream.close();
            tmpFile.delete();
            GlobalCC.sysInformation("File " + filename +
                                    " Successfully Generated");
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("UPDATE TQC_DIRECT_DEBIT \n" +
                        "			SET DD_FILE_GENERATED = 'Y',\n" +
                        "					DD_FILE_NO = " + reportNo +
                        " \n" +
                        "			WHERE DD_CODE = " +
                        session.getAttribute("ddCode") + " ");
            callStmt.execute();
            callStmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public byte[] getBytesFromFile(File file) {
        byte[] bytes = null;
        try {
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            // Create the byte array to hold the data
            bytes = new byte[(int)length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length &&
                   (numRead = is.read(bytes, offset, bytes.length - offset)) >=
                   0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                GlobalCC.errorValueNotEntered("Could not completely read file " +
                                              file.getName());
                /*throw new IOException("Could not completely read file " +
                                file.getName());*/
            }

            // Close the input stream and return bytes
            is.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return bytes;
    }

    public String generateDirectDebitTrans() {
        Object key2 = tblDirectDebit2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        return null;
    }

    public void setTxtDirectDebit(RichCommandLink txtDirectDebit) {
        this.txtDirectDebit = txtDirectDebit;
    }

    public RichCommandLink getTxtDirectDebit() {
        return txtDirectDebit;
    }

    public void setTable(RichTable table) {
        this.table = table;
    }

    public RichTable getTable() {
        return table;
    }

    public List getRows() {
        if (rows.size() == 0) {

            processSchedule();

        }
        return rows.subList(1, rows.size());
    }

    public String processSchedule() {
        List<String> columnNames1 = new ArrayList<String>();
        List<String> titleNames1 = new ArrayList<String>();
        List<String> columnDataTypes1 = new ArrayList<String>();
        // List<String> columnDefault = new ArrayList<String>();
        String tableName = "";
        String tablePrefix = "";
        DBConnector con = new DBConnector();
        Connection conn = con.getDatabaseConnection();
        CallableStatement cst = null;
        ResultSet rst = null;
        try {

            String ScheduleColumns =
                "SELECT DISTINCT TQC_BANK_IMP_TMPLT.* " + "FROM TQC_BANK_IMP_TMPLT " +
                " WHERE LPIT_BNK_CODE = " + session.getAttribute("bankCode") +
                " AND DSL_LEVEL_NO = 1";

            cst = conn.prepareCall(ScheduleColumns);
            rst = cst.executeQuery();
            while (rst.next()) {
                tableName = rst.getString(4);
                tablePrefix = rst.getString(6);

                int k = 8;
                // System.out.println("This is oK"+rst.getString(k));
                while (rst.getString(k) != null) {
                    int t = k + 1;
                    int u = k + 2;

                    int v = k + 3;

                    columnDataTypes1.add(rst.getString(u));
                    //System.out.println("This is columnDataTypes1"+rst.getString(u));
                    columnNames1.add(rst.getString(k));
                    //System.out.println("This is columnNames1"+rst.getString(k));
                    if (rst.getString(u).equals("INVS")) {
                        titleNames1.add(rst.getString(t));
                        // columnDefault.add(rst.getString(v));
                    } else {
                        titleNames1.add(rst.getString(t));
                        // columnDefault.add(rst.getString(v));
                    }
                    if (t == 138) {
                        k = v + 1;
                    } else {
                        //System.out.println("This is Ok"+k);
                        k = k + 5;
                    }
                }

            }
            //System.out.println("Table Name " + tableName);
            //System.out.println("numberOfColumns " + columnNames1.size());
            numberOfColumns = columnNames1.size();
            String parameterString = null;
            String columnDesc = null;
            int k = 0;
            while (k < numberOfColumns) {
                int equivalence = 0;
                equivalence = k + 1;
                if (parameterString == null) {
                    parameterString = "";
                    columnDesc = "";
                }
                if (equivalence == numberOfColumns) {
                    columnDesc = columnDesc + "'" + titleNames1.get(k) + "'";
                    //parameterString = parameterString + "?";
                } else {
                    columnDesc =
                            columnDesc + "'" + titleNames1.get(k) + "'" + ",";

                }

                k++;
            }
            //System.out.println("This is column Description"+columnDesc);
            String query = "SELECT " + columnDesc + " FROM DUAL";
            cst = conn.prepareCall(query);
            rst = cst.executeQuery();
            rows = new ArrayList();

            while (rst.next()) {
                Map data = new HashMap();
                for (int j = 1; j < numberOfColumns + 1; j++) {
                    data.put("cell" + (j), rst.getString(j));
                }
                rows.add(data);
            }
            columnDesc = "";
            k = 0;

            while (k < numberOfColumns) {
                int equivalence = 0;
                equivalence = k + 1;
                if (parameterString == null) {
                    parameterString = "";
                    columnDesc = "";
                }
                if (equivalence == numberOfColumns) {
                    columnDesc = columnDesc + columnNames1.get(k);

                } else {
                    columnDesc = columnDesc + columnNames1.get(k) + ",";
                }

                k++;
            }

            query =
                    "SELECT " + columnDesc + " FROM " + tableName + " WHERE " + tablePrefix +
                    "_IPU_CODE =" +
                    (BigDecimal)session.getAttribute("IPUCode");
            // System.out.println(query);
            cst = conn.prepareCall(query);
            rst = cst.executeQuery();

            while (rst.next()) {
                Map data = new HashMap();
                for (int j = 1; j < numberOfColumns + 1; j++) {
                    data.put("cell" + (j), rst.getString(j));
                }
                rows.add(data);
            }

            rst.close();
            cst.close();
            conn.close();
            setupTableColumns();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, rst);

        }
        return null;
    }

    public void getData(String tableName) {
        rows = new ArrayList();
        rows2 = new ArrayList();
        rows3 = new ArrayList();
        numberOfColumns = 0;
        numberOfColumns2 = 0;
        numberOfColumns3 = 0;
    }

    private void setupTableColumns() {
        // System.out.println("This is executed");
        List<String> columnDataTypes1 = new ArrayList<String>();
        String tableName = "";
        DBConnector con = new DBConnector();
        Connection conn = con.getDatabaseConnection();
        String ScheduleColumns =
            "SELECT DISTINCT TQC_BANK_IMP_TMPLT.* " + "FROM TQC_BANK_IMP_TMPLT " +
            " WHERE LPIT_BNK_CODE = " + session.getAttribute("bankCode") +
            " ORDER BY LPIT_COL1_POSITION DESC";
        CallableStatement cst = null;
        ResultSet rst = null;
        try {

            cst = conn.prepareCall(ScheduleColumns);
            rst = cst.executeQuery();
            while (rst.next()) {
                tableName = rst.getString(4);
                int k = 8;
                while (rst.getString(k) != null) {
                    int t = k + 1;
                    int u = k + 2;
                    /// int v = k + 3;
                    columnDataTypes1.add(rst.getString(u));
                    if (t == 138)
                        k = k + 1;
                    else
                        k = k + 5;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, rst);

        }
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        table.getChildren().clear();
        RichColumn col =
            (RichColumn)app.createComponent(RichColumn.COMPONENT_TYPE);
        RichOutputText cell =
            (RichOutputText)app.createComponent(RichOutputText.COMPONENT_TYPE);
        col.setId("rowheader");

        cell.setId("rowcell");
        cell.setValueBinding("value",
                             app.createValueBinding("#{rowStatus.index}"));

        col.getChildren().add(cell);
        col.setHeaderText("^");
        col.setRendered(false);
        table.getChildren().add(col);
        Map columnHeaders = new HashMap();
        columnHeaders = getColumnHeaders();
        //System.out.println("numberOfColumns"+numberOfColumns);
        for (int i = 1; i < numberOfColumns + 1; i++) {
            col = (RichColumn)app.createComponent(RichColumn.COMPONENT_TYPE);
            col.setId("col" + i);

            if (columnDataTypes1.get(i - 1).equals("INVS")) {
                col.setVisible(false);

            }

            cell =
(RichOutputText)app.createComponent(RichOutputText.COMPONENT_TYPE);
            cell.setId("cell" + i);
            cell.setValueBinding("value",
                                 app.createValueBinding("#{row['cell" + (i) +
                                                        "']}"));
            //System.out.println("Value Binding "+"#{row['cell" + (i) + "']}");
            col.getChildren().add(cell);
            col.setSortable(true);
            col.setSortProperty("cell" + (i));
            String headerText = (String)((Map)rows.get(0)).get("cell" + (i));
            if (headerText.startsWith("*")) {
                headerText = new String(headerText.substring(1));
            }
            col.setHeaderText(headerText);
            table.getChildren().add(col);
        }
    }

    public Map getColumnHeaders() {
        Map columnHeaders = new HashMap();
        for (int i = 0; i < numberOfColumns + 1; i++)
            columnHeaders.put(Integer.toString(i),
                              ((Map)rows.get(0)).get("cell" + (i + 1)));
        return columnHeaders;

    }

    public String GenerateTemplateFile() {
        Connection conn = null;
        String[][] ColumnValues = null;
        int DivCnt = 0;
        int FinalPosition = 0;
        // String Params2 = null;
        String value2 = null;
        String Params3 = null;
        try {
            //session.setAttribute("QuoteCode", new BigDecimal("20124043"));
            conn = new DBConnector().getDatabaseConnection();
            //DETERMINE IF THE QUOTE HAS SUBDIVISIONS...
            //GET THE COLUMNS ON THE TEMPLATE TABLE..
            Integer TotalColumns = ValidateTableName("TQC_BANK_IMP_TMPLT");
            Integer ArraySize = TotalColumns; /*/ 3;*/
            float xFloat;
            xFloat = ArraySize + DivCnt;
            ColumnValues = new String[Math.round(xFloat)][5];


            String Query =
                "SELECT * FROM TQC_BANK_IMP_TMPLT WHERE LPIT_BNK_CODE = ?";
            CallableStatement cst = null;
            cst = conn.prepareCall(Query);
            System.out.print("bank code" +
                             session.getAttribute("bankCodeVal"));
            cst.setBigDecimal(1,
                              new BigDecimal(session.getAttribute("bankCodeVal").toString()));
            ResultSet rs = cst.executeQuery();
            int iCount = 4;
            while (rs.next()) {
                while (iCount <= TotalColumns) {
                    if (rs.getString(iCount) == null) {
                        //do nothing...
                    } else {
                        int Position = rs.getInt(iCount + 1);
                        // ColumnValues=updateDetails(Position);
                        ColumnValues[Position - 1][0] = rs.getString(iCount);
                        FinalPosition = FinalPosition + 1;

                    }
                    iCount = iCount + 3;
                }
            }


            //////////////////////////////////////

            //APPEND DIVISIONS TO THE ARRAY
            int myCount = 0;
            String Params = null;
            while (myCount < ColumnValues.length) {
                String Value = ColumnValues[myCount][0];
                //  System.out.println("This is it"+Value);
                if (Value == null) {
                    //do nothing..
                } else {
                    if (Params == null) {
                        Params = ColumnValues[myCount][0] + ",";
                    } else {
                        Params = Params + ColumnValues[myCount][0] + ",";
                    }
                }
                myCount++;
            }
            //////////////////////////////////
            String Query1 =
                "SELECT * FROM TQC_BANK_IMP_TMPLT WHERE LPIT_BNK_CODE = ?";
            CallableStatement cst2 = null;
            cst2 = conn.prepareCall(Query1);
            System.out.print("bank code" +
                             session.getAttribute("bankCodeVal"));
            cst2.setBigDecimal(1,
                               new BigDecimal(session.getAttribute("bankCodeVal").toString()));
            ResultSet rst = cst.executeQuery();
            int uCount = 4;
            while (rst.next()) {
                while (uCount <= TotalColumns) {
                    if (rst.getString(uCount) == null) {
                        //do nothing...
                    } else {
                        int Position = rst.getInt(uCount + 1);
                        // ColumnValues=updateDetails(Position);
                        ColumnValues[Position - 1][0] = rst.getString(uCount);
                        FinalPosition = FinalPosition + 1;

                    }
                    uCount = uCount + 3;
                }
            }


            //////////////////////////////////////

            //APPEND DIVISIONS TO THE ARRAY
            int pcount = 0;
            String Params2 = null;
            while (pcount < ColumnValues.length) {
                String Value = ColumnValues[pcount][0];
                //  System.out.println("This is it"+Value);
                if (Value == null) {
                    //do nothing..
                } else {
                    if (Params2 == null) {
                        Params2 = ColumnValues[pcount][0] + ",";
                    } else {
                        Params2 = Params2 + ColumnValues[pcount][0] + ",";
                    }
                }
                pcount++;
            }

            if (Params != null) {
                value2 = Params.substring(0, Params.length() - 1);
                System.out.println("This is the column" + value2);
                String CountQuery =
                    "SELECT DDD_CODE, POLICY, BANK_ACC_NO, FORWARDING_BNK, DDD_POL_FREQ_PYMNT, DDD_PREM_DUE_DATE, AMOUNT, CLNT_NAME, NARRATTIVE, ORG_NAME, DDH_STATUS, DDH_FAIL_REMARKS, DD_REF_NO, BANK_BRANCH FROM TQC_DIRECT_DEBIT_EXPORT_DT WHERE DD_CODE = ?";
                CallableStatement cst1 = null;
                cst1 = conn.prepareCall(CountQuery);
                cst1.setBigDecimal(1,
                                   new BigDecimal(session.getAttribute("ddCode").toString()));
                ResultSet rs2 = cst1.executeQuery();
                Params3 = null;
                String test[] = value2.split(",");
                String value3 = null;
                // int count = 1;
                while (rs2.next()) {

                    if (Params3 == null && value3 != null) {
                        Params3 = value3;
                    } else {
                        for (int count = 0; count < test.length; count++) {
                            System.out.print("This is ok" +
                                             rs2.getString(test[count]));
                            value3 = rs2.getString(test[count]) + ",";
                            if (Params3 == null && value3 != null) {
                                Params3 = value3;
                            } else {
                                if (value3 != null) {
                                    Params3 = Params3 + value3;
                                }
                            }
                        }

                    }

                    Params3 += "\n";
                }
                String Header = Params + "\n" +
                    Params3;

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

                String output = "Template.csv";
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition",
                                   "attachment; filename=" + output + "");


                response.setContentLength(barray.length);

                bufferedOutputStream.write(barray, 0, barray.length);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();

                response.reset();
                response.resetBuffer();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
        return null;
    }
    public List<DirectDebit> fetchDirectDebitReport() {
        List<DirectDebit> directDebitList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitsReport(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            directDebitList = new ArrayList<DirectDebit>();
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, oracle.jdbc.internal.OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("ddCode"));
            statement.setObject(3, session.getAttribute("query"));
            statement.execute();
            OracleResultSet rs = (OracleResultSet)statement.getObject(1);
            String period = null;

            while (rs.next()) {
                DirectDebit directDebit = new DirectDebit();
                directDebit.setDddCode(rs.getBigDecimal(1));
                directDebit.setPolicyNo(rs.getString(2));
                directDebit.setAccountNo(rs.getString(3));
                directDebit.setSortCode(rs.getString(4));
                directDebit.setAmount(rs.getBigDecimal(5));
                directDebit.setAccName(rs.getString(6));
                directDebit.setNarration(rs.getString(7));
                directDebit.setCompany(rs.getString(8));
                directDebit.setBbRefCode(rs.getString(9));
                directDebit.setNextDueDate(rs.getDate(10));
                directDebit.setPayFreq(rs.getString(11));
                directDebit.setDdRefNumber(rs.getString(12));
                directDebit.setDdBankBranch(rs.getString(13));
                directDebit.setDdDebitDay(rs.getString(14));
                period = rs.getString(15);
                directDebitList.add(directDebit);
            }
            String ddFileDesc = null;
            if (session.getAttribute("ddFileDesc") != null) {
                ddFileDesc =
                        session.getAttribute("ddFileDesc").toString() + " - " +
                        period;
                session.setAttribute("ddFileDesc", ddFileDesc);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return directDebitList;
    }
    public void generateDDExcelFile(ActionEvent actionEvent) { 
        List<DirectDebit> directDebitList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.getDirectDebitsReport(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        try {
                Workbook wb = new XSSFWorkbook();
                CreationHelper createHelper = wb.getCreationHelper();
                Sheet sheet = wb.createSheet("DIRECT DEBITS REPORT");
                
                // Create a row and put some cells in it. Rows are 0 based.
                org.apache.poi.ss.usermodel.Row header = sheet.createRow((short) 0); 
                // Or do it on one line.
                header.createCell(0).setCellValue(createHelper.createRichTextString("ACCOUNT NAME"));
                header.createCell(1).setCellValue(createHelper.createRichTextString("POLICY NO"));
                header.createCell(2).setCellValue(createHelper.createRichTextString("AMOUNT"));
                header.createCell(3).setCellValue(createHelper.createRichTextString("ACCOUNT NO"));
                header.createCell(4).setCellValue(createHelper.createRichTextString("SORT CODE"));
                header.createCell(5).setCellValue(createHelper.createRichTextString("DEBIT DAY")); 
                header.createCell(6).setCellValue(createHelper.createRichTextString("DDD CODE"));
                header.createCell(7).setCellValue(createHelper.createRichTextString("PAY FREQ"));
                header.createCell(8).setCellValue(createHelper.createRichTextString("DUE DATE"));
                header.createCell(9).setCellValue(createHelper.createRichTextString("NARRATION"));
                header.createCell(10).setCellValue(createHelper.createRichTextString("COMPANY"));
                header.createCell(11).setCellValue(createHelper.createRichTextString("FAILED (F/A)")); 
                header.createCell(12).setCellValue(createHelper.createRichTextString("FAILED REMARKS")); 
                header.createCell(13).setCellValue(createHelper.createRichTextString("REF NO"));
                header.createCell(14).setCellValue(createHelper.createRichTextString("BANK BRANCH")); 
                //header.createCell(9).setCellValue(createHelper.createRichTextString("BRANCH REF CODE"));
                
                
                
                
                
                //header.createCell(15).setCellValue(createHelper.createRichTextString("PERIOD")); 
            
              System.out.println("ddCode: "+session.getAttribute("ddCode")); 
               System.out.println("query: "+session.getAttribute("query"));
                 
                directDebitList = new ArrayList<DirectDebit>();
                connection = (OracleConnection)dbConnector.getDatabaseConnection();
                statement = (OracleCallableStatement)connection.prepareCall(query);
    
                statement.registerOutParameter(1, oracle.jdbc.internal.OracleTypes.CURSOR);
                statement.setObject(2, session.getAttribute("ddCode"));
                statement.setObject(3, session.getAttribute("query"));
                statement.execute();
                OracleResultSet rs = (OracleResultSet)statement.getObject(1);
                String period = null;
                int i = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow( i++ ); 
                    row.createCell(0).setCellValue(createHelper.createRichTextString(rs.getString(6)));
                    row.createCell(1).setCellValue(createHelper.createRichTextString(rs.getString(2)));
                    row.createCell(2).setCellValue(createHelper.createRichTextString(rs.getString(5)));
                    row.createCell(3).setCellValue(createHelper.createRichTextString(rs.getString(3)));
                    row.createCell(4).setCellValue(createHelper.createRichTextString(rs.getString(4)));
                    row.createCell(5).setCellValue(createHelper.createRichTextString(rs.getString(14)));
                    row.createCell(6).setCellValue(createHelper.createRichTextString(rs.getString(1)));
                    row.createCell(7).setCellValue(createHelper.createRichTextString(rs.getString(11)));
                    row.createCell(8).setCellValue(createHelper.createRichTextString(rs.getString(10)));
                    row.createCell(9).setCellValue(createHelper.createRichTextString(rs.getString(7)));
                    row.createCell(10).setCellValue(createHelper.createRichTextString(rs.getString(8))); 
                    row.createCell(11).setCellValue("");
                    row.createCell(12).setCellValue("");
                    row.createCell(13).setCellValue(createHelper.createRichTextString(rs.getString(12)));
                    row.createCell(14).setCellValue(createHelper.createRichTextString(rs.getString(13))); 
                    period = rs.getString(15); 
                    System.out.println(i);
                }
            String ddFileDesc = null;
            if (session.getAttribute("ddFileDesc") != null) {
                ddFileDesc =
                        session.getAttribute("ddFileDesc").toString() + " - " +
                        period;
                session.setAttribute("ddFileDesc", ddFileDesc);
            } 
            String output = ddFileDesc+".xlsx";
            // Write the output to a file
            FileOutputStream fileOut=null;
            fileOut = new FileOutputStream(output);
            wb.write(fileOut);
            fileOut.close(); 
            byte bytes[] = getBytesFromFile(new File(output));
            if (bytes == null) {
                return;
            }
            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream;
            response.reset();
            response.resetBuffer();
            servletOutputStream = response.getOutputStream(); 

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",  "attachment; filename=" + output + "");

            response.setContentLength(bytes.length);

            servletOutputStream.write(bytes, 0, bytes.length);
            servletOutputStream.flush();
            servletOutputStream.close(); 
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e); 
        } 
    }

    


    public Integer ValidateTableName(String TableName) {
        Connection conn = null;
        Integer TotalColumns = 0;
        try {
            conn = new DBConnector().getDatabaseConnection();
            String CountQuery =
                "SELECT COUNT(*) FROM TQC_USER_TAB_COLUMNS WHERE TABLE_NAME='" +
                TableName + "'";
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(CountQuery);
            ResultSet rs1 = cst1.executeQuery();
            while (rs1.next()) {
                TotalColumns = rs1.getInt(1);
            }
            cst1.close();
            rs1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return TotalColumns;
    }

    private String[][] appendDtls(int pos) {
        String[][] ColumnValues = null;
        try {
            DBConnector dbConnector = new DBConnector();
            String query =
                "SELECT * FROM TQC_DIRECT_DEBIT_EXPORT_DT WHERE DD_CODE = ?";
            OracleCallableStatement statement = null;
            OracleConnection connection = null;
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            System.out.print("ddCode code" + session.getAttribute("ddCode"));
            statement.setBigDecimal(1,
                                    new BigDecimal(session.getAttribute("ddCode").toString()));
            ResultSet rst = statement.executeQuery();
            int count = 1;
            while (rst.next()) {
                System.out.println("This is not good" + count +
                                   "Column values" + rst.getString(count));

                System.out.println("This is positions" + pos);
                ColumnValues[pos - 1][count] = rst.getString(count);
                count = count + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ColumnValues;
    }

    private String[][] updateDetails(int pos) {
        Connection conn = null;
        String[][] ColumnValues = null;
        float xFloat;
        //  xFloat = ArraySize + DivCnt;
        ColumnValues = new String[10][15];
        try {
            conn = new DBConnector().getDatabaseConnection();
            String CountQuery =
                "SELECT * FROM TQC_DIRECT_DEBIT_EXPORT_DT WHERE DD_CODE = ?";
            CallableStatement cst1 = null;
            cst1 = conn.prepareCall(CountQuery);
            cst1.setBigDecimal(1,
                               new BigDecimal(session.getAttribute("ddCode").toString()));
            ResultSet rs1 = cst1.executeQuery();
            int count = 1;
            while (rs1.next()) {
                System.out.println("This is not good" + count +
                                   "Column values" + rs1.getString(count));

                System.out.println("This is positions" + pos);
                ColumnValues[pos - 1][count] = rs1.getString(count);
                count = count + 1;
            }
            cst1.close();
            rs1.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        return ColumnValues;
    }

    public void setClearFailureUpdate(RichCommandButton clearFailureUpdate) {
        this.clearFailureUpdate = clearFailureUpdate;
    }

    public RichCommandButton getClearFailureUpdate() {
        return clearFailureUpdate;
    }

    public void setImportFailureUpdate(RichCommandButton importFailureUpdate) {
        this.importFailureUpdate = importFailureUpdate;
    }

    public RichCommandButton getImportFailureUpdate() {
        return importFailureUpdate;
    }

    public String launchImport() {

        session.setAttribute("launchType", "ImportFailUpdate");
        clearFailureUpdate.setText("Clear Failure Updates");
        importFailureUpdate.setText("Import Failure Updates");
        GlobalCC.refreshUI(clearFailureUpdate);
        GlobalCC.refreshUI(importFailureUpdate);

        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

            fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:impFail" +
                             "').show(hints);");
        return null;
    }

    public String launchImportRelaunchDDHeader() {
        session.setAttribute("launchType", "ImportRelaunch");

        clearFailureUpdate.setText("Clear Relaunch Updates");
        importFailureUpdate.setText("Import Relaunch Updates");
        GlobalCC.refreshUI(clearFailureUpdate);
        GlobalCC.refreshUI(importFailureUpdate);

        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

            fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:impFail" +
                             "').show(hints);");
        return null;
    }

    public void setInstallmentDays(List<SelectItem> installmentDays) {
        this.installmentDays = installmentDays;
    }

    public List<SelectItem> getInstallmentDays() {
        if (this.installmentDays != null) {
            this.installmentDays.clear();
        }
        DBConnector connection = new DBConnector();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        try {
            if (!GlobalCC.tableExists("gin_instalment_days")) {
                return installmentDays;
            }
            conn = connection.getDatabaseConnection();
            stmt =
conn.prepareStatement("select id_instlmt_day,id_instlmt_day from gin_instalment_days");

            stmt.execute();
            rst = stmt.executeQuery();
            while (rst.next()) {
                this.installmentDays.add(new SelectItem(rst.getString(1),
                                                        rst.getString(2)));
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return this.installmentDays;
    }

    public void setTxtInstallDays(RichSelectOneChoice txtInstallDays) {
        this.txtInstallDays = txtInstallDays;
    }

    public RichSelectOneChoice getTxtInstallDays() {
        return txtInstallDays;
    }


    public void setTxtReceiptDate(RichInputDate txtReceiptDate) {
        this.txtReceiptDate = txtReceiptDate;
    }

    public RichInputDate getTxtReceiptDate() {
        return txtReceiptDate;
    }


    public void setTxtBookingDateTo(RichInputDate txtBookingDateTo) {
        this.txtBookingDateTo = txtBookingDateTo;
    }

    public RichInputDate getTxtBookingDateTo() {
        return txtBookingDateTo;
    }

    public void toBankOrBranchListener(ValueChangeEvent valueChangeEvent) {
        String newValue = (String)valueChangeEvent.getNewValue();
        System.out.println("////newvalue= " + newValue);
        if (newValue.equalsIgnoreCase("BB")) {
            session.setAttribute("_b_or_bb", "BB");
        } else if (newValue.equalsIgnoreCase("B")) {
            session.setAttribute("_b_or_bb", "B");
        }
        ADFUtils.findIterator("fetchBankBranchesIterator").executeQuery();
    }

    public void setBankOrBranch(RichSelectOneRadio bankOrBranch) {
        this.bankOrBranch = bankOrBranch;
    }

    public RichSelectOneRadio getBankOrBranch() {
        return bankOrBranch;
    }
    public static void main(String[] args) { 
            //session.setAttribute("currencyCode",BigDecimal.ONE); 
            System.out.println("System Out");
            //List<ExchangeRate> rates =dao.fetchCurrencyRatesByBaseCurrency();  
    }

    public void setMyGlassPane(RichPopup myGlassPane) {
        this.myGlassPane = myGlassPane;
    }

    public RichPopup getMyGlassPane() {
        return myGlassPane;
    }

    public void setTxtProdDesc(RichInputText txtProdDesc) {
        this.txtProdDesc = txtProdDesc;
    }

    public RichInputText getTxtProdDesc() {
        return txtProdDesc;
    }

    public void setProductsTab(RichTable productsTab) {
        this.productsTab = productsTab;
    }

    public RichTable getProductsTab() {
        return productsTab;
    }

    public String selectProduct() {
        DCIteratorBinding dciter =
                  ADFUtils.findIterator("findDDProductsIterator");
              RowKeySet set = productsTab.getSelectedRowKeys();
              Iterator rowKeySetIter = set.iterator();

              while (rowKeySetIter.hasNext()) {
                  List l = (List)rowKeySetIter.next();
                  Key key = (Key)l.get(0);
                  dciter.setCurrentRowWithKey(key.toStringFormat(true));
                  Row r = dciter.getCurrentRow();
                  txtProdDesc.setValue(r.getAttribute("PROD_DESC"));
                 
                  session.setAttribute("prodUmbrella",
                                       r.getAttribute("PROD_UMBRELLA"));
                  session.setAttribute("productCode",
                                       r.getAttribute("PROD_CODE"));                                  
                  
               
              }
              
              AdfFacesContext.getCurrentInstance().addPartialTarget(txtProdDesc);

              return null;
    }
}
