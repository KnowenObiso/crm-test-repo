package TurnQuest.view.roles;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.SysProcessSubAreaLevel;
import TurnQuest.view.models.SysProcessSubAreaLimit;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.bean.DCDataRow;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.RowDisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class RoleManipulation {
    private RichTable sysRoles;
    private RichTreeTable roleAreas;
    private RichSelectManyShuttle userRoles;
    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();
    private RichTree roleAreaSubAreas;
    private RichSelectBooleanCheckbox roleAreaCheck;
    private RichSelectBooleanCheckbox roleSubAreaCheck;
    private RichTable sysUserSystems;
    private RichInputText userSystem;
    private RichSelectManyShuttle userSystemRoles;
    private UISelectItems userSystemRoleSelect;
    boolean processSelected = false;
    boolean subAreaSelected = false;
    boolean areaSelected = false;
    private HtmlInputText debitLimit;
    private HtmlInputText creditLimit;
    String debitLimitValue;
    String creditLimitValue;
    private RichTable systems;
    private RichInputText sysVal;
    private RichInputText sysRoleName;
    private RichInputText sysRoleShtDesc;
    private RichSelectOneChoice sysProcessVal;
    private RichCommandButton editBtn;
    private RichCommandButton saveBtn;
    private RichTree processAreas;
    private RichSelectBooleanCheckbox processCheck;
    private RichTable systemsLOV;
    private RichInputText systemDesc;
    private RichTable systemUsersLOV;
    private RichTree treeSystemRoles;
    private RichInputText txtSysRoleCode;
    private RichInputText txtSysCode;
    private RichInputText txtSysRoleName;
    private RichInputDate txtSysRoleDateCreated;
    private RichInputText txtSysRoleShortDesc;
    private RichSelectOneChoice txtSysRoleStatus;
    private RichPanelBox panelMainSysRoleDetails;
    private RichCommandButton btnCreateUpdateSystemRole;
    private RichShowDetailItem tabRoleDetails;
    private RichPanelBox panelMain;
    private RichShowDetailItem tabRoleAreas;
    private RichShowDetailItem roleAreaLevels;
    private RichTable subAreaLevels;
    private RichTable subAreaTab;
    private RichInputNumberSpinbox lvlId;
    private RichInputNumberSpinbox lvlNo;
    private RichInputText lvlDesc;
    private RichTable sysAuthRoles;
    private RichPanelBox rolesPanel;
    private RichTable tblSubAreaLimits;
    private RichTable tblSubAreaLevels;
    private RichInputText txtSpsatCode;
    private RichInputText txtSpsatSprsaCode;
    private RichInputNumberSpinbox txtSpsatNoOfLevel;
    private RichInputNumberSpinbox txtSpsatMinLimit;
    private RichInputNumberSpinbox txtSpsatMaxLimit;
    private RichCommandButton btnSaveUpdateSubAreaLimit;
    private RichInputText txtSpsalCode;
    private RichInputText txtSpsalSprsaCode;
    private RichInputText txtSpsalSpsatCode;
    private RichInputNumberSpinbox txtSpsalLevel;
    private RichSelectOneChoice txtSpsalApproverType;
    private RichInputText txtSpsalApproverId;
    private RichCommandButton btnSaveUpdateSubAreaLevel;
    private RichTable tblUserGroups;
    private RichPanelBox panelSubAreaLevel;
    private RichInputText txtUsrGrpApproverName;
    private RichTable tblUsersPop;
    private RichInputText txtUsrApproverName;
    private RichTable tblSystemPostsPop;
    private RichInputText txtSysPostApproverName;
    private RichPanelLabelAndMessage plamUser;
    private RichPanelLabelAndMessage plamUserGroup;
    private RichPanelLabelAndMessage plamSysPost;
    private RichPanelTabbed mainPanel;
    private RichPanelGroupLayout mainLay;
    private RichTable processSubArea;
    private RichSelectBooleanCheckbox rowChecked;
    private RichTable processAreasTbl;
    private RichCommandButton btnAuthorizeSystemRole;
    private RichInputText txtSysRoleAuthorized;
    private RichSelectBooleanCheckbox rowSelect;
    private RichPopup rolesDetailsPopUp;
    private RichSelectOneChoice populatedSystems;
    private RichTree rolesTree;
    public RoleManipulation() {
        //roleAreaSubAreas.setInitiallyExpanded();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void rolesSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSysRolesIterator");
            RowKeySet set = selectionEvent.getAddedSet();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("roleCode", r.getAttribute("roleCode"));
                System.out.println("The current role code is : " +
                                   session.getAttribute("roleCode"));

            }


            ADFUtils.findIterator("findSystemProcessIterator").executeQuery();
            GlobalCC.refreshUI(processAreas);

        }

    }

    public String findSystemSelected() {
        try {

            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSystemsIterator");
            RowKeySet set = systemsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();

                systemDesc.setValue(r.getAttribute("sysName"));
                session.setAttribute("sysCode", r.getAttribute("sysCode"));

                System.out.println(session.getAttribute("sysName"));

            }

            ADFUtils.findIterator("findAllSystemUsersIterator").executeQuery();
            GlobalCC.refreshUI(systemUsersLOV);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setSysRoles(RichTable sysRoles) {
        this.sysRoles = sysRoles;
    }

    public RichTable getSysRoles() {
        return sysRoles;
    }


    /* public void roleSelectedVal(ValueChangeEvent valueChangeEvent) {

        if (roleAreaCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findRoleAreasIterator");
            RowKeySet set = roleAreas.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                if (r.getAttribute("areaSubArea").equals("A")) {
                    System.out.println(r.getAttribute("processAreaCode"));
                } else {
                    System.out.println(r.getAttribute("processSubAreaCode"));
                }

                //System.out.println(r.getAttribute("processSubAreaCode"));

                // session.setAttribute("processCode",r.getAttribute("processCode"));
                // session.setAttribute("processRoleCode",r.getAttribute("roleProcessCode"));

            }
            ADFUtils.findIterator("findRoleAreasIterator").executeQuery();
            GlobalCC.refreshUI(roleAreas);
        }
        //userRoles.getValue().toString()
        // Add event code here...
    }*/

    public void setRoleAreas(RichTreeTable roleAreas) {
        this.roleAreas = roleAreas;
    }

    public RichTreeTable getRoleAreas() {
        return roleAreas;
    }


    public void setUserRoles(RichSelectManyShuttle userRoles) {
        this.userRoles = userRoles;
    }

    public RichSelectManyShuttle getUserRoles() {
        /*        Integer[] gata = (Integer[])userRoles.getValue();
        int length = gata.length;
        Integer mine = new  Integer(2);
        gata[length] = mine;
    userRoles.setValue(gata); */

        return userRoles;
    }

    public void userRolesSelected(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findSysRolesIterator");
            int k = 0;
            Row[] myrows = dciter.getAllRowsInRange();
            while (k < myrows.length) {
                Row r = myrows[k];
                System.out.println(r);
                System.out.println(r.getAttribute("roleProcessCode"));


                //System.out.println(r.getAttribute("processSubAreaCode"));

                // session.setAttribute("processCode",r.getAttribute("processCode"));
                // session.setAttribute("processRoleCode",r.getAttribute("roleProcessCode"));
                k++;
            }
            //ArrayList rightItems = (ArrayList)userRoles.getValue();
            Integer[] gata = (Integer[])userRoles.getValue();
            int len = 0;
            while (len < gata.length) {
                System.out.println(gata[len]);
                len++;

            }
        }
        // Add event code here...
    }


    public void setRoleAreaSubAreas(RichTree roleAreaSubAreas) {
        this.roleAreaSubAreas = roleAreaSubAreas;
    }

    public RichTree getRoleAreaSubAreas() {
        return roleAreaSubAreas;
    }


    public void setRoleAreaCheck(RichSelectBooleanCheckbox roleAreaCheck) {
        this.roleAreaCheck = roleAreaCheck;
    }

    public RichSelectBooleanCheckbox getRoleAreaCheck() {
        return roleAreaCheck;
    }

    public void setRoleSubAreaCheck(RichSelectBooleanCheckbox roleSubAreaCheck) {
        this.roleSubAreaCheck = roleSubAreaCheck;
    }

    public RichSelectBooleanCheckbox getRoleSubAreaCheck() {
        return roleSubAreaCheck;
    }

    public void setSysUserSystems(RichTable sysUserSystems) {
        this.sysUserSystems = sysUserSystems;
    }

    public RichTable getSysUserSystems() {
        return sysUserSystems;
    }

    public void setUserSystem(RichInputText userSystem) {
        this.userSystem = userSystem;
    }

    public RichInputText getUserSystem() {
        return userSystem;
    }

    public void roleAreaValue(ValueChangeEvent valueChangeEvent) {
        if (roleAreaCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            areaSelected = true;
        }

        if (!roleAreaCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            areaSelected = false;
        }
    }

    public void roleSubAreaValue(ValueChangeEvent valueChangeEvent) {
        if (roleSubAreaCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            subAreaSelected = true;

        }
        if (!roleSubAreaCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            subAreaSelected = false;

        }
    }

    public String sysUserSystemSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserSystemsIterator");
        RowKeySet set = sysUserSystems.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            userSystem.setValue(r.getAttribute("sysName"));
            session.setAttribute("sysCode", r.getAttribute("sysCode"));

        }
        GlobalCC.refreshUI(userSystem);
        UserSystemsRoleValues();
        GlobalCC.refreshUI(userSystemRoles);
        return null;
    }

    public void sysUsersSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findAllSystemUsersIterator");
            RowKeySet set = selectionEvent.getAddedSet();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("sysUserCode",
                                     r.getAttribute("userCode"));
                System.out.println(session.getAttribute("sysUserCode"));
            }
            GlobalCC.refreshUI(userSystem);
            UserSystemsRoleValues();
            GlobalCC.refreshUI(userSystemRoles);

        }
    }

    public String UserSystemsRoleValues() {
        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int t = 0;
            while (t < selectValues.size()) {
                selectValues.remove(t);
                t++;
            }
            while (rs.next()) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(rs.getBigDecimal(1).toString());
                selectItem.setDescription(rs.getString(2));
                selectItem.setLabel(rs.getString(2));
                selectValues.add(selectItem);
            }

            rs.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String userQuery =
            "begin ? := tqc_roles_cursor.Get_User_Roles(?,?); end;";
        OracleCallableStatement cstUsr = null;
        try {

            cstUsr = (OracleCallableStatement)conn.prepareCall(userQuery);
            cstUsr.registerOutParameter(1,
                                        OracleTypes.CURSOR); //authorization code
            cstUsr.setBigDecimal(2,
                                 (BigDecimal)session.getAttribute("sysCode"));
            cstUsr.setBigDecimal(3,
                                 (BigDecimal)session.getAttribute("sysUserCode"));

            cstUsr.execute();
            OracleResultSet rsUser = (OracleResultSet)cstUsr.getObject(1);
            int t = 0;
            while (t < displayValue.size()) {
                displayValue.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue.add(rsUser.getBigDecimal(7).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        if (userSystemRoles == null) {
        } else {

            userSystemRoles.setValue(displayValue);
            userSystemRoleSelect.setValue(selectValues);
        }
        return null;
    }

    public void setSelectValues(List<SelectItem> selectValues) {
        this.selectValues = selectValues;
    }

    public List<SelectItem> getSelectValues() {
        return selectValues;
    }

    public void setDisplayValue(List<String> displayValue) {
        this.displayValue = displayValue;
    }

    public List<String> getDisplayValue() {
        return displayValue;
    }

    public void setUserSystemRoles(RichSelectManyShuttle userSystemRoles) {
        this.userSystemRoles = userSystemRoles;
    }

    public RichSelectManyShuttle getUserSystemRoles() {
        return userSystemRoles;
    }

    public void setUserSystemRoleSelect(UISelectItems userSystemRoleSelect) {
        this.userSystemRoleSelect = userSystemRoleSelect;
    }

    public UISelectItems getUserSystemRoleSelect() {
        return userSystemRoleSelect;
    }

    public void setDebitLimit(HtmlInputText debitLimit) {
        this.debitLimit = debitLimit;
    }

    public HtmlInputText getDebitLimit() {
        return debitLimit;
    }

    public void setCreditLimit(HtmlInputText creditLimit) {
        this.creditLimit = creditLimit;
    }

    public HtmlInputText getCreditLimit() {
        return creditLimit;
    }

    public void userRolesGrant(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            try {
                conn = datahandler.getDatabaseConnection();
                ArrayList<String> myVals =
                    (ArrayList<String>)userSystemRoles.getValue();
                ArrayList<SelectItem> myVals2 =
                    (ArrayList<SelectItem>)userSystemRoleSelect.getValue();
                int v = 0;
                String revokeQuery =
                    "begin tqc_roles_cursor.Revoke_User_Role(?,?,?); end;";

                while (v < myVals2.size()) {
                    SelectItem select = myVals2.get(v);
                    OracleCallableStatement cst = null;

                    cst =
(OracleCallableStatement)conn.prepareCall(revokeQuery);
                    cst.setBigDecimal(1,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setBigDecimal(2,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setString(3, (String)select.getValue());
                    cst.execute();
                    conn.commit();
                    v++;
                }
                String query =
                    "begin tqc_roles_cursor.Grant_User_Role(?,?,?); end;";
                int k = 0;
                while (k < myVals.size()) {
                    OracleCallableStatement cst = null;


                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setBigDecimal(2,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setString(3, (String)myVals.get(k));
                    cst.execute();
                    conn.commit();

                    System.out.println();

                    k++;
                }
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }

    public void processSubAreaValue(FacesContext facesContext,
                                    UIComponent uIComponent, Object object) {
        if (roleSubAreaCheck.isSelected()) {
            System.out.println("JOSE");
        }
    }

    public String grantRoleProcessArea(BigDecimal processArea,
                                       BigDecimal processRoleCode) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Grant_Role_Process_Area(?,?); end;";
            System.out.println("Granting Role Process Area (" + processArea +
                               ", " +
                               (BigDecimal)session.getAttribute("roleCode") +
                               " ) ");

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processArea);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("processRoleCode"));
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String revokeRoleProcessArea(BigDecimal processArea,
                                        BigDecimal processRoleCode) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Revoke_Role_Process_Area(?,?); end;";

            System.out.println("Revoking Role Process Area (" + processArea +
                               ", " +
                               (BigDecimal)session.getAttribute("roleCode") +
                               ") ");

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processArea);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("processRoleCode"));
            cst.execute();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String grantRoleProcessSubArea(BigDecimal processSubArea,
                                          BigDecimal processCode,
                                          String debitLimit,
                                          String creditLimit) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Grant_Role_Process_SubArea(?,?,?,?); end;";

            System.out.println("Granting Role Process Sub Area : (" +
                               processSubArea + "," +
                               (BigDecimal)session.getAttribute("roleCode") +
                               ")");

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processSubArea);
            //cst.setBigDecimal(2,processCode);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("roleCode"));
            cst.setString(3, debitLimit);
            cst.setString(4, creditLimit);
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String revokeRoleProcessSubArea(BigDecimal processSubArea,
                                           BigDecimal processCode) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Revoke_Role_Process_SubArea(?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processSubArea);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("roleCode"));
            cst.execute();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void debitValueList(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            debitLimitValue = valueChangeEvent.getNewValue().toString();

        }
    }

    public void creditValueList(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            creditLimitValue = valueChangeEvent.getNewValue().toString();
        }
    }

    public String systemSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findSystemsIterator");
        RowKeySet set = systems.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            sysVal.setValue(r.getAttribute("sysName"));
            session.setAttribute("sysCode", r.getAttribute("sysCode"));
        }
        ADFUtils.findIterator("findSystemProcessIterator").executeQuery();
        GlobalCC.refreshUI(processAreas);
        ADFUtils.findIterator("findSysRolesIterator").executeQuery();
        GlobalCC.refreshUI(sysRoles);

        return null;
    }

    public void setSystems(RichTable systems) {
        this.systems = systems;
    }

    public RichTable getSystems() {
        return systems;
    }


    public void setSysVal(RichInputText sysVal) {
        this.sysVal = sysVal;
    }

    public RichInputText getSysVal() {
        return sysVal;
    }

    public void setSysRoleName(RichInputText sysRoleName) {
        this.sysRoleName = sysRoleName;
    }

    public RichInputText getSysRoleName() {
        return sysRoleName;
    }

    public void setSysRoleShtDesc(RichInputText sysRoleShtDesc) {
        this.sysRoleShtDesc = sysRoleShtDesc;
    }

    public RichInputText getSysRoleShtDesc() {
        return sysRoleShtDesc;
    }

    public void setSysProcessVal(RichSelectOneChoice sysProcessVal) {
        this.sysProcessVal = sysProcessVal;
    }

    public RichSelectOneChoice getSysProcessVal() {
        return sysProcessVal;
    }

    public String createRole() {
        String roleNameVal = null;
        String roleShtDescVal = null;
        roleNameVal = GlobalCC.checkNullValues(sysRoleName.getValue());
        roleShtDescVal = GlobalCC.checkNullValues(sysRoleShtDesc.getValue());
        if (roleNameVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }
        if (roleShtDescVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }
        /*if (session.getAttribute("processCode") == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }*/
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Creat_Role(?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, (BigDecimal)session.getAttribute("sysCode"));
            cst.setString(2, roleNameVal);
            cst.setString(3, roleShtDescVal);
            /* cst.setBigDecimal(4,
                              (BigDecimal)session.getAttribute("processCode"));*/
            cst.execute();

            ADFUtils.findIterator("findSysRolesIterator").executeQuery();
            GlobalCC.refreshUI(sysRoles);

            conn.close();
            sysRoleName.setValue(null);
            sysRoleShtDesc.setValue(null);
            editBtn.setRendered(false);
            saveBtn.setRendered(true);
            sysProcessVal.setDisabled(false);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String DropRole() {

        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery = "begin tqc_roles_cursor.Drop_Role(?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("systemRoleCode"));
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String RestoreRole() {

        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery = "begin tqc_roles_cursor.Restore_Role(?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("processRoleCode"));
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String saveEditRole() {
        String roleNameVal = null;
        String roleShtDescVal = null;
        roleNameVal = GlobalCC.checkNullValues(sysRoleName.getValue());
        roleShtDescVal = GlobalCC.checkNullValues(sysRoleShtDesc.getValue());
        if (roleNameVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }
        if (roleShtDescVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }

        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Edit_Role(?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("processRoleCode"));
            cst.setString(2, roleNameVal);
            cst.setString(3, roleShtDescVal);
            cst.execute();

            conn.close();
            sysRoleName.setValue(null);
            sysRoleShtDesc.setValue(null);
            editBtn.setRendered(false);
            saveBtn.setRendered(true);
            sysProcessVal.setDisabled(false);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String editRole() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "template:pc1:p2" + "').show(hints);");
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findSysRolesIterator");
        RowKeySet set = sysRoles.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();

            sysRoleName.setValue(r.getAttribute("roleName"));
            sysRoleShtDesc.setValue(r.getAttribute("roleShtDesc"));

            editBtn.setRendered(true);
            saveBtn.setRendered(false);
            sysProcessVal.setDisabled(true);
        }

        return null;
    }

    public String cancelValues() {
        sysRoleName.setValue(null);
        sysRoleShtDesc.setValue(null);
        editBtn.setRendered(false);
        saveBtn.setRendered(true);
        sysProcessVal.setDisabled(false);
        return null;
    }

    public void setEditBtn(RichCommandButton editBtn) {
        this.editBtn = editBtn;
    }

    public RichCommandButton getEditBtn() {
        return editBtn;
    }

    public void setSaveBtn(RichCommandButton saveBtn) {
        this.saveBtn = saveBtn;
    }

    public RichCommandButton getSaveBtn() {
        return saveBtn;
    }

    public String grant2() {
        int k = 0;
        while (k < processAreas.getRowCount()) {
            Row r = (Row)processAreas.getRowData(k);

            if (r.getAttribute("areaSubArea").equals("P")) {
                if (processSelected == true) {
                    grantProcess((BigDecimal)r.getAttribute("processCode"));
                } else {
                    revokeProcess((BigDecimal)r.getAttribute("processCode"));
                }

            } else if (r.getAttribute("areaSubArea").equals("A")) {
                if (areaSelected == true) {
                    grantRoleProcessArea((BigDecimal)r.getAttribute("processAreaCode"),
                                         (BigDecimal)r.getAttribute("processCode"));
                } else {
                    revokeRoleProcessArea((BigDecimal)r.getAttribute("processAreaCode"),
                                          (BigDecimal)r.getAttribute("processCode"));
                }

            } else {
                if (subAreaSelected == true) {
                    System.out.println("The subarea was selected ..." +
                                       r.getAttribute("processSubAreaType").toString());
                    if (r.getAttribute("processSubAreaType").equals("A")) { //"A")) {

                        grantRoleProcessSubArea((BigDecimal)r.getAttribute("processSubAreaCode"),
                                                (BigDecimal)r.getAttribute("processRoleSubAreaCode"),
                                                debitLimitValue,
                                                creditLimitValue);

                    } else {
                        grantRoleProcessSubArea((BigDecimal)r.getAttribute("processSubAreaCode"),
                                                (BigDecimal)r.getAttribute("processRoleSubAreaCode"),
                                                null, null);
                    }

                } else {
                    if ((debitLimitValue != null) ||
                        (creditLimitValue != null)) {
                        grantRoleProcessSubArea((BigDecimal)r.getAttribute("processSubAreaCode"),
                                                (BigDecimal)r.getAttribute("processRoleSubAreaCode"),
                                                debitLimitValue,
                                                creditLimitValue);
                    } else {
                        revokeRoleProcessSubArea((BigDecimal)r.getAttribute("processSubAreaCode"),
                                                 (BigDecimal)r.getAttribute("processRoleSubAreaCode"));
                    }
                }
            }
            k++;
        }
        return null;
    }

    public String grantRevokeRoles() {
        System.out.println("Granting Roles Jose ...");
        // Get access to the Tree Collection Model. The tree component instance
        // is accessed through its binding property reference to this managed bean
        CollectionModel treeModel = (CollectionModel)processAreas.getValue();

        // The CollectionModel is of type FacesModel, which is an inner class.
        // To get to the ADF tree binding, we can call wrappedData on the
        // Collection Model
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)treeModel.getWrappedData();

        //get the selected tree nodes
        RowKeySet rks = processAreas.getSelectedRowKeys();
        //Store original rowKey
        Object oldRowKey = processAreas.getRowKey();

        System.out.println("Granting Roles ...");

        //If there is a tree node selected ...
        if (!rks.isEmpty() && rks != null) {

            // get first selected node as we assume this code to execute in a
            // single row selection use case. Iterate over the whole iterator if
            // you are in a multi node selection case
            List firstSet = (List)rks.iterator().next();
            System.out.println("This is the value of node" + firstSet);
            // get the ADF node binding. If you want to access sub nodes, make
            // sure that you set the primary key attribute for the entity
            // representing this node. If you don't do this, the next line
            // throws a NullPointerException
            JUCtrlHierNodeBinding node =
                treeBinding.findNodeByKeyPath(firstSet);
            System.out.println("This is the value of node" + node);
            if (node != null) {
                DCDataRow rw = (DCDataRow)node.getRow();

                // The data provider is the object - the entity - that is used
                // to render the node. Get the object
                Object entity = rw.getDataProvider();

                node.getKeyPath();
                Row r = node.getRow();


                if (r.getAttribute("areaSubArea").equals("P")) {

                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;
                    System.out.println("Length" + rows.length);
                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];
                        boolean checked =
                            (Boolean)rwr.getAttribute("processSelected");
                        if (checked) {

                            grantProcess((BigDecimal)rwr.getAttribute("processCode"));
                        } else {
                            revokeProcess((BigDecimal)rwr.getAttribute("processCode"));
                        }

                    }
                } else if (r.getAttribute("areaSubArea").equals("A")) {
                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;
                    System.out.println("Length" + rows.length);
                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];


                        boolean checked =
                            (Boolean)rwr.getAttribute("processAreaSelected");

                        if (checked) {
                            grantRoleProcessArea((BigDecimal)rwr.getAttribute("processAreaCode"),
                                                 (BigDecimal)rwr.getAttribute("processCode"));
                        } else {
                            revokeRoleProcessArea((BigDecimal)rwr.getAttribute("processAreaCode"),
                                                  (BigDecimal)rwr.getAttribute("processCode"));
                        }


                    }


                } else {
                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;
                    System.out.println("Length" + rows.length);
                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];
                        session.setAttribute("creditLimitValue", null);
                        session.setAttribute("debitLimitValue", null);
                        session.setAttribute("creditLimitValue",
                                             rwr.getAttribute("processSubAredCreditLimit"));
                        session.setAttribute("debitLimitValue",
                                             rwr.getAttribute("processSubAreaDebitLimit"));

                        boolean checked =
                            (Boolean)rwr.getAttribute("processSubAreaSelected");

                        if (checked) {

                            if (rwr.getAttribute("processSubAreaType").toString().equals("A")) {
                                if (session.getAttribute("debitLimitValue") ==
                                    null) {
                                    session.setAttribute("debitLimitValue",
                                                         0.00);
                                    // debitLimitValue=((BigDecimal)session.getAttribute("debitLimitValue")).toString();
                                }
                                if (session.getAttribute("creditLimitValue") ==
                                    null) {
                                    session.setAttribute("creditLimitValue",
                                                         0.00);
                                    // creditLimitValue=((BigDecimal)session.getAttribute("creditLimitValue")).toString();
                                }
                                grantRoleProcessSubArea((BigDecimal)rwr.getAttribute("processSubAreaCode"),
                                                        (BigDecimal)rwr.getAttribute("processRoleSubAreaCode"),
                                                        session.getAttribute("debitLimitValue").toString(),
                                                        session.getAttribute("creditLimitValue").toString());

                            } else {
                                grantRoleProcessSubArea((BigDecimal)rwr.getAttribute("processSubAreaCode"),
                                                        (BigDecimal)rwr.getAttribute("processRoleSubAreaCode"),
                                                        null, null);
                            }


                        } else {

                            revokeRoleProcessSubArea((BigDecimal)rwr.getAttribute("processSubAreaCode"),
                                                     (BigDecimal)rwr.getAttribute("processRoleSubAreaCode"));

                        }
                    }
                }
            } // end check for NULL node
            else {
                System.out.println("The node is empty");
            }
        } // End check if keyset is empty
        else {
            System.out.println("The keyset is empty");
        }

        ADFUtils.findIterator("findSystemProcessIterator").executeQuery();
        GlobalCC.refreshUI(processAreas);

        GlobalCC.refreshUI(rolesPanel);

        GlobalCC.INFORMATIONREPORTING("Operation successfully executed");
        return null;
    }

    public String grantRevokesUserRoles() {
        JUCtrlValueBinding r = null;
        OracleConnection conn = null;
        int rowCount2 = processSubArea.getRowCount();
        int count = 0;
        System.out.println("this is row count" + rowCount2);
        for (int i = 0; i < rowCount2; i++) {
            Boolean Accept = false;
            r = (JUCtrlValueBinding)processSubArea.getRowData(i);
            Accept = (Boolean)r.getAttribute("processSubAreaSelected");
            if (Accept)
                count++;
        }

        try {
            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)processSubArea.getRowData(i);
                if (r == null)
                    continue;
                Accept = (Boolean)r.getAttribute("processSubAreaSelected");
                if (Accept) {
                    DBConnector datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();

                    if (r.getAttribute("processSubAreaType").toString().equalsIgnoreCase("A")) {
                        System.out.println("This grants with limits");
                        String grantQuery =
                            "begin tqc_roles_cursor.Grant_Role_Process_SubArea(?,?,?,?); end;";

                        System.out.println("Granting Role Process Sub Area : (" +
                                           processSubArea + "," +
                                           (BigDecimal)session.getAttribute("roleCode") +
                                           ")");
                        OracleCallableStatement cst = null;
                        cst =
(OracleCallableStatement)conn.prepareCall(grantQuery);
                        cst.setBigDecimal(1,
                                          (BigDecimal)r.getAttribute("processSubAreaCode"));
                        cst.setBigDecimal(2,
                                          (BigDecimal)session.getAttribute("roleCode"));
                        cst.setBigDecimal(3,
                                          (BigDecimal)r.getAttribute("processSubAreaDebitLimit"));
                        cst.setBigDecimal(4,
                                          (BigDecimal)r.getAttribute("processSubAredCreditLimit"));
                        cst.execute();
                    } else {
                        System.out.println("This grants without limits");
                        String grantQuery =
                            "begin tqc_roles_cursor.Grant_Role_Process_SubArea(?,?,?,?); end;";

                        System.out.println("Granting Role Process Sub Area : (" +
                                           processSubArea + "," +
                                           (BigDecimal)session.getAttribute("roleCode") +
                                           ")");
                        OracleCallableStatement cst = null;
                        cst =
(OracleCallableStatement)conn.prepareCall(grantQuery);
                        cst.setBigDecimal(1,
                                          (BigDecimal)r.getAttribute("processSubAreaCode"));
                        cst.setBigDecimal(2,
                                          (BigDecimal)session.getAttribute("roleCode"));
                        cst.setBigDecimal(3, null);
                        cst.setBigDecimal(4, null);
                        cst.execute();

                    }
                } else {
                    revokeRoleProcessSubArea((BigDecimal)r.getAttribute("processSubAreaCode"),
                                             null);
                }
            }
          unAuthorizeRole();
          ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
          GlobalCC.refreshUI(treeSystemRoles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
        GlobalCC.INFORMATIONREPORTING("Process Executed Successfully");

        return null;
    }

    public void setProcessAreas(RichTree processAreas) {
        this.processAreas = processAreas;
    }

    public RichTree getProcessAreas() {
        return processAreas;
    }

    public void setProcessCheck(RichSelectBooleanCheckbox processCheck) {
        this.processCheck = processCheck;
    }

    public RichSelectBooleanCheckbox getProcessCheck() {
        return processCheck;
    }

    public void estabDisclosed(RowDisclosureEvent rowDisclosureEvent) {
        RichTree table = (RichTree)rowDisclosureEvent.getSource();
        RowKeySet discloseRowKeySet = table.getDisclosedRowKeys();
        RowKeySet lastAddedRowKeySet = rowDisclosureEvent.getAddedSet();
        Iterator lastAddedRowKeySetIter = lastAddedRowKeySet.iterator();
        if (lastAddedRowKeySetIter.hasNext()) {
            discloseRowKeySet.clear();
            Object lastRowKey = lastAddedRowKeySetIter.next();
            discloseRowKeySet.add(lastRowKey);
            makeDisclosedRowCurrent(table, lastAddedRowKeySet);
            AdfFacesContext adfFacesContext = null;
            adfFacesContext = AdfFacesContext.getCurrentInstance();
            adfFacesContext.addPartialTarget(table.getParent());
        }
    }

    private void makeDisclosedRowCurrent(RichTree table, RowKeySet keySet) {
        table.setSelectedRowKeys(keySet);
        CollectionModel tableModel = (CollectionModel)table.getValue();
        JUCtrlHierBinding tableHierBinding = null;
        tableHierBinding = (JUCtrlHierBinding)(tableModel).getWrappedData();
        DCIteratorBinding dCIteratorBindin = null;
        dCIteratorBindin = tableHierBinding.getDCIteratorBinding();
        Iterator keySetIter = keySet.iterator();
        List firstKey = (List)keySetIter.next();
        oracle.jbo.Key key = (oracle.jbo.Key)firstKey.get(0);
        dCIteratorBindin.setCurrentRowWithKey(key.toStringFormat(true));
    }

    public void processCheckVal(ValueChangeEvent valueChangeEvent) {
        System.out.println("Process check initiated ...");
        if (processCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            processSelected = true;

        }

        if (!processCheck.isSelected() &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            processSelected = false;
        }
    }

    public String grantProcess(BigDecimal processCode) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.grantProcessRole(?,?); end;";

            System.out.println("Granting process (" + processCode + "," +
                               (BigDecimal)session.getAttribute("roleCode") +
                               ")");

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processCode);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("roleCode"));
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String revokeProcess(BigDecimal processCode) {

        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.revokeProcessRole(?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, processCode);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("roleCode"));
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setSystemsLOV(RichTable systemsLOV) {
        this.systemsLOV = systemsLOV;
    }

    public RichTable getSystemsLOV() {
        return systemsLOV;
    }

    public void setSystemDesc(RichInputText systemDesc) {
        this.systemDesc = systemDesc;
    }

    public RichInputText getSystemDesc() {
        return systemDesc;
    }

    public void setSystemUsersLOV(RichTable systemUsersLOV) {
        this.systemUsersLOV = systemUsersLOV;
    }

    public RichTable getSystemUsersLOV() {
        return systemUsersLOV;
    }

    public void setTreeSystemRoles(RichTree treeSystemRoles) {
        this.treeSystemRoles = treeSystemRoles;
    }

    public RichTree getTreeSystemRoles() {
        return treeSystemRoles;
    }

    public void treeSystemRolesListener(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeSystemRoles.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)treeSystemRoles.getRowData();

                    String nodeType =
                        (String)nodeBinding.getRow().getAttribute("nodeType");

                    if (nodeType.equals("S")) {
                        //sysVal.setValue(nodeBinding.getRow().getAttribute("sysName"));
                        mainPanel.setRendered(true);
                        GlobalCC.refreshUI(mainLay);
                        session.setAttribute("sysCode",
                                             nodeBinding.getRow().getAttribute("processCode"));
                        session.setAttribute("systemRoleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));
                        session.setAttribute("processRoleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));
                        session.removeAttribute("processAreaCode");
                        //                      session.removeAttribute("processRoleCode");
                        //                      session.removeAttribute("processCode");
                        session.setAttribute("roleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));

                        txtSysRoleCode.setValue(nodeBinding.getRow().getAttribute("roleCode"));
                        txtSysCode.setValue(nodeBinding.getRow().getAttribute("processCode"));
                        txtSysRoleName.setValue(nodeBinding.getRow().getAttribute("roleName"));
                        txtSysRoleDateCreated.setValue(nodeBinding.getRow().getAttribute("roleCrtDate"));
                        txtSysRoleShortDesc.setValue(nodeBinding.getRow().getAttribute("roleShtDesc"));
                        txtSysRoleStatus.setValue(nodeBinding.getRow().getAttribute("roleStatus"));
                        txtSysRoleAuthorized.setValue(nodeBinding.getRow().getAttribute("roleAuthorized"));
                        btnCreateUpdateSystemRole.setText("Edit");
                        
                    } else if (nodeType.equals("P")) {
                        session.setAttribute("sysCode",
                                             nodeBinding.getRow().getAttribute("sysCode"));
                        mainPanel.setRendered(false);
                        GlobalCC.refreshUI(mainLay);
                        txtSysRoleCode.setValue(null);
                        txtSysCode.setValue(null);
                        txtSysRoleName.setValue(null);
                        txtSysRoleDateCreated.setValue(null);
                        txtSysRoleShortDesc.setValue(null);
                        //txtSysRoleStatus.setValue(null);
                        btnCreateUpdateSystemRole.setText("Save");
                    }
                }
            }
        }

        session.setAttribute("sprsaCode", null);
        ADFUtils.findIterator("findSubAreaAuthLevelsIterator").executeQuery();
        GlobalCC.refreshUI(subAreaLevels);
        ADFUtils.findIterator("findSystemSubAreasIterator").executeQuery();
        GlobalCC.refreshUI(subAreaTab);

        ADFUtils.findIterator("findSystemProcessIterator").executeQuery();
        GlobalCC.refreshUI(processAreas);
        ADFUtils.findIterator("findSysRolesIterator").executeQuery();
        GlobalCC.refreshUI(sysRoles);

        ADFUtils.findIterator("fetchSystemPostsBySystemIterator").executeQuery();
        GlobalCC.refreshUI(tblSystemPostsPop);

        GlobalCC.refreshUI(roleAreaLevels);
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea); //Refresh the panel
        GlobalCC.refreshUI(panelMainSysRoleDetails);
    }
    public void treeSystemRolesListenerNew(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    rolesTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)rolesTree.getRowData();

                    String nodeType =
                        (String)nodeBinding.getRow().getAttribute("nodeType");

                    if (nodeType.equals("S")) {
                        //sysVal.setValue(nodeBinding.getRow().getAttribute("sysName"));
                        mainPanel.setRendered(true);
                        GlobalCC.refreshUI(mainLay);
                        session.setAttribute("systemRoleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));
                        session.setAttribute("processRoleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));
                        session.removeAttribute("processAreaCode");
                        //                      session.removeAttribute("processRoleCode");
                        //                      session.removeAttribute("processCode");
                        session.setAttribute("roleCode",
                                             nodeBinding.getRow().getAttribute("roleCode"));
                       
                        
                            txtSysRoleCode.setValue(nodeBinding.getRow().getAttribute("roleCode"));
                            txtSysCode.setValue(nodeBinding.getRow().getAttribute("processCode"));
                            txtSysRoleName.setValue(nodeBinding.getRow().getAttribute("roleName"));
                            txtSysRoleDateCreated.setValue(nodeBinding.getRow().getAttribute("roleCrtDate"));
                            txtSysRoleShortDesc.setValue(nodeBinding.getRow().getAttribute("roleShtDesc"));
                            txtSysRoleStatus.setValue(nodeBinding.getRow().getAttribute("roleStatus"));
                            txtSysRoleAuthorized.setValue(nodeBinding.getRow().getAttribute("roleAuthorized"));
                            btnCreateUpdateSystemRole.setText("Edit");
                        
                    } else if (nodeType.equals("P")) {
                        mainPanel.setRendered(false);
                        GlobalCC.refreshUI(mainLay);
                        txtSysRoleCode.setValue(null);
                        txtSysCode.setValue(null);
                        txtSysRoleName.setValue(null);
                        txtSysRoleDateCreated.setValue(null);
                        txtSysRoleShortDesc.setValue(null);
                        //txtSysRoleStatus.setValue(null);
                        btnCreateUpdateSystemRole.setText("Save");
                    }
                }
            }
        }

        session.setAttribute("sprsaCode", null);
        ADFUtils.findIterator("findSubAreaAuthLevelsIterator").executeQuery();
        GlobalCC.refreshUI(subAreaLevels);
        ADFUtils.findIterator("findSystemSubAreasIterator").executeQuery();
        GlobalCC.refreshUI(subAreaTab);

        ADFUtils.findIterator("findSystemProcessIterator").executeQuery();
        GlobalCC.refreshUI(processAreas);
        ADFUtils.findIterator("findSysRolesIterator").executeQuery();
        GlobalCC.refreshUI(sysRoles);

        ADFUtils.findIterator("fetchSystemPostsBySystemIterator").executeQuery();
        GlobalCC.refreshUI(tblSystemPostsPop);

        GlobalCC.refreshUI(roleAreaLevels);
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea); //Refresh the panel
        GlobalCC.refreshUI(panelMainSysRoleDetails);
    }

    public void setTxtSysRoleCode(RichInputText txtSysRoleCode) {
        this.txtSysRoleCode = txtSysRoleCode;
    }

    public RichInputText getTxtSysRoleCode() {
        return txtSysRoleCode;
    }

    public void setTxtSysCode(RichInputText txtSysCode) {
        this.txtSysCode = txtSysCode;
    }

    public RichInputText getTxtSysCode() {
        return txtSysCode;
    }

    public void setTxtSysRoleName(RichInputText txtSysRoleName) {
        this.txtSysRoleName = txtSysRoleName;
    }

    public RichInputText getTxtSysRoleName() {
        return txtSysRoleName;
    }

    public void setTxtSysRoleDateCreated(RichInputDate txtSysRoleDateCreated) {
        this.txtSysRoleDateCreated = txtSysRoleDateCreated;
    }

    public RichInputDate getTxtSysRoleDateCreated() {
        return txtSysRoleDateCreated;
    }

    public void setTxtSysRoleShortDesc(RichInputText txtSysRoleShortDesc) {
        this.txtSysRoleShortDesc = txtSysRoleShortDesc;
    }

    public RichInputText getTxtSysRoleShortDesc() {
        return txtSysRoleShortDesc;
    }

    public void setTxtSysRoleStatus(RichSelectOneChoice txtSysRoleStatus) {
        this.txtSysRoleStatus = txtSysRoleStatus;
    }

    public RichSelectOneChoice getTxtSysRoleStatus() {
        return txtSysRoleStatus;
    }

    public void setPanelMainSysRoleDetails(RichPanelBox panelMainSysRoleDetails) {
        this.panelMainSysRoleDetails = panelMainSysRoleDetails;
    }

    public RichPanelBox getPanelMainSysRoleDetails() {
        return panelMainSysRoleDetails;
    }

    public String actionNewSystemRole() {
        if (session.getAttribute("sysCode") == null) {
            GlobalCC.errorValueNotEntered("Value Missing : You need to select a System first.");
        } else {

            txtSysRoleCode.setValue(null);
            txtSysCode.setValue(session.getAttribute("sysCode"));
            txtSysRoleName.setValue(null);
            txtSysRoleDateCreated.setValue(null);
            txtSysRoleShortDesc.setValue(null);
            //txtSysRoleStatus.setValue(null);
            btnCreateUpdateSystemRole.setText("Save");

            //Refresh the panel
            GlobalCC.refreshUI(panelMainSysRoleDetails);
            tabRoleAreas.setDisclosed(false);
            tabRoleDetails.setDisclosed(true);
            GlobalCC.refreshUI(panelMain);
            mainPanel.setRendered(true);
            GlobalCC.refreshUI(mainLay);
        }
        return null;
    }
    
    public String newSystemRolePopUp() {
        if (session.getAttribute("sysCode") == null) {
            GlobalCC.errorValueNotEntered("Value Missing : You need to select a System first.");
        } else {

            txtSysRoleCode.setValue(null);
            txtSysCode.setValue(session.getAttribute("sysCode"));
            txtSysRoleName.setValue(null);
            txtSysRoleDateCreated.setValue(null);
            txtSysRoleShortDesc.setValue(null);
            //txtSysRoleStatus.setValue(null);
            btnCreateUpdateSystemRole.setText("Save");

            //Refresh the panel
            GlobalCC.showPopup("fms:rolesDetailsPop");
        }
        return null;
    }
    
    
    public String editSystemRolePopUp() {
        if (session.getAttribute("roleCode") == null) {
            GlobalCC.errorValueNotEntered("Value Missing : You need to select a Role first.");
        } else {
            
            txtSysCode.setValue(session.getAttribute("sysCode"));

            //Refresh the panel
            GlobalCC.showPopup("fms:rolesDetailsPop");
            GlobalCC.refreshUI(rolesDetailsPopUp);
            
        }
        return null;
    }

    public String actionCreateUpdateSystemRole() {
        if (btnCreateUpdateSystemRole.getText().equals("Edit")) {
            actionUpdateSystemRole();
        } else {
            String roleNameVal =
                GlobalCC.checkNullValues(txtSysRoleName.getValue());
            String roleShtDescVal =
                GlobalCC.checkNullValues(txtSysRoleShortDesc.getValue());

            if (roleNameVal == null) {
                GlobalCC.errorValueNotEntered("Missing Value: Role Name");
                return null;
            }
            if (roleShtDescVal == null) {
                GlobalCC.errorValueNotEntered("Missing Value: Role Short Description");
                return null;
            }

            DBConnector datahandler = new DBConnector();
            OracleConnection conn = datahandler.getDatabaseConnection();
            try {
                String grantQuery =
                    "begin tqc_roles_cursor.Creat_Role(?,?,?,?); end;";
                
            
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
                cst.setBigDecimal(1,(BigDecimal)session.getAttribute("sysCode"));
                cst.setString(2, roleNameVal);
                cst.setString(3, roleShtDescVal);
                cst.setString(4, (String)session.getAttribute("Username"));
                cst.execute();
              
                txtSysRoleCode.setValue(null);
                txtSysCode.setValue(session.getAttribute("sysCode"));
                txtSysRoleName.setValue(null);
                txtSysRoleDateCreated.setValue(null);
                txtSysRoleShortDesc.setValue(null);
                txtSysRoleAuthorized.setValue(null);
                txtSysRoleStatus.setValue("I"); 
                btnCreateUpdateSystemRole.setText("Save");
              
                ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
                GlobalCC.refreshUI(treeSystemRoles);
                ADFUtils.findIterator("findSysRolesIterator").executeQuery(); 
                GlobalCC.refreshUI(sysRoles); 
                // FIX Refresh System Roles on adding
                ADFUtils.findIterator("findSystemRolesIterator").executeQuery(); 
                GlobalCC.refreshUI(rolesTree);
                // END. FIX Refresh System Roles on adding
                GlobalCC.refreshUI(panelMainSysRoleDetails); 
                conn.close(); 
                
                GlobalCC.INFORMATIONREPORTING("System Role Created Successfully!.");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            

        }
        ADFUtils.findIterator("findSystemRolesIterator").executeQuery();
        GlobalCC.refreshUI(rolesTree);
        return null;
    }
    public String createUpdateSystemRolePopUp() {
        if (btnCreateUpdateSystemRole.getText().equals("Edit")) {
            actionUpdateSystemRole();
        } else {
            String roleNameVal =
                GlobalCC.checkNullValues(txtSysRoleName.getValue());
            String roleShtDescVal =
                GlobalCC.checkNullValues(txtSysRoleShortDesc.getValue());

            if (roleNameVal == null) {
                GlobalCC.errorValueNotEntered("Missing Value: Role Name");
                return null;
            }
            if (roleShtDescVal == null) {
                GlobalCC.errorValueNotEntered("Missing Value: Role Short Description");
                return null;
            }

            DBConnector datahandler = new DBConnector();
            OracleConnection conn = datahandler.getDatabaseConnection();
            try {
                String grantQuery =
                    "begin tqc_roles_cursor.Creat_Role(?,?,?,?); end;";

                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
                cst.setBigDecimal(1,
                                  (BigDecimal)session.getAttribute("sysCode"));
                cst.setString(2, roleNameVal);
                cst.setString(3, roleShtDescVal);
                cst.setString(4, (String)session.getAttribute("Username"));
                cst.execute();
              
                txtSysRoleCode.setValue(null);
                txtSysCode.setValue(session.getAttribute("sysCode"));
                txtSysRoleName.setValue(null);
                txtSysRoleDateCreated.setValue(null);
                txtSysRoleShortDesc.setValue(null);
                txtSysRoleAuthorized.setValue(null);
                txtSysRoleStatus.setValue("I"); 
                btnCreateUpdateSystemRole.setText("Save");
              
                ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
                GlobalCC.refreshUI(treeSystemRoles);
                ADFUtils.findIterator("findSysRolesIterator").executeQuery(); 
                GlobalCC.refreshUI(sysRoles); 
                GlobalCC.refreshUI(panelMainSysRoleDetails); 
                conn.close(); 
                
                GlobalCC.INFORMATIONREPORTING("System Role Created Successfully!.");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            

        }
        GlobalCC.hidePopup("fms:rolesDetailsPop");
        return null;
    }

    public String actionUpdateSystemRole() {
        String roleCodeVal =
            GlobalCC.checkNullValues(txtSysRoleCode.getValue());
        String roleNameVal =
            GlobalCC.checkNullValues(txtSysRoleName.getValue());
        String roleShtDescVal =
            GlobalCC.checkNullValues(txtSysRoleShortDesc.getValue());
        String roleStatus =
            GlobalCC.checkNullValues(txtSysRoleStatus.getValue());

        if (roleCodeVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Code");
            return null;
        }
        if (roleNameVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Name");
            return null;
        }
        if (roleShtDescVal == null) {
            GlobalCC.errorValueNotEntered("Missing Value: Role Short Description");
            return null;
        }

        DBConnector datahandler = new DBConnector();
        OracleConnection conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery = "begin tqc_roles_cursor.Edit_Role(?,?,?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1, new BigDecimal(roleCodeVal));
            cst.setString(2, roleNameVal);
            cst.setString(3, roleShtDescVal);
            cst.setString(4, roleStatus);
            cst.setString(5, (String)session.getAttribute("Username"));
            cst.execute(); 
  
            conn.close();
            
            txtSysRoleCode.setValue(null);
            txtSysCode.setValue(session.getAttribute("sysCode"));
            txtSysRoleName.setValue(null);
            txtSysRoleDateCreated.setValue(null);
            txtSysRoleShortDesc.setValue(null);
            txtSysRoleAuthorized.setValue(null);
            txtSysRoleStatus.setValue("I"); 
            btnCreateUpdateSystemRole.setText("Save");
          
            ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
            ADFUtils.findIterator("findSysRolesIterator").executeQuery(); 
            
            GlobalCC.refreshUI(sysRoles);
            GlobalCC.refreshUI(treeSystemRoles);
            GlobalCC.refreshUI(panelMainSysRoleDetails);
            
            // FIX Refresh System Roles on adding
            ADFUtils.findIterator("findSystemRolesIterator").executeQuery(); 
            GlobalCC.refreshUI(rolesTree);
            // END. FIX Refresh System Roles on adding
          
            
            GlobalCC.INFORMATIONREPORTING("System Role Updated Successfully!.");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } 
        return null;
    }

  public String actionAuthorizeSystemRole() { 
    
      /*Authorization auth = new Authorization();
      String process = "SA";
      String processArea = "SAR";
      String processSubArea = "SARAUTH";
      String AccessGranted =
          auth.checkUserRights(process, processArea, processSubArea, null,
                               null);
      if(AccessGranted.equalsIgnoreCase("Y")!=true) { 
          GlobalCC.accessDenied();
          return null;
      }*/
    
      BigDecimal roleCodeVal =
          GlobalCC.checkBDNullValues(txtSysRoleCode.getValue());

      if (roleCodeVal == null) {
          GlobalCC.errorValueNotEntered("Missing Value: Role Code");
          return null;
      }

      DBConnector datahandler = new DBConnector();
      OracleConnection conn = datahandler.getDatabaseConnection();
      try {
          String grantQuery =
              "begin tqc_roles_cursor.authorize_role(?,?,?); end;";

          OracleCallableStatement cst = null;
          cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
          cst.registerOutParameter(1, OracleTypes.VARCHAR);
          cst.setString(2, (String)session.getAttribute("Username"));
          cst.setBigDecimal(3, roleCodeVal);
           
          cst.execute(); 
          String msg=cst.getString(1); 
          conn.close();
        
          txtSysRoleCode.setValue(null);
          txtSysCode.setValue(session.getAttribute("sysCode"));
          txtSysRoleName.setValue(null);
          txtSysRoleDateCreated.setValue(null);
          txtSysRoleShortDesc.setValue(null);
          txtSysRoleAuthorized.setValue(null);
          txtSysRoleStatus.setValue("I"); 
        
          ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
          GlobalCC.refreshUI(treeSystemRoles);
          ADFUtils.findIterator("findSysRolesIterator").executeQuery(); 
          GlobalCC.refreshUI(sysRoles);  
          GlobalCC.refreshUI(panelMainSysRoleDetails);
        
          if(!("success".equalsIgnoreCase(msg)))
          {
            GlobalCC.EXCEPTIONREPORTING(msg);
          }else
          {
            GlobalCC.INFORMATIONREPORTING("System Role Updated Successfully!.");
          } 
         
      } catch (Exception e) {
          GlobalCC.EXCEPTIONREPORTING(conn, e);
      }
      GlobalCC.hidePopup("fms:rolesDetailsPop");
      return null;
  }
    public void setBtnCreateUpdateSystemRole(RichCommandButton btnCreateUpdateSystemRole) {
        this.btnCreateUpdateSystemRole = btnCreateUpdateSystemRole;
    }

    public RichCommandButton getBtnCreateUpdateSystemRole() {
        return btnCreateUpdateSystemRole;
    }

    public String actionCancelSystemRole() {
        txtSysRoleCode.setValue(null);
        txtSysCode.setValue(session.getAttribute("sysCode"));
        txtSysRoleName.setValue(null);
        txtSysRoleDateCreated.setValue(null);
        txtSysRoleShortDesc.setValue(null);
        //txtSysRoleStatus.setValue(null);
        btnCreateUpdateSystemRole.setText("Save");
        GlobalCC.hidePopup("fms:rolesDetailsPop");
        return null;
    }

    public String actionDropSystemRole() {
        if (session.getAttribute("systemRoleCode") != null) {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            try {
                String grantQuery =
                    "begin tqc_roles_cursor.Drop_Role(?); end;";

                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
                cst.setBigDecimal(1,
                                  (BigDecimal)session.getAttribute("systemRoleCode"));
                cst.execute();
                conn.close();

                ADFUtils.findIterator("findSystemsIterator").executeQuery();
                GlobalCC.refreshUI(treeSystemRoles);
                GlobalCC.INFORMATIONREPORTING("System Role Dropped Successfully!.");
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected");
        }
        return null;
    }

    public void setTabRoleDetails(RichShowDetailItem tabRoleDetails) {
        this.tabRoleDetails = tabRoleDetails;
    }

    public RichShowDetailItem getTabRoleDetails() {
        return tabRoleDetails;
    }

    public void setPanelMain(RichPanelBox panelMain) {
        this.panelMain = panelMain;
    }

    public RichPanelBox getPanelMain() {
        return panelMain;
    }

    public void setTabRoleAreas(RichShowDetailItem tabRoleAreas) {
        this.tabRoleAreas = tabRoleAreas;
    }

    public RichShowDetailItem getTabRoleAreas() {
        return tabRoleAreas;
    }

    public void setRoleAreaLevels(RichShowDetailItem roleAreaLevels) {
        this.roleAreaLevels = roleAreaLevels;
    }

    public RichShowDetailItem getRoleAreaLevels() {
        return roleAreaLevels;
    }

    public void setSubAreaLevels(RichTable subAreaLevels) {
        this.subAreaLevels = subAreaLevels;
    }

    public RichTable getSubAreaLevels() {
        return subAreaLevels;
    }

    public void setSubAreaTab(RichTable subAreaTab) {
        this.subAreaTab = subAreaTab;
    }

    public RichTable getSubAreaTab() {
        return subAreaTab;
    }

    public void subAreaSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet rowKeySet = subAreaTab.getSelectedRowKeys();

            Object key2 = rowKeySet.iterator().next();
            subAreaTab.setRowKey(key2);

            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)subAreaTab.getRowData();
            session.setAttribute("sprsaCode",
                                 nodeBinding.getAttribute("processSubAreaCode"));
            session.setAttribute("spsatCode", null);

            ADFUtils.findIterator("findSubAreaAuthLevelsIterator").executeQuery();
            GlobalCC.refreshUI(subAreaLevels);

            ADFUtils.findIterator("fetchSysProcessSubAreaLimitsIterator").executeQuery();
            GlobalCC.refreshUI(tblSubAreaLimits);
            ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblSubAreaLevels);


        }
    }

    public String adLevel() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "fms:authLvl" +
                             "').show(hints);");
        lvlId.setValue(null);
        lvlNo.setValue(null);
        lvlDesc.setValue(null);
        lvlDesc.setLabel(null);
        return null;
    }

    public String editlevel() {
        RowKeySet rowKeySet = subAreaLevels.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        subAreaLevels.setRowKey(key2);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)subAreaLevels.getRowData();
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "fms:authLvl" +
                             "').show(hints);");
        lvlId.setValue(nodeBinding.getAttribute("tqualCode"));
        lvlNo.setValue(nodeBinding.getAttribute("levelId"));
        lvlDesc.setValue(nodeBinding.getAttribute("srlsName"));
        lvlDesc.setLabel(nodeBinding.getAttribute("srlsCode").toString());
        return null;
    }

    public String deleteLevel() {
        RowKeySet rowKeySet = subAreaLevels.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        subAreaLevels.setRowKey(key2);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)subAreaLevels.getRowData();
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {


            String query =
                "begin TQC_SETUPS_CURSOR.authLevelsProc(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "D");
            cst.setBigDecimal(2,
                              (BigDecimal)nodeBinding.getAttribute("tqualCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("sprsaCode"));
            cst.setBigDecimal(4,
                              (BigDecimal)nodeBinding.getAttribute("levelId"));
            cst.setBigDecimal(5,
                              (BigDecimal)nodeBinding.getAttribute("srlsCode"));
            cst.execute();
            cst.close();
            conn.close();

            ADFUtils.findIterator("findSubAreaAuthLevelsIterator").executeQuery();
            GlobalCC.refreshUI(subAreaLevels);
            GlobalCC.INFORMATIONREPORTING("Record Deleted");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setLvlId(RichInputNumberSpinbox lvlId) {
        this.lvlId = lvlId;
    }

    public RichInputNumberSpinbox getLvlId() {
        return lvlId;
    }

    public void setLvlNo(RichInputNumberSpinbox lvlNo) {
        this.lvlNo = lvlNo;
    }

    public RichInputNumberSpinbox getLvlNo() {
        return lvlNo;
    }

    public void setLvlDesc(RichInputText lvlDesc) {
        this.lvlDesc = lvlDesc;
    }

    public RichInputText getLvlDesc() {
        return lvlDesc;
    }

    public String launchRoles() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "fms:sysRoles" +
                             "').show(hints);");
        return null;
    }

    public String saveAuthLvl() {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        try {

            if (GlobalCC.checkNullValues(lvlDesc.getValue()) == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Role");
                return null;
            }
            String query =
                "begin TQC_SETUPS_CURSOR.authLevelsProc(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (GlobalCC.checkNullValues(lvlId.getValue()) != null) {
                cst.setString(1, "E");

            } else {
                cst.setString(1, "A");
            }
            cst.setString(2, GlobalCC.checkNullValues(lvlId.getValue()));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("sprsaCode"));
            cst.setString(4, GlobalCC.checkNullValues(lvlNo.getValue()));
            cst.setString(5, lvlDesc.getLabel());
            cst.execute();
            cst.close();
            conn.close();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:authLvl" + "').hide(hints);");
            ADFUtils.findIterator("findSubAreaAuthLevelsIterator").executeQuery();
            GlobalCC.refreshUI(subAreaLevels);
            GlobalCC.INFORMATIONREPORTING("Record Saved");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setSysAuthRoles(RichTable sysAuthRoles) {
        this.sysAuthRoles = sysAuthRoles;
    }

    public RichTable getSysAuthRoles() {
        return sysAuthRoles;
    }

    public String sysRoleSelected() {
        RowKeySet rowKeySet = sysAuthRoles.getSelectedRowKeys();
        if (rowKeySet == null) {
            return null;
        }
        if (!rowKeySet.iterator().hasNext()) //no selection
        {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        sysAuthRoles.setRowKey(key2);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)sysAuthRoles.getRowData();
        if (nodeBinding == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        lvlDesc.setValue(nodeBinding.getAttribute("roleName"));
        lvlDesc.setLabel(nodeBinding.getAttribute("roleCode").toString());
        GlobalCC.refreshUI(lvlDesc);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "fms:sysRoles" +
                             "').hide(hints);");
        return null;
    }

    public void setRolesPanel(RichPanelBox rolesPanel) {
        this.rolesPanel = rolesPanel;
    }

    public RichPanelBox getRolesPanel() {
        return rolesPanel;
    }

    public void selectRoleListener(SelectionEvent selectionEvent) {
        System.out.println("executed");
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    processAreas.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)processAreas.getRowData();
                    if (nd.getRow().getAttribute("areaSubArea").equals("P")) {
                        session.setAttribute("processCode",
                                             nd.getRow().getAttribute("processCode"));

                    } else if (nd.getRow().getAttribute("areaSubArea").equals("A")) {
                        session.setAttribute("processArea",
                                             nd.getRow().getAttribute("processArea"));
                        session.setAttribute("processAreaCode",
                                             nd.getRow().getAttribute("processAreaCode"));
                    }
                    ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
                    GlobalCC.refreshUI(processSubArea);
                }

            }
        }
    }

    // ======================= Limits and Levels ==========================

    public void setTblSubAreaLimits(RichTable tblSubAreaLimits) {
        this.tblSubAreaLimits = tblSubAreaLimits;
    }

    public RichTable getTblSubAreaLimits() {
        return tblSubAreaLimits;
    }

    public void setTblSubAreaLevels(RichTable tblSubAreaLevels) {
        this.tblSubAreaLevels = tblSubAreaLevels;
    }

    public RichTable getTblSubAreaLevels() {
        return tblSubAreaLevels;
    }

    public void setTxtSpsatCode(RichInputText txtSpsatCode) {
        this.txtSpsatCode = txtSpsatCode;
    }

    public RichInputText getTxtSpsatCode() {
        return txtSpsatCode;
    }

    public void setTxtSpsatSprsaCode(RichInputText txtSpsatSprsaCode) {
        this.txtSpsatSprsaCode = txtSpsatSprsaCode;
    }

    public RichInputText getTxtSpsatSprsaCode() {
        return txtSpsatSprsaCode;
    }

    public void setTxtSpsatNoOfLevel(RichInputNumberSpinbox txtSpsatNoOfLevel) {
        this.txtSpsatNoOfLevel = txtSpsatNoOfLevel;
    }

    public RichInputNumberSpinbox getTxtSpsatNoOfLevel() {
        return txtSpsatNoOfLevel;
    }

    public void setTxtSpsatMinLimit(RichInputNumberSpinbox txtSpsatMinLimit) {
        this.txtSpsatMinLimit = txtSpsatMinLimit;
    }

    public RichInputNumberSpinbox getTxtSpsatMinLimit() {
        return txtSpsatMinLimit;
    }

    public void setTxtSpsatMaxLimit(RichInputNumberSpinbox txtSpsatMaxLimit) {
        this.txtSpsatMaxLimit = txtSpsatMaxLimit;
    }

    public RichInputNumberSpinbox getTxtSpsatMaxLimit() {
        return txtSpsatMaxLimit;
    }

    public void setBtnSaveUpdateSubAreaLimit(RichCommandButton btnSaveUpdateSubAreaLimit) {
        this.btnSaveUpdateSubAreaLimit = btnSaveUpdateSubAreaLimit;
    }

    public RichCommandButton getBtnSaveUpdateSubAreaLimit() {
        return btnSaveUpdateSubAreaLimit;
    }

    public void setTxtSpsalCode(RichInputText txtSpsalCode) {
        this.txtSpsalCode = txtSpsalCode;
    }

    public RichInputText getTxtSpsalCode() {
        return txtSpsalCode;
    }

    public void setTxtSpsalSprsaCode(RichInputText txtSpsalSprsaCode) {
        this.txtSpsalSprsaCode = txtSpsalSprsaCode;
    }

    public RichInputText getTxtSpsalSprsaCode() {
        return txtSpsalSprsaCode;
    }

    public void setTxtSpsalSpsatCode(RichInputText txtSpsalSpsatCode) {
        this.txtSpsalSpsatCode = txtSpsalSpsatCode;
    }

    public RichInputText getTxtSpsalSpsatCode() {
        return txtSpsalSpsatCode;
    }

    public void setTxtSpsalLevel(RichInputNumberSpinbox txtSpsalLevel) {
        this.txtSpsalLevel = txtSpsalLevel;
    }

    public RichInputNumberSpinbox getTxtSpsalLevel() {
        return txtSpsalLevel;
    }

    public void setTxtSpsalApproverType(RichSelectOneChoice txtSpsalApproverType) {
        this.txtSpsalApproverType = txtSpsalApproverType;
    }

    public RichSelectOneChoice getTxtSpsalApproverType() {
        return txtSpsalApproverType;
    }

    public void setTxtSpsalApproverId(RichInputText txtSpsalApproverId) {
        this.txtSpsalApproverId = txtSpsalApproverId;
    }

    public RichInputText getTxtSpsalApproverId() {
        return txtSpsalApproverId;
    }

    public void setBtnSaveUpdateSubAreaLevel(RichCommandButton btnSaveUpdateSubAreaLevel) {
        this.btnSaveUpdateSubAreaLevel = btnSaveUpdateSubAreaLevel;
    }

    public RichCommandButton getBtnSaveUpdateSubAreaLevel() {
        return btnSaveUpdateSubAreaLevel;
    }

    public void tblSubAreaLimitsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSubAreaLimits.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("spsatCode",
                                 nodeBinding.getAttribute("spsatCode"));

            ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblSubAreaLevels);
        }
    }

    public void clearSubAreaLimitFields() {
        txtSpsatCode.setValue(null);
        txtSpsatSprsaCode.setValue(session.getAttribute("sprsaCode"));
        txtSpsatNoOfLevel.setValue(1); // Default is 1
        txtSpsatMinLimit.setValue(null);
        txtSpsatMaxLimit.setValue(null);

        btnSaveUpdateSubAreaLimit.setText("Save");
    }

    public String actionNewSubAreaLimit() {
        if (session.getAttribute("sprsaCode") != null) {
            clearSubAreaLimitFields();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:subAreaLimit" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Sub Area to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditSubAreaLimit() {
        Object key2 = tblSubAreaLimits.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpsatCode.setValue(nodeBinding.getAttribute("spsatCode"));
            txtSpsatSprsaCode.setValue(nodeBinding.getAttribute("spsatSprsaCode"));
            txtSpsatNoOfLevel.setValue(nodeBinding.getAttribute("spsatNoOfLevel"));
            txtSpsatMinLimit.setValue(nodeBinding.getAttribute("spsatMinLimit"));
            txtSpsatMaxLimit.setValue(nodeBinding.getAttribute("spsatMaxLimit"));

            btnSaveUpdateSubAreaLimit.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:subAreaLimit" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteSubAreaLimit() {
        Object key2 = tblSubAreaLimits.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("spsatCode").toString();
            String sprsaCode =
                nodeBinding.getAttribute("spsatSprsaCode").toString();
            String noOfLevel = null;
            String minLimit = null;
            String maxLimit = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPrcssSubAreaLmts_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LMTS_TAB",
                                                     conn);
                ArrayList limitsList = new ArrayList();
                SysProcessSubAreaLimit subAreaLimit =
                    new SysProcessSubAreaLimit();
                subAreaLimit.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LMTS_OBJ");

                subAreaLimit.setSpsatCode(code == null ? null :
                                          new BigDecimal(code));
                subAreaLimit.setSpsatSprsaCode(sprsaCode == null ? null :
                                               new BigDecimal(sprsaCode));
                subAreaLimit.setSpsatNoOfLevel(noOfLevel == null ? null :
                                               new BigDecimal(noOfLevel));
                subAreaLimit.setSpsatMinLimit(minLimit == null ? null :
                                              new BigDecimal(minLimit));
                subAreaLimit.setSpsatMaxLimit(maxLimit == null ? null :
                                              new BigDecimal(maxLimit));

                limitsList.add(subAreaLimit);
                ARRAY array =
                    new ARRAY(descriptor, conn, limitsList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                session.removeAttribute("spsatCode");

                ADFUtils.findIterator("fetchSysProcessSubAreaLimitsIterator").executeQuery();
                GlobalCC.refreshUI(tblSubAreaLimits);

                ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblSubAreaLevels);

                clearSubAreaLimitFields();

                String message = "Record DELETED Successfully!";
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

    public String actionSaveUpdateSubAreaLimit() {
        if (btnSaveUpdateSubAreaLimit.getText().equals("Edit")) {
            actionUpdateSubAreaLimit();
        } else {
            String code = GlobalCC.checkNullValues(txtSpsatCode.getValue());
            String sprsaCode =
                GlobalCC.checkNullValues(txtSpsatSprsaCode.getValue());
            String noOfLevel =
                GlobalCC.checkNullValues(txtSpsatNoOfLevel.getValue());
            String minLimit =
                GlobalCC.checkNullValues(txtSpsatMinLimit.getValue());
            String maxLimit =
                GlobalCC.checkNullValues(txtSpsatMaxLimit.getValue());

            /*if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;*/

            if (sprsaCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Code is Empty");
                return null;

            } else if (noOfLevel == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: No. Of Level is Empty");
                return null;

            } else if (minLimit == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Minimum Limit is Empty");
                return null;

            } else if (maxLimit == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Maximum Limit is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn = dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.sysPrcssSubAreaLmts_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LMTS_TAB",
                                                         conn);
                    ArrayList limitsList = new ArrayList();
                    SysProcessSubAreaLimit subAreaLimit =
                        new SysProcessSubAreaLimit();
                    subAreaLimit.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LMTS_OBJ");

                    subAreaLimit.setSpsatCode(code == null ? null :
                                              new BigDecimal(code));
                    subAreaLimit.setSpsatSprsaCode(sprsaCode == null ? null :
                                                   new BigDecimal(sprsaCode));
                    subAreaLimit.setSpsatNoOfLevel(noOfLevel == null ? null :
                                                   new BigDecimal(noOfLevel));
                    subAreaLimit.setSpsatMinLimit(minLimit == null ? null :
                                                  new BigDecimal(minLimit));
                    subAreaLimit.setSpsatMaxLimit(maxLimit == null ? null :
                                                  new BigDecimal(maxLimit));

                    limitsList.add(subAreaLimit);
                    ARRAY array =
                        new ARRAY(descriptor, conn, limitsList.toArray());

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
                                         "fms:subAreaLimit" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchSysProcessSubAreaLimitsIterator").executeQuery();
                    GlobalCC.refreshUI(tblSubAreaLimits);

                    clearSubAreaLimitFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateSubAreaLimit() {
        String code = GlobalCC.checkNullValues(txtSpsatCode.getValue());
        String sprsaCode =
            GlobalCC.checkNullValues(txtSpsatSprsaCode.getValue());
        String noOfLevel =
            GlobalCC.checkNullValues(txtSpsatNoOfLevel.getValue());
        String minLimit =
            GlobalCC.checkNullValues(txtSpsatMinLimit.getValue());
        String maxLimit =
            GlobalCC.checkNullValues(txtSpsatMaxLimit.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (sprsaCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Code is Empty");
            return null;

        } else if (noOfLevel == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: No. Of Level is Empty");
            return null;

        } else if (minLimit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Minimum Limit is Empty");
            return null;

        } else if (maxLimit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Maximum Limit is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPrcssSubAreaLmts_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LMTS_TAB",
                                                     conn);
                ArrayList limitsList = new ArrayList();
                SysProcessSubAreaLimit subAreaLimit =
                    new SysProcessSubAreaLimit();
                subAreaLimit.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LMTS_OBJ");

                subAreaLimit.setSpsatCode(code == null ? null :
                                          new BigDecimal(code));
                subAreaLimit.setSpsatSprsaCode(sprsaCode == null ? null :
                                               new BigDecimal(sprsaCode));
                subAreaLimit.setSpsatNoOfLevel(noOfLevel == null ? null :
                                               new BigDecimal(noOfLevel));
                subAreaLimit.setSpsatMinLimit(minLimit == null ? null :
                                              new BigDecimal(minLimit));
                subAreaLimit.setSpsatMaxLimit(maxLimit == null ? null :
                                              new BigDecimal(maxLimit));

                limitsList.add(subAreaLimit);
                ARRAY array =
                    new ARRAY(descriptor, conn, limitsList.toArray());

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
                                     "fms:subAreaLimit" + "').hide(hints);");

                ADFUtils.findIterator("fetchSysProcessSubAreaLimitsIterator").executeQuery();
                GlobalCC.refreshUI(tblSubAreaLimits);

                clearSubAreaLimitFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void clearSubAreaLevelFields() {
        txtSpsalCode.setValue(null);
        txtSpsalSprsaCode.setValue(session.getAttribute("sprsaCode"));
        txtSpsalSpsatCode.setValue(session.getAttribute("spsatCode"));
        txtSpsalLevel.setValue(null); // Default is 1
        txtSpsalApproverType.setValue(null);
        txtSpsalApproverId.setValue(null);
        txtUsrApproverName.setValue(null);
        btnSaveUpdateSubAreaLevel.setText("Save");
    }

    public String actionNewSubAreaLevel() {
        if (session.getAttribute("spsatCode") != null) {
            clearSubAreaLevelFields();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:subAreaLevel" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Sub Area Limit to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditSubAreaLevel() {
        Object key2 = tblSubAreaLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpsalCode.setValue(nodeBinding.getAttribute("spsalCode"));
            txtSpsalSprsaCode.setValue(nodeBinding.getAttribute("spsalSprsaCode"));
            txtSpsalSpsatCode.setValue(nodeBinding.getAttribute("spsalSpsatCode"));
            txtSpsalLevel.setValue(nodeBinding.getAttribute("spsalLevel"));
            txtSpsalApproverType.setValue(nodeBinding.getAttribute("spsalApproverType"));
            txtSpsalApproverId.setValue(nodeBinding.getAttribute("spsalApproverId"));
            txtUsrApproverName.setValue(nodeBinding.getAttribute("userName"));
            txtUsrGrpApproverName.setValue(nodeBinding.getAttribute("userName"));
            String approverType =
                txtSpsalApproverType.getValue() != null ? txtSpsalApproverType.getValue().toString() :
                null;
            plamUser.setVisible(false);
            plamUserGroup.setVisible(false);
            plamSysPost.setVisible(false);
            if (approverType != null) {
                if (approverType.equalsIgnoreCase("USER"))
                    plamUser.setVisible(true);
                else if (approverType.equalsIgnoreCase("UGRP"))
                    plamUserGroup.setVisible(true);
                if (approverType.equalsIgnoreCase("POST"))
                    plamSysPost.setVisible(true);
            }

            btnSaveUpdateSubAreaLevel.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:subAreaLevel" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteSubAreaLevel() {
        Object key2 = tblSubAreaLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("spsalCode").toString();
            String sprsaCode =
                nodeBinding.getAttribute("spsalSprsaCode").toString();
            String spsatCode =
                nodeBinding.getAttribute("spsalSpsatCode").toString();
            String level = null;
            String approverType = null;
            String approverId = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPrcssSubAreaLvls_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LVLS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                SysProcessSubAreaLevel subAreaLevel =
                    new SysProcessSubAreaLevel();
                subAreaLevel.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ");

                subAreaLevel.setSpsalCode(code == null ? null :
                                          new BigDecimal(code));
                subAreaLevel.setSpsalSprsaCode(sprsaCode == null ? null :
                                               new BigDecimal(sprsaCode));
                subAreaLevel.setSpsalSpsatCode(spsatCode == null ? null :
                                               new BigDecimal(spsatCode));
                subAreaLevel.setSpsalLevel(level == null ? null :
                                           new BigDecimal(level));
                subAreaLevel.setSpsalApproverType(approverType);
                subAreaLevel.setSpsalApproverId(approverId == null ? null :
                                                new BigDecimal(approverId));

                levelList.add(subAreaLevel);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblSubAreaLevels);

                clearSubAreaLevelFields();

                String message = "Record DELETED Successfully!";
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

    public String actionSaveUpdateSubAreaLevel() {
        if (btnSaveUpdateSubAreaLevel.getText().equals("Edit")) {
            actionUpdateSubAreaLevel();
        } else {
            String code = GlobalCC.checkNullValues(txtSpsalCode.getValue());
            String sprsaCode =
                GlobalCC.checkNullValues(txtSpsalSprsaCode.getValue());
            String spsatCode =
                GlobalCC.checkNullValues(txtSpsalSpsatCode.getValue());
            String level = GlobalCC.checkNullValues(txtSpsalLevel.getValue());
            String approverType =
                GlobalCC.checkNullValues(txtSpsalApproverType.getValue());
            String approverId =
                GlobalCC.checkNullValues(txtSpsalApproverId.getValue());

            /*if (code == null) {
		        GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
		        return null;*/

            if (sprsaCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Code is Empty");
                return null;

            } else if (spsatCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Limit Code is Empty");
                return null;

            } else if (level == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Level is Empty");
                return null;

            } else if (approverType == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Approver Type is Empty");
                return null;

            } else if (approverId == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Approver Id is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn = dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.sysPrcssSubAreaLvls_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LVLS_TAB",
                                                         conn);
                    ArrayList levelList = new ArrayList();
                    SysProcessSubAreaLevel subAreaLevel =
                        new SysProcessSubAreaLevel();
                    subAreaLevel.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ");

                    subAreaLevel.setSpsalCode(code == null ? null :
                                              new BigDecimal(code));
                    subAreaLevel.setSpsalSprsaCode(sprsaCode == null ? null :
                                                   new BigDecimal(sprsaCode));
                    subAreaLevel.setSpsalSpsatCode(spsatCode == null ? null :
                                                   new BigDecimal(spsatCode));
                    subAreaLevel.setSpsalLevel(level == null ? null :
                                               new BigDecimal(level));
                    subAreaLevel.setSpsalApproverType(approverType);
                    subAreaLevel.setSpsalApproverId(approverId == null ? null :
                                                    new BigDecimal(approverId));

                    levelList.add(subAreaLevel);
                    ARRAY array =
                        new ARRAY(descriptor, conn, levelList.toArray());

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
                                         "fms:subAreaLevel" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
                    GlobalCC.refreshUI(tblSubAreaLevels);

                    clearSubAreaLevelFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateSubAreaLevel() {
        String code = GlobalCC.checkNullValues(txtSpsalCode.getValue());
        String sprsaCode =
            GlobalCC.checkNullValues(txtSpsalSprsaCode.getValue());
        String spsatCode =
            GlobalCC.checkNullValues(txtSpsalSpsatCode.getValue());
        String level = GlobalCC.checkNullValues(txtSpsalLevel.getValue());
        String approverType =
            GlobalCC.checkNullValues(txtSpsalApproverType.getValue());
        String approverId =
            GlobalCC.checkNullValues(txtSpsalApproverId.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (sprsaCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Code is Empty");
            return null;

        } else if (spsatCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: SubArea Limit Code is Empty");
            return null;

        } else if (level == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Level is Empty");
            return null;

        } else if (approverType == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Approver Type is Empty");
            return null;

        } else if (approverId == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Approver Id is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPrcssSubAreaLvls_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_PRCSS_SUBAREA_LVLS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                SysProcessSubAreaLevel subAreaLevel =
                    new SysProcessSubAreaLevel();
                subAreaLevel.setSQLTypeName("TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ");

                subAreaLevel.setSpsalCode(code == null ? null :
                                          new BigDecimal(code));
                subAreaLevel.setSpsalSprsaCode(sprsaCode == null ? null :
                                               new BigDecimal(sprsaCode));
                subAreaLevel.setSpsalSpsatCode(spsatCode == null ? null :
                                               new BigDecimal(spsatCode));
                subAreaLevel.setSpsalLevel(level == null ? null :
                                           new BigDecimal(level));
                subAreaLevel.setSpsalApproverType(approverType);
                subAreaLevel.setSpsalApproverId(approverId == null ? null :
                                                new BigDecimal(approverId));

                levelList.add(subAreaLevel);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

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
                                     "fms:subAreaLevel" + "').hide(hints);");

                ADFUtils.findIterator("fetchSysProcessSubAreaLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblSubAreaLevels);

                clearSubAreaLevelFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTblUserGroups(RichTable tblUserGroups) {
        this.tblUserGroups = tblUserGroups;
    }

    public RichTable getTblUserGroups() {
        return tblUserGroups;
    }

    public void setPanelSubAreaLevel(RichPanelBox panelSubAreaLevel) {
        this.panelSubAreaLevel = panelSubAreaLevel;
    }

    public RichPanelBox getPanelSubAreaLevel() {
        return panelSubAreaLevel;
    }

    public void setTxtUsrGrpApproverName(RichInputText txtUsrGrpApproverName) {
        this.txtUsrGrpApproverName = txtUsrGrpApproverName;
    }

    public RichInputText getTxtUsrGrpApproverName() {
        return txtUsrGrpApproverName;
    }

    public void setTblUsersPop(RichTable tblUsersPop) {
        this.tblUsersPop = tblUsersPop;
    }

    public RichTable getTblUsersPop() {
        return tblUsersPop;
    }

    public void setTxtUsrApproverName(RichInputText txtUsrApproverName) {
        this.txtUsrApproverName = txtUsrApproverName;
    }

    public RichInputText getTxtUsrApproverName() {
        return txtUsrApproverName;
    }

    public void setTblSystemPostsPop(RichTable tblSystemPostsPop) {
        this.tblSystemPostsPop = tblSystemPostsPop;
    }

    public RichTable getTblSystemPostsPop() {
        return tblSystemPostsPop;
    }

    public void setTxtSysPostApproverName(RichInputText txtSysPostApproverName) {
        this.txtSysPostApproverName = txtSysPostApproverName;
    }

    public RichInputText getTxtSysPostApproverName() {
        return txtSysPostApproverName;
    }

    public void setPlamUser(RichPanelLabelAndMessage plamUser) {
        this.plamUser = plamUser;
    }

    public RichPanelLabelAndMessage getPlamUser() {
        return plamUser;
    }

    public void setPlamUserGroup(RichPanelLabelAndMessage plamUserGroup) {
        this.plamUserGroup = plamUserGroup;
    }

    public RichPanelLabelAndMessage getPlamUserGroup() {
        return plamUserGroup;
    }

    public void setPlamSysPost(RichPanelLabelAndMessage plamSysPost) {
        this.plamSysPost = plamSysPost;
    }

    public RichPanelLabelAndMessage getPlamSysPost() {
        return plamSysPost;
    }

    public String actionAcceptUserGroup() {
        Object key2 = tblUserGroups.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpsalApproverId.setValue(nodeBinding.getAttribute("usrCode"));
            txtUsrGrpApproverName.setValue(nodeBinding.getAttribute("usrName"));
        }

        GlobalCC.refreshUI(panelSubAreaLevel);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:usersByGroupPop" + "').hide(hints);");
        return null;
    }

    public String actionShowUsersByGroup() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:usersByGroupPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptUser() {
        Object key2 = tblUsersPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpsalApproverId.setValue(nodeBinding.getAttribute("usrCode"));
            txtUsrApproverName.setValue(nodeBinding.getAttribute("usrName"));
        }

        GlobalCC.refreshUI(panelSubAreaLevel);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:usersByUserTypePop" + "').hide(hints);");
        return null;
    }

    public String actionShowUsersByUser() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:usersByUserTypePop" + "').show(hints);");
        return null;
    }

    public String actionAcceptSystemPost() {
        Object key2 = tblSystemPostsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpsalApproverId.setValue(nodeBinding.getAttribute("spostCode"));
            txtSysPostApproverName.setValue(nodeBinding.getAttribute("spostDesc"));
        }

        GlobalCC.refreshUI(panelSubAreaLevel);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:systemPostsPop" + "').hide(hints);");
        return null;
    }

    public String actionShowSysPosts() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:systemPostsPop" + "').show(hints);");
        return null;
    }

    public void approverTypeChangeListener(ValueChangeEvent valueChangeEvent) {
        String newApproverType = null;
        if (valueChangeEvent.getNewValue() != null) {
            newApproverType = valueChangeEvent.getNewValue().toString();

            if (newApproverType.equals("POST")) {
                plamSysPost.setVisible(true);
                plamUser.setVisible(false);
                plamUserGroup.setVisible(false);

            } else if (newApproverType.equals("RANK")) {
                // The Approver ID will be null
                plamSysPost.setVisible(false);
                plamUser.setVisible(false);
                plamUserGroup.setVisible(false);

            } else if (newApproverType.equals("USER")) {
                plamSysPost.setVisible(false);
                plamUser.setVisible(true);
                plamUserGroup.setVisible(false);

            } else if (newApproverType.equals("UGRP")) {
                plamSysPost.setVisible(false);
                plamUser.setVisible(false);
                plamUserGroup.setVisible(true);

            }

            GlobalCC.refreshUI(panelSubAreaLevel);
        }
    }

    public void setMainPanel(RichPanelTabbed mainPanel) {
        this.mainPanel = mainPanel;
    }

    public RichPanelTabbed getMainPanel() {
        return mainPanel;
    }

    public void setMainLay(RichPanelGroupLayout mainLay) {
        this.mainLay = mainLay;
    }

    public RichPanelGroupLayout getMainLay() {
        return mainLay;
    }

    public void actionConfirmDropRole(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            if (session.getAttribute("systemRoleCode") != null) {
                actionDropSystemRole();

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected");
            }
        }
    }

    public String actionShowDropRole() {
        String roleCode = GlobalCC.checkNullValues(session.getAttribute("roleCode"));
        if (roleCode!= null) {
            
            DBConnector datahandler = null;
            datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            
            /* ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:confirmDeleteRole" + "').show(hints);"); */
            
            try{
           
            String query;
            query ="UPDATE TQC_SYS_ROLES SET SRLS_VISIBLE='N' WHERE SRLS_CODE = :v_role_code";
            
            query=query.replaceAll(":v_role_code", roleCode);
            System.out.println(query);
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.executeUpdate();
            cst.close();
            conn.commit();
            conn.close();
                
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            
            GlobalCC.INFORMATIONREPORTING("Role Successfully Dropped!");
            ADFUtils.findIterator("findSystemRolesIterator").executeQuery();
            GlobalCC.refreshUI(rolesTree);
        } else {
            
            GlobalCC.INFORMATIONREPORTING("No Role Selected!");
        }
        return null;
    }

    public void setProcessSubArea(RichTable processSubArea) {
        this.processSubArea = processSubArea;
    }

    public RichTable getProcessSubArea() {
        return processSubArea;
    }

    public void selectProcessSubArea(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            JUCtrlHierNodeBinding nd =
                (JUCtrlHierNodeBinding)processAreas.getRowData();
            session.setAttribute("processCode",
                                 nd.getRow().getAttribute("processCode"));
            session.setAttribute("processArea",
                                 nd.getRow().getAttribute("processArea"));
            session.setAttribute("processAreaCode",
                                 nd.getRow().getAttribute("processAreaCode"));
        }
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
    }

    public void setRowChecked(RichSelectBooleanCheckbox rowChecked) {
        this.rowChecked = rowChecked;
    }

    public RichSelectBooleanCheckbox getRowChecked() {
        return rowChecked;
    }

    public void subAreaSelected(ValueChangeEvent valueChangeEvent) {
        boolean selected = false;
        Object key = processSubArea.getSelectedRowData();
        JUCtrlValueBinding row = (JUCtrlValueBinding)key;
        if (row == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a role");
            return;
        }
        selected = (Boolean)row.getAttribute("processSubAreaSelected");
        if (selected) {
            row.setAttribute("processSubAreaSelected", false);

        } else {
            row.setAttribute("processSubAreaSelected", true);

        }
        //     if((boolean)row.getAttribute("processSubAreaSelected")==true)
        //     {
        //           rowChecked.setSelected(true);
        //     }else{
        //           rowChecked.setSelected(false);
        //         }
        //       GlobalCC.refreshUI(rowChecked);

    }
    public void subAreaSelectedNew(ValueChangeEvent valueChangeEvent) {
        
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        String action;
        
        boolean selected = false;
        Object key = processSubArea.getSelectedRowData();
        
        
        JUCtrlValueBinding row = (JUCtrlValueBinding)key;
        
        
        BigDecimal processArea = GlobalCC.checkBDNullValues(session.getAttribute("processAreaCode"));
        BigDecimal processSubAreaCode = GlobalCC.checkBDNullValues(row.getAttribute("processSubAreaCode"));
        BigDecimal role = GlobalCC.checkBDNullValues(session.getAttribute("roleCode"));
        BigDecimal process = GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
        BigDecimal debitLimitVal= GlobalCC.checkBDNullValues(row.getAttribute("processSubAreaDebitLimit"));
        BigDecimal creditLimitVal= GlobalCC.checkBDNullValues(row.getAttribute("processSubAredCreditLimit"));
        String processChecked =  GlobalCC.checkNullValues(session.getAttribute("processChecked"));
        String processAreaChecked =  GlobalCC.checkNullValues(session.getAttribute("processAreaChecked"));
        
        System.out.println("processSubAreaDebitLimit:::"+row.getAttribute("processSubAreaDebitLimit"));
        System.out.println("processSubAredCreditLimit:::"+ row.getAttribute("processSubAredCreditLimit"));
        
        if (row == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a role");
            return;
        }
        
        if(role==null){
            GlobalCC.INFORMATIONREPORTING("Please Select Role!");
            return;
            }
        if(process==null||processChecked.equalsIgnoreCase("false")){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process First!");
            return;
            }
        if(processArea==null||processAreaChecked.equalsIgnoreCase("false")){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process Area First!");
            return;
            }
        if(processSubAreaCode==null){
            GlobalCC.INFORMATIONREPORTING("Please Grant SubArea First!");
            return;
            }
        
        /*selected = (Boolean)row.getAttribute("processSubAreaSelected");
        if (selected) {
            row.setAttribute("processSubAreaSelected", false);

        } else {
            row.setAttribute("processSubAreaSelected", true);

        }*/
        
        if(rowChecked.isSelected())
        {
            action="A";
            }
        else
        {
            action="U";
            }
            
        
        try {
            String authorizeQuery ="begin tqc_roles_pkg.authorize_role_sub_areas(?,?,?,?,?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(authorizeQuery);
            cst.setBigDecimal(1, processSubAreaCode);
            cst.setBigDecimal(2, processArea);
            cst.setBigDecimal(3, process);
            cst.setBigDecimal(4, role);
            cst.setBigDecimal(5, debitLimitVal);
            cst.setBigDecimal(6, creditLimitVal);
            cst.setString(7,action);
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        
        unAuthorizeRole();
        
        System.out.println("processSubAreaDebitLimit:::"+row.getAttribute("processSubAreaDebitLimit"));
        System.out.println("processSubAredCreditLimit:::"+ row.getAttribute("processSubAredCreditLimit"));
        
        
        
        //     if((boolean)row.getAttribute("processSubAreaSelected")==true)
        //     {
        //           rowChecked.setSelected(true);
        //     }else{
        //           rowChecked.setSelected(false);
        //         }
        //       GlobalCC.refreshUI(rowChecked);

    }
    public void selectTreeData(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    processAreas.setRowKey(treeRowKey);
        JUCtrlHierNodeBinding nd =  (JUCtrlHierNodeBinding)processAreas.getRowData();
        session.setAttribute("processCode",nd.getRow().getAttribute("processCode"));
        session.setAttribute("processChecked", processCheck.getValue());            
                  
                }
            }
        }
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
        ADFUtils.findIterator("findSystemRoleAreasIterator").executeQuery();
        GlobalCC.refreshUI(processAreasTbl);
        System.out.println("This is working"+processCheck.getValue());
    }
    public void selectProcessAreaData(SelectionEvent selectionEvent) {
        Object key = processAreasTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        //       session.setAttribute("processCode",
        //                           r.getAttribute("processCode"));
        System.out.println("rowSelect.getValue()"+r.getAttribute("processAreaSelected"));
        session.setAttribute("processAreaChecked", r.getAttribute("processAreaSelected"));
        session.setAttribute("processArea", r.getAttribute("processArea"));
        session.setAttribute("processAreaCode", r.getAttribute("processAreaCode"));
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
    }
    public void populateSysRoles(ValueChangeEvent valueChangeEvent){
            try{
                BigDecimal sysCode=GlobalCC.checkBDNullValues(populatedSystems.getValue());
                session.setAttribute("sysCode",sysCode);
                session.removeAttribute("roleCode");
                session.removeAttribute("processAreaCode");
                session.removeAttribute("processCode");
                ADFUtils.findIterator("findSystemRolesIterator").executeQuery();
                GlobalCC.refreshUI(rolesTree);
            }catch(Exception e){
                GlobalCC.EXCEPTIONREPORTING(e);
            }  
        }

    public void areaSelected(ValueChangeEvent valueChangeEvent) {
        Object key = processAreasTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        //       session.setAttribute("processCode",
        //                           r.getAttribute("processCode"));
        session.setAttribute("processArea", r.getAttribute("processArea"));
        session.setAttribute("processAreaCode",
                             r.getAttribute("processAreaCode"));
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
    }
    
    public void areaSelectedNew(ValueChangeEvent valueChangeEvent) {
        Object key = processAreasTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        
        BigDecimal processArea = GlobalCC.checkBDNullValues(r.getAttribute("processAreaCode"));
        BigDecimal role = GlobalCC.checkBDNullValues(session.getAttribute("roleCode"));
        BigDecimal process = GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
        String processChecked =  GlobalCC.checkNullValues(session.getAttribute("processChecked"));
        String action;
        
        System.out.println("processCheck.isSelected()"+processCheck.isSelected());
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected!");
            return;
        }
        if(role==null){
            GlobalCC.INFORMATIONREPORTING("Please Select Role!");
            return;
            }
        if(process==null||processChecked.equalsIgnoreCase("false")){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process First!");
            return;
            }
        if(processArea==null){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process Area First!");
            return;
            }
        if(rowSelect.isSelected())
        {
            action="A";
            }
        else
        {
            action="U";
            GlobalCC.showPopup("fms:popRevokeArea");
            return ;
        }
            
        
        try {
            String authorizeQuery ="begin tqc_roles_pkg.authorize_role_processes_areas(?,?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(authorizeQuery);
            cst.setBigDecimal(1, processArea);
            cst.setBigDecimal(2, process);
            cst.setBigDecimal(3, role);
            cst.setString(4,action);
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
            
            unAuthorizeRole();
            System.out.println("Process Selected:::"+r.getAttribute("processAreaCode"));
            System.out.println("Process Checked:::"+ rowSelect.isSelected());
            
            
        //       session.setAttribute("processCode",
        //                           r.getAttribute("processCode"));
        session.setAttribute("processAreaChecked", rowSelect.isSelected());
        session.setAttribute("processArea", r.getAttribute("processArea"));
        session.setAttribute("processAreaCode",r.getAttribute("processAreaCode"));
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
    }

    public void setProcessAreasTbl(RichTable processAreasTbl) {
        this.processAreasTbl = processAreasTbl;
    }

    public RichTable getProcessAreasTbl() {
        return processAreasTbl;
    }

    public void processSelected(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            JUCtrlHierNodeBinding nd =
                (JUCtrlHierNodeBinding)processAreas.getRowData();
            session.setAttribute("processCode",
                                 nd.getRow().getAttribute("processCode"));
        }
        ADFUtils.findIterator("findSystemRoleAreasIterator").executeQuery();
        GlobalCC.refreshUI(processAreasTbl);
    }
    
    public void processSelectedNew(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        
        JUCtrlHierNodeBinding nd = (JUCtrlHierNodeBinding)processAreas.getRowData();
        BigDecimal process = GlobalCC.checkBDNullValues(nd.getRow().getAttribute("processCode")); 
        BigDecimal role = GlobalCC.checkBDNullValues(session.getAttribute("roleCode")); 
        String action;
        
            
        if (nd == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        if(role==null){
            GlobalCC.INFORMATIONREPORTING("Please select Role first");
            return;
            }
        
        if(processCheck.isSelected())
        {
            action="A";
        }
        else
         {
            action = "U";
            GlobalCC.showPopup("fms:popRevokeProcess");
            return;
         } 
         changeProcessAuth(action, process,  role) ;
    }



    public String grantProcessAreaRoles() {
        JUCtrlValueBinding r = null;
        OracleConnection conn = null;
        int rowCount2 = processAreasTbl.getRowCount();
        int count = 0;
        System.out.println("this is row count" + rowCount2);
        for (int i = 0; i < rowCount2; i++) {
            Boolean Accept = false;
            r = (JUCtrlValueBinding)processAreasTbl.getRowData(i);
            Accept = (Boolean)r.getAttribute("processAreaSelected");
            if (Accept)
                count++;
        }

        try {
            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)processAreasTbl.getRowData(i);
                if (r == null)
                    continue;
                Accept = (Boolean)r.getAttribute("processAreaSelected");
                if (Accept) {

                    grantRoleProcessArea((BigDecimal)r.getAttribute("processAreaCode"),
                                         (BigDecimal)r.getAttribute("processCode"));

                } else {

                    revokeRoleProcessArea((BigDecimal)r.getAttribute("processAreaCode"),
                                          (BigDecimal)r.getAttribute("processCode"));

                }
            }
            unAuthorizeRole();
            ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
            GlobalCC.refreshUI(treeSystemRoles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String grantRevokeProcess() {
        JUCtrlValueBinding r = null;
        OracleConnection conn = null;
        int rowCount2 = processAreas.getRowCount();
        int count = 0;
       
        System.out.println("this is row count" + rowCount2);
        for (int i = 0; i < rowCount2; i++) {
            Boolean Accept = false;
            r = (JUCtrlValueBinding)processAreas.getRowData(i);
            Accept = (Boolean)r.getAttribute("processSelected");
            if (Accept)
                count++;
        }

        try {
            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)processAreas.getRowData(i);
                if (r == null)
                    continue;
                Accept = (Boolean)r.getAttribute("processSelected");
                if (Accept) { 

                } else { 
                    revokeRoleProcess((BigDecimal)r.getAttribute("processRoleCode")); 
                }
                
            }
            unAuthorizeRole();
          
            ADFUtils.findIterator("findSystemsIterator").executeQuery(); 
            GlobalCC.refreshUI(treeSystemRoles);
          
            ADFUtils.findIterator("findSystemRoleAreasIterator").executeQuery();
            GlobalCC.refreshUI(processAreasTbl);
            
            ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
            GlobalCC.refreshUI(processSubArea);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String revokeRoleProcess( BigDecimal processRoleCode) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.revoke_role_process(?); end;";
          

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1,processRoleCode); 
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
  public String unAuthorizeRole() {
      DBConnector datahandler = new DBConnector();
      OracleConnection conn;
      conn = datahandler.getDatabaseConnection();
      OracleCallableStatement cst = null;
      
      try {
          BigDecimal roleCode=GlobalCC.checkBDNullValues(session.getAttribute("systemRoleCode"));
          if(roleCode!=null)
          {
             String query =
              "UPDATE tqc_sys_roles\n" + 
              "                 SET srls_authorized = 'N'\n" + 
              "           WHERE srls_code = :v_role_code ";  
              query=query.replaceAll(":v_role_code",roleCode.toString());
              System.out.println("query: "+query);
              cst = (OracleCallableStatement)conn.prepareCall(query); 
              cst.execute(); 
              conn.commit(); 
          } 
      } catch (Exception e) {
          GlobalCC.EXCEPTIONREPORTING(conn, e);
      }finally
      {
        DbUtils.closeQuietly(conn, cst, null);
      }
      return null;
  }
    private List<SelectItem> roleSystem = new ArrayList<SelectItem>();
    public List<SelectItem> getRoleSystem() {
        
        if (roleSystem != null) {
            roleSystem.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        String status = null;
        Boolean value = true;
        try {
            conn = dbConnector.getDatabaseConnection(); 
            String query1 = "SELECT sys_code, sys_sht_desc, sys_name, sys_active\n" + 
            "                FROM tqc_systems\n" + 
            "                WHERE sys_active <> 'N'";
            System.out.println("query1: "+query1 );
            stmt = (OracleCallableStatement)conn.prepareCall(query1); 
            rst = (OracleResultSet)stmt.executeQuery();
            while (rst.next()) {
                roleSystem.add(new SelectItem(rst.getString(1),rst.getString(3)));
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return roleSystem;
    }

  public void setBtnAuthorizeSystemRole(RichCommandButton btnAuthorizeSystemRole)
  {
    this.btnAuthorizeSystemRole = btnAuthorizeSystemRole;
  }

  public RichCommandButton getBtnAuthorizeSystemRole()
  {
    return btnAuthorizeSystemRole;
  }

  public void setTxtSysRoleAuthorized(RichInputText txtSysRoleAuthorized)
  {
    this.txtSysRoleAuthorized = txtSysRoleAuthorized;
  }

  public RichInputText getTxtSysRoleAuthorized()
  {
    return txtSysRoleAuthorized;
  }

    public void setRowSelect(RichSelectBooleanCheckbox rowSelect) {
        this.rowSelect = rowSelect;
    }

    public RichSelectBooleanCheckbox getRowSelect() {
        return rowSelect;
    }

    public void setRolesDetailsPopUp(RichPopup rolesDetailsPopUp) {
        this.rolesDetailsPopUp = rolesDetailsPopUp;
    }

    public RichPopup getRolesDetailsPopUp() {
        return rolesDetailsPopUp;
    }

    public void setPopulatedSystems(RichSelectOneChoice populatedSystems) {
        this.populatedSystems = populatedSystems;
    }

    public RichSelectOneChoice getPopulatedSystems() {
        return populatedSystems;
    }

    public void setRolesTree(RichTree rolesTree) {
        this.rolesTree = rolesTree;
    }

    public RichTree getRolesTree() {
        return rolesTree;
    }
    public void confirmRevokeProc(DialogEvent dialogEvent) {
        session.setAttribute("revoke", null);
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) { 
            session.setAttribute("revoke", "N"); 
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {  
            BigDecimal process = GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
            BigDecimal role = GlobalCC.checkBDNullValues(session.getAttribute("roleCode")); 
            changeProcessAuth("U", process,  role) ;
        }
    }
    public void confirmRevokeAreas(DialogEvent dialogEvent) {
        session.setAttribute("revoke", null);
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) { 
            session.setAttribute("revoke", "N"); 
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {   
            changeAreaAuth("U") ;
        }
    }
    public void changeProcessAuth(String action,BigDecimal process, BigDecimal role) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        
        if(role==null){
            GlobalCC.INFORMATIONREPORTING("Please select Role first");
            return;
        }
   
       
        try {
            String authorizeQuery ="begin tqc_roles_pkg.authorize_role_processes(?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(authorizeQuery);
            cst.setBigDecimal(1, process);
            cst.setBigDecimal(2, role);
            cst.setString(3,action);
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        
            unAuthorizeRole();
            
            session.setAttribute("processCode",process);
            session.setAttribute("processChecked", processCheck.isSelected());
            System.out.println("Process Selected:::"+process);
            System.out.println("Process Checked:::"+ processCheck.isSelected());
            
            ADFUtils.findIterator("findSystemRoleAreasIterator").executeQuery();
            GlobalCC.refreshUI(processAreasTbl);
            ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
            GlobalCC.refreshUI(processSubArea);
    }
    public void changeAreaAuth(String action) {
    
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        
        BigDecimal processArea = GlobalCC.checkBDNullValues(session.getAttribute("processAreaCode"));
        BigDecimal role = GlobalCC.checkBDNullValues(session.getAttribute("roleCode"));
        BigDecimal process = GlobalCC.checkBDNullValues(session.getAttribute("processCode"));
        String processChecked =  GlobalCC.checkNullValues(session.getAttribute("processChecked"));
        
       
        if(role==null){
            GlobalCC.INFORMATIONREPORTING("Please Select Role!");
            return;
            }
        if(process==null||processChecked.equalsIgnoreCase("false")){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process First!");
            return;
            }
        if(processArea==null){
            GlobalCC.INFORMATIONREPORTING("Please Grant Process Area First!");
            return;
            }
        
        try {
            String authorizeQuery ="begin tqc_roles_pkg.authorize_role_processes_areas(?,?,?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(authorizeQuery);
            cst.setBigDecimal(1, processArea);
            cst.setBigDecimal(2, process);
            cst.setBigDecimal(3, role);
            cst.setString(4,action);
            cst.execute();

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
            
        unAuthorizeRole();
        System.out.println("Process Selected:::"+processArea);
        System.out.println("Process Checked:::"+ rowSelect.isSelected());
            
            
        // session.setAttribute("processCode",
        //  r.getAttribute("processCode"));
        session.setAttribute("processAreaChecked", rowSelect.isSelected()); ;
        ADFUtils.findIterator("findSystemProcessAreasIterator").executeQuery();
        GlobalCC.refreshUI(processSubArea);
    }
}
