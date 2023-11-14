package TurnQuest.view.Branches;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;

import oracle.jbo.Key;
import oracle.jbo.Row;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class UserBranches {
    private RichTable users;
    private RichSelectManyShuttle userBranches;
    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();

    private UISelectItems userSystemsSelect;
    private RichTable organizations;
    private RichInputText organizationDesc;
    private RichTable regions;
    private RichInputText regionDesc;
    private RichTable userBranchesTab;

    public UserBranches() {
        super();
        // GlobalCC.refreshUI(userSystems);
        if (session.getAttribute("sysUserCode") == null) {
            UserBranchesValues();
        }
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public void setUsers(RichTable users) {
        this.users = users;
    }

    public RichTable getUsers() {
        return users;
    }

    public void userSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findUsersIterator");
            RowKeySet set = selectionEvent.getAddedSet();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("sysUserCode",
                                     r.getAttribute("userCode"));

            }
            UserBranchesValues();
            GlobalCC.refreshUI(userBranches);

            /*   ADFUtils.findIterator("findUserSystemsIterator").executeQuery();
            GlobalCC.refreshUI(userSystemDates);*/

        }
    }

    public void userBranchGrant(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                ArrayList<String> myVals =
                    (ArrayList<String>)userBranches.getValue();
                ArrayList<SelectItem> myVals2 =
                    (ArrayList<SelectItem>)userSystemsSelect.getValue();
                int v = 0;
                String revokeQuery =
                    "begin tqc_roles_cursor.Revoke_User_Branch(?,?); end;";

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
                    "begin tqc_roles_cursor.Grant_User_Branch(?,?); end;";
                int k = 0;
                while (k < myVals.size()) {
                    OracleCallableStatement cst = null;


                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1,
                                      (BigDecimal)session.getAttribute("sysUserCode"));
                    cst.setString(2, (String)myVals.get(k));
                    cst.execute();


                    k++;
                }

                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
            GlobalCC.refreshUI(userBranchesTab);
        }
    }

    public void setUserBranches(RichSelectManyShuttle userBranches) {
        this.userBranches = userBranches;
    }

    public RichSelectManyShuttle getUserBranches() {
        return userBranches;
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

    public String UserBranchesValues() {
        String query = "begin ? := tqc_roles_cursor.GetBranches(?); end;";
        OracleCallableStatement cst = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = (OracleConnection)datahandler.getDatabaseConnection();

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

            conn = (OracleConnection)datahandler.getDatabaseConnection();

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
            cstUsr.close();
            rsUser.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        if (userBranches == null) {
        } else {
            userBranches.setValue(displayValue);
            userSystemsSelect.setValue(selectValues);
        }
        return null;
    }

    public void setOrganizations(RichTable organizations) {
        this.organizations = organizations;
    }

    public RichTable getOrganizations() {
        return organizations;
    }

    public String organizationSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findOrganizationsIterator");
        RowKeySet set = organizations.getSelectedRowKeys();
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
            GlobalCC.refreshUI(regions);

            regionDesc.setValue(null);
            UserBranchesValues();
            GlobalCC.refreshUI(userBranches);
            ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
            GlobalCC.refreshUI(userBranchesTab);

        }
        return null;
    }

    public void setOrganizationDesc(RichInputText organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public RichInputText getOrganizationDesc() {
        return organizationDesc;
    }

    public String regionSelected() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findRegionsIterator");
        RowKeySet set = regions.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            session.setAttribute("regCode", r.getAttribute("regCode"));
            regionDesc.setValue(r.getAttribute("regName"));

        }
        UserBranchesValues();
        GlobalCC.refreshUI(userBranches);
        ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        GlobalCC.refreshUI(userBranchesTab);
        ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
        GlobalCC.refreshUI(userBranchesTab);
        return null;
    }

    public void setRegions(RichTable regions) {
        this.regions = regions;
    }

    public RichTable getRegions() {
        return regions;
    }

    public void setRegionDesc(RichInputText regionDesc) {
        this.regionDesc = regionDesc;
    }

    public RichInputText getRegionDesc() {
        return regionDesc;
    }

    public void setUserBranchesTab(RichTable userBranchesTab) {
        this.userBranchesTab = userBranchesTab;
    }

    public RichTable getUserBranchesTab() {
        return userBranchesTab;
    }

    public String makeDefault() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserBranchesIterator");
        RowKeySet set = userBranchesTab.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            try {

                conn = (OracleConnection)datahandler.getDatabaseConnection();
                OracleCallableStatement cst = null;
                String query =
                    "begin tqc_roles_cursor.makeDefaultBranch(?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setBigDecimal(1,
                                  (BigDecimal)r.getAttribute("usrBranchCode")); //authorization code
                cst.execute();
                conn.close();
                cst.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
            GlobalCC.refreshUI(userBranchesTab);

        }
        return null;
    }

    public String disableBranch() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("findUserBranchesIterator");
        RowKeySet set = userBranchesTab.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));


            Row r = dciter.getCurrentRow();
            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            try {

                conn = (OracleConnection)datahandler.getDatabaseConnection();
                OracleCallableStatement cst = null;
                String query =
                    "begin tqc_roles_cursor.enableDisableBranch(?); end;";
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setBigDecimal(1,
                                  (BigDecimal)r.getAttribute("usrBranchCode")); //authorization code
                cst.execute();
                conn.close();
                cst.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
            ADFUtils.findIterator("findUserBranchesIterator").executeQuery();
            GlobalCC.refreshUI(userBranchesTab);


        }
        return null;
    }
}
