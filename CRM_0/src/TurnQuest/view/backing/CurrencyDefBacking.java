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
import TurnQuest.view.setups.BaseSetupDAO;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class CurrencyDefBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblCurrency;
    private RichTable tblCurrencyDenomination;
    private RichInputText txtCurCode;
    private RichInputText txtCurSymbol;
    private RichInputText txtCurDescription;
    private RichInputNumberSpinbox txtCurRound;
    private RichInputText txtCurNumWord;
    private RichInputText txtCurDecimalWord;
    private RichCommandButton btnSaveUpdateCurrency;
    private RichInputText txtCudCode;
    private RichInputText txtCudCurCode;
    private RichInputNumberSpinbox txtCudValue;
    private RichInputText txtCudName;
    private RichInputDate txtCudWefDate;
    private RichCommandButton btnSaveUpdateCurrencyDenomination;
    private RichCommandButton btnActionEditCurrency;
    private RichCommandButton actionDeleteCurrency;
    private RichCommandButton btnActionDeleteCurrency;
    private RichCommandButton btnActionNewCurrencyDenomination;
    private RichCommandButton btnActionEditCurrencyDenomination;
    private RichCommandButton btnActionDeleteCurrencyDenomination;
    private RichInputText txtCountryName;
    private RichInputText txtShtDesc;
    private RichTable countryTbl;
    private RichTable countyTbl;
    private RichInputText txtPopup;
    private RichCommandButton cmdButton;
    private RichTable couTbl;

    public CurrencyDefBacking() {
    }

    public void setTblCurrency(RichTable tblCurrency) {
        this.tblCurrency = tblCurrency;
    }

    public RichTable getTblCurrency() {
        return tblCurrency;
    }

    public void setTblCurrencyDenomination(RichTable tblCurrencyDenomination) {
        this.tblCurrencyDenomination = tblCurrencyDenomination;
    }

    public RichTable getTblCurrencyDenomination() {
        return tblCurrencyDenomination;
    }

    public void setTxtCurCode(RichInputText txtCurCode) {
        this.txtCurCode = txtCurCode;
    }

    public RichInputText getTxtCurCode() {
        return txtCurCode;
    }

    public void setTxtCurSymbol(RichInputText txtCurSymbol) {
        this.txtCurSymbol = txtCurSymbol;
    }

    public RichInputText getTxtCurSymbol() {
        return txtCurSymbol;
    }

    public void setTxtCurDescription(RichInputText txtCurDescription) {
        this.txtCurDescription = txtCurDescription;
    }

    public RichInputText getTxtCurDescription() {
        return txtCurDescription;
    }

    public void setTxtCurRound(RichInputNumberSpinbox txtCurRound) {
        this.txtCurRound = txtCurRound;
    }

    public RichInputNumberSpinbox getTxtCurRound() {
        return txtCurRound;
    }

    public void setTxtCurNumWord(RichInputText txtCurNumWord) {
        this.txtCurNumWord = txtCurNumWord;
    }

    public RichInputText getTxtCurNumWord() {
        return txtCurNumWord;
    }

    public void setTxtCurDecimalWord(RichInputText txtCurDecimalWord) {
        this.txtCurDecimalWord = txtCurDecimalWord;
    }

    public RichInputText getTxtCurDecimalWord() {
        return txtCurDecimalWord;
    }

    public void setBtnSaveUpdateCurrency(RichCommandButton btnSaveUpdateCurrency) {
        this.btnSaveUpdateCurrency = btnSaveUpdateCurrency;
    }

    public RichCommandButton getBtnSaveUpdateCurrency() {
        return btnSaveUpdateCurrency;
    }

    public void setTxtCudCode(RichInputText txtCudCode) {
        this.txtCudCode = txtCudCode;
    }

    public RichInputText getTxtCudCode() {
        return txtCudCode;
    }

    public void setTxtCudCurCode(RichInputText txtCudCurCode) {
        this.txtCudCurCode = txtCudCurCode;
    }

    public RichInputText getTxtCudCurCode() {
        return txtCudCurCode;
    }

    public void setTxtCudValue(RichInputNumberSpinbox txtCudValue) {
        this.txtCudValue = txtCudValue;
    }

    public RichInputNumberSpinbox getTxtCudValue() {
        return txtCudValue;
    }

    public void setTxtCudName(RichInputText txtCudName) {
        this.txtCudName = txtCudName;
    }

    public RichInputText getTxtCudName() {
        return txtCudName;
    }

    public void setTxtCudWefDate(RichInputDate txtCudWefDate) {
        this.txtCudWefDate = txtCudWefDate;
    }

    public RichInputDate getTxtCudWefDate() {
        return txtCudWefDate;
    }

    public void setBtnSaveUpdateCurrencyDenomination(RichCommandButton btnSaveUpdateCurrencyDenomination) {
        this.btnSaveUpdateCurrencyDenomination =
                btnSaveUpdateCurrencyDenomination;
    }

    public RichCommandButton getBtnSaveUpdateCurrencyDenomination() {
        return btnSaveUpdateCurrencyDenomination;
    }

    public void tblCurrencyListener(SelectionEvent selectionEvent) {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("currencyCode",
                                 nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
            GlobalCC.refreshUI(tblCurrencyDenomination);
            btnActionEditCurrency.setDisabled(false);
            btnActionDeleteCurrency.setDisabled(false);
            btnActionNewCurrencyDenomination.setDisabled(false);
            btnActionEditCurrencyDenomination.setDisabled(true);
            btnActionDeleteCurrencyDenomination.setDisabled(true);
            GlobalCC.refreshUI(btnActionEditCurrency);
            GlobalCC.refreshUI(btnActionDeleteCurrency);
            GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
            GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
            GlobalCC.refreshUI(btnActionEditCurrencyDenomination);

        }
    }

    public void clearCurrencyFields() {
        txtCurCode.setValue(null);
        txtCurSymbol.setValue(null);
        txtCurDescription.setValue(null);
        txtCurRound.setValue(null);
        txtCurNumWord.setValue(null);
        txtCurDecimalWord.setValue(null);

        btnSaveUpdateCurrency.setText("Save");
    }

    public String actionNewCurrency() {
        clearCurrencyFields();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:currencyPop" + "').show(hints);");
        return null;
    }

    public String actionEditCurrency() {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtCurCode.setValue(nodeBinding.getAttribute("code"));
            txtCurSymbol.setValue(nodeBinding.getAttribute("symbol"));
            txtCurDescription.setValue(nodeBinding.getAttribute("description"));
            txtCurRound.setValue(nodeBinding.getAttribute("round"));
            txtCurNumWord.setValue(nodeBinding.getAttribute("curNumWord"));
            txtCurDecimalWord.setValue(nodeBinding.getAttribute("curDecimalWord"));

            btnSaveUpdateCurrency.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:currencyPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteCurrency() {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            BaseSetupDAO baseSetupDAO = new BaseSetupDAO();
            if (baseSetupDAO.fetchCurrencyDenominationsByCurrency().size() >
                0) {
                GlobalCC.INFORMATIONREPORTING("The selected Currency has Currency Denominations defined for it. You need to delete them first.");
                return null;
            } else {
                String code = nodeBinding.getAttribute("code").toString();

                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "D");
                    statement.setBigDecimal(2, new BigDecimal(code));
                    statement.setString(3, null);
                    statement.setString(4, null);
                    statement.setString(5, null);
                    statement.registerOutParameter(6, OracleTypes.VARCHAR);
                    statement.setString(7, null);
                    statement.setString(8, null);

                    statement.execute();
                    String err = statement.getString(6);
                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrency);

                    ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyDenomination);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                        return null;
                    } else {

                        String message = "Record DELETED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        btnActionEditCurrency.setDisabled(true);
                        btnActionDeleteCurrency.setDisabled(true);
                        btnActionNewCurrencyDenomination.setDisabled(true);
                        btnActionEditCurrencyDenomination.setDisabled(true);
                        btnActionDeleteCurrencyDenomination.setDisabled(true);
                        GlobalCC.refreshUI(btnActionEditCurrency);
                        GlobalCC.refreshUI(btnActionDeleteCurrency);
                        GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                        GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
                        GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                    }
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateCurrency() {
        if (btnSaveUpdateCurrency.getText().equals("Edit")) {
            actionUpdateCurrency();
        } else {
            String code = GlobalCC.checkNullValues(txtCurCode.getValue());
            String symbol = GlobalCC.checkNullValues(txtCurSymbol.getValue());
            String description =
                GlobalCC.checkNullValues(txtCurDescription.getValue());
            String round = GlobalCC.checkNullValues(txtCurRound.getValue());
            String numWord =
                GlobalCC.checkNullValues(txtCurNumWord.getValue());
            String decWord =
                GlobalCC.checkNullValues(txtCurDecimalWord.getValue());

            if (symbol == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Symbol is Empty");
                return null;

            } else if (description == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);

                    statement.setString(1, "A");
                    statement.setBigDecimal(2, null);
                    statement.setString(3, symbol);
                    statement.setString(4, description);
                    statement.setBigDecimal(5,
                                            round == null ? null : new BigDecimal(round));
                    statement.registerOutParameter(6, OracleTypes.VARCHAR);
                    statement.setString(7, numWord);
                    statement.setString(8, decWord);
                    statement.execute();
                    String err = statement.getString(6);
                    statement.close();
                    conn.commit();
                    conn.close();


                    ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrency);

                    ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyDenomination);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                        return null;
                    } else {
                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "crm:currencyPop" +
                                             "').hide(hints);");
                        btnActionEditCurrency.setDisabled(true);
                        btnActionDeleteCurrency.setDisabled(true);
                        btnActionNewCurrencyDenomination.setDisabled(true);
                        btnActionEditCurrencyDenomination.setDisabled(true);
                        btnActionDeleteCurrencyDenomination.setDisabled(true);
                        GlobalCC.refreshUI(btnActionEditCurrency);
                        GlobalCC.refreshUI(btnActionDeleteCurrency);
                        GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                        GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
                        GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                    }
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateCurrency() {
        String code = GlobalCC.checkNullValues(txtCurCode.getValue());
        String symbol = GlobalCC.checkNullValues(txtCurSymbol.getValue());
        String description =
            GlobalCC.checkNullValues(txtCurDescription.getValue());
        String round = GlobalCC.checkNullValues(txtCurRound.getValue());
        String numWord = GlobalCC.checkNullValues(txtCurNumWord.getValue());
        String decWord =
            GlobalCC.checkNullValues(txtCurDecimalWord.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (symbol == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Symbol is Empty");
            return null;

        } else if (description == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                statement.setString(1, "E");
                statement.setBigDecimal(2, new BigDecimal(code));
                statement.setString(3, symbol);
                statement.setString(4, description);
                statement.setBigDecimal(5,
                                        round == null ? null : new BigDecimal(round));
                statement.registerOutParameter(6, OracleTypes.VARCHAR);
                statement.setString(7, numWord);
                statement.setString(8, decWord);
                statement.execute();
                String err = statement.getString(6);
                statement.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrency);

                ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyDenomination);
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);

                    return null;
                } else {
                    String message = "Record UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:currencyPop" +
                                         "').hide(hints);");
                    btnActionEditCurrency.setDisabled(true);
                    btnActionDeleteCurrency.setDisabled(true);
                    btnActionNewCurrencyDenomination.setDisabled(true);
                    btnActionEditCurrencyDenomination.setDisabled(true);
                    btnActionDeleteCurrencyDenomination.setDisabled(true);
                    GlobalCC.refreshUI(btnActionEditCurrency);
                    GlobalCC.refreshUI(btnActionDeleteCurrency);
                    GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                    GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
                    GlobalCC.refreshUI(btnActionNewCurrencyDenomination);
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void clearCurrencyDenominationFields() {
        txtCudCode.setValue(null);
        txtCudCurCode.setValue(session.getAttribute("currencyCode"));
        txtCudValue.setValue(null);
        txtCudName.setValue(null);
        txtCudWefDate.setValue(null);

        btnSaveUpdateCurrencyDenomination.setText("Save");
    }

    public String actionNewCurrencyDenomination() {
        if (session.getAttribute("currencyCode") != null) {
            clearCurrencyDenominationFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:currencyDenominationPop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Currency Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditCurrencyDenomination() {
        Object key2 = tblCurrencyDenomination.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtCudCode.setValue(nodeBinding.getAttribute("code"));
            txtCudCurCode.setValue(nodeBinding.getAttribute("currencyCode"));
            txtCudValue.setValue(nodeBinding.getAttribute("value"));
            txtCudName.setValue(nodeBinding.getAttribute("name"));
            txtCudWefDate.setValue(nodeBinding.getAttribute("wef"));

            btnSaveUpdateCurrencyDenomination.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:currencyDenominationPop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteCurrencyDenomination() {
        Object key2 = tblCurrencyDenomination.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2, new BigDecimal(code));
                statement.setBigDecimal(3, null);
                statement.setString(4, null);
                statement.setString(5, null);
                statement.setDate(6, null);
                statement.registerOutParameter(7, OracleTypes.VARCHAR);

                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyDenomination);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Currency Denomination Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateCurrencyDenomination() {
        if (btnSaveUpdateCurrencyDenomination.getText().equals("Edit")) {
            actionUpdateCurrencyDenomination();
        } else {
            String code = GlobalCC.checkNullValues(txtCudCode.getValue());
            String curCode =
                GlobalCC.checkNullValues(txtCudCurCode.getValue());
            String value = GlobalCC.checkNullValues(txtCudValue.getValue());
            String name = GlobalCC.checkNullValues(txtCudName.getValue());
            String wef = GlobalCC.checkNullValues(txtCudWefDate.getValue());

            if (curCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Currency Code is Empty");
                return null;

            } else if (value == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Value is Empty");
                return null;

            } else if (name == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            } else if (wef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpWefDate = new Date();
                    if (txtCudWefDate.getValue() != null &&
                        !(txtCudWefDate.getValue().equals(""))) {
                        String date1 = df.format(txtCudWefDate.getValue());
                        tmpWefDate = df.parse(date1);
                    }

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setBigDecimal(2, null);
                    statement.setBigDecimal(3, new BigDecimal(curCode));
                    statement.setBigDecimal(4,
                                            value == null ? null : new BigDecimal(value));
                    statement.setString(5, name);
                    statement.setDate(6,
                                      tmpWefDate == null ? null : new java.sql.Date(tmpWefDate.getTime()));
                    statement.registerOutParameter(7, OracleTypes.VARCHAR);
                    statement.execute();
                    String err = statement.getString(7);
                    statement.close();
                    conn.commit();
                    conn.close();


                    ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyDenomination);
                    btnActionEditCurrencyDenomination.setDisabled(true);
                    btnActionDeleteCurrencyDenomination.setDisabled(true);
                    GlobalCC.refreshUI(btnActionEditCurrencyDenomination);
                    GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                        return null;
                    } else {
                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "crm:currencyDenominationPop" +
                                             "').hide(hints);");

                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionShowDeleteDenominitions() {
        Object key2 = tblCurrencyDenomination.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDenominationDeleteDialog" +
                                 "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

    }

    public String actionShowDeleteCurrency() {
        Object key2 = tblCurrency.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteCurrency" +
                                 "').show(hints);");
            return null;

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
    }

    public String actionUpdateCurrencyDenomination() {
        String code = GlobalCC.checkNullValues(txtCudCode.getValue());
        String curCode = GlobalCC.checkNullValues(txtCudCurCode.getValue());
        String value = GlobalCC.checkNullValues(txtCudValue.getValue());
        String name = GlobalCC.checkNullValues(txtCudName.getValue());
        String wef = GlobalCC.checkNullValues(txtCudWefDate.getValue());

        if (curCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Currency Code is Empty");
            return null;

        } else if (value == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Value is Empty");
            return null;

        } else if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
            return null;

        } else if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtCudWefDate.getValue() != null &&
                    !(txtCudWefDate.getValue().equals(""))) {
                    String date1 = df.format(txtCudWefDate.getValue());
                    tmpWefDate = df.parse(date1);
                }

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setBigDecimal(2, new BigDecimal(code));
                statement.setBigDecimal(3, new BigDecimal(curCode));
                statement.setBigDecimal(4,
                                        value == null ? null : new BigDecimal(value));
                statement.setString(5, name);
                statement.setDate(6,
                                  tmpWefDate == null ? null : new java.sql.Date(tmpWefDate.getTime()));
                statement.registerOutParameter(7, OracleTypes.VARCHAR);
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
                                     "crm:currencyDenominationPop" +
                                     "').hide(hints);");

                ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyDenomination);

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void currencyDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteCurrency();
        }
    }

    public void currencyDenominationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteCurrencyDenomination();
        }
    }

    public void setBtnActionEditCurrency(RichCommandButton btnActionEditCurrency) {
        this.btnActionEditCurrency = btnActionEditCurrency;
    }

    public RichCommandButton getBtnActionEditCurrency() {
        return btnActionEditCurrency;
    }

    public void setActionDeleteCurrency(RichCommandButton actionDeleteCurrency) {
        this.actionDeleteCurrency = actionDeleteCurrency;
    }

    public RichCommandButton getActionDeleteCurrency() {
        return actionDeleteCurrency;
    }

    public void setBtnActionDeleteCurrency(RichCommandButton btnActionDeleteCurrency) {
        this.btnActionDeleteCurrency = btnActionDeleteCurrency;
    }

    public RichCommandButton getBtnActionDeleteCurrency() {
        return btnActionDeleteCurrency;
    }

    public void setBtnActionNewCurrencyDenomination(RichCommandButton btnActionNewCurrencyDenomination) {
        this.btnActionNewCurrencyDenomination =
                btnActionNewCurrencyDenomination;
    }

    public RichCommandButton getBtnActionNewCurrencyDenomination() {
        return btnActionNewCurrencyDenomination;
    }

    public void setBtnActionEditCurrencyDenomination(RichCommandButton btnActionEditCurrencyDenomination) {
        this.btnActionEditCurrencyDenomination =
                btnActionEditCurrencyDenomination;
    }

    public RichCommandButton getBtnActionEditCurrencyDenomination() {
        return btnActionEditCurrencyDenomination;
    }

    public void setBtnActionDeleteCurrencyDenomination(RichCommandButton btnActionDeleteCurrencyDenomination) {
        this.btnActionDeleteCurrencyDenomination =
                btnActionDeleteCurrencyDenomination;
    }

    public RichCommandButton getBtnActionDeleteCurrencyDenomination() {
        return btnActionDeleteCurrencyDenomination;
    }


    public void tblDenominations(SelectionEvent selectionEvent) {


        btnActionEditCurrencyDenomination.setDisabled(false);
        btnActionDeleteCurrencyDenomination.setDisabled(false);
        GlobalCC.refreshUI(btnActionDeleteCurrencyDenomination);
        GlobalCC.refreshUI(btnActionEditCurrencyDenomination);
    }

    public void setTxtCountryName(RichInputText txtCountryName) {
        this.txtCountryName = txtCountryName;
    }

    public RichInputText getTxtCountryName() {
        return txtCountryName;
    }

    public void setTxtShtDesc(RichInputText txtShtDesc) {
        this.txtShtDesc = txtShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtShtDesc;
    }

    public String addNewCountry() {
        session.setAttribute("action", "A");
        txtCountryName.setValue(null);
        txtShtDesc.setValue(null);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CountryPop" + "').show(hints);");

        return null;
    }

    public String saveCountry() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            if (txtCountryName.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please enter Country Name");
                return null;
            }
            if (txtShtDesc.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please enter Sht Desc");
                return null;
            }
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.AddUpdateCountry(?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);

            statement.setString(1, session.getAttribute("action").toString());
            if (session.getAttribute("countryCode") == null) {
                statement.setBigDecimal(2, null);
            } else {
                statement.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("countryCode"));
            }
            statement.setString(4, txtCountryName.getValue().toString());
            statement.setString(3, txtShtDesc.getValue().toString());
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
                                 "crm:CountryPop" + "').hide(hints);");
            ADFUtils.findIterator("findCountriesIterator").executeQuery();
            GlobalCC.refreshUI(countryTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String editCountry() {
        session.setAttribute("action", "E");
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        session.setAttribute("countryCode", r.getAttribute("countryCode"));
        txtShtDesc.setValue(r.getAttribute("couShtDesc"));
        txtCountryName.setValue(r.getAttribute("counntryName"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CountryPop" + "').show(hints);");
        return null;
    }

    public void setCountryTbl(RichTable countryTbl) {
        this.countryTbl = countryTbl;
    }

    public RichTable getCountryTbl() {
        return countryTbl;
    }

    public String deleteCountry() {
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.AddUpdateCountry(?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);

            statement.setString(1, "D");
            statement.setBigDecimal(2,
                                    (BigDecimal)r.getAttribute("countryCode"));
            statement.setString(4, null);
            statement.setString(3, null);
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findCountriesIterator").executeQuery();
            GlobalCC.refreshUI(countryTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void selectCountry(SelectionEvent selectionEvent) {
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        session.setAttribute("countryCode", r.getAttribute("countryCode"));
        ADFUtils.findIterator("findCountiesIterator").executeQuery();
        GlobalCC.refreshUI(countyTbl);
    }

    public void setCountyTbl(RichTable countyTbl) {
        this.countyTbl = countyTbl;
    }

    public RichTable getCountyTbl() {
        return countyTbl;
    }

    public void setTxtPopup(RichInputText txtPopup) {
        this.txtPopup = txtPopup;
    }

    public RichInputText getTxtPopup() {
        return txtPopup;
    }

    public void setCmdButton(RichCommandButton cmdButton) {
        this.cmdButton = cmdButton;
    }

    public RichCommandButton getCmdButton() {
        return cmdButton;
    }

    public String selectCountry() {
        Object key = couTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        txtPopup.setValue(r.getAttribute("counntryName"));
        GlobalCC.refreshUI(txtPopup);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:CountryPop" + "').show(hints);");
        return null;
    }

    public void setCouTbl(RichTable couTbl) {
        this.couTbl = couTbl;
    }

    public RichTable getCouTbl() {
        return couTbl;
    }
}
