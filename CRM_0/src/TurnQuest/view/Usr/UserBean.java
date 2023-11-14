package TurnQuest.view.Usr;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Branches.Branch;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.UserSystem;
import TurnQuest.view.orgs.OrganizationDAO;

import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.bean.DCDataRow;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
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
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class UserBean {
    Rendering renderer = new Rendering();
    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();

    private List<SelectItem> selectValues2 = new ArrayList<SelectItem>();
    private List<String> displayValue2 = new ArrayList<String>();
    private List<SelectItem> securityQuestion = new ArrayList<SelectItem>();
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable users;
    private RichSelectManyShuttle userSystems;
    private RichSelectManyShuttle userRoles;
    private RichInputText sysUsername;
    private RichInputText sysFullname;
    private RichInputText sysEmail;
    private RichInputText sysPassword;
    private RichInputText sysPersonelRank;
    private RichSelectOneChoice sysStatus;
    private RichSelectOneChoice sysType;
    private RichCommandButton editBtn;
    private RichCommandButton createBtn;
    private UISelectItems userSystemsSelect;
    private UISelectItems userRolesSelect;
    private RichSelectBooleanCheckbox sysPassReset;
    private RichTable userSystemDates;
    private RichInputText sysWetSystemName;
    private RichInputDate sysWef;
    private RichInputDate sysWet;
    private RichInputText organizationDesc;
    private RichTable tblOrganizations;
    private RichTable tblAllBranches;
    private RichInputText regionDesc;
    private RichTable tblRegionsPop;
    private RichTable tblUserBranches;
    private RichTable systemsLOV;
    private RichInputText systemDesc;
    private RichTable tblAllSysRoles;
    private RichTable tblAllUsrRoles;
    private RichTable tblAllDivisions;
    private RichTable tblUserDivisions;
    private RichTree treeUnassignedSystems;
    private RichTree treeAssignedSystems;
    private RichCommandButton btnAddUserSystem;
    private RichCommandButton btnRemoveUserSystem;
    private RichInputText txtSelectedUserSystemCode;
    private RichShowDetailItem detailSystems;
    private RichPanelBox panelDetailSystems;
    private RichPanelBox panelUserRoles;
    private RichInputText txtSelectedUserRoleCode;
    private RichCommandButton btnAddUserRole;
    private RichCommandButton btnRemoveUseRole;
    private RichTree treeAssignedRoles;
    private RichTree treeUnassignedRoles;
    private RichPanelBox panelDetailBranches;
    private RichInputText txtSelectedUserBranchCode;
    private RichTree treeUnassignedBranches;
    private RichTree treeAssignedBranches;
    private RichPanelBox panelDetailDivisions;
    private RichInputText txtSelectedUserDivisionCode;
    private RichTree treeUnassignedDivisions;
    private RichTree treeAssignedDivisions;
    private RichInputText txtUserDefaultBranch;
    private RichInputText txtUserDefaultDivision;
    private RichPanelBox panelDefaultBranch;
    private RichPanelBox panelDefaultDivision;
    private RichPanelBox pbxMainPanel;
    private RichTable tblUserBranchesPop;
    private RichTable tblUserDivisionsPop;
    private RichInputText sysConfirmPassword;
    private RichOutputLabel errorDisplayTxt;
    private RichTable tblPersonnel;
    private RichInputText txtPersonnelId;
    private RichInputText txtPersonnelName;
    private RichInputText txtSelectedUserRegionCode;
    private RichTable tblAssignedSystems;
    private RichInputText txtUsysCode;
    private RichInputText txtUsysUsrCode;
    private RichInputText txtUsysSysCode;
    private RichInputDate txtUsysWef;
    private RichInputDate txtUsysWet;
    private RichInputText txtUsysSpostCode;
    private RichInputText txtSpostDesc;
    private RichCommandButton btnSaveUpdateUserSystem;
    private RichPanelBox panelUserSystem;
    private RichTable tblSystemPostsPop;
    private RichTable tblGroup;
    private RichTable tblGroupMembers;
    private RichCommandButton btnNewMember;
    private RichCommandButton btnDeleteGroupMember;
    private RichTable tbAddUserToGroup;
    private RichInputText txtSelectedUserOrgCode;
    private RichInputText txtSelectedUserSysCode;
    private RichSelectBooleanCheckbox branchSelected;
    private RichSelectBooleanCheckbox branchToBeRemoved;
    private RichSelectBooleanCheckbox roleToBeAdded;
    private RichSelectBooleanCheckbox roleToBeRemoved;
    private RichInputText txtUserDefaultBranchCode;
    private RichColumn chkUserSelected;
    private RichSelectBooleanCheckbox chkUserColSelected;
    private RichSelectBooleanCheckbox userToRemoveSelected;
    private RichPanelTabbed pnTabbedUserAccess;
    private RichSelectBooleanCheckbox divToBeAdded;
    private RichSelectBooleanCheckbox divToBeRemoved;
    private RichSelectOneChoice txtAccManager;
    private UploadedFile UploadedImageFile;
    private RichInputFile userSignature;
    private UploadedFile signatureFile;
    private RichImage userSignImage;
    private String filename;
    private long filesize;
    private static String fileContent;
    private static InputStream fileStream;
    private RichInputText txtPhoneNumber;
    private RichInputDate txtWefdate;
    private RichInputDate txtWetDate;
    private RichSelectOneChoice txtSecurityQuestions;
    private RichInputText txtSecurityAnswer;
    private RichTable usersTable;
    private RichCommandButton ldapUser;
    private RichInputText txtSactCode;
    private RichInputText txtSactName;
    private RichInputText txtPin;
    private RichCommandButton txtSelectSubAccount;
    private RichTable tblSubAcctTypes;
    private RichInputText txtUserAuthorized;
    private RichOutputLabel lblWefdate;

    private RichTable tblUserGrpType;
    private RichTable tblUserGroupTypeMember;
    private RichCommandButton btnNewUserGrpMember;
    private RichTable tblAddUserToGroupType;
    private RichSelectBooleanCheckbox userToRemoveSelection;
    private RichSelectBooleanCheckbox chkSelectedUser;
    private RichShowDetailItem usersDetailItempanel;
    private RichShowDetailItem accountsDetailItempanel;
    private RichTable tblAccountTypesPop;
    private RichInputText txtAccountTypeCode;
    private RichTable tblAgencyInfo;
    private RichTable tblGroupAgencies;
    private RichSelectBooleanCheckbox chkSelectedAcount;
    private RichSelectBooleanCheckbox accountToRemoveSelection;
    private RichTable tblGroupTypeBranches;
    private RichInputText txtGroupTypeBranch;
    private RichTable tblGroupTypes;
    private RichInputText txtGroupTypeName;
    private RichInputText txtGroupShtDesc;
    private RichTable tblUserGroupTypes;
    private RichCommandButton btnEditUserInGroup;
    private RichTable tblSelectTeamLeader;
    private RichCommandButton btnSaveGroupTypes;


    private RichShowDetailItem KPIDetailItempanel;
    private RichInputText kpiTask;
    private RichInputText kpiSubTask;
    private RichInputText kpiParam;
    private RichSelectOneChoice kpiUnit;
   
    private RichInputText kpiComment;
    private RichCommandButton createKpiBtn;
    private RichCommandButton cancelKPIDetails;
    private RichPopup KPITaskLovPopUp;

    private RichCommandButton btnSelectTask;
    private RichCommandButton btnSelectTask2;
    private RichPopup KPITaskLovPopUpS;
    private RichCommandButton btnSelectTasks;
    private RichCommandButton btnSelectTask2s;
    private RichTable tblSubTask;
    private RichTable tblTask;
    private RichTable tblKpiTab;
   

    public UserBean() {

        // GlobalCC.refreshUI(userSystems);
        if (session.getAttribute("sysUserCode") == null) {
            UserSystemsValues();
        }
    }


    public void setUsers(RichTable users) {
        this.users = users;
    }

    public RichTable getUsers() {
        return users;
    }

    public void setSysUsername(RichInputText sysUsername) {
        this.sysUsername = sysUsername;
    }

    public RichInputText getSysUsername() {
        return sysUsername;
    }

    public void setSysFullname(RichInputText sysFullname) {
        this.sysFullname = sysFullname;
    }

    public RichInputText getSysFullname() {
        return sysFullname;
    }

    public void setSysEmail(RichInputText sysEmail) {
        this.sysEmail = sysEmail;
    }

    public RichInputText getSysEmail() {
        return sysEmail;
    }

    public void setSysPassword(RichInputText sysPassword) {
        this.sysPassword = sysPassword;
    }

    public RichInputText getSysPassword() {
        return sysPassword;
    }

    public void setSysPersonelRank(RichInputText sysPersonelRank) {
        this.sysPersonelRank = sysPersonelRank;
    }

    public RichInputText getSysPersonelRank() {
        return sysPersonelRank;
    }

    public void setSysStatus(RichSelectOneChoice sysStatus) {
        this.sysStatus = sysStatus;
    }

    public RichSelectOneChoice getSysStatus() {
        return sysStatus;
    }

    public void setSysType(RichSelectOneChoice sysType) {
        this.sysType = sysType;
    }

    public RichSelectOneChoice getSysType() {
        return sysType;
    }

    public void setUserSystems(RichSelectManyShuttle userSystems) {
        this.userSystems = userSystems;
    }

    public RichSelectManyShuttle getUserSystems() {
        return userSystems;
    }

    public void setEditBtn(RichCommandButton editBtn) {
        this.editBtn = editBtn;
    }

    public RichCommandButton getEditBtn() {
        return editBtn;
    }

    public void setCreateBtn(RichCommandButton createBtn) {
        this.createBtn = createBtn;
    }

    public RichCommandButton getCreateBtn() {
        return createBtn;
    }


    public String refreshAndfetchData() {

        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedUserSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedSystems);

        ADFUtils.findIterator("fetchAllUserAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(tblAssignedSystems);

        //UserSystemsValues();
        //GlobalCC.refreshUI(userSystems);
        //ADFUtils.findIterator("findUserSystemsIterator").executeQuery();
        //GlobalCC.refreshUI(userSystemDates);

        UserSystemsRoleValues();
        GlobalCC.refreshUI(userRoles);
        ADFUtils.findIterator("fetchAllSystemRolesIterator").executeQuery();

        // Get the user branches
        ADFUtils.findIterator("fetchUserUnassignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedBranches);
        GlobalCC.refreshUI(treeAssignedBranches);


        // Get the user roles
        ADFUtils.findIterator("fetchUnassignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("fetchAssignedUserRolesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedRoles);
        GlobalCC.refreshUI(treeAssignedRoles);

        // Get the assigned user divisions
        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserNonDefaultDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedDivisions);
        GlobalCC.refreshUI(treeAssignedDivisions);
        GlobalCC.refreshUI(tblUserDivisionsPop);

        // Get the user default branch
        txtUserDefaultBranch.setValue(null);
        String query =
            "begin ? := tqc_roles_cursor.get_default_user_branch(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            while (rs.next()) {
                UtilObject branchObj = new UtilObject();

                branchObj.setOrgCode(rs.getString(1));
                branchObj.setOrgName(rs.getString(2));
                branchObj.setRegionCode(rs.getString(3));
                branchObj.setRegionName(rs.getString(4));
                branchObj.setBranchCode(rs.getString(5));
                branchObj.setBranchName(rs.getString(6));
                txtUserDefaultBranch.setValue(branchObj.getBranchName());
                txtUserDefaultBranchCode.setValue(branchObj.getBranchCode());
            }
            GlobalCC.refreshUI(panelDefaultBranch);

            cst.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        // Get the user default division
        txtUserDefaultDivision.setValue(null);
        String query2 =
            "begin ? := tqc_roles_cursor.get_default_user_division(?); end;";
        OracleCallableStatement cst2 = null;
        DBConnector datahandler2 = new DBConnector();
        OracleConnection conn2 = null;
        try {

            conn2 = datahandler2.getDatabaseConnection();

            cst2 = (OracleCallableStatement)conn2.prepareCall(query2);
            cst2.registerOutParameter(1, OracleTypes.CURSOR);
            cst2.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("sysUserCode"));
            cst2.execute();
            OracleResultSet rset = (OracleResultSet)cst2.getObject(1);

            while (rset.next()) {
                UtilObject divisionObj = new UtilObject();

                divisionObj.setOrgCode(rset.getString(1));
                divisionObj.setOrgName(rset.getString(2));
                divisionObj.setRegionCode(rset.getString(3));
                divisionObj.setRegionName(rset.getString(4));
                divisionObj.setBranchCode(rset.getString(5));
                divisionObj.setBranchName(rset.getString(6));
                divisionObj.setDivisionCode(rset.getString(7));
                divisionObj.setDivisionName(rset.getString(8));
                txtUserDefaultDivision.setValue(divisionObj.getDivisionName());
            }
            GlobalCC.refreshUI(panelDefaultDivision);

            cst2.close();
            rset.close();
            conn2.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn2, e);
        }


        return null;
    }

    public String UserSystemsRoleValues() {
        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {


            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int t = 0;
            while (t < selectValues2.size()) {
                selectValues2.remove(t);
                t++;
            }
            while (rs.next()) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(rs.getBigDecimal(1).toString());
                selectItem.setDescription(rs.getString(2));
                selectItem.setLabel(rs.getString(2));
                selectValues2.add(selectItem);
            }

            rs.close();
            //conn.close();

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
            while (t < displayValue2.size()) {
                displayValue2.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue2.add(rsUser.getBigDecimal(7).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        if (userRoles == null) {
        } else {

            userRoles.setValue(displayValue2);
            userRolesSelect.setValue(selectValues2);
        }
        return null;
    }

    public String UserSystemsValues() {
        String query = "begin ? := tqc_roles_cursor.Get_Systems; end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = datahandler.getDatabaseConnection();

        try {

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
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
                selectItem.setLabel(rs.getString(3));
                selectValues.add(selectItem);
            }

            rs.close();
            //conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String userQuery =
            "begin ? := tqc_roles_cursor.Get_User_Systems(?); end;";
        OracleCallableStatement cstUsr = null;
        try {

            cstUsr = (OracleCallableStatement)conn.prepareCall(userQuery);
            cstUsr.registerOutParameter(1,
                                        OracleTypes.CURSOR); //authorization code
            cstUsr.setBigDecimal(2,
                                 (BigDecimal)session.getAttribute("sysUserCode"));

            cstUsr.execute();
            OracleResultSet rsUser = (OracleResultSet)cstUsr.getObject(1);
            int t = 0;
            while (t < displayValue.size()) {
                displayValue.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue.add(rsUser.getBigDecimal(2).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        if (userSystems == null) {
        } else {

            userSystems.setValue(displayValue);
            userSystemsSelect.setValue(selectValues);
        }
        return null;
    }

    public String UserSystemsRoles() {
        String query = "begin ? := tqc_roles_cursor.Get_Sys_Roles(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = datahandler.getDatabaseConnection();

        try {

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("sysCode"));
            cst.execute();
            OracleResultSet rs = (OracleResultSet)cst.getObject(1);
            int t = 0;
            while (t < selectValues2.size()) {
                selectValues2.remove(t);
                t++;
            }
            while (rs.next()) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(rs.getBigDecimal(1).toString());
                selectItem.setDescription(rs.getString(2));
                selectItem.setLabel(rs.getString(3));
                selectValues2.add(selectItem);
            }

            rs.close();
            //conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String userQuery =
            "begin ? := tqc_roles_cursor.Get_User_Roles(?); end;";
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
            while (t < displayValue2.size()) {
                displayValue2.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue2.add(rsUser.getBigDecimal(2).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        if (userRoles == null) {
        } else {

            userRoles.setValue(displayValue2);
            userRolesSelect.setValue(selectValues2);
        }
        return null;
    }

    public String saveKPIDetails() {

        BigDecimal userGroupCode = null;
        BigDecimal taskCode = null;
        BigDecimal subTaskCode = null;
        BigDecimal kpiCode = null;
        String option = null;
        
        if (session.getAttribute("grpUserCode") != null) {
            userGroupCode = (BigDecimal)(session.getAttribute("grpUserCode"));

        }
        
        if (session.getAttribute("kipCode") != null) {
            kpiCode = (BigDecimal)(session.getAttribute("kipCode"));

        }

        if (session.getAttribute("taskCode") != null) {
            taskCode = (BigDecimal)(session.getAttribute("taskCode"));

        }


        if (session.getAttribute("subTaskCode") != null) {
            subTaskCode = (BigDecimal)(session.getAttribute("subTaskCode"));

        }

        // Check if the user wishes to SAVE or UPDATE
        if (createKpiBtn.getText().equals("Update")) {

            option = "E";
        }

        if (createKpiBtn.getText().equals("Save")) {

            option = "A";
        }

        if (kpiTask.getValue() == null) {
            GlobalCC.errorValueNotEntered("Enter Task ");
            return null;
        }

        if (kpiSubTask.getValue() == null) {
            GlobalCC.errorValueNotEntered("Enter Sub Task");
            return null;
        }
        if (kpiParam.getValue() == null) {
            GlobalCC.errorValueNotEntered("Please Parameter");
            return null;
        }
        
        if (kpiUnit.getValue() == null) {
            GlobalCC.errorValueNotEntered("Please Unit");
            return null;
        }
        
        
        if (kpiComment.getValue() == null) {
            GlobalCC.errorValueNotEntered("Please Comment");
            return null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        try {
            String query =
                "begin TQC_SETUPS_PKG.update_kpi(?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, option);
            cst.setBigDecimal(2, userGroupCode);
            cst.setBigDecimal(3, taskCode);
            cst.setBigDecimal(4, subTaskCode);
            cst.setString(5, kpiTask.getValue().toString());
            cst.setString(6, kpiSubTask.getValue().toString());
            cst.setString(7, kpiParam.getValue().toString());
            cst.setString(8, kpiUnit.getValue().toString());
            cst.setString(9, kpiComment.getValue().toString());
            cst.setBigDecimal(10,kpiCode);
            
            cst.execute();
                                 
            kpiComment.setValue(null);
            kpiSubTask.setValue(null);
            kpiTask.setValue(null);
            kpiParam.setValue(null);
            kpiUnit.setValue(null);
            GlobalCC.refreshUI(kpiComment);
            GlobalCC.refreshUI(kpiSubTask);
            GlobalCC.refreshUI(kpiTask);
            GlobalCC.refreshUI(kpiParam);
            GlobalCC.refreshUI(kpiUnit);
            
            ADFUtils.findIterator("fetchKpisIterator").executeQuery();
            GlobalCC.refreshUI(tblKpiTab);


            GlobalCC.dismissPopUp("crm", "p1k");
        
            createKpiBtn.setText("Save");
            GlobalCC.refreshUI(createKpiBtn);
            GlobalCC.INFORMATIONREPORTING("Process Successfully Completed!");

            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return null;

    }


    public String cancelKPIDetails() {
        return null;
    }


    public String saveSysUserDetails() {
        String usernameVal = null;
        String fullnameVal = null;
        String emailVal = null;
        String passVal = null;
        String passConfirmVal = null;
        String personelRankVal = null;
        String statusVal = null;
        String sysTypeVal = null;
        String personnelId = null;
        String sysPassResetVal = null;
        String phoneNumber;
        String securityQuestion;
        String currentUser = null;
        Util util = new Util();

        if (sysPassReset.isSelected()) {
            sysPassResetVal = "Y";
        } else {
            sysPassResetVal = "N";
        }
        String autoCreatePwd =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_CREATE_PWD");

        usernameVal = GlobalCC.checkNullValues(sysUsername.getValue());
        fullnameVal = GlobalCC.checkNullValues(sysFullname.getValue());
        emailVal = GlobalCC.checkNullValues(sysEmail.getValue());
        passVal = GlobalCC.checkNullValues(sysPassword.getValue());
        passConfirmVal =
                GlobalCC.checkNullValues(sysConfirmPassword.getValue());
        personelRankVal = GlobalCC.checkNullValues(sysPersonelRank.getValue());
        statusVal = GlobalCC.checkNullValues(sysStatus.getValue());
        sysTypeVal = GlobalCC.checkNullValues(sysType.getValue());
        personnelId = GlobalCC.checkNullValues(txtPersonnelId.getValue());
        String pin = GlobalCC.checkNullValues(txtPin.getValue());
        BigDecimal sactCode =
            GlobalCC.checkBDNullValues(txtSactCode.getValue());
        currentUser = session.getAttribute("Username").toString();
        UploadedFile file = (UploadedFile)userSignature.getValue();
        System.out.println("fileStream " + fileStream);
        if (usernameVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Username");
            return null;
            //errorText = errorText + "Value Missing: Username\n";
        }
        if (fullnameVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Fullname");
            return null;
            //errorText = errorText + "Value Missing: Fullname\n";
        }
        if (emailVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Email");
            return null;
            //errorText = errorText + "Value Missing: Email \n";
        }
        String type = "NORMAL";
        if (session.getAttribute("SIGN_IN_MODE") != null) {
            type = session.getAttribute("SIGN_IN_MODE").toString();
        }
        if (type.equalsIgnoreCase("LDAP") ||
            "Y".equalsIgnoreCase(autoCreatePwd)) {

        } else {
            if (passVal == null) {
                GlobalCC.errorValueNotEntered("Value Missing: Password");
                return null;
                //errorText = errorText + "Value Missing: Password\n";
            }
            if (!passVal.equals(passConfirmVal)) {
                GlobalCC.errorValueNotEntered("Inconsistent Values: Passwords do not match");
                return null;
                //errorText = errorText + "Inconsistent Values: Passwords do not match\n";
            }
        }

        if (personelRankVal == null) {
            // GlobalCC.errorValueNotEntered("Value Missing: Personnel Rank");
            // return null;
            //errorText = errorText + "Value Missing: Personnel Rankn";
        }
        if (sysTypeVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: User Type");
            return null;

        }
        if (txtPhoneNumber.getValue() != null) {
            phoneNumber = txtPhoneNumber.getValue().toString();

        } else {
            phoneNumber = null;
        }
        securityQuestion = util.getParameterValue("SECURITY_QUESTION_MAND");
        if (securityQuestion.equals("Y") &&
            (txtSecurityQuestions.getValue() == null ||
             txtSecurityAnswer.getValue() == null)) {
            GlobalCC.INFORMATIONREPORTING("Please select a security Question and Enter an Answer");
            return null;
        }
        String query =
            "begin tqc_roles_cursor.Create_User(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {
            //GlobalCC.INFORMATIONREPORTING(currentUser);
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, usernameVal);
            cst.setString(2, fullnameVal);
            cst.setString(3, passVal);
            cst.setString(4, emailVal);
            cst.setString(5, personelRankVal);
            cst.setString(6, statusVal);
            cst.setString(7, sysTypeVal);
            cst.setString(8, sysPassResetVal);
            cst.setString(9, personnelId);
            cst.registerOutParameter(10, OracleTypes.NUMBER);
            cst.setObject(11, txtAccManager.getValue());
            cst.setBlob(12, fileStream);
            cst.setString(13, phoneNumber);
            cst.setObject(14, GlobalCC.extractDate(txtWefdate));
            cst.setObject(15, GlobalCC.extractDate(txtWetDate));
            cst.setObject(16, txtSecurityQuestions.getValue());
            cst.setObject(17, txtSecurityAnswer.getValue());
            cst.setString(18, currentUser);
            cst.setBigDecimal(19, sactCode);
            cst.setString(20, pin);
            cst.registerOutParameter(21, OracleTypes.VARCHAR);
            cst.execute();

            session.setAttribute("sysUserCode", cst.getBigDecimal(10));
            String msg = cst.getString(21);
            conn.commit();
            conn.close();
            conn = null;
            GlobalCC.hidePopup("crm:p1");
            ADFUtils.findIterator("findUsersIterator").executeQuery();
            ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
            ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(users);
            GlobalCC.refreshUI(pbxMainPanel);

            sysUsername.setValue(null);
            sysFullname.setValue(null);
            sysEmail.setValue(null);
            sysPassword.setValue(null);
            sysConfirmPassword.setValue(null);
            sysPersonelRank.setValue(null);
            sysPassReset.setSelected(false);
            txtPersonnelId.setValue(null);
            txtPersonnelName.setValue(null);
            txtAccManager.setValue(null);
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        System.out.println("am here");
        // }

        return null;
    }

    public String editUserDetails() {
        editBtn.setRendered(true);
        createBtn.setRendered(false);

        String autoResetPwd =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_RESET_PWD");
        boolean autoReset = "Y".equalsIgnoreCase(autoResetPwd);

        DCIteratorBinding dciter = ADFUtils.findIterator("findUsersIterator");
        RowKeySet set = users.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            session.setAttribute("sysUserCode", r.getAttribute("userCode"));

            sysUsername.setValue(r.getAttribute("username"));
            sysUsername.setDisabled(false);
            sysFullname.setValue(r.getAttribute("userFullname"));
            sysEmail.setValue(r.getAttribute("userEmail"));
            //sysPassword.setDisabled(false);
            //sysConfirmPassword.setDisabled(false);
            sysType.setValue(r.getAttribute("userType"));
            sysStatus.setValue(r.getAttribute("userStatus"));
            txtAccManager.setValue(r.getAttribute("userAccManager"));
            txtPhoneNumber.setValue(r.getAttribute("phoneNumber"));
            txtPin.setValue(r.getAttribute("pin"));

            txtSecurityQuestions.setValue(r.getAttribute("usrSecurityQuestion"));
            txtSecurityAnswer.setValue(r.getAttribute("usrSecurityAnswer"));
            txtSactCode.setValue(r.getAttribute("USR_SACT_CODE"));
            txtSactName.setValue(r.getAttribute("SACT_SHT_DESC"));
            txtUserAuthorized.setValue(r.getAttribute("userAuthorized"));

            GlobalCC.refreshUI(txtSactCode);
            GlobalCC.refreshUI(txtSactName);
            GlobalCC.refreshUI(txtUserAuthorized);

            if (r.getAttribute("userType").equals("User")) {
                sysType.setValue("U");
            } else if (r.getAttribute("userType").equals("Group")) {
                sysType.setValue("G");
            }


            //   sysStatus.setDisabled(true);
            sysPersonelRank.setValue(r.getAttribute("userPersonnelRank"));
            if (r.getAttribute("userPassReset") == null) {
                sysPassReset.setSelected(false);
            } else {
                if (((String)r.getAttribute("userPassReset")).equalsIgnoreCase("Yes")) {
                    sysPassReset.setSelected(true);
                } else {
                    sysPassReset.setSelected(false);
                }
            }
            txtPersonnelId.setValue(r.getAttribute("userPerId"));


            UserDAO userDAO = new UserDAO();

            if (r.getAttribute("userPerId") != null) {
                txtPersonnelName.setValue(userDAO.getPersonnelName(r.getAttribute("userPerId").toString()));
            } else {
                txtPersonnelName.setValue(null);
            }
            txtWefdate.setDisabled(true);
            lblWefdate.setRendered(true);
            txtWefdate.setValue(r.getAttribute("sysWef"));
            txtWetDate.setValue(r.getAttribute("sysWet"));
            GlobalCC.refreshUI(lblWefdate);
            GlobalCC.refreshUI(txtWefdate);
            GlobalCC.refreshUI(txtWetDate);
        }
        //String type = "NORMAL";

        ldapUser.setRendered(false);
        sysUsername.setDisabled(false);
        GlobalCC.showPopup("crm:p1");
        return null;
    }

    public String editGroupUserDetails() {
        editBtn.setRendered(true);
        createBtn.setRendered(false);

        String autoResetPwd =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_RESET_PWD");
        boolean autoReset = "Y".equalsIgnoreCase(autoResetPwd);

        DCIteratorBinding dciter =
            ADFUtils.findIterator("findGroupUserIterator");
        RowKeySet set = tblGroup.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            session.setAttribute("sysUserCode", r.getAttribute("userCode"));

            sysUsername.setValue(r.getAttribute("username"));
            sysUsername.setDisabled(false);
            sysFullname.setValue(r.getAttribute("userFullname"));
            sysEmail.setValue(r.getAttribute("userEmail"));
            //sysPassword.setDisabled(false);
            //sysConfirmPassword.setDisabled(false);
            sysType.setValue(r.getAttribute("userType"));
            sysStatus.setValue(r.getAttribute("userStatus"));
            txtAccManager.setValue(r.getAttribute("userAccManager"));
            txtPhoneNumber.setValue(r.getAttribute("phoneNumber"));
            txtPin.setValue(r.getAttribute("pin"));

            txtSecurityQuestions.setValue(r.getAttribute("usrSecurityQuestion"));
            txtSecurityAnswer.setValue(r.getAttribute("usrSecurityAnswer"));
            txtSactCode.setValue(r.getAttribute("USR_SACT_CODE"));
            txtSactName.setValue(r.getAttribute("SACT_SHT_DESC"));
            txtUserAuthorized.setValue(r.getAttribute("userAuthorized"));

            GlobalCC.refreshUI(txtSactCode);
            GlobalCC.refreshUI(txtSactName);
            GlobalCC.refreshUI(txtUserAuthorized);

          
            if ("User".equals(r.getAttribute("userType"))) {
                sysType.setValue("U");
            } else if ("Group".equals(r.getAttribute("userType"))) {
                sysType.setValue("G");
            }


            //   sysStatus.setDisabled(true);
            sysPersonelRank.setValue(r.getAttribute("userPersonnelRank"));
            if (r.getAttribute("userPassReset") == null) {
                sysPassReset.setSelected(false);
            } else {
                if (((String)r.getAttribute("userPassReset")).equalsIgnoreCase("Yes")) {
                    sysPassReset.setSelected(true);
                } else {
                    sysPassReset.setSelected(false);
                }
            }
            txtPersonnelId.setValue(r.getAttribute("userPerId"));


            UserDAO userDAO = new UserDAO();

            if (r.getAttribute("userPerId") != null) {
                txtPersonnelName.setValue(userDAO.getPersonnelName(r.getAttribute("userPerId").toString()));
            } else {
                txtPersonnelName.setValue(null);
            }
            txtWefdate.setDisabled(true);
            lblWefdate.setRendered(true);
            txtWefdate.setValue(r.getAttribute("sysWef"));
            txtWetDate.setValue(r.getAttribute("sysWet"));
            GlobalCC.refreshUI(lblWefdate);
            GlobalCC.refreshUI(txtWefdate);
            GlobalCC.refreshUI(txtWetDate);
        }
        //String type = "NORMAL";

        ldapUser.setRendered(false);
        sysUsername.setDisabled(false);
        GlobalCC.showPopup("crm:p1");
        return null;
    }

    public String actionAuthorizeUser() {

        if (GlobalCC.checkBDNullValues(session.getAttribute("sysUserCode")) ==
            null) {
            GlobalCC.errorValueNotEntered("Please Select a user!");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        String query = null;
        try {

            conn = dbConnector.getDatabaseConnection();
            query = "begin tqc_roles_cursor.authorize_user ( ?, ?, ? ); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables
            callStmt.registerOutParameter(1, OracleTypes.VARCHAR);
            callStmt.setString(2,
                               GlobalCC.checkNullValues(session.getAttribute("Username")));
            callStmt.setBigDecimal(3,
                                   GlobalCC.checkBDNullValues(session.getAttribute("sysUserCode")));
            callStmt.execute();
            conn.commit();
            String msg = callStmt.getString(1);

            if ("success".equalsIgnoreCase(msg)) {
                ADFUtils.findIterator("findUsersIterator").executeQuery();
                GlobalCC.refreshUI(users);
                GlobalCC.INFORMATIONREPORTING("User Authorized successfully!");
            } else {
                GlobalCC.EXCEPTIONREPORTING(msg);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, null);
        }
        return null;
    }

    public String actionAutoResetPwd() {

        if (GlobalCC.checkBDNullValues(session.getAttribute("sysUserCode")) ==
            null) {
            GlobalCC.errorValueNotEntered("Please Select a user to autoreset password!");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        String query = null;
        try {

            conn = dbConnector.getDatabaseConnection();
            query =
                    "begin ? := tqc_roles_cursor.Auto_Reset_Usr_Pwd ( ?, ?, ? ); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables
            callStmt.registerOutParameter(1, OracleTypes.NUMBER);
            callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
            callStmt.setBigDecimal(3,
                                   GlobalCC.checkBDNullValues(session.getAttribute("sysUserCode")));
            callStmt.setString(4,
                               GlobalCC.checkNullValues(session.getAttribute("Username")));
            callStmt.execute();
            conn.commit();
            String msg = callStmt.getString(2);
            ADFUtils.findIterator("findUsersIterator").executeQuery();
            GlobalCC.refreshUI(users);
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, null);
        }
        return null;
    }

    public String cancelUserDetails() {
        editBtn.setRendered(false);
        createBtn.setRendered(true);
        sysUsername.setValue(null);
        sysFullname.setValue(null);
        sysEmail.setValue(null);
        sysPassword.setValue(null);
        sysPassword.setDisabled(false);
        sysConfirmPassword.setValue(null);
        sysStatus.setDisabled(false);
        sysPersonelRank.setValue(null);
        sysPassReset.setSelected(false);
        txtPersonnelId.setValue(null);
        txtPersonnelName.setValue(null);
        txtWefdate.setValue(null);
        txtWetDate.setValue(null);
        return null;
    }


    public String saveEditUserDetails() {
        String usernameVal = null;
        String fullnameVal = null;
        String emailVal = null;
        String personelRankVal = null;
        String sysTypeVal = null;
        String phoneNumber;

        usernameVal = GlobalCC.checkNullValues(sysUsername.getValue());
        fullnameVal = GlobalCC.checkNullValues(sysFullname.getValue());
        emailVal = GlobalCC.checkNullValues(sysEmail.getValue());
        personelRankVal = GlobalCC.checkNullValues(sysPersonelRank.getValue());
        sysTypeVal = GlobalCC.checkNullValues(sysType.getValue());
        String sysPassResetVal = null;
        String personnelId =
            GlobalCC.checkNullValues(txtPersonnelId.getValue());
        String pin = GlobalCC.checkNullValues(txtPin.getValue());
        String postedBy =
            GlobalCC.checkNullValues(session.getAttribute("Username"));


        if (sysPassReset.isSelected()) {
            sysPassResetVal = "Y";
        } else {
            sysPassResetVal = "N";
        }
        if (usernameVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Username");
            return null;
        }
        if (fullnameVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Fullname");
            return null;
        }
        if (emailVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: Email");
            return null;
        }
        if (personelRankVal == null) {
            //GlobalCC.errorValueNotEntered("Value Missing: Personnel Rank");
            // return null;
        }
        if (sysTypeVal == null) {
            GlobalCC.errorValueNotEntered("Value Missing: User Type");
            return null;
        }
        if (txtPhoneNumber.getValue() != null) {
            phoneNumber = txtPhoneNumber.getValue().toString();

        } else {
            phoneNumber = null;
        }

        String query =
            "begin tqc_roles_cursor.UpDate_User(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.setString(2, usernameVal);
            cst.setString(3, fullnameVal);
            cst.setString(4, emailVal);
            cst.setString(5, personelRankVal);
            cst.setString(6, sysTypeVal);
            cst.setString(7, sysPassResetVal);
            cst.setString(8, personnelId);
            cst.setObject(9, sysStatus.getValue());
            cst.setObject(10, txtAccManager.getValue());
            cst.setObject(11, phoneNumber);
            cst.setObject(12, GlobalCC.extractDate(txtWefdate));
            cst.setObject(13, GlobalCC.extractDate(txtWetDate));
            cst.setObject(14, txtSecurityQuestions.getValue());
            cst.setObject(15, txtSecurityAnswer.getValue());
            cst.setBigDecimal(16, (BigDecimal)txtSactCode.getValue());
            cst.setString(17, postedBy);
            cst.setString(18, pin);
            cst.registerOutParameter(19, OracleTypes.VARCHAR);
            cst.execute();
            String msg = GlobalCC.checkNullValues(cst.getString(19));
            System.out.println("Am here...");
            if (msg != null) {
                if (msg.indexOf("success") >= 0) {
                    conn.commit();
                    conn.close();
                    conn = null;
                    ADFUtils.findIterator("findUsersIterator").executeQuery();
                    ADFUtils.findIterator("findGroupUserIterator").executeQuery();
                    GlobalCC.refreshUI(users);
                    GlobalCC.refreshUI(tblGroup);
                    editBtn.setRendered(false);
                    createBtn.setRendered(true);
                    sysUsername.setValue(null);
                    sysFullname.setValue(null);
                    sysEmail.setValue(null);
                    sysPassword.setValue(null);
                    sysPassword.setDisabled(false);
                    sysStatus.setDisabled(false);
                    sysPersonelRank.setValue(null);
                    sysPassReset.setSelected(false);
                    txtPersonnelId.setValue(null);
                    txtPersonnelName.setValue(null);
                    txtAccManager.setValue(null);
                    txtWefdate.setValue(null);
                    txtWetDate.setValue(null);

                    GlobalCC.hidePopup("crm:p1");
                }
            }
            GlobalCC.INFORMATIONREPORTING(msg);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return null;
    }

    public String DropUser() {

        String query = "begin tqc_roles_cursor.Drop_User(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("sysUserCode"));

            cst.execute();

            conn.close();
            ADFUtils.findIterator("findUsersIterator").executeQuery();
            GlobalCC.refreshUI(users);
            editBtn.setRendered(false);
            createBtn.setRendered(true);
            sysUsername.setValue(null);
            sysFullname.setValue(null);
            sysEmail.setValue(null);
            sysPassword.setValue(null);
            sysPassword.setDisabled(false);
            sysStatus.setDisabled(false);
            sysPersonelRank.setValue(null);
            sysPassReset.setSelected(false);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String RestoreUser() {

        String query = "begin tqc_roles_cursor.Restore_User(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("sysUserCode"));

            cst.execute();

            conn.close();
            ADFUtils.findIterator("findUsersIterator").executeQuery();
            GlobalCC.refreshUI(users);
            editBtn.setRendered(false);
            createBtn.setRendered(true);
            sysUsername.setValue(null);
            sysFullname.setValue(null);
            sysEmail.setValue(null);
            sysPassword.setValue(null);
            sysPassword.setDisabled(false);
            sysStatus.setDisabled(false);
            sysPersonelRank.setValue(null);
            sysPassReset.setSelected(false);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public void userSystemGrant(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        System.out.println("UserCode: " +
                           (BigDecimal)session.getAttribute("sysUserCode"));
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            try {
                conn = datahandler.getDatabaseConnection();
                ArrayList<String> myVals =
                    (ArrayList<String>)userSystems.getValue();
                ArrayList<SelectItem> myVals2 =
                    (ArrayList<SelectItem>)userSystemsSelect.getValue();
                int v = 0;
                String revokeQuery =
                    "begin tqc_roles_cursor.Revoke_User_System(?,?); end;";

                while (v < myVals2.size()) {
                    SelectItem select = myVals2.get(v);
                    OracleCallableStatement cst = null;


                    cst =
(OracleCallableStatement)conn.prepareCall(revokeQuery);
                    cst.setBigDecimal(1,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setString(2, (String)select.getValue());
                    cst.execute();

                    v++;
                }
                String query =
                    "begin tqc_roles_cursor.Grant_User_System(?,?); end;";
                int k = 0;
                while (k < myVals.size()) {
                    OracleCallableStatement cst = null;


                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setString(2, myVals.get(k));
                    cst.execute();


                    k++;
                }
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        // Add event code here...
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

    public void setUserSystemsSelect(UISelectItems userSystemsSelect) {
        this.userSystemsSelect = userSystemsSelect;
    }

    public UISelectItems getUserSystemsSelect() {
        return userSystemsSelect;
    }

    public void setSysPassReset(RichSelectBooleanCheckbox sysPassReset) {
        this.sysPassReset = sysPassReset;
    }

    public RichSelectBooleanCheckbox getSysPassReset() {
        return sysPassReset;
    }

    public void setUserSystemDates(RichTable userSystemDates) {
        this.userSystemDates = userSystemDates;
    }

    public RichTable getUserSystemDates() {
        return userSystemDates;
    }

    public void setSysWetSystemName(RichInputText sysWetSystemName) {
        this.sysWetSystemName = sysWetSystemName;
    }

    public RichInputText getSysWetSystemName() {
        return sysWetSystemName;
    }

    public void setSysWef(RichInputDate sysWef) {
        this.sysWef = sysWef;
    }

    public RichInputDate getSysWef() {
        return sysWef;
    }

    public void setSysWet(RichInputDate sysWet) {
        this.sysWet = sysWet;
    }

    public RichInputDate getSysWet() {
        return sysWet;
    }

    public String saveWet() {
        String wetVal = null;
        wetVal = GlobalCC.checkNullValues(sysWet.getValue());
        if (wetVal == null) {

        } else {
            wetVal = GlobalCC.parseDate(wetVal);
        }
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        try {
            String grantQuery =
                "begin tqc_roles_cursor.Edit_User_SysWet(?,?); end;";

            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(grantQuery);
            cst.setBigDecimal(1,
                              (BigDecimal)session.getAttribute("sysUserCode"));
            cst.setString(2, wetVal);
            cst.execute();

            conn.close();
            ADFUtils.findIterator("findUserSystemsIterator").executeQuery();
            GlobalCC.refreshUI(userSystemDates);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        // Add event code here...
        return null;
    }

    public String editWet() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "template:pc2:p2" + "').show(hints);");
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserSystemsIterator");
        RowKeySet set = userSystemDates.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();

            sysWetSystemName.setValue(r.getAttribute("sysName"));
            sysWef.setValue(r.getAttribute("sysWef"));
            session.setAttribute("sysUserCode", r.getAttribute("userSysCode"));

        }

        return null;
    }

    public void setOrganizationDesc(RichInputText organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public RichInputText getOrganizationDesc() {
        return organizationDesc;
    }

    public String organizationSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findOrganizationsIterator");
        RowKeySet set = tblOrganizations.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            session.setAttribute("orgCode", r.getAttribute("orgCode"));

            organizationDesc.setValue(r.getAttribute("orgName"));


            //session.removeAttribute("regCode");
            ADFUtils.findIterator("findRegionsIterator").executeQuery();
            GlobalCC.refreshUI(tblRegionsPop);

            regionDesc.setValue(null);

            //UserBranchesValues();
            //GlobalCC.refreshUI(userBranches);
            //ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
            //GlobalCC.refreshUI(userBranchesTab);
        }
        return null;
    }

    public void setTblOrganizations(RichTable tblOrganizations) {
        this.tblOrganizations = tblOrganizations;
    }

    public RichTable getTblOrganizations() {
        return tblOrganizations;
    }

    public String UserBranchesValues() {
        String query = "begin ? := tqc_roles_cursor.GetBranches(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            if (session.getAttribute("regCode") == null) {
                cst.setBigDecimal(2, null);
            } else {
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("regCode"));
            }

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
                selectItem.setDescription(rs.getString(4));
                selectItem.setLabel(rs.getString(4));
                selectValues.add(selectItem);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String userQuery =
            "begin ? := tqc_roles_cursor.GetUserBranches(?,?); end;";
        OracleCallableStatement cstUsr = null;
        try {

            conn = datahandler.getDatabaseConnection();

            cstUsr = (OracleCallableStatement)conn.prepareCall(userQuery);
            cstUsr.registerOutParameter(1,
                                        OracleTypes.CURSOR); //authorization code
            cstUsr.setBigDecimal(2,
                                 (BigDecimal)session.getAttribute("sysUserCode"));
            if (session.getAttribute("regCode") == null) {
                cstUsr.setBigDecimal(3, null);
            } else {
                cstUsr.setBigDecimal(3,
                                     (BigDecimal)session.getAttribute("regCode"));
            }
            cstUsr.execute();
            OracleResultSet rsUser = (OracleResultSet)cstUsr.getObject(1);
            int t = 0;
            while (t < displayValue.size()) {
                displayValue.remove(t);
                t++;
            }

            while (rsUser.next()) {
                displayValue.add(rsUser.getBigDecimal(2).toString());
            }

            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        /*if (userBranches == null) {
        } else {
            userBranches.setValue(displayValue);
            userSystemsSelect.setValue(selectValues);
        }*/
        return null;
    }

    public void setTblAllBranches(RichTable tblAllBranches) {
        this.tblAllBranches = tblAllBranches;
    }

    public RichTable getTblAllBranches() {
        return tblAllBranches;
    }

    public String regionSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findRegionsIterator");
        RowKeySet set = tblRegionsPop.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            session.setAttribute("regCode", r.getAttribute("regCode"));
            regionDesc.setValue(r.getAttribute("regName"));

        }
        //UserBranchesValues();

        ADFUtils.findIterator("fetchAllBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblAllBranches);
        //.getCurrentInstance().addPartialTarget(userBranches);
        //ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        //GlobalCC.refreshUI(userBranchesTab);
        //ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        //GlobalCC.refreshUI(userBranchesTab);
        return null;
    }

    public void setRegionDesc(RichInputText regionDesc) {
        this.regionDesc = regionDesc;
    }

    public RichInputText getRegionDesc() {
        return regionDesc;
    }

    public void setTblRegionsPop(RichTable tblRegionsPop) {
        this.tblRegionsPop = tblRegionsPop;
    }

    public RichTable getTblRegionsPop() {
        return tblRegionsPop;
    }

    public void setTblUserBranches(RichTable tblUserBranches) {
        this.tblUserBranches = tblUserBranches;
    }

    public RichTable getTblUserBranches() {
        return tblUserBranches;
    }

    public String actionAddUserBranch() {
        String branchCode1 = null;
        String branchCode2 = null;
        String userCode =
            GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
        List branchList = new ArrayList();

        // 1. Get the selected branch code
        DCIteratorBinding dciter1 =
            ADFUtils.findIterator("fetchAllBranchesIterator");
        RowKeySet set = tblAllBranches.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter1.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter1.getCurrentRow();
            branchCode1 = GlobalCC.checkNullValues(r.getAttribute("regCode"));
        }


        // 2. Check if the selected branch exists for the current user
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserBranchesIterator");
        int rowCount = tblUserBranches.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            tblUserBranches.setRowIndex(i);
            List l = (List)tblUserBranches.getRowKey();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));
            Row r = dciter.getCurrentRow();

            branchCode2 =
                    GlobalCC.checkNullValues(r.getAttribute("branchCode"));
            if (branchCode2.equals(branchCode1)) {
                branchList.add(branchCode2);
            } else {
                // Do nothing
            }
        }

        // 3. Show error if branch exists, else proceed to add
        if (branchList.size() > 0) {
            GlobalCC.errorValueNotEntered("The selected branch exists. Select another.");
        } else {
            String query =
                "begin tqc_roles_cursor.Grant_User_Branch(?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);

                cst.setBigDecimal(1, new BigDecimal(userCode));
                cst.setBigDecimal(2, new BigDecimal(branchCode1));
                cst.setString(3,
                              GlobalCC.checkNullValues(session.getAttribute("Username")));
                cst.execute();

                cst.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }

        ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblUserBranches);

        return null;
    }

    public String selectCountry() {
        return null;
    }


    public String actionRemoveUserBranch() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserBranchesIterator");
        RowKeySet set = tblUserBranches.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            // Ensure the user code and the branch code is not empty
            String userCode =
                GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
            String branchCode =
                GlobalCC.checkNullValues(r.getAttribute("branchCode"));

            if (userCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: User Code is empty");
                return null;

            } else if (branchCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Branch Code is empty");
                return null;

            } else {
                String query =
                    "begin tqc_roles_cursor.Revoke_User_Branch(?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(query);

                    cst.setBigDecimal(1, new BigDecimal(userCode));
                    cst.setBigDecimal(2, new BigDecimal(branchCode));
                    cst.setString(3,
                                  GlobalCC.checkNullValues(session.getAttribute("Username")));
                    cst.execute();

                    cst.close();
                    conn.close();

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            }
        }

        ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblUserBranches);

        return null;
    }

    public void setSystemsLOV(RichTable systemsLOV) {
        this.systemsLOV = systemsLOV;
    }

    public RichTable getSystemsLOV() {
        return systemsLOV;
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


            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        ADFUtils.findIterator("fetchAllSystemRolesIterator").executeQuery();
        GlobalCC.refreshUI(tblAllSysRoles);

        ADFUtils.findIterator("fetchAllSystemRolesIterator").executeQuery();
        GlobalCC.refreshUI(userRoles);

        return null;
    }

    public void setSystemDesc(RichInputText systemDesc) {
        this.systemDesc = systemDesc;
    }

    public RichInputText getSystemDesc() {
        return systemDesc;
    }

    public void setTblAllSysRoles(RichTable tblAllSysRoles) {
        this.tblAllSysRoles = tblAllSysRoles;
    }

    public RichTable getTblAllSysRoles() {
        return tblAllSysRoles;
    }

    public void setTblAllUsrRoles(RichTable tblAllUsrRoles) {
        this.tblAllUsrRoles = tblAllUsrRoles;
    }

    public RichTable getTblAllUsrRoles() {
        return tblAllUsrRoles;
    }

    public String actionAddUserRole() {
        String roleCode1 = null;
        String roleCode2 = null;
        String userCode =
            GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
        List rolesList = new ArrayList();

        // 1. Get the selected role code
        DCIteratorBinding dciter1 =
            ADFUtils.findIterator("fetchAllSystemRolesIterator");
        RowKeySet set = tblAllSysRoles.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter1.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter1.getCurrentRow();
            roleCode1 = GlobalCC.checkNullValues(r.getAttribute("roleCode"));
        }

        // 2. Check if the selected role exists for the current user
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchAllUserRolesIterator");
        int rowCount = tblAllUsrRoles.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            tblAllUsrRoles.setRowIndex(i);
            List l = (List)tblAllUsrRoles.getRowKey();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));
            Row r = dciter.getCurrentRow();

            roleCode2 = GlobalCC.checkNullValues(r.getAttribute("roleCode"));
            if (roleCode2.equals(roleCode1)) {
                rolesList.add(roleCode2);
            } else {
                // Do nothing
            }
        }

        // 3. Show error if branch exists, else proceed to add
        if (rolesList.size() > 0) {
            GlobalCC.errorValueNotEntered("The selected role exists. Select another.");
        } else {
            String query =
                "begin tqc_roles_cursor.Grant_User_Role(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);

                cst.setBigDecimal(1, new BigDecimal(userCode));
                cst.setBigDecimal(2, new BigDecimal(userCode));
                cst.setBigDecimal(3, new BigDecimal(roleCode1));
                cst.setString(4,
                              GlobalCC.checkNullValues(session.getAttribute("Username")));

                cst.execute();

                cst.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }

        ADFUtils.findIterator("fetchAllUserRolesIterator").executeQuery();
        GlobalCC.refreshUI(tblAllUsrRoles);

        return null;
    }

    public String actionRemoveUserRole() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchAllUserRolesIterator");
        RowKeySet set = tblAllUsrRoles.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            // Ensure the user code and the branch code is not empty
            String userCode =
                GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
            String roleCode =
                GlobalCC.checkNullValues(r.getAttribute("roleCode"));

            if (userCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: User Code is empty");
                return null;

            } else if (roleCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Role Code is empty");
                return null;

            } else {
                String query =
                    "begin tqc_roles_cursor.Revoke_User_Role(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(query);

                    cst.setBigDecimal(1, new BigDecimal(userCode));
                    cst.setBigDecimal(2, new BigDecimal(userCode));
                    cst.setBigDecimal(3, new BigDecimal(roleCode));
                    cst.setString(4,
                                  GlobalCC.checkNullValues(session.getAttribute("Username")));

                    cst.execute();

                    cst.close();
                    conn.commit();
                    conn.close();

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            }
        }

        ADFUtils.findIterator("fetchAllUserRolesIterator").executeQuery();
        GlobalCC.refreshUI(tblAllUsrRoles);

        return null;
    }

    public void setTblAllDivisions(RichTable tblAllDivisions) {
        this.tblAllDivisions = tblAllDivisions;
    }

    public RichTable getTblAllDivisions() {
        return tblAllDivisions;
    }

    public void setTblUserDivisions(RichTable tblUserDivisions) {
        this.tblUserDivisions = tblUserDivisions;
    }

    public RichTable getTblUserDivisions() {
        return tblUserDivisions;
    }

    public String actionAddDivision() {
        String divisionCode1 = null;
        String divisionCode2 = null;
        String userCode =
            GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
        List divisionsList = new ArrayList();

        // 1. Get the selected division code
        DCIteratorBinding dciter1 =
            ADFUtils.findIterator("findDivisionsIterator");
        RowKeySet set = tblAllDivisions.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter1.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter1.getCurrentRow();
            divisionCode1 =
                    GlobalCC.checkNullValues(r.getAttribute("DIV_CODE"));
        }

        // 2. Check if the selected division exists for the current user
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchUserDivisionsIterator");
        int rowCount = tblUserDivisions.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            tblUserDivisions.setRowIndex(i);
            List l = (List)tblUserDivisions.getRowKey();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));
            Row r = dciter.getCurrentRow();

            divisionCode2 =
                    GlobalCC.checkNullValues(r.getAttribute("DIV_CODE"));
            if (divisionCode2.equals(divisionCode1)) {
                divisionsList.add(divisionCode2);
            } else {
                // Do nothing
            }
        }

        // 3. Show error if division exists, else proceed to add
        if (divisionsList.size() > 0) {
            GlobalCC.errorValueNotEntered("The selected division exists. Please Select another.");
        } else {
            String query =
                "begin tqc_web_cursor.grant_user_division(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(query);

                cst.setBigDecimal(1, null);
                cst.setBigDecimal(2, new BigDecimal(userCode));
                cst.setBigDecimal(3, new BigDecimal(divisionCode1));
                cst.setString(4, "N");

                cst.execute();

                cst.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }

        ADFUtils.findIterator("fetchUserDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(tblUserDivisions);

        return null;
    }

    public String actionRemoveDivision() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchUserDivisionsIterator");
        RowKeySet set = tblUserDivisions.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();

            // Ensure the user code and the division code is not empty
            String userCode =
                GlobalCC.checkNullValues(session.getAttribute("sysUserCode"));
            String divisionCode =
                GlobalCC.checkNullValues(r.getAttribute("DIV_CODE"));

            if (userCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: User Code is empty");
                return null;

            } else if (divisionCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Division Code is empty");
                return null;

            } else {
                String query =
                    "begin tqc_web_cursor.revoke_user_division(?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(query);

                    cst.setBigDecimal(1, new BigDecimal(userCode));
                    cst.setBigDecimal(2, new BigDecimal(divisionCode));

                    cst.execute();

                    cst.close();
                    conn.close();

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            }
        }

        ADFUtils.findIterator("fetchUserDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(tblUserDivisions);

        return null;
    }

    public void setTreeUnassignedSystems(RichTree treeUnassignedSystems) {
        this.treeUnassignedSystems = treeUnassignedSystems;
    }

    public RichTree getTreeUnassignedSystems() {
        return treeUnassignedSystems;
    }

    public void setTreeAssignedSystems(RichTree treeAssignedSystems) {
        this.treeAssignedSystems = treeAssignedSystems;
    }

    public RichTree getTreeAssignedSystems() {
        return treeAssignedSystems;
    }

    public void setSelectValues2(List<SelectItem> selectValues2) {
        this.selectValues2 = selectValues2;
    }

    public List<SelectItem> getSelectValues2() {
        return selectValues2;
    }

    public void setDisplayValue2(List<String> displayValue2) {
        this.displayValue2 = displayValue2;
    }

    public List<String> getDisplayValue2() {
        return displayValue2;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setUserRoles(RichSelectManyShuttle userRoles) {
        this.userRoles = userRoles;
    }

    public RichSelectManyShuttle getUserRoles() {
        return userRoles;
    }

    public void userRoleGrant(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setUserRolesSelect(UISelectItems userRolesSelect) {
        this.userRolesSelect = userRolesSelect;
    }

    public UISelectItems getUserRolesSelect() {
        return userRolesSelect;
    }

    public void setBtnAddUserSystem(RichCommandButton btnAddUserSystem) {
        this.btnAddUserSystem = btnAddUserSystem;
    }

    public RichCommandButton getBtnAddUserSystem() {
        return btnAddUserSystem;
    }

    public void setBtnRemoveUserSystem(RichCommandButton btnRemoveUserSystem) {
        this.btnRemoveUserSystem = btnRemoveUserSystem;
    }

    public RichCommandButton getBtnRemoveUserSystem() {
        return btnRemoveUserSystem;
    }

    public String processAddUserSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode") == null ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }

        if (txtSelectedUserSystemCode.getValue() == null ||
            txtSelectedUserSystemCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;

            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Grant_User_System(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedUserSystemCode.getValue().toString()));
                statement.setString(3,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedUserSystemCode.setValue(null);

        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedUserSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedSystems);

        ADFUtils.findIterator("fetchAllUserAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(tblAssignedSystems);

        ADFUtils.findIterator("fetchUserUnassignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedBranches);
        GlobalCC.refreshUI(treeAssignedBranches);


        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserNonDefaultDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedDivisions);
        GlobalCC.refreshUI(treeAssignedDivisions);
        GlobalCC.refreshUI(tblUserDivisionsPop);

        // Get the user roles
        ADFUtils.findIterator("fetchUnassignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("fetchAssignedUserRolesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedRoles);
        GlobalCC.refreshUI(treeAssignedRoles);

        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);


        return null;
    }

    public String actionselectTask() {
        Object key2 = tblTask.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            kpiTask.setValue(nodeBinding.getAttribute("kpiTask"));
            session.setAttribute("taskCode",
                                 nodeBinding.getAttribute("kpiTcktCode"));
            GlobalCC.refreshUI(kpiTask);

        }
        GlobalCC.dismissPopUp("crm", "KPITaskLovPopUp");
        return null;
    }


    public String actionselectTaskS() {
        Object key2 = tblSubTask.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            kpiSubTask.setValue(nodeBinding.getAttribute("kpiSubTask"));
            session.setAttribute("subTaskCode",
                                 nodeBinding.getAttribute("kpidbId"));
            GlobalCC.refreshUI(kpiSubTask);

        }
        GlobalCC.dismissPopUp("crm", "KPITaskLovPopUp2");
        return null;
    }


    public String actionCancelSubTask() {
        GlobalCC.dismissPopUp("crm", "KPITaskLovPopUp2");
        return null;
    }

    public String actionCancelTask() {
        GlobalCC.dismissPopUp("crm", "KPITaskLovPopUp");
        return null;
    }


    public String processRemoveUserSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode") == null ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }

        if (txtSelectedUserSystemCode.getValue() == null ||
            txtSelectedUserSystemCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Revoke_User_System(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedUserSystemCode.getValue().toString()));
                statement.setString(3,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedUserSystemCode.setValue(null);

        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedUserSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedSystems);

        ADFUtils.findIterator("fetchAllUserAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(tblAssignedSystems);

        ADFUtils.findIterator("fetchUnassignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("fetchAssignedUserRolesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedRoles);
        GlobalCC.refreshUI(treeAssignedRoles);

        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);

        return null;
    }

    public void unassignedSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedSystems.getRowData();

                    txtSelectedUserSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void assignedSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedSystems.getRowData();

                    txtSelectedUserSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void setTxtSelectedUserSystemCode(RichInputText txtSelectedUserSystemCode) {
        this.txtSelectedUserSystemCode = txtSelectedUserSystemCode;
    }

    public RichInputText getTxtSelectedUserSystemCode() {
        return txtSelectedUserSystemCode;
    }

    public void setDetailSystems(RichShowDetailItem detailSystems) {
        this.detailSystems = detailSystems;
    }

    public RichShowDetailItem getDetailSystems() {
        return detailSystems;
    }

    public void setPanelDetailSystems(RichPanelBox panelDetailSystems) {
        this.panelDetailSystems = panelDetailSystems;
    }

    public RichPanelBox getPanelDetailSystems() {
        return panelDetailSystems;
    }

    public void setPanelUserRoles(RichPanelBox panelUserRoles) {
        this.panelUserRoles = panelUserRoles;
    }

    public RichPanelBox getPanelUserRoles() {
        return panelUserRoles;
    }

    public void setTxtSelectedUserRoleCode(RichInputText txtSelectedUserRoleCode) {
        this.txtSelectedUserRoleCode = txtSelectedUserRoleCode;
    }

    public RichInputText getTxtSelectedUserRoleCode() {
        return txtSelectedUserRoleCode;
    }

    public void setBtnAddUserRole(RichCommandButton btnAddUserRole) {
        this.btnAddUserRole = btnAddUserRole;
    }

    public RichCommandButton getBtnAddUserRole() {
        return btnAddUserRole;
    }

    public void setBtnRemoveUseRole(RichCommandButton btnRemoveUseRole) {
        this.btnRemoveUseRole = btnRemoveUseRole;
    }

    public RichCommandButton getBtnRemoveUseRole() {
        return btnRemoveUseRole;
    }

    public void addUserRole(String rolecode) {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_roles_cursor.Grant_User_Role(?,?,?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(3, new BigDecimal(rolecode));
            statement.setString(4,
                                GlobalCC.checkNullValues(session.getAttribute("Username")));
            statement.execute();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
    }


    public String processAddUserRole() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode") == null ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }
        Object userRole = txtSelectedUserRoleCode.getValue();
        String sysCode =
            GlobalCC.checkNullValues(txtSelectedUserSysCode.getValue());
        if (userRole != null) {
            if (userRole.equals(null) || userRole == "") {
                processStatusOK = false;
                GlobalCC.errorValueNotEntered("You need to select a Role first.");
                return null;
            }

            if (processStatusOK) {
                assignRevokeUserRole("addRole", treeUnassignedRoles);
            }

            txtSelectedUserRoleCode.setValue(null);

            // Get the user systems

        } else if (sysCode != null) {

            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Grant_User_AllRoleInSys(?,?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(3, null);
                statement.setBigDecimal(4, new BigDecimal(sysCode));
                statement.setString(5,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();
                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need to First select  a Role .");
            return null;
        }
        ADFUtils.findIterator("fetchUnassignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("fetchAssignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);
        GlobalCC.refreshUI(treeUnassignedRoles);
        GlobalCC.refreshUI(treeAssignedRoles);


        return null;
    }

    public void removeUseRole(String roleCode) {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_roles_cursor.Revoke_User_Role(?,?,?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(3, new BigDecimal(roleCode));
            statement.setString(4,
                                GlobalCC.checkNullValues(session.getAttribute("Username")));
            statement.execute();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
    }

    public String processRemoveUseRole() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode").equals(null) ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
            return null;
        }
        Object userRole = txtSelectedUserRoleCode.getValue();
        String sysCode =
            GlobalCC.checkNullValues(txtSelectedUserSysCode.getValue());

        if (userRole != null) {
            assignRevokeUserRole("removeRole", treeAssignedRoles);

            if (userRole.equals(null) || userRole == "") {
                processStatusOK = false;
                GlobalCC.errorValueNotEntered("You need to select a Role first.");
                return null;
            }

            if (processStatusOK) {
                assignRevokeUserRole("removeRole", treeAssignedRoles);
            }
        } else if (sysCode != null) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Revoke_User_AllRoleInSys(?,?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(2,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                statement.setBigDecimal(3, null);
                statement.setBigDecimal(4, new BigDecimal(sysCode));
                statement.setString(5,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();
                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }

        } else {
            GlobalCC.errorValueNotEntered("You need to select a Role first.");
            return null;
        }
        txtSelectedUserRoleCode.setValue(null);

        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("fetchAssignedUserRolesIterator").executeQuery();
        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);
        GlobalCC.refreshUI(treeUnassignedRoles);
        GlobalCC.refreshUI(treeAssignedRoles);

        return null;
    }

    public void setTreeAssignedRoles(RichTree treeAssignedRoles) {
        this.treeAssignedRoles = treeAssignedRoles;
    }

    public RichTree getTreeAssignedRoles() {
        return treeAssignedRoles;
    }

    public void setTreeUnassignedRoles(RichTree treeUnassignedRoles) {
        this.treeUnassignedRoles = treeUnassignedRoles;
    }

    public RichTree getTreeUnassignedRoles() {
        return treeUnassignedRoles;
    }

    public void unassignedRolesSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedRoles.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedRoles.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "S") {
                        txtSelectedUserRoleCode.setValue(nd.getRow().getAttribute("roleCode"));
                        txtSelectedUserSysCode.setValue(null);
                    } else if (nd.getRow().getAttribute("nodeType") == "P") {
                        txtSelectedUserSysCode.setValue(nd.getRow().getAttribute("sysCode"));

                        txtSelectedUserRoleCode.setValue(null);
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelUserRoles);
    }

    public void assignedRolesSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedRoles.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedRoles.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "S") {
                        txtSelectedUserRoleCode.setValue(nd.getRow().getAttribute("roleCode"));
                        txtSelectedUserSysCode.setValue(null);
                    } else if (nd.getRow().getAttribute("nodeType") == "P") {
                        txtSelectedUserSysCode.setValue(nd.getRow().getAttribute("sysCode"));
                        txtSelectedUserRoleCode.setValue(null);
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelUserRoles);
    }

    public void setPanelDetailBranches(RichPanelBox panelDetailBranches) {
        this.panelDetailBranches = panelDetailBranches;
    }

    public RichPanelBox getPanelDetailBranches() {
        return panelDetailBranches;
    }

    public void setTxtSelectedUserBranchCode(RichInputText txtSelectedUserBranchCode) {
        this.txtSelectedUserBranchCode = txtSelectedUserBranchCode;
    }

    public RichInputText getTxtSelectedUserBranchCode() {
        return txtSelectedUserBranchCode;
    }

    public String assignUserBranch(String userCode, String branchCode) {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {

            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_roles_cursor.Grant_User_Branch(?,?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, new BigDecimal(userCode));
            statement.setBigDecimal(2, new BigDecimal(branchCode));
            statement.setString(3,
                                GlobalCC.checkNullValues(session.getAttribute("Username")));
            statement.execute();
            connection.commit();
            connection.close();
            return null;
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }

    }


    public void assignRevokeUserRole(String action, RichTree tree) {

        // Get access to the Tree Collection Model. The tree component instance
        // is accessed through its binding property reference to this managed bean
        CollectionModel treeModel = (CollectionModel)tree.getValue();

        // The CollectionModel is of type FacesModel, which is an inner class.
        // To get to the ADF tree binding, we can call wrappedData on the
        // Collection Model
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)treeModel.getWrappedData();

        //get the selected tree nodes
        RowKeySet rks = tree.getSelectedRowKeys();

        //Store original rowKey
        Object oldRowKey = tree.getRowKey();


        //If there is a tree node selected ...
        if (!rks.isEmpty() && rks != null) {

            // get first selected node as we assume this code to execute in a
            // single row selection use case. Iterate over the whole iterator if
            // you are in a multi node selection case
            List firstSet = (List)rks.iterator().next();

            // get the ADF node binding. If you want to access sub nodes, make
            // sure that you set the primary key attribute for the entity
            // representing this node. If you don't do this, the next line
            // throws a NullPointerException
            JUCtrlHierNodeBinding node =
                treeBinding.findNodeByKeyPath(firstSet);

            if (node != null) {
                DCDataRow rw = (DCDataRow)node.getRow();

                // The data provider is the object - the entity - that is used
                // to render the node. Get the object
                Object entity = rw.getDataProvider();

                node.getKeyPath();
                Row r = node.getRow();


                if (r.getAttribute("nodeType").equals("S")) {

                    String userCode =
                        session.getAttribute("sysUserCode") == null ? null :
                        session.getAttribute("sysUserCode").toString();
                    String branchCode = null;
                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;

                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];
                        boolean checked = false;

                        if (action.equals("addRole")) {
                            session.setAttribute("roleCode", null);
                            session.setAttribute("roleCode",
                                                 rwr.getAttribute("roleCode"));

                            checked =
                                    (Boolean)rwr.getAttribute("roleSelected");

                            if (checked) {
                                addUserRole(rwr.getAttribute("roleCode") ==
                                            null ? null :
                                            rwr.getAttribute("roleCode").toString());

                            }
                        } else if (action.equals("removeRole")) {
                            session.setAttribute("roleCode", null);
                            session.setAttribute("roleCode",
                                                 rwr.getAttribute("roleCode"));


                            checked =
                                    (Boolean)rwr.getAttribute("roleSelected");

                            int count = 0;
                            if (checked) {
                                removeUseRole(rwr.getAttribute("roleCode") ==
                                              null ? null :
                                              rwr.getAttribute("roleCode").toString());
                            }
                        }

                    }
                }

            }

        }
    }

    public String assignRevokeBranch(String action, RichTree tree) {

        // Get access to the Tree Collection Model. The tree component instance
        // is accessed through its binding property reference to this managed bean
        CollectionModel treeModel = (CollectionModel)tree.getValue();

        // The CollectionModel is of type FacesModel, which is an inner class.
        // To get to the ADF tree binding, we can call wrappedData on the
        // Collection Model
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)treeModel.getWrappedData();

        //get the selected tree nodes
        RowKeySet rks = tree.getSelectedRowKeys();

        //Store original rowKey


        //If there is a tree node selected ...
        if (!rks.isEmpty() && rks != null) {

            // get first selected node as we assume this code to execute in a
            // single row selection use case. Iterate over the whole iterator if
            // you are in a multi node selection case
            List firstSet = (List)rks.iterator().next();

            // get the ADF node binding. If you want to access sub nodes, make
            // sure that you set the primary key attribute for the entity
            // representing this node. If you don't do this, the next line
            // throws a NullPointerException
            JUCtrlHierNodeBinding node =
                treeBinding.findNodeByKeyPath(firstSet);

            if (node != null) {
                DCDataRow rw = (DCDataRow)node.getRow();

                // The data provider is the object - the entity - that is used
                // to render the node. Get the object
                Object entity = rw.getDataProvider();

                node.getKeyPath();
                Row r = node.getRow();


                if (r.getAttribute("nodeType").equals("SB")) {

                    String userCode =
                        session.getAttribute("sysUserCode") == null ? null :
                        session.getAttribute("sysUserCode").toString();
                    String branchCode = null;
                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;

                    String err = null;
                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];
                        boolean checked = false;

                        if (action.equals("addBrn")) {

                            session.setAttribute("branchCode", null);
                            session.setAttribute("branchCode",
                                                 rwr.getAttribute("branchCode"));


                            checked =
                                    (Boolean)rwr.getAttribute("branchSelected");

                            if (checked) {
                                assignUserBranch(userCode,
                                                 rwr.getAttribute("branchCode") ==
                                                 null ? null :
                                                 rwr.getAttribute("branchCode").toString());

                            }
                        } else if (action.equals("removeBrn")) {
                            String defaultBrn =
                                GlobalCC.checkNullValues(txtUserDefaultBranchCode.getValue());

                            session.setAttribute("branchCode", null);
                            session.setAttribute("branchCode",
                                                 rwr.getAttribute("branchCode"));


                            checked =
                                    (Boolean)rwr.getAttribute("branchSelected");

                            int count = 0;
                            if (checked) {
                                if (defaultBrn != null) {
                                    if (rwr.getAttribute("branchCode").toString().equalsIgnoreCase(defaultBrn)) {
                                        err = "Default Branch Not Removed:";
                                    } else {
                                        RemoveUserAssignedUserBranches(userCode,
                                                                       rwr.getAttribute("branchCode") ==
                                                                       null ?
                                                                       null :
                                                                       rwr.getAttribute("branchCode").toString());

                                    }
                                } else {
                                    RemoveUserAssignedUserBranches(userCode,
                                                                   rwr.getAttribute("branchCode") ==
                                                                   null ?
                                                                   null :
                                                                   rwr.getAttribute("branchCode").toString());

                                }

                            }
                        }

                    }
                    return err;
                }

            }

        }
        return null;
    }

    public String processAddUserBranch() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode").equals(null) ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }
        String orgCode =
            txtSelectedUserOrgCode.getValue() == null ? null : txtSelectedUserOrgCode.getValue().toString();
        String branchCode =
            txtSelectedUserBranchCode.getValue() == null ? null :
            txtSelectedUserBranchCode.getValue().toString();
        String regionCode =
            txtSelectedUserRegionCode.getValue() == null ? null :
            txtSelectedUserRegionCode.getValue().toString();
        String userCode =
            session.getAttribute("sysUserCode") == null ? null : session.getAttribute("sysUserCode").toString();


        if (branchCode != null) {
            if (processStatusOK) {
                assignRevokeBranch("addBrn", treeUnassignedBranches);
                //assignUserBranch(userCode,branchCode);
            }
        } else if (regionCode != null) {
            OrganizationDAO orgDAO = new OrganizationDAO();
            List<Branch> branchesList = new ArrayList<Branch>();
            branchesList =
                    orgDAO.fetchUnassignedBranchesByRegion(new BigDecimal(regionCode),
                                                           new BigDecimal(userCode));
            Iterator itr = branchesList.iterator();


            DBConnector connector = new DBConnector();
            OracleConnection connection = null;

            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Grant_User_Branch(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                while (itr.hasNext()) {
                    Branch branch = (Branch)itr.next();
                    statement.setBigDecimal(1, new BigDecimal(userCode));
                    statement.setBigDecimal(2, branch.getBranchCode());
                    statement.setString(3,
                                        GlobalCC.checkNullValues(session.getAttribute("Username")));
                    statement.execute();

                }

                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        } else if (orgCode != null) {
            OrganizationDAO orgDAO = new OrganizationDAO();


            DBConnector connector = new DBConnector();
            OracleConnection connection = null;

            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Grant_User_BranchByOrg(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);


                statement.setBigDecimal(1, new BigDecimal(userCode));
                statement.setBigDecimal(2, new BigDecimal(orgCode));
                statement.setString(3,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();


                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need to either select a Branch or Region first.");
        }

        txtSelectedUserBranchCode.setValue(null);
        txtSelectedUserRegionCode.setValue(null);

        // Get the user Branches
        ADFUtils.findIterator("fetchUserUnassignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedBranchesIterator").executeQuery();

        GlobalCC.refreshUI(treeUnassignedBranches);
        GlobalCC.refreshUI(treeAssignedBranches);

        // Refresh the tree for assigning the default branches
        ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblUserBranchesPop);

        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();

        GlobalCC.refreshUI(treeAssignedDivisions);
        GlobalCC.refreshUI(treeUnassignedDivisions);

        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);
        return null;
    }

    public void RemoveUserAssignedUserBranches(String userCode,
                                               String branchCode) {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_roles_cursor.Revoke_User_Branch(?,?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, new BigDecimal(userCode));
            statement.setBigDecimal(2, new BigDecimal(branchCode));
            statement.setString(3,
                                GlobalCC.checkNullValues(session.getAttribute("Username")));
            statement.execute();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
    }


    public String processRemoveUserBranch() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode").equals(null) ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }
        String orgCode =
            txtSelectedUserOrgCode.getValue() == null ? null : txtSelectedUserOrgCode.getValue().toString();
        String branchCode =
            txtSelectedUserBranchCode.getValue() == null ? null :
            txtSelectedUserBranchCode.getValue().toString();
        String regionCode =
            txtSelectedUserRegionCode.getValue() == null ? null :
            txtSelectedUserRegionCode.getValue().toString();
        String userCode =
            session.getAttribute("sysUserCode") == null ? null : session.getAttribute("sysUserCode").toString();
        String err = null;
        if (branchCode != null) {
            if (processStatusOK) {
                err = assignRevokeBranch("removeBrn", treeAssignedBranches);
            }
        } else if (regionCode != null) {
            OrganizationDAO orgDAO = new OrganizationDAO();
            List<Branch> branchesList = new ArrayList<Branch>();
            branchesList =
                    orgDAO.fetchAssignedBranchesByRegion(new BigDecimal(regionCode),
                                                         new BigDecimal(userCode));
            Iterator itr = branchesList.iterator();


            DBConnector connector = new DBConnector();
            OracleConnection connection = null;

            try {
                String defaultBrn =
                    GlobalCC.checkNullValues(txtUserDefaultBranchCode.getValue());
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Revoke_User_Branch(?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                while (itr.hasNext()) {
                    Branch branch = (Branch)itr.next();
                    if (defaultBrn != null) {
                        if (defaultBrn.equalsIgnoreCase(branch.getBranchCode().toString())) {
                            err = "Default Branch Not Removed";

                        } else {
                            statement.setBigDecimal(1,
                                                    new BigDecimal(userCode));
                            statement.setBigDecimal(2, branch.getBranchCode());
                            statement.setString(3,
                                                GlobalCC.checkNullValues(session.getAttribute("Username")));
                            statement.execute();
                        }
                    } else {

                    }

                }

                connection.commit();
                connection.close();
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                    return null;
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }

        } else if (orgCode != null) {
            OrganizationDAO orgDAO = new OrganizationDAO();
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;

            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.Revoke_User_BranchByOrg(?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);


                statement.setBigDecimal(1, new BigDecimal(userCode));
                statement.setBigDecimal(2, new BigDecimal(orgCode));
                statement.registerOutParameter(3, OracleTypes.VARCHAR);
                statement.setString(4,
                                    GlobalCC.checkNullValues(session.getAttribute("Username")));
                statement.execute();


                connection.commit();
                err = statement.getString(3);
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        } else {
            GlobalCC.errorValueNotEntered("You need First to either select a(an) Org, Branch or Region ");

        }
        txtSelectedUserBranchCode.setValue(null);
        txtSelectedUserRegionCode.setValue(null);

        // Get the user Branches
        ADFUtils.findIterator("fetchUserUnassignedBranchesIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedBranchesIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedBranches);
        GlobalCC.refreshUI(treeAssignedBranches);

        // Refresh the tree for assigning the default branches
        ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblUserBranchesPop);


        ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator").executeQuery();
        GlobalCC.refreshUI(tblUserBranchesPop);

        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();

        GlobalCC.refreshUI(treeAssignedDivisions);
        GlobalCC.refreshUI(treeUnassignedDivisions);

        ADFUtils.findIterator("findUsersIterator").executeQuery();
        GlobalCC.refreshUI(users);

        if (err != null) {
            GlobalCC.INFORMATIONREPORTING(err);
        }

        return null;
    }


    public void setTreeUnassignedBranches(RichTree treeUnassignedBranches) {
        this.treeUnassignedBranches = treeUnassignedBranches;
    }

    public RichTree getTreeUnassignedBranches() {
        return treeUnassignedBranches;
    }

    public void setTreeAssignedBranches(RichTree treeAssignedBranches) {
        this.treeAssignedBranches = treeAssignedBranches;
    }

    public RichTree getTreeAssignedBranches() {
        return treeAssignedBranches;
    }

    public void unassignedBranchesSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedBranches.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedBranches.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "SB") {
                        txtSelectedUserBranchCode.setValue(nd.getRow().getAttribute("branchCode"));
                        txtSelectedUserRegionCode.setValue(null);
                        txtSelectedUserOrgCode.setValue(null);
                    } else if (nd.getRow().getAttribute("nodeType") == "S") {
                        txtSelectedUserRegionCode.setValue(nd.getRow().getAttribute("regionCode"));
                        txtSelectedUserBranchCode.setValue(null);
                        txtSelectedUserOrgCode.setValue(null);
                    }


                    else if (nd.getRow().getAttribute("nodeType") == "P") {
                        txtSelectedUserOrgCode.setValue(nd.getRow().getAttribute("orgCode"));
                        txtSelectedUserBranchCode.setValue(null);
                        txtSelectedUserRegionCode.setValue(null);
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelDetailBranches);
    }

    public void assignedBranchesSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedBranches.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedBranches.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "SB") {
                        txtSelectedUserBranchCode.setValue(nd.getRow().getAttribute("branchCode"));
                        txtSelectedUserRegionCode.setValue(null);
                        txtSelectedUserOrgCode.setValue(null);
                    } else if (nd.getRow().getAttribute("nodeType") == "S") {
                        txtSelectedUserRegionCode.setValue(nd.getRow().getAttribute("regionCode"));
                        txtSelectedUserBranchCode.setValue(null);
                        txtSelectedUserOrgCode.setValue(null);
                    } else if (nd.getRow().getAttribute("nodeType") == "P") {
                        txtSelectedUserOrgCode.setValue(nd.getRow().getAttribute("orgCode"));
                        txtSelectedUserBranchCode.setValue(null);
                        txtSelectedUserRegionCode.setValue(null);
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelDetailBranches);
    }

    public void setPanelDetailDivisions(RichPanelBox panelDetailDivisions) {
        this.panelDetailDivisions = panelDetailDivisions;
    }

    public RichPanelBox getPanelDetailDivisions() {
        return panelDetailDivisions;
    }

    public void setTxtSelectedUserDivisionCode(RichInputText txtSelectedUserDivisionCode) {
        this.txtSelectedUserDivisionCode = txtSelectedUserDivisionCode;
    }

    public RichInputText getTxtSelectedUserDivisionCode() {
        return txtSelectedUserDivisionCode;
    }

    public String processAddUserDivision() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode") == null ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }

        if (txtSelectedUserDivisionCode.getValue() == null ||
            txtSelectedUserDivisionCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Division first.");
        }


        if (processStatusOK) {
            assignRevokeUserDivision("addUserDivision",
                                     treeUnassignedDivisions);
        }
        return null;
    }

    public String addUserDivision() {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_web_cursor.grant_user_division(?,?,?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, null);
            statement.setBigDecimal(2,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(3,
                                    new BigDecimal(txtSelectedUserDivisionCode.getValue().toString()));
            statement.setString(4, "N");
            statement.execute();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }


        txtSelectedUserDivisionCode.setValue(null);

        // Get the user Divisions
        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedDivisions);
        GlobalCC.refreshUI(treeAssignedDivisions);

        // Refresh the tree for assigning the default branches
        ADFUtils.findIterator("fetchUserNonDefaultDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(tblUserDivisionsPop);

        return null;
    }

    public String processRemoveUserDivision() {
        boolean processStatusOK = true;
        if (session.getAttribute("sysUserCode") == null ||
            (session.getAttribute("sysUserCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a user first.");
        }

        if (txtSelectedUserDivisionCode.getValue() == null ||
            txtSelectedUserDivisionCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Division first.");
        }

        if (processStatusOK) {
            assignRevokeUserDivision("removeUserDivision",
                                     treeAssignedDivisions);
        }
        return null;
    }

    public void setTreeUnassignedDivisions(RichTree treeUnassignedDivisions) {
        this.treeUnassignedDivisions = treeUnassignedDivisions;
    }

    public RichTree getTreeUnassignedDivisions() {
        return treeUnassignedDivisions;
    }

    public void setTreeAssignedDivisions(RichTree treeAssignedDivisions) {
        this.treeAssignedDivisions = treeAssignedDivisions;
    }

    public RichTree getTreeAssignedDivisions() {
        return treeAssignedDivisions;
    }

    public void unassignedDivisionsSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedDivisions.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedDivisions.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "SBB") {
                        txtSelectedUserDivisionCode.setValue(nd.getRow().getAttribute("DIV_CODE"));
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelDetailDivisions);
    }

    public void assignedDivisionsSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedDivisions.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedDivisions.getRowData();

                    if (nd.getRow().getAttribute("nodeType") == "SBB") {
                        txtSelectedUserDivisionCode.setValue(nd.getRow().getAttribute("DIV_CODE"));
                    }
                }
            }
        }
        GlobalCC.refreshUI(panelDetailDivisions);
    }

    public void setTxtUserDefaultBranch(RichInputText txtUserDefaultBranch) {
        this.txtUserDefaultBranch = txtUserDefaultBranch;
    }

    public RichInputText getTxtUserDefaultBranch() {
        return txtUserDefaultBranch;
    }

    public void setTxtUserDefaultDivision(RichInputText txtUserDefaultDivision) {
        this.txtUserDefaultDivision = txtUserDefaultDivision;
    }

    public RichInputText getTxtUserDefaultDivision() {
        return txtUserDefaultDivision;
    }

    public void setPanelDefaultBranch(RichPanelBox panelDefaultBranch) {
        this.panelDefaultBranch = panelDefaultBranch;
    }

    public RichPanelBox getPanelDefaultBranch() {
        return panelDefaultBranch;
    }

    public void setPanelDefaultDivision(RichPanelBox panelDefaultDivision) {
        this.panelDefaultDivision = panelDefaultDivision;
    }

    public RichPanelBox getPanelDefaultDivision() {
        return panelDefaultDivision;
    }

    public void setTblUserBranchesPop(RichTable tblUserBranchesPop) {
        this.tblUserBranchesPop = tblUserBranchesPop;
    }

    public RichTable getTblUserBranchesPop() {
        return tblUserBranchesPop;
    }

    public void setTblUserDivisionsPop(RichTable tblUserDivisionsPop) {
        this.tblUserDivisionsPop = tblUserDivisionsPop;
    }

    public RichTable getTblUserDivisionsPop() {
        return tblUserDivisionsPop;
    }

    public String actionChangeUserDefaultBranch() {
        if (session.getAttribute("sysUserCode").equals(null) ||
            session.getAttribute("sysUserCode").equals("")) {
            GlobalCC.errorValueNotEntered("You need to first select a User!");

        } else {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:userBranchesPop" + "').show(hints);");
        }
        return null;
    }

    public String actionChangeUserDefaultDivision() {
        if (session.getAttribute("sysUserCode").equals(null) ||
            session.getAttribute("sysUserCode").equals("")) {
            GlobalCC.errorValueNotEntered("You need to first select a User!");

        } else {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:userDivisionsPop" + "').show(hints);");
        }
        return null;
    }

    public String actionSaveDefaultBranch() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchUserNonDefaultBranchesIterator");
        RowKeySet set = tblUserBranchesPop.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            BigDecimal userCode =
                (BigDecimal)session.getAttribute("sysUserCode");
            String branchCode = (String)rows.getAttribute("branchCode");

            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.assign_user_default_branch(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1, userCode);
                statement.setBigDecimal(2, new BigDecimal(branchCode));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
            txtUserDefaultBranch.setValue(rows.getAttribute("branchName"));
            txtUserDefaultBranchCode.setValue(rows.getAttribute("branchCode"));
            GlobalCC.refreshUI(panelDefaultBranch);
        }
        return null;
    }

    public String actionSaveDefaultDivision() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchUserNonDefaultDivisionsIterator");
        RowKeySet set = tblUserDivisionsPop.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            BigDecimal userCode =
                (BigDecimal)session.getAttribute("sysUserCode");
            String divisionCode = (String)rows.getAttribute("divisionCode");

            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.assign_user_default_division(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1, userCode);
                statement.setBigDecimal(2, new BigDecimal(divisionCode));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
            txtUserDefaultDivision.setValue(rows.getAttribute("divisionName"));
            GlobalCC.refreshUI(panelDefaultDivision);
        }
        return null;
    }

    public void setSysConfirmPassword(RichInputText sysConfirmPassword) {
        this.sysConfirmPassword = sysConfirmPassword;
    }

    public RichInputText getSysConfirmPassword() {
        return sysConfirmPassword;
    }

    public String actionShowKPIPop() {

        if (session.getAttribute("grpUserCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select User Group");
            return null;
        } else {

            GlobalCC.showPopup("crm:p1k");
        }
        return null;
    }

    public String actionEditKPIPop() {        
        Object key2 = tblKpiTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            kpiTask.setValue(nodeBinding.getAttribute("kpiTask"));
            kpiSubTask.setValue(nodeBinding.getAttribute("kpiSubTask"));
            kpiParam.setValue(nodeBinding.getAttribute("kpiParameter"));
            kpiUnit.setValue(nodeBinding.getAttribute("kpiUnit"));
            kpiComment.setValue(nodeBinding.getAttribute("kpiComment"));
            
            session.setAttribute("taskCode", nodeBinding.getAttribute("kpiTaskCode"));
            session.setAttribute("subTaskCode", nodeBinding.getAttribute("kpiSubTaskCode"));     
            session.setAttribute("userGroupCode", nodeBinding.getAttribute("kpiUserGroupCode")); 
            session.setAttribute("kipCode", nodeBinding.getAttribute("kpiCode"));
            
           
            
            createKpiBtn.setText("Update");     
            GlobalCC.refreshUI(createKpiBtn);

            // Open the popup dialog for Services
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:p1k" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;

    }


    public String actionDropKPIPop() {
          
              Object key2 = tblKpiTab.getSelectedRowData();
              JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

              if (nodeBinding != null) {
                  ExtendedRenderKitService erkService =
                      Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                         ExtendedRenderKitService.class);
                  erkService.addScript(FacesContext.getCurrentInstance(),
                                       "var hints = {autodismissNever:false}; " +
                                       "AdfPage.PAGE.findComponent('" +
                                       "crm:confirmDeleteKPI" +
                                       "').show(hints);");
              } else {
                  GlobalCC.INFORMATIONREPORTING("No record Selected");
              }
      
          return null;
          }
    
    public void actionConfirmDeleteKPI(DialogEvent dialogEvent){
        
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
                } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
                    Object key2 = tblKpiTab.getSelectedRowData();
                    JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

                    if (nodeBinding != null) {

                        BigDecimal kpiCode =   GlobalCC.checkBDNullValues(nodeBinding.getAttribute("kpiCode"));
                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn;
                        conn = dbConnector.getDatabaseConnection();
                        try {
                            String query = null;
                            //modify the query for treaty groups procedure
                            query =
                                    "BEGIN TQC_SETUPS_PKG.delete_KPI(?,?);END;";

                            OracleCallableStatement cst = null;
                            cst = (OracleCallableStatement)conn.prepareCall(query);
                            cst.setBigDecimal(1, kpiCode);
                            cst.registerOutParameter(2, OracleTypes.VARCHAR);
                            cst.execute();
                            String errMessage = cst.getString(2);
                            if (errMessage != null) {
                                GlobalCC.INFORMATIONREPORTING(errMessage);
                            } else {
                                ADFUtils.findIterator("fetchKpisIterator").executeQuery();
                                GlobalCC.refreshUI(tblKpiTab);
                                GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully");
                            }
                            conn.close();
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(conn, e);

                        }
                    } else {
                        GlobalCC.INFORMATIONREPORTING("NO RECORD SELECTED");
                    }
                }
    }

    public String actionShowCreateUserPop() {
        /*String type = "NORMAL";
        if(session.getAttribute("SIGN_IN_MODE")!=null){
          type = session.getAttribute("SIGN_IN_MODE").toString();
        }
        if(type.equalsIgnoreCase("LDAP")){
          ldapUser.setRendered(true);
          sysUsername.setDisabled(true);
        }else{*/
        ldapUser.setRendered(false);
        sysUsername.setDisabled(false);
        // }
        String autoCreatePwd =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_CREATE_PWD");
        boolean autoCreate = "Y".equalsIgnoreCase(autoCreatePwd);
        //"Y".equalsIgnoreCase(autoCreatePwd)
        sysStatus.setValue("I");
        sysType.setValue("U");
        sysUsername.setValue(null);
        sysFullname.setValue(null);
        sysEmail.setValue(null);
        sysPassReset.setSelected(!autoCreate);
        sysPassword.setValue(null);
        sysPassword.setVisible(!autoCreate);
        sysConfirmPassword.setValue(null);
        sysConfirmPassword.setVisible(!autoCreate);
        sysPersonelRank.setValue(null);
        txtPersonnelName.setValue(null);
        txtPersonnelId.setValue(null);
        this.UploadedImageFile = null;
        txtAccManager.setValue("N");
        //sysConfirmPassword.setDisabled(false);
        session.setAttribute("sysUserCode", null);
        editBtn.setRendered(false);
        createBtn.setRendered(true);
        lblWefdate.setRendered(true);
        txtWefdate.setRendered(true);
        txtWefdate.setValue(new java.util.Date());
        txtWetDate.setValue(null);
        txtPhoneNumber.setValue(null);
        txtSactCode.setValue(null);
        txtSactName.setValue(null);
        txtPin.setValue(null);
        txtUserAuthorized.setValue(null);
        
        txtUserAuthorized.setValue("NO");


        GlobalCC.refreshUI(txtUserAuthorized);
        GlobalCC.refreshUI(lblWefdate);
        GlobalCC.refreshUI(txtWefdate);
        GlobalCC.refreshUI(sysStatus);
        GlobalCC.refreshUI(sysStatus);


        GlobalCC.refreshUI(txtSecurityAnswer);
        GlobalCC.refreshUI(txtWetDate);


        GlobalCC.showPopup("crm:p1");


        return null;
    }

    public void setErrorDisplayTxt(RichOutputLabel errorDisplayTxt) {
        this.errorDisplayTxt = errorDisplayTxt;
    }

    public RichOutputLabel getErrorDisplayTxt() {
        return errorDisplayTxt;
    }

    public String actionAcceptPersonnel() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchAllPersonnelsIterator");
        RowKeySet set = tblPersonnel.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            txtPersonnelId.setValue(r.getAttribute("perId"));
            txtPersonnelName.setValue(r.getAttribute("perFullNames"));
            GlobalCC.refreshUI(txtPersonnelName);


        }

        // Open the popup dialog
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:personnelPop').hide(hints);");
        return null;
    }

    public void setTblPersonnel(RichTable tblPersonnel) {
        this.tblPersonnel = tblPersonnel;
    }

    public RichTable getTblPersonnel() {
        return tblPersonnel;
    }

    public String showPersonnelPopup() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:personnelPop').show(hints);");
        return null;
    }

    public void setTxtPersonnelId(RichInputText txtPersonnelId) {
        this.txtPersonnelId = txtPersonnelId;
    }

    public RichInputText getTxtPersonnelId() {
        return txtPersonnelId;
    }

    public void setTxtPersonnelName(RichInputText txtPersonnelName) {
        this.txtPersonnelName = txtPersonnelName;
    }

    public RichInputText getTxtPersonnelName() {
        return txtPersonnelName;
    }

    public String actionCancelPersonnel() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:p1').show(hints);");
        return null;
    }

    public void setTxtSelectedUserRegionCode(RichInputText txtSelectedUserRegionCode) {
        this.txtSelectedUserRegionCode = txtSelectedUserRegionCode;
    }

    public RichInputText getTxtSelectedUserRegionCode() {
        return txtSelectedUserRegionCode;
    }

    public void setTblAssignedSystems(RichTable tblAssignedSystems) {
        this.tblAssignedSystems = tblAssignedSystems;
    }

    public RichTable getTblAssignedSystems() {
        return tblAssignedSystems;
    }

    public void tblAssignedSystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblAssignedSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode",
                                 nodeBinding.getAttribute("sysCode"));
            txtSelectedUserSystemCode.setValue(nodeBinding.getAttribute("sysCode"));

            ADFUtils.findIterator("fetchSystemPostsBySystemIterator").executeQuery();
            GlobalCC.refreshUI(tblSystemPostsPop);
        }
    }

    public void setTxtUsysCode(RichInputText txtUsysCode) {
        this.txtUsysCode = txtUsysCode;
    }

    public RichInputText getTxtUsysCode() {
        return txtUsysCode;
    }

    public void setTxtUsysUsrCode(RichInputText txtUsysUsrCode) {
        this.txtUsysUsrCode = txtUsysUsrCode;
    }

    public RichInputText getTxtUsysUsrCode() {
        return txtUsysUsrCode;
    }

    public void setTxtUsysSysCode(RichInputText txtUsysSysCode) {
        this.txtUsysSysCode = txtUsysSysCode;
    }

    public RichInputText getTxtUsysSysCode() {
        return txtUsysSysCode;
    }

    public void setTxtUsysWef(RichInputDate txtUsysWef) {
        this.txtUsysWef = txtUsysWef;
    }

    public RichInputDate getTxtUsysWef() {
        return txtUsysWef;
    }

    public void setTxtUsysWet(RichInputDate txtUsysWet) {
        this.txtUsysWet = txtUsysWet;
    }

    public RichInputDate getTxtUsysWet() {
        return txtUsysWet;
    }

    public void setTxtUsysSpostCode(RichInputText txtUsysSpostCode) {
        this.txtUsysSpostCode = txtUsysSpostCode;
    }

    public RichInputText getTxtUsysSpostCode() {
        return txtUsysSpostCode;
    }

    public void setTxtSpostDesc(RichInputText txtSpostDesc) {
        this.txtSpostDesc = txtSpostDesc;
    }

    public RichInputText getTxtSpostDesc() {
        return txtSpostDesc;
    }

    public void setBtnSaveUpdateUserSystem(RichCommandButton btnSaveUpdateUserSystem) {
        this.btnSaveUpdateUserSystem = btnSaveUpdateUserSystem;
    }

    public RichCommandButton getBtnSaveUpdateUserSystem() {
        return btnSaveUpdateUserSystem;
    }

    public void setPanelUserSystem(RichPanelBox panelUserSystem) {
        this.panelUserSystem = panelUserSystem;
    }

    public RichPanelBox getPanelUserSystem() {
        return panelUserSystem;
    }

    public String actionUpdateUserSystem() {
        Object key2 = tblAssignedSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtUsysCode.setValue(nodeBinding.getAttribute("usysCode"));
            txtUsysUsrCode.setValue(nodeBinding.getAttribute("usysUsrCode"));
            txtUsysSysCode.setValue(nodeBinding.getAttribute("usysSysCode"));
            txtUsysWef.setValue(nodeBinding.getAttribute("usysWef"));
            txtUsysWet.setValue(nodeBinding.getAttribute("usysWet"));
            txtUsysSpostCode.setValue(nodeBinding.getAttribute("usysSpostCode"));
            txtSpostDesc.setValue(nodeBinding.getAttribute("spostDesc"));

            btnSaveUpdateUserSystem.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:userSystemPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateUserSystem() {
        if (btnSaveUpdateUserSystem.getText().equals("Edit")) {
            actionEditUserSystem();
        } else {
            // Saving at this point is not allowed
        }
        return null;
    }

    public String actionEditUserSystem() {
        String code = GlobalCC.checkNullValues(txtUsysCode.getValue());
        String usrCode = GlobalCC.checkNullValues(txtUsysUsrCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtUsysSysCode.getValue());
        String wef = GlobalCC.checkNullValues(txtUsysWef.getValue());
        String wet = GlobalCC.checkNullValues(txtUsysWet.getValue());
        String spostCode =
            GlobalCC.checkNullValues(txtUsysSpostCode.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (usrCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: User Code is Empty");
            return null;

        } else if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.userSystems_prc(?,?); end;";

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtUsysWef.getValue() != null &&
                    !(txtUsysWef.getValue().equals(""))) {
                    String date1 = df.format(txtUsysWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                Date tmpWetDate = new Date();
                if (txtUsysWet.getValue() != null &&
                    !(txtUsysWet.getValue().equals(""))) {
                    String date1 = df.format(txtUsysWet.getValue());
                    tmpWetDate = df.parse(date1);
                }

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_USER_SYSTEMS_TAB",
                                                     conn);
                ArrayList userSystemsList = new ArrayList();
                UserSystem userSystem = new UserSystem();
                userSystem.setSQLTypeName("TQC_USER_SYSTEMS_OBJ");

                userSystem.setUsysCode(code == null ? null :
                                       new BigDecimal(code));
                userSystem.setUsysUsrCode(usrCode == null ? null :
                                          new BigDecimal(usrCode));
                userSystem.setUsysSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));
                userSystem.setUsysWef(tmpWefDate == null ? null :
                                      new java.sql.Date(tmpWefDate.getTime()));
                userSystem.setUsysWet(tmpWetDate == null ? null :
                                      new java.sql.Date(tmpWetDate.getTime()));
                userSystem.setUsysSpostCode(spostCode == null ? null :
                                            new BigDecimal(spostCode));

                userSystemsList.add(userSystem);
                ARRAY array =
                    new ARRAY(descriptor, conn, userSystemsList.toArray());

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
                                     "crm:userSystemPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchAllUserAssignedSystemsIterator").executeQuery();
                GlobalCC.refreshUI(tblAssignedSystems);

                //clearDivLevelTypeFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTblSystemPostsPop(RichTable tblSystemPostsPop) {
        this.tblSystemPostsPop = tblSystemPostsPop;
    }

    public RichTable getTblSystemPostsPop() {
        return tblSystemPostsPop;
    }

    public String actionAcceptSystemPost() {
        Object key2 = tblSystemPostsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtUsysSpostCode.setValue(nodeBinding.getAttribute("spostCode"));
            txtSpostDesc.setValue(nodeBinding.getAttribute("spostDesc"));
        }

        GlobalCC.refreshUI(panelUserSystem);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemPostsPop" + "').hide(hints);");
        return null;
    }

    public String actionShowSystemPostsLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemPostsPop" + "').show(hints);");
        return null;
    }

    public void tblGroupUsers(SelectionEvent selectionEvent) {
        Object key2 = tblGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("grpUserCode",
                                 nodeBinding.getAttribute("userCode"));
        }
        ADFUtils.findIterator("findUsersGroupMembersIterator").executeQuery();
        GlobalCC.refreshUI(tblGroupMembers);

    }

    public String actionNewGroupMember() {

        if (session.getAttribute("grpUserCode") != null) {
            ADFUtils.findIterator("fetchUnassignedUserSystemsIterator").executeQuery();
            GlobalCC.refreshUI(tbAddUserToGroup);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:addUserToGroup" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("First select the group ");
            return null;
        }

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
                if (nodeBinding.getAttribute(attribute) != null) {
                    if (nodeBinding.getAttribute(attribute).toString().equalsIgnoreCase("true")) {

                        count = count + 1;
                    }
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

    public String selectALLuser() {
        selectAll(tbAddUserToGroup, chkUserColSelected);
        return null;
    }

    public String deSelectALLuser() {
        deselectAll(tbAddUserToGroup, chkUserColSelected, "userSelected");
        return null;
    }

    public String selectALLuserToRemove() {
        selectAll(tblGroupMembers, userToRemoveSelected);
        return null;
    }

    public String deSelectALLuserToRemove() {
        deselectAll(tblGroupMembers, userToRemoveSelected, "userSelected");
        return null;
    }

    public String actionAcceptUserLov() {


        if (checkIfAnyTableRowselected(tbAddUserToGroup, "userSelected")) {
            int rowcount = tbAddUserToGroup.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tbAddUserToGroup.setRowIndex(i);
                Object key = tbAddUserToGroup.getRowKey();
                tbAddUserToGroup.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tbAddUserToGroup.getRowData();


                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("userSelected");

                    if (selected) {

                        if (session.getAttribute("grpUserCode") != null) {


                            DBConnector dbConnector = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement statement = null;
                            try {
                                conn = dbConnector.getDatabaseConnection();
                                String query =
                                    "begin TQC_SETUPS_PKG.userGroup_prc(?,?,?,?); end;";


                                statement =
                                        (OracleCallableStatement)conn.prepareCall(query);
                                statement.setString(1, "A");
                                statement.setBigDecimal(2, null);
                                statement.setBigDecimal(3,
                                                        new BigDecimal(session.getAttribute("grpUserCode").toString()));
                                statement.setBigDecimal(4,
                                                        new BigDecimal(nodeBinding.getAttribute("userCode").toString()));
                                statement.execute();

                                statement.close();
                                conn.commit();
                                conn.close();


                            } catch (Exception e) {

                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return null;
                            }

                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return null;
                        }
                    } //END IF
                } else {
                    GlobalCC.INFORMATIONREPORTING("No record  selected ::");
                    return null;
                } //END ELSE

            } //END FOR
            GlobalCC.INFORMATIONREPORTING("User(S) Added Successfully");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }

        ADFUtils.findIterator("findUsersGroupMembersIterator").executeQuery();
        ADFUtils.findIterator("findAllUsersNotInGroupIterator").executeQuery();
        GlobalCC.refreshUI(tbAddUserToGroup);
        GlobalCC.refreshUI(tblGroupMembers);

        return null;
    }

    public String actionHideUserGroupLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:addUserToGroup" + "').hide(hints);");
        return null;
    }


    public String actionDeleteGroupMember() {

        if (checkIfAnyTableRowselected(tblGroupMembers, "userSelected")) {
            int rowcount = tblGroupMembers.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblGroupMembers.setRowIndex(i);
                Object key = tblGroupMembers.getRowKey();
                tblGroupMembers.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblGroupMembers.getRowData();


                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("userSelected");

                    if (selected) {


                        if (session.getAttribute("grpUserCode") != null) {


                            DBConnector dbConnector = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement statement = null;
                            try {
                                conn = dbConnector.getDatabaseConnection();
                                String query =
                                    "begin TQC_SETUPS_PKG.userGroup_prc(?,?,?,?); end;";


                                statement =
                                        (OracleCallableStatement)conn.prepareCall(query);
                                statement.setString(1, "D");
                                statement.setBigDecimal(2,
                                                        new BigDecimal(nodeBinding.getAttribute("gusr_Code").toString()));
                                statement.setBigDecimal(3, null);
                                statement.setBigDecimal(4, null);
                                statement.execute();

                                statement.close();
                                conn.commit();
                                conn.close();


                            } catch (Exception e) {

                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return null;
                            }

                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return null;
                        }
                    }
                } //end  if nodebinding null
                else {
                    GlobalCC.INFORMATIONREPORTING("No record selected::");
                    return null;
                }

            } //end for
            GlobalCC.INFORMATIONREPORTING("User(s) Removed Successfully");
            ADFUtils.findIterator("findUsersGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblGroupMembers);
        }
        return null;
    }

    public void actionConfirmDeleteUserFromGroup(DialogEvent dialogEvent) {

        if (session.getAttribute("grpUserCode") != null) {

            if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
                actionDeleteGroupMember();
                GlobalCC.INFORMATIONREPORTING("User Successfully Set to Inactivate!");
            }

            // Add event code here...

        }
    }

    public void actionConfirmDeleteUser(DialogEvent dialogEvent) {


        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DropUser();
        }

        // Add event code here...


    }

    public String actionShowDeleteGrpMemberPop() {


        if (checkIfAnyTableRowselected(tblGroupMembers, "userSelected")) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteUserFromgroup" +
                                 "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
    }

    public String actionShowDropUser() {
        Object key2 = users.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteUser" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected:");
            return null;
        }
    }

    public void setTblGroupMembers(RichTable tblGroupMembers) {
        this.tblGroupMembers = tblGroupMembers;
    }

    public RichTable getTblGroupMembers() {
        return tblGroupMembers;
    }

    public void setBtnNewMember(RichCommandButton btnNewMember) {
        this.btnNewMember = btnNewMember;
    }

    public RichCommandButton getBtnNewMember() {
        return btnNewMember;
    }

    public void setBtnDeleteGroupMember(RichCommandButton btnDeleteGroupMember) {
        this.btnDeleteGroupMember = btnDeleteGroupMember;
    }

    public void setTblGroup(RichTable tblGroup) {
        this.tblGroup = tblGroup;
    }

    public RichTable getTblGroup() {
        return tblGroup;
    }


    public RichCommandButton getBtnDeleteGroupMember() {
        return btnDeleteGroupMember;
    }

    public void setTbAddUserToGroup(RichTable tbAddUserToGroup) {
        this.tbAddUserToGroup = tbAddUserToGroup;
    }

    public RichTable getTbAddUserToGroup() {
        return tbAddUserToGroup;
    }


    public void setTxtSelectedUserOrgCode(RichInputText txtSelectedUserOrgCode) {
        this.txtSelectedUserOrgCode = txtSelectedUserOrgCode;
    }

    public RichInputText getTxtSelectedUserOrgCode() {
        return txtSelectedUserOrgCode;
    }

    public void setTxtSelectedUserSysCode(RichInputText txtSelectedUserSysCode) {
        this.txtSelectedUserSysCode = txtSelectedUserSysCode;
    }

    public RichInputText getTxtSelectedUserSysCode() {
        return txtSelectedUserSysCode;
    }

    public void setBranchSelected(RichSelectBooleanCheckbox branchSelected) {
        this.branchSelected = branchSelected;
    }

    public RichSelectBooleanCheckbox getBranchSelected() {
        return branchSelected;
    }

    public void setBranchToBeRemoved(RichSelectBooleanCheckbox branchToBeRemoved) {
        this.branchToBeRemoved = branchToBeRemoved;
    }

    public RichSelectBooleanCheckbox getBranchToBeRemoved() {
        return branchToBeRemoved;
    }

    public void setRoleToBeAdded(RichSelectBooleanCheckbox roleToBeAdded) {
        this.roleToBeAdded = roleToBeAdded;
    }

    public RichSelectBooleanCheckbox getRoleToBeAdded() {
        return roleToBeAdded;
    }

    public void setRoleToBeRemoved(RichSelectBooleanCheckbox roleToBeRemoved) {
        this.roleToBeRemoved = roleToBeRemoved;
    }

    public RichSelectBooleanCheckbox getRoleToBeRemoved() {
        return roleToBeRemoved;
    }

    public void setTxtUserDefaultBranchCode(RichInputText txtUserDefaultBranchCode) {
        this.txtUserDefaultBranchCode = txtUserDefaultBranchCode;
    }

    public RichInputText getTxtUserDefaultBranchCode() {
        return txtUserDefaultBranchCode;
    }

    public void setChkUserSelected(RichColumn chkUserSelected) {
        this.chkUserSelected = chkUserSelected;
    }

    public RichColumn getChkUserSelected() {
        return chkUserSelected;
    }

    public void setChkUserColSelected(RichSelectBooleanCheckbox chkUserColSelected) {
        this.chkUserColSelected = chkUserColSelected;
    }

    public RichSelectBooleanCheckbox getChkUserColSelected() {
        return chkUserColSelected;
    }

    public void setUserToRemoveSelected(RichSelectBooleanCheckbox userToRemoveSelected) {
        this.userToRemoveSelected = userToRemoveSelected;
    }

    public RichSelectBooleanCheckbox getUserToRemoveSelected() {
        return userToRemoveSelected;
    }

    public void actionCreatedDateListener(ValueChangeEvent valueChangeEvent) {
        RichInputDate component =
            (RichInputDate)valueChangeEvent.getComponent();
        String id = component.getId();
        String DateCreated = GlobalCC.checkNullValues(component.getValue());
        if (id.equalsIgnoreCase("idUserGroup")) {
            session.setAttribute("usrGrpDate", null);
            if (DateCreated != null && DateCreated.trim().length() > 0) {
                String date = GlobalCC.parseNormalDate(DateCreated);
                session.setAttribute("usrGrpDate", date);
            }
            ADFUtils.findIterator("findGroupUserIterator").executeQuery();
            GlobalCC.refreshUI(tblGroup);
        }
        if (id.equalsIgnoreCase("idUserCreatedDate")) {
            session.setAttribute("idUserCreatedDate", null);

            if (DateCreated != null && DateCreated.trim().length() > 0) {
                String date = GlobalCC.parseNormalDate(DateCreated);
                session.setAttribute("idUserCreatedDate", date);
            }
            ADFUtils.findIterator("findUsersIterator").executeQuery();
            GlobalCC.refreshUI(users);
        }

    }

    public void setPnTabbedUserAccess(RichPanelTabbed pnTabbedUserAccess) {
        this.pnTabbedUserAccess = pnTabbedUserAccess;
    }

    public RichPanelTabbed getPnTabbedUserAccess() {
        return pnTabbedUserAccess;
    }

    public void actionUsersDisclosed(DisclosureEvent disclosureEvent) {

        pnTabbedUserAccess.setVisible(false);
        GlobalCC.refreshUI(pnTabbedUserAccess);
    }

    public void setDivToBeAdded(RichSelectBooleanCheckbox divToBeAdded) {
        this.divToBeAdded = divToBeAdded;
    }

    public RichSelectBooleanCheckbox getDivToBeAdded() {
        return divToBeAdded;
    }

    public void setDivToBeRemoved(RichSelectBooleanCheckbox divToBeRemoved) {
        this.divToBeRemoved = divToBeRemoved;
    }

    public RichSelectBooleanCheckbox getDivToBeRemoved() {
        return divToBeRemoved;
    }

    private String assignRevokeUserDivision(String action,
                                            RichTree treeUnassignedDivisions) {
        // Get access to the Tree Collection Model. The tree component instance
        // is accessed through its binding property reference to this managed bean
        CollectionModel treeModel =
            (CollectionModel)treeUnassignedDivisions.getValue();

        // The CollectionModel is of type FacesModel, which is an inner class.
        // To get to the ADF tree binding, we can call wrappedData on the
        // Collection Model
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)treeModel.getWrappedData();

        //get the selected tree nodes
        RowKeySet rks = treeUnassignedDivisions.getSelectedRowKeys();

        //Store original rowKey


        //If there is a tree node selected ...
        if (!rks.isEmpty() && rks != null) {

            // get first selected node as we assume this code to execute in a
            // single row selection use case. Iterate over the whole iterator if
            // you are in a multi node selection case
            List firstSet = (List)rks.iterator().next();

            // get the ADF node binding. If you want to access sub nodes, make
            // sure that you set the primary key attribute for the entity
            // representing this node. If you don't do this, the next line
            // throws a NullPointerException
            JUCtrlHierNodeBinding node =
                treeBinding.findNodeByKeyPath(firstSet);

            if (node != null) {

                node.getKeyPath();
                Row r = node.getRow();

                if (r.getAttribute("nodeType").equals("SBB")) {

                    Row rows[] = node.getAllRowsInRange();
                    int i, z = 0;

                    String err = null;
                    for (i = 0; i <= rows.length - 1; i++) {
                        Row rwr = rows[i];
                        boolean checked = false;
                        txtSelectedUserDivisionCode.setValue(rwr.getAttribute("DIV_CODE"));
                        checked =
                                (Boolean)rwr.getAttribute("divisionSelected");
                        if (action.equalsIgnoreCase("addUserDivision")) {

                            if (checked) {
                                addUserDivision();
                            }
                        } else if (action.equals("removeUserDivision")) {
                            if (checked) {
                                removeUserDivision();
                            }

                        }

                    }

                }
            }
        }

        txtSelectedUserDivisionCode.setValue(null);

        // Get the user Divisions
        ADFUtils.findIterator("fetchUserUnassignedDivisionsIterator").executeQuery();
        ADFUtils.findIterator("fetchUserAssignedDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedDivisions);
        GlobalCC.refreshUI(treeAssignedDivisions);

        return null;
    }

    private void removeUserDivision() {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = connector.getDatabaseConnection();
            String query =
                "begin tqc_web_cursor.revoke_user_division(?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.setBigDecimal(1,
                                    (BigDecimal)session.getAttribute("sysUserCode"));
            statement.setBigDecimal(2,
                                    new BigDecimal(txtSelectedUserDivisionCode.getValue().toString()));

            statement.execute();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }


    }

    public void setTxtAccManager(RichSelectOneChoice txtAccManager) {
        this.txtAccManager = txtAccManager;
    }

    public RichSelectOneChoice getTxtAccManager() {
        return txtAccManager;
    }

    public void ImageSignatureUploadedListener(ValueChangeEvent valueChangeEvent) {
        if (session.getAttribute("sysUserCode") != null) {
            if (valueChangeEvent.getNewValue() !=
                valueChangeEvent.getOldValue()) {
                UploadedFile _file =
                    (UploadedFile)valueChangeEvent.getNewValue();
                InputStream Reader;
                this.UploadedImageFile = _file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    System.out.println(" UploadedImageFile " + _file);
                    byte[] ImageBytes = new byte[Reader.available()];
                    //int BytesLength = ImageBytes.length;
                    Boolean isOk =
                        GlobalCC.validateUploadedImg(UploadedImageFile,
                                                     "User Signature");
                    if (isOk == true) {

                        InsertUserSignature(Reader, Val);
                        userSignImage.setSource("/clientimagesservlet?id=" +
                                                session.getAttribute("sysUserCode"));
                        GlobalCC.refreshUI(userSignImage);

                    } else {
                        userSignature.setValue(null);
                        GlobalCC.refreshUI(userSignature);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            }
        }

    }

    public void InsertUserSignature(InputStream Image, long BytesLength) {
        try {

            DBConnector connector = new DBConnector();
            Connection conn = connector.getDatabaseConnection();
            String systemsQuery =
                "  UPDATE TQC_USERS SET USR_SIGNATURE=? WHERE USR_CODE=?";
            CallableStatement cst = null;
            cst = conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setObject(2, session.getAttribute("sysUserCode"));

            cst.execute();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void setUserSignature(RichInputFile userSignature) {

        this.userSignature = userSignature;
    }

    public RichInputFile getUserSignature() {
        return userSignature;
    }

    public void setUploadedImageFile(UploadedFile UploadedImageFile) {
        this.UploadedImageFile = UploadedImageFile;
    }

    public UploadedFile getUploadedImageFile() {
        return UploadedImageFile;
    }

    public UploadedFile getSignatureFile() {
        return signatureFile;
    }

    public void setSignatureFile(UploadedFile signatureFile) {
        if (signatureFile != null) {
            this.filename = signatureFile.getFilename();
            this.filesize = signatureFile.getLength();
            this.fileContent = signatureFile.getContentType();
            try {
                fileStream = signatureFile.getInputStream();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.signatureFile = signatureFile;
    }

    public void setUserSignImage(RichImage userSignImage) {
        this.userSignImage = userSignImage;
    }

    public RichImage getUserSignImage() {
        return userSignImage;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getFilesize() {
        return filesize;
    }

    public static void setFileContent(String fileContent) {
        UserBean.fileContent = fileContent;
    }

    public static String getFileContent() {
        return fileContent;
    }

    public static void setFileStream(InputStream fileStream) {
        UserBean.fileStream = fileStream;
    }

    public static InputStream getFileStream() {
        return fileStream;
    }

    public void setTxtPhoneNumber(RichInputText txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public RichInputText getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public void setTxtWefdate(RichInputDate txtWefdate) {
        this.txtWefdate = txtWefdate;
    }

    public RichInputDate getTxtWefdate() {
        return txtWefdate;
    }

    public void setTxtWetDate(RichInputDate txtWetDate) {
        this.txtWetDate = txtWetDate;
    }

    public RichInputDate getTxtWetDate() {
        return txtWetDate;
    }

    public void setTxtSecurityQuestions(RichSelectOneChoice txtSecurityQuestions) {
        this.txtSecurityQuestions = txtSecurityQuestions;
    }

    public RichSelectOneChoice getTxtSecurityQuestions() {
        return txtSecurityQuestions;
    }

    public void setTxtSecurityAnswer(RichInputText txtSecurityAnswer) {
        this.txtSecurityAnswer = txtSecurityAnswer;
    }

    public RichInputText getTxtSecurityAnswer() {
        return txtSecurityAnswer;
    }

    public void setSecurityQuestion(List<SelectItem> securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public List<SelectItem> getSecurityQuestion() {
        return securityQuestion;
    }

    public List<SelectItem> getSecurityQuestions() {
        if (securityQuestion != null) {
            securityQuestion.clear();
        }
        DBConnector connector = new DBConnector();
        Connection conn = connector.getDatabaseConnection();
        // Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rst = null;
        try {

            stmt =
conn.prepareCall("begin ?:= TQ_CRM.TQC_ROLES_CURSOR.getAllsecurityQuestions;end;");
            stmt.registerOutParameter(1,
                                      oracle.jdbc.internal.OracleTypes.CURSOR);
            stmt.execute();
            rst = (ResultSet)stmt.getObject(1);
            while (rst.next()) {
                securityQuestion.add(new SelectItem(rst.getString(1),
                                                    rst.getString(3)));
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return securityQuestion;
    }

    public String ldapUsers() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:ldap" +
                             "').show(hints);");
        return null;
    }

    public void actionUserItemSelected(SelectionEvent selectionEvent) {
        System.out.println("Selected!");

        JUCtrlValueBinding n = (JUCtrlValueBinding)users.getSelectedRowData();

        session.setAttribute("sysUserCode",
                             (n != null) ? n.getAttribute("userCode") : null);

        System.out.println("sysUserCode:" +
                           session.getAttribute("sysUserCode"));

        pnTabbedUserAccess.setVisible(true);
        GlobalCC.refreshUI(pnTabbedUserAccess);

        refreshAndfetchData();
    }


    public void actionGroupUserItemSelected(SelectionEvent selectionEvent) {
        System.out.println("Selected!");

        JUCtrlValueBinding n = (JUCtrlValueBinding)users.getSelectedRowData();

        session.setAttribute("sysUserCode",
                             (n != null) ? n.getAttribute("userCode") : null);

        System.out.println("sysUserCode:" +
                           session.getAttribute("sysUserCode"));

        //        pnTabbedUserAccess.setVisible(true);
        //        GlobalCC.refreshUI(pnTabbedUserAccess);

        refreshAndfetchData();
    }

    public String actionDomainUserSelected() {

        Object key2 = usersTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }

        sysUsername.setValue(r.getAttribute("username"));
        sysUsername.setDisabled(false);
        sysFullname.setValue(r.getAttribute("userFullname"));
        sysEmail.setValue(r.getAttribute("userEmail"));

        GlobalCC.refreshUI(sysUsername);
        GlobalCC.refreshUI(sysFullname);
        GlobalCC.refreshUI(sysEmail);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ldap').hide(hints);");
        return null;
    }

    public String cancelUser() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:ldap" +
                             "').hide(hints);");
        return null;
    }

    public void userSelection(SelectionEvent selectionEvent) {
        // Add event code here...
    }

    public void setUsersTable(RichTable usersTable) {
        this.usersTable = usersTable;
    }

    public RichTable getUsersTable() {
        return usersTable;
    }

    public void setLdapUser(RichCommandButton ldapUser) {
        this.ldapUser = ldapUser;
    }

    public RichCommandButton getLdapUser() {
        return ldapUser;
    }

    public void usrRoleWet(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            RichInputDate wetDate =
                (RichInputDate)valueChangeEvent.getComponent();
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.wet_user_allroleinsys(?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                if (GlobalCC.checkNullValues(wetDate.getValue()) == null) {
                    statement.setObject(2, null);
                } else {
                    if (wetDate.getValue().toString().contains(":")) {
                        statement.setObject(2,
                                            GlobalCC.parseDate(wetDate.getValue()));
                    } else {
                        statement.setObject(2,
                                            GlobalCC.upDateParseDate(GlobalCC.checkNullValues(wetDate.getValue())));
                    }
                }

                statement.setObject(3, wetDate.getHelpTopicId());
                statement.setObject(4, null);
                statement.execute();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        // Add event code here...
    }

    public void usrRoleWef(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            RichInputDate wefDate =
                (RichInputDate)valueChangeEvent.getComponent();
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection = connector.getDatabaseConnection();
                String query =
                    "begin tqc_roles_cursor.wef_user_allroleinsys(?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        (BigDecimal)session.getAttribute("sysUserCode"));
                if (GlobalCC.checkNullValues(wefDate.getValue()) == null) {
                    statement.setObject(2, null);
                } else {
                    if (wefDate.getValue().toString().contains(":")) {
                        statement.setObject(2,
                                            GlobalCC.parseDate(wefDate.getValue()));
                    } else {
                        statement.setObject(2,
                                            GlobalCC.upDateParseDate(GlobalCC.checkNullValues(wefDate.getValue())));
                    }
                }

                statement.setObject(3, wefDate.getHelpTopicId());
                statement.setObject(4, null);
                statement.execute();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        // Add event code here...
    }
    /*    public String actionAcceptClientTitle() {
        Object key2 = tblClientTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtClientTitleCode.setValue(nodeBinding.getAttribute("shortDesc"));
            txtClientTitle.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtClientTitle);
        }
        GlobalCC.dismissPopUp("pt1", "clientTitlePop");
        return null;
    }*/

    public String selectSubAcctType() {
        Object key2 = tblSubAcctTypes.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n != null) {
            txtSactCode.setValue(n.getAttribute("SACT_CODE"));
            txtSactName.setValue(n.getAttribute("SACT_DESCRIPTION"));
            GlobalCC.refreshUI(txtSactCode);
            GlobalCC.refreshUI(txtSactName);
        }
        GlobalCC.hidePopup("crm:subAccountTypePop");
        return null;
    }

    public void setTxtSactCode(RichInputText txtSactCode) {
        this.txtSactCode = txtSactCode;
    }

    public RichInputText getTxtSactCode() {
        return txtSactCode;
    }

    public void setTxtSactName(RichInputText txtSactName) {
        this.txtSactName = txtSactName;
    }

    public RichInputText getTxtSactName() {
        return txtSactName;
    }

    public void setTxtSelectSubAccount(RichCommandButton txtSelectSubAccount) {
        this.txtSelectSubAccount = txtSelectSubAccount;
    }

    public RichCommandButton getTxtSelectSubAccount() {
        return txtSelectSubAccount;
    }

    public void setTblSubAcctTypes(RichTable tblSubAcctTypes) {
        this.tblSubAcctTypes = tblSubAcctTypes;
    }

    public RichTable getTblSubAcctTypes() {
        return tblSubAcctTypes;
    }

    public String cancelSubAccountSelect() {
        GlobalCC.hidePopup("crm:subAccountTypePop");
        return null;
    }

    public void setPbxMainPanel(RichPanelBox pbxMainPanel) {
        this.pbxMainPanel = pbxMainPanel;
    }

    public RichPanelBox getPbxMainPanel() {
        return pbxMainPanel;
    }

    public void setTxtUserAuthorized(RichInputText txtUserAuthorized) {
        this.txtUserAuthorized = txtUserAuthorized;
    }

    public RichInputText getTxtUserAuthorized() {
        return txtUserAuthorized;
    }

    public void setLblWefdate(RichOutputLabel lblWefdate) {
        this.lblWefdate = lblWefdate;
    }

    public RichOutputLabel getLblWefdate() {
        return lblWefdate;
    }

    public void setTxtPin(RichInputText txtPin) {
        this.txtPin = txtPin;
    }

    public RichInputText getTxtPin() {
        return txtPin;
    }

    public void tblUserGrpTypes(SelectionEvent selectionEvent) {
        // get selected
        Object selectedrow = this.tblUserGrpType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedrow;

        if (nodeBinding != null) {
            session.setAttribute("groupTypeUserCode",
                                 nodeBinding.getAttribute("code"));
        }

        ADFUtils.findIterator("findGroupUserTypesMembersIterator").executeQuery();
        GlobalCC.refreshUI(tblUserGroupTypeMember);
        ADFUtils.findIterator("fetchAccountGroupTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblGroupAgencies);
    }

    public String actionHideNewUserGrpMember() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:addUserToGroupType" + "').hide(hints);");
        return null;
    }

    public String actionNewUserGrpMember() {
        if (this.usersDetailItempanel.isDisclosed() &&
            session.getAttribute("groupTypeUserCode") != null) {
            // Add Users

            ADFUtils.findIterator("findAllUsersNotInGroupTypeIterator").executeQuery();
            GlobalCC.refreshUI(tblAddUserToGroupType);
            GlobalCC.showPop("crm:addUserToGroupType");

            return null;
        } else if (this.accountsDetailItempanel.isDisclosed() &&
                   session.getAttribute("groupTypeUserCode") != null) {
            // Add accounts

            GlobalCC.showPop("crm:addAccountsToGroup");

            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("First select the group ");
            return null;
        }
    }

    public void setTblUserGrpType(RichTable tblUserGrpType) {
        this.tblUserGrpType = tblUserGrpType;
    }

    public RichTable getTblUserGrpType() {
        return tblUserGrpType;
    }

    public void setTblUserGroupTypeMember(RichTable tblUserGroupTypeMembers) {
        this.tblUserGroupTypeMember = tblUserGroupTypeMembers;
    }

    public RichTable getTblUserGroupTypeMember() {
        return tblUserGroupTypeMember;
    }

    public void setBtnNewUserGrpMember(RichCommandButton btnNewUserGrpMember) {
        this.btnNewUserGrpMember = btnNewUserGrpMember;
    }

    public RichCommandButton getBtnNewUserGrpMember() {
        return btnNewUserGrpMember;
    }

    public void setTblAddUserToGroupType(RichTable tblAddUserToGroupType) {
        this.tblAddUserToGroupType = tblAddUserToGroupType;
    }

    public RichTable getTblAddUserToGroupType() {
        return tblAddUserToGroupType;
    }

    public String actionAcceptGroupTypeAccountsLov() {

        if (checkIfAnyTableRowselected(tblAgencyInfo, "select")) {
            int rowCount = tblAgencyInfo.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                tblAgencyInfo.setRowIndex(i);
                Object key = tblAgencyInfo.getRowKey();
                tblAgencyInfo.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblAgencyInfo.getRowData();
                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("select");
                    if (selected) {
                        if (session.getAttribute("groupTypeUserCode") !=
                            null) {
                            DBConnector dbHandler = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement cstmt = null;
                            try {
                                conn = dbHandler.getDatabaseConnection();
                                String query =
                                    "begin tqc_setups_pkg.accountgrouptype_prc(?,?,?,?); end;";

                                cstmt =
                                        (OracleCallableStatement)conn.prepareCall(query);

                                cstmt.setString(1, "A");
                                cstmt.setObject(2, null);
                                cstmt.setObject(3,
                                                session.getAttribute("groupTypeUserCode"));
                                cstmt.setObject(4,
                                                nodeBinding.getAttribute("code"));
                                cstmt.execute();

                                cstmt.close();
                                conn.commit();
                                conn.close();

                            } catch (Exception e) {
                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return null;
                            }
                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return null;
                        }
                    }
                } else {
                    GlobalCC.INFORMATIONREPORTING("No record  selected ::");
                    return null;
                }
            } // END FOR
            GlobalCC.INFORMATIONREPORTING("Account(S) Added Successfully");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }


        ADFUtils.findIterator("fetchAccountGroupTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblAgencyInfo);
        ADFUtils.findIterator("findAllAccountsNotInGroupTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblGroupAgencies);

        return null;
    }

    public String actionAcceptGroupTypeUserLov() {
        if (checkIfAnyTableRowselected(tblAddUserToGroupType,
                                       "userSelected")) {
            int rowcount = tblAddUserToGroupType.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblAddUserToGroupType.setRowIndex(i);
                Object key = tblAddUserToGroupType.getRowKey();
                tblAddUserToGroupType.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblAddUserToGroupType.getRowData();


                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("userSelected");

                    if (selected) {

                        if (session.getAttribute("groupTypeUserCode") !=
                            null) {


                            DBConnector dbConnector = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement statement = null;
                            try {
                                conn = dbConnector.getDatabaseConnection();
                                String query =
                                    "begin TQC_SETUPS_PKG.usergrouptype_prc(?,?,?,?); end;";


                                statement =
                                        (OracleCallableStatement)conn.prepareCall(query);
                                statement.setString(1, "A");
                                statement.setObject(2, null);
                                statement.setObject(3,
                                                    session.getAttribute("groupTypeUserCode"));
                                statement.setObject(4,
                                                    nodeBinding.getAttribute("userCode"));
                                statement.execute();

                                statement.close();
                                conn.commit();
                                conn.close();


                            } catch (Exception e) {

                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return null;
                            }

                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return null;
                        }
                    } //END IF
                } else {
                    GlobalCC.INFORMATIONREPORTING("No record  selected ::");
                    return null;
                } //END ELSE

            } //END FOR
            GlobalCC.INFORMATIONREPORTING("User(S) Added Successfully");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }

        ADFUtils.findIterator("findGroupUserTypesMembersIterator").executeQuery();
        ADFUtils.findIterator("findAllUsersNotInGroupTypeIterator").executeQuery();
        GlobalCC.refreshUI(tblAddUserToGroupType);
        GlobalCC.refreshUI(tblUserGroupTypeMember);

        return null;
    }

    public void actionConfirmDeleteUserFromGroupType(DialogEvent dialogEvent) {
        if (session.getAttribute("groupTypeUserCode") != null) {

            if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
                if (actionDeleteGroupTypeMember()) {
                    GlobalCC.INFORMATIONREPORTING("User Successfully Removed!");
                }
            }

        }
    }

    public void actionConfirmDeleteAccountFromGroupType(DialogEvent dialogEvent) {
        if (session.getAttribute("groupTypeUserCode") != null) {
            if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

            } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
                if (actionDeleteGroupTypeAccounts()) {
                    GlobalCC.INFORMATIONREPORTING("Account Successfully Removed!");
                }
            }
        }
    }

    public String actionRemoveUserGrpMembers() {
        if (this.usersDetailItempanel.isDisclosed() &&
            checkIfAnyTableRowselected(tblUserGroupTypeMember,
                                       "userSelected")) {
            // Remove users selected.
            GlobalCC.showPop("crm:confirmDeleteUserFromGroupType");
            return null;
        } else if (this.accountsDetailItempanel.isDisclosed() &&
                   checkIfAnyTableRowselected(tblGroupAgencies, "select")) {
            // Remove selected accounts

            GlobalCC.showPop("crm:confirmDeleteAccountFromGroupType");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
    }

    public Boolean actionDeleteGroupTypeAccounts() {
        if (checkIfAnyTableRowselected(tblGroupAgencies, "select")) {
            int rowcount = tblGroupAgencies.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblGroupAgencies.setRowIndex(i);
                Object key = tblGroupAgencies.getRowKey();
                tblGroupAgencies.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblGroupAgencies.getRowData();

                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("select");

                    if (selected) {
                        if (session.getAttribute("groupTypeUserCode") !=
                            null) {
                            DBConnector dbHandler = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement cstmt = null;

                            try {
                                conn = dbHandler.getDatabaseConnection();
                                String query =
                                    "begin TQC_SETUPS_PKG.accountgrouptype_prc(?,?,?,?); end;";
                                cstmt =
                                        (OracleCallableStatement)conn.prepareCall(query);
                                cstmt.setString(1, "D");
                                cstmt.setBigDecimal(2,
                                                    (BigDecimal)nodeBinding.getAttribute("code"));
                                cstmt.setObject(3, null);
                                cstmt.setObject(4, null);
                                cstmt.execute();

                                cstmt.close();
                                conn.commit();
                                conn.close();

                            } catch (Exception e) {
                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return false;
                            } // try catch
                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return false;
                        }
                    }
                } else {
                    GlobalCC.INFORMATIONREPORTING("No record selected::");
                    return false;
                }
            }
            GlobalCC.INFORMATIONREPORTING("Account(s) Removed Successfully");
            ADFUtils.findIterator("fetchAccountGroupTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblGroupAgencies);
        }
        return true;
    }

    private Boolean actionDeleteGroupTypeMember() {
        if (checkIfAnyTableRowselected(tblUserGroupTypeMember,
                                       "userSelected")) {
            int rowcount = tblUserGroupTypeMember.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblUserGroupTypeMember.setRowIndex(i);
                Object key = tblUserGroupTypeMember.getRowKey();
                tblUserGroupTypeMember.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblUserGroupTypeMember.getRowData();


                if (nodeBinding != null) {
                    Boolean selected =
                        (Boolean)nodeBinding.getAttribute("userSelected");

                    if (selected) {


                        if (session.getAttribute("groupTypeUserCode") !=
                            null) {


                            DBConnector dbConnector = new DBConnector();
                            OracleConnection conn = null;
                            OracleCallableStatement statement = null;
                            try {
                                conn = dbConnector.getDatabaseConnection();
                                String query =
                                    "begin TQC_SETUPS_PKG.usergrouptype_prc(?,?,?,?); end;";


                                statement =
                                        (OracleCallableStatement)conn.prepareCall(query);
                                statement.setString(1, "D");
                                statement.setObject(2,
                                                    nodeBinding.getAttribute("gtUsrcode"));
                                statement.setObject(3, null);
                                statement.setObject(4, null);
                                statement.execute();

                                statement.close();
                                conn.commit();
                                conn.close();


                            } catch (Exception e) {

                                GlobalCC.EXCEPTIONREPORTING(conn, e);
                                return false;
                            }

                        } else {
                            GlobalCC.INFORMATIONREPORTING("First Select user Group ::");
                            return false;
                        }
                    }
                } //end  if nodebinding null
                else {
                    GlobalCC.INFORMATIONREPORTING("No record selected::");
                    return false;
                }

            } //end for
            GlobalCC.INFORMATIONREPORTING("User(s) Removed Successfully");
            ADFUtils.findIterator("findGroupUserTypesMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblUserGroupTypeMember);
        }
        return true;
    }

    public void setUserToRemoveSelection(RichSelectBooleanCheckbox userToRemoveSelection) {
        this.userToRemoveSelection = userToRemoveSelection;
    }

    public RichSelectBooleanCheckbox getUserToRemoveSelection() {
        return userToRemoveSelection;
    }

    public String selectAllUsersToRemoveFromGroup() {
        if (this.usersDetailItempanel.isDisclosed()) {
            // select all users
            this.selectAll(tblUserGroupTypeMember, userToRemoveSelection);
            return null;
        } else if (this.accountsDetailItempanel.isDisclosed()) {
            // select all accounts
            this.selectAll(tblGroupAgencies, accountToRemoveSelection);
            return null;
        }
        return null;
    }

    public String delectAllUsersToRemoveFromGroup() {
        if (this.usersDetailItempanel.isDisclosed()) {
            // deselect all userss

            this.deselectAll(tblUserGroupTypeMember, userToRemoveSelection,
                             "userSelected");
            return null;
        } else if (this.accountsDetailItempanel.isDisclosed()) {
            // deselect all accounts
            this.deselectAll(tblGroupAgencies, accountToRemoveSelection,
                             "select");
            return null;
        }
        return null;
    }

    public String selectAllAccountsToAddToGroup() {
        this.selectAll(tblAgencyInfo, chkSelectedAcount);
        return null;
    }

    public String deSelectAllAccountsToAddToGroup() {
        this.deselectAll(tblAgencyInfo, chkSelectedAcount, "select");
        return null;
    }

    public String selectAllUsersToAddToGroup() {
        this.selectAll(tblAddUserToGroupType, chkSelectedUser);
        return null;
    }

    public String deSelectAllUsersToAddToGroup() {
        this.deselectAll(tblAddUserToGroupType, chkSelectedUser,
                         "userSelected");
        return null;
    }

    public void setChkSelectedUser(RichSelectBooleanCheckbox chkSelectedUser) {
        this.chkSelectedUser = chkSelectedUser;
    }

    public RichSelectBooleanCheckbox getChkSelectedUser() {
        return chkSelectedUser;
    }

    public void setUsersDetailItempanel(RichShowDetailItem usersDetailItempanel) {
        this.usersDetailItempanel = usersDetailItempanel;
    }

    public RichShowDetailItem getUsersDetailItempanel() {
        return usersDetailItempanel;
    }

    public void setAccountsDetailItempanel(RichShowDetailItem accountsDetailItempanel) {
        this.accountsDetailItempanel = accountsDetailItempanel;
    }

    public RichShowDetailItem getAccountsDetailItempanel() {
        return accountsDetailItempanel;
    }

    public String showAccountTypesPop() {
        GlobalCC.showPopup("crm:accountTypesPop");
        return null;
    }

    public String actionNewGroupType() {
        session.setAttribute("ActionType", "ADD");
        session.setAttribute("userGroupTypesCode", "");
        this.txtGroupShtDesc.setValue("");
        this.txtGroupTypeBranch.setValue("");
        this.txtGroupTypeName.setValue("");
        btnSaveGroupTypes.setText("Save");

        GlobalCC.showPop("crm:addNewGroupType");
        return null;
    }

    public String showEditAccountTypesPop() {
        //        txtGroupTypeName
        //        txtGroupTypeBranch
        //        txtGroupShtDesc
        Object selectedRow = this.tblUserGroupTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (nodeBinding != null) {
            // retrieve values
            session.setAttribute("ActionType", "EDIT");
            session.setAttribute("userGroupTypesCode",
                                 nodeBinding.getAttribute("code"));
            session.setAttribute("groupTypeID",
                                 nodeBinding.getAttribute("typeId"));
            session.setAttribute("groupTypeBranchCode",
                                 nodeBinding.getAttribute("branchCode"));

            this.txtGroupShtDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            this.txtGroupTypeBranch.setValue(nodeBinding.getAttribute("branchName"));
            this.txtGroupTypeName.setValue(nodeBinding.getAttribute("groupType"));

            btnSaveGroupTypes.setText("Update");

            System.out.println(nodeBinding.getAttribute("typeId"));
            GlobalCC.showPop("crm:addNewGroupType");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }


        return null;
    }

    public String actionDeleteCategoryGroupType() {
        Object selectedRow = this.tblUserGroupTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (nodeBinding != null) {
            GlobalCC.showPop("crm:confirmDeleteCategoryGroupTypePopup");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }

        return null;
    }

    public String actionConfirmDeleteCategoryGroupType(DialogEvent dialogEvent) {
        Object selectedRow = this.tblUserGroupTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            if (nodeBinding != null) {
                String query =
                    "begin tqc_setups_pkg.grouptype_prc(?,?,?,?,?,?); end;";
                OracleConnection conn = null;
                OracleCallableStatement cstmt = null;
                DBConnector dbHandler = new DBConnector();
                try {
                    conn = dbHandler.getDatabaseConnection();
                    cstmt = (OracleCallableStatement)conn.prepareCall(query);

                    cstmt.setString(1, "D");
                    cstmt.setObject(2, nodeBinding.getAttribute("code"));
                    cstmt.setString(3, null);
                    cstmt.setString(4, null);
                    cstmt.setString(5, null);
                    cstmt.setBigDecimal(6, null);
                    cstmt.execute();

                    cstmt.close();
                    conn.commit();
                    conn.close();

                    GlobalCC.dismissPopUp("crm",
                                          "confirmDeleteCategoryGroupTypePopup");
                    ADFUtils.findIterator("fetchUserGroupTypesIterator").executeQuery();
                    GlobalCC.refreshUI(tblUserGroupTypes);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                    return null;
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");
                return null;
            }
        } else {
            GlobalCC.dismissPopUp("crm",
                                  "confirmDeleteCategoryGroupTypePopup");
        }

        return null;
    }

    public String actionSaveGroupType() {
        // validate that a group type is selected.
        // validate that the group name is provided
        // validate if a branch is selected
        final String groupShtDesc =
            GlobalCC.checkNullValues(txtGroupShtDesc.getValue());

        if (session.getAttribute("groupTypeID") != null &&
            groupShtDesc != null &&
            session.getAttribute("groupTypeBranchCode") != null) {
            String query =
                "begin tqc_setups_pkg.grouptype_prc(?,?,?,?,?,?); end;";
            OracleConnection conn = null;
            OracleCallableStatement cstmt = null;
            DBConnector dbHandler = new DBConnector();
            try {
                conn = dbHandler.getDatabaseConnection();
                cstmt = (OracleCallableStatement)conn.prepareCall(query);

                if (session.getAttribute("ActionType").toString().equalsIgnoreCase("ADD")) {
                    cstmt.setString(1, "A");
                    cstmt.setBigDecimal(2, null);
                } else if (session.getAttribute("ActionType").toString().equalsIgnoreCase("EDIT")) {
                    cstmt.setString(1, "E");
                    cstmt.setObject(2,
                                    session.getAttribute("userGroupTypesCode"));
                }
                cstmt.setString(3,
                                (String)session.getAttribute("groupTypeID"));
                cstmt.setString(4, (String)txtGroupTypeName.getValue());
                cstmt.setString(5, groupShtDesc);
                cstmt.setBigDecimal(6,
                                    (BigDecimal)session.getAttribute("groupTypeBranchCode"));
                cstmt.execute();

                cstmt.close();
                conn.commit();
                conn.close();

                GlobalCC.dismissPopUp("crm", "addNewGroupType");
                ADFUtils.findIterator("fetchUserGroupTypesIterator").executeQuery();
                GlobalCC.refreshUI(tblUserGroupTypes);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }
        } else {
            if (session.getAttribute("groupTypeID") == null) {
                GlobalCC.EXCEPTIONREPORTING("Select a Group Type from the LOV.");
                return null;
            }
            if (groupShtDesc == null) {
                GlobalCC.EXCEPTIONREPORTING("Provide Group type name.");
                return null;
            }
            if (session.getAttribute("groupTypeBranchCode") == null) {
                GlobalCC.EXCEPTIONREPORTING("Select a Branch from the LOV.");
                return null;
            }

        }
        return null;
    }

    public String actionAcceptGroupType() {
        Object selectedRow = this.tblGroupTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (nodeBinding != null) {
            session.setAttribute("groupTypeID",
                                 nodeBinding.getAttribute("typeId"));
            session.setAttribute("groupTypeName",
                                 nodeBinding.getAttribute("typeName"));
            this.txtGroupTypeName.setValue((String)nodeBinding.getAttribute("typeName"));
            GlobalCC.refreshUI(txtGroupTypeName);

            GlobalCC.dismissPopUp("crm", "groupTypePopup");
        }
        return null;
    }

    public String actionAcceptBranches() {
        Object selectedRow = this.tblGroupTypeBranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (nodeBinding != null) {
            BigDecimal branchCode =
                (BigDecimal)nodeBinding.getAttribute("bnsCode");

            session.setAttribute("groupTypeBranchCode", branchCode);
            this.txtGroupTypeBranch.setValue((String)nodeBinding.getAttribute("bnsName"));
            GlobalCC.refreshUI(txtGroupTypeBranch);

            GlobalCC.dismissPopUp("crm", "branchesPopup");
        }
        return null;
    }

    public String actionAcceptAccountTypes() {
        Object selectedRow = this.tblAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedRow;
        if (nodeBinding != null) {

            String mainAccID = nodeBinding.getAttribute("typeId").toString();
            String mainAccName =
                nodeBinding.getAttribute("accountType").toString();

            session.setAttribute("accountTypeID", mainAccID);
            session.setAttribute("accountName", mainAccName);
            txtAccountTypeCode.setValue(mainAccName);
            GlobalCC.refreshUI(txtAccountTypeCode);

            GlobalCC.dismissPopUp("crm", "accountTypesPop");

            ADFUtils.findIterator("findAllAccountsNotInGroupTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);
        }
        return null;
    }

    public void setTblAccountTypesPop(RichTable tblAccountTypesPop) {
        this.tblAccountTypesPop = tblAccountTypesPop;
    }

    public RichTable getTblAccountTypesPop() {
        return tblAccountTypesPop;
    }

    public void setTxtAccountTypeCode(RichInputText txtAccountTypeCode) {
        this.txtAccountTypeCode = txtAccountTypeCode;
    }

    public RichInputText getTxtAccountTypeCode() {
        return txtAccountTypeCode;
    }

    public void setTblAgencyInfo(RichTable tblAgencyInfo) {
        this.tblAgencyInfo = tblAgencyInfo;
    }

    public RichTable getTblAgencyInfo() {
        return tblAgencyInfo;
    }

    public void setTblGroupAgencies(RichTable tblGroupAgencies) {
        this.tblGroupAgencies = tblGroupAgencies;
    }

    public RichTable getTblGroupAgencies() {
        return tblGroupAgencies;
    }

    public void setChkSelectedAcount(RichSelectBooleanCheckbox chkSelectedAcount) {
        this.chkSelectedAcount = chkSelectedAcount;
    }

    public RichSelectBooleanCheckbox getChkSelectedAcount() {
        return chkSelectedAcount;
    }

    public void setAccountToRemoveSelection(RichSelectBooleanCheckbox accountToRemoveSelection) {
        this.accountToRemoveSelection = accountToRemoveSelection;
    }

    public RichSelectBooleanCheckbox getAccountToRemoveSelection() {
        return accountToRemoveSelection;
    }

    public void setTblGroupTypeBranches(RichTable tblGroupTypeBranches) {
        this.tblGroupTypeBranches = tblGroupTypeBranches;
    }

    public RichTable getTblGroupTypeBranches() {
        return tblGroupTypeBranches;
    }

    public void setTxtGroupTypeBranch(RichInputText txtGroupTypeBranch) {
        this.txtGroupTypeBranch = txtGroupTypeBranch;
    }

    public RichInputText getTxtGroupTypeBranch() {
        return txtGroupTypeBranch;
    }

    public void setTblGroupTypes(RichTable tblGroupTypes) {
        this.tblGroupTypes = tblGroupTypes;
    }

    public RichTable getTblGroupTypes() {
        return tblGroupTypes;
    }

    public void setTxtGroupTypeName(RichInputText txtGroupTypeName) {
        this.txtGroupTypeName = txtGroupTypeName;
    }

    public RichInputText getTxtGroupTypeName() {
        return txtGroupTypeName;
    }

    public void setTxtGroupShtDesc(RichInputText txtGroupName) {
        this.txtGroupShtDesc = txtGroupName;
    }

    public RichInputText getTxtGroupShtDesc() {
        return txtGroupShtDesc;
    }

    public void setTblUserGroupTypes(RichTable tblUserGroupTypes) {
        this.tblUserGroupTypes = tblUserGroupTypes;
    }

    public RichTable getTblUserGroupTypes() {
        return tblUserGroupTypes;
    }

    public void setBtnEditUserInGroup(RichCommandButton btnEditUserInGroup) {
        this.btnEditUserInGroup = btnEditUserInGroup;
    }

    public RichCommandButton getBtnEditUserInGroup() {
        return btnEditUserInGroup;
    }

    public void actionUsersDetailDisclosed(DisclosureEvent disclosureEvent) {
        btnEditUserInGroup.setVisible(true);
        GlobalCC.refreshUI(btnEditUserInGroup);
    }

    public void actionAccountsDetailDisclosed(DisclosureEvent disclosureEvent) {
        btnEditUserInGroup.setVisible(false);
        GlobalCC.refreshUI(btnEditUserInGroup);
    }

    public String actionSelectAsTeamLeader() {
        Object selectedrow = this.tblUserGroupTypeMember.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedrow;

        if (session.getAttribute("groupTypeUserCode") != null &&
            nodeBinding != null) {
            OracleConnection conn = null;
            OracleCallableStatement cstmt = null;
            DBConnector dbHandler = new DBConnector();
            String query =
                "begin tqc_setups_pkg.usergrouptypeteamleader_prc(?,?); end;";
            try {
                conn = dbHandler.getDatabaseConnection();
                cstmt = (OracleCallableStatement)conn.prepareCall(query);
                cstmt.setObject(1, session.getAttribute("groupTypeUserCode"));
                cstmt.setObject(2, nodeBinding.getAttribute("userCode"));
                cstmt.execute();

                cstmt.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findGroupUserTypesMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblUserGroupTypeMember);

                return null;
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }
        } else {
            if (session.getAttribute("groupTypeUserCode") == null) {
                GlobalCC.INFORMATIONREPORTING("Select group first...");
                return null;
            }
            if (nodeBinding == null) {
                GlobalCC.INFORMATIONREPORTING("Select user to set as a TeamLeader first...");
                return null;
            }
            return null;
        }
    }

    public void setTblSelectTeamLeader(RichTable tblSelectTeamLeader) {
        this.tblSelectTeamLeader = tblSelectTeamLeader;
    }

    public RichTable getTblSelectTeamLeader() {
        return tblSelectTeamLeader;
    }

    public void setBtnSaveGroupTypes(RichCommandButton btnSaveGroupTypes) {
        this.btnSaveGroupTypes = btnSaveGroupTypes;
    }

    public RichCommandButton getBtnSaveGroupTypes() {
        return btnSaveGroupTypes;
    }

    public void setKPIDetailItempanel(RichShowDetailItem KPIDetailItempanel) {
        this.KPIDetailItempanel = KPIDetailItempanel;
    }

    public RichShowDetailItem getKPIDetailItempanel() {
        return KPIDetailItempanel;
    }

    public void setKpiTask(RichInputText kpiTask) {
        this.kpiTask = kpiTask;
    }

    public RichInputText getKpiTask() {
        return kpiTask;
    }

    public void setKpiSubTask(RichInputText kpiSubTask) {
        this.kpiSubTask = kpiSubTask;
    }

    public RichInputText getKpiSubTask() {
        return kpiSubTask;
    }

    public void setKpiParam(RichInputText kpiParam) {
        this.kpiParam = kpiParam;
    }

    public RichInputText getKpiParam() {
        return kpiParam;
    }

  
    public void setKpiComment(RichInputText kpiComment) {
        this.kpiComment = kpiComment;
    }

    public RichInputText getKpiComment() {
        return kpiComment;
    }

    public void setCreateKpiBtn(RichCommandButton createKpiBtn) {
        this.createKpiBtn = createKpiBtn;
    }

    public RichCommandButton getCreateKpiBtn() {
        return createKpiBtn;
    }

    public void setCancelKPIDetails(RichCommandButton cancelKPIDetails) {
        this.cancelKPIDetails = cancelKPIDetails;
    }

    public RichCommandButton getCancelKPIDetails() {
        return cancelKPIDetails;
    }

    public void setKPITaskLovPopUp(RichPopup KPITaskLovPopUp) {
        this.KPITaskLovPopUp = KPITaskLovPopUp;
    }

    public RichPopup getKPITaskLovPopUp() {
        return KPITaskLovPopUp;
    }

    public void setBtnSelectTask(RichCommandButton btnSelectTask) {
        this.btnSelectTask = btnSelectTask;
    }

    public RichCommandButton getBtnSelectTask() {
        return btnSelectTask;
    }

    public void setBtnSelectTask2(RichCommandButton btnSelectTask2) {
        this.btnSelectTask2 = btnSelectTask2;
    }

    public RichCommandButton getBtnSelectTask2() {
        return btnSelectTask2;
    }

    public void setKPITaskLovPopUpS(RichPopup KPITaskLovPopUpS) {
        this.KPITaskLovPopUpS = KPITaskLovPopUpS;
    }

    public RichPopup getKPITaskLovPopUpS() {
        return KPITaskLovPopUpS;
    }

    public void setBtnSelectTasks(RichCommandButton btnSelectTasks) {
        this.btnSelectTasks = btnSelectTasks;
    }

    public RichCommandButton getBtnSelectTasks() {
        return btnSelectTasks;
    }

    public void setBtnSelectTask2s(RichCommandButton btnSelectTask2s) {
        this.btnSelectTask2s = btnSelectTask2s;
    }

    public RichCommandButton getBtnSelectTask2s() {
        return btnSelectTask2s;
    }

    public void setTblTask(RichTable tblTask) {
        this.tblTask = tblTask;
    }

    public RichTable getTblTask() {
        return tblTask;
    }

    public void setTblSubTask(RichTable tblSubTask) {
        this.tblSubTask = tblSubTask;
    }

    public RichTable getTblSubTask() {
        return tblSubTask;
    }

    public void setTblKpiTab(RichTable tblKpiTab) {
        this.tblKpiTab = tblKpiTab;
    }

    public RichTable getTblKpiTab() {
        return tblKpiTab;
    }


    public void setKpiUnit(RichSelectOneChoice kpiUnit) {
        this.kpiUnit = kpiUnit;
    }

    public RichSelectOneChoice getKpiUnit() {
        return kpiUnit;
    }
}
