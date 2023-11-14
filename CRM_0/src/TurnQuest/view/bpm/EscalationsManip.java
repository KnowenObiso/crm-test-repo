package TurnQuest.view.bpm;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class EscalationsManip {
    private RichTree systemProcesses;
    private RichTable activities;
    private RichTable levels;
    private RichTable users;

    private RichInputText escCode;
    private RichSelectOneChoice escLevel;
    private RichInputText escAssignee;
    private RichInputText ccAssignee;
    private RichInputNumberSpinbox escDuration;
    private RichSelectBooleanCheckbox lvlCheck;

    private RichInputText escCode1;
    private RichSelectOneChoice escLevel1;
    private RichInputText escAssignee1;
    private RichInputText ccAssignee1;
    private RichInputNumberSpinbox escDuration1;
    private RichSelectBooleanCheckbox lvlCheck1;

    private RichInputText escCode2;
    private RichSelectOneChoice escLevel2;
    private RichInputText escAssignee2;
    private RichInputText ccAssignee2;
    private RichInputNumberSpinbox escDuration2;
    private RichSelectBooleanCheckbox lvlCheck2;

    private RichInputText escCode3;
    private RichSelectOneChoice escLevel3;
    private RichInputText escAssignee3;
    private RichInputText ccAssignee3;
    private RichInputNumberSpinbox escDuration3;
    private RichSelectBooleanCheckbox lvlCheck3;
    private RichPanelGroupLayout dtls;
    
    private RichCommandButton btnSaveUpdateEsc;
    private RichTable escalationTbl;    
    private BigDecimal sreCode;
    private String sreSrtCode;
    private RichInputText sreLvlDuration;
    private RichInputText sreLvlOneAssignee;
    private RichInputText sreLvlTwoDuration;
    private RichInputText sreLvlTwoAssignee;
    private RichInputText sreSrDesc;
    private RichTable servReqCatTbl;
    private RichTable usersLvlOneTbl;
    private RichTable usersLvlTwoTbl;
    private RichInputNumberSpinbox sreLvlOneDur;
    private RichInputNumberSpinbox sreLvlTwoDurat;


    public EscalationsManip() {
        super();
    }

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public void treeSysProcessSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    systemProcesses.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)systemProcesses.getRowData();

                    if (nd.getRow().getAttribute("type") == "S") {

                        session.setAttribute("sysCode",
                                             nd.getRow().getAttribute("sysCode"));
                        session.setAttribute("jpdlName",
                                             nd.getRow().getAttribute("name"));
                        /*

                       shtDesc.setValue(nd.getRow().getAttribute("DIV_SHT_DESC"));
                       divName.setValue(nd.getRow().getAttribute("DIV_NAME"));
                       divStatus.setValue(nd.getRow().getAttribute("DIV_DIVISION_STATUS"));

                        //shtDesc.setDisabled(true);
                       // divName.setDisabled(true);
                       // divStatus.setDisabled(true);

                      GlobalCC.refreshUI(shtDesc);
                      GlobalCC.refreshUI(divName);
                      GlobalCC.refreshUI(divStatus);

                      ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
                      GlobalCC.refreshUI(subDivisionsLOV);

                      ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
                      GlobalCC.refreshUI(activities);*/

                    } else if (nd.getRow().getAttribute("type") == "SP") {
                        session.setAttribute("sysCode",
                                             nd.getRow().getAttribute("sysCode"));
                        session.setAttribute("deployment",
                                             nd.getRow().getAttribute("deployment"));
                        session.setAttribute("jpdlName",
                                             nd.getRow().getAttribute("name"));
                        session.setAttribute("activity", null);
        
                        ADFUtils.findIterator("findProcessDefActivitiesIterator").executeQuery();
                        GlobalCC.refreshUI(activities);
                        ADFUtils.findIterator("findEscalationLevelsOneIterator").executeQuery();
                        ADFUtils.findIterator("findEscalationLevelsTwoIterator").executeQuery();
                        ADFUtils.findIterator("findEscalationLevelsThreeIterator").executeQuery();
                        ADFUtils.findIterator("findEscalationLevelsFourIterator").executeQuery();
                        GlobalCC.refreshUI(dtls);
                        //ADFUtils.findIterator("findEscalationLevelsIterator").executeQuery();
                        // GlobalCC.refreshUI(levels);
                        // Hide the panel
                        //panelTransList.setVisible(false);
                        //GlobalCC.refreshUI(panelMain);
                    }
                }
            }
        }
    }
    
    public void treeEscalationSelectListener(SelectionEvent selectionEvent) {
       
    }

    public void setSystemProcesses(RichTree systemProcesses) {
        this.systemProcesses = systemProcesses;
    }

    public RichTree getSystemProcesses() {
        return systemProcesses;
    }

    public void setActivities(RichTable activities) {
        this.activities = activities;
    }

    public RichTable getActivities() {
        return activities;
    }

    public void setLevels(RichTable levels) {
        this.levels = levels;
    }

    public RichTable getLevels() {
        return levels;
    }

    public void activitiesSelected(SelectionEvent evt) {
        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = evt.getAddedSet();

            Object key2 = rowKeySet.iterator().next();
            activities.setRowKey(key2);

            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)activities.getRowData();
            session.setAttribute("activity",
                                 nodeBinding.getAttribute("activityName"));
            ADFUtils.findIterator("findEscalationLevelsOneIterator").executeQuery();
            ADFUtils.findIterator("findEscalationLevelsTwoIterator").executeQuery();
            ADFUtils.findIterator("findEscalationLevelsThreeIterator").executeQuery();
            ADFUtils.findIterator("findEscalationLevelsFourIterator").executeQuery();
            GlobalCC.refreshUI(dtls);
            //ADFUtils.findIterator("findEscalationLevelsIterator").executeQuery();
            //GlobalCC.refreshUI(levels);
        }
    }

    public void setEscCode(RichInputText escCode) {
        this.escCode = escCode;
    }

    public RichInputText getEscCode() {
        return escCode;
    }

    public void setEscLevel(RichSelectOneChoice escLevel) {
        this.escLevel = escLevel;
    }

    public RichSelectOneChoice getEscLevel() {
        return escLevel;
    }

    public void setEscAssignee(RichInputText escAssignee) {
        this.escAssignee = escAssignee;
    }

    public RichInputText getEscAssignee() {
        return escAssignee;
    }

    public void setUsers(RichTable users) {
        this.users = users;
    }

    public RichTable getUsers() {
        return users;
    }

    public String newEsc() {
        GlobalCC.showPopup("crm:escalations");
        escCode.setValue(null);
        escLevel.setValue(null);
        escAssignee.setValue(null);
        escAssignee.setLabel(null);
        escDuration.setValue(null);
        return null;
    }

    public String editEsc() {
        Object key = levels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        GlobalCC.showPopup("crm:escalations");
        escCode.setValue(nodeBinding.getAttribute("code"));
        escLevel.setValue(nodeBinding.getAttribute("level"));
        escAssignee.setValue(nodeBinding.getAttribute("username"));
        escAssignee.setLabel(nodeBinding.getAttribute("userCode").toString());
        escDuration.setValue(nodeBinding.getAttribute("duration"));
        GlobalCC.refreshUI(escLevel);
        return null;
    }
    public String cancelServReqPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:escPop" +
                             "').hide(hints);");
        return null;
    }
    public String deleteEsc() {
        Object key = levels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        DBConnector dbConnect = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbConnect.getDatabaseConnection();
            String queryString =
                "begin TQC_SETUPS_PKG.escalations_proc(?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(queryString);
            cst.setObject(1, "D");
            cst.setObject(2, nodeBinding.getAttribute("code"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.setObject(7, null);
            cst.setObject(8, null);
            cst.execute();
            cst.close();
            conn.close();
            ADFUtils.findIterator("findEscalationLevelsIterator").executeQuery();
            GlobalCC.refreshUI(levels);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String launchUser() {
        session.setAttribute("type", "one");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchUserTwo() {
        session.setAttribute("type", "two");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchUserThree() {
        session.setAttribute("type", "three");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchUserFour() {
        session.setAttribute("type", "four");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchCCUser() {
        session.setAttribute("type", "ccOne");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchCCUserTwo() {
        session.setAttribute("type", "ccTwo");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchCCUserThree() {
        session.setAttribute("type", "ccThree");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String launchCCUserFour() {
        session.setAttribute("type", "ccFour");
        GlobalCC.showPopup("crm:users");
        return null;
    }

    public String saveEsc() {
        String codeVal;
        String level;
        String assignee;
        String duration;
        codeVal = GlobalCC.checkNullValues(escCode.getValue());
        level = GlobalCC.checkNullValues(escLevel.getValue());
        assignee = GlobalCC.checkNullValues(escAssignee.getValue());
        duration = GlobalCC.checkNullValues(escDuration.getValue());
        if (level == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: level");
            return null;
        }
        if (duration == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Duration");
            return null;
        }

        if (assignee == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Assignee");
            return null;
        }
        assignee = escAssignee.getLabel();
        DBConnector dbConnect = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbConnect.getDatabaseConnection();
            String queryString =
                "begin TQC_SETUPS_PKG.escalations_proc(?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(queryString);
            if (codeVal == null) {
                cst.setObject(1, "A");
            } else {
                cst.setObject(1, "E");
            }
            cst.setObject(2, codeVal);
            cst.setObject(3, session.getAttribute("sysCode"));
            cst.setObject(4, session.getAttribute("jpdlName"));
            cst.setObject(5, session.getAttribute("activity"));
            cst.setObject(6, level);
            cst.setObject(7, assignee);
            cst.setObject(8, duration);
            cst.execute();
            cst.close();
            conn.close();
            GlobalCC.hidePopup("crm:escalations");
            ADFUtils.findIterator("findEscalationLevelsIterator").executeQuery();
            GlobalCC.refreshUI(levels);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String selectUser() {
        Object key = users.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        String type = (String)session.getAttribute("type");
        if (type.equalsIgnoreCase("one")) {
            escAssignee.setValue(nodeBinding.getAttribute("username"));
            escAssignee.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(escAssignee);
        } else if (type.equalsIgnoreCase("ccOne")) {
            ccAssignee.setValue(nodeBinding.getAttribute("username"));
            ccAssignee.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(ccAssignee);
        } else if (type.equalsIgnoreCase("two")) {
            escAssignee1.setValue(nodeBinding.getAttribute("username"));
            escAssignee1.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(escAssignee1);
        } else if (type.equalsIgnoreCase("ccTwo")) {
            ccAssignee1.setValue(nodeBinding.getAttribute("username"));
            ccAssignee1.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(ccAssignee1);
        } else if (type.equalsIgnoreCase("three")) {
            escAssignee2.setValue(nodeBinding.getAttribute("username"));
            escAssignee2.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(escAssignee2);
        } else if (type.equalsIgnoreCase("ccThree")) {
            ccAssignee2.setValue(nodeBinding.getAttribute("username"));
            ccAssignee2.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(ccAssignee2);
        } else if (type.equalsIgnoreCase("four")) {
            escAssignee3.setValue(nodeBinding.getAttribute("username"));
            escAssignee3.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(escAssignee3);
        } else if (type.equalsIgnoreCase("ccFour")) {
            ccAssignee3.setValue(nodeBinding.getAttribute("username"));
            ccAssignee3.setLabel(nodeBinding.getAttribute("userCode").toString());
            GlobalCC.refreshUI(ccAssignee3);
        }
        GlobalCC.hidePopup("crm:users");
        return null;
    }

    public void setEscDuration(RichInputNumberSpinbox escDuration) {
        this.escDuration = escDuration;
    }

    public RichInputNumberSpinbox getEscDuration() {
        return escDuration;
    }

    public void setLvlCheck(RichSelectBooleanCheckbox lvlCheck) {
        this.lvlCheck = lvlCheck;
    }

    public RichSelectBooleanCheckbox getLvlCheck() {
        return lvlCheck;
    }

    public void setEscCode1(RichInputText escCode1) {
        this.escCode1 = escCode1;
    }

    public RichInputText getEscCode1() {
        return escCode1;
    }

    public void setEscLevel1(RichSelectOneChoice escLevel1) {
        this.escLevel1 = escLevel1;
    }

    public RichSelectOneChoice getEscLevel1() {
        return escLevel1;
    }

    public void setEscAssignee1(RichInputText escAssignee1) {
        this.escAssignee1 = escAssignee1;
    }

    public RichInputText getEscAssignee1() {
        return escAssignee1;
    }

    public void setEscDuration1(RichInputNumberSpinbox escDuration1) {
        this.escDuration1 = escDuration1;
    }

    public RichInputNumberSpinbox getEscDuration1() {
        return escDuration1;
    }

    public void setLvlCheck1(RichSelectBooleanCheckbox lvlCheck1) {
        this.lvlCheck1 = lvlCheck1;
    }

    public RichSelectBooleanCheckbox getLvlCheck1() {
        return lvlCheck1;
    }

    public void setEscCode2(RichInputText escCode2) {
        this.escCode2 = escCode2;
    }

    public RichInputText getEscCode2() {
        return escCode2;
    }

    public void setEscLevel2(RichSelectOneChoice escLevel2) {
        this.escLevel2 = escLevel2;
    }

    public RichSelectOneChoice getEscLevel2() {
        return escLevel2;
    }

    public void setEscAssignee2(RichInputText escAssignee2) {
        this.escAssignee2 = escAssignee2;
    }

    public RichInputText getEscAssignee2() {
        return escAssignee2;
    }

    public void setEscDuration2(RichInputNumberSpinbox escDuration2) {
        this.escDuration2 = escDuration2;
    }

    public RichInputNumberSpinbox getEscDuration2() {
        return escDuration2;
    }

    public void setLvlCheck2(RichSelectBooleanCheckbox lvlCheck2) {
        this.lvlCheck2 = lvlCheck2;
    }

    public RichSelectBooleanCheckbox getLvlCheck2() {
        return lvlCheck2;
    }

    public void setEscCode3(RichInputText escCode3) {
        this.escCode3 = escCode3;
    }

    public RichInputText getEscCode3() {
        return escCode3;
    }

    public void setEscLevel3(RichSelectOneChoice escLevel3) {
        this.escLevel3 = escLevel3;
    }

    public RichSelectOneChoice getEscLevel3() {
        return escLevel3;
    }

    public void setEscAssignee3(RichInputText escAssignee3) {
        this.escAssignee3 = escAssignee3;
    }

    public RichInputText getEscAssignee3() {
        return escAssignee3;
    }

    public void setEscDuration3(RichInputNumberSpinbox escDuration3) {
        this.escDuration3 = escDuration3;
    }

    public RichInputNumberSpinbox getEscDuration3() {
        return escDuration3;
    }

    public void setLvlCheck3(RichSelectBooleanCheckbox lvlCheck3) {
        this.lvlCheck3 = lvlCheck3;
    }

    public RichSelectBooleanCheckbox getLvlCheck3() {
        return lvlCheck3;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setDtls(RichPanelGroupLayout dtls) {
        this.dtls = dtls;
    }

    public RichPanelGroupLayout getDtls() {
        return dtls;
    }

    public String saveEscalations() {
        String codeVal = null;
        String level = null;
        String assignee = null;
        String cc = null;
        String assigneeVal = null;
        String ccVal = null;
        String duration = null;

        boolean selected = false;
        boolean manipulate = false;
        int k = 0;
        while (k < 4) {
            switch (k) {
            case 0:
                selected = lvlCheck.isSelected();
                codeVal = GlobalCC.checkNullValues(escCode.getValue());
                level = "1";
                assignee = GlobalCC.checkNullValues(escAssignee.getValue());
                cc = GlobalCC.checkNullValues(ccAssignee.getValue());
                assigneeVal = escAssignee.getLabel();
                ccVal = ccAssignee.getLabel();
                duration = GlobalCC.checkNullValues(escDuration.getValue());
                break;
            case 1:
                selected = lvlCheck1.isSelected();
                codeVal = GlobalCC.checkNullValues(escCode1.getValue());
                level = "2";
                assignee = GlobalCC.checkNullValues(escAssignee1.getValue());
                cc = GlobalCC.checkNullValues(ccAssignee1.getValue());
                assigneeVal = escAssignee1.getLabel();
                ccVal = ccAssignee1.getLabel();
                duration = GlobalCC.checkNullValues(escDuration1.getValue());
                break;
            case 2:
                selected = lvlCheck2.isSelected();
                codeVal = GlobalCC.checkNullValues(escCode2.getValue());
                level = "3";
                assignee = GlobalCC.checkNullValues(escAssignee2.getValue());
                cc = GlobalCC.checkNullValues(ccAssignee2.getValue());
                assigneeVal = escAssignee2.getLabel();
                ccVal = ccAssignee2.getLabel();
                duration = GlobalCC.checkNullValues(escDuration2.getValue());
                break;
            case 3:
                selected = lvlCheck3.isSelected();
                codeVal = GlobalCC.checkNullValues(escCode3.getValue());
                level = "4";
                assignee = GlobalCC.checkNullValues(escAssignee3.getValue());
                cc = GlobalCC.checkNullValues(ccAssignee3.getValue());
                assigneeVal = escAssignee3.getLabel();
                ccVal = ccAssignee3.getLabel();
                duration = GlobalCC.checkNullValues(escDuration3.getValue());
                break;
            }
            System.out.println("CODE");
            System.out.println(codeVal);
            System.out.println(level);
            if (selected) {
                if (level == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: level");
                    return null;
                }
                if (duration == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Duration");
                    return null;
                }

                if (assignee == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Assignee");
                    return null;
                }
                if (cc == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: CC");
                    return null;
                }
                //assignee = escAssignee.getLabel();
                // cc = ccAssignee.getLabel();
                manipulate = true;

            } else if (!selected && codeVal == null) {
                selected = false;
                codeVal = null;
                assignee = null;
                duration = null;
                manipulate = false;
            } else {
                manipulate = true;
            }

            if (manipulate) {
                DBConnector dbConnect = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement cst = null;
                try {
                    conn = dbConnect.getDatabaseConnection();
                    String queryString =
                        "begin TQC_SETUPS_PKG.escalations_proc(?,?,?,?,?,?,?,?,?); end;";
                    cst =
(OracleCallableStatement)conn.prepareCall(queryString);
                    if (codeVal == null) {
                        cst.setObject(1, "A");
                    } else if (selected) {
                        cst.setObject(1, "E");
                    } else {
                        cst.setObject(1, "D");
                        assignee = null;
                    }
                    cst.setObject(2, codeVal);
                    cst.setObject(3, session.getAttribute("sysCode"));
                    cst.setObject(4, session.getAttribute("jpdlName"));
                    cst.setObject(5, session.getAttribute("activity"));
                    cst.setObject(6, level);
                    cst.setObject(7, assigneeVal);
                    cst.setObject(8, duration);
                    cst.setObject(9, ccVal);
                    cst.execute();
                    cst.close();
                    conn.close();
                    // GlobalCC.hidePopup("crm:escalations");

                    // ADFUtils.findIterator("findEscalationLevelsIterator").executeQuery();
                    // GlobalCC.refreshUI(levels);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
            manipulate = false;
            selected = false;
            codeVal = null;
            assignee = null;
            cc = null;
            assigneeVal = null;
            ccVal = null;
            duration = null;
            k++;
        }


        GlobalCC.sysInformation("Save Successfull");
        ADFUtils.findIterator("findEscalationLevelsOneIterator").executeQuery();
        ADFUtils.findIterator("findEscalationLevelsTwoIterator").executeQuery();
        ADFUtils.findIterator("findEscalationLevelsThreeIterator").executeQuery();
        ADFUtils.findIterator("findEscalationLevelsFourIterator").executeQuery();
        GlobalCC.refreshUI(dtls);
        return null;
    }

    public void setCcAssignee(RichInputText ccAssignee) {
        this.ccAssignee = ccAssignee;
    }

    public RichInputText getCcAssignee() {
        return ccAssignee;
    }

    public void setCcAssignee1(RichInputText ccAssignee1) {
        this.ccAssignee1 = ccAssignee1;
    }

    public RichInputText getCcAssignee1() {
        return ccAssignee1;
    }

    public void setCcAssignee2(RichInputText ccAssignee2) {
        this.ccAssignee2 = ccAssignee2;
    }

    public RichInputText getCcAssignee2() {
        return ccAssignee2;
    }

    public void setCcAssignee3(RichInputText ccAssignee3) {
        this.ccAssignee3 = ccAssignee3;
    }

    public RichInputText getCcAssignee3() {
        return ccAssignee3;
    }
    public void clearEscFields() { 
        sreLvlOneDur.setValue(null);
        sreLvlOneAssignee.setValue(null);
        sreLvlTwoDurat.setValue(null);
        sreLvlTwoAssignee.setValue(null);
        sreSrDesc.setValue(null);
      
        btnSaveUpdateEsc.setText("Save");
    }
    public String actionNewEscalation() {
        clearEscFields();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:escPop" +
                             "').show(hints);");
        return null;
    }
    public String actionEditEscalation() {
        Object key2 = escalationTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            sreLvlOneDur.setValue(nodeBinding.getAttribute("sreLvlDuration"));
            sreLvlOneAssignee.setValue(nodeBinding.getAttribute("sreLvlOneAssigneeName"));
            sreLvlTwoDurat.setValue(nodeBinding.getAttribute("sreLvlTwoDuration"));
            sreLvlTwoAssignee.setValue(nodeBinding.getAttribute("sreLvlTwoAssigneeName"));
            sreSrDesc.setValue(nodeBinding.getAttribute("sreSrtName"));
            session.setAttribute("sreCode", nodeBinding.getAttribute("sreCode"));
            session.setAttribute("lvlOneAssignee", nodeBinding.getAttribute("sreLvlOneAssignee"));
            session.setAttribute("lvlTwoAssignee", nodeBinding.getAttribute("sreLvlTwoAssignee"));
            session.setAttribute("sreSrtCode", nodeBinding.getAttribute("sreSrtCode"));
            
              
               
        btnSaveUpdateEsc.setText("Edit");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:escPop" +
                             "').show(hints);");
        return null;
        }else{
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
       
    }
    
    public String actionShowDeleteEscalation() {
        Object key2 = escalationTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteEsc" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

    }
    public void actionConfirmDeleteEsc(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String actionDelEsc = actionDeleteEsc();
        }

        // Add event code here...
    }
    public String actionDeleteEsc() {
        Object key2 = escalationTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String sreCode = nodeBinding.getAttribute("sreCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin tqc_service_requests.servReqClntEscalationsProc(?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2, new BigDecimal(sreCode));
                statement.setString(3, null);
                statement.setString(4, null);
                statement.setString(5, null);
                statement.setString(6, null);
                statement.setString(7, null);
               
                statement.execute();
                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findServiceReqEscalationsIterator").executeQuery();
                GlobalCC.refreshUI(escalationTbl);

                    String message = " Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }
    public void setBtnSaveUpdateEsc(RichCommandButton btnSaveUpdateEsc) {
        this.btnSaveUpdateEsc = btnSaveUpdateEsc;
    }

    public RichCommandButton getBtnSaveUpdateEsc() {
        return btnSaveUpdateEsc;
    }

    public void setEscalationTbl(RichTable escalationTbl) {
        this.escalationTbl = escalationTbl;
    }

    public RichTable getEscalationTbl() {
        return escalationTbl;
    }

    public void setSreCode(BigDecimal sreCode) {
        this.sreCode = sreCode;
    }

    public BigDecimal getSreCode() {
        return sreCode;
    }

    public void setSreSrtCode(String sreSrtCode) {
        this.sreSrtCode = sreSrtCode;
    }

    public String getSreSrtCode() {
        return sreSrtCode;
    }

    public void setSreLvlDuration(RichInputText sreLvlDuration) {
        this.sreLvlDuration = sreLvlDuration;
    }

    public RichInputText getSreLvlDuration() {
        return sreLvlDuration;
    }

    public void setSreLvlOneAssignee(RichInputText sreLvlOneAssignee) {
        this.sreLvlOneAssignee = sreLvlOneAssignee;
    }

    public RichInputText getSreLvlOneAssignee() {
        return sreLvlOneAssignee;
    }

    public void setSreLvlTwoDuration(RichInputText sreLvlTwoDuration) {
        this.sreLvlTwoDuration = sreLvlTwoDuration;
    }

    public RichInputText getSreLvlTwoDuration() {
        return sreLvlTwoDuration;
    }

    public void setSreLvlTwoAssignee(RichInputText sreLvlTwoAssignee) {
        this.sreLvlTwoAssignee = sreLvlTwoAssignee;
    }

    public RichInputText getSreLvlTwoAssignee() {
        return sreLvlTwoAssignee;
    }

    public void setSreSrDesc(RichInputText sreSrDesc) {
        this.sreSrDesc = sreSrDesc;
    }

    public RichInputText getSreSrDesc() {
        return sreSrDesc;
    }
    public String actionShowServReqLvls() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categoryType" + "').show(hints);");
        return null;
    }
    public String actionShowLvlOneAssignee() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:usersLevelOne" + "').show(hints);");
        return null;
    }
    public String actionShowLvlTwoAssignee() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:usersLevelTwo" + "').show(hints);");
        return null;
    }
    public void actionNewEsc(ActionEvent actionEvent) {
        // Add event code here...
        clearEscFields();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:escPop" +
                             "').show(hints);");
        
    }

    public String saveUpdateEsc() {
        if (GlobalCC.checkNullValues(sreLvlOneDur.getValue())==null){
            GlobalCC.INFORMATIONREPORTING("Please Enter Level One Duration");
            return null;
        }
        if (GlobalCC.checkNullValues(sreLvlTwoDurat.getValue())==null){
            GlobalCC.INFORMATIONREPORTING("Please Enter Level Two Duration");
            return null;
        }
        if (GlobalCC.checkNullValues(session.getAttribute("lvlOneAssignee"))==null){
            GlobalCC.INFORMATIONREPORTING("Please Select Level One Assignee");
            return null;
        }
        if (GlobalCC.checkNullValues(session.getAttribute("lvlOneAssignee"))==null){
            GlobalCC.INFORMATIONREPORTING("Please Select Level Two Assignee");
            return null;
        }
        if (btnSaveUpdateEsc.getText().equals("Edit")) {
            // Add event code here...
            Object key2 = escalationTbl.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            if (nodeBinding != null) {

            BigDecimal sreCode = (BigDecimal)nodeBinding.getAttribute("sreCode");

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
                BigDecimal levelOneDuration = new BigDecimal(sreLvlOneDur.getValue().toString());
                BigDecimal levelOneAssigee =new BigDecimal(session.getAttribute("lvlOneAssignee").toString());
                BigDecimal levelTwoDuration=new BigDecimal(sreLvlTwoDurat.getValue().toString());
                BigDecimal levelTwoAssignee =new BigDecimal(session.getAttribute("lvlTwoAssignee").toString());
                BigDecimal sreSrtCode = new BigDecimal (session.getAttribute("sreSrtCode").toString());
                    
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin tqc_service_requests.servReqClntEscalationsProc(?,?,?,?,?,?,?); end;";

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setBigDecimal(2, (sreCode));
                statement.setBigDecimal(3, sreSrtCode);
                statement.setBigDecimal(4, levelOneDuration);
                statement.setBigDecimal(5, levelOneAssigee);
                statement.setBigDecimal(6, levelTwoDuration);
                statement.setBigDecimal(7, levelTwoAssignee);
               
                statement.execute();
                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findServiceReqEscalationsIterator").executeQuery();
                GlobalCC.refreshUI(escalationTbl);

                    String message = " Record SAVED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" + "crm:clntCommentsPop" +
                                     "').hide(hints);");
                
                
                GlobalCC.hidePopup("crm:escPop");
                return null;

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            } else {
                GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
                return null;
            }
        } else {
       
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        BigDecimal levelOneDuration = new BigDecimal(sreLvlOneDur.getValue().toString());
        BigDecimal levelOneAssigee =new BigDecimal(session.getAttribute("lvlOneAssignee").toString());
        BigDecimal levelTwoDuration=new BigDecimal(sreLvlTwoDurat.getValue().toString());
        BigDecimal levelTwoAssignee =new BigDecimal(session.getAttribute("lvlTwoAssignee").toString());
        BigDecimal sreSrtCode = new BigDecimal (session.getAttribute("sreSrtCode").toString());
            
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin tqc_service_requests.servReqClntEscalationsProc(?,?,?,?,?,?,?); end;";

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "A");
            //statement.setBigDecimal(2, (null));
            statement.registerOutParameter(2, OracleTypes.NUMBER);
            statement.setBigDecimal(3, sreSrtCode);
            statement.setBigDecimal(4, levelOneDuration);
            statement.setBigDecimal(5, levelOneAssigee);
            statement.setBigDecimal(6, levelTwoDuration);
            statement.setBigDecimal(7, levelTwoAssignee);
           
            statement.execute();
            BigDecimal result = statement.getBigDecimal(2);
            statement.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("findServiceReqEscalationsIterator").executeQuery();
            GlobalCC.refreshUI(escalationTbl);
            
            if(result==null|| !result.equals("-1")){
                String message = " Record SAVED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
                
                // close the popup
                GlobalCC.hidePopup("crm:escPop");
                }
            else
            {
                if(result.toString().equals("-1")){
                    String message = "ERROR Saving: Record Catergory already exists!";
                    GlobalCC.EXCEPTIONREPORTING(message);
                    }
           
                }
            
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
        return null;
    }
    public String selectCategoryType() {
        Object key = servReqCatTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        sreSrDesc.setValue(r.getAttribute("srtDesc"));
        GlobalCC.refreshUI(sreSrDesc);
        session.setAttribute("sreSrtCode", r.getAttribute("srtCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:categoryType" + "').hide(hints);");
        return null;
    }
    public String cancelCategory() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:categoryType" +
                             "').hide(hints);");
        return null;
    }
    public void setServReqCatTbl(RichTable servReqCatTbl) {
        this.servReqCatTbl = servReqCatTbl;
    }

    public RichTable getServReqCatTbl() {
        return servReqCatTbl;
    }
    public String launchAssignee() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:users" +
                             "').show(hints);");
        return null;
    }
    public String userLevelOneSelected() {
        Object key = usersLvlOneTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        sreLvlOneAssignee.setValue(r.getAttribute("username"));
        session.setAttribute("lvlOneAssignee",r.getAttribute("userCode").toString());
        GlobalCC.refreshUI(sreLvlOneAssignee);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:usersLevelOne" +
                             "').hide(hints);");
        return null;
    }
    public String userLevelTwoSelected() {
        Object key = usersLvlTwoTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        sreLvlTwoAssignee.setValue(r.getAttribute("username"));
        session.setAttribute("lvlTwoAssignee",r.getAttribute("userCode").toString());
        GlobalCC.refreshUI(sreLvlTwoAssignee);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:usersLevelTwo" +
                             "').hide(hints);");
        return null;
    }
    
    public String cancelUserLevelOne() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:usersLevelOne" +
                             "').hide(hints);");
        return null;
    }
    public String cancelUserLevelTwo() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:usersLevelTwo" +
                             "').hide(hints);");
        return null;
    }

    public void setUsersLvlOneTbl(RichTable usersLvlOneTbl) {
        this.usersLvlOneTbl = usersLvlOneTbl;
    }

    public RichTable getUsersLvlOneTbl() {
        return usersLvlOneTbl;
    }

    public void setUsersLvlTwoTbl(RichTable usersLvlTwoTbl) {
        this.usersLvlTwoTbl = usersLvlTwoTbl;
    }

    public RichTable getUsersLvlTwoTbl() {
        return usersLvlTwoTbl;
    }

    public void setSreLvlOneDur(RichInputNumberSpinbox sreLvlOneDur) {
        this.sreLvlOneDur = sreLvlOneDur;
    }

    public RichInputNumberSpinbox getSreLvlOneDur() {
        return sreLvlOneDur;
    }

    public void setSreLvlTwoDurat(RichInputNumberSpinbox sreLvlTwoDurat) {
        this.sreLvlTwoDurat = sreLvlTwoDurat;
    }

    public RichInputNumberSpinbox getSreLvlTwoDurat() {
        return sreLvlTwoDurat;
    }
}
