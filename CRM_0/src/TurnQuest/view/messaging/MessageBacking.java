package TurnQuest.view.messaging;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class MessageBacking {

    private RichTree systemTree;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable messageTemplateLov;
    private RichInputText txtMessageCode;
    private RichInputText txtMessage;
    private RichSelectOneChoice txtSysModule;
    private RichSelectOneChoice txtMessageType;

    public void setSystemTree(RichTree systemTree) {
        this.systemTree = systemTree;
    }

    public RichTree getSystemTree() {
        return systemTree;
    }

    public void systemSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    systemTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)systemTree.getRowData();
                    BigDecimal sysCode =
                        (BigDecimal)nd.getRow().getAttribute("sysCode");
                    session.setAttribute("sysCode", sysCode);
                    ADFUtils.findIterator("findMessageTemplatesIterator").executeQuery();
                    GlobalCC.refreshUI(messageTemplateLov);
                }
            }
        }
    }

    public void setMessageTemplateLov(RichTable messageTemplateLov) {
        this.messageTemplateLov = messageTemplateLov;
    }

    public RichTable getMessageTemplateLov() {
        return messageTemplateLov;
    }

    public void setTxtMessageCode(RichInputText txtMessageCode) {
        this.txtMessageCode = txtMessageCode;
    }

    public RichInputText getTxtMessageCode() {
        return txtMessageCode;
    }

    public void setTxtMessage(RichInputText txtMessage) {
        this.txtMessage = txtMessage;
    }

    public RichInputText getTxtMessage() {
        return txtMessage;
    }

    public void setTxtSysModule(RichSelectOneChoice txtSysModule) {
        this.txtSysModule = txtSysModule;
    }

    public RichSelectOneChoice getTxtSysModule() {
        return txtSysModule;
    }

    public void setTxtMessageType(RichSelectOneChoice txtMessageType) {
        this.txtMessageType = txtMessageType;
    }

    public RichSelectOneChoice getTxtMessageType() {
        return txtMessageType;
    }

    public String addMessageTemplate() {
        if (session.getAttribute("sysCode") == null) {
            GlobalCC.errorValueNotEntered("Select System to Add Message Template!");
            return null;
        }
        txtMessageCode.setValue(null);
        txtMessage.setValue(null);
        txtSysModule.setValue(null);
        txtMessageType.setValue(null);
        session.removeAttribute("msgTmpCode");
        session.setAttribute("addAction", "A");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crmTemplate:p1" + "').show(hints);");
        return null;
    }

    public String saveMessageTemplate() {
        if (txtMessageCode.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error Enter Message Code");
        } else if (txtMessage.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error Enter Message");
        } else if (txtSysModule.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error Select System Module");
        } else if (txtMessageType.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error Select Message Type");
        } else {
            OracleConnection conn = null;
            try {
                String Query =
                    "begin TQC_SETUPS_PKG.message_templates_prc(?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                DBConnector datahandler = new DBConnector();

                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, (String)session.getAttribute("addAction"));
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("msgTmpCode"));
                cst.setString(3, txtMessageCode.getValue().toString());
                cst.setString(4, txtMessage.getValue().toString());
                cst.setBigDecimal(5,
                                  (BigDecimal)session.getAttribute("sysCode"));
                cst.setString(6, txtSysModule.getValue().toString());
                cst.setString(7, txtMessageType.getValue().toString());
                cst.execute();

                cst.close();
                conn.close();

                ADFUtils.findIterator("findMessageTemplatesIterator").executeQuery();
                GlobalCC.refreshUI(messageTemplateLov);

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "AdfPage.PAGE.findComponent('" +
                                     "crmTemplate:p1" + "').hide();");

                String message = "New Record CREATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String editMessageTemplate() {
        Object key2 = messageTemplateLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("msgTmpCode",
                                 nodeBinding.getAttribute("msgtCode"));

            txtMessageCode.setValue(nodeBinding.getAttribute("msgtShtDesc"));
            txtMessage.setValue(nodeBinding.getAttribute("msgtMsg"));
            txtSysModule.setValue(nodeBinding.getAttribute("msgtSysModule"));
            txtMessageType.setValue(nodeBinding.getAttribute("msgtType"));
            session.setAttribute("addAction", "E");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crmTemplate:p1" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String deleteMessageTemplate() {
        Object key2 = messageTemplateLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("msgTmpCode",
                                 nodeBinding.getAttribute("msgtCode"));
            session.setAttribute("addAction", "D");

            String Query =
                "begin TQC_SETUPS_PKG.message_templates_prc(?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;

            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();


            try {
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, (String)session.getAttribute("addAction"));
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("msgTmpCode"));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setBigDecimal(5,
                                  (BigDecimal)session.getAttribute("sysCode"));
                cst.setString(6, null);
                cst.setString(7, null);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findMessageTemplatesIterator").executeQuery();
                GlobalCC.refreshUI(messageTemplateLov);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }
}
