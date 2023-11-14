package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class BaseSetupsBacking1 {
    private RichSelectOneChoice txtProxyAuthType;
    private RichInputText txtProxyPassword;
    private RichInputText txtProxyUsername;
    private RichInputNumberSpinbox txtProxyPort;
    private RichInputText txtProxyHost;
    private RichSelectOneChoice txtSecEncryptType;
    private RichSelectOneChoice txtSecAuthType;
    private RichInputText txtSecPassword;
    private RichInputText txtSecUsename;
    private RichInputText txtFilterCommand;
    private RichSelectOneChoice txtFilter;
    private RichInputText txtUri;
    private RichInputText txtServerName;
    private RichInputText txtCode;
    private RichCommandButton btnSavePrintServer;
    private RichTable tblPrinterServers;
    private RichInputText txtAccountNo;

    public BaseSetupsBacking1() {
    }

    public String actionNewPrintServer() {
        clearPrinterServerForm();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:printerServer" + "').show(hints);");

        return null;
    }

    public String actionSavePrinterServer() {

        String code = GlobalCC.checkNullValues(txtCode.getValue());
        String serverName = GlobalCC.checkNullValues(txtServerName.getValue());
        String uri = GlobalCC.checkNullValues(txtUri.getValue());
        String filter = GlobalCC.checkNullValues(txtFilter.getValue());
        String filterCommand =
            GlobalCC.checkNullValues(txtFilterCommand.getValue());
        String secUsr = GlobalCC.checkNullValues(txtSecUsename.getValue());
        String secPwd = GlobalCC.checkNullValues(txtSecPassword.getValue());
        String secAuth = GlobalCC.checkNullValues(txtSecAuthType.getValue());
        String secEncrypt =
            GlobalCC.checkNullValues(txtSecEncryptType.getValue());
        String proxyHost = GlobalCC.checkNullValues(txtProxyHost.getValue());
        String proxyPort = GlobalCC.checkNullValues(txtProxyPort.getValue());
        String proxyUsr =
            GlobalCC.checkNullValues(txtProxyUsername.getValue());
        String proxyPwd =
            GlobalCC.checkNullValues(txtProxyPassword.getValue());
        String proxyAuth =
            GlobalCC.checkNullValues(txtProxyAuthType.getValue());
        String option = null;
        if (serverName == null) {
            GlobalCC.errorValueNotEntered("Server Name required");
            return null;
        }
        if (uri == null) {
            GlobalCC.errorValueNotEntered("URI required");
            return null;
        }
        if (btnSavePrintServer.getText().equalsIgnoreCase("Save")) {
            option = "A";
        } else {
            option = "E";
            if (code == null) {
                GlobalCC.INFORMATIONREPORTING("Server Code:: Required::");
            }
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_SETUPS_PKG.printer_server_prc(?,?,?, ?,?,?, ?,?,?, ?,?,? ,?,?,? ); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables
            callStmt.setString(1, option);
            callStmt.setBigDecimal(2,
                                   code == null ? null : new BigDecimal(code));
            callStmt.setString(3, serverName);
            callStmt.setString(4, filter);
            callStmt.setString(5, uri);
            callStmt.setString(6, filterCommand);
            callStmt.setString(7, secUsr);
            callStmt.setString(8, secPwd);
            callStmt.setString(9, secAuth);
            callStmt.setString(10, secEncrypt);
            callStmt.setString(11, proxyHost);
            callStmt.setString(12, proxyPort);
            callStmt.setString(13, proxyUsr);
            callStmt.setString(14, proxyPwd);
            callStmt.setString(15, proxyAuth);
            callStmt.execute();
            callStmt.close();
            conn.commit();
            conn.close();

            clearPrinterServerForm();
            ADFUtils.findIterator("fetchAllPrintServersIterator").executeQuery();
            GlobalCC.refreshUI(tblPrinterServers);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:printerServer" + "').hide(hints);");
            if (option.equalsIgnoreCase("A")) {
                GlobalCC.INFORMATIONREPORTING("Record saved Successfully:");
                return null;
            } else {
                GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }


    }

    public void clearPrinterServerForm() {
        txtCode.setValue(null);
        txtServerName.setValue(null);
        txtUri.setValue(null);
        txtFilter.setValue("Pdf to Post Script");
        txtFilterCommand.setValue(null);
        txtSecUsename.setValue(null);
        txtSecPassword.setValue(null);
        txtSecAuthType.setValue("None");
        txtSecEncryptType.setValue("None");
        txtProxyHost.setValue(null);
        txtProxyPort.setValue(null);
        txtProxyUsername.setValue(null);
        txtProxyPassword.setValue(null);
        txtProxyAuthType.setValue("None");
        btnSavePrintServer.setText("Save");

    }

    public String actionEditPrintServer() {
        Object key2 = tblPrinterServers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            btnSavePrintServer.setText("Update");
            txtCode.setValue(nodeBinding.getAttribute("code"));
            txtServerName.setValue(nodeBinding.getAttribute("name"));
            txtUri.setValue(nodeBinding.getAttribute("uri"));
            txtFilter.setValue(nodeBinding.getAttribute("filter"));
            txtFilterCommand.setValue(nodeBinding.getAttribute("filter_command"));
            txtSecUsename.setValue(nodeBinding.getAttribute("sec_username"));
            txtSecPassword.setValue(nodeBinding.getAttribute("sec_password"));
            txtSecAuthType.setValue(nodeBinding.getAttribute("sec_auth_type"));
            txtSecEncryptType.setValue(nodeBinding.getAttribute("sec_encrpt_type"));
            txtProxyHost.setValue(nodeBinding.getAttribute("proxy_host"));
            txtProxyPort.setValue(nodeBinding.getAttribute("proxy_port"));
            txtProxyUsername.setValue(nodeBinding.getAttribute("proxy_username"));
            txtProxyPassword.setValue(nodeBinding.getAttribute("proxy_pasword"));
            txtProxyAuthType.setValue(nodeBinding.getAttribute("proxy_authen_type"));
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:printerServer" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

    }

    public String actionDeletePrintServer() {
        Object key2 = tblPrinterServers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeletePrinterServer" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected!");
            return null;

        }
        return null;
    }

    public void actionConfirmDeletePrinterServer(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = tblPrinterServers.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                String code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("code"));
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn;
                conn = dbConnector.getDatabaseConnection();
                try {
                    String query = null;
                    //modify the query for treaty groups procedure
                    query =
                            "begin TQC_SETUPS_PKG.printer_server_prc(?,?,?, ?,?,?, ?,?,?, ?,?,? ,?,?,? ); end;";


                    OracleCallableStatement callStmt = null;
                    callStmt =
                            (OracleCallableStatement)conn.prepareCall(query);
                    //bind the variables
                    callStmt.setString(1, "D");
                    callStmt.setBigDecimal(2,
                                           code == null ? null : new BigDecimal(code));
                    callStmt.setString(3, null);
                    callStmt.setString(4, null);
                    callStmt.setString(5, null);
                    callStmt.setString(6, null);
                    callStmt.setString(7, null);
                    callStmt.setString(8, null);
                    callStmt.setString(9, null);
                    callStmt.setString(10, null);
                    callStmt.setString(11, null);
                    callStmt.setString(12, null);
                    callStmt.setString(13, null);
                    callStmt.setString(14, null);
                    callStmt.setString(15, null);
                    callStmt.execute();
                    callStmt.close();
                    conn.commit();
                    conn.close();

                    clearPrinterServerForm();
                    ADFUtils.findIterator("fetchAllPrintServersIterator").executeQuery();
                    GlobalCC.refreshUI(tblPrinterServers);

                    GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully:");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);

                }
            }
        }
    }

    public void setTxtProxyAuthType(RichSelectOneChoice txtProxyAuthType) {
        this.txtProxyAuthType = txtProxyAuthType;
    }

    public RichSelectOneChoice getTxtProxyAuthType() {
        return txtProxyAuthType;
    }

    public void setTxtProxyPassword(RichInputText txtProxyPassword) {
        this.txtProxyPassword = txtProxyPassword;
    }

    public RichInputText getTxtProxyPassword() {
        return txtProxyPassword;
    }

    public void setTxtProxyUsername(RichInputText txtProxyUsername) {
        this.txtProxyUsername = txtProxyUsername;
    }

    public RichInputText getTxtProxyUsername() {
        return txtProxyUsername;
    }

    public void setTxtProxyPort(RichInputNumberSpinbox txtProxyPort) {
        this.txtProxyPort = txtProxyPort;
    }

    public RichInputNumberSpinbox getTxtProxyPort() {
        return txtProxyPort;
    }

    public void setTxtProxyHost(RichInputText txtProxyHost) {
        this.txtProxyHost = txtProxyHost;
    }

    public RichInputText getTxtProxyHost() {
        return txtProxyHost;
    }

    public void setTxtSecEncryptType(RichSelectOneChoice txtSecEncryptType) {
        this.txtSecEncryptType = txtSecEncryptType;
    }

    public RichSelectOneChoice getTxtSecEncryptType() {
        return txtSecEncryptType;
    }

    public void setTxtSecAuthType(RichSelectOneChoice txtSecAuthType) {
        this.txtSecAuthType = txtSecAuthType;
    }

    public RichSelectOneChoice getTxtSecAuthType() {
        return txtSecAuthType;
    }

    public void setTxtSecPassword(RichInputText txtSecPassword) {
        this.txtSecPassword = txtSecPassword;
    }

    public RichInputText getTxtSecPassword() {
        return txtSecPassword;
    }

    public void setTxtSecUsename(RichInputText txtSecUsename) {
        this.txtSecUsename = txtSecUsename;
    }

    public RichInputText getTxtSecUsename() {
        return txtSecUsename;
    }

    public void setTxtFilterCommand(RichInputText txtFilterCommand) {
        this.txtFilterCommand = txtFilterCommand;
    }

    public RichInputText getTxtFilterCommand() {
        return txtFilterCommand;
    }

    public void setTxtFilter(RichSelectOneChoice txtFilter) {
        this.txtFilter = txtFilter;
    }

    public RichSelectOneChoice getTxtFilter() {
        return txtFilter;
    }

    public void setTxtUri(RichInputText txtUri) {
        this.txtUri = txtUri;
    }

    public RichInputText getTxtUri() {
        return txtUri;
    }

    public void setTxtServerName(RichInputText txtServerName) {
        this.txtServerName = txtServerName;
    }

    public RichInputText getTxtServerName() {
        return txtServerName;
    }

    public void setTxtCode(RichInputText txtCode) {
        this.txtCode = txtCode;
    }

    public RichInputText getTxtCode() {
        return txtCode;
    }

    public void setBtnSavePrintServer(RichCommandButton btnSavePrintServer) {
        this.btnSavePrintServer = btnSavePrintServer;
    }

    public RichCommandButton getBtnSavePrintServer() {
        return btnSavePrintServer;
    }

    public void setTblPrinterServers(RichTable tblPrinterServers) {
        this.tblPrinterServers = tblPrinterServers;
    }

    public RichTable getTblPrinterServers() {
        return tblPrinterServers;
    }


    public void setTxtAccountNo(RichInputText txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public RichInputText getTxtAccountNo() {
        return txtAccountNo;
    }
}
