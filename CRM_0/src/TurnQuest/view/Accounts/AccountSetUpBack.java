package TurnQuest.view.Accounts;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.navigation.Links;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class AccountSetUpBack {
    private RichTable tblAgencyClasses;
    private RichInputText agClassFullDesc;
    private RichInputText holdCompanyFullDesc;
    private RichTable tblAgencyHoldingCompany;
    private RichInputText txtSectorFullDesc;
    private RichTable tblSectors;
    private RichShowDetailItem detAccount;
    private RichOutputText textToSHow;
    private RichInputText txtShortDescformat;
    private RichInputText txtMgrNoInfix;
    private RichTable tblAgentAccountFrom;
    private RichTable tblAgentAccountTo;
    private RichInputText txtFromAgentAccount;
    private RichInputText txtToAgentAccount;
    private RichSelectOneRadio accType;
    private RichSelectOneRadio txtSystem;
    private RichSelectOneChoice txtSearchBy;
    private RichInputText txtEntity;
    private RichInputText txtShrtDesc;
   private RichTable tblEntities;
    private RichTable tblEntityRelations;
    private RichSelectOneChoice txtEntityType;

    public AccountSetUpBack() {
        super();
    }


    private RichTable accountTable;
    private RichSelectOneChoice smapping;
    private RichInputText id;
    private RichInputText cmapping;
    private RichInputNumberSpinbox whRate;
    private RichInputNumberSpinbox ovrate;
    private RichInputNumberSpinbox commrate;
    private RichInputText acnoformat;
    private RichInputNumberSpinbox vatrate;
    private RichInputNumberSpinbox txtCommissionLevyRate;
    private RichCommandButton saveAccountTypes;
    private RichTree accountTypes;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable agentTable;
    private RichPanelTabbed infoTab;
    private RichInputText agentID;
    private RichInputText agentFullName;
    private RichInputText agentPhysAddress;
    private RichInputText address;
    private RichInputText country;
    private RichInputText towb;
    private RichInputText postalAddress;
    private RichInputText agBranch;
    private RichInputText contCode;
    private RichInputText personnel;
    private RichInputText stCode;
    private RichInputText smsNo;
    private RichSelectOneChoice licensed;
    private RichInputText agClass;
    private RichInputText phone1;
    private RichInputText phone2;
    private RichInputText fax;
    private RichInputText emailAddress;
    private RichInputText webAddress;
    private RichInputText contactPerson;
    private RichInputText contactTitle;
    private RichInputText gaccountNo;
    private RichInputText idNumber;
    private RichInputText pinNumber;
    private RichSelectOneChoice agStatus;
    private RichSelectOneChoice runOff;
    private RichInputText remarks;
    private RichInputText sector;
    private RichInputText affiliateTo;
    private RichInputText holdCompany;
    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();
    private RichSelectManyShuttle systemShuttle;
    private UISelectItems systemSelectItem;
    private RichCommandButton saveAccount;
    private RichCommandButton updateAccount;
    private RichCommandButton deleteAccount;
    private RichTable branchTable;
    private RichInputText branchId;
    private RichTable countryTable;
    private RichInputText countryId;
    private RichTable townTable;
    private RichInputText townId;
    private RichInputDate regyear;
    private RichInputText regno;
    private RichInputDate regWef;
    private RichInputDate regWet;
    private RichSelectOneChoice regAccepted;
    private RichInputNumberSpinbox regGracePeriod;
    private RichCommandButton saveRegDetails;
    private RichInputText regkey;
    private RichTable registrationTable;
    private RichTable agentDirectorsTable;
    private RichCommandButton saveAgDirectorButton;
    private RichInputDate directYr;
    private RichInputText directname;
    private RichInputText directQualifications;
    private RichInputNumberSpinbox directshare;
    private RichInputText agencyDirectorId;
    private RichInputText refereeName;
    private RichInputText refereephAddress;
    private RichInputText refereePostAddress;
    private RichInputText refereeIDNo;
    private RichInputText refereeEmail;
    private RichInputText refereeTelNo;
    private RichCommandButton saveRefereeAgButton;
    private RichTable agencyRefereeTable;
    private RichInputText refereeId;
    private RichCommandButton saveWebUsersButton;
    private RichInputText webusername;
    private RichInputText webfullNames;
    private RichInputText webEmail;
    private RichInputText webPersonalRank;
    private RichSelectOneRadio webAllowLogin;
    private RichSelectOneChoice webUserStatus;
    private RichSelectOneRadio webReset;
    private RichInputText webPassword;
    private RichTable webUsersTable;
    private RichInputText webUserId;

    public void setAccountTable(RichTable accountTable) {
        this.accountTable = accountTable;
    }

    public RichTable getAccountTable() {
        return accountTable;
    }

    public void setSmapping(RichSelectOneChoice smapping) {
        this.smapping = smapping;
    }

    public RichSelectOneChoice getSmapping() {
        return smapping;
    }

    public void setId(RichInputText id) {
        this.id = id;
    }

    public RichInputText getId() {
        return id;
    }

    public void setCmapping(RichInputText cmapping) {
        this.cmapping = cmapping;
    }

    public RichInputText getCmapping() {
        return cmapping;
    }

    public void setWhRate(RichInputNumberSpinbox whRate) {
        this.whRate = whRate;
    }

    public RichInputNumberSpinbox getWhRate() {
        return whRate;
    }

    public void setOvrate(RichInputNumberSpinbox ovrate) {
        this.ovrate = ovrate;
    }

    public RichInputNumberSpinbox getOvrate() {
        return ovrate;
    }

    public void setCommrate(RichInputNumberSpinbox commrate) {
        this.commrate = commrate;
    }

    public RichInputNumberSpinbox getCommrate() {
        return commrate;
    }

    public void setAcnoformat(RichInputText acnoformat) {
        this.acnoformat = acnoformat;
    }

    public RichInputText getAcnoformat() {
        return acnoformat;
    }

    public void setVatrate(RichInputNumberSpinbox vatrate) {
        this.vatrate = vatrate;
    }

    public RichInputNumberSpinbox getVatrate() {
        return vatrate;
    }

    public void changeMapping(ValueChangeEvent evt) {
        String smapp = (String)smapping.getValue();
        if (smapp.equals("DIRECT")) {
            id.setValue("D");
        } else if (smapp.equals("FACULTATIVE IN")) {
            id.setValue("FI");
        } else if (smapp.equals("FACULTATIVE OUT")) {
            id.setValue("FO");
        } else if (smapp.equals("INSURANCE COMPANY")) {
            id.setValue("I");
        } else if (smapp.equals("REINSURANCE COMPANY")) {
            id.setValue("R");
        } else if (smapp.equals("REINSURANCE CONSULTANCY")) {
            id.setValue("RC");
        } else if (smapp.equals("CHECK-OFF INSTITUTIONS")) {
            id.setValue("CK");
        } else if (smapp.equals("DEBTORS")) {
            id.setValue("DR");
        } else if (smapp.equals("STAFF")) {
            id.setValue("ST");
        } else if (smapp.equals("INHOUSE AGENTS")) {
            id.setValue("IA");
        } else if (smapp.equals("SALVAGES")) {
            id.setValue("SV");
        } else if (smapp.equals("EXCESS")) {
            id.setValue("XS");
        } else if (smapp.equals("BDE")) {
            id.setValue("BD");
        } else if (smapp.equals("BSP")) {
            id.setValue("BS");
        } else if (smapp.equals("MSP")) {
            id.setValue("MS");
        } else if (smapp.equals("MSE")) {
            id.setValue("ME");
        } else if (smapp.equals("UNIT MANAGER")) {
            id.setValue("BE");
        } else if (smapp.equals("BRANCH MANAGERS")) {
            id.setValue("BM");
        } else if (smapp.equals("BRANCH")) {
            id.setValue("BR");
        } else if (smapp.equals("AGENCY MANAGER")) {
            id.setValue("BA");
        } else if (smapp.equals("THIRD PARTY RECOVERY")) {
            id.setValue("TR");
        } else if (smapp.equals("REGIONAL MANAGER")) {
            id.setValue("RM");
        } else if (smapp.equals("NATIONAL MANAGER")) {
            id.setValue("NM");
        } else if (smapp.equals("PETTY CASH")) {
            id.setValue("PC");
        }

    }

    public String addAccountTypes() {
        
        smapping.setValue("");
        id.setValue("");
        cmapping.setValue("");
        whRate.setValue("");
        ovrate.setValue("");
        commrate.setValue("");
        acnoformat.setValue("");
        vatrate.setValue("");
        txtCommissionLevyRate.setValue("");
        saveAccountTypes.setText("Save");
        AdfFacesContext ctx=AdfFacesContext.getCurrentInstance();
        ctx.addPartialTarget(smapping);
        ctx.addPartialTarget(id);
        ctx.addPartialTarget(cmapping);
        ctx.addPartialTarget(whRate);
        ctx.addPartialTarget(ovrate);
        ctx.addPartialTarget(commrate);
        ctx.addPartialTarget(vatrate);
        ctx.addPartialTarget(saveAccountTypes);
        ctx.addPartialTarget(txtCommissionLevyRate);
        
        GlobalCC.showPopup("pt1:accountpop");
        return null;
    }

    public void setSaveAccountTypes(RichCommandButton saveAccountTypes) {
        this.saveAccountTypes = saveAccountTypes;
    }

    public RichCommandButton getSaveAccountTypes() {
        return saveAccountTypes;
    }

    public String saveAccTypes() {
        // Add event code here...
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.acc_types_prc(?,?,?,?,?,?,?,?,?,?);end;";
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        conn =
(OracleConnection)(OracleConnection)OracleConnection.getDatabaseConnection();
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("fingAccountTypesIterator");
            RowKeySet set = accountTable.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));

                Row r = dciter.getCurrentRow();
                //ClientCode = (BigDecimal)r.getAttribute("clientCode");
                session.setAttribute("actCode", r.getAttribute("ACT_CODE"));
            }
            String shortDesc =
                GlobalCC.checkNullValues(txtShortDescformat.getValue());
            stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, (BigDecimal)session.getAttribute("actCode"));
            if (cmapping.getValue() == null) {
                stmt.setString(2, null);
            } else {
                stmt.setString(2, cmapping.getValue().toString());
            }
            if (whRate.getValue() == null) {
                stmt.setString(3, null);
            } else {
                stmt.setString(3, whRate.getValue().toString());
            }
            if (ovrate.getValue() == null) {
                stmt.setString(4, null);
            } else {
                stmt.setString(4, ovrate.getValue().toString());
            }
            if (commrate.getValue() == null) {
                stmt.setString(5, null);
            } else {
                stmt.setString(5, commrate.getValue().toString());
            }
            if (acnoformat.getValue() == null) {
                stmt.setString(6, null);
            } else {
                stmt.setString(6, acnoformat.getValue().toString());
            }
            if (vatrate.getValue() == null) {
                stmt.setString(7, null);
            } else {
                stmt.setString(7, vatrate.getValue().toString());
            }
            if (shortDesc == null) {
                stmt.setString(8, null);
            } else {
                stmt.setString(8, shortDesc.toString());
            }
            if (txtMgrNoInfix.getValue() == null) {
                stmt.setString(9, null);
            } else {
                stmt.setString(9, txtMgrNoInfix.getValue().toString());
            }


            if (txtCommissionLevyRate.getValue() == null) {
                stmt.setBigDecimal(10, null);
            } else {
                stmt.setBigDecimal(10, (BigDecimal)txtCommissionLevyRate.getValue());
            }
            stmt.execute();
            conn.commit();
            conn.close();
            session.setAttribute("actCode", null);
            ADFUtils.findIterator("fingAccountTypesIterator").executeQuery();
            GlobalCC.refreshUI(accountTable);
            GlobalCC.dismissPopUp("pt1", "accountpop");
            String message = "Record UPDATED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String updateAcountTypes() {
        Object key2 = accountTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            smapping.setValue(nodeBinding.getAttribute("smapping"));
            id.setValue(nodeBinding.getAttribute("smapping"));
            cmapping.setValue(nodeBinding.getAttribute("cmapping"));
            whRate.setValue(nodeBinding.getAttribute("wrate"));
            ovrate.setValue(nodeBinding.getAttribute("orate"));
            commrate.setValue(nodeBinding.getAttribute("crate"));
            acnoformat.setValue(nodeBinding.getAttribute("accountFormat"));
            vatrate.setValue(nodeBinding.getAttribute("vatrate"));
            txtCommissionLevyRate.setValue(nodeBinding.getAttribute("ACT_COMMISION_LEVY_RATE"));
            saveAccountTypes.setText("Save");
            saveAccountTypes.setIcon("/images/update.gif");
            txtShortDescformat.setValue(nodeBinding.getAttribute("acformat"));
            txtMgrNoInfix.setValue(nodeBinding.getAttribute("mgrNoInFix"));
            AdfFacesContext ctx=AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(smapping);
            ctx.addPartialTarget(id);
            ctx.addPartialTarget(cmapping);
            ctx.addPartialTarget(whRate);
            ctx.addPartialTarget(ovrate);
            ctx.addPartialTarget(commrate);
            ctx.addPartialTarget(vatrate);
            ctx.addPartialTarget(saveAccountTypes);
            ctx.addPartialTarget(txtCommissionLevyRate);
            
            GlobalCC.showPopup("pt1:accountpop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;
    }

    public void showAccountTypes(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    accountTypes.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)accountTypes.getRowData();
                    HttpServletRequest request =
                        (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

                    if (nd.getRow().getAttribute("type").equals("P")) {

                        session.setAttribute("accounttypeid",
                                             nd.getRow().getAttribute(0));
                        session.setAttribute("accagncode",
                                             nd.getRow().getAttribute("id"));

                        ADFUtils.findIterator("fingAccountTypesIterator").executeQuery();
                        ADFUtils.findIterator("findAgentsIterator").executeQuery();
                        GlobalCC.refreshUI(agentTable);
                        GlobalCC.refreshUI(accountTable);

                    } else if (nd.getRow().getAttribute("type").equals("S")) {

                        session.setAttribute("accounttypeid",
                                             nd.getParent().getRow().getAttribute(0));
                        session.setAttribute("accagncode",
                                             nd.getRow().getAttribute("id"));

                        DBConnector OracleConnection = new DBConnector();
                        String query =
                            "begin TQC_AGENCIES_CURSORS.get_agencies(?,?);end;";

                        OracleCallableStatement stmt = null;
                        OracleConnection conn = null;
                        try {
                            Map<String, String> sysmap =
                                new HashMap<String, String>();
                            conn =
(OracleConnection)(OracleConnection)OracleConnection.getDatabaseConnection();
                            stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                            stmt.setBigDecimal(1,
                                               (BigDecimal)nd.getRow().getAttribute("accCode"));
                            stmt.registerOutParameter(2, OracleTypes.CURSOR);
                            stmt.execute();
                            ResultSet rst = (ResultSet)stmt.getObject(2);
                            while (rst.next()) {
                                agentID.setValue(rst.getString(3));
                                agentFullName.setValue(rst.getString(4));
                                agentPhysAddress.setValue(rst.getString(5));
                                address.setValue(rst.getString(6));
                                country.setValue(rst.getString(10));
                                countryId.setValue(rst.getString(9));
                                towb.setValue(rst.getString(8));
                                townId.setValue(rst.getString(7));
                                postalAddress.setValue(rst.getString(13));
                                agBranch.setValue(rst.getString(43));
                                branchId.setValue(rst.getString(41));
                                contCode.setValue(rst.getString(46));
                                personnel.setValue(rst.getString(61));
                                stCode.setValue(rst.getString(63));
                                smsNo.setValue(rst.getString(48));
                                licensed.setValue(rst.getString(55));
                                agClass.setValue(rst.getString(1));
                                phone1.setValue(rst.getString(16));
                                phone2.setValue(rst.getString(17));
                                fax.setValue(rst.getString(18));
                                emailAddress.setValue(rst.getString(11));
                                webAddress.setValue(rst.getString(12));
                                contactPerson.setValue(rst.getString(14));
                                contactTitle.setValue(rst.getString(15));
                                gaccountNo.setValue(rst.getString(19));
                                idNumber.setValue(rst.getString(45));
                                pinNumber.setValue(rst.getString(20));
                                agStatus.setValue(rst.getString(25));
                                runOff.setValue(rst.getString(54));
                                remarks.setValue(rst.getString(58));
                                sector.setValue(rst.getString(1));
                                affiliateTo.setValue(rst.getString(59));
                                holdCompany.setValue(rst.getString(60));
                                stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_CURSORS.get_agn_unassigned_sys(?,?);end;");
                                stmt.setBigDecimal(1,
                                                   (BigDecimal)nd.getRow().getAttribute("accCode"));
                                stmt.registerOutParameter(2,
                                                          OracleTypes.CURSOR);
                                stmt.execute();
                                rst = (ResultSet)stmt.getObject(2);
                                while (rst.next()) {
                                    selectValues.add(new SelectItem(rst.getString(2),
                                                                    rst.getString(3)));
                                    sysmap.put(rst.getString(2),
                                               rst.getString(3) + "*" +
                                               rst.getString(1));
                                }
                                stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_CURSORS.get_agn_assigned_sys(?,?);end;");
                                stmt.setBigDecimal(1,
                                                   (BigDecimal)nd.getRow().getAttribute("accCode"));
                                stmt.registerOutParameter(2,
                                                          OracleTypes.CURSOR);
                                stmt.execute();
                                rst = (ResultSet)stmt.getObject(2);
                                while (rst.next()) {
                                    displayValue.add(rst.getString(6));
                                }

                            }
                            systemShuttle.setValue(displayValue);
                            systemSelectItem.setValue(selectValues);
                            saveAccount.setDisabled(true);
                            updateAccount.setDisabled(false);
                            deleteAccount.setDisabled(false);
                            rst.close();
                            stmt.close();
                            conn.close();
                            session.setAttribute("systemssMap", sysmap);

                            ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
                            ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                            ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                            ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
                            GlobalCC.refreshUI(infoTab);
                            GlobalCC.refreshUI(detAccount);

                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                        }
                    }
                }
            }
        }
    }

    public void setAccountTypes(RichTree accountTypes) {
        this.accountTypes = accountTypes;
    }

    public RichTree getAccountTypes() {
        return accountTypes;
    }

    public void setAgentTable(RichTable agentTable) {
        this.agentTable = agentTable;
    }

    public RichTable getAgentTable() {
        return agentTable;
    }

    public void SelectAgent(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Map<String, String> sysmap = new HashMap<String, String>();
            DCIteratorBinding binder =
                ADFUtils.findIterator("findAgentsIterator");
            RowKeySet set = selectionEvent.getAddedSet();
            Iterator row = set.iterator();
            DBConnector OracleConnection = new DBConnector();
            String query = "begin TQC_AGENCIES_CURSORS.get_agencies(?,?);end;";
            OracleCallableStatement stmt = null;
            OracleConnection conn = null;
            while (row.hasNext()) {
                List data = (List)row.next();
                Key key = (Key)data.get(0);
                binder.setCurrentRowWithKey(key.toStringFormat(true));
                Row rows = binder.getCurrentRow();
                session.setAttribute("accagncode", rows.getAttribute("id"));
                String agncode = (String)rows.getAttribute("id");
                try {
                    conn =
(OracleConnection)(OracleConnection)OracleConnection.getDatabaseConnection();
                    stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                    stmt.setBigDecimal(1, new BigDecimal(agncode));
                    stmt.registerOutParameter(2, OracleTypes.CURSOR);
                    stmt.execute();
                    ResultSet rst = (ResultSet)stmt.getObject(2);
                    while (rst.next()) {
                        agentID.setValue(rst.getString(3));
                        agentFullName.setValue(rst.getString(4));
                        agentPhysAddress.setValue(rst.getString(5));
                        address.setValue(rst.getString(6));
                        country.setValue(rst.getString(10));
                        countryId.setValue(rst.getString(9));
                        towb.setValue(rst.getString(8));
                        townId.setValue(rst.getString(7));
                        postalAddress.setValue(rst.getString(13));
                        agBranch.setValue(rst.getString(43));
                        branchId.setValue(rst.getString(41));
                        contCode.setValue(rst.getString(46));
                        personnel.setValue(rst.getString(61));
                        stCode.setValue(rst.getString(63));
                        smsNo.setValue(rst.getString(48));
                        licensed.setValue(rst.getString(55));
                        agClass.setValue(rst.getString(1));
                        phone1.setValue(rst.getString(16));
                        phone2.setValue(rst.getString(17));
                        fax.setValue(rst.getString(18));
                        emailAddress.setValue(rst.getString(11));
                        webAddress.setValue(rst.getString(12));
                        contactPerson.setValue(rst.getString(14));
                        contactTitle.setValue(rst.getString(15));
                        gaccountNo.setValue(rst.getString(19));
                        idNumber.setValue(rst.getString(45));
                        pinNumber.setValue(rst.getString(20));
                        agStatus.setValue(rst.getString(25));
                        runOff.setValue(rst.getString(54));
                        remarks.setValue(rst.getString(58));
                        sector.setValue(rst.getString(1));
                        affiliateTo.setValue(rst.getString(59));
                        holdCompany.setValue(rst.getString(60));
                        stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_CURSORS.get_agn_unassigned_sys(?,?);end;");
                        stmt.setBigDecimal(1, new BigDecimal(agncode));
                        stmt.registerOutParameter(2, OracleTypes.CURSOR);
                        stmt.execute();
                        rst = (ResultSet)stmt.getObject(2);
                        while (rst.next()) {
                            selectValues.add(new SelectItem(rst.getString(2),
                                                            rst.getString(3)));
                            sysmap.put(rst.getString(2),
                                       rst.getString(3) + "*" +
                                       rst.getString(1));
                        }
                        stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_CURSORS.get_agn_assigned_sys(?,?);end;");
                        stmt.setBigDecimal(1, new BigDecimal(agncode));
                        stmt.registerOutParameter(2, OracleTypes.CURSOR);
                        stmt.execute();
                        rst = (ResultSet)stmt.getObject(2);
                        while (rst.next()) {
                            displayValue.add(rst.getString(6));
                        }

                    }
                    systemShuttle.setValue(displayValue);
                    systemSelectItem.setValue(selectValues);
                    saveAccount.setDisabled(true);
                    updateAccount.setDisabled(false);
                    deleteAccount.setDisabled(false);
                    rst.close();
                    stmt.close();
                    conn.close();
                    session.setAttribute("systemssMap", sysmap);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }
            ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
            ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
            ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
            ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
            GlobalCC.refreshUI(infoTab);
        }
    }

    public void setInfoTab(RichPanelTabbed infoTab) {
        this.infoTab = infoTab;
    }

    public RichPanelTabbed getInfoTab() {
        return infoTab;
    }

    public void setAgentID(RichInputText agentID) {
        this.agentID = agentID;
    }

    public RichInputText getAgentID() {
        return agentID;
    }

    public void setAgentFullName(RichInputText agentFullName) {
        this.agentFullName = agentFullName;
    }

    public RichInputText getAgentFullName() {
        return agentFullName;
    }

    public void setAgentPhysAddress(RichInputText agentPhysAddress) {
        this.agentPhysAddress = agentPhysAddress;
    }

    public RichInputText getAgentPhysAddress() {
        return agentPhysAddress;
    }

    public void setAddress(RichInputText address) {
        this.address = address;
    }

    public RichInputText getAddress() {
        return address;
    }

    public void setCountry(RichInputText country) {
        this.country = country;
    }

    public RichInputText getCountry() {
        return country;
    }

    public void setTowb(RichInputText towb) {
        this.towb = towb;
    }

    public RichInputText getTowb() {
        return towb;
    }

    public void setPostalAddress(RichInputText postalAddress) {
        this.postalAddress = postalAddress;
    }

    public RichInputText getPostalAddress() {
        return postalAddress;
    }

    public void setAgBranch(RichInputText agBranch) {
        this.agBranch = agBranch;
    }

    public RichInputText getAgBranch() {
        return agBranch;
    }

    public void setContCode(RichInputText contCode) {
        this.contCode = contCode;
    }

    public RichInputText getContCode() {
        return contCode;
    }

    public void setPersonnel(RichInputText personnel) {
        this.personnel = personnel;
    }

    public RichInputText getPersonnel() {
        return personnel;
    }

    public void setStCode(RichInputText stCode) {
        this.stCode = stCode;
    }

    public RichInputText getStCode() {
        return stCode;
    }

    public void setSmsNo(RichInputText smsNo) {
        this.smsNo = smsNo;
    }

    public RichInputText getSmsNo() {
        return smsNo;
    }

    public void setLicensed(RichSelectOneChoice licensed) {
        this.licensed = licensed;
    }

    public RichSelectOneChoice getLicensed() {
        return licensed;
    }

    public void setAgClass(RichInputText agClass) {
        this.agClass = agClass;
    }

    public RichInputText getAgClass() {
        return agClass;
    }

    public void setPhone1(RichInputText phone1) {
        this.phone1 = phone1;
    }

    public RichInputText getPhone1() {
        return phone1;
    }

    public void setPhone2(RichInputText phone2) {
        this.phone2 = phone2;
    }

    public RichInputText getPhone2() {
        return phone2;
    }

    public void setFax(RichInputText fax) {
        this.fax = fax;
    }

    public RichInputText getFax() {
        return fax;
    }

    public void setEmailAddress(RichInputText emailAddress) {
        this.emailAddress = emailAddress;
    }

    public RichInputText getEmailAddress() {
        return emailAddress;
    }

    public void setWebAddress(RichInputText webAddress) {
        this.webAddress = webAddress;
    }

    public RichInputText getWebAddress() {
        return webAddress;
    }

    public void setContactPerson(RichInputText contactPerson) {
        this.contactPerson = contactPerson;
    }

    public RichInputText getContactPerson() {
        return contactPerson;
    }

    public void setContactTitle(RichInputText contactTitle) {
        this.contactTitle = contactTitle;
    }

    public RichInputText getContactTitle() {
        return contactTitle;
    }

    public void setGaccountNo(RichInputText gaccountNo) {
        this.gaccountNo = gaccountNo;
    }

    public RichInputText getGaccountNo() {
        return gaccountNo;
    }

    public void setIdNumber(RichInputText idNumber) {
        this.idNumber = idNumber;
    }

    public RichInputText getIdNumber() {
        return idNumber;
    }

    public void setPinNumber(RichInputText pinNumber) {
        this.pinNumber = pinNumber;
    }

    public RichInputText getPinNumber() {
        return pinNumber;
    }

    public void setAgStatus(RichSelectOneChoice agStatus) {
        this.agStatus = agStatus;
    }

    public RichSelectOneChoice getAgStatus() {
        return agStatus;
    }

    public void setRunOff(RichSelectOneChoice runOff) {
        this.runOff = runOff;
    }

    public RichSelectOneChoice getRunOff() {
        return runOff;
    }

    public void setRemarks(RichInputText remarks) {
        this.remarks = remarks;
    }

    public RichInputText getRemarks() {
        return remarks;
    }

    public void setSector(RichInputText sector) {
        this.sector = sector;
    }

    public RichInputText getSector() {
        return sector;
    }

    public void setAffiliateTo(RichInputText affiliateTo) {
        this.affiliateTo = affiliateTo;
    }

    public RichInputText getAffiliateTo() {
        return affiliateTo;
    }

    public void setHoldCompany(RichInputText holdCompany) {
        this.holdCompany = holdCompany;
    }

    public RichInputText getHoldCompany() {
        return holdCompany;
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

    public void setSystemShuttle(RichSelectManyShuttle systemShuttle) {
        this.systemShuttle = systemShuttle;
    }

    public RichSelectManyShuttle getSystemShuttle() {
        return systemShuttle;
    }

    public void setSystemSelectItem(UISelectItems systemSelectItem) {
        this.systemSelectItem = systemSelectItem;
    }

    public UISelectItems getSystemSelectItem() {
        return systemSelectItem;
    }

    public void setSaveAccount(RichCommandButton saveAccount) {
        this.saveAccount = saveAccount;
    }

    public RichCommandButton getSaveAccount() {
        return saveAccount;
    }

    public void setUpdateAccount(RichCommandButton updateAccount) {
        this.updateAccount = updateAccount;
    }

    public RichCommandButton getUpdateAccount() {
        return updateAccount;
    }

    public void setDeleteAccount(RichCommandButton deleteAccount) {
        this.deleteAccount = deleteAccount;
    }

    public RichCommandButton getDeleteAccount() {
        return deleteAccount;
    }

    public String saveAccountaction() {
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_AGENCIES_PKG.TQC_AGENCIES_PRC(?,?,?,?);end;";
        ARRAY array = null;

        List listOfAgents = new LinkedList();
        AgentBean agent = new AgentBean();
        agent.setAGN_SHT_DESC((String)agentID.getValue());
        agent.setAGN_NAME((String)agentFullName.getValue());
        agent.setAGN_PHYSICAL_ADDRESS((String)agentPhysAddress.getValue());
        agent.setAGN_POSTAL_ADDRESS((String)address.getValue());
        agent.setAGN_COU_CODE(new BigDecimal((String)countryId.getValue()));
        agent.setAGN_TWN_CODE(new BigDecimal((String)townId.getValue()));
        agent.setAGN_ZIP((String)postalAddress.getValue());
        agent.setAGN_COUNTRY((String)agBranch.getValue());
        agent.setAGN_CON_CODE((String)contCode.getValue());
        agent.setAGN_SMS_TEL((String)smsNo.getValue());
        agent.setAGN_LICENSED((String)licensed.getValue());
        agent.setAGN_AGNC_CLASS_CODE((String)agClass.getValue());
        agent.setAGN_TEL1((String)phone1.getValue());
        agent.setAGN_TEL2((String)phone2.getValue());
        agent.setAGN_FAX((String)fax.getValue());
        agent.setAGN_EMAIL_ADDRESS((String)emailAddress.getValue());
        agent.setAGN_WEB_ADDRESS((String)webAddress.getValue());
        agent.setAGN_CONTACT_PERSON((String)contactPerson.getValue());
        agent.setAGN_CONTACT_TITLE((String)contactTitle.getValue());
        agent.setAGN_ACC_NO((String)gaccountNo.getValue());
        agent.setAGN_ID_NO((String)idNumber.getValue());
        agent.setAGN_PIN((String)pinNumber.getValue());
        agent.setAGN_STATUS((String)agStatus.getValue());
        agent.setAGN_RUNOFF((String)runOff.getValue());
        agent.setAGN_STATUS_REMARKS((String)remarks.getValue());
        agent.setAGN_SEC_CODE(new BigDecimal((String)sector.getValue()));
        if (affiliateTo.getValue() != null)
            agent.setAGN_AGN_CODE(new BigDecimal((String)affiliateTo.getValue()));
        else
            agent.setAGN_AGN_CODE(null);
        if (holdCompany.getValue() != null)
            agent.setAGN_AHC_CODE(new BigDecimal((String)holdCompany.getValue()));
        else
            agent.setAGN_AHC_CODE(null);
        agent.setAGN_ACT_CODE(new BigDecimal((String)(session.getAttribute("accounttypeid"))));
        agent.setAGN_BRN_CODE(new BigDecimal((String)branchId.getValue()));
        listOfAgents.add(agent);
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        try {
            conn = OracleConnection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_AGENCIES_TAB", conn);
            array =
                    new ARRAY(descriptor, conn, (Object[])listOfAgents.toArray());
            stmt.setString(1, "A");
            stmt.setArray(2, array);
            stmt.setString(3, "MSHOTE");
            stmt.registerOutParameter(4, OracleTypes.VARCHAR);
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }
        return null;
    }

    public String updateAccountAction() {
        DBConnector OracleConnection = new DBConnector();
        String query = "begin TQC_AGENCIES_PKG.TQC_AGENCIES_PRC(?,?,?,?);end;";
        ARRAY array = null;

        List listOfAgents = new LinkedList();
        AgentBean agent = new AgentBean();
        agent.setAGN_SHT_DESC((String)agentID.getValue());
        agent.setAGN_NAME((String)agentFullName.getValue());
        agent.setAGN_PHYSICAL_ADDRESS((String)agentPhysAddress.getValue());
        agent.setAGN_POSTAL_ADDRESS((String)address.getValue());
        agent.setAGN_COU_CODE(new BigDecimal((String)countryId.getValue()));
        agent.setAGN_TWN_CODE(new BigDecimal((String)townId.getValue()));
        agent.setAGN_ZIP((String)postalAddress.getValue());
        agent.setAGN_COUNTRY((String)agBranch.getValue());
        agent.setAGN_CON_CODE((String)contCode.getValue());
        agent.setAGN_SMS_TEL((String)smsNo.getValue());
        agent.setAGN_LICENSED((String)licensed.getValue());
        agent.setAGN_AGNC_CLASS_CODE((String)agClass.getValue());
        agent.setAGN_TEL1((String)phone1.getValue());
        agent.setAGN_TEL2((String)phone2.getValue());
        agent.setAGN_FAX((String)fax.getValue());
        agent.setAGN_EMAIL_ADDRESS((String)emailAddress.getValue());
        agent.setAGN_WEB_ADDRESS((String)webAddress.getValue());
        agent.setAGN_CONTACT_PERSON((String)contactPerson.getValue());
        agent.setAGN_CONTACT_TITLE((String)contactTitle.getValue());
        agent.setAGN_ACC_NO((String)gaccountNo.getValue());
        agent.setAGN_ID_NO((String)idNumber.getValue());
        agent.setAGN_PIN((String)pinNumber.getValue());
        agent.setAGN_STATUS((String)agStatus.getValue());
        agent.setAGN_RUNOFF((String)runOff.getValue());
        agent.setAGN_STATUS_REMARKS((String)remarks.getValue());
        agent.setAGN_SEC_CODE(new BigDecimal((String)sector.getValue()));
        if (affiliateTo.getValue() != null)
            agent.setAGN_AGN_CODE(new BigDecimal((String)affiliateTo.getValue()));
        else
            agent.setAGN_AGN_CODE(null);
        if (holdCompany.getValue() != null)
            agent.setAGN_AHC_CODE(new BigDecimal((String)holdCompany.getValue()));
        else
            agent.setAGN_AHC_CODE(null);
        agent.setAGN_ACT_CODE(new BigDecimal((String)(session.getAttribute("accounttypeid"))));
        agent.setAGN_BRN_CODE(new BigDecimal((String)branchId.getValue()));
        agent.setAGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
        listOfAgents.add(agent);
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;
        try {
            conn = (OracleConnection)OracleConnection.getDatabaseConnection();
            stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_AGENCIES_TAB", conn);
            array =
                    new ARRAY(descriptor, conn, (Object[])listOfAgents.toArray());
            stmt.setString(1, "E");
            stmt.setArray(2, array);
            stmt.setString(3, "MSHOTE");
            stmt.registerOutParameter(4, OracleTypes.VARCHAR);
            stmt.execute();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }
        return null;
    }

    public String deleteAccountAction() {
        // Add event code here...
        return null;
    }

    public String selectBranch() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAccountBranchesIterator");
        RowKeySet set = branchTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            agBranch.setValue(rows.getAttribute("name"));
            branchId.setValue(rows.getAttribute("id"));
        }
        return null;

    }

    public void setBranchTable(RichTable branchTable) {
        this.branchTable = branchTable;
    }

    public RichTable getBranchTable() {
        return branchTable;
    }

    public void setBranchId(RichInputText branchId) {
        this.branchId = branchId;
    }

    public RichInputText getBranchId() {
        return branchId;
    }

    public String selectCountry() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAccountCountriesIterator");
        RowKeySet set = countryTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            country.setValue(rows.getAttribute("name"));
            countryId.setValue(rows.getAttribute("id"));
            session.setAttribute("countryId", rows.getAttribute("id"));
            ADFUtils.findIterator("findAccountTownsIterator").executeQuery();
            GlobalCC.refreshUI(townTable);
        }
        return null;
    }

    public void setCountryTable(RichTable countryTable) {
        this.countryTable = countryTable;
    }

    public RichTable getCountryTable() {
        return countryTable;
    }

    public void setCountryId(RichInputText countryId) {
        this.countryId = countryId;
    }

    public RichInputText getCountryId() {
        return countryId;
    }

    public String selectTown() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAccountTownsIterator");
        RowKeySet set = townTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            towb.setValue(rows.getAttribute("name"));
            townId.setValue(rows.getAttribute("id"));
        }
        return null;
    }

    public void setTownTable(RichTable townTable) {
        this.townTable = townTable;
    }

    public RichTable getTownTable() {
        return townTable;
    }

    public void setTownId(RichInputText townId) {
        this.townId = townId;
    }

    public RichInputText getTownId() {
        return townId;
    }

    public String addRegDetails() {
        //regPop
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:regPop" +
                             "').show(hints);");
        regyear.setValue("");
        regno.setValue("");
        regWef.setValue("");
        regWet.setValue("");
        regAccepted.setValue("");
        saveRegDetails.setText("Save");
        return null;
    }

    public void setRegyear(RichInputDate regyear) {
        this.regyear = regyear;
    }

    public RichInputDate getRegyear() {
        return regyear;
    }

    public void setRegno(RichInputText regno) {
        this.regno = regno;
    }

    public RichInputText getRegno() {
        return regno;
    }

    public void setRegWef(RichInputDate regWef) {
        this.regWef = regWef;
    }

    public RichInputDate getRegWef() {
        return regWef;
    }

    public void setRegWet(RichInputDate regWet) {
        this.regWet = regWet;
    }

    public RichInputDate getRegWet() {
        return regWet;
    }

    public void setRegAccepted(RichSelectOneChoice regAccepted) {
        this.regAccepted = regAccepted;
    }

    public RichSelectOneChoice getRegAccepted() {
        return regAccepted;
    }

    public void setRegGracePeriod(RichInputNumberSpinbox regGracePeriod) {
        this.regGracePeriod = regGracePeriod;
    }

    public RichInputNumberSpinbox getRegGracePeriod() {
        return regGracePeriod;
    }

    public void setSaveRegDetails(RichCommandButton saveRegDetails) {
        this.saveRegDetails = saveRegDetails;
    }

    public RichCommandButton getSaveRegDetails() {
        return saveRegDetails;
    }

    public String saveRegistrationDetails() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REGISTRATION_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (saveRegDetails.getText().equals("Save")) {
            AgentRegistration regbean = new AgentRegistration();
            Date year = (Date)regyear.getValue();
            LinkedList reglist = new LinkedList();
            BigDecimal yr =
                new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setAREG_CODE(null);
            regbean.setAREG_ACCEPTED((String)regAccepted.getValue());
            regbean.setAREG_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setAREG_REG_NO((String)regno.getValue());
            Date wef = (Date)regWef.getValue();
            regbean.setAREG_WEF(new java.sql.Date(wef.getTime()));
            Date wet = (Date)regWet.getValue();
            regbean.setAREG_WET(new java.sql.Date(wet.getTime()));
            regbean.setAREG_YEAR(yr);
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "A");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                try {
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            }

        } else if (saveRegDetails.getText().equals("Update")) {
            AgentRegistration regbean = new AgentRegistration();
            Date year = (Date)regyear.getValue();
            LinkedList reglist = new LinkedList();
            BigDecimal yr =
                new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setAREG_CODE(new BigDecimal((String)regkey.getValue()));
            regbean.setAREG_ACCEPTED((String)regAccepted.getValue());
            regbean.setAREG_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setAREG_REG_NO((String)regno.getValue());
            Date wef = (Date)regWef.getValue();
            regbean.setAREG_WEF(new java.sql.Date(wef.getTime()));
            Date wet = (Date)regWet.getValue();
            regbean.setAREG_WET(new java.sql.Date(wet.getTime()));
            regbean.setAREG_YEAR(yr);
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                try {
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            }
        }
        ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
        GlobalCC.refreshUI(registrationTable);
        return null;
    }

    public String updateRegDetails() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:regPop" +
                             "').show(hints);");
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsRegistrationIterator");
        RowKeySet set = registrationTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            regyear.setValue(rows.getAttribute("year"));
            regno.setValue(rows.getAttribute("regno"));
            regWef.setValue(rows.getAttribute("wef"));
            regWet.setValue(rows.getAttribute("wet"));
            regAccepted.setValue(rows.getAttribute("accepted"));
            regkey.setValue(rows.getAttribute("id"));
            saveRegDetails.setText("Update");
        }
        return null;
    }

    public void setRegkey(RichInputText regkey) {
        this.regkey = regkey;
    }

    public RichInputText getRegkey() {
        return regkey;
    }

    public void setRegistrationTable(RichTable registrationTable) {
        this.registrationTable = registrationTable;
    }

    public RichTable getRegistrationTable() {
        return registrationTable;
    }

    public String deleteRegDetails() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsRegistrationIterator");
        RowKeySet set = registrationTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REGISTRATION_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            AgentRegistration regbean = new AgentRegistration();
            LinkedList reglist = new LinkedList();
            regbean.setAREG_CODE(new BigDecimal((String)rows.getAttribute("id")));
            regbean.setAREG_ACCEPTED(null);
            regbean.setAREG_AGN_CODE(null);
            regbean.setAREG_REG_NO(null);
            regbean.setAREG_WEF(null);
            regbean.setAREG_WET(null);
            regbean.setAREG_YEAR(null);
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "D");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);

                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                try {
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            }
            ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
            GlobalCC.refreshUI(registrationTable);
        }
        return null;
    }

    public void setAgentDirectorsTable(RichTable agentDirectorsTable) {
        this.agentDirectorsTable = agentDirectorsTable;
    }

    public RichTable getAgentDirectorsTable() {
        return agentDirectorsTable;
    }

    public void setSaveAgDirectorButton(RichCommandButton saveAgDirectorButton) {
        this.saveAgDirectorButton = saveAgDirectorButton;
    }

    public RichCommandButton getSaveAgDirectorButton() {
        return saveAgDirectorButton;
    }

    public void setDirectYr(RichInputDate directYr) {
        this.directYr = directYr;
    }

    public RichInputDate getDirectYr() {
        return directYr;
    }

    public void setDirectname(RichInputText directname) {
        this.directname = directname;
    }

    public RichInputText getDirectname() {
        return directname;
    }

    public void setDirectQualifications(RichInputText directQualifications) {
        this.directQualifications = directQualifications;
    }

    public RichInputText getDirectQualifications() {
        return directQualifications;
    }

    public void setDirectshare(RichInputNumberSpinbox directshare) {
        this.directshare = directshare;
    }

    public RichInputNumberSpinbox getDirectshare() {
        return directshare;
    }

    public String addAgencyDirectors() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agentDirectorsPop" + "').show(hints);");
        directYr.setValue("");
        directname.setValue("");
        directQualifications.setValue("");
        directshare.setValue("");
        saveAgDirectorButton.setText("Save");
        return null;
    }

    public String saveAgDirectorsAction() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_DIRECTORS_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (saveAgDirectorButton.getText().equals("Save")) {
            AgDirector regbean = new AgDirector();
            Date year = (Date)directYr.getValue();
            LinkedList reglist = new LinkedList();
            BigDecimal yr =
                new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setADIR_CODE(null);
            regbean.setADIR_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setADIR_DESIGNATION("");
            regbean.setADIR_NAME((String)directname.getValue());
            regbean.setADIR_PCT_HOLDG(new BigDecimal(directshare.getValue().toString()));
            regbean.setADIR_YEAR(yr);
            regbean.setADIR_QUALIFICATIONS((String)directQualifications.getValue());
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "A");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);

                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else if (saveAgDirectorButton.getText().equals("Update")) {
            AgDirector regbean = new AgDirector();
            Date year = (Date)directYr.getValue();
            LinkedList reglist = new LinkedList();
            BigDecimal yr =
                new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setADIR_CODE(new BigDecimal((String)agencyDirectorId.getValue()));
            regbean.setADIR_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setADIR_DESIGNATION("");
            regbean.setADIR_NAME((String)directname.getValue());
            regbean.setADIR_PCT_HOLDG(new BigDecimal(directshare.getValue().toString()));
            regbean.setADIR_YEAR(yr);
            regbean.setADIR_QUALIFICATIONS((String)directQualifications.getValue());
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }


        }
        ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
        GlobalCC.refreshUI(agentDirectorsTable);
        return null;
    }

    public String editAgencyDirectors() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agentDirectorsPop" + "').show(hints);");
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsDirectorsIterator");
        RowKeySet set = agentDirectorsTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            agencyDirectorId.setValue(rows.getAttribute("id"));
            directYr.setValue(rows.getAttribute("year"));
            directname.setValue(rows.getAttribute("name"));
            directQualifications.setValue(rows.getAttribute("qualification"));
            directshare.setValue(rows.getAttribute("shareholding"));
            saveAgDirectorButton.setText("Update");
        }
        return null;
    }

    public void setAgencyDirectorId(RichInputText agencyDirectorId) {
        this.agencyDirectorId = agencyDirectorId;
    }

    public RichInputText getAgencyDirectorId() {
        return agencyDirectorId;
    }

    public String deleteAgencyDirectors() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_DIRECTORS_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsDirectorsIterator");
        RowKeySet set = agentDirectorsTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            AgDirector regbean = new AgDirector();
            LinkedList reglist = new LinkedList();
            regbean.setADIR_CODE(new BigDecimal((String)rows.getAttribute("id")));
            regbean.setADIR_AGN_CODE(null);
            regbean.setADIR_DESIGNATION(null);
            regbean.setADIR_NAME(null);
            regbean.setADIR_PCT_HOLDG(null);
            regbean.setADIR_YEAR(null);
            regbean.setADIR_QUALIFICATIONS(null);
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "D");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);

                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
        GlobalCC.refreshUI(agentDirectorsTable);
        return null;
    }

    public String saveAgencyReferees() {
        //
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agencyRefereePop" + "').show(hints);");
        refereeName.setValue("");
        refereephAddress.setValue("");
        refereePostAddress.setValue("");
        refereeIDNo.setValue("");
        refereeEmail.setValue("");
        refereeTelNo.setValue("");
        saveRefereeAgButton.setText("Save");
        return null;
    }

    public void setRefereeName(RichInputText refereeName) {
        this.refereeName = refereeName;
    }

    public RichInputText getRefereeName() {
        return refereeName;
    }

    public void setRefereephAddress(RichInputText refereephAddress) {
        this.refereephAddress = refereephAddress;
    }

    public RichInputText getRefereephAddress() {
        return refereephAddress;
    }

    public void setRefereePostAddress(RichInputText refereePostAddress) {
        this.refereePostAddress = refereePostAddress;
    }

    public RichInputText getRefereePostAddress() {
        return refereePostAddress;
    }

    public void setRefereeIDNo(RichInputText refereeIDNo) {
        this.refereeIDNo = refereeIDNo;
    }

    public RichInputText getRefereeIDNo() {
        return refereeIDNo;
    }

    public void setRefereeEmail(RichInputText refereeEmail) {
        this.refereeEmail = refereeEmail;
    }

    public RichInputText getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeTelNo(RichInputText refereeTelNo) {
        this.refereeTelNo = refereeTelNo;
    }

    public RichInputText getRefereeTelNo() {
        return refereeTelNo;
    }

    public void setSaveRefereeAgButton(RichCommandButton saveRefereeAgButton) {
        this.saveRefereeAgButton = saveRefereeAgButton;
    }

    public RichCommandButton getSaveRefereeAgButton() {
        return saveRefereeAgButton;
    }

    public String saveRefereeAgencies() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REFEREES_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (saveRefereeAgButton.getText().equals("Save")) {
            AgencyReferee regbean = new AgencyReferee();
            LinkedList reglist = new LinkedList();
            regbean.setAREF_CODE(null);
            regbean.setAREF_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setAREF_NAME((String)refereeName.getValue());
            regbean.setAREF_PHYSICAL_ADDRESS((String)refereephAddress.getValue());
            regbean.setAREF_POSTAL_ADDRESS((String)refereePostAddress.getValue());
            regbean.setAREF_ID_NO((String)refereeIDNo.getValue());
            regbean.setAREF_EMAIL_ADDRESS((String)refereeEmail.getValue());
            regbean.setAREF_TEL((String)refereeTelNo.getValue());
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "A");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);

                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else if (saveRefereeAgButton.getText().equals("Update")) {
            AgencyReferee regbean = new AgencyReferee();
            LinkedList reglist = new LinkedList();
            regbean.setAREF_CODE(new BigDecimal((String)refereeId.getValue()));
            regbean.setAREF_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
            regbean.setAREF_NAME((String)refereeName.getValue());
            regbean.setAREF_PHYSICAL_ADDRESS((String)refereephAddress.getValue());
            regbean.setAREF_POSTAL_ADDRESS((String)refereePostAddress.getValue());
            regbean.setAREF_ID_NO((String)refereeIDNo.getValue());
            regbean.setAREF_EMAIL_ADDRESS((String)refereeEmail.getValue());
            regbean.setAREF_TEL((String)refereeTelNo.getValue());
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();

                stmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }
        ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
        GlobalCC.refreshUI(agencyRefereeTable);

        return null;
    }

    public void setAgencyRefereeTable(RichTable agencyRefereeTable) {
        this.agencyRefereeTable = agencyRefereeTable;
    }

    public RichTable getAgencyRefereeTable() {
        return agencyRefereeTable;
    }

    public String updateAgencyReferees() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agencyRefereePop" + "').show(hints);");
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsRefereesIterator");
        RowKeySet set = agencyRefereeTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            refereeId.setValue(rows.getAttribute("id"));
            refereeName.setValue(rows.getAttribute("name"));
            refereephAddress.setValue(rows.getAttribute("phyaddress"));
            refereePostAddress.setValue(rows.getAttribute("postaddress"));
            refereeIDNo.setValue(rows.getAttribute("idno"));
            refereeEmail.setValue(rows.getAttribute("email"));
            refereeTelNo.setValue(rows.getAttribute("telno"));
            saveRefereeAgButton.setText("Update");
        }
        return null;
    }

    public void setRefereeId(RichInputText refereeId) {
        this.refereeId = refereeId;
    }

    public RichInputText getRefereeId() {
        return refereeId;
    }

    public String deleteAgencyReferee() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REFEREES_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgentsRefereesIterator");
        RowKeySet set = agencyRefereeTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            AgencyReferee regbean = new AgencyReferee();
            LinkedList reglist = new LinkedList();
            regbean.setAREF_CODE(new BigDecimal((String)rows.getAttribute("id")));
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "D");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();

                stmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }
        ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
        GlobalCC.refreshUI(agencyRefereeTable);
        return null;
    }

    public void updateAgentSystems(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        HashMap<String, String> map =
            (HashMap<String, String>)session.getAttribute("systemssMap");
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            conn = (OracleConnection)datahandler.getDatabaseConnection();
            ArrayList<SelectItem> selectitems =
                (ArrayList<SelectItem>)systemSelectItem.getValue();
            ArrayList<String> select =
                (ArrayList<String>)systemShuttle.getValue();
            int i = 0;
            while (i < selectitems.size()) {
                SelectItem item = (SelectItem)selectitems.get(i);
                try {
                    String val = map.get(item.getValue());
                    String code = val.split("\\*")[1];
                    stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_PKG.revoke_agent_system(?,?);end;");
                    BigDecimal agncode =
                        new BigDecimal((String)session.getAttribute("accagncode"));
                    BigDecimal syscode = new BigDecimal(code);
                    stmt.setBigDecimal(1, agncode);
                    stmt.setBigDecimal(2, syscode);
                    stmt.execute();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
                i++;
            }
            int j = 0;
            while (j < select.size()) {
                try {
                    stmt =
(OracleCallableStatement)conn.prepareCall("begin TQC_AGENCIES_PKG.grant_agent_system(?,?,?,?,?,?);end;");
                    String val = map.get(select.get(j));
                    String code = val.split("\\*")[1];
                    BigDecimal agncode =
                        new BigDecimal((String)session.getAttribute("accagncode"));
                    BigDecimal syscode = new BigDecimal(code);
                    stmt.setString(1, "A");
                    stmt.setBigDecimal(2, agncode);
                    stmt.setBigDecimal(3, syscode);
                    stmt.setDate(4, null);
                    stmt.setDate(5, null);
                    stmt.setString(6, null);
                    stmt.execute();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
                j++;
            }

            try {

                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }


        }
    }

    public String addWebUserAction() {

        if (agentID.getValue() == null || agentID.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to first select an Agent!");

        } else {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:webUserPop" + "').show(hints);");
            webusername.setValue("");
            webfullNames.setValue("");
            webEmail.setValue("");
            webPersonalRank.setValue("");
            webAllowLogin.setValue("");
            webUserStatus.setValue("");
            webReset.setValue("");
            webPassword.setValue("");
            saveWebUsersButton.setText("Save");
        }

        return null;
    }

    public String updateWebUserAction() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webUserPop" + "').show(hints);");
        DCIteratorBinding binder =
            ADFUtils.findIterator("findwebUserAccountsIterator");
        RowKeySet set = webUsersTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            webUserId.setValue(rows.getAttribute("id"));
            webusername.setValue(rows.getAttribute("username"));
            webfullNames.setValue(rows.getAttribute("name"));
            webEmail.setValue(rows.getAttribute("email"));
            webPersonalRank.setValue(rows.getAttribute("personalrank"));
            webAllowLogin.setValue(rows.getAttribute("allowlogin"));
            webUserStatus.setValue(rows.getAttribute("status"));
            webReset.setValue(rows.getAttribute("reset"));
            webPassword.setValue("");
            saveWebUsersButton.setText("Update");
        }
        return null;
    }

    public String deleteWebUserAction() {
        DBConnector OracleConnection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_ACCOUNT_CONTACTS_PRC(?,?,?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String currentUser = (String)session.getAttribute("Username");
        DCIteratorBinding binder =
            ADFUtils.findIterator("findwebUserAccountsIterator");
        RowKeySet set = webUsersTable.getSelectedRowKeys();
        Iterator row = set.iterator();
        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();
            BigDecimal id = new BigDecimal((String)rows.getAttribute("id"));
            WebUser regbean = new WebUser();
            LinkedList reglist = new LinkedList();
            regbean.setACCC_CODE(id);
            reglist.add(regbean);
            try {
                conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "D");
                stmt.setArray(2, array);
                stmt.setString(3, currentUser);
                stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                stmt.setString(5, null);
                stmt.execute();

                stmt.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
        GlobalCC.refreshUI(webUsersTable);
        return null;
    }

    /**
     * @return
     */
    public String saveWebUsersOperation() {

        if (agentID.getValue() == null || agentID.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to first select an Agent!");

        } else {

            DBConnector OracleConnection = new DBConnector();
            String query =
                "begin TQC_AGENCIES_PKG.TQC_ACCOUNT_CONTACTS_PRC(?,?,?,?,?);end;";
            ARRAY array = null;
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            String currentUser = (String)session.getAttribute("Username");
            if (saveWebUsersButton.getText().equals("Save")) {
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(null);
                regbean.setACCC_AGN_CODE(new BigDecimal((String)agentID.getValue()));
                regbean.setACCC_USERNAME((String)webusername.getValue());
                regbean.setACCC_NAME((String)webfullNames.getValue());
                regbean.setACCC_EMAIL_ADDR((String)webEmail.getValue());
                regbean.setACCC_PERSONEL_RANK((String)webPersonalRank.getValue());
                regbean.setACCC_CREATED_BY(currentUser);
                regbean.setACCC_LOGIN_ALLOWED((String)webAllowLogin.getValue());
                regbean.setACCC_STATUS((String)webUserStatus.getValue());
                regbean.setACCC_PWD_RESET((String)webReset.getValue());
                regbean.setACCC_PWD((String)webPassword.getValue());
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)OracleConnection.getDatabaseConnection();
                    stmt =
(OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "A");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                    stmt.setString(5, null);
                    stmt.execute();
                    String error = stmt.getString(4);
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            } else if (saveWebUsersButton.getText().equals("Update")) {
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(new BigDecimal((String)webUserId.getValue()));
                regbean.setACCC_AGN_CODE(new BigDecimal((String)session.getAttribute("accagncode")));
                regbean.setACCC_USERNAME((String)webusername.getValue());
                regbean.setACCC_NAME((String)webfullNames.getValue());
                regbean.setACCC_EMAIL_ADDR((String)webEmail.getValue());
                regbean.setACCC_PERSONEL_RANK((String)webPersonalRank.getValue());
                regbean.setACCC_CREATED_BY(currentUser);
                regbean.setACCC_LOGIN_ALLOWED((String)webAllowLogin.getValue());
                regbean.setACCC_STATUS((String)webUserStatus.getValue());
                regbean.setACCC_PWD_RESET((String)webReset.getValue());
                regbean.setACCC_PWD((String)webPassword.getValue());
                reglist.add(regbean);
                try {
                    conn = OracleConnection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "E");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                    stmt.execute();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
            ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
            GlobalCC.refreshUI(webUsersTable);
        }

        return null;

    }

    public void setSaveWebUsersButton(RichCommandButton saveWebUsersButton) {
        this.saveWebUsersButton = saveWebUsersButton;
    }

    public RichCommandButton getSaveWebUsersButton() {
        return saveWebUsersButton;
    }

    public void setWebusername(RichInputText webusername) {
        this.webusername = webusername;
    }

    public RichInputText getWebusername() {
        return webusername;
    }

    public void setWebfullNames(RichInputText webfullNames) {
        this.webfullNames = webfullNames;
    }

    public RichInputText getWebfullNames() {
        return webfullNames;
    }

    public void setWebEmail(RichInputText webEmail) {
        this.webEmail = webEmail;
    }

    public RichInputText getWebEmail() {
        return webEmail;
    }

    public void setWebPersonalRank(RichInputText webPersonalRank) {
        this.webPersonalRank = webPersonalRank;
    }

    public RichInputText getWebPersonalRank() {
        return webPersonalRank;
    }

    public void setWebAllowLogin(RichSelectOneRadio webAllowLogin) {
        this.webAllowLogin = webAllowLogin;
    }

    public RichSelectOneRadio getWebAllowLogin() {
        return webAllowLogin;
    }

    public void setWebUserStatus(RichSelectOneChoice webUserStatus) {
        this.webUserStatus = webUserStatus;
    }

    public RichSelectOneChoice getWebUserStatus() {
        return webUserStatus;
    }

    public void setWebReset(RichSelectOneRadio webReset) {
        this.webReset = webReset;
    }

    public RichSelectOneRadio getWebReset() {
        return webReset;
    }

    public void setWebPassword(RichInputText webPassword) {
        this.webPassword = webPassword;
    }

    public RichInputText getWebPassword() {
        return webPassword;
    }

    public void setWebUsersTable(RichTable webUsersTable) {
        this.webUsersTable = webUsersTable;
    }

    public RichTable getWebUsersTable() {
        return webUsersTable;
    }

    public void setWebUserId(RichInputText webUserId) {
        this.webUserId = webUserId;
    }

    public RichInputText getWebUserId() {
        return webUserId;
    }

    public String actionAcceptAgencyClass() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("findAgencyClassesIterator");
        RowKeySet set = tblAgencyClasses.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            agClass.setValue(rows.getAttribute("id"));
            agClassFullDesc.setValue(rows.getAttribute("name"));
        }
        return null;
    }

    public void setTblAgencyClasses(RichTable tblAgencyClasses) {
        this.tblAgencyClasses = tblAgencyClasses;
    }

    public RichTable getTblAgencyClasses() {
        return tblAgencyClasses;
    }

    public void setAgClassFullDesc(RichInputText agClassFullDesc) {
        this.agClassFullDesc = agClassFullDesc;
    }

    public RichInputText getAgClassFullDesc() {
        return agClassFullDesc;
    }

    public void setHoldCompanyFullDesc(RichInputText holdCompanyFullDesc) {
        this.holdCompanyFullDesc = holdCompanyFullDesc;
    }

    public RichInputText getHoldCompanyFullDesc() {
        return holdCompanyFullDesc;
    }

    public void setTblAgencyHoldingCompany(RichTable tblAgencyHoldingCompany) {
        this.tblAgencyHoldingCompany = tblAgencyHoldingCompany;
    }

    public RichTable getTblAgencyHoldingCompany() {
        return tblAgencyHoldingCompany;
    }

    public String actionAcceptHoldingCompany() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchAllAgencyHoldingCompaniesIterator");
        RowKeySet set = tblAgencyHoldingCompany.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            holdCompany.setValue(rows.getAttribute("code"));
            holdCompanyFullDesc.setValue(rows.getAttribute("name"));
        }
        return null;
    }

    public void setTxtSectorFullDesc(RichInputText txtSectorFullDesc) {
        this.txtSectorFullDesc = txtSectorFullDesc;
    }

    public RichInputText getTxtSectorFullDesc() {
        return txtSectorFullDesc;
    }

    public void setTblSectors(RichTable tblSectors) {
        this.tblSectors = tblSectors;
    }

    public RichTable getTblSectors() {
        return tblSectors;
    }

    public String actionAcceptSector() {
        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchAllSectorsIterator");
        RowKeySet set = tblSectors.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            sector.setValue(rows.getAttribute("code"));
            txtSectorFullDesc.setValue(rows.getAttribute("name"));
        }
        return null;
    }

    public void setDetAccount(RichShowDetailItem detAccount) {
        this.detAccount = detAccount;
    }

    public RichShowDetailItem getDetAccount() {
        return detAccount;
    }

    public void setTextToSHow(RichOutputText textToSHow) {
        this.textToSHow = textToSHow;
    }

    public RichOutputText getTextToSHow() {
        return textToSHow;
    }

    public String processPrintAction() {
        textToSHow.setValue(null);
        textToSHow.setValue("Prime Value : Roll out");


        return null;
    }


    public void setTxtShortDescformat(RichInputText txtShortDescformat) {
        this.txtShortDescformat = txtShortDescformat;
    }

    public RichInputText getTxtShortDescformat() {
        return txtShortDescformat;
    }

    public void setTxtMgrNoInfix(RichInputText txtMgrNoInfix) {
        this.txtMgrNoInfix = txtMgrNoInfix;
    }

    public RichInputText getTxtMgrNoInfix() {
        return txtMgrNoInfix;
    }

    public String clientAccSelectedFrom() {

        Object key2 = tblAgentAccountFrom.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtFromAgentAccount.setValue(r.getAttribute("name"));
        GlobalCC.refreshUI(txtFromAgentAccount);
        session.setAttribute("agentFromAccountCode",
                             r.getAttribute("agentCode"));
        return null;

    }

    public String transferToAccount() {
        if (session.getAttribute("agentFromAccountCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Select From Account");
            return null;
        }
        if (session.getAttribute("agentToAccountCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Select To Account");
            return null;
        }
        String system = GlobalCC.checkNullValues(txtSystem.getValue());
        if (system == null) {
            GlobalCC.INFORMATIONREPORTING("Select System");
            return null;
        }
        if (system.equalsIgnoreCase("G")) {
            DBConnector connection = new DBConnector();
            Connection conn = null;
            CallableStatement stmt = null;
            try {
                String query =
                    "begin TQ_gis.GIS_UTILITIES.TRANSFER_ACCOUNT_RECS(?,?,?);end;";
                conn = connection.getDatabaseConnection();
                stmt = conn.prepareCall(query);
                stmt.setObject(1,
                               session.getAttribute("agentFromAccountCode"));
                stmt.setObject(2, session.getAttribute("agentToAccountCode"));
                stmt.setObject(3, accType.getValue());
                stmt.execute();
                GlobalCC.INFORMATIONREPORTING("Transfer Successful");
                session.removeAttribute("agentFromAccountCode");
                session.removeAttribute("agentToAccountCode");
                txtFromAgentAccount.setValue(null);
                txtToAgentAccount.setValue(null);
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        } else if (system.equalsIgnoreCase("L")) {
            DBConnector connection = new DBConnector();
            Connection conn = null;
            CallableStatement stmt = null;
            try {
                String query =
                    "begin TQ_lms.Lms_Web_Pkg_Und.Consolid_agents(?,?);end;";
                conn = connection.getDatabaseConnection();
                stmt = conn.prepareCall(query);
                stmt.setObject(1,
                               session.getAttribute("agentFromAccountCode"));
                stmt.setObject(2, session.getAttribute("agentToAccountCode"));

                stmt.execute();
                GlobalCC.INFORMATIONREPORTING("Transfer Successful");
                session.removeAttribute("agentFromAccountCode");
                session.removeAttribute("agentToAccountCode");
                txtFromAgentAccount.setValue(null);
                txtToAgentAccount.setValue(null);
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        } else if (system.equalsIgnoreCase("B")) {
            DBConnector connection = new DBConnector();
            Connection conn = null;
            CallableStatement stmt = null;
            try {
                String query =
                    "begin TQ_lms.Lms_Web_Pkg_Und.Consolid_agents(?,?);end;";
                conn = connection.getDatabaseConnection();
                stmt = conn.prepareCall(query);
                stmt.setObject(1,
                               session.getAttribute("agentFromAccountCode"));
                stmt.setObject(2, session.getAttribute("agentToAccountCode"));
                stmt.execute();

                query =
                        "begin TQ_gis.GIS_UTILITIES.TRANSFER_ACCOUNT_RECS(?,?,?);end;";
                stmt = conn.prepareCall(query);
                stmt.setObject(1,
                               session.getAttribute("agentFromAccountCode"));
                stmt.setObject(2, session.getAttribute("agentToAccountCode"));
                stmt.setObject(3, accType.getValue());
                stmt.execute();
                GlobalCC.INFORMATIONREPORTING("Transfer Successful");
                session.removeAttribute("agentFromAccountCode");
                session.removeAttribute("agentToAccountCode");
                txtFromAgentAccount.setValue(null);
                txtToAgentAccount.setValue(null);
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
        return null;
    }

    public String clientAccSelectedTo() {

        Object key2 = tblAgentAccountTo.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        txtToAgentAccount.setValue(r.getAttribute("name"));
        GlobalCC.refreshUI(txtToAgentAccount);
        session.setAttribute("agentToAccountCode",
                             r.getAttribute("agentCode"));

        return null;
    }

    public void setTblAgentAccountFrom(RichTable tblAgentAccountFrom) {
        this.tblAgentAccountFrom = tblAgentAccountFrom;
    }

    public RichTable getTblAgentAccountFrom() {
        return tblAgentAccountFrom;
    }

    public void setTblAgentAccountTo(RichTable tblAgentAccountTo) {
        this.tblAgentAccountTo = tblAgentAccountTo;
    }

    public RichTable getTblAgentAccountTo() {
        return tblAgentAccountTo;
    }

    public void setTxtFromAgentAccount(RichInputText txtFromAgentAccount) {
        this.txtFromAgentAccount = txtFromAgentAccount;
    }

    public RichInputText getTxtFromAgentAccount() {
        return txtFromAgentAccount;
    }

    public void setTxtToAgentAccount(RichInputText txtToAgentAccount) {
        this.txtToAgentAccount = txtToAgentAccount;
    }

    public RichInputText getTxtToAgentAccount() {
        return txtToAgentAccount;
    }

    public void setAccType(RichSelectOneRadio accType) {
        this.accType = accType;
    }

    public RichSelectOneRadio getAccType() {
        return accType;
    }

    public void setTxtSystem(RichSelectOneRadio txtSystem) {
        this.txtSystem = txtSystem;
    }

    public RichSelectOneRadio getTxtSystem() {
        return txtSystem;
    }

    public void setTxtCommissionLevyRate(RichInputNumberSpinbox txtCommissionLevyRate) {
        this.txtCommissionLevyRate = txtCommissionLevyRate;
    }

    public RichInputNumberSpinbox getTxtCommissionLevyRate() {
        return txtCommissionLevyRate;
    }
    
 
 
    public void setTxtSearchBy(RichSelectOneChoice txtSearchBy) {
        this.txtSearchBy = txtSearchBy;
    }

    public RichSelectOneChoice getTxtSearchBy() {
        return txtSearchBy;
    }

   
    public void setTxtEntity(RichInputText txtEntity) {
        this.txtEntity = txtEntity;
    }

    public RichInputText getTxtEntity() {
        return txtEntity;
    }

    public void setTxtShrtDesc(RichInputText txtShrtDesc) {
        this.txtShrtDesc = txtShrtDesc;
    }

    public RichInputText getTxtShrtDesc() {
        return txtShrtDesc;
    }

    public String actionSearchEntity() {
        
        if(GlobalCC.checkNullValues(txtSearchBy.getValue()).equalsIgnoreCase("P")){
             session.setAttribute("entityPin",GlobalCC.checkNullValues(txtEntity.getValue()));
             session.setAttribute("entityName",null);
        }
       
        else{
                session.setAttribute("entityPin",null);
                session.setAttribute("entityName",GlobalCC.checkNullValues(txtEntity.getValue()));
            
            }
        
         session.setAttribute("entityShortDesc",GlobalCC.checkNullValues(txtShrtDesc.getValue()));
         session.setAttribute("entityCode",null);
        
      
        
        
      
        ADFUtils.findIterator("fetchEntitiesIterator").executeQuery();
        GlobalCC.refreshUI(tblEntities);
        ADFUtils.findIterator("fetchEntitiesRelationsIterator").executeQuery();
        GlobalCC.refreshUI(tblEntityRelations);
        
        
        return null;
    }

   

    public void actionSelectTblEntitys(SelectionEvent selectionEvent) {
        
        Object key=tblEntities.getSelectedRowData();
        JUCtrlValueBinding node = (JUCtrlValueBinding)key;
        
        if(node==null){
            return;
            }
        
        session.setAttribute("entityCode", node.getAttribute("entCode"));
        ADFUtils.findIterator("fetchEntitiesRelationsIterator").executeQuery();
        GlobalCC.refreshUI(tblEntityRelations);
    }
    public String actionCreateNewEntity() {
        
        
       Object key=tblEntities.getSelectedRowData();
       JUCtrlValueBinding node = (JUCtrlValueBinding)key;
        Links ln=new Links();
        ServletContext servletContext =(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String returnTo="showEntities.jspx";
       if(GlobalCC.checkNullValues(node.getAttribute("newRelation")).equalsIgnoreCase("C")){
           
             
                
             ln.showClients();
              
               String back_url =
                request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() +
                servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                session.setAttribute("back_url",back_url);
                System.out.println("back_url: "+back_url);
           return null;
           }
        else if(GlobalCC.checkNullValues(node.getAttribute("newRelation")).equalsIgnoreCase("I")){
            
              ln.showAccountDefinitions(); 
               
                String back_url =
                 request.getScheme() + "://" + request.getServerName() +
                 ":" + request.getServerPort() +
                 servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                 session.setAttribute("back_url",back_url);
                 System.out.println("back_url: "+back_url);
                return null;
            }
         
        else if(GlobalCC.checkNullValues(node.getAttribute("newRelation")).equalsIgnoreCase("S"))
            {
            
                ln.showServiceProviders();
                String back_url =
                    
               
                 request.getScheme() + "://" + request.getServerName() +
                 ":" + request.getServerPort() +
                 servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                 session.setAttribute("back_url",back_url);
                 System.out.println("back_url: "+back_url);
                return null;
            }
         
        return null;
      
    }
    public String actionCreateNewNonRelatedEntity() {
        
        
        String value=GlobalCC.checkNullValues(txtEntityType.getValue());
       Links ln=new Links();
        ServletContext servletContext =(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String returnTo="showEntities.jspx";
       if(value.equalsIgnoreCase("C")){
           
             
                
             ln.showClients();
              
               String back_url =
                request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() +
                servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                session.setAttribute("back_url",back_url);
                System.out.println("back_url: "+back_url);
           return null;
           }
        else if(value.equalsIgnoreCase("I")){
            
              ln.showAccountDefinitions(); 
               
                String back_url =
                 request.getScheme() + "://" + request.getServerName() +
                 ":" + request.getServerPort() +
                 servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                 session.setAttribute("back_url",back_url);
                 System.out.println("back_url: "+back_url);
                return null;
            }
         
        else if(value.equalsIgnoreCase("S"))
            {
            
                ln.showServiceProviders();
                String back_url =
                    
               
                 request.getScheme() + "://" + request.getServerName() +
                 ":" + request.getServerPort() +
                 servletContext.getContextPath()+"/faces/"+returnTo+"?aid="+session.getAttribute("UserCode");
                 session.setAttribute("back_url",back_url);
                 System.out.println("back_url: "+back_url);
                return null;
            }
         
        return null;
      
    }
    public void setTblEntities(RichTable tblEntities) {
        this.tblEntities = tblEntities;
    }

    public RichTable getTblEntities() {
        return tblEntities;
    }

    public void setTblEntityRelations(RichTable tblEntityRelations) {
        this.tblEntityRelations = tblEntityRelations;
    }

    public RichTable getTblEntityRelations() {
        return tblEntityRelations;
    }

    public void setTxtEntityType(RichSelectOneChoice txtEntityType) {
        this.txtEntityType = txtEntityType;
    }

    public RichSelectOneChoice getTxtEntityType() {
        return txtEntityType;
    }
}

