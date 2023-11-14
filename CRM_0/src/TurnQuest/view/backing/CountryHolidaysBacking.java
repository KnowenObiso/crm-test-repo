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
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.HolidayDefinition;

import java.math.BigDecimal;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class CountryHolidaysBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblCountries;
    private RichTable tblHolidays;
    private RichInputText txtThdDesc;
    private RichSelectOneChoice txtThdMonth;
    private RichInputNumberSpinbox txtThdDay;
    private RichSelectOneChoice txtThdStatus;
    private RichInputText txtThdCouCode;
    private RichCommandButton btnSaveUpdateHoliday;

    public CountryHolidaysBacking() {
    }

    public void setTblCountries(RichTable tblCountries) {
        this.tblCountries = tblCountries;
    }

    public RichTable getTblCountries() {
        return tblCountries;
    }

    public void setTblHolidays(RichTable tblHolidays) {
        this.tblHolidays = tblHolidays;
    }

    public RichTable getTblHolidays() {
        return tblHolidays;
    }

    public void setTxtThdDesc(RichInputText txtThdDesc) {
        this.txtThdDesc = txtThdDesc;
    }

    public RichInputText getTxtThdDesc() {
        return txtThdDesc;
    }

    public void setTxtThdMonth(RichSelectOneChoice txtThdMonth) {
        this.txtThdMonth = txtThdMonth;
    }

    public RichSelectOneChoice getTxtThdMonth() {
        return txtThdMonth;
    }

    public void setTxtThdDay(RichInputNumberSpinbox txtThdDay) {
        this.txtThdDay = txtThdDay;
    }

    public RichInputNumberSpinbox getTxtThdDay() {
        return txtThdDay;
    }

    public void setTxtThdStatus(RichSelectOneChoice txtThdStatus) {
        this.txtThdStatus = txtThdStatus;
    }

    public RichSelectOneChoice getTxtThdStatus() {
        return txtThdStatus;
    }

    public void setTxtThdCouCode(RichInputText txtThdCouCode) {
        this.txtThdCouCode = txtThdCouCode;
    }

    public RichInputText getTxtThdCouCode() {
        return txtThdCouCode;
    }

    public void setBtnSaveUpdateHoliday(RichCommandButton btnSaveUpdateHoliday) {
        this.btnSaveUpdateHoliday = btnSaveUpdateHoliday;
    }

    public RichCommandButton getBtnSaveUpdateHoliday() {
        return btnSaveUpdateHoliday;
    }

    public void tblCountriesListener(SelectionEvent selectionEvent) {
        Object key2 = tblCountries.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("couCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchHolidayDefinitionsByCountryIterator").executeQuery();
            GlobalCC.refreshUI(tblHolidays);
        }
    }

    public void clearHolidayFields() {
        txtThdDesc.setValue(null);
        //txtThdMonth.setValue( null );
        txtThdDay.setValue(null);
        //txtThdStatus.setValue( 'A' );
        txtThdCouCode.setValue(session.getAttribute("couCode"));

        txtThdDesc.setReadOnly(false);
        txtThdMonth.setReadOnly(false);
        txtThdDay.setReadOnly(false);

        btnSaveUpdateHoliday.setText("Save");
    }

    public String actionNewHoliday() {
        if (session.getAttribute("couCode") != null) {
            clearHolidayFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:holidayPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Country to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditHoliday() {
        Object key2 = tblHolidays.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtThdDesc.setValue(nodeBinding.getAttribute("thdDesc"));
            txtThdMonth.setValue(nodeBinding.getAttribute("thdMonth"));
            txtThdDay.setValue(nodeBinding.getAttribute("thdDay"));
            txtThdStatus.setValue(nodeBinding.getAttribute("thdStatus"));
            txtThdCouCode.setValue(nodeBinding.getAttribute("thdCouCode"));

           txtThdDesc.setReadOnly(false);
           txtThdMonth.setReadOnly(false);
           txtThdDay.setReadOnly(false);

            btnSaveUpdateHoliday.setText("Edit");

            GlobalCC.showPopup( "crm:holidayPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteHoliday() {
        Object key2 = tblHolidays.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String desc = nodeBinding.getAttribute("thdDesc").toString();
            String month = nodeBinding.getAttribute("thdMonth").toString();
            String day = nodeBinding.getAttribute("thdDay").toString();
            String status = nodeBinding.getAttribute("thdStatus").toString();
            String couCode = nodeBinding.getAttribute("thdCouCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.holidayDefinitions_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_DEFINITIONS_TAB",
                                                     conn);
                ArrayList holidayList = new ArrayList();
                HolidayDefinition holiday = new HolidayDefinition();
                holiday.setSQLTypeName("TQC_HOLIDAYS_DEFINITIONS_OBJ");

                holiday.setThdDesc(desc);
                holiday.setThdDay(new BigDecimal(day));
                holiday.setThdMonth(new BigDecimal(month));
                holiday.setThdStatus(status);
                holiday.setThdCouCode(new BigDecimal(couCode));

                holidayList.add(holiday);
                ARRAY array =
                    new ARRAY(descriptor, conn, holidayList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchHolidayDefinitionsByCountryIterator").executeQuery();
                GlobalCC.refreshUI(tblHolidays);

                clearHolidayFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateHoliday() {
        if (btnSaveUpdateHoliday.getText().equals("Edit")) {
            actionUpdateHoliday();
        } else {
            String desc = GlobalCC.checkNullValues(txtThdDesc.getValue());
            String month = GlobalCC.checkNullValues(txtThdMonth.getValue());
            String day = GlobalCC.checkNullValues(txtThdDay.getValue());
            String status = GlobalCC.checkNullValues(txtThdStatus.getValue());
            String couCode =
                GlobalCC.checkNullValues(txtThdCouCode.getValue());

            if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else if (month == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Month is Empty");
                return null;

            } else if (day == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Day is Empty");
                return null;

            } else if (status == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Status is Empty");
                return null;

            } else if (couCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.holidayDefinitions_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_DEFINITIONS_TAB",
                                                         conn);
                    ArrayList holidayList = new ArrayList();
                    HolidayDefinition holiday = new HolidayDefinition();
                    holiday.setSQLTypeName("TQC_HOLIDAYS_DEFINITIONS_OBJ");

                    holiday.setThdDesc(desc);
                    holiday.setThdDay(new BigDecimal(day));
                    holiday.setThdMonth(new BigDecimal(month));
                    holiday.setThdStatus(status);
                    holiday.setThdCouCode(new BigDecimal(couCode));

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

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:holidayPop" + "').hide(hints);");

                    ADFUtils.findIterator("fetchHolidayDefinitionsByCountryIterator").executeQuery();
                    GlobalCC.refreshUI(tblHolidays);

                    clearHolidayFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateHoliday() {
        String desc = GlobalCC.checkNullValues(txtThdDesc.getValue());
        String month = GlobalCC.checkNullValues(txtThdMonth.getValue());
        String day = GlobalCC.checkNullValues(txtThdDay.getValue());
        String status = GlobalCC.checkNullValues(txtThdStatus.getValue());
        String couCode = GlobalCC.checkNullValues(txtThdCouCode.getValue());

        if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else if (month == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Month is Empty");
            return null;

        } else if (day == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Day is Empty");
            return null;

        } else if (status == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Status is Empty");
            return null;

        } else if (couCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Country Code is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.holidayDefinitions_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_HOLIDAYS_DEFINITIONS_TAB",
                                                     conn);
                ArrayList holidayList = new ArrayList();
                HolidayDefinition holiday = new HolidayDefinition();
                holiday.setSQLTypeName("TQC_HOLIDAYS_DEFINITIONS_OBJ");

                holiday.setThdDesc(desc);
                holiday.setThdDay(new BigDecimal(day));
                holiday.setThdMonth(new BigDecimal(month));
                holiday.setThdStatus(status);
                holiday.setThdCouCode(new BigDecimal(couCode));

                holidayList.add(holiday);
                ARRAY array =
                    new ARRAY(descriptor, conn, holidayList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:holidayPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchHolidayDefinitionsByCountryIterator").executeQuery();
                GlobalCC.refreshUI(tblHolidays);

                clearHolidayFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }
}
